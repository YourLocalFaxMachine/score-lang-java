package org.scorelang.object;

import org.scorelang.function.ScoreFunction;
import org.scorelang.ScoreException;
import org.scorelang.util.ScoreVector;
import org.scorelang.value.*;
import org.scorelang.value.array.*;

public class ScoreObject {
	
	public static final byte NULL			= 0x00;
	
	public static final byte BOOL			= 0x01;
	public static final byte CHAR			= 0x02;
	public static final byte FLOAT			= 0x03;
	public static final byte INT			= 0x04;
	public static final byte ROUT			= 0x05;
	public static final byte STRING			= 0x06;
	
	public static final byte VAR_ARRAY		= 0x07; // But no var, because var means ANYTHING but var arrays CONTAIN everything (so need an object for them).
	public static final byte BOOL_ARRAY		= 0x08;
	public static final byte CHAR_ARRAY		= 0x09;
	public static final byte FLOAT_ARRAY	= 0x0A;
	public static final byte INT_ARRAY		= 0x0B;
	public static final byte ROUT_ARRAY		= 0x0C;
	public static final byte STRING_ARRAY	= 0x0D;
	
	// Native compatability
	public static boolean isTypeCompatable(ScoreObject val, String typeString, boolean isArray) {
		switch (typeString) {
			case "var":
				if (isArray)
					return val.isArray(); // always, but only if it's an array
				return true;
			case "bool":
				if (isArray)
					return val.isBoolArray();
				return val.isBool();
			case "char":
				if (isArray)
					return val.isCharArray();
				return val.isChar();
			case "float":
				if (isArray)
					return val.isFloatArray();
				return val.isFloat() || val.isChar() || val.isInt();
			case "int":
				if (isArray)
					return val.isIntArray();
				return val.isChar() || val.isInt();
			case "rout":
				if (isArray)
					return val.isRoutArray();
				return val.isRout();
			case "string":
				if (isArray)
					return val.isStringArray();
				return val.isString();
		}
		return false;
	}
	
	// Native compatability
	public static ScoreObject nativeCastType(ScoreObject val, String typeString, boolean isArray) {
		switch (typeString) {
			case "var": return val;
			case "bool":
				if (isArray) {
					if (val.getType() == BOOL_ARRAY)
						return val;
				} else {
					if (val.getType() == BOOL)
						return val;
				}
				break;
			case "char":
				if (isArray) {
					if (val.getType() == CHAR_ARRAY)
						return val;
				} else {
					if (val.getType() == CHAR)
						return val;
				}
				break;
			case "float":
				if (isArray) {
					if (val.getType() == FLOAT_ARRAY)
						return val;
				} else {
					if (val.getType() == FLOAT)
						return val;
					else if (val.getType() == INT)
						return new ScoreObject((double) val.getInt());
					else if (val.getType() == CHAR)
						return new ScoreObject((double) val.getChar());
				}
				break;
			case "int":
				if (isArray) {
					if (val.getType() == INT_ARRAY)
						return val;
				} else {
					if (val.getType() == INT)
						return val;
					else if (val.getType() == CHAR)
						return new ScoreObject((long) val.getChar());
				}
				break;
			case "rout":
				if (isArray) {
					if (val.getType() == ROUT_ARRAY)
						return val;
				} else {
					if (val.getType() == ROUT)
						return val;
				}
				break;
			case "string":
				if (isArray) {
					if (val.getType() == STRING_ARRAY)
						return val;
				} else {
					if (val.getType() == STRING)
						return val;
				}
				break;
		}
		throw new ScoreException("Can't natively cast that value to that type.");
	}
	
	// Type Information
	private byte _type;
	private boolean _isArray;
	
	// Data
	private ScoreValue _value;
	
	public ScoreObject() {
		this(NULL, false);
	}
	
	public ScoreObject(ScoreValue val) {
		this(val.getType(), val instanceof ScoreValueArray);
		_value = val;
	}
	
	public ScoreObject(byte type, boolean isArray) {
		setType(type, isArray);
	}
	
	public ScoreObject(boolean value) {
		setBool(value);
	}
	
	public ScoreObject(char value) {
		setChar(value);
	}
	
	public ScoreObject(double value) {
		setFloat(value);
	}
	
	public ScoreObject(long value) {
		setInt(value);
	}
	
	public ScoreObject(ScoreInt[] value) {
		setIntArray(value);
	}
	
	public ScoreObject(long[] value) {
		setIntArray(value);
	}
	
	public ScoreObject(ScoreFunction value) {
		setRout(value);
	}
	
	public ScoreObject(String value) {
		setString(value);
	}
	
	// Type
	
	private void setType(byte type, boolean isArray) {
		_type = type;
		_isArray = isArray;
	}
	
	public String getTypeName() {
		switch (_type) {
			case NULL:			return "null";
			case BOOL:			return "bool";
			case CHAR:			return "char";
			case FLOAT:			return "float";
			case INT:			return "int";
			case ROUT:			return "rout";
			case STRING:		return "string";
			case VAR_ARRAY:		return "var[]";
			case BOOL_ARRAY:	return "bool[]";
			case CHAR_ARRAY:	return "char[]";
			case FLOAT_ARRAY:	return "float[]";
			case INT_ARRAY:		return "int[]";
			case ROUT_ARRAY:	return "rout[]";
			case STRING_ARRAY:	return "string[]";
		}
		throw new ScoreException("Type not recognized.");
	}
	
	// Compound
	
	public boolean isNumeric() {
		return isFloat() || isInt() || isChar();
	}
	
	// Bool
	
	public boolean isBool() {
		return _type == BOOL;
	}
	
	public ScoreBool getBoolValue() {
		if (_type != BOOL)
			throw new ScoreException("Object is not a bool.");
		return (ScoreBool) _value;
	}
	
	public boolean getBool() {
		return getBoolValue().get();
	}
	
	public boolean getAsBool() {
		if (isBool())
			return getBool();
		throw new ScoreException("Cannot convert type " + getTypeName() + " to a float.");
	}
	
	public void setBool(boolean value) {
		if (_type == BOOL)
			getBoolValue().set(value);
		else setValue(new ScoreBool(value));
	}
	
	// Bool Array
	
	public boolean isBoolArray() {
		return _type == BOOL_ARRAY;
	}
	
	public ScoreBoolArray getBoolArrayValue() {
		if (_type != BOOL_ARRAY)
			throw new ScoreException("Object is not a bool[].");
		return (ScoreBoolArray) _value;
	}
	
	public ScoreBool[] getBoolArray() {
		return getBoolArrayValue().get();
	}
	
	public boolean[] getBoolArrayBools() {
		return getBoolArrayValue().getBools();
	}
	
	public void setBoolArray(ScoreBool[] values) {
		if (_type == BOOL_ARRAY)
			getBoolArrayValue().set(values);
		else setValue(new ScoreBoolArray(values));
	}
	
	public void setBoolArray(boolean[] values) {
		if (_type == BOOL_ARRAY)
			getBoolArrayValue().set(values);
		else setValue(new ScoreBoolArray(values));
	}
	
	// Char
	
	public boolean isChar() {
		return _type == CHAR;
	}
	
	public ScoreChar getCharValue() {
		if (_type != CHAR)
			throw new ScoreException("Object is not a char.");
		return (ScoreChar) _value;
	}
	
	public char getChar() {
		return getCharValue().get();
	}
	
	public char getAsChar() {
		if (isChar())
			return getChar();
		switch (_type) {
			case FLOAT:	return (char) getFloat();
			case INT:	return (char) getInt();
		}
		throw new ScoreException("Cannot convert type " + getTypeName() + " to a float.");
	}
	
	public void setChar(char value) {
		if (_type == CHAR)
			getCharValue().set(value);
		else setValue(new ScoreChar(value));
	}
	
	// Char Array
	
	public boolean isCharArray() {
		return _type == CHAR_ARRAY;
	}
	
	public ScoreCharArray getCharArrayValue() {
		if (_type != CHAR_ARRAY)
			throw new ScoreException("Object is not a char[].");
		return (ScoreCharArray) _value;
	}
	
