/*    */ package org.json.simple.parser;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Yytoken
/*    */ {
/*    */   public static final int TYPE_VALUE = 0;
/*    */   public static final int TYPE_LEFT_BRACE = 1;
/*    */   public static final int TYPE_RIGHT_BRACE = 2;
/*    */   public static final int TYPE_LEFT_SQUARE = 3;
/*    */   public static final int TYPE_RIGHT_SQUARE = 4;
/*    */   public static final int TYPE_COMMA = 5;
/*    */   public static final int TYPE_COLON = 6;
/*    */   public static final int TYPE_EOF = -1;
/* 20 */   public int type = 0;
/* 21 */   public Object value = null;
/*    */   
/*    */   public Yytoken(int type, Object value) {
/* 24 */     this.type = type;
/* 25 */     this.value = value;
/*    */   }
/*    */   
/*    */   public String toString() {
/* 29 */     StringBuffer sb = new StringBuffer();
/* 30 */     switch (this.type) {
/*    */       case 0:
/* 32 */         sb.append("VALUE(").append(this.value).append(")");
/*    */         break;
/*    */       case 1:
/* 35 */         sb.append("LEFT BRACE({)");
/*    */         break;
/*    */       case 2:
/* 38 */         sb.append("RIGHT BRACE(})");
/*    */         break;
/*    */       case 3:
/* 41 */         sb.append("LEFT SQUARE([)");
/*    */         break;
/*    */       case 4:
/* 44 */         sb.append("RIGHT SQUARE(])");
/*    */         break;
/*    */       case 5:
/* 47 */         sb.append("COMMA(,)");
/*    */         break;
/*    */       case 6:
/* 50 */         sb.append("COLON(:)");
/*    */         break;
/*    */       case -1:
/* 53 */         sb.append("END OF FILE");
/*    */         break;
/*    */     } 
/* 56 */     return sb.toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\org\json\simple\parser\Yytoken.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */