package bofa;

import java.util.List;

/**
 * A class that represents a function with a given alphabet.
 */
public class SelectionOfCharsIndex implements FunctionToInt<Character> {
    /**
     * The chars of the objects' alphabet.
     */
    private final char[] theChars;

    /**
     * Constructs a new SelectionOfCharsIndex object with the given alphabet scope.
     * The given alphabet must not be null, has to contain at least one element and each element has to be unique.
     *
     * @param theAlphabet The given alphabet.
     */
    public SelectionOfCharsIndex(List<Character> theAlphabet) {
        theChars = new char[theAlphabet.size()];
        int i = 0; for (Character c : theAlphabet) {theChars[i] = c; i++;}
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int sizeOfAlphabet() {
        return theChars.length;
    }

    /**
     * Returns the index at which the given parameter is contained in the alphabet.
     *
     * @param character The given parameter to be searched for.
     * @return The index of the given parameter.
     * @throws IllegalArgumentException Iff the given parameter is not contained in the alphabet.
     */
    @Override
    public int apply(Character character) throws IllegalArgumentException {
        int i = 0; for (Character c : theChars) {if (c.equals(character)) return i; i++;}
        throw new IllegalArgumentException();
    }
}
