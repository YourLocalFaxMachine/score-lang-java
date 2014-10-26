package org.scorelang.lexer;

import static org.scorelang.lexer.ScoreToken.*;
import static org.scorelang.lexer.ScoreKeywords.*;

import java.io.InputStream;

import org.scorelang.ScoreException;

public class ScoreLexer {
	
	private InputStream _input;
	private int _lineNumber = 1;
	
	private int[] _tokens; // Current and next
	private String[] _strings;
	
	private char _val;
	
	private StringBuilder _sb;
	
	public ScoreLexer(InputStream input) {
		_input = input;
		
		_tokens = new int[2];
		_strings = new String[2];
		
		_tokens[1] = -1;
		
		_sb = new StringBuilder();
		
		read();
	}
	
	private void read() {
		try {
			_val = (char) _input.read();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void prepareString() {
		_sb.delete(0, _sb.length());
		_sb.trimToSize();
	}
	
	// Lexing
	
	public void lex() {
		if (_tokens[1] != -1) {
			_tokens[0] = _tokens[1]; _tokens[1] = -1;
			_strings[0] = _strings[1]; _strings[1] = null;
		} else {
			_tokens[0] = lexToken();
			_strings[0] = _sb.toString();
		}
	}
	
	public void next() {
		if (_tokens[1] != -1)
			return; // Already have the next one.
		_tokens[1] = lexToken();
		_strings[1] = _sb.toString();
	}
	
	private int lexToken() {
		while (_val != (char) -1) {
			switch (_val) {
				case '\n':
					_lineNumber++;
				case '\r': case '\t': case ' ':
					read();
					continue;
				case '\'':
					return readChar();
				case '@':
					read();
					if (_val == '"')
						return readString(true);
					throw new ScoreException("Unexpected character '@'");
				case '"':
					return readString(false);
				case ';':
					read();
					return TK_SEMICOLON;
				case ',':
					read();
					return TK_COMMA;
				case '=':
					read();
					if (_val == '=') {
						read();
						return TK_EQUALTO;
					}
					return TK_EQUALS;
				case '&':
					read();
					if (_val == '&') {
						read();
						return TK_BOOLAND;
					} else if (_val == '=') {
						read();
						return TK_ANDEQ;
					}
					return TK_AND;
				case '|':
					read();
					if (_val == '|') {
						read();
						return TK_BOOLOR;
					} else if (_val == '=') {
						read();
						return TK_OREQ;
					}
					return TK_PIPE;
				case '!':
					read();
					if (_val == '=') {
						read();
						return TK_NOTEQUALTO;
					} else if (_val == '!') {
						read();
						return TK_NOTNOT;
					}
					return TK_BOOLNOT;
				case '#':
					read();
					return TK_HASH;
				case '~':
					read();
					return TK_TILDE;
				case '+':
					read();
					if (_val == '=') {
						read();
						return TK_PLUSEQ;
					} else if (_val == '+') {
						read();
						return TK_PLUSPLUS;
					}
					return TK_PLUS;
				case '-':
					read();
					if (_val == '=') {
						read();
						return TK_MINUSEQ;
					} else if (_val == '-') {
						read();
						return TK_MINUSMINUS;
					}
					return TK_MINUS;
				case '*':
					read();
					if (_val == '=') {
						read();
						return TK_MULEQ;
					}
					return TK_MUL;
				case '/':
					read();
					if (_val == '=') {
						read();
						return TK_DIVEQ;
					} else if (_val == '/') {
						readLineComment();
						continue;
					} else if (_val == '*') {
						readBlockComment();
						continue;
					}
					return TK_DIV;
				case '(':
					read();
					return TK_OPENPAREN;
				case ')':
					read();
					return TK_CLOSEPAREN;
				case '{':
					read();
					return TK_OPENBRACE;
				case '}':
					read();
					return TK_CLOSEBRACE;
				case '[':
					read();
					if (_val == ']') {
						read();
						return TK_BRACKETS;
					}
					return TK_OPENBRACKET;
				case ']':
					read();
					return TK_CLOSEBRACKET;
				case '<':
					read();
					if (_val == '=') {
						read();
						return TK_LESSEQ;
					}
					return TK_LESS;
				case '>':
					read();
					if (_val == '=') {
						read();
						return TK_GREATEREQ;
					}
					return TK_GREATER;
				default:
					if (Character.isDigit(_val) || _val == '.')
						return readNumber();
					else if (Character.isLetter(_val) || _val == '_')
						return readIdent();
					throw new ScoreException("Unexpected token: " + _val);
			}
		}
		return -1;
	}
	
	// Lex Literals
	
	private char escape(char v) {
		switch (v) {
			case '\\': return '\\';
			case 'b':  return '\b';
			case 't':  return '\t';
			case 'n':  return '\n';
			case 'f':  return '\f';
			case 'r':  return '\r';
			case '0':  return '\0';
			case '"':  return '"';
			case '\'': return '\'';
			default: throw new ScoreException("Unrecognized excape char '\\" + _val + "'");
		}
	}
	
	private int readChar() {
		read();
		prepareString();
		if (_val == '\\') {
			read();
			_sb.append(escape(_val));
		}
		_sb.append(_val);
		read();
		if (_val != '\'')
			throw new ScoreException("Unfinished char.");
		read();
		return TK_CHARLITERAL;
	}
	
	private void readLineComment() {
		do {
			read();
		} while (_val != '\n' && _val != (char) -1);
		read();
		_lineNumber++;
	}
	
	private void readBlockComment() {
		read();
		boolean done = false;
		do {
			switch (_val) {
				case (char) -1:
					done = true;
					break;
				case '*':
					read();
					if (_val == '/') {
						done = true;
						read();
					}
					continue;
				case '\n':
					_lineNumber++;
				default:
					read();
			}
		} while (!done); // the end of the string
	}
	
	private int readString(boolean verbatim) {
		read();
		prepareString();
		do {
			// check escapes
			switch (_val) {
				case (char) -1:
					throw new ScoreException("Unfinished string");
				case '\n':
				case '\r':
					if (!verbatim)
						throw new ScoreException("Cannot have a line break in a single-line string.");
					_sb.append(_val);
					break;
				case '\\':
					read();
					_sb.append(escape(_val));
					break;
				default:
					_sb.append(_val);
			}
			read();
		} while (_val != '\"'); // the end of the string
		read();
		return TK_STRINGLITERAL;
	}
	
	private int readNumber() {
		prepareString();
		boolean isFloat = false;
		do {
			if (_val == '.')
				isFloat = true;
			_sb.append(_val);
			read();
		} while (Character.isDigit(_val) || _val == '.');
		if (_val == 'f') {
			isFloat = true;
			read();
		}
		if (isFloat)
			return TK_FLOATLITERAL;
		return TK_INTLITERAL;
	}
	
	private int readIdent() {
		prepareString();
		do {
			_sb.append(_val);
			read();
		} while (Character.isLetterOrDigit(_val) || _val == '_'); // while it's a valid identifier character, get characters it.
		String temp = _sb.toString();
		if (ScoreKeywords.isKeyword(temp))
			return ScoreKeywords.getToken(temp);
		return TK_IDENT;
	}
	
	// Getters
	
	public int getToken() {
		return _tokens[0];
	}
	
	public String getString() {
		return _strings[0];
	}
	
	public int nextToken() {
		return _tokens[1];
	}
	
	public String nextString() {
		return _strings[1];
	}
	
	public int getLineNumber() {
		return _lineNumber;
	}
	
}