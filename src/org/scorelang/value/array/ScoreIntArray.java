package org.scorelang.value.array;

import org.scorelang.object.ScoreObject;
import org.scorelang.value.ScoreInt;

public class ScoreIntArray extends ScoreValueArray<ScoreInt> {
    
    public ScoreIntArray() {
        super();
    }
    
    public ScoreIntArray(int initialSize) {
        super(initialSize);
    }
    
    public ScoreIntArray(ScoreInt... values) {
        this(10);
        set(values);
    }
    
    public ScoreIntArray(long... values) {
        this(10);
        set(values);
    }
	
	@Override
	public byte getType() {
		return ScoreObject.INT_ARRAY;
	}
	
	public ScoreInt[] get() {
	    return toArray(new ScoreInt[size()]);
	}
	
	public long[] getLongs() {
	    long[] res = new long[size()];
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