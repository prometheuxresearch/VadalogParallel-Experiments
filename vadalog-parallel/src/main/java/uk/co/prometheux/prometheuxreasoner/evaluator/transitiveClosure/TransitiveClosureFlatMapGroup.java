package uk.co.prometheux.prometheuxreasoner.evaluator.transitiveClosure;

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

public class TransitiveClosureFlatMapGroup implements FlatMapGroupsFunction<Row, Row, Row> {

	private static final long serialVersionUID = 9078121082040863488L;

	private Broadcast<Object2ObjectOpenHashMap<Object, ObjectOpenHashBigSet<Row>>> bVar;

	public TransitiveClosureFlatMapGroup(Broadcast<Object2ObjectOpenHashMap<Object, ObjectOpenHashBigSet<Row>>> bVar) {
		this.bVar = bVar;
	}

	@Override
	public Iterator<Row> call(Row k, Iterator<Row> it) throws Exception {

		String threadName = Thread.currentThread().getName();
		int partitionId = TaskContext.get().partitionId();
		System.out.println("TRANSITIVE CLOSURE, Partition key: " + k + ", Partition ID: " + partitionId
				+ ", Thread Name: " + threadName + ": BEGIN parallel reasoning");

		ObjectOpenHashBigSet<Row> deltaTc = new ObjectOpenHashBigSet<Row>(it);
		ObjectOpenHashBigSet<Row> allTc = new ObjectOpenHashBigSet<>(deltaTc);
		ObjectOpenHashBigSet<Row> newDelta = new ObjectOpenHashBigSet<>();

		while (!deltaTc.isEmpty()) {
			Iterator<Row> iterator = deltaTc.iterator();
			while (iterator.hasNext()) {
				Row row = iterator.next();
				Object key = row.get(1);
				if (this.bVar.getValue().containsKey(key)) {
					ObjectOpenHashBigSet<Row> arcs = this.bVar.getValue().get(key);
					for (Row arc : arcs) {
						Row newTc = RowFactory.create(row.get(0), arc.get(1));
						if (!allTc.contains(newTc)) {
							newDelta.add(newTc);
							allTc.add(newTc);
						}
					}
				}
			}
			deltaTc = newDelta;
			newDelta = new ObjectOpenHashBigSet<>();

		}
		System.out.println("TRANSITIVE CLOSURE, Partition key: " + k + ", Partition ID: " + partitionId
				+ ", Thread Name: " + threadName + ": END parallel reasoning");
		return allTc.iterator();
	}
}
