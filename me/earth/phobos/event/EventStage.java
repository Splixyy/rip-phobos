/*    */ package me.earth.phobos.event;
/*    */ 
/*    */ import net.minecraftforge.fml.common.eventhandler.Event;
/*    */ 
/*    */ public class EventStage
/*    */   extends Event {
/*    */   private int stage;
/*    */   
/*    */   public EventStage() {}
/*    */   
/*    */   public EventStage(int stage) {
/* 12 */     this.stage = stage;
/*    */   }
/*    */   
/*    */   public int getStage() {
/* 16 */     return this.stage;
/*    */   }
/*    */   
/*    */   public void setStage(int stage) {
/* 20 */     this.stage = stage;
/*    */   }
/*    */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\me\earth\phobos\event\EventStage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */