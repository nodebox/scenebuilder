package nodebox.pixie;

import nodebox.node.NodeManager;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

public class PixieActivator implements BundleActivator {

    public void start(BundleContext context) throws Exception {
        NodeManager m = getNodeManager(context);
        m.registerNodeClass(Grayscale.class, "Image");
        m.registerNodeClass(Invert.class, "Image");
        m.registerNodeClass(Invertalpha.class, "Image");
        m.registerNodeClass(Lighten.class, "Image");
        m.registerNodeClass(Posterize.class, "Image");
        m.registerNodeClass(RGBAdjust.class, "Image");
        m.registerNodeClass(Saturation.class, "Image");
    }

    public void stop(BundleContext context) throws Exception {
        NodeManager m = getNodeManager(context);
        m.unregisterNodeClass(Grayscale.class);
        m.unregisterNodeClass(Invert.class);
        m.unregisterNodeClass(Invertalpha.class);
        m.unregisterNodeClass(Lighten.class);
        m.unregisterNodeClass(Posterize.class);
        m.unregisterNodeClass(RGBAdjust.class);
        m.unregisterNodeClass(Saturation.class);
    }

    private NodeManager getNodeManager(BundleContext context) {
        ServiceReference ref = context.getServiceReference(NodeManager.class.getName());
        return (NodeManager) context.getService(ref);
    }

}
