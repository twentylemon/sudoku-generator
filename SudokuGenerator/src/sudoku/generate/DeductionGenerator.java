package sudoku.generate;

import sudoku.SolverService;
import sudoku.SudokuBoard;
import sudoku.SudokuSolver;
import sudoku.clp.CLPBoardGenerator;
import sudoku.util.ArrayUtil;

/**
 * Tries to generate hard sudoku problems using clp basically.
 * For each cell from a fully solved board, add that to our return if the value
 * in that cell is not deduced by other clues already given. Then go through
 * each clue and try to remove it from the board.
 *
 * @author Taras Mychaskiw
 */
public class DeductionGenerator implements SudokuGenerator {

    private final int[] cells;
    private final SudokuBoard brd;

    /**
     * Constructs a sudoku board generator able to generate boards with the
     * given dimensions.
     *
     * @param p the small box width
     * @param q the small box height
     */
    public DeductionGenerator(int p, int q){
        this.brd = new SudokuBoard(p, q);
        cells = ArrayUtil.range(0, brd.getNumCells());
    }

    /**
     * Constructs a sudoku board generator able to generate boards with the
     * same dimensions as the given board.
     *
     * @param board a sudoku board
     */
    public DeductionGenerator(SudokuBoard board){
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
        final SudokuBoard board = new SudokuBoard(brd);
        ArrayUtil.shuffle(cells);

        //go through each cell, reveal it only if it is not deduced by others
        for (int cell : cells){
            if (board.getOptions(cell).cardinality() > 1){
                board.setCell(cell, solved.getCell(cell));
            }
        }

        //try removing additional values
        for (int cell : cells){
            if (board.isSet(cell)){
                board.clearCell(cell);
                int form = SolverService.getFormity(board);
                if (form != SudokuSolver.UNIQUE_SOLUTION){
                    board.setCell(cell, solved.getCell(cell));
                }
            }
        }
        return board;
    }
}
