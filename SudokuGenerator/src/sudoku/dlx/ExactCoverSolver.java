package sudoku.dlx;

import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import sudoku.SudokuBoard;
import sudoku.SudokuSolver;

/**
 * Uses exact cover and dancing links to solve the sudoku.
 *
 * @author Taras Mychaskiw
 */
public class ExactCoverSolver implements SudokuSolver {

    /**
     * Tries to solve the sudoku board.
     *
     * @param board the board to solve
     * @return a solved version of the board, or null if unsolvable
     */
    @Override
    public SudokuBoard solve(SudokuBoard board){
        return solve(new LinkedList<Node>(), DLXGenerator.generate(board));
    }

    /**
     * Tries to solve the sudoku board.
     *
     * @param partial the stack of node containing the solution so far
     * @param dlx the DancingLinks which holds the board
     * @return a solved version of the board, or null if unsolvable
     */
    private SudokuBoard solve(Deque<Node> partial, DancingLinks dlx){
        if (dlx.isSolved()){
            return listToBoard(partial, dlx);
        }
        ColumnHeader col = dlx.findBestColumn();
        SudokuBoard result = null;
        dlx.cover(col);
        Node row = col.down;
        while (row != col){
            partial.add(row);
            Node node = row.right;
            while (node != row){
                dlx.cover(node.head);
                node = node.right;
            }
            result = solve(partial, dlx);
            if (result != null){
                return result;
            }
            partial.removeLast();
            node = row.left;
            while (node != row){
                dlx.uncover(node.head);
                node = node.left;
            }
            row = row.down;
        }
        dlx.uncover(col);
        return result;
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
        fill(new LinkedList<Node>(), DLXGenerator.generate(board), list);
        return list;
    }

    /**
     * Populates the list with solutions to the board. Basically just solves
     * the sudoku over and over again, keeping the solutions.
     *
     * @param partial the stack of node containing the solution so far
     * @param dlx the DancingLinks which holds the board
     * @param list where to store the solutions
     */
    private void fill(Deque<Node> partial, DancingLinks dlx, List<SudokuBoard> list){
        if (dlx.isSolved()){
            list.add(listToBoard(partial, dlx));
            return;
        }
        ColumnHeader col = dlx.findBestColumn();
        dlx.cover(col);
        Node row = col.down;
        while (row != col){
            partial.add(row);
            Node node = row.right;
            while (node != row){
                dlx.cover(node.head);
                node = node.right;
            }
            fill(partial, dlx, list);
            partial.removeLast();
            node = row.left;
            while (node != row){
                dlx.uncover(node.head);
                node = node.left;
            }
            row = row.down;
        }
        dlx.uncover(col);
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
        return isWellFormed(new LinkedList<Node>(), DLXGenerator.generate(board), set) && set.size() == 1;
    }

    /**
     * Continually solves the sudoku until there are no more solutions or until
     * a second solution was found. If a second solution is found, then an
     * exception is thrown to immediately stop the process. I wish java had
     * lazy evaluation. length(enumerations(board)) > 1; done
     *
     * @param partial the stack of node containing the solution so far
     * @param dlx the DancingLinks which holds the board
     * @param soln the set to store the solutions in
     * @return true if the sudoku board has only one solution
     */
    private boolean isWellFormed(Deque<Node> partial, DancingLinks dlx, Set<SudokuBoard> soln){
        if (Thread.interrupted()){
            throw new RuntimeException();
            //return false;
        }
        if (dlx.isSolved()){
            return !soln.add(listToBoard(partial, dlx)) || soln.size() <= 1;
        }
        ColumnHeader col = dlx.findBestColumn();
        dlx.cover(col);
        Node row = col.down;
        while (row != col){
            partial.add(row);
            Node node = row.right;
            while (node != row){
                dlx.cover(node.head);
                node = node.right;
            }
            if (!isWellFormed(partial, dlx, soln)){
                return false;
            }
            partial.removeLast();
            node = row.left;
            while (node != row){
                dlx.uncover(node.head);
                node = node.left;
            }
            row = row.down;
        }
        dlx.uncover(col);
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
        if (isWellFormed(new LinkedList<Node>(), DLXGenerator.generate(board), set)){
            if (set.isEmpty()){
                return NO_SOLUTIONS;
            }
            else if (set.size() == 1){
                return UNIQUE_SOLUTION;
            }
        }
        return MULTIPLE_SOLUTIONS;
    }


    /**
     * Converts the list containing the solution into a SudokuBoard.
     *
     * @param list the list of nodes with the solution
     * @param dlx the DancingLinks which holds the board
     * @return the SudokuBoard held in the list of Nodes
     */
    private SudokuBoard listToBoard(Deque<Node> list, DancingLinks dlx){
        SudokuBoard board = new SudokuBoard(dlx.getBoard());
        List<List<Constraint>> solution = new LinkedList<>();
        for (Node row : list){
            solution.add(dlx.getRowName(row));
        }

        for (List<Constraint> row : solution){
            int x = -1, y = -1, value = -1;
            for (Constraint item : row){
                if (item instanceof ColumnConstraint){
                    x = ((ColumnConstraint)item).column;
                    value = ((ColumnConstraint)item).value;
                }
                else if (item instanceof RowConstraint){
                    y = ((RowConstraint)item).row;
                }
            }
            board.setCell(x, y, value);
        }
        return board;
    }
}
