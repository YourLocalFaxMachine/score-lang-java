package org.scorelang.object;

import org.scorelang.function.ScoreFunction;
import org.scorelang.ScoreException;
import org.scorelang.util.ScoreVector;
import org.scorelang.value.*;

public class ScoreObject {
	
	public static final byte NULL			= 0x00;
	public static final byte BOOL			= 0x01;
	public static final byte CHAR			= 0x02;
	public static final byte FLOAT			= 0x03;
	public static final byte INT			= 0x04;
	public static final byte ROUT			= 0x05;
	public static final byte STRING			= 0x06;
	
	// Native compatability
	public static boolean isTypeCompatable(ScoreObject val, String typeString) {
		switch (typeString) {
			case "var": return true;
			case "bool":
				return val.isBool();
			case "char":
				return val.isChar();
			case "float":
				return val.isFloat() || val.isChar() || val.isInt();
			case "int":
				return val.isChar() || val.isInt();
			case "string":
				return val.isString();
		}
		return false;
	}
	
	// Native compatability
	public static ScoreObject nativeCastType(ScoreObject val, String typeString) {
		switch (typeString) {
			case "var": return val;
			case "bool": return new ScoreObject(val.getBool());
			case "char": return new ScoreObject(val.getChar());
			case "float":
				if (val.getType() == FLOAT)
					return new ScoreObject(val.getFloat());
				else if (val.getType() == INT)
					return new ScoreObject((double) val.getInt());
				else if (val.getType() == CHAR)
					return new ScoreObject((double) val.getChar());
			case "int":
				if (val.getType() == INT)
					return new ScoreObject(val.getInt());
				else if (val.getType() == CHAR)
					return new ScoreObject((long) val.getChar());
			case "string": return new ScoreObject(val.getString());
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
			case NULL:		return "null";
			case BOOL:		return "bool";
			case CHAR:		return "char";
			case FLOAT:		return "float";
			case INT:		return "int";
			case ROUT:		return "rout";
			case STRING:	return "string";
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
	
	private ScoreBool getBoolValue() {
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
	
	// Char
	
	public boolean isChar() {
		return _type == CHAR;
	}
	
	private ScoreChar getCharValue() {
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
	
	// Float
	
	public boolean isFloat() {
		return _type == FLOAT;
	}
	
	private ScoreFloat getFloatValue() {
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
	
	// Int
	
	public boolean isInt() {
		return _type == INT;
	}
	
	private ScoreInt getIntValue() {
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
	
	// Rout
	
	public boolean isRout() {
		return _type == ROUT;
	}
	
	private ScoreRout getRoutValue() {
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
	
	// String
	
	public boolean isString() {
		return _type == STRING;
	}
	
	private ScoreString getStringValue() {
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
	
	// Value
	
	public boolean isNull() {
		return _value == null;
	}
	
	public void setValue(ScoreValue value) {
		setType(value.getType(), false);
		_value = value;
	}
	
	public void setToNull() {
		setType((byte) 0, false);
		_value = null;
	}
	
	// Getters
	
	public byte getType() {
		return _type;
	}
	
	public boolean isArray() {
		return _isArray;
	}
	
	// To String
	
	public String valueString() {
		return _value.toString();
	}
	
	public String toString() {
		return getTypeName() + ":" + _value;
	}
	
}