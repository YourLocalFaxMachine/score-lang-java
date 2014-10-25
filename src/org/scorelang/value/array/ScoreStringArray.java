package org.scorelang.value.array;

import org.scorelang.object.ScoreObject;
import org.scorelang.value.ScoreString;

public class ScoreStringArray extends ScoreValueArray<ScoreString> {
    
    public ScoreStringArray() {
        super();
    }
    
    public ScoreStringArray(int initialSize) {
        super(initialSize);
        for (int i = 0; i < initialSize; i++)
        	push(new ScoreString());
    }
    
    public ScoreStringArray(ScoreString[] values) {
        this();
        set(values);
    }
    
    public ScoreStringArray(String[] values) {
        this();
        set(values);
    }
	
	@Override
	public byte getType() {
		return ScoreObject.STRING_ARRAY;
	}
	
	public ScoreString[] get() {
	    return toArray(new ScoreString[size()]);
	}
	
	public String[] getStrings() {
		ScoreString[] v = get();
	    String[] res = new String[v.length];
	    for (int i = 0; i < v.length; i++)
	    	res[i] = v[i].getString();
	    return res;
	}
	
	public void set(ScoreString[] values) {
	    clear();
	    for (int i = 0; i < values.length; i++)
	        push(values[i]);
	}
	
	public void set(String[] values) {
	    clear();
	    for (int i = 0; i < values.length; i++)
	        push(new ScoreString(values[i]));
	}
    
}