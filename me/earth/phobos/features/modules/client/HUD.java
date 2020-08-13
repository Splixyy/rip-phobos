/*     */ package me.earth.phobos.features.modules.client;
/*     */ 
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.Date;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import me.earth.phobos.Phobos;
/*     */ import me.earth.phobos.event.events.Render2DEvent;
/*     */ import me.earth.phobos.features.modules.Module;
/*     */ import me.earth.phobos.features.modules.misc.ToolTips;
/*     */ import me.earth.phobos.features.setting.Setting;
/*     */ import me.earth.phobos.util.ColorUtil;
/*     */ import me.earth.phobos.util.EntityUtil;
/*     */ import me.earth.phobos.util.MathUtil;
/*     */ import me.earth.phobos.util.RenderUtil;
/*     */ import me.earth.phobos.util.Timer;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.potion.PotionEffect;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import net.minecraftforge.event.entity.player.AttackEntityEvent;
/*     */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class HUD
/*     */   extends Module
/*     */ {
/*  33 */   private final Setting<Boolean> renderingUp = register(new Setting("RenderingUp", Boolean.valueOf(false), "Orientation of the HUD-Elements."));
/*  34 */   public Setting<Boolean> potionIcons = register(new Setting("PotionIcons", Boolean.valueOf(true), "Draws Potion Icons."));
/*  35 */   public Setting<Boolean> shadow = register(new Setting("Shadow", Boolean.valueOf(false), "Draws the text with a shadow."));
/*  36 */   private final Setting<WaterMark> watermark = register(new Setting("Logo", WaterMark.NONE, "WaterMark"));
/*  37 */   private final Setting<Boolean> modeVer = register(new Setting("Version", Boolean.valueOf(false), v -> (this.watermark.getValue() != WaterMark.NONE)));
/*  38 */   private final Setting<Boolean> arrayList = register(new Setting("ActiveModules", Boolean.valueOf(false), "Lists the active modules."));
/*  39 */   private final Setting<Boolean> serverBrand = register(new Setting("ServerBrand", Boolean.valueOf(false), "Brand of the server you are on."));
/*  40 */   private final Setting<Boolean> ping = register(new Setting("Ping", Boolean.valueOf(false), "Your response time to the server."));
/*  41 */   private final Setting<Boolean> tps = register(new Setting("TPS", Boolean.valueOf(false), "Ticks per second of the server."));
/*  42 */   private final Setting<Boolean> fps = register(new Setting("FPS", Boolean.valueOf(false), "Your frames per second."));
/*  43 */   private final Setting<Boolean> coords = register(new Setting("Coords", Boolean.valueOf(false), "Your current coordinates"));
/*  44 */   private final Setting<Boolean> direction = register(new Setting("Direction", Boolean.valueOf(false), "The Direction you are facing."));
/*  45 */   private final Setting<Boolean> speed = register(new Setting("Speed", Boolean.valueOf(false), "Your Speed"));
/*  46 */   private final Setting<Boolean> potions = register(new Setting("Potions", Boolean.valueOf(false), "Your Speed"));
/*  47 */   public Setting<Boolean> textRadar = register(new Setting("TextRadar", Boolean.valueOf(false), "A TextRadar"));
/*  48 */   private final Setting<Boolean> armor = register(new Setting("Armor", Boolean.valueOf(false), "ArmorHUD"));
/*  49 */   private final Setting<Boolean> percent = register(new Setting("Percent", Boolean.valueOf(true), v -> ((Boolean)this.armor.getValue()).booleanValue()));
/*  50 */   private final Setting<Boolean> totems = register(new Setting("Totems", Boolean.valueOf(false), "TotemHUD"));
/*  51 */   private final Setting<Greeter> greeter = register(new Setting("Greeter", Greeter.NONE, "Greets you."));
/*  52 */   private final Setting<String> spoofGreeter = register(new Setting("GreeterName", "3arthqu4ke", v -> (this.greeter.getValue() == Greeter.CUSTOM)));
/*  53 */   public Setting<Boolean> time = register(new Setting("Time", Boolean.valueOf(false), "The time"));
/*  54 */   private final Setting<LagNotify> lag = register(new Setting("Lag", LagNotify.GRAY, "Lag Notifier"));
/*  55 */   private final Setting<Boolean> hitMarkers = register(new Setting("HitMarkers", Boolean.valueOf(true)));
/*  56 */   public Setting<Integer> hudRed = register(new Setting("Red", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255)));
/*  57 */   public Setting<Integer> hudGreen = register(new Setting("Green", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(255)));
/*  58 */   public Setting<Integer> hudBlue = register(new Setting("Blue", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(255)));
/*  59 */   private final Setting<Boolean> grayNess = register(new Setting("FutureColour", Boolean.valueOf(true)));
/*  60 */   private static HUD INSTANCE = new HUD();
/*     */   
/*  62 */   private Map<String, Integer> players = new HashMap<>();
/*     */   
/*  64 */   private static final ResourceLocation box = new ResourceLocation("textures/gui/container/shulker_box.png");
/*  65 */   private static final ItemStack totem = new ItemStack(Items.field_190929_cY);
/*     */   private int color;
/*     */   private boolean shouldIncrement;
/*     */   private int hitMarkerTimer;
/*  69 */   private final Timer timer = new Timer();
/*     */   
/*     */   public HUD() {
/*  72 */     super("HUD", "HUD Elements rendered on your screen", Module.Category.CLIENT, true, false, false);
/*  73 */     setInstance();
/*     */   }
/*     */   
/*     */   private void setInstance() {
/*  77 */     INSTANCE = this;
/*     */   }
/*     */   
/*     */   public static HUD getInstance() {
/*  81 */     if (INSTANCE == null) {
/*  82 */       INSTANCE = new HUD();
/*     */     }
/*  84 */     return INSTANCE;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onUpdate() {
/*  89 */     if (this.timer.passedMs(((Integer)(Managers.getInstance()).textRadarUpdates.getValue()).intValue())) {
/*  90 */       this.players = getTextRadarPlayers();
/*  91 */       this.timer.reset();
/*     */     } 
/*  93 */     if (this.shouldIncrement) {
/*  94 */       this.hitMarkerTimer++;
/*     */     }
/*  96 */     if (this.hitMarkerTimer == 10) {
/*  97 */       this.hitMarkerTimer = 0;
/*  98 */       this.shouldIncrement = false;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onRender2D(Render2DEvent event) {
/* 104 */     if (fullNullCheck()) {
/*     */       return;
/*     */     }
/*     */     
/* 108 */     int width = this.renderer.scaledWidth;
/* 109 */     int height = this.renderer.scaledHeight;
/* 110 */     this.color = ColorUtil.toRGBA(((Integer)this.hudRed.getValue()).intValue(), ((Integer)this.hudGreen.getValue()).intValue(), ((Integer)this.hudBlue.getValue()).intValue());
/* 111 */     String grayString = ((Boolean)this.grayNess.getValue()).booleanValue() ? "§7" : "";
/*     */     
/* 113 */     switch ((WaterMark)this.watermark.getValue()) {
/*     */       case TIME:
/* 115 */         this.renderer.drawString("Phobos" + (((Boolean)this.modeVer.getValue()).booleanValue() ? " v1.3.3" : ""), 2.0F, 2.0F, this.color, true);
/*     */         break;
/*     */       case LONG:
/* 118 */         this.renderer.drawString("3arthh4ck" + (((Boolean)this.modeVer.getValue()).booleanValue() ? " v1.3.3" : ""), 2.0F, 2.0F, this.color, true);
/*     */         break;
/*     */     } 
/*     */     
/* 122 */     if (((Boolean)this.textRadar.getValue()).booleanValue()) {
/* 123 */       drawTextRadar((ToolTips.getInstance().isOff() || !((Boolean)(ToolTips.getInstance()).shulkerSpy.getValue()).booleanValue() || !((Boolean)(ToolTips.getInstance()).render.getValue()).booleanValue()) ? 0 : ToolTips.getInstance().getTextRadarY());
/*     */     }
/* 125 */     int j = ((Boolean)this.renderingUp.getValue()).booleanValue() ? 0 : ((mc.field_71462_r instanceof net.minecraft.client.gui.GuiChat) ? 14 : 0);
/* 126 */     if (((Boolean)this.arrayList.getValue()).booleanValue()) {
/* 127 */       if (((Boolean)this.renderingUp.getValue()).booleanValue()) {
/* 128 */         for (int k = 0; k < Phobos.moduleManager.sortedModules.size(); k++) {
/* 129 */           Module module = Phobos.moduleManager.sortedModules.get(k);
/* 130 */           String str = module.getDisplayName() + "§7" + ((module.getDisplayInfo() != null) ? (" [§f" + module.getDisplayInfo() + "§7" + "]") : "");
/* 131 */           this.renderer.drawString(str, (width - 2 - this.renderer.getStringWidth(str)), (2 + j * 10), this.color, true);
/* 132 */           j++;
/*     */         } 
/*     */       } else {
/* 135 */         for (int k = 0; k < Phobos.moduleManager.sortedModules.size(); k++) {
/* 136 */           Module module = Phobos.moduleManager.sortedModules.get(k);
/* 137 */           String str = module.getDisplayName() + "§7" + ((module.getDisplayInfo() != null) ? (" [§f" + module.getDisplayInfo() + "§7" + "]") : "");
/* 138 */           j += 10; this.renderer.drawString(str, (width - 2 - this.renderer.getStringWidth(str)), (height - j), this.color, true);
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/* 143 */     int i = (mc.field_71462_r instanceof net.minecraft.client.gui.GuiChat) ? 14 : 0;
/* 144 */     if (((Boolean)this.renderingUp.getValue()).booleanValue()) {
/* 145 */       if (((Boolean)this.serverBrand.getValue()).booleanValue()) {
/* 146 */         String str = grayString + "Server brand " + "§f" + Phobos.serverManager.getServerBrand();
/* 147 */         i += 10; this.renderer.drawString(str, (width - this.renderer.getStringWidth(str) + 2), (height - 2 - i), this.color, true);
/*     */       } 
/* 149 */       if (((Boolean)this.potions.getValue()).booleanValue()) {
/* 150 */         for (PotionEffect effect : Phobos.potionManager.getOwnPotions()) {
/* 151 */           String str = Phobos.potionManager.getColoredPotionString(effect);
/* 152 */           i += 10; this.renderer.drawString(str, (width - this.renderer.getStringWidth(str) + 2), (height - 2 - i), this.color, true);
/*     */         } 
/*     */       }
/* 155 */       if (((Boolean)this.speed.getValue()).booleanValue()) {
/* 156 */         String str = grayString + "Speed " + "§f" + Phobos.speedManager.getSpeedKpH() + " km/h";
/* 157 */         i += 10; this.renderer.drawString(str, (width - this.renderer.getStringWidth(str) + 2), (height - 2 - i), this.color, true);
/*     */       } 
/* 159 */       if (((Boolean)this.time.getValue()).booleanValue()) {
/* 160 */         String str = grayString + "Time " + "§f" + (new SimpleDateFormat("h:mm a")).format(new Date());
/* 161 */         i += 10; this.renderer.drawString(str, (width - this.renderer.getStringWidth(str) + 2), (height - 2 - i), this.color, true);
/*     */       } 
/* 163 */       if (((Boolean)this.tps.getValue()).booleanValue()) {
/* 164 */         String str = grayString + "TPS " + "§f" + Phobos.serverManager.getTPS();
/* 165 */         i += 10; this.renderer.drawString(str, (width - this.renderer.getStringWidth(str) + 2), (height - 2 - i), this.color, true);
/*     */       } 
/* 167 */       String fpsText = grayString + "FPS " + "§f" + Minecraft.field_71470_ab;
/* 168 */       String str1 = grayString + "Ping " + "§f" + Phobos.serverManager.getPing();
/* 169 */       if (this.renderer.getStringWidth(str1) > this.renderer.getStringWidth(fpsText)) {
/* 170 */         if (((Boolean)this.ping.getValue()).booleanValue()) {
/* 171 */           i += 10; this.renderer.drawString(str1, (width - this.renderer.getStringWidth(str1) + 2), (height - 2 - i), this.color, true);
/*     */         } 
/* 173 */         if (((Boolean)this.fps.getValue()).booleanValue()) {
/* 174 */           i += 10; this.renderer.drawString(fpsText, (width - this.renderer.getStringWidth(fpsText) + 2), (height - 2 - i), this.color, true);
/*     */         } 
/*     */       } else {
/* 177 */         if (((Boolean)this.fps.getValue()).booleanValue()) {
/* 178 */           i += 10; this.renderer.drawString(fpsText, (width - this.renderer.getStringWidth(fpsText) + 2), (height - 2 - i), this.color, true);
/*     */         } 
/* 180 */         if (((Boolean)this.ping.getValue()).booleanValue()) {
/* 181 */           i += 10; this.renderer.drawString(str1, (width - this.renderer.getStringWidth(str1) + 2), (height - 2 - i), this.color, true);
/*     */         } 
/*     */       } 
/*     */     } else {
/* 185 */       if (((Boolean)this.serverBrand.getValue()).booleanValue()) {
/* 186 */         String str = grayString + "Server brand " + "§r" + Phobos.serverManager.getServerBrand();
/* 187 */         this.renderer.drawString(str, (width - this.renderer.getStringWidth(str) + 2), (2 + i++ * 10), this.color, true);
/*     */       } 
/* 189 */       if (((Boolean)this.potions.getValue()).booleanValue()) {
/* 190 */         for (PotionEffect effect : Phobos.potionManager.getOwnPotions()) {
/* 191 */           String str = Phobos.potionManager.getColoredPotionString(effect);
/* 192 */           this.renderer.drawString(str, (width - this.renderer.getStringWidth(str) + 2), (2 + i++ * 10), this.color, true);
/*     */         } 
/*     */       }
/* 195 */       if (((Boolean)this.speed.getValue()).booleanValue()) {
/* 196 */         String str = grayString + "Speed " + "§r" + Phobos.speedManager.getSpeedKpH() + " km/h";
/* 197 */         this.renderer.drawString(str, (width - this.renderer.getStringWidth(str) + 2), (2 + i++ * 10), this.color, true);
/*     */       } 
/* 199 */       if (((Boolean)this.time.getValue()).booleanValue()) {
/* 200 */         String str = grayString + "Time " + "§r" + (new SimpleDateFormat("h:mm a")).format(new Date());
/* 201 */         this.renderer.drawString(str, (width - this.renderer.getStringWidth(str) + 2), (2 + i++ * 10), this.color, true);
/*     */       } 
/* 203 */       if (((Boolean)this.tps.getValue()).booleanValue()) {
/* 204 */         String str = grayString + "TPS " + "§r" + Phobos.serverManager.getTPS();
/* 205 */         this.renderer.drawString(str, (width - this.renderer.getStringWidth(str) + 2), (2 + i++ * 10), this.color, true);
/*     */       } 
/* 207 */       String fpsText = grayString + "FPS " + "§r" + Minecraft.field_71470_ab;
/* 208 */       String str1 = grayString + "Ping " + "§r" + Phobos.serverManager.getPing();
/* 209 */       if (this.renderer.getStringWidth(str1) > this.renderer.getStringWidth(fpsText)) {
/* 210 */         if (((Boolean)this.ping.getValue()).booleanValue()) {
/* 211 */           this.renderer.drawString(str1, (width - this.renderer.getStringWidth(str1) + 2), (2 + i++ * 10), this.color, true);
/*     */         }
/* 213 */         if (((Boolean)this.fps.getValue()).booleanValue()) {
/* 214 */           this.renderer.drawString(fpsText, (width - this.renderer.getStringWidth(fpsText) + 2), (2 + i++ * 10), this.color, true);
/*     */         }
/*     */       } else {
/* 217 */         if (((Boolean)this.fps.getValue()).booleanValue()) {
/* 218 */           this.renderer.drawString(fpsText, (width - this.renderer.getStringWidth(fpsText) + 2), (2 + i++ * 10), this.color, true);
/*     */         }
/* 220 */         if (((Boolean)this.ping.getValue()).booleanValue()) {
/* 221 */           this.renderer.drawString(str1, (width - this.renderer.getStringWidth(str1) + 2), (2 + i++ * 10), this.color, true);
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 226 */     boolean inHell = mc.field_71441_e.func_180494_b(mc.field_71439_g.func_180425_c()).func_185359_l().equals("Hell");
/*     */     
/* 228 */     int posX = (int)mc.field_71439_g.field_70165_t;
/* 229 */     int posY = (int)mc.field_71439_g.field_70163_u;
/* 230 */     int posZ = (int)mc.field_71439_g.field_70161_v;
/*     */     
/* 232 */     float nether = !inHell ? 0.125F : 8.0F;
/* 233 */     int hposX = (int)(mc.field_71439_g.field_70165_t * nether);
/* 234 */     int hposZ = (int)(mc.field_71439_g.field_70161_v * nether);
/*     */     
/* 236 */     if (((Boolean)this.renderingUp.getValue()).booleanValue()) {
/* 237 */       Phobos.notificationManager.handleNotifications(height - i + 16);
/*     */     } else {
/* 239 */       Phobos.notificationManager.handleNotifications(height - j + 16);
/*     */     } 
/*     */     
/* 242 */     i = (mc.field_71462_r instanceof net.minecraft.client.gui.GuiChat) ? 14 : 0;
/* 243 */     String coordinates = "§rXYZ §f" + posX + ", " + posY + ", " + posZ + " " + "§r" + "[" + "§f" + hposX + ", " + hposZ + "§r" + "]";
/* 244 */     String text = (((Boolean)this.direction.getValue()).booleanValue() ? (Phobos.rotationManager.getDirection4D(false) + " ") : "") + (((Boolean)this.coords.getValue()).booleanValue() ? coordinates : "") + "";
/*     */     
/* 246 */     i += 10; this.renderer.drawString(text, 2.0F, (height - i), this.color, true);
/*     */     
/* 248 */     if (((Boolean)this.armor.getValue()).booleanValue()) {
/* 249 */       renderArmorHUD(((Boolean)this.percent.getValue()).booleanValue());
/*     */     }
/*     */     
/* 252 */     if (((Boolean)this.totems.getValue()).booleanValue()) {
/* 253 */       renderTotemHUD();
/*     */     }
/*     */     
/* 256 */     if (this.greeter.getValue() != Greeter.NONE) {
/* 257 */       renderGreeter();
/*     */     }
/*     */     
/* 260 */     if (this.lag.getValue() != LagNotify.NONE) {
/* 261 */       renderLag();
/*     */     }
/*     */   }
/*     */   
/*     */   public Map<String, Integer> getTextRadarPlayers() {
/* 266 */     return EntityUtil.getTextRadarPlayers();
/*     */   }
/*     */   
/*     */   public void renderGreeter() {
/* 270 */     int width = this.renderer.scaledWidth;
/* 271 */     String text = "";
/* 272 */     switch ((Greeter)this.greeter.getValue()) {
/*     */       case TIME:
/* 274 */         text = text + MathUtil.getTimeOfDay() + mc.field_71439_g.getDisplayNameString();
/*     */         break;
/*     */       case LONG:
/* 277 */         text = text + "Welcome to Phobos.eu " + mc.field_71439_g.getDisplayNameString() + " :^)";
/*     */         break;
/*     */       case CUSTOM:
/* 280 */         text = text + (String)this.spoofGreeter.getValue();
/*     */         break;
/*     */       default:
/* 283 */         text = text + "Welcome " + mc.field_71439_g.getDisplayNameString(); break;
/*     */     } 
/* 285 */     this.renderer.drawString(text, width / 2.0F - this.renderer.getStringWidth(text) / 2.0F + 2.0F, 2.0F, this.color, true);
/*     */   }
/*     */   
/*     */   public void renderLag() {
/* 289 */     int width = this.renderer.scaledWidth;
/* 290 */     if (Phobos.serverManager.isServerNotResponding()) {
/* 291 */       String text = ((this.lag.getValue() == LagNotify.GRAY) ? "§7" : "§c") + "Server not responding: " + MathUtil.round((float)Phobos.serverManager.serverRespondingTime() / 1000.0F, 1) + "s.";
/* 292 */       this.renderer.drawString(text, width / 2.0F - this.renderer.getStringWidth(text) / 2.0F + 2.0F, 20.0F, this.color, true);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void renderTotemHUD() {
/* 297 */     int width = this.renderer.scaledWidth;
/* 298 */     int height = this.renderer.scaledHeight;
/* 299 */     int totems = mc.field_71439_g.field_71071_by.field_70462_a.stream().filter(itemStack -> (itemStack.func_77973_b() == Items.field_190929_cY)).mapToInt(ItemStack::func_190916_E).sum();
/* 300 */     if (mc.field_71439_g.func_184592_cb().func_77973_b() == Items.field_190929_cY)
/* 301 */       totems += mc.field_71439_g.func_184592_cb().func_190916_E(); 
/* 302 */     if (totems > 0) {
/* 303 */       GlStateManager.func_179098_w();
/* 304 */       int i = width / 2;
/* 305 */       int iteration = 0;
/* 306 */       int y = height - 55 - ((mc.field_71439_g.func_70090_H() && mc.field_71442_b.func_78763_f()) ? 10 : 0);
/* 307 */       int x = i - 189 + 180 + 2;
/* 308 */       GlStateManager.func_179126_j();
/* 309 */       RenderUtil.itemRender.field_77023_b = 200.0F;
/* 310 */       RenderUtil.itemRender.func_180450_b(totem, x, y);
/* 311 */       RenderUtil.itemRender.func_180453_a(mc.field_71466_p, totem, x, y, "");
/* 312 */       RenderUtil.itemRender.field_77023_b = 0.0F;
/* 313 */       GlStateManager.func_179098_w();
/* 314 */       GlStateManager.func_179140_f();
/* 315 */       GlStateManager.func_179097_i();
/* 316 */       this.renderer.drawStringWithShadow(totems + "", (x + 19 - 2 - this.renderer.getStringWidth(totems + "")), (y + 9), 16777215);
/*     */       
/* 318 */       GlStateManager.func_179126_j();
/* 319 */       GlStateManager.func_179140_f();
/*     */     } 
/*     */   }
/*     */   
/*     */   public void renderArmorHUD(boolean percent) {
/* 324 */     int width = this.renderer.scaledWidth;
/* 325 */     int height = this.renderer.scaledHeight;
/* 326 */     GlStateManager.func_179098_w();
/* 327 */     int i = width / 2;
/* 328 */     int iteration = 0;
/* 329 */     int y = height - 55 - ((mc.field_71439_g.func_70090_H() && mc.field_71442_b.func_78763_f()) ? 10 : 0);
/* 330 */     for (ItemStack is : mc.field_71439_g.field_71071_by.field_70460_b) {
/* 331 */       iteration++;
/* 332 */       if (is.func_190926_b())
/* 333 */         continue;  int x = i - 90 + (9 - iteration) * 20 + 2;
/* 334 */       GlStateManager.func_179126_j();
/* 335 */       RenderUtil.itemRender.field_77023_b = 200.0F;
/* 336 */       RenderUtil.itemRender.func_180450_b(is, x, y);
/* 337 */       RenderUtil.itemRender.func_180453_a(mc.field_71466_p, is, x, y, "");
/* 338 */       RenderUtil.itemRender.field_77023_b = 0.0F;
/* 339 */       GlStateManager.func_179098_w();
/* 340 */       GlStateManager.func_179140_f();
/* 341 */       GlStateManager.func_179097_i();
/* 342 */       String s = (is.func_190916_E() > 1) ? (is.func_190916_E() + "") : "";
/* 343 */       this.renderer.drawStringWithShadow(s, (x + 19 - 2 - this.renderer.getStringWidth(s)), (y + 9), 16777215);
/*     */ 
/*     */       
/* 346 */       if (percent) {
/* 347 */         int dmg = 0;
/* 348 */         int itemDurability = is.func_77958_k() - is.func_77952_i();
/* 349 */         float green = (is.func_77958_k() - is.func_77952_i()) / is.func_77958_k();
/* 350 */         float red = 1.0F - green;
/* 351 */         if (percent) {
/* 352 */           dmg = 100 - (int)(red * 100.0F);
/*     */         } else {
/* 354 */           dmg = itemDurability;
/*     */         } 
/* 356 */         this.renderer.drawStringWithShadow(dmg + "", (x + 8 - this.renderer.getStringWidth(dmg + "") / 2), (y - 11), ColorUtil.toRGBA((int)(red * 255.0F), (int)(green * 255.0F), 0));
/*     */       } 
/*     */     } 
/* 359 */     GlStateManager.func_179126_j();
/* 360 */     GlStateManager.func_179140_f();
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onUpdateWalkingPlayer(AttackEntityEvent event) {
/* 365 */     this.shouldIncrement = true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void drawHitMarkers() {}
/*     */ 
/*     */   
/*     */   public void drawTextRadar(int yOffset) {
/* 373 */     if (!this.players.isEmpty()) {
/* 374 */       int y = this.renderer.getFontHeight() + 7 + yOffset;
/* 375 */       for (Map.Entry<String, Integer> player : this.players.entrySet()) {
/* 376 */         String text = (String)player.getKey() + " ";
/* 377 */         int textheight = this.renderer.getFontHeight() + 1;
/* 378 */         this.renderer.drawString(text, 2.0F, y, this.color, true);
/* 379 */         y += textheight;
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public enum Greeter {
/* 385 */     NONE,
/* 386 */     NAME,
/* 387 */     TIME,
/* 388 */     LONG,
/* 389 */     CUSTOM;
/*     */   }
/*     */   
/*     */   public enum LagNotify {
/* 393 */     NONE,
/* 394 */     RED,
/* 395 */     GRAY;
/*     */   }
/*     */   
/*     */   public enum WaterMark {
/* 399 */     NONE,
/* 400 */     PHOBOS,
/* 401 */     EARTH;
/*     */   }
/*     */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\me\earth\phobos\features\modules\client\HUD.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */