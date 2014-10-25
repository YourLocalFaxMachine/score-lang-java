package org.scorelang.vm;

import java.util.HashMap;

import org.scorelang.ScoreException;
import org.scorelang.object.ScoreObject;

public class ScoreTable {
	
	private static final class VarData {
		
		private ScoreObject _value;
		private String _type;
		private boolean _isArray, _static, _final;
		
		// TODO get rid of static, only needed in classes.
		private VarData(ScoreObject value, String type, boolean array, boolean stat, boolean fin) {
			_value = value;
			_type = type;
			_isArray = array;
			_static = stat;
			_final = fin;
		}
		
	}
	
	private HashMap<String, VarData> _table;
	
	public ScoreTable() {
		_table = new HashMap<String, VarData>();
	}
	
	public void assign(String key, ScoreObject value, String type, boolean array, boolean stat, boolean fin) {
		if (contains(key))
			throw new ScoreException("Variable with name \"" + key + "\" already exists.");
		if (!ScoreObject.isTypeCompatable(value, type, array)) {
			String ta = array ? "[]" : "";
			throw new ScoreException("Value of type " + value.getTypeName() + " cannot be assigned type " + type + ta + ".");
		}
		_table.put(key, new VarData(ScoreObject.nativeCastType(value, type, array), type, array, stat, fin));
	}
	
	public void set(String key, ScoreObject value) {
		if (!contains(key))
			throw new ScoreException("Variable " + key + " does not exist.");
		VarData v = _table.get(key);
		if (!ScoreObject.isTypeCompatable(value, v._type, v._isArray)) {
			String va = value.isArray() ? "[]" : "", ta = v._isArray ? "[]" : "";
			throw new ScoreException("Value of type " + value.getTypeName() + va + " cannot be assigned type " + v._type + ta + ".");
		}
		if (v._final)
			throw new ScoreException("Variable " + key + " was declared final, cannot set to it.");
		// Check for type compatability
		v._value = ScoreObject.nativeCastType(value, v._type, value.isArray());
	}
	
	public ScoreObject get(String key) {
		if (!contains(key))
			throw new ScoreException("Variable " + key + " does not exist.");
		return _table.get(key)._value;
	}
	
	public boolean contains(String key) {
		return _table.containsKey(key);
	}
	
}