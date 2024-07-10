package uk.co.prometheux.prometheuxreasoner.evaluator.samegeneration;

import java.util.Iterator;

import org.apache.spark.TaskContext;
import org.apache.spark.api.java.function.FlatMapGroupsFunction;
import org.apache.spark.broadcast.Broadcast;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashBigSet;

/**
 * 
 * Copyright (C) Prometheux Limited. All rights reserved.
 * 
 * @author Prometheux Limited
 */

public class SameGenerationFlatMapGroup implements FlatMapGroupsFunction<Row, Row, Row> {

	private static final long serialVersionUID = 5277710114026239859L;

	private Broadcast<Object2ObjectOpenHashMap<Integer, ObjectOpenHashBigSet<Row>>> arc;

	public SameGenerationFlatMapGroup(Broadcast<Object2ObjectOpenHashMap<Integer, ObjectOpenHashBigSet<Row>>> bVar) {
		this.arc = bVar;
	}

	@Override
	public Iterator<Row> call(Row k, Iterator<Row> it) throws Exception {

		String threadName = Thread.currentThread().getName();
		int partitionId = TaskContext.get().partitionId();
		System.out.println("SAME GENERATION, Partition key: " + k + ", Partition ID: " + partitionId + ", Thread Name: "
				+ threadName + ": BEGIN parallel reasoning");

		ObjectOpenHashBigSet<Row> allSg = new ObjectOpenHashBigSet<>();
		ObjectOpenHashBigSet<Row> deltaSg = new ObjectOpenHashBigSet<>();

		// linear rule
		while (it.hasNext()) {
			Row currentArc = it.next();
			Integer p = currentArc.getInt(0);
			Integer x = currentArc.getInt(1);
			if (this.arc.getValue().containsKey(p)) {
				ObjectOpenHashBigSet<Row> matchingRight = this.arc.getValue().get(p);
				for (Row arcRhs : matchingRight) {
					Integer y = arcRhs.getInt(1);
					if (!x.equals(y)) {
						Row newRow = RowFactory.create(x, y);
						if (!allSg.contains(newRow)) {
							allSg.add(newRow);
							deltaSg.add(newRow);
						}
					}
				}
			}

		}

		ObjectOpenHashBigSet<Row> newDelta = new ObjectOpenHashBigSet<>();
		while (!deltaSg.isEmpty()) {
			Iterator<Row> iterator = deltaSg.iterator();
			while (iterator.hasNext()) {
				Row currentSg = iterator.next();
				Integer a = currentSg.getInt(0);
				Integer b = currentSg.getInt(1);
				if (this.arc.getValue().containsKey(a) && this.arc.getValue().containsKey(b)) {
					ObjectOpenHashBigSet<Row> matching1 = this.arc.getValue().get(a);
					ObjectOpenHashBigSet<Row> matching2 = this.arc.getValue().get(b);
					for (Row row1 : matching1) {
						for (Row row2 : matching2) {
							Integer x = row1.getInt(1);
							Integer y = row2.getInt(1);
							Row newRow = RowFactory.create(x, y);
							if (!allSg.contains(newRow)) {
								allSg.add(newRow);
								newDelta.add(newRow);
							}

						}
					}

				}
			}
			deltaSg = newDelta;
			newDelta = new ObjectOpenHashBigSet<>();
		}

		System.out.println("SAME GENERATION, Partition key: " + k + ", Partition ID: " + partitionId + ", Thread Name: "
				+ threadName + ": END parallel reasoning");

		return allSg.iterator();

	}

}
