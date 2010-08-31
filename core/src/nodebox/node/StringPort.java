package nodebox.node;

public class StringPort extends Port {

    private String value;

    public StringPort(Node node, String name, Direction direction, String value) {
        super(node, name, direction);
        this.value = value;
    }

    @Override
    public Object getValue() {
        return value;
    }

    public String get() {
        return value;
    }

    @Override
    public void setValue(Object value) throws IllegalArgumentException {
        if (value instanceof String) {
            this.value = (String) value;
        } else {
            throw new IllegalArgumentException("Given value is not a string.");
        }
    }

    public void set(String value) {
        this.value = value;
    }

    @Override
    public Object parseValue(String value) throws IllegalArgumentException {
        return value;
    }

    @Override
    public String getValueAsString() {
        return value;
    }
}
