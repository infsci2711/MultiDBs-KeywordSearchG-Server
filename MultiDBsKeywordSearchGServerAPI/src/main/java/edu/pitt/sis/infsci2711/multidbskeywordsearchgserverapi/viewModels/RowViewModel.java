package edu.pitt.sis.infsci2711.multidbskeywordsearchgserverapi.viewModels;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class RowViewModel {
	//@XmlElementWrapper(name="row")
	@XmlElement(name="row")
	String[] row;
	
	public RowViewModel() {
		
	}
	
	public RowViewModel(final String[] dataP) {
		row = dataP;
	}
	
	public String[] getRow() {
		return row;
	}
}