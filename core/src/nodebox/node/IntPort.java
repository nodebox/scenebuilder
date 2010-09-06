package nodebox.node;

/**
 * An integer port holds an integer number.
 *
 * Integer ports support both integer data and floating-point data. Floating-point data is rounded.
 */
public class IntPort extends Port implements PersistablePort {

    private int value;

    public IntPort(Node node, String name, Direction direction) {
        super(node, name, direction);
    }

    public IntPort(Node node, String name, Direction direction, int value) {
        super(node, name, direction);
        this.value = value;
    }

    public Object getValue() {
        return value;
    }

    public int get() {
        return value;
    }

    public void setValue(Object value) {
        if (value instanceof Integer) {
            set((Integer) value);
        } else if (value instanceof Float) {
            set(Math.round((Float) value));
        } else {
            throw new IllegalArgumentException(this + ": Value is not an integer.");
        }
    }

    public void set(int value) {
        this.value = value;
    }

    public Object parseValue(String value) throws IllegalArgumentException {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("The given string cannot be parsed as a number.");
        }
    }

    public String getValueAsString() {
        return Integer.toString(value);
    }

    @Override
    protected boolean canReceiveFrom(Port output) {
        // Int ports can receive data from float ports as well. The value is rounded.
        return (output instanceof IntPort) || (output instanceof FloatPort);
    }

}
