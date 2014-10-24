package org.scorelang.value;

import org.scorelang.object.ScoreObject;

public class ScoreBool implements ScoreValue {
	
	private boolean _value;
	
	public ScoreBool() {
	}
	
	public ScoreBool(boolean value) {
		_value = value;
	}
	
	@Override
	public byte getType() {
		return ScoreObject.BOOL;
	}
	
	// Getters/Setters
	
	public boolean get() {
		return _value;
	}
	
	public void set(boolean value) {
		_value = value;
	}
	
	// To String
	
	public String toString() {
		return Boolean.toString(_value);
	}
	
}