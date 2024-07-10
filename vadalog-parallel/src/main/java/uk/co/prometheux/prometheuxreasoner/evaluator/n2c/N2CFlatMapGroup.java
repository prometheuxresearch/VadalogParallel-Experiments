package uk.co.prometheux.prometheuxreasoner.evaluator.n2c;

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
 * @author Prometheux Limited
 */

public class N2CFlatMapGroup implements FlatMapGroupsFunction<Row, Row, Row> {

	private static final long serialVersionUID = 9078121082040863488L;

	private Broadcast<Object2ObjectOpenHashMap<Integer, ObjectOpenHashBigSet<Row>>> map;

	public N2CFlatMapGroup(Broadcast<Object2ObjectOpenHashMap<Integer, ObjectOpenHashBigSet<Row>>> bVar) {
		this.map = bVar;
	}

	@Override
	public Iterator<Row> call(Row k, Iterator<Row> it) throws Exception {
		String threadName = Thread.currentThread().getName();
		int partitionId = TaskContext.get().partitionId();

		System.out.println("N2C, Partition key: " + k + ", Partition ID: " + partitionId
				+ ", Thread Name: " + threadName + ": BEGIN parallel reasoning");
		ObjectOpenHashBigSet<Row> deltaN2C = new ObjectOpenHashBigSet<Row>();
		ObjectOpenHashBigSet<Row> N2C = new ObjectOpenHashBigSet<>();
		ObjectOpenHashBigSet<Row> newDeltaN2C = new ObjectOpenHashBigSet<>();
		
		/*
		 * We fill the N2C paths with the odds, which are the edges
		 * at distance 1
		 */
		while(it.hasNext()) {
			Row next = it.next();
			Row oddN2C = RowFactory.create(next.getInt(0), next.getInt(1), "ODD");
			N2C.add(oddN2C);
			deltaN2C.add(oddN2C);
		}
		
		/*
		 * We iterate over the new N2C paths, until we do not have new 
		 * ones
		 */
		while (!deltaN2C.isEmpty()) {
			Iterator<Row> iterator = deltaN2C.iterator();
			while (iterator.hasNext()) {
				Row row = iterator.next();
				Integer key = row.getInt(1);
				if (this.map.getValue().containsKey(key)) {
					ObjectOpenHashBigSet<Row> edges = this.map.getValue().get(key);
					for (Row edge : edges) {
						String oddOrEven = row.getString(2).equals("ODD")? "EVEN" : "ODD";
						Row N2CRow = RowFactory.create(row.getInt(0), edge.getInt(1), oddOrEven);
						if (!N2C.contains(N2CRow)) {
							newDeltaN2C.add(N2CRow);
							N2C.add(N2CRow);
						}
					}
				}
			}
			deltaN2C = newDeltaN2C;
			newDeltaN2C = new ObjectOpenHashBigSet<>();

		}
		System.out.println("N2C, Partition key: " + k + ", Partition ID: " + partitionId
				+ ", Thread Name: " + threadName + ": END parallel reasoning");
		return N2C.iterator();
	}
}
