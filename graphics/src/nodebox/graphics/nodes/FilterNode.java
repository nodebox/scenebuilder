package nodebox.graphics.nodes;

import nodebox.node.Port;

public abstract class FilterNode extends GeneratorNode {

    public final GeometryPort pGeometry = new GeometryPort(this, "geometry", Port.Direction.INPUT);
}
