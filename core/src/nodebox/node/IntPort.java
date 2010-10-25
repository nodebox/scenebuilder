package nodebox.node;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * An integer port holds an integer number.
 * <p/>
 * Integer ports support both integer data and floating-point data. Floating-point data is rounded.
 */
public class IntPort extends Port implements PersistablePort {

    private int value;
    private Map<Integer, String> menuItems;

    public IntPort(Node node, String name, Direction direction) {
        super(node, name, direction);
    }

    public IntPort(Node node, String name, Direction direction, int value) {
        super(node, name, direction);
        this.value = value;
    }

    @Override
    public String getWidget() {
        if (hasMenu()) {
            return "menu";
        } else {
            return "int";
        }
    }

    public Object getValue() {
        return value;
    }

    public int get() {
        return value;
    }

    public void setValue(Object value) {
        if (value instanceof Integer) {
            set((Integer) value);
        } else if (value instanceof Float) {
            set(Math.round((Float) value));
        } else {
            throw new IllegalArgumentException(this + ": Value is not an integer.");
        }
    }

    public void set(int value) {
        this.value = value;
    }

    public Object parseValue(String value) throws IllegalArgumentException {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("The given string cannot be parsed as a number.");
        }
    }

    public String getValueAsString() {
        return Integer.toString(value);
    }

    @Override
    protected boolean canReceiveFrom(Port output) {
        // Int ports can receive data from float ports as well. The value is rounded.
        return (output.getPortClass() == IntPort.class) || (output.getPortClass() == FloatPort.class);
    }

    //// Menu support ////

    public boolean hasMenu() {
        return menuItems != null && menuItems.size() > 0;
    }

    public void addMenuItem(int key, String label) {
        ensureMenuItems();
        menuItems.put(key, label);
    }

    public void removeMenuItem(int key) {
        if (menuItems == null) return;
        menuItems.remove(key);
    }

    public Map<Integer, String> getMenuItems() {
        return menuItems != null ? menuItems : Collections.<Integer, String>emptyMap();
    }

    private void ensureMenuItems() {
        if (menuItems == null)
            menuItems = new HashMap<Integer, String>();
    }

}
