package nodebox.builtins.time;

import nodebox.node.*;

@Description("Provide access to the current time, as the seconds from the start of the run.")
@Category("Time")
@ExternalInput
public class CurrentTime extends Node {

    public FloatPort pTime = new FloatPort(this, "time", Port.Direction.OUTPUT);

    @Override
    public void execute(Context context, float time) {
        pTime.set(time);
    }
}
