package org.scorelang.value.array;

import org.scorelang.object.ScoreObject;
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
    
    public ScoreValueArray(ScoreVector<T> values) {
        _values = values;
    }
    
    protected final void clear() {
        _values.clear();
    }
    
    protected final T[] toArray(T[] res) {
        for (int i = 0; i < size(); i++)
            res[i] = _values.get(i);
        return res;
    }
    
    protected final ScoreVector<T> sub(int start, int end) {
        return _values.sub(start, end);
    }
    
    protected abstract ScoreObject getDefaultValue();
    
    public abstract ScoreObject subArray(int start, int end);
    
	@SuppressWarnings("unchecked")
    public void setLength(int len) {
        int oldsize = size();
        _values.setSize(len);
        if (len > oldsize)
            for (int i = oldsize; i < len; i++)
                _values.set(i, (T) getDefaultValue().getValue());
    }
    
    public void push(T value) {
        _values.push(value);
    }
    
    public T pop() {
        return _values.pop();
    }
    
    public T get(int idx) {
        return _values.get(idx);
    }
    
    public int size() {
        return _values.size();
    }
	
	// To String
	
	public String toString() {
		return _values.toString();
	}
    
}