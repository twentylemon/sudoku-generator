package sudoku;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import sudoku.util.StringUtil;

/**
 * The sudoku board. The board itself is a basic integer array.
 * The board also maintains static lists of peers and units for fast repeated
 * access. This doesn't allow for multiple size boards in one instance.
 *
 * @author Taras Mychaskiw
 */
public class SudokuBoard {

    protected final int[] board;                //the actual board values
    protected final int p, q, n;                //board dimensions

    //only one size can exist per instance of the program... oh well
    protected static final Map<Integer,Set<Integer>> peers = new HashMap<>();
    protected static final Map<Integer,List<List<Integer>>> units = new HashMap<>();
    protected final String LINE;
    protected final int CELL_WIDTH;

    /**
     * Constructs a new board with the given small box dimensions.
     *
     * @param p width of the small box
     * @param q height of the small box
     */
    public SudokuBoard(int p, int q){
        this.p = p;
        this.q = q;
        this.n = p * q;
        board = new int[getNumCells()];

        CELL_WIDTH = 2 + getSize() / 10;
        String[] build = new String[q];
        Arrays.fill(build, StringUtil.repeat("-", p * CELL_WIDTH));
        LINE = StringUtil.join(build, "+");
    }

    /**
     * Constructs a new board with the given small box dimensions, and parses
     * the string so the this board has the values from the string.
     *
     * @param p width of the small box
     * @param q height of the small box
     * @param sudoku a result of SudokuBoard.toString() to copy into this
     */
    public SudokuBoard(int p, int q, String sudoku){
        this(p, q);
        fromString(sudoku);
    }

    /**
     * Copy constructor. Creates a deep copy of the other board.
     *
     * @param other the board to copy
     */
    public SudokuBoard(SudokuBoard other){
        this.p = other.p;
        this.q = other.q;
        this.n = other.n;
        this.board = new int[n * n];
        this.CELL_WIDTH = other.CELL_WIDTH;
        this.LINE = other.LINE;
        System.arraycopy(other.board, 0, board, 0, getNumCells());
    }

    public boolean isSet(int cell){ return board[cell] != 0; }
    public boolean isSet(int x, int y){ return isSet(pointToCell(x, y)); }
    public void setCell(int cell, int value){ board[cell] = value; }
    public void setCell(int x, int y, int value){ setCell(pointToCell(x, y), value); }
    public int getCell(int cell){ return board[cell]; }
    public int getCell(int x, int y){ return getCell(pointToCell(x, y)); }
    public void clearCell(int cell){ board[cell] = 0; }
    public void clearCell(int x, int y){ clearCell(pointToCell(x, y)); }

    public int getSmallWidth(){ return p; }
    public int getSmallHeight(){ return q; }
    public int getSize(){ return n; }
    public int getNumCells(){ return n * n; }

    public int getRow(int cell){ return cell % n; }
    public int getCol(int cell){ return cell / n; }

    public int getBoxRow(int x, int y){ return y - y % getSmallWidth(); }
    public int getBoxRow(int cell){ return getBoxRow(cellToPoint(cell)); }
    public int getBoxRow(Point point){ return getBoxRow(point.x, point.y); }

    public int getBoxCol(int x, int y){ return x - x % getSmallHeight(); }
    public int getBoxCol(int cell){ return getBoxCol(cellToPoint(cell)); }
    public int getBoxCol(Point point){ return getBoxCol(point.x, point.y); }

    public Point getBoxCorner(int x, int y){ return new Point(getBoxCol(x, y), getBoxRow(x, y)); }
    public Point getBoxCorner(int cell){ return getBoxCorner(cellToPoint(cell)); }
    public Point getBoxCorner(Point point){ return getBoxCorner(point.x, point.y); }

    public int pointToCell(int x, int y){ return y*n + x; }
    public int pointToCell(Point p){ return pointToCell(p.x, p.y); }
    public Point cellToPoint(int cell){ return new Point(getRow(cell), getCol(cell)); }


    /**
     * @return the number of cells that are set in the board
     */
    public int getNumClues(){
        int clues = 0;
        for (int i = 0; i < board.length; i++){
            if (isSet(i)){
                clues++;
            }
        }
        return clues;
    }

    /**
     * Returns the set of peers to the point (x,y). The peers are those cells
     * which (x,y) cannot be the same value as.
     *
     * @param x row index
     * @param y column index
     * @return set of cell index peers to (x,y)
     */
    public Set<Integer> getPeers(int x, int y){
        if (!peers.containsKey(pointToCell(x, y))){
            final Set<Integer> set = new HashSet<>();
            for (int i = 0; i < getSize(); i++){
                set.add(pointToCell(i, y));
                set.add(pointToCell(x, i));
            }

            final Point box = getBoxCorner(x, y);
            for (int row = 0; row < getSmallWidth(); row++){
                for (int col = 0; col < getSmallHeight(); col++){
                    set.add(pointToCell(box.x + col, box.y + row));
                }
            }
            set.remove(pointToCell(x, y));
            peers.put(pointToCell(x, y), set);
        }
        return peers.get(pointToCell(x, y));
    }
    public Set<Integer> getPeers(int cell){ return getPeers(cellToPoint(cell)); }
    public Set<Integer> getPeers(Point point){ return getPeers(point.x, point.y); }


