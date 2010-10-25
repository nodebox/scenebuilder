package nodebox.toxiclibscore;

import nodebox.node.Node;
import nodebox.node.Port;
import toxi.geom.Polygon2D;

public class Polygon2DPort extends Port {

    private Polygon2D value;

    public Polygon2DPort(Node node, String name, Direction direction) {
        super(node, name, direction);
    }

    @Override
    public Object getValue() {
        return value;
    }

    public Polygon2D get() {
        return value;
    }

    @Override
    public void setValue(Object value) throws IllegalArgumentException {
        if (value instanceof Polygon2D || value == null) {
            set((Polygon2D) value);
        } else {
            throw new IllegalArgumentException("Value is not a Polygon2D.");
        }
    }

    public void set(Polygon2D value) {
        this.value = value;
    }
}
