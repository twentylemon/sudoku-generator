package sudoku.dlx;

/**
 * A node at the header of each column in the matrix. Basically just a node
 * except it also stored which constraint the column satisfies and the number
 * of nodes that are in the column.
 *
 * @author Taras Mychaskiw
 */
class ColumnHeader extends Node {

    Constraint constraint;
    int size;

    /**
     * Makes a new ColumnNode which points to itself in all directions.
     *
     * @param size the number of elements in this column
     */
    ColumnHeader(Constraint constraint, int size){
        this.constraint = constraint;
        this.size = size;
        init();
    }

    ColumnHeader(){
        this(null, 0);
    }
    ColumnHeader(Constraint constraint){
        this(constraint, 0);
    }

    /**
     * Sets all links pointing back to itself.
     */
    private void init(){
        setLeft(this).setRight(this).setUp(this).setDown(this).setHead(this);
    }
}
