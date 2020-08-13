/*     */ package me.earth.phobos.features.modules.combat;
/*     */ 
/*     */ import me.earth.phobos.Phobos;
/*     */ import me.earth.phobos.event.events.UpdateWalkingPlayerEvent;
/*     */ import me.earth.phobos.features.modules.Module;
/*     */ import me.earth.phobos.features.setting.Setting;
/*     */ import me.earth.phobos.util.DamageUtil;
/*     */ import me.earth.phobos.util.EntityUtil;
/*     */ import me.earth.phobos.util.MathUtil;
/*     */ import me.earth.phobos.util.Timer;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.inventory.ClickType;
/*     */ import net.minecraft.util.EnumHand;
/*     */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*     */ 
/*     */ public class Killaura
/*     */   extends Module
/*     */ {
/*  21 */   public Setting<Float> range = register(new Setting("Range", Float.valueOf(6.0F), Float.valueOf(0.1F), Float.valueOf(7.0F)));
/*  22 */   private Setting<TargetMode> targetMode = register(new Setting("Target", TargetMode.CLOSEST));
/*  23 */   public Setting<Float> health = register(new Setting("Health", Float.valueOf(6.0F), Float.valueOf(0.1F), Float.valueOf(36.0F), v -> (this.targetMode.getValue() == TargetMode.SMART)));
/*  24 */   public Setting<Boolean> delay = register(new Setting("Delay", Boolean.valueOf(true)));
/*  25 */   public Setting<Boolean> rotate = register(new Setting("Rotate", Boolean.valueOf(true)));
/*  26 */   public Setting<Boolean> armorBreak = register(new Setting("ArmorBreak", Boolean.valueOf(false)));
/*  27 */   public Setting<Boolean> eating = register(new Setting("Eating", Boolean.valueOf(true)));
/*  28 */   public Setting<Boolean> onlySharp = register(new Setting("Axe/Sword", Boolean.valueOf(true)));
/*  29 */   public Setting<Boolean> teleport = register(new Setting("Teleport", Boolean.valueOf(false)));
/*  30 */   public Setting<Float> raytrace = register(new Setting("Raytrace", Float.valueOf(6.0F), Float.valueOf(0.1F), Float.valueOf(7.0F), v -> !((Boolean)this.teleport.getValue()).booleanValue(), "Wall Range."));
/*  31 */   public Setting<Float> teleportRange = register(new Setting("TpRange", Float.valueOf(15.0F), Float.valueOf(0.1F), Float.valueOf(50.0F), v -> ((Boolean)this.teleport.getValue()).booleanValue(), "Teleport Range."));
/*  32 */   public Setting<Boolean> lagBack = register(new Setting("LagBack", Boolean.valueOf(true), v -> ((Boolean)this.teleport.getValue()).booleanValue()));
/*  33 */   public Setting<Boolean> teekaydelay = register(new Setting("32kDelay", Boolean.valueOf(false)));
/*  34 */   public Setting<Integer> time32k = register(new Setting("32kTime", Integer.valueOf(5), Integer.valueOf(1), Integer.valueOf(50)));
/*  35 */   public Setting<Integer> multi = register(new Setting("32kPackets", Integer.valueOf(2), v -> !((Boolean)this.teekaydelay.getValue()).booleanValue()));
/*  36 */   public Setting<Boolean> multi32k = register(new Setting("Multi32k", Boolean.valueOf(false)));
/*  37 */   public Setting<Boolean> players = register(new Setting("Players", Boolean.valueOf(true)));
/*  38 */   public Setting<Boolean> mobs = register(new Setting("Mobs", Boolean.valueOf(false)));
/*  39 */   public Setting<Boolean> animals = register(new Setting("Animals", Boolean.valueOf(false)));
/*  40 */   public Setting<Boolean> vehicles = register(new Setting("Entities", Boolean.valueOf(false)));
/*  41 */   public Setting<Boolean> projectiles = register(new Setting("Projectiles", Boolean.valueOf(false)));
/*  42 */   public Setting<Boolean> tps = register(new Setting("TpsSync", Boolean.valueOf(true)));
/*  43 */   public Setting<Boolean> packet = register(new Setting("Packet", Boolean.valueOf(false)));
/*  44 */   public Setting<Boolean> info = register(new Setting("Info", Boolean.valueOf(true)));
/*     */   
/*  46 */   private final Timer timer = new Timer();
/*     */   public static Entity target;
/*     */   
/*     */   public Killaura() {
/*  50 */     super("Killaura", "Kills aura.", Module.Category.COMBAT, true, false, false);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onTick() {
/*  55 */     if (!((Boolean)this.rotate.getValue()).booleanValue()) {
/*  56 */       doKillaura();
/*     */     }
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onUpdateWalkingPlayerEvent(UpdateWalkingPlayerEvent event) {
/*  62 */     if (event.getStage() == 0 && ((Boolean)this.rotate.getValue()).booleanValue()) {
/*  63 */       doKillaura();
/*     */     }
/*     */   }
/*     */   
/*     */   private void doKillaura() {
/*  68 */     if (((Boolean)this.onlySharp.getValue()).booleanValue() && !EntityUtil.holdingWeapon((EntityPlayer)mc.field_71439_g)) {
/*  69 */       target = null;
/*     */       
/*     */       return;
/*     */     } 
/*  73 */     int wait = (!((Boolean)this.delay.getValue()).booleanValue() || (EntityUtil.holding32k((EntityPlayer)mc.field_71439_g) && !((Boolean)this.teekaydelay.getValue()).booleanValue())) ? 0 : (int)(DamageUtil.getCooldownByWeapon((EntityPlayer)mc.field_71439_g) * (((Boolean)this.tps.getValue()).booleanValue() ? Phobos.serverManager.getTpsFactor() : 1.0F));
/*  74 */     if (!this.timer.passedMs(wait) || (!((Boolean)this.eating.getValue()).booleanValue() && mc.field_71439_g.func_184587_cr() && (!mc.field_71439_g.func_184592_cb().func_77973_b().equals(Items.field_185159_cQ) || mc.field_71439_g.func_184600_cs() != EnumHand.OFF_HAND))) {
/*     */       return;
/*     */     }
/*     */     
/*  78 */     if (this.targetMode.getValue() != TargetMode.FOCUS || target == null || (mc.field_71439_g.func_70068_e(target) >= MathUtil.square(((Float)this.range.getValue()).floatValue()) && (!((Boolean)this.teleport.getValue()).booleanValue() || mc.field_71439_g.func_70068_e(target) >= MathUtil.square(((Float)this.teleportRange.getValue()).floatValue()))) || (!mc.field_71439_g.func_70685_l(target) && !EntityUtil.canEntityFeetBeSeen(target) && mc.field_71439_g.func_70068_e(target) >= MathUtil.square(((Float)this.raytrace.getValue()).floatValue()) && !((Boolean)this.teleport.getValue()).booleanValue())) {
/*  79 */       target = getTarget();
/*     */     }
/*     */     
/*  82 */     if (target == null) {
/*     */       return;
/*     */     }
/*     */     
/*  86 */     if (((Boolean)this.rotate.getValue()).booleanValue()) {
/*  87 */       Phobos.rotationManager.lookAtEntity(target);
/*     */     }
/*     */     
/*  90 */     if (((Boolean)this.teleport.getValue()).booleanValue()) {
/*  91 */       Phobos.positionManager.setPositionPacket(target.field_70165_t, EntityUtil.canEntityFeetBeSeen(target) ? target.field_70163_u : (target.field_70163_u + target.func_70047_e()), target.field_70161_v, true, true, !((Boolean)this.lagBack.getValue()).booleanValue());
/*     */     }
/*     */     
/*  94 */     if (EntityUtil.holding32k((EntityPlayer)mc.field_71439_g) && !((Boolean)this.teekaydelay.getValue()).booleanValue()) {
/*  95 */       if (((Boolean)this.multi32k.getValue()).booleanValue()) {
/*  96 */         for (EntityPlayer player : mc.field_71441_e.field_73010_i) {
/*  97 */           if (EntityUtil.isValid((Entity)player, ((Float)this.range.getValue()).floatValue())) {
/*  98 */             teekayAttack((Entity)player);
/*     */           }
/*     */         } 
/*     */       } else {
/* 102 */         teekayAttack(target);
/*     */       } 
/* 104 */       this.timer.reset();
/*     */       
/*     */       return;
/*     */     } 
/* 108 */     if (((Boolean)this.armorBreak.getValue()).booleanValue()) {
/* 109 */       mc.field_71442_b.func_187098_a(mc.field_71439_g.field_71069_bz.field_75152_c, 9, mc.field_71439_g.field_71071_by.field_70461_c, ClickType.SWAP, (EntityPlayer)mc.field_71439_g);
/* 110 */       EntityUtil.attackEntity(target, ((Boolean)this.packet.getValue()).booleanValue(), true);
/* 111 */       mc.field_71442_b.func_187098_a(mc.field_71439_g.field_71069_bz.field_75152_c, 9, mc.field_71439_g.field_71071_by.field_70461_c, ClickType.SWAP, (EntityPlayer)mc.field_71439_g);
/* 112 */       EntityUtil.attackEntity(target, ((Boolean)this.packet.getValue()).booleanValue(), true);
/*     */     } else {
/* 114 */       EntityUtil.attackEntity(target, ((Boolean)this.packet.getValue()).booleanValue(), true);
/*     */     } 
/* 116 */     this.timer.reset();
/*     */   }
/*     */   
/*     */   private void teekayAttack(Entity entity) {
/* 120 */     for (int i = 0; i < ((Integer)this.multi.getValue()).intValue(); i++)
/*     */     {
/* 122 */       startEntityAttackThread(entity, i * ((Integer)this.time32k.getValue()).intValue());
/*     */     }
/*     */   }
/*     */   
/*     */   private void startEntityAttackThread(Entity entity, int time) {
/* 127 */     (new Thread(() -> {
/*     */           Timer timer = new Timer();
/*     */           timer.reset();
/*     */           try {
/*     */             Thread.sleep(time);
/* 132 */           } catch (InterruptedException ex) {
/*     */             Thread.currentThread().interrupt();
/*     */           } 
/*     */           EntityUtil.attackEntity(entity, true, true);
/* 136 */         })).start();
/*     */   }
/*     */   
/*     */   private Entity getTarget() {
/* 140 */     Entity target = null;
/* 141 */     double distance = ((Boolean)this.teleport.getValue()).booleanValue() ? ((Float)this.teleportRange.getValue()).floatValue() : ((Float)this.range.getValue()).floatValue();
/* 142 */     double maxHealth = 36.0D;
/* 143 */     for (Entity entity : mc.field_71441_e.field_72996_f) {
/* 144 */       if (((!((Boolean)this.players.getValue()).booleanValue() || !(entity instanceof EntityPlayer)) && (!((Boolean)this.animals.getValue()).booleanValue() || !EntityUtil.isPassive(entity)) && (!((Boolean)this.mobs.getValue()).booleanValue() || !EntityUtil.isMobAggressive(entity)) && (!((Boolean)this.vehicles.getValue()).booleanValue() || !EntityUtil.isVehicle(entity)) && (!((Boolean)this.projectiles.getValue()).booleanValue() || !EntityUtil.isProjectile(entity))) || (
/* 145 */         entity instanceof net.minecraft.entity.EntityLivingBase && EntityUtil.isntValid(entity, distance))) {
/*     */         continue;
/*     */       }
/*     */       
/* 149 */       if (!((Boolean)this.teleport.getValue()).booleanValue() && !mc.field_71439_g.func_70685_l(entity) && !EntityUtil.canEntityFeetBeSeen(entity) && mc.field_71439_g.func_70068_e(entity) > MathUtil.square(((Float)this.raytrace.getValue()).floatValue())) {
/*     */         continue;
/*     */       }
/*     */       
/* 153 */       if (target == null) {
/* 154 */         target = entity;
/* 155 */         distance = mc.field_71439_g.func_70068_e(entity);
/* 156 */         maxHealth = EntityUtil.getHealth(entity);
/*     */         
/*     */         continue;
/*     */       } 
/* 160 */       if (entity instanceof EntityPlayer && DamageUtil.isArmorLow((EntityPlayer)entity, 18)) {
/* 161 */         target = entity;
/*     */         
/*     */         break;
/*     */       } 
/* 165 */       if (this.targetMode.getValue() == TargetMode.SMART && EntityUtil.getHealth(entity) < ((Float)this.health.getValue()).floatValue()) {
/* 166 */         target = entity;
/*     */         
/*     */         break;
/*     */       } 
/* 170 */       if (this.targetMode.getValue() != TargetMode.HEALTH && mc.field_71439_g.func_70068_e(entity) < distance) {
/* 171 */         target = entity;
/* 172 */         distance = mc.field_71439_g.func_70068_e(entity);
/* 173 */         maxHealth = EntityUtil.getHealth(entity);
/*     */       } 
/*     */       
/* 176 */       if (this.targetMode.getValue() == TargetMode.HEALTH && EntityUtil.getHealth(entity) < maxHealth) {
/* 177 */         target = entity;
/* 178 */         distance = mc.field_71439_g.func_70068_e(entity);
/* 179 */         maxHealth = EntityUtil.getHealth(entity);
/*     */       } 
/*     */     } 
/*     */     
/* 183 */     return target;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getDisplayInfo() {
/* 188 */     if (((Boolean)this.info.getValue()).booleanValue() && target != null && target instanceof EntityPlayer) {
/* 189 */       return target.func_70005_c_();
/*     */     }
/* 191 */     return null;
/*     */   }
/*     */   
/*     */   public enum TargetMode {
/* 195 */     FOCUS,
/* 196 */     CLOSEST,
/* 197 */     HEALTH,
/* 198 */     SMART;
/*     */   }
/*     */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\me\earth\phobos\features\modules\combat\Killaura.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */