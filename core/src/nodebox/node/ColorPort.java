package nodebox.node;

import java.awt.*;

public class ColorPort extends Port {

    private Color value;

    public ColorPort(Node node, String name, Direction direction, Color value) {
        super(node, name, direction);
        this.value = value;
    }

    @Override
    public Object getValue() {
        return value;
    }

    public Color get() {
        return value;
    }

    @Override
    public void setValue(Object value) throws IllegalArgumentException {
        if (value instanceof Color) {
            set((Color)value);
        } else {
            throw new IllegalArgumentException("The given value is not a color.");
        }
    }

    public void set(Color value) {
        this.value = value;
    }

    @Override
    public Object parseValue(String value) throws IllegalArgumentException {
        try {
            return Color.decode(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    @Override
    public String getValueAsString() {
        return String.format("#%h%h%h%h", value.getRed(), value.getGreen(), value.getBlue(), value.getAlpha());
    }
}
