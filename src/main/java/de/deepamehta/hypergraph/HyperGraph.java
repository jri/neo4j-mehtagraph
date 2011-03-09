package de.deepamehta.hypergraph;

import java.util.Map;



public interface HyperGraph {

    public HyperNode createHyperNode();

    public HyperEdge createHyperEdge(String edgeType);

    // ---

    public Transaction beginTx();

    public void shutdown();
}
