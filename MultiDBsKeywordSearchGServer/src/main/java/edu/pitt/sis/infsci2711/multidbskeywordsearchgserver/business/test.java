package edu.pitt.sis.infsci2711.multidbskeywordsearchgserver.business;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

import edu.pitt.sis.infsci2711.multidbskeywordsearchgserver.utils.Neo4j;

public class test {
	
	public static void main(String args[]){
		String Neo4j_Path = "/Users/jiechen/Google Drive/Eclipse-Luna/neo4j-community-2.2.0-M02/OMG22222";
		GraphDatabaseService graphDataService = new GraphDatabaseFactory().newEmbeddedDatabase(Neo4j_Path);
		

		Neo4j neo = new Neo4j();
		neo.shutDown(graphDataService);
			
		
	}

}
