/*     */ package me.earth.phobos.features.modules.client;
/*     */ 
/*     */ import java.text.DecimalFormat;
/*     */ import me.earth.phobos.Phobos;
/*     */ import me.earth.phobos.event.events.Render2DEvent;
/*     */ import me.earth.phobos.features.modules.Module;
/*     */ import me.earth.phobos.features.modules.combat.AutoCrystal;
/*     */ import me.earth.phobos.features.modules.combat.Killaura;
/*     */ import me.earth.phobos.features.setting.Setting;
/*     */ import me.earth.phobos.util.ColorUtil;
/*     */ import me.earth.phobos.util.EntityUtil;
/*     */ import me.earth.phobos.util.MathUtil;
/*     */ import me.earth.phobos.util.RenderUtil;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.client.entity.EntityPlayerSP;
/*     */ import net.minecraft.client.gui.ScaledResolution;
/*     */ import net.minecraft.client.gui.inventory.GuiInventory;
/*     */ import net.minecraft.client.renderer.DestroyBlockProgress;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.client.renderer.OpenGlHelper;
/*     */ import net.minecraft.client.renderer.RenderHelper;
/*     */ import net.minecraft.client.renderer.entity.RenderManager;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.init.MobEffects;
/*     */ import net.minecraft.inventory.Slot;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.util.NonNullList;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import net.minecraft.util.math.BlockPos;
/*     */ import net.minecraft.util.math.MathHelper;
/*     */ import net.minecraft.util.math.Vec3d;
/*     */ import org.lwjgl.opengl.GL11;
/*     */ 
/*     */ public class Components
/*     */   extends Module
/*     */ {
/*  40 */   private static final ResourceLocation box = new ResourceLocation("textures/gui/container/shulker_box.png");
/*  41 */   private ResourceLocation logo = new ResourceLocation("textures/phobos.png");
/*     */   
/*     */   private static final double HALF_PI = 1.5707963267948966D;
/*     */   
/*  45 */   public Setting<Boolean> inventory = register(new Setting("Inventory", Boolean.valueOf(false)));
/*  46 */   public Setting<Integer> invX = register(new Setting("InvX", Integer.valueOf(564), Integer.valueOf(0), Integer.valueOf(1000), v -> ((Boolean)this.inventory.getValue()).booleanValue()));
/*  47 */   public Setting<Integer> invY = register(new Setting("InvY", Integer.valueOf(467), Integer.valueOf(0), Integer.valueOf(1000), v -> ((Boolean)this.inventory.getValue()).booleanValue()));
/*  48 */   public Setting<Integer> fineinvX = register(new Setting("InvFineX", Integer.valueOf(0), v -> ((Boolean)this.inventory.getValue()).booleanValue()));
/*  49 */   public Setting<Integer> fineinvY = register(new Setting("InvFineY", Integer.valueOf(0), v -> ((Boolean)this.inventory.getValue()).booleanValue()));
/*  50 */   public Setting<Boolean> renderXCarry = register(new Setting("RenderXCarry", Boolean.valueOf(false), v -> ((Boolean)this.inventory.getValue()).booleanValue()));
/*  51 */   public Setting<Integer> invH = register(new Setting("InvH", Integer.valueOf(3), v -> ((Boolean)this.inventory.getValue()).booleanValue()));
/*  52 */   public Setting<Boolean> holeHud = register(new Setting("HoleHUD", Boolean.valueOf(false)));
/*  53 */   public Setting<Integer> holeX = register(new Setting("HoleX", Integer.valueOf(279), Integer.valueOf(0), Integer.valueOf(1000), v -> ((Boolean)this.holeHud.getValue()).booleanValue()));
/*  54 */   public Setting<Integer> holeY = register(new Setting("HoleY", Integer.valueOf(485), Integer.valueOf(0), Integer.valueOf(1000), v -> ((Boolean)this.holeHud.getValue()).booleanValue()));
/*  55 */   public Setting<Compass> compass = register(new Setting("Compass", Compass.NONE));
/*  56 */   public Setting<Integer> compassX = register(new Setting("CompX", Integer.valueOf(472), Integer.valueOf(0), Integer.valueOf(1000), v -> (this.compass.getValue() != Compass.NONE)));
/*  57 */   public Setting<Integer> compassY = register(new Setting("CompY", Integer.valueOf(424), Integer.valueOf(0), Integer.valueOf(1000), v -> (this.compass.getValue() != Compass.NONE)));
/*  58 */   public Setting<Integer> scale = register(new Setting("Scale", Integer.valueOf(3), Integer.valueOf(0), Integer.valueOf(10), v -> (this.compass.getValue() != Compass.NONE)));
/*  59 */   public Setting<Boolean> playerViewer = register(new Setting("PlayerViewer", Boolean.valueOf(false)));
/*  60 */   public Setting<Integer> playerViewerX = register(new Setting("PlayerX", Integer.valueOf(752), Integer.valueOf(0), Integer.valueOf(1000), v -> ((Boolean)this.playerViewer.getValue()).booleanValue()));
/*  61 */   public Setting<Integer> playerViewerY = register(new Setting("PlayerY", Integer.valueOf(497), Integer.valueOf(0), Integer.valueOf(1000), v -> ((Boolean)this.playerViewer.getValue()).booleanValue()));
/*  62 */   public Setting<Float> playerScale = register(new Setting("PlayerScale", Float.valueOf(1.0F), Float.valueOf(0.1F), Float.valueOf(2.0F), v -> ((Boolean)this.playerViewer.getValue()).booleanValue()));
/*  63 */   public Setting<Boolean> imageLogo = register(new Setting("ImageLogo", Boolean.valueOf(false)));
/*  64 */   public Setting<Integer> imageX = register(new Setting("ImageX", Integer.valueOf(2), Integer.valueOf(0), Integer.valueOf(1000), v -> ((Boolean)this.imageLogo.getValue()).booleanValue()));
/*  65 */   public Setting<Integer> imageY = register(new Setting("ImageY", Integer.valueOf(2), Integer.valueOf(0), Integer.valueOf(1000), v -> ((Boolean)this.imageLogo.getValue()).booleanValue()));
/*  66 */   public Setting<Integer> imageWidth = register(new Setting("ImageWidth", Integer.valueOf(100), Integer.valueOf(0), Integer.valueOf(1000), v -> ((Boolean)this.imageLogo.getValue()).booleanValue()));
/*  67 */   public Setting<Integer> imageHeight = register(new Setting("ImageHeight", Integer.valueOf(100), Integer.valueOf(0), Integer.valueOf(1000), v -> ((Boolean)this.imageLogo.getValue()).booleanValue()));
/*  68 */   public Setting<Boolean> targetHud = register(new Setting("TargetHud", Boolean.valueOf(false)));
/*  69 */   public Setting<Boolean> targetHudBackground = register(new Setting("TargetHudBackground", Boolean.valueOf(true), v -> ((Boolean)this.targetHud.getValue()).booleanValue()));
/*  70 */   public Setting<Integer> targetHudX = register(new Setting("TargetHudX", Integer.valueOf(2), Integer.valueOf(0), Integer.valueOf(1000), v -> ((Boolean)this.targetHud.getValue()).booleanValue()));
/*  71 */   public Setting<Integer> targetHudY = register(new Setting("TargetHudY", Integer.valueOf(2), Integer.valueOf(0), Integer.valueOf(1000), v -> ((Boolean)this.targetHud.getValue()).booleanValue()));
/*     */   
/*     */   public Components() {
/*  74 */     super("Components", "HudComponents", Module.Category.CLIENT, false, false, true);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onRender2D(Render2DEvent event) {
/*  79 */     if (fullNullCheck()) {
/*     */       return;
/*     */     }
/*     */     
/*  83 */     if (((Boolean)this.playerViewer.getValue()).booleanValue()) {
/*  84 */       drawPlayer();
/*     */     }
/*     */     
/*  87 */     if (this.compass.getValue() != Compass.NONE) {
/*  88 */       drawCompass();
/*     */     }
/*     */     
/*  91 */     if (((Boolean)this.holeHud.getValue()).booleanValue()) {
/*  92 */       drawOverlay(event.partialTicks);
/*     */     }
/*     */     
/*  95 */     if (((Boolean)this.inventory.getValue()).booleanValue()) {
/*  96 */       renderInventory();
/*     */     }
/*     */     
/*  99 */     if (((Boolean)this.imageLogo.getValue()).booleanValue()) {
/* 100 */       drawImageLogo();
/*     */     }
/*     */     
/* 103 */     if (((Boolean)this.targetHud.getValue()).booleanValue())
/* 104 */       drawTargetHud(event.partialTicks); 
/*     */   }
/*     */   
/*     */   public void drawTargetHud(float partialTicks) {
/*     */     EntityPlayer target;
/*     */     int healthColor, color;
/* 110 */     if (AutoCrystal.target != null) {
/* 111 */       target = AutoCrystal.target;
/* 112 */     } else if (Killaura.target instanceof EntityPlayer) {
/* 113 */       target = (EntityPlayer)Killaura.target;
/*     */     } else {
/* 115 */       target = getClosestEnemy();
/*     */     } 
/* 117 */     if (target == null)
/* 118 */       return;  if (((Boolean)this.targetHudBackground.getValue()).booleanValue()) {
/* 119 */       RenderUtil.drawRectangleCorrectly(((Integer)this.targetHudX.getValue()).intValue(), ((Integer)this.targetHudY.getValue()).intValue(), 210, 100, ColorUtil.toRGBA(20, 20, 20, 160));
/*     */     }
/* 121 */     GlStateManager.func_179101_C();
/* 122 */     GlStateManager.func_179138_g(OpenGlHelper.field_77476_b);
/* 123 */     GlStateManager.func_179090_x();
/* 124 */     GlStateManager.func_179138_g(OpenGlHelper.field_77478_a);
/* 125 */     GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
/*     */     
/*     */     try {
/* 128 */       GuiInventory.func_147046_a(((Integer)this.targetHudX.getValue()).intValue() + 30, ((Integer)this.targetHudY.getValue()).intValue() + 90, 45, 0.0F, 0.0F, (EntityLivingBase)target);
/* 129 */     } catch (Exception e) {
/* 130 */       e.printStackTrace();
/*     */     } 
/*     */     
/* 133 */     GlStateManager.func_179091_B();
/* 134 */     GlStateManager.func_179098_w();
/* 135 */     GlStateManager.func_179147_l();
/*     */     
/* 137 */     GlStateManager.func_179120_a(770, 771, 1, 0);
/*     */     
/* 139 */     this.renderer.drawStringWithShadow(target.func_70005_c_(), (((Integer)this.targetHudX.getValue()).intValue() + 60), (((Integer)this.targetHudY.getValue()).intValue() + 10), ColorUtil.toRGBA(255, 0, 0, 255));
/*     */ 
/*     */     
/* 142 */     float health = target.func_110143_aJ() + target.func_110139_bj();
/* 143 */     if (health >= 16.0F) {
/* 144 */       healthColor = ColorUtil.toRGBA(0, 255, 0, 255);
/* 145 */     } else if (health >= 10.0F) {
/* 146 */       healthColor = ColorUtil.toRGBA(255, 255, 0, 255);
/*     */     } else {
/* 148 */       healthColor = ColorUtil.toRGBA(255, 0, 0, 255);
/*     */     } 
/*     */     
/* 151 */     DecimalFormat df = new DecimalFormat("##.#");
/*     */     
/* 153 */     this.renderer.drawStringWithShadow(df.format((target.func_110143_aJ() + target.func_110139_bj())), (((Integer)this.targetHudX.getValue()).intValue() + 60 + this.renderer.getStringWidth(target.func_70005_c_() + "  ")), (((Integer)this.targetHudY.getValue()).intValue() + 10), healthColor);
/*     */     
/* 155 */     Integer ping = Integer.valueOf(EntityUtil.isFakePlayer(target) ? 0 : ((mc.func_147114_u().func_175102_a(target.func_110124_au()) == null) ? 0 : mc.func_147114_u().func_175102_a(target.func_110124_au()).func_178853_c()));
/*     */ 
/*     */     
/* 158 */     if (ping.intValue() >= 100) {
/* 159 */       color = ColorUtil.toRGBA(0, 255, 0, 255);
/* 160 */     } else if (ping.intValue() > 50) {
/* 161 */       color = ColorUtil.toRGBA(255, 255, 0, 255);
/*     */     } else {
/* 163 */       color = ColorUtil.toRGBA(255, 0, 0, 255);
/*     */     } 
/*     */     
/* 166 */     this.renderer.drawStringWithShadow("Ping: " + ((ping == null) ? 0 : ping.intValue()), (((Integer)this.targetHudX.getValue()).intValue() + 60), (((Integer)this.targetHudY.getValue()).intValue() + this.renderer.getFontHeight() + 20), color);
/*     */     
/* 168 */     this.renderer.drawStringWithShadow("Pops: " + Phobos.totemPopManager.getTotemPops(target), (((Integer)this.targetHudX.getValue()).intValue() + 60), (((Integer)this.targetHudY.getValue()).intValue() + this.renderer.getFontHeight() * 2 + 30), ColorUtil.toRGBA(255, 0, 0, 255));
/*     */     
/* 170 */     GlStateManager.func_179098_w();
/* 171 */     int iteration = 0;
/* 172 */     int i = ((Integer)this.targetHudX.getValue()).intValue() + 50;
/* 173 */     int y = ((Integer)this.targetHudY.getValue()).intValue() + this.renderer.getFontHeight() * 3 + 44;
/* 174 */     for (ItemStack is : target.field_71071_by.field_70460_b) {
/* 175 */       iteration++;
/* 176 */       if (is.func_190926_b())
/* 177 */         continue;  int x = i - 90 + (9 - iteration) * 20 + 2;
/* 178 */       GlStateManager.func_179126_j();
/* 179 */       RenderUtil.itemRender.field_77023_b = 200.0F;
/* 180 */       RenderUtil.itemRender.func_180450_b(is, x, y);
/* 181 */       RenderUtil.itemRender.func_180453_a(mc.field_71466_p, is, x, y, "");
/* 182 */       RenderUtil.itemRender.field_77023_b = 0.0F;
/* 183 */       GlStateManager.func_179098_w();
/* 184 */       GlStateManager.func_179140_f();
/* 185 */       GlStateManager.func_179097_i();
/* 186 */       String s = (is.func_190916_E() > 1) ? (is.func_190916_E() + "") : "";
/* 187 */       this.renderer.drawStringWithShadow(s, (x + 19 - 2 - this.renderer.getStringWidth(s)), (y + 9), 16777215);
/*     */ 
/*     */       
/* 190 */       int dmg = 0;
/* 191 */       int itemDurability = is.func_77958_k() - is.func_77952_i();
/* 192 */       float green = (is.func_77958_k() - is.func_77952_i()) / is.func_77958_k();
/* 193 */       float red = 1.0F - green;
/* 194 */       dmg = 100 - (int)(red * 100.0F);
/* 195 */       this.renderer.drawStringWithShadow(dmg + "", (x + 8) - this.renderer.getStringWidth(dmg + "") / 2.0F, (y - 5), ColorUtil.toRGBA((int)(red * 255.0F), (int)(green * 255.0F), 0));
/*     */     } 
/*     */     
/* 198 */     drawOverlay(partialTicks, (Entity)target, ((Integer)this.targetHudX.getValue()).intValue() + 150, ((Integer)this.targetHudY.getValue()).intValue() + 6);
/*     */     
/* 200 */     this.renderer.drawStringWithShadow("Strength", (((Integer)this.targetHudX.getValue()).intValue() + 150), (((Integer)this.targetHudY.getValue()).intValue() + 60), target.func_70644_a(MobEffects.field_76420_g) ? ColorUtil.toRGBA(0, 255, 0, 255) : ColorUtil.toRGBA(255, 0, 0, 255));
/*     */     
/* 202 */     this.renderer.drawStringWithShadow("Weakness", (((Integer)this.targetHudX.getValue()).intValue() + 150), (((Integer)this.targetHudY.getValue()).intValue() + this.renderer.getFontHeight() + 70), target.func_70644_a(MobEffects.field_76437_t) ? ColorUtil.toRGBA(0, 255, 0, 255) : ColorUtil.toRGBA(255, 0, 0, 255));
/*     */   }
/*     */ 
/*     */   
/*     */   public static EntityPlayer getClosestEnemy() {
/* 207 */     EntityPlayer closestPlayer = null;
/* 208 */     for (EntityPlayer player : mc.field_71441_e.field_73010_i) {
/* 209 */       if (player == mc.field_71439_g || 
/* 210 */         Phobos.friendManager.isFriend(player))
/* 211 */         continue;  if (closestPlayer == null) {
/* 212 */         closestPlayer = player; continue;
/* 213 */       }  if (mc.field_71439_g.func_70068_e((Entity)player) < mc.field_71439_g.func_70068_e((Entity)closestPlayer)) {
/* 214 */         closestPlayer = player;
/*     */       }
/*     */     } 
/* 217 */     return closestPlayer;
/*     */   }
/*     */   
/*     */   public void drawImageLogo() {
/* 221 */     GlStateManager.func_179098_w();
/* 222 */     GlStateManager.func_179084_k();
/* 223 */     mc.func_110434_K().func_110577_a(this.logo);
/* 224 */     drawCompleteImage(((Integer)this.imageX.getValue()).intValue(), ((Integer)this.imageY.getValue()).intValue(), ((Integer)this.imageWidth.getValue()).intValue(), ((Integer)this.imageHeight.getValue()).intValue());
/* 225 */     mc.func_110434_K().func_147645_c(this.logo);
/* 226 */     GlStateManager.func_179147_l();
/* 227 */     GlStateManager.func_179090_x();
/*     */   }
/*     */   
/*     */   public void drawCompass() {
/* 231 */     ScaledResolution sr = new ScaledResolution(mc);
/* 232 */     if (this.compass.getValue() == Compass.LINE) {
/* 233 */       float playerYaw = mc.field_71439_g.field_70177_z;
/* 234 */       float rotationYaw = MathUtil.wrap(playerYaw);
/* 235 */       RenderUtil.drawRect(((Integer)this.compassX.getValue()).intValue(), ((Integer)this.compassY.getValue()).intValue(), (((Integer)this.compassX.getValue()).intValue() + 100), (((Integer)this.compassY.getValue()).intValue() + this.renderer.getFontHeight()), 1963986960);
/* 236 */       RenderUtil.glScissor(((Integer)this.compassX.getValue()).intValue(), ((Integer)this.compassY.getValue()).intValue(), (((Integer)this.compassX.getValue()).intValue() + 100), (((Integer)this.compassY.getValue()).intValue() + this.renderer.getFontHeight()), sr);
/* 237 */       GL11.glEnable(3089);
/* 238 */       float zeroZeroYaw = MathUtil.wrap((float)(Math.atan2(0.0D - mc.field_71439_g.field_70161_v, 0.0D - mc.field_71439_g.field_70165_t) * 180.0D / Math.PI) - 90.0F);
/* 239 */       RenderUtil.drawLine(((Integer)this.compassX.getValue()).intValue() - rotationYaw + 50.0F + zeroZeroYaw, (((Integer)this.compassY.getValue()).intValue() + 2), ((Integer)this.compassX.getValue()).intValue() - rotationYaw + 50.0F + zeroZeroYaw, (((Integer)this.compassY.getValue()).intValue() + this.renderer.getFontHeight() - 2), 2.0F, -61424);
/* 240 */       RenderUtil.drawLine(((Integer)this.compassX.getValue()).intValue() - rotationYaw + 50.0F + 45.0F, (((Integer)this.compassY.getValue()).intValue() + 2), ((Integer)this.compassX.getValue()).intValue() - rotationYaw + 50.0F + 45.0F, (((Integer)this.compassY.getValue()).intValue() + this.renderer.getFontHeight() - 2), 2.0F, -1);
/* 241 */       RenderUtil.drawLine(((Integer)this.compassX.getValue()).intValue() - rotationYaw + 50.0F - 45.0F, (((Integer)this.compassY.getValue()).intValue() + 2), ((Integer)this.compassX.getValue()).intValue() - rotationYaw + 50.0F - 45.0F, (((Integer)this.compassY.getValue()).intValue() + this.renderer.getFontHeight() - 2), 2.0F, -1);
/* 242 */       RenderUtil.drawLine(((Integer)this.compassX.getValue()).intValue() - rotationYaw + 50.0F + 135.0F, (((Integer)this.compassY.getValue()).intValue() + 2), ((Integer)this.compassX.getValue()).intValue() - rotationYaw + 50.0F + 135.0F, (((Integer)this.compassY.getValue()).intValue() + this.renderer.getFontHeight() - 2), 2.0F, -1);
/* 243 */       RenderUtil.drawLine(((Integer)this.compassX.getValue()).intValue() - rotationYaw + 50.0F - 135.0F, (((Integer)this.compassY.getValue()).intValue() + 2), ((Integer)this.compassX.getValue()).intValue() - rotationYaw + 50.0F - 135.0F, (((Integer)this.compassY.getValue()).intValue() + this.renderer.getFontHeight() - 2), 2.0F, -1);
/* 244 */       this.renderer.drawStringWithShadow("n", ((Integer)this.compassX.getValue()).intValue() - rotationYaw + 50.0F + 180.0F - this.renderer.getStringWidth("n") / 2.0F, ((Integer)this.compassY.getValue()).intValue(), -1);
/* 245 */       this.renderer.drawStringWithShadow("n", ((Integer)this.compassX.getValue()).intValue() - rotationYaw + 50.0F - 180.0F - this.renderer.getStringWidth("n") / 2.0F, ((Integer)this.compassY.getValue()).intValue(), -1);
/* 246 */       this.renderer.drawStringWithShadow("e", ((Integer)this.compassX.getValue()).intValue() - rotationYaw + 50.0F - 90.0F - this.renderer.getStringWidth("e") / 2.0F, ((Integer)this.compassY.getValue()).intValue(), -1);
/* 247 */       this.renderer.drawStringWithShadow("s", ((Integer)this.compassX.getValue()).intValue() - rotationYaw + 50.0F - this.renderer.getStringWidth("s") / 2.0F, ((Integer)this.compassY.getValue()).intValue(), -1);
/* 248 */       this.renderer.drawStringWithShadow("w", ((Integer)this.compassX.getValue()).intValue() - rotationYaw + 50.0F + 90.0F - this.renderer.getStringWidth("w") / 2.0F, ((Integer)this.compassY.getValue()).intValue(), -1);
/* 249 */       RenderUtil.drawLine((((Integer)this.compassX.getValue()).intValue() + 50), (((Integer)this.compassY.getValue()).intValue() + 1), (((Integer)this.compassX.getValue()).intValue() + 50), (((Integer)this.compassY.getValue()).intValue() + this.renderer.getFontHeight() - 1), 2.0F, -7303024);
/* 250 */       GL11.glDisable(3089);
/*     */     } else {
/* 252 */       double centerX = ((Integer)this.compassX.getValue()).intValue();
/* 253 */       double centerY = ((Integer)this.compassY.getValue()).intValue();
/* 254 */       for (Direction dir : Direction.values()) {
/* 255 */         double rad = getPosOnCompass(dir);
/* 256 */         this.renderer.drawStringWithShadow(dir.name(), (float)(centerX + getX(rad)), (float)(centerY + getY(rad)), (dir == Direction.N) ? -65536 : -1);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void drawPlayer(EntityPlayer player, int x, int y) {
/* 262 */     EntityPlayer ent = player;
/* 263 */     GlStateManager.func_179094_E();
/* 264 */     GlStateManager.func_179124_c(1.0F, 1.0F, 1.0F);
/* 265 */     RenderHelper.func_74519_b();
/* 266 */     GlStateManager.func_179141_d();
/* 267 */     GlStateManager.func_179103_j(7424);
/* 268 */     GlStateManager.func_179141_d();
/* 269 */     GlStateManager.func_179126_j();
/* 270 */     GlStateManager.func_179114_b(0.0F, 0.0F, 5.0F, 0.0F);
/* 271 */     GlStateManager.func_179142_g();
/* 272 */     GlStateManager.func_179094_E();
/* 273 */     GlStateManager.func_179109_b((((Integer)this.playerViewerX.getValue()).intValue() + 25), (((Integer)this.playerViewerY.getValue()).intValue() + 25), 50.0F);
/* 274 */     GlStateManager.func_179152_a(-50.0F * ((Float)this.playerScale.getValue()).floatValue(), 50.0F * ((Float)this.playerScale.getValue()).floatValue(), 50.0F * ((Float)this.playerScale.getValue()).floatValue());
/* 275 */     GlStateManager.func_179114_b(180.0F, 0.0F, 0.0F, 1.0F);
/* 276 */     GlStateManager.func_179114_b(135.0F, 0.0F, 1.0F, 0.0F);
/* 277 */     RenderHelper.func_74519_b();
/* 278 */     GlStateManager.func_179114_b(-135.0F, 0.0F, 1.0F, 0.0F);
/* 279 */     GlStateManager.func_179114_b(-((float)Math.atan((((Integer)this.playerViewerY.getValue()).intValue() / 40.0F))) * 20.0F, 1.0F, 0.0F, 0.0F);
/* 280 */     GlStateManager.func_179109_b(0.0F, 0.0F, 0.0F);
/* 281 */     RenderManager rendermanager = mc.func_175598_ae();
/* 282 */     rendermanager.func_178631_a(180.0F);
/* 283 */     rendermanager.func_178633_a(false);
/*     */     try {
/* 285 */       rendermanager.func_188391_a((Entity)ent, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, false);
/* 286 */     } catch (Exception exception) {}
/* 287 */     rendermanager.func_178633_a(true);
/* 288 */     GlStateManager.func_179121_F();
/* 289 */     RenderHelper.func_74518_a();
/* 290 */     GlStateManager.func_179101_C();
/* 291 */     GlStateManager.func_179138_g(OpenGlHelper.field_77476_b);
/* 292 */     GlStateManager.func_179090_x();
/* 293 */     GlStateManager.func_179138_g(OpenGlHelper.field_77478_a);
/* 294 */     GlStateManager.func_179143_c(515);
/* 295 */     GlStateManager.func_179117_G();
/* 296 */     GlStateManager.func_179097_i();
/* 297 */     GlStateManager.func_179121_F();
/*     */   }
/*     */   
/*     */   public void drawPlayer() {
/* 301 */     EntityPlayerSP entityPlayerSP = mc.field_71439_g;
/* 302 */     GlStateManager.func_179094_E();
/* 303 */     GlStateManager.func_179124_c(1.0F, 1.0F, 1.0F);
/* 304 */     RenderHelper.func_74519_b();
/* 305 */     GlStateManager.func_179141_d();
/* 306 */     GlStateManager.func_179103_j(7424);
/* 307 */     GlStateManager.func_179141_d();
/* 308 */     GlStateManager.func_179126_j();
/* 309 */     GlStateManager.func_179114_b(0.0F, 0.0F, 5.0F, 0.0F);
/* 310 */     GlStateManager.func_179142_g();
/* 311 */     GlStateManager.func_179094_E();
/* 312 */     GlStateManager.func_179109_b((((Integer)this.playerViewerX.getValue()).intValue() + 25), (((Integer)this.playerViewerY.getValue()).intValue() + 25), 50.0F);
/* 313 */     GlStateManager.func_179152_a(-50.0F * ((Float)this.playerScale.getValue()).floatValue(), 50.0F * ((Float)this.playerScale.getValue()).floatValue(), 50.0F * ((Float)this.playerScale.getValue()).floatValue());
/* 314 */     GlStateManager.func_179114_b(180.0F, 0.0F, 0.0F, 1.0F);
/* 315 */     GlStateManager.func_179114_b(135.0F, 0.0F, 1.0F, 0.0F);
/* 316 */     RenderHelper.func_74519_b();
/* 317 */     GlStateManager.func_179114_b(-135.0F, 0.0F, 1.0F, 0.0F);
/* 318 */     GlStateManager.func_179114_b(-((float)Math.atan((((Integer)this.playerViewerY.getValue()).intValue() / 40.0F))) * 20.0F, 1.0F, 0.0F, 0.0F);
/* 319 */     GlStateManager.func_179109_b(0.0F, 0.0F, 0.0F);
/* 320 */     RenderManager rendermanager = mc.func_175598_ae();
/* 321 */     rendermanager.func_178631_a(180.0F);
/* 322 */     rendermanager.func_178633_a(false);
/*     */     try {
/* 324 */       rendermanager.func_188391_a((Entity)entityPlayerSP, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, false);
/* 325 */     } catch (Exception exception) {}
/* 326 */     rendermanager.func_178633_a(true);
/* 327 */     GlStateManager.func_179121_F();
/* 328 */     RenderHelper.func_74518_a();
/* 329 */     GlStateManager.func_179101_C();
/* 330 */     GlStateManager.func_179138_g(OpenGlHelper.field_77476_b);
/* 331 */     GlStateManager.func_179090_x();
/* 332 */     GlStateManager.func_179138_g(OpenGlHelper.field_77478_a);
/* 333 */     GlStateManager.func_179143_c(515);
/* 334 */     GlStateManager.func_179117_G();
/* 335 */     GlStateManager.func_179097_i();
/* 336 */     GlStateManager.func_179121_F();
/*     */   }
/*     */   
/*     */   private double getX(double rad) {
/* 340 */     return Math.sin(rad) * (((Integer)this.scale.getValue()).intValue() * 10);
/*     */   }
/*     */   
/*     */   private double getY(double rad) {
/* 344 */     double epicPitch = MathHelper.func_76131_a(mc.field_71439_g.field_70125_A + 30.0F, -90.0F, 90.0F);
/* 345 */     double pitchRadians = Math.toRadians(epicPitch);
/* 346 */     return Math.cos(rad) * Math.sin(pitchRadians) * (((Integer)this.scale.getValue()).intValue() * 10);
/*     */   }
/*     */   
/*     */   private enum Direction {
/* 350 */     N,
/* 351 */     W,
/* 352 */     S,
/* 353 */     E;
/*     */   }
/*     */   
/*     */   private static double getPosOnCompass(Direction dir) {
/* 357 */     double yaw = Math.toRadians(MathHelper.func_76142_g(mc.field_71439_g.field_70177_z));
/* 358 */     int index = dir.ordinal();
/* 359 */     return yaw + index * 1.5707963267948966D;
/*     */   }
/*     */   
/*     */   public enum Compass {
/* 363 */     NONE,
/* 364 */     CIRCLE,
/* 365 */     LINE;
/*     */   }
/*     */   
/*     */   public void drawOverlay(float partialTicks) {
/* 369 */     float yaw = 0.0F;
/* 370 */     int dir = MathHelper.func_76128_c((mc.field_71439_g.field_70177_z * 4.0F / 360.0F) + 0.5D) & 0x3;
/*     */     
/* 372 */     switch (dir) {
/*     */       case 1:
/* 374 */         yaw = 90.0F;
/*     */         break;
/*     */       case 2:
/* 377 */         yaw = -180.0F;
/*     */         break;
/*     */       case 3:
/* 380 */         yaw = -90.0F;
/*     */         break;
/*     */     } 
/*     */ 
/*     */     
/* 385 */     BlockPos northPos = traceToBlock(partialTicks, yaw);
/* 386 */     Block north = getBlock(northPos);
/* 387 */     if (north != null && north != Blocks.field_150350_a) {
/* 388 */       int damage = getBlockDamage(northPos);
/* 389 */       if (damage != 0) {
/* 390 */         RenderUtil.drawRect((((Integer)this.holeX.getValue()).intValue() + 16), ((Integer)this.holeY.getValue()).intValue(), (((Integer)this.holeX.getValue()).intValue() + 32), (((Integer)this.holeY.getValue()).intValue() + 16), 1627324416);
/*     */       }
/* 392 */       drawBlock(north, (((Integer)this.holeX.getValue()).intValue() + 16), ((Integer)this.holeY.getValue()).intValue());
/*     */     } 
/*     */     
/* 395 */     BlockPos southPos = traceToBlock(partialTicks, yaw - 180.0F);
/* 396 */     Block south = getBlock(southPos);
/* 397 */     if (south != null && south != Blocks.field_150350_a) {
/* 398 */       int damage = getBlockDamage(southPos);
/* 399 */       if (damage != 0) {
/* 400 */         RenderUtil.drawRect((((Integer)this.holeX.getValue()).intValue() + 16), (((Integer)this.holeY.getValue()).intValue() + 32), (((Integer)this.holeX.getValue()).intValue() + 32), (((Integer)this.holeY.getValue()).intValue() + 48), 1627324416);
/*     */       }
/* 402 */       drawBlock(south, (((Integer)this.holeX.getValue()).intValue() + 16), (((Integer)this.holeY.getValue()).intValue() + 32));
/*     */     } 
/*     */     
/* 405 */     BlockPos eastPos = traceToBlock(partialTicks, yaw + 90.0F);
/* 406 */     Block east = getBlock(eastPos);
/* 407 */     if (east != null && east != Blocks.field_150350_a) {
/* 408 */       int damage = getBlockDamage(eastPos);
/* 409 */       if (damage != 0) {
/* 410 */         RenderUtil.drawRect((((Integer)this.holeX.getValue()).intValue() + 32), (((Integer)this.holeY.getValue()).intValue() + 16), (((Integer)this.holeX.getValue()).intValue() + 48), (((Integer)this.holeY.getValue()).intValue() + 32), 1627324416);
/*     */       }
/* 412 */       drawBlock(east, (((Integer)this.holeX.getValue()).intValue() + 32), (((Integer)this.holeY.getValue()).intValue() + 16));
/*     */     } 
/*     */     
/* 415 */     BlockPos westPos = traceToBlock(partialTicks, yaw - 90.0F);
/* 416 */     Block west = getBlock(westPos);
/* 417 */     if (west != null && west != Blocks.field_150350_a) {
/* 418 */       int damage = getBlockDamage(westPos);
/*     */       
/* 420 */       if (damage != 0) {
/* 421 */         RenderUtil.drawRect(((Integer)this.holeX.getValue()).intValue(), (((Integer)this.holeY.getValue()).intValue() + 16), (((Integer)this.holeX.getValue()).intValue() + 16), (((Integer)this.holeY.getValue()).intValue() + 32), 1627324416);
/*     */       }
/* 423 */       drawBlock(west, ((Integer)this.holeX.getValue()).intValue(), (((Integer)this.holeY.getValue()).intValue() + 16));
/*     */     } 
/*     */   }
/*     */   
/*     */   public void drawOverlay(float partialTicks, Entity player, int x, int y) {
/* 428 */     float yaw = 0.0F;
/* 429 */     int dir = MathHelper.func_76128_c((player.field_70177_z * 4.0F / 360.0F) + 0.5D) & 0x3;
/*     */     
/* 431 */     switch (dir) {
/*     */       case 1:
/* 433 */         yaw = 90.0F;
/*     */         break;
/*     */       case 2:
/* 436 */         yaw = -180.0F;
/*     */         break;
/*     */       case 3:
/* 439 */         yaw = -90.0F;
/*     */         break;
/*     */     } 
/*     */ 
/*     */     
/* 444 */     BlockPos northPos = traceToBlock(partialTicks, yaw, player);
/* 445 */     Block north = getBlock(northPos);
/* 446 */     if (north != null && north != Blocks.field_150350_a) {
/* 447 */       int damage = getBlockDamage(northPos);
/* 448 */       if (damage != 0) {
/* 449 */         RenderUtil.drawRect((x + 16), y, (x + 32), (y + 16), 1627324416);
/*     */       }
/* 451 */       drawBlock(north, (x + 16), y);
/*     */     } 
/*     */     
/* 454 */     BlockPos southPos = traceToBlock(partialTicks, yaw - 180.0F, player);
/* 455 */     Block south = getBlock(southPos);
/* 456 */     if (south != null && south != Blocks.field_150350_a) {
/* 457 */       int damage = getBlockDamage(southPos);
/* 458 */       if (damage != 0) {
/* 459 */         RenderUtil.drawRect((x + 16), (y + 32), (x + 32), (y + 48), 1627324416);
/*     */       }
/* 461 */       drawBlock(south, (x + 16), (y + 32));
/*     */     } 
/*     */     
/* 464 */     BlockPos eastPos = traceToBlock(partialTicks, yaw + 90.0F, player);
/* 465 */     Block east = getBlock(eastPos);
/* 466 */     if (east != null && east != Blocks.field_150350_a) {
/* 467 */       int damage = getBlockDamage(eastPos);
/* 468 */       if (damage != 0) {
/* 469 */         RenderUtil.drawRect((x + 32), (y + 16), (x + 48), (y + 32), 1627324416);
/*     */       }
/* 471 */       drawBlock(east, (x + 32), (y + 16));
/*     */     } 
/*     */     
/* 474 */     BlockPos westPos = traceToBlock(partialTicks, yaw - 90.0F, player);
/* 475 */     Block west = getBlock(westPos);
/* 476 */     if (west != null && west != Blocks.field_150350_a) {
/* 477 */       int damage = getBlockDamage(westPos);
/*     */       
/* 479 */       if (damage != 0) {
/* 480 */         RenderUtil.drawRect(x, (y + 16), (x + 16), (y + 32), 1627324416);
/*     */       }
/* 482 */       drawBlock(west, x, (y + 16));
/*     */     } 
/*     */   }
/*     */   
/*     */   private int getBlockDamage(BlockPos pos) {
/* 487 */     for (DestroyBlockProgress destBlockProgress : mc.field_71438_f.field_72738_E.values()) {
/* 488 */       if (destBlockProgress.func_180246_b().func_177958_n() == pos.func_177958_n() && destBlockProgress.func_180246_b().func_177956_o() == pos.func_177956_o() && destBlockProgress.func_180246_b().func_177952_p() == pos.func_177952_p()) {
/* 489 */         return destBlockProgress.func_73106_e();
/*     */       }
/*     */     } 
/* 492 */     return 0;
/*     */   }
/*     */   
/*     */   private BlockPos traceToBlock(float partialTicks, float yaw) {
/* 496 */     Vec3d pos = EntityUtil.interpolateEntity((Entity)mc.field_71439_g, partialTicks);
/* 497 */     Vec3d dir = MathUtil.direction(yaw);
/* 498 */     return new BlockPos(pos.field_72450_a + dir.field_72450_a, pos.field_72448_b, pos.field_72449_c + dir.field_72449_c);
/*     */   }
/*     */   
/*     */   private BlockPos traceToBlock(float partialTicks, float yaw, Entity player) {
/* 502 */     Vec3d pos = EntityUtil.interpolateEntity(player, partialTicks);
/* 503 */     Vec3d dir = MathUtil.direction(yaw);
/* 504 */     return new BlockPos(pos.field_72450_a + dir.field_72450_a, pos.field_72448_b, pos.field_72449_c + dir.field_72449_c);
/*     */   }
/*     */   
/*     */   private Block getBlock(BlockPos pos) {
/* 508 */     Block block = mc.field_71441_e.func_180495_p(pos).func_177230_c();
/* 509 */     if (block == Blocks.field_150357_h || block == Blocks.field_150343_Z) {
/* 510 */       return block;
/*     */     }
/* 512 */     return Blocks.field_150350_a;
/*     */   }
/*     */   
/*     */   private void drawBlock(Block block, float x, float y) {
/* 516 */     ItemStack stack = new ItemStack(block);
/* 517 */     GlStateManager.func_179094_E();
/* 518 */     GlStateManager.func_179147_l();
/* 519 */     GlStateManager.func_179120_a(770, 771, 1, 0);
/* 520 */     RenderHelper.func_74520_c();
/* 521 */     GlStateManager.func_179109_b(x, y, 0.0F);
/* 522 */     (mc.func_175599_af()).field_77023_b = 501.0F;
/* 523 */     mc.func_175599_af().func_180450_b(stack, 0, 0);
/* 524 */     (mc.func_175599_af()).field_77023_b = 0.0F;
/* 525 */     RenderHelper.func_74518_a();
/* 526 */     GlStateManager.func_179084_k();
/* 527 */     GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
/* 528 */     GlStateManager.func_179121_F();
/*     */   }
/*     */   
/*     */   public void renderInventory() {
/* 532 */     boxrender(((Integer)this.invX.getValue()).intValue() + ((Integer)this.fineinvX.getValue()).intValue(), ((Integer)this.invY.getValue()).intValue() + ((Integer)this.fineinvY.getValue()).intValue());
/* 533 */     itemrender(mc.field_71439_g.field_71071_by.field_70462_a, ((Integer)this.invX.getValue()).intValue() + ((Integer)this.fineinvX.getValue()).intValue(), ((Integer)this.invY.getValue()).intValue() + ((Integer)this.fineinvY.getValue()).intValue());
/*     */   }
/*     */   
/*     */   private static void preboxrender() {
/* 537 */     GL11.glPushMatrix();
/* 538 */     GlStateManager.func_179094_E();
/* 539 */     GlStateManager.func_179118_c();
/* 540 */     GlStateManager.func_179086_m(256);
/* 541 */     GlStateManager.func_179147_l();
/* 542 */     GlStateManager.func_179131_c(255.0F, 255.0F, 255.0F, 255.0F);
/*     */   }
/*     */   
/*     */   private static void postboxrender() {
/* 546 */     GlStateManager.func_179084_k();
/* 547 */     GlStateManager.func_179097_i();
/* 548 */     GlStateManager.func_179140_f();
/* 549 */     GlStateManager.func_179126_j();
/* 550 */     GlStateManager.func_179141_d();
/* 551 */     GlStateManager.func_179121_F();
/* 552 */     GL11.glPopMatrix();
/*     */   }
/*     */   
/*     */   private static void preitemrender() {
/* 556 */     GL11.glPushMatrix();
/* 557 */     GL11.glDepthMask(true);
/* 558 */     GlStateManager.func_179086_m(256);
/* 559 */     GlStateManager.func_179097_i();
/* 560 */     GlStateManager.func_179126_j();
/* 561 */     RenderHelper.func_74519_b();
/* 562 */     GlStateManager.func_179152_a(1.0F, 1.0F, 0.01F);
/*     */   }
/*     */   
/*     */   private static void postitemrender() {
/* 566 */     GlStateManager.func_179152_a(1.0F, 1.0F, 1.0F);
/* 567 */     RenderHelper.func_74518_a();
/* 568 */     GlStateManager.func_179141_d();
/* 569 */     GlStateManager.func_179084_k();
/* 570 */     GlStateManager.func_179140_f();
/* 571 */     GlStateManager.func_179139_a(0.5D, 0.5D, 0.5D);
/* 572 */     GlStateManager.func_179097_i();
/* 573 */     GlStateManager.func_179126_j();
/* 574 */     GlStateManager.func_179152_a(2.0F, 2.0F, 2.0F);
/* 575 */     GL11.glPopMatrix();
/*     */   }
/*     */   
/*     */   private void boxrender(int x, int y) {
/* 579 */     preboxrender();
/* 580 */     mc.field_71446_o.func_110577_a(box);
/* 581 */     RenderUtil.drawTexturedRect(x, y, 0, 0, 176, 16, 500);
/* 582 */     RenderUtil.drawTexturedRect(x, y + 16, 0, 16, 176, 54 + ((Integer)this.invH.getValue()).intValue(), 500);
/* 583 */     RenderUtil.drawTexturedRect(x, y + 16 + 54, 0, 160, 176, 8, 500);
/* 584 */     postboxrender();
/*     */   }
/*     */   private void itemrender(NonNullList<ItemStack> items, int x, int y) {
/*     */     int i;
/* 588 */     for (i = 0; i < items.size() - 9; i++) {
/* 589 */       int iX = x + i % 9 * 18 + 8;
/* 590 */       int iY = y + i / 9 * 18 + 18;
/* 591 */       ItemStack itemStack = (ItemStack)items.get(i + 9);
/* 592 */       preitemrender();
/* 593 */       (mc.func_175599_af()).field_77023_b = 501.0F;
/* 594 */       RenderUtil.itemRender.func_180450_b(itemStack, iX, iY);
/* 595 */       RenderUtil.itemRender.func_180453_a(mc.field_71466_p, itemStack, iX, iY, null);
/* 596 */       (mc.func_175599_af()).field_77023_b = 0.0F;
/* 597 */       postitemrender();
/*     */     } 
/*     */     
/* 600 */     if (((Boolean)this.renderXCarry.getValue()).booleanValue()) {
/* 601 */       for (i = 1; i < 5; i++) {
/* 602 */         int iX = x + (i + 4) % 9 * 18 + 8;
/* 603 */         ItemStack itemStack = ((Slot)mc.field_71439_g.field_71069_bz.field_75151_b.get(i)).func_75211_c();
/* 604 */         if (itemStack != null && !itemStack.field_190928_g) {
/* 605 */           preitemrender();
/* 606 */           (mc.func_175599_af()).field_77023_b = 501.0F;
/* 607 */           RenderUtil.itemRender.func_180450_b(itemStack, iX, y + 1);
/* 608 */           RenderUtil.itemRender.func_180453_a(mc.field_71466_p, itemStack, iX, y + 1, null);
/* 609 */           (mc.func_175599_af()).field_77023_b = 0.0F;
/* 610 */           postitemrender();
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
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
/*     */   public static void drawCompleteImage(int posX, int posY, int width, int height) {
/* 626 */     GL11.glPushMatrix();
/* 627 */     GL11.glTranslatef(posX, posY, 0.0F);
/* 628 */     GL11.glBegin(7);
/* 629 */     GL11.glTexCoord2f(0.0F, 0.0F);
/* 630 */     GL11.glVertex3f(0.0F, 0.0F, 0.0F);
/* 631 */     GL11.glTexCoord2f(0.0F, 1.0F);
/* 632 */     GL11.glVertex3f(0.0F, height, 0.0F);
/* 633 */     GL11.glTexCoord2f(1.0F, 1.0F);
/* 634 */     GL11.glVertex3f(width, height, 0.0F);
/* 635 */     GL11.glTexCoord2f(1.0F, 0.0F);
/* 636 */     GL11.glVertex3f(width, 0.0F, 0.0F);
/* 637 */     GL11.glEnd();
/* 638 */     GL11.glPopMatrix();
/*     */   }
/*     */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\me\earth\phobos\features\modules\client\Components.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */