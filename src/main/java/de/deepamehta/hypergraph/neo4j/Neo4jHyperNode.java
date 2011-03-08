package de.deepamehta.hypergraph.neo4j;

import de.deepamehta.hypergraph.HyperEdge;
import de.deepamehta.hypergraph.HyperNode;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;

import java.util.logging.Logger;



class Neo4jHyperNode extends Neo4jBase implements HyperNode {

    // ---------------------------------------------------------------------------------------------- Instance Variables

    private final Logger logger = Logger.getLogger(getClass().getName());

    private Node node;

    // ---------------------------------------------------------------------------------------------------- Constructors

    Neo4jHyperNode(Node node, GraphDatabaseService neo4j) {
        super(neo4j);
        this.node = node;
    }

    // -------------------------------------------------------------------------------------------------- Public Methods

    @Override
    public String getString(String key) {
        return (String) node.getProperty(key);
    }

    @Override
    public HyperNode traverse(String myRoleType, String edgeType, String othersRoleType) {
        Relationship rel = node.getSingleRelationship(getRelationshipType(myRoleType), Direction.INCOMING);
        if (rel == null) return null;
        Node auxiliaryNode = rel.getStartNode();
        if (!auxiliaryNode.getProperty(KEY_HYPER_EDGE_TYPE).equals(edgeType)) return null;
        rel = auxiliaryNode.getSingleRelationship(getRelationshipType(othersRoleType), Direction.OUTGOING);
        if (rel == null) return null;
        return new Neo4jHyperNode(rel.getEndNode(), neo4j);
    }

    // ---

    @Override
    public String toString() {
        return "hypernode " + node.getId() + " " + getProperties(node);
    }

    // ----------------------------------------------------------------------------------------- Package Private Methods

    Node getNode() {
        return node;
    }
}
