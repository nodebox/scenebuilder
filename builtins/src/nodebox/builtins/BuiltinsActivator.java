package nodebox.builtins;

import nodebox.builtins.looper.Looper;
import nodebox.builtins.looper.LooperVariables;
import nodebox.builtins.render.Clear;
import nodebox.builtins.render.Rect;
import nodebox.node.NodeManager;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

public class BuiltinsActivator implements BundleActivator {

    public void start(BundleContext context) throws Exception {
        NodeManager m = getNodeManager(context);
        m.registerNodeClass(Clear.class, "Render");
        m.registerNodeClass(Rect.class, "Render");
        m.registerNodeClass(Looper.class, "Utility");
        m.registerNodeClass(LooperVariables.class, "Utility");
    }

    public void stop(BundleContext context) throws Exception {
        NodeManager m = getNodeManager(context);
        m.unregisterNodeClass(Clear.class);
        m.unregisterNodeClass(Rect.class);
    }

    private NodeManager getNodeManager(BundleContext context) {
        ServiceReference ref = context.getServiceReference(NodeManager.class.getName());
        return (NodeManager) context.getService(ref);
    }

}
