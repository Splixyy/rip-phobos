/*     */ package me.earth.phobos.features.modules.movement;
/*     */ import me.earth.phobos.event.events.MoveEvent;
/*     */ import me.earth.phobos.event.events.PacketEvent;
/*     */ import me.earth.phobos.event.events.UpdateWalkingPlayerEvent;
/*     */ import me.earth.phobos.features.modules.Module;
/*     */ import me.earth.phobos.features.setting.Setting;
/*     */ import me.earth.phobos.util.MathUtil;
/*     */ import me.earth.phobos.util.Timer;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.inventory.EntityEquipmentSlot;
/*     */ import net.minecraft.item.ItemElytra;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.network.Packet;
/*     */ import net.minecraft.network.play.client.CPacketEntityAction;
/*     */ import net.minecraft.network.play.client.CPacketPlayer;
/*     */ import net.minecraft.util.math.MathHelper;
/*     */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*     */ 
/*     */ public class ElytraFlight extends Module {
/*  22 */   public Setting<Mode> mode = register(new Setting("Mode", Mode.FLY));
/*  23 */   public Setting<Integer> devMode = register(new Setting("Type", Integer.valueOf(2), Integer.valueOf(1), Integer.valueOf(3), v -> (this.mode.getValue() == Mode.BYPASS || this.mode.getValue() == Mode.BETTER), "EventMode"));
/*  24 */   public Setting<Float> speed = register(new Setting("Speed", Float.valueOf(1.0F), Float.valueOf(0.0F), Float.valueOf(10.0F), v -> (this.mode.getValue() != Mode.FLY && this.mode.getValue() != Mode.BOOST && this.mode.getValue() != Mode.BETTER && this.mode.getValue() != Mode.OHARE), "The Speed."));
/*  25 */   public Setting<Float> vSpeed = register(new Setting("VSpeed", Float.valueOf(0.3F), Float.valueOf(0.0F), Float.valueOf(10.0F), v -> (this.mode.getValue() == Mode.BETTER || this.mode.getValue() == Mode.OHARE), "Vertical Speed"));
/*  26 */   public Setting<Float> hSpeed = register(new Setting("HSpeed", Float.valueOf(1.0F), Float.valueOf(0.0F), Float.valueOf(10.0F), v -> (this.mode.getValue() == Mode.BETTER || this.mode.getValue() == Mode.OHARE), "Horizontal Speed"));
/*  27 */   public Setting<Float> glide = register(new Setting("Glide", Float.valueOf(1.0E-4F), Float.valueOf(0.0F), Float.valueOf(0.2F), v -> (this.mode.getValue() == Mode.BETTER), "Glide Speed"));
/*  28 */   public Setting<Float> tooBeeSpeed = register(new Setting("TooBeeSpeed", Float.valueOf(1.8000001F), Float.valueOf(1.0F), Float.valueOf(2.0F), v -> (this.mode.getValue() == Mode.TOOBEE), "Speed for flight on 2b2t"));
/*  29 */   public Setting<Boolean> autoStart = register(new Setting("AutoStart", Boolean.valueOf(true)));
/*  30 */   public Setting<Boolean> disableInLiquid = register(new Setting("NoLiquid", Boolean.valueOf(true)));
/*  31 */   public Setting<Boolean> infiniteDura = register(new Setting("InfiniteDura", Boolean.valueOf(false)));
/*  32 */   public Setting<Boolean> noKick = register(new Setting("NoKick", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.PACKET)));
/*  33 */   public Setting<Boolean> allowUp = register(new Setting("AllowUp", Boolean.valueOf(true), v -> (this.mode.getValue() == Mode.BETTER)));
/*     */   
/*  35 */   private static ElytraFlight INSTANCE = new ElytraFlight();
/*  36 */   private final Timer timer = new Timer();
/*     */   private Double posX;
/*     */   private Double flyHeight;
/*     */   private Double posZ;
/*     */   
/*     */   public ElytraFlight() {
/*  42 */     super("ElytraFlight", "Makes Elytra Flight better.", Module.Category.MOVEMENT, true, false, false);
/*  43 */     setInstance();
/*     */   }
/*     */   
/*     */   private void setInstance() {
/*  47 */     INSTANCE = this;
/*     */   }
/*     */   
/*     */   public static ElytraFlight getInstance() {
/*  51 */     if (INSTANCE == null) {
/*  52 */       INSTANCE = new ElytraFlight();
/*     */     }
/*  54 */     return INSTANCE;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onEnable() {
/*  59 */     if (this.mode.getValue() == Mode.BETTER && !((Boolean)this.autoStart.getValue()).booleanValue() && ((Integer)this.devMode.getValue()).intValue() == 1) {
/*  60 */       mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketEntityAction((Entity)mc.field_71439_g, CPacketEntityAction.Action.START_FALL_FLYING));
/*     */     }
/*  62 */     this.flyHeight = null;
/*  63 */     this.posX = null;
/*  64 */     this.posZ = null;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getDisplayInfo() {
/*  69 */     return this.mode.currentEnumName();
/*     */   }
/*     */ 
/*     */   
/*     */   public void onUpdate() {
/*  74 */     if (this.mode.getValue() == Mode.BYPASS && ((Integer)this.devMode.getValue()).intValue() == 1 && 
/*  75 */       mc.field_71439_g.func_184613_cA()) {
/*  76 */       mc.field_71439_g.field_70159_w = 0.0D;
/*  77 */       mc.field_71439_g.field_70181_x = -1.0E-4D;
/*  78 */       mc.field_71439_g.field_70179_y = 0.0D;
/*  79 */       double forwardInput = mc.field_71439_g.field_71158_b.field_192832_b;
/*  80 */       double strafeInput = mc.field_71439_g.field_71158_b.field_78902_a;
/*  81 */       double[] result = forwardStrafeYaw(forwardInput, strafeInput, mc.field_71439_g.field_70177_z);
/*  82 */       double forward = result[0];
/*  83 */       double strafe = result[1];
/*  84 */       double yaw = result[2];
/*  85 */       if (forwardInput != 0.0D || strafeInput != 0.0D) {
/*  86 */         mc.field_71439_g.field_70159_w = forward * ((Float)this.speed.getValue()).floatValue() * Math.cos(Math.toRadians(yaw + 90.0D)) + strafe * ((Float)this.speed.getValue()).floatValue() * Math.sin(Math.toRadians(yaw + 90.0D));
/*  87 */         mc.field_71439_g.field_70179_y = forward * ((Float)this.speed.getValue()).floatValue() * Math.sin(Math.toRadians(yaw + 90.0D)) - strafe * ((Float)this.speed.getValue()).floatValue() * Math.cos(Math.toRadians(yaw + 90.0D));
/*     */       } 
/*     */       
/*  90 */       if (mc.field_71474_y.field_74311_E.func_151470_d()) {
/*  91 */         mc.field_71439_g.field_70181_x = -1.0D;
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onSendPacket(PacketEvent.Send event) {
/*  99 */     if (event.getPacket() instanceof CPacketPlayer && this.mode.getValue() == Mode.TOOBEE) {
/* 100 */       CPacketPlayer packet = (CPacketPlayer)event.getPacket();
/* 101 */       if (mc.field_71439_g.func_184613_cA() && 
/* 102 */         !mc.field_71439_g.field_71158_b.field_78901_c && 
/* 103 */         packet.field_149473_f < 1.0F) packet.field_149473_f = 1.0F;
/*     */     
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onMove(MoveEvent event) {
/* 111 */     if (this.mode.getValue() == Mode.OHARE) {
/* 112 */       ItemStack itemstack = mc.field_71439_g.func_184582_a(EntityEquipmentSlot.CHEST);
/* 113 */       if (itemstack.func_77973_b() == Items.field_185160_cR && ItemElytra.func_185069_d(itemstack) && 
/* 114 */         mc.field_71439_g.func_184613_cA()) {
/* 115 */         event.setY(mc.field_71474_y.field_74314_A.func_151470_d() ? ((Float)this.vSpeed.getValue()).floatValue() : (mc.field_71474_y.field_74311_E.func_151470_d() ? -((Float)this.vSpeed.getValue()).floatValue() : 0.0D));
/* 116 */         mc.field_71439_g.func_70024_g(0.0D, mc.field_71474_y.field_74314_A.func_151470_d() ? ((Float)this.vSpeed.getValue()).floatValue() : (mc.field_71474_y.field_74311_E.func_151470_d() ? -((Float)this.vSpeed.getValue()).floatValue() : 0.0D), 0.0D);
/* 117 */         mc.field_71439_g.field_184835_a = 0.0F;
/* 118 */         mc.field_71439_g.field_184836_b = 0.0F;
/* 119 */         mc.field_71439_g.field_184837_c = 0.0F;
/* 120 */         mc.field_71439_g.field_70701_bs = mc.field_71474_y.field_74314_A.func_151470_d() ? ((Float)this.vSpeed.getValue()).floatValue() : (mc.field_71474_y.field_74311_E.func_151470_d() ? -((Float)this.vSpeed.getValue()).floatValue() : 0.0F);
/* 121 */         double forward = mc.field_71439_g.field_71158_b.field_192832_b;
/* 122 */         double strafe = mc.field_71439_g.field_71158_b.field_78902_a;
/* 123 */         float yaw = mc.field_71439_g.field_70177_z;
/* 124 */         if (forward == 0.0D && strafe == 0.0D) {
/* 125 */           event.setX(0.0D);
/* 126 */           event.setZ(0.0D);
/*     */         } else {
/* 128 */           if (forward != 0.0D) {
/* 129 */             if (strafe > 0.0D) {
/* 130 */               yaw += ((forward > 0.0D) ? -45 : 45);
/* 131 */             } else if (strafe < 0.0D) {
/* 132 */               yaw += ((forward > 0.0D) ? 45 : -45);
/*     */             } 
/* 134 */             strafe = 0.0D;
/* 135 */             if (forward > 0.0D) {
/* 136 */               forward = 1.0D;
/* 137 */             } else if (forward < 0.0D) {
/* 138 */               forward = -1.0D;
/*     */             } 
/*     */           } 
/* 141 */           double cos = Math.cos(Math.toRadians((yaw + 90.0F)));
/* 142 */           double sin = Math.sin(Math.toRadians((yaw + 90.0F)));
/* 143 */           event.setX(forward * ((Float)this.hSpeed.getValue()).floatValue() * cos + strafe * ((Float)this.hSpeed.getValue()).floatValue() * sin);
/* 144 */           event.setZ(forward * ((Float)this.hSpeed.getValue()).floatValue() * sin - strafe * ((Float)this.hSpeed.getValue()).floatValue() * cos);
/*     */         }
/*     */       
/*     */       } 
/* 148 */     } else if (event.getStage() == 0 && this.mode.getValue() == Mode.BYPASS && ((Integer)this.devMode.getValue()).intValue() == 3) {
/* 149 */       if (mc.field_71439_g.func_184613_cA()) {
/* 150 */         event.setX(0.0D);
/* 151 */         event.setY(-1.0E-4D);
/* 152 */         event.setZ(0.0D);
/* 153 */         double forwardInput = mc.field_71439_g.field_71158_b.field_192832_b;
/* 154 */         double strafeInput = mc.field_71439_g.field_71158_b.field_78902_a;
/* 155 */         double[] result = forwardStrafeYaw(forwardInput, strafeInput, mc.field_71439_g.field_70177_z);
/* 156 */         double forward = result[0];
/* 157 */         double strafe = result[1];
/* 158 */         double yaw = result[2];
/* 159 */         if (forwardInput != 0.0D || strafeInput != 0.0D) {
/* 160 */           event.setX(forward * ((Float)this.speed.getValue()).floatValue() * Math.cos(Math.toRadians(yaw + 90.0D)) + strafe * ((Float)this.speed.getValue()).floatValue() * Math.sin(Math.toRadians(yaw + 90.0D)));
/* 161 */           event.setY(forward * ((Float)this.speed.getValue()).floatValue() * Math.sin(Math.toRadians(yaw + 90.0D)) - strafe * ((Float)this.speed.getValue()).floatValue() * Math.cos(Math.toRadians(yaw + 90.0D)));
/*     */         } 
/*     */         
/* 164 */         if (mc.field_71474_y.field_74311_E.func_151470_d()) {
/* 165 */           event.setY(-1.0D);
/*     */         }
/*     */       } 
/* 168 */     } else if (this.mode.getValue() == Mode.TOOBEE) {
/* 169 */       if (!mc.field_71439_g.func_184613_cA())
/* 170 */         return;  if (!mc.field_71439_g.field_71158_b.field_78901_c) {
/* 171 */         if (mc.field_71439_g.field_71158_b.field_78899_d) {
/* 172 */           mc.field_71439_g.field_70181_x = -(((Float)this.tooBeeSpeed.getValue()).floatValue() / 2.0F);
/* 173 */           event.setY(-(((Float)this.speed.getValue()).floatValue() / 2.0F));
/* 174 */         } else if (event.getY() != -1.01E-4D) {
/* 175 */           event.setY(-1.01E-4D);
/* 176 */           mc.field_71439_g.field_70181_x = -1.01E-4D;
/*     */         } 
/*     */       } else {
/*     */         return;
/*     */       } 
/*     */     } 
/* 182 */     setMoveSpeed(event, ((Float)this.tooBeeSpeed.getValue()).floatValue());
/*     */   }
/*     */   
/*     */   private void setMoveSpeed(MoveEvent event, double speed) {
/* 186 */     double forward = mc.field_71439_g.field_71158_b.field_192832_b;
/* 187 */     double strafe = mc.field_71439_g.field_71158_b.field_78902_a;
/* 188 */     float yaw = mc.field_71439_g.field_70177_z;
/* 189 */     if (forward == 0.0D && strafe == 0.0D) {
/* 190 */       event.setX(0.0D);
/* 191 */       event.setZ(0.0D);
/* 192 */       mc.field_71439_g.field_70159_w = 0.0D;
/* 193 */       mc.field_71439_g.field_70179_y = 0.0D;
/*     */     } else {
/* 195 */       if (forward != 0.0D) {
/* 196 */         if (strafe > 0.0D) {
/* 197 */           yaw += ((forward > 0.0D) ? -45 : 45);
/* 198 */         } else if (strafe < 0.0D) {
/* 199 */           yaw += ((forward > 0.0D) ? 45 : -45);
/*     */         } 
/* 201 */         strafe = 0.0D;
/* 202 */         if (forward > 0.0D) {
/* 203 */           forward = 1.0D;
/* 204 */         } else if (forward < 0.0D) {
/* 205 */           forward = -1.0D;
/*     */         } 
/*     */       } 
/* 208 */       double x = forward * speed * -Math.sin(Math.toRadians(yaw)) + strafe * speed * Math.cos(Math.toRadians(yaw));
/*     */       
/* 210 */       double z = forward * speed * Math.cos(Math.toRadians(yaw)) - strafe * speed * -Math.sin(Math.toRadians(yaw));
/*     */       
/* 212 */       event.setX(x);
/* 213 */       event.setZ(z);
/* 214 */       mc.field_71439_g.field_70159_w = x;
/* 215 */       mc.field_71439_g.field_70179_y = z;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onTick() {
/* 221 */     if (!mc.field_71439_g.func_184613_cA())
/* 222 */       return;  switch ((Mode)this.mode.getValue()) {
/*     */       case BOOST:
/* 224 */         if (mc.field_71439_g.func_70090_H()) {
/* 225 */           mc.func_147114_u().func_147297_a((Packet)new CPacketEntityAction((Entity)mc.field_71439_g, CPacketEntityAction.Action.START_FALL_FLYING));
/*     */           
/*     */           return;
/*     */         } 
/* 229 */         if (mc.field_71474_y.field_74314_A.func_151470_d()) {
/* 230 */           mc.field_71439_g.field_70181_x += 0.08D;
/* 231 */         } else if (mc.field_71474_y.field_74311_E.func_151470_d()) {
/* 232 */           mc.field_71439_g.field_70181_x -= 0.04D;
/*     */         } 
/*     */ 
/*     */         
/* 236 */         if (mc.field_71474_y.field_74351_w.func_151470_d()) {
/*     */           
/* 238 */           float yaw = (float)Math.toRadians(mc.field_71439_g.field_70177_z);
/* 239 */           mc.field_71439_g.field_70159_w -= (MathHelper.func_76126_a(yaw) * 0.05F);
/* 240 */           mc.field_71439_g.field_70179_y += (MathHelper.func_76134_b(yaw) * 0.05F); break;
/* 241 */         }  if (mc.field_71474_y.field_74368_y.func_151470_d()) {
/*     */           
/* 243 */           float yaw = (float)Math.toRadians(mc.field_71439_g.field_70177_z);
/* 244 */           mc.field_71439_g.field_70159_w += (MathHelper.func_76126_a(yaw) * 0.05F);
/* 245 */           mc.field_71439_g.field_70179_y -= (MathHelper.func_76134_b(yaw) * 0.05F);
/*     */         } 
/*     */         break;
/*     */       case FLY:
/* 249 */         mc.field_71439_g.field_71075_bZ.field_75100_b = true;
/*     */         break;
/*     */     } 
/*     */   }
/*     */   @SubscribeEvent
/*     */   public void onUpdateWalkingPlayer(UpdateWalkingPlayerEvent event) {
/*     */     double rotationYaw;
/* 256 */     if (mc.field_71439_g.func_184582_a(EntityEquipmentSlot.CHEST).func_77973_b() != Items.field_185160_cR) {
/*     */       return;
/*     */     }
/*     */     
/* 260 */     switch (event.getStage()) {
/*     */       case 0:
/* 262 */         if (((Boolean)this.disableInLiquid.getValue()).booleanValue() && (mc.field_71439_g.func_70090_H() || mc.field_71439_g.func_180799_ab())) {
/* 263 */           if (mc.field_71439_g.func_184613_cA()) {
/* 264 */             mc.func_147114_u().func_147297_a((Packet)new CPacketEntityAction((Entity)mc.field_71439_g, CPacketEntityAction.Action.START_FALL_FLYING));
/*     */           }
/*     */           
/*     */           return;
/*     */         } 
/* 269 */         if (((Boolean)this.autoStart.getValue()).booleanValue() && 
/* 270 */           mc.field_71474_y.field_74314_A.func_151470_d() && !mc.field_71439_g.func_184613_cA() && 
/* 271 */           mc.field_71439_g.field_70181_x < 0.0D && 
/* 272 */           this.timer.passedMs(250L)) {
/* 273 */           mc.func_147114_u().func_147297_a((Packet)new CPacketEntityAction((Entity)mc.field_71439_g, CPacketEntityAction.Action.START_FALL_FLYING));
/* 274 */           this.timer.reset();
/*     */         } 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 280 */         if (this.mode.getValue() == Mode.BETTER) {
/* 281 */           double[] dir = MathUtil.directionSpeed((((Integer)this.devMode.getValue()).intValue() == 1) ? ((Float)this.speed.getValue()).floatValue() : ((Float)this.hSpeed.getValue()).floatValue());
/* 282 */           switch (((Integer)this.devMode.getValue()).intValue()) {
/*     */             case 1:
/* 284 */               mc.field_71439_g.func_70016_h(0.0D, 0.0D, 0.0D);
/* 285 */               mc.field_71439_g.field_70747_aH = ((Float)this.speed.getValue()).floatValue();
/*     */               
/* 287 */               if (mc.field_71474_y.field_74314_A.func_151470_d()) {
/* 288 */                 mc.field_71439_g.field_70181_x += ((Float)this.speed.getValue()).floatValue();
/*     */               }
/*     */               
/* 291 */               if (mc.field_71474_y.field_74311_E.func_151470_d()) {
/* 292 */                 mc.field_71439_g.field_70181_x -= ((Float)this.speed.getValue()).floatValue();
/*     */               }
/*     */               
/* 295 */               if (mc.field_71439_g.field_71158_b.field_78902_a != 0.0F || mc.field_71439_g.field_71158_b.field_192832_b != 0.0F) {
/* 296 */                 mc.field_71439_g.field_70159_w = dir[0];
/* 297 */                 mc.field_71439_g.field_70179_y = dir[1]; break;
/*     */               } 
/* 299 */               mc.field_71439_g.field_70159_w = 0.0D;
/* 300 */               mc.field_71439_g.field_70179_y = 0.0D;
/*     */               break;
/*     */             
/*     */             case 2:
/* 304 */               if (mc.field_71439_g.func_184613_cA()) {
/* 305 */                 if (this.flyHeight == null) {
/* 306 */                   this.flyHeight = Double.valueOf(mc.field_71439_g.field_70163_u);
/*     */                 }
/*     */               } else {
/* 309 */                 this.flyHeight = null;
/*     */                 
/*     */                 return;
/*     */               } 
/* 313 */               if (((Boolean)this.noKick.getValue()).booleanValue()) {
/* 314 */                 this.flyHeight = Double.valueOf(this.flyHeight.doubleValue() - ((Float)this.glide.getValue()).floatValue());
/*     */               }
/*     */               
/* 317 */               this.posX = Double.valueOf(0.0D);
/* 318 */               this.posZ = Double.valueOf(0.0D);
/*     */               
/* 320 */               if (mc.field_71439_g.field_71158_b.field_78902_a != 0.0F || mc.field_71439_g.field_71158_b.field_192832_b != 0.0F) {
/* 321 */                 this.posX = Double.valueOf(dir[0]);
/* 322 */                 this.posZ = Double.valueOf(dir[1]);
/*     */               } 
/*     */               
/* 325 */               if (mc.field_71474_y.field_74314_A.func_151470_d()) {
/* 326 */                 this.flyHeight = Double.valueOf(mc.field_71439_g.field_70163_u + ((Float)this.vSpeed.getValue()).floatValue());
/*     */               }
/*     */               
/* 329 */               if (mc.field_71474_y.field_74311_E.func_151470_d()) {
/* 330 */                 this.flyHeight = Double.valueOf(mc.field_71439_g.field_70163_u - ((Float)this.vSpeed.getValue()).floatValue());
/*     */               }
/*     */               
/* 333 */               mc.field_71439_g.func_70107_b(mc.field_71439_g.field_70165_t + this.posX.doubleValue(), this.flyHeight.doubleValue(), mc.field_71439_g.field_70161_v + this.posZ.doubleValue());
/* 334 */               mc.field_71439_g.func_70016_h(0.0D, 0.0D, 0.0D);
/*     */               break;
/*     */             case 3:
/* 337 */               if (mc.field_71439_g.func_184613_cA()) {
/* 338 */                 if (this.flyHeight == null || this.posX == null || this.posX
/* 339 */                   .doubleValue() == 0.0D || this.posZ == null || this.posZ
/* 340 */                   .doubleValue() == 0.0D) {
/* 341 */                   this.flyHeight = Double.valueOf(mc.field_71439_g.field_70163_u);
/* 342 */                   this.posX = Double.valueOf(mc.field_71439_g.field_70165_t);
/* 343 */                   this.posZ = Double.valueOf(mc.field_71439_g.field_70161_v);
/*     */                 } 
/*     */               } else {
/* 346 */                 this.flyHeight = null;
/* 347 */                 this.posX = null;
/* 348 */                 this.posZ = null;
/*     */                 
/*     */                 return;
/*     */               } 
/* 352 */               if (((Boolean)this.noKick.getValue()).booleanValue()) {
/* 353 */                 this.flyHeight = Double.valueOf(this.flyHeight.doubleValue() - ((Float)this.glide.getValue()).floatValue());
/*     */               }
/*     */               
/* 356 */               if (mc.field_71439_g.field_71158_b.field_78902_a != 0.0F || mc.field_71439_g.field_71158_b.field_192832_b != 0.0F) {
/* 357 */                 this.posX = Double.valueOf(this.posX.doubleValue() + dir[0]);
/* 358 */                 this.posZ = Double.valueOf(this.posZ.doubleValue() + dir[1]);
/*     */               } 
/*     */               
/* 361 */               if (((Boolean)this.allowUp.getValue()).booleanValue() && mc.field_71474_y.field_74314_A.func_151470_d()) {
/* 362 */                 this.flyHeight = Double.valueOf(mc.field_71439_g.field_70163_u + (((Float)this.vSpeed.getValue()).floatValue() / 10.0F));
/*     */               }
/*     */               
/* 365 */               if (mc.field_71474_y.field_74311_E.func_151470_d()) {
/* 366 */                 this.flyHeight = Double.valueOf(mc.field_71439_g.field_70163_u - (((Float)this.vSpeed.getValue()).floatValue() / 10.0F));
/*     */               }
/*     */               
/* 369 */               mc.field_71439_g.func_70107_b(this.posX.doubleValue(), this.flyHeight.doubleValue(), this.posZ.doubleValue());
/* 370 */               mc.field_71439_g.func_70016_h(0.0D, 0.0D, 0.0D);
/*     */               break;
/*     */           } 
/*     */         
/*     */         } 
/* 375 */         rotationYaw = Math.toRadians(mc.field_71439_g.field_70177_z);
/* 376 */         if (mc.field_71439_g.func_184613_cA()) {
/* 377 */           float speedScaled; double[] directionSpeedPacket; switch ((Mode)this.mode.getValue()) {
/*     */             case VANILLA:
/* 379 */               speedScaled = ((Float)this.speed.getValue()).floatValue() * 0.05F;
/*     */               
/* 381 */               if (mc.field_71474_y.field_74314_A.func_151470_d()) {
/* 382 */                 mc.field_71439_g.field_70181_x += speedScaled;
/*     */               }
/*     */               
/* 385 */               if (mc.field_71474_y.field_74311_E.func_151470_d()) {
/* 386 */                 mc.field_71439_g.field_70181_x -= speedScaled;
/*     */               }
/*     */               
/* 389 */               if (mc.field_71474_y.field_74351_w.func_151470_d()) {
/* 390 */                 mc.field_71439_g.field_70159_w -= Math.sin(rotationYaw) * speedScaled;
/* 391 */                 mc.field_71439_g.field_70179_y += Math.cos(rotationYaw) * speedScaled;
/*     */               } 
/*     */               
/* 394 */               if (mc.field_71474_y.field_74368_y.func_151470_d()) {
/* 395 */                 mc.field_71439_g.field_70159_w += Math.sin(rotationYaw) * speedScaled;
/* 396 */                 mc.field_71439_g.field_70179_y -= Math.cos(rotationYaw) * speedScaled;
/*     */               } 
/*     */               break;
/*     */             case PACKET:
/* 400 */               freezePlayer((EntityPlayer)mc.field_71439_g);
/* 401 */               runNoKick((EntityPlayer)mc.field_71439_g);
/*     */               
/* 403 */               directionSpeedPacket = MathUtil.directionSpeed(((Float)this.speed.getValue()).floatValue());
/*     */               
/* 405 */               if (mc.field_71439_g.field_71158_b.field_78901_c) {
/* 406 */                 mc.field_71439_g.field_70181_x = ((Float)this.speed.getValue()).floatValue();
/*     */               }
/*     */               
/* 409 */               if (mc.field_71439_g.field_71158_b.field_78899_d) {
/* 410 */                 mc.field_71439_g.field_70181_x = -((Float)this.speed.getValue()).floatValue();
/*     */               }
/*     */               
/* 413 */               if (mc.field_71439_g.field_71158_b.field_78902_a != 0.0F || mc.field_71439_g.field_71158_b.field_192832_b != 0.0F) {
/* 414 */                 mc.field_71439_g.field_70159_w = directionSpeedPacket[0];
/* 415 */                 mc.field_71439_g.field_70179_y = directionSpeedPacket[1];
/*     */               } 
/*     */               
/* 418 */               mc.func_147114_u().func_147297_a((Packet)new CPacketEntityAction((Entity)mc.field_71439_g, CPacketEntityAction.Action.START_FALL_FLYING));
/* 419 */               mc.func_147114_u().func_147297_a((Packet)new CPacketEntityAction((Entity)mc.field_71439_g, CPacketEntityAction.Action.START_FALL_FLYING));
/*     */               break;
/*     */             case BYPASS:
/* 422 */               if (((Integer)this.devMode.getValue()).intValue() == 3) {
/* 423 */                 if (mc.field_71474_y.field_74314_A.func_151470_d()) {
/* 424 */                   mc.field_71439_g.field_70181_x = 0.019999999552965164D;
/*     */                 }
/*     */                 
/* 427 */                 if (mc.field_71474_y.field_74311_E.func_151470_d()) {
/* 428 */                   mc.field_71439_g.field_70181_x = -0.20000000298023224D;
/*     */                 }
/*     */                 
/* 431 */                 if (mc.field_71439_g.field_70173_aa % 8 == 0 && mc.field_71439_g.field_70163_u <= 240.0D) {
/* 432 */                   mc.field_71439_g.field_70181_x = 0.019999999552965164D;
/*     */                 }
/*     */                 
/* 435 */                 mc.field_71439_g.field_71075_bZ.field_75100_b = true;
/* 436 */                 mc.field_71439_g.field_71075_bZ.func_75092_a(0.025F);
/*     */                 
/* 438 */                 double[] directionSpeedBypass = MathUtil.directionSpeed(0.5199999809265137D);
/* 439 */                 if (mc.field_71439_g.field_71158_b.field_78902_a != 0.0F || mc.field_71439_g.field_71158_b.field_192832_b != 0.0F) {
/* 440 */                   mc.field_71439_g.field_70159_w = directionSpeedBypass[0];
/* 441 */                   mc.field_71439_g.field_70179_y = directionSpeedBypass[1]; break;
/*     */                 } 
/* 443 */                 mc.field_71439_g.field_70159_w = 0.0D;
/* 444 */                 mc.field_71439_g.field_70179_y = 0.0D;
/*     */               } 
/*     */               break;
/*     */           } 
/*     */ 
/*     */         
/*     */         } 
/* 451 */         if (((Boolean)this.infiniteDura.getValue()).booleanValue()) {
/* 452 */           mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketEntityAction((Entity)mc.field_71439_g, CPacketEntityAction.Action.START_FALL_FLYING));
/*     */         }
/*     */         break;
/*     */       case 1:
/* 456 */         if (((Boolean)this.infiniteDura.getValue()).booleanValue()) {
/* 457 */           mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketEntityAction((Entity)mc.field_71439_g, CPacketEntityAction.Action.START_FALL_FLYING));
/*     */         }
/*     */         break;
/*     */     } 
/*     */   }
/*     */   
/*     */   private double[] forwardStrafeYaw(double forward, double strafe, double yaw) {
/* 464 */     double[] result = new double[3];
/* 465 */     result[0] = forward;
/* 466 */     result[1] = strafe;
/* 467 */     result[2] = yaw;
/* 468 */     if ((forward != 0.0D || strafe != 0.0D) && 
/* 469 */       forward != 0.0D) {
/* 470 */       if (strafe > 0.0D) {
/* 471 */         result[2] = result[2] + ((forward > 0.0D) ? -45 : 45);
/* 472 */       } else if (strafe < 0.0D) {
/* 473 */         result[2] = result[2] + ((forward > 0.0D) ? 45 : -45);
/*     */       } 
/*     */       
/* 476 */       result[1] = 0.0D;
/* 477 */       if (forward > 0.0D) {
/* 478 */         result[0] = 1.0D;
/* 479 */       } else if (forward < 0.0D) {
/* 480 */         result[0] = -1.0D;
/*     */       } 
/*     */     } 
/*     */     
/* 484 */     return result;
/*     */   }
/*     */   
/*     */   private void freezePlayer(EntityPlayer player) {
/* 488 */     player.field_70159_w = 0.0D;
/* 489 */     player.field_70181_x = 0.0D;
/* 490 */     player.field_70179_y = 0.0D;
/*     */   }
/*     */   
/*     */   private void runNoKick(EntityPlayer player) {
/* 494 */     if (((Boolean)this.noKick.getValue()).booleanValue() && !player.func_184613_cA() && player.field_70173_aa % 4 == 0) {
/* 495 */       player.field_70181_x = -0.03999999910593033D;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void onDisable() {
/* 501 */     if (fullNullCheck() || mc.field_71439_g.field_71075_bZ.field_75098_d)
/* 502 */       return;  mc.field_71439_g.field_71075_bZ.field_75100_b = false;
/*     */   }
/*     */   
/*     */   public enum Mode {
/* 506 */     VANILLA,
/* 507 */     PACKET,
/* 508 */     BOOST,
/* 509 */     FLY,
/* 510 */     BYPASS,
/* 511 */     BETTER,
/* 512 */     OHARE,
/* 513 */     TOOBEE;
/*     */   }
/*     */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\me\earth\phobos\features\modules\movement\ElytraFlight.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */