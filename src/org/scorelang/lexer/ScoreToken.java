package org.scorelang.lexer;

public class ScoreToken {
	
	public static final boolean isModifier(int token) {
		switch (token) {
			case TK_LOCAL:
			case TK_STATIC:
			case TK_FINAL:
			case TK_COMMON:
				return true;
			default: return false;
		}
	}
	
	public static final int TK_IDENT			= 0x00;
	public static final int TK_PRINT			= 0x01;
	public static final int TK_PRINTLN			= 0x02;
	public static final int TK_ERROR			= 0x03;
	public static final int TK_ERRORLN			= 0x04;
	public static final int TK_CHARLITERAL		= 0x06;
	public static final int TK_FLOATLITERAL		= 0x07;
	public static final int TK_INTLITERAL		= 0x08;
	public static final int TK_STRINGLITERAL	= 0x09;
	public static final int TK_SEMICOLON		= 0x0A;
	public static final int TK_COMMA			= 0x0B;
	public static final int TK_BOOLAND			= 0x0C;
	public static final int TK_BOOLOR			= 0x0D;
	public static final int TK_EQUALTO			= 0x0E;
	public static final int TK_NOTEQUALTO		= 0x0F;
	public static final int TK_LESS				= 0x10;
	public static final int TK_GREATER			= 0x11;
	public static final int TK_LESSEQ			= 0x12;
	public static final int TK_GREATEREQ		= 0x13;
	public static final int TK_TYPEOF			= 0x14;
	public static final int TK_PLUS				= 0x15;
	public static final int TK_MINUS			= 0x16;
	public static final int TK_MUL				= 0x17;
	public static final int TK_DIV				= 0x18;
	public static final int TK_MOD				= 0x19;
	public static final int TK_POW				= 0x1A;
	public static final int TK_OPENPAREN		= 0x1B;
	public static final int TK_CLOSEPAREN		= 0x1C;
	public static final int TK_OPENBRACE		= 0x1D;
	public static final int TK_CLOSEBRACE		= 0x1E;
	public static final int TK_BOOLNOT			= 0x1F;
	public static final int TK_EQUALS			= 0x20;
	public static final int TK_IF				= 0x21;
	public static final int TK_ELSE				= 0x22;
	public static final int TK_FOR				= 0x23;
	public static final int TK_FOREACH			= 0x24;
	public static final int TK_TRUE				= 0x25;
	public static final int TK_FALSE			= 0x26;
	public static final int TK_NOTTYPEOF		= 0x27;
	public static final int TK_PLUSPLUS			= 0x28;
	public static final int TK_MINUSMINUS		= 0x29;
	public static final int TK_PLUSEQ			= 0x2A;
	public static final int TK_MINUSEQ			= 0x2B;
	public static final int TK_MULEQ			= 0x2C;
	public static final int TK_DIVEQ			= 0x2D;
	public static final int TK_MODEQ			= 0x2E;
	public static final int TK_POWEQ			= 0x2F;
	public static final int TK_NOTNOT			= 0x30;
	public static final int TK_WHILE			= 0x31;
	public static final int TK_SLEEP			= 0x32;
	public static final int TK_AND				= 0x33;
	public static final int TK_PIPE				= 0x34;
	public static final int TK_ANDEQ			= 0x35;
	public static final int TK_OREQ				= 0x36;
	public static final int TK_OPENBRACKET		= 0x37;
	public static final int TK_CLOSEBRACKET		= 0x38;
	public static final int TK_BREAK			= 0x39;
	public static final int TK_CONTINUE			= 0x3A;
	public static final int TK_NEW				= 0x3B;
	public static final int TK_HASH				= 0x3C;
	public static final int TK_LOCAL			= 0x3D;
	public static final int TK_STATIC			= 0x3E;
	public static final int TK_FINAL			= 0x3F;
	public static final int TK_COMMON			= 0x40;
	
}