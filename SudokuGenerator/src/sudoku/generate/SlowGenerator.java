package sudoku.generate;

import java.util.PriorityQueue;
import java.util.Random;
import sudoku.CandidateSet;
import sudoku.SolverService;
import sudoku.SudokuBoard;
import sudoku.SudokuSolver;
import sudoku.util.ArrayUtil;

/**
 * Tries to make very few clue puzzles. Only adds the next clue if the
 * total number of known constraints is maximized among all possibilities.
 * More specifically, the sum of the cardinalities of all other option sets
 * should be maximized per each clue addition.
 *
 * @author Taras Mychaskiw
 */
public class SlowGenerator implements SudokuGenerator {

    private final int[] cells;
    private final SudokuBoard brd;
    private final Random rand;

    /**
     * Constructs a sudoku board generator able to generate boards with the
     * given dimensions.
     *
     * @param p the small box width
     * @param q the small box height
     */
    public SlowGenerator(int p, int q){
        this.brd = new SudokuBoard(p, q);
        cells = ArrayUtil.range(0, brd.getNumCells());
        rand = new Random();
    }

    /**
     * Constructs a sudoku board generator able to generate boards with the
     * same dimensions as the given board.
     *
     * @param board a sudoku board
     */
    public SlowGenerator(SudokuBoard board){
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
        int firstCell = rand.nextInt(brd.getNumCells());
        board.setCell(firstCell, 1);
        boolean done = false;

        while (!done){
            //build the priority queue
            PriorityQueue<Cell> queue = new PriorityQueue<>();
            for (int cell = 0; cell < board.getNumCells(); cell++){
                if (!board.isSet(cell)){
                    CandidateSet options = board.getOptions(cell);
                    for (int value : options){
                        int cardinality = 0;
                        board.setCell(cell, value);
                        for (int i = 0; i < board.getNumCells(); i++){
                            if (!board.isSet(i)){
                                cardinality += board.getOptions(i).cardinality();
                            }
                        }
                        board.clearCell(cell);
                        queue.add(new Cell(cell, value, cardinality));
                    }
                }
            }

            while (true){
                Cell c = queue.remove();    //set the first value
                board.setCell(c.cell, c.value);

                int form = SolverService.getFormity(board);
                if (form == SudokuSolver.NO_SOLUTIONS){
                    board.clearCell(c.cell);    //bad cell, clear and try next
                }
                else if (form == SudokuSolver.UNIQUE_SOLUTION){
                    done = true;    //we're done, return the board
                    break;
                }
                else {
                    break;  //multiple solutions still, add more values
                }
            }
        }

        //try removing additional values
        ArrayUtil.shuffle(cells);
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

    /**
     * Simple class to hold the cell and total cardinality pair.
     */
    private class Cell implements Comparable<Cell> {

        final int cell;
        final int value;
        final int card;

        Cell(int cell, int value, int cardinality){
            this.cell = cell;
            this.value = value;
            this.card = cardinality;
        }

        /**
         * Returns the comparison of this CellPair and another.
         * @param t the other CellPair to compare to
         * @return -1 if this CellPair has lower cardinality, 1 otherwise
         */
        @Override
        public int compareTo(Cell t){
            if (card < t.card){
                return 1;
            }
            else if (card > t.card){
                return -1;
            }
            return 0;
        }
    }
}
