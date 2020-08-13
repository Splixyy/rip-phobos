/*     */ package me.earth.phobos.features.gui.font;
/*     */ import java.awt.Font;
/*     */ import java.awt.FontMetrics;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.RenderingHints;
/*     */ import java.awt.geom.Rectangle2D;
/*     */ import java.awt.image.BufferedImage;
/*     */ import net.minecraft.client.renderer.texture.DynamicTexture;
/*     */ import org.lwjgl.opengl.GL11;
/*     */ 
/*     */ public class CFont {
/*  12 */   private float imgSize = 512.0F;
/*  13 */   protected CharData[] charData = new CharData[256];
/*     */   protected Font font;
/*     */   protected boolean antiAlias;
/*     */   protected boolean fractionalMetrics;
/*  17 */   protected int fontHeight = -1;
/*  18 */   protected int charOffset = 0;
/*     */   protected DynamicTexture tex;
/*     */   
/*     */   public CFont(Font font, boolean antiAlias, boolean fractionalMetrics) {
/*  22 */     this.font = font;
/*  23 */     this.antiAlias = antiAlias;
/*  24 */     this.fractionalMetrics = fractionalMetrics;
/*  25 */     this.tex = setupTexture(font, antiAlias, fractionalMetrics, this.charData);
/*     */   }
/*     */ 
/*     */   
/*     */   protected DynamicTexture setupTexture(Font font, boolean antiAlias, boolean fractionalMetrics, CharData[] chars) {
/*  30 */     BufferedImage img = generateFontImage(font, antiAlias, fractionalMetrics, chars);
/*     */     try {
/*  32 */       return new DynamicTexture(img);
/*  33 */     } catch (Exception e) {
/*  34 */       e.printStackTrace();
/*     */       
/*  36 */       return null;
/*     */     } 
/*     */   }
/*     */   
/*     */   protected BufferedImage generateFontImage(Font font, boolean antiAlias, boolean fractionalMetrics, CharData[] chars) {
/*  41 */     int imgSize = (int)this.imgSize;
/*  42 */     BufferedImage bufferedImage = new BufferedImage(imgSize, imgSize, 2);
/*  43 */     Graphics2D g = (Graphics2D)bufferedImage.getGraphics();
/*  44 */     g.setFont(font);
/*  45 */     g.setColor(new Color(255, 255, 255, 0));
/*  46 */     g.fillRect(0, 0, imgSize, imgSize);
/*  47 */     g.setColor(Color.WHITE);
/*  48 */     g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, fractionalMetrics ? RenderingHints.VALUE_FRACTIONALMETRICS_ON : RenderingHints.VALUE_FRACTIONALMETRICS_OFF);
/*     */ 
/*     */     
/*  51 */     g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, antiAlias ? RenderingHints.VALUE_TEXT_ANTIALIAS_ON : RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
/*     */ 
/*     */     
/*  54 */     g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, antiAlias ? RenderingHints.VALUE_ANTIALIAS_ON : RenderingHints.VALUE_ANTIALIAS_OFF);
/*     */ 
/*     */     
/*  57 */     FontMetrics fontMetrics = g.getFontMetrics();
/*  58 */     int charHeight = 0;
/*  59 */     int positionX = 0;
/*  60 */     int positionY = 1;
/*     */     
/*  62 */     for (int i = 0; i < chars.length; i++) {
/*  63 */       char ch = (char)i;
/*  64 */       CharData charData = new CharData();
/*  65 */       Rectangle2D dimensions = fontMetrics.getStringBounds(String.valueOf(ch), g);
/*  66 */       charData.width = (dimensions.getBounds()).width + 8;
/*  67 */       charData.height = (dimensions.getBounds()).height;
/*     */       
/*  69 */       if (positionX + charData.width >= imgSize) {
/*  70 */         positionX = 0;
/*  71 */         positionY += charHeight;
/*  72 */         charHeight = 0;
/*     */       } 
/*     */       
/*  75 */       if (charData.height > charHeight) {
/*  76 */         charHeight = charData.height;
/*     */       }
/*     */       
/*  79 */       charData.storedX = positionX;
/*  80 */       charData.storedY = positionY;
/*     */       
/*  82 */       if (charData.height > this.fontHeight) {
/*  83 */         this.fontHeight = charData.height;
/*     */       }
/*     */       
/*  86 */       chars[i] = charData;
/*  87 */       g.drawString(String.valueOf(ch), positionX + 2, positionY + fontMetrics.getAscent());
/*  88 */       positionX += charData.width;
/*     */     } 
/*  90 */     return bufferedImage;
/*     */   }
/*     */   
/*     */   public void drawChar(CharData[] chars, char c, float x, float y) throws ArrayIndexOutOfBoundsException {
/*     */     try {
/*  95 */       drawQuad(x, y, (chars[c]).width, (chars[c]).height, (chars[c]).storedX, (chars[c]).storedY, (chars[c]).width, (chars[c]).height);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     }
/* 104 */     catch (Exception e) {
/* 105 */       e.printStackTrace();
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void drawQuad(float x, float y, float width, float height, float srcX, float srcY, float srcWidth, float srcHeight) {
/* 110 */     float renderSRCX = srcX / this.imgSize;
/* 111 */     float renderSRCY = srcY / this.imgSize;
/* 112 */     float renderSRCWidth = srcWidth / this.imgSize;
/* 113 */     float renderSRCHeight = srcHeight / this.imgSize;
/* 114 */     GL11.glTexCoord2f(renderSRCX + renderSRCWidth, renderSRCY);
/* 115 */     GL11.glVertex2d((x + width), y);
/* 116 */     GL11.glTexCoord2f(renderSRCX, renderSRCY);
/* 117 */     GL11.glVertex2d(x, y);
/* 118 */     GL11.glTexCoord2f(renderSRCX, renderSRCY + renderSRCHeight);
/* 119 */     GL11.glVertex2d(x, (y + height));
/* 120 */     GL11.glTexCoord2f(renderSRCX, renderSRCY + renderSRCHeight);
/* 121 */     GL11.glVertex2d(x, (y + height));
/* 122 */     GL11.glTexCoord2f(renderSRCX + renderSRCWidth, renderSRCY + renderSRCHeight);
/* 123 */     GL11.glVertex2d((x + width), (y + height));
/* 124 */     GL11.glTexCoord2f(renderSRCX + renderSRCWidth, renderSRCY);
/* 125 */     GL11.glVertex2d((x + width), y);
/*     */   }
/*     */   
/*     */   public int getStringHeight(String text) {
/* 129 */     return getHeight();
/*     */   }
/*     */   
/*     */   public int getHeight() {
/* 133 */     return (this.fontHeight - 8) / 2;
/*     */   }
/*     */   
/*     */   public int getStringWidth(String text) {
/* 137 */     int width = 0;
/* 138 */     for (char c : text.toCharArray()) {
/* 139 */       if (c < this.charData.length && c >= '\000') {
/* 140 */         width += (this.charData[c]).width - 8 + this.charOffset;
/*     */       }
/*     */     } 
/* 143 */     return width / 2;
/*     */   }
/*     */   
/*     */   public boolean isAntiAlias() {
/* 147 */     return this.antiAlias;
/*     */   }
/*     */   
/*     */   public void setAntiAlias(boolean antiAlias) {
/* 151 */     if (this.antiAlias != antiAlias) {
/* 152 */       this.antiAlias = antiAlias;
/* 153 */       this.tex = setupTexture(this.font, antiAlias, this.fractionalMetrics, this.charData);
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean isFractionalMetrics() {
/* 158 */     return this.fractionalMetrics;
/*     */   }
/*     */   
/*     */   public void setFractionalMetrics(boolean fractionalMetrics) {
/* 162 */     if (this.fractionalMetrics != fractionalMetrics) {
/* 163 */       this.fractionalMetrics = fractionalMetrics;
/* 164 */       this.tex = setupTexture(this.font, this.antiAlias, fractionalMetrics, this.charData);
/*     */     } 
/*     */   }
/*     */   
/*     */   public Font getFont() {
/* 169 */     return this.font;
/*     */   }
/*     */   
/*     */   public void setFont(Font font) {
/* 173 */     this.font = font;
/* 174 */     this.tex = setupTexture(font, this.antiAlias, this.fractionalMetrics, this.charData);
/*     */   }
/*     */   
/*     */   protected static class CharData {
/*     */     public int width;
/*     */     public int height;
/*     */     public int storedX;
/*     */     public int storedY;
/*     */   }
/*     */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\me\earth\phobos\features\gui\font\CFont.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */