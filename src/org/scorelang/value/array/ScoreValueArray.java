package org.scorelang.value.array;

import org.scorelang.util.ScoreVector;
import org.scorelang.value.ScoreValue;

public abstract class ScoreValueArray<T extends ScoreValue> implements ScoreValue  {
    
    private ScoreVector<T> _values;
    
    public ScoreValueArray() {
        this(0);
    }
    
    public ScoreValueArray(int initialSize) {
        _values = new ScoreVector<T>(initialSize);
    }
    
    protected final void clear() {
        _values.clear();
    }
    
    protected final T[] toArray(T[] res) {
        for (int i = 0; i < size(); i++)
            res[i] = _values.get(i);
        return res;
    }
    
    public void push(T value) {
        _values.push(value);
    }
    
    public T pop() {
        return _values.pop();
    }
    
    public int size() {
        return _values.size();
    }
	
	// To String
	
	public String toString() {
		return _values.toString();
	}
    
}