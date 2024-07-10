package uk.co.prometheux.prometheuxreasoner.evaluator.companycontrol;

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
 * @author Prometheux Limited
 */

public class CompanyControlProgramEvaluator extends ProgramEvaluator {

	private String ownInputPath;
	private String companyControlOutputPath;
	private Broadcast<Object2ObjectOpenHashMap<Integer, ObjectOpenHashBigSet<Row>>> bVar;

	public CompanyControlProgramEvaluator(List<BindAnnotation> inputAnnotations, List<BindAnnotation> outputAnnotations) {
		BindAnnotation inputOwn = inputAnnotations.get(0);
		BindAnnotation outputCompanyControl = outputAnnotations.get(0);
		ownInputPath = inputOwn.getSchema() + File.separator + inputOwn.getTableOrQuery();
		companyControlOutputPath = outputCompanyControl.getSchema() + File.separator + outputCompanyControl.getTableOrQuery();
	}

	@Override
	public void evaluate() {
		System.out.println("COMPANY CONTROL, reading input file " + new File(ownInputPath).getAbsolutePath());

		Dataset<Row> input_own = spark.read().format("csv").option("header", "true").option("inferSchema", "true")
				.load(ownInputPath);
		StructType schema = input_own.schema();
		StructField thirdColumn = schema.fields()[2];
		Dataset<Row> own;
		if (thirdColumn.dataType().equals(DataTypes.IntegerType)) {
			own = input_own.withColumn(input_own.columns()[2],
					new Column(input_own.columns()[2]).cast(DataTypes.DoubleType));
		} else {
			own = input_own;
		}

		Iterator<Row> it = own.toLocalIterator();
		Object2ObjectOpenHashMap<Integer, ObjectOpenHashBigSet<Row>> map = new Object2ObjectOpenHashMap<>();
		while (it.hasNext()) {
			Row row = it.next();
			if (!map.containsKey(row.getInt(0))) {
				map.put(row.getInt(0), new ObjectOpenHashBigSet<Row>());
			}
			map.get(row.getInt(0)).add(row);
		}

		ClassTag<Object2ObjectOpenHashMap<Integer, ObjectOpenHashBigSet<Row>>> tag = scala.reflect.ClassTag$.MODULE$
				.apply(map.getClass());
		bVar = spark.sparkContext().broadcast(map, tag);

		own = own.alias("own").withColumnRenamed(own.columns()[0], "own_0").withColumnRenamed(own.columns()[1], "own_1")
				.withColumnRenamed(own.columns()[2], "own_2");

		Dataset<Row> companyControl = this.withFlatMapGroup(own);
		companyControl.write().mode(SaveMode.Overwrite).csv(companyControlOutputPath);
		System.out.println("COMPANY CONTROL, written output file " + new File(companyControlOutputPath).getAbsolutePath());
	}

	private Dataset<Row> withFlatMapGroup(Dataset<Row> own) {
		List<Integer> keyPositions = Arrays.asList(0);
		Encoder<Row> keyEncoder = EncoderGenerator.createEncoderFromEncoder(own.encoder(), keyPositions);
		Encoder<Row> companyControlEncoder = EncoderGenerator.createEncoderFromEncoder(own.encoder(), Arrays.asList(0,1));
		KeyGroupMapFunction keys = new KeyGroupMapFunction(Arrays.asList(0));
		CompanyControlFlatMapGroup companyControlFlatMapGroup = new CompanyControlFlatMapGroup(bVar);
		Dataset<Row> companyControl = own.groupByKey(keys, keyEncoder).flatMapGroups(companyControlFlatMapGroup, companyControlEncoder);
		return companyControl;
	}

	@Override
	public void cleanUp() {
		if (bVar != null) {
			bVar.destroy();
		}
		super.cleanUp();
	}

}
