package de.deepamehta.mehtagraph;

import de.deepamehta.mehtagraph.impl.Neo4jMehtaGraph;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.kernel.EmbeddedGraphDatabase;

import java.util.logging.Logger;



public class MehtaGraphFactory {

    // ---------------------------------------------------------------------------------------------- Instance Variables

    private static Logger logger = Logger.getLogger("MehtaGraphFactory");

    // -------------------------------------------------------------------------------------------------- Public Methods

    public static MehtaGraph createInstance(String databasePath) {
        GraphDatabaseService neo4j = null;
        try {
            neo4j = new EmbeddedGraphDatabase(databasePath);
            return new Neo4jMehtaGraph(neo4j);
        } catch (Exception e) {
            if (neo4j != null) {
                logger.info("Shutdown Neo4j");
                neo4j.shutdown();
            }
            throw new RuntimeException("Creating a Neo4jMehtaGraph instance failed", e);
        }
    }
}
