package simple;

import java.util.ArrayList;
import java.util.List;

/**
 * This class implements a simple string matcher.
 */
public class SimpleStringMatcher {

    public static List<Integer> findAllMatches(String sourceText, String searchText) {
        if (sourceText == null || searchText == null) return null;

        int sourceLength = sourceText.length();
        int searchLength = searchText.length();

        if (searchLength == 0 || searchLength > sourceLength) return new ArrayList<>();

        ArrayList<Integer> listOfIndices = new ArrayList<>(sourceLength - searchLength + 1);
        ArrayList<Integer> listOfCandidates = new ArrayList<>(sourceLength);

        char firstCharacter = searchText.charAt(0);

        for (int i = 0; i < sourceLength; i++) {
            final char currentCharacter = sourceText.charAt(i);
            if (firstCharacter == currentCharacter) listOfCandidates.add(i + 1);
            for (int index = 0, counter = listOfCandidates.size(); counter > 0; counter--) {
                int n = listOfCandidates.get(index);
                int e = i - n + 1;
                if (searchText.charAt(e) == currentCharacter) {
                    if (e + 1 == searchLength) listOfIndices.add(listOfCandidates.remove(index));
                    else index++;
                } else listOfCandidates.remove(index);
            }
        }
        return listOfIndices;
    }
}
