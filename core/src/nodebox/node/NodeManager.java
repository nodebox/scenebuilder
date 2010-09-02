package nodebox.node;

import java.util.*;

import static nodebox.util.Preconditions.checkNotNull;

public class NodeManager {

    private Map<String, NodeInfo> nodeInfoMap = new HashMap<String, NodeInfo>();

    private transient HashMap<String, Set<NodeInfo>> categoryMap = null;

    private static String nodeId(Class<? extends Node> nodeClass) {
        checkNotNull(nodeClass);
        return nodeClass.getName();
    }

    public NodeManager() {
        System.out.println("Constructing new node manager.");
        registerNodeClass(Network.class, "Utility");
    }

    public void registerNodeClass(Class<? extends Node> nodeClass, String category) {
        checkNotNull(nodeClass);
        checkNotNull(category);
        System.out.println("Adding node class " + nodeId(nodeClass));
        NodeInfo info = new NodeInfo(nodeClass, category);
        nodeInfoMap.put(nodeId(nodeClass), info);
        invalidateCategoryMap();
    }

    public void unregisterNodeClass(Class<? extends Node> nodeClass) {
        checkNotNull(nodeClass);
        System.out.println("Removing node class " + nodeClass);
        nodeInfoMap.remove(nodeClass.getName());
        invalidateCategoryMap();
    }

    public Class<? extends Node> getNodeClass(String nodeId) {
        checkNotNull(nodeId);
        NodeInfo info = nodeInfoMap.get(nodeId);
        if (info != null) {
            return info.nodeClass;
        } else {
            return null;
        }
    }

    private void invalidateCategoryMap() {
        categoryMap = null;
    }

    private void updateCategoryMap() {
        if (categoryMap != null) return;
        categoryMap = new HashMap<String, Set<NodeInfo>>(nodeInfoMap.size() / 2);
        for (NodeInfo info : nodeInfoMap.values()) {
            String category = info.category;
            Set<NodeInfo> nodeInfoSet = categoryMap.get(info.category);
            if (nodeInfoSet == null) {
                nodeInfoSet = new HashSet<NodeInfo>();
                categoryMap.put(category, nodeInfoSet);
            }
            nodeInfoSet.add(info);
        }
    }

    public Set<String> getNodeCategories() {
        updateCategoryMap();
        return categoryMap.keySet();
    }

    public Set<Class<? extends Node>> getNodeClasses(String category) {
        checkNotNull(category);
        updateCategoryMap();
        Set<NodeInfo> nodeInfoSet = categoryMap.get(category);
        return extractNodeClasses(nodeInfoSet);
    }

    public Collection<Class<? extends Node>> getNodeClasses() {
        return extractNodeClasses(nodeInfoMap.values());
    }

    private Set<Class<? extends Node>> extractNodeClasses(Collection<NodeInfo> nodeInfoSet) {
        HashSet<Class<? extends Node>> nodeClassSet = new HashSet<Class<? extends Node>>();
        for (NodeInfo nodeInfo : nodeInfoSet) {
            nodeClassSet.add(nodeInfo.nodeClass);
        }
        return nodeClassSet;
    }

    /**
     * Create a node instance.
     *
     * @param nodeId the node class name, e.g. nodebox.builtins.render.Clear
     * @return a node instance
     * @throws RuntimeException If the node class could not be found or could not be instantiated.
     */
    public Node createNode(String nodeId) throws RuntimeException {
        checkNotNull(nodeId);
        System.out.println("Creating new node  " + nodeId);
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

    private class NodeInfo {
        public final Class<? extends Node> nodeClass;
        public final String category;

        private NodeInfo(Class<? extends Node> nodeClass, String category) {
            this.nodeClass = nodeClass;
            this.category = category;
        }
    }

}
