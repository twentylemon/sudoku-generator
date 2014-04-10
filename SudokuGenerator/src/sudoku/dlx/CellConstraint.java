package sudoku.dlx;

/**
 * Constraint for each cell. Only one number can reside in any cell.
 * 
 * @author Taras Mychaskiw
 */
class CellConstraint implements Constraint {

    final int x, y;

    CellConstraint(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
