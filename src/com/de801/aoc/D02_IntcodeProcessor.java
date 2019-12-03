package com.de801.aoc;

import java.util.List;

import com.de801.aoc.utils.DEFileReader;

public class D02_IntcodeProcessor {

	public final static int OFFSET = 4;

	public final static int OP_ADD = 1;
	public final static int OP_MULT = 2;

	public Integer[] processInput(Integer[] code) {

		int i = 0;
		int opcode = 99;

		opcode = code[i];
		while (opcode != 99 && i < code.length) {
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
		if (action == OP_ADD) {
			// System.out.println(i + " + " + j + " = " + (i + j) + " in pos " +
			// pos3);
			code[pos3] = i + j;
		} else if (action == OP_MULT) {
			// System.out.println(i + " * " + j + " = " + (i * j) + " in pos " +
			// pos3);
			code[pos3] = i * j;
		} else {
			throw new RuntimeException("Uh oh");
		}

		// System.out.println("processOpcode output: " + code);

		return code;
	}

	public static void main(String[] args) {

		// Final inputs
		List<String> inputs = DEFileReader.getInputsFromFile("resources/d02_input");

		String intcode = inputs.get(0);

		String[] intcodeSplit = intcode.split(",");

		Integer[] code = new Integer[intcodeSplit.length];
		int i = 0;
		for (String s : intcodeSplit) {
			code[i] = Integer.valueOf(s);
			i++;
		}

		for (int noun = 0; noun < 100; noun++) {
			for (int verb = 0; verb < 100; verb++) {
				try {
					Integer[] newCode = code.clone();
					newCode[1] = noun;
					newCode[2] = verb;

					// System.out.println(Arrays.toString(newCode));

					D02_IntcodeProcessor ip = new D02_IntcodeProcessor();
					Integer[] output = ip.processInput(newCode);

					// System.out.println(Arrays.toString(output));

					if (output[0] == 19690720) {
						System.out.println("NOUN: " + noun + ", VERB: " + verb);
					}

				} catch (Exception e) {
					System.out.println("Not that one.");
				}
			}
		}

	}

}
