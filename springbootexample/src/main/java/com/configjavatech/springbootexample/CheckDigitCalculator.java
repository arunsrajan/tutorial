package com.configjavatech.springbootexample;

public class CheckDigitCalculator {

	public static void main(String[] args) {
		String code = "426206892613";
		int sumeven = 0;
		int sumodd = 0;
		int sum;
		for (int i = 1; i <= 12; i++) {
			if (i % 2 == 1) {
				sumodd+=Integer.valueOf(""+code.charAt(i-1));
			} else {
				sumeven += Integer.valueOf(""+code.charAt(i-1));
			}
		}
		sum = sumeven * 3 + sumodd;
		int lastdigit_13 = 10 - sum % 10;
		code = code+lastdigit_13;
		System.out.println(code);
	}

}
