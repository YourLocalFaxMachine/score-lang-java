package org.scorelang.value;

import org.scorelang.object.ScoreObject;

public class ScoreChar implements ScoreValue {
	
	private char _value;
	
	public ScoreChar() {
	}
	
	public ScoreChar(char value) {
		_value = value;
	}
	
	@Override
	public byte getType() {
		return ScoreObject.CHAR;
	}
	
	// Getters/Setters
	
	public char get() {
		return _value;
	}
	
	public void set(char value) {
		_value = value;
	}
	
	// To String
	
	public String toString() {
		return Character.toString(_value);
	}
	
}