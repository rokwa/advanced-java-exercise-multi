package com.example.util;

import java.util.Scanner;
import java.util.InputMismatchException;
import java.util.Random;
import java.util.ArrayList;
import java.util.HashMap;

public final class RandomUtil {
	
	// Separate/group Util methods accdg. to use (InputUtil, RandomUtil, etc.)
	// Adjust variable names
	
	// Util class, contains common methods used throughout the program.
	private static Random random = new Random();

	// Generates random ASCII string
	public static String generateRandomStringWithLength(int length) {
		StringBuilder sb = new StringBuilder(length);
		for (int i = 0; i < length; i++){
			char randomChar;
			do{
				randomChar = (char) (random.nextInt(94) + 33);
			}while(randomChar == ' ' || randomChar == ',');
			sb.append(randomChar);
		}
		return sb.toString();
	}
}
