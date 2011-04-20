package de.deepamehta.hypergraph;

import java.util.List;
import java.util.Map;
import java.util.Set;



public interface HyperGraph {

    HyperNode createHyperNode();
    HyperEdge createHyperEdge();

    // ---

    HyperNode getHyperNode(long id);
    HyperNode getHyperNode(String key, Object value);

    List<HyperNode> queryHyperNodes(Object value);
    List<HyperNode> queryHyperNodes(String key, Object value);

    // ---

    HyperEdge getHyperEdge(long id);

    Set<HyperEdge> getHyperEdges(long node1Id, long node2Id);

    // ---

    HyperGraphTransaction beginTx();
    void shutdown();
}
