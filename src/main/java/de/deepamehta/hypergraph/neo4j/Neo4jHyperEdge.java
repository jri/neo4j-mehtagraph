package de.deepamehta.hypergraph.neo4j;

import de.deepamehta.hypergraph.HyperEdge;
import de.deepamehta.hypergraph.HyperNode;

import org.neo4j.graphdb.DynamicRelationshipType;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
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

import java.util.logging.Logger;



class Neo4jHyperEdge extends Neo4jBase implements HyperEdge {

    // ---------------------------------------------------------------------------------------------- Instance Variables

    private final Logger logger = Logger.getLogger(getClass().getName());

    private Node auxiliaryNode;

    // ---------------------------------------------------------------------------------------------------- Constructors

    Neo4jHyperEdge(Node auxiliaryNode, GraphDatabaseService neo4j, IndexService exactIndex,
                        LuceneFulltextQueryIndexService fulltextIndex
                        /* FIXME: Index exactIndex, Index fulltextIndex */) {
        super(neo4j, exactIndex, fulltextIndex);
        this.auxiliaryNode = auxiliaryNode;
    }

    // -------------------------------------------------------------------------------------------------- Public Methods

    @Override
    public void setAttribute(String key, Object value) {
        auxiliaryNode.setProperty(key, value);
    }

    // ---

    @Override
    public Iterable<String> getAttributeKeys() {
        return auxiliaryNode.getPropertyKeys();
    }

    // ---

    @Override
    public void addHyperNode(HyperNode node, String roleType) {
        Node dstNode = ((Neo4jHyperNode) node).getNode();
        auxiliaryNode.createRelationshipTo(dstNode, getRelationshipType(roleType));
    }

    @Override
    public void addHyperEdge(HyperEdge edge, String roleType) {
        Node dstNode = ((Neo4jHyperEdge) edge).getNode();
        auxiliaryNode.createRelationshipTo(dstNode, getRelationshipType(roleType));
    }

    // ----------------------------------------------------------------------------------------- Package Private Methods

    Node getNode() {
        return auxiliaryNode;
    }

    // ------------------------------------------------------------------------------------------------- Private Methods

}
