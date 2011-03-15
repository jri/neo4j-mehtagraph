package de.deepamehta.hypergraph;



public interface HyperEdge {

    long getId();

    void addHyperNode(HyperNode node, String roleType);
    void addHyperEdge(HyperEdge edge, String roleType);

    HyperNode getHyperNode(String roleType);

    // === Get Attributes ===

    Iterable<String> getAttributeKeys();

    // === Set Attributes ===

    void setAttribute(String key, Object value);
}
