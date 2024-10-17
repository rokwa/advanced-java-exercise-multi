package com.example.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class GenUtil {

    // Method to count occurrences of a regex pattern in a string
    public static int countOccurrences(Pattern pattern, String input) {
        Matcher matcher = pattern.matcher(input);
        int count = 0;
        while (matcher.find()) {
            count++;
        }
        return count;
    }
}
