package org.scorelang.function;

import org.scorelang.object.ScoreObject;
import org.scorelang.util.ScoreVector;
import org.scorelang.vm.ScoreInstruction;

public class ScoreFunctionProto {
	
	private ScoreVector<ScoreInstruction> _inst;
	private ScoreVector<ScoreObject> _values;
	
	private int _params;
	
	public ScoreFunctionProto() {
		this(0);
	}
	
	public ScoreFunctionProto(int params) {
		_inst = new ScoreVector<ScoreInstruction>();
		_values = new ScoreVector<ScoreObject>();
		_params = params;
	}
	
	// Getters/Setters
	
	public ScoreVector<ScoreInstruction> getInstructions() {
		return _inst;
	}
	
	void setInstructions(ScoreVector<ScoreInstruction> inst) {
		_inst = inst;
	}
	
	public ScoreVector<ScoreObject> getValues() {
		return _values;
	}
	
	void setValues(ScoreVector<ScoreObject> values) {
		_values = values;
	}
	
	public int getNumParams() {
		return _params;
	}
	
}