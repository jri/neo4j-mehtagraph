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
            hyperObjects.add(new Neo4jHyperObjectRole(hyperObject, roleType, rel));
        }
        // sanity check
        if (hyperObjects.size() != 2) {
            // Note: custom toString() stringifier called here to avoid endless recursion.
            // The default stringifier would call getHyperObjects() again and fail endlessly.
            throw new RuntimeException("Graph inconsistency: hyper edge " + getId() + " connects " +
                hyperObjects.size() + " hyper objects instead of 2 (" + toString(hyperObjects) + ")");
        }
        //
        return hyperObjects;
    }

    @Override
    public HyperObjectRole getHyperObject(long objectId) {
        List<HyperObjectRole> roles = getHyperObjects();
        long id1 = roles.get(0).getHyperObject().getId();
        long id2 = roles.get(1).getHyperObject().getId();
        //
        if (id1 == objectId && id2 == objectId) {
            throw new RuntimeException("Self-connected hyper objects are not supported (" + this + ")");
        }
        //
        if (id1 == objectId) {
            return roles.get(0);
        } else if (id2 == objectId) {
            return roles.get(1);
        } else {
            throw new RuntimeException("Hyper object " + objectId + " plays no role in " + this);
        }
    }

    @Override
    public HyperObject getHyperObject(String roleType) {
        Relationship rel = node.getSingleRelationship(getRelationshipType(roleType), Direction.OUTGOING);
        if (rel == null) return null;
        return buildHyperObject(rel.getEndNode());
    }



    // === HyperObject Overrides ===

    @Override
    public void delete() {
        // delete all the node's relationships
        // FIXME: hyper edges connected with this edge gets truncated
        for (Relationship rel : node.getRelationships()) {
            rel.delete();
        }
        //
        node.delete();
    }



    // === Java API ===

    @Override
    public String toString() {
        return toString(getHyperObjects());
    }



    // ----------------------------------------------------------------------------------------- Package Private Methods

    void addHyperObject(HyperObjectRole object) {
        Node dstNode = ((Neo4jHyperObject) object.getHyperObject()).getNode();
        node.createRelationshipTo(dstNode, getRelationshipType(object.getRoleType()));
    }

    // ------------------------------------------------------------------------------------------------- Private Methods

    /**
     * Custom stringifier to avoid endless recursion.
     */
    private String toString(List<HyperObjectRole> hyperObjects) {
        StringBuilder str = new StringBuilder("hyper edge " + getId());
        for (HyperObjectRole hyperObject : hyperObjects) {
            str.append("\n        " + hyperObject);
        }
        return str.toString();
    }
}
