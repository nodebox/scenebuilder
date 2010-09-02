package nodebox.node;

import java.util.*;

import static nodebox.util.Preconditions.checkArgument;
import static nodebox.util.Preconditions.checkNotNull;

public class Network extends Node {

    private List<Node> children;
    private List<Connection> connections;
    private Set<Port> publishedPorts;

    public Network() {
        setAttribute(DISPLAY_NAME_ATTRIBUTE, "Network Node");
        setAttribute(DESCRIPTION_ATTRIBUTE, "A Node that can contain other nodes.");
        children = new LinkedList<Node>();
        connections = new LinkedList<Connection>();
        publishedPorts = new LinkedHashSet<Port>();
    }

    @Override
    public boolean isRendering() {
        return true;
    }

    //// Children ////

    public List<Node> getChildren() {
        return new ArrayList<Node>(children);
    }

    public void addChild(Node child) {
        children.add(child);
        child.setNetwork(this);
    }

    public void removeChild(Node child) {
        children.remove(child);
        child.setNetwork(null);
    }

    public Node getChild(String childName) {
        for (Node child : children) {
            if (child.getName().equals(childName)) {
                return child;
            }
        }
        throw new IllegalArgumentException("This network has no child node named " + childName);
    }

    public boolean hasChild(Node child) {
        return children.contains(child);
    }

    public boolean hasChild(String childName) {
        for (Node child : children) {
            if (child.getName().equals(childName)) {
                return true;
            }
        }
        return false;
    }

    //// Connections ////

    public Collection<Connection> getConnections() {
        return connections;
    }

    public void connect(Port output, Port input) {
        checkNotNull(output);
        checkNotNull(input);
        checkArgument(output.getNode().getNetwork() == this, "Output %s is not a child of this network.", output);
        checkArgument(input.getNode().getNetwork() == this, "Input %s is not a child of this network.", input);
        connections.add(new Connection(this, output, input));
    }

    public Collection<Connection> getInputConnections(Node node) {
        LinkedList<Connection> cs = new LinkedList<Connection>();
        for (Connection c : connections) {
            if (c.getInputNode() == node) {
                cs.add(c);
            }
        }
        return cs;
    }

    //// Published Ports ////

    public Port publishPort(Port port) {
        if (!hasChild(port.getNode()))
            throw new IllegalArgumentException(String.format("Node %s is not a child of this network.", port.getNode()));
        PublishedPort publishedPort = new PublishedPort(this, port);
        addPort(publishedPort);
        return publishedPort;
    }

    public void unPublishPort(Port port) {
        if (!hasChild(port.getNode()))
            throw new IllegalArgumentException(String.format("Node %s is not a child of this network.", port.getNode()));
        removePort(port.getName());
    }

    public boolean isPublished(Port port) {
        if (!hasChild(port.getNode()))
            throw new IllegalArgumentException(String.format("Node %s is not a child of this network.", port.getNode()));
        for (Port p : getPorts()) {
            if (p instanceof PublishedPort) {
                PublishedPort pp = (PublishedPort) p;
                if (pp.getOriginalPort() == port) return true;
            }
        }
        return false;
    }


    /**
     * Render all nodes with the render function.
     *
     * @param context the rendering context
     * @param time    the current time
     * @return true if all children returned true.
     */
    @Override
    public boolean execute(Context context, double time) {
        for (Node child : children) {
            if (child.isRendering()) {
                if (!updateChildDependencies(child, context, time))
                    return false;
                if (!child.execute(context, time))
                    return false;
            }
        }
        return true;
    }

    private boolean updateChildDependencies(Node child, Context context, double time) {
        // Update all
        for (Connection c : getInputConnections(child)) {
            Node n = c.getOutputNode();
            if (!context.hasExecuted(n)) {
                context.addToExecutedNodes(n);
                updateChildDependencies(n, context, time);
                if (!n.execute(context, time)) return false;
            }
            c.getInputPort().setValue(c.getOutputPort().getValue());
        }
        return true;
    }

    public boolean isConnected(Port port) {
        for (Connection c : connections) {
            if (c.getInputPort() == port || c.getOutputPort() == port) return true;
        }
        return false;
    }
}
