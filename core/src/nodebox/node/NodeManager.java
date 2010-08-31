package nodebox.node;

import java.util.*;

public class NodeManager {

    private Map<String, Class<? extends Node>> nodeClassMap = new HashMap<String, Class<? extends Node>>();

    private HashMap<String, Set<Class<? extends Node>>> categoryMap = new HashMap<String, Set<Class<? extends Node>>>();

    public NodeManager() {
        System.out.println("Constructing new node manager.");
    }

    public void addNodeClass(Class<? extends Node> c, String category) {
        System.out.println("Adding node class " + c);
        nodeClassMap.put(c.getName(), c);
//        Set<Class<? extends Node>> nodeClasses = categoryMap.get(category);
//        if (nodeClasses == null) {
//            nodeClasses = new HashSet<Class<? extends Node>>();
//            categoryMap.put(category, nodeClasses);
//        }
//        nodeClasses.add(c);
    }

    public Class<? extends Node> getNodeClass(String nodeId) {
        return nodeClassMap.get(nodeId);
    }

    public void removeNodeClass(Class<? extends Node> c) {
        System.out.println("Removing node class " + c);
        nodeClassMap.remove(c.getName());
    }

    public Set<String> getNodeCategories() {
        return categoryMap.keySet();
    }

    public Set<Class<? extends Node>> getNodeClasses(String category) {
        Set<Class<? extends Node>> nodeClasses = categoryMap.get(category);
        if (nodeClasses == null) return Collections.emptySet();
        return Collections.unmodifiableSet(nodeClasses);
    }

    public Collection<Class<? extends Node>> getNodeClasses() {
        return Collections.unmodifiableCollection(nodeClassMap.values());
    }

    /**
     * Create a node instance
     *
     * @param nodeId the node class name, e.g. nodebox.builtins.render.Clear
     * @return a node instance
     */
    public Node createNode(String nodeId) throws RuntimeException {
        System.out.println("Classes: ");
        for (String s : nodeClassMap.keySet()) {
            System.out.println("class = " + s);
        }
        System.out.println("Creating new  " + nodeId);
        try {
            Class<? extends Node> nodeClass = getNodeClass(nodeId);
            if (nodeClass == null) {
                throw new IllegalArgumentException("No node class named " + nodeId);
            } else {
                return nodeClass.newInstance();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
