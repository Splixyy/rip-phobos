/*    */ package me.earth.phobos.features.modules.misc;
/*    */ 
/*    */ import me.earth.phobos.event.events.PacketEvent;
/*    */ import me.earth.phobos.features.command.Command;
/*    */ import me.earth.phobos.features.modules.Module;
/*    */ import me.earth.phobos.features.setting.Setting;
/*    */ import net.minecraft.init.Items;
/*    */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*    */ 
/*    */ public class Logger
/*    */   extends Module {
/* 12 */   public Setting<Packets> packets = register(new Setting("Packets", Packets.OUTGOING));
/* 13 */   public Setting<Boolean> chat = register(new Setting("Chat", Boolean.valueOf(false)));
/*    */   
/*    */   public Logger() {
/* 16 */     super("Logger", "Logs stuff", Module.Category.MISC, true, false, false);
/*    */   }
/*    */   
/*    */   @SubscribeEvent
/*    */   public void onPacketSend(PacketEvent.Send event) {
/* 21 */     if (this.packets.getValue() == Packets.OUTGOING || this.packets.getValue() == Packets.ALL) {
/* 22 */       if (((Boolean)this.chat.getValue()).booleanValue()) {
/* 23 */         Command.sendMessage(event.getPacket().toString());
/*    */       } else {
/* 25 */         System.out.println(event.getPacket().toString());
/*    */       } 
/*    */     }
/*    */   }
/*    */   
/*    */   @SubscribeEvent
/*    */   public void onPacketReceive(PacketEvent.Receive event) {
/* 32 */     if (this.packets.getValue() == Packets.INCOMING || this.packets.getValue() == Packets.ALL) {
/* 33 */       boolean xD = true;
/* 34 */       if (mc.field_71439_g != null) {
/* 35 */         xD = (mc.field_71439_g.field_71069_bz.func_75139_a(6).func_75211_c().func_77973_b() == Items.field_185160_cR);
/* 36 */         System.out.println(xD);
/*    */       } 
/* 38 */       if (((Boolean)this.chat.getValue()).booleanValue()) {
/* 39 */         Command.sendMessage(xD + event.getPacket().toString());
/*    */       } else {
/* 41 */         System.out.println(xD + event.getPacket().toString());
/*    */       } 
/*    */     } 
/*    */   }
/*    */   
/*    */   public enum Packets {
/* 47 */     NONE,
/* 48 */     INCOMING,
/* 49 */     OUTGOING,
/* 50 */     ALL;
/*    */   }
/*    */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\me\earth\phobos\features\modules\misc\Logger.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */