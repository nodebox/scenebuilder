package nodebox.node;

import processing.core.PApplet;
import processing.core.PGraphics;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Context {

    private Set<Node> executedNodes = new HashSet<Node>();
    private HashMap<Node, HashMap<String, Object>> nodeValues = new HashMap<Node, HashMap<String, Object>>();
    private PApplet applet;

    public Context(PApplet applet) {
        this.applet = applet;
    }

    public Context(Context parentContext) {
        this.applet = parentContext.applet;
        nodeValues = new HashMap<Node, HashMap<String, Object>>(parentContext.nodeValues);
    }

    public int getMouseX() {
        return applet.mouseX;
    }

    public int getMouseY() {
        return applet.mouseY;
    }

    public boolean hasExecuted(Node node) {
        return executedNodes.contains(node);
    }

    public void addToExecutedNodes(Node node) {
        executedNodes.add(node);
    }

    //// Node values ////

    public Map<String, Object> valuesForNode(Node node) {
        return nodeValues.get(node);
    }

    public Object getValueForNodeKey(Node node, String key) {
        Map<String, Object> values = nodeValues.get(node);
        if (values == null) return null;
        return values.get(key);
    }

    public Set<String> keysForNodes(Node node) {
        Map<String, Object> values = nodeValues.get(node);
        if (values == null) return new HashSet<String>(0);
        return values.keySet();
    }

    public void setValueForNodeKey(Node node, String key, Object value) {
        HashMap<String, Object> values = nodeValues.get(node);
        if (values == null) {
            values = new HashMap<String, Object>();
            nodeValues.put(node, values);
        }
        values.put(key, value);

    }

    public PApplet getApplet() {
        return applet;
    }

    public PGraphics getGraphics() {
        return applet.g;
    }


}
