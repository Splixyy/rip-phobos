/*     */ package me.earth.phobos.manager;
/*     */ 
/*     */ import java.awt.Font;
/*     */ import me.earth.phobos.Phobos;
/*     */ import me.earth.phobos.features.Feature;
/*     */ import me.earth.phobos.features.gui.font.CustomFont;
/*     */ import me.earth.phobos.features.modules.client.FontMod;
/*     */ import me.earth.phobos.util.Timer;
/*     */ import net.minecraft.util.math.MathHelper;
/*     */ 
/*     */ public class TextManager
/*     */   extends Feature
/*     */ {
/*  14 */   private CustomFont customFont = new CustomFont(new Font("Verdana", 0, 17), true, false);
/*     */   
/*     */   public int scaledWidth;
/*     */   
/*     */   public int scaledHeight;
/*     */   public int scaleFactor;
/*  20 */   private final Timer idleTimer = new Timer();
/*     */   private boolean idling;
/*     */   
/*     */   public TextManager() {
/*  24 */     updateResolution();
/*     */   }
/*     */   
/*     */   public void init(boolean startup) {
/*  28 */     FontMod cFont = Phobos.moduleManager.<FontMod>getModuleByClass(FontMod.class);
/*     */     try {
/*  30 */       setFontRenderer(new Font((String)cFont.fontName.getValue(), ((Integer)cFont.fontStyle.getValue()).intValue(), ((Integer)cFont.fontSize.getValue()).intValue()), ((Boolean)cFont.antiAlias.getValue()).booleanValue(), ((Boolean)cFont.fractionalMetrics.getValue()).booleanValue());
/*  31 */     } catch (Exception exception) {}
/*     */   }
/*     */   
/*     */   public void drawStringWithShadow(String text, float x, float y, int color) {
/*  35 */     drawString(text, x, y, color, true);
/*     */   }
/*     */   
/*     */   public void drawString(String text, float x, float y, int color, boolean shadow) {
/*  39 */     if (Phobos.moduleManager.isModuleEnabled(FontMod.class)) {
/*  40 */       if (shadow) {
/*  41 */         this.customFont.drawStringWithShadow(text, x, y, color);
/*     */       } else {
/*  43 */         this.customFont.drawString(text, x, y, color);
/*     */       } 
/*     */       return;
/*     */     } 
/*  47 */     mc.field_71466_p.func_175065_a(text, x, y, color, shadow);
/*     */   }
/*     */   
/*     */   public int getStringWidth(String text) {
/*  51 */     if (Phobos.moduleManager.isModuleEnabled(FontMod.class)) {
/*  52 */       return this.customFont.getStringWidth(text);
/*     */     }
/*  54 */     return mc.field_71466_p.func_78256_a(text);
/*     */   }
/*     */   
/*     */   public int getFontHeight() {
/*  58 */     if (Phobos.moduleManager.isModuleEnabled(FontMod.class)) {
/*  59 */       String text = "A";
/*  60 */       return this.customFont.getStringHeight(text);
/*     */     } 
/*  62 */     return mc.field_71466_p.field_78288_b;
/*     */   }
/*     */   
/*     */   public void setFontRenderer(Font font, boolean antiAlias, boolean fractionalMetrics) {
/*  66 */     this.customFont = new CustomFont(font, antiAlias, fractionalMetrics);
/*     */   }
/*     */   
/*     */   public Font getCurrentFont() {
/*  70 */     return this.customFont.getFont();
/*     */   }
/*     */   
/*     */   public void updateResolution() {
/*  74 */     this.scaledWidth = mc.field_71443_c;
/*  75 */     this.scaledHeight = mc.field_71440_d;
/*  76 */     this.scaleFactor = 1;
/*  77 */     boolean flag = mc.func_152349_b();
/*  78 */     int i = mc.field_71474_y.field_74335_Z;
/*     */     
/*  80 */     if (i == 0) {
/*  81 */       i = 1000;
/*     */     }
/*     */     
/*  84 */     while (this.scaleFactor < i && this.scaledWidth / (this.scaleFactor + 1) >= 320 && this.scaledHeight / (this.scaleFactor + 1) >= 240) {
/*  85 */       this.scaleFactor++;
/*     */     }
/*     */     
/*  88 */     if (flag && this.scaleFactor % 2 != 0 && this.scaleFactor != 1) {
/*  89 */       this.scaleFactor--;
/*     */     }
/*     */     
/*  92 */     double scaledWidthD = this.scaledWidth / this.scaleFactor;
/*  93 */     double scaledHeightD = this.scaledHeight / this.scaleFactor;
/*  94 */     this.scaledWidth = MathHelper.func_76143_f(scaledWidthD);
/*  95 */     this.scaledHeight = MathHelper.func_76143_f(scaledHeightD);
/*     */   }
/*     */   
/*     */   public String getIdleSign() {
/*  99 */     if (this.idleTimer.passedMs(500L)) {
/* 100 */       this.idling = !this.idling;
/* 101 */       this.idleTimer.reset();
/*     */     } 
/*     */     
/* 104 */     if (this.idling) {
/* 105 */       return "_";
/*     */     }
/* 107 */     return "";
/*     */   }
/*     */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\me\earth\phobos\manager\TextManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */