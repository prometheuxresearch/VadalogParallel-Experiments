package uk.co.prometheux.prometheuxreasoner.evaluator.transitiveClosure;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.spark.broadcast.Broadcast;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoder;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SaveMode;
import org.apache.spark.sql.types.DataType;
import org.apache.spark.sql.types.DataTypes;

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

public class TransitiveClosureProgramEvaluator extends ProgramEvaluator {

	private String arcInputPath;
	private String tcOutputPath;
	private Broadcast<Object2ObjectOpenHashMap<Object, ObjectOpenHashBigSet<Row>>> bVar;


	public TransitiveClosureProgramEvaluator(List<BindAnnotation> inputAnnotations, List<BindAnnotation> outputAnnotations) {
		BindAnnotation inputArc = inputAnnotations.get(0);
		BindAnnotation outputTc = outputAnnotations.get(0);
		arcInputPath = inputArc.getSchema() + File.separator + inputArc.getTableOrQuery();
		tcOutputPath = outputTc.getSchema() + File.separator + outputTc.getTableOrQuery();
	}

	@Override
	public void evaluate() {

		System.out.println("TRANSITIVE CLOSURE, reading input file "+new File(arcInputPath).getAbsolutePath());
		Dataset<Row> arc = spark.read().format("csv").option("header", "true").option("inferSchema", "true").load(arcInputPath);
		Iterator<Row> it = arc.toLocalIterator();
		Object2ObjectOpenHashMap<Object, ObjectOpenHashBigSet<Row>> map = new Object2ObjectOpenHashMap<>();
		while (it.hasNext()) {
			Row row = it.next();
			if (!map.containsKey(row.get(0))) {
				map.put(row.get(0), new ObjectOpenHashBigSet<Row>());
			}
			map.get(row.get(0)).add(row);
		}

		ClassTag<Object2ObjectOpenHashMap<Object, ObjectOpenHashBigSet<Row>>> tag = scala.reflect.ClassTag$.MODULE$
				.apply(map.getClass());
		bVar = spark.sparkContext().broadcast(map, tag);

		Dataset<Row> tc = this.withFlatMapGroup(arc);
		
		tc.write().mode(SaveMode.Overwrite).csv(tcOutputPath);
		System.out.println("TRANSITIVE CLOSURE, written output file "+new File(tcOutputPath).getAbsolutePath());
	}

	private Dataset<Row> withFlatMapGroup(Dataset<Row> arc) {
		List<Integer> keyPositions = Arrays.asList(0);
		Encoder<Row> keyEncoder = EncoderGenerator.createEncoderFromEncoder(arc.encoder(), keyPositions);
		KeyGroupMapFunction keys = new KeyGroupMapFunction(Arrays.asList(0));
		
		List<DataType> datatypes = new ArrayList<DataType>();
		datatypes.add(DataTypes.IntegerType);
		datatypes.add(DataTypes.IntegerType);
		Encoder<Row> tcEncoder = EncoderGenerator.generateDefaultEncoder("tc", 2, datatypes);
		TransitiveClosureFlatMapGroup tcFlatMapGroup = new TransitiveClosureFlatMapGroup(bVar);
		Dataset<Row> tc = arc.groupByKey(keys, keyEncoder).flatMapGroups(tcFlatMapGroup, tcEncoder);
		return tc;
	}

	@Override
	public void cleanUp() {
		if (bVar != null) {
			bVar.destroy();
		}
		super.cleanUp();
	}


}
