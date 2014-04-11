package sudoku;

import java.awt.Point;
import sudoku.generate.BottomUpGenerator;
import sudoku.generate.DeductionGenerator;
import sudoku.generate.SudokuGenerator;
import sudoku.generate.TopDownGenerator;
import sudoku.util.ArrayUtil;

/**
 * Parses the command line arguments.
 *
 * @author Taras Mychaskiw
 */
public class ArgsParser {

    private final SudokuGenerator gen;
    private final int p, q;
    private final int num;
    private final String path;
    private final boolean verbose;

    /**
     * Pulls all the information out of the command line arguments.
     *
     * @param args the command line arguments
     */
    public ArgsParser(String[] args){
        if (args.length == 0){
            usage();    //display help and exit
            System.exit(0);
        }
        Point size = parseSize(args);
        p = size.x; q = size.y;
        gen = parseGen(args);
        num = parseNum(args);
        path = parsePath(args);
        verbose = ArrayUtil.contains(args, "-v");
    }

    public SudokuGenerator getGenerator(){ return gen; }
    public int getSmallWidth(){ return p; }
    public int getSmallHeight(){ return q; }
    public int getNumToCreate(){ return num; }
    public String getPath(){ return path; }
    public boolean isVerbose(){ return verbose; }

    /**
     * @param args the command line arguments
     * @return the size of the sudoku to create
     */
    private Point parseSize(String[] args){
        for (int i = 0; i < args.length; i++){
            if (args[i].equals("-s")){
                return new Point(Integer.valueOf(args[i+1]), Integer.valueOf(args[i+2]));
            }
        }
        return new Point(3, 3);
    }

    /**
     * @param args the command line arguments
     * @return the generator to use
     */
    private SudokuGenerator parseGen(String[] args){
        for (int i = 0; i < args.length; i++){
            if (args[i].equals("-g")){
                switch (args[i+1]) {
                    case "t":
                        return new TopDownGenerator(p, q);
                    case "b":
                        return new BottomUpGenerator(p, q);
                    case "d":
                        return new DeductionGenerator(p, q);
                }
            }
        }
        throw new IllegalArgumentException("generator type if required: use -g t|b");
    }

    /**
     * @param args the command line arguments
     * @return the number of sudokus to create
     */
    private int parseNum(String[] args){
        for (int i = 0; i < args.length; i++){
            if (args[i].equals("-n")){
                return Integer.valueOf(args[i+1]);
            }
        }
        return 100;
    }

    /**
     * @param args the command line arguments
     * @return the file path to where the sudoku problems should be stored
     */
    private String parsePath(String[] args){
        for (int i = 0; i < args.length; i++){
            if (args[i].equals("-o")){
                return args[i+1];
            }
        }
        return System.currentTimeMillis() + ".sudoku.txt";
    }

    /**
     * Displays usage details.
     */
    public void usage(){
        System.out.println("Usage: java -cp ./build/classes sudoku.Main [-s p q] -g t|b|d [-n number] [-o path] [-v]\n\n"
                + "\t-s\tSpecifiy the size of the sudoku boards to create.\n"
                + "\t\tp: width of the box regions\n"
                + "\t\tq: height of the box regions\n"
                + "\t\tBy default, 9x9 (p=q=3) boards are created.\n\n"
                + "\t-g\tSpecifiy which type of generator to use.\n"
                + "\t\tt: use a top down generator\n"
                + "\t\tb: use a bottom up generator\n"
                + "\t\td: use a deduction generator\n\n"
                + "\t-n\tSpecify the number of sudokus to create.\n"
                + "\t\tnumber: the number of sudokus to create\n"
                + "\t\tBy default, 100 sudokus are created.\n\n"
                + "\t-o\tSpecify the location to store the sudokus.\n"
                + "\t\tpath: the file to store all the sudokus in\n"
                + "\t\tBy default, \"[system_time].sudoku.txt\" is used.\n\n"
                + "\t-v\tTurn verbose mode on, default off.\n"
        );
    }
}
