package uk.co.prometheux.prometheuxreasoner.evaluator.samegeneration;

import java.io.File;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.spark.broadcast.Broadcast;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoder;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SaveMode;

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

public class SameGenerationProgramEvaluator extends ProgramEvaluator {

	private String arcInputPath;
	private String sgOutputPath;
	private Broadcast<Object2ObjectOpenHashMap<Integer, ObjectOpenHashBigSet<Row>>> bVar;

	public SameGenerationProgramEvaluator(List<BindAnnotation> inputAnnotations,
			List<BindAnnotation> outputAnnotations) {
		BindAnnotation inputArc = inputAnnotations.get(0);
		BindAnnotation outputSg = outputAnnotations.get(0);
		arcInputPath = inputArc.getSchema() + File.separator + inputArc.getTableOrQuery();
		sgOutputPath = outputSg.getSchema() + File.separator + outputSg.getTableOrQuery();
	}

	@Override
	public void evaluate() {

		System.out.println("SAME GENERATION, reading input file " + new File(arcInputPath).getAbsolutePath());
		Dataset<Row> arc = spark.read().format("csv").option("header", "true").option("inferSchema", "true")
				.load(arcInputPath);
		Iterator<Row> it = arc.toLocalIterator();
		Object2ObjectOpenHashMap<Integer, ObjectOpenHashBigSet<Row>> map = new Object2ObjectOpenHashMap<>();
		while (it.hasNext()) {
			Row row = it.next();
			if (!map.containsKey(row.get(0))) {
				map.put(row.getInt(0), new ObjectOpenHashBigSet<Row>());
			}
			map.get(row.getInt(0)).add(row);
		}

		ClassTag<Object2ObjectOpenHashMap<Integer, ObjectOpenHashBigSet<Row>>> tag = scala.reflect.ClassTag$.MODULE$
				.apply(map.getClass());
		bVar = spark.sparkContext().broadcast(map, tag);

		Dataset<Row> sameGeneration = arc.alias("sameGeneration")
				.withColumnRenamed(arc.columns()[0], "sameGeneration_0")
				.withColumnRenamed(arc.columns()[1], "sameGeneration_1");

		sameGeneration = this.withFlatMapGroup(sameGeneration).distinct();

		sameGeneration.write().mode(SaveMode.Overwrite).csv(sgOutputPath);
		System.out.println("SAME GENERATION, written output file " + new File(sgOutputPath).getAbsolutePath());
	}

	private Dataset<Row> withFlatMapGroup(Dataset<Row> arc) {
		KeyGroupMapFunction keys = new KeyGroupMapFunction(Arrays.asList(1));
		List<Integer> keyPositions = Arrays.asList(1);
		Encoder<Row> keyEncoder = EncoderGenerator.createEncoderFromEncoder(arc.encoder(), keyPositions);
		SameGenerationFlatMapGroup sameGeneration = new SameGenerationFlatMapGroup(bVar);
		Dataset<Row> sg = arc.groupByKey(keys, keyEncoder).flatMapGroups(sameGeneration, arc.encoder());
		return sg;
	}

	@Override
	public void cleanUp() {
		if (bVar != null) {
			bVar.destroy();
		}
		super.cleanUp();
	}

}
