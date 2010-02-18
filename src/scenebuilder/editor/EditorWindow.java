package scenebuilder.editor;

import scenebuilder.library.animation.LFO;
import scenebuilder.library.image.generator.Source;
import scenebuilder.library.render.Clear;
import scenebuilder.library.render.Sprite;
import scenebuilder.model.Macro;
import scenebuilder.model.Node;
import scenebuilder.model.Scene;
import scenebuilder.render.SceneRenderer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

public class EditorWindow extends JFrame {
    private SceneViewer viewer;
    private ParameterPanel parameters;
    private final Scene scene;
    private Thread rendererThread;

    public EditorWindow(Scene scene) throws HeadlessException {
        super("Editor");
        this.scene = scene;
        setSize(800, 600);
        JPanel content = new JPanel(new BorderLayout(0, 0));
        viewer = new SceneViewer(scene);
        parameters = new ParameterPanel();
        viewer.addPropertyChangeListener(SceneViewer.SELECT_PROPERTY, parameters);
        content.add(viewer, BorderLayout.CENTER);
        content.add(parameters, BorderLayout.EAST);
        setContentPane(content);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Setup renderer
         rendererThread = new Thread() {
            @Override
            public void run() {
                try {
                    SceneRenderer renderer = new SceneRenderer(EditorWindow.this.scene);
                    renderer.init();
                    renderer.run();
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

    public void close() {
        rendererThread.interrupt();
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        dispose();
    }

    public static void imageMacro(Macro root, String fileName, int y) {
        // Background image
        Source source = new Source();
        source.setPosition(new Point(50, y));
        source.loadImage(new File(fileName));
        Sprite sprite = new Sprite();
        sprite.setPosition(new Point(250, y));
        sprite.setValue(Sprite.PORT_WIDTH, 851.0 / 500.0 * 200.0);
        sprite.setValue(Sprite.PORT_HEIGHT, 200.0);
        root.addChild(source);
        root.addChild(sprite);
        root.connect(source, Source.PORT_IMAGE, sprite, Sprite.PORT_IMAGE);
    }


    public static void main(String[] args) {
        // Initialize scene
        final Scene scene = new Scene();
        Macro root = scene.getRootMacro();
        Node clear = new Clear();
        clear.setPosition(new Point(250, 50));
        clear.setValue(Clear.PORT_COLOR, Color.DARK_GRAY);
        Node lfo = new LFO();
        lfo.setPosition(new Point(50, 50));
        Node sprite = new Sprite();
        sprite.setPosition(new Point(250, 150));
        sprite.setValue(Sprite.PORT_COLOR, Color.RED);
        root.addChild(lfo);
        root.addChild(clear);
        root.addChild(sprite);
        root.connect(lfo, LFO.PORT_RESULT, sprite, Sprite.PORT_X);

        // Load full scene.
        int y = 300;
        imageMacro(root, "images/background.png", y += 200);
        imageMacro(root, "images/middle.png", y += 200);
        imageMacro(root, "images/colors.png", y += 200);
        imageMacro(root, "images/creatures.png", y += 200);
        imageMacro(root, "images/foreground.png", y += 200);
        imageMacro(root, "images/manysparkles.png", y += 200);


        EditorWindow win = new EditorWindow(scene);
        win.setVisible(true);
    }

}
