package sudoku.clp;

import sudoku.CandidateSet;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import sudoku.SudokuBoard;

/**
 * In addition to what a SudokuBoard has, this also holds the possible values
 * that each cell can have. SudokuBoard.getOptions(int) does this already,
 * but this is a different approach entirely. Instead of arbitrarily assigning
 * values to a cell, the major action is instead to eliminate a possible
 * candidate for a cell.
 *
 * @author Taras Mychaskiw
 */
class CLPBoard {

    private final List<CandidateSet> values;
    private final SudokuBoard board;  //treat as immutable

    CLPBoard(SudokuBoard board){
        this.board = board;
        values = new ArrayList<>(board.getNumCells());
        for (int i = 0; i < board.getNumCells(); i++){
            values.add(new CandidateSet(board.getSize()));
        }
        for (int cell = 0; cell < board.getNumCells(); cell++){
            if (board.isSet(cell)){
                assign(cell, board.getCell(cell));
            }
        }
    }

    CLPBoard(CLPBoard other){
        this.board = other.board;
        values = new ArrayList<>(board.getNumCells());
        for (int i = 0; i < board.getNumCells(); i++){
            values.add(new CandidateSet(other.getCandidateSet(i)));
        }
    }

    CandidateSet getCandidateSet(int cell){ return values.get(cell); }
    SudokuBoard getBoard(){ return board; }


    /**
     * Assigns the value to the cell. What actually happens is all other
     * candidates for the cell are eliminated.
     *
     * @param cell the cell to assign a value to
     * @param valToAssign the value to assign
     * @return true if it was possible to assign the value
     */
    boolean assign(int cell, int valToAssign){
        for (Integer value : values.get(cell)){
            if (value != valToAssign && !eliminate(cell, value)){
                return false;
            }
        }
        return true;
    }


    /**
     * Eliminates the value from the list of candidates for the cell.
     * If any new constraints are found, they are propagated out.
     *
     * @param cell the cell to eliminate a candidate from
     * @param valToRemove the candidate to remove
     * @return true if it was possible to remove the candidate from the cell
     */
    boolean eliminate(int cell, int valToRemove){
        if (!values.get(cell).has(valToRemove)){
            return true;
        }

        values.get(cell).remove(valToRemove);   //remove the candidate
        if (values.get(cell).cardinality() == 0){
            return false;   //removed last candidate
        }
        else if (values.get(cell).cardinality() == 1){
            //only one possibilty left, remove the value from this cell's peers
            final int value = values.get(cell).getLowestValue();
            for (int peer : board.getPeers(cell)){
                if (!eliminate(peer, value)){
                    return false;   //failed to eliminate value from a peer
                }
            }
        }

        /**
         * check units: if any unit is reduced to one place for a value, assign
         * that value there. if there is nowhere else to put the value, fail
         */
        for (List<Integer> unit : board.getUnits(cell)){
            final LinkedList<Integer> valuePlaces = new LinkedList<>();
            for (Integer spot : unit){
                if (values.get(spot).has(valToRemove)){
                    valuePlaces.add(spot);
                }
            }

            if (valuePlaces.isEmpty()){
                return false;   //nowhere for the value to be
            }
            else if (valuePlaces.size() == 1){
                //the value can only be in one place, assign it there
                if (!assign(valuePlaces.getFirst().intValue(), valToRemove)){
                    return false;   //failed to assign
                }
            }
        }
        return true;
    }


    /**
     * Returns the cell which is most constrained, or -1 if the board is solved.
     * The most constrained cell is the one with the fewest number of possible
     * values that can be set in it.
     *
     * @return constrained cell, or -1 if solved
     */
    int getConstrainedCell(){
        int cell = -1, min = Integer.MAX_VALUE;
        for (int i = 0; i < board.getNumCells(); i++){
            int size = values.get(i).cardinality();
            if (size > 1 && size < min){
                min = size;
                cell = i;
                if (min == 2){
                    return cell;
                }
            }
        }
        return cell;
    }

    /**
     * Returns a new SudokuBoard which is the solved version of this board.
     *
     * @return a solved SudokuBoard
     */
    SudokuBoard getSolvedBoard(){
        SudokuBoard brd = new SudokuBoard(this.board);
        for (int cell = 0; cell < brd.getNumCells(); cell++){
            brd.setCell(cell, values.get(cell).getLowestValue());
        }
        return brd;
    }
}
