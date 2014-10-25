package org.scorelang.function;

import org.scorelang.object.ScoreObject;
import org.scorelang.util.ScoreVector;
import org.scorelang.value.ScoreValue;
import org.scorelang.vm.ScoreInstruction;

public class ScoreFunctionState {
	
	private ScoreVector<ScoreInstruction> _inst;
	private ScoreVector<ScoreObject> _values;
	
	private int _params;
	
	public ScoreFunctionState() {
		this(0);
	}
	
	public ScoreFunctionState(int params) {
		_inst = new ScoreVector<ScoreInstruction>();
		_values = new ScoreVector<ScoreObject>();
		_params = params;
	}
	
	// Build
	
	public ScoreFunctionProto buildProto() {
		ScoreFunctionProto res = new ScoreFunctionProto(_params);
		
		res.setValues(_values);
		res.setInstructions(_inst);
		
		return res;
	}
	
	// Values
	
	public int getValue(ScoreObject obj) {
		_values.push(obj);
		return _values.size() - 1;
	}
	
	public int getValue(ScoreValue val) {
		return getValue(new ScoreObject(val));
	}
	
	public int getBoolValue(boolean val) {
		// TODO Optimize
		_values.push(new ScoreObject(val));
		return _values.size() - 1;
	}
	
	public int getCharValue(char val) {
		// TODO Optimize
		_values.push(new ScoreObject(val));
		return _values.size() - 1;
	}
	
	public int getNumericValue(double val) {
		// TODO Optimize
		_values.push(new ScoreObject(val));
		return _values.size() - 1;
	}
	
	public int getNumericValue(long val) {
		// TODO Optimize
		_values.push(new ScoreObject(val));
		return _values.size() - 1;
	}
	
	public int getStringValue(String val) {
		// TODO Optimize
		_values.push(new ScoreObject(val));
		return _values.size() - 1;
	}
	
	// Instructions
	
	public int addInst(byte op) {
		_inst.push(new ScoreInstruction(op));
		return _inst.size() - 1;
	}
	
	public int addInst(byte op, int arg0) {
		_inst.push(new ScoreInstruction(op, arg0));
		return _inst.size() - 1;
	}
	
	public int addInst(byte op, int arg0, int arg1) {
		_inst.push(new ScoreInstruction(op, arg0, arg1));
		return _inst.size() - 1;
	}
	
	public int addInst(byte op, int arg0, int arg1, int arg2) {
		_inst.push(new ScoreInstruction(op, arg0, arg1, arg2));
		return _inst.size() - 1;
	}
	
	public int addInst(byte op, int arg0, int arg1, int arg2, int arg3) {
		_inst.push(new ScoreInstruction(op, arg0, arg1, arg2, arg3));
		return _inst.size() - 1;
	}
	
	public int addInst(ScoreInstruction inst) {
		_inst.push(inst);
		return _inst.size() - 1;
	}
	
	public void setInstArg(int idx, int param, int arg) {
		ScoreInstruction inst = _inst.get(idx);
		switch (param) {
			case 0: inst.setArg0(arg);
			case 1: inst.setArg1(arg);
			case 2: inst.setArg2(arg);
			case 3: inst.setArg3(arg);
		}
	}
	
	public ScoreInstruction getInstAt(int i) {
		return _inst.get(i);
	}
	
	public ScoreInstruction popInst() {
		return _inst.pop();
	}
	
	public void popInst(int amt) {
		for (int i = 0; i < amt; i++)
			_inst.pop();
	}
	
	public int getCurrentInstPos() {
		return _inst.size() - 1;
	}
	
	// Getters/Setters
	
	public void setNumParams(int params) {
		_params = params;
	}
	
}