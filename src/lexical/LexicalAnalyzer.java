package lexical;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

	public List<Token> nextToken() {

		tkLine = currentLine;

		for(Character c : LexicalTable.symbolList){
			organizeSymbols(c);
		}
		
		System.out.println(line);

		List<String> lineWords = new ArrayList<String>();

		//Split por espaços ou tabs, menos se estiver entre aspas
		Pattern regex = Pattern.compile("[^\\s\"']+|\"[^\"]*\"|'[^']*'");
		Matcher regexMatcher = regex.matcher(line);
		
		while (regexMatcher.find()) {
			lineWords.add(regexMatcher.group());
		} 
		
		int count = 1;

		List<Token> tokens = new ArrayList<>();

		for (String word : lineWords) {

			cCol += word.length() + 1;
			if (cCol > line.length()) {
				cCol = line.length() - 1;
			}
			tkCol = count * word.length();

			Tokens category = analyzeCategory(word);

			Token token = new Token();
			token.setValue(word);
			token.setLine(tkLine);
			token.setColumn(tkCol);
			token.setCategory(category);
			count++;

			if (token.getCategory().equals(Tokens.cmt)) {
				if (hasMoreTokens()) {
					return nextToken();
				}
			}
			
			tokens.add(token);
		}

		return tokens;

	}

	private void organizeSymbols(Character c) {
		
		if(c == '.' || c == ' '){
			return;
		}
		
		line = line.replace(" " + c, c + "");
		line = line.replace(c + "", " " + c);
		
		line = line.replace(c + " ", c + "");
		line = line.replace(c + "", c + " ");

		if(c == '-'){
			line = line.replace("< -", "<-");
			line = line.replace("- >", "->");
		}
		
		if(c == '/'){
			line = line.replace("/ #", "/# ");
		}
		
		
	}

	private Tokens analyzeCategory(String tkValue) {

		if (LexicalTable.lexemMap.containsKey(tkValue)) {
			return LexicalTable.lexemMap.get(tkValue);
		} else if (isNumber(tkValue)) {
			return checkTypeNumber(tkValue);
		} else if (isIdentifier(tkValue)) {
			return Tokens.id;
		} else if (isString(tkValue)) {
			return Tokens.tLitString;
		}

		return Tokens.tUnknown;
	}

	private boolean isString(String tkValue) {
		if (tkValue.startsWith("\"")) {
			if (tkValue.endsWith("\"")) {
				return true;
			}
		}
		return false;
	}

	private Tokens checkTypeNumber(String tkValue) {

		if (tkValue.contains(".")) {
			return Tokens.tLitFloat;
		}

		return Tokens.tLitInt;
	}

	private boolean isNumber(String tkValue) {
		if (tkValue.matches("[0-9]")) {
			return true;
		}
		return false;
	}

	public void readFile() {

		BufferedReader reader = null;
		try {

			File f = new File(filePath);

			if (!f.exists()) {
				System.out.println(filePath + " not exists");
				return;
			}

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
				currentLine++;
				cCol = 0;
				return true;
			}
		}

		return false;

	}

	private boolean isIdentifier(String tkValue) {

		if (tkValue.matches("[_a-zA-Z][_a-zA-Z0-9]*")) {
			if (tkValue.length() < 16) {
				return true;
			} else {
				printError("Identificador muito longo.", tkValue);
			}

		} else if (tkValue.matches("[^_a-zA-Z\"'].*")) {
			printError("Identificador não iniciado com letra ou '_'.", tkValue);
		} else if (tkValue.matches("[_a-zA-Z].*")) {
			printError("Identificador contém caracter inválido.", tkValue);

		}
		return false;
	}

	private void printError(String string, String token) {
		System.err.println("Erro na <linha, coluna> " + "= <" + currentLine + "," + cCol + ">. " + "'" + token + "'"
				+ " " + string);
		System.exit(1);
	}
}
