package org.scorelang.value.array;

import org.scorelang.object.ScoreObject;
import org.scorelang.util.ScoreVector;
import org.scorelang.value.ScoreChar;
import org.scorelang.value.ScoreValue;

public class ScoreCharArray extends ScoreValueArray<ScoreChar> {
    
    public ScoreCharArray() {
        super();
    }
    
    public ScoreCharArray(int initialSize) {
        super(initialSize);
        for (int i = 0; i < initialSize; i++)
        	push(new ScoreChar('\u0000'));
    }
    
    public ScoreCharArray(ScoreVector<ScoreChar> values) {
    	super(values);
    }
    
    public ScoreCharArray(ScoreChar[] values) {
        this();
        set(values);
    }
    
    public ScoreCharArray(char[] values) {
        this();
        set(values);
    }
    
    @Override
    protected ScoreObject getDefaultValue() {
    	return new ScoreObject('\u0000');
    }
    
    @Override
    public boolean isCompatible(ScoreValue val) {
        return val instanceof ScoreChar;
    }
    
    @Override
    public ScoreObject subArray(int start, int end) {
    	return new ScoreObject(new ScoreCharArray(sub(start, end)));
    }
    
    @Override
    public ScoreObject reverse() {
        return new ScoreObject(new ScoreCharArray(rev()));
    }
	
	@Override
	public byte getType() {
		return ScoreObject.CHAR_ARRAY;
	}
	
	public ScoreChar[] get() {
	    return toArray(new ScoreChar[size()]);
	}
	
	public char[] getChars() {
		ScoreChar[] v = get();
	    char[] res = new char[v.length];
	    for (int i = 0; i < v.length; i++)
	    	res[i] = v[i].get();
	    return res;
	}
	
	public void set(ScoreChar[] values) {
	    clear();
	    for (int i = 0; i < values.length; i++)
	        push(values[i]);
	}
	
	public void set(char[] values) {
	    clear();
	    for (int i = 0; i < values.length; i++)
	        push(new ScoreChar(values[i]));
	}
    
}