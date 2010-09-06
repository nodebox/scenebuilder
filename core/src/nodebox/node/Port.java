package nodebox.node;

import nodebox.util.Strings;

import java.util.HashMap;
import java.util.Map;

import static nodebox.util.Preconditions.checkNotNull;

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
     * Check if this port can connect to the given port.
     * <p/>
     * The default implementation checks to see if both port classes are exactly the same.
     *
     * @param port the port to connect to.
     * @return true if this port can connect to the given port.
     */
    public final boolean canConnectTo(Port port) {
        checkNotNull(port);
        // One port needs to be input, the other output.
        // The direction can thus not be the same.
        if (this.direction == port.direction) return false;
        if (this.direction == Direction.INPUT) {
            return this.canReceiveFrom(port);
        } else if (port.direction == Direction.INPUT) {
            return this.canReceiveFrom(port);
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
        return this.getClass().equals(output.getClass());
    }

    @Override
    public String toString() {
        return "Port[" + node.getName() + "." + name + "]";
    }
}
