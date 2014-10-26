package org.scorelang.value.array;

import org.scorelang.object.ScoreObject;
import org.scorelang.value.ScoreRout;

public class ScoreRoutArray extends ScoreValueArray<ScoreRout> {
    
    public ScoreRoutArray() {
        super();
    }
    
    public ScoreRoutArray(int initialSize) {
        super(initialSize);
        for (int i = 0; i < initialSize; i++)
        	push(new ScoreRout());
    }
    
    public ScoreRoutArray(ScoreRout[] values) {
        this();
        set(values);
    }
    
    @Override
    protected ScoreObject getDefaultValue() {
    	return new ScoreObject();
    }
    
	@Override
	public byte getType() {
		return ScoreObject.ROUT_ARRAY;
	}
	
	public ScoreRout[] get() {
	    return toArray(new ScoreRout[size()]);
	}
	
	public void set(ScoreRout[] values) {
	    clear();
	    for (int i = 0; i < values.length; i++)
	        push(values[i]);
	}
	
}