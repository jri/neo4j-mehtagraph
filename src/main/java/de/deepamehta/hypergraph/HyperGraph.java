package de.deepamehta.hypergraph;

import java.util.Map;



public interface HyperGraph {

    public HyperNode createHyperNode(Map<String, Object> properties);

    public HyperEdge createHyperEdge(String edgeType, Map<String, Object> properties);

    // ---

    public Transaction beginTx();

    public void shutdown();
}
