package nodebox.toxiclibscore;

import nodebox.node.Node;
import nodebox.node.PersistablePort;
import nodebox.node.Port;
import toxi.geom.ReadonlyVec2D;
import toxi.geom.Vec2D;

public class Vec2DPort extends Port implements PersistablePort {

    private ReadonlyVec2D value;

    public Vec2DPort(Node node, String name, Direction direction) {
        super(node, name, direction);
    }

    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public void setValue(Object value) throws IllegalArgumentException {
        if (value instanceof ReadonlyVec2D || value == null) {
            this.value = (ReadonlyVec2D) value;
        } else {
            throw new IllegalArgumentException("Value is not a Vec2D.");
        }
    }

    public Object parseValue(String value) throws IllegalArgumentException {
        String[] xyString = value.split(",");
        if (xyString.length != 2) {
            throw new IllegalArgumentException("Value is not in required x,y format.");
        }
        String xString = xyString[0];
        String yString = xyString[1];
        try {
            float x = Float.parseFloat(xString);
            float y = Float.parseFloat(yString);
            return new Vec2D(x, y);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Could not parse " + value + " as two floats.");
        }
    }

    public String getValueAsString() {
        return String.format("%.f,%.f", value.x(), value.y());
    }


}
