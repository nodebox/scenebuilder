package nodebox.builtins.time;

import nodebox.node.*;

@Description("Provide the current time in different formats")
public class Calendar extends Node {

    public FloatPort pTime = new FloatPort(this, "time", Port.Direction.OUTPUT);
    public IntPort pDay = new IntPort(this, "day", Port.Direction.OUTPUT);
    public IntPort pMonth = new IntPort(this, "month", Port.Direction.OUTPUT);
    public IntPort pYear = new IntPort(this, "year", Port.Direction.OUTPUT);
    public IntPort pHours24 = new IntPort(this, "hours24", Port.Direction.OUTPUT);
    public IntPort pHours12 = new IntPort(this, "hours12", Port.Direction.OUTPUT);
    public IntPort pMinutes = new IntPort(this, "minutes", Port.Direction.OUTPUT);
    public IntPort pSeconds = new IntPort(this, "seconds", Port.Direction.OUTPUT);
    public IntPort pDayOfWeek = new IntPort(this, "dayOfWeek", Port.Direction.OUTPUT);

    @Override
    public void execute(Context context, double time) {
        pTime.set(System.currentTimeMillis() / 1000f);
        java.util.Calendar c = java.util.Calendar.getInstance();
        pDay.set(c.get(java.util.Calendar.DAY_OF_MONTH));
        pMonth.set(c.get(java.util.Calendar.MONTH) + 1);
        pYear.set(c.get(java.util.Calendar.YEAR));
        pHours24.set(c.get(java.util.Calendar.HOUR_OF_DAY) + 1);
        pHours12.set(c.get(java.util.Calendar.HOUR) + 1);
        pMinutes.set(c.get(java.util.Calendar.MINUTE));
        pSeconds.set(c.get(java.util.Calendar.SECOND));
        pDayOfWeek.set(c.get(java.util.Calendar.DAY_OF_WEEK));
    }
}
