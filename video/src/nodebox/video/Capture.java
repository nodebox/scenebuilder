package nodebox.video;

import nodebox.node.*;
import processing.core.PGraphics;

@Description("Captures video images.")
@Drawable
@Category("Video")
@ExternalInput
public class Capture extends Node {

    private static final int CAPTURE_WIDTH = 320;
    private static final int CAPTURE_HEIGHT = 240;
    private static final int CAPTURE_FRAME_RATE = 30;

    public final ImagePort pImage = new ImagePort(this, "image", Port.Direction.OUTPUT);
    private processing.video.Capture capture;

    @Override
    public void activate() {
        // TODO: Capture should be initialized here, but activate/deactivate are run in a different thread,
        // causing a race condition.
    }

    @Override
    public void execute(Context context, float time) {
        if (capture == null) {
            capture = new processing.video.Capture(getScene().getApplet(), CAPTURE_WIDTH, CAPTURE_HEIGHT, CAPTURE_FRAME_RATE);
        }
        if (capture.available()) {
            capture.read();
        }
        pImage.set(capture.get());
    }

    @Override
    public void draw(PGraphics g, Context context, float time) {
        g.image(pImage.get(), 0, 0);
    }

    @Override
    public void deactivate() {
        capture.stop();
        capture.dispose();
        capture = null;
    }

}
