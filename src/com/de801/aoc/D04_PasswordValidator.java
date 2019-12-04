package com.de801.aoc;

import java.util.HashSet;
import java.util.Set;

public class D04_PasswordValidator {
	
	public static boolean isValidPassword(int password) {
		boolean valid = true;
		
		String sPassword = Integer.toString(password);
		
		Set<Boolean> allDoubled = new HashSet<Boolean>();
		Boolean doubled = false;
		int repeatCount = 0;
		char prevChar = '/';
		
		int i = 0;
		for(char c : sPassword.toCharArray()) {
//			System.out.println("Char: " + c);
			if(c < prevChar)
				return false;
			
			if(c == prevChar) {
				repeatCount++;
				if(repeatCount > 1) {
					doubled = false;
//					System.out.println("Matches too long");
				} else {
					doubled = true;
//					System.out.println("Doubled");
					if(i == sPassword.length() - 1) {
						allDoubled.add(true);
					}

				}
			} else {
				if(doubled == true) {
					allDoubled.add(true);
//					System.out.println("Double success");
				}
				
				doubled = false;
				repeatCount = 0;
			}
			
			prevChar = c;
			i++;
		}
		
		return valid && (allDoubled.size() > 0);
	}
	
	public static void main(String[] args) {
		
		int count = 0;
		
//		System.out.println(isValidPassword(111111));
//		System.out.println(isValidPassword(223450));
//		System.out.println(isValidPassword(123789));
		
//		System.out.println(isValidPassword(112233));
//		System.out.println(isValidPassword(123444));
//		System.out.println(isValidPassword(111122));
		
		for(int i = 183564; i <= 657474; i++) {
			if(isValidPassword(i))
				count++;
		}
		
		
		
		System.out.println(count);
	}

}
