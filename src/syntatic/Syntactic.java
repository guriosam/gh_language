package syntatic;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Stack;

import lexical.LexicalAnalyzer;
import lexical.Token;
import slr.MySLRGrammar;
import slr.MySLRTable;
import slr.Production;
import slr.Term;

public class Syntactic {

	private LexicalAnalyzer lexical;
	private Token token;

	private MySLRGrammar grammar;
	private MySLRTable actionTable, transitionTable;
	private Stack<Term> stack;
	private HashMap<Integer, Boolean> flags;

	public Syntactic(LexicalAnalyzer lexical, String pathGrammar, String pathAction, String pathTransition)
			throws IOException {
		this.lexical = lexical;
		this.actionTable = new MySLRTable(pathAction);
		this.transitionTable = new MySLRTable(pathTransition);
		this.grammar = new MySLRGrammar(pathGrammar);
		this.stack = new Stack();
		this.flags = new HashMap<>();
	}

	@SuppressWarnings("unused")
	public boolean analyze() throws FileNotFoundException {

		stack.push(new Term(0));
		int state, tokenColumn;
		String action = "";

		if (lexical.hasMoreTokens()) {
			token = lexical.nextToken();
		}

		if (token == null) {
			System.err.println("Error - Empty file.");
			return false;
		}

		while (!action.equals("acc")) {
			state = stack.peek().getState();

			tokenColumn = actionTable.getTableHeader().get(token == null ? "$" : token.getCategory().toString());

			action = actionTable.getTableContent()[state][tokenColumn];

			if (action == null) {
				if (token == null) {
					System.err.println("Code not recognized.");
				} else {
					System.err.printf("Unexpected error at [Row %d, Column %d]: (%s, '%s')", token.getLine(),
							token.getColumn(), token.getCategory().toString(), token.getValue());
				}
				return false;

			}

			switch (action.charAt(0)) {

			case 's':
				stack.push(new Term(Integer.valueOf(action.substring(1)),
						token == null ? "$" : token.getCategory().toString(), true));

				if (!flags.containsKey(token.getLine())) {
					flags.put(token.getLine(), false);
				}

				if (!flags.get(token.getLine())) {
					System.out.format("|%04d|  %s\n", token.getLine() + 1, lexical.getLine().trim());
					flags.put(token.getLine(), true);
				}

				System.out.println(token);
				if (lexical.hasMoreTokens()) {

					token = lexical.nextToken();

					if (token == null) {
						return false;
					}

				} else {
					token = null;
				}

				break;
			case 'r':

				int prod = Integer.valueOf(action.substring(1));
				Production production = grammar.getProductions().get(prod);
				System.out.println(production.toString());

				for (int i = production.getSize(); i > 0; i--) {
					stack.pop();
				}

				tokenColumn = transitionTable.getTableHeader().get(production.getHead());
				state = Integer.valueOf(transitionTable.getTableContent()[stack.peek().getState()][tokenColumn]);
				stack.push(new Term(state, production.getHead(), false));

				break;

			}

		}

		System.out.println("---------- Code Accepted ----------");
		return true;
	}

}
