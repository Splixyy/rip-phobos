/*    */ package me.earth.phobos.features.modules.render;
/*    */ 
/*    */ import me.earth.phobos.features.modules.Module;
/*    */ import me.earth.phobos.features.setting.Setting;
/*    */ 
/*    */ public class SmallShield
/*    */   extends Module {
/*  8 */   public Setting<Boolean> normalOffset = register(new Setting("OffNormal", Boolean.valueOf(false)));
/*  9 */   public Setting<Float> offset = register(new Setting("Offset", Float.valueOf(0.7F), Float.valueOf(0.0F), Float.valueOf(1.0F), v -> ((Boolean)this.normalOffset.getValue()).booleanValue()));
/* 10 */   public Setting<Float> offX = register(new Setting("OffX", Float.valueOf(0.0F), Float.valueOf(-1.0F), Float.valueOf(1.0F), v -> !((Boolean)this.normalOffset.getValue()).booleanValue()));
/* 11 */   public Setting<Float> offY = register(new Setting("OffY", Float.valueOf(0.0F), Float.valueOf(-1.0F), Float.valueOf(1.0F), v -> !((Boolean)this.normalOffset.getValue()).booleanValue()));
/* 12 */   public Setting<Float> mainX = register(new Setting("MainX", Float.valueOf(0.0F), Float.valueOf(-1.0F), Float.valueOf(1.0F)));
/* 13 */   public Setting<Float> mainY = register(new Setting("MainY", Float.valueOf(0.0F), Float.valueOf(-1.0F), Float.valueOf(1.0F)));
/*    */   
/* 15 */   private static SmallShield INSTANCE = new SmallShield();
/*    */   
/*    */   public SmallShield() {
/* 18 */     super("SmallShield", "Makes you offhand lower.", Module.Category.RENDER, false, false, false);
/* 19 */     setInstance();
/*    */   }
/*    */   
/*    */   private void setInstance() {
/* 23 */     INSTANCE = this;
/*    */   }
/*    */ 
/*    */   
/*    */   public void onUpdate() {
/* 28 */     if (((Boolean)this.normalOffset.getValue()).booleanValue()) {
/* 29 */       mc.field_71460_t.field_78516_c.field_187471_h = ((Float)this.offset.getValue()).floatValue();
/*    */     }
/*    */   }
/*    */   
/*    */   public static SmallShield getINSTANCE() {
/* 34 */     if (INSTANCE == null) {
/* 35 */       INSTANCE = new SmallShield();
/*    */     }
/* 37 */     return INSTANCE;
/*    */   }
/*    */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\me\earth\phobos\features\modules\render\SmallShield.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */