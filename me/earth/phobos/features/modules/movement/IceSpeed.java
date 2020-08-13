/*    */ package me.earth.phobos.features.modules.movement;
/*    */ 
/*    */ import me.earth.phobos.features.modules.Module;
/*    */ import me.earth.phobos.features.setting.Setting;
/*    */ import net.minecraft.init.Blocks;
/*    */ 
/*    */ public class IceSpeed
/*    */   extends Module {
/*  9 */   private Setting<Float> speed = register(new Setting("Speed", Float.valueOf(0.4F), Float.valueOf(0.2F), Float.valueOf(1.5F)));
/*    */   
/* 11 */   private static IceSpeed INSTANCE = new IceSpeed();
/*    */   
/*    */   public IceSpeed() {
/* 14 */     super("IceSpeed", "Speeds you up on ice.", Module.Category.MOVEMENT, false, false, false);
/* 15 */     INSTANCE = this;
/*    */   }
/*    */   
/*    */   public static IceSpeed getINSTANCE() {
/* 19 */     if (INSTANCE == null) {
/* 20 */       INSTANCE = new IceSpeed();
/*    */     }
/* 22 */     return INSTANCE;
/*    */   }
/*    */ 
/*    */   
/*    */   public void onUpdate() {
/* 27 */     Blocks.field_150432_aD.field_149765_K = ((Float)this.speed.getValue()).floatValue();
/* 28 */     Blocks.field_150403_cj.field_149765_K = ((Float)this.speed.getValue()).floatValue();
/* 29 */     Blocks.field_185778_de.field_149765_K = ((Float)this.speed.getValue()).floatValue();
/*    */   }
/*    */ 
/*    */   
/*    */   public void onDisable() {
/* 34 */     Blocks.field_150432_aD.field_149765_K = 0.98F;
/* 35 */     Blocks.field_150403_cj.field_149765_K = 0.98F;
/* 36 */     Blocks.field_185778_de.field_149765_K = 0.98F;
/*    */   }
/*    */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\me\earth\phobos\features\modules\movement\IceSpeed.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */