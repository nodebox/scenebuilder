package scenebuilder.model;

import processing.core.PImage;
import scenebuilder.util.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

public abstract class Node {

    public static final String DISPLAY_NAME_ATTRIBUTE = "displayName";
    public static final String DESCRIPTION_ATTRIBUTE = "description";

    public static final HashMap<Class, Integer> instanceCounts = new HashMap<Class, Integer>();

    private Macro parent;
    private String name = "";
    private Point position = new Point(0, 0);
    private Map<String, Object> attributes = new HashMap<String, Object>();
    private Map<String, Port> ports = new LinkedHashMap<String, Port>();

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
        setAttribute(DISPLAY_NAME_ATTRIBUTE, StringUtils.humanizeName(name));
        setAttribute(DESCRIPTION_ATTRIBUTE, "Generic node.");
    }

    public boolean isRendering() {
        return false;
    }

    public Macro getParent() {
        return parent;
    }

    public void setParent(Macro parent) {
        this.parent = parent;
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
        return new LinkedList<Port>(ports.values());
    }

    public java.util.List<Port> getInputPorts() {
        LinkedList<Port> inputPorts = new LinkedList<Port>();
        for (Port port : ports.values()) {
            if (port.getDirection() == Port.Direction.INPUT) {
                inputPorts.add(port);
            }
        }
        return inputPorts;
    }

    public java.util.List<Port> getOutputPorts() {
        LinkedList<Port> inputPorts = new LinkedList<Port>();
        for (Port port : ports.values()) {
            if (port.getDirection() == Port.Direction.OUTPUT) {
                inputPorts.add(port);
            }
        }
        return inputPorts;
    }

    public Port addPort(Port port) {
        ports.put(port.getName(), port);
        return port;
    }

    public void removePort(String name) {
        ports.remove(name);
    }

    public Port addInputPort(Port.Type type, String name, Object value) {
        Port port = new Port(this, name, type, Port.Direction.INPUT, value);
        ports.put(name, port);
        return port;
    }

    public void removeInputPort(String name) {
        ports.remove(name);
    }

    public Port addOutputPort(Port.Type type, String name) {
        Port port = new Port(this, name, type, Port.Direction.OUTPUT, null);
        ports.put(name, port);
        return port;
    }

    public void removeOutputPort(String name) {
        ports.remove(name);
    }

    public Port getPort(String name) {
        return ports.get(name);
    }

    private Port getPortChecked(String name) {
        Port p = ports.get(name);
        if (p == null) throw new IllegalArgumentException(String.format("%s: port %s does not exist.", this, name));
        return p;
    }

    //// Port values ////

    public Object getValue(String name) {
        Port port = getPortChecked(name);
        return port.getValue();
    }

    public boolean asBoolean(String name) {
        try {
            return (Boolean) getValue(name);
        } catch (ClassCastException e) {
            throw new IllegalArgumentException(String.format("%s: port %s is not of type boolean.", this, name));
        }
    }

    public double asNumber(String name) {
        try {
            return (Double) getValue(name);
        } catch (ClassCastException e) {
            throw new IllegalArgumentException(String.format("%s: port %s is not of type number.", this, name));
        }
    }

    public int asInteger(String name) {
        try {
            return (Integer) getValue(name);
        } catch (ClassCastException e) {
            throw new IllegalArgumentException(String.format("%s: port %s is not of type integer.", this, name));
        }
    }

    public String asString(String name) {
        try {
            return (String) getValue(name);
        } catch (ClassCastException e) {
            throw new IllegalArgumentException(String.format("%s: port %s is not of type string.", this, name));
        }
    }

    public Color asColor(String name) {
        try {
            return (Color) getValue(name);
        } catch (ClassCastException e) {
            throw new IllegalArgumentException(String.format("%s: port %s is not of type color.", this, name));
        }
    }

    public PImage asImage(String name) {
        try {
            return (PImage) getValue(name);
        } catch (ClassCastException e) {
            throw new IllegalArgumentException(String.format("%s: port %s is not of type image.", this, name));
        }
    }

    public void setValue(String name, Object value) {
        Port port = getPortChecked(name);
        port.setValue(value);
    }

    public Object parseValue(String portName, String value) {
        Port port = getPortChecked(portName);
        if (value == null) return null;
        switch (port.getType()) {
            case BOOLEAN:
                return Boolean.parseBoolean(value);
            case NUMBER:
                return Double.parseDouble(value);
            case INTEGER:
                return Integer.parseInt(value);
            case COLOR:
                // TODO: Parse color
                return Color.BLACK;
            case STRING:
                return value;
            case IMAGE:
                return null;
        }
        throw new AssertionError(String.format("Unknown port type %s.", port.getType()));
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
