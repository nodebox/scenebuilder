package nodebox.builtins;

import nodebox.builtins.color.RGBColor;
import nodebox.builtins.device.Keyboard;
import nodebox.builtins.device.Mouse;
import nodebox.builtins.draw.*;
import nodebox.builtins.image.LoadImage;
import nodebox.builtins.math.*;
import nodebox.builtins.random.RandomFloat;
import nodebox.builtins.time.Calendar;
import nodebox.builtins.time.CurrentTime;
import nodebox.builtins.utility.Grid;
import nodebox.builtins.utility.GridVariables;
import nodebox.builtins.utility.Looper;
import nodebox.builtins.utility.LooperVariables;
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
        m.registerNodeClass(Box.class, "Draw 3D");
        m.registerNodeClass(Clear.class, "Draw");
        m.registerNodeClass(Ellipse.class, "Draw");
        m.registerNodeClass(Image.class, "Draw");
        m.registerNodeClass(Sequence.class, "Draw");
        m.registerNodeClass(Line.class, "Draw");
        m.registerNodeClass(Point.class, "Draw");
        m.registerNodeClass(Rect.class, "Draw");
        m.registerNodeClass(LoadImage.class, "Image");
        m.registerNodeClass(RandomFloat.class, "Random");
        m.registerNodeClass(Angle.class, "Math");
        m.registerNodeClass(Clamp.class, "Math");
        m.registerNodeClass(ConvertRange.class, "Math");
        m.registerNodeClass(Distance.class, "Math");
        m.registerNodeClass(SineWave.class, "Math");
        m.registerNodeClass(Calendar.class, "Time");
        m.registerNodeClass(CurrentTime.class, "Time");
        m.registerNodeClass(Grid.class, "Utility");
        m.registerNodeClass(GridVariables.class, "Utility");
        m.registerNodeClass(Looper.class, "Utility");
        m.registerNodeClass(LooperVariables.class, "Utility");
    }

    public void stop(BundleContext context) throws Exception {
        NodeManager m = getNodeManager(context);
        m.unregisterNodeClass(RGBColor.class);
        m.unregisterNodeClass(Keyboard.class);
        m.unregisterNodeClass(Mouse.class);
        m.unregisterNodeClass(Arc.class);
        m.unregisterNodeClass(Box.class);
        m.unregisterNodeClass(Clear.class);
        m.unregisterNodeClass(Ellipse.class);
        m.unregisterNodeClass(Image.class);
        m.unregisterNodeClass(Line.class);
        m.unregisterNodeClass(Sequence.class);
        m.unregisterNodeClass(Point.class);
        m.unregisterNodeClass(Rect.class);
        m.unregisterNodeClass(LoadImage.class);
        m.unregisterNodeClass(RandomFloat.class);
        m.unregisterNodeClass(Angle.class);
        m.unregisterNodeClass(Clamp.class);
        m.unregisterNodeClass(ConvertRange.class);
        m.unregisterNodeClass(Distance.class);
        m.unregisterNodeClass(SineWave.class);
        m.unregisterNodeClass(Calendar.class);
        m.unregisterNodeClass(CurrentTime.class);
        m.unregisterNodeClass(Grid.class);
        m.unregisterNodeClass(GridVariables.class);
        m.unregisterNodeClass(Looper.class);
        m.unregisterNodeClass(LooperVariables.class);
    }

    private NodeManager getNodeManager(BundleContext context) {
        ServiceReference ref = context.getServiceReference(NodeManager.class.getName());
        return (NodeManager) context.getService(ref);
    }

}
