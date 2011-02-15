package nodebox.graphics.nodes;

import nodebox.graphics.Geometry;
import nodebox.node.Node;
import nodebox.node.Port;

public class GeometryPort extends Port {
     private Geometry value;

    public GeometryPort(Node node, String name, Direction direction) {
        super(node, name, direction);
    }

    @Override
    public Object getValue() {
        return value;
    }

    public Geometry get() {
        return value;
    }

    @Override
    public void setValue(Object value) throws IllegalArgumentException {
        if (value instanceof Geometry || value == null) {
            set((Geometry) value);
        } else {
            throw new IllegalArgumentException("Value is not a Geometry.");
        }
    }

    public void set(Geometry value) {
        this.value = value;
        markDirty();
    }
}
