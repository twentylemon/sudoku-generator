package sudoku.dlx;

/**
 * Constraint for a column. Each number can only appear once in a column.
 * 
 * @author Taras Mychaskiw
 */
class ColumnConstraint implements Constraint {

    final int column, value;

    ColumnConstraint(int column, int value) {
        this.column = column;
        this.value = value;
    }
}
