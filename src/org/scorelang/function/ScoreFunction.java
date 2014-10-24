package org.scorelang.function;

import org.scorelang.object.ScoreObject;
import org.scorelang.util.ScoreVector;
import org.scorelang.vm.ScoreInstruction;
import org.scorelang.vm.ScoreVM;

public class ScoreFunction {
	
	private ScoreFunctionProto _proto;
	private ScoreVM.Scope _parentScope; // for to access the scope it was declared in.
	
	public ScoreFunction(ScoreFunctionProto proto) {
		_proto = proto;
	}
	
	// Getters/Setters
	
	public ScoreVector<ScoreInstruction> getInstructions() {
		return _proto.getInstructions();
	}
	
	public ScoreVector<ScoreObject> getValues() {
		return _proto.getValues();
	}
	
	public int getNumParams() {
		return _proto.getNumParams();
	}
	
	public void setScope(ScoreVM.Scope scope) {
		_parentScope = scope;
	}
	
	public ScoreVM.Scope getScope() {
		return _parentScope;
	}
	
}