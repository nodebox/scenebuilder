package scenebuilder.model;

import java.util.HashSet;
import java.util.Set;

public class Context {

    private Set<Node> executedNodes = new HashSet<Node>();


    public boolean hasExecuted(Node node) {
        return executedNodes.contains(node);
    }

    public void addToExecutedNodes(Node node) {
        executedNodes.add(node);
    }

}
