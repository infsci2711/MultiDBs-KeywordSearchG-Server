package edu.pitt.sis.infsci2711.multidbskeywordsearchgserverapi.viewModels;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class QueryViewModel {
	private String query;
	
	public String getQuery() {
		return query;
	}
	
	public void setQuery(final String gueryP) {
		query = gueryP;
	}
}
