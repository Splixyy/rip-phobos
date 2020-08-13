/*      */ package me.earth.phobos.util;
/*      */ 
/*      */ import java.awt.Color;
/*      */ import java.util.HashMap;
/*      */ import java.util.Objects;
/*      */ import me.earth.phobos.Phobos;
/*      */ import net.minecraft.block.material.Material;
/*      */ import net.minecraft.block.state.IBlockState;
/*      */ import net.minecraft.client.gui.Gui;
/*      */ import net.minecraft.client.gui.ScaledResolution;
/*      */ import net.minecraft.client.model.ModelBiped;
/*      */ import net.minecraft.client.renderer.BufferBuilder;
/*      */ import net.minecraft.client.renderer.GlStateManager;
/*      */ import net.minecraft.client.renderer.OpenGlHelper;
/*      */ import net.minecraft.client.renderer.RenderGlobal;
/*      */ import net.minecraft.client.renderer.RenderItem;
/*      */ import net.minecraft.client.renderer.Tessellator;
/*      */ import net.minecraft.client.renderer.culling.Frustum;
/*      */ import net.minecraft.client.renderer.culling.ICamera;
/*      */ import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
/*      */ import net.minecraft.client.shader.Framebuffer;
/*      */ import net.minecraft.entity.Entity;
/*      */ import net.minecraft.entity.player.EntityPlayer;
/*      */ import net.minecraft.util.EnumFacing;
/*      */ import net.minecraft.util.math.AxisAlignedBB;
/*      */ import net.minecraft.util.math.BlockPos;
/*      */ import net.minecraft.util.math.Vec3d;
/*      */ import net.minecraft.world.IBlockAccess;
/*      */ import net.minecraft.world.World;
/*      */ import org.lwjgl.opengl.EXTFramebufferObject;
/*      */ import org.lwjgl.opengl.GL11;
/*      */ import org.lwjgl.util.glu.Sphere;
/*      */ 
/*      */ public class RenderUtil
/*      */   implements Util
/*      */ {
/*   37 */   public static RenderItem itemRender = mc.func_175599_af();
/*   38 */   public static ICamera camera = (ICamera)new Frustum();
/*      */   
/*   40 */   private static boolean depth = GL11.glIsEnabled(2896);
/*   41 */   private static boolean texture = GL11.glIsEnabled(3042);
/*   42 */   private static boolean clean = GL11.glIsEnabled(3553);
/*   43 */   private static boolean bind = GL11.glIsEnabled(2929);
/*   44 */   private static boolean override = GL11.glIsEnabled(2848);
/*      */   
/*      */   public static void drawRectangleCorrectly(int x, int y, int w, int h, int color) {
/*   47 */     GL11.glLineWidth(1.0F);
/*   48 */     Gui.func_73734_a(x, y, x + w, y + h, color);
/*      */   }
/*      */   
/*      */   public static AxisAlignedBB interpolateAxis(AxisAlignedBB bb) {
/*   52 */     return new AxisAlignedBB(bb.field_72340_a - 
/*   53 */         (mc.func_175598_ae()).field_78730_l, bb.field_72338_b - 
/*   54 */         (mc.func_175598_ae()).field_78731_m, bb.field_72339_c - 
/*   55 */         (mc.func_175598_ae()).field_78728_n, bb.field_72336_d - 
/*   56 */         (mc.func_175598_ae()).field_78730_l, bb.field_72337_e - 
/*   57 */         (mc.func_175598_ae()).field_78731_m, bb.field_72334_f - 
/*   58 */         (mc.func_175598_ae()).field_78728_n);
/*      */   }
/*      */   
/*      */   public static void drawTexturedRect(int x, int y, int textureX, int textureY, int width, int height, int zLevel) {
/*   62 */     Tessellator tessellator = Tessellator.func_178181_a();
/*   63 */     BufferBuilder BufferBuilder = tessellator.func_178180_c();
/*   64 */     BufferBuilder.func_181668_a(7, DefaultVertexFormats.field_181707_g);
/*   65 */     BufferBuilder.func_181662_b((x + 0), (y + height), zLevel).func_187315_a(((textureX + 0) * 0.00390625F), ((textureY + height) * 0.00390625F)).func_181675_d();
/*   66 */     BufferBuilder.func_181662_b((x + width), (y + height), zLevel).func_187315_a(((textureX + width) * 0.00390625F), ((textureY + height) * 0.00390625F)).func_181675_d();
/*   67 */     BufferBuilder.func_181662_b((x + width), (y + 0), zLevel).func_187315_a(((textureX + width) * 0.00390625F), ((textureY + 0) * 0.00390625F)).func_181675_d();
/*   68 */     BufferBuilder.func_181662_b((x + 0), (y + 0), zLevel).func_187315_a(((textureX + 0) * 0.00390625F), ((textureY + 0) * 0.00390625F)).func_181675_d();
/*   69 */     tessellator.func_78381_a();
/*      */   }
/*      */   
/*      */   public static void blockESP(BlockPos b, Color c, double length, double length2) {
/*   73 */     blockEsp(b, c, length, length2);
/*      */   }
/*      */   
/*      */   public static void drawBoxESP(BlockPos pos, Color color, boolean secondC, Color secondColor, float lineWidth, boolean outline, boolean box, int boxAlpha, boolean air) {
/*   77 */     if (box) {
/*   78 */       drawBox(pos, new Color(color.getRed(), color.getGreen(), color.getBlue(), boxAlpha));
/*      */     }
/*      */     
/*   81 */     if (outline) {
/*   82 */       drawBlockOutline(pos, secondC ? secondColor : color, lineWidth, air);
/*      */     }
/*      */   }
/*      */   
/*      */   public static void glScissor(float x, float y, float x1, float y1, ScaledResolution sr) {
/*   87 */     GL11.glScissor((int)(x * sr.func_78325_e()), (int)(mc.field_71440_d - y1 * sr.func_78325_e()), (int)((x1 - x) * sr.func_78325_e()), (int)((y1 - y) * sr.func_78325_e()));
/*      */   }
/*      */   
/*      */   public static void drawLine(float x, float y, float x1, float y1, float thickness, int hex) {
/*   91 */     float red = (hex >> 16 & 0xFF) / 255.0F;
/*   92 */     float green = (hex >> 8 & 0xFF) / 255.0F;
/*   93 */     float blue = (hex & 0xFF) / 255.0F;
/*   94 */     float alpha = (hex >> 24 & 0xFF) / 255.0F;
/*      */     
/*   96 */     GlStateManager.func_179094_E();
/*   97 */     GlStateManager.func_179090_x();
/*   98 */     GlStateManager.func_179147_l();
/*   99 */     GlStateManager.func_179118_c();
/*  100 */     GlStateManager.func_179120_a(770, 771, 1, 0);
/*  101 */     GlStateManager.func_179103_j(7425);
/*  102 */     GL11.glLineWidth(thickness);
/*  103 */     GL11.glEnable(2848);
/*  104 */     GL11.glHint(3154, 4354);
/*  105 */     Tessellator tessellator = Tessellator.func_178181_a();
/*  106 */     BufferBuilder bufferbuilder = tessellator.func_178180_c();
/*  107 */     bufferbuilder.func_181668_a(3, DefaultVertexFormats.field_181706_f);
/*  108 */     bufferbuilder.func_181662_b(x, y, 0.0D).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  109 */     bufferbuilder.func_181662_b(x1, y1, 0.0D).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  110 */     tessellator.func_78381_a();
/*  111 */     GlStateManager.func_179103_j(7424);
/*  112 */     GL11.glDisable(2848);
/*  113 */     GlStateManager.func_179084_k();
/*  114 */     GlStateManager.func_179141_d();
/*  115 */     GlStateManager.func_179098_w();
/*  116 */     GlStateManager.func_179121_F();
/*      */   }
/*      */   
/*      */   public static void drawBox(BlockPos pos, Color color) {
/*  120 */     AxisAlignedBB bb = new AxisAlignedBB(pos.func_177958_n() - (mc.func_175598_ae()).field_78730_l, pos.func_177956_o() - (mc.func_175598_ae()).field_78731_m, pos.func_177952_p() - (mc.func_175598_ae()).field_78728_n, (pos.func_177958_n() + 1) - (mc.func_175598_ae()).field_78730_l, (pos.func_177956_o() + 1) - (mc.func_175598_ae()).field_78731_m, (pos.func_177952_p() + 1) - (mc.func_175598_ae()).field_78728_n);
/*  121 */     camera.func_78547_a(((Entity)Objects.requireNonNull((T)mc.func_175606_aa())).field_70165_t, (mc.func_175606_aa()).field_70163_u, (mc.func_175606_aa()).field_70161_v);
/*  122 */     if (camera.func_78546_a(new AxisAlignedBB(bb.field_72340_a + (mc.func_175598_ae()).field_78730_l, bb.field_72338_b + (mc.func_175598_ae()).field_78731_m, bb.field_72339_c + (mc.func_175598_ae()).field_78728_n, bb.field_72336_d + (mc.func_175598_ae()).field_78730_l, bb.field_72337_e + (mc.func_175598_ae()).field_78731_m, bb.field_72334_f + (mc.func_175598_ae()).field_78728_n))) {
/*  123 */       GlStateManager.func_179094_E();
/*  124 */       GlStateManager.func_179147_l();
/*  125 */       GlStateManager.func_179097_i();
/*  126 */       GlStateManager.func_179120_a(770, 771, 0, 1);
/*  127 */       GlStateManager.func_179090_x();
/*  128 */       GlStateManager.func_179132_a(false);
/*  129 */       GL11.glEnable(2848);
/*  130 */       GL11.glHint(3154, 4354);
/*  131 */       RenderGlobal.func_189696_b(bb, color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F, color.getAlpha() / 255.0F);
/*  132 */       GL11.glDisable(2848);
/*  133 */       GlStateManager.func_179132_a(true);
/*  134 */       GlStateManager.func_179126_j();
/*  135 */       GlStateManager.func_179098_w();
/*  136 */       GlStateManager.func_179084_k();
/*  137 */       GlStateManager.func_179121_F();
/*      */     } 
/*      */   }
/*      */   
/*      */   public static void drawBlockOutline(BlockPos pos, Color color, float linewidth, boolean air) {
/*  142 */     IBlockState iblockstate = mc.field_71441_e.func_180495_p(pos);
/*  143 */     if ((air || iblockstate.func_185904_a() != Material.field_151579_a) && mc.field_71441_e.func_175723_af().func_177746_a(pos)) {
/*  144 */       Vec3d interp = EntityUtil.interpolateEntity((Entity)mc.field_71439_g, mc.func_184121_ak());
/*  145 */       drawBlockOutline(iblockstate.func_185918_c((World)mc.field_71441_e, pos).func_186662_g(0.0020000000949949026D).func_72317_d(-interp.field_72450_a, -interp.field_72448_b, -interp.field_72449_c), color, linewidth);
/*      */     } 
/*      */   }
/*      */   
/*      */   public static void drawBlockOutline(AxisAlignedBB bb, Color color, float linewidth) {
/*  150 */     float red = color.getRed() / 255.0F;
/*  151 */     float green = color.getGreen() / 255.0F;
/*  152 */     float blue = color.getBlue() / 255.0F;
/*  153 */     float alpha = color.getAlpha() / 255.0F;
/*  154 */     GlStateManager.func_179094_E();
/*  155 */     GlStateManager.func_179147_l();
/*  156 */     GlStateManager.func_179097_i();
/*  157 */     GlStateManager.func_179120_a(770, 771, 0, 1);
/*  158 */     GlStateManager.func_179090_x();
/*  159 */     GlStateManager.func_179132_a(false);
/*  160 */     GL11.glEnable(2848);
/*  161 */     GL11.glHint(3154, 4354);
/*  162 */     GL11.glLineWidth(linewidth);
/*  163 */     Tessellator tessellator = Tessellator.func_178181_a();
/*  164 */     BufferBuilder bufferbuilder = tessellator.func_178180_c();
/*  165 */     bufferbuilder.func_181668_a(3, DefaultVertexFormats.field_181706_f);
/*  166 */     bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  167 */     bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  168 */     bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72338_b, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  169 */     bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72338_b, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  170 */     bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  171 */     bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72337_e, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  172 */     bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72337_e, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  173 */     bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  174 */     bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72338_b, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  175 */     bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72337_e, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  176 */     bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72337_e, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  177 */     bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72337_e, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  178 */     bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72337_e, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  179 */     bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72338_b, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  180 */     bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72337_e, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  181 */     bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72337_e, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  182 */     tessellator.func_78381_a();
/*  183 */     GL11.glDisable(2848);
/*  184 */     GlStateManager.func_179132_a(true);
/*  185 */     GlStateManager.func_179126_j();
/*  186 */     GlStateManager.func_179098_w();
/*  187 */     GlStateManager.func_179084_k();
/*  188 */     GlStateManager.func_179121_F();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void drawBoxESP(BlockPos pos, Color color, float lineWidth, boolean outline, boolean box, int boxAlpha) {
/*  198 */     AxisAlignedBB bb = new AxisAlignedBB(pos.func_177958_n() - (mc.func_175598_ae()).field_78730_l, pos.func_177956_o() - (mc.func_175598_ae()).field_78731_m, pos.func_177952_p() - (mc.func_175598_ae()).field_78728_n, (pos.func_177958_n() + 1) - (mc.func_175598_ae()).field_78730_l, (pos.func_177956_o() + 1) - (mc.func_175598_ae()).field_78731_m, (pos.func_177952_p() + 1) - (mc.func_175598_ae()).field_78728_n);
/*  199 */     camera.func_78547_a(((Entity)Objects.requireNonNull((T)mc.func_175606_aa())).field_70165_t, (mc.func_175606_aa()).field_70163_u, (mc.func_175606_aa()).field_70161_v);
/*  200 */     if (camera.func_78546_a(new AxisAlignedBB(bb.field_72340_a + (mc.func_175598_ae()).field_78730_l, bb.field_72338_b + 
/*  201 */           (mc.func_175598_ae()).field_78731_m, bb.field_72339_c + 
/*  202 */           (mc.func_175598_ae()).field_78728_n, bb.field_72336_d + 
/*  203 */           (mc.func_175598_ae()).field_78730_l, bb.field_72337_e + 
/*  204 */           (mc.func_175598_ae()).field_78731_m, bb.field_72334_f + 
/*  205 */           (mc.func_175598_ae()).field_78728_n))) {
/*  206 */       GlStateManager.func_179094_E();
/*  207 */       GlStateManager.func_179147_l();
/*  208 */       GlStateManager.func_179097_i();
/*  209 */       GlStateManager.func_179120_a(770, 771, 0, 1);
/*  210 */       GlStateManager.func_179090_x();
/*  211 */       GlStateManager.func_179132_a(false);
/*  212 */       GL11.glEnable(2848);
/*  213 */       GL11.glHint(3154, 4354);
/*  214 */       GL11.glLineWidth(lineWidth);
/*  215 */       double dist = mc.field_71439_g.func_70011_f((pos.func_177958_n() + 0.5F), (pos.func_177956_o() + 0.5F), (pos.func_177952_p() + 0.5F)) * 0.75D;
/*  216 */       if (box) {
/*  217 */         RenderGlobal.func_189696_b(bb, color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F, boxAlpha / 255.0F);
/*      */       }
/*      */       
/*  220 */       if (outline) {
/*  221 */         RenderGlobal.func_189694_a(bb.field_72340_a, bb.field_72338_b, bb.field_72339_c, bb.field_72336_d, bb.field_72337_e, bb.field_72334_f, color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F, color.getAlpha() / 255.0F);
/*      */       }
/*  223 */       GL11.glDisable(2848);
/*  224 */       GlStateManager.func_179132_a(true);
/*  225 */       GlStateManager.func_179126_j();
/*  226 */       GlStateManager.func_179098_w();
/*  227 */       GlStateManager.func_179084_k();
/*  228 */       GlStateManager.func_179121_F();
/*      */     } 
/*      */   }
/*      */   
/*      */   public static void drawText(BlockPos pos, String text) {
/*  233 */     GlStateManager.func_179094_E();
/*  234 */     glBillboardDistanceScaled(pos.func_177958_n() + 0.5F, pos.func_177956_o() + 0.5F, pos.func_177952_p() + 0.5F, (EntityPlayer)mc.field_71439_g, 1.0F);
/*  235 */     GlStateManager.func_179097_i();
/*  236 */     GlStateManager.func_179137_b(-(Phobos.textManager.getStringWidth(text) / 2.0D), 0.0D, 0.0D);
/*  237 */     Phobos.textManager.drawStringWithShadow(text, 0.0F, 0.0F, -5592406);
/*  238 */     GlStateManager.func_179121_F();
/*      */   }
/*      */   
/*      */   public static void drawOutlinedBlockESP(BlockPos pos, Color color, float linewidth) {
/*  242 */     IBlockState iblockstate = mc.field_71441_e.func_180495_p(pos);
/*  243 */     Vec3d interp = EntityUtil.interpolateEntity((Entity)mc.field_71439_g, mc.func_184121_ak());
/*  244 */     drawBoundingBox(iblockstate.func_185918_c((World)mc.field_71441_e, pos).func_186662_g(0.0020000000949949026D).func_72317_d(-interp.field_72450_a, -interp.field_72448_b, -interp.field_72449_c), linewidth, ColorUtil.toRGBA(color));
/*      */   }
/*      */   
/*      */   public static void blockEsp(BlockPos blockPos, Color c, double length, double length2) {
/*  248 */     double x = blockPos.func_177958_n() - mc.field_175616_W.field_78725_b;
/*  249 */     double y = blockPos.func_177956_o() - mc.field_175616_W.field_78726_c;
/*  250 */     double z = blockPos.func_177952_p() - mc.field_175616_W.field_78723_d;
/*  251 */     GL11.glPushMatrix();
/*  252 */     GL11.glBlendFunc(770, 771);
/*  253 */     GL11.glEnable(3042);
/*  254 */     GL11.glLineWidth(2.0F);
/*  255 */     GL11.glDisable(3553);
/*  256 */     GL11.glDisable(2929);
/*  257 */     GL11.glDepthMask(false);
/*  258 */     GL11.glColor4d((c.getRed() / 255.0F), (c.getGreen() / 255.0F), (c.getBlue() / 255.0F), 0.25D);
/*  259 */     drawColorBox(new AxisAlignedBB(x, y, z, x + length2, y + 1.0D, z + length), 0.0F, 0.0F, 0.0F, 0.0F);
/*  260 */     GL11.glColor4d(0.0D, 0.0D, 0.0D, 0.5D);
/*  261 */     drawSelectionBoundingBox(new AxisAlignedBB(x, y, z, x + length2, y + 1.0D, z + length));
/*  262 */     GL11.glLineWidth(2.0F);
/*  263 */     GL11.glEnable(3553);
/*  264 */     GL11.glEnable(2929);
/*  265 */     GL11.glDepthMask(true);
/*  266 */     GL11.glDisable(3042);
/*  267 */     GL11.glPopMatrix();
/*  268 */     GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
/*      */   }
/*      */   
/*      */   public static void drawRect(float x, float y, float w, float h, int color) {
/*  272 */     float alpha = (color >> 24 & 0xFF) / 255.0F;
/*  273 */     float red = (color >> 16 & 0xFF) / 255.0F;
/*  274 */     float green = (color >> 8 & 0xFF) / 255.0F;
/*  275 */     float blue = (color & 0xFF) / 255.0F;
/*  276 */     Tessellator tessellator = Tessellator.func_178181_a();
/*  277 */     BufferBuilder bufferbuilder = tessellator.func_178180_c();
/*  278 */     GlStateManager.func_179147_l();
/*  279 */     GlStateManager.func_179090_x();
/*  280 */     GlStateManager.func_179120_a(770, 771, 1, 0);
/*  281 */     bufferbuilder.func_181668_a(7, DefaultVertexFormats.field_181706_f);
/*  282 */     bufferbuilder.func_181662_b(x, h, 0.0D).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  283 */     bufferbuilder.func_181662_b(w, h, 0.0D).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  284 */     bufferbuilder.func_181662_b(w, y, 0.0D).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  285 */     bufferbuilder.func_181662_b(x, y, 0.0D).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  286 */     tessellator.func_78381_a();
/*  287 */     GlStateManager.func_179098_w();
/*  288 */     GlStateManager.func_179084_k();
/*      */   }
/*      */   
/*      */   public static void drawColorBox(AxisAlignedBB axisalignedbb, float red, float green, float blue, float alpha) {
/*  292 */     Tessellator ts = Tessellator.func_178181_a();
/*  293 */     BufferBuilder vb = ts.func_178180_c();
/*  294 */     vb.func_181668_a(7, DefaultVertexFormats.field_181707_g);
/*  295 */     vb.func_181662_b(axisalignedbb.field_72340_a, axisalignedbb.field_72338_b, axisalignedbb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  296 */     vb.func_181662_b(axisalignedbb.field_72340_a, axisalignedbb.field_72337_e, axisalignedbb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  297 */     vb.func_181662_b(axisalignedbb.field_72336_d, axisalignedbb.field_72338_b, axisalignedbb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  298 */     vb.func_181662_b(axisalignedbb.field_72336_d, axisalignedbb.field_72337_e, axisalignedbb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  299 */     vb.func_181662_b(axisalignedbb.field_72336_d, axisalignedbb.field_72338_b, axisalignedbb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  300 */     vb.func_181662_b(axisalignedbb.field_72336_d, axisalignedbb.field_72337_e, axisalignedbb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  301 */     vb.func_181662_b(axisalignedbb.field_72340_a, axisalignedbb.field_72338_b, axisalignedbb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  302 */     vb.func_181662_b(axisalignedbb.field_72340_a, axisalignedbb.field_72337_e, axisalignedbb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  303 */     ts.func_78381_a();
/*  304 */     vb.func_181668_a(7, DefaultVertexFormats.field_181707_g);
/*  305 */     vb.func_181662_b(axisalignedbb.field_72336_d, axisalignedbb.field_72337_e, axisalignedbb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  306 */     vb.func_181662_b(axisalignedbb.field_72336_d, axisalignedbb.field_72338_b, axisalignedbb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  307 */     vb.func_181662_b(axisalignedbb.field_72340_a, axisalignedbb.field_72337_e, axisalignedbb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  308 */     vb.func_181662_b(axisalignedbb.field_72340_a, axisalignedbb.field_72338_b, axisalignedbb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  309 */     vb.func_181662_b(axisalignedbb.field_72340_a, axisalignedbb.field_72337_e, axisalignedbb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  310 */     vb.func_181662_b(axisalignedbb.field_72340_a, axisalignedbb.field_72338_b, axisalignedbb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  311 */     vb.func_181662_b(axisalignedbb.field_72336_d, axisalignedbb.field_72337_e, axisalignedbb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  312 */     vb.func_181662_b(axisalignedbb.field_72336_d, axisalignedbb.field_72338_b, axisalignedbb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  313 */     ts.func_78381_a();
/*  314 */     vb.func_181668_a(7, DefaultVertexFormats.field_181707_g);
/*  315 */     vb.func_181662_b(axisalignedbb.field_72340_a, axisalignedbb.field_72337_e, axisalignedbb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  316 */     vb.func_181662_b(axisalignedbb.field_72336_d, axisalignedbb.field_72337_e, axisalignedbb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  317 */     vb.func_181662_b(axisalignedbb.field_72336_d, axisalignedbb.field_72337_e, axisalignedbb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  318 */     vb.func_181662_b(axisalignedbb.field_72340_a, axisalignedbb.field_72337_e, axisalignedbb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  319 */     vb.func_181662_b(axisalignedbb.field_72340_a, axisalignedbb.field_72337_e, axisalignedbb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  320 */     vb.func_181662_b(axisalignedbb.field_72340_a, axisalignedbb.field_72337_e, axisalignedbb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  321 */     vb.func_181662_b(axisalignedbb.field_72336_d, axisalignedbb.field_72337_e, axisalignedbb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  322 */     vb.func_181662_b(axisalignedbb.field_72336_d, axisalignedbb.field_72337_e, axisalignedbb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  323 */     ts.func_78381_a();
/*  324 */     vb.func_181668_a(7, DefaultVertexFormats.field_181707_g);
/*  325 */     vb.func_181662_b(axisalignedbb.field_72340_a, axisalignedbb.field_72338_b, axisalignedbb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  326 */     vb.func_181662_b(axisalignedbb.field_72336_d, axisalignedbb.field_72338_b, axisalignedbb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  327 */     vb.func_181662_b(axisalignedbb.field_72336_d, axisalignedbb.field_72338_b, axisalignedbb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  328 */     vb.func_181662_b(axisalignedbb.field_72340_a, axisalignedbb.field_72338_b, axisalignedbb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  329 */     vb.func_181662_b(axisalignedbb.field_72340_a, axisalignedbb.field_72338_b, axisalignedbb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  330 */     vb.func_181662_b(axisalignedbb.field_72340_a, axisalignedbb.field_72338_b, axisalignedbb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  331 */     vb.func_181662_b(axisalignedbb.field_72336_d, axisalignedbb.field_72338_b, axisalignedbb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  332 */     vb.func_181662_b(axisalignedbb.field_72336_d, axisalignedbb.field_72338_b, axisalignedbb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  333 */     ts.func_78381_a();
/*  334 */     vb.func_181668_a(7, DefaultVertexFormats.field_181707_g);
/*  335 */     vb.func_181662_b(axisalignedbb.field_72340_a, axisalignedbb.field_72338_b, axisalignedbb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  336 */     vb.func_181662_b(axisalignedbb.field_72340_a, axisalignedbb.field_72337_e, axisalignedbb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  337 */     vb.func_181662_b(axisalignedbb.field_72340_a, axisalignedbb.field_72338_b, axisalignedbb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  338 */     vb.func_181662_b(axisalignedbb.field_72340_a, axisalignedbb.field_72337_e, axisalignedbb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  339 */     vb.func_181662_b(axisalignedbb.field_72336_d, axisalignedbb.field_72338_b, axisalignedbb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  340 */     vb.func_181662_b(axisalignedbb.field_72336_d, axisalignedbb.field_72337_e, axisalignedbb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  341 */     vb.func_181662_b(axisalignedbb.field_72336_d, axisalignedbb.field_72338_b, axisalignedbb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  342 */     vb.func_181662_b(axisalignedbb.field_72336_d, axisalignedbb.field_72337_e, axisalignedbb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  343 */     ts.func_78381_a();
/*  344 */     vb.func_181668_a(7, DefaultVertexFormats.field_181707_g);
/*  345 */     vb.func_181662_b(axisalignedbb.field_72340_a, axisalignedbb.field_72337_e, axisalignedbb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  346 */     vb.func_181662_b(axisalignedbb.field_72340_a, axisalignedbb.field_72338_b, axisalignedbb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  347 */     vb.func_181662_b(axisalignedbb.field_72340_a, axisalignedbb.field_72337_e, axisalignedbb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  348 */     vb.func_181662_b(axisalignedbb.field_72340_a, axisalignedbb.field_72338_b, axisalignedbb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  349 */     vb.func_181662_b(axisalignedbb.field_72336_d, axisalignedbb.field_72337_e, axisalignedbb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  350 */     vb.func_181662_b(axisalignedbb.field_72336_d, axisalignedbb.field_72338_b, axisalignedbb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  351 */     vb.func_181662_b(axisalignedbb.field_72336_d, axisalignedbb.field_72337_e, axisalignedbb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  352 */     vb.func_181662_b(axisalignedbb.field_72336_d, axisalignedbb.field_72338_b, axisalignedbb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  353 */     ts.func_78381_a();
/*      */   }
/*      */   
/*      */   public static void drawSelectionBoundingBox(AxisAlignedBB boundingBox) {
/*  357 */     Tessellator tessellator = Tessellator.func_178181_a();
/*  358 */     BufferBuilder vertexbuffer = tessellator.func_178180_c();
/*  359 */     vertexbuffer.func_181668_a(3, DefaultVertexFormats.field_181705_e);
/*  360 */     vertexbuffer.func_181662_b(boundingBox.field_72340_a, boundingBox.field_72338_b, boundingBox.field_72339_c).func_181675_d();
/*  361 */     vertexbuffer.func_181662_b(boundingBox.field_72336_d, boundingBox.field_72338_b, boundingBox.field_72339_c).func_181675_d();
/*  362 */     vertexbuffer.func_181662_b(boundingBox.field_72336_d, boundingBox.field_72338_b, boundingBox.field_72334_f).func_181675_d();
/*  363 */     vertexbuffer.func_181662_b(boundingBox.field_72340_a, boundingBox.field_72338_b, boundingBox.field_72334_f).func_181675_d();
/*  364 */     vertexbuffer.func_181662_b(boundingBox.field_72340_a, boundingBox.field_72338_b, boundingBox.field_72339_c).func_181675_d();
/*  365 */     tessellator.func_78381_a();
/*  366 */     vertexbuffer.func_181668_a(3, DefaultVertexFormats.field_181705_e);
/*  367 */     vertexbuffer.func_181662_b(boundingBox.field_72340_a, boundingBox.field_72337_e, boundingBox.field_72339_c).func_181675_d();
/*  368 */     vertexbuffer.func_181662_b(boundingBox.field_72336_d, boundingBox.field_72337_e, boundingBox.field_72339_c).func_181675_d();
/*  369 */     vertexbuffer.func_181662_b(boundingBox.field_72336_d, boundingBox.field_72337_e, boundingBox.field_72334_f).func_181675_d();
/*  370 */     vertexbuffer.func_181662_b(boundingBox.field_72340_a, boundingBox.field_72337_e, boundingBox.field_72334_f).func_181675_d();
/*  371 */     vertexbuffer.func_181662_b(boundingBox.field_72340_a, boundingBox.field_72337_e, boundingBox.field_72339_c).func_181675_d();
/*  372 */     tessellator.func_78381_a();
/*  373 */     vertexbuffer.func_181668_a(1, DefaultVertexFormats.field_181705_e);
/*  374 */     vertexbuffer.func_181662_b(boundingBox.field_72340_a, boundingBox.field_72338_b, boundingBox.field_72339_c).func_181675_d();
/*  375 */     vertexbuffer.func_181662_b(boundingBox.field_72340_a, boundingBox.field_72337_e, boundingBox.field_72339_c).func_181675_d();
/*  376 */     vertexbuffer.func_181662_b(boundingBox.field_72336_d, boundingBox.field_72338_b, boundingBox.field_72339_c).func_181675_d();
/*  377 */     vertexbuffer.func_181662_b(boundingBox.field_72336_d, boundingBox.field_72337_e, boundingBox.field_72339_c).func_181675_d();
/*  378 */     vertexbuffer.func_181662_b(boundingBox.field_72336_d, boundingBox.field_72338_b, boundingBox.field_72334_f).func_181675_d();
/*  379 */     vertexbuffer.func_181662_b(boundingBox.field_72336_d, boundingBox.field_72337_e, boundingBox.field_72334_f).func_181675_d();
/*  380 */     vertexbuffer.func_181662_b(boundingBox.field_72340_a, boundingBox.field_72338_b, boundingBox.field_72334_f).func_181675_d();
/*  381 */     vertexbuffer.func_181662_b(boundingBox.field_72340_a, boundingBox.field_72337_e, boundingBox.field_72334_f).func_181675_d();
/*  382 */     tessellator.func_78381_a();
/*      */   }
/*      */   
/*      */   public static void glrendermethod() {
/*  386 */     GL11.glEnable(3042);
/*  387 */     GL11.glBlendFunc(770, 771);
/*  388 */     GL11.glEnable(2848);
/*  389 */     GL11.glLineWidth(2.0F);
/*  390 */     GL11.glDisable(3553);
/*  391 */     GL11.glEnable(2884);
/*  392 */     GL11.glDisable(2929);
/*  393 */     double viewerPosX = (mc.func_175598_ae()).field_78730_l;
/*  394 */     double viewerPosY = (mc.func_175598_ae()).field_78731_m;
/*  395 */     double viewerPosZ = (mc.func_175598_ae()).field_78728_n;
/*  396 */     GL11.glPushMatrix();
/*  397 */     GL11.glTranslated(-viewerPosX, -viewerPosY, -viewerPosZ);
/*      */   }
/*      */   
/*      */   public static void glStart(float n, float n2, float n3, float n4) {
/*  401 */     glrendermethod();
/*  402 */     GL11.glColor4f(n, n2, n3, n4);
/*      */   }
/*      */   
/*      */   public static void glEnd() {
/*  406 */     GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
/*  407 */     GL11.glPopMatrix();
/*  408 */     GL11.glEnable(2929);
/*  409 */     GL11.glEnable(3553);
/*  410 */     GL11.glDisable(3042);
/*  411 */     GL11.glDisable(2848);
/*      */   }
/*      */   
/*      */   public static AxisAlignedBB getBoundingBox(BlockPos blockPos) {
/*  415 */     return mc.field_71441_e.func_180495_p(blockPos).func_185900_c((IBlockAccess)mc.field_71441_e, blockPos).func_186670_a(blockPos);
/*      */   }
/*      */   
/*      */   public static void drawOutlinedBox(AxisAlignedBB axisAlignedBB) {
/*  419 */     GL11.glBegin(1);
/*  420 */     GL11.glVertex3d(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c);
/*  421 */     GL11.glVertex3d(axisAlignedBB.field_72336_d, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c);
/*  422 */     GL11.glVertex3d(axisAlignedBB.field_72336_d, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c);
/*  423 */     GL11.glVertex3d(axisAlignedBB.field_72336_d, axisAlignedBB.field_72338_b, axisAlignedBB.field_72334_f);
/*  424 */     GL11.glVertex3d(axisAlignedBB.field_72336_d, axisAlignedBB.field_72338_b, axisAlignedBB.field_72334_f);
/*  425 */     GL11.glVertex3d(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72334_f);
/*  426 */     GL11.glVertex3d(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72334_f);
/*  427 */     GL11.glVertex3d(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c);
/*  428 */     GL11.glVertex3d(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c);
/*  429 */     GL11.glVertex3d(axisAlignedBB.field_72340_a, axisAlignedBB.field_72337_e, axisAlignedBB.field_72339_c);
/*  430 */     GL11.glVertex3d(axisAlignedBB.field_72336_d, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c);
/*  431 */     GL11.glVertex3d(axisAlignedBB.field_72336_d, axisAlignedBB.field_72337_e, axisAlignedBB.field_72339_c);
/*  432 */     GL11.glVertex3d(axisAlignedBB.field_72336_d, axisAlignedBB.field_72338_b, axisAlignedBB.field_72334_f);
/*  433 */     GL11.glVertex3d(axisAlignedBB.field_72336_d, axisAlignedBB.field_72337_e, axisAlignedBB.field_72334_f);
/*  434 */     GL11.glVertex3d(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72334_f);
/*  435 */     GL11.glVertex3d(axisAlignedBB.field_72340_a, axisAlignedBB.field_72337_e, axisAlignedBB.field_72334_f);
/*  436 */     GL11.glVertex3d(axisAlignedBB.field_72340_a, axisAlignedBB.field_72337_e, axisAlignedBB.field_72339_c);
/*  437 */     GL11.glVertex3d(axisAlignedBB.field_72336_d, axisAlignedBB.field_72337_e, axisAlignedBB.field_72339_c);
/*  438 */     GL11.glVertex3d(axisAlignedBB.field_72336_d, axisAlignedBB.field_72337_e, axisAlignedBB.field_72339_c);
/*  439 */     GL11.glVertex3d(axisAlignedBB.field_72336_d, axisAlignedBB.field_72337_e, axisAlignedBB.field_72334_f);
/*  440 */     GL11.glVertex3d(axisAlignedBB.field_72336_d, axisAlignedBB.field_72337_e, axisAlignedBB.field_72334_f);
/*  441 */     GL11.glVertex3d(axisAlignedBB.field_72340_a, axisAlignedBB.field_72337_e, axisAlignedBB.field_72334_f);
/*  442 */     GL11.glVertex3d(axisAlignedBB.field_72340_a, axisAlignedBB.field_72337_e, axisAlignedBB.field_72334_f);
/*  443 */     GL11.glVertex3d(axisAlignedBB.field_72340_a, axisAlignedBB.field_72337_e, axisAlignedBB.field_72339_c);
/*  444 */     GL11.glEnd();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void drawFilledBoxESPN(BlockPos pos, Color color) {
/*  454 */     AxisAlignedBB bb = new AxisAlignedBB(pos.func_177958_n() - (mc.func_175598_ae()).field_78730_l, pos.func_177956_o() - (mc.func_175598_ae()).field_78731_m, pos.func_177952_p() - (mc.func_175598_ae()).field_78728_n, (pos.func_177958_n() + 1) - (mc.func_175598_ae()).field_78730_l, (pos.func_177956_o() + 1) - (mc.func_175598_ae()).field_78731_m, (pos.func_177952_p() + 1) - (mc.func_175598_ae()).field_78728_n);
/*  455 */     int rgba = ColorUtil.toRGBA(color);
/*  456 */     drawFilledBox(bb, rgba);
/*      */   }
/*      */   
/*      */   public static void drawFilledBox(AxisAlignedBB bb, int color) {
/*  460 */     GlStateManager.func_179094_E();
/*  461 */     GlStateManager.func_179147_l();
/*  462 */     GlStateManager.func_179097_i();
/*  463 */     GlStateManager.func_179120_a(770, 771, 0, 1);
/*  464 */     GlStateManager.func_179090_x();
/*  465 */     GlStateManager.func_179132_a(false);
/*      */     
/*  467 */     float alpha = (color >> 24 & 0xFF) / 255.0F;
/*  468 */     float red = (color >> 16 & 0xFF) / 255.0F;
/*  469 */     float green = (color >> 8 & 0xFF) / 255.0F;
/*  470 */     float blue = (color & 0xFF) / 255.0F;
/*      */     
/*  472 */     Tessellator tessellator = Tessellator.func_178181_a();
/*  473 */     BufferBuilder bufferbuilder = tessellator.func_178180_c();
/*      */     
/*  475 */     bufferbuilder.func_181668_a(7, DefaultVertexFormats.field_181706_f);
/*  476 */     bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  477 */     bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72338_b, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  478 */     bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72338_b, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  479 */     bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
/*      */     
/*  481 */     bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72337_e, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  482 */     bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72337_e, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  483 */     bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72337_e, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  484 */     bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72337_e, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
/*      */     
/*  486 */     bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  487 */     bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72337_e, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  488 */     bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72337_e, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  489 */     bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72338_b, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
/*      */     
/*  491 */     bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72338_b, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  492 */     bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72337_e, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  493 */     bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72337_e, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  494 */     bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72338_b, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
/*      */     
/*  496 */     bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  497 */     bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72338_b, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  498 */     bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72337_e, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  499 */     bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72337_e, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
/*      */     
/*  501 */     bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  502 */     bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  503 */     bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72337_e, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  504 */     bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72337_e, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  505 */     tessellator.func_78381_a();
/*  506 */     GlStateManager.func_179132_a(true);
/*  507 */     GlStateManager.func_179126_j();
/*  508 */     GlStateManager.func_179098_w();
/*  509 */     GlStateManager.func_179084_k();
/*  510 */     GlStateManager.func_179121_F();
/*      */   }
/*      */   
/*      */   public static void drawBoundingBox(AxisAlignedBB bb, float width, int color) {
/*  514 */     GlStateManager.func_179094_E();
/*  515 */     GlStateManager.func_179147_l();
/*  516 */     GlStateManager.func_179097_i();
/*  517 */     GlStateManager.func_179120_a(770, 771, 0, 1);
/*  518 */     GlStateManager.func_179090_x();
/*  519 */     GlStateManager.func_179132_a(false);
/*  520 */     GL11.glEnable(2848);
/*  521 */     GL11.glHint(3154, 4354);
/*  522 */     GL11.glLineWidth(width);
/*      */     
/*  524 */     float alpha = (color >> 24 & 0xFF) / 255.0F;
/*  525 */     float red = (color >> 16 & 0xFF) / 255.0F;
/*  526 */     float green = (color >> 8 & 0xFF) / 255.0F;
/*  527 */     float blue = (color & 0xFF) / 255.0F;
/*      */     
/*  529 */     Tessellator tessellator = Tessellator.func_178181_a();
/*  530 */     BufferBuilder bufferbuilder = tessellator.func_178180_c();
/*      */     
/*  532 */     bufferbuilder.func_181668_a(3, DefaultVertexFormats.field_181706_f);
/*  533 */     bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  534 */     bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  535 */     bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72338_b, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  536 */     bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72338_b, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  537 */     bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  538 */     bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72337_e, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  539 */     bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72337_e, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  540 */     bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  541 */     bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72338_b, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  542 */     bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72337_e, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  543 */     bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72337_e, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  544 */     bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72337_e, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  545 */     bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72337_e, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  546 */     bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72338_b, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  547 */     bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72337_e, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  548 */     bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72337_e, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  549 */     tessellator.func_78381_a();
/*  550 */     GL11.glDisable(2848);
/*  551 */     GlStateManager.func_179132_a(true);
/*  552 */     GlStateManager.func_179126_j();
/*  553 */     GlStateManager.func_179098_w();
/*  554 */     GlStateManager.func_179084_k();
/*  555 */     GlStateManager.func_179121_F();
/*      */   }
/*      */   
/*      */   public static void glBillboard(float x, float y, float z) {
/*  559 */     float scale = 0.02666667F;
/*  560 */     GlStateManager.func_179137_b(x - (mc.func_175598_ae()).field_78725_b, y - (mc.func_175598_ae()).field_78726_c, z - (mc.func_175598_ae()).field_78723_d);
/*  561 */     GlStateManager.func_187432_a(0.0F, 1.0F, 0.0F);
/*  562 */     GlStateManager.func_179114_b(-mc.field_71439_g.field_70177_z, 0.0F, 1.0F, 0.0F);
/*  563 */     GlStateManager.func_179114_b(mc.field_71439_g.field_70125_A, (mc.field_71474_y.field_74320_O == 2) ? -1.0F : 1.0F, 0.0F, 0.0F);
/*  564 */     GlStateManager.func_179152_a(-scale, -scale, scale);
/*      */   }
/*      */   
/*      */   public static void glBillboardDistanceScaled(float x, float y, float z, EntityPlayer player, float scale) {
/*  568 */     glBillboard(x, y, z);
/*  569 */     int distance = (int)player.func_70011_f(x, y, z);
/*  570 */     float scaleDistance = distance / 2.0F / (2.0F + 2.0F - scale);
/*  571 */     if (scaleDistance < 1.0F)
/*  572 */       scaleDistance = 1.0F; 
/*  573 */     GlStateManager.func_179152_a(scaleDistance, scaleDistance, scaleDistance);
/*      */   }
/*      */   
/*      */   public static void drawColoredBoundingBox(AxisAlignedBB bb, float width, float red, float green, float blue, float alpha) {
/*  577 */     GlStateManager.func_179094_E();
/*  578 */     GlStateManager.func_179147_l();
/*  579 */     GlStateManager.func_179097_i();
/*  580 */     GlStateManager.func_179120_a(770, 771, 0, 1);
/*  581 */     GlStateManager.func_179090_x();
/*  582 */     GlStateManager.func_179132_a(false);
/*  583 */     GL11.glEnable(2848);
/*  584 */     GL11.glHint(3154, 4354);
/*  585 */     GL11.glLineWidth(width);
/*      */     
/*  587 */     Tessellator tessellator = Tessellator.func_178181_a();
/*  588 */     BufferBuilder bufferbuilder = tessellator.func_178180_c();
/*      */     
/*  590 */     bufferbuilder.func_181668_a(3, DefaultVertexFormats.field_181706_f);
/*  591 */     bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72339_c).func_181666_a(red, green, blue, 0.0F).func_181675_d();
/*  592 */     bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  593 */     bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72338_b, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  594 */     bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72338_b, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  595 */     bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  596 */     bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  597 */     bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72337_e, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  598 */     bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72337_e, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  599 */     bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72337_e, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  600 */     bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72337_e, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  601 */     bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72337_e, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  602 */     bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72337_e, bb.field_72334_f).func_181666_a(red, green, blue, 0.0F).func_181675_d();
/*  603 */     bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  604 */     bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72337_e, bb.field_72334_f).func_181666_a(red, green, blue, 0.0F).func_181675_d();
/*  605 */     bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72338_b, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  606 */     bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72337_e, bb.field_72339_c).func_181666_a(red, green, blue, 0.0F).func_181675_d();
/*  607 */     bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72338_b, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
/*  608 */     bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72338_b, bb.field_72339_c).func_181666_a(red, green, blue, 0.0F).func_181675_d();
/*  609 */     tessellator.func_78381_a();
/*  610 */     GL11.glDisable(2848);
/*  611 */     GlStateManager.func_179132_a(true);
/*  612 */     GlStateManager.func_179126_j();
/*  613 */     GlStateManager.func_179098_w();
/*  614 */     GlStateManager.func_179084_k();
/*  615 */     GlStateManager.func_179121_F();
/*      */   }
/*      */   
/*      */   public static void drawSphere(double x, double y, double z, float size, int slices, int stacks) {
/*  619 */     Sphere s = new Sphere();
/*  620 */     GL11.glPushMatrix();
/*  621 */     GL11.glBlendFunc(770, 771);
/*  622 */     GL11.glEnable(3042);
/*  623 */     GL11.glLineWidth(1.2F);
/*  624 */     GL11.glDisable(3553);
/*  625 */     GL11.glDisable(2929);
/*  626 */     GL11.glDepthMask(false);
/*  627 */     s.setDrawStyle(100013);
/*  628 */     GL11.glTranslated(x - mc.field_175616_W.field_78725_b, y - mc.field_175616_W.field_78726_c, z - mc.field_175616_W.field_78723_d);
/*  629 */     s.draw(size, slices, stacks);
/*  630 */     GL11.glLineWidth(2.0F);
/*  631 */     GL11.glEnable(3553);
/*  632 */     GL11.glEnable(2929);
/*  633 */     GL11.glDepthMask(true);
/*  634 */     GL11.glDisable(3042);
/*  635 */     GL11.glPopMatrix();
/*      */   }
/*      */   
/*      */   public static void GLPre(float lineWidth) {
/*  639 */     depth = GL11.glIsEnabled(2896);
/*  640 */     texture = GL11.glIsEnabled(3042);
/*  641 */     clean = GL11.glIsEnabled(3553);
/*  642 */     bind = GL11.glIsEnabled(2929);
/*  643 */     override = GL11.glIsEnabled(2848);
/*  644 */     GLPre(depth, texture, clean, bind, override, lineWidth);
/*      */   }
/*      */   
/*      */   public static void GlPost() {
/*  648 */     GLPost(depth, texture, clean, bind, override);
/*      */   }
/*      */   
/*      */   private static void GLPre(boolean depth, boolean texture, boolean clean, boolean bind, boolean override, float lineWidth) {
/*  652 */     if (depth) {
/*  653 */       GL11.glDisable(2896);
/*      */     }
/*  655 */     if (!texture) {
/*  656 */       GL11.glEnable(3042);
/*      */     }
/*  658 */     GL11.glLineWidth(lineWidth);
/*      */     
/*  660 */     if (clean) {
/*  661 */       GL11.glDisable(3553);
/*      */     }
/*  663 */     if (bind) {
/*  664 */       GL11.glDisable(2929);
/*      */     }
/*  666 */     if (!override) {
/*  667 */       GL11.glEnable(2848);
/*      */     }
/*  669 */     GlStateManager.func_187401_a(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
/*  670 */     GL11.glHint(3154, 4354);
/*  671 */     GlStateManager.func_179132_a(false);
/*      */   }
/*      */   
/*      */   public static float[][] getBipedRotations(ModelBiped biped) {
/*  675 */     float[][] rotations = new float[5][];
/*      */     
/*  677 */     float[] headRotation = new float[3];
/*  678 */     headRotation[0] = biped.field_78116_c.field_78795_f;
/*  679 */     headRotation[1] = biped.field_78116_c.field_78796_g;
/*  680 */     headRotation[2] = biped.field_78116_c.field_78808_h;
/*  681 */     rotations[0] = headRotation;
/*      */     
/*  683 */     float[] rightArmRotation = new float[3];
/*  684 */     rightArmRotation[0] = biped.field_178723_h.field_78795_f;
/*  685 */     rightArmRotation[1] = biped.field_178723_h.field_78796_g;
/*  686 */     rightArmRotation[2] = biped.field_178723_h.field_78808_h;
/*  687 */     rotations[1] = rightArmRotation;
/*      */     
/*  689 */     float[] leftArmRotation = new float[3];
/*  690 */     leftArmRotation[0] = biped.field_178724_i.field_78795_f;
/*  691 */     leftArmRotation[1] = biped.field_178724_i.field_78796_g;
/*  692 */     leftArmRotation[2] = biped.field_178724_i.field_78808_h;
/*  693 */     rotations[2] = leftArmRotation;
/*      */     
/*  695 */     float[] rightLegRotation = new float[3];
/*  696 */     rightLegRotation[0] = biped.field_178721_j.field_78795_f;
/*  697 */     rightLegRotation[1] = biped.field_178721_j.field_78796_g;
/*  698 */     rightLegRotation[2] = biped.field_178721_j.field_78808_h;
/*  699 */     rotations[3] = rightLegRotation;
/*      */     
/*  701 */     float[] leftLegRotation = new float[3];
/*  702 */     leftLegRotation[0] = biped.field_178722_k.field_78795_f;
/*  703 */     leftLegRotation[1] = biped.field_178722_k.field_78796_g;
/*  704 */     leftLegRotation[2] = biped.field_178722_k.field_78808_h;
/*  705 */     rotations[4] = leftLegRotation;
/*      */     
/*  707 */     return rotations;
/*      */   }
/*      */   
/*      */   private static void GLPost(boolean depth, boolean texture, boolean clean, boolean bind, boolean override) {
/*  711 */     GlStateManager.func_179132_a(true);
/*  712 */     if (!override) {
/*  713 */       GL11.glDisable(2848);
/*      */     }
/*  715 */     if (bind) {
/*  716 */       GL11.glEnable(2929);
/*      */     }
/*  718 */     if (clean) {
/*  719 */       GL11.glEnable(3553);
/*      */     }
/*  721 */     if (!texture) {
/*  722 */       GL11.glDisable(3042);
/*      */     }
/*  724 */     if (depth) {
/*  725 */       GL11.glEnable(2896);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public static void drawArc(float cx, float cy, float r, float start_angle, float end_angle, int num_segments) {
/*  731 */     GL11.glBegin(4);
/*      */     
/*  733 */     for (int i = (int)(num_segments / 360.0F / start_angle) + 1; i <= num_segments / 360.0F / end_angle; i++) {
/*  734 */       double previousangle = 6.283185307179586D * (i - 1) / num_segments;
/*  735 */       double angle = 6.283185307179586D * i / num_segments;
/*  736 */       GL11.glVertex2d(cx, cy);
/*  737 */       GL11.glVertex2d(cx + Math.cos(angle) * r, cy + Math.sin(angle) * r);
/*  738 */       GL11.glVertex2d(cx + Math.cos(previousangle) * r, cy + Math.sin(previousangle) * r);
/*      */     } 
/*      */     
/*  741 */     glEnd();
/*      */   }
/*      */   
/*      */   public static void drawArcOutline(float cx, float cy, float r, float start_angle, float end_angle, int num_segments) {
/*  745 */     GL11.glBegin(2);
/*      */     
/*  747 */     for (int i = (int)(num_segments / 360.0F / start_angle) + 1; i <= num_segments / 360.0F / end_angle; i++) {
/*  748 */       double angle = 6.283185307179586D * i / num_segments;
/*  749 */       GL11.glVertex2d(cx + Math.cos(angle) * r, cy + Math.sin(angle) * r);
/*      */     } 
/*      */     
/*  752 */     glEnd();
/*      */   }
/*      */   
/*      */   public static void drawCircleOutline(float x, float y, float radius) {
/*  756 */     drawCircleOutline(x, y, radius, 0, 360, 40);
/*      */   }
/*      */   
/*      */   public static void drawCircleOutline(float x, float y, float radius, int start, int end, int segments) {
/*  760 */     drawArcOutline(x, y, radius, start, end, segments);
/*      */   }
/*      */   
/*      */   public static void drawCircle(float x, float y, float radius) {
/*  764 */     drawCircle(x, y, radius, 0, 360, 64);
/*      */   }
/*      */   
/*      */   public static void drawCircle(float x, float y, float radius, int start, int end, int segments) {
/*  768 */     drawArc(x, y, radius, start, end, segments);
/*      */   }
/*      */   
/*      */   public static void drawOutlinedRoundedRectangle(int x, int y, int width, int height, float radius, float dR, float dG, float dB, float dA, float outlineWidth) {
/*  772 */     drawRoundedRectangle(x, y, width, height, radius);
/*  773 */     GL11.glColor4f(dR, dG, dB, dA);
/*  774 */     drawRoundedRectangle(x + outlineWidth, y + outlineWidth, width - outlineWidth * 2.0F, height - outlineWidth * 2.0F, radius);
/*      */   }
/*      */   
/*      */   public static void drawRectangle(float x, float y, float width, float height) {
/*  778 */     GL11.glEnable(3042);
/*  779 */     GL11.glBlendFunc(770, 771);
/*      */     
/*  781 */     GL11.glBegin(2);
/*      */     
/*  783 */     GL11.glVertex2d(width, 0.0D);
/*  784 */     GL11.glVertex2d(0.0D, 0.0D);
/*  785 */     GL11.glVertex2d(0.0D, height);
/*  786 */     GL11.glVertex2d(width, height);
/*      */     
/*  788 */     glEnd();
/*      */   }
/*      */   
/*      */   public static void drawRectangleXY(float x, float y, float width, float height) {
/*  792 */     GL11.glEnable(3042);
/*  793 */     GL11.glBlendFunc(770, 771);
/*      */     
/*  795 */     GL11.glBegin(2);
/*      */     
/*  797 */     GL11.glVertex2d((x + width), y);
/*  798 */     GL11.glVertex2d(x, y);
/*  799 */     GL11.glVertex2d(x, (y + height));
/*  800 */     GL11.glVertex2d((x + width), (y + height));
/*      */     
/*  802 */     glEnd();
/*      */   }
/*      */   
/*      */   public static void drawFilledRectangle(float x, float y, float width, float height) {
/*  806 */     GL11.glEnable(3042);
/*  807 */     GL11.glBlendFunc(770, 771);
/*      */     
/*  809 */     GL11.glBegin(7);
/*      */     
/*  811 */     GL11.glVertex2d((x + width), y);
/*  812 */     GL11.glVertex2d(x, y);
/*  813 */     GL11.glVertex2d(x, (y + height));
/*  814 */     GL11.glVertex2d((x + width), (y + height));
/*      */     
/*  816 */     glEnd();
/*      */   }
/*      */   
/*      */   public static void drawRoundedRectangle(float x, float y, float width, float height, float radius) {
/*  820 */     GL11.glEnable(3042);
/*  821 */     drawArc(x + width - radius, y + height - radius, radius, 0.0F, 90.0F, 16);
/*  822 */     drawArc(x + radius, y + height - radius, radius, 90.0F, 180.0F, 16);
/*  823 */     drawArc(x + radius, y + radius, radius, 180.0F, 270.0F, 16);
/*  824 */     drawArc(x + width - radius, y + radius, radius, 270.0F, 360.0F, 16);
/*      */     
/*  826 */     GL11.glBegin(4);
/*      */     
/*  828 */     GL11.glVertex2d((x + width - radius), y);
/*  829 */     GL11.glVertex2d((x + radius), y);
/*  830 */     GL11.glVertex2d((x + width - radius), (y + radius));
/*      */     
/*  832 */     GL11.glVertex2d((x + width - radius), (y + radius));
/*  833 */     GL11.glVertex2d((x + radius), y);
/*  834 */     GL11.glVertex2d((x + radius), (y + radius));
/*      */ 
/*      */     
/*  837 */     GL11.glVertex2d((x + width), (y + radius));
/*  838 */     GL11.glVertex2d(x, (y + radius));
/*  839 */     GL11.glVertex2d(x, (y + height - radius));
/*      */     
/*  841 */     GL11.glVertex2d((x + width), (y + radius));
/*  842 */     GL11.glVertex2d(x, (y + height - radius));
/*  843 */     GL11.glVertex2d((x + width), (y + height - radius));
/*      */ 
/*      */     
/*  846 */     GL11.glVertex2d((x + width - radius), (y + height - radius));
/*  847 */     GL11.glVertex2d((x + radius), (y + height - radius));
/*  848 */     GL11.glVertex2d((x + width - radius), (y + height));
/*      */     
/*  850 */     GL11.glVertex2d((x + width - radius), (y + height));
/*  851 */     GL11.glVertex2d((x + radius), (y + height - radius));
/*  852 */     GL11.glVertex2d((x + radius), (y + height));
/*      */     
/*  854 */     glEnd();
/*      */   }
/*      */   
/*      */   public static void renderOne(float lineWidth) {
/*  858 */     checkSetupFBO();
/*  859 */     GL11.glPushAttrib(1048575);
/*  860 */     GL11.glDisable(3008);
/*  861 */     GL11.glDisable(3553);
/*  862 */     GL11.glDisable(2896);
/*  863 */     GL11.glEnable(3042);
/*  864 */     GL11.glBlendFunc(770, 771);
/*  865 */     GL11.glLineWidth(lineWidth);
/*  866 */     GL11.glEnable(2848);
/*  867 */     GL11.glEnable(2960);
/*  868 */     GL11.glClear(1024);
/*  869 */     GL11.glClearStencil(15);
/*  870 */     GL11.glStencilFunc(512, 1, 15);
/*  871 */     GL11.glStencilOp(7681, 7681, 7681);
/*  872 */     GL11.glPolygonMode(1032, 6913);
/*      */   }
/*      */   
/*      */   public static void renderTwo() {
/*  876 */     GL11.glStencilFunc(512, 0, 15);
/*  877 */     GL11.glStencilOp(7681, 7681, 7681);
/*  878 */     GL11.glPolygonMode(1032, 6914);
/*      */   }
/*      */   
/*      */   public static void renderThree() {
/*  882 */     GL11.glStencilFunc(514, 1, 15);
/*  883 */     GL11.glStencilOp(7680, 7680, 7680);
/*  884 */     GL11.glPolygonMode(1032, 6913);
/*      */   }
/*      */   
/*      */   public static void renderFour(Color color) {
/*  888 */     setColor(color);
/*  889 */     GL11.glDepthMask(false);
/*  890 */     GL11.glDisable(2929);
/*  891 */     GL11.glEnable(10754);
/*  892 */     GL11.glPolygonOffset(1.0F, -2000000.0F);
/*  893 */     OpenGlHelper.func_77475_a(OpenGlHelper.field_77476_b, 240.0F, 240.0F);
/*      */   }
/*      */   
/*      */   public static void renderFive() {
/*  897 */     GL11.glPolygonOffset(1.0F, 2000000.0F);
/*  898 */     GL11.glDisable(10754);
/*  899 */     GL11.glEnable(2929);
/*  900 */     GL11.glDepthMask(true);
/*  901 */     GL11.glDisable(2960);
/*  902 */     GL11.glDisable(2848);
/*  903 */     GL11.glHint(3154, 4352);
/*  904 */     GL11.glEnable(3042);
/*  905 */     GL11.glEnable(2896);
/*  906 */     GL11.glEnable(3553);
/*  907 */     GL11.glEnable(3008);
/*  908 */     GL11.glPopAttrib();
/*      */   }
/*      */   
/*      */   public static void setColor(Color color) {
/*  912 */     GL11.glColor4d(color.getRed() / 255.0D, color.getGreen() / 255.0D, color.getBlue() / 255.0D, color.getAlpha() / 255.0D);
/*      */   }
/*      */   
/*      */   public static void checkSetupFBO() {
/*  916 */     Framebuffer fbo = mc.field_147124_at;
/*  917 */     if (fbo != null && 
/*  918 */       fbo.field_147624_h > -1) {
/*  919 */       setupFBO(fbo);
/*  920 */       fbo.field_147624_h = -1;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private static void setupFBO(Framebuffer fbo) {
/*  926 */     EXTFramebufferObject.glDeleteRenderbuffersEXT(fbo.field_147624_h);
/*  927 */     int stencilDepthBufferID = EXTFramebufferObject.glGenRenderbuffersEXT();
/*  928 */     EXTFramebufferObject.glBindRenderbufferEXT(36161, stencilDepthBufferID);
/*  929 */     EXTFramebufferObject.glRenderbufferStorageEXT(36161, 34041, mc.field_71443_c, mc.field_71440_d);
/*  930 */     EXTFramebufferObject.glFramebufferRenderbufferEXT(36160, 36128, 36161, stencilDepthBufferID);
/*  931 */     EXTFramebufferObject.glFramebufferRenderbufferEXT(36160, 36096, 36161, stencilDepthBufferID);
/*      */   }
/*      */   
/*      */   public static final class GeometryMasks
/*      */   {
/*  936 */     public static final HashMap<EnumFacing, Integer> FACEMAP = new HashMap<>();
/*      */     static {
/*  938 */       FACEMAP.put(EnumFacing.DOWN, Integer.valueOf(1));
/*  939 */       FACEMAP.put(EnumFacing.WEST, Integer.valueOf(16));
/*  940 */       FACEMAP.put(EnumFacing.NORTH, Integer.valueOf(4));
/*  941 */       FACEMAP.put(EnumFacing.SOUTH, Integer.valueOf(8));
/*  942 */       FACEMAP.put(EnumFacing.EAST, Integer.valueOf(32));
/*  943 */       FACEMAP.put(EnumFacing.UP, Integer.valueOf(2));
/*      */     }
/*      */     
/*      */     public static final class Quad {
/*      */       public static final int DOWN = 1;
/*      */       public static final int UP = 2;
/*      */       public static final int NORTH = 4;
/*      */       public static final int SOUTH = 8;
/*      */       public static final int WEST = 16;
/*      */       public static final int EAST = 32;
/*      */       public static final int ALL = 63;
/*      */     }
/*      */     
/*      */     public static final class Line {
/*      */       public static final int DOWN_WEST = 17;
/*      */       public static final int UP_WEST = 18;
/*      */       public static final int DOWN_EAST = 33;
/*      */       public static final int UP_EAST = 34;
/*      */       public static final int DOWN_NORTH = 5;
/*      */       public static final int UP_NORTH = 6;
/*      */       public static final int DOWN_SOUTH = 9;
/*      */       public static final int UP_SOUTH = 10;
/*      */       public static final int NORTH_WEST = 20;
/*      */       public static final int NORTH_EAST = 36;
/*      */       public static final int SOUTH_WEST = 24;
/*      */       public static final int SOUTH_EAST = 40;
/*      */       public static final int ALL = 63;
/*      */     }
/*      */   }
/*      */   
/*      */   public static class RenderTesselator
/*      */     extends Tessellator {
/*  975 */     public static RenderTesselator INSTANCE = new RenderTesselator();
/*      */     
/*      */     public RenderTesselator() {
/*  978 */       super(2097152);
/*      */     }
/*      */     
/*      */     public static void prepare(int mode) {
/*  982 */       prepareGL();
/*  983 */       begin(mode);
/*      */     }
/*      */ 
/*      */     
/*      */     public static void prepareGL() {
/*  988 */       GL11.glBlendFunc(770, 771);
/*  989 */       GlStateManager.func_187428_a(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
/*  990 */       GlStateManager.func_187441_d(1.5F);
/*  991 */       GlStateManager.func_179090_x();
/*  992 */       GlStateManager.func_179132_a(false);
/*  993 */       GlStateManager.func_179147_l();
/*  994 */       GlStateManager.func_179097_i();
/*  995 */       GlStateManager.func_179140_f();
/*  996 */       GlStateManager.func_179129_p();
/*  997 */       GlStateManager.func_179141_d();
/*  998 */       GlStateManager.func_179124_c(1.0F, 1.0F, 1.0F);
/*      */     }
/*      */     
/*      */     public static void begin(int mode) {
/* 1002 */       INSTANCE.func_178180_c().func_181668_a(mode, DefaultVertexFormats.field_181706_f);
/*      */     }
/*      */     
/*      */     public static void release() {
/* 1006 */       render();
/* 1007 */       releaseGL();
/*      */     }
/*      */     
/*      */     public static void render() {
/* 1011 */       INSTANCE.func_78381_a();
/*      */     }
/*      */     
/*      */     public static void releaseGL() {
/* 1015 */       GlStateManager.func_179089_o();
/* 1016 */       GlStateManager.func_179132_a(true);
/* 1017 */       GlStateManager.func_179098_w();
/* 1018 */       GlStateManager.func_179147_l();
/* 1019 */       GlStateManager.func_179126_j();
/*      */     }
/*      */     
/*      */     public static void drawBox(BlockPos blockPos, int argb, int sides) {
/* 1023 */       int a = argb >>> 24 & 0xFF;
/* 1024 */       int r = argb >>> 16 & 0xFF;
/* 1025 */       int g = argb >>> 8 & 0xFF;
/* 1026 */       int b = argb & 0xFF;
/* 1027 */       drawBox(blockPos, r, g, b, a, sides);
/*      */     }
/*      */     
/*      */     public static void drawBox(float x, float y, float z, int argb, int sides) {
/* 1031 */       int a = argb >>> 24 & 0xFF;
/* 1032 */       int r = argb >>> 16 & 0xFF;
/* 1033 */       int g = argb >>> 8 & 0xFF;
/* 1034 */       int b = argb & 0xFF;
/* 1035 */       drawBox(INSTANCE.func_178180_c(), x, y, z, 1.0F, 1.0F, 1.0F, r, g, b, a, sides);
/*      */     }
/*      */     
/*      */     public static void drawBox(BlockPos blockPos, int r, int g, int b, int a, int sides) {
/* 1039 */       drawBox(INSTANCE.func_178180_c(), blockPos.func_177958_n(), blockPos.func_177956_o(), blockPos.func_177952_p(), 1.0F, 1.0F, 1.0F, r, g, b, a, sides);
/*      */     }
/*      */     
/*      */     public static BufferBuilder getBufferBuilder() {
/* 1043 */       return INSTANCE.func_178180_c();
/*      */     }
/*      */     
/*      */     public static void drawBox(BufferBuilder buffer, float x, float y, float z, float w, float h, float d, int r, int g, int b, int a, int sides) {
/* 1047 */       if ((sides & 0x1) != 0) {
/* 1048 */         buffer.func_181662_b((x + w), y, z).func_181669_b(r, g, b, a).func_181675_d();
/* 1049 */         buffer.func_181662_b((x + w), y, (z + d)).func_181669_b(r, g, b, a).func_181675_d();
/* 1050 */         buffer.func_181662_b(x, y, (z + d)).func_181669_b(r, g, b, a).func_181675_d();
/* 1051 */         buffer.func_181662_b(x, y, z).func_181669_b(r, g, b, a).func_181675_d();
/*      */       } 
/*      */       
/* 1054 */       if ((sides & 0x2) != 0) {
/* 1055 */         buffer.func_181662_b((x + w), (y + h), z).func_181669_b(r, g, b, a).func_181675_d();
/* 1056 */         buffer.func_181662_b(x, (y + h), z).func_181669_b(r, g, b, a).func_181675_d();
/* 1057 */         buffer.func_181662_b(x, (y + h), (z + d)).func_181669_b(r, g, b, a).func_181675_d();
/* 1058 */         buffer.func_181662_b((x + w), (y + h), (z + d)).func_181669_b(r, g, b, a).func_181675_d();
/*      */       } 
/*      */       
/* 1061 */       if ((sides & 0x4) != 0) {
/* 1062 */         buffer.func_181662_b((x + w), y, z).func_181669_b(r, g, b, a).func_181675_d();
/* 1063 */         buffer.func_181662_b(x, y, z).func_181669_b(r, g, b, a).func_181675_d();
/* 1064 */         buffer.func_181662_b(x, (y + h), z).func_181669_b(r, g, b, a).func_181675_d();
/* 1065 */         buffer.func_181662_b((x + w), (y + h), z).func_181669_b(r, g, b, a).func_181675_d();
/*      */       } 
/*      */       
/* 1068 */       if ((sides & 0x8) != 0) {
/* 1069 */         buffer.func_181662_b(x, y, (z + d)).func_181669_b(r, g, b, a).func_181675_d();
/* 1070 */         buffer.func_181662_b((x + w), y, (z + d)).func_181669_b(r, g, b, a).func_181675_d();
/* 1071 */         buffer.func_181662_b((x + w), (y + h), (z + d)).func_181669_b(r, g, b, a).func_181675_d();
/* 1072 */         buffer.func_181662_b(x, (y + h), (z + d)).func_181669_b(r, g, b, a).func_181675_d();
/*      */       } 
/*      */       
/* 1075 */       if ((sides & 0x10) != 0) {
/* 1076 */         buffer.func_181662_b(x, y, z).func_181669_b(r, g, b, a).func_181675_d();
/* 1077 */         buffer.func_181662_b(x, y, (z + d)).func_181669_b(r, g, b, a).func_181675_d();
/* 1078 */         buffer.func_181662_b(x, (y + h), (z + d)).func_181669_b(r, g, b, a).func_181675_d();
/* 1079 */         buffer.func_181662_b(x, (y + h), z).func_181669_b(r, g, b, a).func_181675_d();
/*      */       } 
/*      */       
/* 1082 */       if ((sides & 0x20) != 0) {
/* 1083 */         buffer.func_181662_b((x + w), y, (z + d)).func_181669_b(r, g, b, a).func_181675_d();
/* 1084 */         buffer.func_181662_b((x + w), y, z).func_181669_b(r, g, b, a).func_181675_d();
/* 1085 */         buffer.func_181662_b((x + w), (y + h), z).func_181669_b(r, g, b, a).func_181675_d();
/* 1086 */         buffer.func_181662_b((x + w), (y + h), (z + d)).func_181669_b(r, g, b, a).func_181675_d();
/*      */       } 
/*      */     }
/*      */     
/*      */     public static void drawLines(BufferBuilder buffer, float x, float y, float z, float w, float h, float d, int r, int g, int b, int a, int sides) {
/* 1091 */       if ((sides & 0x11) != 0) {
/* 1092 */         buffer.func_181662_b(x, y, z).func_181669_b(r, g, b, a).func_181675_d();
/* 1093 */         buffer.func_181662_b(x, y, (z + d)).func_181669_b(r, g, b, a).func_181675_d();
/*      */       } 
/*      */       
/* 1096 */       if ((sides & 0x12) != 0) {
/* 1097 */         buffer.func_181662_b(x, (y + h), z).func_181669_b(r, g, b, a).func_181675_d();
/* 1098 */         buffer.func_181662_b(x, (y + h), (z + d)).func_181669_b(r, g, b, a).func_181675_d();
/*      */       } 
/*      */       
/* 1101 */       if ((sides & 0x21) != 0) {
/* 1102 */         buffer.func_181662_b((x + w), y, z).func_181669_b(r, g, b, a).func_181675_d();
/* 1103 */         buffer.func_181662_b((x + w), y, (z + d)).func_181669_b(r, g, b, a).func_181675_d();
/*      */       } 
/*      */       
/* 1106 */       if ((sides & 0x22) != 0) {
/* 1107 */         buffer.func_181662_b((x + w), (y + h), z).func_181669_b(r, g, b, a).func_181675_d();
/* 1108 */         buffer.func_181662_b((x + w), (y + h), (z + d)).func_181669_b(r, g, b, a).func_181675_d();
/*      */       } 
/*      */       
/* 1111 */       if ((sides & 0x5) != 0) {
/* 1112 */         buffer.func_181662_b(x, y, z).func_181669_b(r, g, b, a).func_181675_d();
/* 1113 */         buffer.func_181662_b((x + w), y, z).func_181669_b(r, g, b, a).func_181675_d();
/*      */       } 
/*      */       
/* 1116 */       if ((sides & 0x6) != 0) {
/* 1117 */         buffer.func_181662_b(x, (y + h), z).func_181669_b(r, g, b, a).func_181675_d();
/* 1118 */         buffer.func_181662_b((x + w), (y + h), z).func_181669_b(r, g, b, a).func_181675_d();
/*      */       } 
/*      */       
/* 1121 */       if ((sides & 0x9) != 0) {
/* 1122 */         buffer.func_181662_b(x, y, (z + d)).func_181669_b(r, g, b, a).func_181675_d();
/* 1123 */         buffer.func_181662_b((x + w), y, (z + d)).func_181669_b(r, g, b, a).func_181675_d();
/*      */       } 
/*      */       
/* 1126 */       if ((sides & 0xA) != 0) {
/* 1127 */         buffer.func_181662_b(x, (y + h), (z + d)).func_181669_b(r, g, b, a).func_181675_d();
/* 1128 */         buffer.func_181662_b((x + w), (y + h), (z + d)).func_181669_b(r, g, b, a).func_181675_d();
/*      */       } 
/*      */       
/* 1131 */       if ((sides & 0x14) != 0) {
/* 1132 */         buffer.func_181662_b(x, y, z).func_181669_b(r, g, b, a).func_181675_d();
/* 1133 */         buffer.func_181662_b(x, (y + h), z).func_181669_b(r, g, b, a).func_181675_d();
/*      */       } 
/*      */       
/* 1136 */       if ((sides & 0x24) != 0) {
/* 1137 */         buffer.func_181662_b((x + w), y, z).func_181669_b(r, g, b, a).func_181675_d();
/* 1138 */         buffer.func_181662_b((x + w), (y + h), z).func_181669_b(r, g, b, a).func_181675_d();
/*      */       } 
/*      */       
/* 1141 */       if ((sides & 0x18) != 0) {
/* 1142 */         buffer.func_181662_b(x, y, (z + d)).func_181669_b(r, g, b, a).func_181675_d();
/* 1143 */         buffer.func_181662_b(x, (y + h), (z + d)).func_181669_b(r, g, b, a).func_181675_d();
/*      */       } 
/*      */       
/* 1146 */       if ((sides & 0x28) != 0) {
/* 1147 */         buffer.func_181662_b((x + w), y, (z + d)).func_181669_b(r, g, b, a).func_181675_d();
/* 1148 */         buffer.func_181662_b((x + w), (y + h), (z + d)).func_181669_b(r, g, b, a).func_181675_d();
/*      */       } 
/*      */     }
/*      */     
/*      */     public static void drawBoundingBox(AxisAlignedBB bb, float width, float red, float green, float blue, float alpha) {
/* 1153 */       GlStateManager.func_179094_E();
/* 1154 */       GlStateManager.func_179147_l();
/* 1155 */       GlStateManager.func_179097_i();
/* 1156 */       GlStateManager.func_179120_a(770, 771, 0, 1);
/* 1157 */       GlStateManager.func_179090_x();
/* 1158 */       GlStateManager.func_179132_a(false);
/* 1159 */       GL11.glEnable(2848);
/* 1160 */       GL11.glHint(3154, 4354);
/* 1161 */       GL11.glLineWidth(width);
/* 1162 */       Tessellator tessellator = Tessellator.func_178181_a();
/* 1163 */       BufferBuilder bufferbuilder = tessellator.func_178180_c();
/* 1164 */       bufferbuilder.func_181668_a(3, DefaultVertexFormats.field_181706_f);
/* 1165 */       bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
/* 1166 */       bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
/* 1167 */       bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72338_b, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
/* 1168 */       bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72338_b, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
/* 1169 */       bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
/* 1170 */       bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72337_e, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
/* 1171 */       bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72337_e, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
/* 1172 */       bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
/* 1173 */       bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72338_b, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
/* 1174 */       bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72337_e, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
/* 1175 */       bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72337_e, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
/* 1176 */       bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72337_e, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
/* 1177 */       bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72337_e, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
/* 1178 */       bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72338_b, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
/* 1179 */       bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72337_e, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
/* 1180 */       bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72337_e, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
/* 1181 */       tessellator.func_78381_a();
/* 1182 */       GL11.glDisable(2848);
/* 1183 */       GlStateManager.func_179132_a(true);
/* 1184 */       GlStateManager.func_179126_j();
/* 1185 */       GlStateManager.func_179098_w();
/* 1186 */       GlStateManager.func_179084_k();
/* 1187 */       GlStateManager.func_179121_F();
/*      */     }
/*      */     
/*      */     public static void drawFullBox(AxisAlignedBB bb, BlockPos blockPos, float width, int argb, int alpha2) {
/* 1191 */       int a = argb >>> 24 & 0xFF;
/* 1192 */       int r = argb >>> 16 & 0xFF;
/* 1193 */       int g = argb >>> 8 & 0xFF;
/* 1194 */       int b = argb & 0xFF;
/* 1195 */       drawFullBox(bb, blockPos, width, r, g, b, a, alpha2);
/*      */     }
/*      */     
/*      */     public static void drawFullBox(AxisAlignedBB bb, BlockPos blockPos, float width, int red, int green, int blue, int alpha, int alpha2) {
/* 1199 */       prepare(7);
/* 1200 */       drawBox(blockPos, red, green, blue, alpha, 63);
/* 1201 */       release();
/* 1202 */       drawBoundingBox(bb, width, red, green, blue, alpha2);
/*      */     }
/*      */     
/*      */     public static void drawHalfBox(BlockPos blockPos, int argb, int sides) {
/* 1206 */       int a = argb >>> 24 & 0xFF;
/* 1207 */       int r = argb >>> 16 & 0xFF;
/* 1208 */       int g = argb >>> 8 & 0xFF;
/* 1209 */       int b = argb & 0xFF;
/* 1210 */       drawHalfBox(blockPos, r, g, b, a, sides);
/*      */     }
/*      */     
/*      */     public static void drawHalfBox(BlockPos blockPos, int r, int g, int b, int a, int sides) {
/* 1214 */       drawBox(INSTANCE.func_178180_c(), blockPos.func_177958_n(), blockPos.func_177956_o(), blockPos.func_177952_p(), 1.0F, 0.5F, 1.0F, r, g, b, a, sides);
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\me\earth\phobo\\util\RenderUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */