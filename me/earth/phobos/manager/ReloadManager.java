/*    */ package me.earth.phobos.manager;
/*    */ 
/*    */ import me.earth.phobos.Phobos;
/*    */ import me.earth.phobos.event.events.PacketEvent;
/*    */ import me.earth.phobos.features.Feature;
/*    */ import me.earth.phobos.features.command.Command;
/*    */ import net.minecraft.network.play.client.CPacketChatMessage;
/*    */ import net.minecraftforge.common.MinecraftForge;
/*    */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*    */ 
/*    */ public class ReloadManager
/*    */   extends Feature
/*    */ {
/*    */   public String prefix;
/*    */   
/*    */   public void init(String prefix) {
/* 17 */     this.prefix = prefix;
/* 18 */     MinecraftForge.EVENT_BUS.register(this);
/* 19 */     if (!fullNullCheck()) {
/* 20 */       Command.sendMessage("§cPhobos has been unloaded. Type " + prefix + "reload to reload.");
/*    */     }
/*    */   }
/*    */   
/*    */   public void unload() {
/* 25 */     MinecraftForge.EVENT_BUS.unregister(this);
/*    */   }
/*    */   
/*    */   @SubscribeEvent
/*    */   public void onPacketSend(PacketEvent.Send event) {
/* 30 */     if (event.getPacket() instanceof CPacketChatMessage) {
/* 31 */       CPacketChatMessage packet = (CPacketChatMessage)event.getPacket();
/* 32 */       if (packet.func_149439_c().startsWith(this.prefix) && packet.func_149439_c().contains("reload")) {
/* 33 */         Phobos.load();
/* 34 */         event.setCanceled(true);
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\me\earth\phobos\manager\ReloadManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */