package edu.pitt.sis.infsci2711.multidbskeywordsearchgserver.models;

public class ResultModel {
	private String result;
	private String come_from;

	public ResultModel() {
	}

	public ResultModel(String result, String come_from) {
		this.setResult(result);
		this.setComeFrom(come_from);
	}
	
	public void setResult(String result) {
		this.result = result;
	}
	
	public void setComeFrom(String come_from) {
		this.come_from = come_from;
	}

	public String getResult() {
		return this.result;
	}
	
	public String getComeFrom() {
		return this.come_from;
	}
}
