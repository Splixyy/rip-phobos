/*    */ package me.earth.phobos.features.modules.client;
/*    */ 
/*    */ import java.util.Objects;
/*    */ import me.earth.phobos.event.events.PacketEvent;
/*    */ import me.earth.phobos.features.command.Command;
/*    */ import me.earth.phobos.features.modules.Module;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.network.play.client.CPacketUseEntity;
/*    */ import net.minecraft.world.World;
/*    */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*    */ 
/*    */ public class PingBypass extends Module {
/*    */   public PingBypass() {
/* 14 */     super("PingBypass", "Big Hack", Module.Category.CLIENT, true, false, false);
/*    */   }
/*    */   
/*    */   @SubscribeEvent
/*    */   public void onPacketSend(PacketEvent.Send event) {
/* 19 */     if (event.getPacket() instanceof CPacketUseEntity) {
/* 20 */       CPacketUseEntity packet = (CPacketUseEntity)event.getPacket();
/* 21 */       Command.sendMessage(((Entity)Objects.<Entity>requireNonNull(packet.func_149564_a((World)mc.field_71441_e))).func_145782_y() + "");
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\me\earth\phobos\features\modules\client\PingBypass.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */