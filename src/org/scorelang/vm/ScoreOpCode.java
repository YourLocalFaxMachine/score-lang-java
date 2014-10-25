package org.scorelang.vm;

public class ScoreOpCode {
	
	public static final byte PUSH		= 0x00;
	public static final byte PUSHNULL	= 0x01;
	public static final byte POP		= 0x02;
	public static final byte ASSIGN		= 0x03;
	public static final byte ASSIGNL	= 0x04;
	public static final byte SET		= 0x05;
	public static final byte SETL		= 0x06;
	public static final byte GET		= 0x07;
	public static final byte GETL		= 0x08;
	
	public static final byte ADD		= 0x10;
	public static final byte MUL		= 0x11;
	public static final byte SUB		= 0x12;
	public static final byte DIV		= 0x13;
	public static final byte LESS		= 0x14;
	public static final byte LESSEQ		= 0x15;
	public static final byte GREATER	= 0x16;
	public static final byte GREATEREQ	= 0x17;
	public static final byte EQUALTO	= 0x18;
	public static final byte NOTEQUALTO	= 0x19;
	public static final byte TYPEOF		= 0x1A;
	public static final byte ISTYPEOF	= 0x1B;
	public static final byte NOTTYPEOF	= 0x1C;
	public static final byte MOD		= 0x1D;
	public static final byte POW		= 0x1E;
	
	public static final byte BOOLOR		= 0x20;
	public static final byte BOOLAND	= 0x21;
	public static final byte CAST		= 0x22;
	public static final byte UNM		= 0x23;
	
	public static final byte PRINT		= 0x30;
	public static final byte PRINTLN	= 0x31;
	public static final byte ERROR		= 0x32;
	public static final byte ERRORLN	= 0x33;
	public static final byte SLEEP		= 0x34;
	
	public static final byte SCOPEBEGIN	= 0x40;
	public static final byte SCOPEEND	= 0x41;
	public static final byte JMP		= 0x42;
	public static final byte JMPF		= 0x43;
	public static final byte JMPT		= 0x44;
	public static final byte INC		= 0x45;
	public static final byte BOOLNOT	= 0x46;
	public static final byte BOOLNOTNOT	= 0x47;
	public static final byte ABS		= 0x48;
	
}