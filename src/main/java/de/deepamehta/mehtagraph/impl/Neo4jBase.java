package de.deepamehta.mehtagraph.impl;

import de.deepamehta.mehtagraph.MehtaObject;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Path;
import org.neo4j.graphdb.traversal.Evaluation;
import org.neo4j.graphdb.traversal.Evaluator;
import org.neo4j.graphdb.traversal.TraversalDescription;
// ### FIXME: new index API doesn't work with OSGi
import org.neo4j.graphdb.index.Index;
//
// ### Using old index API instead
// ### import org.neo4j.index.IndexService;
// ### import org.neo4j.index.lucene.LuceneFulltextQueryIndexService;
import org.neo4j.kernel.Traversal;
import org.neo4j.kernel.Uniqueness;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;



class Neo4jBase {

    // ------------------------------------------------------------------------------------------------------- Constants

    protected static final String KEY_IS_MEHTA_EDGE = "_is_mehta_edge";
    protected static final String KEY_FULLTEXT = "_fulltext";

    // ---------------------------------------------------------------------------------------------- Instance Variables

    private final Logger logger = Logger.getLogger(getClass().getName());

    protected final GraphDatabaseService neo4j;
    protected Neo4jRelationtypeCache relTypeCache;
    // ### FIXME: new index API doesn't work with OSGi
    protected Index<Node> exactIndex;
    protected Index<Node> fulltextIndex;
    //
    // ### Using old index API instead
    // ### protected IndexService exactIndex;
    // ### protected LuceneFulltextQueryIndexService fulltextIndex;

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

    // ----------------------------------------------------------------------------------------------- Protected Methods

    protected final Neo4jMehtaNode buildMehtaNode(Node node) {
        if (node == null) {
            throw new IllegalArgumentException("Tried to build a MehtaNode from a null Node");
        }
        if (isAuxiliaryNode(node)) {
            throw new IllegalArgumentException("ID " + node.getId() + " refers not to a MehtaNode but to a MehtaEdge");
        }
        return new Neo4jMehtaNode(node, this);
    }

    protected final Neo4jMehtaEdge buildMehtaEdge(Node auxiliaryNode) {
        if (auxiliaryNode == null) {
            throw new IllegalArgumentException("Tried to build a MehtaEdge from a null auxiliary Node");
        }
        if (!isAuxiliaryNode(auxiliaryNode)) {
            throw new IllegalArgumentException("ID " + auxiliaryNode.getId() + " refers not to a MehtaEdge but to " +
                "a MehtaNode");
        }
        return new Neo4jMehtaEdge(auxiliaryNode, this);
    }

    protected final MehtaObject buildMehtaObject(Node node) {
        return isAuxiliaryNode(node) ? buildMehtaEdge(node) : buildMehtaNode(node);
    }

    // ---

    protected final boolean isAuxiliaryNode(Node node) {
        return (Boolean) node.getProperty(KEY_IS_MEHTA_EDGE, false);
    }



    // === Traversal ===

    /**
     * The created traversal description allows to find all mehta edges between two mehta nodes.
     * <p>
     * Called from {@link Neo4jMehtaGraph#getMehtaEdges}
     */
    protected final TraversalDescription createTraversalDescription(long connectedNodeId) {
        return Traversal.description()
            .evaluator(new AuxiliaryEvaluator())
            .evaluator(new ConnectedNodeEvaluator(connectedNodeId))
            .uniqueness(Uniqueness.RELATIONSHIP_GLOBAL);
        // Note: we need to traverse a node more than once. Consider this case: mehta node A
        // is connected with mehta node B via mehta edge C and A is connected to C as well.
        // (default uniqueness is not RELATIONSHIP_GLOBAL, but probably NODE_GLOBAL).
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
