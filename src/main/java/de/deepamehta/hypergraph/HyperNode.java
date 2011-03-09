package de.deepamehta.hypergraph;



public interface HyperNode {

    public void setAttribute(String key, Object value);
    public void setAttribute(String key, Object value, IndexMode indexMode);
    public void setAttribute(String key, Object value, IndexMode indexMode, String indexKey);

    public Iterable<String> getAttributeKeys();

    public String getString(String key);

    public HyperNode traverse(String myRoleType, String edgeType, String othersRoleType);
}
