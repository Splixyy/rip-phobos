/*    */ package me.earth.phobos.features.modules.render;
/*    */ 
/*    */ import me.earth.phobos.features.modules.Module;
/*    */ 
/*    */ public class Chams
/*    */   extends Module {
/*  7 */   private static Chams INSTANCE = new Chams();
/*    */   
/*    */   public Chams() {
/* 10 */     super("Chams", "Renders players through walls.", Module.Category.RENDER, false, false, false);
/* 11 */     setInstance();
/*    */   }
/*    */   
/*    */   private void setInstance() {
/* 15 */     INSTANCE = this;
/*    */   }
/*    */   
/*    */   public static Chams getInstance() {
/* 19 */     if (INSTANCE == null) {
/* 20 */       INSTANCE = new Chams();
/*    */     }
/* 22 */     return INSTANCE;
/*    */   }
/*    */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\me\earth\phobos\features\modules\render\Chams.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */