package edu.pitt.sis.infsci2711.multidbskeywordsearchgserver;

import java.sql.SQLException;
import java.util.List;

import edu.pitt.sis.infsci2711.multidbskeywordsearchgserver.dao.KeywordSearchDAO;
import edu.pitt.sis.infsci2711.multidbskeywordsearchgserver.models.ResultModel;

public class ResultService {
	
	public List<ResultModel> find(final String str) throws SQLException, Exception {
		List<ResultModel> result = KeywordSearchDAO.search(str);
		
		return result;
	}
	
}
