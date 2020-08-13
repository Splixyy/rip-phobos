/*    */ package me.earth.phobos.event.events;
/*    */ 
/*    */ import me.earth.phobos.event.EventStage;
/*    */ import net.minecraft.entity.MoverType;
/*    */ import net.minecraftforge.fml.common.eventhandler.Cancelable;
/*    */ 
/*    */ @Cancelable
/*    */ public class MoveEvent
/*    */   extends EventStage {
/*    */   private MoverType type;
/*    */   private double x;
/*    */   
/*    */   public MoveEvent(int stage, MoverType type, double x, double y, double z) {
/* 14 */     super(stage);
/* 15 */     this.type = type;
/* 16 */     this.x = x;
/* 17 */     this.y = y;
/* 18 */     this.z = z;
/*    */   }
/*    */   private double y; private double z;
/*    */   public MoverType getType() {
/* 22 */     return this.type;
/*    */   }
/*    */   
/*    */   public void setType(MoverType type) {
/* 26 */     this.type = type;
/*    */   }
/*    */   
/*    */   public double getX() {
/* 30 */     return this.x;
/*    */   }
/*    */   
/*    */   public double getY() {
/* 34 */     return this.y;
/*    */   }
/*    */   
/*    */   public double getZ() {
/* 38 */     return this.z;
/*    */   }
/*    */   
/*    */   public void setX(double x) {
/* 42 */     this.x = x;
/*    */   }
/*    */   
/*    */   public void setY(double y) {
/* 46 */     this.y = y;
/*    */   }
/*    */   
/*    */   public void setZ(double z) {
/* 50 */     this.z = z;
/*    */   }
/*    */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\me\earth\phobos\event\events\MoveEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */