/*    */ package me.earth.phobos.event.events;
/*    */ 
/*    */ import me.earth.phobos.event.EventStage;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraftforge.fml.common.eventhandler.Cancelable;
/*    */ 
/*    */ @Cancelable
/*    */ public class PushEvent
/*    */   extends EventStage {
/*    */   public Entity entity;
/*    */   public double x;
/*    */   public double y;
/*    */   public double z;
/*    */   public boolean airbone;
/*    */   
/*    */   public PushEvent(Entity entity, double x, double y, double z, boolean airbone) {
/* 17 */     super(0);
/* 18 */     this.entity = entity;
/* 19 */     this.x = x;
/* 20 */     this.y = y;
/* 21 */     this.z = z;
/* 22 */     this.airbone = airbone;
/*    */   }
/*    */   
/*    */   public PushEvent(int stage) {
/* 26 */     super(stage);
/*    */   }
/*    */   
/*    */   public PushEvent(int stage, Entity entity) {
/* 30 */     super(stage);
/* 31 */     this.entity = entity;
/*    */   }
/*    */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\me\earth\phobos\event\events\PushEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */