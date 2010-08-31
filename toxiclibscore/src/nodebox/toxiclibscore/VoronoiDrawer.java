package nodebox.toxiclibscore;

import nodebox.node.*;
import processing.core.PApplet;
import processing.core.PConstants;
import toxi.geom.Polygon2D;
import toxi.geom.Vec2D;
import toxi.geom.mesh2d.Voronoi;

import java.util.Random;

public class VoronoiDrawer extends RenderingNode {

    public final FloatPort pX;
    public final FloatPort pY;
    public final FloatPort pSize;
    public final IntPort pAmount;
    public final IntPort pSeed;

    public VoronoiDrawer() {
        setAttribute(DISPLAY_NAME_ATTRIBUTE, "Voronoi Drawer");
        setAttribute(DESCRIPTION_ATTRIBUTE, "Draws a voronoi shape at the given position.");
        pX = (FloatPort) addPort(new FloatPort(this, "x", Port.Direction.INPUT, 0f));
        pY = (FloatPort) addPort(new FloatPort(this, "y", Port.Direction.INPUT, 0f));
        pSize = (FloatPort) addPort(new FloatPort(this, "size", Port.Direction.INPUT, 500f));
        pAmount = (IntPort) addPort(new IntPort(this, "amount", Port.Direction.INPUT, 100));
        pSeed = (IntPort) addPort(new IntPort(this, "seed", Port.Direction.INPUT, 100));
    }

    @Override
    public boolean execute(Context context, double time) {
        if (!pEnabled.get()) return true;
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
        PApplet g = context.getApplet();
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
        return true;
    }
}
