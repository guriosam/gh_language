package start;

import java.io.FileNotFoundException;

import lexical.LexicalAnalyzer;
import slr.MySLRTable;
import syntatic.Syntactic;

public class GHStart {

	public static void main(String[] args) {
		
		
		String path = "test_codes/test4.gh";
		String pathAction = "files/action.csv";
		String pathTransition = "files/goto.csv";
		String pathGrammar = "files/gh_grammar.txt";

//		String path = args[0];
//		String pathAction = args[1];
//		String pathTransition = args[2];
//		String pathGrammar = args[3];
		
		try {
			
			LexicalAnalyzer lexical = new LexicalAnalyzer(path);
			Syntactic syntaticAnalyzer = new Syntactic(lexical, pathGrammar, pathAction, pathTransition);
			syntaticAnalyzer.analyze();
			
		} catch (FileNotFoundException e) {
			System.out.println("The file that you are trying to open doesn't exists." + "\n" + "Path: " + path);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
