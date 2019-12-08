package com.de801.aoc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.de801.aoc.d07.IntcodeProcessor;
import com.de801.aoc.utils.DEFileReader;

public class D07_AmplificationCircuit {

	public static void main(String[] args) throws Exception {
		List<String> code = DEFileReader.getInputsFromFile("resources/d07_input");
		String intcode = code.get(0);

//		String intcode = "3,15,3,16,1002,16,10,16,1,16,15,15,4,15,99,0,0";

//		PermutationStation part1 = new PermutationStation();
//		String[] a = {"0", "1", "2", "3", "4"};
//		part1.generate(a.length, a);
		
		PermutationStation part2 = new PermutationStation();
		String[] b = {"5", "6", "7", "8", "9"};
		part2.generate(b.length, b);

		ArrayList<String[]> tmp = new ArrayList<String[]>();
		tmp.add(new String[]{"4", "3", "2", "1", "0"});
		
		ArrayList<Integer> outputs = new ArrayList<Integer>();
		
		for(String[] inputs : part2.permutations) {
			System.out.println("Processing " + Arrays.toString(inputs));
			String result = "0";
			ArrayList<IntcodeProcessor> ipArray = new ArrayList<IntcodeProcessor>();
			ArrayList<Thread> threadArray = new ArrayList<Thread>();
			
			for(int i = 0; i < 5; i++) {
				ipArray.add(i, new IntcodeProcessor(intcode));
				ipArray.get(i).addInput(inputs[i]);
				Thread t = new Thread(ipArray.get(i));
				t.start();
				threadArray.add(t);
			}
			
			ipArray.get(0).addInput("0");
			
			while(threadArray.get(4).isAlive()) {
				for(int i = 0; i < 5; i++) {
					IntcodeProcessor ip = ipArray.get(i);
					if(ip.getOutputs().size() > 0) {
						result = ip.getLastOutput();
						ipArray.get(i == 4 ? 0 : i + 1).addInput(result);
					}
				}
				Thread.sleep(100);
			}
			
			for(Thread t : threadArray) {
				t.join();
			}
			
			//			System.out.println(Arrays.toString(result));
			outputs.add(Integer.valueOf(ipArray.get(4).getLastOutput()));
		}
		System.out.println("Max value: " + Collections.max(outputs));
	}

	public static class PermutationStation {
		
		ArrayList<String[]> permutations = new ArrayList<String[]>();
		
		public void generate(int k, String[] a) {
			if(k == 1) {
				permutations.add(Arrays.copyOf(a, a.length));
			} else {
				generate(k-1, a);

				for (int i = 0; i < k-1; i++) {
					if(k % 2 == 0)
						swap(a, i, k-1);
					else
						swap(a, 0, k-1);
					generate(k-1, a);
					
				}
			}
		}
		
		public void swap(String[] a, int i, int j) {
			String t = a[i];
			a[i] = a[j];
			a[j] = t;
		}
	}

}
