/*     */ package me.earth.phobos.features.modules.combat;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import me.earth.phobos.Phobos;
/*     */ import me.earth.phobos.event.events.UpdateWalkingPlayerEvent;
/*     */ import me.earth.phobos.features.modules.Module;
/*     */ import me.earth.phobos.features.setting.Setting;
/*     */ import me.earth.phobos.util.BlockUtil;
/*     */ import me.earth.phobos.util.EntityUtil;
/*     */ import me.earth.phobos.util.InventoryUtil;
/*     */ import me.earth.phobos.util.MathUtil;
/*     */ import me.earth.phobos.util.Timer;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.ItemEndCrystal;
/*     */ import net.minecraft.network.Packet;
/*     */ import net.minecraft.network.play.client.CPacketPlayer;
/*     */ import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.EnumHand;
/*     */ import net.minecraft.util.math.BlockPos;
/*     */ import net.minecraft.util.math.MathHelper;
/*     */ import net.minecraft.util.math.RayTraceResult;
/*     */ import net.minecraft.util.math.Vec3d;
/*     */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*     */ 
/*     */ public class AntiTrap
/*     */   extends Module {
/*  32 */   public Setting<Rotate> rotate = register(new Setting("Rotate", Rotate.NORMAL));
/*  33 */   private final Setting<Integer> coolDown = register(new Setting("CoolDown", Integer.valueOf(400), Integer.valueOf(0), Integer.valueOf(1000)));
/*  34 */   private final Setting<InventoryUtil.Switch> switchMode = register(new Setting("Switch", InventoryUtil.Switch.NORMAL));
/*  35 */   public Setting<Boolean> sortY = register(new Setting("SortY", Boolean.valueOf(true)));
/*     */   
/*  37 */   private final Vec3d[] surroundTargets = new Vec3d[] { new Vec3d(1.0D, 0.0D, 0.0D), new Vec3d(0.0D, 0.0D, 1.0D), new Vec3d(-1.0D, 0.0D, 0.0D), new Vec3d(0.0D, 0.0D, -1.0D), new Vec3d(1.0D, 0.0D, -1.0D), new Vec3d(1.0D, 0.0D, 1.0D), new Vec3d(-1.0D, 0.0D, -1.0D), new Vec3d(-1.0D, 0.0D, 1.0D), new Vec3d(1.0D, 1.0D, 0.0D), new Vec3d(0.0D, 1.0D, 1.0D), new Vec3d(-1.0D, 1.0D, 0.0D), new Vec3d(0.0D, 1.0D, -1.0D), new Vec3d(1.0D, 1.0D, -1.0D), new Vec3d(1.0D, 1.0D, 1.0D), new Vec3d(-1.0D, 1.0D, -1.0D), new Vec3d(-1.0D, 1.0D, 1.0D) };
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
/*  56 */   private int lastHotbarSlot = -1;
/*     */   private boolean switchedItem;
/*     */   private boolean offhand = false;
/*  59 */   private final Timer timer = new Timer();
/*     */   
/*     */   public AntiTrap() {
/*  62 */     super("AntiTrap", "Places a crystal to prevent you getting trapped.", Module.Category.COMBAT, true, false, false);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onEnable() {
/*  67 */     if (fullNullCheck() || !this.timer.passedMs(((Integer)this.coolDown.getValue()).intValue())) {
/*  68 */       disable();
/*     */       return;
/*     */     } 
/*  71 */     this.lastHotbarSlot = mc.field_71439_g.field_71071_by.field_70461_c;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onDisable() {
/*  76 */     if (fullNullCheck()) {
/*     */       return;
/*     */     }
/*  79 */     switchItem(true);
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onUpdateWalkingPlayer(UpdateWalkingPlayerEvent event) {
/*  84 */     if (!fullNullCheck() && event.getStage() == 0) {
/*  85 */       doAntiTrap();
/*     */     }
/*     */   }
/*     */   
/*     */   public void doAntiTrap() {
/*  90 */     this.offhand = (mc.field_71439_g.func_184592_cb().func_77973_b() == Items.field_185158_cP);
/*  91 */     if (!this.offhand && InventoryUtil.findHotbarBlock(ItemEndCrystal.class) == -1) {
/*  92 */       disable();
/*     */       return;
/*     */     } 
/*  95 */     this.lastHotbarSlot = mc.field_71439_g.field_71071_by.field_70461_c;
/*  96 */     List<Vec3d> targets = new ArrayList<>();
/*  97 */     Collections.addAll(targets, BlockUtil.convertVec3ds(mc.field_71439_g.func_174791_d(), this.surroundTargets));
/*  98 */     EntityPlayer closestPlayer = EntityUtil.getClosestEnemy(6.0D);
/*  99 */     if (closestPlayer != null) {
/* 100 */       targets.sort((vec3d, vec3d2) -> Double.compare(closestPlayer.func_70092_e(vec3d2.field_72450_a, vec3d2.field_72448_b, vec3d2.field_72449_c), closestPlayer.func_70092_e(vec3d.field_72450_a, vec3d.field_72448_b, vec3d.field_72449_c)));
/* 101 */       if (((Boolean)this.sortY.getValue()).booleanValue()) {
/* 102 */         targets.sort(Comparator.comparingDouble(vec3d -> vec3d.field_72448_b));
/*     */       }
/*     */     } 
/*     */     
/* 106 */     for (Vec3d vec3d : targets) {
/* 107 */       BlockPos pos = new BlockPos(vec3d);
/* 108 */       if (BlockUtil.canPlaceCrystal(pos)) {
/* 109 */         placeCrystal(pos);
/* 110 */         disable();
/*     */         break;
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void placeCrystal(BlockPos pos) {
/* 117 */     boolean mainhand = (mc.field_71439_g.func_184614_ca().func_77973_b() == Items.field_185158_cP);
/* 118 */     if (!mainhand && !this.offhand && 
/* 119 */       !switchItem(false)) {
/* 120 */       disable();
/*     */       
/*     */       return;
/*     */     } 
/* 124 */     RayTraceResult result = mc.field_71441_e.func_72933_a(new Vec3d(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u + mc.field_71439_g.func_70047_e(), mc.field_71439_g.field_70161_v), new Vec3d(pos.func_177958_n() + 0.5D, pos.func_177956_o() - 0.5D, pos.func_177952_p() + 0.5D));
/* 125 */     EnumFacing facing = (result == null || result.field_178784_b == null) ? EnumFacing.UP : result.field_178784_b;
/* 126 */     float[] angle = MathUtil.calcAngle(mc.field_71439_g.func_174824_e(mc.func_184121_ak()), new Vec3d((pos.func_177958_n() + 0.5F), (pos.func_177956_o() - 0.5F), (pos.func_177952_p() + 0.5F)));
/* 127 */     switch ((Rotate)this.rotate.getValue()) {
/*     */ 
/*     */       
/*     */       case NORMAL:
/* 131 */         Phobos.rotationManager.setPlayerRotations(angle[0], angle[1]);
/*     */         break;
/*     */       case PACKET:
/* 134 */         mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Rotation(angle[0], MathHelper.func_180184_b((int)angle[1], 360), mc.field_71439_g.field_70122_E));
/*     */         break;
/*     */     } 
/*     */     
/* 138 */     mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayerTryUseItemOnBlock(pos, facing, this.offhand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, 0.0F, 0.0F, 0.0F));
/* 139 */     mc.field_71439_g.func_184609_a(EnumHand.MAIN_HAND);
/* 140 */     this.timer.reset();
/*     */   }
/*     */   
/*     */   private boolean switchItem(boolean back) {
/* 144 */     if (this.offhand) {
/* 145 */       return true;
/*     */     }
/* 147 */     boolean[] value = InventoryUtil.switchItemToItem(back, this.lastHotbarSlot, this.switchedItem, (InventoryUtil.Switch)this.switchMode.getValue(), Items.field_185158_cP);
/* 148 */     this.switchedItem = value[0];
/* 149 */     return value[1];
/*     */   }
/*     */   
/*     */   public enum Rotate {
/* 153 */     NONE,
/* 154 */     NORMAL,
/* 155 */     PACKET;
/*     */   }
/*     */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\me\earth\phobos\features\modules\combat\AntiTrap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */