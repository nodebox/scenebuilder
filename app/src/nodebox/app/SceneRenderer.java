package nodebox.app;

import nodebox.node.Context;
import nodebox.node.Scene;
import processing.core.PApplet;

import javax.swing.*;


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
//        Scene scene = Application.basicLFOScene();
//        SceneRenderer renderer = new SceneRenderer(scene);
//        renderer.init();
//        JFrame frame = new JFrame("Test Renderer");
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.getContentPane().add(renderer);
//        frame.setSize(800, 600);
//        frame.setLocationRelativeTo(null);
//        frame.setVisible(true);
//        renderer.requestFocus();
    }
}
