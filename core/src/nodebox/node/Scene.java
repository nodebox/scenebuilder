package nodebox.node;

public class Scene {

    private Network rootNetwork;

    public Scene() {
        rootNetwork = new Network();
        rootNetwork.setName("root");
        rootNetwork.setAttribute(Node.DISPLAY_NAME_ATTRIBUTE, "Root");
    }

    public Network getRootNetwork() {
        return rootNetwork;
    }

    public void execute(Context context, double time) {
        rootNetwork.execute(context, time);
    }
}
