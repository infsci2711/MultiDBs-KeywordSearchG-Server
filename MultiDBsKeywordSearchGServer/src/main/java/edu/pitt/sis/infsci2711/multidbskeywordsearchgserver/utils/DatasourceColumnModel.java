package edu.pitt.sis.infsci2711.multidbskeywordsearchgserver.utils;

public class DatasourceColumnModel {
	private String columnName;
	
	public DatasourceColumnModel() {
		
	}
	
	public DatasourceColumnModel(String columnName) {
		this.setColumnName(columnName);
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
}
