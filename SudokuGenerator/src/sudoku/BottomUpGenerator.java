package sudoku;

import java.util.List;
import java.util.Random;

/**
 *
 * @author Taras Mychaskiw
 */
public class BottomUpGenerator implements SudokuGenerator {

    private final SudokuBoard brd;
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
        SudokuBoard board = new SudokuBoard(brd);

        while (true){
            int cell;
            List<Integer> options = null;
            do {
                cell = rand.nextInt(board.getNumCells());
                if (!board.isSet(cell)){
                    options = board.getOptionsList(cell);
                }
            } while (board.isSet(cell) || options.size() <= 1);

            int value = options.get(rand.nextInt(options.size()));
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
        for (int cell = 0; cell < board.getNumCells(); cell++){
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
