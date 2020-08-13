/*     */ package me.earth.phobos.features.modules.render;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import me.earth.phobos.Phobos;
/*     */ import me.earth.phobos.event.events.Render3DEvent;
/*     */ import me.earth.phobos.features.modules.Module;
/*     */ import me.earth.phobos.features.setting.Setting;
/*     */ import me.earth.phobos.util.EntityUtil;
/*     */ import me.earth.phobos.util.RenderUtil;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.client.renderer.entity.RenderManager;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.util.math.RayTraceResult;
/*     */ import net.minecraft.util.math.Vec3d;
/*     */ import org.lwjgl.opengl.GL11;
/*     */ 
/*     */ public class Ranges
/*     */   extends Module
/*     */ {
/*     */   private final Setting<Boolean> hitSpheres;
/*     */   private final Setting<Boolean> circle;
/*     */   private final Setting<Boolean> ownSphere;
/*     */   private final Setting<Float> lineWidth;
/*     */   private final Setting<Double> radius;
/*     */   
/*     */   public Ranges() {
/*  30 */     super("Ranges", "Draws a circle around the player.", Module.Category.RENDER, false, false, false);
/*     */ 
/*     */     
/*  33 */     this.hitSpheres = register(new Setting("HitSpheres", Boolean.valueOf(false)));
/*  34 */     this.circle = register(new Setting("Circle", Boolean.valueOf(true)));
/*  35 */     this.ownSphere = register(new Setting("OwnSphere", Boolean.valueOf(false), v -> ((Boolean)this.hitSpheres.getValue()).booleanValue()));
/*  36 */     this.lineWidth = register(new Setting("LineWidth", Float.valueOf(1.5F), Float.valueOf(0.1F), Float.valueOf(5.0F)));
/*  37 */     this.radius = register(new Setting("Radius", Double.valueOf(4.5D), Double.valueOf(0.1D), Double.valueOf(8.0D)));
/*     */   }
/*     */   
/*     */   public void onRender3D(Render3DEvent event) {
/*  41 */     if (((Boolean)this.circle.getValue()).booleanValue()) {
/*  42 */       GlStateManager.func_179094_E();
/*  43 */       RenderUtil.GLPre(((Float)this.lineWidth.getValue()).floatValue());
/*  44 */       GlStateManager.func_179147_l();
/*  45 */       GlStateManager.func_187441_d(3.0F);
/*  46 */       GlStateManager.func_179090_x();
/*  47 */       GlStateManager.func_179132_a(false);
/*  48 */       GlStateManager.func_179097_i();
/*  49 */       GlStateManager.func_187428_a(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
/*     */       
/*  51 */       RenderManager renderManager = mc.func_175598_ae();
/*  52 */       Color color = Color.RED;
/*     */       
/*  54 */       List<Vec3d> hVectors = new ArrayList<>();
/*  55 */       List<Vec3d> vVectors = new ArrayList<>();
/*     */       
/*  57 */       double x = mc.field_71439_g.field_70142_S + (mc.field_71439_g.field_70165_t - mc.field_71439_g.field_70142_S) * event.getPartialTicks() - renderManager.field_78725_b;
/*     */       
/*  59 */       double y = mc.field_71439_g.field_70137_T + (mc.field_71439_g.field_70163_u - mc.field_71439_g.field_70137_T) * event.getPartialTicks() - renderManager.field_78726_c;
/*     */       
/*  61 */       double z = mc.field_71439_g.field_70136_U + (mc.field_71439_g.field_70161_v - mc.field_71439_g.field_70136_U) * event.getPartialTicks() - renderManager.field_78723_d;
/*     */       
/*  63 */       GL11.glColor4f(color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F, color.getAlpha() / 255.0F);
/*  64 */       GL11.glLineWidth(((Float)this.lineWidth.getValue()).floatValue());
/*  65 */       GL11.glBegin(1);
/*  66 */       for (int i = 0; i <= 360; i++) {
/*  67 */         Vec3d vec = new Vec3d(x + Math.sin(i * Math.PI / 180.0D) * ((Double)this.radius.getValue()).doubleValue(), y + 0.1D, z + Math.cos(i * Math.PI / 180.0D) * ((Double)this.radius.getValue()).doubleValue());
/*  68 */         RayTraceResult result = mc.field_71441_e.func_147447_a(new Vec3d(x, y + 0.1D, z), vec, false, true, false);
/*  69 */         if (result != null) {
/*  70 */           hVectors.add(result.field_72307_f);
/*     */         } else {
/*  72 */           hVectors.add(vec);
/*     */         } 
/*     */       } 
/*  75 */       for (int j = 0; j < hVectors.size() - 1; j++) {
/*  76 */         GL11.glVertex3d(((Vec3d)hVectors.get(j)).field_72450_a, ((Vec3d)hVectors.get(j)).field_72448_b, ((Vec3d)hVectors.get(j)).field_72449_c);
/*  77 */         GL11.glVertex3d(((Vec3d)hVectors.get(j + 1)).field_72450_a, ((Vec3d)hVectors.get(j + 1)).field_72448_b, ((Vec3d)hVectors.get(j + 1)).field_72449_c);
/*     */       } 
/*  79 */       GL11.glEnd();
/*     */       
/*  81 */       GlStateManager.func_179117_G();
/*  82 */       GlStateManager.func_179126_j();
/*  83 */       GlStateManager.func_179132_a(true);
/*  84 */       GlStateManager.func_179098_w();
/*  85 */       GlStateManager.func_179084_k();
/*  86 */       RenderUtil.GlPost();
/*  87 */       GlStateManager.func_179121_F();
/*     */     } 
/*     */     
/*  90 */     if (((Boolean)this.hitSpheres.getValue()).booleanValue())
/*     */     {
/*  92 */       for (EntityPlayer player : mc.field_71441_e.field_73010_i) {
/*  93 */         if (player != null && (!player.equals(mc.field_71439_g) || ((Boolean)this.ownSphere.getValue()).booleanValue())) {
/*  94 */           Vec3d interpolated = EntityUtil.interpolateEntity((Entity)player, event.getPartialTicks());
/*  95 */           if (Phobos.friendManager.isFriend(player.func_70005_c_())) {
/*  96 */             GL11.glColor4f(0.15F, 0.15F, 1.0F, 1.0F);
/*     */           }
/*  98 */           else if (mc.field_71439_g.func_70032_d((Entity)player) >= 64.0F) {
/*  99 */             GL11.glColor4f(0.0F, 1.0F, 0.0F, 1.0F);
/*     */           } else {
/* 101 */             GL11.glColor4f(1.0F, mc.field_71439_g.func_70032_d((Entity)player) / 150.0F, 0.0F, 1.0F);
/*     */           } 
/*     */           
/* 104 */           RenderUtil.drawSphere(interpolated.field_72450_a, interpolated.field_72448_b, interpolated.field_72449_c, ((Double)this.radius.getValue()).floatValue(), 20, 15);
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\me\earth\phobos\features\modules\render\Ranges.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */