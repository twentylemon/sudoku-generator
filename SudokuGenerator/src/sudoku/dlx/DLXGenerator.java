package sudoku.dlx;

import java.awt.Point;
import java.util.LinkedList;
import java.util.List;
import sudoku.SudokuBoard;

/**
 * Utility class to create a new DancingLinks instance.
 *
 * @author Taras Mychaskiw
 */
class DLXGenerator {

    /**
     * Generates a DancingLinks instance for the board specified.
     *
     * @param board the board to create a DLX for
     * @return the DancingLinks instance
     */
    static DancingLinks generate(SudokuBoard board){
        DancingLinks links = new DancingLinks(board);
        //create the column constraints
        for (int col = 0; col < board.getSize(); col++){
            for (int value = 1; value <= board.getSize(); value++){
                links.addColumn(new ColumnConstraint(col, value));
            }
        }
        //create the row constraints
        for (int row = 0; row < board.getSize(); row++){
            for (int value = 1; value <= board.getSize(); value++){
                links.addColumn(new RowConstraint(row, value));
            }
        }
        //create the box constraints
        for (int x = 0; x < board.getSmallWidth(); x++){
            for (int y = 0; y < board.getSmallHeight(); y++){
                for (int value = 1; value <= board.getSize(); value++){
                    links.addColumn(new BoxConstraint(x, y, value));
                }
            }
        }
        //create the cell constraints
        for (int x = 0; x < board.getSize(); x++){
            for (int y = 0; y < board.getSize(); y++){
                links.addColumn(new CellConstraint(x, y));
            }
        }

        List<Integer> list = new LinkedList<>();
        for (int y = 0; y < board.getSize(); y++){
            for (int x = 0; x < board.getSize(); x++){
                if (board.isSet(x, y)){
                    fillValues(list, x, y, board.getCell(x, y), board);
                    links.addRow(list);
                }
                else {
                    for (int value = 1; value <= board.getSize(); value++){
                        fillValues(list, x, y, value, board);
                        links.addRow(list);
                    }
                }
            }
        }
        return links;
    }


    /**
     * Fills the list with the index values of the turned on constraints.
     *
     * @param list the list to fill
     * @param x the x coord
     * @param y the y coord
     * @param value the value in (x,y)
     * @param board the sudoku board
     */
    private static void fillValues(List<Integer> list, int x, int y, int value, SudokuBoard board){
        list.clear();
        Point box = new Point(x / board.getSmallHeight(), y / board.getSmallWidth());
        list.add(x*board.getSize() + value - 1);    //column
        list.add(board.getNumCells() + y*board.getSize() + value - 1);  //row
        list.add(2*board.getNumCells() + (box.x*board.getSmallHeight() + box.y)*board.getSize() + value - 1);   //box
        list.add(3*board.getNumCells() + x*board.getSize() + y);    //cell
    }
}
