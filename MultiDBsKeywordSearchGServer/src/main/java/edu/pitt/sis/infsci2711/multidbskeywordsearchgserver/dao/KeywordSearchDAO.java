package edu.pitt.sis.infsci2711.multidbskeywordsearchgserver.dao;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.neo4j.cypher.javacompat.ExecutionEngine;
import org.neo4j.cypher.javacompat.ExecutionResult;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
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
		List<ResultModel> resultSet;

		try (Transaction ignored = db.beginTx()) {

			result = engine
					.execute("MATCH (a)-[:`BELONG_TO`]->(b) Where a.value=~\".*"
							+ str + ".*\" RETURN a,b");
			//resultString = result.dumpToString();

			//System.out.println(resultString);

			resultSet = new ArrayList<ResultModel>();

			for (Map<String, Object> row : result) {
				Node a = (Node) row.get("a");
				Node b = (Node) row.get("b");
				resultSet.add(new ResultModel(
						a.getProperty("value").toString(), b.getProperty(
								"value").toString()));
			}

		}
        db.shutdown();

		return resultSet;
	}

}
