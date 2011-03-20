
Neo4j Hypergraph
================

A persistence layer for hypergraphs on-top of Neo4j. In mathematics, a [hypergraph](http://en.wikipedia.org/wiki/Hypergraph) is a generalization of a graph, where an edge can connect any number of nodes (not only two). In this implementation nodes and edges are attributed. The role a node occupies in an edge is modeled as *role type*.

**This implementation goes beyond hypergraphs:** An edge can not only connect nodes, but other edges as well! (So, *Neo4j Hypergraph* is not quite an accurate name.)

Neo4j Hypergraph is utilized as the storage layer for upcoming [DeepaMehta 3](https://github.com/jri/deepamehta3) v0.5.


Are you a mathematician?
------------------------

Is a graph where an edge can link to other edges a known structure? What's its name?


Version History
---------------

**v0.3** -- Mar 20, 2011

* More traversal

**v0.2** -- Mar 13, 2011

* Indexing
* Query index

**v0.1** -- Mar 8, 2011

* Creating hypernodes and hyperedges
* Basic traversal.


------------
Jörg Richter  
Mar 20, 2011
