package de.deepamehta.hypergraph.neo4j;

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

    // ---

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

    // ---

    public boolean hasAttribute(String key) {
        return node.hasProperty(key);
    }

    // ---

    @Override
    public HyperNode traverse(String myRoleType, String othersRoleType) {
        Relationship rel = node.getSingleRelationship(getRelationshipType(myRoleType), Direction.INCOMING);
        if (rel == null) return null;
        Node auxiliaryNode = rel.getStartNode();
        rel = auxiliaryNode.getSingleRelationship(getRelationshipType(othersRoleType), Direction.OUTGOING);
        if (rel == null) return null;
        return buildHyperNode(rel.getEndNode());
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
