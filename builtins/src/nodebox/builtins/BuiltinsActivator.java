package nodebox.builtins;

import nodebox.builtins.device.Mouse;
import nodebox.builtins.draw.Ellipse;
import nodebox.builtins.draw.Line;
import nodebox.builtins.looper.Looper;
import nodebox.builtins.looper.LooperVariables;
import nodebox.builtins.random.RandomFloat;
import nodebox.builtins.draw.Clear;
import nodebox.builtins.draw.Rect;
import nodebox.node.NodeManager;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

public class BuiltinsActivator implements BundleActivator {

    public void start(BundleContext context) throws Exception {
        NodeManager m = getNodeManager(context);
        m.registerNodeClass(Mouse.class, "Device");
        m.registerNodeClass(Clear.class, "Draw");
        m.registerNodeClass(Ellipse.class, "Draw");
        m.registerNodeClass(Line.class, "Draw");
        m.registerNodeClass(Rect.class, "Draw");
        m.registerNodeClass(RandomFloat.class, "Random");
        m.registerNodeClass(Looper.class, "Utility");
        m.registerNodeClass(LooperVariables.class, "Utility");
    }

    public void stop(BundleContext context) throws Exception {
        NodeManager m = getNodeManager(context);
        m.unregisterNodeClass(Mouse.class);
        m.unregisterNodeClass(Clear.class);
        m.unregisterNodeClass(Ellipse.class);
        m.unregisterNodeClass(Line.class);
        m.unregisterNodeClass(Rect.class);
        m.unregisterNodeClass(RandomFloat.class);
        m.unregisterNodeClass(Looper.class);
        m.unregisterNodeClass(LooperVariables.class);
    }

    private NodeManager getNodeManager(BundleContext context) {
        ServiceReference ref = context.getServiceReference(NodeManager.class.getName());
        return (NodeManager) context.getService(ref);
    }

}
