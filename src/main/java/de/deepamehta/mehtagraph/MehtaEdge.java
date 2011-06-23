package de.deepamehta.mehtagraph;

import java.util.List;



public interface MehtaEdge extends MehtaObject {

    List<MehtaObjectRole> getMehtaObjects();

    MehtaObjectRole getMehtaObject(long objectId);

    /**
     * Returns the mehta object that plays the given role in this mehta edge.
     * <p>
     * If more than one such mehta object exists, an exception is thrown.
     */
    MehtaObject getMehtaObject(String roleType);
}
