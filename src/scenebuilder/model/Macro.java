package scenebuilder.model;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class Macro extends Node {

    private List<Node> children;
    private List<Connection> connections;

    public Macro() {
        super(Function.Filter);
        children = new LinkedList<Node>();
        connections = new LinkedList<Connection>();
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

    //// Connections ////

    public Collection<Connection> getConnections() {
        return connections;
    }

    public void connect(Node outputNode, String outputPort, Node inputNode, String inputPort) {
        Port output = outputNode.getPort(outputPort);
        Port input = inputNode.getPort(inputPort);
        if (outputNode == null || inputNode == null) throw new RuntimeException("Nodes cannot be null.");
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
