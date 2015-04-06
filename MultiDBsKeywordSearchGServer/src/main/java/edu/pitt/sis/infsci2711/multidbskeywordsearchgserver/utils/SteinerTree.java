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
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;
import org.neo4j.tooling.GlobalGraphOperations;


public class SteinerTree {
	
	public Map<Map<List<Node>, List<Relationship>>,Integer> Steiner(List<Node> all,Map<Map<List<Node>, List<Relationship>>,Integer> T,List<Relationship> X,GraphDatabaseService graphDataService ){
		
		
		Map<Map<List<Node>, List<Relationship>>,Integer> steinerTree=new HashMap<>();
		List<String> relation = null;
		int cost=0;
		try(Transaction tx =  graphDataService.beginTx() ){
			Neo4j neo=new Neo4j();
			Set<Node> nodes=new HashSet<>();
			Map<Relationship, Integer> unsorted=new HashMap<>();
			ValueComparator bvc =  new ValueComparator(unsorted);
			TreeMap<Relationship, Integer> sorted=new TreeMap<>(bvc);
			
			if(T.isEmpty()){
			for(int i=0;i<all.size()-1;i++){
				Node start=all.get(i);
				Node end=all.get(i+1);
				Map<Set<Node>, Map<Relationship, Integer>>  temp=neo.FindShortestPath(start,end,X,graphDataService);
				
				Iterator it=temp.entrySet().iterator();
				while(it.hasNext()){
					 Map.Entry pair = (Map.Entry)it.next();
					 nodes.addAll((Set<Node>)pair.getKey());
					 unsorted.putAll((Map)pair.getValue());
				}		
			}
			
			}else{
				List<Node> node=new ArrayList<>();
				List<Relationship> relationship=new ArrayList<>();
				Map<List<Node>, List<Relationship>> g = null;
				Iterator it =T.entrySet().iterator();
				while(it.hasNext()){
					Map.Entry pair=(Entry) it.next();
					g=(Map<List<Node>, List<Relationship>>) pair.getKey();	
				}
				Iterator ite =g.entrySet().iterator();
				while(ite.hasNext()){
					Map.Entry pair=(Entry) ite.next();
					node=(List<Node>) pair.getKey();
					relationship=(List<Relationship>) pair.getValue();
				}
				nodes.addAll(node);
				for(Relationship rel:relationship){
					if(!X.contains(rel)){
						int c=(int)rel.getProperty("cost");
						unsorted.put(rel, c);
					}
				}
				for(Relationship out:X){
					Node[] sd=out.getNodes();
					Map<Set<Node>, Map<Relationship, Integer>>  temp=neo.FindShortestPath(sd[0],sd[1],X,graphDataService);
					if(temp.isEmpty()){
						return null;
					}
					Iterator i=temp.entrySet().iterator();
					while(i.hasNext()){
						 Map.Entry pair = (Map.Entry)i.next();
						 nodes.addAll((Set<Node>)pair.getKey());
						 unsorted.putAll((Map)pair.getValue());
						 
					}		
				}
			}
			sorted.putAll(unsorted);
			
			 Iterator test=sorted.entrySet().iterator();
				Relationship path;
				while(test.hasNext()){
					Map.Entry pair=(Entry) test.next();
					path=(Relationship) pair.getKey();
					
				}
				
			Kruskal spanningT=new Kruskal();
			Map<List<Node>, List<Relationship>> links=spanningT.executeKruskal(sorted, nodes, graphDataService);
			
			links=constructSteriner(links,all,graphDataService);
			
			//calculate total cost
			cost=Cost(links,graphDataService);
			
			if(cost<Integer.MAX_VALUE){
				steinerTree.put(links, cost);
			}
			
			
			tx.success();
			tx.close();
			return steinerTree;
		}
	}
	
	public Map<List<Node>, List<Relationship>> constructSteriner(Map<List<Node>, List<Relationship>> links,List<Node> all,GraphDatabaseService graphDataService){
		
		Map<Node, Integer> count= new HashMap<>();
		Iterator i=links.entrySet().iterator();
		List<Relationship> edges = new ArrayList<>();
		List<Node> nodes = new ArrayList<>();
		while(i.hasNext()){
			Map.Entry pair=(Map.Entry)i.next();
			edges=(List<Relationship>) pair.getValue();
			nodes=(List<Node>) pair.getKey();
		}
		
		for(Relationship r:edges){
			Node[] n=r.getNodes();
			int m=0;
			while(m<2){
				if(count.containsKey(n[m])){
					int c=count.get(n[m])+1;
					count.put(n[m], c);
				}else{
					count.put(n[m], 1);
				}
				m++;
			}	
		}
		
		//delete some nodes
		try(Transaction tx=graphDataService.beginTx()){
			int flag=1;
			while(flag>0){
				flag=0;
				Iterator a=count.entrySet().iterator();
	
				while(a.hasNext()){
					Map.Entry pair=(Entry)a.next();
					if((int)pair.getValue()==1 && !all.contains(pair.getKey())){
						Iterator ite=links.entrySet().iterator();
						while(ite.hasNext()){
							Map.Entry p=(Map.Entry)ite.next();
							List<Relationship> r=(List<Relationship>) p.getValue();
							for(Relationship rel:r){
								Node node1=rel.getStartNode();
								Node node2=rel.getEndNode();
								if(node1.equals(pair.getKey())){
									Node t=node2;
									nodes.remove(node1);
									edges.remove(rel);
									links.clear();
									links.put(nodes, edges);
									count.remove(pair.getKey());
									int con=count.get(t)-1;
									count.put(t, con);
									break;
								}
								if(node2.equals(pair.getKey())){
									Node t=node1;
									nodes.remove(node2);
									edges.remove(rel);
									links.clear();
									links.put(nodes, edges);
									count.remove(pair.getKey());
									int con=count.get(t)-1;
									count.put(t, con);
									break;
								}
							}
						}
						flag++;		
						break;
					}
				}
			}
			tx.success();
			tx.close();
		}
		return links;
	}
	
