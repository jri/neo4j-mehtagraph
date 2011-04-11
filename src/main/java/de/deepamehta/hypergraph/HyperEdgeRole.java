package de.deepamehta.hypergraph;



public class HyperEdgeRole {

    // ---------------------------------------------------------------------------------------------- Instance Variables

    private HyperEdge hyperEdge;
    private String roleType;

    // ---------------------------------------------------------------------------------------------------- Constructors

    public HyperEdgeRole(HyperEdge hyperEdge, String roleType) {
        this.hyperEdge = hyperEdge;
        this.roleType = roleType;
    }

    // -------------------------------------------------------------------------------------------------- Public Methods

    public HyperEdge getHyperEdge() {
        return hyperEdge;
    }

    public String getRoleType() {
        return roleType;
    }
}
