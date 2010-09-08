/*
 * This file is part of NodeBox.
 *
 * Copyright (C) 2008 Frederik De Bleser (frederik@pandora.be)
 *
 * NodeBox is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * NodeBox is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with NodeBox. If not, see <http://www.gnu.org/licenses/>.
 */

package nodebox.graphics;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Color implements Cloneable {

    private static final Pattern HEX_STRING_PATTERN = Pattern.compile("^#([0-9a-f]{2})([0-9a-f]{2})([0-9a-f]{2})([0-9a-f]{2})$");

    public enum Mode {
        RGB, HSB, CMYK
    }

    private float r, g, b, a;
    private float h, s, v;

    public static float clamp(float v) {
        return Math.max(0f, Math.min(1f, v));
    }

    /**
     * Create an empty (black) color object.
     */
    public Color() {
        this(0, 0, 0, 1, Mode.RGB);
    }

    /**
     * Create a new color with the given grayscale value.
     *
     * @param v the gray component.
     */
    public Color(float v) {
        this(v, v, v, 1, Mode.RGB);
    }

    /**
     * Create a new color with the given grayscale and alpha value.
     *
     * @param v the grayscale value.
     * @param a the alpha value.
     */

    public Color(float v, float a) {
        this(v, v, v, a, Mode.RGB);
    }

    /**
     * Create a new color with the the given R/G/B value.
     *
     * @param x the red or hue component.
     * @param y the green or saturation component.
     * @param z the blue or brightness component.
     * @param m the specified color mode.
     */
    public Color(float x, float y, float z, Mode m) {
        this(x, y, z, 1f, m);
    }

    /**
     * Create a new color with the the given R/G/B value.
     *
     * @param r the red component.
     * @param g the green component.
     * @param b the blue component.
     */
    public Color(float r, float g, float b) {
        this(r, g, b, 1f, Mode.RGB);
    }

    /**
     * Create a new color with the the given R/G/B/A or H/S/B/A value.
     *
     * @param r the red component.
     * @param g the green component.
     * @param b the blue component.
     * @param a the alpha component.
     */
    public Color(float r, float g, float b, float a) {
        this(r, g, b, a, Mode.RGB);
    }

    /**
     * Create a new color with the the given R/G/B/A or H/S/B/A value.
     *
     * @param x the red or hue component.
     * @param y the green or saturation component.
     * @param z the blue or brightness component.
     * @param a the alpha component.
     * @param m the specified color mode.
     */
    public Color(float x, float y, float z, float a, Mode m) {
        switch (m) {
            case RGB:
                this.r = clamp(x);
                this.g = clamp(y);
                this.b = clamp(z);
                this.a = clamp(a);
                updateHSB();
                updateCMYK();
                break;
            case HSB:
                this.h = clamp(x);
                this.s = clamp(y);
                this.v = clamp(z);
                this.a = clamp(a);
                updateRGB();
                updateCMYK();
                break;
            case CMYK:
                throw new RuntimeException("CMYK color mode is not implemented yet.");
        }
    }

    public Color(String colorName) {
        // We can only parse RGBA hex color values.
        Matcher m = HEX_STRING_PATTERN.matcher(colorName);
        if (!m.matches())
            throw new IllegalArgumentException("The given value '" + colorName + "' is not of the format #112233ff.");
        int r255 = Integer.parseInt(m.group(1), 16);
        int g255 = Integer.parseInt(m.group(2), 16);
        int b255 = Integer.parseInt(m.group(3), 16);
        int a255 = Integer.parseInt(m.group(4), 16);
        this.r = r255 / 255f;
        this.g = g255 / 255f;
        this.b = b255 / 255f;
        this.a = a255 / 255f;
        updateHSB();
        updateCMYK();
    }

    /**
     * Create a new color with the the given color.
     * <p/>
     * The color object is cloned; you can change the original afterwards.
     * If the color object is null, the new color is turned off (same as nocolor).
     *
     * @param color the color object.
     */
    public Color(java.awt.Color color) {
        this.r = color.getRed() / 255f;
        this.g = color.getGreen() / 255f;
        this.b = color.getBlue() / 255f;
        this.a = color.getAlpha() / 255f;
        updateHSB();
        updateCMYK();
    }

    /**
     * Create a new color with the the given color.
     * <p/>
     * The color object is cloned; you can change the original afterwards.
     * If the color object is null, the new color is turned off (same as nocolor).
     *
     * @param other the color object.
     */
    public Color(Color other) {
        this.r = other.r;
        this.g = other.g;
        this.b = other.b;
        this.a = other.a;
        updateHSB();
        updateCMYK();
    }

    public float getRed() {
        return r;
    }

    public float getR() {
        return r;
    }

    public void setRed(float r) {
        this.r = clamp(r);
        updateHSB();
        updateCMYK();
    }

    public void setR(float r) {
        setRed(r);
    }

    public float getGreen() {
        return g;
    }

    public float getG() {
        return g;
    }

    public void setGreen(float g) {
        this.g = clamp(g);
        updateHSB();
        updateCMYK();
    }

    public void setG(float g) {
        setGreen(g);
    }

    public float getBlue() {
        return b;
    }

    public float getB() {
        return b;
    }

    public void setBlue(float b) {
        this.b = clamp(b);
        updateHSB();
        updateCMYK();
    }

    public void setB(float b) {
        setBlue(b);
    }

    public float getAlpha() {
        return a;
    }

    public float getA() {
        return a;
    }

    public void setAlpha(float a) {
        this.a = clamp(a);
        updateHSB();
        updateCMYK();
    }

    public void setA(float a) {
        setAlpha(a);
    }

    public boolean isVisible() {
        return a > 0f;
    }

    public float getHue() {
        return h;
    }

    public float getH() {
        return h;
    }

    public void setHue(float h) {
        this.h = clamp(h);
        updateRGB();
        updateCMYK();
    }

    public void setH(float h) {
        setHue(h);
    }

    public float getSaturation() {
        return s;
    }

    public float getS() {
        return s;
    }

    public void setSaturation(float s) {
        this.s = clamp(s);
        updateRGB();
        updateCMYK();
    }

    public void setS(float s) {
        setSaturation(s);
    }

    public float getBrightness() {
        return v;
    }

    public float getV() {
        return v;
    }

    public void setBrightness(float v) {
        this.v = clamp(v);
        updateRGB();
        updateCMYK();
    }

    public void setV(float v) {
        setBrightness(v);
    }

    private void updateRGB() {
        if (s == 0)
            this.r = this.g = this.b = this.v;
        else {
            float h = this.h;
            float s = this.s;
            float v = this.v;
            float f, p, q, t;
            h = h / (60f / 360);
            int i = (int) Math.floor(h);
            f = h - i;
            p = v * (1 - s);
            q = v * (1 - s * f);
            t = v * (1 - s * (1 - f));

            float[] rgb;
            if (i == 0)
                rgb = new float[]{v, t, p};
            else if (i == 1)
                rgb = new float[]{q, v, p};
            else if (i == 2)
                rgb = new float[]{p, v, t};
            else if (i == 3)
                rgb = new float[]{p, q, v};
            else if (i == 4)
                rgb = new float[]{t, p, v};
            else
                rgb = new float[]{v, p, q};

            this.r = rgb[0];
            this.g = rgb[1];
            this.b = rgb[2];
        }
    }

    private void updateHSB() {
        float h = 0;
        float s = 0;
        float v = Math.max(Math.max(r, g), b);
        float d = v - Math.min(Math.min(r, g), b);

        if (v != 0)
            s = d / v;

        if (s != 0) {
            if (r == v)
                h = 0 + (g - b) / d;
            else if (g == v)
                h = 2 + (b - r) / d;
            else
                h = 4 + (r - g) / d;
        }

        h = h * (60f / 360);
        if (h < 0)
            h = h + 1;

        this.h = h;
        this.s = s;
        this.v = v;
    }

    private void updateCMYK() {
        // TODO: implement
    }

    public java.awt.Color getAwtColor() {
        return new java.awt.Color(getRed(), getGreen(), getBlue(), getAlpha());
    }

    @Override
    public Color clone() {
        return new Color(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Color)) return false;
        Color other = (Color) obj;
        // Because of the conversion to/from hex, we can have rounding errors.
        // Therefore, we only compare what we can store, i.e. values in the 0-255 range.
        return Math.round(r * 255) == Math.round(other.r * 255)
                && Math.round(g * 255) == Math.round(other.g * 255)
                && Math.round(b * 255) == Math.round(other.b * 255)
                && Math.round(a * 255) == Math.round(other.a * 255);
    }

    /**
     * Parse a hexadecimal value and return a Color object.
     * <p/>
     * The value needs to have four components. (R,G,B,A)
     *
     * @param value the hexadecimal color value, e.g. #995423ff
     * @return a Color object.
     */
    public static Color parseColor(String value) {
        return new Color(value);
    }

    private String paddedHexString(int v) {
        String s = Integer.toHexString(v);
        if (s.length() == 1) {
            return "0" + s;
        } else if (s.length() == 2) {
            return s;
        } else {
            throw new AssertionError("Value too large (must be between 0-255, was " + v + ").");
        }
    }

    /**
     * Returns the color as a 8-bit hexadecimal value, e.g. #ae45cdff
     *
     * @return the color as a 8-bit hexadecimal value
     */
    @Override
    public String toString() {
        int r256 = Math.round(r * 255);
        int g256 = Math.round(g * 255);
        int b256 = Math.round(b * 255);
        int a256 = Math.round(a * 255);
        return "#"
                + paddedHexString(r256)
                + paddedHexString(g256)
                + paddedHexString(b256)
                + paddedHexString(a256);
    }
}
