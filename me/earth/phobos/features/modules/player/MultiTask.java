/*    */ package me.earth.phobos.features.modules.player;
/*    */ 
/*    */ import me.earth.phobos.features.modules.Module;
/*    */ 
/*    */ public class MultiTask
/*    */   extends Module {
/*  7 */   private static MultiTask INSTANCE = new MultiTask();
/*    */   
/*    */   public MultiTask() {
/* 10 */     super("MultiTask", "Allows you to eat while mining.", Module.Category.PLAYER, false, false, false);
/* 11 */     setInstance();
/*    */   }
/*    */   
/*    */   private void setInstance() {
/* 15 */     INSTANCE = this;
/*    */   }
/*    */   
/*    */   public static MultiTask getInstance() {
/* 19 */     if (INSTANCE == null) {
/* 20 */       INSTANCE = new MultiTask();
/*    */     }
/* 22 */     return INSTANCE;
/*    */   }
/*    */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\me\earth\phobos\features\modules\player\MultiTask.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */