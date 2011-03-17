package de.deepamehta.hypergraph;

import java.util.Set;



public interface HyperNode {

    public long getId();

    // === Get Attributes ===

    /**
     * @throws  Exception    if node has no attribute with that key
     */
    public String getString(String key);
    public String getString(String key, String defaultValue);

    /**
     * @throws  Exception    if node has no attribute with that key
     */
    public int getInteger(String key);
    public int getInteger(String key, int defaultValue);

    /**
     * @throws  Exception    if node has no attribute with that key
     */
    public Object get(String key);
    public Object get(String key, Object defaultValue);

    public Iterable<String> getAttributeKeys();

    public boolean hasAttribute(String key);

    // === Set Attributes ===

    /**
     * @throws  IllegalArgumentException    if value is null
     */
    public void setAttribute(String key, Object value);

    /**
     * @throws  IllegalArgumentException    if value is null
     */
    public void setAttribute(String key, Object value, IndexMode indexMode);

    /**
     * @throws  IllegalArgumentException    if value is null
     */
    public void setAttribute(String key, Object value, IndexMode indexMode, String indexKey);

    // === Traversal ===

    public Set<HyperEdge> getHyperEdges(String myRoleType);

    public ConnectedHyperNode getConnectedHyperNode(String myRoleType, String othersRoleType);
    public Set<ConnectedHyperNode> getConnectedHyperNodes(String myRoleType, String othersRoleType);
}
