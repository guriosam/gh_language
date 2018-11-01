package slr;

public class Term {

	private int state;
	private String element;
	private boolean isToken;

	public Term(int state) {
		this.state = state;
	}

	public Term(int state, String element, boolean isToken) {
		this.state = state;
		this.element = element;
		this.isToken = isToken;
	}

	@Override
	public String toString() {
		return "(state: " + state + ", element: " + element + ", isToken: " + isToken + ")";
	}

	public int getState() {
		return state;
	}

	public String getElement() {
		return element;
	}

	public boolean isToken() {
		return isToken;
	}
	
	
}