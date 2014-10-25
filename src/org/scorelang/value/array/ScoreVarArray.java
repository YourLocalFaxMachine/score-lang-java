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
	
	@Override
	public byte getType() {
		return ScoreObject.VAR_ARRAY;
	}
    
}