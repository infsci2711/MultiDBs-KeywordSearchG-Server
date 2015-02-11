package edu.pitt.sis.infsci2711.multidbskeywordsearchgserverapi.server;

import edu.pitt.sis.infsci2711.multidbs.utils.JerseyJettyServer;

public class Server {
	public static void main(final String[] args) throws Exception {
		JerseyJettyServer server = new JerseyJettyServer(7654,
				"edu.pitt.sis.infsci2711.multidbskeywordsearchgserverapi.rest");
		server.start();
	}
}
