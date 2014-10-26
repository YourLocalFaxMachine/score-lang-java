package org.scorelang.value.array;

import org.scorelang.object.ScoreObject;
import org.scorelang.util.ScoreVector;
import org.scorelang.value.ScoreRout;
import org.scorelang.value.ScoreValue;

public class ScoreRoutArray extends ScoreValueArray<ScoreRout> {
    
    public ScoreRoutArray() {
        super();
    }
    
    public ScoreRoutArray(int initialSize) {
        super(initialSize);
        for (int i = 0; i < initialSize; i++)
        	push(new ScoreRout());
    }
    
    public ScoreRoutArray(ScoreVector<ScoreRout> values) {
    	super(values);
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
    public boolean isCompatible(ScoreValue val) {
        return val instanceof ScoreRout;
    }
    
    @Override
    public ScoreObject subArray(int start, int end) {
    	return new ScoreObject(new ScoreRoutArray(sub(start, end)));
    }
    
    @Override
    public ScoreObject reverse() {
        return new ScoreObject(new ScoreRoutArray(rev()));
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