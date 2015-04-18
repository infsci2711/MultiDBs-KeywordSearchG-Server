package edu.pitt.sis.infsci2711.multidbskeywordsearchgserverapi.rest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.client.Entity;

import org.codehaus.jettison.json.JSONObject;

import edu.pitt.sis.infsci2711.multidbs.utils.JerseyClientUtil;
import edu.pitt.sis.infsci2711.multidbs.utils.PropertiesManager;
import edu.pitt.sis.infsci2711.multidbskeywordsearchgserver.business.SQL2Neo4J;
import edu.pitt.sis.infsci2711.multidbskeywordsearchgserver.models.DatasourceDBModel;
import edu.pitt.sis.infsci2711.multidbskeywordsearchgserver.models.JoinModel;
import edu.pitt.sis.infsci2711.multidbskeywordsearchgserver.models.TableModel;
import edu.pitt.sis.infsci2711.multidbskeywordsearchgserverapi.viewModels.ColumnViewModel;
import edu.pitt.sis.infsci2711.multidbskeywordsearchgserverapi.viewModels.DatasourceViewModel;
import edu.pitt.sis.infsci2711.multidbskeywordsearchgserverapi.viewModels.JoinAPIModel;
import edu.pitt.sis.infsci2711.multidbskeywordsearchgserverapi.viewModels.QueryResultViewModel;
import edu.pitt.sis.infsci2711.multidbskeywordsearchgserverapi.viewModels.QueryViewModel;
import edu.pitt.sis.infsci2711.multidbskeywordsearchgserverapi.viewModels.RowViewModel;
import edu.pitt.sis.infsci2711.multidbskeywordsearchgserverapi.viewModels.TableViewModel;

@Path("Get/")
public class GetRestApi {
	
	private static final QueryViewModel Client = null;


	@Path("helloWorld")
	@GET
    @Produces(MediaType.APPLICATION_JSON)
	public Response helloWorld() {
		return Response.status(200).entity("{\"msg\" : \"Hello World\"}").build();
	}
	
	
	@Path("helloWorld2")
	@GET
    @Produces(MediaType.APPLICATION_JSON)
	public Response helloWorld2() {
		
		// This will send GET request to 54.152.26.131:7654/datasources
		Response result = JerseyClientUtil.doGet(PropertiesManager.getInstance().getStringProperty("metastore.rest.base"),
				PropertiesManager.getInstance().getStringProperty("metastore.rest.getAllDatasources"));
		
		// do something with result if you needed
		
		
		//Can send another request. Here is example of POST
		
//		Response result2 = JerseyClientUtil.doPost(PropertiesManager.getInstance().getStringProperty("metastore.rest.base"),
//				PropertiesManager.getInstance().getStringProperty("metastore.rest.addDatasource"),
//				new SomeViewModel(123));
		
		// do something with result2 if needed
		
		//finally send response to the client to their original request
		return Response.status(200).entity("{\"msg\" : \"Hello World\"}").build();
	}
	
	//get the message of creating new database from metastore
	@Path("Create")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public void createNewDB(final DatasourceViewModel datasourceVM){
		SQL2Neo4J sql2neo =new SQL2Neo4J();
		int did = datasourceVM.getId();
		String dbName = datasourceVM.getDbName();
		List<TableViewModel> tablesVM =  datasourceVM.getTables();
		//List<TableModel> tables = convertViewModelToTab(tablesVM);
		//List<ColumnViewModel> colsVM = new ArrayList<>();
		//Map<String, List<String>> tab_col = new LinkedHashMap<>();
		Map<String, List<String>> col_val = new LinkedHashMap<>();
		Map<String, Map<String, List<String>>> data = new HashMap<>();
		
		
		for(TableViewModel tableVM : tablesVM){
			String tableName = tableVM.getTableName();
			List<ColumnViewModel> colsVM = tableVM.getColumns();
			List<String> columns = new ArrayList<>();
			for(ColumnViewModel col:colsVM){
				String columnName = col.getColumnName();
				String tabcol = tableName+"."+columnName;
				columns.add(columnName);
				List<String> values = getValue(did, tableName, columnName);
				col_val.put(columnName,values);
			}
			//tab_col.put(tableName, columns);
			data.put(tableName, col_val);
		}	
		sql2neo.add(did, dbName, data);
	}
	
	//send request to PrestoDB
	public List<String> getValue(int did, String tableName,String columnName){
		
		String query="Select '"+columnName+"' from '"+did+"'.'"+tableName+"'";
		QueryViewModel QueryViewModel = new QueryViewModel();
		QueryViewModel.setQuery(query);
		List<String> values = new ArrayList<>();
		
		Client client= ClientBuilder.newClient();
		WebTarget target = client.target("http://54.174.80.167:7654/");
		target = target.path("Query/");
		Response response = target.request(MediaType.APPLICATION_JSON)
	             .put(Entity.entity(QueryViewModel, MediaType.APPLICATION_JSON),Response.class);
		
		QueryResultViewModel qresult=response.readEntity(QueryResultViewModel.class);
		RowViewModel[] r=qresult.getData();
		for(int i=0; i<r.length;i++){
			values.add(r[i].getRow()[0]);	
		}
		return values;	
	}
	
	
	
	
	
	
	
	
	private List<TableModel> convertViewModelToTab(
			final List<TableViewModel> tablesVM) {
		List<TableModel> tables = new ArrayList<TableModel>();
		for (TableViewModel table : tablesVM) {
			tables.add(convertViewModelToTab(table));
		}
		return tables;
	}

	
	public TableModel convertViewModelToTab(TableViewModel tablesVM ){
		return new TableModel(tablesVM.getTableName());
	}
}





