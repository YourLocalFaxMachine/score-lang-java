package org.scorelang.vm;

import static org.scorelang.lexer.ScoreToken.*;
import static org.scorelang.object.ScoreObject.*;
import static org.scorelang.vm.ScoreOpCode.*;

import org.scorelang.ScoreException;
import org.scorelang.function.ScoreFunction;
import org.scorelang.object.ScoreObject;
import org.scorelang.util.ScoreVector;
import org.scorelang.value.*;
import org.scorelang.value.array.*;

public class ScoreVM {
	
	private static final int CALL = 0x00;
	
	private static final class CallInfo {
		
		private ScoreFunction _func;
		private ScoreVector<ScoreInstruction> _inst;
		private ScoreVector<ScoreObject> _values;
		
	}
	
	public static final class Scope {
		
		private Scope _parent;
		
		private int _stackFloor; // the place the stack was at before this scope started.
		private ScoreTable _locals;
		
		public Scope(Scope parent, int stackFloor) {
			_parent = parent;
			_stackFloor = stackFloor;
			_locals = new ScoreTable();
		}
		
		public boolean hasVar(String name) {
			return _locals.contains(name);
		}
		
	}
	
	private ScoreVector<ScoreObject> _stack;
	private ScoreTable _root;
	private CallInfo _ci;
	private Scope _scope;
	
	public ScoreVM(int initStackSize) {
		_stack = new ScoreVector<ScoreObject>(initStackSize);
		_root = new ScoreTable();
		_ci = new CallInfo();
	}
	
	private void startCall(ScoreFunction func, int nargs) {
		if (func.getNumParams() != nargs)
			throw new ScoreException("Wrong number of parameters.");
		_ci._func = func;
		_ci._inst = func.getInstructions();
		_ci._values = func.getValues();
	}
	
	private int execute(ScoreFunction func, int nargs, int stackbase, int exeType) {
		if (func.getScope() == null)
			func.setScope(new Scope(_scope, _stack.size()));
		
		int retAmt = 0;
		
		switch (exeType) {
			case CALL:
				startCall(func, nargs);
				break;
		}
		
		ScoreVector<ScoreInstruction> inst = _ci._inst;
		
		for (int i = 0; i < inst.size(); i++) {
			ScoreInstruction in = inst.get(i);
			
			byte op = in.getOp();
			int arg0 = in.getArg0();
			int arg1 = in.getArg1();
			int arg2 = in.getArg2();
			
			switch (op) {
				// TODO: Scopes are not perfect yet, they just kinda work. I have to restructure this for functions and such
				// I'm thinking store the scope as part of the call info? When creating the function in the VM, store the
				// CURRENT scope in the function (the scope it was defined in) because that should give valid scope information.
				case SCOPEBEGIN: {
					// System.out.println("Beginning New Scope");
					_scope = new Scope(_scope, _stack.size());
					break;
				}
				case SCOPEEND: {
					// System.out.println("Ending Current Scope");
					if (_scope == null)
						throw new ScoreException("No scope to close!");
					_scope = _scope._parent;
					break;
				}
				case PUSH:
					push(_ci._values.get(arg0));
					break;
				case PUSHNULL:
					push(new ScoreObject());
					break;
				case POP:
					pop();
					break;
				case JMP:
					i += arg0;
					break;
				case JMPF:
					if (isFalse(pop()))
						i += arg0;
					break;
				case INC: {
					ScoreObject name = pop();
					String nameString = name.getString();
					ScoreTable t = getLocalTable(nameString);
					ScoreObject val = t.get(nameString);
					t.set(nameString, addop(val, _ci._values.get(arg0)));
					break;
				}
				case BOOLNOTNOT: {
					ScoreObject name = pop();
					String nameString = name.getString();
					ScoreTable t = getLocalTable(nameString);
					ScoreObject val = t.get(nameString);
					t.set(nameString, new ScoreObject(isFalse(val)));
					break;
				}
				case ASSIGN:
				case ASSIGNL: { // outers
					ScoreObject val = pop();
					ScoreObject name = pop();
					ScoreObject type = pop();
					// TODO arrays don't exist yet, so this sets everything to NOT AN ARRAY: FIX SOON
					if (op == ASSIGN && _scope == null) {
						_root.assign(name.getString(), val, type.getString(), arg0 == 1 ? true : false, arg1 == 1 ? true : false, arg2 == 1 ? true : false);
						// System.out.println("ASSIGN type " + type + " - name "  + name + " - val " + val);
					} else {
						_scope._locals.assign(name.getString(), val, type.getString(), arg0 == 1 ? true : false, arg1 == 1 ? true : false, arg2 == 1 ? true : false);
					}
					break;
				}
				case GET: { // outers
					ScoreObject name = pop();
					String nameString = name.getString();
					push(getLocalTable(nameString).get(name.getString()));
					// System.out.println("Getting " + name);
					break;
				}
				case GETINDEX: {
					ScoreObject idx = pop();
					ScoreObject val = pop(); // the object (gotten by GET)
					push(getindex(val, idx));
					break;
				}
				case SET: {
					ScoreObject val = pop();
					ScoreObject name = pop();
					String nameString = name.getString();
					getLocalTable(nameString).set(nameString, val);
					push(val); // push it back onto the stack
					// push the value back to the stack for chained things.
					break;
				}
				case SETINDEX: {
					break;
				}
				case SUBARRAY: {
					ScoreObject end = pop();
					ScoreObject start = pop();
					ScoreObject val = pop();
					push(subarray(val, start, end));
					break;
				}
				case CAST: {
					ScoreObject val = pop();
					ScoreObject type = pop();
					push(typecast(val, type.getString()));
					break;
				}
				case UNM:
					push(opunm(pop()));
					break;
				case GETLENGTH:
					push(lengthof(pop()));
					break;
				case SETLENGTH: {
					ScoreObject len = pop();
					ScoreObject val = pop();
					setlength(val, len);
					push(len);
					break;
				}
				case ADD:
				case SUB:
				case MUL:
				case DIV: {
					ScoreObject b = pop();
					ScoreObject a = pop();
					push(binop(op, a, b));
					break;
				}
				case EQUALTO: {
					ScoreObject b = pop();
					ScoreObject a = pop();
					push(new ScoreObject(isEqual(a, b)));
					break;
				}
				case NOTEQUALTO: {
					ScoreObject b = pop();
					ScoreObject a = pop();
					push(new ScoreObject(!isEqual(a, b)));
					break;
				}
				case BOOLNOT: {
					ScoreObject a = pop();
					push(new ScoreObject(isFalse(a)));
					break;
				}
				case LESS: {
					ScoreObject b = pop();
					ScoreObject a = pop();
					push(new ScoreObject(isLess(a, b)));
					break;
				}
				case LESSEQ: {
					ScoreObject b = pop();
					ScoreObject a = pop();
					push(new ScoreObject(isLessEq(a, b)));
					break;
				}
				case GREATER: {
					ScoreObject b = pop();
					ScoreObject a = pop();
					push(new ScoreObject(!isLessEq(a, b)));
					break;
				}
				case GREATEREQ: {
					ScoreObject b = pop();
					ScoreObject a = pop();
					push(new ScoreObject(!isLess(a, b)));
					break;
				}
				case TYPEOF: {
					ScoreObject a = pop();
					push(new ScoreObject(a.getTypeName()));
					break;
				}
				case NOTTYPEOF:
				case ISTYPEOF: {
					ScoreObject b = pop();
					ScoreObject a = pop();
					boolean isType = a.getTypeName().equals(b.getString());
					push(new ScoreObject(op == NOTTYPEOF ? !isType : isType));
					break;
				}
				case ABS: {
					ScoreObject val = pop();
					push(absop(val));
					break;
				}
				case MKARRAY: {
					ScoreObject[] vals = pop(arg1);
					ScoreObject t = _ci._values.get(arg0);
					ScoreObject s = pop();
					// check that the size is an integer
					if (!s.isInt())
						throw new ScoreException("Array sizes can only be integers.");
					int size = (int) s.getInt();
					if (size == -1)
						size = arg1;
					else if (size != arg1 && arg1 != -1)
						throw new ScoreException("Defined array size and default array length do not match.");
					String type = t.getString(); // must be string
					ScoreObject arr;
					switch (type) {
						case "bool":
							if (arg1 == -1) { // NO defaults
								arr = new ScoreObject(new ScoreBoolArray(size));
							} else {
								ScoreBool[] v = new ScoreBool[vals.length];
								for (int j = 0; j < vals.length; j++)
									v[j] = new ScoreBool(vals[vals.length - j - 1].getAsBool());
								arr = new ScoreObject(new ScoreBoolArray(v));
							}
							break;
						case "char":
							if (arg1 == -1) { // NO defaults
								arr = new ScoreObject(new ScoreCharArray(size));
							} else {
								ScoreChar[] v = new ScoreChar[vals.length];
								for (int j = 0; j < vals.length; j++)
									v[j] = new ScoreChar(vals[vals.length - j - 1].getAsChar());
								arr = new ScoreObject(new ScoreCharArray(v));
							}
							break;
						case "float":
							if (arg1 == -1) { // NO defaults
								arr = new ScoreObject(new ScoreFloatArray(size));
							} else {
								ScoreFloat[] v = new ScoreFloat[vals.length];
								for (int j = 0; j < vals.length; j++)
									v[j] = new ScoreFloat(vals[vals.length - j - 1].getAsFloat());
								arr = new ScoreObject(new ScoreFloatArray(v));
							}
							break;
						case "int":
							if (arg1 == -1) { // NO defaults
								arr = new ScoreObject(new ScoreIntArray(size));
							} else {
								ScoreInt[] v = new ScoreInt[vals.length];
								for (int j = 0; j < vals.length; j++)
									v[j] = new ScoreInt(vals[vals.length - j - 1].getAsInt());
								arr = new ScoreObject(new ScoreIntArray(v));
							}
							break;
						case "string":
							if (arg1 == -1) { // NO defaults
								arr = new ScoreObject(new ScoreStringArray(size));
							} else {
								ScoreString[] v = new ScoreString[vals.length];
								for (int j = 0; j < vals.length; j++)
									v[j] = new ScoreString(vals[vals.length - j - 1].getAsString());
								arr = new ScoreObject(new ScoreStringArray(v));
							}
							break;
						default: throw new ScoreException("Cannot create an array of type " + type + ".");
					}
					push(arr);
					break;
				}
				case PRINT:
				case PRINTLN:
					System.out.print(toPrintString(arg0, op == PRINTLN));
					break;
				case ERROR:
				case ERRORLN:
					System.err.print(toPrintString(arg0, op == ERRORLN));
					break;
				case SLEEP:
					try {
						Thread.sleep((int) (pop().getAsFloat() * 1000));
					} catch (Exception e) {
						e.printStackTrace();
					}
			}
		}
		
		return retAmt;
	}
	
	public int call(ScoreObject obj, int params, int stackbase) {
		switch (obj.getType()) {
			case ROUT:
				return execute(obj.getRout(), params, stackbase, CALL);
		}
		return 0;
	}
	
	private ScoreTable getLocalTable(String name) {
		ScoreTable t = _root;
		Scope s = _scope;
		for (;;) {
			if (s == null) {
				t = _root;
				break;
			}
			if (s.hasVar(name)) {
				t = s._locals;
				break;
			} else
				s = s._parent;
		}
		return t;
	}
	
	// Object manipulations
	
	private String toPrintString(int amt, boolean ln) {
		StringBuilder sb = new StringBuilder();
		for (int j = 0; j < amt; j++) {
			sb.append(asString(get(-(amt - j))));
			if (j < amt - 1)
				sb.append(' ');
		}
		if (ln)
			sb.append('\n');
		pop(amt);
		return sb.toString();
	}
	
	private String asString(ScoreObject obj) {
		switch (obj.getType()) {
			case BOOL:
			case CHAR:
			case FLOAT:
			case INT:
			case ROUT:
			case STRING:
			case VAR_ARRAY:
			case BOOL_ARRAY:
			case CHAR_ARRAY:
			case FLOAT_ARRAY:
			case INT_ARRAY:
			case ROUT_ARRAY:
			case STRING_ARRAY:
				return obj.valueString();
		}
		return "null";
	}
	
	// Operations
	
	private boolean isEqual(ScoreObject a, ScoreObject b) {
		if (a == b)
			return true;
		// Do a numeric check first then do the equals check
		if (a.isNumeric() && b.isNumeric())
			return a.getAsFloat() == b.getAsFloat();
		else if (a.isString() && b.isString())
			return a.getString().equals(b.getString());
		else if (a.isBool() && b.isBool())
			return a.getBool() == b.getBool();
		else if (a.getType() == b.getType())
			return a.equals(b);
		return false;
	}
	
	private boolean isLess(ScoreObject a, ScoreObject b) {
		if (a.isNumeric() && b.isNumeric())
			return a.getAsFloat() < b.getAsFloat();
		throw new ScoreException("Cannot compare those two types (" + a.getTypeName() + " and " + b.getTypeName() + ").");
	}
	
	private boolean isLessEq(ScoreObject a, ScoreObject b) {
		if (a.isNumeric() && b.isNumeric())
			return a.getAsFloat() <= b.getAsFloat();
		throw new ScoreException("Cannot compare those two types (" + a.getTypeName() + " and " + b.getTypeName() + ").");
	}
	
	private boolean isFalse(ScoreObject a) {
		if (a.isBool())
			return !a.getBool();
		else if (a.isNumeric())
			return a.getAsFloat() <= 0.0;
		else return a.isNull();
	}
	
	private ScoreObject binop(byte op, ScoreObject a, ScoreObject b) {
		switch (op) {
			case ADD: return addop(a, b);
			case SUB: return subop(a, b);
			case MUL: return mulop(a, b);
			case DIV: return divop(a, b);
		}
		throw new ScoreException("Invalid operation.");
	}
	
	private ScoreObject addop(ScoreObject a, ScoreObject b) {
		if (a.isNumeric() && b.isNumeric()) {
			if (a.isFloat() || b.isFloat())
				return new ScoreObject(a.getAsFloat() + b.getAsFloat());
			// one is an int?
			else if (a.isInt() || b.isInt())
				return new ScoreObject(a.getAsInt() + b.getAsInt());
			// must both be chars then
			else return new ScoreObject(a.getChar() + b.getChar());
		} else if (a.isString() || b.isString())
			return new ScoreObject(a.getAsString() + b.getAsString());
		throw new ScoreException("Cannot add type " + b.getTypeName() + " to type " + a.getTypeName() + ".");
	}
	
	private ScoreObject subop(ScoreObject a, ScoreObject b) {
		if (a.isNumeric() && b.isNumeric()) {
			if (a.isFloat() || b.isFloat())
				return new ScoreObject(a.getAsFloat() - b.getAsFloat());
			// one is an int?
			else if (a.isInt() || b.isInt())
				return new ScoreObject(a.getAsInt() - b.getAsInt());
			// must both be chars then
			else return new ScoreObject(a.getChar() - b.getChar());
		}
		throw new ScoreException("Cannot add type " + b.getTypeName() + " to type " + a.getTypeName() + ".");
	}
	
	private ScoreObject mulop(ScoreObject a, ScoreObject b) {
		if (a.isNumeric() && b.isNumeric()) {
			if (a.isFloat() || b.isFloat())
				return new ScoreObject(a.getAsFloat() * b.getAsFloat());
			// must both be ints then
			else if (a.isInt() || b.isInt())
				return new ScoreObject(a.getAsInt() * b.getAsInt());
		} else if (a.isString() || b.isString()) {
			if (a.isString() && b.isInt())
				return new ScoreObject(repString(a.getString(), b.getInt()));
			else if (a.isInt() && b.isString())
				return new ScoreObject(repString(b.getString(), a.getInt()));
		}
		throw new ScoreException("Cannot add type " + b.getTypeName() + " to type " + a.getTypeName() + ".");
	}
	
