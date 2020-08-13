/*     */ package me.earth.phobos.features.modules.render;
/*     */ 
/*     */ import java.util.Objects;
/*     */ import me.earth.phobos.Phobos;
/*     */ import me.earth.phobos.event.events.Render3DEvent;
/*     */ import me.earth.phobos.features.modules.Module;
/*     */ import me.earth.phobos.features.setting.Setting;
/*     */ import me.earth.phobos.util.DamageUtil;
/*     */ import me.earth.phobos.util.EntityUtil;
/*     */ import me.earth.phobos.util.RenderUtil;
/*     */ import net.minecraft.client.network.NetHandlerPlayClient;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.client.renderer.RenderHelper;
/*     */ import net.minecraft.enchantment.Enchantment;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.nbt.NBTTagList;
/*     */ import net.minecraft.util.text.TextFormatting;
/*     */ import org.lwjgl.opengl.GL11;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Nametags
/*     */   extends Module
/*     */ {
/*  28 */   private final Setting<Boolean> health = register(new Setting("Health", Boolean.valueOf(true)));
/*  29 */   private final Setting<Boolean> armor = register(new Setting("Armor", Boolean.valueOf(true)));
/*  30 */   private final Setting<Float> scaling = register(new Setting("Size", Float.valueOf(0.3F), Float.valueOf(0.1F), Float.valueOf(20.0F)));
/*  31 */   private final Setting<Boolean> invisibles = register(new Setting("Invisibles", Boolean.valueOf(false)));
/*  32 */   private final Setting<Boolean> ping = register(new Setting("Ping", Boolean.valueOf(true)));
/*  33 */   private final Setting<Boolean> totemPops = register(new Setting("TotemPops", Boolean.valueOf(true)));
/*  34 */   private final Setting<Boolean> gamemode = register(new Setting("Gamemode", Boolean.valueOf(false)));
/*  35 */   private final Setting<Boolean> entityID = register(new Setting("ID", Boolean.valueOf(false)));
/*  36 */   private final Setting<Boolean> rect = register(new Setting("Rectangle", Boolean.valueOf(true)));
/*  37 */   private final Setting<Boolean> sneak = register(new Setting("SneakColor", Boolean.valueOf(false)));
/*  38 */   private final Setting<Boolean> heldStackName = register(new Setting("StackName", Boolean.valueOf(false)));
/*  39 */   private final Setting<Boolean> whiter = register(new Setting("White", Boolean.valueOf(false)));
/*  40 */   private final Setting<Boolean> scaleing = register(new Setting("Scale", Boolean.valueOf(false)));
/*  41 */   private final Setting<Float> factor = register(new Setting("Factor", Float.valueOf(0.3F), Float.valueOf(0.1F), Float.valueOf(1.0F), v -> ((Boolean)this.scaleing.getValue()).booleanValue()));
/*  42 */   private final Setting<Boolean> smartScale = register(new Setting("SmartScale", Boolean.valueOf(false), v -> ((Boolean)this.scaleing.getValue()).booleanValue()));
/*     */   
/*  44 */   private static Nametags INSTANCE = new Nametags();
/*     */   
/*     */   public Nametags() {
/*  47 */     super("Nametags", "Better Nametags", Module.Category.RENDER, false, false, false);
/*  48 */     setInstance();
/*     */   }
/*     */   
/*     */   private void setInstance() {
/*  52 */     INSTANCE = this;
/*     */   }
/*     */   
/*     */   public static Nametags getInstance() {
/*  56 */     if (INSTANCE == null) {
/*  57 */       INSTANCE = new Nametags();
/*     */     }
/*  59 */     return INSTANCE;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onRender3D(Render3DEvent event) {
/*  64 */     for (EntityPlayer player : mc.field_71441_e.field_73010_i) {
/*  65 */       if (player != null && !player.equals(mc.field_71439_g) && player.func_70089_S() && (!player.func_82150_aj() || ((Boolean)this.invisibles.getValue()).booleanValue())) {
/*  66 */         double x = interpolate(player.field_70142_S, player.field_70165_t, event.getPartialTicks()) - (mc.func_175598_ae()).field_78725_b;
/*  67 */         double y = interpolate(player.field_70137_T, player.field_70163_u, event.getPartialTicks()) - (mc.func_175598_ae()).field_78726_c;
/*  68 */         double z = interpolate(player.field_70136_U, player.field_70161_v, event.getPartialTicks()) - (mc.func_175598_ae()).field_78723_d;
/*  69 */         renderNameTag(player, x, y, z, event.getPartialTicks());
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void renderNameTag(EntityPlayer player, double x, double y, double z, float delta) {
/*  75 */     double tempY = y;
/*  76 */     tempY += player.func_70093_af() ? 0.5D : 0.7D;
/*  77 */     Entity camera = mc.func_175606_aa();
/*  78 */     assert camera != null;
/*  79 */     double originalPositionX = camera.field_70165_t;
/*  80 */     double originalPositionY = camera.field_70163_u;
/*  81 */     double originalPositionZ = camera.field_70161_v;
/*  82 */     camera.field_70165_t = interpolate(camera.field_70169_q, camera.field_70165_t, delta);
/*  83 */     camera.field_70163_u = interpolate(camera.field_70167_r, camera.field_70163_u, delta);
/*  84 */     camera.field_70161_v = interpolate(camera.field_70166_s, camera.field_70161_v, delta);
/*     */     
/*  86 */     String displayTag = getDisplayTag(player);
/*  87 */     double distance = camera.func_70011_f(x + (mc.func_175598_ae()).field_78730_l, y + (mc.func_175598_ae()).field_78731_m, z + (mc.func_175598_ae()).field_78728_n);
/*  88 */     int width = this.renderer.getStringWidth(displayTag) / 2;
/*  89 */     double scale = (0.0018D + ((Float)this.scaling.getValue()).floatValue() * distance * ((Float)this.factor.getValue()).floatValue()) / 1000.0D;
/*     */     
/*  91 */     if (distance <= 8.0D && ((Boolean)this.smartScale.getValue()).booleanValue()) {
/*  92 */       scale = 0.0245D;
/*     */     }
/*     */     
/*  95 */     if (!((Boolean)this.scaleing.getValue()).booleanValue()) {
/*  96 */       scale = ((Float)this.scaling.getValue()).floatValue() / 100.0D;
/*     */     }
/*     */     
/*  99 */     GlStateManager.func_179094_E();
/* 100 */     RenderHelper.func_74519_b();
/* 101 */     GlStateManager.func_179088_q();
/* 102 */     GlStateManager.func_179136_a(1.0F, -1500000.0F);
/* 103 */     GlStateManager.func_179140_f();
/* 104 */     GlStateManager.func_179109_b((float)x, (float)tempY + 1.4F, (float)z);
/* 105 */     GlStateManager.func_179114_b(-(mc.func_175598_ae()).field_78735_i, 0.0F, 1.0F, 0.0F);
/* 106 */     GlStateManager.func_179114_b((mc.func_175598_ae()).field_78732_j, (mc.field_71474_y.field_74320_O == 2) ? -1.0F : 1.0F, 0.0F, 0.0F);
/* 107 */     GlStateManager.func_179139_a(-scale, -scale, scale);
/* 108 */     GlStateManager.func_179097_i();
/* 109 */     GlStateManager.func_179147_l();
/*     */     
/* 111 */     GlStateManager.func_179147_l();
/* 112 */     if (((Boolean)this.rect.getValue()).booleanValue()) {
/* 113 */       RenderUtil.drawRect((-width - 2), -(this.renderer.getFontHeight() + 1), width + 2.0F, 1.5F, 1426063360);
/*     */     }
/* 115 */     GlStateManager.func_179084_k();
/*     */     
/* 117 */     ItemStack renderMainHand = player.func_184614_ca().func_77946_l();
/* 118 */     if (renderMainHand.func_77962_s() && (renderMainHand.func_77973_b() instanceof net.minecraft.item.ItemTool || renderMainHand.func_77973_b() instanceof net.minecraft.item.ItemArmor)) {
/* 119 */       renderMainHand.field_77994_a = 1;
/*     */     }
/*     */     
/* 122 */     if (((Boolean)this.heldStackName.getValue()).booleanValue() && !renderMainHand.field_190928_g && renderMainHand.func_77973_b() != Items.field_190931_a) {
/* 123 */       String stackName = renderMainHand.func_82833_r();
/* 124 */       int stackNameWidth = this.renderer.getStringWidth(stackName) / 2;
/* 125 */       GL11.glPushMatrix();
/* 126 */       GL11.glScalef(0.75F, 0.75F, 0.0F);
/* 127 */       this.renderer.drawStringWithShadow(stackName, -stackNameWidth, -(getBiggestArmorTag(player) + 20.0F), -1);
/* 128 */       GL11.glScalef(1.5F, 1.5F, 1.0F);
/* 129 */       GL11.glPopMatrix();
/*     */     } 
/*     */     
/* 132 */     if (((Boolean)this.armor.getValue()).booleanValue()) {
/* 133 */       GlStateManager.func_179094_E();
/* 134 */       int xOffset = -8;
/* 135 */       for (ItemStack stack : player.field_71071_by.field_70460_b) {
/* 136 */         if (stack != null) {
/* 137 */           xOffset -= 8;
/*     */         }
/*     */       } 
/*     */       
/* 141 */       xOffset -= 8;
/* 142 */       ItemStack renderOffhand = player.func_184592_cb().func_77946_l();
/* 143 */       if (renderOffhand.func_77962_s() && (renderOffhand.func_77973_b() instanceof net.minecraft.item.ItemTool || renderOffhand.func_77973_b() instanceof net.minecraft.item.ItemArmor)) {
/* 144 */         renderOffhand.field_77994_a = 1;
/*     */       }
/*     */       
/* 147 */       renderItemStack(renderOffhand, xOffset, -26);
/* 148 */       xOffset += 16;
/*     */       
/* 150 */       for (ItemStack stack : player.field_71071_by.field_70460_b) {
/* 151 */         if (stack != null) {
/* 152 */           ItemStack armourStack = stack.func_77946_l();
/* 153 */           if (armourStack.func_77962_s() && (armourStack.func_77973_b() instanceof net.minecraft.item.ItemTool || armourStack.func_77973_b() instanceof net.minecraft.item.ItemArmor)) {
/* 154 */             armourStack.field_77994_a = 1;
/*     */           }
/*     */           
/* 157 */           renderItemStack(armourStack, xOffset, -26);
/* 158 */           xOffset += 16;
/*     */         } 
/*     */       } 
/*     */       
/* 162 */       renderItemStack(renderMainHand, xOffset, -26);
/*     */       
/* 164 */       GlStateManager.func_179121_F();
/*     */     } 
/*     */     
/* 167 */     this.renderer.drawStringWithShadow(displayTag, -width, -(this.renderer.getFontHeight() - 1), getDisplayColour(player));
/*     */     
/* 169 */     camera.field_70165_t = originalPositionX;
/* 170 */     camera.field_70163_u = originalPositionY;
/* 171 */     camera.field_70161_v = originalPositionZ;
/* 172 */     GlStateManager.func_179126_j();
/* 173 */     GlStateManager.func_179084_k();
/* 174 */     GlStateManager.func_179113_r();
/* 175 */     GlStateManager.func_179136_a(1.0F, 1500000.0F);
/* 176 */     GlStateManager.func_179121_F();
/*     */   }
/*     */   
/*     */   private void renderItemStack(ItemStack stack, int x, int y) {
/* 180 */     GlStateManager.func_179094_E();
/* 181 */     GlStateManager.func_179132_a(true);
/* 182 */     GlStateManager.func_179086_m(256);
/*     */     
/* 184 */     RenderHelper.func_74519_b();
/* 185 */     (mc.func_175599_af()).field_77023_b = -150.0F;
/* 186 */     GlStateManager.func_179118_c();
/* 187 */     GlStateManager.func_179126_j();
/* 188 */     GlStateManager.func_179129_p();
/*     */     
/* 190 */     mc.func_175599_af().func_180450_b(stack, x, y);
/* 191 */     mc.func_175599_af().func_175030_a(mc.field_71466_p, stack, x, y);
/*     */     
/* 193 */     (mc.func_175599_af()).field_77023_b = 0.0F;
/* 194 */     RenderHelper.func_74518_a();
/*     */     
/* 196 */     GlStateManager.func_179089_o();
/* 197 */     GlStateManager.func_179141_d();
/*     */     
/* 199 */     GlStateManager.func_179152_a(0.5F, 0.5F, 0.5F);
/* 200 */     GlStateManager.func_179097_i();
/* 201 */     renderEnchantmentText(stack, x, y);
/* 202 */     GlStateManager.func_179126_j();
/* 203 */     GlStateManager.func_179152_a(2.0F, 2.0F, 2.0F);
/* 204 */     GlStateManager.func_179121_F();
/*     */   }
/*     */   
/*     */   private void renderEnchantmentText(ItemStack stack, int x, int y) {
/* 208 */     int enchantmentY = y - 8;
/*     */     
/* 210 */     if (stack.func_77973_b() == Items.field_151153_ao && stack.func_77962_s()) {
/* 211 */       this.renderer.drawStringWithShadow("god", (x * 2), enchantmentY, -3977919);
/* 212 */       enchantmentY -= 8;
/*     */     } 
/*     */     
/* 215 */     NBTTagList enchants = stack.func_77986_q();
/* 216 */     for (int index = 0; index < enchants.func_74745_c(); index++) {
/* 217 */       short id = enchants.func_150305_b(index).func_74765_d("id");
/* 218 */       short level = enchants.func_150305_b(index).func_74765_d("lvl");
/* 219 */       Enchantment enc = Enchantment.func_185262_c(id);
/* 220 */       if (enc != null) {
/*     */ 
/*     */ 
/*     */         
/* 224 */         String encName = enc.func_190936_d() ? (TextFormatting.RED + enc.func_77316_c(level).substring(11).substring(0, 1).toLowerCase()) : enc.func_77316_c(level).substring(0, 1).toLowerCase();
/* 225 */         encName = encName + level;
/* 226 */         this.renderer.drawStringWithShadow(encName, (x * 2), enchantmentY, -1);
/* 227 */         enchantmentY -= 8;
/*     */       } 
/*     */     } 
/* 230 */     if (DamageUtil.hasDurability(stack)) {
/* 231 */       String color; int percent = DamageUtil.getRoundedDamage(stack);
/*     */       
/* 233 */       if (percent >= 60) {
/* 234 */         color = "§a";
/* 235 */       } else if (percent >= 25) {
/* 236 */         color = "§e";
/*     */       } else {
/* 238 */         color = "§c";
/*     */       } 
/* 240 */       this.renderer.drawStringWithShadow(color + percent + "%", (x * 2), enchantmentY, -1);
/*     */     } 
/*     */   }
/*     */   
/*     */   private float getBiggestArmorTag(EntityPlayer player) {
/* 245 */     float enchantmentY = 0.0F;
/* 246 */     boolean arm = false;
/* 247 */     for (ItemStack stack : player.field_71071_by.field_70460_b) {
/* 248 */       float encY = 0.0F;
/* 249 */       if (stack != null) {
/* 250 */         NBTTagList enchants = stack.func_77986_q();
/* 251 */         for (int index = 0; index < enchants.func_74745_c(); index++) {
/* 252 */           short id = enchants.func_150305_b(index).func_74765_d("id");
/* 253 */           Enchantment enc = Enchantment.func_185262_c(id);
/* 254 */           if (enc != null) {
/* 255 */             encY += 8.0F;
/* 256 */             arm = true;
/*     */           } 
/*     */         } 
/*     */       } 
/* 260 */       if (encY > enchantmentY) enchantmentY = encY; 
/*     */     } 
/* 262 */     ItemStack renderMainHand = player.func_184614_ca().func_77946_l();
/* 263 */     if (renderMainHand.func_77962_s()) {
/* 264 */       float encY = 0.0F;
/* 265 */       NBTTagList enchants = renderMainHand.func_77986_q();
/* 266 */       for (int index = 0; index < enchants.func_74745_c(); index++) {
/* 267 */         short id = enchants.func_150305_b(index).func_74765_d("id");
/* 268 */         Enchantment enc = Enchantment.func_185262_c(id);
/* 269 */         if (enc != null) {
/* 270 */           encY += 8.0F;
/* 271 */           arm = true;
/*     */         } 
/*     */       } 
/* 274 */       if (encY > enchantmentY) enchantmentY = encY; 
/*     */     } 
/* 276 */     ItemStack renderOffHand = player.func_184592_cb().func_77946_l();
/* 277 */     if (renderOffHand.func_77962_s()) {
/* 278 */       float encY = 0.0F;
/* 279 */       NBTTagList enchants = renderOffHand.func_77986_q();
/* 280 */       for (int index = 0; index < enchants.func_74745_c(); index++) {
/* 281 */         short id = enchants.func_150305_b(index).func_74765_d("id");
/* 282 */         Enchantment enc = Enchantment.func_185262_c(id);
/* 283 */         if (enc != null) {
/* 284 */           encY += 8.0F;
/* 285 */           arm = true;
/*     */         } 
/*     */       } 
/* 288 */       if (encY > enchantmentY) enchantmentY = encY; 
/*     */     } 
/* 290 */     return (arm ? false : 20) + enchantmentY;
/*     */   }
/*     */   
/*     */   private String getDisplayTag(EntityPlayer player) {
/* 294 */     String color, name = player.func_145748_c_().func_150254_d();
/* 295 */     if (name.contains(mc.func_110432_I().func_111285_a())) {
/* 296 */       name = "You";
/*     */     }
/*     */     
/* 299 */     if (!((Boolean)this.health.getValue()).booleanValue()) {
/* 300 */       return name;
/*     */     }
/*     */     
/* 303 */     float health = EntityUtil.getHealth((Entity)player);
/*     */ 
/*     */     
/* 306 */     if (health > 18.0F) {
/* 307 */       color = "§a";
/* 308 */     } else if (health > 16.0F) {
/* 309 */       color = "§2";
/* 310 */     } else if (health > 12.0F) {
/* 311 */       color = "§e";
/* 312 */     } else if (health > 8.0F) {
/* 313 */       color = "§6";
/* 314 */     } else if (health > 5.0F) {
/* 315 */       color = "§c";
/*     */     } else {
/* 317 */       color = "§4";
/*     */     } 
/*     */     
/* 320 */     String pingStr = "";
/* 321 */     if (((Boolean)this.ping.getValue()).booleanValue()) {
/*     */       try {
/* 323 */         int responseTime = ((NetHandlerPlayClient)Objects.<NetHandlerPlayClient>requireNonNull(mc.func_147114_u())).func_175102_a(player.func_110124_au()).func_178853_c();
/* 324 */         pingStr = pingStr + responseTime + "ms ";
/* 325 */       } catch (Exception exception) {}
/*     */     }
/*     */     
/* 328 */     String popStr = " ";
/* 329 */     if (((Boolean)this.totemPops.getValue()).booleanValue()) {
/* 330 */       popStr = popStr + Phobos.totemPopManager.getTotemPopString(player);
/*     */     }
/*     */     
/* 333 */     String idString = "";
/* 334 */     if (((Boolean)this.entityID.getValue()).booleanValue()) {
/* 335 */       idString = idString + "ID: " + player.func_145782_y() + " ";
/*     */     }
/*     */     
/* 338 */     String gameModeStr = "";
/* 339 */     if (((Boolean)this.gamemode.getValue()).booleanValue()) {
/* 340 */       if (player.func_184812_l_()) {
/* 341 */         gameModeStr = gameModeStr + "[C] ";
/* 342 */       } else if (player.func_175149_v() || player.func_82150_aj()) {
/* 343 */         gameModeStr = gameModeStr + "[I] ";
/*     */       } else {
/* 345 */         gameModeStr = gameModeStr + "[S] ";
/*     */       } 
/*     */     }
/*     */     
/* 349 */     if (Math.floor(health) == health) {
/* 350 */       name = name + color + " " + ((health > 0.0F) ? (String)Integer.valueOf((int)Math.floor(health)) : "dead");
/*     */     } else {
/* 352 */       name = name + color + " " + ((health > 0.0F) ? (String)Integer.valueOf((int)health) : "dead");
/*     */     } 
/* 354 */     return pingStr + idString + gameModeStr + name + popStr;
/*     */   }
/*     */   
/*     */   private int getDisplayColour(EntityPlayer player) {
/* 358 */     int colour = -5592406;
/* 359 */     if (((Boolean)this.whiter.getValue()).booleanValue()) {
/* 360 */       colour = -1;
/*     */     }
/* 362 */     if (Phobos.friendManager.isFriend(player))
/* 363 */       return -11157267; 
/* 364 */     if (player.func_82150_aj()) {
/* 365 */       colour = -1113785;
/* 366 */     } else if (player.func_70093_af() && ((Boolean)this.sneak.getValue()).booleanValue()) {
/* 367 */       colour = -6481515;
/*     */     } 
/* 369 */     return colour;
/*     */   }
/*     */   
/*     */   private double interpolate(double previous, double current, float delta) {
/* 373 */     return previous + (current - previous) * delta;
/*     */   }
/*     */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\me\earth\phobos\features\modules\render\Nametags.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */