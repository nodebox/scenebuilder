package nodebox.builtins;

import nodebox.builtins.render.Clear;
import nodebox.builtins.render.Rect;
import nodebox.node.NodeManager;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

public class BuiltinsActivator implements BundleActivator {

    public void start(BundleContext context) throws Exception {
        NodeManager m = getNodeManager(context);
        m.addNodeClass(Clear.class, "Clear");
        m.addNodeClass(Rect.class, "Render");
    }

    public void stop(BundleContext context) throws Exception {
        NodeManager m = getNodeManager(context);
        m.removeNodeClass(Clear.class);
        m.removeNodeClass(Rect.class);
    }

    private NodeManager getNodeManager(BundleContext context) {
        System.out.println("GET NODE MAN "+ NodeManager.class.getName());
        ServiceReference ref = context.getServiceReference(NodeManager.class.getName());
        return (NodeManager) context.getService(ref);
    }

}
