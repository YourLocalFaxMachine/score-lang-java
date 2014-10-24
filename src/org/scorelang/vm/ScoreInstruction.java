package org.scorelang.vm;

public class ScoreInstruction {
	
	private byte _op;
	private int _arg0, _arg1, _arg2, _arg3;
	
	public ScoreInstruction(byte op, int arg0, int arg1, int arg2, int arg3) {
		_op = op;
		_arg0 = arg0;
		_arg1 = arg1;
		_arg2 = arg2;
		_arg3 = arg3;
	}
	
	public ScoreInstruction(byte op, int arg0, int arg1, int arg2) {
		_op = op;
		_arg0 = arg0;
		_arg1 = arg1;
		_arg2 = arg2;
	}
	
	public ScoreInstruction(byte op, int arg0, int arg1) {
		_op = op;
		_arg0 = arg0;
		_arg1 = arg1;
	}
	
	public ScoreInstruction(byte op, int arg0) {
		_op = op;
		_arg0 = arg0;
	}
	
	public ScoreInstruction(byte op) {
		_op = op;
	}
	
	// Getters/Setters
	
	public byte getOp() {
		return _op;
	}
	
	public int getArg0() {
		return _arg0;
	}
	
	public void setArg0(int arg0) {
		_arg0 = arg0;
	}
	
	public int getArg1() {
		return _arg1;
	}
	
	public void setArg1(int arg1) {
		_arg1 = arg1;
	}
	
	public int getArg2() {
		return _arg2;
	}
	
	public void setArg2(int arg2) {
		_arg2 = arg2;
	}
	
	public int getArg3() {
		return _arg3;
	}
	
	public void setArg3(int arg3) {
		_arg3 = arg3;
	}
	
}