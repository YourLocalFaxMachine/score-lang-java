package org.scorelang.value;

import org.scorelang.object.ScoreObject;

public class ScoreFloat implements ScoreValue {
	
	private double _value;
	
	public ScoreFloat() {
	}
	
	public ScoreFloat(double value) {
		_value = value;
	}
	
	@Override
	public byte getType() {
		return ScoreObject.FLOAT;
	}
	
	// Getters/Setters
	
	public double get() {
		return _value;
	}
	
	public void set(double value) {
		_value = value;
	}
	
	// To String
	
	public String toString() {
		return Double.toString(_value);
	}
	
}