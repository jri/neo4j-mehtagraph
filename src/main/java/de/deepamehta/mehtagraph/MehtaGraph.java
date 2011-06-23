package de.deepamehta.mehtagraph;

import java.util.List;
import java.util.Set;



public interface MehtaGraph {

    MehtaNode createMehtaNode();
    MehtaEdge createMehtaEdge(MehtaObjectRole object1, MehtaObjectRole object2);

    // ---

    MehtaNode getMehtaNode(long id);
    MehtaNode getMehtaNode(String key, Object value);

    // ---

    List<MehtaNode> queryMehtaNodes(Object value);
    List<MehtaNode> queryMehtaNodes(String key, Object value);

    // ---

    MehtaEdge getMehtaEdge(long id);
    Set<MehtaEdge> getMehtaEdges(long node1Id, long node2Id);

    // ---

    MehtaGraphTransaction beginTx();
    void shutdown();
}
