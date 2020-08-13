/*     */ package me.earth.phobos.features.modules.movement;
/*     */ 
/*     */ import java.util.List;
/*     */ import me.earth.phobos.Phobos;
/*     */ import me.earth.phobos.event.events.UpdateWalkingPlayerEvent;
/*     */ import me.earth.phobos.features.modules.Module;
/*     */ import me.earth.phobos.features.setting.Setting;
/*     */ import me.earth.phobos.util.EntityUtil;
/*     */ import me.earth.phobos.util.MathUtil;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.network.Packet;
/*     */ import net.minecraft.network.play.client.CPacketPlayer;
/*     */ import net.minecraft.util.math.BlockPos;
/*     */ import net.minecraft.world.IBlockAccess;
/*     */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*     */ 
/*     */ public class HoleTP
/*     */   extends Module {
/*  21 */   private final Setting<Mode> mode = register(new Setting("Mode", Mode.TELEPORT));
/*  22 */   private final Setting<Float> range = register(new Setting("Range", Float.valueOf(1.0F), Float.valueOf(0.0F), Float.valueOf(5.0F), v -> (this.mode.getValue() == Mode.TELEPORT)));
/*  23 */   private final Setting<Boolean> setY = register(new Setting("SetY", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.TELEPORT)));
/*  24 */   private final Setting<Float> xOffset = register(new Setting("XOffset", Float.valueOf(0.5F), Float.valueOf(0.0F), Float.valueOf(1.0F), v -> (this.mode.getValue() == Mode.TELEPORT)));
/*  25 */   private final Setting<Float> yOffset = register(new Setting("YOffset", Float.valueOf(0.0F), Float.valueOf(0.0F), Float.valueOf(1.0F), v -> (this.mode.getValue() == Mode.TELEPORT)));
/*  26 */   private final Setting<Float> zOffset = register(new Setting("ZOffset", Float.valueOf(0.5F), Float.valueOf(0.0F), Float.valueOf(1.0F), v -> (this.mode.getValue() == Mode.TELEPORT)));
/*     */   
/*  28 */   private static HoleTP INSTANCE = new HoleTP();
/*     */   
/*  30 */   private final double[] oneblockPositions = new double[] { 0.42D, 0.75D };
/*     */   private int packets;
/*     */   private boolean jumped = false;
/*     */   
/*     */   public HoleTP() {
/*  35 */     super("HoleTP", "Teleports you in a hole.", Module.Category.MOVEMENT, true, false, false);
/*  36 */     setInstance();
/*     */   }
/*     */   
/*     */   private void setInstance() {
/*  40 */     INSTANCE = this;
/*     */   }
/*     */   
/*     */   public static HoleTP getInstance() {
/*  44 */     if (INSTANCE == null) {
/*  45 */       INSTANCE = new HoleTP();
/*     */     }
/*  47 */     return INSTANCE;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onEnable() {
/*  52 */     if (this.mode.getValue() == Mode.TELEPORT) {
/*  53 */       Phobos.holeManager.update();
/*  54 */       List<BlockPos> holes = Phobos.holeManager.getSortedHoles();
/*  55 */       if (!holes.isEmpty()) {
/*  56 */         BlockPos pos = holes.get(0);
/*  57 */         if (mc.field_71439_g.func_174818_b(pos) <= MathUtil.square(((Float)this.range.getValue()).floatValue())) {
/*  58 */           Phobos.positionManager.setPositionPacket((pos.func_177958_n() + ((Float)this.xOffset.getValue()).floatValue()), (((Boolean)this.setY.getValue()).booleanValue() ? pos.func_177956_o() : mc.field_71439_g.field_70163_u) + ((Float)this.yOffset.getValue()).floatValue(), (pos.func_177952_p() + ((Float)this.zOffset.getValue()).floatValue()), (((Boolean)this.setY.getValue()).booleanValue() && ((Float)this.yOffset.getValue()).floatValue() == 0.0F), true, true);
/*     */         }
/*     */       } 
/*  61 */       disable();
/*     */     } 
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onUpdateWalkingPlayer(UpdateWalkingPlayerEvent event) {
/*  67 */     if (event.getStage() == 1 && this.mode.getValue() == Mode.STEP && (!Phobos.moduleManager.isModuleEnabled(Speed.class) || ((Speed)Phobos.moduleManager.getModuleByClass(Speed.class)).mode.getValue() == Speed.Mode.INSTANT) && !Phobos.moduleManager.isModuleEnabled(Strafe.class)) {
/*  68 */       if (!mc.field_71439_g.field_70122_E)
/*  69 */       { if (mc.field_71474_y.field_74314_A.func_151470_d())
/*  70 */           this.jumped = true;  }
/*  71 */       else { this.jumped = false; }
/*  72 */        if (!this.jumped && mc.field_71439_g.field_70143_R < 0.5D && isInHole() && mc.field_71439_g.field_70163_u - getNearestBlockBelow() <= 1.125D && mc.field_71439_g.field_70163_u - getNearestBlockBelow() <= 0.95D && !EntityUtil.isOnLiquid() && !EntityUtil.isInLiquid()) {
/*  73 */         if (!mc.field_71439_g.field_70122_E) {
/*  74 */           this.packets++;
/*     */         }
/*  76 */         if (!mc.field_71439_g.field_70122_E && !mc.field_71439_g.func_70055_a(Material.field_151586_h) && !mc.field_71439_g.func_70055_a(Material.field_151587_i) && !mc.field_71474_y.field_74314_A.func_151470_d() && !mc.field_71439_g.func_70617_f_() && this.packets > 0) {
/*  77 */           BlockPos blockPos = new BlockPos(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u, mc.field_71439_g.field_70161_v);
/*  78 */           for (double position : this.oneblockPositions) {
/*  79 */             mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position((blockPos.func_177958_n() + 0.5F), mc.field_71439_g.field_70163_u - position, (blockPos.func_177952_p() + 0.5F), true));
/*     */           }
/*  81 */           mc.field_71439_g.func_70107_b((blockPos.func_177958_n() + 0.5F), getNearestBlockBelow() + 0.1D, (blockPos.func_177952_p() + 0.5F));
/*  82 */           this.packets = 0;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean isInHole() {
/*  89 */     BlockPos blockPos = new BlockPos(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u, mc.field_71439_g.field_70161_v);
/*  90 */     IBlockState blockState = mc.field_71441_e.func_180495_p(blockPos);
/*  91 */     return isBlockValid(blockState, blockPos);
/*     */   }
/*     */   
/*     */   private double getNearestBlockBelow() {
/*  95 */     for (double y = mc.field_71439_g.field_70163_u; y > 0.0D; y -= 0.001D) {
/*  96 */       if (!(mc.field_71441_e.func_180495_p(new BlockPos(mc.field_71439_g.field_70165_t, y, mc.field_71439_g.field_70161_v)).func_177230_c() instanceof net.minecraft.block.BlockSlab) && mc.field_71441_e.func_180495_p(new BlockPos(mc.field_71439_g.field_70165_t, y, mc.field_71439_g.field_70161_v)).func_177230_c().func_176223_P().func_185890_d((IBlockAccess)mc.field_71441_e, new BlockPos(0, 0, 0)) != null) {
/*  97 */         return y;
/*     */       }
/*     */     } 
/* 100 */     return -1.0D;
/*     */   }
/*     */   
/*     */   private boolean isBlockValid(IBlockState blockState, BlockPos blockPos) {
/* 104 */     if (blockState.func_177230_c() != Blocks.field_150350_a)
/* 105 */       return false; 
/* 106 */     if (mc.field_71439_g.func_174818_b(blockPos) < 1.0D)
/* 107 */       return false; 
/* 108 */     if (mc.field_71441_e.func_180495_p(blockPos.func_177984_a()).func_177230_c() != Blocks.field_150350_a)
/* 109 */       return false; 
/* 110 */     if (mc.field_71441_e.func_180495_p(blockPos.func_177981_b(2)).func_177230_c() != Blocks.field_150350_a) {
/* 111 */       return false;
/*     */     }
/* 113 */     return (isBedrockHole(blockPos) || isObbyHole(blockPos) || isBothHole(blockPos) || isElseHole(blockPos));
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean isObbyHole(BlockPos blockPos) {
/* 118 */     BlockPos[] touchingBlocks = { blockPos.func_177978_c(), blockPos.func_177968_d(), blockPos.func_177974_f(), blockPos.func_177976_e(), blockPos.func_177977_b() };
/* 119 */     for (BlockPos pos : touchingBlocks) {
/* 120 */       IBlockState touchingState = mc.field_71441_e.func_180495_p(pos);
/* 121 */       if (touchingState.func_177230_c() == Blocks.field_150350_a || touchingState.func_177230_c() != Blocks.field_150343_Z) {
/* 122 */         return false;
/*     */       }
/*     */     } 
/*     */     
/* 126 */     return true;
/*     */   }
/*     */   
/*     */   private boolean isBedrockHole(BlockPos blockPos) {
/* 130 */     BlockPos[] touchingBlocks = { blockPos.func_177978_c(), blockPos.func_177968_d(), blockPos.func_177974_f(), blockPos.func_177976_e(), blockPos.func_177977_b() };
/* 131 */     for (BlockPos pos : touchingBlocks) {
/* 132 */       IBlockState touchingState = mc.field_71441_e.func_180495_p(pos);
/* 133 */       if (touchingState.func_177230_c() == Blocks.field_150350_a || touchingState.func_177230_c() != Blocks.field_150357_h) {
/* 134 */         return false;
/*     */       }
/*     */     } 
/*     */     
/* 138 */     return true;
/*     */   }
/*     */   
/*     */   private boolean isBothHole(BlockPos blockPos) {
/* 142 */     BlockPos[] touchingBlocks = { blockPos.func_177978_c(), blockPos.func_177968_d(), blockPos.func_177974_f(), blockPos.func_177976_e(), blockPos.func_177977_b() };
/* 143 */     for (BlockPos pos : touchingBlocks) {
/* 144 */       IBlockState touchingState = mc.field_71441_e.func_180495_p(pos);
/* 145 */       if (touchingState.func_177230_c() == Blocks.field_150350_a || (touchingState.func_177230_c() != Blocks.field_150357_h && touchingState.func_177230_c() != Blocks.field_150343_Z)) {
/* 146 */         return false;
/*     */       }
/*     */     } 
/* 149 */     return true;
/*     */   }
/*     */   
/*     */   private boolean isElseHole(BlockPos blockPos) {
/* 153 */     BlockPos[] touchingBlocks = { blockPos.func_177978_c(), blockPos.func_177968_d(), blockPos.func_177974_f(), blockPos.func_177976_e(), blockPos.func_177977_b() };
/* 154 */     for (BlockPos pos : touchingBlocks) {
/* 155 */       IBlockState touchingState = mc.field_71441_e.func_180495_p(pos);
/* 156 */       if (touchingState.func_177230_c() == Blocks.field_150350_a || !touchingState.func_185913_b()) {
/* 157 */         return false;
/*     */       }
/*     */     } 
/* 160 */     return true;
/*     */   }
/*     */   
/*     */   public enum Mode {
/* 164 */     TELEPORT,
/* 165 */     STEP;
/*     */   }
/*     */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\me\earth\phobos\features\modules\movement\HoleTP.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */