/*    */ package me.earth.phobos.manager;
/*    */ 
/*    */ import me.earth.phobos.Phobos;
/*    */ import me.earth.phobos.features.Feature;
/*    */ import me.earth.phobos.features.modules.player.TimerSpeed;
/*    */ 
/*    */ public class TimerManager
/*    */   extends Feature {
/*  9 */   private float timer = 1.0F;
/*    */   private TimerSpeed module;
/*    */   
/*    */   public void init() {
/* 13 */     this.module = Phobos.moduleManager.<TimerSpeed>getModuleByClass(TimerSpeed.class);
/*    */   }
/*    */   
/*    */   public void unload() {
/* 17 */     this.timer = 1.0F;
/* 18 */     mc.field_71428_T.field_194149_e = 50.0F;
/*    */   }
/*    */   
/*    */   public void update() {
/* 22 */     if (this.module != null && this.module.isEnabled()) {
/* 23 */       this.timer = this.module.speed;
/*    */     }
/* 25 */     mc.field_71428_T.field_194149_e = 50.0F / ((this.timer <= 0.0F) ? 0.1F : this.timer);
/*    */   }
/*    */   
/*    */   public void setTimer(float timer) {
/* 29 */     if (timer > 0.0F) {
/* 30 */       this.timer = timer;
/*    */     }
/*    */   }
/*    */   
/*    */   public float getTimer() {
/* 35 */     return this.timer;
/*    */   }
/*    */   
/*    */   public void reset() {
/* 39 */     this.timer = 1.0F;
/*    */   }
/*    */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\me\earth\phobos\manager\TimerManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */