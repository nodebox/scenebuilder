package nodebox.node;

import java.util.Comparator;

import static nodebox.util.Preconditions.checkNotNull;

public final class NodeInfo {

    public static final AlphabeticComparator ALPHABETIC_COMPARATOR = new AlphabeticComparator();

    public static NodeInfo nodeInfoFromClass(Class<? extends Node> nodeClass) {
        Description descriptionAnnotation = nodeClass.getAnnotation(Description.class);
        String description = descriptionAnnotation != null ? descriptionAnnotation.value() : "";
        Category categoryAnnotation = nodeClass.getAnnotation(Category.class);
        String category = categoryAnnotation != null ? categoryAnnotation.value() : "Uncategorized";
        Drawable drawableAnnotation = nodeClass.getAnnotation(Drawable.class);
        boolean drawable = (drawableAnnotation != null);
        return new NodeInfo(nodeClass, description, category, drawable);
    }

    private final Class<? extends Node> nodeClass;
    private final String description;
    private final String category;
    private final boolean drawable;

    public NodeInfo(Class<? extends Node> nodeClass, String description, String category, boolean drawable) {
        checkNotNull(nodeClass);
        checkNotNull(description);
        checkNotNull(category);
        this.nodeClass = nodeClass;
        this.description = description;
        this.category = category;
        this.drawable = drawable;
    }

    public Class<? extends Node> getNodeClass() {
        return nodeClass;
    }

    public String getDescription() {
        return description;
    }

    public String getCategory() {
        return category;
    }

    public boolean isDrawable() {
        return drawable;
    }

    @Override
    public String toString() {
        return nodeClass.getName();
    }

    public static class AlphabeticComparator implements Comparator<NodeInfo> {
        public int compare(NodeInfo info1, NodeInfo info2) {
            String name1 = info1.getNodeClass().getSimpleName();
            String name2 = info2.getNodeClass().getSimpleName();
            return name1.compareTo(name2);
        }
    }

}
