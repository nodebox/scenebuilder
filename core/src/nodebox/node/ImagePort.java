package nodebox.node;

import processing.core.PImage;

public class ImagePort extends Port {

    private PImage value;

    public ImagePort(Node node, String name, Direction direction) {
        super(node, name, direction);
    }

    public ImagePort(Node node, String name, Direction direction, PImage value) {
        super(node, name, direction);
        this.value = value;
    }

    @Override
    public Object getValue() {
        return value;
    }

    public PImage get() {
        return value;
    }

    @Override
    public void setValue(Object value) throws IllegalArgumentException {
        if (value instanceof PImage || value == null) {
            set((PImage)value);
        } else {
            throw new IllegalArgumentException("The given value is not a PImage.");
        }
    }

    public void set(PImage value) {
        this.value = value;
    }
}
