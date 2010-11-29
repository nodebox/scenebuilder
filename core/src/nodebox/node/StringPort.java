package nodebox.node;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class StringPort extends Port implements PersistablePort, MenuPort {

    private String value;
    private Map<String, String> menuItems;

    public StringPort(Node node, String name, Direction direction) {
        super(node, name, direction);
    }

    public StringPort(Node node, String name, Direction direction, String value) {
        super(node, name, direction);
        this.value = value;
    }

    @Override
    public String getWidget() {
        if (hasMenu()) {
            return "menu";
        } else {
            return "string";
        }
    }

    @Override
    public Object getValue() {
        return value;
    }

    public String get() {
        return value;
    }

    @Override
    public void setValue(Object value) throws IllegalArgumentException {
        if (value instanceof String) {
            this.value = (String) value;
        } else if (value == null) {
            set("");
        } else {
            throw new IllegalArgumentException("Given value is not a string.");
        }
    }

    public void set(String value) {
        this.value = value;
    }

    public Object parseValue(String value) throws IllegalArgumentException {
        return value;
    }

    public String getValueAsString() {
        return value;
    }

    //// Menu support ////

    public boolean hasMenu() {
        return menuItems != null && menuItems.size() > 0;
    }

    public void addMenuItem(String key, String label) {
        ensureMenuItems();
        menuItems.put(key, label);
    }

    public void removeMenuItem(String key) {
        if (menuItems == null) return;
        menuItems.remove(key);
    }

    public Map<String, String> getMenuItems() {
        return menuItems != null ? menuItems : Collections.<String, String>emptyMap();
    }

    private void ensureMenuItems() {
        if (menuItems == null)
            menuItems = new LinkedHashMap<String, String>();
    }
}
