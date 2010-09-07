package nodebox.node;

import nodebox.util.Strings;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import static nodebox.util.Preconditions.checkArgument;
import static nodebox.util.Preconditions.checkNotNull;

public abstract class Node {

    public static final String DISPLAY_NAME_ATTRIBUTE = "displayName";
    public static final String DESCRIPTION_ATTRIBUTE = "description";

    public static final HashMap<Class, Integer> instanceCounts = new HashMap<Class, Integer>();

    private Network network;
    private String name = "";
    private Point position = new Point(0, 0);
    private Map<String, String> attributes = new HashMap<String, String>();
    private LinkedList<Port> ports = new LinkedList<Port>();

    public static int createInstance(Class c) {
        synchronized (instanceCounts) {
            Integer index = instanceCounts.get(c);
            if (index == null) {
                index = 1;
            } else {
                index += 1;
            }
            instanceCounts.put(c, index);
            return index;
        }
    }

    public Node() {
        name = getClass().getSimpleName() + createInstance(getClass());
        setAttribute(DISPLAY_NAME_ATTRIBUTE, Strings.humanizeName(name));
        setAttribute(DESCRIPTION_ATTRIBUTE, "Generic node.");
    }

    public boolean canDraw() {
        return false;
    }

    public Network getNetwork() {
        return network;
    }

    public void setNetwork(Network network) {
        this.network = network;
    }

    public String getName() {
        return name;
    }

    public void setDisplayName(String displayName) {
        if (displayName == null || displayName.trim().length() == 0) {
            displayName = Strings.humanizeName(name);
        }
        setAttribute(DISPLAY_NAME_ATTRIBUTE, displayName);
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplayName() {
        return getAttribute(Node.DISPLAY_NAME_ATTRIBUTE);
    }

    public String getDescription() {
        Description description = getClass().getAnnotation(Description.class);
        if (description != null) {
            return description.value();
        } else {
            return "";
        }
    }

    public Point getPosition() {
        return position;
    }

    public void setPosition(Point position) {
        this.position = position;
    }

    public void setPosition(int x, int y) {
        this.position = new Point(x, y);
    }

    //// Attributes ////

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public void setAttribute(String attribute, String value) {
        attributes.put(attribute, value);
    }

    public String getAttribute(String attribute) {
        String v = attributes.get(attribute);
        return v != null ? v : "";
    }

    //// Ports ////

    public java.util.List<Port> getPorts() {
        return ports;
    }

    public java.util.List<Port> getInputPorts() {
        ArrayList<Port> inputPorts = new ArrayList<Port>();
        for (Port port : ports) {
            if (port.getDirection() == Port.Direction.INPUT) {
                inputPorts.add(port);
            }
        }
        return inputPorts;
    }

    public java.util.List<Port> getOutputPorts() {
        ArrayList<Port> inputPorts = new ArrayList<Port>();
        for (Port port : ports) {
            if (port.getDirection() == Port.Direction.OUTPUT) {
                inputPorts.add(port);
            }
        }
        return inputPorts;
    }

    /**
     * Add a port to this node.
     * <p/>
     * The port.node should be set to this node, and the port name should be unique.
     *
     * @param port the port to add.
     * @return the given port
     * @throws IllegalArgumentException if the port name is not unique or the node is not set to this node.
     */
    public Port addPort(Port port) {
        checkArgument(port.getNode() == this, "Port.node should be set to this.");
        checkArgument(!hasPort(port.getName()), "This node already has a port named %s", port.getName());
        ports.add(port);
        return port;
    }

    /**
     * Remove the given port from this node.
     *
     * @param port the port to remove.
     */
    public void removePort(Port port) {
        ports.remove(port);
    }

    /**
     * Remove the port with the given name.
     *
     * @param name the name of the port.
     * @throws IllegalArgumentException if a port with the given name could not be found.
     */
    public void removePort(String name) throws IllegalArgumentException {
        Port port = getPort(name);
        removePort(port);
    }

    /**
     * Check if this node has a port with the given name.
     *
     * @param name the name to check.
     * @return true if this node has a port with the given name.
     */
    public boolean hasPort(String name) {
        for (Port p : ports) {
            if (p.getName().equals(name)) return true;
        }
        return false;
    }

    /**
     * Get the port with the given name.
     *
     * @param name the port name.
     * @return the port object
     * @throws IllegalArgumentException if a port with the given name does not exist.
     */
    public Port getPort(String name) throws IllegalArgumentException {
        checkNotNull(name);
        for (Port p : ports) {
            if (p.getName().equals(name)) return p;
        }
        throw new IllegalArgumentException("This node has no port named " + name);
    }

    //// Port values ////

    public void setValue(String portName, Object value) {
        Port port = getPort(portName);
        port.setValue(value);
    }

    public Object getValue(String portName) {
        Port port = getPort(portName);
        return port.getValue();
    }

    public Object parseValue(String portName, String value) {
        checkNotNull(value);
        Port port = getPort(portName);
        if (port.isPersistable()) {
            PersistablePort persistablePort = (PersistablePort) port;
            return persistablePort.parseValue(value);
        } else {
            return null;
        }
    }

    //// Custom interface ////

    public JComponent createEditorComponent() {
        return null;
    }


    //// Execution ////

    public boolean startExecution(Context context) {
        return true;
    }

    public void stopExecution(Context context) {
    }

    /**
     * Process the node and update the values of the output ports.
     *
     * @param context the drawing context
     * @param time    the current time
     */
    public abstract void execute(Context context, float time);

    @Override
    public String toString() {
        return "Node[" + name + "]";
    }
}
