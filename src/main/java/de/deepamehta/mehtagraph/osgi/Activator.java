package de.deepamehta.mehtagraph.osgi;

import de.deepamehta.mehtagraph.MehtaGraph;
import de.deepamehta.mehtagraph.MehtaGraphFactory;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import java.util.Map;
import java.util.logging.Logger;



public class Activator implements BundleActivator {

    // ------------------------------------------------------------------------------------------------------- Constants

    private static final String DATABASE_PATH = System.getProperty("dm4.database.path");

    // ------------------------------------------------------------------------------------------------- Class Variables

    private MehtaGraph mg;

    // ---------------------------------------------------------------------------------------------- Instance Variables

    private Logger logger = Logger.getLogger(getClass().getName());

    // -------------------------------------------------------------------------------------------------- Public Methods



    // **************************************
    // *** BundleActivator Implementation ***
    // **************************************



    @Override
    public void start(BundleContext context) {
        try {
            logger.info("========== Starting bundle \"Neo4j Mehtagraph\" ==========");
            logger.info("Opening DB and indexing services (path=" + DATABASE_PATH + ")");
            this.mg = MehtaGraphFactory.createInstance(DATABASE_PATH);
            //
            logger.info("Registering Neo4j Mehtagraph service at OSGi framework");
            context.registerService(MehtaGraph.class.getName(), mg, null);
        } catch (Exception e) {
            logger.severe("Activation of Neo4j Mehtagraph failed:");
            e.printStackTrace();
            // Note: an exception thrown from here is swallowed by the container without reporting
            // and let File Install retry to start the bundle endlessly.
        }
    }

    @Override
    public void stop(BundleContext context) {
        logger.info("========== Stopping Neo4j Mehtagraph ==========");
        if (mg != null) {
            mg.shutdown();
        }
    }
}
