package org.scorelang.value.array;

import org.scorelang.object.ScoreObject;
import org.scorelang.value.ScoreInt;

public class ScoreIntArray extends ScoreValueArray<ScoreInt> {
    
    public ScoreIntArray() {
        super();
    }
    
    public ScoreIntArray(int initialSize) {
        super(initialSize);
        for (int i = 0; i < initialSize; i++)
        	push(new ScoreInt(0));
    }
    
    public ScoreIntArray(ScoreInt[] values) {
        this();
        set(values);
    }
    
    public ScoreIntArray(long[] values) {
        this();
        set(values);
    }
    
    @Override
    protected ScoreObject getDefaultValue() {
    	return new ScoreObject(0L);
    }
	
	@Override
	public byte getType() {
		return ScoreObject.INT_ARRAY;
	}
	
	public ScoreInt[] get() {
	    return toArray(new ScoreInt[size()]);
	}
	
	public long[] getLongs() {
		ScoreInt[] v = get();
	    long[] res = new long[v.length];
	    for (int i = 0; i < v.length; i++)
	    	res[i] = v[i].get();
	    return res;
	}
	
	public void set(ScoreInt[] values) {
	    clear();
	    for (int i = 0; i < values.length; i++)
	        push(values[i]);
	}
	
	public void set(long[] values) {
	    clear();
	    for (int i = 0; i < values.length; i++)
	        push(new ScoreInt(values[i]));
	}
    
}