	public ScoreChar[] getCharArray() {
		return getCharArrayValue().get();
	}
	
	public char[] getCharArrayChars() {
		return getCharArrayValue().getChars();
	}
	
	public void setCharArray(ScoreChar[] values) {
		if (_type == CHAR_ARRAY)
			getCharArrayValue().set(values);
		else setValue(new ScoreCharArray(values));
	}
	
	public void setCharArray(char[] values) {
		if (_type == CHAR_ARRAY)
			getCharArrayValue().set(values);
		else setValue(new ScoreCharArray(values));
	}
	
	// Float
	
	public boolean isFloat() {
		return _type == FLOAT;
	}
	
	public ScoreFloat getFloatValue() {
		if (_type != FLOAT)
			throw new ScoreException("Object is not a float.");
		return (ScoreFloat) _value;
	}
	
	public double getFloat() {
		return getFloatValue().get();
	}
	
	public double getAsFloat() {
		if (isFloat())
			return getFloat();
		switch (_type) {
			case CHAR:	return (double) getChar();
			case INT:	return (double) getInt();
		}
		throw new ScoreException("Cannot convert type " + getTypeName() + " to a float.");
	}
	
	public void setFloat(double value) {
		if (_type == FLOAT)
			getFloatValue().set(value);
		else setValue(new ScoreFloat(value));
	}
	
	// Float Array
	
	public boolean isFloatArray() {
		return _type == FLOAT_ARRAY;
	}
	
	public ScoreFloatArray getFloatArrayValue() {
		if (_type != FLOAT_ARRAY)
			throw new ScoreException("Object is not a float[].");
		return (ScoreFloatArray) _value;
	}
	
	public ScoreFloat[] getFloatArray() {
		return getFloatArrayValue().get();
	}
	
	public double[] getFloatArrayDoubles() {
		return getFloatArrayValue().getDoubles();
	}
	
	public void setFloatArray(ScoreFloat[] values) {
		if (_type == FLOAT_ARRAY)
			getFloatArrayValue().set(values);
		else setValue(new ScoreFloatArray(values));
	}
	
	public void setFloatArray(double[] values) {
		if (_type == FLOAT_ARRAY)
			getFloatArrayValue().set(values);
		else setValue(new ScoreFloatArray(values));
	}
	
	// Int
	
	public boolean isInt() {
		return _type == INT;
	}
	
	public ScoreInt getIntValue() {
		if (_type != INT)
			throw new ScoreException("Object is not an int.");
		return (ScoreInt) _value;
	}
	
	public long getInt() {
		return getIntValue().get();
	}
	
	public long getAsInt() {
		if (isInt())
			return getInt();
		switch (_type) {
			case CHAR:	return (long) getChar();
			case FLOAT:	return (long) getFloat();
		}
		throw new ScoreException("Cannot convert type " + getTypeName() + " to an int.");
	}
	
	public void setInt(long value) {
		if (_type == INT)
			getIntValue().set(value);
		else setValue(new ScoreInt(value));
	}
	
	// Int Array
	
	public boolean isIntArray() {
		return _type == INT_ARRAY;
	}
	
	public ScoreIntArray getIntArrayValue() {
		if (_type != INT_ARRAY)
			throw new ScoreException("Object is not an int[].");
		return (ScoreIntArray) _value;
	}
	
	public ScoreInt[] getIntArray() {
		return getIntArrayValue().get();
	}
	
	public long[] getIntArrayLongs() {
		return getIntArrayValue().getLongs();
	}
	
	public void setIntArray(ScoreInt[] values) {
		if (_type == INT_ARRAY)
			getIntArrayValue().set(values);
		else setValue(new ScoreIntArray(values));
	}
	
	public void setIntArray(long[] values) {
		if (_type == INT_ARRAY)
			getIntArrayValue().set(values);
		else setValue(new ScoreIntArray(values));
	}
	
	// Rout
	
	public boolean isRout() {
		return _type == ROUT;
	}
	
	public ScoreRout getRoutValue() {
		if (_type != ROUT)
			throw new ScoreException("Object is not a rout.");
		return (ScoreRout) _value;
	}
	
