package scenebuilder.render;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Mouse;
import org.lwjgl.openal.AL;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Timer;
import scenebuilder.library.animation.LFO;
import scenebuilder.library.render.Clear;
import scenebuilder.library.render.Sprite;
import scenebuilder.model.Context;
import scenebuilder.model.Macro;
import scenebuilder.model.Node;
import scenebuilder.model.Scene;

import java.awt.*;


public class SceneRenderer {
    private static final int FRAME_RATE = 60;
    private boolean finished = false;
    private Scene scene;
    private Timer timer;
    private int screenWidth, screenHeight;
    private double width, height;

    public SceneRenderer(Scene scene) {
        this.scene = scene;
    }

    public void init() throws Exception {
        // Initialize LWJGL
        DisplayMode mode = new DisplayMode(800, 600);
        screenWidth = mode.getWidth();
        screenHeight = mode.getHeight();
        Display.setTitle("Viewer");
        Display.setDisplayMode(mode);
        Display.setFullscreen(false);
        Display.setVSyncEnabled(true);
        Display.create();
        AL.create();
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();

        double factor = mode.getWidth() / (double) mode.getHeight();
        if (mode.getWidth() < mode.getHeight()) {
            width = 100.0;
            height = factor * 100.0;
        } else {
            width = factor * 100.0;
            height = 100.0;
        }
        GL11.glOrtho(-width, width, -height, height, -1.0, 1.0);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadIdentity();
        GL11.glViewport(0, 0, screenWidth, screenHeight);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        timer = new Timer();
    }

    public void update() {
        Timer.tick();
        //scene.update();
    }

    public void render() {
        GL11.glPushMatrix();
        Context context = new Context();
        double relativeX = Mouse.getX() / (double) screenWidth;
        double relativeY = Mouse.getY() / (double) screenHeight;
        context.setMouseX((relativeX - 0.5) * width);
        context.setMouseY((relativeY - 0.5) * height);
        scene.execute(context, timer.getTime());
        GL11.glPopMatrix();
    }

    public void run() {
        while (!finished) {
            Display.update();
            if (Display.isCloseRequested() || Thread.currentThread().isInterrupted())
                finished = true;
            else if (Display.isActive() || true) { // TODO: only needs to be inactive if the app is no longer in front.
                // The window is in the foreground, so we should play the game
                update();
                render();
                Display.sync(FRAME_RATE);
            } else {
                // The window is not in the foreground, so we can allow other stuff to run and
                // infrequently update
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                update();
                if (Display.isVisible() || Display.isDirty())
                    // Only bother rendering if the window is visible or dirty
                    render();
            }
        }
        cleanup();
    }

    public void cleanup() {
        AL.destroy();
        try {
            Display.releaseContext();
        } catch (LWJGLException e) {
            throw new RuntimeException(e);
        }
        Display.destroy();
    }

    public static void main(String[] args) {
        // Initialize scene
        Scene scene = new Scene();
        Macro root = scene.getRootMacro();
        Node clear = new Clear();
        clear.setValue(Clear.PORT_COLOR, Color.DARK_GRAY);
        Node lfo = new LFO();
        Node sprite = new Sprite();
        sprite.setValue(Sprite.PORT_COLOR, Color.RED);
        root.addChild(clear);
        root.addChild(lfo);
        root.addChild(sprite);
        root.connect(lfo, LFO.PORT_RESULT, sprite, Sprite.PORT_X);

        SceneRenderer renderer = new SceneRenderer(scene);
        try {
            renderer.init();
            renderer.run();
            renderer.cleanup();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        System.exit(0);
    }
}
