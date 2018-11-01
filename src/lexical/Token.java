package lexical;

public class Token {
	
	private String value;
	private Tokens category;
	private int line;
	private int column;

	
	@Override
	public String toString() {
		return String.format("        [%04d, %04d] (%04d, %10s) {%s}", line + 1 , column, category.ordinal(), category, value);
	}

	public Token(String value, int line, int column, Tokens category) {
		super();
		this.value = value;
		this.category = category;
		this.line = line;
		this.column = column;
	}
	
	public Token(Tokens category) {
		super();
		this.category = category;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Tokens getCategory() {
		return category;
	}

	public void setCategory(Tokens category) {
		this.category = category;
	}

	public int getLine() {
		return line;
	}

	public void setLine(int line) {
		this.line = line;
	}

	public int getColumn() {
		return column;
	}

	public void setColumn(int column) {
		this.column = column;
	}


}
