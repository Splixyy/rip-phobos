/*     */ package me.earth.phobos.features.modules.player;
/*     */ 
/*     */ import me.earth.phobos.event.events.JesusEvent;
/*     */ import me.earth.phobos.event.events.PacketEvent;
/*     */ import me.earth.phobos.event.events.UpdateWalkingPlayerEvent;
/*     */ import me.earth.phobos.features.modules.Module;
/*     */ import me.earth.phobos.features.setting.Setting;
/*     */ import me.earth.phobos.util.EntityUtil;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.network.play.client.CPacketPlayer;
/*     */ import net.minecraft.util.math.AxisAlignedBB;
/*     */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*     */ 
/*     */ public class Jesus
/*     */   extends Module {
/*  17 */   public Setting<Mode> mode = register(new Setting("Mode", Mode.NORMAL));
/*  18 */   public Setting<Boolean> cancelVehicle = register(new Setting("NoVehicle", Boolean.valueOf(false)));
/*  19 */   public Setting<EventMode> eventMode = register(new Setting("Jump", EventMode.PRE, v -> (this.mode.getValue() == Mode.TRAMPOLINE)));
/*  20 */   public Setting<Boolean> fall = register(new Setting("NoFall", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.TRAMPOLINE)));
/*     */   
/*  22 */   public static AxisAlignedBB offset = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.9999D, 1.0D);
/*  23 */   private static Jesus INSTANCE = new Jesus();
/*     */   private boolean grounded = false;
/*     */   
/*     */   public Jesus() {
/*  27 */     super("Jesus", "Allows you to walk on water", Module.Category.PLAYER, true, false, false);
/*  28 */     INSTANCE = this;
/*     */   }
/*     */   
/*     */   public static Jesus getInstance() {
/*  32 */     if (INSTANCE == null) {
/*  33 */       INSTANCE = new Jesus();
/*     */     }
/*  35 */     return INSTANCE;
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void updateWalkingPlayer(UpdateWalkingPlayerEvent event) {
/*  40 */     if (fullNullCheck() || Freecam.getInstance().isOn()) {
/*     */       return;
/*     */     }
/*     */     
/*  44 */     if (event.getStage() == 0 && (this.mode.getValue() == Mode.BOUNCE || this.mode.getValue() == Mode.VANILLA || this.mode.getValue() == Mode.NORMAL) && 
/*  45 */       !mc.field_71439_g.func_70093_af() && !mc.field_71439_g.field_70145_X && !mc.field_71474_y.field_74314_A.func_151470_d() && EntityUtil.isInLiquid()) {
/*  46 */       mc.field_71439_g.field_70181_x = 0.10000000149011612D;
/*     */     }
/*     */ 
/*     */     
/*  50 */     if (event.getStage() == 0 && this.mode.getValue() == Mode.TRAMPOLINE && (this.eventMode.getValue() == EventMode.ALL || this.eventMode.getValue() == EventMode.PRE)) {
/*  51 */       doTrampoline();
/*  52 */     } else if (event.getStage() == 1 && this.mode.getValue() == Mode.TRAMPOLINE && (this.eventMode.getValue() == EventMode.ALL || this.eventMode.getValue() == EventMode.POST)) {
/*  53 */       doTrampoline();
/*     */     } 
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void sendPacket(PacketEvent.Send event) {
/*  59 */     if (event.getPacket() instanceof CPacketPlayer && Freecam.getInstance().isOff() && (
/*  60 */       this.mode.getValue() == Mode.BOUNCE || this.mode.getValue() == Mode.NORMAL) && mc.field_71439_g.func_184187_bx() == null && !mc.field_71474_y.field_74314_A.func_151470_d()) {
/*  61 */       CPacketPlayer packet = (CPacketPlayer)event.getPacket();
/*  62 */       if (!EntityUtil.isInLiquid() && EntityUtil.isOnLiquid(0.05000000074505806D) && EntityUtil.checkCollide() && mc.field_71439_g.field_70173_aa % 3 == 0) {
/*  63 */         packet.field_149477_b -= 0.05000000074505806D;
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onLiquidCollision(JesusEvent event) {
/*  71 */     if (fullNullCheck() || Freecam.getInstance().isOn()) {
/*     */       return;
/*     */     }
/*     */     
/*  75 */     if (event.getStage() == 0 && (this.mode.getValue() == Mode.BOUNCE || this.mode.getValue() == Mode.VANILLA || this.mode.getValue() == Mode.NORMAL) && 
/*  76 */       mc.field_71441_e != null && mc.field_71439_g != null && 
/*  77 */       EntityUtil.checkCollide() && mc.field_71439_g.field_70181_x < 0.10000000149011612D && event.getPos().func_177956_o() < mc.field_71439_g.field_70163_u - 0.05000000074505806D) {
/*  78 */       if (mc.field_71439_g.func_184187_bx() != null) {
/*  79 */         event.setBoundingBox(new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.949999988079071D, 1.0D));
/*     */       } else {
/*  81 */         event.setBoundingBox(Block.field_185505_j);
/*     */       } 
/*  83 */       event.setCanceled(true);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onPacketReceived(PacketEvent.Receive event) {
/*  91 */     if (((Boolean)this.cancelVehicle.getValue()).booleanValue() && event.getPacket() instanceof net.minecraft.network.play.server.SPacketMoveVehicle) {
/*  92 */       event.setCanceled(true);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public String getDisplayInfo() {
/*  98 */     if (this.mode.getValue() == Mode.NORMAL) {
/*  99 */       return null;
/*     */     }
/* 101 */     return this.mode.currentEnumName();
/*     */   }
/*     */ 
/*     */   
/*     */   private void doTrampoline() {
/* 106 */     if (mc.field_71439_g.func_70093_af()) {
/*     */       return;
/*     */     }
/*     */     
/* 110 */     if (EntityUtil.isAboveLiquid((Entity)mc.field_71439_g) && !mc.field_71439_g.func_70093_af() && !mc.field_71474_y.field_74314_A.field_74513_e) {
/* 111 */       mc.field_71439_g.field_70181_x = 0.1D;
/*     */       
/*     */       return;
/*     */     } 
/* 115 */     if (mc.field_71439_g.field_70122_E || mc.field_71439_g.func_70617_f_()) {
/* 116 */       this.grounded = false;
/*     */     }
/*     */     
/* 119 */     if (mc.field_71439_g.field_70181_x > 0.0D) {
/* 120 */       if (mc.field_71439_g.field_70181_x < 0.03D && this.grounded) {
/* 121 */         mc.field_71439_g.field_70181_x += 0.06713D;
/*     */       }
/* 123 */       else if (mc.field_71439_g.field_70181_x <= 0.05D && this.grounded) {
/* 124 */         mc.field_71439_g.field_70181_x *= 1.20000000999D;
/* 125 */         mc.field_71439_g.field_70181_x += 0.06D;
/*     */       }
/* 127 */       else if (mc.field_71439_g.field_70181_x <= 0.08D && this.grounded) {
/* 128 */         mc.field_71439_g.field_70181_x *= 1.20000003D;
/* 129 */         mc.field_71439_g.field_70181_x += 0.055D;
/*     */       }
/* 131 */       else if (mc.field_71439_g.field_70181_x <= 0.112D && this.grounded) {
/* 132 */         mc.field_71439_g.field_70181_x += 0.0535D;
/*     */       }
/* 134 */       else if (this.grounded) {
/* 135 */         mc.field_71439_g.field_70181_x *= 1.000000000002D;
/* 136 */         mc.field_71439_g.field_70181_x += 0.0517D;
/*     */       } 
/*     */     }
/*     */     
/* 140 */     if (this.grounded && mc.field_71439_g.field_70181_x < 0.0D && mc.field_71439_g.field_70181_x > -0.3D) {
/* 141 */       mc.field_71439_g.field_70181_x += 0.045835D;
/*     */     }
/*     */     
/* 144 */     if (!((Boolean)this.fall.getValue()).booleanValue()) {
/* 145 */       mc.field_71439_g.field_70143_R = 0.0F;
/*     */     }
/*     */     
/* 148 */     if (!EntityUtil.checkForLiquid((Entity)mc.field_71439_g, true)) {
/*     */       return;
/*     */     }
/*     */     
/* 152 */     if (EntityUtil.checkForLiquid((Entity)mc.field_71439_g, true)) {
/* 153 */       mc.field_71439_g.field_70181_x = 0.5D;
/*     */     }
/*     */     
/* 156 */     this.grounded = true;
/*     */   }
/*     */   
/*     */   public enum EventMode {
/* 160 */     PRE,
/* 161 */     POST,
/* 162 */     ALL;
/*     */   }
/*     */   
/*     */   public enum Mode {
/* 166 */     TRAMPOLINE,
/* 167 */     BOUNCE,
/* 168 */     VANILLA,
/* 169 */     NORMAL;
/*     */   }
/*     */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\me\earth\phobos\features\modules\player\Jesus.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */