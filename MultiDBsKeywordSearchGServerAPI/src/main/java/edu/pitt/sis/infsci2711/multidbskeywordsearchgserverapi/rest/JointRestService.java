package edu.pitt.sis.infsci2711.multidbskeywordsearchgserverapi.rest;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.neo4j.helpers.collection.Iterables;

import edu.pitt.sis.infsci2711.multidbskeywordsearchgserver.ResultService;
import edu.pitt.sis.infsci2711.multidbskeywordsearchgserver.models.AllModel;
import edu.pitt.sis.infsci2711.multidbskeywordsearchgserver.models.JoinModel;
import edu.pitt.sis.infsci2711.multidbskeywordsearchgserver.models.ResultModel;
import edu.pitt.sis.infsci2711.multidbskeywordsearchgserverapi.viewModels.AllAPIModel;
import edu.pitt.sis.infsci2711.multidbskeywordsearchgserverapi.viewModels.JoinAPIModel;
import edu.pitt.sis.infsci2711.multidbskeywordsearchgserverapi.viewModels.ResultAPIModel;




@Path("Join/")
public class JointRestService {
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
            AllModel joinSet = resultService.joinTables(resultSet);
			if (joinSet != null) {
				List<ResultAPIModel>result =RconvertDbToViewModel( joinSet.getSearchResult());
				List<JoinAPIModel> joint =JconvertDbToViewModel( joinSet.getJoinResult());
				AllAPIModel join = convertDbToViewModel(result,joint);
				GenericEntity entity = new GenericEntity<AllAPIModel>(
						join) {
				};

				return Response.status(200).entity(entity).build();
			}
			return Response.status(404).entity("Person not found").build();
		} catch (Exception e) {
			System.out.println(e);
			return Response.status(500).build();
		}

	}
	
	
    //Join Result
	private List<JoinAPIModel> JconvertDbToViewModel(
			final List<JoinModel> joinSet) {
		List<JoinAPIModel> join = new ArrayList<JoinAPIModel>();
		for (JoinModel joinDB : joinSet) {
			join.add(convertDbToViewModel(joinDB));
		}

		return join;
	}

	private JoinAPIModel convertDbToViewModel(final JoinModel joinDB) {
		return new JoinAPIModel(joinDB.getRelations(),joinDB.getTables(),joinDB.getCost(), joinDB.getRank());
	}
	
	//Search Result
	private List<ResultAPIModel> RconvertDbToViewModel(
			final List<ResultModel> resultSet) {
		List<ResultAPIModel> result = new ArrayList<ResultAPIModel>();
		for (ResultModel resultDB : resultSet) {
			result.add(convertDbToViewModel(resultDB));
		}

		return result;
	}

	private ResultAPIModel convertDbToViewModel(final ResultModel resultDB) {
		return new ResultAPIModel(resultDB.getRecord(),resultDB.getColumn(),
				resultDB.getTable(),resultDB.getDatabase(),resultDB.getKeyword());
	}
	
	
	private AllAPIModel convertDbToViewModel(final List<ResultAPIModel> result, final List<JoinAPIModel> join){
		return new AllAPIModel(result,join);
	}
}

