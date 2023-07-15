package bofa;

/**
 * A class that represents string matching BOFA using an intern matrix.
 *
 * @param <T> The type of the function/letters of the used alphabet.
 */
public class PartialMatchLengthUpdateValuesAsMatrix<T> extends PartialMatchLengthUpdateValues<T> {
    /**
     * The matrix of this object in which the lookup-table is implemented.
     */
    private int[][] matrix;

    /**
     * Constructs a PartialMatchLengthUpdateValuesAsMatrix object with the given function and search string.
     * This is done by creating the private matrix of this object so that it may be used to look up next possible states.
     *
     * @param fct          The function to be used.
     * @param searchString The search string to be used.
     */
    public PartialMatchLengthUpdateValuesAsMatrix(FunctionToInt<T> fct, T[] searchString) {
        super(fct);
        final int size = fct.sizeOfAlphabet(), len = searchString.length, k = computePartialMatchLengthUpdateValues(searchString);
        matrix = new int[len + 1][size];
        if (len == 0) return;
        final T[] arrayWithDistinctLetters = buildArrayWithDistinctLetters(fct, searchString);
        for (int i = 0, state = 1;; i++, state++) {
            if (i == len) {
                if (k == 0) for (T t : arrayWithDistinctLetters) matrix[i][fct.apply(t)] = buildAndCompute(searchString, t, i, this);
                else for (int j = 0; j < size; j++) matrix[i][j] = matrix[k][j];
                return;
            }
            final int currentLetter = fct.apply(searchString[i]);
            for (T t : arrayWithDistinctLetters) {
                final int tAsInt = fct.apply(t);
                if (tAsInt == currentLetter) continue;
                matrix[i][tAsInt] = buildAndCompute(searchString, t, i, this);
            }
            matrix[i][currentLetter] = state;
        }
    }

    @SuppressWarnings("unchecked")
    private static <T> int buildAndCompute(T[] searchString, T letter, int index, PartialMatchLengthUpdateValues<T> values) {
        final T[] array = (T[]) new Object[index + 1];
        for (int i = 0; i < index; i++) array[i] = searchString[i];
        array[index] = letter;
        return values.computePartialMatchLengthUpdateValues(array);
    }

    @SuppressWarnings("unchecked")
    private static <T> T[] buildArrayWithDistinctLetters(FunctionToInt<T> fct, T[] searchString) {
        int len = searchString.length, counter = 0;
        final T[] array = (T[]) new Object[len];
        for (T letter : searchString) if (!containsElement(fct, array, fct.apply(letter))) array[counter++] = letter;
        return getArrayCopy(array, counter);
    }

    private static <T> boolean containsElement(FunctionToInt<T> fct, T[] array, int letter) {
        for (T t : array) {
            if (t == null) return false;
            if (fct.apply(t) == letter) return true;
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    private static <T> T[] getArrayCopy(T[] array, int counter) {
        final T[] a = (T[]) new Object[counter];
        int index = 0;
        for (T t : array) {
            if (t == null) break;
            a[index++] = t;
        }
        return a;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getPartialMatchLengthUpdate(int state, T letter) {
        return matrix[state][fct.apply(letter)];
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getSearchStringLength() {
        return matrix.length - 1;
    }
}
