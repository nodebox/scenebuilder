package nodebox.graphics.nodes;

import nodebox.node.NodeManager;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

public class GraphicsNodesActivator  implements BundleActivator {
    public void start(BundleContext context) throws Exception {
        NodeManager m = getNodeManager(context);
        m.registerNodeClass(EllipseNode.class, "Geometry");
        m.registerNodeClass(GridNode.class, "Geometry");
        m.registerNodeClass(LineNode.class, "Geometry");
        m.registerNodeClass(PolygonNode.class, "Geometry");
        m.registerNodeClass(StarNode.class, "Geometry");
        m.registerNodeClass(RectNode.class, "Geometry");
        m.registerNodeClass(PlaceNode.class, "Geometry");
        m.registerNodeClass(ResampleNode.class, "Geometry");
        m.registerNodeClass(SnapNode.class, "Geometry");
        m.registerNodeClass(TextPathNode.class, "Geometry");
        m.registerNodeClass(WiggleNode.class, "Geometry");
        m.registerNodeClass(ScatterNode.class, "Geometry");
        m.registerNodeClass(ColorNode.class, "Geometry");
        m.registerNodeClass(TransformNode.class, "Geometry");
    }

    public void stop(BundleContext context) throws Exception {
        NodeManager m = getNodeManager(context);
        m.unregisterNodeClass(EllipseNode.class);
        m.unregisterNodeClass(GridNode.class);
        m.unregisterNodeClass(LineNode.class);
        m.unregisterNodeClass(PolygonNode.class);
        m.unregisterNodeClass(StarNode.class);
        m.unregisterNodeClass(RectNode.class);
        m.unregisterNodeClass(PlaceNode.class);
        m.unregisterNodeClass(ResampleNode.class);
        m.unregisterNodeClass(SnapNode.class);
        m.unregisterNodeClass(TextPathNode.class);
        m.unregisterNodeClass(WiggleNode.class);
        m.unregisterNodeClass(ScatterNode.class);
        m.unregisterNodeClass(ColorNode.class);
        m.unregisterNodeClass(TransformNode.class);
    }

     private NodeManager getNodeManager(BundleContext context) {
        ServiceReference ref = context.getServiceReference(NodeManager.class.getName());
        return (NodeManager) context.getService(ref);
    }
}
