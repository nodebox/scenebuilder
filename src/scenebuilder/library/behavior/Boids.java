package scenebuilder.library.behavior;

import scenebuilder.model.Context;
import scenebuilder.model.Macro;
import scenebuilder.model.Port;

import java.util.ArrayList;
import java.util.Collections;

public class Boids extends Macro {

    public static final String PORT_X = "x";
    public static final String PORT_Y = "y";
    public static final String PORT_WIDTH = "width";
    public static final String PORT_HEIGHT = "height";
    public static final String PORT_AMOUNT = "amount";
    public static final String PORT_SPEED = "speed";
    public static final String KEY_AMOUNT = "amount";
    public static final String KEY_INDEX = "index";
    public static final String KEY_X = "x";
    public static final String KEY_Y = "y";
    public static final String KEY_Z = "z";
    public static final String KEY_VX = "vx";
    public static final String KEY_VY = "vy";
    public static final String KEY_VZ = "vz";

    private Flock currentFlock;

    public Boids() {
        setDisplayName("Boids");
        setAttribute(DESCRIPTION_ATTRIBUTE, "Simulate coordinated animal motion such as bird flocks and fish schools.");
        addInputPort(Port.Type.NUMBER, PORT_X, 0.0);
        addInputPort(Port.Type.NUMBER, PORT_Y, 0.0);
        addInputPort(Port.Type.NUMBER, PORT_WIDTH, 100.0);
        addInputPort(Port.Type.NUMBER, PORT_HEIGHT, 100.0);
        addInputPort(Port.Type.INTEGER, PORT_AMOUNT, 10);
        addInputPort(Port.Type.NUMBER, PORT_SPEED, 10.0);
    }

    @Override
    public boolean execute(Context context, double time) {
        int amount = asInteger(PORT_AMOUNT);
        double x = asNumber(PORT_X);
        double y = asNumber(PORT_Y);
        double width = asNumber(PORT_WIDTH);
        double height = asNumber(PORT_HEIGHT);
        if (currentFlock == null || currentFlock.getAmount() != amount) {
            currentFlock = new Flock(amount);
        }
        currentFlock.speed = asNumber("speed");
        currentFlock.update();
        for (int i = 0; i < amount; i++) {
            Boid b = currentFlock.boids.get(i);
            Context childContext = new Context(context);
            childContext.setValueForNodeKey(this, KEY_AMOUNT, amount);
            childContext.setValueForNodeKey(this, KEY_INDEX, i);
            childContext.setValueForNodeKey(this, KEY_X, x + b.x * width);
            childContext.setValueForNodeKey(this, KEY_Y, y + b.y * height);
            childContext.setValueForNodeKey(this, KEY_Z, b.z);
            childContext.setValueForNodeKey(this, KEY_VX, b.vx);
            childContext.setValueForNodeKey(this, KEY_VY, b.vy);
            childContext.setValueForNodeKey(this, KEY_VZ, b.vz);
            boolean success = super.execute(childContext, time);
            if (!success) return false;
        }
        return true;
    }

    private static double random(double start, double length) {
        return start + Math.random() * length;
    }

    private class Flock {
        ArrayList<Boid> boids = new ArrayList<Boid>();
        double cx, cy;
        double speed;


        Flock(int amount) {
            this.speed = 10.0;
            for (int i = 0; i < amount; i++) {
                boids.add(new Boid(this, Math.random() - 0.5, Math.random() - 0.5, 0.0));
            }
        }

        void update() {
            // Calculate center
            boolean first = true;
            double tx = 0.0;
            double ty = 0.0;
            for (Boid b : boids) {
                if (first) {
                    tx = b.x;
                    ty = b.y;
                    first = false;
                } else {
                    tx += b.x;
                    ty += b.y;
                }
            }
            cx = tx / boids.size();
            cy = ty / boids.size();


            ArrayList<Boid> shuffledBoids = new ArrayList<Boid>(boids);
            Collections.shuffle(shuffledBoids);
            for (Boid boid : shuffledBoids) {
                boid.update();
            }
        }

