package org.scorelang.lexer;

import static org.scorelang.lexer.ScoreToken.*;

import java.util.HashMap;
import java.util.Collection;

import org.scorelang.ScoreException;

public class ScoreKeywords {
	
	private static class Keyword {
		
		private final String _id, _word;
		private final int _token;
		
		public Keyword(String id, String word, int token) {
			_id = id;
			_word = word;
			_token = token;
		}
		
	}
	
	private static final HashMap<String, Keyword> _keywords = new HashMap<String, Keyword>();
	
	private static final void addKeyword(String id, String word, int token) {
		_keywords.put(id, new Keyword(id, word, token));
	}
	
	public static final String getIDFromKeyword(String word) {
		if (word == null || word.trim().equals(""))
			return null;
		Collection<Keyword> v = _keywords.values();
		for (Keyword k : v)
			if (word.equals(k._word))
				return k._id;
		return null;
	}
	
	public static final String getKeyword(String id) {
		return _keywords.get(id)._word;
	}
	
	public static final int getToken(String id) {
		return _keywords.get(id)._token;
	}
	
	public static final boolean isKeyword(String word) {
		if (word == null || word.trim().equals(""))
			return false;
		Collection<Keyword> v = _keywords.values();
		for (Keyword k : v)
			if (word.equals(k._word))
				return true;
		return false;
	}
	
	static {
		addKeyword("var",		"var",		TK_IDENT);
		addKeyword("bool",		"bool",		TK_IDENT);
		addKeyword("char",		"char",		TK_IDENT);
		addKeyword("float",		"float",	TK_IDENT);
		addKeyword("int",		"int",		TK_IDENT);
		addKeyword("rout",		"rout",		TK_IDENT);
		addKeyword("string",	"string",	TK_IDENT);
		
		addKeyword("true",		"true",		TK_TRUE);
		addKeyword("false",		"false",	TK_FALSE);
		addKeyword("if",		"if",		TK_IF);
		addKeyword("else",		"else",		TK_ELSE);
		addKeyword("for",		"for",		TK_FOR);
		addKeyword("foreach",	"foreach",	TK_FOREACH);
		addKeyword("while",		"while",	TK_WHILE);
		addKeyword("break",		"break",	TK_BREAK);
		addKeyword("continue",	"continue",	TK_CONTINUE);
		addKeyword("new",		"new",		TK_NEW);
		addKeyword("print",		"print",	TK_PRINT);
		addKeyword("println",	"println",	TK_PRINTLN);
		addKeyword("error",		"error",	TK_ERROR);
		addKeyword("errorln",	"errorln",	TK_ERRORLN);
		addKeyword("typeof",	"typeof",	TK_TYPEOF);
		addKeyword("sleep",		"sleep",	TK_SLEEP);
		addKeyword("local",		"local",	TK_LOCAL);
		addKeyword("static",	"static",	TK_STATIC);
		addKeyword("final",		"final",	TK_FINAL);
		addKeyword("common",	"cp,,pm",	TK_COMMON);
	}
	
}