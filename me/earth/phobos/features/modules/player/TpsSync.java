/*    */ package me.earth.phobos.features.modules.player;
/*    */ 
/*    */ import me.earth.phobos.features.modules.Module;
/*    */ import me.earth.phobos.features.setting.Setting;
/*    */ 
/*    */ public class TpsSync
/*    */   extends Module {
/*  8 */   public Setting<Boolean> mining = register(new Setting("Mining", Boolean.valueOf(true)));
/*  9 */   public Setting<Boolean> attack = register(new Setting("Attack", Boolean.valueOf(false)));
/*    */ 
/*    */   
/* 12 */   private static TpsSync INSTANCE = new TpsSync();
/*    */ 
/*    */   
/*    */   public TpsSync() {
/* 16 */     super("TpsSync", "Syncs your client with the TPS.", Module.Category.PLAYER, true, false, false);
/* 17 */     setInstance();
/*    */   }
/*    */   
/*    */   private void setInstance() {
/* 21 */     INSTANCE = this;
/*    */   }
/*    */   
/*    */   public static TpsSync getInstance() {
/* 25 */     if (INSTANCE == null) {
/* 26 */       INSTANCE = new TpsSync();
/*    */     }
/* 28 */     return INSTANCE;
/*    */   }
/*    */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\me\earth\phobos\features\modules\player\TpsSync.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */