package nodebox.node;

import java.util.*;

import static nodebox.util.Preconditions.checkArgument;
import static nodebox.util.Preconditions.checkNotNull;

public class NodeManager {

    private Map<String, NodeInfo> nodeInfoMap = new HashMap<String, NodeInfo>();

    private transient HashMap<String, Set<NodeInfo>> categoryMap = null;

    public static String nodeId(Node node) {
        checkNotNull(node);
        return nodeId(node.getClass());
    }

    public static String nodeId(NodeInfo info) {
        checkNotNull(info);
        return nodeId(info.getNodeClass());
    }

    public static String nodeId(Class<? extends Node> nodeClass) {
        checkNotNull(nodeClass);
        return nodeClass.getName();
    }

    public NodeManager() {
        registerNodeInfo(NodeInfo.nodeInfoFromClass(Network.class));
    }

    public void registerNodeClass(Class<? extends Node> nodeClass) {
        registerNodeInfo(NodeInfo.nodeInfoFromClass(nodeClass));
    }

    public void registerNodeInfo(NodeInfo info) {
        checkNotNull(info);
        System.out.println("Adding node class " + nodeId(info));
        nodeInfoMap.put(nodeId(info), info);
        invalidateCategoryMap();
    }

    public void unregisterNodeInfo(String nodeId) {
        checkNotNull(nodeId);
        System.out.println("Removing node class " + nodeId);
        nodeInfoMap.remove(nodeId);
        invalidateCategoryMap();
    }

    public Class<? extends Node> getNodeClass(String nodeId) {
        checkNotNull(nodeId);
        NodeInfo info = nodeInfoMap.get(nodeId);
        if (info != null) {
            return info.getNodeClass();
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
            String category = info.getCategory();
            Set<NodeInfo> nodeInfoSet = categoryMap.get(info.getCategory());
            if (nodeInfoSet == null) {
                nodeInfoSet = new HashSet<NodeInfo>();
                categoryMap.put(category, nodeInfoSet);
            }
            nodeInfoSet.add(info);
        }
    }

    public List<String> getNodeCategories() {
        updateCategoryMap();
        return new ArrayList<String>(categoryMap.keySet());
    }

    public List<NodeInfo> getNodeInfoList(String category) {
        checkNotNull(category);
        updateCategoryMap();
        List<NodeInfo> nodeInfoList = new ArrayList<NodeInfo>(categoryMap.get(category));
        return nodeInfoList;
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
            nodeClassSet.add(nodeInfo.getNodeClass());
        }
        return nodeClassSet;
    }

    /**
     * Create a node instance.
     *
     * @param nodeId the node class name, e.g. nodebox.builtins.draw.Clear
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

}
