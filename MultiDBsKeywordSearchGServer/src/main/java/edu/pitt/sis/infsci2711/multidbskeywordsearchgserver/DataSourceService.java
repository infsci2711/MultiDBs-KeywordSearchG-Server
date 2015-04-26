package edu.pitt.sis.infsci2711.multidbskeywordsearchgserver;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import edu.pitt.sis.infsci2711.multidbs.utils.JerseyClientUtil;
import edu.pitt.sis.infsci2711.multidbs.utils.PropertiesManager;
import edu.pitt.sis.infsci2711.multidbskeywordsearchgserver.business.SQL2Neo4J;
import edu.pitt.sis.infsci2711.multidbskeywordsearchgserver.utils.DataSourceTableModel;
import edu.pitt.sis.infsci2711.multidbskeywordsearchgserver.utils.DatasourceColumnModel;
import edu.pitt.sis.infsci2711.multidbskeywordsearchgserver.utils.DatasourceDBModel;
import edu.pitt.sis.infsci2711.multidbskeywordsearchgserver.utils.QueryModel;
import edu.pitt.sis.infsci2711.multidbskeywordsearchgserver.utils.QueryResultModel;
import edu.pitt.sis.infsci2711.multidbskeywordsearchgserver.utils.RowModel;

public class DataSourceService {

	public boolean initDataSource() throws SQLException, Exception {

//		Client client = ClientBuilder.newClient();
//		WebTarget targetMetaStore = client.target("http://54.152.26.131:7654/")
//				.path("datasources");
//		Response response = targetMetaStore.request(MediaType.APPLICATION_JSON)
//				.get();
		
		Response responseQuery = JerseyClientUtil.doGet(PropertiesManager.getInstance().getStringProperty("metastore.rest.base"), 
				PropertiesManager.getInstance().getStringProperty("metastore.rest.datasources"));
		
		System.out.println(responseQuery);
		List<DatasourceDBModel> responseMetaStore = responseQuery.readEntity(new GenericType<List<DatasourceDBModel>>(){});
		
		SQL2Neo4J sql2neo = new SQL2Neo4J();
		for (DatasourceDBModel db : responseMetaStore) {
			int did = db.getId();

			if (did != 1 && did != 2 && did != 16) // WHAT ARE THOSE NUMBERS???????
			{
				String dbName = db.getDbName();
				List<DataSourceTableModel> tables = db.getTables();
				Map<String, List<String>> col_val = new LinkedHashMap<>();
				Map<String, Map<String, List<String>>> data = new HashMap<>();

				for (DataSourceTableModel table : tables) {
					String tableName = table.getTableName();
					List<DatasourceColumnModel> cols = table.getColumns();
					List<String> columns = new ArrayList<>();
					for (DatasourceColumnModel col : cols) {
						String columnName = col.getColumnName();
						String tabcol = tableName + "." + columnName;
						columns.add(columnName);
						List<String> values = getValue(did, tableName,
								columnName);
						col_val.put(columnName, values);
					}
					data.put(tableName, col_val);
				}
				sql2neo.add(did, dbName, data);
			}
		}
		// System.out.println(responseMetaStore.readEntity(String.class));

		return false;
	}

	public boolean add(final DatasourceDBModel db) throws SQLException, Exception {
		SQL2Neo4J sql2neo = new SQL2Neo4J();
		
		int did = db.getId();
		if (did != 1 && did != 2 && did != 16) {
			String dbName = db.getDbName();
			List<DataSourceTableModel> tables = db.getTables();
			Map<String, List<String>> col_val = new LinkedHashMap<>();
			Map<String, Map<String, List<String>>> data = new HashMap<>();

			for (DataSourceTableModel table : tables) {
				String tableName = table.getTableName();
				List<DatasourceColumnModel> cols = table.getColumns();
				List<String> columns = new ArrayList<>();
				for (DatasourceColumnModel col : cols) {
					String columnName = col.getColumnName();
					String tabcol = tableName + "." + columnName;
					columns.add(columnName);
					List<String> values = getValue(did, tableName,
							columnName);
					col_val.put(columnName, values);
				}
				data.put(tableName, col_val);
			}
			sql2neo.add(did, dbName, data);
			return true;
		}
		return false;
	}

	// send request to PrestoDB
	public List<String> getValue(final int did, final String tableName, final String columnName) {

		String query = "Select " + columnName + " from " + did + "."
				+ tableName + "";
		// String query = "select aid from 19.test";
		QueryModel queryModel = new QueryModel();
		queryModel.setQuery(query);
		List<String> values = new ArrayList<>();
		System.out.println(query);
		
		Response responseQuery = JerseyClientUtil.doPut(PropertiesManager.getInstance().getStringProperty("query.rest.base"), 
				PropertiesManager.getInstance().getStringProperty("query.rest.query"), queryModel);
		
//		Client client = ClientBuilder.newClient();
//		WebTarget target = client.target("http://54.174.80.167:7654/");
//		target = target.path("Query/");
//		
//		Response response = target.request(MediaType.APPLICATION_JSON).put(
//				Entity.entity(QueryModel, MediaType.APPLICATION_JSON),
//				Response.class);
		
		System.out.println(responseQuery);
		QueryResultModel qresult = responseQuery.readEntity(QueryResultModel.class);
		System.out.println(qresult);
		RowModel[] r = qresult.getData();
		System.out.println(r);
		for (int i = 0; i < r.length; i++) {
			values.add(r[i].getRow()[0]);
		}

		return values;
	}

}
