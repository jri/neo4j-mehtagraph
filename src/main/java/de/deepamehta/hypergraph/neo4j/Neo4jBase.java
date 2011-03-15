package de.deepamehta.hypergraph.neo4j;

import de.deepamehta.hypergraph.HyperNode;
import de.deepamehta.hypergraph.HyperEdge;

import org.neo4j.graphdb.DynamicRelationshipType;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.PropertyContainer;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
// FIXME: new index API doesn't work with OSGi
// import org.neo4j.graphdb.index.Index;
//
// Using old index API instead
import org.neo4j.index.IndexHits;
import org.neo4j.index.IndexService;
import org.neo4j.index.lucene.LuceneIndexService;
import org.neo4j.index.lucene.LuceneFulltextQueryIndexService;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;



class Neo4jBase {

    // ------------------------------------------------------------------------------------------------------- Constants

    protected static final String KEY_IS_HYPER_EDGE = "_is_hyper_edge";

    protected static final String KEY_FULLTEXT = "_fulltext";

    // ---------------------------------------------------------------------------------------------- Instance Variables

    private final Logger logger = Logger.getLogger(getClass().getName());

    protected GraphDatabaseService neo4j;
    // FIXME: new index API doesn't work with OSGi
    // protected Index<Node> exactIndex;
    // protected Index<Node> fulltextIndex;
    //
    // Using old index API instead
    protected IndexService exactIndex;
    protected LuceneFulltextQueryIndexService fulltextIndex;

    // ---------------------------------------------------------------------------------------------------- Constructors

    protected Neo4jBase(GraphDatabaseService neo4j) {
        this.neo4j = neo4j;
    }

    protected Neo4jBase(GraphDatabaseService neo4j, IndexService exactIndex,
                        LuceneFulltextQueryIndexService fulltextIndex
                        /* FIXME: Index exactIndex, Index fulltextIndex */) {
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
            throw new NullPointerException("Tried to build a HyperNode from a null Node");
        }
        return new Neo4jHyperNode(node, neo4j, exactIndex, fulltextIndex);
    }

    protected final HyperEdge buildHyperEdge(Node auxiliaryNode) {
        if (auxiliaryNode == null) {
            throw new NullPointerException("Tried to build a HyperEdge from a null auxiliary Node");
        }
        return new Neo4jHyperEdge(auxiliaryNode, neo4j, exactIndex, fulltextIndex);
    }
}
