/*    */ package me.earth.phobos.manager;
/*    */ 
/*    */ import me.earth.phobos.features.Feature;
/*    */ import net.minecraft.network.Packet;
/*    */ import net.minecraft.network.play.client.CPacketPlayer;
/*    */ 
/*    */ public class PositionManager extends Feature {
/*    */   private double x;
/*    */   private double y;
/*    */   private double z;
/*    */   private boolean onground;
/*    */   
/*    */   public void updatePosition() {
/* 14 */     this.x = mc.field_71439_g.field_70165_t;
/* 15 */     this.y = mc.field_71439_g.field_70163_u;
/* 16 */     this.z = mc.field_71439_g.field_70161_v;
/* 17 */     this.onground = mc.field_71439_g.field_70122_E;
/*    */   }
/*    */   
/*    */   public void restorePosition() {
/* 21 */     mc.field_71439_g.field_70165_t = this.x;
/* 22 */     mc.field_71439_g.field_70163_u = this.y;
/* 23 */     mc.field_71439_g.field_70161_v = this.z;
/* 24 */     mc.field_71439_g.field_70122_E = this.onground;
/*    */   }
/*    */   
/*    */   public void setPlayerPosition(double x, double y, double z) {
/* 28 */     mc.field_71439_g.field_70165_t = x;
/* 29 */     mc.field_71439_g.field_70163_u = y;
/* 30 */     mc.field_71439_g.field_70161_v = z;
/*    */   }
/*    */   
/*    */   public void setPlayerPosition(double x, double y, double z, boolean onground) {
/* 34 */     mc.field_71439_g.field_70165_t = x;
/* 35 */     mc.field_71439_g.field_70163_u = y;
/* 36 */     mc.field_71439_g.field_70161_v = z;
/* 37 */     mc.field_71439_g.field_70122_E = onground;
/*    */   }
/*    */   
/*    */   public void setPositionPacket(double x, double y, double z, boolean onGround, boolean setPos, boolean noLagBack) {
/* 41 */     mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(x, y, z, onGround));
/* 42 */     if (setPos) {
/* 43 */       mc.field_71439_g.func_70107_b(x, y, z);
/* 44 */       if (noLagBack) {
/* 45 */         updatePosition();
/*    */       }
/*    */     } 
/*    */   }
/*    */   
/*    */   public double getX() {
/* 51 */     return this.x;
/*    */   }
/*    */   
/*    */   public void setX(double x) {
/* 55 */     this.x = x;
/*    */   }
/*    */   
/*    */   public double getY() {
/* 59 */     return this.y;
/*    */   }
/*    */   
/*    */   public void setY(double y) {
/* 63 */     this.y = y;
/*    */   }
/*    */   
/*    */   public double getZ() {
/* 67 */     return this.z;
/*    */   }
/*    */   
/*    */   public void setZ(double z) {
/* 71 */     this.z = z;
/*    */   }
/*    */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\me\earth\phobos\manager\PositionManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */