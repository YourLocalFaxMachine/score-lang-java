package org.scorelang.compiler;

import static org.scorelang.lexer.ScoreToken.*;
import static org.scorelang.vm.ScoreOpCode.*;

import org.scorelang.ScoreException;
import org.scorelang.function.*;
import org.scorelang.lexer.ScoreLexer;
import org.scorelang.object.ScoreObject;
import org.scorelang.util.ScoreVector;
import org.scorelang.vm.ScoreInstruction;

import java.io.InputStream;

public class ScoreCompiler {
	
	private static final int EXPR	= 0;
	private static final int IDENT	= 1;
	
	private static final class ExpState {
		
		private int _type;
		
		public ExpState() {
		}
		
		public ExpState(ExpState other) {
			_type = other._type;
		}
		
	}
	
	private final class CompilerException extends ScoreException {
		
		public CompilerException(String message) {
			super(message + " (line " + _lexer.getLineNumber() + ")");
		}
		
	}
	
	// Things
	
	private boolean _semiOptional = true;
	
	// VARS
	
	private ScoreLexer _lexer;
	private ScoreFunctionState _func;
	private ExpState _es;
	
	// Tokens and stuff
	
	private int _pretoken, _token, _nexttoken;
	private String _prestring, _string, _nextstring;
	
	// Definitions
	
	private boolean _defining = false;
	private boolean _local = false, _static = false, _final = false;
	private String _typeString = "";
	
	//
	
	public ScoreCompiler(InputStream input) {
		_lexer = new ScoreLexer(input);
	}
	
	// Lexer
	
	private int lex() {
		_lexer.lex();
		_pretoken = _token;
		_prestring = _string;
		
		_token = _lexer.getToken();
		_string = _lexer.getString();
		_nexttoken = _lexer.nextToken();
		_nextstring = _lexer.nextString();
		
		// Check special cases
		if (_token == TK_BOOLNOT) {
			next();
			if (_nexttoken == TK_TYPEOF) {
				_lexer.lex();
				
				_token = TK_NOTTYPEOF;
				_string = _lexer.getString();
				_nexttoken = _lexer.nextToken();
				_nextstring = _lexer.nextString();
			}
		}
		
		return _token;
	}
	
	private int next() {
		_lexer.next();
		_nexttoken = _lexer.nextToken();
		_nextstring = _lexer.nextString();
		return _nexttoken;
	}
	
	// Token Stuff
	
	private void checkSemi() {
		if (_semiOptional) {
			if (_token == TK_SEMICOLON)
				lex();
		} else {
			if (_token != TK_SEMICOLON)
				throw new CompilerException("Expected ';'.");
			lex();
		}
	}
	
	private ScoreObject expect(int token) {
		if (_token != token)
			throw new CompilerException("Unexpected token (value " + _token + ").");
		lex();
		switch (token) {
			case TK_IDENT: return new ScoreObject(_string);
		}
		return null;
	}
	
	// Scope
	
	private void beginScope() {
		_func.addInst(SCOPEBEGIN);
	}
	
	private void endScope() {
		_func.addInst(SCOPEEND);
	}
	
	// Defining
	
	private void defStart() {
		_defining = true;
	}
	
	private void defTypeMod(boolean local, boolean stat, boolean fin) {
		_defining = true;
		_local = local;
		_static = stat;
		_final = fin;
	}
	
	private void defTypeString(String type) {
		_defining = true;
		_func.addInst(PUSH, _func.getStringValue(type));
		_typeString = type;
	}
	
	private void defEnd() {
		_defining = false;
		_local = false;
		_static = false;
		_final = false;
	}
	
	// Compile
	
	public ScoreObject compile() {
		_func = new ScoreFunctionState(0);
		_es = new ExpState();
		
		lex();
		
		while (_token != -1) {
			statement();
			if (_pretoken != TK_SEMICOLON && _pretoken != TK_CLOSEBRACE)
				checkSemi();
		}
		
		return new ScoreObject(new ScoreFunction(_func.buildProto()));
	}
	
	// Code Generation
	
	private void statement() {
		switch (_token) {
			case TK_IF:
				ifStatement();
				break;
			case TK_FOR:
				forStatement();
				break;
			case TK_WHILE:
				whileStatement();
				break;
			case TK_PRINT:
			case TK_PRINTLN: {
				int token = _token;
				int amt = 0; lex();
				if (_token != TK_SEMICOLON)
					amt = commaExpr();
				_func.addInst(token == TK_PRINT ? PRINT : PRINTLN, amt);
				break;
			}
			case TK_SLEEP:
				lex(); load();
				_func.addInst(SLEEP);
				break;
			case TK_OPENBRACE: {
				beginScope();
				lex(); statements(); expect(TK_CLOSEBRACE);
				endScope();
				break;
			}
			default:
				statementDeclMod(); // also calls expression if it needs to.
				break;
		}
	}
	
	// For modified declarations
	private void statementDeclMod() {
		// Check modifiers HERE
		statementDecl();
	}
	
	// For unmodified declarations
	private void statementDecl() {
		next(); // To check for all of the types
		if (_token == TK_IDENT && _nexttoken == TK_IDENT) {
			// _func.addInst(PUSH, _func.getStringValue(_string));
			// defStart();
			defTypeString(_string);
			lex();
		}
		expression();
	}
	
	private void statements() {
		while (_token != TK_CLOSEBRACE) { // && not default or case, for switch statements and all
			statement();
			if (_pretoken != TK_SEMICOLON && _pretoken != TK_CLOSEBRACE)
				checkSemi();
		}
	}
	
	private int commaExpr() {
		int n = 1;
		while (true) {
			expression(); // pushes the object to the stack
			if (_token != TK_COMMA)
				break;
			lex(); // Because it IS a comma!
			n++;
		}
		return n;
	}
	
	private void expression() {
		ExpState es = new ExpState(_es);
		_es._type = EXPR;
		
		factor();
		if (_defining && _es._type == EXPR)
			throw new CompilerException("Cannot use an expression as a variable name.");
		
		switch (_token) {
			case TK_EQUALS: {
				if (_es._type == EXPR)
					throw new CompilerException("Cannot assign to an expression.");
				
				_func.popInst(); // Pop the get inst thingy (get)
				lex(); factor();
				
				// Both require three things on the stack:
				// The type, name and value
				if (_defining) {
					// Assign outer
					if (_local)
						_func.addInst(ASSIGNL, _static ? 1 : 0, _final ? 1 : 0);
					else _func.addInst(ASSIGN, _static ? 1 : 0, _final ? 1 : 0);
					defEnd();
				} else {
					_func.addInst(SET);
				}
				break;
			}
			default: {
				if (_defining) {
					// DEFAULT!
					_func.popInst(); // Pop the get inst thingy (get)
					// For now push null, We'll do things soon
					switch (_typeString) {
						case "bool": _func.addInst(PUSH, _func.getBoolValue(false)); break;
						case "char": _func.addInst(PUSH, _func.getCharValue('\u0000')); break;
						case "float": _func.addInst(PUSH, _func.getNumericValue(0.0)); break;
						case "int": _func.addInst(PUSH, _func.getNumericValue(0)); break;
						case "string": _func.addInst(PUSH, _func.getStringValue("")); break;
						default: _func.addInst(PUSHNULL);
					}
					if (_local)
						_func.addInst(ASSIGNL, _static ? 1 : 0, _final ? 1 : 0);
					else _func.addInst(ASSIGN, _static ? 1 : 0, _final ? 1 : 0);
					defEnd();
				}
				break;
			}
		}
		
		_es = es;
	}
	
	private void load() {
		switch (_token) {
			case TK_MINUS:
				lex(); load();
				_func.addInst(UNM);
				_es._type = EXPR;
				break;
			case TK_OPENPAREN:
				lex(); expression();
				expect(TK_CLOSEPAREN);
				_es._type = EXPR;
				break;
			case TK_LESS:
				lex();
				_func.addInst(PUSH, _func.getValue(expect(TK_IDENT)));
				expect(TK_GREATER); load();
				_func.addInst(CAST);
				_es._type = EXPR;
				break;
			case TK_TYPEOF:
				lex(); load();
				_es._type = EXPR;
				_func.addInst(TYPEOF);
				break;
			case TK_BOOLNOT:
				lex(); load();
				_es._type = EXPR;
				_func.addInst(BOOLNOT);
				break;
			case TK_PLUSPLUS: { // this will always be BEFORE the thing:
				// do PRE plusplus
				lex(); load();
				if (_es._type == EXPR)
					throw new CompilerException("Cannot ++ an expression!");
				ScoreInstruction i0 = _func.popInst(), i1 = _func.popInst();
				_func.addInst(i1);
				_func.addInst(INC, _func.getNumericValue(1));
				_func.addInst(i1); 	_func.addInst(i0);
				break;
			}
			case TK_MINUSMINUS: { // this will always be BEFORE the thing:
				// do PRE minusminus
				lex(); load();
				if (_es._type == EXPR)
					throw new CompilerException("Cannot -- an expression!");
				ScoreInstruction i0 = _func.popInst(), i1 = _func.popInst();
				_func.addInst(i1);
				_func.addInst(INC, _func.getNumericValue(-1));
				_func.addInst(i1); 	_func.addInst(i0);
				break;
			}
			case TK_NOTNOT: { // this will always be BEFORE the thing:
				// do PRE notnot
				lex(); load();
				if (_es._type == EXPR)
					throw new CompilerException("Cannot !! an expression!");
				ScoreInstruction i0 = _func.popInst(), i1 = _func.popInst();
				_func.addInst(i1);
				_func.addInst(BOOLNOTNOT);
				_func.addInst(i1); 	_func.addInst(i0);
				break;
			}
			case TK_TRUE:
			case TK_FALSE:
				_func.addInst(PUSH, _func.getBoolValue(_token == TK_TRUE ? true : false));
				_es._type = EXPR;
				lex();
				break;
			case TK_CHARLITERAL:
				_func.addInst(PUSH, _func.getCharValue(_string.charAt(0)));
				_es._type = EXPR;
				lex();
				break;
			case TK_FLOATLITERAL:
				_func.addInst(PUSH, _func.getNumericValue(Double.parseDouble(_string)));
				_es._type = EXPR;
				lex();
				break;
			case TK_INTLITERAL:
				_func.addInst(PUSH, _func.getNumericValue(Long.parseLong(_string)));
				_es._type = EXPR;
				lex();
				break;
			case TK_STRINGLITERAL:
				_func.addInst(PUSH, _func.getStringValue(_string));
				_es._type = EXPR;
				lex();
				break;
			case TK_IDENT: {
				String varName = _string;
				_es._type = IDENT;
				_func.addInst(PUSH, _func.getStringValue(varName));
				_func.addInst(GET);
				lex();
				switch (_token) {
					case TK_PLUSPLUS:
						lex();
						_func.addInst(PUSH, _func.getStringValue(varName));
						_func.addInst(INC, _func.getNumericValue(1));
						break;
					case TK_MINUSMINUS:
						lex();
						_func.addInst(PUSH, _func.getStringValue(varName));
						_func.addInst(INC, _func.getNumericValue(-1));
						break;
					case TK_NOTNOT:
						lex();
						_func.addInst(PUSH, _func.getStringValue(varName));
						_func.addInst(BOOLNOTNOT);
						break;
					// default means nothing's there so we good ^_^
				}
				break;
			}
			default: throw new CompilerException("Unexpected token (value " + _token + ") when loading a value.");
		}
	}
	
	private void ifStatement() {
		boolean hasElse = false;
		
		lex(); expect(TK_OPENPAREN);
		
		// condition
		expression();
		expect(TK_CLOSEPAREN);
		
		int ifStart = _func.addInst(JMPF, 0); // 0 is the amount to jump so far
		
		beginScope();
		statement();
		if (_pretoken != TK_CLOSEBRACE && _pretoken != TK_SEMICOLON)
			checkSemi();
		endScope();
		
		int ifEnd = _func.getCurrentInstPos();
		
		if (_token == TK_ELSE) {
			hasElse = true;
			
			int elseStart = _func.addInst(JMP, 0); // 0 is the amount to jump so far
			
			beginScope();
			statement();
			if (_pretoken != TK_CLOSEBRACE && _pretoken != TK_SEMICOLON)
				checkSemi();
			endScope();
			
			_func.setInstArg(elseStart, 0, _func.getCurrentInstPos() - elseStart);
		}
		
		_func.setInstArg(ifStart, 0, ifEnd - ifStart + (hasElse ? 1 : 0));
	}
	
