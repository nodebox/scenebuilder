package nodebox.launcher;

import org.eclipse.core.runtime.adaptor.EclipseStarter;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.BundleListener;

public class Launcher implements Runnable, BundleListener {
    private BundleContext context;

    public Launcher() {
    }

    private void start(String[] args) {
        try {
            context = EclipseStarter.startup(args, this);
            context.addBundleListener(this);
            System.out.println("context.getBundles() " + context.getBundles());            
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void run() {
        System.out.println("end of splash");
    }

    public void bundleChanged(BundleEvent bundleEvent) {
        System.out.println("event " + bundleEvent);
    }

    public static void main(String[] args) throws Exception {
        Launcher launcher = new Launcher();
        launcher.start(args);
        //EclipseStarter.main(args);
    }
}
