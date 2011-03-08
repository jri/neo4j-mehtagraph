package de.deepamehta.hypergraph.neo4j;

import de.deepamehta.hypergraph.HyperEdge;
import de.deepamehta.hypergraph.HyperGraph;
import de.deepamehta.hypergraph.HyperNode;
import de.deepamehta.hypergraph.Transaction;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.DynamicRelationshipType;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Path;
import org.neo4j.graphdb.PropertyContainer;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.ReturnableEvaluator;
import org.neo4j.graphdb.StopEvaluator;
import org.neo4j.graphdb.TraversalPosition;
import org.neo4j.graphdb.Traverser.Order;
import org.neo4j.graphdb.traversal.PruneEvaluator;
import org.neo4j.graphdb.traversal.TraversalDescription;
import org.neo4j.graphdb.traversal.Traverser;
import org.neo4j.helpers.Predicate;
import org.neo4j.kernel.EmbeddedGraphDatabase;
import org.neo4j.kernel.Traversal;
import org.neo4j.index.IndexHits;
import org.neo4j.index.IndexService;
import org.neo4j.index.lucene.LuceneIndexService;
import org.neo4j.index.lucene.LuceneFulltextQueryIndexService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;



public class Neo4jHyperGraph extends Neo4jBase implements HyperGraph {

    // ---------------------------------------------------------------------------------------------- Instance Variables

    private final Logger logger = Logger.getLogger(getClass().getName());

    // ---------------------------------------------------------------------------------------------------- Constructors

    public Neo4jHyperGraph(GraphDatabaseService neo4j) {
        super(neo4j);
    }

    // -------------------------------------------------------------------------------------------------- Public Methods

    @Override
    public HyperNode createHyperNode(Map<String, Object> properties) {
        Node node = neo4j.createNode();
        setProperties(node, properties);
        logger.info("### node ID=" + node.getId());
        //
        return new Neo4jHyperNode(node, neo4j);
    }

    @Override
    public HyperEdge createHyperEdge(String edgeType, Map<String, Object> properties) {
        if (properties == null) {
            properties = new HashMap();
        }
        properties.put(KEY_IS_HYPER_EDGE, true);
        properties.put(KEY_HYPER_EDGE_TYPE, edgeType);
        //
        Node auxiliaryNode = neo4j.createNode();
        logger.info("### auxiliary node ID=" + auxiliaryNode.getId());
        setProperties(auxiliaryNode, properties);
        //
        return new Neo4jHyperEdge(auxiliaryNode, neo4j);
    }

    // ---

    @Override
    public Transaction beginTx() {
        return new Neo4jTransaction(neo4j);
    }

    @Override
    public void shutdown() {
        logger.info("Shutdown DB");
        neo4j.shutdown();
    }

    // ------------------------------------------------------------------------------------------------- Private Methods

    private void setProperties(PropertyContainer container, Map<String, Object> properties) {
        for (String key : properties.keySet()) {
            Object value = properties.get(key);
            container.setProperty(key, value);
        }
    }
}
