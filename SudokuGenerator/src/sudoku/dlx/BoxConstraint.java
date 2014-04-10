package sudoku.dlx;

/**
 * Constraint for the box. Each number can only appear once in a box.
 *
 * @author Taras Mychaskiw
 */
class BoxConstraint implements Constraint {

    final int x, y, value;

    BoxConstraint(int x, int y, int value) {
        this.x = x;
        this.y = y;
        this.value = value;
    }
}
