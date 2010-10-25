package nodebox.node;

import processing.core.PApplet;

public class MockContext extends Context {
    public MockContext() {
        super((PApplet)null);
    }

    public MockContext(Context parentContext) {
        super(parentContext);
    }
}
