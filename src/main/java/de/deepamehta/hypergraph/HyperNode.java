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
    Object getObject(String key);
    Object getObject(String key, Object defaultValue);

    // ---

    Iterable<String> getAttributeKeys();

    boolean hasAttribute(String key);



    // === Set Attributes ===

    /**
     * @throws  IllegalArgumentException    if value is null
     */
    String setString(String key, String value);

    Integer setInteger(String key, int value);

    Boolean setBoolean(String key, boolean value);

    /**
     * @throws  IllegalArgumentException    if value is null
     */
    Object setObject(String key, Object value);



    // === Indexing ===

    void indexAttribute(HyperGraphIndexMode indexMode, Object value, Object oldValue);
    void indexAttribute(HyperGraphIndexMode indexMode, String indexKey, Object value, Object oldValue);



    // === Traversal ===

    Set<HyperEdge> getHyperEdges();
    Set<HyperEdge> getHyperEdges(String myRoleType);

    Set<ConnectedHyperNode> getConnectedHyperNodes();

    ConnectedHyperNode getConnectedHyperNode(String myRoleType, String othersRoleType);
    Set<ConnectedHyperNode> getConnectedHyperNodes(String myRoleType, String othersRoleType);

    ConnectedHyperEdge getConnectedHyperEdge(String myRoleType, String othersRoleType);
    Set<ConnectedHyperEdge> getConnectedHyperEdges(String myRoleType, String othersRoleType);



    // === Deletion ===

    void delete();
}
