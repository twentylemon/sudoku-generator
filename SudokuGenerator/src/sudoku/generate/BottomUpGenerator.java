package sudoku.generate;

import java.util.Random;
import sudoku.CandidateSet;
import sudoku.SolverService;
import sudoku.SudokuBoard;
import sudoku.SudokuSolver;
import sudoku.util.ArrayUtil;

/**
 * Generates sudoku problems from the bottom up. At each iteration, a cell
 * with more than one candidate is selected, and assigned a random value. If
 * the puzzle has no solutions, clear the cell and continue. If there are
 * multiple solutions, continue. If there is only one solution, break.
 * After, go through each cell and try clearing the value there. If there is
 * no longer one solution, replace the value.
 *
 * @author Taras Mychaskiw
 */
public class BottomUpGenerator implements SudokuGenerator {

    private final SudokuBoard brd;
    private final int[] cells;
    private final Random rand = new Random();

    /**
     * Constructs a sudoku board generator able to generate boards with the
     * given dimensions.
     *
     * @param p the small box width
     * @param q the small box height
     */
    public BottomUpGenerator(int p, int q){
        this.brd = new SudokuBoard(p, q);
        cells = ArrayUtil.range(0, brd.getNumCells());
    }

    /**
     * Constructs a sudoku board generator able to generate boards with the
     * same dimensions as the given board.
     *
     * @param board a sudoku board
     */
    public BottomUpGenerator(SudokuBoard board){
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
        final SudokuBoard board = new SudokuBoard(brd);
        ArrayUtil.shuffle(cells);
        int pos = 0;

        while (true){
            int cell;
            CandidateSet options = null;
            do {
                cell = cells[pos];
                pos = (pos + 1) % cells.length;
                if (!board.isSet(cell)){
                    options = board.getOptions(cell);
                }
            } while (board.isSet(cell) || options.cardinality() <= 1);

            int value = options.getRandomValue();
            board.setCell(cell, value);

            int form = SolverService.getFormity(board);
            if (form == SudokuSolver.NO_SOLUTIONS){
                board.clearCell(cell);
            }
            else if (form == SudokuSolver.UNIQUE_SOLUTION){
                break;
            }
        }

        //try removing additional values
        for (int cell : cells){
            if (board.isSet(cell)){
                int value = board.getCell(cell);
                board.clearCell(cell);
                int form = SolverService.getFormity(board);
                if (form != SudokuSolver.UNIQUE_SOLUTION){
                    board.setCell(cell, value);
                }
            }
        }

        return board;
    }
}
