/*    */ package me.earth.phobos.features.modules.misc;
/*    */ 
/*    */ import me.earth.phobos.event.events.PacketEvent;
/*    */ import me.earth.phobos.features.modules.Module;
/*    */ import me.earth.phobos.features.setting.Setting;
/*    */ import net.minecraft.init.SoundEvents;
/*    */ import net.minecraft.network.Packet;
/*    */ import net.minecraft.network.play.client.CPacketPlayerDigging;
/*    */ import net.minecraft.network.play.server.SPacketSoundEffect;
/*    */ import net.minecraft.util.EnumFacing;
/*    */ import net.minecraft.util.math.BlockPos;
/*    */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class OffhandCrash
/*    */   extends Module
/*    */ {
/* 23 */   private final Setting<Boolean> antilag = register(new Setting("AntiOffhandCrash", Boolean.valueOf(true)));
/* 24 */   private final Setting<Boolean> docrash = register(new Setting("Use Offhand Crash", Boolean.valueOf(true)));
/* 25 */   private final Setting<Integer> loopzz = register(new Setting("Times to loop", Integer.valueOf(500), Integer.valueOf(100), Integer.valueOf(5000)));
/*    */   
/*    */   public OffhandCrash() {
/* 28 */     super("OffhandCrash", "Spams server with item swap packets to lag/crash other players with large amounts of sound", Module.Category.MISC, false, false, false);
/*    */   }
/*    */   
/*    */   @SubscribeEvent
/*    */   public void onPacketSend(PacketEvent.Receive event) {
/* 33 */     if (((Boolean)this.antilag.getValue()).booleanValue())
/*    */     {
/* 35 */       if (event.getPacket() instanceof SPacketSoundEffect) {
/* 36 */         SPacketSoundEffect packet = (SPacketSoundEffect)event.getPacket();
/* 37 */         if (packet.func_186978_a() == SoundEvents.field_187719_p) {
/* 38 */           event.setCanceled(true);
/*    */         }
/*    */       } 
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public void onLogout() {
/* 46 */     disable();
/*    */   }
/*    */ 
/*    */   
/*    */   public void onUpdate() {
/* 51 */     if (((Boolean)this.docrash.getValue()).booleanValue()) {
/* 52 */       for (int i = 0; i < ((Integer)this.loopzz.getValue()).intValue(); i++) {
/* 53 */         BlockPos playerBlock = new BlockPos(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u - 1.0D, mc.field_71439_g.field_70161_v);
/* 54 */         mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.SWAP_HELD_ITEMS, playerBlock, EnumFacing.UP));
/*    */       } 
/*    */     }
/* 57 */     if (mc.field_71462_r instanceof net.minecraft.client.gui.GuiMainMenu || mc.field_71462_r instanceof net.minecraft.client.gui.GuiDisconnected || mc.field_71462_r instanceof net.minecraft.client.gui.GuiDownloadTerrain || mc.field_71462_r instanceof net.minecraft.client.multiplayer.GuiConnecting || mc.field_71462_r instanceof net.minecraft.client.gui.GuiMultiplayer)
/*    */     {
/*    */ 
/*    */ 
/*    */ 
/*    */       
/* 63 */       if (isEnabled())
/* 64 */         toggle(); 
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\me\earth\phobos\features\modules\misc\OffhandCrash.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */