package de.deepamehta.hypergraph;

import java.util.Set;



public interface HyperNode {

    public long getId();

    // === Get Attributes ===

    public String getString(String key);
    public String getString(String key, String defaultValue);
    public int getInteger(String key);
    public int getInteger(String key, int defaultValue);
    public Object get(String key);
    public Object get(String key, Object defaultValue);

    public Iterable<String> getAttributeKeys();

    public boolean hasAttribute(String key);

    // === Set Attributes ===

    public void setAttribute(String key, Object value);
    public void setAttribute(String key, Object value, IndexMode indexMode);
    public void setAttribute(String key, Object value, IndexMode indexMode, String indexKey);

    // === Traversal ===

    public Set<HyperEdge> getHyperEdges(String myRoleType);

    public HyperNode traverseSingle(String myRoleType, String othersRoleType);
    public Set<ConnectedHyperNode> traverse(String myRoleType, String othersRoleType);
}
