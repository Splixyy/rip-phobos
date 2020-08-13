/*     */ package me.earth.phobos.features.modules.render;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import me.earth.phobos.event.events.Render3DEvent;
/*     */ import me.earth.phobos.features.modules.Module;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.util.EnumHand;
/*     */ import net.minecraft.util.math.AxisAlignedBB;
/*     */ import net.minecraft.util.math.MathHelper;
/*     */ import net.minecraft.util.math.RayTraceResult;
/*     */ import net.minecraft.util.math.Vec3d;
/*     */ import org.lwjgl.opengl.GL11;
/*     */ import org.lwjgl.util.glu.Cylinder;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Trajectories
/*     */   extends Module
/*     */ {
/*     */   public Trajectories() {
/*  28 */     super("Trajectories", "Shows the way of projectiles.", Module.Category.RENDER, false, false, false);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onRender3D(Render3DEvent event) {
/*  33 */     if (mc.field_71441_e == null || mc.field_71439_g == null)
/*  34 */       return;  double renderPosX = mc.field_71439_g.field_70142_S + (mc.field_71439_g.field_70165_t - mc.field_71439_g.field_70142_S) * event.getPartialTicks();
/*  35 */     double renderPosY = mc.field_71439_g.field_70137_T + (mc.field_71439_g.field_70163_u - mc.field_71439_g.field_70137_T) * event.getPartialTicks();
/*  36 */     double renderPosZ = mc.field_71439_g.field_70136_U + (mc.field_71439_g.field_70161_v - mc.field_71439_g.field_70136_U) * event.getPartialTicks();
/*  37 */     mc.field_71439_g.func_184586_b(EnumHand.MAIN_HAND);
/*  38 */     if (mc.field_71474_y.field_74320_O != 0 || (!(mc.field_71439_g.func_184586_b(EnumHand.MAIN_HAND).func_77973_b() instanceof net.minecraft.item.ItemBow) && !(mc.field_71439_g.func_184586_b(EnumHand.MAIN_HAND).func_77973_b() instanceof net.minecraft.item.ItemFishingRod) && !(mc.field_71439_g.func_184586_b(EnumHand.MAIN_HAND).func_77973_b() instanceof net.minecraft.item.ItemEnderPearl) && !(mc.field_71439_g.func_184586_b(EnumHand.MAIN_HAND).func_77973_b() instanceof net.minecraft.item.ItemEgg) && !(mc.field_71439_g.func_184586_b(EnumHand.MAIN_HAND).func_77973_b() instanceof net.minecraft.item.ItemSnowball) && !(mc.field_71439_g.func_184586_b(EnumHand.MAIN_HAND).func_77973_b() instanceof net.minecraft.item.ItemExpBottle)))
/*     */       return; 
/*  40 */     GL11.glPushMatrix();
/*  41 */     Item item = mc.field_71439_g.func_184586_b(EnumHand.MAIN_HAND).func_77973_b();
/*  42 */     double posX = renderPosX - (MathHelper.func_76134_b(mc.field_71439_g.field_70177_z / 180.0F * 3.1415927F) * 0.16F);
/*  43 */     double posY = renderPosY + mc.field_71439_g.func_70047_e() - 0.1000000014901161D;
/*  44 */     double posZ = renderPosZ - (MathHelper.func_76126_a(mc.field_71439_g.field_70177_z / 180.0F * 3.1415927F) * 0.16F);
/*  45 */     double motionX = (-MathHelper.func_76126_a(mc.field_71439_g.field_70177_z / 180.0F * 3.1415927F) * MathHelper.func_76134_b(mc.field_71439_g.field_70125_A / 180.0F * 3.1415927F)) * ((item instanceof net.minecraft.item.ItemBow) ? 1.0D : 0.4D);
/*  46 */     double motionY = -MathHelper.func_76126_a(mc.field_71439_g.field_70125_A / 180.0F * 3.1415927F) * ((item instanceof net.minecraft.item.ItemBow) ? 1.0D : 0.4D);
/*  47 */     double motionZ = (MathHelper.func_76134_b(mc.field_71439_g.field_70177_z / 180.0F * 3.1415927F) * MathHelper.func_76134_b(mc.field_71439_g.field_70125_A / 180.0F * 3.1415927F)) * ((item instanceof net.minecraft.item.ItemBow) ? 1.0D : 0.4D);
/*  48 */     int var6 = 72000 - mc.field_71439_g.func_184605_cv();
/*  49 */     float power = var6 / 20.0F;
/*  50 */     power = (power * power + power * 2.0F) / 3.0F;
/*  51 */     if (power > 1.0F) {
/*  52 */       power = 1.0F;
/*     */     }
/*  54 */     float distance = MathHelper.func_76133_a(motionX * motionX + motionY * motionY + motionZ * motionZ);
/*  55 */     motionX /= distance;
/*  56 */     motionY /= distance;
/*  57 */     motionZ /= distance;
/*  58 */     float pow = (item instanceof net.minecraft.item.ItemBow) ? (power * 2.0F) : ((item instanceof net.minecraft.item.ItemFishingRod) ? 1.25F : ((mc.field_71439_g.func_184586_b(EnumHand.MAIN_HAND).func_77973_b() == Items.field_151062_by) ? 0.9F : 1.0F));
/*  59 */     motionX *= (pow * ((item instanceof net.minecraft.item.ItemFishingRod) ? 0.75F : ((mc.field_71439_g.func_184586_b(EnumHand.MAIN_HAND).func_77973_b() == Items.field_151062_by) ? 0.75F : 1.5F)));
/*  60 */     motionY *= (pow * ((item instanceof net.minecraft.item.ItemFishingRod) ? 0.75F : ((mc.field_71439_g.func_184586_b(EnumHand.MAIN_HAND).func_77973_b() == Items.field_151062_by) ? 0.75F : 1.5F)));
/*  61 */     motionZ *= (pow * ((item instanceof net.minecraft.item.ItemFishingRod) ? 0.75F : ((mc.field_71439_g.func_184586_b(EnumHand.MAIN_HAND).func_77973_b() == Items.field_151062_by) ? 0.75F : 1.5F)));
/*  62 */     enableGL3D(2.0F);
/*  63 */     if (power > 0.6F) {
/*  64 */       GlStateManager.func_179131_c(0.0F, 1.0F, 0.0F, 1.0F);
/*     */     } else {
/*  66 */       GlStateManager.func_179131_c(0.8F, 0.5F, 0.0F, 1.0F);
/*     */     } 
/*  68 */     GL11.glEnable(2848);
/*  69 */     float size = (float)((item instanceof net.minecraft.item.ItemBow) ? 0.3D : 0.25D);
/*  70 */     boolean hasLanded = false;
/*  71 */     Entity landingOnEntity = null;
/*  72 */     RayTraceResult landingPosition = null;
/*  73 */     while (!hasLanded && posY > 0.0D) {
/*  74 */       Vec3d present = new Vec3d(posX, posY, posZ);
/*  75 */       Vec3d future = new Vec3d(posX + motionX, posY + motionY, posZ + motionZ);
/*  76 */       RayTraceResult possibleLandingStrip = mc.field_71441_e.func_147447_a(present, future, false, true, false);
/*  77 */       if (possibleLandingStrip != null && possibleLandingStrip.field_72313_a != RayTraceResult.Type.MISS) {
/*  78 */         landingPosition = possibleLandingStrip;
/*  79 */         hasLanded = true;
/*     */       } 
/*  81 */       AxisAlignedBB arrowBox = new AxisAlignedBB(posX - size, posY - size, posZ - size, posX + size, posY + size, posZ + size);
/*  82 */       List entities = getEntitiesWithinAABB(arrowBox.func_72317_d(motionX, motionY, motionZ).func_72321_a(1.0D, 1.0D, 1.0D));
/*  83 */       for (Object entity : entities) {
/*  84 */         Entity boundingBox = (Entity)entity;
/*  85 */         if (boundingBox.func_70067_L() && boundingBox != mc.field_71439_g) {
/*  86 */           float var7 = 0.3F;
/*  87 */           AxisAlignedBB var8 = boundingBox.func_174813_aQ().func_72321_a(var7, var7, var7);
/*  88 */           RayTraceResult possibleEntityLanding = var8.func_72327_a(present, future);
/*  89 */           if (possibleEntityLanding == null) {
/*     */             continue;
/*     */           }
/*  92 */           hasLanded = true;
/*  93 */           landingOnEntity = boundingBox;
/*  94 */           landingPosition = possibleEntityLanding;
/*     */         } 
/*     */       } 
/*  97 */       if (landingOnEntity != null) {
/*  98 */         GlStateManager.func_179131_c(1.0F, 0.0F, 0.0F, 1.0F);
/*     */       }
/* 100 */       posX += motionX;
/* 101 */       posY += motionY;
/* 102 */       posZ += motionZ;
/* 103 */       float motionAdjustment = 0.99F;
/* 104 */       motionX *= motionAdjustment;
/* 105 */       motionY *= motionAdjustment;
/* 106 */       motionZ *= motionAdjustment;
/* 107 */       motionY -= (item instanceof net.minecraft.item.ItemBow) ? 0.05D : 0.03D;
/*     */     } 
/* 109 */     if (landingPosition != null && landingPosition.field_72313_a == RayTraceResult.Type.BLOCK) {
/* 110 */       GlStateManager.func_179137_b(posX - renderPosX, posY - renderPosY, posZ - renderPosZ);
/* 111 */       int side = landingPosition.field_178784_b.func_176745_a();
/* 112 */       if (side == 2) {
/* 113 */         GlStateManager.func_179114_b(90.0F, 1.0F, 0.0F, 0.0F);
/* 114 */       } else if (side == 3) {
/* 115 */         GlStateManager.func_179114_b(90.0F, 1.0F, 0.0F, 0.0F);
/* 116 */       } else if (side == 4) {
/* 117 */         GlStateManager.func_179114_b(90.0F, 0.0F, 0.0F, 1.0F);
/* 118 */       } else if (side == 5) {
/* 119 */         GlStateManager.func_179114_b(90.0F, 0.0F, 0.0F, 1.0F);
/*     */       } 
/* 121 */       Cylinder c = new Cylinder();
/* 122 */       GlStateManager.func_179114_b(-90.0F, 1.0F, 0.0F, 0.0F);
/* 123 */       c.setDrawStyle(100011);
/* 124 */       if (landingOnEntity != null) {
/* 125 */         GlStateManager.func_179131_c(0.0F, 0.0F, 0.0F, 1.0F);
/* 126 */         GL11.glLineWidth(2.5F);
/* 127 */         c.draw(0.6F, 0.3F, 0.0F, 4, 1);
/* 128 */         GL11.glLineWidth(0.1F);
/* 129 */         GlStateManager.func_179131_c(1.0F, 0.0F, 0.0F, 1.0F);
/*     */       } 
/* 131 */       c.draw(0.6F, 0.3F, 0.0F, 4, 1);
/*     */     } 
/* 133 */     disableGL3D();
/* 134 */     GL11.glPopMatrix();
/*     */   }
/*     */   
/*     */   public void enableGL3D(float lineWidth) {
/* 138 */     GL11.glDisable(3008);
/* 139 */     GL11.glEnable(3042);
/* 140 */     GL11.glBlendFunc(770, 771);
/* 141 */     GL11.glDisable(3553);
/* 142 */     GL11.glDisable(2929);
/* 143 */     GL11.glDepthMask(false);
/* 144 */     GL11.glEnable(2884);
/* 145 */     mc.field_71460_t.func_175072_h();
/* 146 */     GL11.glEnable(2848);
/* 147 */     GL11.glHint(3154, 4354);
/* 148 */     GL11.glHint(3155, 4354);
/* 149 */     GL11.glLineWidth(lineWidth);
/*     */   }
/*     */   
/*     */   public void disableGL3D() {
/* 153 */     GL11.glEnable(3553);
/* 154 */     GL11.glEnable(2929);
/* 155 */     GL11.glDisable(3042);
/* 156 */     GL11.glEnable(3008);
/* 157 */     GL11.glDepthMask(true);
/* 158 */     GL11.glCullFace(1029);
/* 159 */     GL11.glDisable(2848);
/* 160 */     GL11.glHint(3154, 4352);
/* 161 */     GL11.glHint(3155, 4352);
/*     */   }
/*     */   
/*     */   private List getEntitiesWithinAABB(AxisAlignedBB bb) {
/* 165 */     ArrayList list = new ArrayList();
/* 166 */     int chunkMinX = MathHelper.func_76128_c((bb.field_72340_a - 2.0D) / 16.0D);
/* 167 */     int chunkMaxX = MathHelper.func_76128_c((bb.field_72336_d + 2.0D) / 16.0D);
/* 168 */     int chunkMinZ = MathHelper.func_76128_c((bb.field_72339_c - 2.0D) / 16.0D);
/* 169 */     int chunkMaxZ = MathHelper.func_76128_c((bb.field_72334_f + 2.0D) / 16.0D);
/* 170 */     for (int x = chunkMinX; x <= chunkMaxX; x++) {
/* 171 */       for (int z = chunkMinZ; z <= chunkMaxZ; z++) {
/* 172 */         if (mc.field_71441_e.func_72863_F().func_186026_b(x, z) != null) {
/* 173 */           mc.field_71441_e.func_72964_e(x, z).func_177414_a((Entity)mc.field_71439_g, bb, list, null);
/*     */         }
/*     */       } 
/*     */     } 
/* 177 */     return list;
/*     */   }
/*     */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\me\earth\phobos\features\modules\render\Trajectories.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */