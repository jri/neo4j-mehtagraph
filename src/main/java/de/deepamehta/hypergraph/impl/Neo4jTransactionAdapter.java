package de.deepamehta.hypergraph.impl;

import de.deepamehta.hypergraph.HyperGraphTransaction;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Transaction;



/**
 * Adapts a Neo4j transaction to a HyperGraph transaction.
 */
class Neo4jTransactionAdapter implements HyperGraphTransaction {

    private Transaction tx;

    Neo4jTransactionAdapter(GraphDatabaseService neo4j) {
        tx = neo4j.beginTx();
    }

    public void success() {
        tx.success();
    }

    public void failure() {
        tx.failure();
    }

    public void finish() {
        tx.finish();
    }
}
