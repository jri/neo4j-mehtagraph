package de.deepamehta.hypergraph.neo4j;

import de.deepamehta.hypergraph.HyperNode;
import de.deepamehta.hypergraph.HyperEdge;

import org.neo4j.graphdb.DynamicRelationshipType;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.PropertyContainer;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.index.Index;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;



class Neo4jBase {

    // ------------------------------------------------------------------------------------------------------- Constants

    protected static final String KEY_IS_HYPER_EDGE = "is_hyper_edge";
    protected static final String KEY_HYPER_EDGE_TYPE = "hyper_edge_type";

    protected static final String KEY_FULLTEXT = "fulltext";

    // ---------------------------------------------------------------------------------------------- Instance Variables

    private final Logger logger = Logger.getLogger(getClass().getName());

    protected GraphDatabaseService neo4j;
    protected Index<Node> exactIndex;
    protected Index<Node> fulltextIndex;

    // ---------------------------------------------------------------------------------------------------- Constructors

    protected Neo4jBase(GraphDatabaseService neo4j, Index exactIndex, Index fulltextIndex) {
        this.neo4j = neo4j;
        this.exactIndex = exactIndex;
        this.fulltextIndex = fulltextIndex;
    }

    // ----------------------------------------------------------------------------------------------- Protected Methods

    protected final String getAttributesString(PropertyContainer container) {
        Map<String, Object> properties = getProperties(container);
        //
        StringBuilder builder = new StringBuilder("{");
        for (String key : properties.keySet()) {
            if (builder.length() > 1) {
                builder.append(", ");
            }
            builder.append(key + "=" + properties.get(key));
        }
        builder.append("}");
        return builder.toString();
    }

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

    // ---

    protected final HyperNode buildHyperNode(Node node) {
        if (node == null) {
            throw new NullPointerException("Tried to build a HyperNode with node=null");
        }
        return new Neo4jHyperNode(node, neo4j, exactIndex, fulltextIndex);
    }

    protected final HyperEdge buildHyperEdge(Node auxiliaryNode) {
        if (auxiliaryNode == null) {
            throw new NullPointerException("Tried to build a HyperEdge with auxiliaryNode=null");
        }
        return new Neo4jHyperEdge(auxiliaryNode, neo4j, exactIndex, fulltextIndex);
    }
}
