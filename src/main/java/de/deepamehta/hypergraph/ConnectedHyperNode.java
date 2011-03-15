package de.deepamehta.hypergraph;



public class ConnectedHyperNode {

    // ---------------------------------------------------------------------------------------------- Instance Variables

    private HyperNode hyperNode;
    private long hyperEdgeId;

    // ---------------------------------------------------------------------------------------------------- Constructors

    public ConnectedHyperNode(HyperNode hyperNode, long hyperEdgeId) {
        this.hyperNode = hyperNode;
        this.hyperEdgeId = hyperEdgeId;
    }

    // -------------------------------------------------------------------------------------------------- Public Methods

    public HyperNode getHyperNode() {
        return hyperNode;
    }

    public long getHyperEdgeId() {
        return hyperEdgeId;
    }
}
