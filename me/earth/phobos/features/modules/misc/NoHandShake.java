/*    */ package me.earth.phobos.features.modules.misc;
/*    */ 
/*    */ import io.netty.buffer.Unpooled;
/*    */ import me.earth.phobos.event.events.PacketEvent;
/*    */ import me.earth.phobos.features.modules.Module;
/*    */ import net.minecraft.network.PacketBuffer;
/*    */ import net.minecraft.network.play.client.CPacketCustomPayload;
/*    */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*    */ 
/*    */ public class NoHandShake
/*    */   extends Module
/*    */ {
/*    */   public NoHandShake() {
/* 14 */     super("NoHandshake", "Doesnt send your modlist to the server.", Module.Category.MISC, true, false, false);
/*    */   }
/*    */   
/*    */   @SubscribeEvent
/*    */   public void onPacketSend(PacketEvent.Send event) {
/* 19 */     if (event.getPacket() instanceof net.minecraftforge.fml.common.network.internal.FMLProxyPacket && !mc.func_71356_B()) {
/* 20 */       event.setCanceled(true);
/*    */     }
/*    */     
/* 23 */     if (event.getPacket() instanceof CPacketCustomPayload) {
/* 24 */       CPacketCustomPayload packet = (CPacketCustomPayload)event.getPacket();
/* 25 */       if (packet.func_149559_c().equals("MC|Brand"))
/* 26 */         packet.field_149561_c = (new PacketBuffer(Unpooled.buffer())).func_180714_a("vanilla"); 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\me\earth\phobos\features\modules\misc\NoHandShake.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */