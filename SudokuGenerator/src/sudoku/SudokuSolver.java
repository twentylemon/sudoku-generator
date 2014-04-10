package sudoku;

import java.util.List;


/**
 * Base class for different sudoku solving strategies.
 *
 * @author Taras Mychaskiw
 */
public interface SudokuSolver {

    /**
     * Tries to solve the sudoku board.
     *
     * @param board the board to solve
     * @return a solved version of the board, or null if unsolvable
     */
    public SudokuBoard solve(SudokuBoard board);

    /**
     * Returns a list of all solutions to the board given.
     *
     * @param board the board to solve
     * @return the list of all solutions to the board
     */
    public List<SudokuBoard> enumerate(SudokuBoard board);

    /**
     * Returns true if the board is well formed, that is if the
     * board only has one solution.
     *
     * @param board the board to test
     * @return true if it only has one possible solution
     */
    public boolean isWellFormed(SudokuBoard board);

    /**
     * Returns the formity of the board sent.
     *
     * @param board the board to test
     * @return 0 if no solutions, 1 if unique solution, -1 if multiple solutions
     */
    public int getFormity(SudokuBoard board);
    public static final int NO_SOLUTIONS = 0;
    public static final int MULTIPLE_SOLUTIONS = -1;
    public static final int UNIQUE_SOLUTION = 1;
}
