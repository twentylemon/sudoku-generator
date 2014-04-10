package sudoku;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import sudoku.backtrack.BacktrackSolver;
import sudoku.clp.ConstraintSolver;
import sudoku.dlx.ExactCoverSolver;
import sudoku.util.MapUtil;

/**
 * Service that runs all solvers each in their own thread. Once one thread
 * finishes, all other threads are killed and the result is returned.
 *
 * @author Taras Mychaskiw
 */
public class SolverService {

    private static SudokuBoard BOARD;   //board being solved
    private static Class winner = null;
    private static long time = 0;
    private static long totalTime = 0;
    private static final Map<Class,Integer> wins = new HashMap<>();
    private static int totalRuns = 0;

    /**
     * list of all the solvers testing if the sudoku is well formed
     */
    private static final SudokuSolver BACK = new BacktrackSolver();
    private static final SudokuSolver CLP = new ConstraintSolver();
    private static final SudokuSolver DLX = new ExactCoverSolver();
    private static final List<Callable<Integer>> CALLABLES = new LinkedList<Callable<Integer>>(){{
        add(new Callable<Integer>(){
            @Override
            public Integer call(){
                long time = System.currentTimeMillis();
                Integer result = BACK.getFormity(BOARD);
                setWinner(BacktrackSolver.class, System.currentTimeMillis() - time);
                return result;
            }
        });
        add(new Callable<Integer>(){
            @Override
            public Integer call(){
                long time = System.currentTimeMillis();
                Integer result = CLP.getFormity(BOARD);
                setWinner(ConstraintSolver.class, System.currentTimeMillis() - time);
                return result;
            }
        });
        add(new Callable<Integer>(){
            @Override
            public Integer call(){
                long time = System.currentTimeMillis();
                Integer result = DLX.getFormity(BOARD);
                setWinner(ExactCoverSolver.class, System.currentTimeMillis() - time);
                return result;
            }
        });
    }};
    private static final int NUM_THREADS = CALLABLES.size();

    /**
     * Checks if the sudoku is well formed. Instead of picking a general
     * solving strategy, a thread is opened for each strategy and they all test
     * to see if the sudoku is well formed. After the first one returns,
     * the other threads are killed.
     *
     * @param board the board to test
     * @return the formity of the board
     */
    public static int getFormity(SudokuBoard board){
        BOARD = board;
        winner = null;
        ExecutorService executor = Executors.newFixedThreadPool(NUM_THREADS);
        int result = 0;
        try {
            result = executor.invokeAny(CALLABLES);
        } catch (InterruptedException | ExecutionException ex){}
        executor.shutdown();
        return result;
    }

    /**
     * Updates the winner variable.
     *
     * @param winner the class that won
     * @param time how much time they took
     */
    private static void setWinner(Class winner, long time){
        if (SolverService.winner == null){
            SolverService.winner = winner;
            SolverService.time = time;
            wins.put(winner, MapUtil.maybeGet(wins, winner, 0) + 1);
            totalRuns++;
            totalTime += time;
        }
    }

    public static long getLastTime(){ return time; }
    public static Class getLastWinner(){ return winner; }
    public static int getTotalRuns(){ return totalRuns; }
    public static long getTotalTime(){ return totalTime; }
    public static Map<Class,Integer> getWinMap(){ return wins; }
}
