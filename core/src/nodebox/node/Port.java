package nodebox.node;

import nodebox.util.Strings;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import static nodebox.util.Preconditions.checkNotNull;
import static nodebox.util.Preconditions.checkState;

/**
 * A port is a connection point on a node. It contains the values that influence the behavior of the node.
 * <p/>
 * It is a good design practice to name the native accessors (the ones that return/set the internal data type) get/set.
 */
public abstract class Port {

    public static final String DISPLAY_NAME_ATTRIBUTE = "displayName";
    public static final String MINIMUM_VALUE_ATTRIBUTE = "minimumValue";
    public static final String MAXIMUM_VALUE_ATTRIBUTE = "maximumValue";
    public static final String DEFAULT_VALUE_ATTRIBUTE = "defaultValue";

    public static enum Direction {
        INPUT, OUTPUT
    }

    private final Node node;
    private final String name;
    private final Direction direction;
    private Map<String, Object> attributes = new HashMap<String, Object>();
    private transient boolean dirty;

    public Port(Node node, String name, Direction direction) {
        this.node = node;
        this.name = name.intern();
        setAttribute(DISPLAY_NAME_ATTRIBUTE, Strings.humanizeName(name));
        this.direction = direction;
        node.addPort(this);
    }

    public Node getNode() {
        return node;
    }

    public String getWidget() {
        return null;
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

    public Direction getDirection() {
        return direction;
    }

    public boolean isInputPort() {
        return direction == Direction.INPUT;
    }

    public boolean isOutputPort() {
        return direction == Direction.OUTPUT;
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

    //// Value /////

    /**
     * Get the value for this port as an object.
     *
     * @return the value as an object.
     */
    public abstract Object getValue();

    /**
     * Set the value to this port.
     * The value will not be parsed (use parseValue for that), but only cast to the correct internal type.
     *
     * @param value the value to be set
     * @throws IllegalArgumentException if the value is invalid for this port.
     */
    public abstract void setValue(Object value) throws IllegalArgumentException;

    protected void markDirty() {
        if (dirty) return;
        dirty = true;
        fireValueChanged();
    }

    protected void fireValueChanged() {
        //firePortValueChanged(getNode(), this);
//        getLibrary().fireValueChanged(getNode(), this);
//        getNode().markDirty();
    }

    /**
     * Check if this port can write and read its value as a string.
     * <p/>
     * If it can't, the port value is not persisted in the file.
     * <p/>
     * The default implementation checks if the port is an instance of PersistablePort.
     * <p/>
     * Ports that can be persisted are required to implement this interface.
     *
     * @return true if the port can persist itself.
     */
    public boolean isPersistable() {
        return this instanceof PersistablePort;
    }


    public Class getPortClass() {
        return this.getClass();
    }

    //// Connections ////

    /**
     * Check if this port is connected to another port.
     *
     * @return true if this port is connected.
     */
    public boolean isConnected() {
        Network parent = node.getNetwork();
        return parent != null && parent.isConnected(this);
    }

    /**
     * Check if this port is connected to the given port.
     *
     * @return true if the two ports are connected.
     */
    public boolean isConnectedTo(Port port) {
        Network parent = node.getNetwork();
        return parent != null && parent.isConnectedTo(this, port);
    }

    /**
     * Check if this port is connected to any of the ports of the given node.
     *
     * @return true if a connection exists.
     */
    public boolean isConnectedTo(Node node) {
        Network parent = node.getNetwork();
        return parent != null && parent.isConnectedTo(this, node);
    }

    public Collection<Connection> getConnections() {
        Network parent = node.getNetwork();
        if (parent != null) {
            return parent.getConnections(this);
        } else {
            return new LinkedList<Connection>();
        }
    }

    public Connection getConnection() {
        checkState(isInputPort(), "Get connection only works on input ports.");
        Network parent = node.getNetwork();
        if (parent != null) {
            return parent.getInputConnection(this);
        } else {
            return null;
        }
    }

    public Collection<Connection> getOutputConnections() {
        checkState(isOutputPort(), "Get output connections only works on output ports.");
        Network parent = node.getNetwork();
        if (parent != null) {
            return parent.getOutputConnections(this);
        } else {
            return new LinkedList<Connection>();
        }
    }

    /**
     * Check if this port can connect to the given port.
     * <p/>
     * The default implementation checks to see if both port classes are exactly the same.
     *
     * @param port the port to connect to.
     * @return true if this port can connect to the given port.
     */
    public final boolean canConnectTo(Port port) {
        checkNotNull(port);
        if (port instanceof VariantPort || this instanceof VariantPort) return true;
        // One port needs to be input, the other output.
        // The direction can thus not be the same.
        if (this.direction == port.direction) return false;
        if (this.direction == Direction.INPUT) {
            return this.canReceiveFrom(port);
        } else if (port.direction == Direction.INPUT) {
            return port.canReceiveFrom(this);
        } else {
            throw new AssertionError("No input ports. This should never happen.");
        }
    }

    /**
     * Check whether this input port can receive values from the given output port.
     * <p/>
     * This method is called by canConnectTo. It is guaranteed to be called only on input ports,
     * and the given port is guaranteed to be an output port.
     * <p/>
     * The default implementation checks to see if both port classes are exactly the same.
     *
     * @param output the output port
     * @return true if this port can receive data from the given output port
     */
    protected boolean canReceiveFrom(Port output) {
        // Check if port classes are exactly the same.
        return this.getPortClass().equals(output.getPortClass());
    }

    @Override
    public String toString() {
        return "Port[" + node.getName() + "." + name + "]";
    }
}
