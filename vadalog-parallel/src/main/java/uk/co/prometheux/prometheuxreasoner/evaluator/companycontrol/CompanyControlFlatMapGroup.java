package uk.co.prometheux.prometheuxreasoner.evaluator.companycontrol;

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

public class CompanyControlFlatMapGroup implements FlatMapGroupsFunction<Row, Row, Row> {

	private static final long serialVersionUID = 9213860881843120556L;

	private Broadcast<Object2ObjectOpenHashMap<Integer, ObjectOpenHashBigSet<Row>>> own;

	public CompanyControlFlatMapGroup(Broadcast<Object2ObjectOpenHashMap<Integer, ObjectOpenHashBigSet<Row>>> bVar) {
		this.own = bVar;
	}

	@Override
	public Iterator<Row> call(Row k, Iterator<Row> ownPart) throws Exception {
		
		String threadName = Thread.currentThread().getName();
		int partitionId = TaskContext.get().partitionId();

		System.out.println("COMPANY CONTROL, Partition key: " + k + ", Partition ID: " + partitionId + ", Thread Name: "
				+ threadName + ": BEGIN parallel reasoning");
		ObjectOpenHashBigSet<Row> newDeltaControl = new ObjectOpenHashBigSet<Row>();
		Object2ObjectOpenHashMap<Row, Double> control = new Object2ObjectOpenHashMap<>();
		ObjectOpenHashBigSet<Row> controlledShares = new ObjectOpenHashBigSet<Row>();

		Object2ObjectOpenHashMap<Row, Double> aggregationTable = new Object2ObjectOpenHashMap<>();

		ObjectOpenHashBigSet<Row> deltaControl = new ObjectOpenHashBigSet<Row>();

		// direct control
		while (ownPart.hasNext()) {

			Row currentOwn = ownPart.next();
			Integer X = currentOwn.getInt(0);
			Integer Y = currentOwn.getInt(1);
			Double value = currentOwn.getDouble(2);

			// if the current ownership edge is not already visited for the current pair
			if (!X.equals(Y)) {
				Row currentControlledShares = RowFactory.create(X, Y, Y, value);
				if (!controlledShares.contains(currentControlledShares)) {
					controlledShares.add(currentControlledShares);
					// we update its control value
					Row group = RowFactory.create(X, Y);
					if (!aggregationTable.containsKey(group)) {
						aggregationTable.put(group, value);
					} else {
						if (aggregationTable.get(group) < value) {
							aggregationTable.replace(group, value);
						}
					}
					// we evaluate if it is controlled only if not already generated
					if (value >= 0.5) {
						Row newControl = RowFactory.create(X, Y, value);
						deltaControl.add(newControl);
						control.put(group, value);
					}
				}
			}
		}
		// indirect control
		while (!deltaControl.isEmpty()) {
			Iterator<Row> iterator = deltaControl.iterator();
			while (iterator.hasNext()) {
				// join left hand side, control pair
				Row controlRow = iterator.next();
				Integer Z = controlRow.getInt(1);

				// if there is a join match with the ownership values
				if (this.own.getValue().containsKey(Z)) {
					ObjectOpenHashBigSet<Row> ownershipEdges = this.own.getValue().get(Z);
					// current ownership edge
					for (Row ownershipEdge : ownershipEdges) {
						// if X <> Z
						Integer X = controlRow.getInt(0);
						Integer Y = ownershipEdge.getInt(1);
						if (!X.equals(Z) && !Y.equals(Z) && !X.equals(Y)) {
							double value = ownershipEdge.getDouble(2);
							Row currentControlledShares = RowFactory.create(X, Y, Z, value);
							if (!controlledShares.contains(currentControlledShares)) {
								controlledShares.add(currentControlledShares);
								// we create the potential control pair
								Row group = RowFactory.create(X, Y);
								Double newWeight = value;
								// we update the aggregation table and visited edges if they are empty
								if (!aggregationTable.containsKey(group)) {
									aggregationTable.put(group, newWeight);
								} else {
									// we update the aggregation table
									newWeight = aggregationTable.get(group) + value;
									aggregationTable.replace(group, newWeight);
								}
								// check if it is already generated and add it to the delta
								if (newWeight >= 0.5) {
									Row newControl = RowFactory.create(X,Y,newWeight);
									deltaControl.add(newControl);
									control.put(group, newWeight);
								}

							}
						}
					}

				}
			}
			deltaControl = newDeltaControl;
			newDeltaControl = new ObjectOpenHashBigSet<Row>();
		}

		System.out.println("COMPANY CONTROL, Partition key: " + k + ", Partition ID: " + partitionId + ", Thread Name: "
				+ threadName + ": END parallel reasoning");
		
		return control.entrySet().stream().map(r -> r.getKey()).iterator();
	}
	

}