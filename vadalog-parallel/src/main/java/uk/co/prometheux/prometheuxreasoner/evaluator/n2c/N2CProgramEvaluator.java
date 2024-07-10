package uk.co.prometheux.prometheuxreasoner.evaluator.n2c;

import java.io.File;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.spark.broadcast.Broadcast;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoder;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SaveMode;
import org.apache.spark.sql.catalyst.encoders.RowEncoder;
import org.apache.spark.sql.types.DataTypes;
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

public class N2CProgramEvaluator extends ProgramEvaluator {

	private String edgeInputPath;
	private String n2cOutputPath;
	private Broadcast<Object2ObjectOpenHashMap<Integer, ObjectOpenHashBigSet<Row>>> bVar;

	public N2CProgramEvaluator(List<BindAnnotation> inputAnnotations, List<BindAnnotation> outputAnnotations) {
		BindAnnotation edgeInputArc = inputAnnotations.get(0);
		BindAnnotation outputTc = outputAnnotations.get(0);
		edgeInputPath = edgeInputArc.getSchema() + File.separator + edgeInputArc.getTableOrQuery();
		n2cOutputPath = outputTc.getSchema() + File.separator + outputTc.getTableOrQuery();
	}

	@Override
	public void evaluate() {

		System.out.println("N2C, reading input file " + new File(edgeInputPath).getAbsolutePath());

		Dataset<Row> edge = spark.read().format("csv").option("inferSchema", "true").load(edgeInputPath);

		Iterator<Row> it = edge.toLocalIterator();
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
		bVar = spark.sparkContext()
				.broadcast(map, tag);

		Dataset<Row> n2c = edge.alias("n2c").withColumnRenamed(edge.columns()[0], "n2c_0")
				.withColumnRenamed(edge.columns()[1], "n2c_1");

		n2c = this.withFlatMapGroup(n2c);
		n2c.write().mode(SaveMode.Overwrite).csv(n2cOutputPath);
		System.out.println("N2C, written output file " + new File(n2cOutputPath).getAbsolutePath());

		// outside the recursion, we query the even paths with same source and target
		// (we do not have bipartition)
//		N2C = N2C.where(new Column("n2c_2").equalTo("EVEN").and(new Column("n2c_0").equalTo(new Column("n2c_1"))));

	}

	private Dataset<Row> withFlatMapGroup(Dataset<Row> n2c) {
		List<Integer> keyPositions = Arrays.asList(0);
		Encoder<Row> keyEncoder = EncoderGenerator.createEncoderFromEncoder(n2c.encoder(), keyPositions);

		StructType N2CStructType = new StructType();
		N2CStructType = N2CStructType.add("n2c_0", DataTypes.IntegerType, false);
		N2CStructType = N2CStructType.add("n2c_1", DataTypes.IntegerType, false);
		N2CStructType = N2CStructType.add("n2c_2", DataTypes.StringType, false);
		Encoder<Row> N2CEncoder = RowEncoder.apply(N2CStructType);

		KeyGroupMapFunction keys = new KeyGroupMapFunction(Arrays.asList(0));
		N2CFlatMapGroup N2CFlatMapGroup = new N2CFlatMapGroup(bVar);
		n2c = n2c.groupByKey(keys, keyEncoder).flatMapGroups(N2CFlatMapGroup, N2CEncoder);
		return n2c;
	}
	
	@Override
	public void cleanUp() {
		if (bVar != null) {
			bVar.destroy();
		}
		super.cleanUp();
	}

}
