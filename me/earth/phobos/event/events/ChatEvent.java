/*    */ package me.earth.phobos.event.events;
/*    */ 
/*    */ import me.earth.phobos.event.EventStage;
/*    */ import net.minecraftforge.fml.common.eventhandler.Cancelable;
/*    */ 
/*    */ 
/*    */ @Cancelable
/*    */ public class ChatEvent
/*    */   extends EventStage
/*    */ {
/*    */   private final String msg;
/*    */   
/*    */   public ChatEvent(String msg) {
/* 14 */     this.msg = msg;
/*    */   }
/*    */   public String getMsg() {
/* 17 */     return this.msg;
/*    */   }
/*    */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\me\earth\phobos\event\events\ChatEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */