package com.de801.aoc.d09;

public class Instruction {
	
	public static final int POSITION_MODE = 0;
	public static final int IMMEDIATE_MODE = 1;
	public static final int RELATIVE_MODE = 2;
	
	private String codeString;
	public int opcode;
	public int positionMode;
	public int parameterMode1;
	public int parameterMode2;
	public int parameterMode3;

	public Instruction() {
	};

	public Instruction(double code) {
		if (code == 99) {
			this.opcode = 99;
			return;
		}
		this.codeString = String.format("%05d", (int) code);
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
