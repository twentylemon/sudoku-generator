package sudoku.backtrack;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import sudoku.CandidateSet;
import sudoku.SudokuBoard;
import sudoku.SudokuSolver;

/**
 * Uses backtracking to solve the sudoku board.
 *
 * @author Taras Mychaskiw
 */
public class BacktrackSolver implements SudokuSolver {

    /**
     * Tries to solve the sudoku board starting from the cell given.
     * This class employs backtracking to solve the sudoku.
     *
     * @param board the board to solve
     * @param cell the starting cell
     * @return true if the board was solve
     */
    private boolean solve(SudokuBoard board, int cell){
        if (cell == board.getNumCells()){
            return true;
        }
        if (board.isSet(cell)){
            return solve(board, cell + 1);
        }

        final CandidateSet options = board.getOptions(cell);
        for (Integer opt : options){
            board.setCell(cell, opt);
            if (solve(board, cell + 1)){
                return true;
            }
        }

        board.clearCell(cell);
        return false;
    }

    /**
     * Tries to solve the sudoku board.
     *
     * @param board the board to solve
     * @return a solved version of the board, or null if unsolvable
     */
    @Override
    public SudokuBoard solve(SudokuBoard board){
        SudokuBoard toSolve = new SudokuBoard(board);
        if (solve(toSolve, 0)){
            return toSolve;
        }
        return null;
    }


    /**
     * Returns a list of all solutions to the board given.
     *
     * @param board the board to solve
     * @return the list of all solutions to the board
     */
    @Override
    public List<SudokuBoard> enumerate(SudokuBoard board) {
        List<SudokuBoard> list = new LinkedList<>();
        fill(new SudokuBoard(board), 0, list);
        return list;
    }

    /**
     * Populates the list with solutions to the board. Basically just solves
     * the sudoku over and over again, keeping the solutions.
     *
     * @param board the board to solve
     * @param cell the starting cell
     * @param list where to store the solutions
     */
    private void fill(SudokuBoard board, int cell, List<SudokuBoard> list){
        if (cell == board.getNumCells()){
            list.add(new SudokuBoard(board));
            return;
        }
        if (board.isSet(cell)){
            fill(board, cell + 1, list);
            return;
        }

        final CandidateSet options = board.getOptions(cell);
        for (Integer opt : options){
            board.setCell(cell, opt);
            fill(board, cell + 1, list);
        }
        board.clearCell(cell);
    }


    /**
     * Returns true if the board is well formed, that is if the
     * board only has one solution.
     *
     * @param board the board to test
     * @return true if it only has one possible solution
     */
    @Override
    public boolean isWellFormed(SudokuBoard board){
        Set<SudokuBoard> set = new HashSet<>();
        return isWellFormed(new SudokuBoard(board), 0, set) && set.size() == 1;
    }

    /**
     * Continually solves the sudoku until there are no more solutions or until
     * a second solution was found. If a second solution is found, then an
     * exception is thrown to immediately stop the process. I wish java had
     * lazy evaluation. length(enumerations(board)) > 1; done
     *
     * @param board the board to check for well formed
     * @param cell the starting cell
     * @param soln the set to store the solutions in
     * @return true if the sudoku board has only one solution
     */
    private boolean isWellFormed(SudokuBoard board, int cell, Set<SudokuBoard> soln){
        if (Thread.interrupted()){
            throw new RuntimeException();   //fuck the stack
            //return false;
        }
        if (cell == board.getNumCells()){
            return !soln.add(new SudokuBoard(board)) || soln.size() <= 1;
        }
        if (board.isSet(cell)){
            return isWellFormed(board, cell + 1, soln);
        }

        final CandidateSet options = board.getOptions(cell);
        for (Integer opt : options){
            board.setCell(cell, opt);
            if (!isWellFormed(board, cell + 1, soln)){
                return false;
            }
        }
        board.clearCell(cell);
        return true;
    }


    /**
     * Returns the formity of the board sent.
     *
     * @param board the board to test
     * @return 0 if no solutions, 1 if unique solution, -1 if multiple solutions
     */
    @Override
    public int getFormity(SudokuBoard board){
        Set<SudokuBoard> set = new HashSet<>();
        if (isWellFormed(new SudokuBoard(board), 0, set)){
            if (set.isEmpty()){
                return NO_SOLUTIONS;
            }
            else if (set.size() == 1){
                return UNIQUE_SOLUTION;
            }
        }
        return MULTIPLE_SOLUTIONS;
    }
}
