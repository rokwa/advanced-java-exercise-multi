package com.example.util;

import java.util.Scanner;
import java.util.InputMismatchException;
import java.util.Random;
import java.util.ArrayList;
import java.util.HashMap;
import org.apache.commons.lang3.StringUtils;

public final class InputUtil {
	
	// Separate/group Util methods accdg. to use (InputUtil, RandomUtil, etc.)
	// Adjust variable names
	
	// Util class, contains common methods used throughout the program.
    private static Scanner sc = new Scanner(System.in);

	public static int askForInt(String prompt) {
        while(true) {
            System.out.print(prompt);
            String input = sc.next();
            if (StringUtils.isNumeric(input)) {
                return Integer.parseInt(input);
            } else {
                System.out.println("Invalid input. Please enter a valid integer.");
            }
        }
    }

	// Asks for String input
    public static String askForString(String prompt) {
        System.out.print(prompt);
        return sc.next();
    }
}
