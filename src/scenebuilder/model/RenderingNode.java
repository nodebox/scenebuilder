package scenebuilder.model;

public abstract class RenderingNode extends Node {

    @Override
    public boolean isRendering() {
        return true;
    }
}
