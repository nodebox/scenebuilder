package scenebuilder.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class NodeManager {

    private static NodeManager instance = new NodeManager();

    public static NodeManager getInstance() {
        return instance;
    }

    private HashMap<String, Set<Class<? extends Node>>> categoryMap = new HashMap<String, Set<Class<? extends Node>>>();

    private NodeManager() {
    }

    public void addNodeClass(Class<? extends Node> c, String category) {
        Set<Class<? extends Node>> nodeClasses = categoryMap.get(category);
        if (nodeClasses == null) {
            nodeClasses = new HashSet<Class<? extends Node>>();
            categoryMap.put(category, nodeClasses);
        }
        nodeClasses.add(c);
    }

    public Set<String> getNodeCategories() {
        return categoryMap.keySet();
    }

    public Set<Class<? extends Node>> getNodeClasses(String category) {
        Set<Class<? extends Node>> nodeClasses = categoryMap.get(category);
        if (nodeClasses == null) return Collections.emptySet();
        return Collections.unmodifiableSet(nodeClasses);
    }


}
