package org.scorelang.value.array;

import org.scorelang.object.ScoreObject;
import org.scorelang.util.ScoreVector;
import org.scorelang.value.ScoreFloat;
import org.scorelang.value.ScoreValue;

public class ScoreFloatArray extends ScoreValueArray<ScoreFloat> {
    
    public ScoreFloatArray() {
        super();
    }
    
    public ScoreFloatArray(int initialSize) {
        super(initialSize);
        for (int i = 0; i < initialSize; i++)
        	push(new ScoreFloat(0));
    }
    
    public ScoreFloatArray(ScoreVector<ScoreFloat> values) {
    	super(values);
    }
    
    public ScoreFloatArray(ScoreFloat[] values) {
        this();
        set(values);
    }
    
    public ScoreFloatArray(double[] values) {
        this();
        set(values);
    }
    
    @Override
    protected ScoreObject getDefaultValue() {
    	return new ScoreObject(0.0);
    }
    
    @Override
    public boolean isCompatible(ScoreValue val) {
        return val instanceof ScoreFloat;
    }
    
    @Override
    public ScoreObject subArray(int start, int end) {
    	return new ScoreObject(new ScoreFloatArray(sub(start, end)));
    }
    
    @Override
    public ScoreObject reverse() {
        return new ScoreObject(new ScoreFloatArray(rev()));
    }
	
	@Override
	public byte getType() {
		return ScoreObject.FLOAT_ARRAY;
	}
	
	public ScoreFloat[] get() {
	    return toArray(new ScoreFloat[size()]);
	}
	
	public double[] getDoubles() {
		ScoreFloat[] v = get();
	    double[] res = new double[v.length];
	    for (int i = 0; i < v.length; i++)
	    	res[i] = v[i].get();
	    return res;
	}
	
	public void set(ScoreFloat[] values) {
	    clear();
	    for (int i = 0; i < values.length; i++)
	        push(values[i]);
	}
	
	public void set(double[] values) {
	    clear();
	    for (int i = 0; i < values.length; i++)
	        push(new ScoreFloat(values[i]));
	}
    
}