package org.scorelang.value.array;

import org.scorelang.object.ScoreObject;
import org.scorelang.value.ScoreBool;

public class ScoreBoolArray extends ScoreValueArray<ScoreBool> {
    
    public ScoreBoolArray() {
        super();
    }
    
    public ScoreBoolArray(int initialSize) {
        super(initialSize);
        for (int i = 0; i < initialSize; i++)
        	push(new ScoreBool(false));
    }
    
    public ScoreBoolArray(ScoreBool[] values) {
        this();
        set(values);
    }
    
    public ScoreBoolArray(boolean[] values) {
        this();
        set(values);
    }
	
	@Override
	public byte getType() {
		return ScoreObject.BOOL_ARRAY;
	}
	
	public ScoreBool[] get() {
	    return toArray(new ScoreBool[size()]);
	}
	
	public boolean[] getBools() {
		ScoreBool[] v = get();
	    boolean[] res = new boolean[v.length];
	    for (int i = 0; i < v.length; i++)
	    	res[i] = v[i].get();
	    return res;
	}
	
	public void set(ScoreBool[] values) {
	    clear();
	    for (int i = 0; i < values.length; i++)
	        push(values[i]);
	}
	
	public void set(boolean[] values) {
	    clear();
	    for (int i = 0; i < values.length; i++)
	        push(new ScoreBool(values[i]));
	}
    
}