package de.deepamehta.hypergraph;

import java.util.Set;



public interface HyperNode {

    long getId();

    // === Get Attributes ===

    /**
     * @throws  Exception    if node has no attribute with that key
     */
    String getString(String key);
    String getString(String key, String defaultValue);

    /**
     * @throws  Exception    if node has no attribute with that key
     */
    int getInteger(String key);
    int getInteger(String key, int defaultValue);

    /**
     * @throws  Exception    if node has no attribute with that key
     */
    boolean getBoolean(String key);
    boolean getBoolean(String key, boolean defaultValue);

    /**
     * @throws  Exception    if node has no attribute with that key
     */
    Object get(String key);
    Object get(String key, Object defaultValue);

    Iterable<String> getAttributeKeys();

    boolean hasAttribute(String key);

    // === Set Attributes ===

    /**
     * @throws  IllegalArgumentException    if value is null
     */
    void setAttribute(String key, Object value);

    /**
     * @throws  IllegalArgumentException    if value is null
     */
    void setAttribute(String key, Object value, IndexMode indexMode);

    /**
     * @throws  IllegalArgumentException    if value is null
     */
    void setAttribute(String key, Object value, IndexMode indexMode, String indexKey);

    // === Traversal ===

    Set<HyperEdge> getHyperEdges(String myRoleType);

    Set<ConnectedHyperNode> getConnectedHyperNodes();

    ConnectedHyperNode getConnectedHyperNode(String myRoleType, String othersRoleType);
    Set<ConnectedHyperNode> getConnectedHyperNodes(String myRoleType, String othersRoleType);

    ConnectedHyperEdge getConnectedHyperEdge(String myRoleType, String othersRoleType);
    Set<ConnectedHyperEdge> getConnectedHyperEdges(String myRoleType, String othersRoleType);
}
