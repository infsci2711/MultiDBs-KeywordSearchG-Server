package edu.pitt.sis.infsci2711.multidbskeywordsearchgserver;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import edu.pitt.sis.infsci2711.multidbskeywordsearchgserver.business.SQL2Neo4J;
import edu.pitt.sis.infsci2711.multidbskeywordsearchgserver.dao.KeywordSearchDAO;
import edu.pitt.sis.infsci2711.multidbskeywordsearchgserver.models.ResultModel;
import edu.pitt.sis.infsci2711.multidbskeywordsearchgserver.utils.DataSourceTableModel;
import edu.pitt.sis.infsci2711.multidbskeywordsearchgserver.utils.DatasourceColumnModel;
import edu.pitt.sis.infsci2711.multidbskeywordsearchgserver.utils.DatasourceDBModel;
import edu.pitt.sis.infsci2711.multidbskeywordsearchgserver.utils.Neo4j;
import edu.pitt.sis.infsci2711.multidbskeywordsearchgserver.utils.QueryModel;
import edu.pitt.sis.infsci2711.multidbskeywordsearchgserver.utils.QueryResultModel;
import edu.pitt.sis.infsci2711.multidbskeywordsearchgserver.utils.RowModel;

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
	    List<DatasourceDBModel> responseMetaStore
        = response.readEntity(new GenericType<List<DatasourceDBModel>>(){});
	    SQL2Neo4J sql2neo =new SQL2Neo4J();
	    for (DatasourceDBModel db:responseMetaStore) {
		int did = db.getId();
		
		if(did!=1 && did!=2 && did!=16){
		String dbName = db.getDbName();
		List<DataSourceTableModel> tables =  db.getTables();
		Map<String, List<String>> col_val = new LinkedHashMap<>();
		Map<String, Map<String, List<String>>> data = new HashMap<>();
		
		
		for(DataSourceTableModel table : tables){
			String tableName = table.getTableName();
			List<DatasourceColumnModel> cols = table.getColumns();
			List<String> columns = new ArrayList<>();
			for(DatasourceColumnModel col:cols){
				String columnName = col.getColumnName();
				String tabcol = tableName+"."+columnName;
				columns.add(columnName);
				List<String> values = getValue(did, tableName, columnName);
				col_val.put(columnName,values);
			}
			data.put(tableName, col_val);
		}
		sql2neo.add(did, dbName, data);
	    }
	    }
		//System.out.println(responseMetaStore.readEntity(String.class));
	    
		return false;
	}
	
	//send request to PrestoDB
		public List<String> getValue(int did, String tableName,String columnName){
			
			String query="Select "+columnName+" from "+did+"."+tableName+"";
			//String query = "select aid from 19.test";
			QueryModel QueryModel = new QueryModel();
			QueryModel.setQuery(query);
			List<String> values = new ArrayList<>();
			System.out.println(query);
			Client client= ClientBuilder.newClient();
			WebTarget target = client.target("http://54.174.80.167:7654/");
			target = target.path("Query/");
			Response response = target.request(MediaType.APPLICATION_JSON)
		             .put(Entity.entity(QueryModel, MediaType.APPLICATION_JSON),Response.class);
			System.out.println(response);
			QueryResultModel qresult=response.readEntity(QueryResultModel.class);
			System.out.println(qresult);
			RowModel[] r=qresult.getData();
			System.out.println(r);
			for(int i=0; i<r.length;i++){
				values.add(r[i].getRow()[0]);	
			}
			
			return values;	
		}
		
}
