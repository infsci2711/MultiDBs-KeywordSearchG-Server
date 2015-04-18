package edu.pitt.sis.infsci2711.multidbskeywordsearchgserver;

import java.sql.SQLException;
import java.util.List;

import edu.pitt.sis.infsci2711.multidbskeywordsearchgserver.dao.KeywordSearchDAO;
import edu.pitt.sis.infsci2711.multidbskeywordsearchgserver.models.ResultModel;
import edu.pitt.sis.infsci2711.multidbskeywordsearchgserver.utils.DatasourceDBModel;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.MediaType;


public class DataSourceService {
	public boolean initDataSource() throws SQLException, Exception {
		Client client = ClientBuilder.newClient();
		WebTarget targetMetaStore = client.target("http://54.152.26.131:7654/").path("datasources");
		Response responseMetaStore = targetMetaStore.request(MediaType.APPLICATION_JSON).get();
		System.out.println(responseMetaStore.readEntity(String.class));
		return false;
	}
}
