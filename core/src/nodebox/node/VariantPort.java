package nodebox.node;

public class VariantPort extends Port {
    private Object value;

    public VariantPort(Node node, String name, Direction direction) {
        super(node, name, direction);
    }

    public VariantPort(Node node, String name, Direction direction, Object value) {
        super(node, name, direction);
        this.value = value;
    }

    @Override
    public Object getValue() {
        return value;
    }

    public Object get() {
        return value;
    }

    @Override
    public void setValue(Object value) throws IllegalArgumentException {
        this.value = value;
    }

    public void set(Object value) {
        this.value = value;
    }

    @Override
    protected boolean canReceiveFrom(Port output) {
        return true;
    }
}
