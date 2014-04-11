package sudoku.clp;

import sudoku.CandidateSet;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import sudoku.SudokuBoard;
import sudoku.SudokuSolver;

/**
 * Uses constraint logic programming to solve the sudoku board.
 *
 * @author Taras Mychaskiw
 */
public class ConstraintSolver implements SudokuSolver {

    /**
     * Tries to solve the sudoku board.
     *
     * @param board the board to solve
     * @return a solved version of the board, or null if unsolvable
     */
    @Override
    public SudokuBoard solve(SudokuBoard board){
        CLPBoard conBoard = new CLPBoard(board);
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
     */
    private CLPBoard solve(CLPBoard sudoku){
        int cell = sudoku.getConstrainedCell();
        if (cell < 0){
            return sudoku;   //board is solved
        }

        final CandidateSet set = sudoku.getCandidateSet(cell);
        for (Integer value : set){
            final CLPBoard copy = new CLPBoard(sudoku);
            if (copy.assign(cell, value)){
                final CLPBoard board = solve(copy);
                if (board != null){
                    return board;
                }
            }
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
    public List<SudokuBoard> enumerate(SudokuBoard board){
        List<SudokuBoard> list = new LinkedList<>();
        fill(new CLPBoard(board), list);
        return list;
    }

    /**
     * Populates the list with solutions to the board. Basically just solves
     * the sudoku over and over again, keeping the solutions.
     *
     * @param sudoku the board to solve
     * @param list where to store the solutions
     */
    private void fill(CLPBoard sudoku, List<SudokuBoard> list){
        int cell = sudoku.getConstrainedCell();
        if (cell < 0){
            list.add(sudoku.getSolvedBoard());
            return; //board is solved
        }

        final CandidateSet set = sudoku.getCandidateSet(cell);
        for (Integer value : set){
            final CLPBoard copy = new CLPBoard(sudoku);
            if (copy.assign(cell, value)){
                fill(copy, list);
            }
        }
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
        return isWellFormed(new CLPBoard(board), set) && set.size() == 1;
    }

    /**
     * Continually solves the sudoku until there are no more solutions or until
     * a second solution was found. If a second solution is found, then an
     * exception is thrown to immediately stop the process. I wish java had
     * lazy evaluation. length(enumerations(board)) > 1; done
     *
     * @param sudoku the board to check for well formed
     * @param soln the set to store the solutions in
     * @return true if the sudoku board has only one solution
     */
    private boolean isWellFormed(CLPBoard sudoku, Set<SudokuBoard> soln){
        if (Thread.interrupted()){
            throw new RuntimeException();
            //return false;
        }
        int cell = sudoku.getConstrainedCell();
        if (cell < 0){
            return !soln.add(sudoku.getSolvedBoard()) || soln.size() <= 1;
        }

        final CandidateSet set = sudoku.getCandidateSet(cell);
        for (Integer value : set){
            final CLPBoard copy = new CLPBoard(sudoku);
            if (copy.assign(cell, value) && !isWellFormed(copy, soln)){
                return false;
            }
        }
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
        if (isWellFormed(new CLPBoard(board), set)){
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
