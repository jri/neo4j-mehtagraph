package de.deepamehta.hypergraph;

import java.util.List;
import java.util.Map;
import java.util.Set;



public interface HyperGraph {

    HyperNode createHyperNode();

    HyperEdge createHyperEdge(HyperNode node1, String roleType1, HyperNode node2, String roleType2);
    HyperEdge createHyperEdge(HyperNode node,  String roleType1, HyperEdge edge,  String roleType2);
    HyperEdge createHyperEdge(HyperEdge edge1, String roleType1, HyperEdge edge2, String roleType2);

    // ---

    HyperNode getHyperNode(long id);
    HyperNode getHyperNode(String key, Object value);

    // ---

    List<HyperNode> queryHyperNodes(Object value);
    List<HyperNode> queryHyperNodes(String key, Object value);

    // ---

    HyperEdge getHyperEdge(long id);

    Set<HyperEdge> getHyperEdges(long node1Id, long node2Id);

    // ---

    HyperGraphTransaction beginTx();
    void shutdown();
}
