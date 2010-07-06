package scenebuilder.render;

import processing.core.PApplet;
import scenebuilder.library.animation.LFO;
import scenebuilder.library.render.Clear;
import scenebuilder.library.render.Sprite;
import scenebuilder.model.Context;
import scenebuilder.model.Macro;
import scenebuilder.model.Node;
import scenebuilder.model.Scene;

import javax.swing.*;
import java.awt.*;


public class SceneRenderer extends PApplet {
    private static final int FRAME_RATE = 60;
    private boolean finished = false;
    private Scene scene;
    private int screenWidth, screenHeight;
    private double width, height;
    private long startMillis;
    private double time;

    public SceneRenderer(Scene scene) {
        this.scene = scene;
    }

    @Override
    public void setup() {
        startMillis = System.currentTimeMillis();
        size(800, 600, "processing.core.PGraphicsJava2D");
        frameRate(60);
        smooth();
    }

    @Override
    public void draw() {
        double time = (System.currentTimeMillis() - startMillis) / 1000.0;
        Context context = new Context(this);
        //double relativeX = Mouse.getX() / (double) screenWidth;
        //double relativeY = Mouse.getY() / (double) screenHeight;
        //context.setMouseX((relativeX - 0.5) * width);
        //context.setMouseY((relativeY - 0.5) * height);
        scene.execute(context, time);
    }


    public static void main(String[] args) {
        // Initialize scene
        Scene scene = new Scene();
        Macro root = scene.getRootMacro();
        Node clear = new Clear();
        clear.setValue(Clear.PORT_COLOR, Color.DARK_GRAY);
        Node lfo = new LFO();
        lfo.setValue(LFO.PORT_OFFSET, 200.0);
        Node sprite = new Sprite();
        sprite.setValue(Sprite.PORT_COLOR, Color.RED);
        sprite.setValue(Sprite.PORT_Y, 100.0);
        root.addChild(clear);
        root.addChild(lfo);
        root.addChild(sprite);
        root.connect(lfo, LFO.PORT_RESULT, sprite, Sprite.PORT_X);

        SceneRenderer renderer = new SceneRenderer(scene);
        renderer.init();
        JFrame frame = new JFrame("Test Renderer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(renderer);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        renderer.requestFocus();
    }
}
