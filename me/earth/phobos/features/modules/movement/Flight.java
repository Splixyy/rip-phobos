/*     */ package me.earth.phobos.features.modules.movement;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.atomic.AtomicReference;
/*     */ import me.earth.phobos.Phobos;
/*     */ import me.earth.phobos.event.events.ClientEvent;
/*     */ import me.earth.phobos.event.events.MoveEvent;
/*     */ import me.earth.phobos.event.events.PacketEvent;
/*     */ import me.earth.phobos.event.events.PushEvent;
/*     */ import me.earth.phobos.event.events.UpdateWalkingPlayerEvent;
/*     */ import me.earth.phobos.features.modules.Module;
/*     */ import me.earth.phobos.features.setting.Setting;
/*     */ import me.earth.phobos.util.EntityUtil;
/*     */ import me.earth.phobos.util.MathUtil;
/*     */ import me.earth.phobos.util.Timer;
/*     */ import me.earth.phobos.util.Util;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.player.PlayerCapabilities;
/*     */ import net.minecraft.init.MobEffects;
/*     */ import net.minecraft.network.Packet;
/*     */ import net.minecraft.network.play.client.CPacketConfirmTeleport;
/*     */ import net.minecraft.network.play.client.CPacketPlayer;
/*     */ import net.minecraft.network.play.server.SPacketPlayerPosLook;
/*     */ import net.minecraft.util.math.AxisAlignedBB;
/*     */ import net.minecraft.util.math.BlockPos;
/*     */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*     */ import net.minecraftforge.fml.common.gameevent.TickEvent;
/*     */ 
/*     */ public class Flight extends Module {
/*  31 */   public Setting<Mode> mode = register(new Setting("Mode", Mode.PACKET));
/*  32 */   public Setting<Boolean> better = register(new Setting("Better", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.PACKET)));
/*  33 */   public Setting<Format> format = register(new Setting("Format", Format.DAMAGE, v -> (this.mode.getValue() == Mode.DAMAGE)));
/*  34 */   public Setting<PacketMode> type = register(new Setting("Type", PacketMode.Y, v -> (this.mode.getValue() == Mode.PACKET)));
/*  35 */   public Setting<Boolean> phase = register(new Setting("Phase", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.PACKET && ((Boolean)this.better.getValue()).booleanValue())));
/*  36 */   public Setting<Float> speed = register(new Setting("Speed", Float.valueOf(0.1F), Float.valueOf(0.0F), Float.valueOf(10.0F), v -> (this.mode.getValue() == Mode.PACKET || this.mode.getValue() == Mode.DESCEND || this.mode.getValue() == Mode.DAMAGE), "The speed."));
/*  37 */   public Setting<Boolean> noKick = register(new Setting("NoKick", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.PACKET || this.mode.getValue() == Mode.DAMAGE)));
/*  38 */   public Setting<Boolean> noClip = register(new Setting("NoClip", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.DAMAGE)));
/*  39 */   public Setting<Boolean> groundSpoof = register(new Setting("GroundSpoof", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.SPOOF)));
/*  40 */   public Setting<Boolean> antiGround = register(new Setting("AntiGround", Boolean.valueOf(true), v -> (this.mode.getValue() == Mode.SPOOF)));
/*  41 */   public Setting<Integer> cooldown = register(new Setting("Cooldown", Integer.valueOf(1), v -> (this.mode.getValue() == Mode.DESCEND)));
/*  42 */   public Setting<Boolean> ascend = register(new Setting("Ascend", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.DESCEND)));
/*     */   
/*  44 */   private List<CPacketPlayer> packets = new ArrayList<>();
/*  45 */   private int teleportId = 0;
/*  46 */   private static Flight INSTANCE = new Flight();
/*  47 */   private int counter = 0;
/*  48 */   private final Fly flySwitch = new Fly();
/*     */   
/*     */   private double moveSpeed;
/*  51 */   private Timer delayTimer = new Timer(); private double lastDist; private int level;
/*     */   
/*     */   public Flight() {
/*  54 */     super("Flight", "Makes you fly.", Module.Category.MOVEMENT, true, false, false);
/*  55 */     setInstance();
/*     */   }
/*     */   
/*     */   private void setInstance() {
/*  59 */     INSTANCE = this;
/*     */   }
/*     */   
/*     */   public static Flight getInstance() {
/*  63 */     if (INSTANCE == null) {
/*  64 */       INSTANCE = new Flight();
/*     */     }
/*  66 */     return INSTANCE;
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onTickEvent(TickEvent.ClientTickEvent event) {
/*  71 */     if (fullNullCheck() || this.mode.getValue() != Mode.DESCEND) {
/*     */       return;
/*     */     }
/*     */     
/*  75 */     if (event.phase == TickEvent.Phase.END) {
/*  76 */       if (!mc.field_71439_g.func_184613_cA()) {
/*  77 */         if (this.counter < 1) {
/*  78 */           this.counter += ((Integer)this.cooldown.getValue()).intValue();
/*  79 */           mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u, mc.field_71439_g.field_70161_v, false));
/*  80 */           mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u - 0.03D, mc.field_71439_g.field_70161_v, true));
/*     */         } else {
/*  82 */           this.counter--;
/*     */         }
/*     */       
/*     */       }
/*  86 */     } else if (((Boolean)this.ascend.getValue()).booleanValue()) {
/*  87 */       mc.field_71439_g.field_70181_x = ((Float)this.speed.getValue()).floatValue();
/*     */     } else {
/*  89 */       mc.field_71439_g.field_70181_x = -((Float)this.speed.getValue()).floatValue();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void onEnable() {
/*  96 */     if (fullNullCheck()) {
/*     */       return;
/*     */     }
/*     */     
/* 100 */     if (this.mode.getValue() == Mode.PACKET) {
/* 101 */       this.teleportId = 0;
/* 102 */       this.packets.clear();
/* 103 */       CPacketPlayer.Position position = new CPacketPlayer.Position(mc.field_71439_g.field_70165_t, 0.0D, mc.field_71439_g.field_70161_v, mc.field_71439_g.field_70122_E);
/* 104 */       this.packets.add(position);
/* 105 */       mc.field_71439_g.field_71174_a.func_147297_a((Packet)position);
/*     */     } 
/*     */     
/* 108 */     if (this.mode.getValue() == Mode.CREATIVE) {
/* 109 */       mc.field_71439_g.field_71075_bZ.field_75100_b = true;
/* 110 */       if (mc.field_71439_g.field_71075_bZ.field_75098_d)
/* 111 */         return;  mc.field_71439_g.field_71075_bZ.field_75101_c = true;
/*     */     } 
/*     */     
/* 114 */     if (this.mode.getValue() == Mode.SPOOF) {
/* 115 */       this.flySwitch.enable();
/*     */     }
/*     */     
/* 118 */     if (this.mode.getValue() == Mode.DAMAGE) {
/* 119 */       this.level = 0;
/* 120 */       if (this.format.getValue() == Format.PACKET && 
/* 121 */         mc.field_71441_e != null) {
/* 122 */         this.teleportId = 0;
/* 123 */         this.packets.clear();
/* 124 */         CPacketPlayer.Position position = new CPacketPlayer.Position(mc.field_71439_g.field_70165_t, (mc.field_71439_g.field_70163_u <= 10.0D) ? 255.0D : 1.0D, mc.field_71439_g.field_70161_v, mc.field_71439_g.field_70122_E);
/* 125 */         this.packets.add(position);
/* 126 */         mc.field_71439_g.field_71174_a.func_147297_a((Packet)position);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onUpdateWalkingPlayer(UpdateWalkingPlayerEvent event) {
/* 134 */     if (this.mode.getValue() == Mode.DAMAGE) {
/* 135 */       if (this.format.getValue() == Format.DAMAGE) {
/* 136 */         if (event.getStage() == 0) {
/* 137 */           mc.field_71439_g.field_70181_x = 0.0D;
/* 138 */           double motionY = 0.41999998688697815D;
/* 139 */           if (mc.field_71439_g.field_70122_E) {
/* 140 */             if (mc.field_71439_g.func_70644_a(MobEffects.field_76430_j)) {
/* 141 */               motionY += ((mc.field_71439_g.func_70660_b(MobEffects.field_76430_j).func_76458_c() + 1) * 0.1F);
/*     */             }
/* 143 */             Phobos.positionManager.setPlayerPosition(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70181_x = motionY, mc.field_71439_g.field_70161_v);
/*     */             
/* 145 */             this.moveSpeed *= 2.149D;
/*     */           } 
/*     */         } 
/* 148 */         if (mc.field_71439_g.field_70173_aa % 2 == 0) {
/* 149 */           mc.field_71439_g.func_70107_b(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u + MathUtil.getRandom(1.2354235325235235E-14D, 1.2354235325235233E-13D), mc.field_71439_g.field_70161_v);
/*     */         }
/* 151 */         if (mc.field_71474_y.field_74314_A.func_151470_d())
/* 152 */           mc.field_71439_g.field_70181_x += (((Float)this.speed.getValue()).floatValue() / 2.0F); 
/* 153 */         if (mc.field_71474_y.field_74311_E.func_151470_d())
/* 154 */           mc.field_71439_g.field_70181_x -= (((Float)this.speed.getValue()).floatValue() / 2.0F); 
/*     */       } 
/* 156 */       if (this.format.getValue() == Format.NORMAL) {
/* 157 */         if (mc.field_71474_y.field_74314_A.func_151470_d()) {
/* 158 */           mc.field_71439_g.field_70181_x = ((Float)this.speed.getValue()).floatValue();
/* 159 */         } else if (mc.field_71474_y.field_74311_E.func_151470_d()) {
/* 160 */           mc.field_71439_g.field_70181_x = -((Float)this.speed.getValue()).floatValue();
/*     */         } else {
/* 162 */           mc.field_71439_g.field_70181_x = 0.0D;
/*     */         } 
/* 164 */         if (((Boolean)this.noKick.getValue()).booleanValue() && 
/* 165 */           mc.field_71439_g.field_70173_aa % 5 == 0) {
/* 166 */           Phobos.positionManager.setPlayerPosition(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u - 0.03125D, mc.field_71439_g.field_70161_v, true);
/*     */         }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 173 */         double[] dir = EntityUtil.forward(((Float)this.speed.getValue()).floatValue());
/* 174 */         mc.field_71439_g.field_70159_w = dir[0];
/* 175 */         mc.field_71439_g.field_70179_y = dir[1];
/*     */       } 
/* 177 */       if (this.format.getValue() == Format.PACKET) {
/* 178 */         if (this.teleportId <= 0) {
/* 179 */           CPacketPlayer.Position position = new CPacketPlayer.Position(mc.field_71439_g.field_70165_t, (mc.field_71439_g.field_70163_u <= 10.0D) ? 255.0D : 1.0D, mc.field_71439_g.field_70161_v, mc.field_71439_g.field_70122_E);
/* 180 */           this.packets.add(position);
/* 181 */           mc.field_71439_g.field_71174_a.func_147297_a((Packet)position);
/*     */           return;
/*     */         } 
/* 184 */         mc.field_71439_g.func_70016_h(0.0D, 0.0D, 0.0D);
/* 185 */         double posY = -1.0E-8D;
/* 186 */         if (!mc.field_71474_y.field_74314_A.func_151470_d() && !mc.field_71474_y.field_74311_E.func_151470_d()) {
/* 187 */           if (EntityUtil.isMoving()) {
/* 188 */             double x; for (x = 0.0625D; x < ((Float)this.speed.getValue()).floatValue(); x += 0.262D) {
/* 189 */               double[] dir = EntityUtil.forward(x);
/* 190 */               mc.field_71439_g.func_70016_h(dir[0], posY, dir[1]);
/* 191 */               move(dir[0], posY, dir[1]);
/*     */             }
/*     */           
/*     */           } 
/* 195 */         } else if (mc.field_71474_y.field_74314_A.func_151470_d()) {
/* 196 */           for (int i = 0; i <= 3; i++) {
/*     */             
/* 198 */             mc.field_71439_g.func_70016_h(0.0D, (mc.field_71439_g.field_70173_aa % 20 == 0) ? -0.03999999910593033D : (0.062F * i), 0.0D);
/* 199 */             move(0.0D, (mc.field_71439_g.field_70173_aa % 20 == 0) ? -0.03999999910593033D : (0.062F * i), 0.0D);
/*     */           } 
/* 201 */         } else if (mc.field_71474_y.field_74311_E.func_151470_d()) {
/* 202 */           for (int i = 0; i <= 3; i++) {
/* 203 */             mc.field_71439_g.func_70016_h(0.0D, posY - 0.0625D * i, 0.0D);
/* 204 */             move(0.0D, posY - 0.0625D * i, 0.0D);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */       
/* 209 */       if (this.format.getValue() == Format.SLOW) {
/* 210 */         double posX = mc.field_71439_g.field_70165_t;
/* 211 */         double posY = mc.field_71439_g.field_70163_u;
/* 212 */         double posZ = mc.field_71439_g.field_70161_v;
/* 213 */         boolean ground = mc.field_71439_g.field_70122_E;
/* 214 */         mc.field_71439_g.func_70016_h(0.0D, 0.0D, 0.0D);
/* 215 */         if (!mc.field_71474_y.field_74314_A.func_151470_d() && !mc.field_71474_y.field_74311_E.func_151470_d()) {
/* 216 */           double[] dir = EntityUtil.forward(0.0625D);
/* 217 */           mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(posX + dir[0], posY, posZ + dir[1], ground));
/* 218 */           mc.field_71439_g.func_70634_a(posX + dir[0], posY, posZ + dir[1]);
/*     */         
/*     */         }
/* 221 */         else if (mc.field_71474_y.field_74314_A.func_151470_d()) {
/* 222 */           mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(posX, posY + 0.0625D, posZ, ground));
/* 223 */           mc.field_71439_g.func_70634_a(posX, posY + 0.0625D, posZ);
/*     */         }
/* 225 */         else if (mc.field_71474_y.field_74311_E.func_151470_d()) {
/* 226 */           mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(posX, posY - 0.0625D, posZ, ground));
/* 227 */           mc.field_71439_g.func_70634_a(posX, posY - 0.0625D, posZ);
/*     */         } 
/*     */         
/* 230 */         mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(posX + mc.field_71439_g.field_70159_w, (mc.field_71439_g.field_70163_u <= 10.0D) ? 255.0D : 1.0D, posZ + mc.field_71439_g.field_70179_y, ground));
/*     */       } 
/*     */       
/* 233 */       if (this.format.getValue() == Format.DELAY) {
/* 234 */         if (this.delayTimer.passedMs(1000L))
/* 235 */           this.delayTimer.reset(); 
/* 236 */         if (this.delayTimer.passedMs(600L)) {
/* 237 */           mc.field_71439_g.func_70016_h(0.0D, 0.0D, 0.0D);
/*     */           return;
/*     */         } 
/* 240 */         if (this.teleportId <= 0) {
/* 241 */           CPacketPlayer.Position position = new CPacketPlayer.Position(mc.field_71439_g.field_70165_t, (mc.field_71439_g.field_70163_u <= 10.0D) ? 255.0D : 1.0D, mc.field_71439_g.field_70161_v, mc.field_71439_g.field_70122_E);
/* 242 */           this.packets.add(position);
/* 243 */           mc.field_71439_g.field_71174_a.func_147297_a((Packet)position);
/*     */           return;
/*     */         } 
/* 246 */         mc.field_71439_g.func_70016_h(0.0D, 0.0D, 0.0D);
/* 247 */         double posY = -1.0E-8D;
/* 248 */         if (!mc.field_71474_y.field_74314_A.func_151470_d() && !mc.field_71474_y.field_74311_E.func_151470_d()) {
/* 249 */           if (EntityUtil.isMoving()) {
/* 250 */             double[] dir = EntityUtil.forward(0.2D);
/* 251 */             mc.field_71439_g.func_70016_h(dir[0], posY, dir[1]);
/* 252 */             move(dir[0], posY, dir[1]);
/*     */           }
/*     */         
/* 255 */         } else if (mc.field_71474_y.field_74314_A.func_151470_d()) {
/* 256 */           mc.field_71439_g.func_70016_h(0.0D, 0.06199999898672104D, 0.0D);
/* 257 */           move(0.0D, 0.06199999898672104D, 0.0D);
/* 258 */         } else if (mc.field_71474_y.field_74311_E.func_151470_d()) {
/* 259 */           mc.field_71439_g.func_70016_h(0.0D, 0.0625D, 0.0D);
/* 260 */           move(0.0D, 0.0625D, 0.0D);
/*     */         } 
/*     */       } 
/*     */ 
/*     */       
/* 265 */       if (((Boolean)this.noClip.getValue()).booleanValue()) {
/* 266 */         mc.field_71439_g.field_70145_X = true;
/*     */       }
/*     */     } 
/*     */     
/* 270 */     if (event.getStage() == 0) {
/* 271 */       if (this.mode.getValue() == Mode.CREATIVE) {
/* 272 */         mc.field_71439_g.field_71075_bZ.func_75092_a(((Float)this.speed.getValue()).floatValue());
/* 273 */         mc.field_71439_g.field_71075_bZ.field_75100_b = true;
/* 274 */         if (mc.field_71439_g.field_71075_bZ.field_75098_d)
/* 275 */           return;  mc.field_71439_g.field_71075_bZ.field_75101_c = true;
/*     */       } 
/*     */       
/* 278 */       if (this.mode.getValue() == Mode.VANILLA) {
/* 279 */         mc.field_71439_g.func_70016_h(0.0D, 0.0D, 0.0D);
/* 280 */         mc.field_71439_g.field_70747_aH = ((Float)this.speed.getValue()).floatValue();
/* 281 */         if (((Boolean)this.noKick.getValue()).booleanValue() && 
/* 282 */           mc.field_71439_g.field_70173_aa % 4 == 0) {
/* 283 */           mc.field_71439_g.field_70181_x = -0.03999999910593033D;
/*     */         }
/*     */         
/* 286 */         double[] dir = MathUtil.directionSpeed(((Float)this.speed.getValue()).floatValue());
/* 287 */         if (mc.field_71439_g.field_71158_b.field_78902_a != 0.0F || mc.field_71439_g.field_71158_b.field_192832_b != 0.0F) {
/* 288 */           mc.field_71439_g.field_70159_w = dir[0];
/* 289 */           mc.field_71439_g.field_70179_y = dir[1];
/*     */         } else {
/* 291 */           mc.field_71439_g.field_70159_w = 0.0D;
/* 292 */           mc.field_71439_g.field_70179_y = 0.0D;
/*     */         } 
/* 294 */         if (mc.field_71474_y.field_74314_A.func_151470_d()) {
/* 295 */           if (((Boolean)this.noKick.getValue()).booleanValue()) {
/* 296 */             mc.field_71439_g.field_70181_x = (mc.field_71439_g.field_70173_aa % 20 == 0) ? -0.03999999910593033D : ((Float)this.speed.getValue()).floatValue();
/*     */           } else {
/* 298 */             mc.field_71439_g.field_70181_x += ((Float)this.speed.getValue()).floatValue();
/*     */           } 
/*     */         }
/* 301 */         if (mc.field_71474_y.field_74311_E.func_151470_d()) {
/* 302 */           mc.field_71439_g.field_70181_x -= ((Float)this.speed.getValue()).floatValue();
/*     */         }
/*     */       } 
/*     */       
/* 306 */       if (this.mode.getValue() == Mode.PACKET && !((Boolean)this.better.getValue()).booleanValue()) {
/* 307 */         doNormalPacketFly();
/*     */       }
/*     */       
/* 310 */       if (this.mode.getValue() == Mode.PACKET && ((Boolean)this.better.getValue()).booleanValue()) {
/* 311 */         doBetterPacketFly();
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private void doNormalPacketFly() {
/* 317 */     if (this.teleportId <= 0) {
/* 318 */       CPacketPlayer.Position position = new CPacketPlayer.Position(mc.field_71439_g.field_70165_t, 0.0D, mc.field_71439_g.field_70161_v, mc.field_71439_g.field_70122_E);
/* 319 */       this.packets.add(position);
/* 320 */       mc.field_71439_g.field_71174_a.func_147297_a((Packet)position);
/*     */       
/*     */       return;
/*     */     } 
/* 324 */     mc.field_71439_g.func_70016_h(0.0D, 0.0D, 0.0D);
/*     */     
/* 326 */     if (mc.field_71441_e.func_184144_a((Entity)mc.field_71439_g, mc.field_71439_g.func_174813_aQ().func_72321_a(-0.0625D, 0.0D, -0.0625D)).isEmpty()) {
/* 327 */       double ySpeed = 0.0D;
/*     */       
/* 329 */       if (mc.field_71474_y.field_74314_A.func_151470_d()) {
/*     */         
/* 331 */         if (((Boolean)this.noKick.getValue()).booleanValue()) {
/* 332 */           ySpeed = (mc.field_71439_g.field_70173_aa % 20 == 0) ? -0.03999999910593033D : 0.06199999898672104D;
/*     */         } else {
/* 334 */           ySpeed = 0.06199999898672104D;
/*     */         } 
/* 336 */       } else if (mc.field_71474_y.field_74311_E.func_151470_d()) {
/* 337 */         ySpeed = -0.062D;
/*     */       } else {
/* 339 */         ySpeed = mc.field_71441_e.func_184144_a((Entity)mc.field_71439_g, mc.field_71439_g.func_174813_aQ().func_72321_a(-0.0625D, -0.0625D, -0.0625D)).isEmpty() ? ((mc.field_71439_g.field_70173_aa % 4 == 0) ? (((Boolean)this.noKick.getValue()).booleanValue() ? -0.04F : 0.0F) : 0.0D) : 0.0D;
/*     */       } 
/*     */       
/* 342 */       double[] directionalSpeed = MathUtil.directionSpeed(((Float)this.speed.getValue()).floatValue());
/*     */       
/* 344 */       if (mc.field_71474_y.field_74314_A.func_151470_d() || mc.field_71474_y.field_74311_E.func_151470_d() || mc.field_71474_y.field_74351_w.func_151470_d() || mc.field_71474_y.field_74368_y.func_151470_d() || mc.field_71474_y.field_74366_z.func_151470_d() || mc.field_71474_y.field_74370_x.func_151470_d()) {
/* 345 */         if (directionalSpeed[0] != 0.0D || directionalSpeed[1] != 0.0D) {
/* 346 */           if (mc.field_71439_g.field_71158_b.field_78901_c && (mc.field_71439_g.field_70702_br != 0.0F || mc.field_71439_g.field_191988_bg != 0.0F)) {
/* 347 */             mc.field_71439_g.func_70016_h(0.0D, 0.0D, 0.0D);
/* 348 */             move(0.0D, 0.0D, 0.0D);
/* 349 */             for (int i = 0; i <= 3; i++) {
/* 350 */               mc.field_71439_g.func_70016_h(0.0D, ySpeed * i, 0.0D);
/* 351 */               move(0.0D, ySpeed * i, 0.0D);
/*     */             }
/*     */           
/* 354 */           } else if (mc.field_71439_g.field_71158_b.field_78901_c) {
/* 355 */             mc.field_71439_g.func_70016_h(0.0D, 0.0D, 0.0D);
/* 356 */             move(0.0D, 0.0D, 0.0D);
/* 357 */             for (int i = 0; i <= 3; i++) {
/* 358 */               mc.field_71439_g.func_70016_h(0.0D, ySpeed * i, 0.0D);
/* 359 */               move(0.0D, ySpeed * i, 0.0D);
/*     */             } 
/*     */           } else {
/* 362 */             for (int i = 0; i <= 2; i++) {
/* 363 */               mc.field_71439_g.func_70016_h(directionalSpeed[0] * i, ySpeed * i, directionalSpeed[1] * i);
/* 364 */               move(directionalSpeed[0] * i, ySpeed * i, directionalSpeed[1] * i);
/*     */             }
/*     */           
/*     */           }
/*     */         
/*     */         }
/* 370 */       } else if (((Boolean)this.noKick.getValue()).booleanValue() && 
/* 371 */         mc.field_71441_e.func_184144_a((Entity)mc.field_71439_g, mc.field_71439_g.func_174813_aQ().func_72321_a(-0.0625D, -0.0625D, -0.0625D)).isEmpty()) {
/* 372 */         mc.field_71439_g.func_70016_h(0.0D, (mc.field_71439_g.field_70173_aa % 2 == 0) ? 0.03999999910593033D : -0.03999999910593033D, 0.0D);
/* 373 */         move(0.0D, (mc.field_71439_g.field_70173_aa % 2 == 0) ? 0.03999999910593033D : -0.03999999910593033D, 0.0D);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void doBetterPacketFly() {
/* 381 */     if (this.teleportId <= 0) {
/* 382 */       CPacketPlayer.Position position = new CPacketPlayer.Position(mc.field_71439_g.field_70165_t, 10000.0D, mc.field_71439_g.field_70161_v, mc.field_71439_g.field_70122_E);
/* 383 */       this.packets.add(position);
/* 384 */       mc.field_71439_g.field_71174_a.func_147297_a((Packet)position);
/*     */       
/*     */       return;
/*     */     } 
/* 388 */     mc.field_71439_g.func_70016_h(0.0D, 0.0D, 0.0D);
/*     */     
/* 390 */     if (mc.field_71441_e.func_184144_a((Entity)mc.field_71439_g, mc.field_71439_g.func_174813_aQ().func_72321_a(-0.0625D, 0.0D, -0.0625D)).isEmpty()) {
/* 391 */       double ySpeed = 0.0D;
/*     */       
/* 393 */       if (mc.field_71474_y.field_74314_A.func_151470_d()) {
/*     */         
/* 395 */         if (((Boolean)this.noKick.getValue()).booleanValue()) {
/* 396 */           ySpeed = (mc.field_71439_g.field_70173_aa % 20 == 0) ? -0.03999999910593033D : 0.06199999898672104D;
/*     */         } else {
/* 398 */           ySpeed = 0.06199999898672104D;
/*     */         } 
/* 400 */       } else if (mc.field_71474_y.field_74311_E.func_151470_d()) {
/* 401 */         ySpeed = -0.062D;
/*     */       } else {
/* 403 */         ySpeed = mc.field_71441_e.func_184144_a((Entity)mc.field_71439_g, mc.field_71439_g.func_174813_aQ().func_72321_a(-0.0625D, -0.0625D, -0.0625D)).isEmpty() ? ((mc.field_71439_g.field_70173_aa % 4 == 0) ? (((Boolean)this.noKick.getValue()).booleanValue() ? -0.04F : 0.0F) : 0.0D) : 0.0D;
/*     */       } 
/*     */       
/* 406 */       double[] directionalSpeed = MathUtil.directionSpeed(((Float)this.speed.getValue()).floatValue());
/*     */       
/* 408 */       if (mc.field_71474_y.field_74314_A.func_151470_d() || mc.field_71474_y.field_74311_E.func_151470_d() || mc.field_71474_y.field_74351_w.func_151470_d() || mc.field_71474_y.field_74368_y.func_151470_d() || mc.field_71474_y.field_74366_z.func_151470_d() || mc.field_71474_y.field_74370_x.func_151470_d()) {
/* 409 */         if (directionalSpeed[0] != 0.0D || directionalSpeed[1] != 0.0D) {
/* 410 */           if (mc.field_71439_g.field_71158_b.field_78901_c && (mc.field_71439_g.field_70702_br != 0.0F || mc.field_71439_g.field_191988_bg != 0.0F)) {
/* 411 */             mc.field_71439_g.func_70016_h(0.0D, 0.0D, 0.0D);
/* 412 */             move(0.0D, 0.0D, 0.0D);
/* 413 */             for (int i = 0; i <= 3; i++) {
/* 414 */               mc.field_71439_g.func_70016_h(0.0D, ySpeed * i, 0.0D);
/* 415 */               move(0.0D, ySpeed * i, 0.0D);
/*     */             }
/*     */           
/* 418 */           } else if (mc.field_71439_g.field_71158_b.field_78901_c) {
/* 419 */             mc.field_71439_g.func_70016_h(0.0D, 0.0D, 0.0D);
/* 420 */             move(0.0D, 0.0D, 0.0D);
/* 421 */             for (int i = 0; i <= 3; i++) {
/* 422 */               mc.field_71439_g.func_70016_h(0.0D, ySpeed * i, 0.0D);
/* 423 */               move(0.0D, ySpeed * i, 0.0D);
/*     */             } 
/*     */           } else {
/* 426 */             for (int i = 0; i <= 2; i++) {
/* 427 */               mc.field_71439_g.func_70016_h(directionalSpeed[0] * i, ySpeed * i, directionalSpeed[1] * i);
/* 428 */               move(directionalSpeed[0] * i, ySpeed * i, directionalSpeed[1] * i);
/*     */             }
/*     */           
/*     */           }
/*     */         
/*     */         }
/* 434 */       } else if (((Boolean)this.noKick.getValue()).booleanValue() && 
/* 435 */         mc.field_71441_e.func_184144_a((Entity)mc.field_71439_g, mc.field_71439_g.func_174813_aQ().func_72321_a(-0.0625D, -0.0625D, -0.0625D)).isEmpty()) {
/* 436 */         mc.field_71439_g.func_70016_h(0.0D, (mc.field_71439_g.field_70173_aa % 2 == 0) ? 0.03999999910593033D : -0.03999999910593033D, 0.0D);
/* 437 */         move(0.0D, (mc.field_71439_g.field_70173_aa % 2 == 0) ? 0.03999999910593033D : -0.03999999910593033D, 0.0D);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void onUpdate() {
/* 446 */     if (this.mode.getValue() == Mode.SPOOF) {
/* 447 */       if (fullNullCheck()) {
/*     */         return;
/*     */       }
/*     */       
/* 451 */       if (!mc.field_71439_g.field_71075_bZ.field_75101_c) {
/* 452 */         this.flySwitch.disable();
/* 453 */         this.flySwitch.enable();
/* 454 */         mc.field_71439_g.field_71075_bZ.field_75100_b = false;
/*     */       } 
/*     */       
/* 457 */       mc.field_71439_g.field_71075_bZ.func_75092_a(0.05F * ((Float)this.speed.getValue()).floatValue());
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onDisable() {
/* 463 */     if (this.mode.getValue() == Mode.CREATIVE && mc.field_71439_g != null) {
/* 464 */       mc.field_71439_g.field_71075_bZ.field_75100_b = false;
/* 465 */       mc.field_71439_g.field_71075_bZ.func_75092_a(0.05F);
/* 466 */       if (mc.field_71439_g.field_71075_bZ.field_75098_d)
/* 467 */         return;  mc.field_71439_g.field_71075_bZ.field_75101_c = false;
/*     */     } 
/*     */     
/* 470 */     if (this.mode.getValue() == Mode.SPOOF) {
/* 471 */       this.flySwitch.disable();
/*     */     }
/*     */     
/* 474 */     if (this.mode.getValue() == Mode.DAMAGE) {
/* 475 */       Phobos.timerManager.reset();
/* 476 */       mc.field_71439_g.func_70016_h(0.0D, 0.0D, 0.0D);
/* 477 */       this.moveSpeed = Strafe.getBaseMoveSpeed();
/* 478 */       this.lastDist = 0.0D;
/* 479 */       if (((Boolean)this.noClip.getValue()).booleanValue()) {
/* 480 */         mc.field_71439_g.field_70145_X = false;
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public String getDisplayInfo() {
/* 487 */     return this.mode.currentEnumName();
/*     */   }
/*     */ 
/*     */   
/*     */   public void onLogout() {
/* 492 */     if (isOn()) {
/* 493 */       disable();
/*     */     }
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onMove(MoveEvent event) {
/* 499 */     if (event.getStage() == 0 && this.mode.getValue() == Mode.DAMAGE && this.format.getValue() == Format.DAMAGE) {
/* 500 */       double forward = mc.field_71439_g.field_71158_b.field_192832_b;
/* 501 */       double strafe = mc.field_71439_g.field_71158_b.field_78902_a;
/* 502 */       float yaw = mc.field_71439_g.field_70177_z;
/* 503 */       if (forward == 0.0D && strafe == 0.0D) {
/* 504 */         event.setX(0.0D);
/* 505 */         event.setZ(0.0D);
/*     */       } 
/* 507 */       if (forward != 0.0D && strafe != 0.0D) {
/* 508 */         forward *= Math.sin(0.7853981633974483D);
/* 509 */         strafe *= Math.cos(0.7853981633974483D);
/*     */       } 
/* 511 */       if (this.level != 1 || (mc.field_71439_g.field_191988_bg == 0.0F && mc.field_71439_g.field_70702_br == 0.0F)) {
/* 512 */         if (this.level == 2) {
/* 513 */           this.level++;
/*     */         }
/* 515 */         else if (this.level == 3) {
/* 516 */           this.level++;
/* 517 */           double difference = ((mc.field_71439_g.field_70173_aa % 2 == 0) ? -0.05D : 0.1D) * (this.lastDist - Strafe.getBaseMoveSpeed());
/* 518 */           this.moveSpeed = this.lastDist - difference;
/*     */         } else {
/*     */           
/* 521 */           if (mc.field_71441_e.func_184144_a((Entity)mc.field_71439_g, mc.field_71439_g.func_174813_aQ().func_72317_d(0.0D, mc.field_71439_g.field_70181_x, 0.0D)).size() > 0 || mc.field_71439_g.field_70124_G) {
/* 522 */             this.level = 1;
/*     */           }
/* 524 */           this.moveSpeed = this.lastDist - this.lastDist / 159.0D;
/*     */         } 
/*     */       } else {
/*     */         
/* 528 */         this.level = 2;
/* 529 */         double boost = mc.field_71439_g.func_70644_a(MobEffects.field_76424_c) ? 1.86D : 2.05D;
/* 530 */         this.moveSpeed = boost * Strafe.getBaseMoveSpeed() - 0.01D;
/*     */       } 
/* 532 */       this.moveSpeed = Math.max(this.moveSpeed, Strafe.getBaseMoveSpeed());
/* 533 */       double mx = -Math.sin(Math.toRadians(yaw));
/* 534 */       double mz = Math.cos(Math.toRadians(yaw));
/* 535 */       event.setX(forward * this.moveSpeed * mx + strafe * this.moveSpeed * mz);
/* 536 */       event.setZ(forward * this.moveSpeed * mz - strafe * this.moveSpeed * mx);
/*     */     } 
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onPacketSend(PacketEvent.Send event) {
/* 542 */     if (event.getStage() == 0) {
/* 543 */       if (this.mode.getValue() == Mode.PACKET) {
/* 544 */         if (fullNullCheck()) {
/*     */           return;
/*     */         }
/*     */         
/* 548 */         if (event.getPacket() instanceof CPacketPlayer && !(event.getPacket() instanceof CPacketPlayer.Position)) {
/* 549 */           event.setCanceled(true);
/*     */         }
/* 551 */         if (event.getPacket() instanceof CPacketPlayer) {
/* 552 */           CPacketPlayer packet = (CPacketPlayer)event.getPacket();
/* 553 */           if (this.packets.contains(packet)) {
/* 554 */             this.packets.remove(packet);
/*     */             return;
/*     */           } 
/* 557 */           event.setCanceled(true);
/*     */         } 
/*     */       } 
/*     */       
/* 561 */       if (this.mode.getValue() == Mode.SPOOF) {
/* 562 */         if (fullNullCheck()) {
/*     */           return;
/*     */         }
/*     */         
/* 566 */         if (!((Boolean)this.groundSpoof.getValue()).booleanValue() || !(event.getPacket() instanceof CPacketPlayer) || !mc.field_71439_g.field_71075_bZ.field_75100_b) {
/*     */           return;
/*     */         }
/*     */ 
/*     */         
/* 571 */         CPacketPlayer packet = (CPacketPlayer)event.getPacket();
/* 572 */         if (!packet.field_149480_h) {
/*     */           return;
/*     */         }
/*     */         
/* 576 */         AxisAlignedBB range = mc.field_71439_g.func_174813_aQ().func_72321_a(0.0D, -mc.field_71439_g.field_70163_u, 0.0D).func_191195_a(0.0D, -mc.field_71439_g.field_70131_O, 0.0D);
/* 577 */         List<AxisAlignedBB> collisionBoxes = mc.field_71439_g.field_70170_p.func_184144_a((Entity)mc.field_71439_g, range);
/* 578 */         AtomicReference<Double> newHeight = new AtomicReference<>(Double.valueOf(0.0D));
/* 579 */         collisionBoxes.forEach(box -> newHeight.set(Double.valueOf(Math.max(((Double)newHeight.get()).doubleValue(), box.field_72337_e))));
/*     */         
/* 581 */         packet.field_149477_b = ((Double)newHeight.get()).doubleValue();
/* 582 */         packet.field_149474_g = true;
/*     */       } 
/*     */       
/* 585 */       if (this.mode.getValue() == Mode.DAMAGE && (
/* 586 */         this.format.getValue() == Format.PACKET || this.format.getValue() == Format.DELAY)) {
/* 587 */         if (event.getPacket() instanceof CPacketPlayer && !(event.getPacket() instanceof CPacketPlayer.Position)) {
/* 588 */           event.setCanceled(true);
/*     */         }
/* 590 */         if (event.getPacket() instanceof CPacketPlayer) {
/* 591 */           CPacketPlayer packet = (CPacketPlayer)event.getPacket();
/* 592 */           if (this.packets.contains(packet)) {
/* 593 */             this.packets.remove(packet);
/*     */             return;
/*     */           } 
/* 596 */           event.setCanceled(true);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onPacketReceive(PacketEvent.Receive event) {
/* 605 */     if (event.getStage() == 0) {
/* 606 */       if (this.mode.getValue() == Mode.PACKET) {
/* 607 */         if (fullNullCheck()) {
/*     */           return;
/*     */         }
/* 610 */         if (event.getPacket() instanceof SPacketPlayerPosLook) {
/* 611 */           SPacketPlayerPosLook packet = (SPacketPlayerPosLook)event.getPacket();
/* 612 */           if (mc.field_71439_g.func_70089_S() && mc.field_71441_e.func_175667_e(new BlockPos(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u, mc.field_71439_g.field_70161_v)) && !(mc.field_71462_r instanceof net.minecraft.client.gui.GuiDownloadTerrain)) {
/* 613 */             if (this.teleportId <= 0) {
/* 614 */               this.teleportId = packet.func_186965_f();
/*     */             } else {
/* 616 */               event.setCanceled(true);
/*     */             } 
/*     */           }
/*     */         } 
/*     */       } 
/*     */       
/* 622 */       if (this.mode.getValue() == Mode.SPOOF) {
/* 623 */         if (fullNullCheck()) {
/*     */           return;
/*     */         }
/*     */         
/* 627 */         if (!((Boolean)this.antiGround.getValue()).booleanValue() || !(event.getPacket() instanceof SPacketPlayerPosLook) || !mc.field_71439_g.field_71075_bZ.field_75100_b) {
/*     */           return;
/*     */         }
/*     */         
/* 631 */         SPacketPlayerPosLook packet = (SPacketPlayerPosLook)event.getPacket();
/* 632 */         double oldY = mc.field_71439_g.field_70163_u;
/* 633 */         mc.field_71439_g.func_70107_b(packet.field_148940_a, packet.field_148938_b, packet.field_148939_c);
/*     */         
/* 635 */         AxisAlignedBB range = mc.field_71439_g.func_174813_aQ().func_72321_a(0.0D, (256.0F - mc.field_71439_g.field_70131_O) - mc.field_71439_g.field_70163_u, 0.0D).func_191195_a(0.0D, mc.field_71439_g.field_70131_O, 0.0D);
/* 636 */         List<AxisAlignedBB> collisionBoxes = mc.field_71439_g.field_70170_p.func_184144_a((Entity)mc.field_71439_g, range);
/* 637 */         AtomicReference<Double> newY = new AtomicReference<>(Double.valueOf(256.0D));
/* 638 */         collisionBoxes.forEach(box -> newY.set(Double.valueOf(Math.min(((Double)newY.get()).doubleValue(), box.field_72338_b - mc.field_71439_g.field_70131_O))));
/* 639 */         packet.field_148938_b = Math.min(oldY, ((Double)newY.get()).doubleValue());
/*     */       } 
/*     */       
/* 642 */       if (this.mode.getValue() == Mode.DAMAGE && (
/* 643 */         this.format.getValue() == Format.PACKET || this.format.getValue() == Format.DELAY) && 
/* 644 */         event.getPacket() instanceof SPacketPlayerPosLook) {
/* 645 */         SPacketPlayerPosLook packet = (SPacketPlayerPosLook)event.getPacket();
/* 646 */         if (mc.field_71439_g.func_70089_S() && mc.field_71441_e.func_175667_e(new BlockPos(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u, mc.field_71439_g.field_70161_v)) && !(mc.field_71462_r instanceof net.minecraft.client.gui.GuiDownloadTerrain)) {
/* 647 */           if (this.teleportId <= 0) {
/* 648 */             this.teleportId = packet.func_186965_f();
/*     */           } else {
/* 650 */             event.setCanceled(true);
/*     */           } 
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onSettingChange(ClientEvent event) {
/* 661 */     if (event.getStage() == 2 && 
/* 662 */       event.getSetting() != null && event.getSetting().getFeature() != null && event.getSetting().getFeature().equals(this) && 
/* 663 */       isEnabled() && !event.getSetting().equals(this.enabled)) {
/* 664 */       disable();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onPush(PushEvent event) {
/* 672 */     if (event.getStage() == 1 && this.mode.getValue() == Mode.PACKET && ((Boolean)this.better.getValue()).booleanValue() && ((Boolean)this.phase.getValue()).booleanValue()) {
/* 673 */       event.setCanceled(true);
/*     */     }
/*     */   }
/*     */   
/*     */   private void move(double x, double y, double z) {
/* 678 */     CPacketPlayer.Position position2, position1 = new CPacketPlayer.Position(mc.field_71439_g.field_70165_t + x, mc.field_71439_g.field_70163_u + y, mc.field_71439_g.field_70161_v + z, mc.field_71439_g.field_70122_E);
/* 679 */     this.packets.add(position1);
/* 680 */     mc.field_71439_g.field_71174_a.func_147297_a((Packet)position1);
/*     */ 
/*     */     
/* 683 */     if (((Boolean)this.better.getValue()).booleanValue()) {
/* 684 */       CPacketPlayer bounds = createBoundsPacket(x, y, z);
/*     */     } else {
/* 686 */       position2 = new CPacketPlayer.Position(mc.field_71439_g.field_70165_t + x, 0.0D, mc.field_71439_g.field_70161_v + z, mc.field_71439_g.field_70122_E);
/*     */     } 
/* 688 */     this.packets.add(position2);
/* 689 */     mc.field_71439_g.field_71174_a.func_147297_a((Packet)position2);
/*     */     
/* 691 */     this.teleportId++;
/* 692 */     mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketConfirmTeleport(this.teleportId - 1));
/* 693 */     mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketConfirmTeleport(this.teleportId));
/* 694 */     mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketConfirmTeleport(this.teleportId + 1));
/*     */   }
/*     */   
/*     */   public enum Mode {
/* 698 */     CREATIVE,
/* 699 */     VANILLA,
/* 700 */     PACKET,
/* 701 */     SPOOF,
/* 702 */     DESCEND,
/* 703 */     DAMAGE;
/*     */   }
/*     */   
/*     */   public enum Format {
/* 707 */     DAMAGE,
/* 708 */     SLOW,
/* 709 */     DELAY,
/* 710 */     NORMAL,
/* 711 */     PACKET;
/*     */   }
/*     */   
/*     */   private enum PacketMode {
/* 715 */     Up,
/* 716 */     Down,
/* 717 */     Zero,
/* 718 */     Y,
/* 719 */     X,
/* 720 */     Z,
/* 721 */     XZ;
/*     */   }
/*     */   
/*     */   private CPacketPlayer createBoundsPacket(double x, double y, double z) {
/* 725 */     switch ((PacketMode)this.type.getValue()) { case Up:
/* 726 */         return (CPacketPlayer)new CPacketPlayer.Position(mc.field_71439_g.field_70165_t + x, 10000.0D, mc.field_71439_g.field_70161_v + z, mc.field_71439_g.field_70122_E);
/* 727 */       case Down: return (CPacketPlayer)new CPacketPlayer.Position(mc.field_71439_g.field_70165_t + x, -10000.0D, mc.field_71439_g.field_70161_v + z, mc.field_71439_g.field_70122_E);
/* 728 */       case Zero: return (CPacketPlayer)new CPacketPlayer.Position(mc.field_71439_g.field_70165_t + x, 0.0D, mc.field_71439_g.field_70161_v + z, mc.field_71439_g.field_70122_E);
/* 729 */       case Y: return (CPacketPlayer)new CPacketPlayer.Position(mc.field_71439_g.field_70165_t + x, (mc.field_71439_g.field_70163_u + y <= 10.0D) ? 255.0D : 1.0D, mc.field_71439_g.field_70161_v + z, mc.field_71439_g.field_70122_E);
/* 730 */       case X: return (CPacketPlayer)new CPacketPlayer.Position(mc.field_71439_g.field_70165_t + x + 75.0D, mc.field_71439_g.field_70163_u + y, mc.field_71439_g.field_70161_v + z, mc.field_71439_g.field_70122_E);
/* 731 */       case Z: return (CPacketPlayer)new CPacketPlayer.Position(mc.field_71439_g.field_70165_t + x, mc.field_71439_g.field_70163_u + y, mc.field_71439_g.field_70161_v + z + 75.0D, mc.field_71439_g.field_70122_E);
/* 732 */       case XZ: return (CPacketPlayer)new CPacketPlayer.Position(mc.field_71439_g.field_70165_t + x + 75.0D, mc.field_71439_g.field_70163_u + y, mc.field_71439_g.field_70161_v + z + 75.0D, mc.field_71439_g.field_70122_E); }
/* 733 */      return (CPacketPlayer)new CPacketPlayer.Position(mc.field_71439_g.field_70165_t + x, 2000.0D, mc.field_71439_g.field_70161_v + z, mc.field_71439_g.field_70122_E);
/*     */   }
/*     */   
/*     */   private static class Fly {
/*     */     private Fly() {}
/*     */     
/*     */     protected void enable() {
/* 740 */       Util.mc.func_152344_a(() -> {
/*     */             if (Util.mc.field_71439_g == null || Util.mc.field_71439_g.field_71075_bZ == null) {
/*     */               return;
/*     */             }
/*     */             Util.mc.field_71439_g.field_71075_bZ.field_75101_c = true;
/*     */             Util.mc.field_71439_g.field_71075_bZ.field_75100_b = true;
/*     */           });
/*     */     }
/*     */ 
/*     */     
/*     */     protected void disable() {
/* 751 */       Util.mc.func_152344_a(() -> {
/*     */             if (Util.mc.field_71439_g == null || Util.mc.field_71439_g.field_71075_bZ == null) {
/*     */               return;
/*     */             }
/*     */             
/*     */             PlayerCapabilities gmCaps = new PlayerCapabilities();
/*     */             Util.mc.field_71442_b.func_178889_l().func_77147_a(gmCaps);
/*     */             PlayerCapabilities capabilities = Util.mc.field_71439_g.field_71075_bZ;
/*     */             capabilities.field_75101_c = gmCaps.field_75101_c;
/* 760 */             capabilities.field_75100_b = (gmCaps.field_75101_c && capabilities.field_75100_b);
/*     */             capabilities.func_75092_a(gmCaps.func_75093_a());
/*     */           });
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\me\earth\phobos\features\modules\movement\Flight.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */