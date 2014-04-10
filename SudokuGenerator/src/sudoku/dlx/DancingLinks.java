package sudoku.dlx;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import sudoku.SudokuBoard;

/**
 * A quadruply linked list. Contains a sparse matrix of constraints to solve
 * a sudoku problem. Treats sudoku as an exact cover problem, and uses the
 * sparse matrix of constraints (this list) to solve the sudoku. The basis of
 * it's speed lies in the O(1) operation to link or unlink entire columns.
 * 
 * @author Taras Mychaskiw
 */
class DancingLinks {

    private final ColumnHeader head;            //entry point to the list
    private final List<ColumnHeader> columns;   //maintain list of headers
    private final SudokuBoard board;            //board being solved

    DancingLinks(SudokuBoard board){
        head = new ColumnHeader();  //points to itself in all directions
        columns = new ArrayList<>(board.getNumCells() * board.getSize());
        this.board = board;
    }

    boolean isSolved(){ return head.right == head; }
    SudokuBoard getBoard(){ return board; }

    /**
     * Adds a column to the table.
     */
    void addColumn(Constraint constraint){
        ColumnHeader top = new ColumnHeader(constraint);    //points to self
        Node tail = head.left;
        tail.setRight(top);
        top.setLeft(tail).setRight(head);
        head.setLeft(top);
        columns.add(top);
    }

    /**
     * Adds a row to the table.
     *
     * @param values the columns that are satisfied by this row
     */
    void addRow(List<Integer> values){
        Node prev = null;
        for (int i : values){
            ColumnHeader top = columns.get(i);
            top.size++;
            Node bottom = top.up;
            Node node = new Node(null, null, bottom, top, top);
            bottom.setDown(node);
            top.setUp(node);
            if (prev != null){
                Node front = prev.right;
                node.setLeft(prev).setRight(front);
                prev.setRight(node);
                front.setLeft(node);
            }
            else {
                node.setLeft(node).setRight(node);
            }
            prev = node;
        }
    }


    /**
     * Finds the column with the fewest number of choices.
     *
     * @return the column header
     */
    ColumnHeader findBestColumn(){
        int lowSize = Integer.MAX_VALUE;
        ColumnHeader result = null;
        ColumnHeader current = (ColumnHeader) head.right;
        while (current != head){
            if (current.size < lowSize){
                lowSize = current.size;
                result = current;
            }
            current = (ColumnHeader) current.right;
        }
        return result;
    }


    /**
     * Hides a column from the table.
     *
     * @param col the column to hide
     */
    void cover(ColumnHeader col){
        col.coverLeft();
        Node row = col.down;
        while (row != col){
            Node node = row.right;
            while (node != row){
                node.coverUp();
                node.head.size--;
                node = node.right;
            }
            row = row.down;
        }
    }

    /**
     * Uncover a column that was hidden by DancingLinks.cover(ColumnHeader).
     *
     * @param col the column to un-hide
     */
    void uncover(ColumnHeader col){
        Node row = col.up;
        while (row != col){
            Node node = row.left;
            while (node != row){
                node.head.size++;
                node.uncoverUp();
                node = node.left;
            }
            row = row.up;
        }
        col.uncoverLeft();
    }


    /**
     * Get the name of a row by getting the list of constraints it satisfies.
     *
     * @param row the row to make a name for
     * @return the list of column constraints
     */
    List<Constraint> getRowName(Node row){
        List<Constraint> result = new LinkedList<>();
        result.add(row.head.constraint);
        Node node = row.right;
        while (node != row){
            result.add(node.head.constraint);
            node = node.right;
        }
        return result;
    }
}
