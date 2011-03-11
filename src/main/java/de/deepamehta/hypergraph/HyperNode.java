package de.deepamehta.hypergraph;



public interface HyperNode {

    public void setAttribute(String key, Object value);
    public void setAttribute(String key, Object value, IndexMode indexMode);
    public void setAttribute(String key, Object value, IndexMode indexMode, String indexKey);

    public Object get(String key);
    public Object get(String key, Object defaultValue);
    public String getString(String key);
    public String getString(String key, Object defaultValue);

    public Iterable<String> getAttributeKeys();

    public HyperNode traverse(String myRoleType, String edgeType, String othersRoleType);
}
