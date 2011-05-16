package de.deepamehta.hypergraph;

import de.deepamehta.hypergraph.impl.Neo4jHyperGraph;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.index.Index;
import org.neo4j.helpers.collection.MapUtil;
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
        GraphDatabaseService neo4j = new EmbeddedGraphDatabase(createTempDirectory("neo4j"));
        hg = new Neo4jHyperGraph(neo4j);
        //
        setupContent();
    }

    @Test
    public void testTraversal() {
        HyperNode node = hg.getHyperNode("uri", "dm3.core.data_type");
        HyperNode topicType = getType(node);
        logger.info("### topicType=" + topicType);
        assertEquals("dm3.core.topic_type", topicType.getString("uri"));
        assertEquals("Topic Type", topicType.getString("value"));
    }

    @Test
    public void testIndex() {
        List<HyperNode> nodes1 = hg.queryHyperNodes("DeepaMehta");
        assertEquals(2, nodes1.size());
        //
        List<HyperNode> nodes2 = hg.queryHyperNodes("collaboration platform");
        assertEquals(1, nodes2.size());
    }

    @After
    public void shutdown() {
        hg.shutdown();
    }

    // ------------------------------------------------------------------------------------------------- Private Methods

    HyperNode getType(HyperNode node) {
        return node.getConnectedHyperNode("dm3.core.instance", "dm3.core.type").getHyperNode();
    }

    private void setupContent() {
        HyperGraphTransaction tx = hg.beginTx();
        try {
            HyperNode node1 = hg.createHyperNode();
            node1.setString("uri", "dm3.core.topic_type");
            node1.setString("value", "Topic Type");
            node1.indexAttribute(HyperGraphIndexMode.KEY, "uri", "dm3.core.topic_type", null);
            //
            HyperNode node2 = hg.createHyperNode();
            node2.setString("uri", "dm3.core.data_type");
            node2.setString("value", "Data Type");
            node2.indexAttribute(HyperGraphIndexMode.KEY, "uri", "dm3.core.data_type", null);
            //
            HyperEdge edge = hg.createHyperEdge(node1, "dm3.core.type", node2, "dm3.core.instance");
            //
            String text1 = "DeepaMehta is a platform for collaboration and knowledge management";
            String text2 = "Lead developer of DeepaMehta is Jörg Richter";
            //
            HyperNode node3 = hg.createHyperNode();
            node3.setString("uri", "note-1");
            node3.setString("value", text1);
            node3.indexAttribute(HyperGraphIndexMode.FULLTEXT, text1, null);
            //
            HyperNode node4 = hg.createHyperNode();
            node4.setString("uri", "note-2");
            node4.setString("value", text2);
            node4.indexAttribute(HyperGraphIndexMode.FULLTEXT, text2, null);
            //
            tx.success();
        } finally {
            tx.finish();
        }
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
