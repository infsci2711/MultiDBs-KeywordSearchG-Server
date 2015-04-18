package edu.pitt.sis.infsci2711.multidbskeywordsearchgserver.models;

import java.util.List;
import java.util.Map;

import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;

public class ResultModel {
	private String record="";
	private String column="";
	private String table="";
	private String database="";
	private int ID=0;
	private String keyword="";	


	public ResultModel() {
	}

	public ResultModel(String record, String column, String table, String database,int ID,String keyword) {
		this.setRecord(record);
		this.setColumn(column);
		this.setTable(table);
		this.setDatabase(database);
		this.setDbId(ID);
		this.setKeyword(keyword);

	}
	
	public void setDbId(int ID){
		this.ID = ID;
	}
	
	public void setKeyword(String keyword){
		this.keyword=keyword;
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
	
	public int getDbID(){
		return this.ID;
	}
	
	public String getKeyword(){
		return this.keyword;
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
