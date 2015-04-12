package edu.pitt.sis.infsci2711.multidbskeywordsearchgserver.models;

import java.util.List;
import java.util.Map;

public class AllModel {
	
	private List<ResultModel> searchResult;
	private List<JoinModel> join;
	
	public AllModel(){
		
	}
	
	public AllModel( List<ResultModel> searchResult, List<JoinModel> join){
		this.searchResult=searchResult;
		this.join=join;
	}
	
	public List<ResultModel> getSearchResult(){
		return this.searchResult;
	}
	
	public List<JoinModel> getJoinResult(){
		return this.join;
	}

}
