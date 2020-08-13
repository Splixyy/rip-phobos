/*    */ package me.earth.phobos.features.modules.render;
/*    */ 
/*    */ import me.earth.phobos.features.modules.Module;
/*    */ import me.earth.phobos.features.setting.Setting;
/*    */ 
/*    */ public class CameraClip
/*    */   extends Module {
/*  8 */   public Setting<Boolean> extend = register(new Setting("Extend", Boolean.valueOf(false)));
/*  9 */   public Setting<Double> distance = register(new Setting("Distance", Double.valueOf(10.0D), Double.valueOf(0.0D), Double.valueOf(50.0D), v -> ((Boolean)this.extend.getValue()).booleanValue(), "By how much you want to extend the distance."));
/* 10 */   private static CameraClip INSTANCE = new CameraClip();
/*    */   
/*    */   public CameraClip() {
/* 13 */     super("CameraClip", "Makes your Camera clip.", Module.Category.RENDER, false, false, false);
/* 14 */     setInstance();
/*    */   }
/*    */   
/*    */   private void setInstance() {
/* 18 */     INSTANCE = this;
/*    */   }
/*    */   
/*    */   public static CameraClip getInstance() {
/* 22 */     if (INSTANCE == null) {
/* 23 */       INSTANCE = new CameraClip();
/*    */     }
/* 25 */     return INSTANCE;
/*    */   }
/*    */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\me\earth\phobos\features\modules\render\CameraClip.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */