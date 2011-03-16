package de.deepamehta.hypergraph;



public interface HyperGraphTransaction {

    void success();

    void failure();

    void finish();
}
