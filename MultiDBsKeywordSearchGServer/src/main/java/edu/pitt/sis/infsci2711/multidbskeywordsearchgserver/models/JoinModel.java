package edu.pitt.sis.infsci2711.multidbskeywordsearchgserver.models;

import java.util.List;


public class JoinModel {
	
	private List<String> relations;
	private int cost;
	private int rank;

	public JoinModel() {
	}

	public JoinModel(List<String> relations, int cost, int rank) {
		this.setRelations(relations);
		this.setCost(cost);
		this.setRank(rank);		
	}
	
	
	
	public void setRelations(List<String> relations) {
		this.relations = relations;
	}
	
	public void setCost(int cost) {
		this.cost = cost;
	}
	
	public void setRank(int rank) {
		this.rank = rank;
	}
	

	
	public List<String> getRelations() {
		return this.relations;
	}
	
	public int getCost() {
		return this.cost;
	}
	
	public int getRank(){
		return this.rank;
	}
	
}
