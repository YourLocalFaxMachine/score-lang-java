package org.scorelang.value;

import org.scorelang.object.ScoreObject;

public class ScoreInt implements ScoreValue {
	
	private long _value;
	
	public ScoreInt() {
	}
	
	public ScoreInt(long value) {
		_value = value;
	}
	
	@Override
	public byte getType() {
		return ScoreObject.INT;
	}
	
	// Getters/Setters
	
	public long get() {
		return _value;
	}
	
	public void set(long value) {
		_value = value;
	}
	
	// To String
	
	public String toString() {
		return Long.toString(_value);
	}
	
}