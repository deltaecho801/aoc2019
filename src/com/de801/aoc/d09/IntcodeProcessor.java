package com.de801.aoc.d09;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.TreeMap;
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
	public final static int OP_ADJUST_REL_BASE = 9;
	
	private int relativeBase = 0;

	private List<String> inputs = Collections.synchronizedList(new ArrayList<String>());
	private List<String> outputs = Collections.synchronizedList(new ArrayList<String>());

	public TreeMap<Integer, Long> code;

	public TreeMap<Integer, Long> processInput(TreeMap<Integer, Long> inputCode) throws Exception {

		this.code = inputCode;
		int offset = 0;

		Instruction instruction = new Instruction(getCodeAtOffset(offset));

		try {
			while (instruction.opcode != 99 && offset < code.size()) {
				offset = processOpcode(instruction, offset);
				instruction = new Instruction(getCodeAtOffset(offset));
			}
		} catch (Exception e) {
			//System.out.println(this + " " + Arrays.toString(code));
			throw e;
		}

		return code;

	}

	private long getInput() throws Exception {
		if (inputs.size() == 0) {
			throw new RuntimeException("No inputs in queue");
		}

		synchronized (inputs) {

			return Long.valueOf(inputs.remove(0));
		}

	}

	public void addInput(String i) {
		System.out.println(Thread.currentThread().getName() + ": Adding input " + i);
		inputs.add(i);
	}

	private void addOutput(long value) throws Exception {
		System.out.println(Thread.currentThread().getName() + ": Adding output " + value );
		synchronized(outputs) {
			outputs.add(Long.toString(value));
		}
	}

	private Long getParameterValue(int offset, int mode) {
		if (mode == Instruction.IMMEDIATE_MODE) {
			return getCodeAtOffset(offset);
		} else if (mode == Instruction.RELATIVE_MODE) {
			int relOffset = relativeBase + ((int) getCodeAtOffset(offset));

			return getCodeAtOffset(relOffset);
		} else {
			long pos1 = getCodeAtOffset(offset);
			return getCodeAtOffset((int) pos1);
		}
	}

	private long getParameterPosition(int offset, int mode) {
		if (mode == Instruction.IMMEDIATE_MODE) {
			return offset;
		} else if (mode == Instruction.RELATIVE_MODE) {

			int relOffset = relativeBase + ((int) getCodeAtOffset(offset));
			
			return relOffset;
		} else {
			return getCodeAtOffset(offset);
		}
	}

	private int processOpcode(Instruction instruction, int offset) throws Exception {
		long i;
		long j;
		long k;

		switch (instruction.opcode) {
		case OP_ADD:
			i = getParameterValue(offset + 1, instruction.parameterMode1);
			j = getParameterValue(offset + 2, instruction.parameterMode2);
			k = getParameterPosition(offset + 3, instruction.parameterMode3);

			code.put((int) k, i + j);
			return offset += MATH_OFFSET;

		case OP_MULT:
			i = getParameterValue(offset + 1, instruction.parameterMode1);
			j = getParameterValue(offset + 2, instruction.parameterMode2);
			k = getParameterPosition(offset + 3, instruction.parameterMode3);

			code.put((int) k, i * j);
			return offset += MATH_OFFSET;

		case OP_INPUT:
			i = getParameterPosition(offset + 1, instruction.parameterMode1);
			code.put((int) i, getInput());
			return offset += RW_OFFSET;

		case OP_OUTPUT:
			i = getParameterPosition(offset + 1, instruction.parameterMode1);
			addOutput(getCodeAtOffset((int) i));
			return offset += RW_OFFSET;

		case OP_JUMP_IF_TRUE:
			i = getParameterValue(offset + 1, instruction.parameterMode1);
			j = getParameterValue(offset + 2, instruction.parameterMode2);

			if (i != 0) {
				return (int) j;
			} else {
				return offset += JUMP_OFFSET;
			}

		case OP_JUMP_IF_FALSE:
			i = getParameterValue(offset + 1, instruction.parameterMode1);
			j = getParameterValue(offset + 2, instruction.parameterMode2);

			if (i == 0) {
				return (int) j;
			} else {
				return offset += JUMP_OFFSET;
			}

		case OP_LESS_THAN:
			i = getParameterValue(offset + 1, instruction.parameterMode1);
			j = getParameterValue(offset + 2, instruction.parameterMode2);
			k = getParameterPosition(offset + 3, instruction.parameterMode3);

			if (i < j) {
				code.put((int) k, (long) 1);
			} else {
				code.put((int) k, (long) 0);
			}
			return offset += MATH_OFFSET;

		case OP_EQUALS:
			i = getParameterValue(offset + 1, instruction.parameterMode1);
			j = getParameterValue(offset + 2, instruction.parameterMode2);
			k = getParameterPosition(offset + 3, instruction.parameterMode3);

			if (i == j) {
				code.put((int) k, (long) 1);
			} else {
				code.put((int) k, (long) 0);
			}
			return offset += MATH_OFFSET;
			
		case OP_ADJUST_REL_BASE:
			i = getParameterValue(offset + 1, instruction.parameterMode1);
			this.relativeBase += (int) i;
			return offset += RW_OFFSET;

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
		this.code = new TreeMap<Integer, Long>();
		int i = 0;
		for (String s : intcodeSplit) {
			code.put(i, Long.valueOf(s.trim()));
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
	
	public long getCodeAtOffset(int offset) {
		if(offset < 0)
			throw new RuntimeException("Attempting to access negative memory address " + offset);
		return code.getOrDefault(offset, (long) 0);
	}
	
	public String getCodeString() {
		return(code.values().toString());
	}
	
}
