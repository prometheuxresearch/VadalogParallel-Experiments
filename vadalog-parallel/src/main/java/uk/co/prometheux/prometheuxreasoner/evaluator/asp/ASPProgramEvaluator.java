package uk.co.prometheux.prometheuxreasoner.evaluator.asp;

import java.io.File;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.spark.broadcast.Broadcast;
import org.apache.spark.sql.Column;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoder;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SaveMode;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashBigSet;
import scala.reflect.ClassTag;
import uk.co.prometheux.prometheuxreasoner.common.EncoderGenerator;
import uk.co.prometheux.prometheuxreasoner.evaluator.ProgramEvaluator;
import uk.co.prometheux.prometheuxreasoner.function.KeyGroupMapFunction;
import uk.co.prometheux.prometheuxreasoner.model.annotations.BindAnnotation;

/**
 * 
 * Copyright (C) Prometheux Limited. All rights reserved.
 * 
 * @author Prometheux Limited
 */

public class ASPProgramEvaluator extends ProgramEvaluator {

	private String arcInputPath;
	private String ASPOutputPath;
	private Broadcast<Object2ObjectOpenHashMap<Object, ObjectOpenHashBigSet<Row>>> bVar;

	public ASPProgramEvaluator(List<BindAnnotation> inputAnnotations, List<BindAnnotation> outputAnnotations) {
		BindAnnotation inputArc = inputAnnotations.get(0);
		BindAnnotation outputASP = outputAnnotations.get(0);
		arcInputPath = inputArc.getSchema() + File.separator + inputArc.getTableOrQuery();
		ASPOutputPath = outputASP.getSchema() + File.separator + outputASP.getTableOrQuery();
	}

	@Override
	public void evaluate() {
		System.out.println("ASP, reading input file " + new File(arcInputPath).getAbsolutePath());
		Dataset<Row> input_arc = spark.read().format("csv").option("header", "true").option("inferSchema", "true").load(arcInputPath);
		StructType schema = input_arc.schema();
		StructField thirdColumn = schema.fields()[2];
		Dataset<Row> arc;
		if (thirdColumn.dataType().equals(DataTypes.IntegerType)) {
			arc = input_arc.withColumn(input_arc.columns()[2],
					new Column(input_arc.columns()[2]).cast(DataTypes.DoubleType));
		} else {
			arc = input_arc;
		}

		Iterator<Row> it = arc.toLocalIterator();
		Object2ObjectOpenHashMap<Object, ObjectOpenHashBigSet<Row>> map = new Object2ObjectOpenHashMap<>();
		while (it.hasNext()) {
			Row row = it.next();
			if (!map.containsKey(row.getInt(0))) {
				map.put(row.getInt(0), new ObjectOpenHashBigSet<Row>());
			}
			map.get(row.getInt(0)).add(row);
		}

		ClassTag<Object2ObjectOpenHashMap<Object, ObjectOpenHashBigSet<Row>>> tag = scala.reflect.ClassTag$.MODULE$
				.apply(map.getClass());
		bVar = spark.sparkContext().broadcast(map, tag);

		arc = arc.alias("arc").withColumnRenamed(arc.columns()[0], "arc_0").withColumnRenamed(arc.columns()[1], "arc_1")
				.withColumnRenamed(arc.columns()[2], "arc_2");

		Dataset<Row> asp = this.withFlatMapGroup(arc);
		asp.write().mode(SaveMode.Overwrite).csv(ASPOutputPath);
		System.out.println("ASP, written output file " + new File(ASPOutputPath).getAbsolutePath());
	}

	private Dataset<Row> withFlatMapGroup(Dataset<Row> arc) {
		List<Integer> keyPositions = Arrays.asList(0);
		Encoder<Row> keyEncoder = EncoderGenerator.createEncoderFromEncoder(arc.encoder(), keyPositions);
		KeyGroupMapFunction keys = new KeyGroupMapFunction(Arrays.asList(0));
		ASPFlatMapGroup ASPFlatMapGroup = new ASPFlatMapGroup(bVar);
		Dataset<Row> asp = arc.groupByKey(keys, keyEncoder).flatMapGroups(ASPFlatMapGroup, arc.encoder());
		return asp;
	}

	@Override
	public void cleanUp() {
		if (bVar != null) {
			bVar.destroy();
		}
		super.cleanUp();
	}

}
