package uk.co.prometheux.prometheuxreasoner.evaluator.defaultpropagation;

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
 * 
 * @author Prometheux Limited
 */

public class DefaultPropagationProgramEvaluator extends ProgramEvaluator {

	private String exposureInputPath;
	private String loanInputPath;
	private String securityInputPath;
	private String defaultPropagationOutputPath;
	private Broadcast<Object2ObjectOpenHashMap<Integer, ObjectOpenHashBigSet<Row>>> bLoan;
	private Broadcast<Object2ObjectOpenHashMap<Integer, ObjectOpenHashBigSet<Row>>> bSecurity;

	public DefaultPropagationProgramEvaluator(List<BindAnnotation> inputAnnotations,
			List<BindAnnotation> outputAnnotations) {
		BindAnnotation exposure = null;
		BindAnnotation loan = null;
		BindAnnotation security = null;

		for (BindAnnotation bind : inputAnnotations) {
			if (bind.getPredicateName().equals("finEntity")) {
				exposure = bind;
			}
			if (bind.getPredicateName().equals("loan")) {
				loan = bind;
			}
			if (bind.getPredicateName().equals("security")) {
				security = bind;
			}
		}
		exposureInputPath = exposure.getSchema() + File.separator + exposure.getTableOrQuery();
		loanInputPath = loan.getSchema() + File.separator + loan.getTableOrQuery();
		securityInputPath = security.getSchema() + File.separator + security.getTableOrQuery();

		BindAnnotation outputDefaultPropagation = outputAnnotations.get(0);

		defaultPropagationOutputPath = outputDefaultPropagation.getSchema() + File.separator
				+ outputDefaultPropagation.getTableOrQuery();
	}

	@Override
	public void evaluate() {
		System.out.println("DEFAULT PROPAGATION, reading input file " + new File(exposureInputPath).getAbsolutePath());

		Dataset<Row> creditExposure = spark.read().option("header", "true").format("csv").option("inferSchema", "true")
				.load(this.exposureInputPath);

		System.out.println("DEFAULT PROPAGATION, reading input file " + new File(loanInputPath).getAbsolutePath());

		Dataset<Row> loan = spark.read().format("csv").option("header", "true").option("inferSchema", "true")
				.load(this.loanInputPath);

		System.out.println("DEFAULT PROPAGATION, reading input file " + new File(securityInputPath).getAbsolutePath());

		Dataset<Row> security = spark.read().format("csv").option("header", "true").option("inferSchema", "true")
				.load(this.securityInputPath);

		Iterator<Row> it = loan.toLocalIterator();
		Object2ObjectOpenHashMap<Integer, ObjectOpenHashBigSet<Row>> loanIndex = buildIndex(it);
		it = security.toLocalIterator();
		Object2ObjectOpenHashMap<Integer, ObjectOpenHashBigSet<Row>> securityIndex = buildIndex(it);

		ClassTag<Object2ObjectOpenHashMap<Integer, ObjectOpenHashBigSet<Row>>> tag = scala.reflect.ClassTag$.MODULE$
				.apply(loanIndex.getClass());
		bLoan = spark.sparkContext().broadcast(loanIndex, tag);

		tag = scala.reflect.ClassTag$.MODULE$.apply(securityIndex.getClass());
		bSecurity = spark.sparkContext().broadcast(securityIndex, tag);

		creditExposure = creditExposure.alias("creditExposure")
				.withColumnRenamed(creditExposure.columns()[0], "creditExposure_0")
				.withColumnRenamed(creditExposure.columns()[1], "creditExposure_1");

		Dataset<Row> defaults = this.withFlatMapGroup(creditExposure);
		defaults.write().mode(SaveMode.Overwrite).csv(defaultPropagationOutputPath);
		System.out.println(
				"DEFAULT PROPAGATION, written output file " + new File(defaultPropagationOutputPath).getAbsolutePath());

	}

	private Dataset<Row> withFlatMapGroup(Dataset<Row> creditExposure) {
		List<Integer> keyPositions = Arrays.asList(0);
		Encoder<Row> keyEncoder = EncoderGenerator.createEncoderFromEncoder(creditExposure.encoder(), keyPositions);
		KeyGroupMapFunction keys = new KeyGroupMapFunction(Arrays.asList(0));
		DefaultPropagationFlatMapGroup dp = new DefaultPropagationFlatMapGroup(bLoan, bSecurity);

		List<DataType> dataTypes = new ArrayList<>();
		dataTypes.add(DataTypes.IntegerType);
		dataTypes.add(DataTypes.IntegerType);
		dataTypes.add(DataTypes.StringType);
		Encoder<Row> defaultPropagationEncoder = EncoderGenerator.generateDefaultEncoder("defaultPropagation", 3,
				dataTypes);
		Dataset<Row> defaults = creditExposure.groupByKey(keys, keyEncoder).flatMapGroups(dp,
				defaultPropagationEncoder);
		return defaults;
	}

	private Object2ObjectOpenHashMap<Integer, ObjectOpenHashBigSet<Row>> buildIndex(Iterator<Row> it) {
		Object2ObjectOpenHashMap<Integer, ObjectOpenHashBigSet<Row>> map = new Object2ObjectOpenHashMap<>();
		while (it.hasNext()) {
			Row row = it.next();
			if (!map.containsKey(row.getInt(0))) {
				map.put(row.getInt(0), new ObjectOpenHashBigSet<Row>());
			}
			map.get(row.getInt(0)).add(row);
		}
		return map;
	}

	@Override
	public void cleanUp() {
		if (bLoan != null) {
			bLoan.destroy();
		}
		if (bSecurity != null) {
			bSecurity.destroy();
		}
		super.cleanUp();
	}

}
