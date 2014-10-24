package org.scorelang.value;

import org.scorelang.object.ScoreObject;
import org.scorelang.util.ScoreVector;

public class ScoreString implements ScoreValue {
	
	private boolean _updateString = true;
	private String _string;
	
	private ScoreVector<ScoreChar> _value;
	
	public ScoreString() {
	}
	
	public ScoreString(ScoreVector<ScoreChar> value) {
		_value = value;
	}
	
	public ScoreString(String value) {
		set(value);
	}
	
	@Override
	public byte getType() {
		return ScoreObject.STRING;
	}
	
	// Getters/Setters
	
	public ScoreVector<ScoreChar> get() {
		return _value;
	}
	
	public String getString() {
		// TODO Optimize
		if (_updateString) {
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < _value.size(); i++)
				sb.append(_value.get(i).get());
			_string = sb.toString();
			_updateString = false;
		}
		return _string;
	}
	
	public void set(ScoreVector<ScoreChar> value) {
		_updateString = true;
		_value = value;
	}
	
	public void set(String value) {
		_updateString = true;
		char[] chars = value.toCharArray();
		_value = new ScoreVector<ScoreChar>(chars.length);
		for (int i = 0; i < chars.length; i++)
			_value.push(new ScoreChar(chars[i]));
	}
	
	// To String
	
	public String toString() {
		return getString();
	}
	
}