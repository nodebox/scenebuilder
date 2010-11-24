package nodebox.graphics.nodes;

import nodebox.node.Port;

public abstract class FilterNode extends GeneratorNode {

    public final GeometryPort pGeometry = new GeometryPort(this, "inputGeometry", Port.Direction.INPUT);

    public FilterNode() {
        pGeometry.setDisplayName("geo");
    }
}
