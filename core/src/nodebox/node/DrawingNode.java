package nodebox.node;

public abstract class DrawingNode extends Node {

    public final BooleanPort pEnabled = new BooleanPort(this, "enabled", Port.Direction.INPUT, true);

    @Override
    public boolean canDraw() {
        return true;
    }
}
