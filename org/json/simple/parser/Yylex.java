/*     */ package org.json.simple.parser;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.Reader;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class Yylex
/*     */ {
/*     */   public static final int YYEOF = -1;
/*     */   private static final int ZZ_BUFFERSIZE = 16384;
/*     */   public static final int YYINITIAL = 0;
/*     */   public static final int STRING_BEGIN = 2;
/*  23 */   private static final int[] ZZ_LEXSTATE = new int[] { 0, 0, 1, 1 };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String ZZ_CMAP_PACKED = "\t\000\001\007\001\007\002\000\001\007\022\000\001\007\001\000\001\t\b\000\001\006\001\031\001\002\001\004\001\n\n\003\001\032\006\000\004\001\001\005\001\001\024\000\001\027\001\b\001\030\003\000\001\022\001\013\002\001\001\021\001\f\005\000\001\023\001\000\001\r\003\000\001\016\001\024\001\017\001\020\005\000\001\025\001\000\001\026ﾂ\000";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  40 */   private static final char[] ZZ_CMAP = zzUnpackCMap("\t\000\001\007\001\007\002\000\001\007\022\000\001\007\001\000\001\t\b\000\001\006\001\031\001\002\001\004\001\n\n\003\001\032\006\000\004\001\001\005\001\001\024\000\001\027\001\b\001\030\003\000\001\022\001\013\002\001\001\021\001\f\005\000\001\023\001\000\001\r\003\000\001\016\001\024\001\017\001\020\005\000\001\025\001\000\001\026ﾂ\000");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  45 */   private static final int[] ZZ_ACTION = zzUnpackAction();
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String ZZ_ACTION_PACKED_0 = "\002\000\002\001\001\002\001\003\001\004\003\001\001\005\001\006\001\007\001\b\001\t\001\n\001\013\001\f\001\r\005\000\001\f\001\016\001\017\001\020\001\021\001\022\001\023\001\024\001\000\001\025\001\000\001\025\004\000\001\026\001\027\002\000\001\030";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static int[] zzUnpackAction() {
/*  55 */     int[] result = new int[45];
/*  56 */     int offset = 0;
/*  57 */     offset = zzUnpackAction("\002\000\002\001\001\002\001\003\001\004\003\001\001\005\001\006\001\007\001\b\001\t\001\n\001\013\001\f\001\r\005\000\001\f\001\016\001\017\001\020\001\021\001\022\001\023\001\024\001\000\001\025\001\000\001\025\004\000\001\026\001\027\002\000\001\030", offset, result);
/*  58 */     return result;
/*     */   }
/*     */   
/*     */   private static int zzUnpackAction(String packed, int offset, int[] result) {
/*  62 */     int i = 0;
/*  63 */     int j = offset;
/*  64 */     int l = packed.length();
/*  65 */     label10: while (i < l) {
/*  66 */       int count = packed.charAt(i++);
/*  67 */       int value = packed.charAt(i++); while (true)
/*  68 */       { result[j++] = value; if (--count <= 0)
/*     */           continue label10;  } 
/*  70 */     }  return j;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  77 */   private static final int[] ZZ_ROWMAP = zzUnpackRowMap();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String ZZ_ROWMAP_PACKED_0 = "\000\000\000\033\0006\000Q\000l\000\0006\000¢\000½\000Ø\0006\0006\0006\0006\0006\0006\000ó\000Ď\0006\000ĩ\000ń\000ş\000ź\000ƕ\0006\0006\0006\0006\0006\0006\0006\0006\000ư\000ǋ\000Ǧ\000Ǧ\000ȁ\000Ȝ\000ȷ\000ɒ\0006\0006\000ɭ\000ʈ\0006";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static int[] zzUnpackRowMap() {
/*  88 */     int[] result = new int[45];
/*  89 */     int offset = 0;
/*  90 */     offset = zzUnpackRowMap("\000\000\000\033\0006\000Q\000l\000\0006\000¢\000½\000Ø\0006\0006\0006\0006\0006\0006\000ó\000Ď\0006\000ĩ\000ń\000ş\000ź\000ƕ\0006\0006\0006\0006\0006\0006\0006\0006\000ư\000ǋ\000Ǧ\000Ǧ\000ȁ\000Ȝ\000ȷ\000ɒ\0006\0006\000ɭ\000ʈ\0006", offset, result);
/*  91 */     return result;
/*     */   }
/*     */   
/*     */   private static int zzUnpackRowMap(String packed, int offset, int[] result) {
/*  95 */     int i = 0;
/*  96 */     int j = offset;
/*  97 */     int l = packed.length();
/*  98 */     while (i < l) {
/*  99 */       int high = packed.charAt(i++) << 16;
/* 100 */       result[j++] = high | packed.charAt(i++);
/*     */     } 
/* 102 */     return j;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 108 */   private static final int[] ZZ_TRANS = new int[] { 2, 2, 3, 4, 2, 2, 2, 5, 2, 6, 2, 2, 7, 8, 2, 9, 2, 2, 2, 2, 2, 10, 11, 12, 13, 14, 15, 16, 16, 16, 16, 16, 16, 16, 16, 17, 18, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 4, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 4, 19, 20, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 20, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 5, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 21, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 22, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 23, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 16, 16, 16, 16, 16, 16, 16, 16, -1, -1, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, -1, -1, -1, -1, -1, -1, -1, -1, 24, 25, 26, 27, 28, 29, 30, 31, 32, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 33, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 34, 35, -1, -1, 34, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 36, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 37, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 38, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 39, -1, 39, -1, 39, -1, -1, -1, -1, -1, 39, 39, -1, -1, -1, -1, 39, 39, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 33, -1, 20, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 20, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 35, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 38, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 40, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 41, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 42, -1, 42, -1, 42, -1, -1, -1, -1, -1, 42, 42, -1, -1, -1, -1, 42, 42, -1, -1, -1, -1, -1, -1, -1, -1, -1, 43, -1, 43, -1, 43, -1, -1, -1, -1, -1, 43, 43, -1, -1, -1, -1, 43, 43, -1, -1, -1, -1, -1, -1, -1, -1, -1, 44, -1, 44, -1, 44, -1, -1, -1, -1, -1, 44, 44, -1, -1, -1, -1, 44, 44, -1, -1, -1, -1, -1, -1, -1, -1 };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final int ZZ_UNKNOWN_ERROR = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final int ZZ_NO_MATCH = 1;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final int ZZ_PUSHBACK_2BIG = 2;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 185 */   private static final String[] ZZ_ERROR_MSG = new String[] { "Unkown internal scanner error", "Error: could not match input", "Error: pushback value was too large" };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 194 */   private static final int[] ZZ_ATTRIBUTE = zzUnpackAttribute();
/*     */   
/*     */   private static final String ZZ_ATTRIBUTE_PACKED_0 = "\002\000\001\t\003\001\001\t\003\001\006\t\002\001\001\t\005\000\b\t\001\000\001\001\001\000\001\001\004\000\002\t\002\000\001\t";
/*     */   
/*     */   private Reader zzReader;
/*     */   private int zzState;
/*     */   
/*     */   private static int[] zzUnpackAttribute() {
/* 202 */     int[] result = new int[45];
/* 203 */     int offset = 0;
/* 204 */     offset = zzUnpackAttribute("\002\000\001\t\003\001\001\t\003\001\006\t\002\001\001\t\005\000\b\t\001\000\001\001\001\000\001\001\004\000\002\t\002\000\001\t", offset, result);
/* 205 */     return result;
/*     */   }
/*     */   
/*     */   private static int zzUnpackAttribute(String packed, int offset, int[] result) {
/* 209 */     int i = 0;
/* 210 */     int j = offset;
/* 211 */     int l = packed.length();
/* 212 */     label10: while (i < l) {
/* 213 */       int count = packed.charAt(i++);
/* 214 */       int value = packed.charAt(i++); while (true)
/* 215 */       { result[j++] = value; if (--count <= 0)
/*     */           continue label10;  } 
/* 217 */     }  return j;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 227 */   private int zzLexicalState = 0;
/*     */ 
/*     */ 
/*     */   
/* 231 */   private char[] zzBuffer = new char[16384];
/*     */ 
/*     */ 
/*     */   
/*     */   private int zzMarkedPos;
/*     */ 
/*     */ 
/*     */   
/*     */   private int zzCurrentPos;
/*     */ 
/*     */ 
/*     */   
/*     */   private int zzStartRead;
/*     */ 
/*     */ 
/*     */   
/*     */   private int zzEndRead;
/*     */ 
/*     */ 
/*     */   
/*     */   private int yyline;
/*     */ 
/*     */ 
/*     */   
/*     */   private int yychar;
/*     */ 
/*     */   
/*     */   private int yycolumn;
/*     */ 
/*     */   
/*     */   private boolean zzAtBOL = true;
/*     */ 
/*     */   
/*     */   private boolean zzAtEOF;
/*     */ 
/*     */   
/* 267 */   private StringBuffer sb = new StringBuffer();
/*     */   
/*     */   int getPosition() {
/* 270 */     return this.yychar;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Yylex(Reader in) {
/* 282 */     this.zzReader = in;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Yylex(InputStream in) {
/* 292 */     this(new InputStreamReader(in));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static char[] zzUnpackCMap(String packed) {
/* 302 */     char[] map = new char[65536];
/* 303 */     int i = 0;
/* 304 */     int j = 0;
/* 305 */     label10: while (i < 90) {
/* 306 */       int count = packed.charAt(i++);
/* 307 */       char value = packed.charAt(i++); while (true)
/* 308 */       { map[j++] = value; if (--count <= 0)
/*     */           continue label10;  } 
/* 310 */     }  return map;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean zzRefill() throws IOException {
/* 324 */     if (this.zzStartRead > 0) {
/* 325 */       System.arraycopy(this.zzBuffer, this.zzStartRead, this.zzBuffer, 0, this.zzEndRead - this.zzStartRead);
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 330 */       this.zzEndRead -= this.zzStartRead;
/* 331 */       this.zzCurrentPos -= this.zzStartRead;
/* 332 */       this.zzMarkedPos -= this.zzStartRead;
/* 333 */       this.zzStartRead = 0;
/*     */     } 
/*     */ 
/*     */     
/* 337 */     if (this.zzCurrentPos >= this.zzBuffer.length) {
/*     */       
/* 339 */       char[] newBuffer = new char[this.zzCurrentPos * 2];
/* 340 */       System.arraycopy(this.zzBuffer, 0, newBuffer, 0, this.zzBuffer.length);
/* 341 */       this.zzBuffer = newBuffer;
/*     */     } 
/*     */ 
/*     */     
/* 345 */     int numRead = this.zzReader.read(this.zzBuffer, this.zzEndRead, this.zzBuffer.length - this.zzEndRead);
/*     */ 
/*     */     
/* 348 */     if (numRead > 0) {
/* 349 */       this.zzEndRead += numRead;
/* 350 */       return false;
/*     */     } 
/*     */     
/* 353 */     if (numRead == 0) {
/* 354 */       int c = this.zzReader.read();
/* 355 */       if (c == -1) {
/* 356 */         return true;
/*     */       }
/* 358 */       this.zzBuffer[this.zzEndRead++] = (char)c;
/* 359 */       return false;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 364 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void yyclose() throws IOException {
/* 372 */     this.zzAtEOF = true;
/* 373 */     this.zzEndRead = this.zzStartRead;
/*     */     
/* 375 */     if (this.zzReader != null) {
/* 376 */       this.zzReader.close();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void yyreset(Reader reader) {
/* 391 */     this.zzReader = reader;
/* 392 */     this.zzAtBOL = true;
/* 393 */     this.zzAtEOF = false;
/* 394 */     this.zzEndRead = this.zzStartRead = 0;
/* 395 */     this.zzCurrentPos = this.zzMarkedPos = 0;
/* 396 */     this.yyline = this.yychar = this.yycolumn = 0;
/* 397 */     this.zzLexicalState = 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final int yystate() {
/* 405 */     return this.zzLexicalState;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void yybegin(int newState) {
/* 415 */     this.zzLexicalState = newState;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final String yytext() {
/* 423 */     return new String(this.zzBuffer, this.zzStartRead, this.zzMarkedPos - this.zzStartRead);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final char yycharat(int pos) {
/* 439 */     return this.zzBuffer[this.zzStartRead + pos];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final int yylength() {
/* 447 */     return this.zzMarkedPos - this.zzStartRead;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void zzScanError(int errorCode) {
/*     */     String str;
/*     */     try {
/* 468 */       str = ZZ_ERROR_MSG[errorCode];
/*     */     }
/* 470 */     catch (ArrayIndexOutOfBoundsException e) {
/* 471 */       str = ZZ_ERROR_MSG[0];
/*     */     } 
/*     */     
/* 474 */     throw new Error(str);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void yypushback(int number) {
/* 487 */     if (number > yylength()) {
/* 488 */       zzScanError(2);
/*     */     }
/* 490 */     this.zzMarkedPos -= number;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Yytoken yylex() throws IOException, ParseException {
/* 508 */     int zzEndReadL = this.zzEndRead;
/* 509 */     char[] zzBufferL = this.zzBuffer;
/* 510 */     char[] zzCMapL = ZZ_CMAP;
/*     */     
/* 512 */     int[] zzTransL = ZZ_TRANS;
/* 513 */     int[] zzRowMapL = ZZ_ROWMAP;
/* 514 */     int[] zzAttrL = ZZ_ATTRIBUTE; while (true) {
/*     */       int zzInput; Boolean bool; Double double_;
/*     */       Long val;
/* 517 */       int zzMarkedPosL = this.zzMarkedPos;
/*     */       
/* 519 */       this.yychar += zzMarkedPosL - this.zzStartRead;
/*     */       
/* 521 */       int zzAction = -1;
/*     */       
/* 523 */       int zzCurrentPosL = this.zzCurrentPos = this.zzStartRead = zzMarkedPosL;
/*     */       
/* 525 */       this.zzState = ZZ_LEXSTATE[this.zzLexicalState];
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       while (true) {
/* 531 */         if (zzCurrentPosL < zzEndReadL)
/* 532 */         { zzInput = zzBufferL[zzCurrentPosL++]; }
/* 533 */         else { if (this.zzAtEOF) {
/* 534 */             int i = -1;
/*     */             
/*     */             break;
/*     */           } 
/*     */           
/* 539 */           this.zzCurrentPos = zzCurrentPosL;
/* 540 */           this.zzMarkedPos = zzMarkedPosL;
/* 541 */           boolean eof = zzRefill();
/*     */           
/* 543 */           zzCurrentPosL = this.zzCurrentPos;
/* 544 */           zzMarkedPosL = this.zzMarkedPos;
/* 545 */           zzBufferL = this.zzBuffer;
/* 546 */           zzEndReadL = this.zzEndRead;
/* 547 */           if (eof) {
/* 548 */             int i = -1;
/*     */             
/*     */             break;
/*     */           } 
/* 552 */           zzInput = zzBufferL[zzCurrentPosL++]; }
/*     */ 
/*     */         
/* 555 */         int zzNext = zzTransL[zzRowMapL[this.zzState] + zzCMapL[zzInput]];
/* 556 */         if (zzNext == -1)
/* 557 */           break;  this.zzState = zzNext;
/*     */         
/* 559 */         int zzAttributes = zzAttrL[this.zzState];
/* 560 */         if ((zzAttributes & 0x1) == 1) {
/* 561 */           zzAction = this.zzState;
/* 562 */           zzMarkedPosL = zzCurrentPosL;
/* 563 */           if ((zzAttributes & 0x8) == 8) {
/*     */             break;
/*     */           }
/*     */         } 
/*     */       } 
/*     */ 
/*     */       
/* 570 */       this.zzMarkedPos = zzMarkedPosL;
/*     */       
/* 572 */       switch ((zzAction < 0) ? zzAction : ZZ_ACTION[zzAction]) {
/*     */         case 11:
/* 574 */           this.sb.append(yytext()); continue;
/*     */         case 25:
/*     */           continue;
/*     */         case 4:
/* 578 */           this.sb.delete(0, this.sb.length()); yybegin(2); continue;
/*     */         case 26:
/*     */           continue;
/*     */         case 16:
/* 582 */           this.sb.append('\b'); continue;
/*     */         case 27:
/*     */           continue;
/*     */         case 6:
/* 586 */           return new Yytoken(2, null);
/*     */         case 28:
/*     */           continue;
/*     */         case 23:
/* 590 */           bool = Boolean.valueOf(yytext()); return new Yytoken(0, bool);
/*     */         case 29:
/*     */           continue;
/*     */         case 22:
/* 594 */           return new Yytoken(0, null);
/*     */         case 30:
/*     */           continue;
/*     */         case 13:
/* 598 */           yybegin(0); return new Yytoken(0, this.sb.toString());
/*     */         case 31:
/*     */           continue;
/*     */         case 12:
/* 602 */           this.sb.append('\\'); continue;
/*     */         case 32:
/*     */           continue;
/*     */         case 21:
/* 606 */           double_ = Double.valueOf(yytext()); return new Yytoken(0, double_);
/*     */         case 33:
/*     */           continue;
/*     */         case 1:
/* 610 */           throw new ParseException(this.yychar, 0, new Character(yycharat(0)));
/*     */         case 34:
/*     */           continue;
/*     */         case 8:
/* 614 */           return new Yytoken(4, null);
/*     */         case 35:
/*     */           continue;
/*     */         case 19:
/* 618 */           this.sb.append('\r'); continue;
/*     */         case 36:
/*     */           continue;
/*     */         case 15:
/* 622 */           this.sb.append('/'); continue;
/*     */         case 37:
/*     */           continue;
/*     */         case 10:
/* 626 */           return new Yytoken(6, null);
/*     */         case 38:
/*     */           continue;
/*     */         case 14:
/* 630 */           this.sb.append('"'); continue;
/*     */         case 39:
/*     */           continue;
/*     */         case 5:
/* 634 */           return new Yytoken(1, null);
/*     */         case 40:
/*     */           continue;
/*     */         case 17:
/* 638 */           this.sb.append('\f'); continue;
/*     */         case 41:
/*     */           continue;
/*     */         case 24:
/*     */           try {
/* 643 */             int ch = Integer.parseInt(yytext().substring(2), 16);
/* 644 */             this.sb.append((char)ch);
/*     */           }
/* 646 */           catch (Exception e) {
/* 647 */             throw new ParseException(this.yychar, 2, e);
/*     */           }  continue;
/*     */         case 42:
/*     */           continue;
/*     */         case 20:
/* 652 */           this.sb.append('\t'); continue;
/*     */         case 43:
/*     */           continue;
/*     */         case 7:
/* 656 */           return new Yytoken(3, null);
/*     */         case 44:
/*     */           continue;
/*     */         case 2:
/* 660 */           val = Long.valueOf(yytext()); return new Yytoken(0, val);
/*     */         case 45:
/*     */           continue;
/*     */         case 18:
/* 664 */           this.sb.append('\n'); continue;
/*     */         case 46:
/*     */           continue;
/*     */         case 9:
/* 668 */           return new Yytoken(5, null);
/*     */         
/*     */         case 47:
/*     */         case 3:
/*     */         case 48:
/*     */           continue;
/*     */       } 
/*     */       
/* 676 */       if (zzInput == -1 && this.zzStartRead == this.zzCurrentPos) {
/* 677 */         this.zzAtEOF = true;
/* 678 */         return null;
/*     */       } 
/*     */       
/* 681 */       zzScanError(1);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\org\json\simple\parser\Yylex.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */