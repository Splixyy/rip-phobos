/*    */ package me.earth.phobos.features.modules.player;
/*    */ 
/*    */ import me.earth.phobos.event.events.PacketEvent;
/*    */ import me.earth.phobos.features.modules.Module;
/*    */ import me.earth.phobos.features.setting.Setting;
/*    */ import net.minecraft.network.play.client.CPacketEntityAction;
/*    */ import net.minecraft.network.play.client.CPacketPlayer;
/*    */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class NoHunger
/*    */   extends Module
/*    */ {
/* 15 */   public Setting<Boolean> cancelSprint = register(new Setting("CancelSprint", Boolean.valueOf(true)));
/*    */   
/*    */   public NoHunger() {
/* 18 */     super("NoHunger", "Prevents you from getting Hungry", Module.Category.PLAYER, true, false, false);
/*    */   }
/*    */   
/*    */   @SubscribeEvent
/*    */   public void onPacketSend(PacketEvent.Send event) {
/* 23 */     if (event.getPacket() instanceof CPacketPlayer) {
/* 24 */       CPacketPlayer packet = (CPacketPlayer)event.getPacket();
/* 25 */       packet.field_149474_g = (mc.field_71439_g.field_70143_R >= 0.0F || mc.field_71442_b.field_78778_j);
/*    */     } 
/*    */     
/* 28 */     if (((Boolean)this.cancelSprint.getValue()).booleanValue() && event.getPacket() instanceof CPacketEntityAction) {
/* 29 */       CPacketEntityAction packet = (CPacketEntityAction)event.getPacket();
/* 30 */       if (packet.func_180764_b() == CPacketEntityAction.Action.START_SPRINTING || packet.func_180764_b() == CPacketEntityAction.Action.STOP_SPRINTING)
/* 31 */         event.setCanceled(true); 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\me\earth\phobos\features\modules\player\NoHunger.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */