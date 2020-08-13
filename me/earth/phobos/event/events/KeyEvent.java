/*    */ package me.earth.phobos.event.events;
/*    */ 
/*    */ import me.earth.phobos.event.EventStage;
/*    */ 
/*    */ public class KeyEvent
/*    */   extends EventStage {
/*    */   public boolean info;
/*    */   public boolean pressed;
/*    */   
/*    */   public KeyEvent(int stage, boolean info, boolean pressed) {
/* 11 */     super(stage);
/* 12 */     this.info = info;
/* 13 */     this.pressed = pressed;
/*    */   }
/*    */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\me\earth\phobos\event\events\KeyEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */