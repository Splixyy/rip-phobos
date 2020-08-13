/*     */ package org.json.simple.parser;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.Reader;
/*     */ import java.io.StringReader;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.json.simple.JSONArray;
/*     */ import org.json.simple.JSONObject;
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
/*     */ public class JSONParser
/*     */ {
/*     */   public static final int S_INIT = 0;
/*     */   public static final int S_IN_FINISHED_VALUE = 1;
/*     */   public static final int S_IN_OBJECT = 2;
/*     */   public static final int S_IN_ARRAY = 3;
/*     */   public static final int S_PASSED_PAIR_KEY = 4;
/*     */   public static final int S_IN_PAIR_VALUE = 5;
/*     */   public static final int S_END = 6;
/*     */   public static final int S_IN_ERROR = -1;
/*     */   private LinkedList handlerStatusStack;
/*  34 */   private Yylex lexer = new Yylex((Reader)null);
/*  35 */   private Yytoken token = null;
/*  36 */   private int status = 0;
/*     */   
/*     */   private int peekStatus(LinkedList statusStack) {
/*  39 */     if (statusStack.size() == 0)
/*  40 */       return -1; 
/*  41 */     Integer status = statusStack.getFirst();
/*  42 */     return status.intValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void reset() {
/*  50 */     this.token = null;
/*  51 */     this.status = 0;
/*  52 */     this.handlerStatusStack = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void reset(Reader in) {
/*  63 */     this.lexer.yyreset(in);
/*  64 */     reset();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getPosition() {
/*  71 */     return this.lexer.getPosition();
/*     */   }
/*     */   
/*     */   public Object parse(String s) throws ParseException {
/*  75 */     return parse(s, (ContainerFactory)null);
/*     */   }
/*     */   
/*     */   public Object parse(String s, ContainerFactory containerFactory) throws ParseException {
/*  79 */     StringReader in = new StringReader(s);
/*     */     try {
/*  81 */       return parse(in, containerFactory);
/*     */     }
/*  83 */     catch (IOException ie) {
/*     */ 
/*     */ 
/*     */       
/*  87 */       throw new ParseException(-1, 2, ie);
/*     */     } 
/*     */   }
/*     */   
/*     */   public Object parse(Reader in) throws IOException, ParseException {
/*  92 */     return parse(in, (ContainerFactory)null);
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
/*     */   
/*     */   public Object parse(Reader in, ContainerFactory containerFactory) throws IOException, ParseException {
/* 112 */     reset(in);
/* 113 */     LinkedList statusStack = new LinkedList();
/* 114 */     LinkedList valueStack = new LinkedList(); try {
/*     */       do {
/*     */         String key; List val; Map parent; Map newObject; List newArray;
/*     */         Map map1;
/* 118 */         nextToken();
/* 119 */         switch (this.status) {
/*     */           case 0:
/* 121 */             switch (this.token.type) {
/*     */               case 0:
/* 123 */                 this.status = 1;
/* 124 */                 statusStack.addFirst(new Integer(this.status));
/* 125 */                 valueStack.addFirst(this.token.value);
/*     */                 break;
/*     */               case 1:
/* 128 */                 this.status = 2;
/* 129 */                 statusStack.addFirst(new Integer(this.status));
/* 130 */                 valueStack.addFirst(createObjectContainer(containerFactory));
/*     */                 break;
/*     */               case 3:
/* 133 */                 this.status = 3;
/* 134 */                 statusStack.addFirst(new Integer(this.status));
/* 135 */                 valueStack.addFirst(createArrayContainer(containerFactory));
/*     */                 break;
/*     */             } 
/* 138 */             this.status = -1;
/*     */             break;
/*     */ 
/*     */           
/*     */           case 1:
/* 143 */             if (this.token.type == -1) {
/* 144 */               return valueStack.removeFirst();
/*     */             }
/* 146 */             throw new ParseException(getPosition(), 1, this.token);
/*     */           
/*     */           case 2:
/* 149 */             switch (this.token.type) {
/*     */               case 5:
/*     */                 break;
/*     */               case 0:
/* 153 */                 if (this.token.value instanceof String) {
/* 154 */                   String str = (String)this.token.value;
/* 155 */                   valueStack.addFirst(str);
/* 156 */                   this.status = 4;
/* 157 */                   statusStack.addFirst(new Integer(this.status));
/*     */                   break;
/*     */                 } 
/* 160 */                 this.status = -1;
/*     */                 break;
/*     */               
/*     */               case 2:
/* 164 */                 if (valueStack.size() > 1) {
/* 165 */                   statusStack.removeFirst();
/* 166 */                   valueStack.removeFirst();
/* 167 */                   this.status = peekStatus(statusStack);
/*     */                   break;
/*     */                 } 
/* 170 */                 this.status = 1;
/*     */                 break;
/*     */             } 
/*     */             
/* 174 */             this.status = -1;
/*     */             break;
/*     */ 
/*     */ 
/*     */           
/*     */           case 4:
/* 180 */             switch (this.token.type) {
/*     */               case 6:
/*     */                 break;
/*     */               case 0:
/* 184 */                 statusStack.removeFirst();
/* 185 */                 key = (String)valueStack.removeFirst();
/* 186 */                 parent = (Map)valueStack.getFirst();
/* 187 */                 parent.put(key, this.token.value);
/* 188 */                 this.status = peekStatus(statusStack);
/*     */                 break;
/*     */               case 3:
/* 191 */                 statusStack.removeFirst();
/* 192 */                 key = (String)valueStack.removeFirst();
/* 193 */                 parent = (Map)valueStack.getFirst();
/* 194 */                 newArray = createArrayContainer(containerFactory);
/* 195 */                 parent.put(key, newArray);
/* 196 */                 this.status = 3;
/* 197 */                 statusStack.addFirst(new Integer(this.status));
/* 198 */                 valueStack.addFirst(newArray);
/*     */                 break;
/*     */               case 1:
/* 201 */                 statusStack.removeFirst();
/* 202 */                 key = (String)valueStack.removeFirst();
/* 203 */                 parent = (Map)valueStack.getFirst();
/* 204 */                 map1 = createObjectContainer(containerFactory);
/* 205 */                 parent.put(key, map1);
/* 206 */                 this.status = 2;
/* 207 */                 statusStack.addFirst(new Integer(this.status));
/* 208 */                 valueStack.addFirst(map1);
/*     */                 break;
/*     */             } 
/* 211 */             this.status = -1;
/*     */             break;
/*     */ 
/*     */           
/*     */           case 3:
/* 216 */             switch (this.token.type) {
/*     */               case 5:
/*     */                 break;
/*     */               case 0:
/* 220 */                 val = (List)valueStack.getFirst();
/* 221 */                 val.add(this.token.value);
/*     */                 break;
/*     */               case 4:
/* 224 */                 if (valueStack.size() > 1) {
/* 225 */                   statusStack.removeFirst();
/* 226 */                   valueStack.removeFirst();
/* 227 */                   this.status = peekStatus(statusStack);
/*     */                   break;
/*     */                 } 
/* 230 */                 this.status = 1;
/*     */                 break;
/*     */               
/*     */               case 1:
/* 234 */                 val = (List)valueStack.getFirst();
/* 235 */                 newObject = createObjectContainer(containerFactory);
/* 236 */                 val.add(newObject);
/* 237 */                 this.status = 2;
/* 238 */                 statusStack.addFirst(new Integer(this.status));
/* 239 */                 valueStack.addFirst(newObject);
/*     */                 break;
/*     */               case 3:
/* 242 */                 val = (List)valueStack.getFirst();
/* 243 */                 newArray = createArrayContainer(containerFactory);
/* 244 */                 val.add(newArray);
/* 245 */                 this.status = 3;
/* 246 */                 statusStack.addFirst(new Integer(this.status));
/* 247 */                 valueStack.addFirst(newArray);
/*     */                 break;
/*     */             } 
/* 250 */             this.status = -1;
/*     */             break;
/*     */           
/*     */           case -1:
/* 254 */             throw new ParseException(getPosition(), 1, this.token);
/*     */         } 
/* 256 */         if (this.status == -1) {
/* 257 */           throw new ParseException(getPosition(), 1, this.token);
/*     */         }
/* 259 */       } while (this.token.type != -1);
/*     */     }
/* 261 */     catch (IOException ie) {
/* 262 */       throw ie;
/*     */     } 
/*     */     
/* 265 */     throw new ParseException(getPosition(), 1, this.token);
/*     */   }
/*     */   
/*     */   private void nextToken() throws ParseException, IOException {
/* 269 */     this.token = this.lexer.yylex();
/* 270 */     if (this.token == null)
/* 271 */       this.token = new Yytoken(-1, null); 
/*     */   }
/*     */   
/*     */   private Map createObjectContainer(ContainerFactory containerFactory) {
/* 275 */     if (containerFactory == null)
/* 276 */       return (Map)new JSONObject(); 
/* 277 */     Map m = containerFactory.createObjectContainer();
/*     */     
/* 279 */     if (m == null)
/* 280 */       return (Map)new JSONObject(); 
/* 281 */     return m;
/*     */   }
/*     */   
/*     */   private List createArrayContainer(ContainerFactory containerFactory) {
/* 285 */     if (containerFactory == null)
/* 286 */       return (List)new JSONArray(); 
/* 287 */     List l = containerFactory.creatArrayContainer();
/*     */     
/* 289 */     if (l == null)
/* 290 */       return (List)new JSONArray(); 
/* 291 */     return l;
/*     */   }
/*     */   
/*     */   public void parse(String s, ContentHandler contentHandler) throws ParseException {
/* 295 */     parse(s, contentHandler, false);
/*     */   }
/*     */   
/*     */   public void parse(String s, ContentHandler contentHandler, boolean isResume) throws ParseException {
/* 299 */     StringReader in = new StringReader(s);
/*     */     try {
/* 301 */       parse(in, contentHandler, isResume);
/*     */     }
/* 303 */     catch (IOException ie) {
/*     */ 
/*     */ 
/*     */       
/* 307 */       throw new ParseException(-1, 2, ie);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void parse(Reader in, ContentHandler contentHandler) throws IOException, ParseException {
/* 312 */     parse(in, contentHandler, false);
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
/*     */   public void parse(Reader in, ContentHandler contentHandler, boolean isResume) throws IOException, ParseException {
/* 330 */     if (!isResume) {
/* 331 */       reset(in);
/* 332 */       this.handlerStatusStack = new LinkedList();
/*     */     
/*     */     }
/* 335 */     else if (this.handlerStatusStack == null) {
/* 336 */       isResume = false;
/* 337 */       reset(in);
/* 338 */       this.handlerStatusStack = new LinkedList();
/*     */     } 
/*     */ 
/*     */     
/* 342 */     LinkedList statusStack = this.handlerStatusStack;
/*     */     
/*     */     try {
/*     */       do {
/* 346 */         switch (this.status) {
/*     */           case 0:
/* 348 */             contentHandler.startJSON();
/* 349 */             nextToken();
/* 350 */             switch (this.token.type) {
/*     */               case 0:
/* 352 */                 this.status = 1;
/* 353 */                 statusStack.addFirst(new Integer(this.status));
/* 354 */                 if (!contentHandler.primitive(this.token.value))
/*     */                   return; 
/*     */                 break;
/*     */               case 1:
/* 358 */                 this.status = 2;
/* 359 */                 statusStack.addFirst(new Integer(this.status));
/* 360 */                 if (!contentHandler.startObject())
/*     */                   return; 
/*     */                 break;
/*     */               case 3:
/* 364 */                 this.status = 3;
/* 365 */                 statusStack.addFirst(new Integer(this.status));
/* 366 */                 if (!contentHandler.startArray())
/*     */                   return; 
/*     */                 break;
/*     */             } 
/* 370 */             this.status = -1;
/*     */             break;
/*     */ 
/*     */           
/*     */           case 1:
/* 375 */             nextToken();
/* 376 */             if (this.token.type == -1) {
/* 377 */               contentHandler.endJSON();
/* 378 */               this.status = 6;
/*     */               
/*     */               return;
/*     */             } 
/* 382 */             this.status = -1;
/* 383 */             throw new ParseException(getPosition(), 1, this.token);
/*     */ 
/*     */           
/*     */           case 2:
/* 387 */             nextToken();
/* 388 */             switch (this.token.type) {
/*     */               case 5:
/*     */                 break;
/*     */               case 0:
/* 392 */                 if (this.token.value instanceof String) {
/* 393 */                   String key = (String)this.token.value;
/* 394 */                   this.status = 4;
/* 395 */                   statusStack.addFirst(new Integer(this.status));
/* 396 */                   if (!contentHandler.startObjectEntry(key))
/*     */                     return; 
/*     */                   break;
/*     */                 } 
/* 400 */                 this.status = -1;
/*     */                 break;
/*     */               
/*     */               case 2:
/* 404 */                 if (statusStack.size() > 1) {
/* 405 */                   statusStack.removeFirst();
/* 406 */                   this.status = peekStatus(statusStack);
/*     */                 } else {
/*     */                   
/* 409 */                   this.status = 1;
/*     */                 } 
/* 411 */                 if (!contentHandler.endObject())
/*     */                   return; 
/*     */                 break;
/*     */             } 
/* 415 */             this.status = -1;
/*     */             break;
/*     */ 
/*     */ 
/*     */           
/*     */           case 4:
/* 421 */             nextToken();
/* 422 */             switch (this.token.type) {
/*     */               case 6:
/*     */                 break;
/*     */               case 0:
/* 426 */                 statusStack.removeFirst();
/* 427 */                 this.status = peekStatus(statusStack);
/* 428 */                 if (!contentHandler.primitive(this.token.value))
/*     */                   return; 
/* 430 */                 if (!contentHandler.endObjectEntry())
/*     */                   return; 
/*     */                 break;
/*     */               case 3:
/* 434 */                 statusStack.removeFirst();
/* 435 */                 statusStack.addFirst(new Integer(5));
/* 436 */                 this.status = 3;
/* 437 */                 statusStack.addFirst(new Integer(this.status));
/* 438 */                 if (!contentHandler.startArray())
/*     */                   return; 
/*     */                 break;
/*     */               case 1:
/* 442 */                 statusStack.removeFirst();
/* 443 */                 statusStack.addFirst(new Integer(5));
/* 444 */                 this.status = 2;
/* 445 */                 statusStack.addFirst(new Integer(this.status));
/* 446 */                 if (!contentHandler.startObject())
/*     */                   return; 
/*     */                 break;
/*     */             } 
/* 450 */             this.status = -1;
/*     */             break;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*     */           case 5:
/* 459 */             statusStack.removeFirst();
/* 460 */             this.status = peekStatus(statusStack);
/* 461 */             if (!contentHandler.endObjectEntry()) {
/*     */               return;
/*     */             }
/*     */             break;
/*     */           case 3:
/* 466 */             nextToken();
/* 467 */             switch (this.token.type) {
/*     */               case 5:
/*     */                 break;
/*     */               case 0:
/* 471 */                 if (!contentHandler.primitive(this.token.value))
/*     */                   return; 
/*     */                 break;
/*     */               case 4:
/* 475 */                 if (statusStack.size() > 1) {
/* 476 */                   statusStack.removeFirst();
/* 477 */                   this.status = peekStatus(statusStack);
/*     */                 } else {
/*     */                   
/* 480 */                   this.status = 1;
/*     */                 } 
/* 482 */                 if (!contentHandler.endArray())
/*     */                   return; 
/*     */                 break;
/*     */               case 1:
/* 486 */                 this.status = 2;
/* 487 */                 statusStack.addFirst(new Integer(this.status));
/* 488 */                 if (!contentHandler.startObject())
/*     */                   return; 
/*     */                 break;
/*     */               case 3:
/* 492 */                 this.status = 3;
/* 493 */                 statusStack.addFirst(new Integer(this.status));
/* 494 */                 if (!contentHandler.startArray())
/*     */                   return; 
/*     */                 break;
/*     */             } 
/* 498 */             this.status = -1;
/*     */             break;
/*     */ 
/*     */           
/*     */           case 6:
/*     */             return;
/*     */           
/*     */           case -1:
/* 506 */             throw new ParseException(getPosition(), 1, this.token);
/*     */         } 
/* 508 */         if (this.status == -1) {
/* 509 */           throw new ParseException(getPosition(), 1, this.token);
/*     */         }
/* 511 */       } while (this.token.type != -1);
/*     */     }
/* 513 */     catch (IOException ie) {
/* 514 */       this.status = -1;
/* 515 */       throw ie;
/*     */     }
/* 517 */     catch (ParseException pe) {
/* 518 */       this.status = -1;
/* 519 */       throw pe;
/*     */     }
/* 521 */     catch (RuntimeException re) {
/* 522 */       this.status = -1;
/* 523 */       throw re;
/*     */     }
/* 525 */     catch (Error e) {
/* 526 */       this.status = -1;
/* 527 */       throw e;
/*     */     } 
/*     */     
/* 530 */     this.status = -1;
/* 531 */     throw new ParseException(getPosition(), 1, this.token);
/*     */   }
/*     */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\org\json\simple\parser\JSONParser.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */