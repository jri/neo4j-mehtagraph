package de.deepamehta.hypergraph;



public class HyperObjectRole {

    // ---------------------------------------------------------------------------------------------- Instance Variables

    private HyperObject hyperObject;
    private String roleType;

    // ---------------------------------------------------------------------------------------------------- Constructors

    public HyperObjectRole(HyperObject hyperObject, String roleType) {
        this.hyperObject = hyperObject;
        this.roleType = roleType;
    }

    // -------------------------------------------------------------------------------------------------- Public Methods

    public HyperObject getHyperObject() {
        return hyperObject;
    }

    // ---

    public String getRoleType() {
        return roleType;
    }

    public void setRoleType(String roleType) {
        this.roleType = roleType;
    }

    // ---

    @Override
    public String toString() {
        return "hyper object role (roleType=\"" + roleType + "\": " + hyperObject + ")";
    }
}
