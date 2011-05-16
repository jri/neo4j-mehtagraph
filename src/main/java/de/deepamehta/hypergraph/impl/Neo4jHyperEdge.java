package de.deepamehta.hypergraph.impl;

import de.deepamehta.hypergraph.ConnectedHyperNode;
import de.deepamehta.hypergraph.ConnectedHyperEdge;
import de.deepamehta.hypergraph.HyperEdge;
import de.deepamehta.hypergraph.HyperEdgeRole;
import de.deepamehta.hypergraph.HyperNode;
import de.deepamehta.hypergraph.HyperNodeRole;

import org.neo4j.graphdb.Direction;
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

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;



class Neo4jHyperEdge extends Neo4jBase implements HyperEdge {

    // ---------------------------------------------------------------------------------------------- Instance Variables

    /**
     * The underlying Neo4j node.
     */
    private Node auxiliaryNode;

    private final Logger logger = Logger.getLogger(getClass().getName());

    // ---------------------------------------------------------------------------------------------------- Constructors

    Neo4jHyperEdge(Node auxiliaryNode, GraphDatabaseService neo4j, Neo4jRelationtypeCache relTypeCache,
                   IndexService exactIndex, LuceneFulltextQueryIndexService fulltextIndex
                   /* FIXME: Index exactIndex, Index fulltextIndex */) {
        super(neo4j, relTypeCache, exactIndex, fulltextIndex);
        this.auxiliaryNode = auxiliaryNode;
    }

    // -------------------------------------------------------------------------------------------------- Public Methods

    @Override
    public long getId() {
        return auxiliaryNode.getId();
    }

    // ---

    @Override
    public HyperNode getHyperNode(String roleType) {
        Relationship rel = auxiliaryNode.getSingleRelationship(getRelationshipType(roleType), Direction.OUTGOING);
        if (rel == null) return null;
        return buildHyperNode(rel.getEndNode());
    }

    // ---

    @Override
    public Set<HyperNodeRole> getHyperNodes() {
        Set<HyperNodeRole> nodeRoles = new HashSet();
        for (Relationship rel : auxiliaryNode.getRelationships(Direction.OUTGOING)) {
            Node node = rel.getEndNode();
            if (isAuxiliaryNode(node)) {
                continue;
            }
            String roleType = rel.getType().name();
            nodeRoles.add(new HyperNodeRole(buildHyperNode(node), roleType));
        }
        return nodeRoles;
    }

    @Override
    public Set<HyperEdgeRole> getHyperEdges() {
        Set<HyperEdgeRole> edgeRoles = new HashSet();
        for (Relationship rel : auxiliaryNode.getRelationships(Direction.OUTGOING)) {
            Node node = rel.getEndNode();
            if (!isAuxiliaryNode(node)) {
                continue;
            }
            String roleType = rel.getType().name();
            edgeRoles.add(new HyperEdgeRole(buildHyperEdge(node), roleType));
        }
        return edgeRoles;
    }

    // === Get Attributes ===

    @Override
    public Iterable<String> getAttributeKeys() {
        return auxiliaryNode.getPropertyKeys();
    }

    // === Set Attributes ===

    @Override
    public void setAttribute(String key, Object value) {
        auxiliaryNode.setProperty(key, value);
    }

    // === Traversal ===

    @Override
    public ConnectedHyperNode getConnectedHyperNode(String myRoleType, String othersRoleType) {
        return getConnectedHyperNode(auxiliaryNode, myRoleType, othersRoleType);
    }

    @Override
    public Set<ConnectedHyperNode> getConnectedHyperNodes(String myRoleType, String othersRoleType) {
        return getConnectedHyperNodes(auxiliaryNode, myRoleType, othersRoleType);
    }

    // ---

    @Override
    public ConnectedHyperEdge getConnectedHyperEdge(String myRoleType, String othersRoleType) {
        return getConnectedHyperEdge(auxiliaryNode, myRoleType, othersRoleType);
    }

    @Override
    public Set<ConnectedHyperEdge> getConnectedHyperEdges(String myRoleType, String othersRoleType) {
        return getConnectedHyperEdges(auxiliaryNode, myRoleType, othersRoleType);
    }

    // === Deletion ===

    @Override
    public void delete() {
        // delete all the node's relationships
        for (Relationship rel : auxiliaryNode.getRelationships()) {
            rel.delete();
        }
        //
        auxiliaryNode.delete();
    }

    // ---

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder("hyper edge " + auxiliaryNode.getId());
        for (HyperNodeRole nodeRole : getHyperNodes()) {
            str.append("\n        " + nodeRole.getHyperNode() + ", roleType=\"" + nodeRole.getRoleType() + "\"");
        }
        for (HyperEdgeRole edgeRole : getHyperEdges()) {
            str.append("\n        " + edgeRole.getHyperEdge() + ", roleType=\"" + edgeRole.getRoleType() + "\"");
        }
        return str.toString();
    }

    // ----------------------------------------------------------------------------------------- Package Private Methods

    void addHyperNode(HyperNode node, String roleType) {
        Node dstNode = ((Neo4jHyperNode) node).getNode();
        auxiliaryNode.createRelationshipTo(dstNode, getRelationshipType(roleType));
    }

    void addHyperEdge(HyperEdge edge, String roleType) {
        Node dstNode = ((Neo4jHyperEdge) edge).getAuxiliaryNode();
        auxiliaryNode.createRelationshipTo(dstNode, getRelationshipType(roleType));
    }

    // ---

    Node getAuxiliaryNode() {
        return auxiliaryNode;
    }

    // ------------------------------------------------------------------------------------------------- Private Methods

}
