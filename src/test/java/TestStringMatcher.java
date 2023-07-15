import bofa.PartialMatchLengthUpdateValuesAsAutomaton;
import bofa.PartialMatchLengthUpdateValuesAsMatrix;
import bofa.StringMatcher;
import bofa.UnicodeNumberOfCharIndex;
import simple.SimpleStringMatcher
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestStringMatcher {

    @Test
    void testStringMatcher() {
        String sourceText = "Lorem anna ipsum dolor sit amet, AnnA consectetur adipiscing elit," +
                "sed do eiusmod tempor anNA incididunt ut labore ana et dolore anna magna aliqua.";

        String searchText = "anna";

        int source_len = sourceText.length();
        int search_len = searchText.length();

        Character[] source = new Character[source_len];
        Character[] search = new Character[search_len];

        for (int i = 0; i < source_len; i++) source[i] = sourceText.charAt(i);
        for (int i = 0; i < search_len; i++) search[i] = searchText.charAt(i);

        UnicodeNumberOfCharIndex u = new UnicodeNumberOfCharIndex();

        PartialMatchLengthUpdateValuesAsAutomaton<Character> partialAsAutomaton = new PartialMatchLengthUpdateValuesAsAutomaton<>(u, search);
        PartialMatchLengthUpdateValuesAsMatrix<Character> partialAsMatrix = new PartialMatchLengthUpdateValuesAsMatrix<>(u, search);

        StringMatcher<Character> stringMatcherAutomaton = new StringMatcher<>(partialAsAutomaton);
        StringMatcher<Character> stringMatcherMatrix = new StringMatcher<>(partialAsMatrix);

        List<Integer> resultAutomaton = stringMatcherAutomaton.findAllMatches(source);
        List<Integer> resultMatrix = stringMatcherMatrix.findAllMatches(source);
        List<Integer> resultSimple = SimpleStringMatcher.findAllMatches(sourceText, searchText);

        Integer[] expected = {7, 129};

        assertArrayEquals(expected, resultAutomaton.toArray());
        assertArrayEquals(expected, resultMatrix.toArray());
        assertArrayEquals(expected, resultSimple.toArray());
    }
}
