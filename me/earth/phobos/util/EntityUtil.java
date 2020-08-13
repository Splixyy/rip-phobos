/*     */ package me.earth.phobos.util;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.math.RoundingMode;
/*     */ import java.text.DecimalFormat;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import me.earth.phobos.Phobos;
/*     */ import me.earth.phobos.features.modules.client.Managers;
/*     */ import me.earth.phobos.features.modules.combat.Killaura;
/*     */ import me.earth.phobos.features.modules.player.Blink;
/*     */ import me.earth.phobos.features.modules.player.FakePlayer;
/*     */ import me.earth.phobos.features.modules.player.Freecam;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.enchantment.EnchantmentHelper;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.entity.EnumCreatureType;
/*     */ import net.minecraft.entity.monster.EntityEnderman;
/*     */ import net.minecraft.entity.monster.EntityIronGolem;
/*     */ import net.minecraft.entity.monster.EntityPigZombie;
/*     */ import net.minecraft.entity.passive.EntityWolf;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.init.Enchantments;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.nbt.NBTTagList;
/*     */ import net.minecraft.network.Packet;
/*     */ import net.minecraft.network.play.client.CPacketEntityAction;
/*     */ import net.minecraft.network.play.client.CPacketUseEntity;
/*     */ import net.minecraft.potion.Potion;
/*     */ import net.minecraft.potion.PotionEffect;
/*     */ import net.minecraft.util.EnumHand;
/*     */ import net.minecraft.util.MovementInput;
/*     */ import net.minecraft.util.math.AxisAlignedBB;
/*     */ import net.minecraft.util.math.BlockPos;
/*     */ import net.minecraft.util.math.MathHelper;
/*     */ import net.minecraft.util.math.Vec3d;
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
/*     */ public class EntityUtil
/*     */   implements Util
/*     */ {
/*  63 */   public static final Vec3d[] antiDropOffsetList = new Vec3d[] { new Vec3d(0.0D, -2.0D, 0.0D) };
/*     */ 
/*     */ 
/*     */   
/*  67 */   public static final Vec3d[] platformOffsetList = new Vec3d[] { new Vec3d(0.0D, -1.0D, 0.0D), new Vec3d(0.0D, -1.0D, -1.0D), new Vec3d(0.0D, -1.0D, 1.0D), new Vec3d(-1.0D, -1.0D, 0.0D), new Vec3d(1.0D, -1.0D, 0.0D) };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  75 */   public static final Vec3d[] legOffsetList = new Vec3d[] { new Vec3d(-1.0D, 0.0D, 0.0D), new Vec3d(1.0D, 0.0D, 0.0D), new Vec3d(0.0D, 0.0D, -1.0D), new Vec3d(0.0D, 0.0D, 1.0D) };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  82 */   public static final Vec3d[] OffsetList = new Vec3d[] { new Vec3d(1.0D, 1.0D, 0.0D), new Vec3d(-1.0D, 1.0D, 0.0D), new Vec3d(0.0D, 1.0D, 1.0D), new Vec3d(0.0D, 1.0D, -1.0D), new Vec3d(0.0D, 2.0D, 0.0D) };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  91 */   public static final Vec3d[] antiStepOffsetList = new Vec3d[] { new Vec3d(-1.0D, 2.0D, 0.0D), new Vec3d(1.0D, 2.0D, 0.0D), new Vec3d(0.0D, 2.0D, 1.0D), new Vec3d(0.0D, 2.0D, -1.0D) };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  98 */   public static final Vec3d[] antiScaffoldOffsetList = new Vec3d[] { new Vec3d(0.0D, 3.0D, 0.0D) };
/*     */ 
/*     */ 
/*     */   
/*     */   public static void attackEntity(Entity entity, boolean packet, boolean swingArm) {
/* 103 */     if (packet) {
/* 104 */       mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketUseEntity(entity));
/*     */     } else {
/* 106 */       mc.field_71442_b.func_78764_a((EntityPlayer)mc.field_71439_g, entity);
/*     */     } 
/*     */     
/* 109 */     if (swingArm) {
/* 110 */       mc.field_71439_g.func_184609_a(EnumHand.MAIN_HAND);
/*     */     }
/*     */   }
/*     */   
/*     */   public static Vec3d interpolateEntity(Entity entity, float time) {
/* 115 */     return new Vec3d(entity.field_70142_S + (entity.field_70165_t - entity.field_70142_S) * time, entity.field_70137_T + (entity.field_70163_u - entity.field_70137_T) * time, entity.field_70136_U + (entity.field_70161_v - entity.field_70136_U) * time);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static Vec3d getInterpolatedPos(Entity entity, float partialTicks) {
/* 121 */     return (new Vec3d(entity.field_70142_S, entity.field_70137_T, entity.field_70136_U)).func_178787_e(getInterpolatedAmount(entity, partialTicks));
/*     */   }
/*     */   
/*     */   public static Vec3d getInterpolatedRenderPos(Entity entity, float partialTicks) {
/* 125 */     return getInterpolatedPos(entity, partialTicks).func_178786_a((mc.func_175598_ae()).field_78725_b, (mc.func_175598_ae()).field_78726_c, (mc.func_175598_ae()).field_78723_d);
/*     */   }
/*     */   
/*     */   public static Vec3d getInterpolatedRenderPos(Vec3d vec) {
/* 129 */     return (new Vec3d(vec.field_72450_a, vec.field_72448_b, vec.field_72449_c)).func_178786_a((mc.func_175598_ae()).field_78725_b, (mc.func_175598_ae()).field_78726_c, (mc.func_175598_ae()).field_78723_d);
/*     */   }
/*     */   
/*     */   public static Vec3d getInterpolatedAmount(Entity entity, double x, double y, double z) {
/* 133 */     return new Vec3d((entity.field_70165_t - entity.field_70142_S) * x, (entity.field_70163_u - entity.field_70137_T) * y, (entity.field_70161_v - entity.field_70136_U) * z);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Vec3d getInterpolatedAmount(Entity entity, Vec3d vec) {
/* 141 */     return getInterpolatedAmount(entity, vec.field_72450_a, vec.field_72448_b, vec.field_72449_c);
/*     */   }
/*     */   
/*     */   public static Vec3d getInterpolatedAmount(Entity entity, float partialTicks) {
/* 145 */     return getInterpolatedAmount(entity, partialTicks, partialTicks, partialTicks);
/*     */   }
/*     */   
/*     */   public static boolean isPassive(Entity entity) {
/* 149 */     if (entity instanceof EntityWolf && ((EntityWolf)entity).func_70919_bu()) return false; 
/* 150 */     if (entity instanceof net.minecraft.entity.EntityAgeable || entity instanceof net.minecraft.entity.passive.EntityAmbientCreature || entity instanceof net.minecraft.entity.passive.EntitySquid) return true; 
/* 151 */     return (entity instanceof EntityIronGolem && ((EntityIronGolem)entity).func_70643_av() == null);
/*     */   }
/*     */   
/*     */   public static boolean isSafe(Entity entity, int height, boolean floor) {
/* 155 */     return (getUnsafeBlocks(entity, height, floor).size() == 0);
/*     */   }
/*     */   
/*     */   public static boolean stopSneaking(boolean isSneaking) {
/* 159 */     if (isSneaking && mc.field_71439_g != null) {
/* 160 */       mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketEntityAction((Entity)mc.field_71439_g, CPacketEntityAction.Action.STOP_SNEAKING));
/*     */     }
/* 162 */     return false;
/*     */   }
/*     */   public static boolean isSafe(Entity entity) {
/* 165 */     return isSafe(entity, 0, false);
/*     */   }
/*     */   public static BlockPos getPlayerPos(EntityPlayer player) {
/* 168 */     return new BlockPos(Math.floor(player.field_70165_t), Math.floor(player.field_70163_u), Math.floor(player.field_70161_v));
/*     */   }
/*     */   
/*     */   public static List<Vec3d> getUnsafeBlocks(Entity entity, int height, boolean floor) {
/* 172 */     return getUnsafeBlocksFromVec3d(entity.func_174791_d(), height, floor);
/*     */   }
/*     */   
/*     */   public static boolean isMobAggressive(Entity entity) {
/* 176 */     if (entity instanceof EntityPigZombie) {
/* 177 */       if (((EntityPigZombie)entity).func_184734_db() || ((EntityPigZombie)entity).func_175457_ck())
/* 178 */         return true; 
/*     */     } else {
/* 180 */       if (entity instanceof EntityWolf)
/* 181 */         return (((EntityWolf)entity).func_70919_bu() && 
/* 182 */           !mc.field_71439_g.equals(((EntityWolf)entity).func_70902_q())); 
/* 183 */       if (entity instanceof EntityEnderman)
/* 184 */         return ((EntityEnderman)entity).func_70823_r(); 
/*     */     } 
/* 186 */     return isHostileMob(entity);
/*     */   }
/*     */   
/*     */   public static boolean isNeutralMob(Entity entity) {
/* 190 */     return (entity instanceof EntityPigZombie || entity instanceof EntityWolf || entity instanceof EntityEnderman);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isProjectile(Entity entity) {
/* 196 */     return (entity instanceof net.minecraft.entity.projectile.EntityShulkerBullet || entity instanceof net.minecraft.entity.projectile.EntityFireball);
/*     */   }
/*     */   
/*     */   public static boolean isVehicle(Entity entity) {
/* 200 */     return (entity instanceof net.minecraft.entity.item.EntityBoat || entity instanceof net.minecraft.entity.item.EntityMinecart);
/*     */   }
/*     */   
/*     */   public static boolean isFriendlyMob(Entity entity) {
/* 204 */     return ((entity.isCreatureType(EnumCreatureType.CREATURE, false) && !isNeutralMob(entity)) || entity
/* 205 */       .isCreatureType(EnumCreatureType.AMBIENT, false) || entity instanceof net.minecraft.entity.passive.EntityVillager || entity instanceof EntityIronGolem || (
/*     */ 
/*     */       
/* 208 */       isNeutralMob(entity) && !isMobAggressive(entity)));
/*     */   }
/*     */   
/*     */   public static boolean isHostileMob(Entity entity) {
/* 212 */     return (entity.isCreatureType(EnumCreatureType.MONSTER, false) && !isNeutralMob(entity));
/*     */   }
/*     */   
/*     */   public static List<Vec3d> getUnsafeBlocksFromVec3d(Vec3d pos, int height, boolean floor) {
/* 216 */     List<Vec3d> vec3ds = new ArrayList<>();
/* 217 */     for (Vec3d vector : getOffsets(height, floor)) {
/* 218 */       BlockPos targetPos = (new BlockPos(pos)).func_177963_a(vector.field_72450_a, vector.field_72448_b, vector.field_72449_c);
/* 219 */       Block block = mc.field_71441_e.func_180495_p(targetPos).func_177230_c();
/* 220 */       if (block instanceof net.minecraft.block.BlockAir || block instanceof net.minecraft.block.BlockLiquid || block instanceof net.minecraft.block.BlockTallGrass || block instanceof net.minecraft.block.BlockFire || block instanceof net.minecraft.block.BlockDeadBush || block instanceof net.minecraft.block.BlockSnow) {
/* 221 */         vec3ds.add(vector);
/*     */       }
/*     */     } 
/* 224 */     return vec3ds;
/*     */   }
/*     */   public static boolean isInHole(Entity entity) {
/* 227 */     return isBlockValid(new BlockPos(entity.field_70165_t, entity.field_70163_u, entity.field_70161_v));
/*     */   }
/*     */   
/*     */   public static boolean isBlockValid(BlockPos blockPos) {
/* 231 */     return (isBedrockHole(blockPos) || isObbyHole(blockPos) || isBothHole(blockPos));
/*     */   }
/*     */   
/*     */   public static boolean isObbyHole(BlockPos blockPos) {
/* 235 */     BlockPos[] touchingBlocks = { blockPos.func_177978_c(), blockPos.func_177968_d(), blockPos.func_177974_f(), blockPos.func_177976_e(), blockPos.func_177977_b() };
/* 236 */     for (BlockPos pos : touchingBlocks) {
/* 237 */       IBlockState touchingState = mc.field_71441_e.func_180495_p(pos);
/* 238 */       if (touchingState.func_177230_c() == Blocks.field_150350_a || touchingState.func_177230_c() != Blocks.field_150343_Z) {
/* 239 */         return false;
/*     */       }
/*     */     } 
/*     */     
/* 243 */     return true;
/*     */   }
/*     */   
/*     */   public static boolean isBedrockHole(BlockPos blockPos) {
/* 247 */     BlockPos[] touchingBlocks = { blockPos.func_177978_c(), blockPos.func_177968_d(), blockPos.func_177974_f(), blockPos.func_177976_e(), blockPos.func_177977_b() };
/* 248 */     for (BlockPos pos : touchingBlocks) {
/* 249 */       IBlockState touchingState = mc.field_71441_e.func_180495_p(pos);
/* 250 */       if (touchingState.func_177230_c() == Blocks.field_150350_a || touchingState.func_177230_c() != Blocks.field_150357_h) {
/* 251 */         return false;
/*     */       }
/*     */     } 
/*     */     
/* 255 */     return true;
/*     */   }
/*     */   
/*     */   public static boolean isBothHole(BlockPos blockPos) {
/* 259 */     BlockPos[] touchingBlocks = { blockPos.func_177978_c(), blockPos.func_177968_d(), blockPos.func_177974_f(), blockPos.func_177976_e(), blockPos.func_177977_b() };
/* 260 */     for (BlockPos pos : touchingBlocks) {
/* 261 */       IBlockState touchingState = mc.field_71441_e.func_180495_p(pos);
/* 262 */       if (touchingState.func_177230_c() == Blocks.field_150350_a || (touchingState.func_177230_c() != Blocks.field_150357_h && touchingState.func_177230_c() != Blocks.field_150343_Z)) {
/* 263 */         return false;
/*     */       }
/*     */     } 
/* 266 */     return true;
/*     */   }
/*     */   public static Vec3d[] getUnsafeBlockArray(Entity entity, int height, boolean floor) {
/* 269 */     List<Vec3d> list = getUnsafeBlocks(entity, height, floor);
/* 270 */     Vec3d[] array = new Vec3d[list.size()];
/* 271 */     return list.<Vec3d>toArray(array);
/*     */   }
/*     */   
/*     */   public static Vec3d[] getUnsafeBlockArrayFromVec3d(Vec3d pos, int height, boolean floor) {
/* 275 */     List<Vec3d> list = getUnsafeBlocksFromVec3d(pos, height, floor);
/* 276 */     Vec3d[] array = new Vec3d[list.size()];
/* 277 */     return list.<Vec3d>toArray(array);
/*     */   }
/*     */   
/*     */   public static double getDst(Vec3d vec) {
/* 281 */     return mc.field_71439_g.func_174791_d().func_72438_d(vec);
/*     */   }
/*     */   
/*     */   public static boolean isTrapped(EntityPlayer player, boolean antiScaffold, boolean antiStep, boolean legs, boolean platform, boolean antiDrop) {
/* 285 */     return (getUntrappedBlocks(player, antiScaffold, antiStep, legs, platform, antiDrop).size() == 0);
/*     */   }
/*     */   
/*     */   public static boolean isTrappedExtended(int extension, EntityPlayer player, boolean antiScaffold, boolean antiStep, boolean legs, boolean platform, boolean antiDrop, boolean raytrace) {
/* 289 */     return (getUntrappedBlocksExtended(extension, player, antiScaffold, antiStep, legs, platform, antiDrop, raytrace).size() == 0);
/*     */   }
/*     */   
/*     */   public static List<Vec3d> getUntrappedBlocks(EntityPlayer player, boolean antiScaffold, boolean antiStep, boolean legs, boolean platform, boolean antiDrop) {
/* 293 */     List<Vec3d> vec3ds = new ArrayList<>();
/* 294 */     if (!antiStep && getUnsafeBlocks((Entity)player, 2, false).size() == 4) {
/* 295 */       vec3ds.addAll(getUnsafeBlocks((Entity)player, 2, false));
/*     */     }
/* 297 */     for (int i = 0; i < (getTrapOffsets(antiScaffold, antiStep, legs, platform, antiDrop)).length; i++) {
/* 298 */       Vec3d vector = getTrapOffsets(antiScaffold, antiStep, legs, platform, antiDrop)[i];
/* 299 */       BlockPos targetPos = (new BlockPos(player.func_174791_d())).func_177963_a(vector.field_72450_a, vector.field_72448_b, vector.field_72449_c);
/* 300 */       Block block = mc.field_71441_e.func_180495_p(targetPos).func_177230_c();
/* 301 */       if (block instanceof net.minecraft.block.BlockAir || block instanceof net.minecraft.block.BlockLiquid || block instanceof net.minecraft.block.BlockTallGrass || block instanceof net.minecraft.block.BlockFire || block instanceof net.minecraft.block.BlockDeadBush || block instanceof net.minecraft.block.BlockSnow) {
/* 302 */         vec3ds.add(vector);
/*     */       }
/*     */     } 
/* 305 */     return vec3ds;
/*     */   }
/*     */   
/*     */   public static boolean isInWater(Entity entity) {
/* 309 */     if (entity == null) return false;
/*     */     
/* 311 */     double y = entity.field_70163_u + 0.01D;
/*     */     
/* 313 */     for (int x = MathHelper.func_76128_c(entity.field_70165_t); x < MathHelper.func_76143_f(entity.field_70165_t); x++) {
/* 314 */       for (int z = MathHelper.func_76128_c(entity.field_70161_v); z < MathHelper.func_76143_f(entity.field_70161_v); z++) {
/* 315 */         BlockPos pos = new BlockPos(x, (int)y, z);
/*     */         
/* 317 */         if (mc.field_71441_e.func_180495_p(pos).func_177230_c() instanceof net.minecraft.block.BlockLiquid) return true; 
/*     */       } 
/*     */     } 
/* 320 */     return false;
/*     */   }
/*     */   
/*     */   public static boolean isDrivenByPlayer(Entity entityIn) {
/* 324 */     return (mc.field_71439_g != null && entityIn != null && entityIn.equals(mc.field_71439_g.func_184187_bx()));
/*     */   }
/*     */   
/*     */   public static boolean isPlayer(Entity entity) {
/* 328 */     return entity instanceof EntityPlayer;
/*     */   }
/*     */   public static boolean isAboveWater(Entity entity) {
/* 331 */     return isAboveWater(entity, false);
/*     */   }
/*     */   public static boolean isAboveWater(Entity entity, boolean packet) {
/* 334 */     if (entity == null) return false;
/*     */     
/* 336 */     double y = entity.field_70163_u - (packet ? 0.03D : (isPlayer(entity) ? 0.2D : 0.5D));
/*     */     
/* 338 */     for (int x = MathHelper.func_76128_c(entity.field_70165_t); x < MathHelper.func_76143_f(entity.field_70165_t); x++) {
/* 339 */       for (int z = MathHelper.func_76128_c(entity.field_70161_v); z < MathHelper.func_76143_f(entity.field_70161_v); z++) {
/* 340 */         BlockPos pos = new BlockPos(x, MathHelper.func_76128_c(y), z);
/*     */         
/* 342 */         if (mc.field_71441_e.func_180495_p(pos).func_177230_c() instanceof net.minecraft.block.BlockLiquid) return true; 
/*     */       } 
/*     */     } 
/* 345 */     return false;
/*     */   }
/*     */   
/*     */   public static List<Vec3d> getUntrappedBlocksExtended(int extension, EntityPlayer player, boolean antiScaffold, boolean antiStep, boolean legs, boolean platform, boolean antiDrop, boolean raytrace) {
/* 349 */     List<Vec3d> placeTargets = new ArrayList<>();
/* 350 */     if (extension == 1) {
/* 351 */       placeTargets.addAll(targets(player.func_174791_d(), antiScaffold, antiStep, legs, platform, antiDrop, raytrace));
/*     */     } else {
/* 353 */       int extend = 1;
/* 354 */       for (Vec3d vec3d : MathUtil.getBlockBlocks((Entity)player)) {
/* 355 */         if (extend > extension) {
/*     */           break;
/*     */         }
/* 358 */         placeTargets.addAll(targets(vec3d, antiScaffold, antiStep, legs, platform, antiDrop, raytrace));
/* 359 */         extend++;
/*     */       } 
/*     */     } 
/*     */     
/* 363 */     List<Vec3d> removeList = new ArrayList<>();
/* 364 */     for (Vec3d vec3d : placeTargets) {
/* 365 */       BlockPos pos = new BlockPos(vec3d);
/* 366 */       if (BlockUtil.isPositionPlaceable(pos, raytrace) == -1) {
/* 367 */         removeList.add(vec3d);
/*     */       }
/*     */     } 
/*     */     
/* 371 */     for (Vec3d vec3d : removeList) {
/* 372 */       placeTargets.remove(vec3d);
/*     */     }
/*     */     
/* 375 */     return placeTargets;
/*     */   }
/*     */   
/*     */   public static List<Vec3d> targets(Vec3d vec3d, boolean antiScaffold, boolean antiStep, boolean legs, boolean platform, boolean antiDrop, boolean raytrace) {
/* 379 */     List<Vec3d> placeTargets = new ArrayList<>();
/* 380 */     if (antiDrop) {
/* 381 */       Collections.addAll(placeTargets, BlockUtil.convertVec3ds(vec3d, antiDropOffsetList));
/*     */     }
/*     */     
/* 384 */     if (platform) {
/* 385 */       Collections.addAll(placeTargets, BlockUtil.convertVec3ds(vec3d, platformOffsetList));
/*     */     }
/*     */     
/* 388 */     if (legs) {
/* 389 */       Collections.addAll(placeTargets, BlockUtil.convertVec3ds(vec3d, legOffsetList));
/*     */     }
/*     */     
/* 392 */     Collections.addAll(placeTargets, BlockUtil.convertVec3ds(vec3d, OffsetList));
/*     */     
/* 394 */     if (antiStep)
/* 395 */     { Collections.addAll(placeTargets, BlockUtil.convertVec3ds(vec3d, antiStepOffsetList)); }
/*     */     else
/* 397 */     { List<Vec3d> vec3ds = getUnsafeBlocksFromVec3d(vec3d, 2, false);
/* 398 */       if (vec3ds.size() == 4)
/* 399 */       { Iterator<Vec3d> iterator = vec3ds.iterator(); while (true) { if (iterator.hasNext()) { Vec3d vector = iterator.next();
/* 400 */             BlockPos position = (new BlockPos(vec3d)).func_177963_a(vector.field_72450_a, vector.field_72448_b, vector.field_72449_c);
/* 401 */             switch (BlockUtil.isPositionPlaceable(position, raytrace)) {
/*     */               case 0:
/*     */                 break;
/*     */               case -1:
/*     */               case 1:
/*     */               case 2:
/*     */                 continue;
/*     */               case 3:
/* 409 */                 placeTargets.add(vec3d.func_178787_e(vector));
/*     */                 break;
/*     */               default:
/*     */                 break;
/*     */             }  }
/*     */           else
/*     */           { break; }
/*     */           
/* 417 */           if (antiScaffold) {
/* 418 */             Collections.addAll(placeTargets, BlockUtil.convertVec3ds(vec3d, antiScaffoldOffsetList));
/*     */           }
/* 420 */           return placeTargets; }  }  }  if (antiScaffold) Collections.addAll(placeTargets, BlockUtil.convertVec3ds(vec3d, antiScaffoldOffsetList));  return placeTargets;
/*     */   }
/*     */   
/*     */   public static List<Vec3d> getOffsetList(int y, boolean floor) {
/* 424 */     List<Vec3d> offsets = new ArrayList<>();
/* 425 */     offsets.add(new Vec3d(-1.0D, y, 0.0D));
/* 426 */     offsets.add(new Vec3d(1.0D, y, 0.0D));
/* 427 */     offsets.add(new Vec3d(0.0D, y, -1.0D));
/* 428 */     offsets.add(new Vec3d(0.0D, y, 1.0D));
/*     */     
/* 430 */     if (floor) {
/* 431 */       offsets.add(new Vec3d(0.0D, (y - 1), 0.0D));
/*     */     }
/*     */     
/* 434 */     return offsets;
/*     */   }
/*     */   
/*     */   public static Vec3d[] getOffsets(int y, boolean floor) {
/* 438 */     List<Vec3d> offsets = getOffsetList(y, floor);
/* 439 */     Vec3d[] array = new Vec3d[offsets.size()];
/* 440 */     return offsets.<Vec3d>toArray(array);
/*     */   }
/*     */   
/*     */   public static Vec3d[] getTrapOffsets(boolean antiScaffold, boolean antiStep, boolean legs, boolean platform, boolean antiDrop) {
/* 444 */     List<Vec3d> offsets = getTrapOffsetsList(antiScaffold, antiStep, legs, platform, antiDrop);
/* 445 */     Vec3d[] array = new Vec3d[offsets.size()];
/* 446 */     return offsets.<Vec3d>toArray(array);
/*     */   }
/*     */   
/*     */   public static List<Vec3d> getTrapOffsetsList(boolean antiScaffold, boolean antiStep, boolean legs, boolean platform, boolean antiDrop) {
/* 450 */     List<Vec3d> offsets = new ArrayList<>(getOffsetList(1, false));
/* 451 */     offsets.add(new Vec3d(0.0D, 2.0D, 0.0D));
/* 452 */     if (antiScaffold) {
/* 453 */       offsets.add(new Vec3d(0.0D, 3.0D, 0.0D));
/*     */     }
/* 455 */     if (antiStep) {
/* 456 */       offsets.addAll(getOffsetList(2, false));
/*     */     }
/* 458 */     if (legs) {
/* 459 */       offsets.addAll(getOffsetList(0, false));
/*     */     }
/* 461 */     if (platform) {
/* 462 */       offsets.addAll(getOffsetList(-1, false));
/* 463 */       offsets.add(new Vec3d(0.0D, -1.0D, 0.0D));
/*     */     } 
/* 465 */     if (antiDrop) {
/* 466 */       offsets.add(new Vec3d(0.0D, -2.0D, 0.0D));
/*     */     }
/* 468 */     return offsets;
/*     */   }
/*     */   
/*     */   public static Vec3d[] getHeightOffsets(int min, int max) {
/* 472 */     List<Vec3d> offsets = new ArrayList<>();
/* 473 */     for (int i = min; i <= max; i++) {
/* 474 */       offsets.add(new Vec3d(0.0D, i, 0.0D));
/*     */     }
/* 476 */     Vec3d[] array = new Vec3d[offsets.size()];
/* 477 */     return offsets.<Vec3d>toArray(array);
/*     */   }
/*     */   
/*     */   public static BlockPos getRoundedBlockPos(Entity entity) {
/* 481 */     return new BlockPos(MathUtil.roundVec(entity.func_174791_d(), 0));
/*     */   }
/*     */   
/*     */   public static boolean isLiving(Entity entity) {
/* 485 */     return entity instanceof EntityLivingBase;
/*     */   }
/*     */   
/*     */   public static boolean isAlive(Entity entity) {
/* 489 */     return (isLiving(entity) && !entity.field_70128_L && ((EntityLivingBase)entity).func_110143_aJ() > 0.0F);
/*     */   }
/*     */   
/*     */   public static boolean isDead(Entity entity) {
/* 493 */     return !isAlive(entity);
/*     */   }
/*     */   
/*     */   public static float getHealth(Entity entity) {
/* 497 */     if (isLiving(entity)) {
/* 498 */       EntityLivingBase livingBase = (EntityLivingBase)entity;
/* 499 */       return livingBase.func_110143_aJ() + livingBase.func_110139_bj();
/*     */     } 
/* 501 */     return 0.0F;
/*     */   }
/*     */   
/*     */   public static float getHealth(Entity entity, boolean absorption) {
/* 505 */     if (isLiving(entity)) {
/* 506 */       EntityLivingBase livingBase = (EntityLivingBase)entity;
/* 507 */       return livingBase.func_110143_aJ() + (absorption ? livingBase.func_110139_bj() : 0.0F);
/*     */     } 
/* 509 */     return 0.0F;
/*     */   }
/*     */   
/*     */   public static boolean canEntityFeetBeSeen(Entity entityIn) {
/* 513 */     return (mc.field_71441_e.func_147447_a(new Vec3d(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70165_t + mc.field_71439_g.func_70047_e(), mc.field_71439_g.field_70161_v), new Vec3d(entityIn.field_70165_t, entityIn.field_70163_u, entityIn.field_70161_v), false, true, false) == null);
/*     */   }
/*     */   
/*     */   public static boolean isntValid(Entity entity, double range) {
/* 517 */     return (entity == null || isDead(entity) || entity.equals(mc.field_71439_g) || (entity instanceof EntityPlayer && Phobos.friendManager.isFriend(entity.func_70005_c_())) || mc.field_71439_g.func_70068_e(entity) > MathUtil.square(range));
/*     */   }
/*     */   
/*     */   public static boolean isValid(Entity entity, double range) {
/* 521 */     return !isntValid(entity, range);
/*     */   }
/*     */   
/*     */   public static boolean holdingWeapon(EntityPlayer player) {
/* 525 */     return (player.func_184614_ca().func_77973_b() instanceof net.minecraft.item.ItemSword || player.func_184614_ca().func_77973_b() instanceof net.minecraft.item.ItemAxe);
/*     */   }
/*     */   
/*     */   public static double getMaxSpeed() {
/* 529 */     double maxModifier = 0.2873D;
/* 530 */     if (mc.field_71439_g.func_70644_a(Objects.<Potion>requireNonNull(Potion.func_188412_a(1)))) {
/* 531 */       maxModifier *= 1.0D + 0.2D * (((PotionEffect)Objects.<PotionEffect>requireNonNull(mc.field_71439_g.func_70660_b(Objects.<Potion>requireNonNull(Potion.func_188412_a(1))))).func_76458_c() + 1);
/*     */     }
/* 533 */     return maxModifier;
/*     */   }
/*     */   
/*     */   public static void mutliplyEntitySpeed(Entity entity, double multiplier) {
/* 537 */     if (entity != null) {
/* 538 */       entity.field_70159_w *= multiplier;
/* 539 */       entity.field_70179_y *= multiplier;
/*     */     } 
/*     */   }
/*     */   
/*     */   public static boolean isEntityMoving(Entity entity) {
/* 544 */     if (entity == null) {
/* 545 */       return false;
/*     */     }
/* 547 */     if (entity instanceof EntityPlayer) {
/* 548 */       return (mc.field_71474_y.field_74351_w.func_151470_d() || mc.field_71474_y.field_74368_y.func_151470_d() || mc.field_71474_y.field_74370_x.func_151470_d() || mc.field_71474_y.field_74366_z.func_151470_d());
/*     */     }
/* 550 */     return (entity.field_70159_w != 0.0D || entity.field_70181_x != 0.0D || entity.field_70179_y != 0.0D);
/*     */   }
/*     */   
/*     */   public static double getEntitySpeed(Entity entity) {
/* 554 */     if (entity != null) {
/* 555 */       double distTraveledLastTickX = entity.field_70165_t - entity.field_70169_q;
/* 556 */       double distTraveledLastTickZ = entity.field_70161_v - entity.field_70166_s;
/* 557 */       double speed = MathHelper.func_76133_a(distTraveledLastTickX * distTraveledLastTickX + distTraveledLastTickZ * distTraveledLastTickZ);
/* 558 */       return speed * 20.0D;
/*     */     } 
/* 560 */     return 0.0D;
/*     */   }
/*     */   
/*     */   public static boolean holding32k(EntityPlayer player) {
/* 564 */     return is32k(player.func_184614_ca());
/*     */   }
/*     */   
/*     */   public static boolean is32k(ItemStack stack) {
/* 568 */     if (stack == null) {
/* 569 */       return false;
/*     */     }
/*     */     
/* 572 */     if (stack.func_77978_p() == null) {
/* 573 */       return false;
/*     */     }
/* 575 */     NBTTagList enchants = (NBTTagList)stack.func_77978_p().func_74781_a("ench");
/* 576 */     if (enchants == null) {
/* 577 */       return false;
/*     */     }
/*     */     
/* 580 */     for (int i = 0; i < enchants.func_74745_c(); i++) {
/* 581 */       NBTTagCompound enchant = enchants.func_150305_b(i);
/* 582 */       if (enchant.func_74762_e("id") == 16) {
/* 583 */         int lvl = enchant.func_74762_e("lvl");
/* 584 */         if (lvl >= 42) {
/* 585 */           return true;
/*     */         }
/*     */         break;
/*     */       } 
/*     */     } 
/* 590 */     return false;
/*     */   }
/*     */   
/*     */   public static boolean simpleIs32k(ItemStack stack) {
/* 594 */     return (EnchantmentHelper.func_77506_a(Enchantments.field_185302_k, stack) >= 1000);
/*     */   }
/*     */   
/*     */   public static void moveEntityStrafe(double speed, Entity entity) {
/* 598 */     if (entity != null) {
/* 599 */       MovementInput movementInput = mc.field_71439_g.field_71158_b;
/* 600 */       double forward = movementInput.field_192832_b;
/* 601 */       double strafe = movementInput.field_78902_a;
/* 602 */       float yaw = mc.field_71439_g.field_70177_z;
/* 603 */       if (forward == 0.0D && strafe == 0.0D) {
/* 604 */         entity.field_70159_w = 0.0D;
/* 605 */         entity.field_70179_y = 0.0D;
/*     */       } else {
/*     */         
/* 608 */         if (forward != 0.0D) {
/* 609 */           if (strafe > 0.0D) {
/* 610 */             yaw += ((forward > 0.0D) ? -45 : 45);
/*     */           }
/* 612 */           else if (strafe < 0.0D) {
/* 613 */             yaw += ((forward > 0.0D) ? 45 : -45);
/*     */           } 
/* 615 */           strafe = 0.0D;
/* 616 */           if (forward > 0.0D) {
/* 617 */             forward = 1.0D;
/*     */           }
/* 619 */           else if (forward < 0.0D) {
/* 620 */             forward = -1.0D;
/*     */           } 
/*     */         } 
/* 623 */         entity.field_70159_w = forward * speed * Math.cos(Math.toRadians((yaw + 90.0F))) + strafe * speed * Math.sin(Math.toRadians((yaw + 90.0F)));
/* 624 */         entity.field_70179_y = forward * speed * Math.sin(Math.toRadians((yaw + 90.0F))) - strafe * speed * Math.cos(Math.toRadians((yaw + 90.0F)));
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public static boolean rayTraceHitCheck(Entity entity, boolean shouldCheck) {
/* 630 */     return (!shouldCheck || mc.field_71439_g.func_70685_l(entity));
/*     */   }
/*     */   
/*     */   public static Color getColor(Entity entity, int red, int green, int blue, int alpha, boolean colorFriends) {
/* 634 */     Color color = new Color(red / 255.0F, green / 255.0F, blue / 255.0F, alpha / 255.0F);
/* 635 */     if (entity instanceof EntityPlayer) {
/* 636 */       if (colorFriends && Phobos.friendManager.isFriend((EntityPlayer)entity)) {
/* 637 */         color = new Color(0.33333334F, 1.0F, 1.0F, alpha / 255.0F);
/*     */       }
/*     */       
/* 640 */       Killaura killaura = (Killaura)Phobos.moduleManager.getModuleByClass(Killaura.class);
/* 641 */       if (((Boolean)killaura.info.getValue()).booleanValue()) if (Killaura.target != null) if (Killaura.target.equals(entity)) {
/* 642 */             color = new Color(1.0F, 0.0F, 0.0F, alpha / 255.0F);
/*     */           }  
/*     */     } 
/* 645 */     return color;
/*     */   }
/*     */   
/*     */   public static boolean isFakePlayer(EntityPlayer player) {
/* 649 */     Freecam freecam = Freecam.getInstance();
/* 650 */     FakePlayer fakePlayer = FakePlayer.getInstance();
/* 651 */     Blink blink = Blink.getInstance();
/* 652 */     int playerID = player.func_145782_y();
/* 653 */     if (freecam.isOn() && playerID == 69420) {
/* 654 */       return true;
/*     */     }
/*     */     
/* 657 */     if (fakePlayer.isOn()) {
/* 658 */       for (Iterator<Integer> iterator = fakePlayer.fakePlayerIdList.iterator(); iterator.hasNext(); ) { int id = ((Integer)iterator.next()).intValue();
/* 659 */         if (id == playerID) {
/* 660 */           return true;
/*     */         } }
/*     */     
/*     */     }
/*     */     
/* 665 */     if (blink.isOn()) {
/* 666 */       return (playerID == 6942069);
/*     */     }
/*     */     
/* 669 */     return false;
/*     */   }
/*     */   
/*     */   public static boolean isMoving() {
/* 673 */     return (mc.field_71439_g.field_191988_bg != 0.0D || mc.field_71439_g.field_70702_br != 0.0D);
/*     */   }
/*     */   
/*     */   public static EntityPlayer getClosestEnemy(double distance) {
/* 677 */     EntityPlayer closest = null;
/* 678 */     for (EntityPlayer player : mc.field_71441_e.field_73010_i) {
/* 679 */       if (isntValid((Entity)player, distance)) {
/*     */         continue;
/*     */       }
/*     */       
/* 683 */       if (closest == null) {
/* 684 */         closest = player;
/*     */         
/*     */         continue;
/*     */       } 
/* 688 */       if (mc.field_71439_g.func_70068_e((Entity)player) < mc.field_71439_g.func_70068_e((Entity)closest)) {
/* 689 */         closest = player;
/*     */       }
/*     */     } 
/* 692 */     return closest;
/*     */   }
/*     */   
/*     */   public static boolean checkCollide() {
/* 696 */     if (mc.field_71439_g.func_70093_af()) {
/* 697 */       return false;
/*     */     }
/*     */     
/* 700 */     if (mc.field_71439_g.func_184187_bx() != null && (mc.field_71439_g.func_184187_bx()).field_70143_R >= 3.0F) {
/* 701 */       return false;
/*     */     }
/*     */     
/* 704 */     return (mc.field_71439_g.field_70143_R < 3.0F);
/*     */   }
/*     */   
/*     */   public static boolean isInLiquid() {
/* 708 */     if (mc.field_71439_g.field_70143_R >= 3.0F) {
/* 709 */       return false;
/*     */     }
/*     */     
/* 712 */     boolean inLiquid = false;
/* 713 */     AxisAlignedBB bb = (mc.field_71439_g.func_184187_bx() != null) ? mc.field_71439_g.func_184187_bx().func_174813_aQ() : mc.field_71439_g.func_174813_aQ();
/* 714 */     int y = (int)bb.field_72338_b;
/* 715 */     for (int x = MathHelper.func_76128_c(bb.field_72340_a); x < MathHelper.func_76128_c(bb.field_72336_d) + 1; x++) {
/* 716 */       for (int z = MathHelper.func_76128_c(bb.field_72339_c); z < MathHelper.func_76128_c(bb.field_72334_f) + 1; z++) {
/* 717 */         Block block = mc.field_71441_e.func_180495_p(new BlockPos(x, y, z)).func_177230_c();
/* 718 */         if (!(block instanceof net.minecraft.block.BlockAir)) {
/* 719 */           if (!(block instanceof net.minecraft.block.BlockLiquid)) {
/* 720 */             return false;
/*     */           }
/* 722 */           inLiquid = true;
/*     */         } 
/*     */       } 
/*     */     } 
/* 726 */     return inLiquid;
/*     */   }
/*     */   
/*     */   public static boolean isOnLiquid(double offset) {
/* 730 */     if (mc.field_71439_g.field_70143_R >= 3.0F) {
/* 731 */       return false;
/*     */     }
/*     */     
/* 734 */     AxisAlignedBB bb = (mc.field_71439_g.func_184187_bx() != null) ? mc.field_71439_g.func_184187_bx().func_174813_aQ().func_191195_a(0.0D, 0.0D, 0.0D).func_72317_d(0.0D, -offset, 0.0D) : mc.field_71439_g.func_174813_aQ().func_191195_a(0.0D, 0.0D, 0.0D).func_72317_d(0.0D, -offset, 0.0D);
/* 735 */     boolean onLiquid = false;
/* 736 */     int y = (int)bb.field_72338_b;
/* 737 */     for (int x = MathHelper.func_76128_c(bb.field_72340_a); x < MathHelper.func_76128_c(bb.field_72336_d + 1.0D); x++) {
/* 738 */       for (int z = MathHelper.func_76128_c(bb.field_72339_c); z < MathHelper.func_76128_c(bb.field_72334_f + 1.0D); z++) {
/* 739 */         Block block = mc.field_71441_e.func_180495_p(new BlockPos(x, y, z)).func_177230_c();
/* 740 */         if (block != Blocks.field_150350_a) {
/* 741 */           if (!(block instanceof net.minecraft.block.BlockLiquid)) {
/* 742 */             return false;
/*     */           }
/* 744 */           onLiquid = true;
/*     */         } 
/*     */       } 
/*     */     } 
/* 748 */     return onLiquid;
/*     */   }
/*     */   
/*     */   public static boolean isAboveLiquid(Entity entity) {
/* 752 */     if (entity == null) {
/* 753 */       return false;
/*     */     }
/*     */     
/* 756 */     double n = entity.field_70163_u + 0.01D;
/* 757 */     int i = MathHelper.func_76128_c(entity.field_70165_t);
/* 758 */     while (i < MathHelper.func_76143_f(entity.field_70165_t)) {
/* 759 */       int j = MathHelper.func_76128_c(entity.field_70161_v);
/* 760 */       while (j < MathHelper.func_76143_f(entity.field_70161_v)) {
/* 761 */         if (mc.field_71441_e.func_180495_p(new BlockPos(i, (int)n, j)).func_177230_c() instanceof net.minecraft.block.BlockLiquid) {
/* 762 */           return true;
/*     */         }
/* 764 */         j++;
/*     */       } 
/* 766 */       i++;
/*     */     } 
/* 768 */     return false;
/*     */   }
/*     */   
/*     */   public static BlockPos getPlayerPosWithEntity() {
/* 772 */     return new BlockPos(
/* 773 */         (mc.field_71439_g.func_184187_bx() != null) ? (mc.field_71439_g.func_184187_bx()).field_70165_t : mc.field_71439_g.field_70165_t, 
/* 774 */         (mc.field_71439_g.func_184187_bx() != null) ? (mc.field_71439_g.func_184187_bx()).field_70163_u : mc.field_71439_g.field_70163_u, 
/* 775 */         (mc.field_71439_g.func_184187_bx() != null) ? (mc.field_71439_g.func_184187_bx()).field_70161_v : mc.field_71439_g.field_70161_v);
/*     */   }
/*     */   public static boolean checkForLiquid(Entity entity, boolean b) {
/*     */     double n;
/* 779 */     if (entity == null) {
/* 780 */       return false;
/*     */     }
/*     */     
/* 783 */     double posY = entity.field_70163_u;
/*     */     
/* 785 */     if (b) {
/* 786 */       n = 0.03D;
/* 787 */     } else if (entity instanceof EntityPlayer) {
/* 788 */       n = 0.2D;
/*     */     } else {
/* 790 */       n = 0.5D;
/*     */     } 
/*     */     
/* 793 */     double n2 = posY - n;
/* 794 */     int i = MathHelper.func_76128_c(entity.field_70165_t);
/* 795 */     while (i < MathHelper.func_76143_f(entity.field_70165_t)) {
/* 796 */       int j = MathHelper.func_76128_c(entity.field_70161_v);
/* 797 */       while (j < MathHelper.func_76143_f(entity.field_70161_v)) {
/* 798 */         if (mc.field_71441_e.func_180495_p(new BlockPos(i, MathHelper.func_76128_c(n2), j)).func_177230_c() instanceof net.minecraft.block.BlockLiquid) {
/* 799 */           return true;
/*     */         }
/* 801 */         j++;
/*     */       } 
/* 803 */       i++;
/*     */     } 
/* 805 */     return false;
/*     */   }
/*     */   
/*     */   public static boolean isOnLiquid() {
/* 809 */     double y = mc.field_71439_g.field_70163_u - 0.03D;
/* 810 */     for (int x = MathHelper.func_76128_c(mc.field_71439_g.field_70165_t); x < MathHelper.func_76143_f(mc.field_71439_g.field_70165_t); x++) {
/* 811 */       for (int z = MathHelper.func_76128_c(mc.field_71439_g.field_70161_v); z < MathHelper.func_76143_f(mc.field_71439_g.field_70161_v); z++) {
/* 812 */         BlockPos pos = new BlockPos(x, MathHelper.func_76128_c(y), z);
/* 813 */         if (mc.field_71441_e.func_180495_p(pos).func_177230_c() instanceof net.minecraft.block.BlockLiquid) {
/* 814 */           return true;
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 819 */     return false;
/*     */   }
/*     */   
/*     */   public static double[] forward(double speed) {
/* 823 */     float forward = mc.field_71439_g.field_71158_b.field_192832_b;
/* 824 */     float side = mc.field_71439_g.field_71158_b.field_78902_a;
/* 825 */     float yaw = mc.field_71439_g.field_70126_B + (mc.field_71439_g.field_70177_z - mc.field_71439_g.field_70126_B) * mc.func_184121_ak();
/* 826 */     if (forward != 0.0F) {
/* 827 */       if (side > 0.0F) {
/* 828 */         yaw += ((forward > 0.0F) ? -45 : 45);
/* 829 */       } else if (side < 0.0F) {
/* 830 */         yaw += ((forward > 0.0F) ? 45 : -45);
/*     */       } 
/* 832 */       side = 0.0F;
/* 833 */       if (forward > 0.0F) {
/* 834 */         forward = 1.0F;
/* 835 */       } else if (forward < 0.0F) {
/* 836 */         forward = -1.0F;
/*     */       } 
/*     */     } 
/* 839 */     double sin = Math.sin(Math.toRadians((yaw + 90.0F)));
/* 840 */     double cos = Math.cos(Math.toRadians((yaw + 90.0F)));
/* 841 */     double posX = forward * speed * cos + side * speed * sin;
/* 842 */     double posZ = forward * speed * sin - side * speed * cos;
/* 843 */     return new double[] { posX, posZ };
/*     */   }
/*     */   
/*     */   public static Map<String, Integer> getTextRadarPlayers() {
/* 847 */     Map<String, Integer> output = new HashMap<>();
/* 848 */     DecimalFormat dfHealth = new DecimalFormat("#.#");
/* 849 */     dfHealth.setRoundingMode(RoundingMode.CEILING);
/* 850 */     DecimalFormat dfDistance = new DecimalFormat("#.#");
/* 851 */     dfDistance.setRoundingMode(RoundingMode.CEILING);
/* 852 */     StringBuilder healthSB = new StringBuilder();
/* 853 */     StringBuilder distanceSB = new StringBuilder();
/* 854 */     for (EntityPlayer player : mc.field_71441_e.field_73010_i) {
/* 855 */       if (player.func_82150_aj() && !((Boolean)(Managers.getInstance()).tRadarInv.getValue()).booleanValue()) {
/*     */         continue;
/*     */       }
/*     */       
/* 859 */       if (player.func_70005_c_().equals(mc.field_71439_g.func_70005_c_())) {
/*     */         continue;
/*     */       }
/* 862 */       int hpRaw = (int)getHealth((Entity)player);
/* 863 */       String hp = dfHealth.format(hpRaw);
/* 864 */       healthSB.append("§");
/* 865 */       if (hpRaw >= 20) {
/* 866 */         healthSB.append("a");
/* 867 */       } else if (hpRaw >= 10) {
/* 868 */         healthSB.append("e");
/* 869 */       } else if (hpRaw >= 5) {
/* 870 */         healthSB.append("6");
/*     */       } else {
/* 872 */         healthSB.append("c");
/*     */       } 
/* 874 */       healthSB.append(hp);
/* 875 */       int distanceInt = (int)mc.field_71439_g.func_70032_d((Entity)player);
/* 876 */       String distance = dfDistance.format(distanceInt);
/* 877 */       distanceSB.append("§");
/* 878 */       if (distanceInt >= 25) {
/* 879 */         distanceSB.append("a");
/* 880 */       } else if (distanceInt > 10) {
/* 881 */         distanceSB.append("6");
/* 882 */       } else if (distanceInt >= 50) {
/* 883 */         distanceSB.append("7");
/*     */       } else {
/* 885 */         distanceSB.append("c");
/*     */       } 
/* 887 */       distanceSB.append(distance);
/* 888 */       output.put(healthSB.toString() + " " + (Phobos.friendManager.isFriend(player) ? "§b" : "§r") + player.func_70005_c_() + " " + distanceSB.toString() + " " + "§f" + Phobos.totemPopManager.getTotemPopString(player) + Phobos.potionManager.getTextRadarPotion(player), Integer.valueOf((int)mc.field_71439_g.func_70032_d((Entity)player)));
/*     */       
/* 890 */       healthSB.setLength(0);
/* 891 */       distanceSB.setLength(0);
/*     */     } 
/*     */     
/* 894 */     if (!output.isEmpty()) {
/* 895 */       output = MathUtil.sortByValue(output, false);
/*     */     }
/* 897 */     return output;
/*     */   }
/*     */   
/*     */   public static boolean isAboveBlock(Entity entity, BlockPos blockPos) {
/* 901 */     return (entity.field_70163_u >= blockPos.func_177956_o());
/*     */   }
/*     */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\me\earth\phobo\\util\EntityUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */