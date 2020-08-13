/*    */ package me.earth.phobos.features.modules.misc;
/*    */ 
/*    */ import me.earth.phobos.event.events.PacketEvent;
/*    */ import me.earth.phobos.features.modules.Module;
/*    */ import me.earth.phobos.features.setting.Setting;
/*    */ import me.earth.phobos.util.Timer;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.init.Items;
/*    */ import net.minecraft.inventory.ClickType;
/*    */ import net.minecraft.item.ItemStack;
/*    */ import net.minecraft.network.Packet;
/*    */ import net.minecraft.network.play.client.CPacketEntityAction;
/*    */ import net.minecraft.network.play.server.SPacketEntityMetadata;
/*    */ import net.minecraft.network.play.server.SPacketSetSlot;
/*    */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*    */ 
/*    */ public class Bypass extends Module {
/* 19 */   public Setting<Boolean> illegals = register(new Setting("Illegals", Boolean.valueOf(false)));
/* 20 */   public Setting<Boolean> secretClose = register(new Setting("SecretClose", Boolean.valueOf(false), v -> ((Boolean)this.illegals.getValue()).booleanValue()));
/* 21 */   public Setting<Boolean> elytra = register(new Setting("Elytra", Boolean.valueOf(false)));
/* 22 */   public Setting<Boolean> reopen = register(new Setting("Reopen", Boolean.valueOf(false), v -> ((Boolean)this.elytra.getValue()).booleanValue()));
/* 23 */   public Setting<Integer> reopen_interval = register(new Setting("ReopenDelay", Integer.valueOf(1000), Integer.valueOf(0), Integer.valueOf(5000), v -> ((Boolean)this.elytra.getValue()).booleanValue()));
/* 24 */   public Setting<Integer> delay = register(new Setting("Delay", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(1000), v -> ((Boolean)this.elytra.getValue()).booleanValue()));
/* 25 */   public Setting<Boolean> allow_ghost = register(new Setting("Ghost", Boolean.valueOf(true), v -> ((Boolean)this.elytra.getValue()).booleanValue()));
/* 26 */   public Setting<Boolean> cancel_close = register(new Setting("Cancel", Boolean.valueOf(true), v -> ((Boolean)this.elytra.getValue()).booleanValue()));
/* 27 */   public Setting<Boolean> discreet = register(new Setting("Secret", Boolean.valueOf(true), v -> ((Boolean)this.elytra.getValue()).booleanValue()));
/*    */   
/* 29 */   int cooldown = 0;
/* 30 */   private final Timer timer = new Timer();
/*    */   
/*    */   public Bypass() {
/* 33 */     super("Bypass", "Bypass for stuff", Module.Category.MISC, true, false, false);
/*    */   }
/*    */   
/*    */   @SubscribeEvent
/*    */   public void onPacketSend(PacketEvent.Send event) {
/* 38 */     if (((Boolean)this.illegals.getValue()).booleanValue() && ((Boolean)this.secretClose.getValue()).booleanValue() && event.getPacket() instanceof net.minecraft.network.play.client.CPacketCloseWindow) {
/* 39 */       event.setCanceled(true);
/*    */     }
/*    */   }
/*    */   
/*    */   @SubscribeEvent
/*    */   public void onIncomingPacket(PacketEvent.Receive event) {
/* 45 */     if (!fullNullCheck() && ((Boolean)this.elytra.getValue()).booleanValue()) {
/* 46 */       if (event.getPacket() instanceof SPacketSetSlot) {
/* 47 */         SPacketSetSlot packet = (SPacketSetSlot)event.getPacket();
/* 48 */         if (packet.func_149173_d() == 6) {
/* 49 */           event.setCanceled(true);
/*    */         }
/*    */         
/* 52 */         if (!((Boolean)this.allow_ghost.getValue()).booleanValue() && packet.func_149174_e().func_77973_b().equals(Items.field_185160_cR)) {
/* 53 */           event.setCanceled(true);
/*    */         }
/*    */       } 
/*    */       
/* 57 */       if (((Boolean)this.cancel_close.getValue()).booleanValue() && mc.field_71439_g.func_184613_cA() && event.getPacket() instanceof SPacketEntityMetadata) {
/* 58 */         SPacketEntityMetadata MetadataPacket = (SPacketEntityMetadata)event.getPacket();
/* 59 */         if (MetadataPacket.func_149375_d() == mc.field_71439_g.func_145782_y()) {
/* 60 */           event.setCanceled(true);
/*    */         }
/*    */       } 
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void onTick() {
/* 69 */     if (((Boolean)this.elytra.getValue()).booleanValue()) {
/* 70 */       if (this.cooldown > 0) {
/* 71 */         this.cooldown--;
/* 72 */       } else if (mc.field_71439_g != null && !(mc.field_71462_r instanceof net.minecraft.client.gui.inventory.GuiInventory) && (!mc.field_71439_g.field_70122_E || !((Boolean)this.discreet.getValue()).booleanValue())) {
/* 73 */         for (int i = 0; i < 36; i++) {
/* 74 */           ItemStack item = mc.field_71439_g.field_71071_by.func_70301_a(i);
/* 75 */           if (item.func_77973_b().equals(Items.field_185160_cR)) {
/* 76 */             mc.field_71442_b.func_187098_a(0, (i < 9) ? (i + 36) : i, 0, ClickType.QUICK_MOVE, (EntityPlayer)mc.field_71439_g);
/* 77 */             this.cooldown = ((Integer)this.delay.getValue()).intValue();
/*    */             return;
/*    */           } 
/*    */         } 
/*    */       } 
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public void onUpdate() {
/* 87 */     if (((Boolean)this.elytra.getValue()).booleanValue() && this.timer.passedMs(((Integer)this.reopen_interval.getValue()).intValue()) && ((Boolean)this.reopen.getValue()).booleanValue() && !mc.field_71439_g.func_184613_cA() && mc.field_71439_g.field_70143_R > 0.0F)
/* 88 */       mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketEntityAction((Entity)mc.field_71439_g, CPacketEntityAction.Action.START_FALL_FLYING)); 
/*    */   }
/*    */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\me\earth\phobos\features\modules\misc\Bypass.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */