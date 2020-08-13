/*    */ package me.earth.phobos.manager;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import me.earth.phobos.features.Feature;
/*    */ import net.minecraft.network.Packet;
/*    */ 
/*    */ public class PacketManager
/*    */   extends Feature
/*    */ {
/* 11 */   private final List<Packet<?>> noEventPackets = new ArrayList<>();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void sendPacketNoEvent(Packet<?> packet) {
/* 32 */     if (packet != null && !nullCheck()) {
/* 33 */       this.noEventPackets.add(packet);
/* 34 */       mc.field_71439_g.field_71174_a.func_147297_a(packet);
/*    */     } 
/*    */   }
/*    */   
/*    */   public boolean shouldSendPacket(Packet<?> packet) {
/* 39 */     if (this.noEventPackets.contains(packet)) {
/* 40 */       this.noEventPackets.remove(packet);
/* 41 */       return false;
/*    */     } 
/* 43 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\me\earth\phobos\manager\PacketManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */