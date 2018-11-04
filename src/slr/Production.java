package slr;

public class Production {

	private String head;
	private String[] body;

	public Production(String[] production) {
		// String[] aux = production.split("->");
		// S -> Ft main ( Pdl ) Body S
		// Getting the left side of the production
		// head = S
		head = production[0].trim();
		// body = Ft main ( Pdl ) Body S
		body = production[1].trim().split(" ");
	}

	@Override
	public String toString() {
		String ret = "        " + head + " -> ";
		for (String nt : body) {
			ret += " " + nt;
		}
		return ret;
	}

	public int getSize() {
		return body.length;
	}

	public String getHead() {
		return head;
	}

	public String[] getBody() {
		return body;
	}

}
