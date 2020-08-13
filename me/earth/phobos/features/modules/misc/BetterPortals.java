/*    */ package me.earth.phobos.features.modules.misc;
/*    */ 
/*    */ import me.earth.phobos.event.events.PacketEvent;
/*    */ import me.earth.phobos.features.modules.Module;
/*    */ import me.earth.phobos.features.setting.Setting;
/*    */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*    */ 
/*    */ public class BetterPortals
/*    */   extends Module
/*    */ {
/* 11 */   public Setting<Boolean> portalChat = register(new Setting("Chat", Boolean.valueOf(true), "Allows you to chat in portals."));
/* 12 */   public Setting<Boolean> godmode = register(new Setting("Godmode", Boolean.valueOf(false), "Portal Godmode."));
/* 13 */   public Setting<Boolean> fastPortal = register(new Setting("FastPortal", Boolean.valueOf(false)));
/* 14 */   public Setting<Integer> cooldown = register(new Setting("Cooldown", Integer.valueOf(5), Integer.valueOf(1), Integer.valueOf(10), v -> ((Boolean)this.fastPortal.getValue()).booleanValue(), "Portal cooldown."));
/* 15 */   public Setting<Integer> time = register(new Setting("Time", Integer.valueOf(5), Integer.valueOf(0), Integer.valueOf(80), v -> ((Boolean)this.fastPortal.getValue()).booleanValue(), "Time in Portal"));
/*    */   
/* 17 */   private static BetterPortals INSTANCE = new BetterPortals();
/*    */   
/*    */   public BetterPortals() {
/* 20 */     super("BetterPortals", "Tweaks for Portals", Module.Category.MISC, true, false, false);
/* 21 */     setInstance();
/*    */   }
/*    */   
/*    */   private void setInstance() {
/* 25 */     INSTANCE = this;
/*    */   }
/*    */   
/*    */   public static BetterPortals getInstance() {
/* 29 */     if (INSTANCE == null) {
/* 30 */       INSTANCE = new BetterPortals();
/*    */     }
/* 32 */     return INSTANCE;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getDisplayInfo() {
/* 37 */     if (((Boolean)this.godmode.getValue()).booleanValue()) {
/* 38 */       return "Godmode";
/*    */     }
/* 40 */     return null;
/*    */   }
/*    */   
/*    */   @SubscribeEvent
/*    */   public void onPacketSend(PacketEvent.Send event) {
/* 45 */     if (event.getStage() == 0 && ((Boolean)this.godmode.getValue()).booleanValue() && event.getPacket() instanceof net.minecraft.network.play.client.CPacketConfirmTeleport)
/* 46 */       event.setCanceled(true); 
/*    */   }
/*    */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\me\earth\phobos\features\modules\misc\BetterPortals.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */