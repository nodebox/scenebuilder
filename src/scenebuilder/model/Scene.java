package scenebuilder.model;

public class Scene {

    private Macro rootMacro;

    public Scene() {
        rootMacro = new Macro();
        rootMacro.setName("root");
    }

    public Macro getRootMacro() {
        return rootMacro;
    }

    public void execute(Context context, double time) {
        rootMacro.execute(context, time);
    }
}
