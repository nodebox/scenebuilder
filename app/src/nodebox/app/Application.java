package nodebox.app;

import nodebox.node.Node;
import nodebox.node.NodeInfo;
import nodebox.node.NodeManager;
import nodebox.node.Scene;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Application implements BundleActivator {

    private static Application instance;
    private static Logger logger = Logger.getLogger(Application.class.getName());

    public static Application getInstance() {
        return instance;
    }

    public void start(BundleContext context) throws Exception {
        System.out.println("Starting up application.");
        instance = this;
        this.bundleContext = context;
        manager = getNodeManager(context);
        initializeNodeManager();
        createNewDocument();
    }

    public void stop(BundleContext context) throws Exception {
        System.out.println("Shutting down application.");
        for (SceneDocument document : documents) {
            document.close();
        }
        instance = null;
    }

    private String version;
    private NodeManager manager;
    private java.util.List<SceneDocument> documents = new ArrayList<SceneDocument>();
    private SceneDocument currentDocument;
    private JFrame hiddenFrame;
    private BundleContext bundleContext;

    public Application() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        System.setProperty("apple.laf.useScreenMenuBar", "true");
        readVersion();
        //registerForMacOSXEvents();
    }

    private void readVersion() {
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream("version.properties"));
            version = properties.getProperty("app.version");
        } catch (IOException e) {
            throw new RuntimeException("Could not read NodeBox version file. Please re-install NodeBox.", e);
        }
    }

    private void registerForMacOSXEvents() throws RuntimeException {
        if (!PlatformUtils.isMac()) return;
        try {
            // Generate and register the OSXAdapter, passing it a hash of all the methods we wish to
            // use as delegates for various com.apple.eawt.ApplicationListener methods
            OSXAdapter.setQuitHandler(this, getClass().getDeclaredMethod("quit", (Class[]) null));
        } catch (Exception e) {
            throw new RuntimeException("Error while loading the OS X Adapter.", e);
        }
        // Create hidden window.
        hiddenFrame = new JFrame();
        hiddenFrame.setJMenuBar(new MenuBar());
        hiddenFrame.setUndecorated(true);
        hiddenFrame.setSize(0, 0);
        hiddenFrame.pack();
        hiddenFrame.setVisible(true);
    }

    public boolean quit() {
        // Because documents will disappear from the list once they are closed,
        // make a copy of the list.
        java.util.List<SceneDocument> documents = new ArrayList<SceneDocument>(getDocuments());
        for (SceneDocument d : documents) {
            if (!d.shouldClose())
                return false;
        }
        if (hiddenFrame != null) {
            hiddenFrame.dispose();
        }
        System.exit(0);
        return true;
    }

    public String getVersion() {
        return version;
    }

    private NodeManager getNodeManager(BundleContext context) {
        ServiceReference ref = context.getServiceReference(NodeManager.class.getName());
        return (NodeManager) context.getService(ref);
    }

    /**
     * Given a valid node manager, go through all the bundles and find the declared node classes.
     */
    private void initializeNodeManager() {
        for (Bundle bundle : bundleContext.getBundles()) {
            loadNodeClassesFromBundle(bundle);
        }
    }

    /**
     * Go through the bundle, find the declared node classes, and add them to the node manager.
     *
     * @param bundle the bundle to inspect.
     */
    public void loadNodeClassesFromBundle(Bundle bundle) {
        String exportedNodes = getBundleHeader(bundle, "Export-Node");
        if (exportedNodes == null) return;
        for (String exportedNode : exportedNodes.split(",")) {
            exportedNode = exportedNode.trim();
            try {
                Class<? extends Node> nodeClass = (Class<? extends Node>) loadNodeClassFromBundle(bundle, exportedNode);
                if (nodeClass == null) {
                    logger.warning("Bundle " + bundle.getSymbolicName() + ": could not load class " + exportedNode);
                    continue;
                }
                manager.registerNodeClass(nodeClass);
            } catch (ClassCastException e) {
                logger.warning("Bundle " + bundle.getSymbolicName() + ": "+ exportedNode + " is not a Node class.");
            }
        }
    }

    private String getBundleHeader(Bundle bundle, String header) {
        Dictionary d = bundle.getHeaders();
        Object value = d.get(header);
        return value == null ? null : value.toString();
    }

    private Class loadNodeClassFromBundle(Bundle bundle, String nodeClass) {
        try {
            return bundle.loadClass(nodeClass);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    //// Document management ////

    public java.util.List<SceneDocument> getDocuments() {
        return documents;
    }

    public int getDocumentCount() {
        return documents.size();
    }

    public void removeDocument(SceneDocument document) {
        documents.remove(document);
    }

    public SceneDocument createNewDocument() {
        SceneDocument doc = new SceneDocument(manager, new Scene());
        addDocument(doc);
        return doc;
    }

    public boolean openDocument(File file) {
        // Check if the document is already open.
        String path;
        try {
            path = file.getCanonicalPath();
            for (SceneDocument doc : Application.getInstance().getDocuments()) {
                try {
                    if (doc.getDocumentFile() == null) continue;
                    if (doc.getDocumentFile().getCanonicalPath().equals(path)) {
                        // The document is already open. Bring it to the front.
                        doc.toFront();
                        doc.requestFocus();
                        MenuBar.addRecentFile(file);
                        return true;
                    }
                } catch (IOException e) {
                    logger.log(Level.WARNING, "The document " + doc.getDocumentFile() + " refers to path with errors", e);
                }
            }
        } catch (IOException e) {
            logger.log(Level.WARNING, "The document " + file + " refers to path with errors", e);
        }

        try {
            SceneDocument doc = new SceneDocument(manager, file);
            addDocument(doc);
            MenuBar.addRecentFile(file);
            return true;
        } catch (RuntimeException e) {
            logger.log(Level.SEVERE, "Error while loading " + file, e);
            ExceptionDialog d = new ExceptionDialog(null, e);
            d.setVisible(true);
            return false;
        }
    }

    private void addDocument(SceneDocument doc) {
        doc.setVisible(true);
        doc.requestFocus();
        documents.add(doc);
        currentDocument = doc;
    }

    public SceneDocument getCurrentDocument() {
        return currentDocument;
    }

    void setCurrentDocument(SceneDocument document) {
        currentDocument = document;
    }

    public void showNodeLibraryManager() {
        NodeLibraryManager manager = new NodeLibraryManager(bundleContext);
        manager.setVisible(true);
    }
}
