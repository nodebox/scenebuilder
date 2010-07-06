package scenebuilder.model;

import processing.core.PImage;
import scenebuilder.util.StringUtils;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class Port {

    public static enum Type {
        BOOLEAN, NUMBER, INTEGER, COLOR, STRING, IMAGE
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
        switch (getType()) {
            case BOOLEAN:
                if (!(value instanceof Boolean))
                    throw new IllegalArgumentException(this + ": Value is not a boolean.");
                break;
            case NUMBER:
                if (!(value instanceof Double) && !(value instanceof Integer))
                    throw new IllegalArgumentException(this + ": Value is not a double or integer.");
                break;
            case INTEGER:
                if (!(value instanceof Integer))
                    throw new IllegalArgumentException(this + ": Value is not an integer.");
                break;
            case COLOR:
                if (!(value instanceof Color))
                    throw new IllegalArgumentException(this + ": Value is not a color.");
                break;
            case STRING:
                if (!(value instanceof String))
                    throw new IllegalArgumentException(this + ": Value is not a string.");
                break;
            case IMAGE:
                if (!(value instanceof PImage))
                    throw new IllegalArgumentException(this + ": Value is not a PImage.");
                break;
        }
        if (getType() == Type.NUMBER && value instanceof Integer) {
            this.value = Double.valueOf((Integer) value);
        } else {
            this.value = value;
        }
    }

    //// Connections ////

    public boolean isConnected() {
        Macro parent = node.getParent();
        if (parent == null) return false;
        return parent.isConnected(this);
    }

    @Override
    public String toString() {
        return "Port[" + node.getName() + "." + name + "]";
    }
}
