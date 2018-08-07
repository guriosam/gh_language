package start;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import lexical.LexicalAnalyzer;

public class Main {

public static void main(String[] args) {
		
		String path = "D:/test.gh";
		
		LexicalAnalyzer lexical = new LexicalAnalyzer(path);
		lexical.readFile();
		while(lexical.hasMoreTokens()){
			lexical.nextToken();
		}
	}
	
	public static List<String> filesOnFolder(String path) {

		List<String> fileNames = new ArrayList<String>();

		if (path == null) {
			System.out.println("Folder name is null");
		}

		try {
			// filesNamesPath = path;
			File f = new File(path);

			File[] files = f.listFiles();
			
			if (files != null) {

				for (File file : files) {
					System.out.println(file);
					fileNames.add(file.getName());
				}
			}

		} catch (Exception e) {
			System.out.println(path);
			e.printStackTrace();
		}

		return fileNames;
	}

}
