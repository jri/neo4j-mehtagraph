package de.deepamehta.hypergraph;

import java.util.List;



public interface HyperEdge extends HyperObject {

    List<HyperObjectRole> getHyperObjects();

    HyperObjectRole getHyperObject(long objectId);

    /**
     * Returns the hyper object that plays the given role in this hyper edge.
     * <p>
     * If more than one such hyper object exists, an exception is thrown.
     */
    HyperObject getHyperObject(String roleType);
}
