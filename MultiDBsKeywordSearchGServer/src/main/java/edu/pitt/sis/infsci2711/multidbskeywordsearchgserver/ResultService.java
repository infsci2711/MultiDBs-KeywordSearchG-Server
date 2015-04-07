package edu.pitt.sis.infsci2711.multidbskeywordsearchgserver;

import java.sql.SQLException;
import java.util.List;
import java.util.Set;

import org.neo4j.graphdb.GraphDatabaseService;

import edu.pitt.sis.infsci2711.multidbskeywordsearchgserver.dao.KeywordSearchDAO;
import edu.pitt.sis.infsci2711.multidbskeywordsearchgserver.models.JoinModel;
import edu.pitt.sis.infsci2711.multidbskeywordsearchgserver.models.ResultModel;

public class ResultService {
	
	public List<ResultModel> find(final String str) throws SQLException, Exception {
		List<ResultModel> result = KeywordSearchDAO.search(str);
		
		return result;
	}
	
	public List<JoinModel> joinTables(List<ResultModel> result){
		List<JoinModel> joint = KeywordSearchDAO.joinResult(result);
		
		
		return joint;
	}
	
}
