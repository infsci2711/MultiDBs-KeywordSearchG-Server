package edu.pitt.sis.infsci2711.multidbskeywordsearchgserverapi.viewModels;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement
public class QueryResultViewModel {

	@XmlElement(name="schema")
	SchemaViewModel schema;
	
	//@XmlElementWrapper(name="data")
	@XmlElement(name="data")
	RowViewModel[] data;
	
	public QueryResultViewModel() {
		
	}
	
	public QueryResultViewModel(final SchemaViewModel schemaP, final RowViewModel[] dataP) {
		schema = schemaP;
		data = dataP;
	}
	
	public SchemaViewModel getSchema() {
		return schema;
	}
	
	public RowViewModel[] getData() {
		return data;
	}
}
