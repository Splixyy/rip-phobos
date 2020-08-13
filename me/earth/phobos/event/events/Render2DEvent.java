/*    */ package me.earth.phobos.event.events;
/*    */ 
/*    */ import me.earth.phobos.event.EventStage;
/*    */ import net.minecraft.client.gui.ScaledResolution;
/*    */ 
/*    */ public class Render2DEvent
/*    */   extends EventStage
/*    */ {
/*    */   public float partialTicks;
/*    */   public ScaledResolution scaledResolution;
/*    */   
/*    */   public Render2DEvent(float partialTicks, ScaledResolution scaledResolution) {
/* 13 */     this.partialTicks = partialTicks;
/* 14 */     this.scaledResolution = scaledResolution;
/*    */   }
/*    */   
/*    */   public void setPartialTicks(float partialTicks) {
/* 18 */     this.partialTicks = partialTicks;
/*    */   }
/*    */   
/*    */   public void setScaledResolution(ScaledResolution scaledResolution) {
/* 22 */     this.scaledResolution = scaledResolution;
/*    */   }
/*    */   
/*    */   public double getScreenWidth() {
/* 26 */     return this.scaledResolution.func_78327_c();
/*    */   }
/*    */   
/*    */   public double getScreenHeight() {
/* 30 */     return this.scaledResolution.func_78324_d();
/*    */   }
/*    */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\me\earth\phobos\event\events\Render2DEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */