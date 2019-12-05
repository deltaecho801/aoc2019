package com.de801.aoc;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

import com.de801.aoc.utils.DEFileReader;

public class D05_ExpandedIntcode {
	public final static int MATH_OFFSET = 4;
	public final static int RW_OFFSET = 2;
	public final static int JUMP_OFFSET = 3;

	public final static int OP_ADD = 1;
	public final static int OP_MULT = 2;
	public final static int OP_INPUT = 3;
	public final static int OP_OUTPUT = 4;
	public final static int OP_JUMP_IF_TRUE = 5;
	public final static int OP_JUMP_IF_FALSE = 6;
	public final static int OP_LESS_THAN = 7;
	public final static int OP_EQUALS = 8;

	public static final int POSITION_MODE = 0;
	public static final int IMMEDIATE_MODE = 1;

	public Integer[] code;

	public class Instruction {
		private String codeString;
		public int opcode;
		public int positionMode;
		public int parameterMode1;
		public int parameterMode2;
		public int parameterMode3;

		public Instruction() {
		};

		public Instruction(int code) {
			if (code == 99) {
				this.opcode = 99;
				return;
			}
			this.codeString = String.format("%05d", code);
			this.opcode = Integer.parseInt(codeString.substring(4, 5));
			this.positionMode = Integer.parseInt(codeString.substring(3, 4));
			this.parameterMode1 = Integer.parseInt(codeString.substring(2, 3));
			this.parameterMode2 = Integer.parseInt(codeString.substring(1, 2));
			this.parameterMode3 = Integer.parseInt(codeString.substring(0, 1));
		}

		@Override
		public String toString() {
			return codeString;
		}
	}

	public Integer[] processInput(Integer[] inputCode) throws Exception {

		this.code = inputCode;
		int offset = 0;

		Instruction instruction = new Instruction(code[offset]);

		try {
			while (instruction.opcode != 99 && offset < code.length) {
				offset = processOpcode(instruction, offset);
				instruction = new Instruction(code[offset]);
			}
		} catch (Exception e) {
			System.out.println(Arrays.toString(code));
			throw e;
		}

		return code;

	}

	private int getInput() throws Exception {
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("INPUT: ");
		String value = reader.readLine();
		return Integer.valueOf(value);
	}

	private Integer getParameterValue(int offset, int mode) {
		if (mode == IMMEDIATE_MODE) {
			return code[offset];
		} else {
			int pos1 = code[offset];
			return code[pos1];
		}
	}

	private int getParameterPosition(int offset, int mode) {
		if (mode == IMMEDIATE_MODE) {
			return offset;
		} else {
			return code[offset];
		}
	}

	private int processOpcode(Instruction instruction, int offset) throws Exception {
		int i;
		int j;
		int k;

		switch (instruction.opcode) {
		case OP_ADD:
			i = getParameterValue(offset + 1, instruction.parameterMode1);
			j = getParameterValue(offset + 2, instruction.parameterMode2);
			k = getParameterPosition(offset + 3, POSITION_MODE);

			code[k] = i + j;
			return offset += MATH_OFFSET;

		case OP_MULT:
			i = getParameterValue(offset + 1, instruction.parameterMode1);
			j = getParameterValue(offset + 2, instruction.parameterMode2);
			k = getParameterPosition(offset + 3, POSITION_MODE);

			code[k] = i * j;
			return offset += MATH_OFFSET;

		case OP_INPUT:
			i = getParameterPosition(offset + 1, POSITION_MODE);
			code[i] = getInput();
			return offset += RW_OFFSET;

		case OP_OUTPUT:
			i = getParameterPosition(offset + 1, instruction.parameterMode1);
			System.out.println("OUTPUT: " + code[i]);
			return offset += RW_OFFSET;

		case OP_JUMP_IF_TRUE:
			i = getParameterValue(offset + 1, instruction.parameterMode1);
			j = getParameterValue(offset + 2, instruction.parameterMode2);

			if (i != 0) {
				return j;
			} else {
				return offset += JUMP_OFFSET;
			}

		case OP_JUMP_IF_FALSE:
			i = getParameterValue(offset + 1, instruction.parameterMode1);
			j = getParameterValue(offset + 2, instruction.parameterMode2);

			if (i == 0) {
				return j;
			} else {
				return offset += JUMP_OFFSET;
			}

		case OP_LESS_THAN:
			i = getParameterValue(offset + 1, instruction.parameterMode1);
			j = getParameterValue(offset + 2, instruction.parameterMode2);
			k = getParameterPosition(offset + 3, POSITION_MODE);

			if (i < j) {
				code[k] = 1;
			} else {
				code[k] = 0;
			}
			return offset += MATH_OFFSET;

		case OP_EQUALS:
			i = getParameterValue(offset + 1, instruction.parameterMode1);
			j = getParameterValue(offset + 2, instruction.parameterMode2);
			k = getParameterPosition(offset + 3, POSITION_MODE);

			if (i == j) {
				code[k] = 1;
			} else {
				code[k] = 0;
			}
			return offset += MATH_OFFSET;

		default:
			throw new RuntimeException("Invalid instruction: " + instruction.toString());
		}
	}

	public static void main(String[] args) throws Exception {

		// Test inputs
		// String intcode = "3,12,6,12,15,1,13,14,13,4,13,99,-1,0,1,9";

		// Final inputs
		List<String> inputs = DEFileReader.getInputsFromFile("resources/d05_input");
		String intcode = inputs.get(0);

		String[] intcodeSplit = intcode.split(",");
		Integer[] code = new Integer[intcodeSplit.length];
		int i = 0;
		for (String s : intcodeSplit) {
			code[i] = Integer.valueOf(s);
			i++;
		}

		D05_ExpandedIntcode ip = new D05_ExpandedIntcode();
		Integer[] output = ip.processInput(code);
	}

}
