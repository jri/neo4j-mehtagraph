package de.deepamehta.hypergraph;

import java.util.Set;



public interface HyperEdge {

    long getId();

    // ---

    void addHyperNode(HyperNode node, String roleType);

    void addHyperEdge(HyperEdge edge, String roleType);

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

    ConnectedHyperEdge getConnectedHyperEdge(String myRoleType, String othersRoleType);
    Set<ConnectedHyperEdge> getConnectedHyperEdges(String myRoleType, String othersRoleType);
}
