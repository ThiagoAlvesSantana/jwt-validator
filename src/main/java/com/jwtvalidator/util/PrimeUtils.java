package com.jwtvalidator.util;

public class PrimeUtils {
	public static boolean isPrime(int number) {
		if (number <= 1) {
			return false;
		}
		if (number == 2) {
			return true;
		}
		if (number % 2 == 0) {
			return false;
		}
		int sqrt = (int) Math.sqrt(number);
		for (int i = 3; i <= sqrt; i += 2) {
			if (number % i == 0) {
				return false;
			}
		}
		return true;
	}
}