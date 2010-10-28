package nodebox.builtins.utility;

import nodebox.node.*;

@Description("Loop over a grid of points.")
@Category("Utility")
public class Grid extends Network {

    public final IntPort pRows = new IntPort(this, "rows", Port.Direction.INPUT, 10);
    public final IntPort pColumns = new IntPort(this, "columns", Port.Direction.INPUT, 10);
    public final FloatPort pWidth = new FloatPort(this, "width", Port.Direction.INPUT, 500f);
    public final FloatPort pHeight = new FloatPort(this, "height", Port.Direction.INPUT, 500f);
    public final FloatPort pX = new FloatPort(this, "x", Port.Direction.INPUT, 0f);
    public final FloatPort pY = new FloatPort(this, "y", Port.Direction.INPUT, 0f);

    public static final String KEY_ROWS = "rows";
    public static final String KEY_COLUMNS = "columns";
    public static final String KEY_ROW_INDEX = "rowIndex";
    public static final String KEY_COLUMN_INDEX = "columnIndex";

    @Override
    public void execute(Context context, float time) {
        int columns = pColumns.get();
        float colSize = 0f;
        float left = 0f;
        if (columns > 1) {
            colSize = pWidth.get() / (columns - 1f);
            left = pX.get() - pWidth.get() / 2f;
        }
        int rows = pRows.get();
        float rowSize = 0f;
        float top = 0f;
        if (rows > 1) {
            rowSize = pHeight.get() / (rows - 1);
            top = pY.get() - pHeight.get() / 2f;
        }
        for (int rowIndex = 0; rowIndex < rows; rowIndex++) {
            for (int columnIndex = 0; columnIndex < columns; columnIndex++) {
                Context childContext = new Context(context);
                childContext.setValueForNodeKey(this, KEY_ROWS, rows);
                childContext.setValueForNodeKey(this, KEY_COLUMNS, columns);
                childContext.setValueForNodeKey(this, KEY_ROW_INDEX, rowIndex);
                childContext.setValueForNodeKey(this, KEY_COLUMN_INDEX, columnIndex);
                super.execute(childContext, time);
            }
        }


    }
}
