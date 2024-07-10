package uk.co.prometheux.prometheuxreasoner.setrdd;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.apache.spark.Partition;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.Optional;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.FlatMapFunction2;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.storage.StorageLevel;

import scala.Tuple2;
import scala.reflect.ClassTag;
import uk.co.prometheux.prometheuxreasoner.common.ConfigurationManager;

/**
 * This Class implements a SetRDD, i.e. a special resilient distributed dataset
 * that has an hashset of each partition
 * 
 * 
 *  Copyright (C) Prometheux Limited. All rights reserved.
 * @author Prometheux Limited
 *
 */

public class SetRDD extends JavaRDD<Row> {

	private static final long serialVersionUID = -7752448201643992955L;
	/* The underlying rdd */
	/* Each partitions contains a SetRDDPartition (an hash set of rows) */
	private JavaRDD<SetRDDPartition<Row>> partitionsRDD;
	/* The name of this rdd */
	private String name;
	/* The number of partitions associated to this rdd */
	private int numberOfPartitions = ConfigurationManager.getInstance().getIntProperty("shufflePartitions");

	public SetRDD(JavaRDD<Row> rdd, ClassTag<Row> classTag) {
		super(rdd.rdd(), classTag);
		this.partitionsRDD = convertToPartitionRDD(rdd);
	}

	public SetRDD(JavaRDD<Row> rdd, ClassTag<Row> classTag, int numberOfPartitions) {
		super(rdd.rdd(), classTag);
		this.numberOfPartitions = numberOfPartitions;
		this.partitionsRDD = convertToPartitionRDD(rdd);
	}

	public SetRDD(Dataset<Row> dataset, ClassTag<Row> classTag, int numberOfPartitions) {
		super(dataset.rdd(), classTag);
		this.numberOfPartitions = numberOfPartitions;
		this.partitionsRDD = convertToPartitionRDD(dataset.javaRDD());
	}

	/**
	 * It converts a javaRDD into a partition rdd
	 * 
	 * @param the                   input rdd to convert
	 * @param preservePartitioning: whether or not to preserve the partitioning
	 * @return an rdd where each partition is associated to a SetRDDPartition
	 */
	private JavaRDD<SetRDDPartition<Row>> convertToPartitionRDD(JavaRDD<Row> rdd) {
		/*
		 * We shuffle the rows of the original rdd into a defined number of partitions
		 */
		JavaRDD<SetRDDPartition<Row>> result = rdd.groupBy(row -> row, this.numberOfPartitions)
				/* for each partition we create an hash set partition */
				.mapPartitions(new HashSetPartitionConverter(), true);
		return result;
	}

	@Override
	public boolean isEmpty() {
		return this.count() == 0;
	}

	@Override
	public JavaRDD<Row> unpersist(boolean blocking) {
		this.partitionsRDD = this.partitionsRDD.unpersist(blocking);
		return this;
	}

	@Override
	public JavaRDD<Row> cache() {
		return this.persist(StorageLevel.MEMORY_ONLY());
	}

	@Override
	public SetRDD setName(String name) {
		this.partitionsRDD = this.partitionsRDD.setName(name);
		this.name = name;
		return this;
	}

	/**
	 * A list of partition sizes
	 * 
	 * @return a list of the sizes of every partition
	 */
	public List<Long> getPartitionSizes() {
		return this.partitionsRDD.map(x -> x.getSize()).collect();
	}

	@Override
	public String name() {
		return this.name;
	}

	@Override
	public StorageLevel getStorageLevel() {
		return this.partitionsRDD.getStorageLevel();
	}

	@Override
	public void checkpoint() {
		this.partitionsRDD.checkpoint();
	}

	@Override
	public Row first() {
		return this.partitionsRDD.first().iterator().next();
	}

	@Override
	public boolean isCheckpointed() {
		return this.partitionsRDD.isCheckpointed();
	}

	@Override
	public Optional<String> getCheckpointFile() {
		return this.partitionsRDD.getCheckpointFile();
	}

	@Override
	public <U> JavaRDD<U> mapPartitions(FlatMapFunction<Iterator<Row>, U> f) {
		return this.partitionsRDD.mapPartitions(iter -> f.call(iter.next().iterator()));
	}

	@Override
	public long count() {
		return this.partitionsRDD.map(x -> x.getSize()).reduce((x, y) -> x + y);
	}

	@Override
	public List<Partition> partitions() {
		return this.partitionsRDD.partitions();
	}

	@Override
	public List<Row> collect() {
		/* Collect the tuples of each partition into the driver */
		List<List<Row>> rows = this.partitionsRDD.collect().stream().map(p -> p.collectAsList())
				.collect(Collectors.toList());
		/* Returns the whole rdd as a list */
		return rows.stream().flatMap(Collection::stream).collect(Collectors.toList());
	}

	@Override
	public SetRDD union(JavaRDD<Row> other) {
		SetRDD unionRDD = null;
		if (other instanceof SetRDD) {
			SetRDD otherAsSet = (SetRDD) other;
			unionRDD = this.zipSetRDDPartitions(otherAsSet, new HashSetUnionPartition());
		} else {
			unionRDD = this.zipSetRDDPartitionsWithOther(other, new UnionPartition());
		}
		return unionRDD.setName("SetRDD.unionRDD");
	}

	@Override
	public SetRDD subtract(JavaRDD<Row> other) {
		SetRDD diffRDD = null;
		if (other instanceof SetRDD) {
			SetRDD otherAsSet = (SetRDD) other;
			diffRDD = this.zipSetRDDPartitions(otherAsSet, new HashSetDiffPartition());

		} else {
			diffRDD = this.zipSetRDDPartitionsWithOther(other, new DiffPartition());
		}
		return diffRDD.setName("SetRDD.diffRDD");
	}

