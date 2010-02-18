package scenebuilder.model;

import java.util.HashSet;
import java.util.Set;

public class Context {

    private Set<Node> executedNodes = new HashSet<Node>();
    private double mouseX, mouseY;

    public double getMouseX() {
        return mouseX;
    }

    public void setMouseX(double mouseX) {
        this.mouseX = mouseX;
    }

    public double getMouseY() {
        return mouseY;
    }

    public void setMouseY(double mouseY) {
        this.mouseY = mouseY;
    }

    public boolean hasExecuted(Node node) {
        return executedNodes.contains(node);
    }

    public void addToExecutedNodes(Node node) {
        executedNodes.add(node);
    }

}
