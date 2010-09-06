package nodebox.toxiclibscore;

import nodebox.node.*;
import toxi.geom.Polygon2D;
import toxi.geom.Vec2D;
import toxi.geom.mesh2d.Voronoi;

import java.util.List;
import java.util.Random;

@Description("Run the contents of this network for each region of the voronoi.")
public class VoronoiLooper extends Network {

    public final FloatPort pX = new FloatPort(this, "x", Port.Direction.INPUT, 0f);
    public final FloatPort pY = new FloatPort(this, "y", Port.Direction.INPUT, 0f);
    public final FloatPort pSize = new FloatPort(this, "size", Port.Direction.INPUT, 500f);
    public final IntPort pAmount = new IntPort(this, "amount", Port.Direction.INPUT, 100);
    public final IntPort pSeed = new IntPort(this, "seed", Port.Direction.INPUT, 100);
    public static final String KEY_AMOUNT = "amount";
    public static final String KEY_INDEX = "index";
    public static final String KEY_POSITION = "position";
    public static final String KEY_POLYGON = "polygon";

    @Override
    public void execute(Context context, double time) {
        //if (!pEnabled.get()) return true;
        // Construct Voronoi
        float voronoiSize = pSize.get();
        Voronoi voronoi = new Voronoi();
        Random r = new Random(pSeed.get());
        int amount = pAmount.get();
        float startX = pX.get();
        float startY = pY.get();
        for (int i = 0; i < amount; i++) {
            float x = startX + r.nextFloat() * voronoiSize;
            float y = startY + r.nextFloat() * voronoiSize;
            voronoi.addPoint(new Vec2D(x, y));
        }
        // Loop through all regions and execute the children.
        List<Polygon2D> regions = voronoi.getRegions();
        int regionCount = regions.size();
        int index = 0;
        float position;
        for (Polygon2D polygon : regions) {
            position = index / (float) regionCount;
            Context childContext = new Context(context);
            childContext.setValueForNodeKey(this, KEY_AMOUNT, amount);
            childContext.setValueForNodeKey(this, KEY_INDEX, index);
            childContext.setValueForNodeKey(this, KEY_POSITION, position);
            childContext.setValueForNodeKey(this, KEY_POLYGON, polygon);
            super.execute(childContext, time);
            index++;
        }
    }

}
