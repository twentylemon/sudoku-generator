package sudoku.clp;

import sudoku.CandidateSet;
import java.util.ArrayList;
import java.util.List;
import sudoku.SudokuBoard;

/**
 * Class to generate random solved sudoku board. The other solving strategies
 * could also be used to serve this purpose, but this was easiest to implement.
 * And CLP is fast enough, so no need to get crazy will speeding this up.
 *
 * @author Taras Mychaskiw
 */
public class CLPBoardGenerator extends CLPBoard {

    private CLPBoardGenerator(SudokuBoard board){
        super(board);
    }

    CLPBoardGenerator(CLPBoard other){
        super(other);
    }

    /**
     * Returns the cell which is most constrained, or -1 if the board is solved.
     * The most constrained cell is the one with the fewest number of possible
     * values that can be set in it. This version returns a random most
     * constrained cell, instead of the first one found.
     *
     * @return constrained cell, or -1 if solved
     */
    @Override
    int getConstrainedCell(){
        final List<Integer> cells = new ArrayList<>(getBoard().getNumCells());
        int min = Integer.MAX_VALUE;
        for (int i = 0; i < getBoard().getNumCells(); i++){
            int size = getCandidateSet(i).cardinality();
            if (size > 1 && size < min){
                min = size;
                cells.clear();
                cells.add(i);
            }
            else if (size == min){
                cells.add(i);
            }
        }
        if (cells.isEmpty()){
            return -1;
        }
        return cells.get((int)(Math.random() * cells.size()));
    }

    /**
     * Returns a random fully solved sudoku board.
     *
     * @param p the small box width
     * @param q the small box height
     * @return a solved sudoku
     */
    public static SudokuBoard getRandomBoard(int p, int q){
        return getRandomBoard(new SudokuBoard(p, q));
    }


    /**
     * Returns a new random fully solved sudoku board using the given
     * board as a starting point. All cells in the original board are kept.
     *
     * @param board the board to solve
     * @return a solved version of the board, or null if unsolvable
     */
    public static SudokuBoard getRandomBoard(SudokuBoard board){
        CLPBoard conBoard = new CLPBoardGenerator(board);
        conBoard = solve(conBoard);
        if (conBoard != null){
            return conBoard.getSolvedBoard();
        }
        return null;
    }


    /**
     * Performs the actual backtrack search.
     *
     * @param sudoku the board to search on
     * @return the update values if successful, null otherwise
     * @see ConstraintSolver.solve(CLPBoard)
     */
    private static CLPBoard solve(CLPBoard sudoku){
        int cell = sudoku.getConstrainedCell();
        if (cell < 0){
            return sudoku;   //board is solved
        }

        final CandidateSet set = sudoku.getCandidateSet(cell);
        for (Integer value : set){
            final CLPBoard copy = new CLPBoardGenerator(sudoku);
            if (copy.assign(cell, value)){
                final CLPBoard board = solve(copy);
                if (board != null){
                    return board;
                }
            }
        }
        return null;
    }
}
