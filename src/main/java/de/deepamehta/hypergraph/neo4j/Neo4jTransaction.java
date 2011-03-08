package de.deepamehta.hypergraph.neo4j;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Transaction;



class Neo4jTransaction implements de.deepamehta.hypergraph.Transaction {

    private Transaction tx;

    Neo4jTransaction(GraphDatabaseService neo4j) {
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
