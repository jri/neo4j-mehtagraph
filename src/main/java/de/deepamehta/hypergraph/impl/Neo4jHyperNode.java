package de.deepamehta.hypergraph.impl;

import de.deepamehta.hypergraph.ConnectedHyperNode;
import de.deepamehta.hypergraph.HyperEdge;
import de.deepamehta.hypergraph.HyperNode;
import de.deepamehta.hypergraph.IndexMode;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Path;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.traversal.Evaluation;
import org.neo4j.graphdb.traversal.Evaluator;
import org.neo4j.graphdb.traversal.TraversalDescription;
import org.neo4j.kernel.EmbeddedGraphDatabase;
import org.neo4j.kernel.Traversal;
// FIXME: new index API doesn't work with OSGi
// import org.neo4j.graphdb.index.Index;
//
// Using old index API instead
import org.neo4j.index.IndexHits;
import org.neo4j.index.IndexService;
import org.neo4j.index.lucene.LuceneIndexService;
import org.neo4j.index.lucene.LuceneFulltextQueryIndexService;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;



class Neo4jHyperNode extends Neo4jBase implements HyperNode {

    // ------------------------------------------------------------------------------------------------------- Constants

    // ---------------------------------------------------------------------------------------------- Instance Variables

    private final Logger logger = Logger.getLogger(getClass().getName());

    private Node node;

    // ---------------------------------------------------------------------------------------------------- Constructors

    Neo4jHyperNode(Node node, GraphDatabaseService neo4j, IndexService exactIndex,
                        LuceneFulltextQueryIndexService fulltextIndex
                        /* FIXME: Index exactIndex, Index fulltextIndex */) {
        super(neo4j, exactIndex, fulltextIndex);
        this.node = node;
    }

    // -------------------------------------------------------------------------------------------------- Public Methods

    @Override
    public long getId() {
        return node.getId();
    }

    // === Get Attributes ===

    @Override
    public String getString(String key) {
        return (String) get(key);
    }

    @Override
    public String getString(String key, String defaultValue) {
        return (String) get(key, defaultValue);
    }

    @Override
    public int getInteger(String key) {
        return (Integer) get(key);
    }

    @Override
    public int getInteger(String key, int defaultValue) {
        return (Integer) get(key, defaultValue);
    }

    @Override
    public Object get(String key) {
        return node.getProperty(key);
    }

    @Override
    public Object get(String key, Object defaultValue) {
        return node.getProperty(key, defaultValue);
    }

    // ---

    @Override
    public Iterable<String> getAttributeKeys() {
        return node.getPropertyKeys();
    }

    // ---

    public boolean hasAttribute(String key) {
        return node.hasProperty(key);
    }

    // === Set Attributes ===

    @Override
    public void setAttribute(String key, Object value) {
        setAttribute(key, value, IndexMode.OFF);
    }

    @Override
    public void setAttribute(String key, Object value, IndexMode indexMode) {
        setAttribute(key, value, indexMode, key);
    }

    @Override
    public void setAttribute(String key, Object value, IndexMode indexMode, String indexKey) {
        Object oldValue = get(key, null);
        // 1) update DB
        node.setProperty(key, value);
        // 2) update index
        indexProperty(indexMode, indexKey, value, oldValue);
    }

    // === Traversal ===

    @Override
    public Set<HyperEdge> getHyperEdges(String myRoleType) {
        Set edges = new HashSet();
        for (Relationship rel : node.getRelationships(getRelationshipType(myRoleType), Direction.INCOMING)) {
            Node auxiliaryNode = rel.getStartNode();
            edges.add(buildHyperEdge(auxiliaryNode));
        }
        return edges;
    }

    // ---

    @Override
    public ConnectedHyperNode getConnectedHyperNode(String myRoleType, String othersRoleType) {
        Relationship rel = node.getSingleRelationship(getRelationshipType(myRoleType), Direction.INCOMING);
        if (rel == null) return null;
        Node auxiliaryNode = rel.getStartNode();
        rel = auxiliaryNode.getSingleRelationship(getRelationshipType(othersRoleType), Direction.OUTGOING);
        if (rel == null) return null;
        return new ConnectedHyperNode(buildHyperNode(rel.getEndNode()), auxiliaryNode.getId());
    }

    @Override
    public Set<ConnectedHyperNode> getConnectedHyperNodes(String myRoleType, String othersRoleType) {
        TraversalDescription desc = Traversal.description();
        desc = desc.evaluator(new RoleTypeEvaluator(myRoleType, othersRoleType));
        desc = desc.relationships(getRelationshipType(myRoleType), Direction.INCOMING);
        desc = desc.relationships(getRelationshipType(othersRoleType), Direction.OUTGOING);
        //
        Set result = new HashSet();
        for (Path path : desc.traverse(node)) {
            // sanity check
            if (path.length() != 2) {
                throw new RuntimeException("jri doesn't understand Neo4j traversal");
            }
            //
            HyperNode hyperNode = buildHyperNode(path.endNode());
            long hyperEdgeId = path.lastRelationship().getOtherNode(path.endNode()).getId();
            result.add(new ConnectedHyperNode(hyperNode, hyperEdgeId));
        }
        return result;
    }

    // ---

    @Override
    public String toString() {
        return "hypernode " + node.getId() + " " + getAttributesString(node);
    }

    // ----------------------------------------------------------------------------------------- Package Private Methods

    Node getNode() {
        return node;
    }

    // ------------------------------------------------------------------------------------------------- Private Methods

    // === Indexing ===

    private void indexProperty(IndexMode indexMode, String indexKey, Object value, Object oldValue) {
        if (indexMode == IndexMode.OFF) {
            return;
        } else if (indexMode == IndexMode.KEY) {
            if (oldValue != null) {
                // FIXME: new index API doesn't work with OSGi
                // exactIndex.remove(node, indexKey, oldValue);             // remove old
                exactIndex.removeIndex(node, indexKey, oldValue);           // remove old
            }
            // FIXME: new index API doesn't work with OSGi
            // exactIndex.add(node, indexKey, value);                       // index new
            exactIndex.index(node, indexKey, value);                        // index new
        } else if (indexMode == IndexMode.FULLTEXT) {
            // Note: all the topic's FULLTEXT properties are indexed under the same key ("default").
            // So, when removing from index we must explicitley give the old value.
            if (oldValue != null) {
                // FIXME: new index API doesn't work with OSGi
                // fulltextIndex.remove(node, KEY_FULLTEXT, oldValue);      // remove old
                fulltextIndex.removeIndex(node, KEY_FULLTEXT, oldValue);    // remove old
            }
            // FIXME: new index API doesn't work with OSGi
            // fulltextIndex.add(node, KEY_FULLTEXT, value);                // index new
            fulltextIndex.index(node, KEY_FULLTEXT, value);                 // index new
        } else if (indexMode == IndexMode.FULLTEXT_KEY) {
            if (oldValue != null) {
                // FIXME: new index API doesn't work with OSGi
                // fulltextIndex.remove(node, indexKey, oldValue);          // remove old
                fulltextIndex.removeIndex(node, indexKey, oldValue);        // remove old
            }
            // FIXME: new index API doesn't work with OSGi
            // fulltextIndex.add(node, indexKey, value);                    // index new
            fulltextIndex.index(node, indexKey, value);                     // index new
        } else {
            throw new RuntimeException("Index mode \"" + indexMode + "\" not implemented");
        }
    }

    // === Traversal ===

    private class RoleTypeEvaluator implements Evaluator {

        private RelationshipType myRoleType;
        private RelationshipType othersRoleType;

        private RoleTypeEvaluator(String myRoleType, String othersRoleType) {
            this.myRoleType = getRelationshipType(myRoleType);
            this.othersRoleType = getRelationshipType(othersRoleType);
        }

        @Override
        public Evaluation evaluate(Path path) {
            // sanity checks
            Relationship rel = path.lastRelationship();
            if (path.length() == 1) {
                if (!rel.isType(myRoleType)) {
                    throw new RuntimeException("jri doesn't understand Neo4j traversal or your graph is fucked");
                }
                if (rel.getEndNode().getId() != node.getId() || rel.getStartNode().getId() != path.endNode().getId()) {
                    throw new RuntimeException("jri doesn't understand Neo4j traversal or your graph is fucked");
                }
            } else if (path.length() == 2) {
                if (!rel.isType(othersRoleType)) {
                    throw new RuntimeException("jri doesn't understand Neo4j traversal or your graph is fucked");
                }
                if (rel.getEndNode().getId() != path.endNode().getId()) {
                    throw new RuntimeException("jri doesn't understand Neo4j traversal or your graph is fucked");
                }
            }
            //
            boolean includes = path.length() == 2;
            boolean continues = path.length() < 2;
            return Evaluation.of(includes, continues);
        }
    }
}
