package de.deepamehta.hypergraph;



public interface HyperEdge {

    public void setAttribute(String key, Object value);

    public Iterable<String> getAttributeKeys();

    void addHyperNode(HyperNode node, String roleType);
    void addHyperEdge(HyperEdge edge, String roleType);
}
