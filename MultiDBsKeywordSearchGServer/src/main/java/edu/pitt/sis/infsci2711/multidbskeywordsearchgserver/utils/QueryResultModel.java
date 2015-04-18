package edu.pitt.sis.infsci2711.multidbskeywordsearchgserver.utils;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;



public class QueryResultModel {
	@XmlElement(name="schema")
	SchemaModel schema;
	
	//@XmlElementWrapper(name="data")
	@XmlElement(name="data")
	RowModel[] data;
	
	public QueryResultModel() {
		
	}
	
	public QueryResultModel(final SchemaModel schemaP, final RowModel[] dataP) {
		schema = schemaP;
		data = dataP;
	}
	
	public SchemaModel getSchema() {
		return schema;
	}
	
	public RowModel[] getData() {
		return data;
	}

}
