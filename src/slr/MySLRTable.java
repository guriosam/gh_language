package slr;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.HashMap;
import java.util.Map;

public class MySLRTable {

	private Map<String, Integer> tableHeader;
	private String[][] tableContent;

	private int numRows;
	private int numColumns;

	public MySLRTable(String path) throws IOException {

		tableHeader = new HashMap<String, Integer>();
		numColumns = 0;

		readTable(path);
	}

	public void readTable(String path) throws IOException {

		File tableCsvFile = getTable(path);

		BufferedReader buffer = new BufferedReader(new FileReader(tableCsvFile));
		String row = buffer.readLine();

		for (String column : row.split(",")) {
			tableHeader.put(column, numColumns);
			numColumns++;
		}

		tableContent = new String[numRows][numColumns];

		int lineCounter = 0;

		row = buffer.readLine();

		while (row != null) {

			int columnCounter = 0;
			StringBuilder element = new StringBuilder();

			for (char c : row.toCharArray()) {
				if (c == ',') {
					tableContent[lineCounter][columnCounter] = element.length() == 0 ? null : element.toString();
					element.setLength(0);
					columnCounter++;
				} else if (c != '\'') {
					element.append(c);
				}
			}

			tableContent[lineCounter][columnCounter] = element.length() == 0 ? null : element.toString();

			lineCounter++;
			row = buffer.readLine();
		}

		buffer.close();

	}

	public File getTable(String path) throws IOException {

		File csvFile = new File(path);
		FileInputStream csvFileStream = new FileInputStream(path);
		LineNumberReader lineNumberReader = new LineNumberReader(new InputStreamReader(csvFileStream));
		lineNumberReader.skip(csvFile.length());

		numRows = lineNumberReader.getLineNumber();

		lineNumberReader.close();

		return csvFile;

	}

	public Map<String, Integer> getTableHeader() {
		return tableHeader;
	}

	public String[][] getTableContent() {
		return tableContent;
	}



}
