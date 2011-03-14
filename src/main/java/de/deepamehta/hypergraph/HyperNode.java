package de.deepamehta.hypergraph;



public interface HyperNode {

    public long getId();

    public String getString(String key);
    public String getString(String key, String defaultValue);
    public int getInteger(String key);
    public int getInteger(String key, int defaultValue);
    public Object get(String key);
    public Object get(String key, Object defaultValue);

    public Iterable<String> getAttributeKeys();

    public void setAttribute(String key, Object value);
    public void setAttribute(String key, Object value, IndexMode indexMode);
    public void setAttribute(String key, Object value, IndexMode indexMode, String indexKey);

    public boolean hasAttribute(String key);

    public HyperNode traverse(String myRoleType, String othersRoleType);
}