	/**
	 * It returns the JavaRDD associated to this setRDD
	 * 
	 * @return the JavaRDD associated to this setRDD
	 */
	public JavaRDD<Row> javaRDD() {
		return this.partitionsRDD.mapPartitions(new FlatMapFunction<Iterator<SetRDDPartition<Row>>, Row>() {

			private static final long serialVersionUID = 180615051073095736L;

			@Override
			public Iterator<Row> call(Iterator<SetRDDPartition<Row>> it) throws Exception {
				return it.next().iterator();
			}
		});
	}

	/*
	 * Zip this RDD's partitions with one (or more) SetRDD(s) and return a new RDD
	 * by applying a function f to the zipped partitions. Assumes that all the RDDs
	 * have the *same number of partitions*, but does *not* require them to have the
	 * same number of elements in each partition.
	 */
	private SetRDD zipSetRDDPartitions(SetRDD other,
			FlatMapFunction2<Iterator<SetRDDPartition<Row>>, Iterator<SetRDDPartition<Row>>, SetRDDPartition<Row>> f) {
		JavaRDD<SetRDDPartition<Row>> rdd = this.partitionsRDD.zipPartitions(other.partitionsRDD, f);
		this.partitionsRDD = rdd;
		return this;
	}

	/*
	 * Zip this RDD's partitions with one (or more) RDD(s) and return a new RDD by
	 * applying a function f to the zipped partitions. Assumes that all the RDDs
	 * have the *same number of partitions*, but does *not* require them to have the
	 * same number of elements in each partition.
	 */
	private SetRDD zipSetRDDPartitionsWithOther(JavaRDD<Row> other,
			FlatMapFunction2<Iterator<SetRDDPartition<Row>>, Iterator<Row>, SetRDDPartition<Row>> f) {
		JavaRDD<SetRDDPartition<Row>> rdd = this.partitionsRDD.zipPartitions(other, f);
		this.partitionsRDD = rdd;
		return this;
	}

	/**
	 * It converts a partition into an hashSetPartition: every element in the
	 * partition is inserted into an hash set
	 */
	private class HashSetPartitionConverter
			implements FlatMapFunction<Iterator<Tuple2<Row, Iterable<Row>>>, SetRDDPartition<Row>> {
		private static final long serialVersionUID = 4847382769542105002L;

		@Override
		public Iterator<SetRDDPartition<Row>> call(Iterator<Tuple2<Row, Iterable<Row>>> partitionIterator)
				throws Exception {
			/* The hash set underlying the partition */
			Set<Row> set = new HashSet<>();
			/* We iterate over the tuples of the partition */
			while (partitionIterator.hasNext()) {
				Tuple2<Row, Iterable<Row>> tuple = partitionIterator.next();
				set.add(tuple._1);
			}
			/* The tuples in the partition are returned into and hashset partition */
			SetRDDPartition<Row> part = new SetRDDHashSetPartition<>(set);
			return Collections.singletonList(part).iterator();
		}
	}

	/**
	 * It takes two set partitions and merge them into one with the custom union
	 * function.
	 */
	private class HashSetUnionPartition implements
			FlatMapFunction2<Iterator<SetRDDPartition<Row>>, Iterator<SetRDDPartition<Row>>, SetRDDPartition<Row>> {

		private static final long serialVersionUID = 237826198102920310L;

		@Override
		public Iterator<SetRDDPartition<Row>> call(Iterator<SetRDDPartition<Row>> iter1,
				Iterator<SetRDDPartition<Row>> iter2) throws Exception {
			SetRDDPartition<Row> thisPart = iter1.next();
			SetRDDPartition<Row> otherPart = iter2.next();
			return Collections.singletonList(thisPart.union(otherPart)).iterator();
		}

	}

	/**
	 * It takes two set partitions and computes the set difference between the
	 * matching partitions
	 */
	private class HashSetDiffPartition implements
			FlatMapFunction2<Iterator<SetRDDPartition<Row>>, Iterator<SetRDDPartition<Row>>, SetRDDPartition<Row>> {

		private static final long serialVersionUID = 237826198102920310L;

		@Override
		public Iterator<SetRDDPartition<Row>> call(Iterator<SetRDDPartition<Row>> iter1,
				Iterator<SetRDDPartition<Row>> iter2) throws Exception {
			SetRDDPartition<Row> thisPart = iter1.next();
			SetRDDPartition<Row> otherPart = iter2.next();
			return Collections.singletonList(thisPart.diff(otherPart)).iterator();
		}

	}

	private class UnionPartition
			implements FlatMapFunction2<Iterator<SetRDDPartition<Row>>, Iterator<Row>, SetRDDPartition<Row>> {

		private static final long serialVersionUID = 5477736326660272044L;

		@Override
		public Iterator<SetRDDPartition<Row>> call(Iterator<SetRDDPartition<Row>> iter1, Iterator<Row> iter2)
				throws Exception {
			SetRDDPartition<Row> thisPart = iter1.next();
			return Collections.singletonList(thisPart.union(iter2)).iterator();
		}

	}

	private class DiffPartition
			implements FlatMapFunction2<Iterator<SetRDDPartition<Row>>, Iterator<Row>, SetRDDPartition<Row>> {

		private static final long serialVersionUID = 5477736326660272044L;

		@Override
		public Iterator<SetRDDPartition<Row>> call(Iterator<SetRDDPartition<Row>> iter1, Iterator<Row> iter2)
				throws Exception {
			SetRDDPartition<Row> thisPart = iter1.next();
			return Collections.singletonList(thisPart.diff(iter2)).iterator();
		}

	}
}
