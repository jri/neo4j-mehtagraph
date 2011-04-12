package de.deepamehta.hypergraph;



public class ConnectedHyperEdge {

    // ---------------------------------------------------------------------------------------------- Instance Variables

    private HyperEdge hyperEdge;
    private HyperEdge connectingHyperEdge;

    // ---------------------------------------------------------------------------------------------------- Constructors

    public ConnectedHyperEdge(HyperEdge hyperEdge, HyperEdge connectingHyperEdge) {
        this.hyperEdge = hyperEdge;
        this.connectingHyperEdge = connectingHyperEdge;
    }

    // -------------------------------------------------------------------------------------------------- Public Methods

    public HyperEdge getHyperEdge() {
        return hyperEdge;
    }

    public HyperEdge getConnectingHyperEdge() {
        return connectingHyperEdge;
    }
}
