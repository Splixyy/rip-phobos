/*     */ package me.earth.phobos.features.modules.combat;
/*     */ import com.google.common.util.concurrent.AtomicDouble;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
/*     */ import me.earth.phobos.event.events.PacketEvent;
/*     */ import me.earth.phobos.features.setting.Setting;
/*     */ import me.earth.phobos.util.BlockUtil;
/*     */ import me.earth.phobos.util.DamageUtil;
/*     */ import me.earth.phobos.util.EntityUtil;
/*     */ import me.earth.phobos.util.Timer;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.network.play.client.CPacketPlayer;
/*     */ import net.minecraft.network.play.server.SPacketCombatEvent;
/*     */ import net.minecraft.util.EnumHand;
/*     */ import net.minecraft.util.math.BlockPos;
/*     */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*     */ 
/*     */ public class BedBomb extends Module {
/*     */   private final Setting<Integer> breakDelay;
/*     */   private final Setting<Float> breakRange;
/*     */   private final Setting<Boolean> calc;
/*     */   private final Setting<Float> minDamage;
/*     */   private final Setting<Float> range;
/*     */   private final Setting<Boolean> rotate;
/*     */   
/*     */   public BedBomb() {
/*  27 */     super("BedAura", "AutoPlace and Break for beds", Module.Category.COMBAT, true, false, false);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  32 */     this.breakDelay = register(new Setting("Breakdelay", Integer.valueOf(50), Integer.valueOf(0), Integer.valueOf(500)));
/*  33 */     this.breakRange = register(new Setting("BreakRange", Float.valueOf(6.0F), Float.valueOf(1.0F), Float.valueOf(10.0F)));
/*  34 */     this.calc = register(new Setting("Calc", Boolean.valueOf(false)));
/*  35 */     this.minDamage = register(new Setting("MinDamage", Float.valueOf(5.0F), Float.valueOf(1.0F), Float.valueOf(36.0F), v -> ((Boolean)this.calc.getValue()).booleanValue()));
/*  36 */     this.range = register(new Setting("Range", Float.valueOf(10.0F), Float.valueOf(1.0F), Float.valueOf(12.0F), v -> ((Boolean)this.calc.getValue()).booleanValue()));
/*  37 */     this.rotate = register(new Setting("Rotate", Boolean.valueOf(false), v -> ((Boolean)this.calc.getValue()).booleanValue()));
/*  38 */     this.suicide = register(new Setting("Suicide", Boolean.valueOf(false), v -> ((Boolean)this.calc.getValue()).booleanValue()));
/*  39 */     this.offhand = register(new Setting("OffHand", Boolean.valueOf(false)));
/*     */     
/*  41 */     this.breakTimer = new Timer();
/*     */ 
/*     */     
/*  44 */     this.yaw = new AtomicDouble(-1.0D);
/*  45 */     this.pitch = new AtomicDouble(-1.0D);
/*  46 */     this.shouldRotate = new AtomicBoolean(false);
/*     */   } private final Setting<Boolean> suicide; private final Setting<Boolean> offhand; private final Timer breakTimer; private AtomicDouble yaw; private AtomicDouble pitch; private AtomicBoolean shouldRotate;
/*     */   @SubscribeEvent
/*     */   public void onDeath(PacketEvent.Receive event) {
/*  50 */     if (event.getPacket() instanceof SPacketCombatEvent) {
/*  51 */       SPacketCombatEvent packet = (SPacketCombatEvent)event.getPacket();
/*  52 */       if (packet.field_179776_a == SPacketCombatEvent.Event.ENTITY_DIED) {
/*  53 */         Entity entity = mc.field_71441_e.func_73045_a(packet.field_179775_c);
/*  54 */         if (entity instanceof EntityPlayer) {
/*  55 */           Command.sendMessage(entity.func_70005_c_() + " died.");
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onDeath(PacketEvent.Send event) {
/*  63 */     if (((Boolean)this.rotate.getValue()).booleanValue() && this.shouldRotate.get() && 
/*  64 */       event.getPacket() instanceof CPacketPlayer) {
/*  65 */       CPacketPlayer packet = (CPacketPlayer)event.getPacket();
/*  66 */       packet.field_149476_e = (float)this.yaw.get();
/*  67 */       packet.field_149473_f = (float)this.pitch.get();
/*  68 */       this.shouldRotate.set(false);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void onTick() {
/*  75 */     if (mc.field_71439_g.field_71093_bK != -1 && mc.field_71439_g.field_71093_bK != 1) {
/*     */       return;
/*     */     }
/*     */     
/*  79 */     if (this.breakTimer.passedMs(((Integer)this.breakDelay.getValue()).intValue())) {
/*  80 */       BlockPos maxPos = null;
/*  81 */       float maxDamage = 0.5F;
/*  82 */       for (BlockPos pos : BlockUtil.getBlockSphere(((Float)this.breakRange.getValue()).floatValue(), BlockBed.class)) {
/*  83 */         if (((Boolean)this.calc.getValue()).booleanValue()) {
/*  84 */           float selfDamage = DamageUtil.calculateDamage(pos, (Entity)mc.field_71439_g);
/*  85 */           if (selfDamage + 0.5D < EntityUtil.getHealth((Entity)mc.field_71439_g) || !DamageUtil.canTakeDamage(((Boolean)this.suicide.getValue()).booleanValue())) {
/*  86 */             for (EntityPlayer player : mc.field_71441_e.field_73010_i) {
/*  87 */               if (player.func_174818_b(pos) < MathUtil.square(((Float)this.range.getValue()).floatValue()) && EntityUtil.isValid((Entity)player, (((Float)this.range.getValue()).floatValue() + ((Float)this.breakRange.getValue()).floatValue()))) {
/*  88 */                 float damage = DamageUtil.calculateDamage(pos, (Entity)player);
/*  89 */                 if ((damage > selfDamage || (damage > ((Float)this.minDamage.getValue()).floatValue() && !DamageUtil.canTakeDamage(((Boolean)this.suicide.getValue()).booleanValue())) || damage > EntityUtil.getHealth((Entity)player)) && 
/*  90 */                   damage > maxDamage) {
/*  91 */                   maxDamage = damage;
/*  92 */                   maxPos = pos;
/*     */                 } 
/*     */               } 
/*     */             } 
/*     */           }
/*     */           continue;
/*     */         } 
/*  99 */         maxPos = pos;
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 104 */       if (maxPos != null) {
/* 105 */         BlockUtil.rightClickBlockLegit(maxPos, ((Float)this.range.getValue()).floatValue(), ((Boolean)this.rotate.getValue()).booleanValue(), ((Boolean)this.offhand.getValue()).booleanValue() ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, this.yaw, this.pitch, this.shouldRotate);
/* 106 */         this.breakTimer.reset();
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onToggle() {
/* 113 */     this.yaw = new AtomicDouble(-1.0D);
/* 114 */     this.pitch = new AtomicDouble(-1.0D);
/* 115 */     this.shouldRotate = new AtomicBoolean(false);
/*     */   }
/*     */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\me\earth\phobos\features\modules\combat\BedBomb.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */