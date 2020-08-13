/*     */ package me.earth.phobos.features.modules.combat;
/*     */ 
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import me.earth.phobos.features.modules.Module;
/*     */ import me.earth.phobos.features.setting.Setting;
/*     */ import me.earth.phobos.util.BlockUtil;
/*     */ import me.earth.phobos.util.InventoryUtil;
/*     */ import me.earth.phobos.util.Timer;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.network.Packet;
/*     */ import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.EnumHand;
/*     */ import net.minecraft.util.math.BlockPos;
/*     */ import net.minecraft.util.math.RayTraceResult;
/*     */ import net.minecraft.util.math.Vec3d;
/*     */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*     */ import net.minecraftforge.fml.common.gameevent.TickEvent;
/*     */ 
/*     */ public class Crasher
/*     */   extends Module {
/*  23 */   private final Setting<Mode> mode = register(new Setting("Mode", Mode.ONCE));
/*  24 */   private final Setting<Float> placeRange = register(new Setting("PlaceRange", Float.valueOf(6.0F), Float.valueOf(0.0F), Float.valueOf(10.0F)));
/*  25 */   private final Setting<Integer> crystals = register(new Setting("Positions", Integer.valueOf(100), Integer.valueOf(0), Integer.valueOf(1000)));
/*  26 */   private final Setting<Integer> coolDown = register(new Setting("CoolDown", Integer.valueOf(400), Integer.valueOf(0), Integer.valueOf(1000)));
/*  27 */   private final Setting<InventoryUtil.Switch> switchMode = register(new Setting("Switch", InventoryUtil.Switch.NORMAL));
/*  28 */   public Setting<Integer> sort = register(new Setting("Sort", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(2)));
/*     */   
/*     */   private boolean offhand = false;
/*     */   private boolean mainhand = false;
/*  32 */   private Timer timer = new Timer();
/*  33 */   private int lastHotbarSlot = -1;
/*     */   private boolean switchedItem = false;
/*     */   private boolean chinese = false;
/*     */   
/*     */   public Crasher() {
/*  38 */     super("CrystalCrash", "Attempts to crash chinese AutoCrystals", Module.Category.COMBAT, false, false, true);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onEnable() {
/*  43 */     this.chinese = false;
/*  44 */     if (fullNullCheck() || !this.timer.passedMs(((Integer)this.coolDown.getValue()).intValue())) {
/*  45 */       disable();
/*     */       
/*     */       return;
/*     */     } 
/*  49 */     this.lastHotbarSlot = mc.field_71439_g.field_71071_by.field_70461_c;
/*  50 */     if (this.mode.getValue() == Mode.ONCE) {
/*  51 */       placeCrystals();
/*  52 */       disable();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onDisable() {
/*  58 */     this.timer.reset();
/*  59 */     if (this.mode.getValue() == Mode.SPAM) {
/*  60 */       switchItem(true);
/*     */     }
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onTick(TickEvent.ClientTickEvent event) {
/*  66 */     if (fullNullCheck() || event.phase == TickEvent.Phase.START || (isOff() && (this.timer.passedMs(10L) || this.mode.getValue() == Mode.SPAM))) {
/*     */       return;
/*     */     }
/*     */     
/*  70 */     if (this.mode.getValue() == Mode.SPAM) {
/*  71 */       placeCrystals();
/*     */     } else {
/*  73 */       switchItem(true);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void placeCrystals() {
/*  78 */     this.offhand = (mc.field_71439_g.func_184592_cb().func_77973_b() == Items.field_185158_cP);
/*  79 */     this.mainhand = (mc.field_71439_g.func_184614_ca().func_77973_b() == Items.field_185158_cP);
/*  80 */     int crystalcount = 0;
/*  81 */     List<BlockPos> blocks = BlockUtil.possiblePlacePositions(((Float)this.placeRange.getValue()).floatValue(), false);
/*  82 */     if (((Integer)this.sort.getValue()).intValue() == 1) {
/*  83 */       blocks.sort(Comparator.comparingDouble(hole -> mc.field_71439_g.func_174818_b(hole)));
/*  84 */     } else if (((Integer)this.sort.getValue()).intValue() == 2) {
/*  85 */       blocks.sort(Comparator.comparingDouble(hole -> -mc.field_71439_g.func_174818_b(hole)));
/*     */     } 
/*     */     
/*  88 */     for (BlockPos pos : blocks) {
/*  89 */       if (isOff() || crystalcount >= ((Integer)this.crystals.getValue()).intValue()) {
/*     */         break;
/*     */       }
/*  92 */       placeCrystal(pos);
/*  93 */       crystalcount++;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void placeCrystal(BlockPos pos) {
/*  98 */     if (!this.chinese && !this.mainhand && !this.offhand && 
/*  99 */       !switchItem(false)) {
/* 100 */       disable();
/*     */       
/*     */       return;
/*     */     } 
/* 104 */     RayTraceResult result = mc.field_71441_e.func_72933_a(new Vec3d(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u + mc.field_71439_g.func_70047_e(), mc.field_71439_g.field_70161_v), new Vec3d(pos.func_177958_n() + 0.5D, pos.func_177956_o() - 0.5D, pos.func_177952_p() + 0.5D));
/* 105 */     EnumFacing facing = (result == null || result.field_178784_b == null) ? EnumFacing.UP : result.field_178784_b;
/* 106 */     mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayerTryUseItemOnBlock(pos, facing, this.offhand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, 0.0F, 0.0F, 0.0F));
/* 107 */     mc.field_71439_g.func_184609_a(EnumHand.MAIN_HAND);
/*     */   }
/*     */   
/*     */   private boolean switchItem(boolean back) {
/* 111 */     this.chinese = true;
/* 112 */     if (this.offhand) {
/* 113 */       return true;
/*     */     }
/* 115 */     boolean[] value = InventoryUtil.switchItemToItem(back, this.lastHotbarSlot, this.switchedItem, (InventoryUtil.Switch)this.switchMode.getValue(), Items.field_185158_cP);
/* 116 */     this.switchedItem = value[0];
/* 117 */     return value[1];
/*     */   }
/*     */   
/*     */   public enum Mode {
/* 121 */     ONCE,
/* 122 */     SPAM;
/*     */   }
/*     */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\me\earth\phobos\features\modules\combat\Crasher.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */