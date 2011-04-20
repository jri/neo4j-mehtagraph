package de.deepamehta.hypergraph.impl;

import de.deepamehta.hypergraph.ConnectedHyperEdge;
import de.deepamehta.hypergraph.ConnectedHyperNode;
import de.deepamehta.hypergraph.HyperEdge;
import de.deepamehta.hypergraph.HyperNode;
import de.deepamehta.hypergraph.IndexMode;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
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

    // ---------------------------------------------------------------------------------------------- Instance Variables

    /**
     * The underlying Neo4j node.
     */
    private Node node;

    private final Logger logger = Logger.getLogger(getClass().getName());

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
    public boolean getBoolean(String key) {
        return (Boolean) get(key);
    }

    @Override
    public boolean getBoolean(String key, boolean defaultValue) {
        return (Boolean) get(key, defaultValue);
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
    public Set<HyperEdge> getHyperEdges() {
        return getHyperEdges(null);
    }

    @Override
    public Set<HyperEdge> getHyperEdges(String myRoleType) {
        Iterable<Relationship> rels;
        if (myRoleType == null) {
            rels = node.getRelationships(Direction.INCOMING);
        } else {
            rels = node.getRelationships(getRelationshipType(myRoleType), Direction.INCOMING);
        }
        //
        return buildHyperEdges(rels);
    }

    // ---

    @Override
    public Set<ConnectedHyperNode> getConnectedHyperNodes() {
        return getConnectedHyperNodes(null, null);
    }

    // ---

    @Override
    public ConnectedHyperNode getConnectedHyperNode(String myRoleType, String othersRoleType) {
        return getConnectedHyperNode(node, myRoleType, othersRoleType);
    }

    @Override
    public Set<ConnectedHyperNode> getConnectedHyperNodes(String myRoleType, String othersRoleType) {
        return getConnectedHyperNodes(node, myRoleType, othersRoleType);
    }

    // ---

    @Override
    public ConnectedHyperEdge getConnectedHyperEdge(String myRoleType, String othersRoleType) {
        return getConnectedHyperEdge(node, myRoleType, othersRoleType);
    }

    @Override
    public Set<ConnectedHyperEdge> getConnectedHyperEdges(String myRoleType, String othersRoleType) {
        return getConnectedHyperEdges(node, myRoleType, othersRoleType);
    }

    // === Deletion ===

    @Override
    public void delete() {
        node.delete();
    }

    // ---

    @Override
    public String toString() {
        return "hyper node " + node.getId() + " " + getAttributesString(node);
    }

    // ----------------------------------------------------------------------------------------- Package Private Methods

    Node getNode() {
        return node;
    }

    // ------------------------------------------------------------------------------------------------- Private Methods

    private Set<HyperEdge> buildHyperEdges(Iterable<Relationship> relationships) {
        Set edges = new HashSet();
        for (Relationship rel : relationships) {
            Node auxiliaryNode = rel.getStartNode();
            edges.add(buildHyperEdge(auxiliaryNode));
        }
        return edges;
    }

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
}
