/*    */ package org.spongepowered.asm.mixin.injection.invoke.arg;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ArgumentCountException
/*    */   extends IllegalArgumentException
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   
/*    */   public ArgumentCountException(int received, int expected, String desc) {
/* 36 */     super("Invalid number of arguments for setAll, received " + received + " but expected " + expected + ": " + desc);
/*    */   }
/*    */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\org\spongepowered\asm\mixin\injection\invoke\arg\ArgumentCountException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */