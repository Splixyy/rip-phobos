/*     */ package me.earth.phobos.features.modules.movement;
/*     */ 
/*     */ import java.math.BigDecimal;
/*     */ import java.math.RoundingMode;
/*     */ import java.util.Objects;
/*     */ import me.earth.phobos.Phobos;
/*     */ import me.earth.phobos.event.events.MoveEvent;
/*     */ import me.earth.phobos.event.events.UpdateWalkingPlayerEvent;
/*     */ import me.earth.phobos.features.modules.Module;
/*     */ import me.earth.phobos.features.modules.player.Freecam;
/*     */ import me.earth.phobos.features.setting.Setting;
/*     */ import me.earth.phobos.util.EntityUtil;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.init.MobEffects;
/*     */ import net.minecraft.potion.PotionEffect;
/*     */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*     */ 
/*     */ public class Strafe extends Module {
/*  19 */   private final Setting<Mode> mode = register(new Setting("Mode", Mode.NCP));
/*  20 */   private final Setting<Boolean> limiter = register(new Setting("SetGround", Boolean.valueOf(true)));
/*  21 */   private final Setting<Boolean> limiter2 = register(new Setting("Bhop", Boolean.valueOf(false)));
/*  22 */   private final Setting<Integer> specialMoveSpeed = register(new Setting("Speed", Integer.valueOf(100), Integer.valueOf(0), Integer.valueOf(150)));
/*  23 */   private final Setting<Integer> potionSpeed = register(new Setting("Speed1", Integer.valueOf(130), Integer.valueOf(0), Integer.valueOf(150)));
/*  24 */   private final Setting<Integer> potionSpeed2 = register(new Setting("Speed2", Integer.valueOf(125), Integer.valueOf(0), Integer.valueOf(150)));
/*  25 */   private final Setting<Integer> acceleration = register(new Setting("Accel", Integer.valueOf(2149), Integer.valueOf(1000), Integer.valueOf(2500)));
/*  26 */   private final Setting<Boolean> potion = register(new Setting("Potion", Boolean.valueOf(false)));
/*  27 */   private final Setting<Boolean> step = register(new Setting("SetStep", Boolean.valueOf(true), v -> (this.mode.getValue() == Mode.BHOP)));
/*     */   
/*  29 */   private int stage = 1;
/*     */   private double moveSpeed;
/*     */   private double lastDist;
/*  32 */   private int cooldownHops = 0;
/*     */   
/*     */   public Strafe() {
/*  35 */     super("Strafe", "AirControl etc.", Module.Category.MOVEMENT, true, false, false);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onEnable() {
/*  40 */     this.moveSpeed = getBaseMoveSpeed();
/*     */   }
/*     */ 
/*     */   
/*     */   public void onDisable() {
/*  45 */     this.moveSpeed = 0.0D;
/*  46 */     this.stage = 2;
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onUpdateWalkingPlayer(UpdateWalkingPlayerEvent event) {
/*  51 */     if (event.getStage() == 0) {
/*  52 */       this.lastDist = Math.sqrt((mc.field_71439_g.field_70165_t - mc.field_71439_g.field_70169_q) * (mc.field_71439_g.field_70165_t - mc.field_71439_g.field_70169_q) + (mc.field_71439_g.field_70161_v - mc.field_71439_g.field_70166_s) * (mc.field_71439_g.field_70161_v - mc.field_71439_g.field_70166_s));
/*     */     }
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onMove(MoveEvent event) {
/*  58 */     if (event.getStage() != 0 || shouldReturn()) {
/*     */       return;
/*     */     }
/*     */     
/*  62 */     if (this.mode.getValue() == Mode.NCP) {
/*  63 */       doNCP(event);
/*  64 */     } else if (this.mode.getValue() == Mode.BHOP) {
/*  65 */       float moveForward = mc.field_71439_g.field_71158_b.field_192832_b;
/*  66 */       float moveStrafe = mc.field_71439_g.field_71158_b.field_78902_a;
/*  67 */       float rotationYaw = mc.field_71439_g.field_70177_z;
/*     */       
/*  69 */       if (((Boolean)this.limiter2.getValue()).booleanValue() && mc.field_71439_g.field_70122_E) {
/*  70 */         this.stage = 2;
/*     */       }
/*     */       
/*  73 */       if (((Boolean)this.limiter.getValue()).booleanValue() && round(mc.field_71439_g.field_70163_u - (int)mc.field_71439_g.field_70163_u, 3) == round(0.138D, 3)) {
/*  74 */         mc.field_71439_g.field_70181_x -= 0.13D;
/*  75 */         event.setY(event.getY() - 0.13D);
/*  76 */         mc.field_71439_g.field_70163_u -= 0.13D;
/*     */       } 
/*     */       
/*  79 */       if (this.stage == 1 && EntityUtil.isMoving()) {
/*  80 */         this.stage = 2;
/*  81 */         this.moveSpeed = getMultiplier() * getBaseMoveSpeed() - 0.01D;
/*  82 */       } else if (this.stage == 2) {
/*  83 */         this.stage = 3;
/*  84 */         if (EntityUtil.isMoving()) {
/*  85 */           mc.field_71439_g.field_70181_x = 0.4D;
/*  86 */           event.setY(0.4D);
/*  87 */           if (this.cooldownHops > 0) {
/*  88 */             this.cooldownHops--;
/*     */           }
/*  90 */           this.moveSpeed *= ((Integer)this.acceleration.getValue()).intValue() / 1000.0D;
/*     */         } 
/*  92 */       } else if (this.stage == 3) {
/*  93 */         this.stage = 4;
/*  94 */         double difference = 0.66D * (this.lastDist - getBaseMoveSpeed());
/*  95 */         this.moveSpeed = this.lastDist - difference;
/*     */       } else {
/*  97 */         if (mc.field_71441_e.func_184144_a((Entity)mc.field_71439_g, mc.field_71439_g.func_174813_aQ().func_72317_d(0.0D, mc.field_71439_g.field_70181_x, 0.0D)).size() > 0 || mc.field_71439_g.field_70124_G) {
/*  98 */           this.stage = 1;
/*     */         }
/* 100 */         this.moveSpeed = this.lastDist - this.lastDist / 159.0D;
/*     */       } 
/*     */       
/* 103 */       this.moveSpeed = Math.max(this.moveSpeed, getBaseMoveSpeed());
/* 104 */       if (moveForward == 0.0F && moveStrafe == 0.0F) {
/* 105 */         event.setX(0.0D);
/* 106 */         event.setZ(0.0D);
/* 107 */         this.moveSpeed = 0.0D;
/* 108 */       } else if (moveForward != 0.0F) {
/* 109 */         if (moveStrafe >= 1.0F) {
/* 110 */           rotationYaw += (moveForward > 0.0F) ? -45.0F : 45.0F;
/* 111 */           moveStrafe = 0.0F;
/* 112 */         } else if (moveStrafe <= -1.0F) {
/* 113 */           rotationYaw += (moveForward > 0.0F) ? 45.0F : -45.0F;
/* 114 */           moveStrafe = 0.0F;
/*     */         } 
/* 116 */         if (moveForward > 0.0F) {
/* 117 */           moveForward = 1.0F;
/* 118 */         } else if (moveForward < 0.0F) {
/* 119 */           moveForward = -1.0F;
/*     */         } 
/*     */       } 
/*     */       
/* 123 */       double motionX = Math.cos(Math.toRadians((rotationYaw + 90.0F)));
/* 124 */       double motionZ = Math.sin(Math.toRadians((rotationYaw + 90.0F)));
/*     */       
/* 126 */       if (this.cooldownHops == 0) {
/* 127 */         event.setX(moveForward * this.moveSpeed * motionX + moveStrafe * this.moveSpeed * motionZ);
/* 128 */         event.setZ(moveForward * this.moveSpeed * motionZ - moveStrafe * this.moveSpeed * motionX);
/*     */       } 
/*     */       
/* 131 */       if (((Boolean)this.step.getValue()).booleanValue()) {
/* 132 */         mc.field_71439_g.field_70138_W = 0.6F;
/*     */       }
/*     */       
/* 135 */       if (moveForward == 0.0F && moveStrafe == 0.0F) {
/* 136 */         event.setX(0.0D);
/* 137 */         event.setZ(0.0D);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   private void doNCP(MoveEvent event) {
/*     */     double motionY;
/* 143 */     if (!((Boolean)this.limiter.getValue()).booleanValue() && mc.field_71439_g.field_70122_E) {
/* 144 */       this.stage = 2;
/*     */     }
/*     */     
/* 147 */     switch (this.stage) {
/*     */       case 0:
/* 149 */         this.stage++;
/* 150 */         this.lastDist = 0.0D;
/*     */         break;
/*     */       
/*     */       case 2:
/* 154 */         motionY = 0.40123128D;
/* 155 */         if ((mc.field_71439_g.field_191988_bg != 0.0F || mc.field_71439_g.field_70702_br != 0.0F) && mc.field_71439_g.field_70122_E) {
/* 156 */           if (mc.field_71439_g.func_70644_a(MobEffects.field_76430_j)) {
/* 157 */             motionY += ((mc.field_71439_g.func_70660_b(MobEffects.field_76430_j).func_76458_c() + 1) * 0.1F);
/*     */           }
/* 159 */           event.setY(mc.field_71439_g.field_70181_x = motionY);
/* 160 */           this.moveSpeed *= 2.149D;
/*     */         } 
/*     */         break;
/*     */ 
/*     */       
/*     */       case 3:
/* 166 */         this; this.moveSpeed = this.lastDist - 0.76D * (this.lastDist - getBaseMoveSpeed());
/*     */         break;
/*     */       
/*     */       default:
/* 170 */         if (((((Boolean)this.limiter2.getValue()).booleanValue() && mc.field_71441_e.func_184144_a((Entity)mc.field_71439_g, mc.field_71439_g.func_174813_aQ().func_72317_d(0.0D, mc.field_71439_g.field_70181_x, 0.0D)).size() > 0) || mc.field_71439_g.field_70124_G) && this.stage > 0) {
/* 171 */           this.stage = (mc.field_71439_g.field_191988_bg != 0.0F || mc.field_71439_g.field_70702_br != 0.0F) ? 1 : 0;
/*     */         }
/* 173 */         this.moveSpeed = this.lastDist - this.lastDist / 159.0D;
/*     */         break;
/*     */     } 
/*     */     
/* 177 */     this; this.moveSpeed = Math.max(this.moveSpeed, getBaseMoveSpeed());
/* 178 */     double forward = mc.field_71439_g.field_71158_b.field_192832_b;
/* 179 */     double strafe = mc.field_71439_g.field_71158_b.field_78902_a;
/* 180 */     double yaw = mc.field_71439_g.field_70177_z;
/* 181 */     if (forward == 0.0D && strafe == 0.0D) {
/* 182 */       event.setX(0.0D);
/* 183 */       event.setZ(0.0D);
/* 184 */     } else if (forward != 0.0D && strafe != 0.0D) {
/* 185 */       forward *= Math.sin(0.7853981633974483D);
/* 186 */       strafe *= Math.cos(0.7853981633974483D);
/*     */     } 
/* 188 */     event.setX((forward * this.moveSpeed * -Math.sin(Math.toRadians(yaw)) + strafe * this.moveSpeed * Math.cos(Math.toRadians(yaw))) * 0.99D);
/* 189 */     event.setZ((forward * this.moveSpeed * Math.cos(Math.toRadians(yaw)) - strafe * this.moveSpeed * -Math.sin(Math.toRadians(yaw))) * 0.99D);
/* 190 */     this.stage++;
/*     */   }
/*     */   
/*     */   public static double getBaseMoveSpeed() {
/* 194 */     double baseSpeed = 0.272D;
/* 195 */     if (mc.field_71439_g.func_70644_a(MobEffects.field_76424_c)) {
/* 196 */       int amplifier = ((PotionEffect)Objects.<PotionEffect>requireNonNull(mc.field_71439_g.func_70660_b(MobEffects.field_76424_c))).func_76458_c();
/* 197 */       baseSpeed *= 1.0D + 0.2D * amplifier;
/*     */     } 
/* 199 */     return baseSpeed;
/*     */   }
/*     */   
/*     */   private float getMultiplier() {
/* 203 */     float baseSpeed = ((Integer)this.specialMoveSpeed.getValue()).intValue();
/* 204 */     if (((Boolean)this.potion.getValue()).booleanValue() && mc.field_71439_g.func_70644_a(MobEffects.field_76424_c)) {
/* 205 */       int amplifier = ((PotionEffect)Objects.<PotionEffect>requireNonNull(mc.field_71439_g.func_70660_b(MobEffects.field_76424_c))).func_76458_c() + 1;
/* 206 */       if (amplifier >= 2) {
/* 207 */         baseSpeed = ((Integer)this.potionSpeed2.getValue()).intValue();
/*     */       } else {
/* 209 */         baseSpeed = ((Integer)this.potionSpeed.getValue()).intValue();
/*     */       } 
/*     */     } 
/* 212 */     return baseSpeed / 100.0F;
/*     */   }
/*     */   
/*     */   private boolean shouldReturn() {
/* 216 */     return (Phobos.moduleManager.isModuleEnabled(Freecam.class) || Phobos.moduleManager.isModuleEnabled(Phase.class) || Phobos.moduleManager.isModuleEnabled(ElytraFlight.class) || Phobos.moduleManager.isModuleEnabled(Flight.class));
/*     */   }
/*     */ 
/*     */   
/*     */   public String getDisplayInfo() {
/* 221 */     if (this.mode.getValue() != Mode.NONE) {
/* 222 */       if (this.mode.getValue() == Mode.NCP) {
/* 223 */         return this.mode.currentEnumName().toUpperCase();
/*     */       }
/* 225 */       return this.mode.currentEnumName();
/*     */     } 
/*     */     
/* 228 */     return null;
/*     */   }
/*     */   
/*     */   public static double round(double value, int places) {
/* 232 */     if (places < 0) {
/* 233 */       throw new IllegalArgumentException();
/*     */     }
/* 235 */     BigDecimal bigDecimal = (new BigDecimal(value)).setScale(places, RoundingMode.HALF_UP);
/* 236 */     return bigDecimal.doubleValue();
/*     */   }
/*     */   
/*     */   public enum Mode {
/* 240 */     NONE,
/* 241 */     NCP,
/* 242 */     BHOP;
/*     */   }
/*     */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\me\earth\phobos\features\modules\movement\Strafe.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */