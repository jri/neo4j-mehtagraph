package de.deepamehta.mehtagraph;

import de.deepamehta.mehtagraph.impl.Neo4jMehtaGraph;

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



public class MehtaGraphTest {

    private MehtaGraph hg;

    private final Logger logger = Logger.getLogger(getClass().getName());

    // -------------------------------------------------------------------------------------------------- Public Methods

    @Before
    public void setup() {
        GraphDatabaseService neo4j = new EmbeddedGraphDatabase(createTempDirectory("neo4j"));
        hg = new Neo4jMehtaGraph(neo4j);
        //
        setupContent();
    }

    @Test
    public void testTraversal() {
        MehtaNode node = hg.getMehtaNode("uri", "dm4.core.data_type");
        MehtaNode topicType = getType(node);
        logger.info("### topicType=" + topicType);
        assertEquals("dm4.core.topic_type", topicType.getString("uri"));
        assertEquals("Topic Type", topicType.getString("value"));
    }

    @Test
    public void testIndex() {
        List<MehtaNode> nodes1 = hg.queryMehtaNodes("DeepaMehta");
        assertEquals(2, nodes1.size());
        //
        List<MehtaNode> nodes2 = hg.queryMehtaNodes("collaboration platform");
        assertEquals(1, nodes2.size());
    }

    @After
    public void shutdown() {
        hg.shutdown();
    }

    // ------------------------------------------------------------------------------------------------- Private Methods

    MehtaNode getType(MehtaNode node) {
        return node.getConnectedMehtaNode("dm4.core.instance", "dm4.core.type").getMehtaNode();
    }

    private void setupContent() {
        MehtaGraphTransaction tx = hg.beginTx();
        try {
            MehtaNode node1 = hg.createMehtaNode();
            node1.setString("uri", "dm4.core.topic_type");
            node1.setString("value", "Topic Type");
            node1.indexAttribute(MehtaGraphIndexMode.KEY, "uri", "dm4.core.topic_type", null);
            //
            MehtaNode node2 = hg.createMehtaNode();
            node2.setString("uri", "dm4.core.data_type");
            node2.setString("value", "Data Type");
            node2.indexAttribute(MehtaGraphIndexMode.KEY, "uri", "dm4.core.data_type", null);
            //
            MehtaEdge edge = hg.createMehtaEdge(new MehtaObjectRole(node1, "dm4.core.type"),
                                                new MehtaObjectRole(node2, "dm4.core.instance"));
            //
            String text1 = "DeepaMehta is a platform for collaboration and knowledge management";
            String text2 = "Lead developer of DeepaMehta is Jörg Richter";
            //
            MehtaNode node3 = hg.createMehtaNode();
            node3.setString("uri", "note-1");
            node3.setString("value", text1);
            node3.indexAttribute(MehtaGraphIndexMode.FULLTEXT, text1, null);
            //
            MehtaNode node4 = hg.createMehtaNode();
            node4.setString("uri", "note-2");
            node4.setString("value", text2);
            node4.indexAttribute(MehtaGraphIndexMode.FULLTEXT, text2, null);
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
