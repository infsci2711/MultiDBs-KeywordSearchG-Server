package edu.pitt.sis.infsci2711.multidbskeywordsearchgserverapi.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import edu.pitt.sis.infsci2711.multidbskeywordsearchgserver.DataSourceService;



@Path("DataSource/")
public class DataSourceApi {

	@Path("init/")
	@GET
    @Produces(MediaType.APPLICATION_JSON)
	public Response init() {
		
		DataSourceService service = new DataSourceService();
		
		try {
			boolean initDataSource = service.initDataSource();
			if(!initDataSource)
			{
				return Response.status(500).entity("{\"error\" : \"please check your input and try again\"}").build();
			}
			return Response.status(200).entity("{\"success\": \"the whole data source has been add to neo4j\"}").build();
		} catch (Exception e) {
			//return Response.status(500).build();
			String errmsg=e.getCause()==null?e.getMessage():e.getCause().getMessage();
			return Response.status(500).entity("{\"error\" : \""+e.getClass().getSimpleName()+"\" , \"message\" :  \""+errmsg+"\"}").build();
		}
		
	}
	
}
