package sudoku;

import sudoku.generate.SlowGenerator;
import sudoku.generate.SudokuGenerator;

/**
 * Main class for testing stuff.
 *
 * @author Taras Mychaskiw
 */
public class Test {
    public static void main(String[] args){
        /*
        SudokuBoard board = new SudokuBoard(3, 3, "003020600900305001001806400008102900700000008006708200002609500800203009005010300");
        board = new SudokuBoard(3, 3, "4.....8.5.3..........7......2.....6.....8.4......1.......6.3.7.5..2.....1.4......");
        //board = new SudokuBoard(3, 3, ".....6....59.....82....8....45........3........6..3.54...325..6..................");
        //board = new SudokuBoard(3, 3, "..36.49......5....9.......72.......6.4.....5.8.......11.......5..........9273641.");
        board = new SudokuBoard(3, 3, "...42....83......9..6....85...7.1...3.8...9..9.....41..9.....63.75.........679...");
        //board = new SudokuBoard(3, 3, "3...8.......7....51..............36...2..4....7...........6.13..452...........8..");

        System.out.println(board + "\n");
        SudokuSolver s = new BacktrackSolver();
        //s = new ConstraintSolver();
        //s = new ExactCoverSolver();
        long time = System.currentTimeMillis();
        //List<SudokuBoard> list = s.enumerate(board);
        SudokuBoard solved = s.solve(board);
        //boolean well = s.isWellFormed(board);
        time = System.currentTimeMillis() - time;

        //System.out.println(list + "\n" + list.size() + "\n" + time);
        System.out.println(solved + "\n" + time);
        //System.out.println(well + "\n" + time);

        System.out.println(solved.getOptions(0, 0));
        */
/*
        SudokuBoard board = new TopDownGenerator(3, 2).getProblem();
        System.out.println();
        System.out.println(new BacktrackSolver().isWellFormed(board));
        System.out.println(new ConstraintSolver().isWellFormed(board));
        System.out.println(new ExactCoverSolver().isWellFormed(board));


        System.out.println(SolverService.isWellFormed(board));
        System.out.println(SolverService.getLastTime());
        System.out.println(SolverService.getLastWinner());

        System.out.println();
        System.out.println(SolverService.isWellFormed(new SudokuBoard(board)));
        System.out.println(SolverService.getLastTime());
        System.out.println(SolverService.getLastWinner());
*/
        SudokuGenerator gen = new SlowGenerator(3, 3);
        //gen = new TopDownGenerator(3, 3);
        //gen = new DeductionGenerator(3, 3);
        long time = System.currentTimeMillis();
        SudokuBoard prob = gen.getProblem();
        time = System.currentTimeMillis() - time;
        System.out.println(prob);
        System.out.println("time = " + time);
        System.out.println("well = " + SolverService.getFormity(prob));
        System.out.println("clues = " + prob.getNumClues() + " / " + prob.getNumCells());
    }
}
