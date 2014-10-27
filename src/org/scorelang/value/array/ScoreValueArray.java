package org.scorelang.value.array;

import static org.scorelang.object.ScoreObject.*;

import org.scorelang.ScoreException;
import org.scorelang.lexer.ScoreKeywords;
import org.scorelang.object.ScoreObject;
import org.scorelang.util.ScoreVector;
import org.scorelang.value.*;

public class ScoreValueArray implements ScoreValue {

	private static final ScoreValue getDefaultValue(String type) {
		switch (ScoreKeywords.getIDFromKeyword(type)) {
			case "bool":
				return new ScoreBool();
			case "char":
				return new ScoreChar();
			case "float":
				return new ScoreFloat();
			case "int":
				return new ScoreInt();
			case "string":
				return new ScoreString();
			case "rout": case "var": default:
				return null;
		}
	}
	
	private static final boolean isValueCompatible(String type, ScoreValue value) {
		switch (ScoreKeywords.getIDFromKeyword(type)) {
			case "var":
				return true;
			case "bool":
				return value != null && value.getType() == BOOL;
			case "char":
				return value != null && value.getType() == CHAR;
			case "float":
				return value != null && value.getType() == FLOAT;
			case "int":
				return value != null && value.getType() == INT;
			case "string":
				return value == null || value.getType() == STRING;
			case "rout":
				return value == null || value.getType() == ROUT;
		}
		return false;
	}
	
	private static final byte getTypeFromString(String type) {
		switch (ScoreKeywords.getIDFromKeyword(type)) {
			case "var":
				return VAR_ARRAY;
			case "bool":
				return BOOL_ARRAY;
			case "char":
				return CHAR_ARRAY;
			case "float":
				return FLOAT_ARRAY;
			case "int":
				return INT_ARRAY;
			case "string":
				return STRING_ARRAY;
			case "rout":
				return ROUT_ARRAY;
		}
		return NULL;
	}
	
	private final String _type;
	private final ScoreVector<ScoreValue> _values;
	
	// ctors
	
	public ScoreValueArray() {
		this(ScoreKeywords.getKeyword("var"), 0);
	}
	
	public ScoreValueArray(String type) {
		this(type, 0);
	}
	
	public ScoreValueArray(int initialSize) {
		this(ScoreKeywords.getKeyword("var"), initialSize);
	}
	
	public ScoreValueArray(String type, int initialSize) {
		_type = type;
		_values = new ScoreVector<ScoreValue>(initialSize);
		for (int i = 0; i < initialSize; i++)
			push(getDefaultValue(_type));
	}
	
	public ScoreValueArray(ScoreVector<ScoreValue> values) {
		this(ScoreKeywords.getKeyword("var"), values);
	}
	
	public ScoreValueArray(String type, ScoreVector<ScoreValue> values) {
		_type = type;
		checkValues(values);
		_values = values;
	}
	
	public ScoreValueArray(ScoreValue... values) {
	    this(ScoreKeywords.getKeyword("var"), values);
	}
	
	public ScoreValueArray(String type, ScoreValue... values) {
	    _type = type;
		checkValues(values);
		_values = new ScoreVector<ScoreValue>(values.length);
		for (int i = 0; i < values.length; i++)
			push(values[i]);
	}
	
	public ScoreValueArray(ScoreObject... objects) {
	    this(ScoreKeywords.getKeyword("var"), objects);
	}
	
	public ScoreValueArray(String type, ScoreObject... objects) {
	    _type = type;
	    ScoreValue[] values = new ScoreValue[objects.length];
	    for (int i = 0; i < values.length; i++)
	        values[i] = objects[i].getValue();
		checkValues(values);
		_values = new ScoreVector<ScoreValue>(values.length);
		for (int i = 0; i < values.length; i++)
			push(values[i]);
	}
	
	@Override
	public byte getType() {
		return getTypeFromString(_type);
	}
	
	// convenience
	
	private final void checkValues(ScoreVector<ScoreValue> values) {
		for (int i = 0; i < values.size(); i++) {
			if (!isValueCompatible(_type, values.get(i)))
				throw new ScoreException("Value " + values.get(i) + " is not compatible with this arary.");
		}
	}
	
	private final void checkValues(ScoreValue... values) {
		for (int i = 0; i < values.length; i++) {
			if (!isValueCompatible(_type, values[i]))
				throw new ScoreException("Value " + values[i] + " is not compatible with this arary.");
		}
	}
	
	public final void clear() {
		_values.clear();
	}
	
	public final ScoreValue[] toArray() {
		ScoreValue[] res = new ScoreValue[length()];
		for (int i = 0; i < res.length; i++)
			res[i] = _values.get(i);
		return res;
	}
	
	// operations
	
	public final ScoreValueArray sub(int start, int end) {
		return new ScoreValueArray(_type, _values.sub(start, end));
	}
	
	public final ScoreValueArray reverse() {
		return new ScoreValueArray(_type, _values.reverse());
	}
	
	public final void setLength(int len) {
		int oldlen = length();
		_values.setSize(len);
		if (len > oldlen) {
			for (int i = oldlen; i < len; i++)
				_values.set(i, getDefaultValue(_type));
		}
	}
	
	public final ScoreValue get(int idx) {
		return _values.get(idx);
	}
	
	public final void set(ScoreValue... values) {
	    checkValues(values);
	    setLength(values.length);
		for (int i = 0; i < values.length; i++)
			_values.set(i, values[i]);
	}
	
	public final void set(int idx, ScoreValue val) {
		if (!isValueCompatible(_type, val))
			throw new ScoreException("Value " + val + " (type " + val.getType() + ") is not compatible with this arary.");
		_values.set(idx, val);
	}
	
	public final void push(ScoreValue val) {
		if (!isValueCompatible(_type, val))
			throw new ScoreException("Value " + val + " (type " + val.getType() + ") is not compatible with this arary.");
		_values.push(val);
	}
	
	public final ScoreValue pop() {
		return _values.pop();
	}
	
	// properties
	
	public final int length() {
		return _values.size();
	}
	
	@Override
	public String toString() {
		return _values.toString();
	}
	
}