package nodebox.builtins;

import nodebox.builtins.color.RGBColor;
import nodebox.builtins.device.Keyboard;
import nodebox.builtins.device.Mouse;
import nodebox.builtins.draw.*;
import nodebox.builtins.image.LoadImage;
import nodebox.builtins.looper.Looper;
import nodebox.builtins.looper.LooperVariables;
import nodebox.builtins.math.Clamp;
import nodebox.builtins.math.ConvertRange;
import nodebox.builtins.random.RandomFloat;
import nodebox.builtins.time.Calendar;
import nodebox.node.NodeManager;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

public class BuiltinsActivator implements BundleActivator {

    public void start(BundleContext context) throws Exception {
        NodeManager m = getNodeManager(context);
        m.registerNodeClass(RGBColor.class, "Color");
        m.registerNodeClass(Keyboard.class, "Device");
        m.registerNodeClass(Mouse.class, "Device");
        m.registerNodeClass(Arc.class, "Draw");
        m.registerNodeClass(Clear.class, "Draw");
        m.registerNodeClass(Ellipse.class, "Draw");
        m.registerNodeClass(Image.class, "Draw");
        m.registerNodeClass(Line.class, "Draw");
        m.registerNodeClass(Point.class, "Draw");
        m.registerNodeClass(Rect.class, "Draw");
        m.registerNodeClass(LoadImage.class, "Image");
        m.registerNodeClass(RandomFloat.class, "Random");
        m.registerNodeClass(Looper.class, "Utility");
        m.registerNodeClass(LooperVariables.class, "Utility");
        m.registerNodeClass(Clamp.class, "Math");
        m.registerNodeClass(ConvertRange.class, "Math");
        m.registerNodeClass(Calendar.class, "Time");
    }

    public void stop(BundleContext context) throws Exception {
        NodeManager m = getNodeManager(context);
        m.unregisterNodeClass(RGBColor.class);
        m.unregisterNodeClass(Keyboard.class);
        m.unregisterNodeClass(Mouse.class);
        m.unregisterNodeClass(Arc.class);
        m.unregisterNodeClass(Clear.class);
        m.unregisterNodeClass(Ellipse.class);
        m.unregisterNodeClass(Image.class);
        m.unregisterNodeClass(Line.class);
        m.unregisterNodeClass(Point.class);
        m.unregisterNodeClass(Rect.class);
        m.unregisterNodeClass(LoadImage.class);
        m.unregisterNodeClass(RandomFloat.class);
        m.unregisterNodeClass(Looper.class);
        m.unregisterNodeClass(LooperVariables.class);
        m.unregisterNodeClass(Clamp.class);
        m.unregisterNodeClass(ConvertRange.class);
        m.unregisterNodeClass(Calendar.class);
    }

    private NodeManager getNodeManager(BundleContext context) {
        ServiceReference ref = context.getServiceReference(NodeManager.class.getName());
        return (NodeManager) context.getService(ref);
    }

}
