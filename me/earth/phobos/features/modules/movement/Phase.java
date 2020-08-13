/*     */ package me.earth.phobos.features.modules.movement;
/*     */ 
/*     */ import me.earth.phobos.event.events.MoveEvent;
/*     */ import me.earth.phobos.event.events.PacketEvent;
/*     */ import me.earth.phobos.event.events.PushEvent;
/*     */ import me.earth.phobos.event.events.UpdateWalkingPlayerEvent;
/*     */ import me.earth.phobos.features.modules.Module;
/*     */ import me.earth.phobos.features.setting.Setting;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.network.Packet;
/*     */ import net.minecraft.network.play.client.CPacketConfirmTeleport;
/*     */ import net.minecraft.network.play.client.CPacketEntityAction;
/*     */ import net.minecraft.network.play.client.CPacketPlayer;
/*     */ import net.minecraft.network.play.server.SPacketPlayerPosLook;
/*     */ import net.minecraft.util.math.BlockPos;
/*     */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*     */ 
/*     */ public class Phase extends Module {
/*  19 */   public Setting<Mode> mode = register(new Setting("Mode", Mode.PACKETFLY));
/*  20 */   public Setting<PacketFlyMode> type = register(new Setting("Type", PacketFlyMode.SETBACK, v -> (this.mode.getValue() == Mode.PACKETFLY)));
/*     */   
/*  22 */   public Setting<Integer> xMove = register(new Setting("HMove", Integer.valueOf(625), Integer.valueOf(1), Integer.valueOf(1000), v -> (this.mode.getValue() == Mode.PACKETFLY && this.type.getValue() == PacketFlyMode.SETBACK), "XMovement speed."));
/*  23 */   public Setting<Integer> yMove = register(new Setting("YMove", Integer.valueOf(625), Integer.valueOf(1), Integer.valueOf(1000), v -> (this.mode.getValue() == Mode.PACKETFLY && this.type.getValue() == PacketFlyMode.SETBACK), "YMovement speed."));
/*     */   
/*  25 */   public Setting<Boolean> extra = register(new Setting("ExtraPacket", Boolean.valueOf(true), v -> (this.mode.getValue() == Mode.PACKETFLY && this.type.getValue() == PacketFlyMode.SETBACK)));
/*  26 */   public Setting<Integer> offset = register(new Setting("Offset", Integer.valueOf(1337), Integer.valueOf(-1337), Integer.valueOf(1337), v -> (this.mode.getValue() == Mode.PACKETFLY && this.type.getValue() == PacketFlyMode.SETBACK && ((Boolean)this.extra.getValue()).booleanValue()), "Up speed."));
/*  27 */   public Setting<Boolean> fallPacket = register(new Setting("FallPacket", Boolean.valueOf(true), v -> (this.mode.getValue() == Mode.PACKETFLY && this.type.getValue() == PacketFlyMode.SETBACK)));
/*  28 */   public Setting<Boolean> teleporter = register(new Setting("Teleport", Boolean.valueOf(true), v -> (this.mode.getValue() == Mode.PACKETFLY && this.type.getValue() == PacketFlyMode.SETBACK)));
/*  29 */   public Setting<Boolean> boundingBox = register(new Setting("BoundingBox", Boolean.valueOf(true), v -> (this.mode.getValue() == Mode.PACKETFLY && this.type.getValue() == PacketFlyMode.SETBACK)));
/*  30 */   public Setting<Integer> teleportConfirm = register(new Setting("Confirm", Integer.valueOf(2), Integer.valueOf(0), Integer.valueOf(4), v -> (this.mode.getValue() == Mode.PACKETFLY && this.type.getValue() == PacketFlyMode.SETBACK)));
/*  31 */   public Setting<Boolean> ultraPacket = register(new Setting("DoublePacket", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.PACKETFLY && this.type.getValue() == PacketFlyMode.SETBACK)));
/*  32 */   public Setting<Boolean> updates = register(new Setting("Update", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.PACKETFLY && this.type.getValue() == PacketFlyMode.SETBACK)));
/*  33 */   public Setting<Boolean> resetConfirm = register(new Setting("Reset", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.PACKETFLY && this.type.getValue() == PacketFlyMode.SETBACK)));
/*  34 */   public Setting<Boolean> posLook = register(new Setting("PosLook", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.PACKETFLY && this.type.getValue() == PacketFlyMode.SETBACK)));
/*  35 */   public Setting<Boolean> cancel = register(new Setting("Cancel", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.PACKETFLY && this.type.getValue() == PacketFlyMode.SETBACK && ((Boolean)this.posLook.getValue()).booleanValue())));
/*  36 */   public Setting<Boolean> onlyY = register(new Setting("OnlyY", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.PACKETFLY && this.type.getValue() == PacketFlyMode.SETBACK && ((Boolean)this.posLook.getValue()).booleanValue())));
/*  37 */   public Setting<Integer> cancelPacket = register(new Setting("Packets", Integer.valueOf(20), Integer.valueOf(0), Integer.valueOf(20), v -> (this.mode.getValue() == Mode.PACKETFLY && this.type.getValue() == PacketFlyMode.SETBACK && ((Boolean)this.posLook.getValue()).booleanValue())));
/*     */ 
/*     */   
/*  40 */   private static Phase INSTANCE = new Phase();
/*     */   private boolean teleport = true;
/*  42 */   private int teleportIds = 0;
/*     */   private int posLookPackets;
/*     */   
/*     */   public Phase() {
/*  46 */     super("Phase", "Makes you able to phase through blocks.", Module.Category.MOVEMENT, true, false, false);
/*  47 */     setInstance();
/*     */   }
/*     */   
/*     */   private void setInstance() {
/*  51 */     INSTANCE = this;
/*     */   }
/*     */   
/*     */   public static Phase getInstance() {
/*  55 */     if (INSTANCE == null) {
/*  56 */       INSTANCE = new Phase();
/*     */     }
/*  58 */     return INSTANCE;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onDisable() {
/*  63 */     this.posLookPackets = 0;
/*  64 */     if (mc.field_71439_g != null) {
/*  65 */       if (((Boolean)this.resetConfirm.getValue()).booleanValue()) {
/*  66 */         this.teleportIds = 0;
/*     */       }
/*  68 */       mc.field_71439_g.field_70145_X = false;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public String getDisplayInfo() {
/*  74 */     return this.mode.currentEnumName();
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onMove(MoveEvent event) {
/*  79 */     if (this.type.getValue() == PacketFlyMode.NONE || event.getStage() != 0 || mc.func_71356_B() || this.mode.getValue() != Mode.PACKETFLY) {
/*     */       return;
/*     */     }
/*     */     
/*  83 */     if (!((Boolean)this.boundingBox.getValue()).booleanValue() && !((Boolean)this.updates.getValue()).booleanValue()) {
/*  84 */       doPhase(event);
/*     */     }
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onPush(PushEvent event) {
/*  90 */     if (event.getStage() == 1 && this.type.getValue() != PacketFlyMode.NONE) {
/*  91 */       event.setCanceled(true);
/*     */     }
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onMove(UpdateWalkingPlayerEvent event) {
/*  97 */     if (fullNullCheck() || event.getStage() != 0 || this.type.getValue() != PacketFlyMode.SETBACK || this.mode.getValue() != Mode.PACKETFLY) {
/*     */       return;
/*     */     }
/*     */     
/* 101 */     if (((Boolean)this.boundingBox.getValue()).booleanValue()) {
/* 102 */       doBoundingBox();
/*     */     }
/* 104 */     else if (((Boolean)this.updates.getValue()).booleanValue()) {
/* 105 */       doPhase((MoveEvent)null);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void doPhase(MoveEvent event) {
/* 111 */     if (this.type.getValue() == PacketFlyMode.SETBACK && 
/* 112 */       !((Boolean)this.boundingBox.getValue()).booleanValue()) {
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 117 */       double[] dirSpeed = getMotion(this.teleport ? (((Integer)this.yMove.getValue()).intValue() / 10000.0D) : ((((Integer)this.yMove.getValue()).intValue() - 1) / 10000.0D));
/* 118 */       double posX = mc.field_71439_g.field_70165_t + dirSpeed[0];
/* 119 */       double posY = mc.field_71439_g.field_70163_u + (mc.field_71474_y.field_74314_A.func_151470_d() ? (this.teleport ? (((Integer)this.yMove.getValue()).intValue() / 10000.0D) : ((((Integer)this.yMove.getValue()).intValue() - 1) / 10000.0D)) : 1.0E-8D) - (mc.field_71474_y.field_74311_E.func_151470_d() ? (this.teleport ? (((Integer)this.yMove.getValue()).intValue() / 10000.0D) : ((((Integer)this.yMove.getValue()).intValue() - 1) / 10000.0D)) : 2.0E-8D);
/* 120 */       double posZ = mc.field_71439_g.field_70161_v + dirSpeed[1];
/*     */       
/* 122 */       mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.PositionRotation(posX, posY, posZ, mc.field_71439_g.field_70177_z, mc.field_71439_g.field_70125_A, false));
/*     */       
/* 124 */       if (((Integer)this.teleportConfirm.getValue()).intValue() != 3) {
/* 125 */         mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketConfirmTeleport(this.teleportIds - 1));
/* 126 */         this.teleportIds++;
/*     */       } 
/*     */       
/* 129 */       if (((Boolean)this.extra.getValue()).booleanValue()) {
/* 130 */         mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.PositionRotation(mc.field_71439_g.field_70165_t, ((Integer)this.offset.getValue()).intValue() + mc.field_71439_g.field_70163_u, mc.field_71439_g.field_70161_v, mc.field_71439_g.field_70177_z, mc.field_71439_g.field_70125_A, true));
/*     */       }
/*     */       
/* 133 */       if (((Integer)this.teleportConfirm.getValue()).intValue() != 1) {
/* 134 */         mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketConfirmTeleport(this.teleportIds + 1));
/* 135 */         this.teleportIds++;
/*     */       } 
/*     */       
/* 138 */       if (((Boolean)this.ultraPacket.getValue()).booleanValue()) {
/* 139 */         mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.PositionRotation(posX, posY, posZ, mc.field_71439_g.field_70177_z, mc.field_71439_g.field_70125_A, false));
/*     */       }
/*     */       
/* 142 */       if (((Integer)this.teleportConfirm.getValue()).intValue() == 4) {
/* 143 */         mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketConfirmTeleport(this.teleportIds));
/* 144 */         this.teleportIds++;
/*     */       } 
/*     */       
/* 147 */       if (((Boolean)this.fallPacket.getValue()).booleanValue()) {
/* 148 */         mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketEntityAction((Entity)mc.field_71439_g, CPacketEntityAction.Action.START_FALL_FLYING));
/*     */       }
/*     */       
/* 151 */       mc.field_71439_g.func_70107_b(posX, posY, posZ);
/*     */       
/* 153 */       this.teleport = (!((Boolean)this.teleporter.getValue()).booleanValue() || !this.teleport);
/*     */       
/* 155 */       if (event != null) {
/* 156 */         event.setX(0.0D);
/* 157 */         event.setY(0.0D);
/* 158 */         event.setX(0.0D);
/*     */       } else {
/* 160 */         mc.field_71439_g.field_70159_w = 0.0D;
/* 161 */         mc.field_71439_g.field_70181_x = 0.0D;
/* 162 */         mc.field_71439_g.field_70179_y = 0.0D;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void doBoundingBox() {
/* 169 */     double[] dirSpeed = getMotion(this.teleport ? 0.02250000089406967D : 0.02239999920129776D);
/* 170 */     mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.PositionRotation(mc.field_71439_g.field_70165_t + dirSpeed[0], mc.field_71439_g.field_70163_u + (mc.field_71474_y.field_74314_A.func_151470_d() ? (this.teleport ? 0.0625D : 0.0624D) : 1.0E-8D) - (mc.field_71474_y.field_74311_E.func_151470_d() ? (this.teleport ? 0.0625D : 0.0624D) : 2.0E-8D), mc.field_71439_g.field_70161_v + dirSpeed[1], mc.field_71439_g.field_70177_z, mc.field_71439_g.field_70125_A, false));
/* 171 */     mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.PositionRotation(mc.field_71439_g.field_70165_t, -1337.0D, mc.field_71439_g.field_70161_v, mc.field_71439_g.field_70177_z, mc.field_71439_g.field_70125_A, true));
/* 172 */     mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketEntityAction((Entity)mc.field_71439_g, CPacketEntityAction.Action.START_FALL_FLYING));
/* 173 */     mc.field_71439_g.func_70107_b(mc.field_71439_g.field_70165_t + dirSpeed[0], mc.field_71439_g.field_70163_u + (mc.field_71474_y.field_74314_A.func_151470_d() ? (this.teleport ? 0.0625D : 0.0624D) : 1.0E-8D) - (mc.field_71474_y.field_74311_E.func_151470_d() ? (this.teleport ? 0.0625D : 0.0624D) : 2.0E-8D), mc.field_71439_g.field_70161_v + dirSpeed[1]);
/* 174 */     this.teleport = !this.teleport;
/* 175 */     mc.field_71439_g.field_70159_w = mc.field_71439_g.field_70181_x = mc.field_71439_g.field_70179_y = 0.0D;
/* 176 */     mc.field_71439_g.field_70145_X = this.teleport;
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onPacketReceive(PacketEvent.Receive event) {
/* 181 */     if (((Boolean)this.posLook.getValue()).booleanValue() && event.getPacket() instanceof SPacketPlayerPosLook) {
/* 182 */       SPacketPlayerPosLook packet = (SPacketPlayerPosLook)event.getPacket();
/* 183 */       if (mc.field_71439_g.func_70089_S() && mc.field_71441_e.func_175667_e(new BlockPos(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u, mc.field_71439_g.field_70161_v)) && !(mc.field_71462_r instanceof net.minecraft.client.gui.GuiDownloadTerrain)) {
/* 184 */         if (this.teleportIds <= 0) {
/* 185 */           this.teleportIds = packet.func_186965_f();
/* 186 */         } else if (((Boolean)this.cancel.getValue()).booleanValue() && this.posLookPackets >= ((Integer)this.cancelPacket.getValue()).intValue() && (!((Boolean)this.onlyY.getValue()).booleanValue() || (!mc.field_71474_y.field_74351_w.func_151470_d() && !mc.field_71474_y.field_74366_z.func_151470_d() && !mc.field_71474_y.field_74370_x.func_151470_d() && !mc.field_71474_y.field_74368_y.func_151470_d()))) {
/* 187 */           this.posLookPackets = 0;
/* 188 */           event.setCanceled(true);
/*     */         } 
/* 190 */         this.posLookPackets++;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private double[] getMotion(double speed) {
/* 252 */     float moveForward = mc.field_71439_g.field_71158_b.field_192832_b;
/* 253 */     float moveStrafe = mc.field_71439_g.field_71158_b.field_78902_a;
/* 254 */     float rotationYaw = mc.field_71439_g.field_70126_B + (mc.field_71439_g.field_70177_z - mc.field_71439_g.field_70126_B) * mc.func_184121_ak();
/* 255 */     if (moveForward != 0.0F) {
/* 256 */       if (moveStrafe > 0.0F) {
/* 257 */         rotationYaw += ((moveForward > 0.0F) ? -45 : 45);
/*     */       }
/* 259 */       else if (moveStrafe < 0.0F) {
/* 260 */         rotationYaw += ((moveForward > 0.0F) ? 45 : -45);
/*     */       } 
/* 262 */       moveStrafe = 0.0F;
/* 263 */       if (moveForward > 0.0F) {
/* 264 */         moveForward = 1.0F;
/*     */       }
/* 266 */       else if (moveForward < 0.0F) {
/* 267 */         moveForward = -1.0F;
/*     */       } 
/*     */     } 
/* 270 */     double posX = moveForward * speed * -Math.sin(Math.toRadians(rotationYaw)) + moveStrafe * speed * Math.cos(Math.toRadians(rotationYaw));
/* 271 */     double posZ = moveForward * speed * Math.cos(Math.toRadians(rotationYaw)) - moveStrafe * speed * -Math.sin(Math.toRadians(rotationYaw));
/* 272 */     return new double[] { posX, posZ };
/*     */   }
/*     */   
/*     */   public enum Mode {
/* 276 */     PACKETFLY;
/*     */   }
/*     */   
/*     */   public enum PacketFlyMode {
/* 280 */     NONE,
/* 281 */     SETBACK;
/*     */   }
/*     */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\me\earth\phobos\features\modules\movement\Phase.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */