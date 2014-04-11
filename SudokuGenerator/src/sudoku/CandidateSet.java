package sudoku;

import java.util.BitSet;
import java.util.Iterator;
import java.util.Random;

/**
 * Set of possible candidates, one of these will be needed for each cell in the
 * ConstraintBoard. A BitSet is used over a List<Boolean> or Set<Integer> to
 * avoid ConcurrentModifcationExceptions, since the size of the BitSet never
 * changes. A List could be written, but BitSet already has everything needed.
 *
 * @author Taras Mychaskiw
 */
public class CandidateSet implements Iterable<Integer> {

    private final BitSet bitset;    //to hold the candidates
    private final int MAX_SIZE;     //size of the board this set is for
    private static final Random rand = new Random();

    /**
     * Constructs a new candidate set with all values 1..maxSize set to true.
     *
     * @param maxSize the highest value this can hold
     */
    public CandidateSet(int maxSize){
        bitset = new BitSet(maxSize + 1);
        bitset.set(1, maxSize + 1); //set all to true by default
        this.MAX_SIZE = maxSize;
    }

    /**
     * Copy constructor.
     *
     * @param other the CandidateSet to deep copy
     */
    public CandidateSet(CandidateSet other){
        bitset = (BitSet)other.bitset.clone();
        this.MAX_SIZE = other.MAX_SIZE;
    }

    /**
     * @param cand the candidate to check
     * @return true if the candidate value sent is in the set
     */
    public boolean has(int cand){
        return bitset.get(cand);
    }

    /**
     * Sets the candidate value to true.
     *
     * @param cand the candidate to set to true
     */
    public void put(int cand){
        bitset.set(cand);
    }

    /**
     * Removes the candidate from the set.
     *
     * @param cand the candidate to remove
     */
    public void remove(int cand){
        bitset.clear(cand);
    }

    /**
     * @return the first allowed candidate in the set
     */
    public int getLowestValue(){
        return bitset.nextSetBit(1);
    }

    /**
     * @return a random allowed candidate in the set
     */
    public int getRandomValue(){
        int value = rand.nextInt(MAX_SIZE);
        value = bitset.nextSetBit(value);
        if (value == -1){
            return getLowestValue();
        }
        return value;
    }

    /**
     * @return the highest possible value the set can hold
     */
    public int getSize(){
        return MAX_SIZE;
    }

    /**
     * @return the number of candidates that are current on in the set
     */
    public int cardinality(){
        return bitset.cardinality();
    }

    /**
     * @return true if the candidate set has no possible candidates
     */
    public boolean isEmpty(){
        return bitset.isEmpty();
    }

    /**
     * @return string representation of the candidate set
     */
    @Override
    public String toString(){
        return bitset.toString();
    }

    /**
     * @return candidate value iterator
     */
    @Override
    public Iterator<Integer> iterator(){
        return new CandidateIterator(bitset);
    }

    /**
     * Provides iteration over the set values in the set.
     */
    private class CandidateIterator implements Iterator<Integer> {
        final BitSet set;
        int next;

        CandidateIterator(BitSet set){
            this.set = set;
            next = 0;
        }

        /**
         * Advances the next candidate, and returns true if there are more
         * useful values.
         *
         * @return true if there are more values in the set
         */
        @Override
        public boolean hasNext(){
            next = set.nextSetBit(next + 1);
            return next != -1;
        }

        /**
         * @return the next value
         */
        @Override
        public Integer next(){
            return next;
        }

        @Override
        public void remove(){
            throw new UnsupportedOperationException();
        }
    }
}
