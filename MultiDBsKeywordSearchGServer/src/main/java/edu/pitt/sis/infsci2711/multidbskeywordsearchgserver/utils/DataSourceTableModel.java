package edu.pitt.sis.infsci2711.multidbskeywordsearchgserver.utils;

import java.util.List;

public class DataSourceTableModel {
	private String tableName;
	
	private List<DatasourceColumnModel> columns;
	
	public DataSourceTableModel() {
		
	}
	
	public DataSourceTableModel(String tableName) {
		this.setTableName(tableName);
	}
	
	public DataSourceTableModel(String tableName, List<DatasourceColumnModel> columns) {
		this.setTableName(tableName);
		this.setColumns(columns);
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public List<DatasourceColumnModel> getColumns() {
		return columns;
	}

	public void setColumns(List<DatasourceColumnModel> columns) {
		this.columns = columns;
	}
}
