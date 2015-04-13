package edu.pitt.sis.infsci2711.multidbskeywordsearchgserver.dao;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.neo4j.cypher.javacompat.ExecutionEngine;
import org.neo4j.cypher.javacompat.ExecutionResult;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Path;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.helpers.collection.IteratorUtil;

import edu.pitt.sis.infsci2711.multidbskeywordsearchgserver.models.AllModel;
import edu.pitt.sis.infsci2711.multidbskeywordsearchgserver.models.JoinModel;
import edu.pitt.sis.infsci2711.multidbskeywordsearchgserver.models.ResultModel;
import edu.pitt.sis.infsci2711.multidbskeywordsearchgserver.utils.Config;
import edu.pitt.sis.infsci2711.multidbskeywordsearchgserver.utils.Neo4j;
import edu.pitt.sis.infsci2711.multidbskeywordsearchgserver.utils.join;

public class KeywordSearchDAO {
	private static final String DB_PATH = new Config().neo4j_path;
	String resultString;
	String columnsString;
	String nodeResult;
	String rows = "";
	private static GraphDatabaseService db = new GraphDatabaseFactory().newEmbeddedDatabase(DB_PATH);
   // static Neo4j neo=new Neo4j();

	public static void main(String[] args) {
		KeywordSearchDAO javaQuery = new KeywordSearchDAO();
		//System.out.println(javaQuery.search("chun").toString());
	}

	public static List<ResultModel> search(String str) {
		

		ExecutionEngine engine = new ExecutionEngine(db);
		ExecutionResult result;
		List<ResultModel> resultSet = new ArrayList<ResultModel>();
		String[] terms =str.split("\\s+");
		

		try (Transaction ignored = db.beginTx()) {

			for(String term:terms){
			result = engine
					.execute("MATCH p=(a)-[:`BELONG_TO`*0..]->(b) Where a.value=~\"(?i).*"+term+".*\" and b.type='database'  RETURN p");
			// resultString = result.dumpToString();

			// System.out.println(resultString);


			for (Map<String, Object> row : result) {

				String record = "";
				String column = "";
				String table = "";
				String database = "";

				Path p = (Path) row.get("p");
				Iterator<Node> path_it = p.nodes().iterator();
				while (path_it.hasNext()) {
					Node tmp = path_it.next();
					switch (tmp.getProperty("type").toString()) {
					case "record":
						record = tmp.getProperty("value").toString();
						break;
					case "column":
						column = tmp.getProperty("value").toString();
						break;
					case "table":
						table = tmp.getProperty("value").toString();
						break;
					case "database":
						database = tmp.getProperty("value").toString();
						break;
					default:
						break;
					}
				}
				resultSet.add(new ResultModel(record, column, table, database,term));
			}
		  }
			ignored.success();
			//ignored.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
		//sorting
		//resultSet=sort(resultSet,str);
		//joinResult(resultSet,db);
		
		
		return resultSet;
	}
	
	public static AllModel joinResult(List<ResultModel> resultSet){
		join join= new join();
		ExecutionEngine engine = new ExecutionEngine(db);
		ExecutionResult result;
		List<JoinModel> joinResult =new ArrayList<>();
		Neo4j neo=new Neo4j();
		AllModel allModel = new AllModel() ;
		
		//get all the search tables
		List<String> api = new ArrayList<>();
        for(ResultModel model:resultSet){
			String temp=model.getDatabase()+"."+model.getTable();
			if(!model.getTable().isEmpty()){
				api.add(temp);
			}	
		}
        
        //System.out.println(api.get(0).toString());
        
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
		System.out.println("---------Pls Print Out ALL-----------");
		System.out.println(all);
		
		//Cluster
		Map<Integer, List<Node>> map = new HashMap<>();
		List<Relationship> X = new ArrayList<>();
		List<Node> t1 = new ArrayList<>();
		
			t1.add(all.get(0));
			map.put(0, t1);
		System.out.println(map+"-----------------111111");
		
		int j=0;
		
		for(int i=1;i<all.size();i++){
			Boolean flag=false;
			Iterator it = map.entrySet().iterator();
			while(it.hasNext()){
				Map.Entry pair = (Entry) it.next();
				int num = (int) pair.getKey();
				List<Node> cluster = (List<Node>) pair.getValue();
				System.out.println("2");
				System.out.println(cluster.get(0));
				System.out.println(all.get(i));
				Map<Set<Node>, Map<Relationship, Integer>>  temp=neo.FindShortestPath(cluster.get(0),all.get(i),X,db);
				System.out.println(temp+" temp is here-------------");
				if(!temp.isEmpty()){
					flag=true;
					System.out.println(flag+"--------1");
					List<Node> tmp1 = new ArrayList<>();
					tmp1.addAll(cluster);
					tmp1.add(all.get(i));
					System.out.println("4");
					map.put(num, tmp1);
					System.out.println("---------flag=1---------"+map);
					break;
				}
			}
			System.out.println("------New Cluster");
			if(flag==false){
				System.out.println("3");
				j++;
				List<Node> tmp2 = new ArrayList<>();
				tmp2.add(all.get(i));	
				System.out.println("5");
				map.put(j, tmp2);
				System.out.println("---------flag=2---------"+map);
			}
			
		}
		
		
		System.out.println("Hello,,,,,,,Cluster is HERE!!!!!----------");
		System.out.println(map);
		
		Iterator iterator = map.entrySet().iterator();
		while(iterator.hasNext()){
			Map.Entry paire = (Entry) iterator.next();
			List<Node> cluster = (List<Node>) paire.getValue();
			
			if(cluster.size()>1){
				List<Map<Map<List<Node>, List<Relationship>>,Integer>> sort =join.join(cluster, db);
				try(Transaction tx=db.beginTx()){
					int rank=1;
					for(Map<Map<List<Node>, List<Relationship>>,Integer> list:sort){
						Iterator it=list.entrySet().iterator();
						
						while(it.hasNext()){
							Map.Entry pair=(Entry) it.next();
							Map<List<Node>, List<Relationship>> g=(Map<List<Node>, List<Relationship>>) pair.getKey();
							int cost=(int) pair.getValue();
							Iterator ite=g.entrySet().iterator();
							while(ite.hasNext()){
								Map.Entry p=(Entry) ite.next();
								List<Relationship> r=(List<Relationship>) p.getValue();
								List<Node> n = (List<Node>) p.getKey();
					
								List<String> relations=new ArrayList<>();
								List<String> tables= new ArrayList<>();
								for(Node nod:n){
									String tmp = (String) nod.getProperty("value");
									tables.add(tmp);
								}
								for(Relationship rel:r){
									String temp=(String) rel.getProperty("RelationType");
									relations.add(temp);
									//System.out.println(temp);
								}
								
								JoinModel model = new JoinModel(relations,tables,cost,rank);
								joinResult.add(model);
								}
							rank++;
						}	
					}
					tx.success();
					tx.close();
				}
			}else{
				
				try(Transaction tx=db.beginTx()){
								List<String> rel = new ArrayList<>();
				List<String> tab = new ArrayList<>();
				int cost=0;
				int rank=0;
				System.out.println("Can u See Remote here?--------------final");
				tab.add((String) cluster.get(0).getProperty("value"));
				
				System.out.println((String) cluster.get(0).getProperty("value"));
				JoinModel model = new JoinModel(rel,tab,cost,rank);
				joinResult.add(model);
				tx.success();
				tx.close();
			}
				
			}
			
		
		}	
		
		
		
		
		
//		finally{
//			//db.shutdown();
//			//neo.shutDown(db);
//		}
		
		allModel=new AllModel(resultSet,joinResult);
		System.out.println("-----DAO in Server");
		System.out.println(allModel);
		return allModel;
	}
	}

	
	
