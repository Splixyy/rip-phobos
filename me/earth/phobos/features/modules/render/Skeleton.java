/*     */ package me.earth.phobos.features.modules.render;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import me.earth.phobos.event.events.Render3DEvent;
/*     */ import me.earth.phobos.event.events.RenderEntityModelEvent;
/*     */ import me.earth.phobos.features.modules.Module;
/*     */ import me.earth.phobos.features.setting.Setting;
/*     */ import me.earth.phobos.util.EntityUtil;
/*     */ import me.earth.phobos.util.RenderUtil;
/*     */ import net.minecraft.client.model.ModelBiped;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.util.math.Vec3d;
/*     */ import org.lwjgl.opengl.GL11;
/*     */ 
/*     */ public class Skeleton
/*     */   extends Module {
/*  21 */   private final Setting<Integer> red = register(new Setting("Red", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255)));
/*  22 */   private final Setting<Integer> green = register(new Setting("Green", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255)));
/*  23 */   private final Setting<Integer> blue = register(new Setting("Blue", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255)));
/*  24 */   private final Setting<Integer> alpha = register(new Setting("Alpha", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255)));
/*  25 */   private final Setting<Float> lineWidth = register(new Setting("LineWidth", Float.valueOf(1.5F), Float.valueOf(0.1F), Float.valueOf(5.0F)));
/*  26 */   private final Setting<Boolean> colorFriends = register(new Setting("Friends", Boolean.valueOf(true)));
/*  27 */   private final Setting<Boolean> invisibles = register(new Setting("Invisibles", Boolean.valueOf(false)));
/*     */   
/*  29 */   private static Skeleton INSTANCE = new Skeleton();
/*  30 */   private final Map<EntityPlayer, float[][]> rotationList = (Map)new HashMap<>();
/*     */   
/*     */   public Skeleton() {
/*  33 */     super("Skeleton", "Draws a nice Skeleton.", Module.Category.RENDER, false, false, false);
/*  34 */     setInstance();
/*     */   }
/*     */   
/*     */   private void setInstance() {
/*  38 */     INSTANCE = this;
/*     */   }
/*     */   
/*     */   public static Skeleton getInstance() {
/*  42 */     if (INSTANCE == null) {
/*  43 */       INSTANCE = new Skeleton();
/*     */     }
/*  45 */     return INSTANCE;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onRender3D(Render3DEvent event) {
/*  50 */     RenderUtil.GLPre(((Float)this.lineWidth.getValue()).floatValue());
/*  51 */     for (EntityPlayer player : mc.field_71441_e.field_73010_i) {
/*  52 */       if (player != null && player != mc.func_175606_aa() && player.func_70089_S() && !player.func_70608_bn() && (!player.func_82150_aj() || ((Boolean)this.invisibles.getValue()).booleanValue()) && 
/*  53 */         this.rotationList.get(player) != null && mc.field_71439_g.func_70068_e((Entity)player) < 2500.0D) {
/*  54 */         renderSkeleton(player, this.rotationList.get(player), EntityUtil.getColor((Entity)player, ((Integer)this.red.getValue()).intValue(), ((Integer)this.green.getValue()).intValue(), ((Integer)this.blue.getValue()).intValue(), ((Integer)this.alpha.getValue()).intValue(), ((Boolean)this.colorFriends.getValue()).booleanValue()));
/*     */       }
/*     */     } 
/*     */     
/*  58 */     RenderUtil.GlPost();
/*     */   }
/*     */   
/*     */   public void onRenderModel(RenderEntityModelEvent event) {
/*  62 */     if (event.getStage() == 0 && event.entity instanceof EntityPlayer && 
/*  63 */       event.modelBase instanceof ModelBiped) {
/*  64 */       ModelBiped biped = (ModelBiped)event.modelBase;
/*  65 */       float[][] rotations = RenderUtil.getBipedRotations(biped);
/*  66 */       EntityPlayer player = (EntityPlayer)event.entity;
/*  67 */       this.rotationList.put(player, rotations);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void renderSkeleton(EntityPlayer player, float[][] rotations, Color color) {
/*  73 */     GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
/*  74 */     GlStateManager.func_179094_E();
/*  75 */     GlStateManager.func_179131_c(color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F, color.getAlpha() / 255.0F);
/*  76 */     Vec3d interp = EntityUtil.getInterpolatedRenderPos((Entity)player, mc.func_184121_ak());
/*  77 */     double pX = interp.field_72450_a;
/*  78 */     double pY = interp.field_72448_b;
/*  79 */     double pZ = interp.field_72449_c;
/*  80 */     GlStateManager.func_179137_b(pX, pY, pZ);
/*  81 */     GlStateManager.func_179114_b(-player.field_70761_aq, 0.0F, 1.0F, 0.0F);
/*  82 */     GlStateManager.func_179137_b(0.0D, 0.0D, player.func_70093_af() ? -0.235D : 0.0D);
/*  83 */     float sneak = player.func_70093_af() ? 0.6F : 0.75F;
/*  84 */     GlStateManager.func_179094_E();
/*  85 */     GlStateManager.func_179137_b(-0.125D, sneak, 0.0D);
/*  86 */     if (rotations[3][0] != 0.0F) {
/*  87 */       GlStateManager.func_179114_b(rotations[3][0] * 57.295776F, 1.0F, 0.0F, 0.0F);
/*     */     }
/*  89 */     if (rotations[3][1] != 0.0F) {
/*  90 */       GlStateManager.func_179114_b(rotations[3][1] * 57.295776F, 0.0F, 1.0F, 0.0F);
/*     */     }
/*  92 */     if (rotations[3][2] != 0.0F) {
/*  93 */       GlStateManager.func_179114_b(rotations[3][2] * 57.295776F, 0.0F, 0.0F, 1.0F);
/*     */     }
/*     */     
/*  96 */     GlStateManager.func_187447_r(3);
/*  97 */     GL11.glVertex3d(0.0D, 0.0D, 0.0D);
/*  98 */     GL11.glVertex3d(0.0D, -sneak, 0.0D);
/*  99 */     GlStateManager.func_187437_J();
/* 100 */     GlStateManager.func_179121_F();
/* 101 */     GlStateManager.func_179094_E();
/* 102 */     GlStateManager.func_179137_b(0.125D, sneak, 0.0D);
/* 103 */     if (rotations[4][0] != 0.0F) {
/* 104 */       GlStateManager.func_179114_b(rotations[4][0] * 57.295776F, 1.0F, 0.0F, 0.0F);
/*     */     }
/* 106 */     if (rotations[4][1] != 0.0F) {
/* 107 */       GlStateManager.func_179114_b(rotations[4][1] * 57.295776F, 0.0F, 1.0F, 0.0F);
/*     */     }
/* 109 */     if (rotations[4][2] != 0.0F) {
/* 110 */       GlStateManager.func_179114_b(rotations[4][2] * 57.295776F, 0.0F, 0.0F, 1.0F);
/*     */     }
/*     */     
/* 113 */     GlStateManager.func_187447_r(3);
/* 114 */     GL11.glVertex3d(0.0D, 0.0D, 0.0D);
/* 115 */     GL11.glVertex3d(0.0D, -sneak, 0.0D);
/* 116 */     GlStateManager.func_187437_J();
/* 117 */     GlStateManager.func_179121_F();
/* 118 */     GlStateManager.func_179137_b(0.0D, 0.0D, player.func_70093_af() ? 0.25D : 0.0D);
/* 119 */     GlStateManager.func_179094_E();
/* 120 */     double sneakOffset = 0.0D;
/* 121 */     if (player.func_70093_af()) {
/* 122 */       sneakOffset = -0.05D;
/*     */     }
/*     */     
/* 125 */     GlStateManager.func_179137_b(0.0D, sneakOffset, player.func_70093_af() ? -0.01725D : 0.0D);
/* 126 */     GlStateManager.func_179094_E();
/* 127 */     GlStateManager.func_179137_b(-0.375D, sneak + 0.55D, 0.0D);
/* 128 */     if (rotations[1][0] != 0.0F) {
/* 129 */       GlStateManager.func_179114_b(rotations[1][0] * 57.295776F, 1.0F, 0.0F, 0.0F);
/*     */     }
/* 131 */     if (rotations[1][1] != 0.0F) {
/* 132 */       GlStateManager.func_179114_b(rotations[1][1] * 57.295776F, 0.0F, 1.0F, 0.0F);
/*     */     }
/* 134 */     if (rotations[1][2] != 0.0F) {
/* 135 */       GlStateManager.func_179114_b(-rotations[1][2] * 57.295776F, 0.0F, 0.0F, 1.0F);
/*     */     }
/*     */     
/* 138 */     GlStateManager.func_187447_r(3);
/* 139 */     GL11.glVertex3d(0.0D, 0.0D, 0.0D);
/* 140 */     GL11.glVertex3d(0.0D, -0.5D, 0.0D);
/* 141 */     GlStateManager.func_187437_J();
/* 142 */     GlStateManager.func_179121_F();
/* 143 */     GlStateManager.func_179094_E();
/* 144 */     GlStateManager.func_179137_b(0.375D, sneak + 0.55D, 0.0D);
/* 145 */     if (rotations[2][0] != 0.0F) {
/* 146 */       GlStateManager.func_179114_b(rotations[2][0] * 57.295776F, 1.0F, 0.0F, 0.0F);
/*     */     }
/* 148 */     if (rotations[2][1] != 0.0F) {
/* 149 */       GlStateManager.func_179114_b(rotations[2][1] * 57.295776F, 0.0F, 1.0F, 0.0F);
/*     */     }
/* 151 */     if (rotations[2][2] != 0.0F) {
/* 152 */       GlStateManager.func_179114_b(-rotations[2][2] * 57.295776F, 0.0F, 0.0F, 1.0F);
/*     */     }
/*     */     
/* 155 */     GlStateManager.func_187447_r(3);
/* 156 */     GL11.glVertex3d(0.0D, 0.0D, 0.0D);
/* 157 */     GL11.glVertex3d(0.0D, -0.5D, 0.0D);
/* 158 */     GlStateManager.func_187437_J();
/* 159 */     GlStateManager.func_179121_F();
/* 160 */     GlStateManager.func_179094_E();
/* 161 */     GlStateManager.func_179137_b(0.0D, sneak + 0.55D, 0.0D);
/* 162 */     if (rotations[0][0] != 0.0F) {
/* 163 */       GlStateManager.func_179114_b(rotations[0][0] * 57.295776F, 1.0F, 0.0F, 0.0F);
/*     */     }
/*     */     
/* 166 */     GlStateManager.func_187447_r(3);
/* 167 */     GL11.glVertex3d(0.0D, 0.0D, 0.0D);
/* 168 */     GL11.glVertex3d(0.0D, 0.3D, 0.0D);
/* 169 */     GlStateManager.func_187437_J();
/* 170 */     GlStateManager.func_179121_F();
/* 171 */     GlStateManager.func_179121_F();
/* 172 */     GlStateManager.func_179114_b(player.func_70093_af() ? 25.0F : 0.0F, 1.0F, 0.0F, 0.0F);
/*     */     
/* 174 */     if (player.func_70093_af()) {
/* 175 */       sneakOffset = -0.16175D;
/*     */     }
/*     */     
/* 178 */     GlStateManager.func_179137_b(0.0D, sneakOffset, player.func_70093_af() ? -0.48025D : 0.0D);
/* 179 */     GlStateManager.func_179094_E();
/* 180 */     GlStateManager.func_179137_b(0.0D, sneak, 0.0D);
/* 181 */     GlStateManager.func_187447_r(3);
/* 182 */     GL11.glVertex3d(-0.125D, 0.0D, 0.0D);
/* 183 */     GL11.glVertex3d(0.125D, 0.0D, 0.0D);
/* 184 */     GlStateManager.func_187437_J();
/* 185 */     GlStateManager.func_179121_F();
/* 186 */     GlStateManager.func_179094_E();
/* 187 */     GlStateManager.func_179137_b(0.0D, sneak, 0.0D);
/* 188 */     GlStateManager.func_187447_r(3);
/* 189 */     GL11.glVertex3d(0.0D, 0.0D, 0.0D);
/* 190 */     GL11.glVertex3d(0.0D, 0.55D, 0.0D);
/* 191 */     GlStateManager.func_187437_J();
/* 192 */     GlStateManager.func_179121_F();
/* 193 */     GlStateManager.func_179094_E();
/* 194 */     GlStateManager.func_179137_b(0.0D, sneak + 0.55D, 0.0D);
/* 195 */     GlStateManager.func_187447_r(3);
/* 196 */     GL11.glVertex3d(-0.375D, 0.0D, 0.0D);
/* 197 */     GL11.glVertex3d(0.375D, 0.0D, 0.0D);
/* 198 */     GlStateManager.func_187437_J();
/* 199 */     GlStateManager.func_179121_F();
/* 200 */     GlStateManager.func_179121_F();
/*     */   }
/*     */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\me\earth\phobos\features\modules\render\Skeleton.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */