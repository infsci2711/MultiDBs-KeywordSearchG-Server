package edu.pitt.sis.infsci2711.multidbskeywordsearchgserverapi.viewModels;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class SchemaViewModel {
	//@XmlElementWrapper(name="columnNames")
	@XmlElement(name="columnNames")
	String[] columnNames;
	
	public SchemaViewModel() {
		
	}
	
	public SchemaViewModel(final String[] columnNamesP) {
		columnNames = columnNamesP;
	}
	
	public String[] getColumnNames() {
		return columnNames;
	}
}