	public int Cost(Map<List<Node>, List<Relationship>> links,GraphDatabaseService graphDataService){
		
		Iterator itera=links.entrySet().iterator();
		ExecutionEngine engine = new ExecutionEngine(graphDataService);
		ExecutionResult result;
		int cost=0;
		
		try(Transaction tx=graphDataService.beginTx()){
			
			while(itera.hasNext()){
				Map.Entry pair=(Map.Entry)itera.next();
				List<Relationship> rel= (List<Relationship>) pair.getValue();
				for(Relationship relation:rel){
					cost+=(int)relation.getProperty("cost");
				}
			}
			tx.success();
			tx.close();
			return cost;
		}
	}
	
	public Map<Map<List<Node>, List<Relationship>>,Integer> KBestSterinerTree(List<Node> all,int k, GraphDatabaseService graphDataService){
		
		List<Relationship> I=new ArrayList<>();
		List<Relationship> X=new ArrayList<>();
		Map<Map<List<Node>, List<Relationship>>,Integer> A=new HashMap<>();
		//Q(Tree,I,X)
		Transaction tx=graphDataService.beginTx();
		List<Map<Map<Map<List<Node>, List<Relationship>>,Integer>,Map<List<Relationship>,List<Relationship>>>> Ql=new ArrayList<>();
		
		
		Map<Map<List<Node>, List<Relationship>>,Integer> Tree=Steiner(all, A, X, graphDataService);
		
		if(Tree!=null){
			Map<Map<Map<List<Node>, List<Relationship>>,Integer>,Map<List<Relationship>,List<Relationship>>> Q=new HashMap<>();
			Map<List<Relationship>,List<Relationship>> temp=new HashMap<>();
			temp.put(I, X);
			Q.put(Tree, temp);
			Ql.add(Q);
		}
		
		while(!Ql.isEmpty() && k>0){
			k=k-1;
			Map<Map<Map<List<Node>, List<Relationship>>,Integer>,Map<List<Relationship>,List<Relationship>>> que=Ql.get(0);
			Iterator it=que.entrySet().iterator();
			while(it.hasNext()){
				Map.Entry pair=(Entry) it.next();
				Map<List<Relationship>,List<Relationship>> IV=(Map<List<Relationship>, List<Relationship>>) pair.getValue();
				Map<Map<List<Node>, List<Relationship>>,Integer> T=(Map<Map<List<Node>, List<Relationship>>, Integer>) pair.getKey();
				Iterator itera=IV.entrySet().iterator();
				while(itera.hasNext()){
					Map.Entry p=(Entry) itera.next();
					I=(List<Relationship>) p.getKey();
					X=(List<Relationship>) p.getValue();  
				}
                Ql.remove(0);
                
				A.putAll(T);
				Iterator iter=T.entrySet().iterator();
				List<Relationship> E=new ArrayList<>();
				while(iter.hasNext()){
					Map.Entry cp=(Entry) iter.next();
					Map<List<Node>, List<Relationship>> e=(Map<List<Node>, List<Relationship>>) cp.getKey();
					Iterator iterator=e.entrySet().iterator();
				    while(iterator.hasNext()){
				    	Map.Entry pa=(Entry) iterator.next();
				    	E=(List<Relationship>) pa.getValue();
				    }
					
				}
				
				for(Relationship e:E){
					List<Relationship> tempI=new ArrayList<>();
					List<Relationship> tempX=new ArrayList<>();
					Map<Map<List<Node>, List<Relationship>>,Integer> tempTree=new HashMap<>();
					
					tempI.addAll(I);
					for(Relationship ex:E){
						if(!ex.equals(e)){
							tempI.add(ex);
						}
					}
					tempX.addAll(X);
					tempX.add(e);
					tempTree=Steiner(all, Tree, tempX, graphDataService);
					  try{
						if(!tempTree.isEmpty()){
							Map<Map<Map<List<Node>, List<Relationship>>,Integer>,Map<List<Relationship>,List<Relationship>>> Q=new HashMap<>();
							Map<List<Relationship>, List<Relationship>> temp = new HashMap<>();
							temp.put(tempI, tempX);
							Q.put(tempTree, temp);
							Ql.add(Q);
						}
					  }catch(Exception exception){
						  exception.getMessage();
					  }
				}	
			}	
		}
		tx.success();
		tx.close();
		return A;		
	}
}
