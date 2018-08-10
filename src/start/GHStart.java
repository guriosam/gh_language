package start;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import lexical.LexicalAnalyzer;

public class GHStart {

	public static void main(String[] args) {
		
		String path = "test.gh";
		LexicalAnalyzer lexical = new LexicalAnalyzer(path);
		lexical.readFile();
		while(lexical.hasMoreTokens()){
			lexical.nextToken();
		}
	}
	
}
