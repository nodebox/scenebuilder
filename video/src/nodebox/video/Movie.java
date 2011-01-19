package nodebox.video;

import nodebox.node.*;
import processing.core.PGraphics;

@Description("Plays a video.")
@Category("Video")
public class Movie extends Node {

    //private static final int VIDEO_WIDTH = 320;
    //private static final int VIDEO_HEIGHT = 240;
    private static final int VIDEO_FRAME_RATE = 30;

    public final StringPort pFileName = new StringPort(this, "fileName", Port.Direction.INPUT);
    public final FloatPort pX = new FloatPort(this, "x", Port.Direction.INPUT, 0f);
    public final FloatPort pY = new FloatPort(this, "y", Port.Direction.INPUT, 0f);
    public final FloatPort pW = new FloatPort(this, "w", Port.Direction.INPUT, 320f);
    public final FloatPort pH = new FloatPort(this, "h", Port.Direction.INPUT, 240f);
    public final ImagePort pOutput = new ImagePort(this, "output", Port.Direction.OUTPUT);
    private processing.video.Movie video;

    @Override
    public void activate(){



    }
    public void execute(Context context, float time) {


                if (video == null) {
                    video = new processing.video.Movie(getScene().getApplet(), pFileName.get());
                    video.frameRate(VIDEO_FRAME_RATE);

                    video.loop();
        }
        if (video.available()) {
            video.read();
        }


            video.read();
        pOutput.set(video.get());
        //pOutput.set((video.get(),pX.get(),pY.get(),pW.get(), pH.get());
    }

    @Override
    public void draw(PGraphics g, Context context, float time) {
        g.image(pOutput.get(),pX.get(),pY.get(),pW.get(), pH.get());
    }

    @Override
    public void deactivate() {
        video.stop();
        video.dispose();
        video = null;
    }

}
