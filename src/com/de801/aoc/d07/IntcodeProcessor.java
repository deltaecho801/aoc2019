package com.de801.aoc.d07;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class IntcodeProcessor implements Runnable {
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

	private List<String> inputs = Collections.synchronizedList(new ArrayList<String>());
	private List<String> outputs = Collections.synchronizedList(new ArrayList<String>());

	public Integer[] code;

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
			//System.out.println(this + " " + Arrays.toString(code));
			throw e;
		}

		return code;

	}

	private int getInput() throws Exception {
		while (inputs.size() == 0) {
		}
		
		synchronized (inputs) {

			//System.out.println(Thread.currentThread().getName() + ": Processing output");
			return Integer.valueOf(inputs.remove(0));
		}

	}

	public void addInput(String i) {
		//System.out.println(Thread.currentThread().getName() + ": Adding input " + i);
		inputs.add(i);
	}

	private void addOutput(int value) throws Exception {
		//System.out.println(Thread.currentThread().getName() + ": Adding output " + value );
		synchronized(outputs) {
			outputs.add(Integer.toString(value));
		}
	}

	private Integer getParameterValue(int offset, int mode) {
		if (mode == Instruction.IMMEDIATE_MODE) {
			return code[offset];
		} else {
			int pos1 = code[offset];
			return code[pos1];
		}
	}

	private int getParameterPosition(int offset, int mode) {
		if (mode == Instruction.IMMEDIATE_MODE) {
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
			k = getParameterPosition(offset + 3, Instruction.POSITION_MODE);

			code[k] = i + j;
			return offset += MATH_OFFSET;

		case OP_MULT:
			i = getParameterValue(offset + 1, instruction.parameterMode1);
			j = getParameterValue(offset + 2, instruction.parameterMode2);
			k = getParameterPosition(offset + 3, Instruction.POSITION_MODE);

			code[k] = i * j;
			return offset += MATH_OFFSET;

		case OP_INPUT:
			i = getParameterPosition(offset + 1, Instruction.POSITION_MODE);
			code[i] = getInput();
			return offset += RW_OFFSET;

		case OP_OUTPUT:
			i = getParameterPosition(offset + 1, instruction.parameterMode1);
			addOutput(code[i]);
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
			k = getParameterPosition(offset + 3, Instruction.POSITION_MODE);

			if (i < j) {
				code[k] = 1;
			} else {
				code[k] = 0;
			}
			return offset += MATH_OFFSET;

		case OP_EQUALS:
			i = getParameterValue(offset + 1, instruction.parameterMode1);
			j = getParameterValue(offset + 2, instruction.parameterMode2);
			k = getParameterPosition(offset + 3, Instruction.POSITION_MODE);

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

	public IntcodeProcessor(String intcode) {
		this(intcode, null);
	}

	public IntcodeProcessor(String intcode, String[] inputs) {
		if (inputs != null) {
			this.inputs.addAll(Arrays.asList(inputs));
		}

		String[] intcodeSplit = intcode.split(",");
		this.code = new Integer[intcodeSplit.length];
		int i = 0;
		for (String s : intcodeSplit) {
			code[i] = Integer.valueOf(s);
			i++;
		}
	}

	@Override
	public void run() {
		try {
			processInput(code);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getLastOutput() {
		synchronized(outputs) {
			return outputs.remove(0);
		}
	}

	public List<String> getOutputs() {
		synchronized(outputs) {
			return outputs;
		}
	}


}
