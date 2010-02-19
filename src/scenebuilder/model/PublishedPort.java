package scenebuilder.model;

public class PublishedPort extends Port {

    private Port originalPort;

    public PublishedPort(Macro macro, Port port) {
        super(macro, port.getNode().getName() + "_" + port.getName(), port.getType(), port.getDirection(), port.getValue());
        setAttribute(Port.DISPLAY_NAME_ATTRIBUTE, port.getAttribute(Port.DISPLAY_NAME_ATTRIBUTE));
        originalPort = port;
    }

    public Port getOriginalPort() {
        return originalPort;
    }

    public void setValue(Object value) {
        super.setValue(value);
        originalPort.setValue(value);
    }
}