    /**
     * Returns a 2d list of units the cell belongs to. Similar to peers,
     * except each list is the entire list of cells in a particular constraint.
     * For example, at (0,0) the units are:
     * [0,1,2,3,4,5,6,7,8]          row
     * [0,9,18,27,36,45,54,63,72]   column
     * [0,1,2,9,10,11,18,19,20]     box
     *
     * @param x row index
     * @param y column index
     * @return list of units this cell belongs to
     */
    public List<List<Integer>> getUnits(int x, int y){
        if (!units.containsKey(pointToCell(x, y))){
            List<List<Integer>> list = new ArrayList<>();
            list.add(new ArrayList<Integer>(getSize()));
            list.add(new ArrayList<Integer>(getSize()));
            list.add(new ArrayList<Integer>(getSize()));

            for (int i = 0; i < getSize(); i++){
                list.get(0).add(pointToCell(x, i));
                list.get(1).add(pointToCell(i, y));
            }
            final Point box = getBoxCorner(x, y);
            for (int row = 0; row < getSmallWidth(); row++){
                for (int col = 0; col < getSmallHeight(); col++){
                    list.get(2).add(pointToCell(box.x + col, box.y + row));
                }
            }
            units.put(pointToCell(x, y), list);
        }
        return units.get(pointToCell(x, y));
    }
    public List<List<Integer>> getUnits(int cell){ return getUnits(cellToPoint(cell)); }
    public List<List<Integer>> getUnits(Point point){ return getUnits(point.x, point.y); }


    /**
     * Determines the candidates for the cell (x,y)
     *
     * @param x row index
     * @param y column index
     * @return the set of all possible values that can sit in (x,y)
     */
    public CandidateSet getOptions(int x, int y){
        final CandidateSet options = new CandidateSet(getSize());
        for (int value = 1; value <= getSize(); value++){
            if (!isAcceptable(x, y, value)){
                options.remove(value);
            }
        }
        return options;
    }
    public CandidateSet getOptions(int cell){ return getOptions(cellToPoint(cell)); }
    public CandidateSet getOptions(Point point){ return getOptions(point.x, point.y); }


    /**
     * Returns true if the value can legally be set in (x,y)
     *
     * @param x row index
     * @param y column index
     * @param value the value to test for legality in (x,y)
     * @return true if value obeys the sudoku rules
     */
    public boolean isAcceptable(int x, int y, int value){
        for (int cell : getPeers(x, y)){
            if (isSet(cell) && getCell(cell) == value){
                return false;
            }
        }
        return true;
    }
    public boolean isAcceptable(int cell, int value){ return isAcceptable(cellToPoint(cell), value); }
    public boolean isAcceptable(Point point, int value){ return isAcceptable(point.x, point.y, value); }


    /**
     * @return true if this board is solved
     */
    public boolean isSolved(){
        for (int cell = 0; cell < getNumCells(); cell++){
            if (!isSet(cell) || !isAcceptable(cell, getCell(cell))){
                return false;
            }
        }
        return true;
    }


    /**
     * Translates this board into a string.
     *
     * @return string representation of the board
     */
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder((n + p - 1) * LINE.length());

        int cell = 0;
        while (cell < getNumCells()){
            String str = isSet(cell) ? String.valueOf(getCell(cell)) : ".";
            str = StringUtil.padRight(str, ' ', CELL_WIDTH);
            sb.append(str);
            cell++;

            if (cell % getSmallWidth() == 0 && cell != getNumCells()){
                if (cell % getSize() == 0){
                    sb.append("\n");
                    if (cell % (getSmallHeight() * getSize()) == 0){
                        sb.append(LINE).append("\n");
                    }
                }
                else {
                    sb.append("|");
                }
            }
        }
        return sb.toString();
    }


    /**
     * Parses a SudokuBoard.toString() into a SudokuBoard. Two possible
     * String formats are accepted, one is a result of toString(). The other
     * is a String of digits if the size is less than 10, with gives as the
     * numbers, and 0 or any non number as non givens.
     * Example 4x4: ....1234........
     *
     * @param sudoku a result of SudokuBoard.toString() to copy into this
     */
    public void fromString(String sudoku){
        if (sudoku.contains(LINE)){
            fromStringLarge(sudoku);
        }
        else if (getSize() < 10){
            fromStringSmall(sudoku);
        }
    }

    /**
     * Parses a result of toString() into this board.
     *
     * @param sudoku a result of SudokuBoard.toString()
     */
    private void fromStringLarge(String sudoku){
        String[] repl = { LINE, "|", "\n" };    //replace these by empty string
        String[] values = StringUtil.replaceAll(sudoku, repl, "").split("\\s");
        for (int cell = 0; cell < getNumCells(); cell++){
            int v;
            try {
                v = Integer.valueOf(values[cell]);
            } catch (NumberFormatException e){ v = 0; }
            clearCell(cell);
            if (v > 0){
                setCell(cell, v);
            }
        }
    }

    /**
     * Parses a small string into this board.
     *
     * @param sudoku a small sudoku string
     * @see fromString(String)
     */
    private void fromStringSmall(String sudoku){
        for (int i = 0; i < getNumCells(); i++){
            int val = Character.digit(sudoku.charAt(i), 10);
            if (val <= 0){
                clearCell(i);
            }
            else {
                setCell(i, val);
            }
        }
    }


    /**
     * Returns true if the object sent is a board and it contains the same
     * values as this board.
     *
     * @param o the object to test
     * @return true if o is a Board and they represent the same sudoku
     */
    @Override
    public boolean equals(Object o){
        if (o instanceof SudokuBoard){
            SudokuBoard other = (SudokuBoard)o;
            for (int cell = 0; cell < getNumCells(); cell++){
                if (getCell(cell) != other.getCell(cell)){
                    return false;
                }
            }
            return true;
        }
        return false;
    }


    /**
     * Returns the hash code for this board.
     *
     * @return the hash code
     */
    @Override
    public int hashCode(){
        return 29 * 3 + Arrays.hashCode(this.board);
    }
}
