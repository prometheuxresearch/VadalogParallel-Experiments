package uk.co.prometheux.prometheuxreasoner.evaluator.asp;

import java.util.Iterator;

import org.apache.spark.TaskContext;
import org.apache.spark.api.java.function.FlatMapGroupsFunction;
import org.apache.spark.broadcast.Broadcast;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashBigSet;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;

/**
 * 
 * Copyright (C) Prometheux Limited. All rights reserved.
 * 
 * @author Prometheux Limited
 */

public class ASPFlatMapGroup implements FlatMapGroupsFunction<Row, Row, Row> {

	private static final long serialVersionUID = 9213860881843120556L;

	private Broadcast<Object2ObjectOpenHashMap<Object, ObjectOpenHashBigSet<Row>>> arc;

	public ASPFlatMapGroup(Broadcast<Object2ObjectOpenHashMap<Object, ObjectOpenHashBigSet<Row>>> bVar) {
		this.arc = bVar;
	}

	@Override
	public Iterator<Row> call(Row k, Iterator<Row> iterator) throws Exception {
        
		String threadName = Thread.currentThread().getName();
		int partitionId = TaskContext.get().partitionId();

        System.out.println("ASP, Partition key: " + k + 
                           ", Partition ID: " + partitionId +
                           ", Thread Name: "+threadName +
                           ": BEGIN parallel reasoning");
		
		ObjectOpenHashSet<Row> deltaASP = new ObjectOpenHashSet<Row>();
		ObjectOpenHashSet<Row> newDeltaAPSP = new ObjectOpenHashSet<Row>();
		Object2ObjectOpenHashMap<Row, Double> APSP = new Object2ObjectOpenHashMap<>();

		// direct control
		while (iterator.hasNext()) {

			Row currentASP = iterator.next();
			Integer X = currentASP.getInt(0);
			Integer Y = currentASP.getInt(1);
			Double D = currentASP.getDouble(2);

			Row XY = RowFactory.create(X, Y);

			// if the current edge is not already visited for the current pair
//			if (!X.equals(Y)) {
			if (!APSP.containsKey(XY)) {
				APSP.put(XY, D);
				deltaASP.add(currentASP);
			} else {
				Double previousD = APSP.get(XY);
				if (previousD > D) {
					APSP.replace(XY, D);
					Row previousRow = RowFactory.create(X, Y, previousD);
					deltaASP.remove(previousRow);
					deltaASP.add(currentASP);
				}
			}
		}
//		}
		// indirect control
		while (!deltaASP.isEmpty()) {
			Iterator<Row> deltaIterator = deltaASP.iterator();
			while (deltaIterator.hasNext()) {
				Row ASPRow = deltaIterator.next();
				Integer Y = ASPRow.getInt(1);

				if (this.arc.getValue().containsKey(Y)) {
					ObjectOpenHashBigSet<Row> nextEdges = this.arc.getValue().get(Y);
					for (Row nextEdge : nextEdges) {
						Integer X = ASPRow.getInt(0);
						Double D1 = ASPRow.getDouble(2);

						Integer Z = nextEdge.getInt(1);
						Double D2 = nextEdge.getDouble(2);
//						if (X != Z) {
						Row XZ = RowFactory.create(X, Z);
						Row currentSP = RowFactory.create(X, Z, D1 + D2);
						if (!APSP.containsKey(XZ)) {
							APSP.put(XZ, D1 + D2);
							newDeltaAPSP.add(currentSP);
						} else {
							Double previousD = APSP.get(XZ);
							if (previousD > D1 + D2) {
								APSP.replace(XZ, D1 + D2);
								newDeltaAPSP.add(currentSP);
							}
						}
//						}
					}
				}
			}
			deltaASP = newDeltaAPSP;
			newDeltaAPSP = new ObjectOpenHashSet<Row>();
		}
		System.out.println("ASP, Partition key: " + k + 
                ", Partition ID: " + partitionId +
                ", Thread Name: "+threadName +
                ": END parallel reasoning");
        
        return APSP.entrySet().stream()
				.map(r -> RowFactory.create(r.getKey().getInt(0), r.getKey().getInt(1), r.getValue())).iterator();
	}
}
