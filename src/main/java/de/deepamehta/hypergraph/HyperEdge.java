package de.deepamehta.hypergraph;

import java.util.List;



public interface HyperEdge extends HyperObject {

    List<HyperObjectRole> getHyperObjects();

    HyperObject getHyperObject(String roleType);
}
