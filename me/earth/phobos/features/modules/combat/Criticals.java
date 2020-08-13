/*     */ package me.earth.phobos.features.modules.combat;
/*     */ 
/*     */ import java.util.Objects;
/*     */ import me.earth.phobos.event.events.PacketEvent;
/*     */ import me.earth.phobos.features.modules.Module;
/*     */ import me.earth.phobos.features.setting.Setting;
/*     */ import me.earth.phobos.util.Timer;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.network.Packet;
/*     */ import net.minecraft.network.play.client.CPacketPlayer;
/*     */ import net.minecraft.network.play.client.CPacketUseEntity;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*     */ 
/*     */ public class Criticals extends Module {
/*  16 */   private Setting<Mode> mode = register(new Setting("Mode", Mode.PACKET));
/*  17 */   public Setting<Boolean> noDesync = register(new Setting("NoDesync", Boolean.valueOf(true)));
/*  18 */   private Setting<Integer> packets = register(new Setting("Packets", Integer.valueOf(2), Integer.valueOf(1), Integer.valueOf(4), v -> (this.mode.getValue() == Mode.PACKET), "Amount of packets you want to send."));
/*  19 */   private Setting<Integer> desyncDelay = register(new Setting("DesyncDelay", Integer.valueOf(10), Integer.valueOf(0), Integer.valueOf(500), v -> (this.mode.getValue() == Mode.PACKET), "Amount of packets you want to send."));
/*  20 */   public Setting<Boolean> cancelFirst = register(new Setting("CancelFirst32k", Boolean.valueOf(true)));
/*  21 */   public Setting<Integer> delay32k = register(new Setting("32kDelay", Integer.valueOf(25), Integer.valueOf(0), Integer.valueOf(500), v -> ((Boolean)this.cancelFirst.getValue()).booleanValue()));
/*     */   
/*  23 */   private Timer timer = new Timer();
/*  24 */   private Timer timer32k = new Timer();
/*     */   private boolean firstCanceled = false;
/*     */   private boolean resetTimer = false;
/*     */   
/*     */   public Criticals() {
/*  29 */     super("Criticals", "Scores criticals for you", Module.Category.COMBAT, true, false, false);
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onPacketSend(PacketEvent.Send event) {
/*  34 */     if (Auto32k.getInstance().isOn() && this.timer.passedMs(500L) && ((Boolean)this.cancelFirst.getValue()).booleanValue()) {
/*  35 */       this.firstCanceled = true;
/*  36 */     } else if (Auto32k.getInstance().isOff() || !((Boolean)this.cancelFirst.getValue()).booleanValue()) {
/*  37 */       this.firstCanceled = false;
/*     */     } 
/*     */     
/*  40 */     if (event.getPacket() instanceof CPacketUseEntity) {
/*  41 */       CPacketUseEntity packet = (CPacketUseEntity)event.getPacket();
/*  42 */       if (packet.func_149565_c() == CPacketUseEntity.Action.ATTACK) {
/*     */         
/*  44 */         if (this.firstCanceled) {
/*  45 */           this.timer32k.reset();
/*  46 */           this.resetTimer = true;
/*  47 */           this.timer.setMs((((Integer)this.desyncDelay.getValue()).intValue() + 1));
/*  48 */           this.firstCanceled = false;
/*     */           
/*     */           return;
/*     */         } 
/*  52 */         if (this.resetTimer && !this.timer32k.passedMs(((Integer)this.delay32k.getValue()).intValue()))
/*     */           return; 
/*  54 */         if (this.resetTimer && this.timer32k.passedMs(((Integer)this.delay32k.getValue()).intValue())) {
/*  55 */           this.resetTimer = false;
/*     */         }
/*     */         
/*  58 */         if (!this.timer.passedMs(((Integer)this.desyncDelay.getValue()).intValue())) {
/*     */           return;
/*     */         }
/*     */         
/*  62 */         if (mc.field_71439_g.field_70122_E && !mc.field_71474_y.field_74314_A.func_151470_d() && (packet.func_149564_a((World)mc.field_71441_e) instanceof net.minecraft.entity.EntityLivingBase || !((Boolean)this.noDesync.getValue()).booleanValue()) && !mc.field_71439_g.func_70090_H() && !mc.field_71439_g.func_180799_ab()) {
/*  63 */           if (this.mode.getValue() == Mode.PACKET) {
/*  64 */             switch (((Integer)this.packets.getValue()).intValue()) {
/*     */               case 1:
/*  66 */                 mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u + 0.10000000149011612D, mc.field_71439_g.field_70161_v, false));
/*  67 */                 mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u, mc.field_71439_g.field_70161_v, false));
/*     */                 break;
/*     */               case 2:
/*  70 */                 mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u + 0.0625101D, mc.field_71439_g.field_70161_v, false));
/*  71 */                 mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u, mc.field_71439_g.field_70161_v, false));
/*  72 */                 mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u + 1.1E-5D, mc.field_71439_g.field_70161_v, false));
/*  73 */                 mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u, mc.field_71439_g.field_70161_v, false));
/*     */                 break;
/*     */               case 3:
/*  76 */                 mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u + 0.0625101D, mc.field_71439_g.field_70161_v, false));
/*  77 */                 mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u, mc.field_71439_g.field_70161_v, false));
/*  78 */                 mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u + 0.0125D, mc.field_71439_g.field_70161_v, false));
/*  79 */                 mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u, mc.field_71439_g.field_70161_v, false));
/*     */                 break;
/*     */               case 4:
/*  82 */                 mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u + 0.1625D, mc.field_71439_g.field_70161_v, false));
/*  83 */                 mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u, mc.field_71439_g.field_70161_v, false));
/*  84 */                 mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u + 4.0E-6D, mc.field_71439_g.field_70161_v, false));
/*  85 */                 mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u, mc.field_71439_g.field_70161_v, false));
/*  86 */                 mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u + 1.0E-6D, mc.field_71439_g.field_70161_v, false));
/*  87 */                 mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u, mc.field_71439_g.field_70161_v, false));
/*  88 */                 mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer());
/*  89 */                 mc.field_71439_g.func_71009_b(Objects.<Entity>requireNonNull(packet.func_149564_a((World)mc.field_71441_e)));
/*     */                 break;
/*     */             } 
/*     */           
/*     */           } else {
/*  94 */             mc.field_71439_g.func_70664_aZ();
/*     */           } 
/*  96 */           this.timer.reset();
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public String getDisplayInfo() {
/* 104 */     return this.mode.currentEnumName();
/*     */   }
/*     */   
/*     */   public enum Mode {
/* 108 */     JUMP,
/* 109 */     PACKET;
/*     */   }
/*     */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\me\earth\phobos\features\modules\combat\Criticals.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */