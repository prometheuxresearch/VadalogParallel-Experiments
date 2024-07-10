package uk.co.prometheux.prometheuxreasoner.evaluator.stronglinks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.hadoop.shaded.org.apache.commons.codec.digest.DigestUtils;
import org.apache.spark.TaskContext;
import org.apache.spark.api.java.function.CoGroupFunction;
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

public class StrongLinksCoGroup implements CoGroupFunction<String, Row, Row, Row> {
	private static final long serialVersionUID = 1365579740825908889L;

	private Broadcast<Object2ObjectOpenHashMap<String, ObjectOpenHashBigSet<Row>>> bControl;
	private Broadcast<Object2ObjectOpenHashMap<String, ObjectOpenHashBigSet<Row>>> bPerson;
	private int labelledNullIndex = 0;

	public StrongLinksCoGroup(Broadcast<Object2ObjectOpenHashMap<String, ObjectOpenHashBigSet<Row>>> bControl,
			Broadcast<Object2ObjectOpenHashMap<String, ObjectOpenHashBigSet<Row>>> bPerson) {
		super();
		this.bControl = bControl;
		this.bPerson = bPerson;
	}

	@Override
	public Iterator<Row> call(String k, Iterator<Row> keyPersonIt, Iterator<Row> companyIt) throws Exception {
		String threadName = Thread.currentThread().getName();
		int partitionId = TaskContext.get().partitionId();

		System.out.println("STRONG LINKS, Partition key: " + k + ", Partition ID: " + partitionId + ", Thread Name: "
				+ threadName + ": BEGIN parallel reasoning");

		ObjectOpenHashBigSet<String> strongLinksLabelledNull = new ObjectOpenHashBigSet<>();
		ObjectOpenHashBigSet<Row> deltaStrongLinks = new ObjectOpenHashBigSet<Row>();
		String nullSeed = k;
		ObjectOpenHashBigSet<Row> psc = new ObjectOpenHashBigSet<Row>();
		Object2ObjectOpenHashMap<String, ObjectOpenHashBigSet<Row>> pscIndex = new Object2ObjectOpenHashMap<>();

		while (keyPersonIt.hasNext()) {
			Row currRow = keyPersonIt.next();
			String joinKey = currRow.getString(1);
			if (this.bPerson.getValue().containsKey(joinKey)) {
				Row toGenerate = RowFactory.create(currRow.getString(0), currRow.getString(0), currRow.getString(1));
				String reasoningKey = this.getReasoningKey(this.extractArguments(toGenerate));
				if (!strongLinksLabelledNull.contains(reasoningKey)) {
					strongLinksLabelledNull.add(reasoningKey);
					deltaStrongLinks.add(toGenerate);
					String joinKeyStrongLinks = toGenerate.getString(1);
					psc.add(toGenerate);
					if (!pscIndex.containsKey(joinKeyStrongLinks)) {
						pscIndex.put(joinKeyStrongLinks, new ObjectOpenHashBigSet<Row>());
					}
					pscIndex.get(joinKeyStrongLinks).add(toGenerate);
				}

			}
		}

		while (companyIt.hasNext()) {
			Row currRow = companyIt.next();
			this.labelledNullIndex++;
			String nullValue = "z" + "_" + nullSeed + "_" + this.labelledNullIndex;
			Row toGenerate = RowFactory.create(currRow.getString(0), currRow.getString(0), nullValue);
			String reasoningKey = this.getReasoningKey(this.extractArguments(toGenerate));
			if (!strongLinksLabelledNull.contains(reasoningKey)) {
				strongLinksLabelledNull.add(reasoningKey);
				deltaStrongLinks.add(toGenerate);
				String joinKeyStrongLinks = toGenerate.getString(1);
				psc.add(toGenerate);
				if (!pscIndex.containsKey(joinKeyStrongLinks)) {
					pscIndex.put(joinKeyStrongLinks, new ObjectOpenHashBigSet<Row>());
				}
				pscIndex.get(joinKeyStrongLinks).add(toGenerate);
			}
		}

		ObjectOpenHashBigSet<Row> newDelta = new ObjectOpenHashBigSet<Row>();
		while (!deltaStrongLinks.isEmpty()) {
			Iterator<Row> it = deltaStrongLinks.iterator();
			while (it.hasNext()) {
				Row currStrongLinks = it.next();
				String joinKey = currStrongLinks.getString(0);
				if (this.bControl.value().containsKey(joinKey)) {
					for (Row control : this.bControl.getValue().get(joinKey)) {
						Row pscToGenerate = RowFactory.create(control.getString(1), currStrongLinks.getString(1),
								currStrongLinks.getString(1));
						String reasoningKey = this.getReasoningKey(this.extractArguments(pscToGenerate));
						if (!strongLinksLabelledNull.contains(reasoningKey)) {
							strongLinksLabelledNull.add(reasoningKey);
							newDelta.add(pscToGenerate);
							String joinKeyStrongLinks = pscToGenerate.getString(1);
							psc.add(pscToGenerate);
							if (!pscIndex.containsKey(joinKeyStrongLinks)) {
								pscIndex.put(joinKeyStrongLinks, new ObjectOpenHashBigSet<Row>());
							}
							pscIndex.get(joinKeyStrongLinks).add(pscToGenerate);
						}
					}
				}
			}

			deltaStrongLinks = newDelta;
			newDelta = new ObjectOpenHashBigSet<Row>();
		}
		ObjectOpenHashBigSet<Row> strongLinks = this.joinAndCount(psc, pscIndex);
		System.out.println("STRONG LINKS, Partition key: " + k + ", Partition ID: " + partitionId + ", Thread Name: "
				+ threadName + ": END parallel reasoning");
		return strongLinks.iterator();
	}

