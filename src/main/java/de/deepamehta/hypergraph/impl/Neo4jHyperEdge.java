package de.deepamehta.hypergraph.impl;

import de.deepamehta.hypergraph.HyperEdge;
import de.deepamehta.hypergraph.HyperObject;
import de.deepamehta.hypergraph.HyperObjectRole;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;



class Neo4jHyperEdge extends Neo4jHyperObject implements HyperEdge {

    // ---------------------------------------------------------------------------------------------- Instance Variables

    private final Logger logger = Logger.getLogger(getClass().getName());

    // ---------------------------------------------------------------------------------------------------- Constructors

    Neo4jHyperEdge(Node node, Neo4jBase base) {
        super(node, base);
    }

    // -------------------------------------------------------------------------------------------------- Public Methods



    // === HyperEdge Implementation ===

    @Override
    public List<HyperObjectRole> getHyperObjects() {
        List<HyperObjectRole> hyperObjects = new ArrayList();
        for (Relationship rel : node.getRelationships(Direction.OUTGOING)) {
            Node node = rel.getEndNode();
            String roleType = rel.getType().name();
            HyperObject hyperObject = buildHyperObject(node);
            hyperObjects.add(new HyperObjectRole(hyperObject, roleType));
        }
        return hyperObjects;
    }

    @Override
    public HyperObject getHyperObject(String roleType) {
        Relationship rel = node.getSingleRelationship(getRelationshipType(roleType), Direction.OUTGOING);
        if (rel == null) return null;
        return buildHyperObject(rel.getEndNode());
    }

    // --- Deletion ---

    @Override
    public void delete() {
        // delete all the node's relationships
        for (Relationship rel : node.getRelationships()) {
            rel.delete();
        }
        //
        node.delete();
    }



    // === Java API ===

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder("hyper edge " + node.getId());
        List<HyperObjectRole> hyperObjects = getHyperObjects();
        str.append("\n        " + hyperObjects.get(0));
        str.append("\n        " + hyperObjects.get(1));
        return str.toString();
    }



    // ----------------------------------------------------------------------------------------- Package Private Methods

    void addHyperObject(HyperObjectRole object) {
        Node dstNode = ((Neo4jHyperObject) object.getHyperObject()).getNode();
        node.createRelationshipTo(dstNode, getRelationshipType(object.getRoleType()));
    }
}
