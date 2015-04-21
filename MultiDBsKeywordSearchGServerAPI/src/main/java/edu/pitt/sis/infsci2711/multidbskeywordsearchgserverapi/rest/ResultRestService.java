package edu.pitt.sis.infsci2711.multidbskeywordsearchgserverapi.rest;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.neo4j.graphdb.GraphDatabaseService;

import scala.collection.Set;
import edu.pitt.sis.infsci2711.multidbskeywordsearchgserver.ResultService;
import edu.pitt.sis.infsci2711.multidbskeywordsearchgserver.models.ResultModel;
import edu.pitt.sis.infsci2711.multidbskeywordsearchgserverapi.viewModels.ResultAPIModel;

@Path("Result/")
public class ResultRestService {
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response allPersons() {
		try {
			return Response.status(200).build();
		} catch (Exception e) {
			return Response.status(500).build();
		}
	}

	@Path("{str}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response resultByString(@PathParam("str") final String str) {

		ResultService resultService = new ResultService();

		try {
			List<ResultModel> resultSet =  resultService.find(str);

			if (resultSet != null) {
				List<ResultAPIModel> result = convertDbToViewModel(resultSet);

				GenericEntity<List<ResultAPIModel>> entity = new GenericEntity<List<ResultAPIModel>>(
						result) {
				};
				
				return Response.status(200).entity(entity).build();
			}
			return Response.status(404).entity("Person not found").build();
		} catch (Exception e) {
			System.out.println(e);
			return Response.status(500).build();
		}

	}

	private List<ResultAPIModel> convertDbToViewModel(
			final List<ResultModel> resultSet) {
		List<ResultAPIModel> result = new ArrayList<ResultAPIModel>();
		for (ResultModel resultDB : resultSet) {
			result.add(convertDbToViewModel(resultDB));
		}

		return result;
	}

	private ResultAPIModel convertDbToViewModel(final ResultModel resultDB) {
		return new ResultAPIModel(resultDB.getID(), resultDB.getRecord(), resultDB.getColumn(), resultDB.getTable(), resultDB.getDatabase(),resultDB.getKeyword());
	}
}
