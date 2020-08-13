/*     */ package me.earth.phobos.util;
/*     */ 
/*     */ import net.minecraft.enchantment.EnchantmentHelper;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.entity.SharedMonsterAttributes;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.init.MobEffects;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.potion.PotionEffect;
/*     */ import net.minecraft.util.CombatRules;
/*     */ import net.minecraft.util.DamageSource;
/*     */ import net.minecraft.util.math.BlockPos;
/*     */ import net.minecraft.util.math.MathHelper;
/*     */ import net.minecraft.util.math.Vec3d;
/*     */ import net.minecraft.world.Explosion;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DamageUtil
/*     */   implements Util
/*     */ {
/*     */   public static boolean isArmorLow(EntityPlayer player, int durability) {
/*  30 */     for (ItemStack piece : player.field_71071_by.field_70460_b) {
/*  31 */       if (piece == null) {
/*  32 */         return true;
/*     */       }
/*  34 */       if (getItemDamage(piece) < durability) {
/*  35 */         return true;
/*     */       }
/*     */     } 
/*     */     
/*  39 */     return false;
/*     */   }
/*     */   
/*     */   public static int getItemDamage(ItemStack stack) {
/*  43 */     return stack.func_77958_k() - stack.func_77952_i();
/*     */   }
/*     */   
/*     */   public static float getDamageInPercent(ItemStack stack) {
/*  47 */     return getItemDamage(stack) / stack.func_77958_k() * 100.0F;
/*     */   }
/*     */   
/*     */   public static int getRoundedDamage(ItemStack stack) {
/*  51 */     return (int)getDamageInPercent(stack);
/*     */   }
/*     */   
/*     */   public static boolean hasDurability(ItemStack stack) {
/*  55 */     Item item = stack.func_77973_b();
/*  56 */     return (item instanceof net.minecraft.item.ItemArmor || item instanceof net.minecraft.item.ItemSword || item instanceof net.minecraft.item.ItemTool || item instanceof net.minecraft.item.ItemShield);
/*     */   }
/*     */   
/*     */   public static boolean canBreakWeakness(EntityPlayer player) {
/*  60 */     int strengthAmp = 0;
/*  61 */     PotionEffect effect = mc.field_71439_g.func_70660_b(MobEffects.field_76420_g);
/*  62 */     if (effect != null) {
/*  63 */       strengthAmp = effect.func_76458_c();
/*     */     }
/*     */     
/*  66 */     return (!mc.field_71439_g.func_70644_a(MobEffects.field_76437_t) || strengthAmp >= 1 || mc.field_71439_g.func_184614_ca().func_77973_b() instanceof net.minecraft.item.ItemSword || mc.field_71439_g.func_184614_ca().func_77973_b() instanceof net.minecraft.item.ItemPickaxe || mc.field_71439_g.func_184614_ca().func_77973_b() instanceof net.minecraft.item.ItemAxe || mc.field_71439_g.func_184614_ca().func_77973_b() instanceof net.minecraft.item.ItemSpade);
/*     */   }
/*     */   
/*     */   public static float calculateDamage(double posX, double posY, double posZ, Entity entity) {
/*  70 */     float doubleExplosionSize = 12.0F;
/*  71 */     double distancedsize = entity.func_70011_f(posX, posY, posZ) / doubleExplosionSize;
/*  72 */     Vec3d vec3d = new Vec3d(posX, posY, posZ);
/*  73 */     double blockDensity = 0.0D;
/*     */     try {
/*  75 */       blockDensity = entity.field_70170_p.func_72842_a(vec3d, entity.func_174813_aQ());
/*  76 */     } catch (Exception exception) {}
/*  77 */     double v = (1.0D - distancedsize) * blockDensity;
/*  78 */     float damage = (int)((v * v + v) / 2.0D * 7.0D * doubleExplosionSize + 1.0D);
/*  79 */     double finald = 1.0D;
/*  80 */     if (entity instanceof EntityLivingBase) {
/*  81 */       finald = getBlastReduction((EntityLivingBase)entity, getDamageMultiplied(damage), new Explosion((World)mc.field_71441_e, null, posX, posY, posZ, 6.0F, false, true));
/*     */     }
/*  83 */     return (float)finald;
/*     */   }
/*     */   
/*     */   public static float getBlastReduction(EntityLivingBase entity, float damageI, Explosion explosion) {
/*  87 */     float damage = damageI;
/*  88 */     if (entity instanceof EntityPlayer) {
/*  89 */       EntityPlayer ep = (EntityPlayer)entity;
/*  90 */       DamageSource ds = DamageSource.func_94539_a(explosion);
/*  91 */       damage = CombatRules.func_189427_a(damage, ep.func_70658_aO(), (float)ep.func_110148_a(SharedMonsterAttributes.field_189429_h).func_111126_e());
/*     */       
/*  93 */       int k = 0;
/*     */       try {
/*  95 */         k = EnchantmentHelper.func_77508_a(ep.func_184193_aE(), ds);
/*  96 */       } catch (Exception exception) {}
/*  97 */       float f = MathHelper.func_76131_a(k, 0.0F, 20.0F);
/*  98 */       damage *= 1.0F - f / 25.0F;
/*     */       
/* 100 */       if (entity.func_70644_a(MobEffects.field_76429_m)) {
/* 101 */         damage -= damage / 4.0F;
/*     */       }
/*     */       
/* 104 */       damage = Math.max(damage, 0.0F);
/* 105 */       return damage;
/*     */     } 
/* 107 */     damage = CombatRules.func_189427_a(damage, entity.func_70658_aO(), (float)entity.func_110148_a(SharedMonsterAttributes.field_189429_h).func_111126_e());
/* 108 */     return damage;
/*     */   }
/*     */   
/*     */   public static float getDamageMultiplied(float damage) {
/* 112 */     int diff = mc.field_71441_e.func_175659_aa().func_151525_a();
/* 113 */     return damage * ((diff == 0) ? 0.0F : ((diff == 2) ? 1.0F : ((diff == 1) ? 0.5F : 1.5F)));
/*     */   }
/*     */   
/*     */   public static float calculateDamage(Entity crystal, Entity entity) {
/* 117 */     return calculateDamage(crystal.field_70165_t, crystal.field_70163_u, crystal.field_70161_v, entity);
/*     */   }
/*     */   
/*     */   public static float calculateDamage(BlockPos pos, Entity entity) {
/* 121 */     return calculateDamage(pos.func_177958_n() + 0.5D, (pos.func_177956_o() + 1), pos.func_177952_p() + 0.5D, entity);
/*     */   }
/*     */   
/*     */   public static boolean canTakeDamage(boolean suicide) {
/* 125 */     return (!mc.field_71439_g.field_71075_bZ.field_75098_d && !suicide);
/*     */   }
/*     */   
/*     */   public static int getCooldownByWeapon(EntityPlayer player) {
/* 129 */     Item item = player.func_184614_ca().func_77973_b();
/* 130 */     if (item instanceof net.minecraft.item.ItemSword) {
/* 131 */       return 600;
/*     */     }
/*     */     
/* 134 */     if (item instanceof net.minecraft.item.ItemPickaxe) {
/* 135 */       return 850;
/*     */     }
/*     */     
/* 138 */     if (item == Items.field_151036_c) {
/* 139 */       return 1100;
/*     */     }
/*     */     
/* 142 */     if (item == Items.field_151018_J) {
/* 143 */       return 500;
/*     */     }
/*     */     
/* 146 */     if (item == Items.field_151019_K) {
/* 147 */       return 350;
/*     */     }
/*     */     
/* 150 */     if (item == Items.field_151053_p || item == Items.field_151049_t) {
/* 151 */       return 1250;
/*     */     }
/*     */     
/* 154 */     if (item instanceof net.minecraft.item.ItemSpade || item == Items.field_151006_E || item == Items.field_151056_x || item == Items.field_151017_I || item == Items.field_151013_M) {
/* 155 */       return 1000;
/*     */     }
/*     */     
/* 158 */     return 250;
/*     */   }
/*     */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\me\earth\phobo\\util\DamageUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */