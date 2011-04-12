package de.deepamehta.hypergraph;



public class ConnectedHyperNode {

    // ---------------------------------------------------------------------------------------------- Instance Variables

    private HyperNode hyperNode;
    private HyperEdge connectingHyperEdge;

    // ---------------------------------------------------------------------------------------------------- Constructors

    public ConnectedHyperNode(HyperNode hyperNode, HyperEdge connectingHyperEdge) {
        this.hyperNode = hyperNode;
        this.connectingHyperEdge = connectingHyperEdge;
    }

    // -------------------------------------------------------------------------------------------------- Public Methods

    public HyperNode getHyperNode() {
        return hyperNode;
    }

    public HyperEdge getConnectingHyperEdge() {
        return connectingHyperEdge;
    }
}
