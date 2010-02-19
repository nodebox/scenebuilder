package scenebuilder.model;

import scenebuilder.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class Port {

    public static enum Type {
        BOOLEAN, NUMBER, COLOR, STRING, IMAGE
    }

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
    private final Type type;
    private Map<String, Object> attributes = new HashMap<String, Object>();
    private Object value;

    public Port(Node node, String name, Type type, Direction direction, Object value) {
        this.node = node;
        this.name = name.intern();
        setAttribute(DISPLAY_NAME_ATTRIBUTE, StringUtils.humanizeName(name));
        this.type = type;
        this.direction = direction;
        this.value = value;
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
            displayName = StringUtils.humanizeName(name);
        }
        setAttribute(DISPLAY_NAME_ATTRIBUTE, displayName);
    }

    public Direction getDirection() {
        return direction;
    }

    public Type getType() {
        return type;
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

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    //// Connections ////

    public boolean isConnected() {
        Macro parent = node.getParent();
        if (parent == null) return false;
        return parent.isConnected(this);
    }
}
