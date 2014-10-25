package org.scorelang.value.array;

import org.scorelang.object.ScoreObject;
import org.scorelang.value.ScoreChar;

public class ScoreCharArray extends ScoreValueArray<ScoreChar> {
    
    public ScoreCharArray() {
        super();
    }
    
    public ScoreCharArray(int initialSize) {
        super(initialSize);
        for (int i = 0; i < initialSize; i++)
        	push(new ScoreChar('\u0000'));
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