package nodebox.builtins.utility;

import nodebox.node.*;

/**
 * The grid variables can be used in a grid network to get information on the current point.
 * <p/>
 * For each iteration, the grid variables provide the row and column index and the total amount of rows and columns.
 */
@Description("Variables that contain information for every point in the grid.")
public class GridVariables extends Node {

    public final IntPort pRows = new IntPort(this, "rows", Port.Direction.OUTPUT);
    public final IntPort pColumns = new IntPort(this, "columns", Port.Direction.OUTPUT);
    public final IntPort pRowIndex = new IntPort(this, "rowIndex", Port.Direction.OUTPUT);
    public final IntPort pColumnIndex = new IntPort(this, "columnIndex", Port.Direction.OUTPUT);

    @Override
    public void execute(Context context, float time) {
        Network network = getNetwork();
        if (network == null) return;
        if (!(network instanceof Grid)) return;
        pRows.set((Integer) context.getValueForNodeKey(network, Grid.KEY_ROWS));
        pColumns.set((Integer) context.getValueForNodeKey(network, Grid.KEY_COLUMNS));
        pRowIndex.set((Integer) context.getValueForNodeKey(network, Grid.KEY_ROW_INDEX));
        pColumnIndex.set((Integer) context.getValueForNodeKey(network, Grid.KEY_COLUMN_INDEX));
    }

}
