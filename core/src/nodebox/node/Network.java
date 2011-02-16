package nodebox.node;

import processing.core.PGraphics;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static nodebox.util.Preconditions.checkArgument;
import static nodebox.util.Preconditions.checkNotNull;

@Description("A node that contain other nodes")
@Category("Utility")
public class Network extends Node {

    private static final Pattern NUMBER_AT_THE_END = Pattern.compile("^(.*?)(\\d*)$");

    private Scene scene;
    private List<Node> children;
    private List<Connection> connections;
    private Set<Port> publishedPorts;
    private Node renderedNode;
    private Set<Node> activatedNodes = Collections.emptySet();

    public Network() {
        children = new LinkedList<Node>();
        connections = new LinkedList<Connection>();
        publishedPorts = new LinkedHashSet<Port>();
    }

    @Override
    public Scene getScene() {
        return scene;
    }

    public void setScene(Scene scene) {
        checkNotNull(scene, "The scene object cannot be null.");
        this.scene = scene;
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
        n.initialize();
        return n;
    }

    public void addChild(Node child) {
        if (child.getNetwork() != null)
            child.getNetwork().removeChild(child, false);
        children.add(child);
        child.setNetwork(this);
        if (scene != null)
            scene.fireChildAdded(this, child);
    }

    public void removeChild(Node child) {
        removeChild(child, true);
    }

