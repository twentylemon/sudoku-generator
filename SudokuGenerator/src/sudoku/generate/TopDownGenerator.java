package sudoku.generate;

import sudoku.SolverService;
import sudoku.SudokuBoard;
import sudoku.SudokuSolver;
import sudoku.clp.CLPBoardGenerator;
import sudoku.util.ArrayUtil;

/**
 * Approaches generation from the top down. Start with a fully solved board,
 * and remove cells randomly. At each removal, check if the board is still
 * well formed. If it's not, replace the removed cell. Continue until all
 * cells have been tested.
 *
 * @author Taras Mychaskiw
 */
public class TopDownGenerator implements SudokuGenerator {

    private final int[] cells;
    private final SudokuBoard brd;

    /**
     * Constructs a sudoku board generator able to generate boards with the
     * given dimensions.
     *
     * @param p the small box width
     * @param q the small box height
     */
    public TopDownGenerator(int p, int q){
        this.brd = new SudokuBoard(p, q);
        cells = ArrayUtil.range(0, brd.getNumCells());
    }

    /**
     * Constructs a sudoku board generator able to generate boards with the
     * same dimensions as the given board.
     *
     * @param board a sudoku board
     */
    public TopDownGenerator(SudokuBoard board){
        this(board.getSmallWidth(), board.getSmallHeight());
    }

    /**
     * Returns a random minimal well formed sudoku with the same dimensions
     * as specified in creation of this generator.
     *
     * @return a random minimal well formed sudoku problem
     */
    @Override
    public SudokuBoard getProblem(){
        final SudokuBoard solved = CLPBoardGenerator.getRandomBoard(brd);
        SudokuBoard board = new SudokuBoard(solved);
        ArrayUtil.shuffle(cells);

        for (int cell : cells){
            //remove the cell from the sudoku problem
            board.clearCell(cell);

            //is it still well fomred?
            int form = SolverService.getFormity(board);
            if (form != SudokuSolver.UNIQUE_SOLUTION){
                board.setCell(cell, solved.getCell(cell));
            }
        }
        return board;
    }
}
