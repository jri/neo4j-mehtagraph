package de.deepamehta.hypergraph;



public interface HyperNode {

    public String getString(String key);

    public HyperNode traverse(String myRoleType, String edgeType, String othersRoleType);
}
