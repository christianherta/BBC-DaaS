package de.bbcdaas.common.beans.mlp;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Robert Illers
 */
public class Cloud {
	
	private int id;
	private List<Term> terms = new ArrayList<Term>();

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public List<Term> getTerms() {
		return terms;
	}

	public void setTerms(List<Term> terms) {
		this.terms = terms;
	}
}
