package nodebox.app;

import nodebox.node.Network;
import nodebox.node.Node;
import nodebox.node.NodeManager;
import nodebox.node.Scene;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SceneDocument extends JFrame {

    private static Logger logger = Logger.getLogger(SceneDocument.class.getName());
    public static String lastFilePath;

    private final NodeManager manager;
    private final Scene scene;
    private SceneViewer viewer;
    private ParameterPanel parameters;
    private AddressBar addressBar;
    private Network currentNetwork;
    private Thread rendererThread;
    private JMenuBar sceneMenuBar;
    private SceneRenderer renderer;
    private File documentFile;
    private boolean documentChanged;

    public static SceneDocument getCurrentDocument() {
        return Application.getInstance().getCurrentDocument();
    }

    public SceneDocument(NodeManager manager, File file) {
        this(manager, Scene.load(file, manager));
        setDocumentFile(file);
    }

    public SceneDocument(NodeManager manager, Scene scene) throws HeadlessException {
        super("Editor");
        this.manager = manager;
        this.scene = scene;
        currentNetwork = scene.getRootNetwork();
        setSize(800, 800);
        renderer = new SceneRenderer(scene);
        JPanel networkPanel = new JPanel(new BorderLayout(0, 0));
        addressBar = new AddressBar(this);
        viewer = new SceneViewer(this, scene);
        parameters = new ParameterPanel(this);
        viewer.addPropertyChangeListener(SceneViewer.SELECT_PROPERTY, parameters);
        networkPanel.add(addressBar, BorderLayout.NORTH);
        networkPanel.add(viewer, BorderLayout.CENTER);
        networkPanel.add(parameters, BorderLayout.WEST);
        JSplitPane mainSplitter = new JSplitPane(JSplitPane.VERTICAL_SPLIT, renderer, networkPanel);
        mainSplitter.setDividerSize(7);
        mainSplitter.setDividerLocation(300);
        setContentPane(mainSplitter);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setJMenuBar(new MenuBar(this, manager));
        // Setup renderer
        renderer.init();
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                close();
            }
        });
    }

    private void updateTitle() {
        String postfix = "";
        if (!PlatformUtils.isMac()) { // todo: mac only code
            postfix = (documentChanged ? " *" : "");
        } else {
            getRootPane().putClientProperty("Window.documentModified", documentChanged);
        }
        if (documentFile == null) {
            setTitle("Untitled" + postfix);
        } else {
            setTitle(documentFile.getName() + postfix);
            getRootPane().putClientProperty("Window.documentFile", documentFile);
        }
    }

    //// Document file management ////

    public File getDocumentFile() {
        return documentFile;
    }

    public void setDocumentFile(File documentFile) {
        this.documentFile = documentFile;
        updateTitle();
    }

    public boolean isChanged() {
        return documentChanged;
    }

    public void close() {
        if (shouldClose()) {
            renderer.stop();
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            Application.getInstance().removeDocument(this);
            dispose();
            // On Mac the application does not close if the last window is closed.
//            if (PlatformUtils.isMac()) return;
//            // If there are no more documents, exit the application.
//            if (Application.getInstance().getDocumentCount() == 0) {
//                System.exit(0);
//            }
        }
    }

    public boolean shouldClose() {
        if (isChanged()) {
            SaveDialog sd = new SaveDialog();
            int retVal = sd.show(this);
            if (retVal == JOptionPane.YES_OPTION) {
                return save();
            } else if (retVal == JOptionPane.NO_OPTION) {
                return true;
            } else if (retVal == JOptionPane.CANCEL_OPTION) {
                return false;
            }
        }
        return true;
    }

    public boolean save() {
        if (documentFile == null) {
            return saveAs();
        } else {
            return saveToFile(documentFile);
        }
    }

    public boolean saveAs() {
        File chosenFile = FileUtils.showSaveDialog(this, lastFilePath, "ndbx", "NodeBox File");
        if (chosenFile != null) {
            if (!chosenFile.getAbsolutePath().endsWith(".ndbx")) {
                chosenFile = new File(chosenFile.getAbsolutePath() + ".ndbx");
            }
            lastFilePath = chosenFile.getParentFile().getAbsolutePath();
            setDocumentFile(chosenFile);
            MenuBar.addRecentFile(documentFile);
            return saveToFile(documentFile);
        }
        return false;
    }

    public boolean saveToFile(File file) {
        try {
            scene.save(file);
        } catch (RuntimeException e) {
            JOptionPane.showMessageDialog(this, "An error occurred while saving the file.", "NodeBox", JOptionPane.ERROR_MESSAGE);
            logger.log(Level.SEVERE, "An error occurred while saving the file.", e);
            return false;
        }
        documentChanged = false;
        updateTitle();
        return true;
    }

    //// Scene management ////

    public Scene getScene() {
        return scene;
    }

    public Network getCurrentNetwork() {
        return currentNetwork;
    }

    public void setCurrentNetwork(Network currentNetwork) {
        this.currentNetwork = currentNetwork;
        viewer.setCurrentNetwork(currentNetwork);
        addressBar.repaint();

    }

    public void createNode(Class nodeClass) {
        try {
            Node n = (Node) nodeClass.newInstance();
            getCurrentNetwork().addChild(n);
            n.setPosition((int) Math.random() * 300, (int) Math.random() * 300);
            viewer.updateView();
        } catch (Exception e1) {
            throw new RuntimeException(e1);
        }
    }

}
