package sudoku.generate;

import sudoku.CandidateSet;
import sudoku.SolverService;
import sudoku.SudokuBoard;
import sudoku.SudokuSolver;
import sudoku.util.ArrayUtil;

/**
 * Tries to create sudoku boards with the target number of clues. Will
 * give up and return null after a certain number of attempts.
 *
 * @author Taras Mychaskiw
 */
public class TargetGenerator implements SudokuGenerator {

    private final int[] cells;
    private final SudokuBoard brd;
    private final long NUM_ATTEMPTS;
    private final int TARGET_CLUES;

    /**
     * Constructs a sudoku board generator able to generate boards with the
     * given dimensions.
     *
     * @param p the small box width
     * @param q the small box height
     * @param clues the target number of clues this generator should aim for
     * @param tries how many iterations to tries before just returning
     */
    public TargetGenerator(int p, int q, int clues, long tries){
        this.brd = new SudokuBoard(p, q);
        cells = ArrayUtil.range(0, brd.getNumCells());
        TARGET_CLUES = clues;
        NUM_ATTEMPTS = tries;
    }

    /**
     * Constructs a sudoku board generator able to generate boards with the
     * same dimensions as the given board.
     *
     * @param board a sudoku board
     * @param clues the target number of clues this generator should aim for
     * @param tries how many iterations to tries before just returning
     */
    public TargetGenerator(SudokuBoard board, int clues, long tries){
        this(board.getSmallWidth(), board.getSmallHeight(), clues, tries);
    }

    /**
     * Returns a random minimal well formed sudoku with the same dimensions
     * as specified in creation of this generator, or null if failure.
     * This generator tries to generate a sudoku problem with the given
     * number of clues. If after some number of attempts, no problem was found,
     * null is returned.
     *
     * @return a random minimal well formed sudoku problem, or null on failure
     */
    @Override
    public SudokuBoard getProblem(){
        SudokuBoard board;
        ArrayUtil.shuffle(cells);

        for (long i = 0; i < NUM_ATTEMPTS; i++){
            board = new SudokuBoard(brd);
            if (generate(board, TARGET_CLUES)){
                int form = SolverService.getFormity(board);
                if (form == SudokuSolver.UNIQUE_SOLUTION){
                    return board;
                }
            }
        }
        return null;
    }


    /**
     * Adds random clues to the board.
     *
     * @param board the board to add values to
     * @param numClues the number of clues to add
     * @return true if they were added successfully, false if something failed
     */
    private boolean generate(SudokuBoard board, int numClues){
        for (int i = 0; i < numClues; i++){
            int pos = 0;
            while (board.isSet(cells[pos])){
                pos++;
            }
            CandidateSet options = board.getOptions(cells[pos]);
            if (!options.isEmpty()){
                int val = options.getLowestValue();
                board.setCell(cells[pos], val);
            }
            else {
                return false;
            }
        }
        return true;
    }
}
