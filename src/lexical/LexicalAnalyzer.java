package lexical;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class LexicalAnalyzer {

	private List<String> lines;
	private int currentRow, currentCol;
	private Token currentToken;
	private String line;
	private BufferedReader reader;
	private String tokenValue;

	public LexicalAnalyzer(String filePath) throws FileNotFoundException {
		reader = new BufferedReader(new FileReader(filePath));
		currentRow = currentCol = 0;
	}

	public Token nextToken() {

		char current = line.charAt(currentCol);
		tokenValue = "";
		int tokenCol = currentCol;
		int tokenLine = currentRow;

		// Ignoring spaces and tabs
		while (current == ' ' || current == '\t') {
			current = nextChar();
			tokenCol++;
		}

		
		if (checkIfDigitOrId(current)) {
			return setupToken(tokenValue, tokenLine, tokenCol);
		}

		if (checkIfConstantString(current)) {
			return setupToken(tokenValue, tokenLine, tokenCol);
		}

		if (checkCasesSymbols(current)) {
			return setupToken(tokenValue, tokenLine, tokenCol);
		}

		return setupToken(tokenValue, tokenLine, tokenCol);
	}

	private Token setupToken(String tokenValue, int tokenLine, int tokenCol) {
		// previousToken = currentToken;
		Token token = new Token(tokenValue.trim(), tokenLine, tokenCol, analyzeCategory(tokenValue));
		currentToken = token;
		return token;
	}

	private boolean checkCasesSymbols(char current) {

		switch (current) {

		case '<':
			tokenValue += current;
			current = nextChar();
			if (current == '-') {
				tokenValue += current;
				currentCol++;
				return true;
			} else {
				return false;
			}

		case '-':
			tokenValue += current;
			current = nextChar();
			if (current == '>') {
				tokenValue += current;
				currentCol++;
			} else if (current == '-') {
				tokenValue += current;
				currentCol++;
			}
			return true;

		case '+':
			tokenValue += current;
			current = nextChar();
			if (current == '=') {
				tokenValue += current;
				currentCol++;
			} else if (current == '+') {
				tokenValue += current;
				currentCol++;
			}
			tokenValue += current;
			currentCol++;
			return true;

		case '%':
			tokenValue += current;
			current = nextChar();
			return true;
		case '*':
			tokenValue += current;
			current = nextChar();
			return true;
		case '/':
			tokenValue += current;
			current = nextChar();
			if (current == '#') {
				tokenValue += current;
				currentCol++;
			}
			return true;
		case ';':
			tokenValue += current;
			current = nextChar();
			return true;
		case '(':
			tokenValue += current;
			current = nextChar();
			return true;
		case ')':
			tokenValue += current;
			current = nextChar();
			return true;
		case '{':
			tokenValue += current;
			current = nextChar();
			return true;
		case '}':
			tokenValue += current;
			current = nextChar();
			return true;
		}

		return false;

	}

	private boolean checkIfConstantString(char current) {

		if (current == '"') {
			tokenValue += current;
			current = nextChar();

			// Empty String
			if (current == '"') {
				tokenValue += current;
				currentCol++;
			} else {
				// Until \n or "
				while (current != '\n') {
					// Ignoring scape characters
					if (current == '\\') {
						current = nextChar();
					}
					tokenValue += current;
					current = nextChar();
					if (current == '"') {
						tokenValue += current;
						currentCol++;
						break;
					}

				}
			}
		}

		return !tokenValue.isEmpty();
	}

	private boolean checkIfDigitOrId(char current) {
		
		//Int or float
		if (Character.toString(current).matches("\\d")) {
			tokenValue += current;
			current = nextChar();
			while (Character.toString(current).matches("\\d")) {
				tokenValue += current;
				current = nextChar();
			}
			if (current == '.') {
				tokenValue += current;
				current = nextChar();
				while (Character.toString(current).matches("\\d")) {
					tokenValue += current;
					current = nextChar();
				}
			}
		} else {
			//ids
			while (!LexicalTable.symbolList.contains(current)) {
				if (current == '\"') {
					break;
				}

				tokenValue += current;
				current = nextChar();

				if (current == ' ') {
					break;
				}
			}

		}

		return !(tokenValue.equals(""));
	}

	private boolean nextLine() {
		String fileLine = new String();
		try {
			fileLine = reader.readLine();

			if (fileLine != null) {
				line = fileLine;
				return true;
			}

		}  catch (IOException e) {
			e.printStackTrace();
		}

		return false;

	}

	private Tokens analyzeCategory(String tkValue) {

		if (LexicalTable.lexemMap.containsKey(tkValue.trim())) {
			return LexicalTable.lexemMap.get(tkValue.trim());
		} else {
			return isConsOrId(tkValue);
		}

	}

	public boolean hasMoreTokens() {

		//first line
		if (currentRow == 0 && currentCol == 0) {
			nextLine();

			if (line == null) {
				printCodeInfo("");
				return false;
//			} else {
//				printCodeInfo(line);
			}
		}
		
		//if there are more lines..
		
		if (line.substring(currentCol).matches("\\s*")) {
			while (nextLine()) {
				
				currentRow++;
				currentCol = 0;
				//printCodeInfo(line);

				if (!line.matches("\\s*")) {
					return true;
				}
			}
			return false;
		} else {
			return true;
		}
	}

	private void printCodeInfo(String info) {
		info = info.replace('\t', ' ');
		 System.out.format("|%04d|  %s\n", currentRow+1, info.trim());
	}

	private Tokens isConsOrId(String tokenValue) {

		if (tokenValue.matches("\\d+")) {
			return Tokens.tLitInt;
		} else if (tokenValue.matches("(\\d)+\\.(\\d)+")) {
			return Tokens.tLitFloat;
		} else if (tokenValue.startsWith("\"")) {
			if (tokenValue.length() > 1 && tokenValue.endsWith("\"")) {
				return Tokens.tLitString;
			} else {
				// Missing terminating character "
				return Tokens.tErrStrQu;
			}
		} else if (tokenValue.equals("true") || tokenValue.equals("false")) {
			return Tokens.tLitBool;
		} else if (tokenValue.matches("[a-z_A-Z](\\w)*")) {
			return Tokens.id;
		} else if (tokenValue.matches("\\.\\d+")) {
			// Missing number before decimal point;
			return Tokens.tErrMissBfPoint;
		} else if (tokenValue.matches("\\d+\\.")) {
			// Missing number after decimal point;
			return Tokens.tErrMissAfPoint;
		} else if (tokenValue.matches("[a-z_A-Z](.)*")) {
			// Id contains invalid characters;
			return Tokens.tErrIdCtInv;
		} else if (tokenValue.matches("[^a-z_A-Z&|](\\w)*")) {
			// Invalid id starting character
			return Tokens.tErrIdInitInv;
		} else if (tokenValue.matches("[^a-z_A-Z&|](.)*")) {
			// Invalid id
			return Tokens.tErrIdInv;
		}

		return Tokens.tUnknown;
	}

	private Character nextChar() {
		currentCol++;
		if (currentCol < line.length()) {
			return line.charAt(currentCol);
		}

		return '\n';
	}

	public String getLine() {
		return line;
	}

	
}
