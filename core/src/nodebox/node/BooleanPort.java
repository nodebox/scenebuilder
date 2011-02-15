package nodebox.node;

public class BooleanPort extends Port implements PersistablePort {

    private boolean value;

    public BooleanPort(Node node, String name, Direction direction) {
        super(node, name, direction);
    }

    public BooleanPort(Node node, String name, Direction direction, boolean value) {
        super(node, name, direction);
        this.value = value;
    }

    @Override
    public String getWidget() {
        return "boolean";
    }

    @Override
    public Object getValue() {
        return value;
    }

    public boolean get() {
        return value;
    }

    @Override
    public void setValue(Object value) throws IllegalArgumentException {
        if (value instanceof Boolean) {
            set((Boolean) value);
        } else if (value == null) {
            set(false);
        } else {
            throw new IllegalArgumentException("The given value is not a boolean.");
        }
    }

    public void set(boolean value) {
        this.value = value;
        markDirty();
    }

    public Object parseValue(String value) throws IllegalArgumentException {
        return Boolean.parseBoolean(value);
    }

    public String getValueAsString() {
        return Boolean.toString(value);
    }
}
