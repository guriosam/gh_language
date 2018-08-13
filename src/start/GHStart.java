package start;

import java.io.FileNotFoundException;

import lexical.LexicalAnalyzer;
import lexical.Token;

public class GHStart {

	public static void main(String[] args) {
		
		String path = args[0];
		//String path = "test_codes/test3.gh";
		try {
			LexicalAnalyzer lexical = new LexicalAnalyzer(path);
			while(lexical.hasMoreTokens()) {
				Token token = lexical.nextToken();
			}
			
		} catch (FileNotFoundException e) {
			System.out.println("The file that you are trying to open doesn't exists." + "\n" + "Path: " + path);
		}
	}
	
}
