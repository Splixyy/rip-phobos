/*     */ package me.earth.phobos.features.modules.render;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import me.earth.phobos.Phobos;
/*     */ import me.earth.phobos.event.events.Render3DEvent;
/*     */ import me.earth.phobos.features.modules.Module;
/*     */ import me.earth.phobos.features.modules.combat.AutoCrystal;
/*     */ import me.earth.phobos.features.setting.Setting;
/*     */ import me.earth.phobos.util.EntityUtil;
/*     */ import me.earth.phobos.util.MathUtil;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.util.math.Vec3d;
/*     */ import org.lwjgl.opengl.GL11;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Tracer
/*     */   extends Module
/*     */ {
/*  21 */   public Setting<Boolean> players = register(new Setting("Players", Boolean.valueOf(true)));
/*  22 */   public Setting<Boolean> mobs = register(new Setting("Mobs", Boolean.valueOf(false)));
/*  23 */   public Setting<Boolean> animals = register(new Setting("Animals", Boolean.valueOf(false)));
/*  24 */   public Setting<Boolean> invisibles = register(new Setting("Invisibles", Boolean.valueOf(false)));
/*  25 */   public Setting<Boolean> drawFromSky = register(new Setting("DrawFromSky", Boolean.valueOf(false)));
/*  26 */   public Setting<Float> width = register(new Setting("Width", Float.valueOf(1.0F), Float.valueOf(0.1F), Float.valueOf(5.0F)));
/*  27 */   public Setting<Integer> distance = register(new Setting("Radius", Integer.valueOf(300), Integer.valueOf(0), Integer.valueOf(300)));
/*  28 */   public Setting<Boolean> crystalCheck = register(new Setting("CrystalCheck", Boolean.valueOf(false)));
/*     */   
/*     */   public Tracer() {
/*  31 */     super("Tracers", "Draws lines to other players.", Module.Category.RENDER, false, false, false);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onRender3D(Render3DEvent event) {
/*  36 */     if (fullNullCheck()) {
/*     */       return;
/*     */     }
/*     */     
/*  40 */     GlStateManager.func_179094_E();
/*  41 */     mc.field_71441_e.field_72996_f.stream()
/*  42 */       .filter(EntityUtil::isLiving)
/*  43 */       .filter(entity -> (entity instanceof net.minecraft.entity.player.EntityPlayer) ? ((((Boolean)this.players.getValue()).booleanValue() && mc.field_71439_g != entity)) : (EntityUtil.isPassive(entity) ? ((Boolean)this.animals.getValue()).booleanValue() : ((Boolean)this.mobs.getValue()).booleanValue()))
/*  44 */       .filter(entity -> (mc.field_71439_g.func_70068_e(entity) < MathUtil.square(((Integer)this.distance.getValue()).intValue())))
/*  45 */       .filter(entity -> (((Boolean)this.invisibles.getValue()).booleanValue() || !entity.func_82150_aj()))
/*  46 */       .forEach(entity -> {
/*     */           float[] colour = getColorByDistance(entity);
/*     */           drawLineToEntity(entity, colour[0], colour[1], colour[2], colour[3]);
/*     */         });
/*  50 */     GlStateManager.func_179121_F();
/*     */   }
/*     */   
/*     */   public double interpolate(double now, double then) {
/*  54 */     return then + (now - then) * mc.func_184121_ak();
/*     */   }
/*     */   
/*     */   public double[] interpolate(Entity entity) {
/*  58 */     double posX = interpolate(entity.field_70165_t, entity.field_70142_S) - (mc.func_175598_ae()).field_78725_b;
/*  59 */     double posY = interpolate(entity.field_70163_u, entity.field_70137_T) - (mc.func_175598_ae()).field_78726_c;
/*  60 */     double posZ = interpolate(entity.field_70161_v, entity.field_70136_U) - (mc.func_175598_ae()).field_78723_d;
/*  61 */     return new double[] { posX, posY, posZ };
/*     */   }
/*     */   
/*     */   public void drawLineToEntity(Entity e, float red, float green, float blue, float opacity) {
/*  65 */     double[] xyz = interpolate(e);
/*  66 */     drawLine(xyz[0], xyz[1], xyz[2], e.field_70131_O, red, green, blue, opacity);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void drawLine(double posx, double posy, double posz, double up, float red, float green, float blue, float opacity) {
/*  73 */     Vec3d eyes = (new Vec3d(0.0D, 0.0D, 1.0D)).func_178789_a(-((float)Math.toRadians(mc.field_71439_g.field_70125_A))).func_178785_b(
/*  74 */         -((float)Math.toRadians(mc.field_71439_g.field_70177_z)));
/*     */     
/*  76 */     if (!((Boolean)this.drawFromSky.getValue()).booleanValue()) {
/*  77 */       drawLineFromPosToPos(eyes.field_72450_a, eyes.field_72448_b + mc.field_71439_g.func_70047_e(), eyes.field_72449_c, posx, posy, posz, up, red, green, blue, opacity);
/*     */     } else {
/*  79 */       drawLineFromPosToPos(posx, 256.0D, posz, posx, posy, posz, up, red, green, blue, opacity);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void drawLineFromPosToPos(double posx, double posy, double posz, double posx2, double posy2, double posz2, double up, float red, float green, float blue, float opacity) {
/*  84 */     GL11.glBlendFunc(770, 771);
/*  85 */     GL11.glEnable(3042);
/*  86 */     GL11.glLineWidth(((Float)this.width.getValue()).floatValue());
/*  87 */     GL11.glDisable(3553);
/*  88 */     GL11.glDisable(2929);
/*  89 */     GL11.glDepthMask(false);
/*  90 */     GL11.glColor4f(red, green, blue, opacity);
/*  91 */     GlStateManager.func_179140_f();
/*  92 */     GL11.glLoadIdentity();
/*  93 */     mc.field_71460_t.func_78467_g(mc.func_184121_ak());
/*  94 */     GL11.glBegin(1);
/*     */     
/*  96 */     GL11.glVertex3d(posx, posy, posz);
/*  97 */     GL11.glVertex3d(posx2, posy2, posz2);
/*  98 */     GL11.glVertex3d(posx2, posy2, posz2);
/*     */     
/* 100 */     GL11.glEnd();
/* 101 */     GL11.glEnable(3553);
/* 102 */     GL11.glEnable(2929);
/* 103 */     GL11.glDepthMask(true);
/* 104 */     GL11.glDisable(3042);
/* 105 */     GL11.glColor3d(1.0D, 1.0D, 1.0D);
/* 106 */     GlStateManager.func_179145_e();
/*     */   }
/*     */   
/*     */   public float[] getColorByDistance(Entity entity) {
/* 110 */     if (entity instanceof net.minecraft.entity.player.EntityPlayer && Phobos.friendManager.isFriend(entity.func_70005_c_())) {
/* 111 */       return new float[] { 0.0F, 0.5F, 1.0F, 1.0F };
/*     */     }
/* 113 */     AutoCrystal autoCrystal = (AutoCrystal)Phobos.moduleManager.getModuleByClass(AutoCrystal.class);
/* 114 */     Color col = new Color(Color.HSBtoRGB((float)(Math.max(0.0D, Math.min(mc.field_71439_g.func_70068_e(entity), ((Boolean)this.crystalCheck.getValue()).booleanValue() ? (((Float)autoCrystal.placeRange.getValue()).floatValue() * ((Float)autoCrystal.placeRange.getValue()).floatValue()) : 2500.0D) / (((Boolean)this.crystalCheck.getValue()).booleanValue() ? (((Float)autoCrystal.placeRange.getValue()).floatValue() * ((Float)autoCrystal.placeRange.getValue()).floatValue()) : 2500.0F)) / 3.0D), 1.0F, 0.8F) | 0xFF000000);
/* 115 */     return new float[] { col.getRed() / 255.0F, col.getGreen() / 255.0F, col.getBlue() / 255.0F, 1.0F };
/*     */   }
/*     */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\me\earth\phobos\features\modules\render\Tracer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */