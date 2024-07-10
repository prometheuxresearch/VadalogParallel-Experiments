package uk.co.prometheux.prometheuxreasoner.evaluator.trianglecounting;

import java.io.File;
import java.util.List;

import org.apache.spark.sql.Column;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

import uk.co.prometheux.prometheuxreasoner.evaluator.ProgramEvaluator;
import uk.co.prometheux.prometheuxreasoner.model.annotations.BindAnnotation;

/**
 * 
 * Copyright (C) Prometheux Limited. All rights reserved.
 * 
 * @author Prometheux Limited
 */

public class TriangleCountingProgramEvaluator extends ProgramEvaluator {

	private String arcInputPath;

	public TriangleCountingProgramEvaluator(List<BindAnnotation> inputAnnotations,
			List<BindAnnotation> outputAnnotations) {
		BindAnnotation inputArc = inputAnnotations.get(0);
		arcInputPath = inputArc.getSchema() + File.separator + inputArc.getTableOrQuery();
	}

	@Override
	public void evaluate() {
		System.out.println("TRIANGLE COUNTING, reading input file " + new File(arcInputPath).getAbsolutePath());
		Dataset<Row> arc = spark.read().format("csv").option("header", "true").option("inferSchema", "true")
				.load(arcInputPath);
		Dataset<Row> arc2 = arc.alias("arc2").withColumnRenamed(arc.columns()[0], "arc2_0")
				.withColumnRenamed(arc.columns()[1], "arc2_1");

//				t(X,Y,Z) :- vatom_2(X,Y,Z).
//				count_t(C) :- t(X,Y,Z), C=mcount(1).
//				vatom_1(X,Y,Z) :- arc(X,Y), arc(Y,Z), X<Y, Y<Z.
//				vatom_2(X,Y,Z) :- vatom_1(X,Y,Z), arc(Z,X).

		Dataset<Row> vatom_1 = arc.join(arc2, new Column(arc.columns()[1]).equalTo(new Column("arc2_0")))
				.where(new Column(arc.columns()[0]).lt(new Column("arc2_0"))
						.and(new Column("arc2_0").lt(new Column("arc2_1"))))
				.select(new Column(arc.columns()[0]).alias("vatom_1_0"),
						new Column(arc.columns()[1]).alias("vatom_1_1"), new Column("arc2_1").alias("vatom_1_2"));
		Dataset<Row> vatom2 = vatom_1
				.join(arc,
						new Column("vatom_1_0").equalTo(new Column(arc.columns()[1]))
								.and((new Column("vatom_1_2").equalTo(new Column(arc.columns()[0])))))
				.select(new Column("vatom_1_0"), new Column("vatom_1_1"), new Column("vatom_1_2"));

		System.out.println("TRIANGLE COUNTING END parallel reasoning Count:" + vatom2.count());
	}
}
