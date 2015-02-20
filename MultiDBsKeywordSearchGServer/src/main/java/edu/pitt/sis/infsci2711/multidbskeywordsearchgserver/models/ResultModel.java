package edu.pitt.sis.infsci2711.multidbskeywordsearchgserver.models;

public class ResultModel {
	private String record="";
	private String column="";
	private String table="";
	private String database="";


	public ResultModel() {
	}

	public ResultModel(String record, String column, String table, String database) {
		this.setRecord(record);
		this.setColumn(column);
		this.setTable(table);
		this.setDatabase(database);

	}
	
	public void setRecord(String record) {
		this.record = record;
	}
	
	public void setColumn(String column) {
		this.column = column;
	}
	
	public void setTable(String table) {
		this.table = table;
	}
	
	public void setDatabase(String database) {
		this.database = database;
	}

	public String getRecord() {
		return this.record;
	}
	
	public String getColumn() {
		return this.column;
	}
	
	public String getTable() {
		return this.table;
	}

	public String getDatabase() {
		return this.database;
	}
}
