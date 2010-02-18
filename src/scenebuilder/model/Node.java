package scenebuilder.model;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public abstract class Node {


    public static enum Function {
        Generator, Filter, Renderer
    }

    public static final String DISPLAY_NAME_ATTRIBUTE = "displayName";
    public static final String DESCRIPTION_ATTRIBUTE = "description";

    public static final HashMap<Class, Integer> instanceCounts = new HashMap<Class, Integer>();

    private Function function = Function.Generator;
    private Macro parent;
    private String name = "";
    private Point position = new Point(0, 0);
    private Map<String, Object> attributes = new HashMap<String, Object>();
    private Map<String, Port> ports = new HashMap<String, Port>();

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


    public Node(Function function) {
        this.function = function;
        name = getClass().getSimpleName() + createInstance(getClass());

    }

    public Function getFunction() {
        return function;
    }

    public void setFunction(Function function) {
        this.function = function;
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

    public void setName(String name) {
        this.name = name;
    }

    public Point getPosition() {
        return position;
    }

    public void setPosition(Point position) {
        this.position = position;
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

    public void addInputPort(Port.Type type, String name, Object value) {
        Port port = new Port(this, name, type, Port.Direction.INPUT, value);
        ports.put(name, port);
    }

    public void removeInputPort(String name) {
        ports.remove(name);
    }

    public void addOutputPort(Port.Type type, String name) {
        Port port = new Port(this, name, type, Port.Direction.OUTPUT, null);
        ports.put(name, port);
    }

    public void removeOutputPort(String name) {
        ports.remove(name);
    }

    public Port getPort(String name) {
        return ports.get(name);
    }

    private Port getPortChecked(String name) {
        Port p = ports.get(name);
        if (p == null) throw new IllegalArgumentException(String.format("Port %s does not exist.", name));
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
            throw new IllegalArgumentException(String.format("Port %s is not of type boolean.", name));
        }
    }

    public double asNumber(String name) {
        try {
            return (Double) getValue(name);
        } catch (ClassCastException e) {
            throw new IllegalArgumentException(String.format("Port %s is not of type number.", name));
        }
    }

    public String asString(String name) {
        try {
            return (String) getValue(name);
        } catch (ClassCastException e) {
            throw new IllegalArgumentException(String.format("Port %s is not of type string.", name));
        }
    }

    public Color asColor(String name) {
        try {
            return (Color) getValue(name);
        } catch (ClassCastException e) {
            throw new IllegalArgumentException(String.format("Port %s is not of type color.", name));
        }
    }

    public GLImage asImage(String name) {
        try {
            return (GLImage) getValue(name);
        } catch (ClassCastException e) {
            throw new IllegalArgumentException(String.format("Port %s is not of type image.", name));
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
            case COLOR:
                // TODO: Parse color
                return Color.BLACK;
            case STRING:
                return value;
            case IMAGE:
                return null;
        }
        throw new AssertionError(String.format("Unknown port type %s.",port.getType()));
    }

    //// Custom interface ////

    public JComponent createEditorComponent() { return null; }


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
}
