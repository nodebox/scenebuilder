package scenebuilder.model;

import java.util.*;

public class Macro extends Node {

    private List<Node> children;
    private List<Connection> connections;
    private Set<Port> publishedPorts;

    public Macro() {
        super(Function.Renderer);
        setAttribute(DISPLAY_NAME_ATTRIBUTE, "Macro Node");
        setAttribute(DESCRIPTION_ATTRIBUTE, "A Node that can contain other nodes.");
        children = new LinkedList<Node>();
        connections = new LinkedList<Connection>();
        publishedPorts = new LinkedHashSet<Port>();
    }

    //// Children ////

    public Collection<Node> getChildren() {
        return children;
    }

    public void addChild(Node child) {
        children.add(child);
        child.setParent(this);
    }

    public void removeChild(Node child) {
        children.remove(child);
        child.setParent(null);
    }

    public boolean containsChild(Node child) {
        return children.contains(child);
    }

    //// Connections ////

    public Collection<Connection> getConnections() {
        return connections;
    }

    public void connect(Node outputNode, String outputPort, Node inputNode, String inputPort) {
        if (outputNode == null || inputNode == null) throw new RuntimeException("Nodes cannot be null.");
        Port output = outputNode.getPort(outputPort);
        Port input = inputNode.getPort(inputPort);
        if (output == null || input == null) throw new RuntimeException("Invalid port names");
        connections.add(new Connection(this, outputNode, output, inputNode, input));
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
        if (!containsChild(port.getNode()))
            throw new IllegalArgumentException(String.format("Node %s is not a child of this macro.", port.getNode()));
        return addPort(new PublishedPort(this, port));
    }

    public void unPublishPort(Port port) {
        if (!containsChild(port.getNode()))
            throw new IllegalArgumentException(String.format("Node %s is not a child of this macro.", port.getNode()));
        removePort(port.getName());
    }

    public boolean isPublished(Port port) {
        if (!containsChild(port.getNode()))
            throw new IllegalArgumentException(String.format("Node %s is not a child of this macro.", port.getNode()));
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
            if (child.getFunction() == Function.Renderer) {
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
            if (c.getOutputPort().getName().equals("h")) {
                System.out.println("hue");
            }
            child.setValue(c.getInputPort().getName(), n.getValue(c.getOutputPort().getName()));
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
