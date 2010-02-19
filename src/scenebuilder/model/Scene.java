package scenebuilder.model;

public class Scene {

    private Macro rootMacro;

    public Scene() {
        rootMacro = new Macro();
        rootMacro.setName("root");
        rootMacro.setAttribute(Node.DISPLAY_NAME_ATTRIBUTE, "Root");
    }

    public Macro getRootMacro() {
        return rootMacro;
    }

    public void execute(Context context, double time) {
        rootMacro.execute(context, time);
    }
}
