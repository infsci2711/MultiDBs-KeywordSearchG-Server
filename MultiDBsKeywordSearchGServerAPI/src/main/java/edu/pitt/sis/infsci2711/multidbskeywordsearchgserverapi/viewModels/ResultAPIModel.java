package edu.pitt.sis.infsci2711.multidbskeywordsearchgserverapi.viewModels;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ResultAPIModel {
	private String result;
	private String come_from;

	public ResultAPIModel() {
	}

	public ResultAPIModel(String result, String come_from) {
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
