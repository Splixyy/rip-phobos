/*     */ package me.earth.phobos.features.modules.player;
/*     */ 
/*     */ import java.util.Queue;
/*     */ import java.util.concurrent.ConcurrentLinkedQueue;
/*     */ import me.earth.phobos.event.events.PacketEvent;
/*     */ import me.earth.phobos.features.modules.Module;
/*     */ import me.earth.phobos.features.setting.Setting;
/*     */ import me.earth.phobos.util.MathUtil;
/*     */ import me.earth.phobos.util.Timer;
/*     */ import net.minecraft.client.entity.EntityOtherPlayerMP;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.network.Packet;
/*     */ import net.minecraft.util.math.BlockPos;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Blink
/*     */   extends Module
/*     */ {
/*  24 */   public Setting<Boolean> cPacketPlayer = register(new Setting("CPacketPlayer", Boolean.valueOf(true)));
/*  25 */   public Setting<Mode> autoOff = register(new Setting("AutoOff", Mode.MANUAL));
/*  26 */   public Setting<Integer> timeLimit = register(new Setting("Time", Integer.valueOf(20), Integer.valueOf(1), Integer.valueOf(500), v -> (this.autoOff.getValue() == Mode.TIME)));
/*  27 */   public Setting<Integer> packetLimit = register(new Setting("Packets", Integer.valueOf(20), Integer.valueOf(1), Integer.valueOf(500), v -> (this.autoOff.getValue() == Mode.PACKETS)));
/*  28 */   public Setting<Float> distance = register(new Setting("Distance", Float.valueOf(10.0F), Float.valueOf(1.0F), Float.valueOf(100.0F), v -> (this.autoOff.getValue() == Mode.DISTANCE)));
/*     */   
/*  30 */   private Timer timer = new Timer();
/*  31 */   private Queue<Packet<?>> packets = new ConcurrentLinkedQueue<>();
/*     */   private EntityOtherPlayerMP entity;
/*  33 */   private int packetsCanceled = 0;
/*  34 */   private BlockPos startPos = null;
/*     */   
/*  36 */   private static Blink INSTANCE = new Blink();
/*     */   
/*     */   public Blink() {
/*  39 */     super("Blink", "Fakelag.", Module.Category.PLAYER, true, false, false);
/*  40 */     setInstance();
/*     */   }
/*     */   
/*     */   private void setInstance() {
/*  44 */     INSTANCE = this;
/*     */   }
/*     */   
/*     */   public static Blink getInstance() {
/*  48 */     if (INSTANCE == null) {
/*  49 */       INSTANCE = new Blink();
/*     */     }
/*  51 */     return INSTANCE;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onEnable() {
/*  56 */     if (!fullNullCheck()) {
/*  57 */       this.entity = new EntityOtherPlayerMP((World)mc.field_71441_e, mc.field_71449_j.func_148256_e());
/*  58 */       this.entity.func_82149_j((Entity)mc.field_71439_g);
/*  59 */       this.entity.field_70177_z = mc.field_71439_g.field_70177_z;
/*  60 */       this.entity.field_70759_as = mc.field_71439_g.field_70759_as;
/*  61 */       this.entity.field_71071_by.func_70455_b(mc.field_71439_g.field_71071_by);
/*  62 */       mc.field_71441_e.func_73027_a(6942069, (Entity)this.entity);
/*  63 */       this.startPos = mc.field_71439_g.func_180425_c();
/*     */     } else {
/*  65 */       disable();
/*     */     } 
/*  67 */     this.packetsCanceled = 0;
/*  68 */     this.timer.reset();
/*     */   }
/*     */ 
/*     */   
/*     */   public void onUpdate() {
/*  73 */     if (nullCheck() || (this.autoOff.getValue() == Mode.TIME && this.timer.passedS(((Integer)this.timeLimit.getValue()).intValue())) || (this.autoOff.getValue() == Mode.DISTANCE && this.startPos != null && mc.field_71439_g.func_174818_b(this.startPos) >= MathUtil.square(((Float)this.distance.getValue()).floatValue())) || (this.autoOff.getValue() == Mode.PACKETS && this.packetsCanceled >= ((Integer)this.packetLimit.getValue()).intValue())) {
/*  74 */       disable();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void onLogout() {
/*  80 */     if (isOn()) {
/*  81 */       disable();
/*     */     }
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onSendPacket(PacketEvent.Send event) {
/*  87 */     if (event.getStage() == 0 && mc.field_71441_e != null && !mc.func_71356_B()) {
/*  88 */       Packet<?> packet = event.getPacket();
/*  89 */       if (((Boolean)this.cPacketPlayer.getValue()).booleanValue() && packet instanceof net.minecraft.network.play.client.CPacketPlayer) {
/*  90 */         event.setCanceled(true);
/*  91 */         this.packets.add(packet);
/*  92 */         this.packetsCanceled++;
/*     */       } 
/*     */       
/*  95 */       if (!((Boolean)this.cPacketPlayer.getValue()).booleanValue()) {
/*  96 */         if (packet instanceof net.minecraft.network.play.client.CPacketChatMessage || packet instanceof net.minecraft.network.play.client.CPacketConfirmTeleport || packet instanceof net.minecraft.network.play.client.CPacketKeepAlive || packet instanceof net.minecraft.network.play.client.CPacketTabComplete || packet instanceof net.minecraft.network.play.client.CPacketClientStatus) {
/*     */           return;
/*     */         }
/*  99 */         this.packets.add(packet);
/* 100 */         event.setCanceled(true);
/* 101 */         this.packetsCanceled++;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onDisable() {
/* 108 */     if (!fullNullCheck()) {
/* 109 */       mc.field_71441_e.func_72900_e((Entity)this.entity);
/* 110 */       while (!this.packets.isEmpty()) {
/* 111 */         mc.field_71439_g.field_71174_a.func_147297_a(this.packets.poll());
/*     */       }
/*     */     } 
/* 114 */     this.startPos = null;
/*     */   }
/*     */   
/*     */   public enum Mode {
/* 118 */     MANUAL,
/* 119 */     TIME,
/* 120 */     DISTANCE,
/* 121 */     PACKETS;
/*     */   }
/*     */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\me\earth\phobos\features\modules\player\Blink.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */