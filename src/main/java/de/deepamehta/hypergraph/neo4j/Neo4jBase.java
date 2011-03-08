package de.deepamehta.hypergraph.neo4j;

import org.neo4j.graphdb.DynamicRelationshipType;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.PropertyContainer;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;



class Neo4jBase {

    // ------------------------------------------------------------------------------------------------------- Constants

    protected static final String KEY_IS_HYPER_EDGE = "is_hyper_edge";
    protected static final String KEY_HYPER_EDGE_TYPE = "hyper_edge_type";

    // ---------------------------------------------------------------------------------------------- Instance Variables

    private final Logger logger = Logger.getLogger(getClass().getName());

    protected GraphDatabaseService neo4j;

    // ---------------------------------------------------------------------------------------------------- Constructors

    protected Neo4jBase(GraphDatabaseService neo4j) {
        this.neo4j = neo4j;
    }

    // ----------------------------------------------------------------------------------------------- Protected Methods

    protected final Map<String, Object> getProperties(PropertyContainer container) {
        Map properties = new HashMap();
        for (String key : container.getPropertyKeys()) {
            properties.put(key, container.getProperty(key));
        }
        return properties;
    }

    protected final RelationshipType getRelationshipType(String typeName) {
        // search through dynamic types
        for (RelationshipType relType : neo4j.getRelationshipTypes()) {
            if (relType.name().equals(typeName)) {
                return relType;
            }
        }
        // fallback: create new type
        logger.info("### Relation type \"" + typeName + "\" does not exist -- Creating it dynamically");
        return DynamicRelationshipType.withName(typeName);
    }
}
