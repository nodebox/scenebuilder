package nodebox.toxiclibscore;

import nodebox.node.NodeManager;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

public class ToxiclibsCoreActivator implements BundleActivator {

    public void start(BundleContext context) throws Exception {
        NodeManager m = getNodeManager(context);
        m.registerNodeClass(VoronoiDrawer.class, "Toxiclibs 2D");
    }

    public void stop(BundleContext context) throws Exception {
        NodeManager m = getNodeManager(context);
        m.unregisterNodeClass(VoronoiDrawer.class);
    }

    private NodeManager getNodeManager(BundleContext context) {
        ServiceReference ref = context.getServiceReference(NodeManager.class.getName());
        return (NodeManager) context.getService(ref);
    }
}