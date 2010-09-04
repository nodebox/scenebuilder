package nodebox.toxiclibscore;

import nodebox.node.NodeManager;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

public class ToxiclibsCoreActivator implements BundleActivator {

    public void start(BundleContext context) throws Exception {
        NodeManager m = getNodeManager(context);
        m.registerNodeClass(Polygon2DDrawer.class, "Toxiclibs 2D");
        m.registerNodeClass(VoronoiDrawer.class, "Toxiclibs 2D");
        m.registerNodeClass(VoronoiLooper.class, "Toxiclibs 2D");
        m.registerNodeClass(VoronoiVariables.class, "Toxiclibs 2D");
    }

    public void stop(BundleContext context) throws Exception {
        NodeManager m = getNodeManager(context);
        m.unregisterNodeClass(Polygon2DDrawer.class);
        m.unregisterNodeClass(VoronoiDrawer.class);
        m.unregisterNodeClass(VoronoiLooper.class);
        m.unregisterNodeClass(VoronoiVariables.class);
    }

    private NodeManager getNodeManager(BundleContext context) {
        ServiceReference ref = context.getServiceReference(NodeManager.class.getName());
        return (NodeManager) context.getService(ref);
    }
}