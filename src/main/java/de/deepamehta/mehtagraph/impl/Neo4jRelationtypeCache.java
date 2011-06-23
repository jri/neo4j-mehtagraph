package de.deepamehta.mehtagraph.impl;

import org.neo4j.graphdb.DynamicRelationshipType;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.RelationshipType;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;



class Neo4jRelationtypeCache {

    // ---------------------------------------------------------------------------------------------- Instance Variables

    private final Map<String, RelationshipType> relTypeCache;

    private final Logger logger = Logger.getLogger(getClass().getName());

    // ---------------------------------------------------------------------------------------------------- Constructors

    Neo4jRelationtypeCache(GraphDatabaseService neo4j) {
        this.relTypeCache = createRelTypeCache(neo4j);
    }

    // ----------------------------------------------------------------------------------------- Package Private Methods

    RelationshipType get(String typeName) {
        RelationshipType relType = relTypeCache.get(typeName);
        if (relType == null) {
            logger.info("### Creating Neo4j relationship type \"" + typeName + "\" dynamically");
            relType = DynamicRelationshipType.withName(typeName);
            relTypeCache.put(typeName, relType);
        }
        return relType;
    }

    // ------------------------------------------------------------------------------------------------- Private Methods

    private Map<String, RelationshipType> createRelTypeCache(GraphDatabaseService neo4j) {
        Map<String, RelationshipType> relTypeCache = new HashMap();
        for (RelationshipType relType : neo4j.getRelationshipTypes()) {
            relTypeCache.put(relType.name(), relType);
        }
        return relTypeCache;
    }
}
