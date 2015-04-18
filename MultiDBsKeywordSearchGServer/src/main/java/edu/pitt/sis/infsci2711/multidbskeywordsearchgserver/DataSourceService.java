package edu.pitt.sis.infsci2711.multidbskeywordsearchgserver;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import edu.pitt.sis.infsci2711.multidbskeywordsearchgserver.dao.KeywordSearchDAO;
import edu.pitt.sis.infsci2711.multidbskeywordsearchgserver.models.ResultModel;
import edu.pitt.sis.infsci2711.multidbskeywordsearchgserver.utils.DataSourceTableModel;
import edu.pitt.sis.infsci2711.multidbskeywordsearchgserver.utils.DatasourceColumnModel;
import edu.pitt.sis.infsci2711.multidbskeywordsearchgserver.utils.DatasourceDBModel;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.MediaType;


public class DataSourceService {
	public boolean initDataSource() throws SQLException, Exception {
		Client client = ClientBuilder.newClient();
		WebTarget targetMetaStore = client.target("http://54.152.26.131:7654/").path("datasources");
		Response response = targetMetaStore.request(MediaType.APPLICATION_JSON).get();
		
		//DatasourceDBModel responseMetaStore = response.readEntity(Stin.class);
		
	    List<DatasourceDBModel> responseMetaStore
        = response.readEntity(new GenericType<List<DatasourceDBModel>>(){});
		
	    
	    for (DatasourceDBModel tt:responseMetaStore) {
		int did = tt.getId();
		String dbName = tt.getDbName();
		
		
		
		System.out.println(dbName);
		List<DataSourceTableModel> tablesVM =  tt.getTables();
		//List<TableModel> tables = convertViewModelToTab(tablesVM);
		//List<ColumnViewModel> colsVM = new ArrayList<>();
		//Map<String, List<String>> tab_col = new LinkedHashMap<>();
		Map<String, List<String>> col_val = new LinkedHashMap<>();
		Map<String, Map<String, List<String>>> data = new HashMap<>();
		
		
		for(DataSourceTableModel tableVM : tablesVM){
			String tableName = tableVM.getTableName();
			List<DatasourceColumnModel> colsVM = tableVM.getColumns();
			List<String> columns = new ArrayList<>();
			for(DatasourceColumnModel col:colsVM){
				String columnName = col.getColumnName();
				String tabcol = tableName+"."+columnName;
				columns.add(columnName);
				//111 List<String> values = getValue(did, tableName, columnName);
				//111 col_val.put(columnName,values);
				System.out.println(tabcol);
			}
			//tab_col.put(tableName, columns);
			//111 data.put(tableName, col_val);
		}
	    }
		//System.out.println(responseMetaStore.readEntity(String.class));
		return false;
	}
}