	private ObjectOpenHashBigSet<Row> joinAndCount(ObjectOpenHashBigSet<Row> psc,
			Object2ObjectOpenHashMap<String, ObjectOpenHashBigSet<Row>> pscIndex) {
		ObjectOpenHashBigSet<Row> strongLinksGt3 = new ObjectOpenHashBigSet<>();
		Object2ObjectOpenHashMap<Row, Integer> strongLinksAgg = new Object2ObjectOpenHashMap<>();
		for (Row pscRow : psc) {
			String joinKeyStrongLinks = pscRow.getString(1);
			ObjectOpenHashBigSet<Row> matches = pscIndex.get(joinKeyStrongLinks);
			for (Row match : matches) {
				String X = pscRow.getString(0);
				String Y = match.getString(0);
				if (!X.equals(Y)) {
					// X, Y, Z
					Row strongLinksRow = RowFactory.create(X, Y, joinKeyStrongLinks);
					if (!strongLinksAgg.containsKey(strongLinksRow)) {
						strongLinksAgg.put(strongLinksRow, 1);
					} else {
						Integer oldCount = strongLinksAgg.get(strongLinksRow);
						int newCount = oldCount + 1;
						strongLinksAgg.put(strongLinksRow, newCount);
						/*
						 * A strong link if mcount is gt 3
						 */
						if (newCount > 3) {
							strongLinksGt3.remove(RowFactory.create(X, Y, oldCount, joinKeyStrongLinks));
							strongLinksGt3.add(RowFactory.create(X, Y, newCount, joinKeyStrongLinks));
						}
					}
				}
			}
		}
		return strongLinksGt3;
	}

	private List<Object> extractArguments(Row row) {
		List<Object> args = new ArrayList<>();
		for (int i = 0; i < row.length(); i++)
			args.add(row.get(i));
		return args;
	}

	private String getReasoningKey(List<Object> keyArguments) {
		Map<Object, Integer> nullToIndex = new HashMap<>();
		Integer ind = 0;
		List<Object> renamedValues = new ArrayList<>();
		for (Object arg : keyArguments) {
			if (arg instanceof String && ((String) arg).startsWith("z_")) {
				if (!nullToIndex.containsKey(arg)) {
					nullToIndex.put(arg, ind);
					ind++;
				}
				renamedValues.add(nullToIndex.get(arg));
			} else {
				renamedValues.add(arg);
			}
		}
		return DigestUtils.md5Hex(renamedValues.toString());
	}

}
