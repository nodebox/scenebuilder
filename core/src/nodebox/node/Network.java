package nodebox.node;

import processing.core.PGraphics;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static nodebox.util.Preconditions.checkArgument;
import static nodebox.util.Preconditions.checkNotNull;

@Description("A node that contain other nodes")
public class Network extends Node {

    private static final Pattern NUMBER_AT_THE_END = Pattern.compile("^(.*?)(\\d*)$");

    private List<Node> children;
    private List<Connection> connections;
    private Set<Port> publishedPorts;
    private Node renderedNode;

    public Network() {
        children = new LinkedList<Node>();
        connections = new LinkedList<Connection>();
        publishedPorts = new LinkedHashSet<Port>();
    }

    @Override
    public boolean canDraw() {
        return true;
    }

    //// Children ////

    public List<Node> getChildren() {
        return new ArrayList<Node>(children);
    }

    public Node createChild(Class<? extends Node> nodeClass) {
        Node n;
        try {
            n = nodeClass.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        n.setName(uniqueChildName(n.getName()));
        addChild(n);
        return n;
    }

    public void addChild(Node child) {
        children.add(child);
        child.setNetwork(this);
    }

    public void removeChild(Node child) {
        disconnect(child);
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

    public String uniqueChildName(String prefix) {
        Matcher m = NUMBER_AT_THE_END.matcher(prefix);
        m.find();
        String namePrefix = m.group(1);
        String number = m.group(2);
        int counter;
        if (number.length() > 0) {
            counter = Integer.parseInt(number);
        } else {
            counter = 1;
        }
        while (true) {
            String suggestedName = namePrefix + counter;
            if (!hasChild(suggestedName)) {
                // We don't use rename here, since it assumes the node will be in
                // this network.
                return suggestedName;
            }
            counter++;
        }
    }

    //// Rendered node ////

    public Node getRenderedNode() {
        return renderedNode;
    }

    public void setRenderedNode(Node renderedNode) {
        this.renderedNode = renderedNode;
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
        disconnect(input);
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

    public Connection getInputConnection(Port port) {
        checkArgument(port.isInputPort(), "getInputConnection() can only take input ports.");
        for (Connection c : connections) {
            if (c.getInputPort() == port) {
                return c;
            }
        }
        return null;
    }

    public void disconnect(Node node) {
        checkNotNull(node);
        checkArgument(node.getNetwork() == this, "Node %s is not a child of this network.", node);
        LinkedList<Connection> connectionsToRemove = new LinkedList<Connection>();
        for (Connection c : connections) {
            if (c.getInputNode() == node || c.getOutputNode() == node) {
                connectionsToRemove.add(c);
            }
        }
        connections.removeAll(connectionsToRemove);
    }

    public void disconnect(Port port) {
        checkNotNull(port);
        checkArgument(port.getNode().getNetwork() == this, "Port %s is not a child of this network.", port);
        LinkedList<Connection> connectionsToRemove = new LinkedList<Connection>();
        for (Connection c : connections) {
            if (c.getInputPort() == port || c.getOutputPort() == port) {
                connectionsToRemove.add(c);
            }
        }
        connections.removeAll(connectionsToRemove);
    }

    //// Published Ports ////

    public Port publishPort(Port port) {
        if (!hasChild(port.getNode()))
            throw new IllegalArgumentException(String.format("Node %s is not a child of this network.", port.getNode()));
        PublishedPort publishedPort = new PublishedPort(this, port);
        //addPort(publishedPort);
        return publishedPort;
    }

    public void unPublishPort(Port port) {
        if (isPublished(port)) {
            PublishedPort publishedPort = null;
            for (Port p : getPorts()) {
                if (p instanceof PublishedPort) {
                    PublishedPort pp = (PublishedPort) p;
                    if (pp.getOriginalPort() == port)
                        publishedPort = pp;
                }
            }

            if (getNetwork() != null && publishedPort.isConnected())
                getNetwork().disconnect(publishedPort);

            removePort(publishedPort);
        }
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
     * Execute the rendered child node in the network.
     *
     * @param context the rendering context
     * @param time    the current time
     */
    @Override
    public void execute(Context context, float time) {
        if (renderedNode != null) {
            renderedNode.update(context, time);
        }
    }

    /**
     * Draw the rendered child node in the network.
     *
     * @param context the rendering context
     * @param time    the current time
     */
    @Override
    public void draw(PGraphics g, Context context, float time) {
        if (renderedNode != null) {
            renderedNode.draw(g, context, time);
        }
    }

    public boolean isConnected(Port port) {
        for (Connection c : connections) {
            if (c.getInputPort() == port || c.getOutputPort() == port) return true;
        }
        return false;
    }

}
