package com.de801.aoc;

import java.util.List;

import com.de801.aoc.utils.DEFileReader;

public class D01_FuelModule {

	public int calculateFuel(int weight) {
		int result = (Math.floorDiv(weight, 3) - 2);
		if(result < 0) 
			result = 0;
//		System.out.println("calculateFuel: " + result);
		return result;
	}
	
	public int calculateTotalFuel(int weight) {
		if(weight == 0)
			return 0;
		
		int fuel = calculateFuel(weight);
		
		return fuel + calculateTotalFuel(fuel);
	}
	
	public int calculateModuleFuel(int[] mods) {
		int result = 0;
		for(int w : mods) {
			int fuel = calculateTotalFuel(w);
			result += fuel;
		}
//		System.out.println("calculateModuleFuel: " + result);
		return result;
	}
	
	public static void main(String[] args) {
		
		D01_FuelModule fm = new D01_FuelModule();
		
		//Tests
//		System.out.println(fm.calculateTotalFuel(12));
//		System.out.println(fm.calculateTotalFuel(14));
//		System.out.println(fm.calculateTotalFuel(1969));
//		System.out.println(fm.calculateTotalFuel(100756));
		
		//Final inputs
		List<String> inputs = DEFileReader.getInputsFromFile("resources/d01_input");
		int[] mods = new int[inputs.size()];
		for(int i = 0; i < inputs.size(); i++) {
			mods[i] = Integer.valueOf(inputs.get(i));
		}
		
		System.out.println(fm.calculateModuleFuel(mods));
	}
	
}
