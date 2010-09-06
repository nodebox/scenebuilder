package nodebox.toxiclibscore;

import nodebox.node.*;
import toxi.geom.Polygon2D;

/**
 * The voronoi variables can be used in a VoronoiLooper network to get information on the current iteration.
 * <p/>
 * For each iteration, the looper variables provide the index, the total amount of executions,
 * the position (between 0.0-1.0) and the current polygon.
 */
@Description("Variables that contain information for every voronoi region.")
public class VoronoiVariables extends Node {

    public final IntPort pAmount = new IntPort(this, "amount", Port.Direction.OUTPUT);
    public final IntPort pIndex = new IntPort(this, "index", Port.Direction.OUTPUT, 0);
    public final FloatPort pPosition = new FloatPort(this, "position", Port.Direction.OUTPUT, 0f);
    public final Polygon2DPort pPolygon = new Polygon2DPort(this, "polygon", Port.Direction.OUTPUT);

    @Override
    public void execute(Context context, double time) {
        Network network = getNetwork();
        if (network == null) return;
        if (!(network instanceof VoronoiLooper)) return;
        pAmount.set((Integer) context.getValueForNodeKey(network, VoronoiLooper.KEY_AMOUNT));
        pIndex.set((Integer) context.getValueForNodeKey(network, VoronoiLooper.KEY_INDEX));
        pPosition.set((Float) context.getValueForNodeKey(network, VoronoiLooper.KEY_POSITION));
        pPolygon.set((Polygon2D) context.getValueForNodeKey(network, VoronoiLooper.KEY_POLYGON));
    }
}
