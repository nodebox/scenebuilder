package nodebox.app;

import nodebox.node.Network;
import nodebox.node.Node;
import nodebox.node.NodeManager;
import nodebox.node.Scene;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import javax.swing.*;
import java.awt.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Properties;

public class Application implements BundleActivator {

    private static Application instance;

    public static Application getInstance() {
        return instance;
    }

    public void start(BundleContext context) throws Exception {
        System.out.println("Starting up application!");
        instance = this;
        version = context.getBundle().getVersion().toString();
        manager = getNodeManager(context);
        loadScene("basicLFOScene");
    }

    public void stop(BundleContext context) throws Exception {
        System.out.println("Shutting down application.");
        document.close();
        instance = null;
    }

    private String version;
    private SceneDocument document;
    private NodeManager manager;

    public Application() {
        registerForMacOSXEvents();
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
    }

    public boolean quit() {
        document.close();
        System.exit(0);
        return true;
    }

    public String getVersion() {
        return version;
    }

    public void loadScene(String sceneName) {
        try {
            if (document != null) {
                document.close();
            }
            Method m = getClass().getMethod(sceneName);
            Scene scene = (Scene) m.invoke(this);
            document = new SceneDocument(manager, scene);
            document.setLocationRelativeTo(null);
            document.setVisible(true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

     private NodeManager getNodeManager(BundleContext context) {
        ServiceReference ref = context.getServiceReference(NodeManager.class.getName());
        return (NodeManager) context.getService(ref);
    }

    //// Hard-coded demo scenes ////

    public Scene basicLFOScene() {
        Scene scene = new Scene();
        Network root = scene.getRootNetwork();
        Node clear = manager.createNode("nodebox.builtins.render.Clear");
        clear.setPosition(new Point(250, 50));
        clear.setValue("color", Color.DARK_GRAY);
        //Node lfo = new LFO();
        //lfo.setPosition(new Point(50, 50));
        //lfo.setValue(LFO.PORT_OFFSET, 100.0);
        Node rect = manager.createNode("nodebox.builtins.render.Rect");
        rect.setPosition(new Point(250, 150));
        //root.addChild(lfo);
        root.addChild(clear);
        root.addChild(rect);
        //root.connect(lfo, LFO.PORT_RESULT, rect, Rect.PORT_X);
        return scene;
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        Application.getInstance().loadScene("basicLFOScene");
    }

}
