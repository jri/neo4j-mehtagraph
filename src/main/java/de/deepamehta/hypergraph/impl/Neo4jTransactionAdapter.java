package de.deepamehta.hypergraph.impl;

import de.deepamehta.hypergraph.HyperGraphTransaction;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Transaction;



/**
 * Adapts a Neo4j transaction to a HyperGraph transaction.
 */
class Neo4jTransactionAdapter implements HyperGraphTransaction {

    // ---------------------------------------------------------------------------------------------- Instance Variables

    private Transaction tx;

    // ---------------------------------------------------------------------------------------------------- Constructors

    Neo4jTransactionAdapter(GraphDatabaseService neo4j) {
        tx = neo4j.beginTx();
    }

    // -------------------------------------------------------------------------------------------------- Public Methods

    @Override
    public void success() {
        tx.success();
    }

    @Override
    public void failure() {
        tx.failure();
    }

    @Override
    public void finish() {
        tx.finish();
    }
}
