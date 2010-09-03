package nodebox.node;

import junit.framework.TestCase;

import java.awt.*;

public class ColorPortTest extends TestCase {

    public void testValueAsString() {
        ColorPort p = new ColorPort(null, "c", Port.Direction.INPUT, Color.RED);
        assertEquals("#ff0000ff", p.getValueAsString());
        p.set(new Color(0x112233));
        assertEquals("#112233ff", p.getValueAsString());
    }

    public void testParseValue() {
        ColorPort p = new ColorPort(null, "c", Port.Direction.INPUT, Color.BLACK);
        assertEquals(Color.RED, p.parseValue("#ff0000ff"));
        assertEquals(new Color(0x112233), p.parseValue("#112233ff"));
    }
}
