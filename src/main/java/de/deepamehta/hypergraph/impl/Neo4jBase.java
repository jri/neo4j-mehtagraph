package de.deepamehta.hypergraph.impl;

import de.deepamehta.hypergraph.HyperObject;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Path;
import org.neo4j.graphdb.traversal.Evaluation;
import org.neo4j.graphdb.traversal.Evaluator;
import org.neo4j.graphdb.traversal.TraversalDescription;
// FIXME: new index API doesn't work with OSGi
// import org.neo4j.graphdb.index.Index;
//
// Using old index API instead
import org.neo4j.index.IndexService;
import org.neo4j.index.lucene.LuceneFulltextQueryIndexService;
import org.neo4j.kernel.Traversal;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;



class Neo4jBase {

    // ------------------------------------------------------------------------------------------------------- Constants

    protected static final String KEY_IS_HYPER_EDGE = "_is_hyper_edge";
    protected static final String KEY_FULLTEXT = "_fulltext";

    // ---------------------------------------------------------------------------------------------- Instance Variables

    private final Logger logger = Logger.getLogger(getClass().getName());

    protected final GraphDatabaseService neo4j;
    protected Neo4jRelationtypeCache relTypeCache;
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

    protected Neo4jBase(Neo4jBase base) {
        this.neo4j = base.neo4j;
        this.relTypeCache = base.relTypeCache;
        this.exactIndex = base.exactIndex;
        this.fulltextIndex = base.fulltextIndex;
    }

    // Neo4jBase(GraphDatabaseService neo4j, Neo4jRelationtypeCache relTypeCache,
    //          IndexService exactIndex, LuceneFulltextQueryIndexService fulltextIndex
    //          /* FIXME: Index exactIndex, Index fulltextIndex */) {
    //    this.neo4j = neo4j;
    //    this.relTypeCache = relTypeCache;
    //    this.exactIndex = exactIndex;
    //    this.fulltextIndex = fulltextIndex;
    // }

    // ----------------------------------------------------------------------------------------------- Protected Methods

    protected final Neo4jHyperNode buildHyperNode(Node node) {
        if (node == null) {
            throw new IllegalArgumentException("Tried to build a HyperNode from a null Node");
        }
        if (isAuxiliaryNode(node)) {
            throw new IllegalArgumentException("ID " + node.getId() + " refers not to a HyperNode but to a HyperEdge");
        }
        return new Neo4jHyperNode(node, this);
    }

    protected final Neo4jHyperEdge buildHyperEdge(Node auxiliaryNode) {
        if (auxiliaryNode == null) {
            throw new IllegalArgumentException("Tried to build a HyperEdge from a null auxiliary Node");
        }
        if (!isAuxiliaryNode(auxiliaryNode)) {
            throw new IllegalArgumentException("ID " + auxiliaryNode.getId() + " refers not to a HyperEdge but to " +
                "a HyperNode");
        }
        return new Neo4jHyperEdge(auxiliaryNode, this);
    }

    protected final HyperObject buildHyperObject(Node node) {
        return isAuxiliaryNode(node) ? buildHyperEdge(node) : buildHyperNode(node);
    }

    // ---

    protected final boolean isAuxiliaryNode(Node node) {
        return (Boolean) node.getProperty(KEY_IS_HYPER_EDGE, false);
    }



    // === Traversal ===

    protected final TraversalDescription createTraversalDescription(long connectedNodeId) {
        TraversalDescription desc = Traversal.description();
        desc = desc.evaluator(new AuxiliaryEvaluator());
        desc = desc.evaluator(new ConnectedNodeEvaluator(connectedNodeId));
        return desc;
    }

    // ---

    protected class AuxiliaryEvaluator implements Evaluator {

        @Override
        public Evaluation evaluate(Path path) {
            Node node = path.endNode();
            // sanity check
            if (path.length() == 1) {
                if (!isAuxiliaryNode(node)) {
                    throw new RuntimeException("jri doesn't understand Neo4j traversal or your graph is corrupted");
                }
            }
            //
            boolean includes = path.length() == 2 && !isAuxiliaryNode(node);
            boolean continues = path.length() < 2;
            return Evaluation.of(includes, continues);
        }
    }

    private class ConnectedNodeEvaluator implements Evaluator {

        private long connectedNodeId;

        private ConnectedNodeEvaluator(long connectedNodeId) {
            this.connectedNodeId = connectedNodeId;
        }

        @Override
        public Evaluation evaluate(Path path) {
            boolean includes = path.endNode().getId() == connectedNodeId;
            boolean continues = true;
            return Evaluation.of(includes, continues);
        }
    }

    protected abstract class TraveralResultBuilder {

        private Set result = new HashSet();

        protected TraveralResultBuilder(Node node, TraversalDescription desc) {
            for (Path path : desc.traverse(node)) {
                // sanity check
                if (path.length() != 2) {
                    throw new RuntimeException("jri doesn't understand Neo4j traversal");
                }
                //
                Node connectedNode = path.endNode();
                Node auxiliaryNode = path.lastRelationship().getStartNode();
                result.add(buildResult(connectedNode, auxiliaryNode));
            }
        }

        abstract Object buildResult(Node connectedNode, Node auxiliaryNode);

        Set getResult() {
            return result;
        }
    }
}
