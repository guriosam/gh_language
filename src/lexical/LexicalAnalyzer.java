package lexical;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LexicalAnalyzer {

	private List<String> lines;
	private int currentLine, cCol, tkCol, tkLine;
	private String line;
	private String filePath;

	private final char LINE_BREAK = '\n';

	public LexicalAnalyzer(String filePath) {
		lines = new ArrayList<>();
		this.filePath = filePath;
		this.tkCol = 0;
		this.tkLine = 0;
	}

	public Token nextToken() {

		Token token;

		char currentChar;
		String tkValue = "";

		tkCol = cCol;
		tkLine = currentLine;

		currentChar = line.charAt(cCol);

		while (currentChar == ' ' || currentChar == '\t') {
			currentChar = nextChar();
			tkCol++;
		}

		if (Character.toString(currentChar).matches("\\d")) {
			tkValue += currentChar;
			currentChar = nextChar();
			while (Character.toString(currentChar).matches("\\d")) {
				tkValue += currentChar;
				currentChar = nextChar();
			}
			if (currentChar == '.') {
				tkValue += currentChar;
				currentChar = nextChar();
				while (Character.toString(currentChar).matches("\\d")) {
					tkValue += currentChar;
					currentChar = nextChar();
				}
			}

			if (currentChar != ' ') {
				while (!LexicalTable.symbolList.contains(currentChar)) {
					tkValue += currentChar;
					currentChar = nextChar();
					if (currentChar == LINE_BREAK) {
						break;
					}
				}
			}
		} else {

			while (!LexicalTable.symbolList.contains(currentChar)) {
				tkValue += currentChar;
				currentChar = nextChar();
				if (currentChar == LINE_BREAK) {
					break;
				}
			}
		}

		if (tkValue == "") {

			switch (currentChar) {

			case '"':

				tkValue += currentChar;
				currentChar = nextChar();

				if (currentChar == '"') {
					tkValue += currentChar;
					cCol++;
					break;
				}

				while (currentChar != LINE_BREAK) {
					tkValue += currentChar;
					currentChar = nextChar();

					if (currentChar == '"') {
						tkValue += currentChar;
						cCol++;
						break;
					}
				}
				break;

			case '/':
				tkValue += currentChar;
				currentChar = nextChar();

				if (currentChar == '#') {
					tkValue += currentChar;
					currentLine++;
					cCol = 0;
				}
				break;

			case '#':
				tkValue += currentChar;
				currentChar = nextChar();
				if (currentChar == '/') {
					tkValue += currentChar;
					currentChar = nextChar();
				}
				break;

			case '<':
			case '>':
			case '~':
			case '=': // Compondo um token que pode ser <=, >=, ~= ou ==

				tkValue += currentChar;
				currentChar = nextChar();
				if (currentChar == '=') {
					tkValue += currentChar;
					cCol++;
				}
				break;

			case '+': // Compondo um token que pode ser operador aditivo, de
				tkValue += currentChar;
				currentChar = nextChar();

				if (currentChar == '+') {
					tkValue += currentChar;
					currentChar = nextChar();
				}

				break;

			default:
				tkValue += currentChar;
				cCol++;
				break;
			}
		}

		tkValue = tkValue.trim();

		token = new Token();

		token.setValue(tkValue);
		token.setLine(tkLine);
		token.setColumn(tkCol);
		token.setCategory(analyzeCategory(tkValue));

		if (token.getCategory().equals(Tokens.cmt)) {
			if (hasMoreTokens()) {
				return nextToken();
			}
		}
		return token;

	}

	private Tokens analyzeCategory(String tkValue) {

		if (isOpNegUnary(tkValue)) {
			return Tokens.opNegUn;
		} else if (LexicalTable.lexemMap.containsKey(tkValue)) {
			return LexicalTable.lexemMap.get(tkValue);
		} else if (isIdentifier(tkValue)) {
			return Tokens.id;
		}

		return Tokens.tUnknown;
	}

	public void readFile() {

		BufferedReader reader = null;
		try {

			File f = new File(filePath);

			if (!f.exists()) {
				System.out.println(filePath + " not exists");
				return;
			}
			
			System.out.println(f.getAbsolutePath());

			reader = new BufferedReader(new FileReader(filePath));

			// read file line by line
			String line = reader.readLine();

			while (line != null) {
				lines.add(line);
				line = reader.readLine();
			}

			reader.close();

		} catch (Exception e) {
			e.printStackTrace();
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();

				}
			}
		}

	}

	public boolean hasMoreTokens() {

		if (!lines.isEmpty()) {
			if (currentLine < lines.size()) {

				line = lines.get(currentLine);
				line = line.replace('\t', ' ');

				if (line.substring(cCol).matches("\\s*")) {
					currentLine++;
					cCol = 0;
					while (currentLine < lines.size()) {
						line = lines.get(currentLine);
						if (line.matches("\\s*")) {
							currentLine++;
						} else {
							return true;
						}
					}
				} else if (cCol < line.length()) {
					return true;
				} else {
					currentLine++;
					cCol = 0;
					while (currentLine < lines.size()) {
						line = lines.get(currentLine);
						if (line.matches("\\s*")) {
							currentLine++;
						} else {
							return true;
						}
					}
				}
			}
		}

		return false;

	}

	private Character nextChar() {

		cCol++;

		if (cCol < line.length()) {
			return line.charAt(cCol);
		} else {
			return LINE_BREAK;
		}

	}

	private Character previousNotBlankChar() {

		int previousColumn = tkCol - 1;
		char previousChar;

		while (previousColumn >= 0) {
			previousChar = line.charAt(previousColumn);
			if (previousChar != ' ' && previousChar != '\t') {
				return previousChar;
			}
			previousColumn--;
		}
		return null;

	}

	private boolean isOpNegUnary(String tkValue) {
		if (tkValue.equals("-")) { 
			Character previousChar = previousNotBlankChar();
			if ((previousChar != null) && Character.toString(previousChar).matches("[_a-zA-Z0-9]")) {
				return false;
			} else {
				return true;
			}
		}
		return false;
	}

	private boolean isChar(String tkValue) {
		if (tkValue.matches("'(.?)'")) {
			return true;
		} else if (tkValue.startsWith("'")) {
			printError("caracter não fechado corretamente com '.", tkValue);
		}
		return false;
	}

	private boolean isIdentifier(String tkValue) {

		if (tkValue.matches("[_a-zA-Z][_a-zA-Z0-9]*")) {
			if (tkValue.length() < 16) {
				return true;
			} else {
				printError("identificador muito longo.", tkValue);
			}

		} else if (tkValue.matches("[^_a-zA-Z\"'].*")) {
			printError("identificador não iniciado com letra ou '_'.", tkValue);
		} else if (tkValue.matches("[_a-zA-Z].*")) {
			printError("identificador contém caracter inválido.", tkValue);

		}
		return false;
	}

	private void printError(String string, String token) {
		System.err.println("Erro na <linha, coluna> " + "= <" + currentLine + "," + cCol + ">. " + "'" + token + "'"
				+ " " + string);
		System.exit(1);
	}
}
