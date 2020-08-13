/*     */ package me.earth.phobos.features.modules.movement;
/*     */ 
/*     */ import me.earth.phobos.event.events.PacketEvent;
/*     */ import me.earth.phobos.event.events.PushEvent;
/*     */ import me.earth.phobos.features.modules.Module;
/*     */ import me.earth.phobos.features.setting.Setting;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.projectile.EntityFishHook;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.network.play.server.SPacketEntityStatus;
/*     */ import net.minecraft.network.play.server.SPacketEntityVelocity;
/*     */ import net.minecraft.network.play.server.SPacketExplosion;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*     */ 
/*     */ public class Velocity extends Module {
/*  17 */   public Setting<Boolean> noPush = register(new Setting("NoPush", Boolean.valueOf(true)));
/*  18 */   public Setting<Float> horizontal = register(new Setting("Horizontal", Float.valueOf(0.0F), Float.valueOf(0.0F), Float.valueOf(100.0F)));
/*  19 */   public Setting<Float> vertical = register(new Setting("Vertical", Float.valueOf(0.0F), Float.valueOf(0.0F), Float.valueOf(100.0F)));
/*  20 */   public Setting<Boolean> explosions = register(new Setting("Explosions", Boolean.valueOf(true)));
/*  21 */   public Setting<Boolean> bobbers = register(new Setting("Bobbers", Boolean.valueOf(true)));
/*  22 */   public Setting<Boolean> water = register(new Setting("Water", Boolean.valueOf(false)));
/*  23 */   public Setting<Boolean> blocks = register(new Setting("Blocks", Boolean.valueOf(false)));
/*  24 */   public Setting<Boolean> ice = register(new Setting("Ice", Boolean.valueOf(false)));
/*     */   
/*  26 */   private static Velocity INSTANCE = new Velocity();
/*     */   
/*     */   public Velocity() {
/*  29 */     super("Velocity", "Allows you to control your velocity", Module.Category.MOVEMENT, true, false, false);
/*  30 */     setInstance();
/*     */   }
/*     */   
/*     */   private void setInstance() {
/*  34 */     INSTANCE = this;
/*     */   }
/*     */   
/*     */   public static Velocity getINSTANCE() {
/*  38 */     if (INSTANCE == null) {
/*  39 */       INSTANCE = new Velocity();
/*     */     }
/*  41 */     return INSTANCE;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onUpdate() {
/*  46 */     if (IceSpeed.getINSTANCE().isOff() && ((Boolean)this.ice.getValue()).booleanValue()) {
/*  47 */       Blocks.field_150432_aD.field_149765_K = 0.6F;
/*  48 */       Blocks.field_150403_cj.field_149765_K = 0.6F;
/*  49 */       Blocks.field_185778_de.field_149765_K = 0.6F;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onDisable() {
/*  55 */     if (IceSpeed.getINSTANCE().isOff()) {
/*  56 */       Blocks.field_150432_aD.field_149765_K = 0.98F;
/*  57 */       Blocks.field_150403_cj.field_149765_K = 0.98F;
/*  58 */       Blocks.field_185778_de.field_149765_K = 0.98F;
/*     */     } 
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onPacketReceived(PacketEvent.Receive event) {
/*  64 */     if (event.getStage() == 0 && mc.field_71439_g != null) {
/*  65 */       if (event.getPacket() instanceof SPacketEntityVelocity) {
/*  66 */         SPacketEntityVelocity velocity = (SPacketEntityVelocity)event.getPacket();
/*  67 */         if (velocity.func_149412_c() == mc.field_71439_g.field_145783_c) {
/*  68 */           if (((Float)this.horizontal.getValue()).floatValue() == 0.0F && ((Float)this.vertical.getValue()).floatValue() == 0.0F) {
/*  69 */             event.setCanceled(true);
/*     */             
/*     */             return;
/*     */           } 
/*  73 */           velocity.field_149415_b = (int)(velocity.field_149415_b * ((Float)this.horizontal.getValue()).floatValue());
/*  74 */           velocity.field_149416_c = (int)(velocity.field_149416_c * ((Float)this.vertical.getValue()).floatValue());
/*  75 */           velocity.field_149414_d = (int)(velocity.field_149414_d * ((Float)this.horizontal.getValue()).floatValue());
/*     */         } 
/*     */       } 
/*     */       
/*  79 */       if (event.getPacket() instanceof SPacketEntityStatus && ((Boolean)this.bobbers.getValue()).booleanValue()) {
/*  80 */         SPacketEntityStatus packet = (SPacketEntityStatus)event.getPacket();
/*  81 */         if (packet.func_149160_c() == 31) {
/*  82 */           Entity entity = packet.func_149161_a((World)mc.field_71441_e);
/*  83 */           if (entity instanceof EntityFishHook) {
/*  84 */             EntityFishHook fishHook = (EntityFishHook)entity;
/*  85 */             if (fishHook.field_146043_c == mc.field_71439_g) {
/*  86 */               event.setCanceled(true);
/*     */             }
/*     */           } 
/*     */         } 
/*     */       } 
/*     */       
/*  92 */       if (((Boolean)this.explosions.getValue()).booleanValue() && event.getPacket() instanceof SPacketExplosion) {
/*  93 */         if (((Float)this.horizontal.getValue()).floatValue() == 0.0F && ((Float)this.vertical.getValue()).floatValue() == 0.0F) {
/*  94 */           event.setCanceled(true);
/*     */           
/*     */           return;
/*     */         } 
/*  98 */         SPacketExplosion velocity = (SPacketExplosion)event.getPacket();
/*  99 */         velocity.field_149152_f *= ((Float)this.horizontal.getValue()).floatValue();
/* 100 */         velocity.field_149153_g *= ((Float)this.vertical.getValue()).floatValue();
/* 101 */         velocity.field_149159_h *= ((Float)this.horizontal.getValue()).floatValue();
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onPush(PushEvent event) {
/* 108 */     if (event.getStage() == 0 && ((Boolean)this.noPush.getValue()).booleanValue() && event.entity.equals(mc.field_71439_g)) {
/* 109 */       if (((Float)this.horizontal.getValue()).floatValue() == 0.0F && ((Float)this.vertical.getValue()).floatValue() == 0.0F) {
/* 110 */         event.setCanceled(true);
/*     */         
/*     */         return;
/*     */       } 
/* 114 */       event.x = -event.x * ((Float)this.horizontal.getValue()).floatValue();
/* 115 */       event.y = -event.y * ((Float)this.vertical.getValue()).floatValue();
/* 116 */       event.z = -event.z * ((Float)this.horizontal.getValue()).floatValue();
/* 117 */     } else if (event.getStage() == 1 && ((Boolean)this.blocks.getValue()).booleanValue()) {
/* 118 */       event.setCanceled(true);
/* 119 */     } else if (event.getStage() == 2 && ((Boolean)this.water.getValue()).booleanValue() && mc.field_71439_g != null && mc.field_71439_g.equals(event.entity)) {
/* 120 */       event.setCanceled(true);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\me\earth\phobos\features\modules\movement\Velocity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */