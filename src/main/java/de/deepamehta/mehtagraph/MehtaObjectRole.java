package de.deepamehta.mehtagraph;



public class MehtaObjectRole {

    // ---------------------------------------------------------------------------------------------- Instance Variables

    private MehtaObject mehtaObject;
    private String roleType;

    // ---------------------------------------------------------------------------------------------------- Constructors

    public MehtaObjectRole(MehtaObject mehtaObject, String roleType) {
        this.mehtaObject = mehtaObject;
        this.roleType = roleType;
    }

    // -------------------------------------------------------------------------------------------------- Public Methods

    public MehtaObject getMehtaObject() {
        return mehtaObject;
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
        return "mehta object role (roleType=\"" + roleType + "\": " + mehtaObject + ")";
    }
}
