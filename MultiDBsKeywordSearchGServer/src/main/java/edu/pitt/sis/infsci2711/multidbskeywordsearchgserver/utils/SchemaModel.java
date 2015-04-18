package edu.pitt.sis.infsci2711.multidbskeywordsearchgserver.utils;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;


public class SchemaModel {
	//@XmlElementWrapper(name="columnNames")
		@XmlElement(name="columnNames")
		String[] columnNames;
		
		public SchemaModel() {
			
		}
		
		public SchemaModel(final String[] columnNamesP) {
			columnNames = columnNamesP;
		}
		
		public String[] getColumnNames() {
			return columnNames;
		}

}
