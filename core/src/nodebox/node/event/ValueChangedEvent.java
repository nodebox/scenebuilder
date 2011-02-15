package nodebox.node.event;

import nodebox.node.Node;
import nodebox.node.NodeEvent;
import nodebox.node.Port;

public class ValueChangedEvent extends NodeEvent {

    private Port port;

    public ValueChangedEvent(Node source, Port port) {
        super(source);
        this.port = port;
    }

    public Port getPort() {
        return port;
    }

    @Override
    public String toString() {
        return "ValueChangedEvent{" +
                "source=" + getSource() +
                ", port=" + port +
                '}';
    }
}
