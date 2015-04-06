package edu.pitt.sis.infsci2711.multidbskeywordsearchgserver.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import org.neo4j.cypher.javacompat.ExecutionEngine;
import org.neo4j.cypher.javacompat.ExecutionResult;
import org.neo4j.graphalgo.GraphAlgoFactory;
import org.neo4j.graphalgo.PathFinder;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Path;
import org.neo4j.graphdb.PathExpanders;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.tooling.GlobalGraphOperations;

import edu.pitt.sis.infsci2711.multidbskeywordsearchgserver.models.ResultModel;

public class join {
	
	public List<Map<Map<List<Node>, List<Relationship>>,Integer>> join(List<ResultModel> resultSet,GraphDatabaseService graphDataService ){

		
		Transaction tx=graphDataService.beginTx();
		
		
		ExecutionEngine engine = new ExecutionEngine(graphDataService);
		ExecutionResult result;
		
		
		Map<List<String>, Integer> unsorted=new HashMap<>();
		ValueComparatorList bvc =  new ValueComparatorList(unsorted);
		TreeMap<List<String>,Integer> T=new TreeMap<>(bvc);
		
		
		List<String> api = new ArrayList<>();
        for(ResultModel model:resultSet){
			String temp=model.getDatabase()+"."+model.getTable();
			api.add(temp);
		}
        
		Set<String> apis=new HashSet<>();  //make sure the unique tables
		for(int i=0;i<api.size();i++){
			apis.add(api.get(i));
		}
		
		
		List<Node> all=new ArrayList<>();
		
		//find all the target nodes
		for(String tab:apis){
			String[] temp=tab.split("\\.");
			
			result = engine
					.execute("MATCH (n) where n.value='"+temp[1]+"' and n.parent='"+temp[0]+"' RETURN n");
			Node node = null;
			 for(Map<String,Object> tem : result){
				 node=(Node) tem.get("n");
				 all.add(node);
				 
			 }
		}
		
		
		int k=7;
		SteinerTree st=new SteinerTree();
		Map<Map<List<Node>, List<Relationship>>,Integer> unsort=st.KBestSterinerTree(all,k, graphDataService);
		
		List<Map<Map<List<Node>, List<Relationship>>,Integer>> sort=new ArrayList<>();
		
		
		
		while(!unsort.isEmpty()){
			int curr=Integer.MAX_VALUE;
			Map<List<Node>, List<Relationship>> currg=new HashMap<>();
			Map<Map<List<Node>, List<Relationship>>,Integer> tmp=new HashMap<>();
			Iterator i=unsort.entrySet().iterator();
			while(i.hasNext() ){
				Map.Entry pair=(Entry) i.next();
				Map<List<Node>, List<Relationship>> g=(Map<List<Node>, List<Relationship>>) pair.getKey();
				int cost=(int) pair.getValue();
				if(curr>cost){
					curr=cost;
					currg=g;
				}	
			}
			
			tmp.put(currg, curr);
			unsort.remove(currg);
			sort.add(tmp);
		}
		
		
		tx.success();
		
		return sort;		
		
		
		
			
	}
}
