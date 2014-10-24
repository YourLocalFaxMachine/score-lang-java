package org.scorelang.value;

import org.scorelang.function.ScoreFunction;
import org.scorelang.object.*;

public class ScoreRout implements ScoreValue {
	
	private ScoreFunction _value;
	
	public ScoreRout() {
	}
	
	public ScoreRout(ScoreFunction value) {
		_value = value;
	}
	
	@Override
	public byte getType() {
		return ScoreObject.ROUT;
	}
	
	// Getters/Setters
	
	public ScoreFunction get() {
		return _value;
	}
	
	public void set(ScoreFunction value) {
		_value = value;
	}
	
	// To String
	
	public String toString() {
		return "rout";
	}
	
}