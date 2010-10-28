package nodebox.app;

import nodebox.node.Context;
import nodebox.node.Scene;
import nodebox.util.Strings;
import processing.core.PApplet;

import java.awt.*;
import java.util.ArrayList;


public class SceneRenderer extends PApplet {
    private static final int FRAME_RATE = 60;
    private boolean finished = false;
    private Scene scene;
    private int screenWidth, screenHeight;
    private double width, height;
    private long startMillis;
    private double time;
    private boolean drawBackground;
    private Color backgroundColor;
    private Exception renderException;
    private java.util.List<ExceptionListener> exceptionListeners = new ArrayList<ExceptionListener>();

    public SceneRenderer(Scene scene) {
        this.scene = scene;
        scene.setApplet(this);
    }

    @Override
    public void setup() {
        startMillis = System.currentTimeMillis();
        int width = Strings.parseInt(scene.getProperty(Scene.PROCESSING_WIDTH), 500);
        int height = Strings.parseInt(scene.getProperty(Scene.PROCESSING_HEIGHT), 500);
        String renderer = scene.getProperty(Scene.PROCESSING_RENDERER);
        int frameRate = Strings.parseInt(scene.getProperty(Scene.PROCESSING_FRAME_RATE), 60);
        boolean smooth = Strings.parseBoolean(scene.getProperty(Scene.PROCESSING_SMOOTH), true);
        drawBackground = Strings.parseBoolean(scene.getProperty(Scene.PROCESSING_DRAW_BACKGROUND), true);
        backgroundColor = Strings.parseColor(scene.getProperty(Scene.PROCESSING_BACKGROUND_COLOR), Color.LIGHT_GRAY);
        size(width, height, renderer);
        frameRate(frameRate);
        if (smooth) {
            smooth();
        } else {
            noSmooth();
        }
    }

    @Override
    public void draw() {
        try {
            // Initialize the context.
            float time = (float) ((System.currentTimeMillis() - startMillis) / 1000.0);
            Context context = new Context(this);
            // Draw the background, if needed.
            if (drawBackground) {
                background(backgroundColor.getRed(), backgroundColor.getGreen(), backgroundColor.getBlue());
            }
            // Execute and draw the scene.
            scene.execute(context, time);
            scene.draw(context.getGraphics(), context, time);

            // Since everything worked, clear out the render exception.
            clearRenderException();
        } catch (Exception e) {
            setRenderException(e);
        }
    }

    private void clearRenderException() {
        if (renderException != null) {
            fireExceptionCleared();
            renderException = null;
        }
    }

    private void setRenderException(Exception e) {
        renderException = e;
        fireExceptionThrown(e);
    }

    public void addExceptionListener(ExceptionListener listener) {
        exceptionListeners.add(listener);
    }

    public void removeExceptionListener(ExceptionListener listener) {
        exceptionListeners.remove(listener);
    }

    private void fireExceptionThrown(Exception e) {
        for (ExceptionListener listener: exceptionListeners) {
            listener.exceptionThrown(e);
        }
    }

    private void fireExceptionCleared() {
        for (ExceptionListener listener: exceptionListeners) {
            listener.exceptionCleared();
        }
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
