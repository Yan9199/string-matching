package bofa;

import java.util.ArrayList;
import java.util.List;

/**
 * A class that represents string matching BOFA using an intern array of lists of transitions -
 * basically an array containing the states and their possible transitions to other states.
 *
 * @param <T> The type of the function/letters of the used alphabet.
 */
public class PartialMatchLengthUpdateValuesAsAutomaton<T> extends PartialMatchLengthUpdateValues<T> {
    /**
     * The states of the automaton as a list of its transitions.
     */
    private List<Transition<T>>[] theStates;

    /**
     * Constructs a PartialMatchLengthUpdateValuesAsAutomaton object with the given function and search string.
     * This is done by creating the private array of this object by creating the various lists and their possible transitions to other states.
     *
     * @param fct          The function to be used.
     * @param searchString The search string to be used.
     */
    @SuppressWarnings("unchecked")
    public PartialMatchLengthUpdateValuesAsAutomaton(FunctionToInt<T> fct, T[] searchString) {
        super(fct);
        final int len = searchString.length, k = computePartialMatchLengthUpdateValues(searchString);
        theStates = (ArrayList<Transition<T>>[]) new ArrayList[len + 1];
        if (len == 0) {theStates[0] = new ArrayList<>(); return;}
        final T[] arrayWithDistinctLetters = buildArrayWithDistinctLetters(fct, searchString);
        for (int i = 0;; i++) {
            theStates[i] = new ArrayList<>();
            if (i == len) {
                if (k == 0) {
                    ArrayList<T> letters = new ArrayList<>();
                    letters.add(searchString[0]);
                    theStates[i].add(new Transition<>(1, letters));
                    return;
                }
                List<Transition<T>> state = theStates[i];
                for (Transition<T> tr : theStates[k]) {
                    ArrayList<T> letters = new ArrayList<>();
                    for (T t : tr.LETTERS) letters.add(t);
                    state.add(new Transition<>(tr.J, letters));
                }
                return;
            }
            final int currentLetter = fct.apply(searchString[i]);
            for (T t : arrayWithDistinctLetters) {
                if (fct.apply(t) == currentLetter) continue;
                final int value = buildAndCompute(searchString, t, i, this);
                if (value != 0) {
                    ArrayList<T> letters2 = new ArrayList<>();
                    letters2.add(t);
                    theStates[i].add(new Transition<>(value, letters2));
                }
            }
            ArrayList<T> letters = new ArrayList<>();
            letters.add(searchString[i]);
            theStates[i].add(new Transition<>(i + 1, letters));
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
        final int letterAsInt = fct.apply(letter);
        for (Transition<T> tr : theStates[state]) for (T t : tr.LETTERS) if (fct.apply(t) == letterAsInt) return tr.J;
        return 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getSearchStringLength() {
        return theStates.length - 1;
    }
}
