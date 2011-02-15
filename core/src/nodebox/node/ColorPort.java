package nodebox.node;

import java.awt.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ColorPort extends Port implements PersistablePort {

    private Color value;
    private static final Pattern COLOR_PATTERN = Pattern.compile("^#([0-9a-fA-F][0-9a-fA-F])([0-9a-fA-F][0-9a-fA-F])([0-9a-fA-F][0-9a-fA-F])([0-9a-fA-F][0-9a-fA-F])$");

    public ColorPort(Node node, String name, Direction direction) {
        super(node, name, direction);
    }

    public ColorPort(Node node, String name, Direction direction, Color value) {
        super(node, name, direction);
        this.value = value;
    }

    @Override
    public String getWidget() {
        return "color";
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
            set((Color) value);
        } else if (value == null) {
            set(new Color(0, 0, 0));
        } else {
            throw new IllegalArgumentException("The given value is not a color.");
        }
    }

    public void set(Color value) {
        this.value = value;
        markDirty();
    }

    public Object parseValue(String value) throws IllegalArgumentException {
        try {
            Matcher m = COLOR_PATTERN.matcher(value);
            if (!m.matches()) {
                throw new IllegalArgumentException("Color " + value + " is not in the format #11223344");
            }
            int red = Integer.parseInt(m.group(1), 16);
            int green = Integer.parseInt(m.group(2), 16);
            int blue = Integer.parseInt(m.group(3), 16);
            int alpha = Integer.parseInt(m.group(4), 16);
            return new Color(red, green, blue, alpha);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    public String getValueAsString() {
        return String.format("#%02x%02x%02x%02x", value.getRed(), value.getGreen(), value.getBlue(), value.getAlpha());
    }
}
