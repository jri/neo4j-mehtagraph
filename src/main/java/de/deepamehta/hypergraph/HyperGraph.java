package de.deepamehta.hypergraph;

import java.util.List;
import java.util.Map;



public interface HyperGraph {

    public HyperNode createHyperNode();
    public HyperEdge createHyperEdge(String edgeType);

    // ---

    public HyperNode getHyperNode(long id);
    public HyperNode getHyperNode(String key, Object value);

    public List<HyperNode> queryHyperNodes(Object value);
    public List<HyperNode> queryHyperNodes(String key, Object value);

    // ---

    public Transaction beginTx();

    public void shutdown();
}