    public void removeChild(Node child, boolean destroy) {
        if (child.isRenderedNode()) {
            setRenderedNode(null);
        }
        disconnect(child);
        children.remove(child);
        child.setNetwork(null);
        if (destroy)
            child.destroy();
        if (scene != null)
            scene.fireChildRemoved(this, child);
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

    protected void markChildDirty(Node node) {
        checkNotNull(node);
        for (Connection c : connections) {
            if (node == c.getOutputNode()) {
                c.getInputNode().markDirty();
            }
        }
    }

    @Override
    public boolean hasExternalInputs() {
        boolean external = super.hasExternalInputs();
        if (external) return true;
        Node renderedNode = getRenderedNode();
        if (renderedNode != null)
            return renderedNode.hasExternalInputs();
        return false;
    }

    //// Activate/deactivate nodes ////

    private Set<Node> reachableNodes() {
        return reachableNodes(getRenderedNode(), new HashSet<Node>());
    }

    private Set<Node> reachableNodes(Node node, Set<Node> reachableNodes) {
        if (node == null) return reachableNodes;
        reachableNodes.add(node);
        for (Node dependency : node.getDependencies()) {
            reachableNodes(dependency, reachableNodes);
        }
        return reachableNodes;
    }

    private boolean isActivated(Node node) {
        return activatedNodes.contains(node);
    }

    /**
     * Activate/deactivate nodes. Call this method whenever network reachability changes, such as connecting/disconnecting
     * a node or removing a node.
     *
     * @param oldReachableNodes the list of old reachable nodes
     * @param newReachableNodes the list of newly reachable node.
     */
    private void updateNodeActivation(Set<Node> oldReachableNodes, Set<Node> newReachableNodes) {
        // The nodes to activate are all newly reachable nodes, minus the ones that were already active.
        Set<Node> nodesToActivate = new HashSet<Node>(newReachableNodes);
        nodesToActivate.removeAll(this.activatedNodes);

        // The nodes to deactivate are all the old reachable nodes, minus the nodes that are still active.
        // These nodes do not need to be deactivated since they will be reachable afterwards.
        Set<Node> nodesToDeactivate = new HashSet<Node>(oldReachableNodes);
        nodesToDeactivate.removeAll(newReachableNodes);

        for (Node n : nodesToDeactivate) {
            n.deactivate();
        }
        for (Node n : nodesToActivate) {
            n.activate();
        }

        this.activatedNodes = newReachableNodes;
    }

    //// Rendered node ////

    public Node getRenderedNode() {
        return renderedNode;
    }

    public void setRenderedNode(Node renderedNode) {
        Set<Node> oldReachableNodes = reachableNodes();
        this.renderedNode = renderedNode;
        Set<Node> newReachableNodes = reachableNodes();
        updateNodeActivation(oldReachableNodes, newReachableNodes);
        markDirty();
    }

    //// Connections ////

    public Collection<Connection> getConnections() {
        return connections;
    }

    public Connection connect(Port output, Port input) {
        checkNotNull(output);
        checkNotNull(input);
        checkArgument(output.getNode().getNetwork() == this, "Output %s is not a child of this network.", output);
        checkArgument(input.getNode().getNetwork() == this, "Input %s is not a child of this network.", input);
        disconnect(input);
        Connection conn = new Connection(this, output, input);
        ArrayList<Connection> newConnections = new ArrayList<Connection>(connections);
        newConnections.add(conn);
        CycleDetector detector = new CycleDetector(newConnections);
        // This check will throw an IllegalArgumentException, which is the exception we want.
        checkArgument(!detector.hasCycles(), "Creating this connection would cause a cyclic dependency.");
        connections = newConnections;
        input.getNode().markDirty();
        if (scene != null)
            scene.fireConnectionAdded(this, conn);
        return conn;
    }

    public Collection<Connection> getConnections(Node node) {
        LinkedList<Connection> cs = new LinkedList<Connection>();
        for (Connection c : connections) {
            if (c.getInputNode() == node || c.getOutputNode() == node) {
                cs.add(c);
            }
        }
        return cs;
    }

    public Collection<Connection> getConnections(Port port) {
        if (port.isInputPort()) {
            LinkedList<Connection> cs = new LinkedList<Connection>();
            Connection c = getInputConnection(port);
            if (c != null)
                cs.add(c);
            return cs;
        } else {
            return getOutputConnections(port);
        }
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

    public Collection<Connection> getOutputConnections(Node node) {
        LinkedList<Connection> cs = new LinkedList<Connection>();
        for (Connection c : connections) {
            if (c.getOutputNode() == node) {
                cs.add(c);
            }
        }
        return cs;
    }

    public Collection<Connection> getOutputConnections(Port port) {
        checkArgument(port.isOutputPort(), "getOutputConnections() can only take output ports.");
        LinkedList<Connection> cs = new LinkedList<Connection>();
        for (Connection c : connections) {
            if (c.getOutputPort() == port) {
                cs.add(c);
            }
        }
        return cs;
    }

    public void disconnect(Node node) {
        checkNotNull(node);
        checkArgument(node.getNetwork() == this, "Node %s is not a child of this network.", node);

        Set<Node> oldReachableNodes = reachableNodes();

        LinkedList<Connection> connectionsToRemove = new LinkedList<Connection>();
        for (Connection c : connections) {
            if (c.getInputNode() == node || c.getOutputNode() == node) {
                if (scene != null)
                    scene.fireConnectionRemoved(this, c);
                connectionsToRemove.add(c);
            }
        }
        connections.removeAll(connectionsToRemove);

        if (node.isRenderedNode()) {
            setRenderedNode(null);
        }

        Set<Node> newReachableNodes = reachableNodes();
        updateNodeActivation(oldReachableNodes, newReachableNodes);
    }

    public void disconnect(Port port) {
        checkNotNull(port);
        checkArgument(port.getNode().getNetwork() == this, "Port %s is not a child of this network.", port);

        Set<Node> oldReachableNodes = reachableNodes();

        LinkedList<Connection> connectionsToRemove = new LinkedList<Connection>();
        for (Connection c : connections) {
            if (c.getInputPort() == port || c.getOutputPort() == port) {
                if (scene != null)
                    scene.fireConnectionRemoved(this, c);
                connectionsToRemove.add(c);
            }
        }
        connections.removeAll(connectionsToRemove);

        Set<Node> newReachableNodes = reachableNodes();
        updateNodeActivation(oldReachableNodes, newReachableNodes);
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
     * Execute the child nodes in the network that should always evaluate
     * as well as the rendered child node.
     *
     * @param context the rendering context
     * @param time    the current time
     */
    @Override
    public void execute(Context context, float time) {
        if (renderedNode != null) {
            renderedNode.update(context, time);
        }
        for (Node childNode : children) {
            if (childNode != renderedNode && childNode.shouldAlwaysEvaluate())
                childNode.update(context, time);
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

    public boolean isConnected(Node node) {
        for (Connection c : connections) {
            if (c.getInputNode() == node || c.getOutputNode() == node) return true;
        }
        return false;
    }

    public boolean isConnected(Port port) {
        for (Connection c : connections) {
            if (c.getInputPort() == port || c.getOutputPort() == port) return true;
        }
        return false;
    }

    public boolean isConnectedTo(Node node1, Node node2) {
        if (node1 == node2) return false;
        for (Connection c : connections) {
            if ((node1 == c.getOutputNode() && node2 == c.getInputNode()) ||
                (node1 == c.getInputNode() && node2 == c.getOutputNode())) {
                return true;
            }
        }
        return false;
    }

    public boolean isConnectedTo(Port port1, Port port2) {
        if (port1.getDirection() == port2.getDirection()) return false;
        Port output = port1.isOutputPort() ? port1 : port2;
        Port input = port1.isInputPort() ? port1 : port2;
        for (Connection c : connections) {
            if (output == c.getOutputPort() && input == c.getInputPort()) {
                return true;
            }
        }
        return false;
    }

    public boolean isConnectedTo(Port port, Node node) {
        boolean isInput = port.isInputPort();

        for (Connection c : connections) {
            if (isInput) {
                if (c.getInputPort() == port && c.getOutputNode() == node) {
                    return true;
                }
            } else {
                if (c.getOutputPort() == port && c.getInputNode() == node) {
                    return true;
                }
            }
        }
        return false;
    }
}
