package sudoku;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import sudoku.generate.SudokuGenerator;
import sudoku.util.StringUtil;

/**
 * Main entry point for the application.
 *
 * @author Taras Mychaskiw
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args){
        ArgsParser parse = new ArgsParser(args);

        int p = parse.getSmallWidth(), q = parse.getSmallHeight();
        int size = p * q;
        SudokuGenerator gen = parse.getGenerator();
        int num = parse.getNumToCreate();
        String path = parse.getPath();
        boolean verbose = parse.isVerbose();

        System.out.println("sudoku size....: "+size+"x"+size+" (p = "+p+", q = "+q+")");
        System.out.println("generator......: " + gen.getClass().getSimpleName());
        System.out.println("will create....: " + num + " sudoku problems");
        System.out.println("output file....: " + path);
        System.out.println("verbose mode...: " + (verbose ? "on" : "off"));
        System.out.println();

        run(gen, num, path, verbose);
    }

    /**
     * Writes all the problems into the file.
     *
     * @param problems the sudoku problems to save
     * @param output where to save the problems
     */
    public static void writeFile(List<SudokuBoard> problems, String output){
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(output))){
            for (SudokuBoard board : problems){
                writer.write(board + "\n\n");
            }
            writer.close();
            System.out.println("output file created: " + output);
        } catch (IOException ex){
            System.err.println("FAILED TO WRITE OUTPUT FILE");
        }
    }

    /**
     * Runs the program.
     * @param generator what to use to generate the sudoku problems
     * @param numToCreate how many problems to create
     * @param output where to store the problems
     * @param verbose dump problems to screen if on
     */
    public static void run(SudokuGenerator generator, int numToCreate, String output, final boolean verbose){
        List<SudokuBoard> problems = new ArrayList<>(numToCreate);
        int minClues = Integer.MAX_VALUE;
        SudokuBoard minBoard = null;
        int totalClues = 0;
        long totalTime = 0;
        //maxTime is always the first board generated due to JIT it seems
        //long maxTime = Long.MIN_VALUE;
        //SudokuBoard maxBoard = null;
        for (int i = 1; i <= numToCreate; i++){
            long time = System.currentTimeMillis();
            SudokuBoard board = generator.getProblem();
            time = System.currentTimeMillis() - time;

            int numClues = board.getNumClues();
            int numCells = board.getNumCells();
            if (numClues < minClues){
                minClues = numClues;
                minBoard = board;
            }
            /*
            if (time > maxTime){
                maxTime = time;
                maxBoard = board;
            }
                    */
            totalClues += numClues;
            totalTime += time;

            System.out.print("problem " + i + " of " + numToCreate + ": ");
            System.out.println(numClues + " / " + numCells + " problem created, took " + time + "ms");
            if (verbose){
                System.out.println(board);
                System.out.println();
            }
            problems.add(board);
        }

        System.out.println("\nboard with minimal number of clues ("+minClues+") created:\n" + minBoard);
        //System.out.println("\nboard that took longest to create ("+maxTime+"ms):\n" + maxBoard);

        System.out.println("\nsolver statistics");
        System.out.println("        strategy        |  wins  ");
        System.out.println("------------------------+--------");
        Map<Class,Integer> wins = SolverService.getWinMap();
        for (Class strat : wins.keySet()){
            String name = " " + StringUtil.padRight(strat.getSimpleName(), ' ', 23);
            System.out.println(name + "|  " + wins.get(strat));
        }
        System.out.println();
        System.out.println("average number of clues per puzzle:  " + ((double)totalClues / numToCreate));
        System.out.println("average time to generate one puzzle: " + ((double)totalTime / numToCreate) + "ms");
        System.out.println();

        writeFile(problems, output);
    }
}
