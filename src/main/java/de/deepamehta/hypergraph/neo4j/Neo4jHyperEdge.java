package de.deepamehta.hypergraph.neo4j;

import de.deepamehta.hypergraph.HyperEdge;
import de.deepamehta.hypergraph.HyperNode;

import org.neo4j.graphdb.DynamicRelationshipType;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;

import java.util.logging.Logger;



class Neo4jHyperEdge extends Neo4jBase implements HyperEdge {

    // ---------------------------------------------------------------------------------------------- Instance Variables

    private final Logger logger = Logger.getLogger(getClass().getName());

    private Node auxiliaryNode;

    // ---------------------------------------------------------------------------------------------------- Constructors

    Neo4jHyperEdge(Node auxiliaryNode, GraphDatabaseService neo4j) {
        super(neo4j);
        this.auxiliaryNode = auxiliaryNode;
    }

    // -------------------------------------------------------------------------------------------------- Public Methods

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