	public ScoreFunction getRout() {
		return getRoutValue().get();
	}
	
	public void setRout(ScoreFunction value) {
		if (_type == ROUT)
			getRoutValue().set(value);
		else setValue(new ScoreRout(value));
	}
	
	// Rout Array
	
	public boolean isRoutArray() {
		return _type == ROUT_ARRAY;
	}
	
	public ScoreRoutArray getRoutArrayValue() {
		if (_type != ROUT_ARRAY)
			throw new ScoreException("Object is not a rout[].");
		return (ScoreRoutArray) _value;
	}
	
	public ScoreRout[] getRoutArray() {
		return getRoutArrayValue().get();
	}
	
	public void setRoutArray(ScoreRout[] values) {
		if (_type == ROUT_ARRAY)
			getRoutArrayValue().set(values);
		else setValue(new ScoreRoutArray(values));
	}
	
	// String
	
	public boolean isString() {
		return _type == STRING;
	}
	
	public ScoreString getStringValue() {
		if (_type != STRING)
			throw new ScoreException("Object is not a string.");
		return (ScoreString) _value;
	}
	
	public ScoreVector<ScoreChar> getStringVector() {
		return getStringValue().get();
	}
	
	public String getString() {
		return getStringValue().getString();
	}
	
	public void setString(String value) {
		if (_type == STRING)
			getStringValue().set(value);
		else setValue(new ScoreString(value));
	}
	
	public String getAsString() {
		if (isString())
			return getString();
		switch (_type) {
			case BOOL:	return Boolean.toString(getBool());
			case CHAR:	return Character.toString(getChar());
			case FLOAT:	return Double.toString(getFloat());
			case INT:	return Long.toString(getInt());
		}
		return toString();
	}
	
	public void setString(ScoreVector<ScoreChar> value) {
		if (_type == STRING)
			getStringValue().set(value);
		else setValue(new ScoreString(value));
	}
	
	// String Array
	
	public boolean isStringArray() {
		return _type == STRING_ARRAY;
	}
	
	public ScoreStringArray getStringArrayValue() {
		if (_type != STRING_ARRAY)
			throw new ScoreException("Object is not a string[].");
		return (ScoreStringArray) _value;
	}
	
	public ScoreString[] getStringArray() {
		return getStringArrayValue().get();
	}
	
	public String[] getStringArrayStrings() {
		return getStringArrayValue().getStrings();
	}
	
	public void setStringArray(ScoreString[] values) {
		if (_type == INT_ARRAY)
			getStringArrayValue().set(values);
		else setValue(new ScoreStringArray(values));
	}
	
	public void setStringArray(String[] values) {
		if (_type == STRING_ARRAY)
			getStringArrayValue().set(values);
		else setValue(new ScoreStringArray(values));
	}
	
	// Var Array
	
	public boolean isVarArray() {
		return _type == VAR_ARRAY;
	}
	
	public ScoreVarArray getVarArrayValue() {
		if (_type != VAR_ARRAY)
			throw new ScoreException("Object is not a var[].");
		return (ScoreVarArray) _value;
	}
	
	public ScoreValue[] getVarArray() {
		return getVarArrayValue().get();
	}
	
	public void setVarArray(ScoreValue[] values) {
		if (_type == VAR_ARRAY)
			getVarArrayValue().set(values);
		else setValue(new ScoreVarArray(values));
	}
	
	// Value
	
	public boolean isNull() {
		return _value == null || _type == NULL;
	}
	
	public void setValue(ScoreValue value) {
		setType(value.getType(), value instanceof ScoreValueArray);
		_value = value;
	}
	
	public void setToNull() {
		setType(NULL, false);
		_value = null;
	}
	
	// Getters
	
	public byte getType() {
		return _type;
	}
	
	public boolean isArray() {
		return _isArray;
	}
	
	public ScoreValue getValue() {
		return _value;
	}
	
	// To String
	
	public String valueString() {
		return _value.toString();
	}
	
	public String toString() {
		return getTypeName() + ":" + _value;
	}
	
}