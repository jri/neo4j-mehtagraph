package de.deepamehta.hypergraph;



public interface HyperEdge {

    void addHyperNode(HyperNode node, String roleType);

    void addHyperEdge(HyperEdge edge, String roleType);
}
