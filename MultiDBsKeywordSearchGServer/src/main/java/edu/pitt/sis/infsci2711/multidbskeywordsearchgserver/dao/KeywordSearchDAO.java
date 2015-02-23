package edu.pitt.sis.infsci2711.multidbskeywordsearchgserver.dao;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.neo4j.cypher.javacompat.ExecutionEngine;
import org.neo4j.cypher.javacompat.ExecutionResult;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Path;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.helpers.collection.IteratorUtil;

import static org.neo4j.kernel.impl.util.FileUtils.deleteRecursively;
import edu.pitt.sis.infsci2711.multidbskeywordsearchgserver.models.ResultModel;
import edu.pitt.sis.infsci2711.multidbskeywordsearchgserver.utils.Config;

public class KeywordSearchDAO {
	private static final String DB_PATH = new Config().neo4j_path;
	String resultString;
	String columnsString;
	String nodeResult;
	String rows = "";

	public static void main(String[] args) {
		KeywordSearchDAO javaQuery = new KeywordSearchDAO();
		System.out.println(javaQuery.search("chun").toString());
	}

	public static List<ResultModel> search(String str) {
		GraphDatabaseService db = new GraphDatabaseFactory()
				.newEmbeddedDatabase(DB_PATH);

		ExecutionEngine engine = new ExecutionEngine(db);
		ExecutionResult result;
		List<ResultModel> resultSet = null;

		try (Transaction ignored = db.beginTx()) {

			result = engine
					.execute("MATCH p=(a)-[:`BELONG_TO`*0..]->(b) Where a.value=~\"(?i).*"
							+ str + ".*\" and b.type=\"database\" RETURN p");
			// resultString = result.dumpToString();

			// System.out.println(resultString);

			resultSet = new ArrayList<ResultModel>();

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

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.shutdown();
		}
		return resultSet;
	}

}
