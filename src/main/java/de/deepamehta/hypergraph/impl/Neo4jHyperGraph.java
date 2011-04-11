package de.deepamehta.hypergraph.impl;

import de.deepamehta.hypergraph.HyperEdge;
import de.deepamehta.hypergraph.HyperGraph;
import de.deepamehta.hypergraph.HyperNode;
import de.deepamehta.hypergraph.HyperGraphTransaction;

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
// FIXME: new index API doesn't work with OSGi
// import org.neo4j.graphdb.index.Index;
import org.neo4j.graphdb.traversal.PruneEvaluator;
import org.neo4j.graphdb.traversal.TraversalDescription;
import org.neo4j.graphdb.traversal.Traverser;
import org.neo4j.helpers.Predicate;
import org.neo4j.helpers.collection.MapUtil;
import org.neo4j.kernel.EmbeddedGraphDatabase;
import org.neo4j.kernel.Traversal;
// Using old index API instead
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
        try {
            /* FIXME: new index API doesn't work with OSGi
            // access/create indexes
            this.exactIndex = neo4j.index().forNodes("exact");
            if (neo4j.index().existsForNodes("fulltext")) {
                this.fulltextIndex = neo4j.index().forNodes("fulltext");
            } else {
                Map<String, String> configuration = MapUtil.stringMap("provider", "lucene", "type", "fulltext");
                this.fulltextIndex = neo4j.index().forNodes("fulltext", configuration);
            } */
            this.exactIndex = new LuceneIndexService(neo4j);
            this.fulltextIndex = new LuceneFulltextQueryIndexService(neo4j);
        } catch (Exception e) {
            throw new RuntimeException("Creating database indexes failed", e);
        }
    }

    // -------------------------------------------------------------------------------------------------- Public Methods

    @Override
    public HyperNode createHyperNode() {
        return buildHyperNode(neo4j.createNode());
    }

    @Override
    public HyperEdge createHyperEdge() {
        Node auxiliaryNode = neo4j.createNode();
        auxiliaryNode.setProperty(KEY_IS_HYPER_EDGE, true);
        return buildHyperEdge(auxiliaryNode);
    }

    // ---

    @Override
    public HyperNode getHyperNode(long id) {
        return buildHyperNode(neo4j.getNodeById(id));
    }

    @Override
    public HyperNode getHyperNode(String key, Object value) {
        // FIXME: new index API doesn't work with OSGi
        // Node node = exactIndex.get(key, value).getSingle();
        Node node = exactIndex.getSingleNode(key, value);
        return node != null ? buildHyperNode(node) : null;
    }

    @Override
    public List<HyperNode> queryHyperNodes(Object value) {
        return queryHyperNodes(KEY_FULLTEXT, value);
    }

    @Override
    public List<HyperNode> queryHyperNodes(String key, Object value) {
        List nodes = new ArrayList();
        // FIXME: new index API doesn't work with OSGi
        // for (Node node : fulltextIndex.query(key, value)) {
        for (Node node : fulltextIndex.getNodes(key, value)) {
            nodes.add(buildHyperNode(node));
        }
        return nodes;
    }

    // ---

    @Override
    public HyperEdge getHyperEdge(long id) {
        return buildHyperEdge(neo4j.getNodeById(id));
    }

    // ---

    @Override
    public HyperGraphTransaction beginTx() {
        return new Neo4jTransactionAdapter(neo4j);
    }

    @Override
    public void shutdown() {
        logger.info("Shutdown DB");
        neo4j.shutdown();
    }

    // ------------------------------------------------------------------------------------------------- Private Methods
}
