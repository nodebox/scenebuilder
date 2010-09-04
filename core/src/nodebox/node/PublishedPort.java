package nodebox.node;

public class PublishedPort extends Port {

    private Port originalPort;

    public PublishedPort(Network network, Port port) {
        super(network, port.getNode().getName() + "_" + port.getName(), port.getDirection());
        setAttribute(DISPLAY_NAME_ATTRIBUTE, port.getAttribute(DISPLAY_NAME_ATTRIBUTE));
        originalPort = port;
    }

    public Port getOriginalPort() {
        return originalPort;
    }

    @Override
    public Object getValue() {
        return originalPort.getValue();
    }

    public void setValue(Object value) {
        originalPort.setValue(value);
    }
}