	//Ranking
	/*public static List<ResultModel> sort(List<ResultModel> resultSet, String str)
	{
		str=str.toLowerCase();
		Result[] rlist=new Result[resultSet.size()];
		String[] terms=str.split(" ");
		for(int i=0;i<resultSet.size();i++)
		{
			
			String r=resultSet.get(i).getRecord();
			String c=resultSet.get(i).getColumn();
			String t=resultSet.get(i).getTable();
			String d=resultSet.get(i).getDatabase();
			String[] s={r,c,t,d};
			int[] scores={0,0,0,0};
			for(int j=0;j<4;j++)
			{
				for(String term: terms)
				{
					String line=s[j];
					line=line.toLowerCase();
					if(line.indexOf(term)>=0)
						scores[j]++;
					String[] tmps=line.split(" ");
					for(String tmp:tmps)
					{
						if(tmp.equals(term))
						{
							scores[j]++;
						}
					}
				}
			}
			rlist[i]=new Result(r,c,t,d,scores[0],scores[1],scores[2],scores[3]);
			
		}
		Arrays.sort(rlist, new ResultSort());
		resultSet=new ArrayList<ResultModel>();
		System.out.println("\n\nResult:");
		for(int i=0;i<rlist.length;i++)
		{
			resultSet.add(rlist[i].result);
			
		}
		return resultSet;
	}
}
class Result
{
	ResultModel result;
	double recordScore=0;
	double columnScore=0;
	double tableScore=0;
	double dbScore=0;
	double score=0;
	public Result(String record, String column, String table, String database,double a,double b,double c,double d)
	{
		result=new ResultModel(record,  column, table, database);
		this.recordScore=a;
		this.columnScore=b;
		this.tableScore=c;
		this.dbScore=d;
		this.score=a+b+c+d;
	}
}
class ResultSort implements Comparator{
    public int compare(Object obj1, Object obj2){
    	Result o1=(Result) obj1;
    	Result o2=(Result) obj2;
        if(o1.score>o2.score){
            return -1;
        }
        if(o1.score<o2.score){
            return 1;
        }
        return 0;
    }

}*/
