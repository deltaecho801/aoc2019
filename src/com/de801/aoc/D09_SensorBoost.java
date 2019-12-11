package com.de801.aoc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.de801.aoc.d09.IntcodeProcessor;
import com.de801.aoc.utils.DEFileReader;

public class D09_SensorBoost {

	public static void main(String[] args) throws Exception {
		List<String> code = DEFileReader.getInputsFromFile("resources/d09_input");
		String intcode = code.get(0);
		
//		intcode = "1102,34915192,34915192,7,4,7,99,0";

		IntcodeProcessor ip = new IntcodeProcessor(intcode);
		ip.addInput("2");
		ip.run();
		
		System.out.println(ip.getOutputs());
		System.out.println(ip.code);
		
	}
}
