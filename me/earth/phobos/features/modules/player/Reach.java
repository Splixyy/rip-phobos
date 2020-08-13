/*    */ package me.earth.phobos.features.modules.player;
/*    */ 
/*    */ import me.earth.phobos.features.modules.Module;
/*    */ import me.earth.phobos.features.setting.Setting;
/*    */ 
/*    */ public class Reach
/*    */   extends Module {
/*  8 */   public Setting<Boolean> override = register(new Setting("Override", Boolean.valueOf(false)));
/*  9 */   public Setting<Float> add = register(new Setting("Add", Float.valueOf(3.0F), v -> !((Boolean)this.override.getValue()).booleanValue()));
/* 10 */   public Setting<Float> reach = register(new Setting("Reach", Float.valueOf(6.0F), v -> ((Boolean)this.override.getValue()).booleanValue()));
/*    */   
/* 12 */   private static Reach INSTANCE = new Reach();
/*    */   
/*    */   public Reach() {
/* 15 */     super("Reach", "Extends your block reach", Module.Category.PLAYER, true, false, false);
/* 16 */     setInstance();
/*    */   }
/*    */   
/*    */   private void setInstance() {
/* 20 */     INSTANCE = this;
/*    */   }
/*    */   
/*    */   public static Reach getInstance() {
/* 24 */     if (INSTANCE == null) {
/* 25 */       INSTANCE = new Reach();
/*    */     }
/* 27 */     return INSTANCE;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getDisplayInfo() {
/* 32 */     return ((Boolean)this.override.getValue()).booleanValue() ? ((Float)this.reach.getValue()).toString() : ((Float)this.add.getValue()).toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\me\earth\phobos\features\modules\player\Reach.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */