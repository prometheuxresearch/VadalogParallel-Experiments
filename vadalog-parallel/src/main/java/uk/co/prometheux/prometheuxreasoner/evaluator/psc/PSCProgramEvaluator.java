package uk.co.prometheux.prometheuxreasoner.evaluator.psc;

import java.io.File;
import java.util.ArrayList;
//import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.spark.api.java.function.MapFunction;
import org.apache.spark.broadcast.Broadcast;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoder;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.KeyValueGroupedDataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SaveMode;
import org.apache.spark.sql.types.DataType;
import org.apache.spark.sql.types.DataTypes;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashBigSet;
import scala.reflect.ClassTag;
import uk.co.prometheux.prometheuxreasoner.common.EncoderGenerator;
import uk.co.prometheux.prometheuxreasoner.evaluator.ProgramEvaluator;
//import uk.co.prometheux.prometheuxreasoner.function.KeyGroupMapFunction;
import uk.co.prometheux.prometheuxreasoner.model.annotations.BindAnnotation;

/**
 * 
 * Copyright (C) Prometheux Limited. All rights reserved.
 * 
 * @author Prometheux Limited
 */

public class PSCProgramEvaluator extends ProgramEvaluator {

	private String keyPersonInputPath;
	private String personInputPath;
	private String companyInputPath;
	private String controlInputPath;
	private String pscOutputPath;
	private Broadcast<Object2ObjectOpenHashMap<String, ObjectOpenHashBigSet<Row>>> bPerson;
	private Broadcast<Object2ObjectOpenHashMap<String, ObjectOpenHashBigSet<Row>>> bControl;

	public PSCProgramEvaluator(List<BindAnnotation> inputAnnotations, List<BindAnnotation> outputAnnotations) {
		BindAnnotation keyPerson = null;
		BindAnnotation person = null;
		BindAnnotation company = null;
		BindAnnotation control = null;

		for (BindAnnotation bind : inputAnnotations) {
			if (bind.getPredicateName().equals("keyPerson")) {
				keyPerson = bind;
			}
			if (bind.getPredicateName().equals("person")) {
				person = bind;
			}
			if (bind.getPredicateName().equals("company")) {
				company = bind;
			}
			if (bind.getPredicateName().equals("control")) {
				control = bind;
			}
		}
		keyPersonInputPath = keyPerson.getSchema() + File.separator + keyPerson.getTableOrQuery();
		personInputPath = person.getSchema() + File.separator + person.getTableOrQuery();
		companyInputPath = company.getSchema() + File.separator + company.getTableOrQuery();
		controlInputPath = control.getSchema() + File.separator + control.getTableOrQuery();

		BindAnnotation outputPsc = outputAnnotations.get(0);

		pscOutputPath = outputPsc.getSchema() + File.separator + outputPsc.getTableOrQuery();
	}

	@Override
	public void evaluate() {
		System.out.println("PSC, reading input file " + new File(keyPersonInputPath).getAbsolutePath());
		Dataset<Row> keyPerson = spark.read().format("csv").option("header", "true").option("inferSchema", "true")
				.load(this.keyPersonInputPath);

		System.out.println("PSC, reading input file " + new File(personInputPath).getAbsolutePath());
		Dataset<Row> person = spark.read().format("csv").option("header", "true").option("inferSchema", "true")
				.load(this.personInputPath);

		System.out.println("PSC, reading input file " + new File(companyInputPath).getAbsolutePath());
		Dataset<Row> company = spark.read().format("csv").option("header", "true").option("inferSchema", "true")
				.load(this.companyInputPath);

		System.out.println("PSC, reading input file " + new File(controlInputPath).getAbsolutePath());
		Dataset<Row> control = spark.read().format("csv").option("header", "true").option("inferSchema", "true")
				.load(this.controlInputPath);

		Iterator<Row> it = control.toLocalIterator();
		Object2ObjectOpenHashMap<String, ObjectOpenHashBigSet<Row>> controlIndex = buildIndex(it);

		ClassTag<Object2ObjectOpenHashMap<String, ObjectOpenHashBigSet<Row>>> tag = scala.reflect.ClassTag$.MODULE$
				.apply(controlIndex.getClass());
		bControl = spark.sparkContext().broadcast(controlIndex, tag);

		it = person.toLocalIterator();
		Object2ObjectOpenHashMap<String, ObjectOpenHashBigSet<Row>> personIndex = buildIndex(it);

		tag = scala.reflect.ClassTag$.MODULE$.apply(personIndex.getClass());
		bPerson = spark.sparkContext().broadcast(personIndex, tag);

		Dataset<Row> psc = this.withFlatMapGroup(keyPerson, company);
		psc.write().mode(SaveMode.Overwrite).csv(pscOutputPath);

		System.out.println("PSC, written output file " + new File(pscOutputPath).getAbsolutePath());

	}

	private Dataset<Row> withFlatMapGroup(Dataset<Row> keyPerson, Dataset<Row> company) {
        KeyValueGroupedDataset<String, Row> groupedKP = keyPerson.groupByKey(
            (MapFunction<Row, String>) value -> value.getString(0), Encoders.STRING());
        KeyValueGroupedDataset<String, Row> groupedCompany = company.groupByKey(
            (MapFunction<Row, String>) value -> value.getString(0), Encoders.STRING());

//        List<Integer> keyPositions = Arrays.asList(0);
//		KeyGroupMapFunction keysKeyPerson = new KeyGroupMapFunction(keyPositions);
//		Encoder<Row> keyPersonEncoder = EncoderGenerator.createEncoderFromEncoder(keyPerson.encoder(), keyPositions);
//		KeyValueGroupedDataset<Row, Row> groupedKP = keyPerson.groupByKey(keysKeyPerson, keyPersonEncoder);

//		KeyGroupMapFunction keysCompany = new KeyGroupMapFunction(keyPositions);
//		Encoder<Row> keyCompanyEncoder = EncoderGenerator.createEncoderFromEncoder(company.encoder(), keyPositions);
//		KeyValueGroupedDataset<Row, Row> groupedCompany = company.groupByKey(keysCompany, keyCompanyEncoder);

		PscCoGroup pscCoGroup = new PscCoGroup(bControl, bPerson);

		List<DataType> datatypes = new ArrayList<>();
		datatypes.add(DataTypes.StringType);
		datatypes.add(DataTypes.StringType);
		datatypes.add(DataTypes.StringType);

		Encoder<Row> pscEncoder = EncoderGenerator.generateDefaultEncoder("psc", 3, datatypes);

		Dataset<Row> psc = groupedKP.cogroup(groupedCompany, pscCoGroup, pscEncoder);
		psc.show();
		return psc;
	}

	private Object2ObjectOpenHashMap<String, ObjectOpenHashBigSet<Row>> buildIndex(Iterator<Row> it) {
		Object2ObjectOpenHashMap<String, ObjectOpenHashBigSet<Row>> map = new Object2ObjectOpenHashMap<>();
		while (it.hasNext()) {
			Row row = it.next();
			if (!map.containsKey(row.getString(0))) {
				map.put(row.getString(0), new ObjectOpenHashBigSet<Row>());
			}
			map.get(row.getString(0)).add(row);
		}
		return map;
	}
	
	@Override
	public void cleanUp() {
		if (bPerson != null) {
			bPerson.destroy();
		}
		if (bControl != null) {
			bControl.destroy();
		}
		super.cleanUp();
	}

}
