package sudoku.dlx;

/**
 * Constraint for a row. Each number can only appear once in a row.
 *
 * @author Taras Mychaskiw
 */
class RowConstraint implements Constraint {

    final int row, value;

    RowConstraint(int row, int value) {
        this.row = row;
        this.value = value;
    }
}
