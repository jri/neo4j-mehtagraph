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
            node1.setAttribute("uri", "dm3.core.topic_type", IndexMode.KEY);
            node1.setAttribute("value", "Topic Type");
            //
            HyperNode node2 = hg.createHyperNode();
            node2.setAttribute("uri", "dm3.core.data_type", IndexMode.KEY);
            node2.setAttribute("value", "Data Type", IndexMode.KEY, "dm3.core.topic_type");
            //
            HyperEdge edge = hg.createHyperEdge();
            edge.addHyperNode(node1, "dm3.core.type");
            edge.addHyperNode(node2, "dm3.core.instance");
            //
            HyperNode node3 = hg.createHyperNode();
            node3.setAttribute("uri", "note-1", IndexMode.KEY);
            node3.setAttribute("value", "DeepaMehta is a platform for collaboration and knowledge management",
                IndexMode.FULLTEXT, "dm3.notes.text");
            //
            HyperNode node4 = hg.createHyperNode();
            node4.setAttribute("uri", "note-2", IndexMode.KEY);
            node4.setAttribute("value", "Lead developer of DeepaMehta is Jörg Richter",
                IndexMode.FULLTEXT, "dm3.workspaces.description");
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
