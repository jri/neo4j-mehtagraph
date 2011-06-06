package de.deepamehta.hypergraph.impl;

import de.deepamehta.hypergraph.HyperObject;
import de.deepamehta.hypergraph.HyperObjectRole;

import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;



class Neo4jHyperObjectRole extends HyperObjectRole {

    // ---------------------------------------------------------------------------------------------- Instance Variables

    private Relationship rel;

    // ---------------------------------------------------------------------------------------------------- Constructors

    Neo4jHyperObjectRole(HyperObject hyperObject, String roleType, Relationship rel) {
        super(hyperObject, roleType);
        this.rel = rel;
    }

    // -------------------------------------------------------------------------------------------------- Public Methods

    // === HyperObjectRole Overrides ===

    @Override
    public void setRoleType(String roleType) {
        // update memory
        super.setRoleType(roleType);
        // update DB
        storeRoleType(roleType);
    }

    // ------------------------------------------------------------------------------------------------- Private Methods

    private void storeRoleType(String roleType) {
        Node startNode = rel.getStartNode();
        Node endNode = rel.getEndNode();
        rel.delete();
        RelationshipType relType = ((Neo4jHyperObject) getHyperObject()).getRelationshipType(roleType);
        startNode.createRelationshipTo(endNode, relType);
    }
}
