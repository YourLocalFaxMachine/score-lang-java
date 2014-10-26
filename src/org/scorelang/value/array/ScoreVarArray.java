package org.scorelang.value.array;

import org.scorelang.object.ScoreObject;
import org.scorelang.value.ScoreValue;

public class ScoreVarArray extends ScoreValueArray<ScoreValue> {
    
    public ScoreVarArray() {
        super();
    }
    
    public ScoreVarArray(int initialSize) {
        super(initialSize);
    }
    
    public ScoreVarArray(ScoreValue[] values) {
        this();
        set(values);
    }
    
    @Override
    protected ScoreObject getDefaultValue() {
    	return new ScoreObject();
    }
	
	@Override
	public byte getType() {
		return ScoreObject.VAR_ARRAY;
	}
	
	public ScoreValue[] get() {
	    return toArray(new ScoreValue[size()]);
	}
	
	public void set(ScoreValue[] values) {
	    clear();
	    for (int i = 0; i < values.length; i++)
	        push(values[i]);
	}
    
}