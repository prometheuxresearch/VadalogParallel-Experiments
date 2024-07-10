package uk.co.prometheux.prometheuxreasoner.common.io;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import uk.co.prometheux.prometheuxreasoner.PrometheuxRuntimeException;

/**
 * Helper to handle the selectedColumn commnad of CSV properties
 * 
 * 
 *  Copyright (C) Prometheux Limited. All rights reserved.
 * @author Prometheux Limited
 * 
 */

public class SelectedColumnHelper {
	private List<String> originalColumnsNameList = new ArrayList<>();
	private String[] selectedColumnsExprArr;
	private String inputSelectedColumnString = "";
	
	public SelectedColumnHelper(String[] columns, String selectedColumnsString) {
		for(String c : columns) {
			originalColumnsNameList.add(c);
		}
		this.inputSelectedColumnString = selectedColumnsString.trim().replaceAll("\'", "").replaceAll("\"", "");
		this.selectedColumns();
	}

	public SelectedColumnHelper(String[] columns) {
		/*
		 * The columns are the original ones, but we prepend and append ` because Spark
		 * gives error for headers having a field containing spaces
		 */
		selectedColumnsExprArr = new String[columns.length];
		for(int i = 0; i < columns.length; i++) {
			selectedColumnsExprArr[i] = "`"+columns[i]+"`";
		}
	}

	private void selectedColumns() {
		if(!inputSelectedColumnString.startsWith("[") || !inputSelectedColumnString.endsWith("]")) {
			throw new PrometheuxRuntimeException("Malformed input. Examples of correct use of the selectedColumns command are: selectedColumns=[0:3] or selectedColumns=[0:3,\'colName4\',\'colName10\'] or selectedColumns=[\'colName1\',\'colName2\']");
		}
		inputSelectedColumnString = inputSelectedColumnString.substring(1, inputSelectedColumnString.length() - 1 );
		// we separate selectedColumnsNumber int:int from selectedColumnsNames \'colName4\',\'colName10\'
		String[] selectedColumnsArr = inputSelectedColumnString.split(";");
		if (selectedColumnsArr.length == 0) {
			throw new PrometheuxRuntimeException("Malformed input. Examples of correct use of the selectedColumns command are: selectedColumns=[0:3] or selectedColumns=[0:3,\'colName4\',\'colName10\'] or selectedColumns=[\'colName1\',\'colName2\']");
		}
		
		List<Integer> interestedColumnIndexes = new ArrayList<>();
		for(String columnNumberOrNameOrSequenceToSelect : selectedColumnsArr) {
			List<Integer> extractIndexes = this.extractIndexes(columnNumberOrNameOrSequenceToSelect);
			for(Integer extractedIndex : extractIndexes) {
				if(interestedColumnIndexes.contains(extractedIndex)) {
					throw new PrometheuxRuntimeException("The column index '"+extractedIndex+"' that you want to select, overlaps with the another column index, or "
							+ " with the column name "+originalColumnsNameList.get(extractedIndex)+" that you also want to select");
				}
				else {
					interestedColumnIndexes.add(extractedIndex);
				}
			}
		}
		
		// now that we know which are the interested indexes, we get the original names
		// we are sure that we are contained in the original columns, since we checked it in the previous steps
		selectedColumnsExprArr = new String[interestedColumnIndexes.size()];
		int i = 0;
		for(Integer interestedColumnIndex : interestedColumnIndexes) {
			selectedColumnsExprArr[i] = "`"+this.originalColumnsNameList.get(interestedColumnIndex)+"`";
			i++;
		}
	}
	
	private List<Integer> extractIndexes(String columnNumberOrNameOrSequenceToSelect) {
		List<Integer> extractedIndexes = new ArrayList<>();

		// we first understand if it is a sequence int:int
		Pattern regex1 = Pattern.compile("\\d+:\\d+");
		if(regex1.matcher(columnNumberOrNameOrSequenceToSelect).matches()) {
			String[] rangeNCol = columnNumberOrNameOrSequenceToSelect.split(":");
			Integer startCol;
			Integer endCol = null;
			try {
				startCol = Integer.valueOf(rangeNCol[0]);
			} catch (NumberFormatException e) {
				throw new PrometheuxRuntimeException("The first element of " + columnNumberOrNameOrSequenceToSelect + "must be an integer");
			}
			// we also have the end col
			try {
				endCol = Integer.valueOf(rangeNCol[1]);
			} catch (Exception e) {
				throw new PrometheuxRuntimeException("The second element of the column indexes you want to select: '" + columnNumberOrNameOrSequenceToSelect
						+ "' must be an integer, but was not specified");
			}
			if(startCol >= endCol) {
				throw new PrometheuxRuntimeException("The start col '"+startCol+"' must be lower than the end col "+endCol+" of "+columnNumberOrNameOrSequenceToSelect);
			}
			if(endCol > originalColumnsNameList.size() - 1) {
				throw new PrometheuxRuntimeException("The input source has "+ originalColumnsNameList.size()+ " columns but you "
						+ "selected the "+(endCol + 1)+"-th column");
			}
			for (int i = startCol; i <= endCol; i++) {
				extractedIndexes.add(i);
			}
			return extractedIndexes;
		}
		// we now understand if it is an integer or a column name
		Integer singleColumnIndex;
		boolean isString = true;
		try {
			singleColumnIndex = Integer.valueOf(columnNumberOrNameOrSequenceToSelect);
			isString = false;
			extractedIndexes.add(singleColumnIndex);
		} catch (NumberFormatException e) {}
		if(!isString) {
			return extractedIndexes;
		}
		// we are sure that we want to select column by its name
		// we iterate over the original columnNames and we understand which position we are interested
		boolean columnNameExists = false;
		int i = 0;
		for(String originalColumnName : this.originalColumnsNameList) {
			if(columnNumberOrNameOrSequenceToSelect.equals(originalColumnName)) {
				extractedIndexes.add(i);
				columnNameExists = true;
				break;
			}
			i++;
		}
		if(!columnNameExists) {
			throw new PrometheuxRuntimeException("The column name "+columnNumberOrNameOrSequenceToSelect+" that you want to select is not present in the header."
					+ " Available column names are "+originalColumnsNameList);
		}
		return extractedIndexes;
		
	}

	public String[] getSelectedColumnsExprArr() {
		return selectedColumnsExprArr;
	}
	
}
