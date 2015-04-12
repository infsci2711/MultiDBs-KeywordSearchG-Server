package edu.pitt.sis.infsci2711.multidbskeywordsearchgserverapi.viewModels;

import java.util.List;

import edu.pitt.sis.infsci2711.multidbskeywordsearchgserver.models.JoinModel;
import edu.pitt.sis.infsci2711.multidbskeywordsearchgserver.models.ResultModel;



public class AllAPIModel {
	public List<ResultAPIModel> searchResult;
	public List<JoinAPIModel> join;
	
	public AllAPIModel(){
		
	}
	
	public AllAPIModel(List<ResultAPIModel> searchResult, List<JoinAPIModel> join){
		this.searchResult=searchResult;
		this.join=join;
	}
	
	public List<ResultAPIModel> getSearchResult(){
		return this.searchResult;
	}
	
	public List<JoinAPIModel> getJoinResult(){
		return this.join;
	}

	public String toString(){
		String str = this.searchResult.toString() + this.join.toString();
		return str;
	}

}
