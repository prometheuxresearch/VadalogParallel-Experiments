package uk.co.prometheux.prometheuxreasoner.evaluator.defaultpropagation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.hadoop.shaded.org.apache.commons.codec.digest.DigestUtils;
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

public class DefaultPropagationFlatMapGroup implements FlatMapGroupsFunction<Row, Row, Row> {

	private static final long serialVersionUID = -5696690148169060251L;
	private Broadcast<Object2ObjectOpenHashMap<Integer, ObjectOpenHashBigSet<Row>>> bSecurity;
	private Broadcast<Object2ObjectOpenHashMap<Integer, ObjectOpenHashBigSet<Row>>> bLoan;
	private int labelledNullIndex = 0;

	public DefaultPropagationFlatMapGroup(Broadcast<Object2ObjectOpenHashMap<Integer, ObjectOpenHashBigSet<Row>>> bLoan,
			Broadcast<Object2ObjectOpenHashMap<Integer, ObjectOpenHashBigSet<Row>>> bSecurity) {
		this.bLoan = bLoan;
		this.bSecurity = bSecurity;
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

	@Override
	public Iterator<Row> call(Row k, Iterator<Row> creditExp) throws Exception {

		String threadName = Thread.currentThread().getName();
		int partitionId = TaskContext.get().partitionId();

		System.out.println("DEFAULT PROPAGATION, Partition key: " + k + ", Partition ID: " + partitionId
				+ ", Thread Name: " + threadName + ": BEGIN parallel reasoning");

		ObjectOpenHashBigSet<String> defaultsLabelledNulls = new ObjectOpenHashBigSet<>();
		ObjectOpenHashBigSet<Row> deltaDefaults = new ObjectOpenHashBigSet<Row>();
		ObjectOpenHashBigSet<Row> defaults = new ObjectOpenHashBigSet<Row>();
		
		String nullSeed = k.get(0).toString();

		while (creditExp.hasNext()) {
			Row currCE = creditExp.next();
			Double probability = currCE.getDouble(1);
			if (probability > 0.5) {
				this.labelledNullIndex++;
				String nullValue = "z" + "_" + nullSeed + "_" + this.labelledNullIndex;
				Row currDefault = RowFactory.create(currCE.get(0), currCE.get(0), nullValue);
				String reasoningKey = this.getReasoningKey(this.extractArguments(currDefault));
				if (!defaultsLabelledNulls.contains(reasoningKey)) {
					defaultsLabelledNulls.add(reasoningKey);
					deltaDefaults.add(currDefault);
					defaults.add(currDefault);
				}
			}

		}
		ObjectOpenHashBigSet<Row> newDelta = new ObjectOpenHashBigSet<Row>();
		while (!deltaDefaults.isEmpty()) {
			Iterator<Row> it = deltaDefaults.iterator();
			while (it.hasNext()) {
				Row currDef = it.next();
				Integer B = currDef.getInt(0);
				if (this.bLoan.getValue().containsKey(B)) {
					ObjectOpenHashBigSet<Row> loans = this.bLoan.getValue().get(B);
					for (Row loan : loans) {
						if (loan.getDouble(2) >= 0.5) {
							Integer A = currDef.getInt(1);
							Integer C = loan.getInt(1);
							this.labelledNullIndex++;
							String nullValue = "z" + "_" + nullSeed + "_" + this.labelledNullIndex;
							Row currDefault = RowFactory.create(C, A, nullValue);
							String reasoningKey = this.getReasoningKey(this.extractArguments(currDefault));
							if (!defaultsLabelledNulls.contains(reasoningKey)) {
								defaultsLabelledNulls.add(reasoningKey);
								newDelta.add(currDefault);
								defaults.add(currDefault);
							}
						}
					}

				}
				if (this.bSecurity.getValue().containsKey(B)) {
					ObjectOpenHashBigSet<Row> secs = this.bSecurity.getValue().get(B);
					for (Row sec : secs) {
						if (sec.getDouble(2) >= 0.3) {
							Integer A = currDef.getInt(1);
							Integer C = sec.getInt(1);
							this.labelledNullIndex++;
							String nullValue = "z" + "_" + nullSeed + "_" + this.labelledNullIndex;
							Row currDefault = RowFactory.create(C, A, nullValue);
							String reasoningKey = this.getReasoningKey(this.extractArguments(currDefault));
							if (!defaultsLabelledNulls.contains(reasoningKey)) {
								defaultsLabelledNulls.add(reasoningKey);
								newDelta.add(currDefault);
								defaults.add(currDefault);
							}
						}
					}

				}

			}
			deltaDefaults = newDelta;
			newDelta = new ObjectOpenHashBigSet<Row>();
		}
		System.out.println("DEFAULT PROPAGATION, Partition key: " + k + ", Partition ID: " + partitionId
				+ ", Thread Name: " + threadName + ": END parallel reasoning");
		return defaults.iterator();
	}

}
