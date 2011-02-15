package nodebox.video;

import nodebox.node.*;
import processing.core.PGraphics;

@Description("Plays a video.")
@Category("Video")
public class Video extends Node {

    private static final int VIDEO_WIDTH = 320;
    private static final int VIDEO_HEIGHT = 240;
    //private static final int VIDEO_FRAME_RATE = 30;

    public final StringPort pFileName = new StringPort(this, "fileName", Port.Direction.INPUT);
    public final ImagePort pOutput = new ImagePort(this, "output", Port.Direction.OUTPUT);
    private processing.video.Movie video;

    @Override
    public void execute(Context context, float time) {
        if (video == null) {
            video = new processing.video.Movie(getScene().getApplet(), pFileName.get());
        }
            video.read();
        pOutput.set(video.get());
    }

    @Override
    public void draw(PGraphics g, Context context, float time) {
        g.image(pOutput.get(), VIDEO_WIDTH, VIDEO_HEIGHT);
    }

    @Override
    public void deactivate() {
        video.stop();
        video.dispose();
        video = null;
    }

}
