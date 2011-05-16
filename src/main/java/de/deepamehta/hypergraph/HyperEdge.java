package de.deepamehta.hypergraph;

import java.util.Set;



public interface HyperEdge {

    long getId();

    // ---

    HyperNode getHyperNode(String roleType);

    // ---

    Set<HyperNodeRole> getHyperNodes();

    Set<HyperEdgeRole> getHyperEdges();

    // === Get Attributes ===

    Iterable<String> getAttributeKeys();

    // === Set Attributes ===

    void setAttribute(String key, Object value);

    // === Traversal ===

    ConnectedHyperNode getConnectedHyperNode(String myRoleType, String othersRoleType);
    Set<ConnectedHyperNode> getConnectedHyperNodes(String myRoleType, String othersRoleType);

    ConnectedHyperEdge getConnectedHyperEdge(String myRoleType, String othersRoleType);
    Set<ConnectedHyperEdge> getConnectedHyperEdges(String myRoleType, String othersRoleType);

    // === Deletion ===

    void delete();
}
