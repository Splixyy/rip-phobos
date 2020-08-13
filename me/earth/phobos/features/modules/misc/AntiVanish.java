/*    */ package me.earth.phobos.features.modules.misc;
/*    */ 
/*    */ import java.util.Queue;
/*    */ import java.util.UUID;
/*    */ import java.util.concurrent.ConcurrentLinkedQueue;
/*    */ import me.earth.phobos.event.events.PacketEvent;
/*    */ import me.earth.phobos.features.command.Command;
/*    */ import me.earth.phobos.features.modules.Module;
/*    */ import me.earth.phobos.util.PlayerUtil;
/*    */ import net.minecraft.network.play.server.SPacketPlayerListItem;
/*    */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*    */ 
/*    */ 
/*    */ public class AntiVanish
/*    */   extends Module
/*    */ {
/* 17 */   private final Queue<UUID> toLookUp = new ConcurrentLinkedQueue<>();
/*    */   
/*    */   public AntiVanish() {
/* 20 */     super("AntiVanish", "Notifies you when players vanish", Module.Category.MISC, true, false, false);
/*    */   }
/*    */   
/*    */   @SubscribeEvent
/*    */   public void onPacketReceive(PacketEvent.Receive event) {
/* 25 */     if (event.getPacket() instanceof SPacketPlayerListItem) {
/* 26 */       SPacketPlayerListItem sPacketPlayerListItem = (SPacketPlayerListItem)event.getPacket();
/* 27 */       if (sPacketPlayerListItem.func_179768_b() == SPacketPlayerListItem.Action.UPDATE_LATENCY) {
/* 28 */         for (SPacketPlayerListItem.AddPlayerData addPlayerData : sPacketPlayerListItem.func_179767_a()) {
/*    */           try {
/* 30 */             if (mc.func_147114_u().func_175102_a(addPlayerData.func_179962_a().getId()) == null) {
/* 31 */               this.toLookUp.add(addPlayerData.func_179962_a().getId());
/*    */             }
/* 33 */           } catch (Exception e) {
/* 34 */             e.printStackTrace();
/*    */             return;
/*    */           } 
/*    */         } 
/*    */       }
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void onUpdate() {
/* 44 */     if (PlayerUtil.timer.passedS(5.0D)) {
/* 45 */       UUID lookUp = this.toLookUp.poll();
/* 46 */       if (lookUp != null) {
/*    */         try {
/* 48 */           String name = PlayerUtil.getNameFromUUID(lookUp);
/* 49 */           if (name != null) {
/* 50 */             Command.sendMessage("§c" + name + " has gone into vanish.");
/*    */           }
/* 52 */         } catch (Exception exception) {}
/* 53 */         PlayerUtil.timer.reset();
/*    */       } 
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void onLogout() {
/* 60 */     this.toLookUp.clear();
/*    */   }
/*    */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\me\earth\phobos\features\modules\misc\AntiVanish.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */