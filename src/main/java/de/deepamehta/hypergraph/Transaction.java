package de.deepamehta.hypergraph;



public interface Transaction {

    void success();

    void failure();

    void finish();
}
