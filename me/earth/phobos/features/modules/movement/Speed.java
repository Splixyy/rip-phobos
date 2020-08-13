/*     */ package me.earth.phobos.features.modules.movement;
/*     */ 
/*     */ import java.util.Random;
/*     */ import me.earth.phobos.Phobos;
/*     */ import me.earth.phobos.event.events.ClientEvent;
/*     */ import me.earth.phobos.event.events.MoveEvent;
/*     */ import me.earth.phobos.features.modules.Module;
/*     */ import me.earth.phobos.features.setting.Setting;
/*     */ import me.earth.phobos.util.BlockUtil;
/*     */ import me.earth.phobos.util.EntityUtil;
/*     */ import me.earth.phobos.util.MathUtil;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.util.MovementInput;
/*     */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*     */ 
/*     */ public class Speed
/*     */   extends Module
/*     */ {
/*  19 */   public Setting<Mode> mode = register(new Setting("Mode", Mode.INSTANT));
/*  20 */   public Setting<Boolean> strafeJump = register(new Setting("Jump", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.INSTANT)));
/*  21 */   public Setting<Boolean> noShake = register(new Setting("NoShake", Boolean.valueOf(true), v -> (this.mode.getValue() != Mode.INSTANT)));
/*  22 */   public Setting<Boolean> useTimer = register(new Setting("UseTimer", Boolean.valueOf(false), v -> (this.mode.getValue() != Mode.INSTANT)));
/*     */   
/*  24 */   private static Speed INSTANCE = new Speed();
/*     */   
/*  26 */   private double highChainVal = 0.0D;
/*  27 */   private double lowChainVal = 0.0D;
/*     */   private boolean oneTime = false;
/*  29 */   public double startY = 0.0D;
/*     */   public boolean antiShake = false;
/*  31 */   private double bounceHeight = 0.4D;
/*  32 */   private float move = 0.26F;
/*     */   
/*     */   public Speed() {
/*  35 */     super("Speed", "Makes you faster", Module.Category.MOVEMENT, true, false, false);
/*  36 */     setInstance();
/*     */   }
/*     */   
/*     */   private void setInstance() {
/*  40 */     INSTANCE = this;
/*     */   }
/*     */   
/*     */   public static Speed getInstance() {
/*  44 */     if (INSTANCE == null) {
/*  45 */       INSTANCE = new Speed();
/*     */     }
/*  47 */     return INSTANCE;
/*     */   }
/*     */   
/*     */   public enum Mode {
/*  51 */     INSTANT,
/*  52 */     ONGROUND,
/*  53 */     ACCEL,
/*  54 */     BOOST;
/*     */   }
/*     */   
/*     */   private boolean shouldReturn() {
/*  58 */     return (Phobos.moduleManager.isModuleEnabled("Freecam") || Phobos.moduleManager.isModuleEnabled("Phase") || Phobos.moduleManager.isModuleEnabled("ElytraFlight") || Phobos.moduleManager.isModuleEnabled("Strafe") || Phobos.moduleManager.isModuleEnabled("Flight"));
/*     */   }
/*     */ 
/*     */   
/*     */   public void onUpdate() {
/*  63 */     if (shouldReturn() || mc.field_71439_g.func_70093_af() || mc.field_71439_g.func_70090_H() || mc.field_71439_g.func_180799_ab()) {
/*     */       return;
/*     */     }
/*     */     
/*  67 */     switch ((Mode)this.mode.getValue()) {
/*     */       case BOOST:
/*  69 */         doBoost();
/*     */         break;
/*     */       case ACCEL:
/*  72 */         doAccel();
/*     */         break;
/*     */       case ONGROUND:
/*  75 */         doOnground();
/*     */         break;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void doBoost() {
/*  82 */     this.bounceHeight = 0.4D;
/*  83 */     this.move = 0.26F;
/*  84 */     if (mc.field_71439_g.field_70122_E) {
/*  85 */       this.startY = mc.field_71439_g.field_70163_u;
/*     */     }
/*     */     
/*  88 */     if (EntityUtil.getEntitySpeed((Entity)mc.field_71439_g) <= 1.0D) {
/*  89 */       this.lowChainVal = 1.0D;
/*  90 */       this.highChainVal = 1.0D;
/*     */     } 
/*     */     
/*  93 */     if (EntityUtil.isEntityMoving((Entity)mc.field_71439_g) && !mc.field_71439_g.field_70123_F && !BlockUtil.isBlockAboveEntitySolid((Entity)mc.field_71439_g) && BlockUtil.isBlockBelowEntitySolid((Entity)mc.field_71439_g)) {
/*  94 */       this.oneTime = true;
/*  95 */       this.antiShake = (((Boolean)this.noShake.getValue()).booleanValue() && mc.field_71439_g.func_184187_bx() == null);
/*  96 */       Random random = new Random();
/*  97 */       boolean rnd = random.nextBoolean();
/*  98 */       if (mc.field_71439_g.field_70163_u >= this.startY + this.bounceHeight) {
/*  99 */         mc.field_71439_g.field_70181_x = -this.bounceHeight;
/* 100 */         this.lowChainVal++;
/* 101 */         if (this.lowChainVal == 1.0D) {
/* 102 */           this.move = 0.075F;
/*     */         }
/* 104 */         if (this.lowChainVal == 2.0D) {
/* 105 */           this.move = 0.15F;
/*     */         }
/* 107 */         if (this.lowChainVal == 3.0D) {
/* 108 */           this.move = 0.175F;
/*     */         }
/* 110 */         if (this.lowChainVal == 4.0D) {
/* 111 */           this.move = 0.2F;
/*     */         }
/* 113 */         if (this.lowChainVal == 5.0D) {
/* 114 */           this.move = 0.225F;
/*     */         }
/* 116 */         if (this.lowChainVal == 6.0D) {
/* 117 */           this.move = 0.25F;
/*     */         }
/* 119 */         if (this.lowChainVal >= 7.0D) {
/* 120 */           this.move = 0.27895F;
/*     */         }
/* 122 */         if (((Boolean)this.useTimer.getValue()).booleanValue()) {
/* 123 */           Phobos.timerManager.setTimer(1.0F);
/*     */         }
/*     */       } 
/* 126 */       if (mc.field_71439_g.field_70163_u == this.startY) {
/* 127 */         mc.field_71439_g.field_70181_x = this.bounceHeight;
/* 128 */         this.highChainVal++;
/* 129 */         if (this.highChainVal == 1.0D) {
/* 130 */           this.move = 0.075F;
/*     */         }
/* 132 */         if (this.highChainVal == 2.0D) {
/* 133 */           this.move = 0.175F;
/*     */         }
/* 135 */         if (this.highChainVal == 3.0D) {
/* 136 */           this.move = 0.325F;
/*     */         }
/* 138 */         if (this.highChainVal == 4.0D) {
/* 139 */           this.move = 0.375F;
/*     */         }
/* 141 */         if (this.highChainVal == 5.0D) {
/* 142 */           this.move = 0.4F;
/*     */         }
/* 144 */         if (this.highChainVal >= 6.0D) {
/* 145 */           this.move = 0.43395F;
/*     */         }
/* 147 */         if (((Boolean)this.useTimer.getValue()).booleanValue()) {
/* 148 */           if (rnd) {
/* 149 */             Phobos.timerManager.setTimer(1.3F);
/*     */           } else {
/* 151 */             Phobos.timerManager.setTimer(1.0F);
/*     */           } 
/*     */         }
/*     */       } 
/* 155 */       EntityUtil.moveEntityStrafe(this.move, (Entity)mc.field_71439_g);
/*     */     } else {
/* 157 */       if (this.oneTime) {
/* 158 */         mc.field_71439_g.field_70181_x = -0.1D;
/* 159 */         this.oneTime = false;
/*     */       } 
/* 161 */       this.highChainVal = 0.0D;
/* 162 */       this.lowChainVal = 0.0D;
/* 163 */       this.antiShake = false;
/* 164 */       speedOff();
/*     */     } 
/*     */   }
/*     */   
/*     */   private void doAccel() {
/* 169 */     this.bounceHeight = 0.4D;
/* 170 */     this.move = 0.26F;
/* 171 */     if (mc.field_71439_g.field_70122_E) {
/* 172 */       this.startY = mc.field_71439_g.field_70163_u;
/*     */     }
/* 174 */     if (EntityUtil.getEntitySpeed((Entity)mc.field_71439_g) <= 1.0D) {
/* 175 */       this.lowChainVal = 1.0D;
/* 176 */       this.highChainVal = 1.0D;
/*     */     } 
/* 178 */     if (EntityUtil.isEntityMoving((Entity)mc.field_71439_g) && !mc.field_71439_g.field_70123_F && !BlockUtil.isBlockAboveEntitySolid((Entity)mc.field_71439_g) && BlockUtil.isBlockBelowEntitySolid((Entity)mc.field_71439_g)) {
/* 179 */       this.oneTime = true;
/* 180 */       this.antiShake = (((Boolean)this.noShake.getValue()).booleanValue() && mc.field_71439_g.func_184187_bx() == null);
/* 181 */       Random random = new Random();
/* 182 */       boolean rnd = random.nextBoolean();
/* 183 */       if (mc.field_71439_g.field_70163_u >= this.startY + this.bounceHeight) {
/* 184 */         mc.field_71439_g.field_70181_x = -this.bounceHeight;
/* 185 */         this.lowChainVal++;
/* 186 */         if (this.lowChainVal == 1.0D) {
/* 187 */           this.move = 0.075F;
/*     */         }
/* 189 */         if (this.lowChainVal == 2.0D) {
/* 190 */           this.move = 0.175F;
/*     */         }
/* 192 */         if (this.lowChainVal == 3.0D) {
/* 193 */           this.move = 0.275F;
/*     */         }
/* 195 */         if (this.lowChainVal == 4.0D) {
/* 196 */           this.move = 0.35F;
/*     */         }
/* 198 */         if (this.lowChainVal == 5.0D) {
/* 199 */           this.move = 0.375F;
/*     */         }
/* 201 */         if (this.lowChainVal == 6.0D) {
/* 202 */           this.move = 0.4F;
/*     */         }
/* 204 */         if (this.lowChainVal == 7.0D) {
/* 205 */           this.move = 0.425F;
/*     */         }
/* 207 */         if (this.lowChainVal == 8.0D) {
/* 208 */           this.move = 0.45F;
/*     */         }
/* 210 */         if (this.lowChainVal == 9.0D) {
/* 211 */           this.move = 0.475F;
/*     */         }
/* 213 */         if (this.lowChainVal == 10.0D) {
/* 214 */           this.move = 0.5F;
/*     */         }
/* 216 */         if (this.lowChainVal == 11.0D) {
/* 217 */           this.move = 0.5F;
/*     */         }
/* 219 */         if (this.lowChainVal == 12.0D) {
/* 220 */           this.move = 0.525F;
/*     */         }
/* 222 */         if (this.lowChainVal == 13.0D) {
/* 223 */           this.move = 0.525F;
/*     */         }
/* 225 */         if (this.lowChainVal == 14.0D) {
/* 226 */           this.move = 0.535F;
/*     */         }
/* 228 */         if (this.lowChainVal == 15.0D) {
/* 229 */           this.move = 0.535F;
/*     */         }
/* 231 */         if (this.lowChainVal == 16.0D) {
/* 232 */           this.move = 0.545F;
/*     */         }
/* 234 */         if (this.lowChainVal >= 17.0D) {
/* 235 */           this.move = 0.545F;
/*     */         }
/* 237 */         if (((Boolean)this.useTimer.getValue()).booleanValue()) {
/* 238 */           Phobos.timerManager.setTimer(1.0F);
/*     */         }
/*     */       } 
/* 241 */       if (mc.field_71439_g.field_70163_u == this.startY) {
/* 242 */         mc.field_71439_g.field_70181_x = this.bounceHeight;
/* 243 */         this.highChainVal++;
/* 244 */         if (this.highChainVal == 1.0D) {
/* 245 */           this.move = 0.075F;
/*     */         }
/* 247 */         if (this.highChainVal == 2.0D) {
/* 248 */           this.move = 0.175F;
/*     */         }
/* 250 */         if (this.highChainVal == 3.0D) {
/* 251 */           this.move = 0.375F;
/*     */         }
/* 253 */         if (this.highChainVal == 4.0D) {
/* 254 */           this.move = 0.6F;
/*     */         }
/* 256 */         if (this.highChainVal == 5.0D) {
/* 257 */           this.move = 0.775F;
/*     */         }
/* 259 */         if (this.highChainVal == 6.0D) {
/* 260 */           this.move = 0.825F;
/*     */         }
/* 262 */         if (this.highChainVal == 7.0D) {
/* 263 */           this.move = 0.875F;
/*     */         }
/* 265 */         if (this.highChainVal == 8.0D) {
/* 266 */           this.move = 0.925F;
/*     */         }
/* 268 */         if (this.highChainVal == 9.0D) {
/* 269 */           this.move = 0.975F;
/*     */         }
/* 271 */         if (this.highChainVal == 10.0D) {
/* 272 */           this.move = 1.05F;
/*     */         }
/* 274 */         if (this.highChainVal == 11.0D) {
/* 275 */           this.move = 1.1F;
/*     */         }
/* 277 */         if (this.highChainVal == 12.0D) {
/* 278 */           this.move = 1.1F;
/*     */         }
/* 280 */         if (this.highChainVal == 13.0D) {
/* 281 */           this.move = 1.15F;
/*     */         }
/* 283 */         if (this.highChainVal == 14.0D) {
/* 284 */           this.move = 1.15F;
/*     */         }
/* 286 */         if (this.highChainVal == 15.0D) {
/* 287 */           this.move = 1.175F;
/*     */         }
/* 289 */         if (this.highChainVal == 16.0D) {
/* 290 */           this.move = 1.175F;
/*     */         }
/* 292 */         if (this.highChainVal >= 17.0D) {
/* 293 */           this.move = 1.175F;
/*     */         }
/* 295 */         if (((Boolean)this.useTimer.getValue()).booleanValue()) {
/* 296 */           if (rnd) {
/* 297 */             Phobos.timerManager.setTimer(1.3F);
/*     */           } else {
/* 299 */             Phobos.timerManager.setTimer(1.0F);
/*     */           } 
/*     */         }
/*     */       } 
/* 303 */       EntityUtil.moveEntityStrafe(this.move, (Entity)mc.field_71439_g);
/*     */     } else {
/*     */       
/* 306 */       if (this.oneTime) {
/* 307 */         mc.field_71439_g.field_70181_x = -0.1D;
/* 308 */         this.oneTime = false;
/*     */       } 
/* 310 */       this.antiShake = false;
/* 311 */       this.highChainVal = 0.0D;
/* 312 */       this.lowChainVal = 0.0D;
/* 313 */       speedOff();
/*     */     } 
/*     */   }
/*     */   
/*     */   private void doOnground() {
/* 318 */     this.bounceHeight = 0.4D;
/* 319 */     this.move = 0.26F;
/* 320 */     if (mc.field_71439_g.field_70122_E) {
/* 321 */       this.startY = mc.field_71439_g.field_70163_u;
/*     */     }
/* 323 */     if (EntityUtil.getEntitySpeed((Entity)mc.field_71439_g) <= 1.0D) {
/* 324 */       this.lowChainVal = 1.0D;
/* 325 */       this.highChainVal = 1.0D;
/*     */     } 
/* 327 */     if (EntityUtil.isEntityMoving((Entity)mc.field_71439_g) && !mc.field_71439_g.field_70123_F && !BlockUtil.isBlockAboveEntitySolid((Entity)mc.field_71439_g) && BlockUtil.isBlockBelowEntitySolid((Entity)mc.field_71439_g)) {
/* 328 */       this.oneTime = true;
/* 329 */       this.antiShake = (((Boolean)this.noShake.getValue()).booleanValue() && mc.field_71439_g.func_184187_bx() == null);
/* 330 */       Random random = new Random();
/* 331 */       boolean rnd = random.nextBoolean();
/* 332 */       if (mc.field_71439_g.field_70163_u >= this.startY + this.bounceHeight) {
/* 333 */         mc.field_71439_g.field_70181_x = -this.bounceHeight;
/* 334 */         this.lowChainVal++;
/* 335 */         if (this.lowChainVal == 1.0D) {
/* 336 */           this.move = 0.075F;
/*     */         }
/* 338 */         if (this.lowChainVal == 2.0D) {
/* 339 */           this.move = 0.175F;
/*     */         }
/* 341 */         if (this.lowChainVal == 3.0D) {
/* 342 */           this.move = 0.275F;
/*     */         }
/* 344 */         if (this.lowChainVal == 4.0D) {
/* 345 */           this.move = 0.35F;
/*     */         }
/* 347 */         if (this.lowChainVal == 5.0D) {
/* 348 */           this.move = 0.375F;
/*     */         }
/* 350 */         if (this.lowChainVal == 6.0D) {
/* 351 */           this.move = 0.4F;
/*     */         }
/* 353 */         if (this.lowChainVal == 7.0D) {
/* 354 */           this.move = 0.425F;
/*     */         }
/* 356 */         if (this.lowChainVal == 8.0D) {
/* 357 */           this.move = 0.45F;
/*     */         }
/* 359 */         if (this.lowChainVal == 9.0D) {
/* 360 */           this.move = 0.475F;
/*     */         }
/* 362 */         if (this.lowChainVal == 10.0D) {
/* 363 */           this.move = 0.5F;
/*     */         }
/* 365 */         if (this.lowChainVal == 11.0D) {
/* 366 */           this.move = 0.5F;
/*     */         }
/* 368 */         if (this.lowChainVal == 12.0D) {
/* 369 */           this.move = 0.525F;
/*     */         }
/* 371 */         if (this.lowChainVal == 13.0D) {
/* 372 */           this.move = 0.525F;
/*     */         }
/* 374 */         if (this.lowChainVal == 14.0D) {
/* 375 */           this.move = 0.535F;
/*     */         }
/* 377 */         if (this.lowChainVal == 15.0D) {
/* 378 */           this.move = 0.535F;
/*     */         }
/* 380 */         if (this.lowChainVal == 16.0D) {
/* 381 */           this.move = 0.545F;
/*     */         }
/* 383 */         if (this.lowChainVal >= 17.0D) {
/* 384 */           this.move = 0.545F;
/*     */         }
/* 386 */         if (((Boolean)this.useTimer.getValue()).booleanValue()) {
/* 387 */           Phobos.timerManager.setTimer(1.0F);
/*     */         }
/*     */       } 
/* 390 */       if (mc.field_71439_g.field_70163_u == this.startY) {
/* 391 */         mc.field_71439_g.field_70181_x = this.bounceHeight;
/* 392 */         this.highChainVal++;
/* 393 */         if (this.highChainVal == 1.0D) {
/* 394 */           this.move = 0.075F;
/*     */         }
/* 396 */         if (this.highChainVal == 2.0D) {
/* 397 */           this.move = 0.175F;
/*     */         }
/* 399 */         if (this.highChainVal == 3.0D) {
/* 400 */           this.move = 0.375F;
/*     */         }
/* 402 */         if (this.highChainVal == 4.0D) {
/* 403 */           this.move = 0.6F;
/*     */         }
/* 405 */         if (this.highChainVal == 5.0D) {
/* 406 */           this.move = 0.775F;
/*     */         }
/* 408 */         if (this.highChainVal == 6.0D) {
/* 409 */           this.move = 0.825F;
/*     */         }
/* 411 */         if (this.highChainVal == 7.0D) {
/* 412 */           this.move = 0.875F;
/*     */         }
/* 414 */         if (this.highChainVal == 8.0D) {
/* 415 */           this.move = 0.925F;
/*     */         }
/* 417 */         if (this.highChainVal == 9.0D) {
/* 418 */           this.move = 0.975F;
/*     */         }
/* 420 */         if (this.highChainVal == 10.0D) {
/* 421 */           this.move = 1.05F;
/*     */         }
/* 423 */         if (this.highChainVal == 11.0D) {
/* 424 */           this.move = 1.1F;
/*     */         }
/* 426 */         if (this.highChainVal == 12.0D) {
/* 427 */           this.move = 1.1F;
/*     */         }
/* 429 */         if (this.highChainVal == 13.0D) {
/* 430 */           this.move = 1.15F;
/*     */         }
/* 432 */         if (this.highChainVal == 14.0D) {
/* 433 */           this.move = 1.15F;
/*     */         }
/* 435 */         if (this.highChainVal == 15.0D) {
/* 436 */           this.move = 1.175F;
/*     */         }
/* 438 */         if (this.highChainVal == 16.0D) {
/* 439 */           this.move = 1.175F;
/*     */         }
/* 441 */         if (this.highChainVal >= 17.0D) {
/* 442 */           this.move = 1.2F;
/*     */         }
/* 444 */         if (((Boolean)this.useTimer.getValue()).booleanValue()) {
/* 445 */           if (rnd) {
/* 446 */             Phobos.timerManager.setTimer(1.3F);
/*     */           } else {
/* 448 */             Phobos.timerManager.setTimer(1.0F);
/*     */           } 
/*     */         }
/*     */       } 
/* 452 */       EntityUtil.moveEntityStrafe(this.move, (Entity)mc.field_71439_g);
/*     */     } else {
/*     */       
/* 455 */       if (this.oneTime) {
/* 456 */         mc.field_71439_g.field_70181_x = -0.1D;
/* 457 */         this.oneTime = false;
/*     */       } 
/* 459 */       this.antiShake = false;
/* 460 */       this.highChainVal = 0.0D;
/* 461 */       this.lowChainVal = 0.0D;
/* 462 */       speedOff();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onDisable() {
/* 468 */     if (this.mode.getValue() == Mode.ONGROUND || this.mode.getValue() == Mode.BOOST) {
/* 469 */       mc.field_71439_g.field_70181_x = -0.1D;
/*     */     }
/* 471 */     Phobos.timerManager.setTimer(1.0F);
/* 472 */     this.highChainVal = 0.0D;
/* 473 */     this.lowChainVal = 0.0D;
/* 474 */     this.antiShake = false;
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onSettingChange(ClientEvent event) {
/* 479 */     if (event.getStage() == 2 && 
/* 480 */       event.getSetting().equals(this.mode) && 
/* 481 */       this.mode.getPlannedValue() == Mode.INSTANT) {
/* 482 */       mc.field_71439_g.field_70181_x = -0.1D;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getDisplayInfo() {
/* 490 */     return this.mode.currentEnumName();
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onMode(MoveEvent event) {
/* 495 */     if (!shouldReturn() && event.getStage() == 0 && this.mode.getValue() == Mode.INSTANT && !nullCheck() && !mc.field_71439_g.func_70093_af() && !mc.field_71439_g.func_70090_H() && !mc.field_71439_g.func_180799_ab() && (
/* 496 */       mc.field_71439_g.field_71158_b.field_192832_b != 0.0F || mc.field_71439_g.field_71158_b.field_78902_a != 0.0F)) {
/* 497 */       if (mc.field_71439_g.field_70122_E && ((Boolean)this.strafeJump.getValue()).booleanValue()) {
/* 498 */         mc.field_71439_g.field_70181_x = 0.4D;
/* 499 */         event.setY(0.4D);
/*     */       } 
/* 501 */       MovementInput movementInput = mc.field_71439_g.field_71158_b;
/* 502 */       float moveForward = movementInput.field_192832_b;
/* 503 */       float moveStrafe = movementInput.field_78902_a;
/* 504 */       float rotationYaw = mc.field_71439_g.field_70177_z;
/*     */       
/* 506 */       if (moveForward == 0.0D && moveStrafe == 0.0D) {
/* 507 */         event.setX(0.0D);
/* 508 */         event.setZ(0.0D);
/*     */       } else {
/* 510 */         if (moveForward != 0.0D) {
/* 511 */           if (moveStrafe > 0.0D) {
/* 512 */             rotationYaw += ((moveForward > 0.0D) ? -45 : 45);
/* 513 */           } else if (moveStrafe < 0.0D) {
/* 514 */             rotationYaw += ((moveForward > 0.0D) ? 45 : -45);
/*     */           } 
/* 516 */           moveStrafe = 0.0F;
/* 517 */           moveForward = (moveForward == 0.0F) ? moveForward : ((moveForward > 0.0D) ? 1.0F : -1.0F);
/*     */         } 
/* 519 */         moveStrafe = (moveStrafe == 0.0F) ? moveStrafe : ((moveStrafe > 0.0D) ? 1.0F : -1.0F);
/*     */         
/* 521 */         event.setX(moveForward * EntityUtil.getMaxSpeed() * Math.cos(Math.toRadians((rotationYaw + 90.0F))) + moveStrafe * EntityUtil.getMaxSpeed() * Math.sin(Math.toRadians((rotationYaw + 90.0F))));
/* 522 */         event.setZ(moveForward * EntityUtil.getMaxSpeed() * Math.sin(Math.toRadians((rotationYaw + 90.0F))) - moveStrafe * EntityUtil.getMaxSpeed() * Math.cos(Math.toRadians((rotationYaw + 90.0F))));
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void speedOff() {
/* 529 */     float yaw = (float)Math.toRadians(mc.field_71439_g.field_70177_z);
/* 530 */     if (BlockUtil.isBlockAboveEntitySolid((Entity)mc.field_71439_g)) {
/* 531 */       if (mc.field_71474_y.field_74351_w.func_151470_d() && !mc.field_71474_y.field_74311_E.func_151470_d() && mc.field_71439_g.field_70122_E) {
/* 532 */         mc.field_71439_g.field_70159_w -= MathUtil.sin(yaw) * 0.15D;
/* 533 */         mc.field_71439_g.field_70179_y += MathUtil.cos(yaw) * 0.15D;
/*     */       } 
/* 535 */     } else if (mc.field_71439_g.field_70123_F) {
/* 536 */       if (mc.field_71474_y.field_74351_w.func_151470_d() && !mc.field_71474_y.field_74311_E.func_151470_d() && mc.field_71439_g.field_70122_E) {
/* 537 */         mc.field_71439_g.field_70159_w -= MathUtil.sin(yaw) * 0.03D;
/* 538 */         mc.field_71439_g.field_70179_y += MathUtil.cos(yaw) * 0.03D;
/*     */       } 
/* 540 */     } else if (!BlockUtil.isBlockBelowEntitySolid((Entity)mc.field_71439_g)) {
/* 541 */       if (mc.field_71474_y.field_74351_w.func_151470_d() && !mc.field_71474_y.field_74311_E.func_151470_d() && mc.field_71439_g.field_70122_E) {
/* 542 */         mc.field_71439_g.field_70159_w -= MathUtil.sin(yaw) * 0.03D;
/* 543 */         mc.field_71439_g.field_70179_y += MathUtil.cos(yaw) * 0.03D;
/*     */       } 
/*     */     } else {
/* 546 */       mc.field_71439_g.field_70159_w = 0.0D;
/* 547 */       mc.field_71439_g.field_70179_y = 0.0D;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\me\earth\phobos\features\modules\movement\Speed.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */