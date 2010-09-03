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
     * Parse the given string value and return a value that can be used with setValue.
     * <p/>
     * If the value is inappropriate or cannot be parsed, throw an IllegalArgumentException.
     *
     * @param value the string value to parse
     * @return a value in the correct format.
     * @throws IllegalArgumentException if the value cannot be parsed.
     */
    public abstract Object parseValue(String value) throws IllegalArgumentException;

    /**
     * Return the current port value as a string, ready to be serialized.
     * The returned string will be used with parseValue when loading a scene file.
     *
     * @return the value as a string representation.
     */
    public abstract String getValueAsString();

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
    public boolean canConnectTo(Port port) {
        checkNotNull(port);
        // One port needs to be input, the other output.
        // The direction can thus not be the same.
        if (this.direction == port.direction) return false;
        // Check if port classes are exactly the same.
        return port.getClass().equals(getClass());
    }

    @Override
    public String toString() {
        return "Port[" + node.getName() + "." + name + "]";
    }
}
