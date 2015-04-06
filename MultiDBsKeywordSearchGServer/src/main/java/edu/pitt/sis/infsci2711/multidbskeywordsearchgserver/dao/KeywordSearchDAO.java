package edu.pitt.sis.infsci2711.multidbskeywordsearchgserver.dao;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
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

import edu.pitt.sis.infsci2711.multidbskeywordsearchgserver.models.JoinModel;
import edu.pitt.sis.infsci2711.multidbskeywordsearchgserver.models.ResultModel;
import edu.pitt.sis.infsci2711.multidbskeywordsearchgserver.utils.Config;
import edu.pitt.sis.infsci2711.multidbskeywordsearchgserver.utils.join;

public class KeywordSearchDAO {
	private static final String DB_PATH = new Config().neo4j_path;
	String resultString;
	String columnsString;
	String nodeResult;
	String rows = "";
	private static GraphDatabaseService db = new GraphDatabaseFactory().newEmbeddedDatabase(DB_PATH);

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
				resultSet.add(new ResultModel(record, column, table, database));
			}
		  }
			ignored.success();
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			//db.shutdown();
		}
		//sorting
		resultSet=sort(resultSet,str);
		//joinResult(resultSet,db);
		
		
		return resultSet;
	}
	
	public static List<JoinModel> joinResult(List<ResultModel> resultSet){
		join join= new join();
		List<Map<Map<List<Node>, List<Relationship>>,Integer>> sort =join.join(resultSet, db);
		List<JoinModel> joinResult =new ArrayList<>();
		
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
						for(Relationship rel:r){
							String temp=(String) rel.getProperty("RelationType");
							relations.add(temp);
							System.out.println(temp);
						}
						
						JoinModel model = new JoinModel(relations,cost,rank);
						joinResult.add(model);
						}
					rank++;
				}	
			}
			tx.success();
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			db.shutdown();
		}
		System.out.println(joinResult.size()+"-----------DAO");
		return joinResult;
	}
	
	
	
	//Ranking
	public static List<ResultModel> sort(List<ResultModel> resultSet, String str)
	{
		str=str.toLowerCase();
		Result[] rlist=new Result[resultSet.size()];
		String[] terms=str.split(" ");
		for(int i=0;i<resultSet.size();i++)
		{
			System.out.print(resultSet.get(i).getRecord()+"\t");
			System.out.print(resultSet.get(i).getColumn()+"\t");
			System.out.print(resultSet.get(i).getTable()+"\t");
			System.out.print(resultSet.get(i).getDatabase()+"\n");
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
			System.out.println(rlist[i].score);
		}
		Arrays.sort(rlist, new ResultSort());
		resultSet=new ArrayList<ResultModel>();
		System.out.println("\n\nResult:");
		for(int i=0;i<rlist.length;i++)
		{
			resultSet.add(rlist[i].result);
			System.out.print(resultSet.get(i).getRecord()+"\t");
			System.out.print(resultSet.get(i).getColumn()+"\t");
			System.out.print(resultSet.get(i).getTable()+"\t");
			System.out.print(resultSet.get(i).getDatabase()+"\n");
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

}
