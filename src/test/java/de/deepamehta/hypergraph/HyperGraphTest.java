package de.deepamehta.hypergraph;

import de.deepamehta.hypergraph.neo4j.Neo4jHyperGraph;

import static org.junit.Assert.assertEquals;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import org.neo4j.kernel.EmbeddedGraphDatabase;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;



public class HyperGraphTest {

    private HyperGraph hg;

    private final Logger logger = Logger.getLogger(getClass().getName());

    // -------------------------------------------------------------------------------------------------- Public Methods

    @Before
    public void setup() {
        hg = new Neo4jHyperGraph(new EmbeddedGraphDatabase(createTempDirectory("neo4j")));
    }

    @Test
    public void test() {
        Transaction tx = hg.beginTx();
        try {
            Map p1 = new HashMap();
            p1.put("uri", "dm3.core.topic_type");
            p1.put("value", "Topic Type");
            HyperNode node1 = hg.createHyperNode(p1);
            //
            Map p2 = new HashMap();
            p2.put("uri", "dm3.core.data_type");
            p2.put("value", "Data Type");
            HyperNode node2 = hg.createHyperNode(p2);
            //
            HyperEdge edge = hg.createHyperEdge("dm3.core.instantiation", null);
            edge.addHyperNode(node1, "dm3.core.type");
            edge.addHyperNode(node2, "dm3.core.instance");
            //
            HyperNode topicType = getType(node2);
            logger.info("### topicType=" + topicType);
            assertEquals("dm3.core.topic_type", topicType.getString("uri"));
            assertEquals("Topic Type", topicType.getString("value"));
            //
            tx.success();
        } finally {
            tx.finish();
        }
    }

    @After
    public void shutdown() {
        hg.shutdown();
    }

    // ------------------------------------------------------------------------------------------------- Private Methods

    HyperNode getType(HyperNode node) {
        return node.traverse("dm3.core.instance", "dm3.core.instantiation", "dm3.core.type");
    }

    // ---

    private String createTempDirectory(String prefix) {
        try {
            File f = File.createTempFile(prefix, ".dir");
            String n = f.getAbsolutePath();
            f.delete();
            new File(n).mkdir();
            return n;
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}