package scenebuilder.editor;

import scenebuilder.model.Macro;
import scenebuilder.model.Node;
import scenebuilder.model.NodeManager;
import scenebuilder.model.Scene;
import scenebuilder.render.SceneRenderer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class SceneDocument extends JFrame {

    private SceneViewer viewer;
    private ParameterPanel parameters;
    private AddressBar addressBar;
    private final Scene scene;
    private Macro currentMacro;
    private Thread rendererThread;
    private JMenuBar sceneMenuBar;
    private SceneRenderer renderer;

    public SceneDocument(Scene scene) throws HeadlessException {
        super("Editor");
        this.scene = scene;
        currentMacro = scene.getRootMacro();
        initMenu();
        setSize(800, 600);
        JPanel content = new JPanel(new BorderLayout(0, 0));
        addressBar = new AddressBar(this);
        viewer = new SceneViewer(this, scene);
        parameters = new ParameterPanel(this);
        viewer.addPropertyChangeListener(SceneViewer.SELECT_PROPERTY, parameters);
        content.add(addressBar, BorderLayout.NORTH);
        content.add(viewer, BorderLayout.CENTER);
        content.add(parameters, BorderLayout.WEST);
        setContentPane(content);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Setup renderer
        rendererThread = new Thread() {
            @Override
            public void run() {
                try {
                    renderer = new SceneRenderer(SceneDocument.this.scene);
                    renderer.init();
                    renderer.run();
                    System.out.println("renderer finished");
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        };
        rendererThread.start();
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                close();
            }
        });
    }

    private void initMenu() {
        sceneMenuBar = new JMenuBar();
        JMenu sceneMenu = new JMenu("Scene");
        sceneMenu.add(new SceneDocument.SwitchSceneAction("Basic Animation", "basicLFOScene"));
        sceneMenu.add(new SceneDocument.SwitchSceneAction("Mouse Input", "mouseScene"));
        sceneMenu.add(new SceneDocument.SwitchSceneAction("Pong Macro", "pongMacroScene"));
        sceneMenu.add(new SceneDocument.SwitchSceneAction("Creatures", "creaturesScene"));
        sceneMenuBar.add(sceneMenu);
        JMenu createMenu = new JMenu("Create");
        NodeManager m = NodeManager.getInstance();
        for (String category : m.getNodeCategories()) {
            JMenu categoryMenu = new JMenu(category);
            for (Class<? extends Node> nodeClass : m.getNodeClasses(category)) {
                categoryMenu.add(new CreateNodeAction(nodeClass.getSimpleName(), nodeClass));
            }
            createMenu.add(categoryMenu);
        }
        sceneMenuBar.add(createMenu);
        setJMenuBar(sceneMenuBar);
    }

    public Scene getScene() {
        return scene;
    }

    public Macro getCurrentMacro() {
        return currentMacro;
    }

    public void setCurrentMacro(Macro currentMacro) {
        this.currentMacro = currentMacro;
        viewer.setCurrentMacro(currentMacro);
        addressBar.repaint();

    }

    private void createNode(Class nodeClass) {
        try {
            Node n = (Node) nodeClass.newInstance();
            scene.getRootMacro().addChild(n);
            n.setPosition((int) Math.random() * 300, (int) Math.random() * 300);
            viewer.updateView();
        } catch (Exception e1) {
            throw new RuntimeException(e1);
        }
    }

    public void close() {
        renderer.stop();
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        dispose();
    }


    public static class SwitchSceneAction extends AbstractAction {
        public final String sceneId;

        public SwitchSceneAction(String name, String id) {
            super(name);
            sceneId = id;
        }

        public void actionPerformed(ActionEvent e) {
            Application.getInstance().loadScene(sceneId);
        }
    }

    public class CreateNodeAction extends AbstractAction {
        public final Class nodeClass;

        public CreateNodeAction(String name, Class nodeClass) {
            super(name);
            this.nodeClass = nodeClass;
        }

        public void actionPerformed(ActionEvent e) {
            createNode(nodeClass);
        }
    }

}
