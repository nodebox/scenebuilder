package nodebox.node;

import nodebox.util.Preconditions;
import nodebox.util.Strings;

import javax.swing.*;
import java.awt.*;
import java.util.*;

import static nodebox.util.Preconditions.checkArgument;
import static nodebox.util.Preconditions.checkNotNull;

public abstract class Node {

    public static final String DISPLAY_NAME_ATTRIBUTE = "displayName";
    public static final String DESCRIPTION_ATTRIBUTE = "description";

    public static final HashMap<Class, Integer> instanceCounts = new HashMap<Class, Integer>();

    private Network network;
    private String name = "";
    private Point position = new Point(0, 0);
    private Map<String, Object> attributes = new HashMap<String, Object>();
    private LinkedList<Port> ports = new LinkedList<Port>();

    public static Map<String, Object> createAttributes(String displayName, String description) {
        Map<String, Object> attributes = new HashMap<String, Object>();
        attributes.put(DISPLAY_NAME_ATTRIBUTE, displayName);
        attributes.put(DESCRIPTION_ATTRIBUTE, description);
        return attributes;
    }

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

    public boolean isRendering() {
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

    public String getDisplayName() {
        return getAttribute(DISPLAY_NAME_ATTRIBUTE).toString();
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

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public void setAttribute(String attribute, Object value) {
        attributes.put(attribute, value);
    }

    public Object getAttribute(String attribute) {
        return attributes.get(attribute);
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
     * @throws IllegalArgumentException if the port name is not unique or the node is not set to this node.
     * @return the given port
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
        return port.parseValue(value);
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
     * @param context the rendering context
     * @param time    the current time
     * @return false if the node did not execute successfully. The renderer will then stop rendering the current frame.
     */
    public abstract boolean execute(Context context, double time);

    @Override
    public String toString() {
        return "Node[" + name + "]";
    }
}
