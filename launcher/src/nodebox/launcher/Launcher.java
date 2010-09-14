package nodebox.launcher;

import org.eclipse.core.runtime.adaptor.EclipseStarter;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.BundleException;
import org.osgi.framework.BundleListener;

public class Launcher implements Runnable, BundleListener {
    private BundleContext context;

    public Launcher() {
    }

    private void start(String[] args) {
        try {
            context = EclipseStarter.startup(args, this);
            context.addBundleListener(this);
            context.installBundle("file:dist/nodebox-core.jar");
            context.installBundle("file:dist/nodebox-builtins.jar");
            context.installBundle("file:dist/nodebox-graphics.jar");
            //context.installBundle("file:dist/nodebox-toxiclibscore.jar");
            context.installBundle("file:dist/nodebox-app.jar");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void run() {
    }

    public void bundleChanged(BundleEvent event) {
        if (event.getType() == BundleEvent.INSTALLED) {
            try {
                event.getBundle().start();
            } catch (BundleException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void main(String[] args) throws Exception {
        Launcher launcher = new Launcher();
        launcher.start(args);
    }
}
