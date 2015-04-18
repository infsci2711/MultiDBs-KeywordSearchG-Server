package edu.pitt.sis.infsci2711.multidbskeywordsearchgserver.utils;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

public class RowModel {
	//@XmlElementWrapper(name="row")
		@XmlElement(name="row")
		String[] row;
		
		public RowModel() {
			
		}
		
		public RowModel(final String[] dataP) {
			row = dataP;
		}
		
		public String[] getRow() {
			return row;
		}

}
