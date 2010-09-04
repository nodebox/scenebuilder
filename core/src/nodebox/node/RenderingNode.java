package nodebox.node;

public abstract class RenderingNode extends Node {

    public final BooleanPort pEnabled = new BooleanPort(this, "enabled", Port.Direction.INPUT, true);

    @Override
    public boolean isRendering() {
        return true;
    }
}
