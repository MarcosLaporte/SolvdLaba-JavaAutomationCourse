package services;

import org.apache.commons.lang3.StringUtils;

import java.util.HashSet;
import java.util.Set;

public class StringService {

    public static int countUniqueWordsInText(String text) {
        if (StringUtils.isBlank(text))
            return 0;

        text = StringUtils.lowerCase(text);
        String[] wordsInText = StringUtils.split(text, " ,.;!?-()[]{}<>\"'\n\t");
        Set<String> uniqueWords = new HashSet<>();

        for (String word : wordsInText) {
            if (StringUtils.isNotBlank(word))
                uniqueWords.add(word);
        }

        return uniqueWords.size();
    }

    public static int countLettersInText(String text) {
        if (StringUtils.isBlank(text))
            return 0;

        int count = 0;
        char[] characters = text.toCharArray();
        for (char c : characters) {
            if (StringUtils.isAlpha(String.valueOf(c)))
                count++;
        }

        return count;
//        return RegExUtils.removeAll(text, "[^a-zA-Z]").length();
    }

    public static int countGivenWordInText(String text, String word) {
        if (StringUtils.isBlank(text))
            return 0;

        int count = 0;
        String[] wordsInText = StringUtils.split(text, " ,.;!?-()[]{}<>\"'\n\t");

        for (String wordT : wordsInText) {
            if (StringUtils.compareIgnoreCase(wordT, word) == 0)
                count++;
        }

        return count;
    }
}