	private ScoreObject divop(ScoreObject a, ScoreObject b) {
		if (a.isNumeric() && b.isNumeric()) {
			if (a.isFloat() || b.isFloat())
				return new ScoreObject(a.getAsFloat() / b.getAsFloat());
			// must both be int then
			else if (a.isInt() && b.isInt())
				return new ScoreObject(a.getInt() / b.getInt());
		}
		throw new ScoreException("Cannot add type " + b.getTypeName() + " to type " + a.getTypeName() + ".");
	}
	
	private ScoreObject absop(ScoreObject val) {
		if (val.isNumeric()) {
			if (val.isFloat()) {
				double f = val.getFloat();
				return new ScoreObject(f <= 0.0 ? 0.0 - f : f);
			} else if (val.isInt()) {
				long l = val.getInt(); 
				return new ScoreObject(l < 0 ? -l : l);
			} else
				throw new ScoreException("Cannot take the absolute value of that numeric value (" + val.getTypeName() + ").");
		} else
			throw new ScoreException("Currently can only take the absolute value of numbers.");
	}
	
	private ScoreObject typecast(ScoreObject val, String type) {
		switch (type) {
			case "bool": return new ScoreObject(val.getAsBool());
			case "char": return new ScoreObject(val.getAsChar());
			case "float": return new ScoreObject(val.getAsFloat());
			case "int": return new ScoreObject(val.getAsInt());
			case "string": return new ScoreObject(val.getAsString());
			// Add type casting to objects
			default: throw new ScoreException("Type does not exist (yet)!");
		}
	}
	
	private ScoreObject opunm(ScoreObject val) {
		String vType = val.getTypeName();
		switch (vType) {
			case "char": return new ScoreObject(-val.getAsChar());
			case "float": return new ScoreObject(-val.getAsFloat());
			case "int": return new ScoreObject(-val.getAsInt());
			// Add type casting to objects
			default: throw new ScoreException("Cannot perform unary minus on type " + vType + "!");
		}
	}
	
	private ScoreObject getindex(ScoreObject val, ScoreObject idx) {
		// do indexing of arrays, then check types and stuff later when I add them.
		if (val.isArray()) {
			if (!idx.isInt())
				throw new ScoreException("Can only index an array with an int.");
			return new ScoreObject(((ScoreValueArray) val.getValue()).get((int) idx.getInt()));
		}
		return new ScoreObject();
	}
	
	private ScoreObject lengthof(ScoreObject val) {
		// do indexing of arrays, then check types and stuff later when I add them.
		if (val.isArray())
			return new ScoreObject(((ScoreValueArray) val.getValue()).size());
		return new ScoreObject();
	}
	
	private void setlength(ScoreObject val, ScoreObject len) {
		if (val.isArray())
			((ScoreValueArray) val.getValue()).setLength((int) len.getInt());
		else throw new ScoreException("Cannot set the length of '" + val.getTypeName() + "'.");
	}
	
	private ScoreObject subarray(ScoreObject val, ScoreObject start, ScoreObject end) {
		if (val.isArray())
			return ((ScoreValueArray) val.getValue()).subArray((int) start.getInt(), (int) end.getInt());
		else throw new ScoreException("Cannot get a sub-array of '" + val.getTypeName() + "'.");
	}
	
	private String repString(String str, long amt) {
		StringBuilder sb = new StringBuilder();
		for (long i = 0; i < amt; i++)
			sb.append(str);
		return sb.toString();
	}
	
	// Stack operations
	
	public int top() {
		return _stack.size();
	}
	
	public void push(ScoreObject obj) {
		_stack.push(obj);
	}
	
	public ScoreObject pop() {
		return _stack.pop();
	}
	
	// The array is in reverse order from the top-down
	public ScoreObject[] pop(int n) {
		if (n < 1)
			return new ScoreObject[0];
		ScoreObject[] res = new ScoreObject[n];
		for (int i = 0; i < n; i++)
			res[i] = pop();
		return res;
	}
	
	public ScoreObject get(int idx) {
		if (idx < 0)
			idx = _stack.size() + idx;
		return _stack.get(idx);
	}
	
}