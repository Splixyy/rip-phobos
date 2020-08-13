/*    */ package me.earth.phobos.features.modules.player;
/*    */ 
/*    */ import me.earth.phobos.features.modules.Module;
/*    */ 
/*    */ public class LiquidInteract
/*    */   extends Module {
/*  7 */   private static LiquidInteract INSTANCE = new LiquidInteract();
/*    */   
/*    */   public LiquidInteract() {
/* 10 */     super("LiquidInteract", "Interact with liquids", Module.Category.PLAYER, false, false, false);
/* 11 */     setInstance();
/*    */   }
/*    */   
/*    */   private void setInstance() {
/* 15 */     INSTANCE = this;
/*    */   }
/*    */   
/*    */   public static LiquidInteract getInstance() {
/* 19 */     if (INSTANCE == null) {
/* 20 */       INSTANCE = new LiquidInteract();
/*    */     }
/* 22 */     return INSTANCE;
/*    */   }
/*    */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\me\earth\phobos\features\modules\player\LiquidInteract.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */