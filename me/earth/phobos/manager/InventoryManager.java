/*    */ package me.earth.phobos.manager;
/*    */ 
/*    */ import me.earth.phobos.util.Util;
/*    */ import net.minecraft.network.Packet;
/*    */ import net.minecraft.network.play.client.CPacketHeldItemChange;
/*    */ 
/*    */ public class InventoryManager implements Util {
/*  8 */   private int recoverySlot = -1;
/*    */   
/*    */   public void update() {
/* 11 */     if (this.recoverySlot != -1) {
/* 12 */       mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketHeldItemChange((this.recoverySlot == 8) ? 7 : (this.recoverySlot + 1)));
/* 13 */       mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketHeldItemChange(this.recoverySlot));
/* 14 */       mc.field_71439_g.field_71071_by.field_70461_c = this.recoverySlot;
/* 15 */       mc.field_71442_b.func_78750_j();
/* 16 */       this.recoverySlot = -1;
/*    */     } 
/*    */   }
/*    */   
/*    */   public void recoverSilent(int slot) {
/* 21 */     this.recoverySlot = slot;
/*    */   }
/*    */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\me\earth\phobos\manager\InventoryManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */