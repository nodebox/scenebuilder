package nodebox.toxiclibscore;

import nodebox.node.*;
import processing.core.PConstants;
import processing.core.PGraphics;
import toxi.geom.Polygon2D;
import toxi.geom.Vec2D;
import toxi.geom.mesh2d.Voronoi;

import java.util.Random;

@Description("Draw a voronoi shape at the given position.")
@Category("Toxiclibs 2D")
public class VoronoiDrawer extends Node {

    public final FloatPort pX = new FloatPort(this, "x", Port.Direction.INPUT, 0f);
    public final FloatPort pY = new FloatPort(this, "y", Port.Direction.INPUT, 0f);
    public final FloatPort pSize = new FloatPort(this, "size", Port.Direction.INPUT, 500f);
    public final IntPort pAmount = new IntPort(this, "amount", Port.Direction.INPUT, 100);
    public final IntPort pSeed = new IntPort(this, "seed", Port.Direction.INPUT, 100);

    @Override
    public void draw(PGraphics g, Context context, float time) {
        // Construct Voronoi
        float voronoiSize = pSize.get();
        Voronoi voronoi = new Voronoi();
        Random r = new Random(pSeed.get());
        int amount = pAmount.get();
        for (int i = 0; i < amount; i++) {
            float x = r.nextFloat() * voronoiSize;
            float y = r.nextFloat() * voronoiSize;
            voronoi.addPoint(new Vec2D(x, y));
        }
        // Draw
        g.pushStyle();
        g.pushMatrix();
        g.translate(pX.get(), pY.get());
        g.noStroke();
        for (Polygon2D poly : voronoi.getRegions()) {
            g.fill(r.nextFloat() * 255, r.nextFloat() * 255, r.nextFloat() * 255);
            g.beginShape(PConstants.POLYGON);
            for (Vec2D v : poly.vertices) {
                g.vertex(v.x, v.y);
            }
            g.endShape(PConstants.CLOSE);
        }
        g.popMatrix();
        g.popStyle();
    }
}
