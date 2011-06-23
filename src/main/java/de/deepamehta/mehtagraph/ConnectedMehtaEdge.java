package de.deepamehta.mehtagraph;



public class ConnectedMehtaEdge {

    // ---------------------------------------------------------------------------------------------- Instance Variables

    private MehtaEdge mehtaEdge;
    private MehtaEdge connectingMehtaEdge;

    // ---------------------------------------------------------------------------------------------------- Constructors

    public ConnectedMehtaEdge(MehtaEdge mehtaEdge, MehtaEdge connectingMehtaEdge) {
        this.mehtaEdge = mehtaEdge;
        this.connectingMehtaEdge = connectingMehtaEdge;
    }

    // -------------------------------------------------------------------------------------------------- Public Methods

    public MehtaEdge getMehtaEdge() {
        return mehtaEdge;
    }

    public MehtaEdge getConnectingMehtaEdge() {
        return connectingMehtaEdge;
    }
}
