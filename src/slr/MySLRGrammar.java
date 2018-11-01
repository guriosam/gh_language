package slr;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MySLRGrammar {

	private List<Production> productions;

	public MySLRGrammar(String filepath) throws IOException {

		productions = new ArrayList<Production>();

		readGrammar(filepath);

	}

	private void readGrammar(String filepath) throws IOException {
		BufferedReader buffer = new BufferedReader(new FileReader(filepath));

		String line = buffer.readLine();
		while (line != null) {
			if (!line.equals("")) {
				productions.add(new Production(line.split("->")));
			}
			line = buffer.readLine();
		}

		buffer.close();
	}

	public List<Production> getProductions() {
		return productions;
	}

}
