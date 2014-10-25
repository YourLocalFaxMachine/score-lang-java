package org.scorelang.value.array;

import org.scorelang.object.ScoreObject;
import org.scorelang.value.ScoreFloat;

public class ScoreFloatArray extends ScoreValueArray<ScoreFloat> {
    
    public ScoreFloatArray() {
        super();
    }
    
    public ScoreFloatArray(int initialSize) {
        super(initialSize);
        for (int i = 0; i < initialSize; i++)
        	push(new ScoreFloat(0));
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