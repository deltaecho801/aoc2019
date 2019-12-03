package com.de801.aoc;

public class D02_IntcodeProcessor {
	
	public final static int OFFSET = 4;
	
	public final static int OP_ADD = 1;
	public final static int OP_MULT = 2;
	
	public Integer[] processInput(Integer[] code) {
		
		int i = 0;
		int opcode = 99;
		
		opcode = code[i];
		while(opcode != 99 && i < code.length) {
			processOpcode(i, code);
			i += OFFSET;
			opcode = code[i];
		}
		
		return code;
				
	}
	
	public Integer[] processOpcode(Integer opcodePos, Integer[] code) {
		Integer action = code[opcodePos];
		Integer pos1 = code[opcodePos + 1];
		Integer pos2 = code[opcodePos + 2];
		Integer pos3 = code[opcodePos + 3];
		
		int i = code[pos1];
		int j = code[pos2];
		if(action == OP_ADD) {
//			System.out.println(i + " + " + j + " = " + (i + j) + " in pos " + pos3);
			code[pos3] =  i + j;
		} else if (action == OP_MULT) {
//			System.out.println(i + " * " + j + " = " + (i * j) + " in pos " + pos3);
			code[pos3] =  i * j;
		} else {
			throw new RuntimeException("Uh oh");
		}
		
//		System.out.println("processOpcode output: " + code);
		
		return code;
	}
	
	public static void main(String[] args) {
		
		String intcode = "1,0,0,3,1,1,2,3,1,3,4,3,1,5,0,3,2,13,1,19,1,19,10,23,2,10,23,27,1,27,6,31,1,13,31,35,1,13,35,39,1,39,10,43,2,43,13,47,1,47,9,51,2,51,13,55,1,5,55,59,2,59,9,63,1,13,63,67,2,13,67,71,1,71,5,75,2,75,13,79,1,79,6,83,1,83,5,87,2,87,6,91,1,5,91,95,1,95,13,99,2,99,6,103,1,5,103,107,1,107,9,111,2,6,111,115,1,5,115,119,1,119,2,123,1,6,123,0,99,2,14,0,0";
		String[] intcodeSplit = intcode.split(",");
		
		Integer[] code = new Integer[intcodeSplit.length];
		int i = 0;
		for(String s : intcodeSplit) {
			code[i] = Integer.valueOf(s);
			i++;
		}
		
		for(int noun = 0; noun < 100; noun++) {
			for(int verb = 0; verb < 100; verb++) {
				try {
					Integer[] newCode = code.clone();
					newCode[1] = noun;
					newCode[2] = verb;
					
//				    System.out.println(Arrays.toString(newCode));
					
					D02_IntcodeProcessor ip = new D02_IntcodeProcessor();
				    Integer[] output = ip.processInput(newCode);
				    
//				    System.out.println(Arrays.toString(output));
				    
				    if(output[0] == 19690720) {
				    	System.out.println("NOUN: " + noun + ", VERB: " + verb);
				    }
				    
				} catch (Exception e) {
					System.out.println("Not that one.");
				}
			}
		}
			
	}

}
