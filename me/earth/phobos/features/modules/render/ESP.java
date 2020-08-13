/*     */ package me.earth.phobos.features.modules.render;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import me.earth.phobos.event.events.Render3DEvent;
/*     */ import me.earth.phobos.event.events.RenderEntityModelEvent;
/*     */ import me.earth.phobos.features.modules.Module;
/*     */ import me.earth.phobos.features.setting.Setting;
/*     */ import me.earth.phobos.util.EntityUtil;
/*     */ import me.earth.phobos.util.RenderUtil;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.client.renderer.RenderGlobal;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.util.math.AxisAlignedBB;
/*     */ import net.minecraft.util.math.Vec3d;
/*     */ import org.lwjgl.opengl.GL11;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ESP
/*     */   extends Module
/*     */ {
/*  25 */   private final Setting<Mode> mode = register(new Setting("Mode", Mode.OUTLINE));
/*  26 */   private final Setting<Boolean> players = register(new Setting("Players", Boolean.valueOf(true)));
/*  27 */   private final Setting<Boolean> animals = register(new Setting("Animals", Boolean.valueOf(false)));
/*  28 */   private final Setting<Boolean> mobs = register(new Setting("Mobs", Boolean.valueOf(false)));
/*  29 */   private final Setting<Boolean> items = register(new Setting("Items", Boolean.valueOf(false)));
/*  30 */   private final Setting<Boolean> xporbs = register(new Setting("XpOrbs", Boolean.valueOf(false)));
/*  31 */   private final Setting<Boolean> xpbottles = register(new Setting("XpBottles", Boolean.valueOf(false)));
/*  32 */   private final Setting<Boolean> pearl = register(new Setting("Pearls", Boolean.valueOf(false)));
/*  33 */   private final Setting<Integer> red = register(new Setting("Red", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255)));
/*  34 */   private final Setting<Integer> green = register(new Setting("Green", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255)));
/*  35 */   private final Setting<Integer> blue = register(new Setting("Blue", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255)));
/*  36 */   private final Setting<Integer> boxAlpha = register(new Setting("BoxAlpha", Integer.valueOf(120), Integer.valueOf(0), Integer.valueOf(255)));
/*  37 */   private final Setting<Integer> alpha = register(new Setting("Alpha", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255)));
/*  38 */   private final Setting<Float> lineWidth = register(new Setting("LineWidth", Float.valueOf(2.0F), Float.valueOf(0.1F), Float.valueOf(5.0F)));
/*  39 */   private final Setting<Boolean> colorFriends = register(new Setting("Friends", Boolean.valueOf(true)));
/*  40 */   private final Setting<Boolean> self = register(new Setting("Self", Boolean.valueOf(true)));
/*  41 */   private final Setting<Boolean> onTop = register(new Setting("onTop", Boolean.valueOf(true)));
/*  42 */   private final Setting<Boolean> invisibles = register(new Setting("Invisibles", Boolean.valueOf(false)));
/*     */   
/*  44 */   private static ESP INSTANCE = new ESP();
/*     */   
/*     */   public ESP() {
/*  47 */     super("ESP", "Renders a nice ESP.", Module.Category.RENDER, false, false, false);
/*  48 */     setInstance();
/*     */   }
/*     */   
/*     */   private void setInstance() {
/*  52 */     INSTANCE = this;
/*     */   }
/*     */   
/*     */   public static ESP getInstance() {
/*  56 */     if (INSTANCE == null) {
/*  57 */       INSTANCE = new ESP();
/*     */     }
/*  59 */     return INSTANCE;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onRender3D(Render3DEvent event) {
/*  64 */     if (((Boolean)this.items.getValue()).booleanValue()) {
/*  65 */       int i = 0;
/*  66 */       for (Entity entity : mc.field_71441_e.field_72996_f) {
/*  67 */         if (entity instanceof net.minecraft.entity.item.EntityItem && mc.field_71439_g.func_70068_e(entity) < 2500.0D) {
/*  68 */           Vec3d interp = EntityUtil.getInterpolatedRenderPos(entity, mc.func_184121_ak());
/*  69 */           AxisAlignedBB bb = new AxisAlignedBB((entity.func_174813_aQ()).field_72340_a - 0.05D - entity.field_70165_t + interp.field_72450_a, (entity.func_174813_aQ()).field_72338_b - 0.0D - entity.field_70163_u + interp.field_72448_b, (entity.func_174813_aQ()).field_72339_c - 0.05D - entity.field_70161_v + interp.field_72449_c, (entity.func_174813_aQ()).field_72336_d + 0.05D - entity.field_70165_t + interp.field_72450_a, (entity.func_174813_aQ()).field_72337_e + 0.1D - entity.field_70163_u + interp.field_72448_b, (entity.func_174813_aQ()).field_72334_f + 0.05D - entity.field_70161_v + interp.field_72449_c);
/*  70 */           GlStateManager.func_179094_E();
/*  71 */           GlStateManager.func_179147_l();
/*  72 */           GlStateManager.func_179097_i();
/*  73 */           GlStateManager.func_179120_a(770, 771, 0, 1);
/*  74 */           GlStateManager.func_179090_x();
/*  75 */           GlStateManager.func_179132_a(false);
/*  76 */           GL11.glEnable(2848);
/*  77 */           GL11.glHint(3154, 4354);
/*  78 */           GL11.glLineWidth(1.0F);
/*  79 */           RenderGlobal.func_189696_b(bb, ((Integer)this.red.getValue()).intValue() / 255.0F, ((Integer)this.green.getValue()).intValue() / 255.0F, ((Integer)this.blue.getValue()).intValue() / 255.0F, ((Integer)this.boxAlpha.getValue()).intValue() / 255.0F);
/*  80 */           GL11.glDisable(2848);
/*  81 */           GlStateManager.func_179132_a(true);
/*  82 */           GlStateManager.func_179126_j();
/*  83 */           GlStateManager.func_179098_w();
/*  84 */           GlStateManager.func_179084_k();
/*  85 */           GlStateManager.func_179121_F();
/*  86 */           RenderUtil.drawBlockOutline(bb, new Color(((Integer)this.red.getValue()).intValue(), ((Integer)this.green.getValue()).intValue(), ((Integer)this.blue.getValue()).intValue(), ((Integer)this.alpha.getValue()).intValue()), 1.0F);
/*  87 */           i++;
/*  88 */           if (i >= 50) {
/*     */             break;
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/*  95 */     if (((Boolean)this.xporbs.getValue()).booleanValue()) {
/*  96 */       int i = 0;
/*  97 */       for (Entity entity : mc.field_71441_e.field_72996_f) {
/*  98 */         if (entity instanceof net.minecraft.entity.item.EntityXPOrb && mc.field_71439_g.func_70068_e(entity) < 2500.0D) {
/*  99 */           Vec3d interp = EntityUtil.getInterpolatedRenderPos(entity, mc.func_184121_ak());
/* 100 */           AxisAlignedBB bb = new AxisAlignedBB((entity.func_174813_aQ()).field_72340_a - 0.05D - entity.field_70165_t + interp.field_72450_a, (entity.func_174813_aQ()).field_72338_b - 0.0D - entity.field_70163_u + interp.field_72448_b, (entity.func_174813_aQ()).field_72339_c - 0.05D - entity.field_70161_v + interp.field_72449_c, (entity.func_174813_aQ()).field_72336_d + 0.05D - entity.field_70165_t + interp.field_72450_a, (entity.func_174813_aQ()).field_72337_e + 0.1D - entity.field_70163_u + interp.field_72448_b, (entity.func_174813_aQ()).field_72334_f + 0.05D - entity.field_70161_v + interp.field_72449_c);
/* 101 */           GlStateManager.func_179094_E();
/* 102 */           GlStateManager.func_179147_l();
/* 103 */           GlStateManager.func_179097_i();
/* 104 */           GlStateManager.func_179120_a(770, 771, 0, 1);
/* 105 */           GlStateManager.func_179090_x();
/* 106 */           GlStateManager.func_179132_a(false);
/* 107 */           GL11.glEnable(2848);
/* 108 */           GL11.glHint(3154, 4354);
/* 109 */           GL11.glLineWidth(1.0F);
/* 110 */           RenderGlobal.func_189696_b(bb, ((Integer)this.red.getValue()).intValue() / 255.0F, ((Integer)this.green.getValue()).intValue() / 255.0F, ((Integer)this.blue.getValue()).intValue() / 255.0F, ((Integer)this.boxAlpha.getValue()).intValue() / 255.0F);
/* 111 */           GL11.glDisable(2848);
/* 112 */           GlStateManager.func_179132_a(true);
/* 113 */           GlStateManager.func_179126_j();
/* 114 */           GlStateManager.func_179098_w();
/* 115 */           GlStateManager.func_179084_k();
/* 116 */           GlStateManager.func_179121_F();
/* 117 */           RenderUtil.drawBlockOutline(bb, new Color(((Integer)this.red.getValue()).intValue(), ((Integer)this.green.getValue()).intValue(), ((Integer)this.blue.getValue()).intValue(), ((Integer)this.alpha.getValue()).intValue()), 1.0F);
/* 118 */           i++;
/* 119 */           if (i >= 50) {
/*     */             break;
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/* 125 */     if (((Boolean)this.pearl.getValue()).booleanValue()) {
/* 126 */       int i = 0;
/* 127 */       for (Entity entity : mc.field_71441_e.field_72996_f) {
/* 128 */         if (entity instanceof net.minecraft.entity.item.EntityEnderPearl && mc.field_71439_g.func_70068_e(entity) < 2500.0D) {
/* 129 */           Vec3d interp = EntityUtil.getInterpolatedRenderPos(entity, mc.func_184121_ak());
/* 130 */           AxisAlignedBB bb = new AxisAlignedBB((entity.func_174813_aQ()).field_72340_a - 0.05D - entity.field_70165_t + interp.field_72450_a, (entity.func_174813_aQ()).field_72338_b - 0.0D - entity.field_70163_u + interp.field_72448_b, (entity.func_174813_aQ()).field_72339_c - 0.05D - entity.field_70161_v + interp.field_72449_c, (entity.func_174813_aQ()).field_72336_d + 0.05D - entity.field_70165_t + interp.field_72450_a, (entity.func_174813_aQ()).field_72337_e + 0.1D - entity.field_70163_u + interp.field_72448_b, (entity.func_174813_aQ()).field_72334_f + 0.05D - entity.field_70161_v + interp.field_72449_c);
/* 131 */           GlStateManager.func_179094_E();
/* 132 */           GlStateManager.func_179147_l();
/* 133 */           GlStateManager.func_179097_i();
/* 134 */           GlStateManager.func_179120_a(770, 771, 0, 1);
/* 135 */           GlStateManager.func_179090_x();
/* 136 */           GlStateManager.func_179132_a(false);
/* 137 */           GL11.glEnable(2848);
/* 138 */           GL11.glHint(3154, 4354);
/* 139 */           GL11.glLineWidth(1.0F);
/* 140 */           RenderGlobal.func_189696_b(bb, ((Integer)this.red.getValue()).intValue() / 255.0F, ((Integer)this.green.getValue()).intValue() / 255.0F, ((Integer)this.blue.getValue()).intValue() / 255.0F, ((Integer)this.boxAlpha.getValue()).intValue() / 255.0F);
/* 141 */           GL11.glDisable(2848);
/* 142 */           GlStateManager.func_179132_a(true);
/* 143 */           GlStateManager.func_179126_j();
/* 144 */           GlStateManager.func_179098_w();
/* 145 */           GlStateManager.func_179084_k();
/* 146 */           GlStateManager.func_179121_F();
/* 147 */           RenderUtil.drawBlockOutline(bb, new Color(((Integer)this.red.getValue()).intValue(), ((Integer)this.green.getValue()).intValue(), ((Integer)this.blue.getValue()).intValue(), ((Integer)this.alpha.getValue()).intValue()), 1.0F);
/* 148 */           i++;
/* 149 */           if (i >= 50) {
/*     */             break;
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 156 */     if (((Boolean)this.xpbottles.getValue()).booleanValue()) {
/* 157 */       int i = 0;
/* 158 */       for (Entity entity : mc.field_71441_e.field_72996_f) {
/* 159 */         if (entity instanceof net.minecraft.entity.item.EntityExpBottle && mc.field_71439_g.func_70068_e(entity) < 2500.0D) {
/* 160 */           Vec3d interp = EntityUtil.getInterpolatedRenderPos(entity, mc.func_184121_ak());
/* 161 */           AxisAlignedBB bb = new AxisAlignedBB((entity.func_174813_aQ()).field_72340_a - 0.05D - entity.field_70165_t + interp.field_72450_a, (entity.func_174813_aQ()).field_72338_b - 0.0D - entity.field_70163_u + interp.field_72448_b, (entity.func_174813_aQ()).field_72339_c - 0.05D - entity.field_70161_v + interp.field_72449_c, (entity.func_174813_aQ()).field_72336_d + 0.05D - entity.field_70165_t + interp.field_72450_a, (entity.func_174813_aQ()).field_72337_e + 0.1D - entity.field_70163_u + interp.field_72448_b, (entity.func_174813_aQ()).field_72334_f + 0.05D - entity.field_70161_v + interp.field_72449_c);
/* 162 */           GlStateManager.func_179094_E();
/* 163 */           GlStateManager.func_179147_l();
/* 164 */           GlStateManager.func_179097_i();
/* 165 */           GlStateManager.func_179120_a(770, 771, 0, 1);
/* 166 */           GlStateManager.func_179090_x();
/* 167 */           GlStateManager.func_179132_a(false);
/* 168 */           GL11.glEnable(2848);
/* 169 */           GL11.glHint(3154, 4354);
/* 170 */           GL11.glLineWidth(1.0F);
/* 171 */           RenderGlobal.func_189696_b(bb, ((Integer)this.red.getValue()).intValue() / 255.0F, ((Integer)this.green.getValue()).intValue() / 255.0F, ((Integer)this.blue.getValue()).intValue() / 255.0F, ((Integer)this.boxAlpha.getValue()).intValue() / 255.0F);
/* 172 */           GL11.glDisable(2848);
/* 173 */           GlStateManager.func_179132_a(true);
/* 174 */           GlStateManager.func_179126_j();
/* 175 */           GlStateManager.func_179098_w();
/* 176 */           GlStateManager.func_179084_k();
/* 177 */           GlStateManager.func_179121_F();
/* 178 */           RenderUtil.drawBlockOutline(bb, new Color(((Integer)this.red.getValue()).intValue(), ((Integer)this.green.getValue()).intValue(), ((Integer)this.blue.getValue()).intValue(), ((Integer)this.alpha.getValue()).intValue()), 1.0F);
/* 179 */           i++;
/* 180 */           if (i >= 50) {
/*     */             break;
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void onRenderModel(RenderEntityModelEvent event) {
/* 189 */     if (event.getStage() != 0 || event.entity == null || (event.entity.func_82150_aj() && !((Boolean)this.invisibles.getValue()).booleanValue()) || (!((Boolean)this.self.getValue()).booleanValue() && event.entity.equals(mc.field_71439_g)) || (!((Boolean)this.players.getValue()).booleanValue() && event.entity instanceof net.minecraft.entity.player.EntityPlayer) || (!((Boolean)this.animals.getValue()).booleanValue() && EntityUtil.isPassive(event.entity)) || (!((Boolean)this.mobs.getValue()).booleanValue() && !EntityUtil.isPassive(event.entity) && !(event.entity instanceof net.minecraft.entity.player.EntityPlayer))) {
/*     */       return;
/*     */     }
/*     */     
/* 193 */     Color color = EntityUtil.getColor(event.entity, ((Integer)this.red.getValue()).intValue(), ((Integer)this.green.getValue()).intValue(), ((Integer)this.blue.getValue()).intValue(), ((Integer)this.alpha.getValue()).intValue(), ((Boolean)this.colorFriends.getValue()).booleanValue());
/* 194 */     boolean fancyGraphics = mc.field_71474_y.field_74347_j;
/* 195 */     mc.field_71474_y.field_74347_j = false;
/* 196 */     float gamma = mc.field_71474_y.field_74333_Y;
/* 197 */     mc.field_71474_y.field_74333_Y = 10000.0F;
/*     */     
/* 199 */     if (((Boolean)this.onTop.getValue()).booleanValue()) {
/* 200 */       event.modelBase.func_78088_a(event.entity, event.limbSwing, event.limbSwingAmount, event.age, event.headYaw, event.headPitch, event.scale);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 205 */     if (this.mode.getValue() == Mode.OUTLINE) {
/* 206 */       RenderUtil.renderOne(((Float)this.lineWidth.getValue()).floatValue());
/* 207 */       event.modelBase.func_78088_a(event.entity, event.limbSwing, event.limbSwingAmount, event.age, event.headYaw, event.headPitch, event.scale);
/*     */ 
/*     */       
/* 210 */       GlStateManager.func_187441_d(((Float)this.lineWidth.getValue()).floatValue());
/* 211 */       RenderUtil.renderTwo();
/* 212 */       event.modelBase.func_78088_a(event.entity, event.limbSwing, event.limbSwingAmount, event.age, event.headYaw, event.headPitch, event.scale);
/*     */ 
/*     */       
/* 215 */       GlStateManager.func_187441_d(((Float)this.lineWidth.getValue()).floatValue());
/* 216 */       RenderUtil.renderThree();
/* 217 */       RenderUtil.renderFour(color);
/* 218 */       event.modelBase.func_78088_a(event.entity, event.limbSwing, event.limbSwingAmount, event.age, event.headYaw, event.headPitch, event.scale);
/*     */ 
/*     */       
/* 221 */       GlStateManager.func_187441_d(((Float)this.lineWidth.getValue()).floatValue());
/* 222 */       RenderUtil.renderFive();
/*     */     } else {
/* 224 */       GL11.glPushMatrix();
/* 225 */       GL11.glPushAttrib(1048575);
/* 226 */       if (this.mode.getValue() == Mode.WIREFRAME) {
/* 227 */         GL11.glPolygonMode(1032, 6913);
/*     */       } else {
/* 229 */         GL11.glPolygonMode(1028, 6913);
/*     */       } 
/*     */       
/* 232 */       GL11.glDisable(3553);
/* 233 */       GL11.glDisable(2896);
/* 234 */       GL11.glDisable(2929);
/*     */       
/* 236 */       GL11.glEnable(2848);
/* 237 */       GL11.glEnable(3042);
/*     */       
/* 239 */       GlStateManager.func_179112_b(770, 771);
/* 240 */       GlStateManager.func_179131_c(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
/* 241 */       GlStateManager.func_187441_d(((Float)this.lineWidth.getValue()).floatValue());
/*     */       
/* 243 */       event.modelBase.func_78088_a(event.entity, event.limbSwing, event.limbSwingAmount, event.age, event.headYaw, event.headPitch, event.scale);
/*     */ 
/*     */ 
/*     */       
/* 247 */       GL11.glPopAttrib();
/* 248 */       GL11.glPopMatrix();
/*     */     } 
/*     */     
/* 251 */     if (!((Boolean)this.onTop.getValue()).booleanValue()) {
/* 252 */       event.modelBase.func_78088_a(event.entity, event.limbSwing, event.limbSwingAmount, event.age, event.headYaw, event.headPitch, event.scale);
/*     */     }
/*     */     
/*     */     try {
/* 256 */       mc.field_71474_y.field_74347_j = fancyGraphics;
/* 257 */       mc.field_71474_y.field_74333_Y = gamma;
/* 258 */     } catch (Exception exception) {}
/*     */     
/* 260 */     event.setCanceled(true);
/*     */   }
/*     */   
/*     */   public enum Mode {
/* 264 */     WIREFRAME,
/* 265 */     OUTLINE;
/*     */   }
/*     */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\me\earth\phobos\features\modules\render\ESP.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */