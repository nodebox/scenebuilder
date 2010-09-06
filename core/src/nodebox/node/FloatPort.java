package nodebox.node;

public class FloatPort extends Port implements PersistablePort {

    private float value;

    public FloatPort(Node node, String name, Direction direction) {
        super(node, name, direction);
    }

    public FloatPort(Node node, String name, Direction direction, float value) {
        super(node, name, direction);
        this.value = value;
    }

    @Override
    public Object getValue() {
        return value;
    }

    public float get() {
        return value;
    }

    @Override
    public void setValue(Object value) throws IllegalArgumentException {
        if (value instanceof Float) {
            set((Float) value);
        } else if (value instanceof Integer) {
            set((float) (Integer) value);
        } else {
            throw new IllegalArgumentException("The given value is not a float.");
        }
    }

    public void set(float value) {
        this.value = value;
    }

    public Object parseValue(String value) throws IllegalArgumentException {
        return Float.parseFloat(value);
    }

    public String getValueAsString() {
        return Float.toString(value);
    }
}