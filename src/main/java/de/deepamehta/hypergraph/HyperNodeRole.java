package de.deepamehta.hypergraph;



public class HyperNodeRole {

    // ---------------------------------------------------------------------------------------------- Instance Variables

    private HyperNode hyperNode;
    private String roleType;

    // ---------------------------------------------------------------------------------------------------- Constructors

    public HyperNodeRole(HyperNode hyperNode, String roleType) {
        this.hyperNode = hyperNode;
        this.roleType = roleType;
    }

    // -------------------------------------------------------------------------------------------------- Public Methods

    public HyperNode getHyperNode() {
        return hyperNode;
    }

    public String getRoleType() {
        return roleType;
    }
}