	private void forStatement() {
		beginScope();
		lex(); expect(TK_OPENPAREN);
		
		// Decl
		if (_token != TK_SEMICOLON)
			statementDecl();
		expect(TK_SEMICOLON);
		
		int jmppos = _func.getCurrentInstPos();
		int jmpfpos = -1;
		
		// Cond
		if (_token != TK_SEMICOLON) {
			expression();
			jmpfpos = _func.addInst(JMPF);
		}
		expect(TK_SEMICOLON);
		
		// Inc
		int incStart = _func.getCurrentInstPos() + 1;
		if (_token != TK_CLOSEPAREN)
			expression();
		expect(TK_CLOSEPAREN);
		
		int incEnd = _func.getCurrentInstPos();
		int incSize = (incEnd - incStart) + 1;
		
		ScoreVector<ScoreInstruction> inc = new ScoreVector<ScoreInstruction>();
		if (incSize > 0) {
			for (int i = 0; i < incSize; i++)
				inc.push(_func.getInstAt(incStart + i));
			_func.popInst(incSize);
		}
		
		// BREAKABLE
		
		statement();
		
		if (incSize > 0) {
			for (int i = 0; i < incSize; i++)
				_func.addInst(inc.get(i));
		}
		
		_func.addInst(JMP, jmppos - _func.getCurrentInstPos() - 1);
		if (jmpfpos > 0)
			_func.setInstArg(jmpfpos, 0, _func.getCurrentInstPos() - jmpfpos);
			
		endScope();
		
		// NO BREAKABLE
	}
	
	private void whileStatement() {
		lex(); expect(TK_OPENPAREN);
		
		int jmppos = _func.getCurrentInstPos();
		expression(); expect(TK_CLOSEPAREN);
		int jmpfpos = _func.addInst(JMPF, 0);
		
		statement();
		
		_func.addInst(JMP, jmppos - _func.getCurrentInstPos() - 1);
		_func.setInstArg(jmpfpos, 0, _func.getCurrentInstPos() - jmpfpos);
	}
	
	private void factor() {
		load();
		factor(0);
	}
	
	private void factor(int minp) {
		// Here we assume that the left has already been load()'ed
		// as we will be calling factor() first.
		while (isOperator(_token) && opLevel(_token) >= minp) {
			int op = _token; lex(); load(); // push the right hand to the stack.
			while (isOperator(_token) && opLevel(_token) > opLevel(op))
				factor(opLevel(_token));
			_func.addInst(getOpFromExpToken(op));
		}
	}
	
	// private void factor(int lhs, int minp) {
	// 	if (lhs == -1)
	// 		lhs = load();
	// 	while (isOperator(token) && opLevel(token) >= minp) {
	// 		lastop = token;
	// 		int op = token; lex();
	// 		int rhs = load(); // load the nex thing and store it's location
	// 		while (isOperator(token) && opLevel(token) > opLevel(op))
	// 			rhs = factor(rhs, opLevel(token));
	// 		func.addInst(getOpFromExpToken(op), lhs, lhs, rhs);
	// 	}
	// 	return lhs;
	// }
	
	private byte getOpFromExpToken(int token) {
		switch (token) {
			case TK_BOOLAND:	return BOOLAND;
			case TK_BOOLOR:		return BOOLOR;
			case TK_EQUALTO:	return EQUALTO;
			case TK_NOTEQUALTO:	return NOTEQUALTO;
			case TK_LESS:		return LESS;
			case TK_LESSEQ:		return LESSEQ;
			case TK_GREATER:	return GREATER;
			case TK_GREATEREQ:	return GREATEREQ;
			case TK_TYPEOF:		return ISTYPEOF;
			case TK_NOTTYPEOF:	return NOTTYPEOF;
			case TK_PLUS:		return ADD;
			case TK_MINUS:		return SUB;
			case TK_MUL:		return MUL;
			case TK_DIV:		return DIV;
			case TK_MOD:		return MOD;
			case TK_POW:		return POW;
			default:			return -1;
		}
	}
	
	private boolean isOperator(int token) {
		switch (token) {
			case TK_BOOLAND:
			case TK_BOOLOR:
			case TK_EQUALTO:
			case TK_NOTEQUALTO:
			case TK_LESS:
			case TK_GREATER:
			case TK_LESSEQ:
			case TK_GREATEREQ:
			case TK_TYPEOF:
			case TK_NOTTYPEOF:
			case TK_PLUS:
			case TK_MINUS:
			case TK_MUL:
			case TK_DIV:
			case TK_MOD:
			case TK_POW:
				return true;
		}
		return false;
	}
	
	private int opLevel(int token) {
		switch (token) {
			case TK_BOOLAND:
			case TK_BOOLOR:
				return 3;
			case TK_EQUALTO:
			case TK_NOTEQUALTO:
			case TK_LESS:
			case TK_GREATER:
			case TK_LESSEQ:
			case TK_GREATEREQ:
			case TK_TYPEOF:
			case TK_NOTTYPEOF:
				return 5;
			case TK_PLUS:
			case TK_MINUS:
				return 10;
			case TK_MUL:
			case TK_DIV:
			case TK_MOD:
				return 12;
			case TK_POW:
				return 15;
		}
		return 0;
	}
	
}