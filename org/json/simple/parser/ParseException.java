/*    */ package org.json.simple.parser;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ParseException
/*    */   extends Exception
/*    */ {
/*    */   private static final long serialVersionUID = -7880698968187728548L;
/*    */   public static final int ERROR_UNEXPECTED_CHAR = 0;
/*    */   public static final int ERROR_UNEXPECTED_TOKEN = 1;
/*    */   public static final int ERROR_UNEXPECTED_EXCEPTION = 2;
/*    */   private int errorType;
/*    */   private Object unexpectedObject;
/*    */   private int position;
/*    */   
/*    */   public ParseException(int errorType) {
/* 21 */     this(-1, errorType, null);
/*    */   }
/*    */   
/*    */   public ParseException(int errorType, Object unexpectedObject) {
/* 25 */     this(-1, errorType, unexpectedObject);
/*    */   }
/*    */   
/*    */   public ParseException(int position, int errorType, Object unexpectedObject) {
/* 29 */     this.position = position;
/* 30 */     this.errorType = errorType;
/* 31 */     this.unexpectedObject = unexpectedObject;
/*    */   }
/*    */   
/*    */   public int getErrorType() {
/* 35 */     return this.errorType;
/*    */   }
/*    */   
/*    */   public void setErrorType(int errorType) {
/* 39 */     this.errorType = errorType;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int getPosition() {
/* 48 */     return this.position;
/*    */   }
/*    */   
/*    */   public void setPosition(int position) {
/* 52 */     this.position = position;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Object getUnexpectedObject() {
/* 64 */     return this.unexpectedObject;
/*    */   }
/*    */   
/*    */   public void setUnexpectedObject(Object unexpectedObject) {
/* 68 */     this.unexpectedObject = unexpectedObject;
/*    */   }
/*    */   
/*    */   public String toString() {
/* 72 */     StringBuffer sb = new StringBuffer();
/*    */     
/* 74 */     switch (this.errorType)
/*    */     { case 0:
/* 76 */         sb.append("Unexpected character (").append(this.unexpectedObject).append(") at position ").append(this.position).append(".");
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */         
/* 88 */         return sb.toString();case 1: sb.append("Unexpected token ").append(this.unexpectedObject).append(" at position ").append(this.position).append("."); return sb.toString();case 2: sb.append("Unexpected exception at position ").append(this.position).append(": ").append(this.unexpectedObject); return sb.toString(); }  sb.append("Unkown error at position ").append(this.position).append("."); return sb.toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\org\json\simple\parser\ParseException.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */