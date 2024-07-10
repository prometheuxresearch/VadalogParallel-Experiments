package uk.co.prometheux.prometheuxreasoner.common;

import java.util.List;

import org.apache.spark.sql.Encoder;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.catalyst.encoders.RowEncoder;
import org.apache.spark.sql.types.DataType;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;

import uk.co.prometheux.prometheuxreasoner.model.annotations.TypeEnum;
import uk.co.prometheux.prometheuxreasoner.model.types.VadaValue;
//import uk.co.prometheux.prometheuxreasoner.record.TableInfo;

/**
 * 
 * A class with methods for generating custom encoders. An encoder embeds the
 * metadata of a spark dataset (i.e., the names of the columns and the types)
 * 
 * 
 *  Copyright (C) Prometheux Limited. All rights reserved.
 * @author Prometheux Limited
 */

public class EncoderGenerator {

	/**
	 * It creates a default encoder given a dataset name and the set of columns of
	 * string type. Given datasetName = a and numberOfColumns = 2 --> (a_0:String,
	 * a_1:String)
	 * 
	 * @param datasetName     the name of the dataset for such encoder
	 * @param numberOfColumns the number of columns for the encoder
	 * @return a default encoder
	 */
	public static Encoder<Row> generateDefaultEncoder(String datasetName, int numberOfColumns) {
		StructType structType = new StructType();
		for (int index = 0; index < numberOfColumns; index++) {
			structType = structType.add(datasetName + "_" + "" + String.valueOf(index), DataTypes.StringType, false);
		}
		return RowEncoder.apply(structType);
	}
	
	public static Encoder<Row> generateDefaultEncoder(String datasetName, int numberOfColumns, List<DataType> dataTypes) {
	    if (dataTypes.size() != numberOfColumns) {
	        throw new IllegalArgumentException("The size of dataTypes list must match the numberOfColumns");
	    }
	    
	    StructType structType = new StructType();
	    for (int index = 0; index < numberOfColumns; index++) {
	        structType = structType.add(datasetName + "_" + index, dataTypes.get(index), false);
	    }
	    return RowEncoder.apply(structType);
	}

	private static DataType getDataType(String columnType) {
		if (columnType.equals("integer"))
			return DataTypes.IntegerType;
		else if (columnType.equals("string"))
			return DataTypes.StringType;
		else if (columnType.equals("double"))
			return DataTypes.DoubleType;
		else if (columnType.equals("boolean"))
			return DataTypes.BooleanType;
		else if (columnType.equals("date"))
			return DataTypes.DateType;
		else if (columnType.equals("list"))
			return DataTypes.createArrayType(DataTypes.StringType);
		else
			return DataTypes.StringType;
	}
	
	public static DataType getDataType(VadaValue<?> v) {
		TypeEnum te = v.getExplicitType();
		if (te == TypeEnum.INT)
			return DataTypes.IntegerType;
		else if (te == TypeEnum.STRING)
			return DataTypes.StringType;
		else if (te == TypeEnum.DOUBLE)
			return DataTypes.DoubleType;
		else if (te == TypeEnum.BOOLEAN)
			return DataTypes.BooleanType;
		else if (te == TypeEnum.DATE)
			return DataTypes.DateType;
		else if (te == TypeEnum.LIST || te == TypeEnum.SET)
			return DataTypes.createArrayType(getDataType(v.inferInnerType().getType()));
		else
			return DataTypes.StringType;
	}

	/**
	 * It creates an encoder projecting some columns from an input encoder
	 * 
	 * @param from,                the input encoder
	 * @param interestedPositions, the position to project from the input encoder
	 * @return a new encoder
	 */
	public static Encoder<Row> createEncoderFromEncoder(Encoder<Row> from, List<Integer> interestedPositions) {
		StructField[] structFieldFrom = from.schema().fields();
		StructType structTypeTo = new StructType();
		for (int pos : interestedPositions) {
			structTypeTo = structTypeTo.add(structFieldFrom[pos]);
		}
		return RowEncoder.apply(structTypeTo);
	}

}
