package scenebuilder.editor;

import scenebuilder.library.animation.LFO;
import scenebuilder.library.device.Mouse;
import scenebuilder.library.image.generator.Source;
import scenebuilder.library.render.Clear;
import scenebuilder.library.render.Sprite;
import scenebuilder.library.util.color.HSVToColor;
import scenebuilder.model.Macro;
import scenebuilder.model.Node;
import scenebuilder.model.Scene;
import scenebuilder.render.SceneRenderer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

public class SceneDocument extends JFrame {
    private SceneViewer viewer;
    private ParameterPanel parameters;
    private AddressBar addressBar;
    private final Scene scene;
    private Macro currentMacro;
    private Thread rendererThread;

    public SceneDocument(Scene scene) throws HeadlessException {
        super("Editor");
        this.scene = scene;
        currentMacro = scene.getRootMacro();
        setSize(800, 600);
        JPanel content = new JPanel(new BorderLayout(0, 0));
        addressBar = new AddressBar(this);
        viewer = new SceneViewer(scene);
        parameters = new ParameterPanel();
        viewer.addPropertyChangeListener(SceneViewer.SELECT_PROPERTY, parameters);
        content.add(addressBar, BorderLayout.NORTH);
        content.add(viewer, BorderLayout.CENTER);
        content.add(parameters, BorderLayout.EAST);
        setContentPane(content);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Setup renderer
        rendererThread = new Thread() {
            @Override
            public void run() {
                try {
                    SceneRenderer renderer = new SceneRenderer(SceneDocument.this.scene);
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

    public void close() {
        rendererThread.interrupt();
        while(rendererThread.isAlive()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        dispose();
    }

    public static Scene basicLFOScene() {
        Scene scene = new Scene();
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
        return scene;
    }

    public static Scene mouseScene() {
        final Scene scene = new Scene();
        Macro root = scene.getRootMacro();
        Node clear = new Clear();
        clear.setPosition(new Point(450, 50));
        clear.setValue(Clear.PORT_COLOR, Color.DARK_GRAY);
        Mouse mouse = new Mouse();
        mouse.setPosition(new Point(50, 50));
        LFO lfo = new LFO();
        lfo.setPosition(new Point(50, 200));
        lfo.setValue(LFO.PORT_AMPLITUDE, 0.5);
        lfo.setValue(LFO.PORT_OFFSET, 0.5);

        HSVToColor hsvToColor = new HSVToColor();
        hsvToColor.setPosition(new Point(250, 200));
        Node sprite = new Sprite();
        sprite.setPosition(new Point(450, 150));
        sprite.setValue(Sprite.PORT_COLOR, Color.RED);
        sprite.setValue(Sprite.PORT_WIDTH, 20.0);
        sprite.setValue(Sprite.PORT_HEIGHT, 20.0);
        root.addChild(clear);
        root.addChild(mouse);
        root.addChild(lfo);
        root.addChild(hsvToColor);
        root.addChild(sprite);
        root.connect(mouse, Mouse.PORT_X, sprite, Sprite.PORT_X);
        root.connect(mouse, Mouse.PORT_Y, sprite, Sprite.PORT_Y);
        root.connect(lfo, LFO.PORT_RESULT, hsvToColor, HSVToColor.PORT_H);
        root.connect(hsvToColor, HSVToColor.PORT_COLOR, sprite, Sprite.PORT_COLOR);
        return scene;
    }

    public static void imageMacro(Macro root, Mouse mouse, String fileName, double parallax, int y) {
        // Background image
        Source source = new Source();
        source.setPosition(new Point(250, y));
        source.loadImage(new File(fileName));
        scenebuilder.library.util.numeric.Math math = new scenebuilder.library.util.numeric.Math();
        math.setPosition(new Point(250, y + 50));
        math.setValue("operation", "*");
        math.setValue("v2", parallax);
        Sprite sprite = new Sprite();
        sprite.setPosition(new Point(450, y));
        sprite.setValue(Sprite.PORT_WIDTH, 851.0 / 500.0 * 200.0);
        sprite.setValue(Sprite.PORT_HEIGHT, 200.0);
        root.addChild(source);
        root.addChild(sprite);
        root.addChild(math);
        root.connect(source, Source.PORT_IMAGE, sprite, Sprite.PORT_IMAGE);
        root.connect(mouse, Mouse.PORT_X, math, "v1");
        root.connect(math, "result", sprite, Sprite.PORT_X);
    }

    public static Scene creaturesScene() {
        // Load full scene.
        int y = 50;
        Scene scene = new Scene();
        Macro root = scene.getRootMacro();
        Node clear = new Clear();
        clear.setPosition(new Point(450, 10));
        clear.setValue(Clear.PORT_COLOR, Color.BLACK);
        root.addChild(clear);
        Mouse mouse = new Mouse();
        mouse.setPosition(new Point(50, 50));
        root.addChild(mouse);
        imageMacro(root, mouse, "images/background.png", 0.5, y += 200);
        imageMacro(root, mouse, "images/middle.png", 1.0, y += 200);
        imageMacro(root, mouse, "images/colors.png", 1.1, y += 200);
        imageMacro(root, mouse, "images/creatures.png", 1.1, y += 200);
        imageMacro(root, mouse, "images/foreground.png", 1.5, y += 200);
        imageMacro(root, mouse, "images/manysparkles.png", 2.0, y += 200);
        return scene;
    }

    public static class SwitchSceneAction extends AbstractAction {
        public final Scene scene;

        public SwitchSceneAction(String name, Scene scene) {
            super(name);
            this.scene = scene;
        }

        public void actionPerformed(ActionEvent e) {
            documentWindow.close();
            documentWindow = new SceneDocument(scene);
            documentWindow.setJMenuBar(sceneMenuBar);
            documentWindow.setVisible(true);
        }
    }

    static SceneDocument documentWindow;
    static JMenuBar sceneMenuBar;

    public static void main(String[] args) {
        sceneMenuBar = new JMenuBar();
        JMenu sceneMenu = new JMenu("Scene");
        sceneMenu.add(new SwitchSceneAction("Basic Animation", basicLFOScene()));
        sceneMenu.add(new SwitchSceneAction("Mouse Input", mouseScene()));
        sceneMenu.add(new SwitchSceneAction("Creatures Layers", creaturesScene()));
        sceneMenuBar.add(sceneMenu);
        // Initialize scene
        Scene scene = basicLFOScene();
        //Scene scene = mouseScene();
        //Scene scene = creaturesScene();
        // Start document
        documentWindow = new SceneDocument(scene);
        documentWindow.setJMenuBar(sceneMenuBar);
        documentWindow.setVisible(true);
    }

}
