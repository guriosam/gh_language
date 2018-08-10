package lexical;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LexicalTable {

	public static HashMap<String, Tokens> lexemMap;
	public static HashMap<String, Tokens> delimitadors;
	public static List<Character> symbolList;

	static {
		
		symbolList = new ArrayList<>();
		lexemMap = new HashMap<>();

		lexemMap.put("+", Tokens.opAritAd);
		lexemMap.put("-", Tokens.opAritAd);
		lexemMap.put("*", Tokens.opAritMul);
		lexemMap.put("/", Tokens.opAritMul);
		lexemMap.put("%", Tokens.opAritMul);
		lexemMap.put("+=", Tokens.opConc);

		lexemMap.put("greater", Tokens.opRel1);
		lexemMap.put("less", Tokens.opRel1);
		lexemMap.put("greater-equal", Tokens.opRel2);
		lexemMap.put("less-equal", Tokens.opRel2);
		lexemMap.put("is", Tokens.opRel3);
		lexemMap.put("isnot", Tokens.opRel3);

		lexemMap.put("<-", Tokens.opAtrib);
		lexemMap.put("->", Tokens.opAccess);
		lexemMap.put("++", Tokens.opIncre);
		lexemMap.put("--", Tokens.opIncre);

		lexemMap.put("(", Tokens.paramStart);
		lexemMap.put(")", Tokens.paramEnd);
		lexemMap.put("{", Tokens.escStart);
		lexemMap.put("}", Tokens.escEnd);
		lexemMap.put("/#", Tokens.cmt);

		// Terminador

		lexemMap.put(";", Tokens.term);

		// Separador

		lexemMap.put(".", Tokens.point);
		lexemMap.put("in", Tokens.in);

		// Palavras-reservadas (comandos e operadores)

		lexemMap.put("void", Tokens.tVoid);
		lexemMap.put("int", Tokens.tInt);
		lexemMap.put("ints", Tokens.tInts);
		lexemMap.put("float", Tokens.tFloat);
		lexemMap.put("floats", Tokens.tFloats);
		lexemMap.put("string", Tokens.tString);
		lexemMap.put("strings", Tokens.tStrings);
		lexemMap.put("bool", Tokens.tBool);
		lexemMap.put("bools", Tokens.tBools);
		lexemMap.put("commit", Tokens.tCommit);
		lexemMap.put("commits", Tokens.tCommits);
		lexemMap.put("user", Tokens.tUser);
		lexemMap.put("user", Tokens.tUsers);
		lexemMap.put("repository", Tokens.tString);
		lexemMap.put("repositories", Tokens.tString);
		lexemMap.put("file", Tokens.tFile);
		lexemMap.put("files", Tokens.tFiles);
		lexemMap.put("issue", Tokens.tIssue);
		lexemMap.put("issues", Tokens.tIssues);
		lexemMap.put("pullrequest", Tokens.tPullRequest);
		lexemMap.put("pullrequests", Tokens.tPullRequests);
		lexemMap.put("comment", Tokens.tComment);
		lexemMap.put("comments", Tokens.tComments);
		lexemMap.put("date", Tokens.tDate);
		lexemMap.put("dates", Tokens.tDates);
		
		//funções
		
		lexemMap.put("add", Tokens.fAdd);
		lexemMap.put("remove", Tokens.fRemove);
		lexemMap.put("sort", Tokens.fSort);
		lexemMap.put("upper", Tokens.fUpper);
		lexemMap.put("lower", Tokens.fLower);
		lexemMap.put("collect_data", Tokens.fCollectData);
		lexemMap.put("refresh", Tokens.fRefresh);
		lexemMap.put("diff_dates", Tokens.fDiffDates);
		lexemMap.put("length", Tokens.fLength);
		lexemMap.put("compare", Tokens.fCompare);
		

		lexemMap.put("main", Tokens.main);

		lexemMap.put("if", Tokens.prIf);
		lexemMap.put("else", Tokens.prElse);
		lexemMap.put("while", Tokens.prWhile);
		lexemMap.put("for", Tokens.prFor);

		lexemMap.put("not", Tokens.opLogicNeg);
		lexemMap.put("and", Tokens.opLogicAnd);
		lexemMap.put("or", Tokens.opLogicOr);

		lexemMap.put("print", Tokens.prWrite);
		lexemMap.put("read", Tokens.prRead);
		lexemMap.put("return", Tokens.prReturn);

		// S�mbolos que podem indicador pr�ximo token
		symbolList.add(' ');
		symbolList.add('.');
		symbolList.add(';');
		symbolList.add('+');
		symbolList.add('-');
		symbolList.add('*');
		symbolList.add('/');
		symbolList.add('%');
		symbolList.add('(');
		symbolList.add(')');
		symbolList.add('{');
		symbolList.add('}');


	}

}