        int getAmount() {
            return boids.size();
        }


    }

    private class Vector {
        double x, y, z;

        private Vector(double x, double y, double z) {
            this.x = x;
            this.y = y;
        }
    }

    private class Boid {

        Flock flock;
        double x, y, z;
        double vx, vy, vz;
        double mx, my;

        Boid(Flock flock, double x, double y, double z) {
            this.flock = flock;
            this.x = x;
            this.y = y;
            this.z = z;
            this.vx = 0.0;
            this.vy = 0.0;
            this.vz = 0.0;
            this.mx = Math.random() - 0.5;
            this.my = Math.random() - 0.5;
        }

        void limit() {
            double speed = flock.speed / 1000.0;
            if (Math.abs(vx) > speed)
                vx = vx / Math.abs(vx) * speed;
            if (Math.abs(vy) > speed)
                vy = vy / Math.abs(vy) * speed;
            if (Math.abs(vz) > speed)
                vz = vz / Math.abs(vz) * speed;
        }

        Vector cohesion() {
            double d = 100.0;
            double vx = 0.0;
            double vy = 0.0;
            double vz = 0.0;
//            for (Boid b : flock.boids) {
//                if (b == this) continue;
//                vx += b.x;
//                vy += b.y;
//                vz += b.z;
//            }
//            int n = flock.boids.size() - 1;
//            vx /= n;
//            vy /= n;
//            vz /= n;

            return new Vector((vx - x) / d, (vy - y) / d, (vz - z) / d);
        }

        Vector separation() {
            double sep = 0.1;
            double vx = 0.0;
            double vy = 0.0;
            double vz = 0.0;
            for (Boid b : flock.boids) {
                if (b == this) continue;
                if (Math.abs(x - b.x) < sep) vx += x - b.x;
                if (Math.abs(y - b.y) < sep) vy += y - b.y;
                if (Math.abs(z - b.z) < sep) vz += z - b.z;
            }
            return new Vector(vx, vy, vz);
        }

        Vector alignment() {
            double d = 5.0;
            double vx = 0.0;
            double vy = 0.0;
            double vz = 0.0;
            for (Boid b : flock.boids) {
                if (b == this) continue;
                vx += b.x;
                vy += b.y;
                vz += b.z;
            }
            int n = flock.boids.size() - 1;
            vx /= n;
            vy /= n;
            vz /= n;
            return new Vector((vx - this.vx) / d, (vy - this.vy) / d, (vz - this.vz) / d);
        }

        Vector constrain() {
            double dx = 0.5;
            double dy = 0.5;
            double vx = 0.0;
            double vy = 0.0;
            if (x < -dx) vx += Math.random() * dx;
            if (y < -dy) vy += Math.random() * dy;
            if (x > dx) vx -= Math.random() * dx;
            if (y > dy) vy -= Math.random() * dy;
            return new Vector(vx, vy, 0);
        }

        Vector wander() {
            double vx = Math.sin(Math.sin(System.currentTimeMillis() / 1000.0 / mx));
            double vy = Math.sin(Math.sin(System.currentTimeMillis() / 1000.0 / my));
            return new Vector(vx, vy, 0.0);
        }

        void update() {
            //Vector v1 = cohesion();
            //Vector v2 = separation();
            //Vector v3 = alignment();

            Vector v4 = constrain();
            Vector v5 = wander();
            //vx = v1.x + v2.x + v3.x + v4.x;
            //vy = v1.y + v2.y + v3.x + v4.y;
            //vz = v1.z + v2.z + v3.x + v4.z;
            vx = v4.x + v5.x;
            vy = v4.y + v5.y;
            vz = v4.z + v5.z;
            limit();
            x += vx;
            y += vy;
            z += vz;
        }

    }

}
