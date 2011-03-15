package de.deepamehta.hypergraph;



public interface HyperEdge {

    public long getId();

    public Iterable<String> getAttributeKeys();

    public void setAttribute(String key, Object value);

    void addHyperNode(HyperNode node, String roleType);
    void addHyperEdge(HyperEdge edge, String roleType);
}
