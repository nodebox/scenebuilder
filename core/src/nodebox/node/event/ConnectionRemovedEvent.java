package nodebox.node.event;

import nodebox.node.Connection;
import nodebox.node.Network;
import nodebox.node.NodeEvent;

public class ConnectionRemovedEvent extends NodeEvent {

    private Connection connection;

    public ConnectionRemovedEvent(Network source, Connection connection) {
        super(source);
        this.connection = connection;
    }

    public Connection getConnection() {
        return connection;
    }

    @Override
    public String toString() {
        return "ConnectionRemovedEvent{" +
                "source=" + getSource() +
                "connection=" + connection +
                '}';
    }
}
