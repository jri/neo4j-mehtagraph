
Neo4j Mehtagraph
================

A persistence layer for Mehtagraphs on-top of [Neo4j](http://neo4j.org/).

We call it a Mehtagraph because it is a generalization of a graph: edges can not only connect nodes, but other edges as well! That is, an edge can be a player in an edge, just like nodes usually. Edges are still binary (so its not a *hypergraph*): an edge connects either 2 nodes, a node and an edge, or 2 edges.

Further features of this implementation:

* Nodes and edges are attributed (supported attribute types are string, integer, boolean).
* Attribute indexing and retrieval (fulltext or exact value).
* The role a node/edge occupies in an edge is modeled as *role type*.
* Traversal based on role types.

When do you might want a Mehtagraph? In geneal, when you need to make the edges themself objects of discourse, just like the nodes. Some examples:

* Nodes/subgraphs as edge meta-data. ACL example: the creator of an edge can be represented as a "user" node that is connected to that edge (analog the node creator).
* Sorting edges. A specific order of a set of edges can be represented by connecting them with "sequence" edges, each with a "predecessor" and "successor" end (= role types).
* Sub-graphs. A sub-graph can be represented as a node that connects the nodes and edges that belong to it. A node/edge can be involved in any number of sub-graphs.

Neo4j Mehtagraph is utilized as the persistence layer for [DeepaMehta 4.0](https://github.com/jri/deepamehta).  
However, it can be used for other applications as well.


Are you a mathematician?
------------------------

Is a graph where an edge can connect other edges a known structure? What's its name?


Version History
---------------

**v0.9** -- May 19, 2012

* Extended API: retrieve edges by node IDs *and* role types
* Compatible with DeepaMehta 4.0.11

**v0.8** -- Jul 28, 2011

* More flexible role type filter
* API provides factory method (Neo4j is no longer exposed to the application developer)

**v0.7** -- Jul 24, 2011

* Bug fix in edge deletion (no "Graph inconsistency" error anymore)

**v0.6** -- Jun 24, 2011

* Data model change: limit edges to binary ones
* Edges have the same attribute/indexing/traversal capabilities like nodes
* Extended API to work with role types
* Project is named "Neo4j Mehtagraph" now

**v0.5** -- May 15, 2011

* Relationship type cache
* API changes, e.g. setting attributes decoupled from indexing

**v0.4** -- Apr 23, 2011

* More traversal, especially edge-connected edges
* Extended API, e.g. deletion of nodes and edges
* Sanity checks

**v0.3** -- Mar 20, 2011

* More traversal

**v0.2** -- Mar 13, 2011

* Indexing
* Query index

**v0.1** -- Mar 8, 2011

* Creating hyper nodes and hyper edges
* Basic traversal.


------------
Jörg Richter  
May 19, 2012
