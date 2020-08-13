/*     */ package me.earth.phobos.util;
/*     */ 
/*     */ import com.google.common.util.concurrent.AtomicDouble;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
/*     */ import java.util.stream.Collectors;
/*     */ import me.earth.phobos.Phobos;
/*     */ import me.earth.phobos.features.command.Command;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.network.Packet;
/*     */ import net.minecraft.network.play.client.CPacketEntityAction;
/*     */ import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.EnumHand;
/*     */ import net.minecraft.util.NonNullList;
/*     */ import net.minecraft.util.math.AxisAlignedBB;
/*     */ import net.minecraft.util.math.BlockPos;
/*     */ import net.minecraft.util.math.RayTraceResult;
/*     */ import net.minecraft.util.math.Vec3d;
/*     */ import net.minecraft.util.math.Vec3i;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BlockUtil
/*     */   implements Util
/*     */ {
/*  39 */   public static final List<Block> blackList = Arrays.asList(new Block[] { Blocks.field_150477_bB, (Block)Blocks.field_150486_ae, Blocks.field_150447_bR, Blocks.field_150462_ai, Blocks.field_150467_bQ, Blocks.field_150382_bo, (Block)Blocks.field_150438_bZ, Blocks.field_150409_cd, Blocks.field_150367_z, Blocks.field_150415_aT, Blocks.field_150381_bn });
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
/*  53 */   public static final List<Block> shulkerList = Arrays.asList(new Block[] { Blocks.field_190977_dl, Blocks.field_190978_dm, Blocks.field_190979_dn, Blocks.field_190980_do, Blocks.field_190981_dp, Blocks.field_190982_dq, Blocks.field_190983_dr, Blocks.field_190984_ds, Blocks.field_190985_dt, Blocks.field_190986_du, Blocks.field_190987_dv, Blocks.field_190988_dw, Blocks.field_190989_dx, Blocks.field_190990_dy, Blocks.field_190991_dz, Blocks.field_190975_dA });
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
/*  72 */   public static List<Block> unSolidBlocks = Arrays.asList(new Block[] { (Block)Blocks.field_150356_k, Blocks.field_150457_bL, Blocks.field_150433_aE, Blocks.field_150404_cg, Blocks.field_185764_cQ, (Block)Blocks.field_150465_bP, Blocks.field_150457_bL, Blocks.field_150473_bD, (Block)Blocks.field_150479_bC, Blocks.field_150471_bO, Blocks.field_150442_at, Blocks.field_150430_aB, Blocks.field_150468_ap, (Block)Blocks.field_150441_bU, (Block)Blocks.field_150455_bV, (Block)Blocks.field_150413_aR, (Block)Blocks.field_150416_aS, Blocks.field_150437_az, Blocks.field_150429_aA, (Block)Blocks.field_150488_af, Blocks.field_150350_a, (Block)Blocks.field_150427_aO, Blocks.field_150384_bq, (Block)Blocks.field_150355_j, (Block)Blocks.field_150358_i, (Block)Blocks.field_150353_l, (Block)Blocks.field_150356_k, Blocks.field_150345_g, (Block)Blocks.field_150328_O, (Block)Blocks.field_150327_N, (Block)Blocks.field_150338_P, (Block)Blocks.field_150337_Q, Blocks.field_150464_aj, Blocks.field_150459_bM, Blocks.field_150469_bN, Blocks.field_185773_cZ, (Block)Blocks.field_150436_aH, Blocks.field_150393_bb, Blocks.field_150394_bc, Blocks.field_150392_bi, Blocks.field_150388_bm, Blocks.field_150375_by, Blocks.field_185766_cS, Blocks.field_185765_cR, (Block)Blocks.field_150329_H, (Block)Blocks.field_150330_I, Blocks.field_150395_bd, (Block)Blocks.field_150480_ab, Blocks.field_150448_aq, Blocks.field_150408_cc, Blocks.field_150319_E, Blocks.field_150318_D, Blocks.field_150478_aa });
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
/*     */   public static List<BlockPos> getBlockSphere(float breakRange, Class clazz) {
/* 129 */     NonNullList<BlockPos> positions = NonNullList.func_191196_a();
/* 130 */     positions.addAll((Collection)getSphere(EntityUtil.getPlayerPos((EntityPlayer)mc.field_71439_g), breakRange, (int)breakRange, false, true, 0).stream().filter(pos -> clazz.isInstance(mc.field_71441_e.func_180495_p(pos).func_177230_c())).collect(Collectors.toList()));
/* 131 */     return (List<BlockPos>)positions;
/*     */   }
/*     */   
/*     */   public static List<EnumFacing> getPossibleSides(BlockPos pos) {
/* 135 */     List<EnumFacing> facings = new ArrayList<>();
/* 136 */     for (EnumFacing side : EnumFacing.values()) {
/* 137 */       BlockPos neighbour = pos.func_177972_a(side);
/* 138 */       if (mc.field_71441_e.func_180495_p(neighbour).func_177230_c().func_176209_a(mc.field_71441_e.func_180495_p(neighbour), false)) {
/* 139 */         IBlockState blockState = mc.field_71441_e.func_180495_p(neighbour);
/* 140 */         if (!blockState.func_185904_a().func_76222_j()) {
/* 141 */           facings.add(side);
/*     */         }
/*     */       } 
/*     */     } 
/* 145 */     return facings;
/*     */   }
/*     */   
/*     */   public static EnumFacing getFirstFacing(BlockPos pos) {
/* 149 */     Iterator<EnumFacing> iterator = getPossibleSides(pos).iterator(); if (iterator.hasNext()) { EnumFacing facing = iterator.next();
/* 150 */       return facing; }
/*     */     
/* 152 */     return null;
/*     */   }
/*     */   
/*     */   public static EnumFacing getRayTraceFacing(BlockPos pos) {
/* 156 */     RayTraceResult result = mc.field_71441_e.func_72933_a(new Vec3d(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u + mc.field_71439_g.func_70047_e(), mc.field_71439_g.field_70161_v), new Vec3d(pos.func_177958_n() + 0.5D, pos.func_177958_n() - 0.5D, pos.func_177958_n() + 0.5D));
/* 157 */     if (result == null || result.field_178784_b == null) {
/* 158 */       return EnumFacing.UP;
/*     */     }
/* 160 */     return result.field_178784_b;
/*     */   }
/*     */   
/*     */   public static int isPositionPlaceable(BlockPos pos, boolean rayTrace) {
/* 164 */     return isPositionPlaceable(pos, rayTrace, true);
/*     */   }
/*     */   
/*     */   public static int isPositionPlaceable(BlockPos pos, boolean rayTrace, boolean entityCheck) {
/* 168 */     Block block = mc.field_71441_e.func_180495_p(pos).func_177230_c();
/* 169 */     if (!(block instanceof net.minecraft.block.BlockAir) && !(block instanceof net.minecraft.block.BlockLiquid) && !(block instanceof net.minecraft.block.BlockTallGrass) && !(block instanceof net.minecraft.block.BlockFire) && !(block instanceof net.minecraft.block.BlockDeadBush) && !(block instanceof net.minecraft.block.BlockSnow)) {
/* 170 */       return 0;
/*     */     }
/*     */     
/* 173 */     if (!rayTracePlaceCheck(pos, rayTrace, 0.0F)) {
/* 174 */       return -1;
/*     */     }
/*     */     
/* 177 */     if (entityCheck) {
/* 178 */       for (Entity entity : mc.field_71441_e.func_72872_a(Entity.class, new AxisAlignedBB(pos))) {
/* 179 */         if (!(entity instanceof net.minecraft.entity.item.EntityItem) && !(entity instanceof net.minecraft.entity.item.EntityXPOrb)) {
/* 180 */           return 1;
/*     */         }
/*     */       } 
/*     */     }
/*     */     
/* 185 */     for (EnumFacing side : getPossibleSides(pos)) {
/* 186 */       if (canBeClicked(pos.func_177972_a(side))) {
/* 187 */         return 3;
/*     */       }
/*     */     } 
/*     */     
/* 191 */     return 2;
/*     */   }
/*     */   
/*     */   public static void rightClickBlock(BlockPos pos, Vec3d vec, EnumHand hand, EnumFacing direction, boolean packet) {
/* 195 */     if (packet) {
/* 196 */       float f = (float)(vec.field_72450_a - pos.func_177958_n());
/* 197 */       float f1 = (float)(vec.field_72448_b - pos.func_177956_o());
/* 198 */       float f2 = (float)(vec.field_72449_c - pos.func_177952_p());
/* 199 */       mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayerTryUseItemOnBlock(pos, direction, hand, f, f1, f2));
/*     */     } else {
/* 201 */       mc.field_71442_b.func_187099_a(mc.field_71439_g, mc.field_71441_e, pos, direction, vec, hand);
/*     */     } 
/* 203 */     mc.field_71439_g.func_184609_a(EnumHand.MAIN_HAND);
/* 204 */     mc.field_71467_ac = 4;
/*     */   }
/*     */   
/*     */   public static void rightClickBlockLegit(BlockPos pos, float range, boolean rotate, EnumHand hand, AtomicDouble Yaw, AtomicDouble Pitch, AtomicBoolean rotating) {
/* 208 */     Vec3d eyesPos = RotationUtil.getEyesPos();
/* 209 */     Vec3d posVec = (new Vec3d((Vec3i)pos)).func_72441_c(0.5D, 0.5D, 0.5D);
/* 210 */     double distanceSqPosVec = eyesPos.func_72436_e(posVec);
/* 211 */     for (EnumFacing side : EnumFacing.values()) {
/* 212 */       Vec3d hitVec = posVec.func_178787_e((new Vec3d(side.func_176730_m())).func_186678_a(0.5D));
/* 213 */       double distanceSqHitVec = eyesPos.func_72436_e(hitVec);
/*     */       
/* 215 */       if (distanceSqHitVec <= MathUtil.square(range))
/*     */       {
/*     */ 
/*     */         
/* 219 */         if (distanceSqHitVec < distanceSqPosVec)
/*     */         {
/*     */ 
/*     */           
/* 223 */           if (mc.field_71441_e.func_147447_a(eyesPos, hitVec, false, true, false) == null) {
/*     */ 
/*     */ 
/*     */             
/* 227 */             if (rotate) {
/* 228 */               float[] rotations = RotationUtil.getLegitRotations(hitVec);
/* 229 */               Yaw.set(rotations[0]);
/* 230 */               Pitch.set(rotations[1]);
/* 231 */               rotating.set(true);
/*     */             } 
/*     */             
/* 234 */             mc.field_71442_b.func_187099_a(mc.field_71439_g, mc.field_71441_e, pos, side, hitVec, hand);
/* 235 */             mc.field_71439_g.func_184609_a(hand);
/* 236 */             mc.field_71467_ac = 4;
/*     */             break;
/*     */           }  }  } 
/*     */     } 
/*     */   }
/*     */   public static boolean placeBlock(BlockPos pos, EnumHand hand, boolean rotate, boolean packet, boolean isSneaking) {
/* 242 */     boolean sneaking = false;
/* 243 */     EnumFacing side = getFirstFacing(pos);
/* 244 */     if (side == null) {
/* 245 */       return isSneaking;
/*     */     }
/*     */     
/* 248 */     BlockPos neighbour = pos.func_177972_a(side);
/* 249 */     EnumFacing opposite = side.func_176734_d();
/*     */     
/* 251 */     Vec3d hitVec = (new Vec3d((Vec3i)neighbour)).func_72441_c(0.5D, 0.5D, 0.5D).func_178787_e((new Vec3d(opposite.func_176730_m())).func_186678_a(0.5D));
/* 252 */     Block neighbourBlock = mc.field_71441_e.func_180495_p(neighbour).func_177230_c();
/*     */     
/* 254 */     if (!mc.field_71439_g.func_70093_af() && (blackList.contains(neighbourBlock) || shulkerList.contains(neighbourBlock))) {
/* 255 */       mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketEntityAction((Entity)mc.field_71439_g, CPacketEntityAction.Action.START_SNEAKING));
/* 256 */       mc.field_71439_g.func_70095_a(true);
/* 257 */       sneaking = true;
/*     */     } 
/*     */     
/* 260 */     if (rotate) {
/* 261 */       RotationUtil.faceVector(hitVec, true);
/*     */     }
/*     */     
/* 264 */     rightClickBlock(neighbour, hitVec, hand, opposite, packet);
/* 265 */     mc.field_71439_g.func_184609_a(EnumHand.MAIN_HAND);
/* 266 */     mc.field_71467_ac = 4;
/* 267 */     return (sneaking || isSneaking);
/*     */   }
/*     */   
/*     */   public static boolean placeBlockSmartRotate(BlockPos pos, EnumHand hand, boolean rotate, boolean packet, boolean isSneaking) {
/* 271 */     boolean sneaking = false;
/* 272 */     EnumFacing side = getFirstFacing(pos);
/* 273 */     Command.sendMessage(side.toString());
/* 274 */     if (side == null) {
/* 275 */       return isSneaking;
/*     */     }
/*     */     
/* 278 */     BlockPos neighbour = pos.func_177972_a(side);
/* 279 */     EnumFacing opposite = side.func_176734_d();
/*     */     
/* 281 */     Vec3d hitVec = (new Vec3d((Vec3i)neighbour)).func_72441_c(0.5D, 0.5D, 0.5D).func_178787_e((new Vec3d(opposite.func_176730_m())).func_186678_a(0.5D));
/* 282 */     Block neighbourBlock = mc.field_71441_e.func_180495_p(neighbour).func_177230_c();
/*     */     
/* 284 */     if (!mc.field_71439_g.func_70093_af() && (blackList.contains(neighbourBlock) || shulkerList.contains(neighbourBlock))) {
/* 285 */       mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketEntityAction((Entity)mc.field_71439_g, CPacketEntityAction.Action.START_SNEAKING));
/* 286 */       sneaking = true;
/*     */     } 
/*     */     
/* 289 */     if (rotate) {
/* 290 */       Phobos.rotationManager.lookAtVec3d(hitVec);
/*     */     }
/*     */     
/* 293 */     rightClickBlock(neighbour, hitVec, hand, opposite, packet);
/* 294 */     mc.field_71439_g.func_184609_a(EnumHand.MAIN_HAND);
/* 295 */     mc.field_71467_ac = 4;
/* 296 */     return (sneaking || isSneaking);
/*     */   }
/*     */   
/*     */   public static void placeBlockStopSneaking(BlockPos pos, EnumHand hand, boolean rotate, boolean packet, boolean isSneaking) {
/* 300 */     boolean sneaking = placeBlockSmartRotate(pos, hand, rotate, packet, isSneaking);
/* 301 */     if (!isSneaking && sneaking) {
/* 302 */       mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketEntityAction((Entity)mc.field_71439_g, CPacketEntityAction.Action.STOP_SNEAKING));
/*     */     }
/*     */   }
/*     */   
/*     */   public static Vec3d[] getHelpingBlocks(Vec3d vec3d) {
/* 307 */     return new Vec3d[] { new Vec3d(vec3d.field_72450_a, vec3d.field_72448_b - 1.0D, vec3d.field_72449_c), new Vec3d((vec3d.field_72450_a != 0.0D) ? (vec3d.field_72450_a * 2.0D) : vec3d.field_72450_a, vec3d.field_72448_b, (vec3d.field_72450_a != 0.0D) ? vec3d.field_72449_c : (vec3d.field_72449_c * 2.0D)), new Vec3d((vec3d.field_72450_a == 0.0D) ? (vec3d.field_72450_a + 1.0D) : vec3d.field_72450_a, vec3d.field_72448_b, (vec3d.field_72450_a == 0.0D) ? vec3d.field_72449_c : (vec3d.field_72449_c + 1.0D)), new Vec3d((vec3d.field_72450_a == 0.0D) ? (vec3d.field_72450_a - 1.0D) : vec3d.field_72450_a, vec3d.field_72448_b, (vec3d.field_72450_a == 0.0D) ? vec3d.field_72449_c : (vec3d.field_72449_c - 1.0D)), new Vec3d(vec3d.field_72450_a, vec3d.field_72448_b + 1.0D, vec3d.field_72449_c) };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static List<BlockPos> possiblePlacePositions(float placeRange) {
/* 317 */     NonNullList<BlockPos> positions = NonNullList.func_191196_a();
/* 318 */     positions.addAll((Collection)getSphere(EntityUtil.getPlayerPos((EntityPlayer)mc.field_71439_g), placeRange, (int)placeRange, false, true, 0).stream().filter(BlockUtil::canPlaceCrystal).collect(Collectors.toList()));
/* 319 */     return (List<BlockPos>)positions;
/*     */   }
/*     */   
/*     */   public static List<BlockPos> getSphere(BlockPos pos, float r, int h, boolean hollow, boolean sphere, int plus_y) {
/* 323 */     List<BlockPos> circleblocks = new ArrayList<>();
/* 324 */     int cx = pos.func_177958_n();
/* 325 */     int cy = pos.func_177956_o();
/* 326 */     int cz = pos.func_177952_p();
/* 327 */     for (int x = cx - (int)r; x <= cx + r; x++) {
/* 328 */       for (int z = cz - (int)r; z <= cz + r; ) {
/* 329 */         int y = sphere ? (cy - (int)r) : cy; for (;; z++) { if (y < (sphere ? (cy + r) : (cy + h))) {
/* 330 */             double dist = ((cx - x) * (cx - x) + (cz - z) * (cz - z) + (sphere ? ((cy - y) * (cy - y)) : 0));
/* 331 */             if (dist < (r * r) && (!hollow || dist >= ((r - 1.0F) * (r - 1.0F)))) {
/* 332 */               BlockPos l = new BlockPos(x, y + plus_y, z);
/* 333 */               circleblocks.add(l);
/*     */             }  y++; continue;
/*     */           }  }
/*     */       
/*     */       } 
/* 338 */     }  return circleblocks;
/*     */   }
/*     */   
/*     */   public static boolean canPlaceCrystal(BlockPos blockPos) {
/* 342 */     BlockPos boost = blockPos.func_177982_a(0, 1, 0);
/* 343 */     BlockPos boost2 = blockPos.func_177982_a(0, 2, 0);
/*     */     try {
/* 345 */       return ((mc.field_71441_e.func_180495_p(blockPos).func_177230_c() == Blocks.field_150357_h || mc.field_71441_e
/* 346 */         .func_180495_p(blockPos).func_177230_c() == Blocks.field_150343_Z) && mc.field_71441_e
/* 347 */         .func_180495_p(boost).func_177230_c() == Blocks.field_150350_a && mc.field_71441_e
/* 348 */         .func_180495_p(boost2).func_177230_c() == Blocks.field_150350_a && mc.field_71441_e
/* 349 */         .func_72872_a(Entity.class, new AxisAlignedBB(boost)).isEmpty() && mc.field_71441_e
/* 350 */         .func_72872_a(Entity.class, new AxisAlignedBB(boost2)).isEmpty());
/* 351 */     } catch (Exception e) {
/* 352 */       return false;
/*     */     } 
/*     */   }
/*     */   
/*     */   public static List<BlockPos> possiblePlacePositions(float placeRange, boolean specialEntityCheck) {
/* 357 */     NonNullList<BlockPos> positions = NonNullList.func_191196_a();
/* 358 */     positions.addAll((Collection)getSphere(EntityUtil.getPlayerPos((EntityPlayer)mc.field_71439_g), placeRange, (int)placeRange, false, true, 0).stream().filter(pos -> canPlaceCrystal(pos, specialEntityCheck)).collect(Collectors.toList()));
/* 359 */     return (List<BlockPos>)positions;
/*     */   }
/*     */   
/*     */   public static boolean canPlaceCrystal(BlockPos blockPos, boolean specialEntityCheck) {
/* 363 */     BlockPos boost = blockPos.func_177982_a(0, 1, 0);
/* 364 */     BlockPos boost2 = blockPos.func_177982_a(0, 2, 0);
/*     */     try {
/* 366 */       if (mc.field_71441_e.func_180495_p(blockPos).func_177230_c() != Blocks.field_150357_h && mc.field_71441_e.func_180495_p(blockPos).func_177230_c() != Blocks.field_150343_Z) {
/* 367 */         return false;
/*     */       }
/*     */       
/* 370 */       if (mc.field_71441_e.func_180495_p(boost).func_177230_c() != Blocks.field_150350_a || mc.field_71441_e.func_180495_p(boost2).func_177230_c() != Blocks.field_150350_a) {
/* 371 */         return false;
/*     */       }
/*     */       
/* 374 */       if (specialEntityCheck) {
/* 375 */         for (Entity entity : mc.field_71441_e.func_72872_a(Entity.class, new AxisAlignedBB(boost))) {
/* 376 */           if (!(entity instanceof net.minecraft.entity.item.EntityEnderCrystal)) {
/* 377 */             return false;
/*     */           }
/*     */         } 
/*     */         
/* 381 */         for (Entity entity : mc.field_71441_e.func_72872_a(Entity.class, new AxisAlignedBB(boost2))) {
/* 382 */           if (!(entity instanceof net.minecraft.entity.item.EntityEnderCrystal)) {
/* 383 */             return false;
/*     */           }
/*     */         } 
/*     */       } else {
/* 387 */         return (mc.field_71441_e.func_72872_a(Entity.class, new AxisAlignedBB(boost)).isEmpty() && mc.field_71441_e
/* 388 */           .func_72872_a(Entity.class, new AxisAlignedBB(boost2)).isEmpty());
/*     */       } 
/* 390 */     } catch (Exception ignored) {
/* 391 */       return false;
/*     */     } 
/*     */     
/* 394 */     return true;
/*     */   }
/*     */   
/*     */   public static boolean canBeClicked(BlockPos pos) {
/* 398 */     return getBlock(pos).func_176209_a(getState(pos), false);
/*     */   }
/*     */   
/*     */   private static Block getBlock(BlockPos pos) {
/* 402 */     return getState(pos).func_177230_c();
/*     */   }
/*     */   
/*     */   private static IBlockState getState(BlockPos pos) {
/* 406 */     return mc.field_71441_e.func_180495_p(pos);
/*     */   }
/*     */   
/*     */   public static boolean isBlockAboveEntitySolid(Entity entity) {
/* 410 */     if (entity != null) {
/* 411 */       BlockPos pos = new BlockPos(entity.field_70165_t, entity.field_70163_u + 2.0D, entity.field_70161_v);
/* 412 */       return isBlockSolid(pos);
/*     */     } 
/* 414 */     return false;
/*     */   }
/*     */   
/*     */   public static void debugPos(String message, BlockPos pos) {
/* 418 */     Command.sendMessage(message + pos.func_177958_n() + "x, " + pos.func_177956_o() + "y, " + pos.func_177952_p() + "z");
/*     */   }
/*     */   
/*     */   public static void placeCrystalOnBlock(BlockPos pos, EnumHand hand) {
/* 422 */     RayTraceResult result = mc.field_71441_e.func_72933_a(new Vec3d(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u + mc.field_71439_g.func_70047_e(), mc.field_71439_g.field_70161_v), new Vec3d(pos.func_177958_n() + 0.5D, pos.func_177956_o() - 0.5D, pos.func_177952_p() + 0.5D));
/* 423 */     EnumFacing facing = (result == null || result.field_178784_b == null) ? EnumFacing.UP : result.field_178784_b;
/* 424 */     mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayerTryUseItemOnBlock(pos, facing, hand, 0.0F, 0.0F, 0.0F));
/*     */   }
/*     */   
/*     */   public static BlockPos[] toBlockPos(Vec3d[] vec3ds) {
/* 428 */     BlockPos[] list = new BlockPos[vec3ds.length];
/* 429 */     for (int i = 0; i < vec3ds.length; i++) {
/* 430 */       list[i] = new BlockPos(vec3ds[i]);
/*     */     }
/* 432 */     return list;
/*     */   }
/*     */   
/*     */   public static Vec3d posToVec3d(BlockPos pos) {
/* 436 */     return new Vec3d((Vec3i)pos);
/*     */   }
/*     */   
/*     */   public static BlockPos vec3dToPos(Vec3d vec3d) {
/* 440 */     return new BlockPos(vec3d);
/*     */   }
/*     */   
/*     */   public static Boolean isPosInFov(BlockPos pos) {
/* 444 */     int dirnumber = RotationUtil.getDirection4D();
/*     */     
/* 446 */     if (dirnumber == 0 && pos.func_177952_p() - (mc.field_71439_g.func_174791_d()).field_72449_c < 0.0D) {
/* 447 */       return Boolean.valueOf(false);
/*     */     }
/*     */     
/* 450 */     if (dirnumber == 1 && pos.func_177958_n() - (mc.field_71439_g.func_174791_d()).field_72450_a > 0.0D) {
/* 451 */       return Boolean.valueOf(false);
/*     */     }
/*     */     
/* 454 */     if (dirnumber == 2 && pos.func_177952_p() - (mc.field_71439_g.func_174791_d()).field_72449_c > 0.0D) {
/* 455 */       return Boolean.valueOf(false);
/*     */     }
/*     */     
/* 458 */     return Boolean.valueOf((dirnumber != 3 || pos.func_177958_n() - (mc.field_71439_g.func_174791_d()).field_72450_a >= 0.0D));
/*     */   }
/*     */   
/*     */   public static boolean isBlockBelowEntitySolid(Entity entity) {
/* 462 */     if (entity != null) {
/* 463 */       BlockPos pos = new BlockPos(entity.field_70165_t, entity.field_70163_u - 1.0D, entity.field_70161_v);
/* 464 */       return isBlockSolid(pos);
/*     */     } 
/* 466 */     return false;
/*     */   }
/*     */   
/*     */   public static boolean isBlockSolid(BlockPos pos) {
/* 470 */     return !isBlockUnSolid(pos);
/*     */   }
/*     */   
/*     */   public static boolean isBlockUnSolid(BlockPos pos) {
/* 474 */     return isBlockUnSolid(mc.field_71441_e.func_180495_p(pos).func_177230_c());
/*     */   }
/*     */   
/*     */   public static boolean isBlockUnSolid(Block block) {
/* 478 */     return unSolidBlocks.contains(block);
/*     */   }
/*     */   
/*     */   public static Vec3d[] convertVec3ds(Vec3d vec3d, Vec3d[] input) {
/* 482 */     Vec3d[] output = new Vec3d[input.length];
/* 483 */     for (int i = 0; i < input.length; i++) {
/* 484 */       output[i] = vec3d.func_178787_e(input[i]);
/*     */     }
/* 486 */     return output;
/*     */   }
/*     */   
/*     */   public static Vec3d[] convertVec3ds(EntityPlayer entity, Vec3d[] input) {
/* 490 */     return convertVec3ds(entity.func_174791_d(), input);
/*     */   }
/*     */   
/*     */   public static boolean canBreak(BlockPos pos) {
/* 494 */     IBlockState blockState = mc.field_71441_e.func_180495_p(pos);
/* 495 */     Block block = blockState.func_177230_c();
/* 496 */     return (block.func_176195_g(blockState, (World)mc.field_71441_e, pos) != -1.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean isValidBlock(BlockPos pos) {
/* 501 */     Block block = mc.field_71441_e.func_180495_p(pos).func_177230_c();
/* 502 */     return (!(block instanceof net.minecraft.block.BlockLiquid) && block.func_149688_o(null) != Material.field_151579_a);
/*     */   }
/*     */   
/*     */   public static boolean isScaffoldPos(BlockPos pos) {
/* 506 */     return (mc.field_71441_e.func_175623_d(pos) || mc.field_71441_e
/* 507 */       .func_180495_p(pos).func_177230_c() == Blocks.field_150431_aC || mc.field_71441_e
/* 508 */       .func_180495_p(pos).func_177230_c() == Blocks.field_150329_H || mc.field_71441_e
/* 509 */       .func_180495_p(pos).func_177230_c() instanceof net.minecraft.block.BlockLiquid);
/*     */   }
/*     */   
/*     */   public static boolean rayTracePlaceCheck(BlockPos pos, boolean shouldCheck, float height) {
/* 513 */     return (!shouldCheck || mc.field_71441_e.func_147447_a(new Vec3d(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u + mc.field_71439_g.func_70047_e(), mc.field_71439_g.field_70161_v), new Vec3d(pos.func_177958_n(), (pos.func_177956_o() + height), pos.func_177952_p()), false, true, false) == null);
/*     */   }
/*     */   
/*     */   public static boolean rayTracePlaceCheck(BlockPos pos, boolean shouldCheck) {
/* 517 */     return rayTracePlaceCheck(pos, shouldCheck, 1.0F);
/*     */   }
/*     */   
/*     */   public static boolean rayTracePlaceCheck(BlockPos pos) {
/* 521 */     return rayTracePlaceCheck(pos, true);
/*     */   }
/*     */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\me\earth\phobo\\util\BlockUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */