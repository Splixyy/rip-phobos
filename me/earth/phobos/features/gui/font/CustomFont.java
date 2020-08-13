/*     */ package me.earth.phobos.features.gui.font;
/*     */ 
/*     */ import java.awt.Font;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import me.earth.phobos.features.modules.client.FontMod;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.client.renderer.texture.DynamicTexture;
/*     */ import org.lwjgl.opengl.GL11;
/*     */ 
/*     */ public class CustomFont
/*     */   extends CFont
/*     */ {
/*  14 */   protected CFont.CharData[] boldChars = new CFont.CharData[256];
/*  15 */   protected CFont.CharData[] italicChars = new CFont.CharData[256];
/*  16 */   protected CFont.CharData[] boldItalicChars = new CFont.CharData[256];
/*     */   
/*     */   protected DynamicTexture texBold;
/*     */   
/*     */   protected DynamicTexture texItalic;
/*     */   protected DynamicTexture texItalicBold;
/*  22 */   private final int[] colorCode = new int[32];
/*     */ 
/*     */   
/*     */   public CustomFont(Font font, boolean antiAlias, boolean fractionalMetrics) {
/*  26 */     super(font, antiAlias, fractionalMetrics);
/*  27 */     setupMinecraftColorcodes();
/*  28 */     setupBoldItalicIDs();
/*     */   }
/*     */   
/*     */   public float drawStringWithShadow(String text, double x, double y, int color) {
/*  32 */     float shadowWidth = drawString(text, x + 1.0D, y + 1.0D, color, true);
/*  33 */     return Math.max(shadowWidth, drawString(text, x, y, color, false));
/*     */   }
/*     */   
/*     */   public float drawString(String text, float x, float y, int color) {
/*  37 */     return drawString(text, x, y, color, false);
/*     */   }
/*     */   
/*     */   public float drawCenteredString(String text, float x, float y, int color) {
/*  41 */     return drawString(text, x - (getStringWidth(text) / 2), y, color);
/*     */   }
/*     */   
/*     */   public float drawCenteredStringWithShadow(String text, float x, float y, int color) {
/*  45 */     float shadowWidth = drawString(text, (x - (getStringWidth(text) / 2)) + 1.0D, y + 1.0D, color, true);
/*  46 */     return drawString(text, x - (getStringWidth(text) / 2), y, color);
/*     */   }
/*     */   
/*     */   public float drawString(String text, double xI, double yI, int color, boolean shadow) {
/*  50 */     double x = xI;
/*  51 */     double y = yI;
/*     */     
/*  53 */     if (FontMod.getInstance().isOn() && !((Boolean)(FontMod.getInstance()).shadow.getValue()).booleanValue() && shadow) {
/*  54 */       x -= 0.5D;
/*  55 */       y -= 0.5D;
/*     */     } 
/*     */     
/*  58 */     x--;
/*     */     
/*  60 */     if (text == null) {
/*  61 */       return 0.0F;
/*     */     }
/*     */     
/*  64 */     if (color == 553648127) {
/*  65 */       color = 16777215;
/*     */     }
/*     */     
/*  68 */     if ((color & 0xFC000000) == 0) {
/*  69 */       color |= 0xFF000000;
/*     */     }
/*     */     
/*  72 */     if (shadow) {
/*  73 */       color = (color & 0xFCFCFC) >> 2 | color & 0xFF000000;
/*     */     }
/*     */     
/*  76 */     CFont.CharData[] currentData = this.charData;
/*  77 */     float alpha = (color >> 24 & 0xFF) / 255.0F;
/*     */     
/*  79 */     boolean bold = false;
/*  80 */     boolean italic = false;
/*  81 */     boolean strikethrough = false;
/*  82 */     boolean underline = false;
/*  83 */     boolean render = true;
/*  84 */     x *= 2.0D;
/*  85 */     y = (y - 3.0D) * 2.0D;
/*     */     
/*  87 */     if (render) {
/*  88 */       GL11.glPushMatrix();
/*  89 */       GlStateManager.func_179139_a(0.5D, 0.5D, 0.5D);
/*  90 */       GlStateManager.func_179147_l();
/*  91 */       GlStateManager.func_179112_b(770, 771);
/*  92 */       GlStateManager.func_179131_c((color >> 16 & 0xFF) / 255.0F, (color >> 8 & 0xFF) / 255.0F, (color & 0xFF) / 255.0F, alpha);
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  97 */       int size = text.length();
/*  98 */       GlStateManager.func_179098_w();
/*  99 */       GlStateManager.func_179144_i(this.tex.func_110552_b());
/*     */       
/* 101 */       GL11.glBindTexture(3553, this.tex.func_110552_b());
/*     */       
/* 103 */       for (int i = 0; i < size; i++) {
/* 104 */         char character = text.charAt(i);
/*     */         
/* 106 */         if (character == '§' && i < size) {
/* 107 */           int colorIndex = 21;
/*     */           
/*     */           try {
/* 110 */             colorIndex = "0123456789abcdefklmnor".indexOf(text.charAt(i + 1));
/* 111 */           } catch (Exception e) {
/* 112 */             e.printStackTrace();
/*     */           } 
/*     */           
/* 115 */           if (colorIndex < 16) {
/* 116 */             bold = false;
/* 117 */             italic = false;
/*     */             
/* 119 */             underline = false;
/* 120 */             strikethrough = false;
/* 121 */             GlStateManager.func_179144_i(this.tex.func_110552_b());
/*     */ 
/*     */             
/* 124 */             currentData = this.charData;
/*     */             
/* 126 */             if (colorIndex < 0 || colorIndex > 15) {
/* 127 */               colorIndex = 15;
/*     */             }
/*     */             
/* 130 */             if (shadow) {
/* 131 */               colorIndex += 16;
/*     */             }
/*     */             
/* 134 */             int colorcode = this.colorCode[colorIndex];
/* 135 */             GlStateManager.func_179131_c((colorcode >> 16 & 0xFF) / 255.0F, (colorcode >> 8 & 0xFF) / 255.0F, (colorcode & 0xFF) / 255.0F, alpha);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*     */           }
/* 142 */           else if (colorIndex == 17) {
/* 143 */             bold = true;
/*     */             
/* 145 */             if (italic) {
/* 146 */               GlStateManager.func_179144_i(this.texItalicBold.func_110552_b());
/* 147 */               currentData = this.boldItalicChars;
/*     */             } else {
/* 149 */               GlStateManager.func_179144_i(this.texBold.func_110552_b());
/* 150 */               currentData = this.boldChars;
/*     */             } 
/* 152 */           } else if (colorIndex == 18) {
/* 153 */             strikethrough = true;
/* 154 */           } else if (colorIndex == 19) {
/* 155 */             underline = true;
/* 156 */           } else if (colorIndex == 20) {
/* 157 */             italic = true;
/*     */             
/* 159 */             if (bold) {
/* 160 */               GlStateManager.func_179144_i(this.texItalicBold.func_110552_b());
/* 161 */               currentData = this.boldItalicChars;
/*     */             } else {
/* 163 */               GlStateManager.func_179144_i(this.texItalic.func_110552_b());
/* 164 */               currentData = this.italicChars;
/*     */             } 
/* 166 */           } else if (colorIndex == 21) {
/* 167 */             bold = false;
/* 168 */             italic = false;
/*     */             
/* 170 */             underline = false;
/* 171 */             strikethrough = false;
/* 172 */             GlStateManager.func_179131_c((color >> 16 & 0xFF) / 255.0F, (color >> 8 & 0xFF) / 255.0F, (color & 0xFF) / 255.0F, alpha);
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 177 */             GlStateManager.func_179144_i(this.tex.func_110552_b());
/* 178 */             currentData = this.charData;
/*     */           } 
/*     */           
/* 181 */           i++;
/* 182 */         } else if (character < currentData.length && character >= '\000') {
/* 183 */           GL11.glBegin(4);
/* 184 */           drawChar(currentData, character, (float)x, (float)y);
/* 185 */           GL11.glEnd();
/*     */           
/* 187 */           if (strikethrough) {
/* 188 */             drawLine(x, y + ((currentData[character]).height / 2), x + (currentData[character]).width - 8.0D, y + ((currentData[character]).height / 2), 1.0F);
/*     */           }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 196 */           if (underline) {
/* 197 */             drawLine(x, y + (currentData[character]).height - 2.0D, x + (currentData[character]).width - 8.0D, y + (currentData[character]).height - 2.0D, 1.0F);
/*     */           }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 205 */           x += ((currentData[character]).width - 8 + this.charOffset);
/*     */         } 
/*     */       } 
/*     */       
/* 209 */       GL11.glHint(3155, 4352);
/* 210 */       GL11.glPopMatrix();
/*     */     } 
/*     */     
/* 213 */     return (float)x / 2.0F;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getStringWidth(String text) {
/* 218 */     if (text == null) {
/* 219 */       return 0;
/*     */     }
/*     */     
/* 222 */     int width = 0;
/* 223 */     CFont.CharData[] currentData = this.charData;
/* 224 */     boolean bold = false;
/* 225 */     boolean italic = false;
/* 226 */     int size = text.length();
/*     */     
/* 228 */     for (int i = 0; i < size; i++) {
/* 229 */       char character = text.charAt(i);
/*     */       
/* 231 */       if (character == '§' && i < size) {
/* 232 */         int colorIndex = "0123456789abcdefklmnor".indexOf(character);
/*     */         
/* 234 */         if (colorIndex < 16) {
/* 235 */           bold = false;
/* 236 */           italic = false;
/* 237 */         } else if (colorIndex == 17) {
/* 238 */           bold = true;
/*     */           
/* 240 */           if (italic) {
/* 241 */             currentData = this.boldItalicChars;
/*     */           } else {
/* 243 */             currentData = this.boldChars;
/*     */           } 
/* 245 */         } else if (colorIndex == 20) {
/* 246 */           italic = true;
/*     */           
/* 248 */           if (bold) {
/* 249 */             currentData = this.boldItalicChars;
/*     */           } else {
/* 251 */             currentData = this.italicChars;
/*     */           } 
/* 253 */         } else if (colorIndex == 21) {
/* 254 */           bold = false;
/* 255 */           italic = false;
/* 256 */           currentData = this.charData;
/*     */         } 
/*     */         
/* 259 */         i++;
/* 260 */       } else if (character < currentData.length && character >= '\000') {
/* 261 */         width += (currentData[character]).width - 8 + this.charOffset;
/*     */       } 
/*     */     } 
/*     */     
/* 265 */     return width / 2;
/*     */   }
/*     */   
/*     */   public void setFont(Font font) {
/* 269 */     super.setFont(font);
/* 270 */     setupBoldItalicIDs();
/*     */   }
/*     */   
/*     */   public void setAntiAlias(boolean antiAlias) {
/* 274 */     super.setAntiAlias(antiAlias);
/* 275 */     setupBoldItalicIDs();
/*     */   }
/*     */   
/*     */   public void setFractionalMetrics(boolean fractionalMetrics) {
/* 279 */     super.setFractionalMetrics(fractionalMetrics);
/* 280 */     setupBoldItalicIDs();
/*     */   }
/*     */   
/*     */   private void setupBoldItalicIDs() {
/* 284 */     this
/* 285 */       .texBold = setupTexture(this.font
/* 286 */         .deriveFont(1), this.antiAlias, this.fractionalMetrics, this.boldChars);
/* 287 */     this
/* 288 */       .texItalic = setupTexture(this.font
/* 289 */         .deriveFont(2), this.antiAlias, this.fractionalMetrics, this.italicChars);
/* 290 */     this
/* 291 */       .texItalicBold = setupTexture(this.font
/* 292 */         .deriveFont(3), this.antiAlias, this.fractionalMetrics, this.boldItalicChars);
/*     */   }
/*     */   
/*     */   private void drawLine(double x, double y, double x1, double y1, float width) {
/* 296 */     GL11.glDisable(3553);
/* 297 */     GL11.glLineWidth(width);
/* 298 */     GL11.glBegin(1);
/* 299 */     GL11.glVertex2d(x, y);
/* 300 */     GL11.glVertex2d(x1, y1);
/* 301 */     GL11.glEnd();
/* 302 */     GL11.glEnable(3553);
/*     */   }
/*     */   
/*     */   public List<String> wrapWords(String text, double width) {
/* 306 */     List<String> finalWords = new ArrayList();
/*     */     
/* 308 */     if (getStringWidth(text) > width) {
/* 309 */       String[] words = text.split(" ");
/* 310 */       String currentWord = "";
/* 311 */       char lastColorCode = Character.MAX_VALUE;
/*     */       
/* 313 */       for (String word : words) {
/* 314 */         for (int i = 0; i < (word.toCharArray()).length; i++) {
/* 315 */           char c = word.toCharArray()[i];
/*     */           
/* 317 */           if (c == '§' && i < (word.toCharArray()).length - 1) {
/* 318 */             lastColorCode = word.toCharArray()[i + 1];
/*     */           }
/*     */         } 
/*     */         
/* 322 */         if (getStringWidth(currentWord + word + " ") < width) {
/* 323 */           currentWord = currentWord + word + " ";
/*     */         } else {
/* 325 */           finalWords.add(currentWord);
/* 326 */           currentWord = "§" + lastColorCode + word + " ";
/*     */         } 
/*     */       } 
/*     */       
/* 330 */       if (currentWord.length() > 0) {
/* 331 */         if (getStringWidth(currentWord) < width) {
/* 332 */           finalWords.add("§" + lastColorCode + currentWord + " ");
/* 333 */           currentWord = "";
/*     */         } else {
/* 335 */           for (String s : formatString(currentWord, width)) {
/* 336 */             finalWords.add(s);
/*     */           }
/*     */         } 
/*     */       }
/*     */     } else {
/* 341 */       finalWords.add(text);
/*     */     } 
/*     */     
/* 344 */     return finalWords;
/*     */   }
/*     */   
/*     */   public List<String> formatString(String string, double width) {
/* 348 */     List<String> finalWords = new ArrayList();
/* 349 */     String currentWord = "";
/* 350 */     char lastColorCode = Character.MAX_VALUE;
/* 351 */     char[] chars = string.toCharArray();
/*     */     
/* 353 */     for (int i = 0; i < chars.length; i++) {
/* 354 */       char c = chars[i];
/*     */       
/* 356 */       if (c == '§' && i < chars.length - 1) {
/* 357 */         lastColorCode = chars[i + 1];
/*     */       }
/*     */       
/* 360 */       if (getStringWidth(currentWord + c) < width) {
/* 361 */         currentWord = currentWord + c;
/*     */       } else {
/* 363 */         finalWords.add(currentWord);
/* 364 */         currentWord = "§" + lastColorCode + c;
/*     */       } 
/*     */     } 
/*     */     
/* 368 */     if (currentWord.length() > 0) {
/* 369 */       finalWords.add(currentWord);
/*     */     }
/*     */     
/* 372 */     return finalWords;
/*     */   }
/*     */   
/*     */   private void setupMinecraftColorcodes() {
/* 376 */     for (int index = 0; index < 32; index++) {
/* 377 */       int noClue = (index >> 3 & 0x1) * 85;
/* 378 */       int red = (index >> 2 & 0x1) * 170 + noClue;
/* 379 */       int green = (index >> 1 & 0x1) * 170 + noClue;
/* 380 */       int blue = (index >> 0 & 0x1) * 170 + noClue;
/*     */       
/* 382 */       if (index == 6) {
/* 383 */         red += 85;
/*     */       }
/*     */       
/* 386 */       if (index >= 16) {
/* 387 */         red /= 4;
/* 388 */         green /= 4;
/* 389 */         blue /= 4;
/*     */       } 
/* 391 */       this.colorCode[index] = (red & 0xFF) << 16 | (green & 0xFF) << 8 | blue & 0xFF;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\me\earth\phobos\features\gui\font\CustomFont.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */