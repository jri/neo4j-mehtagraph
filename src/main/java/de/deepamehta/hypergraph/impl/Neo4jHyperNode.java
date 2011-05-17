package de.deepamehta.hypergraph.impl;

import de.deepamehta.hypergraph.HyperNode;

import org.neo4j.graphdb.Node;



class Neo4jHyperNode extends Neo4jHyperObject implements HyperNode {

    // ---------------------------------------------------------------------------------------------------- Constructors

    Neo4jHyperNode(Node node, Neo4jBase base) {
        super(node, base);
    }

    // -------------------------------------------------------------------------------------------------- Public Methods

    @Override
    public String toString() {
        return "hyper node " + node.getId() + " " + getAttributesString(node);
    }
}
