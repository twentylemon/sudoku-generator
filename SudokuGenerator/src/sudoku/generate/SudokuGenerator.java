package sudoku.generate;

import sudoku.SudokuBoard;

/**
 * Class that will generate a minimal well formed sudoku.
 *
 * @author Taras Mychaskiw
 */
public interface SudokuGenerator {

    /**
     * Returns a random minimal well formed sudoku with the same dimensions
     * as specified in creation of this generator.
     *
     * @return a random minimal well formed sudoku problem
     */
    public SudokuBoard getProblem();

}
