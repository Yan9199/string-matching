package bofa;

import java.util.ArrayList;
import java.util.List;

/**
 * This class realizes the algorithm of string matching BOFA using a pre-processed PartialMatchLengthUpdateValues object to search through a given source string.
 *
 * @param <T> The type of the letters, etc.
 */
public class StringMatcher<T> {
    /**
     * The update values to be used in the algorithm.
     */
    private final PartialMatchLengthUpdateValues<T> VALUES;

    /**
     * Constructs a new StringMatcher object with the given PartialMatchLengthUpdateValues object.
     *
     * @param values The update values for this object.
     */
    public StringMatcher(PartialMatchLengthUpdateValues<T> values) {
        VALUES = values;
    }

    /**
     * Finds and returns all indices at which an occurrence of the search string (pre-processed with the update values object) starts in the given source.
     *
     * @param source The source string to search through.
     * @return The list of calculated indices.
     */
    public List<Integer> findAllMatches(T[] source) {
        ArrayList<Integer> list = new ArrayList<>();
        if (source == null) return list;
        final int length = source.length, len = VALUES.getSearchStringLength();
        if (length == 0 || length < len || len == 0) return list;
        int currentState = 0;
        for (int i = 0; i < length; i++)
            if ((currentState = VALUES.getPartialMatchLengthUpdate(currentState, source[i])) == len)
                list.add(i - len + 2);
        return list;
    }
}
