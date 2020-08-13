/*     */ package me.earth.phobos.util;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.util.ArrayList;
/*     */ import org.lwjgl.opengl.GL11;
/*     */ 
/*     */ 
/*     */ public class ColorUtil
/*     */ {
/*     */   private ArrayList<ColorName> initColorList() {
/*  11 */     ArrayList<ColorName> colorList = new ArrayList<>();
/*  12 */     colorList.add(new ColorName("AliceBlue", 240, 248, 255));
/*  13 */     colorList.add(new ColorName("AntiqueWhite", 250, 235, 215));
/*  14 */     colorList.add(new ColorName("Aqua", 0, 255, 255));
/*  15 */     colorList.add(new ColorName("Aquamarine", 127, 255, 212));
/*  16 */     colorList.add(new ColorName("Azure", 240, 255, 255));
/*  17 */     colorList.add(new ColorName("Beige", 245, 245, 220));
/*  18 */     colorList.add(new ColorName("Bisque", 255, 228, 196));
/*  19 */     colorList.add(new ColorName("Black", 0, 0, 0));
/*  20 */     colorList.add(new ColorName("BlanchedAlmond", 255, 235, 205));
/*  21 */     colorList.add(new ColorName("Blue", 0, 0, 255));
/*  22 */     colorList.add(new ColorName("BlueViolet", 138, 43, 226));
/*  23 */     colorList.add(new ColorName("Brown", 165, 42, 42));
/*  24 */     colorList.add(new ColorName("BurlyWood", 222, 184, 135));
/*  25 */     colorList.add(new ColorName("CadetBlue", 95, 158, 160));
/*  26 */     colorList.add(new ColorName("Chartreuse", 127, 255, 0));
/*  27 */     colorList.add(new ColorName("Chocolate", 210, 105, 30));
/*  28 */     colorList.add(new ColorName("Coral", 255, 127, 80));
/*  29 */     colorList.add(new ColorName("CornflowerBlue", 100, 149, 237));
/*  30 */     colorList.add(new ColorName("Cornsilk", 255, 248, 220));
/*  31 */     colorList.add(new ColorName("Crimson", 220, 20, 60));
/*  32 */     colorList.add(new ColorName("Cyan", 0, 255, 255));
/*  33 */     colorList.add(new ColorName("DarkBlue", 0, 0, 139));
/*  34 */     colorList.add(new ColorName("DarkCyan", 0, 139, 139));
/*  35 */     colorList.add(new ColorName("DarkGoldenRod", 184, 134, 11));
/*  36 */     colorList.add(new ColorName("DarkGray", 169, 169, 169));
/*  37 */     colorList.add(new ColorName("DarkGreen", 0, 100, 0));
/*  38 */     colorList.add(new ColorName("DarkKhaki", 189, 183, 107));
/*  39 */     colorList.add(new ColorName("DarkMagenta", 139, 0, 139));
/*  40 */     colorList.add(new ColorName("DarkOliveGreen", 85, 107, 47));
/*  41 */     colorList.add(new ColorName("DarkOrange", 255, 140, 0));
/*  42 */     colorList.add(new ColorName("DarkOrchid", 153, 50, 204));
/*  43 */     colorList.add(new ColorName("DarkRed", 139, 0, 0));
/*  44 */     colorList.add(new ColorName("DarkSalmon", 233, 150, 122));
/*  45 */     colorList.add(new ColorName("DarkSeaGreen", 143, 188, 143));
/*  46 */     colorList.add(new ColorName("DarkSlateBlue", 72, 61, 139));
/*  47 */     colorList.add(new ColorName("DarkSlateGray", 47, 79, 79));
/*  48 */     colorList.add(new ColorName("DarkTurquoise", 0, 206, 209));
/*  49 */     colorList.add(new ColorName("DarkViolet", 148, 0, 211));
/*  50 */     colorList.add(new ColorName("DeepPink", 255, 20, 147));
/*  51 */     colorList.add(new ColorName("DeepSkyBlue", 0, 191, 255));
/*  52 */     colorList.add(new ColorName("DimGray", 105, 105, 105));
/*  53 */     colorList.add(new ColorName("DodgerBlue", 30, 144, 255));
/*  54 */     colorList.add(new ColorName("FireBrick", 178, 34, 34));
/*  55 */     colorList.add(new ColorName("FloralWhite", 255, 250, 240));
/*  56 */     colorList.add(new ColorName("ForestGreen", 34, 139, 34));
/*  57 */     colorList.add(new ColorName("Fuchsia", 255, 0, 255));
/*  58 */     colorList.add(new ColorName("Gainsboro", 220, 220, 220));
/*  59 */     colorList.add(new ColorName("GhostWhite", 248, 248, 255));
/*  60 */     colorList.add(new ColorName("Gold", 255, 215, 0));
/*  61 */     colorList.add(new ColorName("GoldenRod", 218, 165, 32));
/*  62 */     colorList.add(new ColorName("Gray", 128, 128, 128));
/*  63 */     colorList.add(new ColorName("Green", 0, 128, 0));
/*  64 */     colorList.add(new ColorName("GreenYellow", 173, 255, 47));
/*  65 */     colorList.add(new ColorName("HoneyDew", 240, 255, 240));
/*  66 */     colorList.add(new ColorName("HotPink", 255, 105, 180));
/*  67 */     colorList.add(new ColorName("IndianRed", 205, 92, 92));
/*  68 */     colorList.add(new ColorName("Indigo", 75, 0, 130));
/*  69 */     colorList.add(new ColorName("Ivory", 255, 255, 240));
/*  70 */     colorList.add(new ColorName("Khaki", 240, 230, 140));
/*  71 */     colorList.add(new ColorName("Lavender", 230, 230, 250));
/*  72 */     colorList.add(new ColorName("LavenderBlush", 255, 240, 245));
/*  73 */     colorList.add(new ColorName("LawnGreen", 124, 252, 0));
/*  74 */     colorList.add(new ColorName("LemonChiffon", 255, 250, 205));
/*  75 */     colorList.add(new ColorName("LightBlue", 173, 216, 230));
/*  76 */     colorList.add(new ColorName("LightCoral", 240, 128, 128));
/*  77 */     colorList.add(new ColorName("LightCyan", 224, 255, 255));
/*  78 */     colorList.add(new ColorName("LightGoldenRodYellow", 250, 250, 210));
/*  79 */     colorList.add(new ColorName("LightGray", 211, 211, 211));
/*  80 */     colorList.add(new ColorName("LightGreen", 144, 238, 144));
/*  81 */     colorList.add(new ColorName("LightPink", 255, 182, 193));
/*  82 */     colorList.add(new ColorName("LightSalmon", 255, 160, 122));
/*  83 */     colorList.add(new ColorName("LightSeaGreen", 32, 178, 170));
/*  84 */     colorList.add(new ColorName("LightSkyBlue", 135, 206, 250));
/*  85 */     colorList.add(new ColorName("LightSlateGray", 119, 136, 153));
/*  86 */     colorList.add(new ColorName("LightSteelBlue", 176, 196, 222));
/*  87 */     colorList.add(new ColorName("LightYellow", 255, 255, 224));
/*  88 */     colorList.add(new ColorName("Lime", 0, 255, 0));
/*  89 */     colorList.add(new ColorName("LimeGreen", 50, 205, 50));
/*  90 */     colorList.add(new ColorName("Linen", 250, 240, 230));
/*  91 */     colorList.add(new ColorName("Magenta", 255, 0, 255));
/*  92 */     colorList.add(new ColorName("Maroon", 128, 0, 0));
/*  93 */     colorList.add(new ColorName("MediumAquaMarine", 102, 205, 170));
/*  94 */     colorList.add(new ColorName("MediumBlue", 0, 0, 205));
/*  95 */     colorList.add(new ColorName("MediumOrchid", 186, 85, 211));
/*  96 */     colorList.add(new ColorName("MediumPurple", 147, 112, 219));
/*  97 */     colorList.add(new ColorName("MediumSeaGreen", 60, 179, 113));
/*  98 */     colorList.add(new ColorName("MediumSlateBlue", 123, 104, 238));
/*  99 */     colorList.add(new ColorName("MediumSpringGreen", 0, 250, 154));
/* 100 */     colorList.add(new ColorName("MediumTurquoise", 72, 209, 204));
/* 101 */     colorList.add(new ColorName("MediumVioletRed", 199, 21, 133));
/* 102 */     colorList.add(new ColorName("MidnightBlue", 25, 25, 112));
/* 103 */     colorList.add(new ColorName("MintCream", 245, 255, 250));
/* 104 */     colorList.add(new ColorName("MistyRose", 255, 228, 225));
/* 105 */     colorList.add(new ColorName("Moccasin", 255, 228, 181));
/* 106 */     colorList.add(new ColorName("NavajoWhite", 255, 222, 173));
/* 107 */     colorList.add(new ColorName("Navy", 0, 0, 128));
/* 108 */     colorList.add(new ColorName("OldLace", 253, 245, 230));
/* 109 */     colorList.add(new ColorName("Olive", 128, 128, 0));
/* 110 */     colorList.add(new ColorName("OliveDrab", 107, 142, 35));
/* 111 */     colorList.add(new ColorName("Orange", 255, 165, 0));
/* 112 */     colorList.add(new ColorName("OrangeRed", 255, 69, 0));
/* 113 */     colorList.add(new ColorName("Orchid", 218, 112, 214));
/* 114 */     colorList.add(new ColorName("PaleGoldenRod", 238, 232, 170));
/* 115 */     colorList.add(new ColorName("PaleGreen", 152, 251, 152));
/* 116 */     colorList.add(new ColorName("PaleTurquoise", 175, 238, 238));
/* 117 */     colorList.add(new ColorName("PaleVioletRed", 219, 112, 147));
/* 118 */     colorList.add(new ColorName("PapayaWhip", 255, 239, 213));
/* 119 */     colorList.add(new ColorName("PeachPuff", 255, 218, 185));
/* 120 */     colorList.add(new ColorName("Peru", 205, 133, 63));
/* 121 */     colorList.add(new ColorName("Pink", 255, 192, 203));
/* 122 */     colorList.add(new ColorName("Plum", 221, 160, 221));
/* 123 */     colorList.add(new ColorName("PowderBlue", 176, 224, 230));
/* 124 */     colorList.add(new ColorName("Purple", 128, 0, 128));
/* 125 */     colorList.add(new ColorName("Red", 255, 0, 0));
/* 126 */     colorList.add(new ColorName("RosyBrown", 188, 143, 143));
/* 127 */     colorList.add(new ColorName("RoyalBlue", 65, 105, 225));
/* 128 */     colorList.add(new ColorName("SaddleBrown", 139, 69, 19));
/* 129 */     colorList.add(new ColorName("Salmon", 250, 128, 114));
/* 130 */     colorList.add(new ColorName("SandyBrown", 244, 164, 96));
/* 131 */     colorList.add(new ColorName("SeaGreen", 46, 139, 87));
/* 132 */     colorList.add(new ColorName("SeaShell", 255, 245, 238));
/* 133 */     colorList.add(new ColorName("Sienna", 160, 82, 45));
/* 134 */     colorList.add(new ColorName("Silver", 192, 192, 192));
/* 135 */     colorList.add(new ColorName("SkyBlue", 135, 206, 235));
/* 136 */     colorList.add(new ColorName("SlateBlue", 106, 90, 205));
/* 137 */     colorList.add(new ColorName("SlateGray", 112, 128, 144));
/* 138 */     colorList.add(new ColorName("Snow", 255, 250, 250));
/* 139 */     colorList.add(new ColorName("SpringGreen", 0, 255, 127));
/* 140 */     colorList.add(new ColorName("SteelBlue", 70, 130, 180));
/* 141 */     colorList.add(new ColorName("Tan", 210, 180, 140));
/* 142 */     colorList.add(new ColorName("Teal", 0, 128, 128));
/* 143 */     colorList.add(new ColorName("Thistle", 216, 191, 216));
/* 144 */     colorList.add(new ColorName("Tomato", 255, 99, 71));
/* 145 */     colorList.add(new ColorName("Turquoise", 64, 224, 208));
/* 146 */     colorList.add(new ColorName("Violet", 238, 130, 238));
/* 147 */     colorList.add(new ColorName("Wheat", 245, 222, 179));
/* 148 */     colorList.add(new ColorName("White", 255, 255, 255));
/* 149 */     colorList.add(new ColorName("WhiteSmoke", 245, 245, 245));
/* 150 */     colorList.add(new ColorName("Yellow", 255, 255, 0));
/* 151 */     colorList.add(new ColorName("YellowGreen", 154, 205, 50));
/* 152 */     return colorList;
/*     */   }
/*     */   
/*     */   public static int toRGBA(double r, double g, double b, double a) {
/* 156 */     return toRGBA((float)r, (float)g, (float)b, (float)a);
/*     */   }
/*     */   
/*     */   public String getColorNameFromRgb(int r, int g, int b) {
/* 160 */     ArrayList<ColorName> colorList = initColorList();
/* 161 */     ColorName closestMatch = null;
/* 162 */     int minMSE = Integer.MAX_VALUE;
/*     */     
/* 164 */     for (ColorName c : colorList) {
/* 165 */       int mse = c.computeMSE(r, g, b);
/* 166 */       if (mse < minMSE) {
/* 167 */         minMSE = mse;
/* 168 */         closestMatch = c;
/*     */       } 
/*     */     } 
/*     */     
/* 172 */     if (closestMatch != null) {
/* 173 */       return closestMatch.getName();
/*     */     }
/* 175 */     return "No matched color name.";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getColorNameFromHex(int hexColor) {
/* 180 */     int r = (hexColor & 0xFF0000) >> 16;
/* 181 */     int g = (hexColor & 0xFF00) >> 8;
/* 182 */     int b = hexColor & 0xFF;
/* 183 */     return getColorNameFromRgb(r, g, b);
/*     */   }
/*     */   
/*     */   public int colorToHex(Color c) {
/* 187 */     return Integer.decode("0x" + 
/* 188 */         Integer.toHexString(c.getRGB()).substring(2)).intValue();
/*     */   }
/*     */   
/*     */   public String getColorNameFromColor(Color color) {
/* 192 */     return getColorNameFromRgb(color.getRed(), color.getGreen(), color
/* 193 */         .getBlue());
/*     */   }
/*     */   
/*     */   public static class ColorName
/*     */   {
/*     */     public int r;
/*     */     public int g;
/*     */     
/*     */     public ColorName(String name, int r, int g, int b) {
/* 202 */       this.r = r;
/* 203 */       this.g = g;
/* 204 */       this.b = b;
/* 205 */       this.name = name;
/*     */     }
/*     */     public int b; public String name;
/*     */     public int computeMSE(int pixR, int pixG, int pixB) {
/* 209 */       return ((pixR - this.r) * (pixR - this.r) + (pixG - this.g) * (pixG - this.g) + (pixB - this.b) * (pixB - this.b)) / 3;
/*     */     }
/*     */ 
/*     */     
/*     */     public int getR() {
/* 214 */       return this.r;
/*     */     }
/*     */     
/*     */     public int getG() {
/* 218 */       return this.g;
/*     */     }
/*     */     
/*     */     public int getB() {
/* 222 */       return this.b;
/*     */     }
/*     */     
/*     */     public String getName() {
/* 226 */       return this.name;
/*     */     }
/*     */   }
/*     */   
/*     */   public static int toRGBA(int r, int g, int b) {
/* 231 */     return toRGBA(r, g, b, 255);
/*     */   }
/*     */   
/*     */   public static int toRGBA(int r, int g, int b, int a) {
/* 235 */     return (r << 16) + (g << 8) + b + (a << 24);
/*     */   }
/*     */   
/*     */   public static int toARGB(int r, int g, int b, int a) {
/* 239 */     return (new Color(r, g, b, a)).getRGB();
/*     */   }
/*     */ 
/*     */   
/*     */   public static int toRGBA(float r, float g, float b, float a) {
/* 244 */     return toRGBA((int)(r * 255.0F), (int)(g * 255.0F), (int)(b * 255.0F), (int)(a * 255.0F));
/*     */   }
/*     */   
/*     */   public static int toRGBA(float[] colors) {
/* 248 */     if (colors.length != 4) throw new IllegalArgumentException("colors[] must have a length of 4!"); 
/* 249 */     return toRGBA(colors[0], colors[1], colors[2], colors[3]);
/*     */   }
/*     */   
/*     */   public static int toRGBA(double[] colors) {
/* 253 */     if (colors.length != 4) throw new IllegalArgumentException("colors[] must have a length of 4!"); 
/* 254 */     return toRGBA((float)colors[0], (float)colors[1], (float)colors[2], (float)colors[3]);
/*     */   }
/*     */   
/*     */   public static int toRGBA(Color color) {
/* 258 */     return toRGBA(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
/*     */   }
/*     */   
/*     */   public static int[] toRGBAArray(int colorBuffer) {
/* 262 */     return new int[] { colorBuffer >> 16 & 0xFF, colorBuffer >> 8 & 0xFF, colorBuffer & 0xFF, colorBuffer >> 24 & 0xFF };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class Colors
/*     */   {
/* 271 */     public static final int WHITE = ColorUtil.toRGBA(255, 255, 255, 255);
/* 272 */     public static final int BLACK = ColorUtil.toRGBA(0, 0, 0, 255);
/* 273 */     public static final int RED = ColorUtil.toRGBA(255, 0, 0, 255);
/* 274 */     public static final int GREEN = ColorUtil.toRGBA(0, 255, 0, 255);
/* 275 */     public static final int BLUE = ColorUtil.toRGBA(0, 0, 255, 255);
/* 276 */     public static final int ORANGE = ColorUtil.toRGBA(255, 128, 0, 255);
/* 277 */     public static final int PURPLE = ColorUtil.toRGBA(163, 73, 163, 255);
/* 278 */     public static final int GRAY = ColorUtil.toRGBA(127, 127, 127, 255);
/* 279 */     public static final int DARK_RED = ColorUtil.toRGBA(64, 0, 0, 255);
/* 280 */     public static final int YELLOW = ColorUtil.toRGBA(255, 255, 0, 255);
/*     */     public static final int RAINBOW = -2147483648;
/*     */   }
/*     */   
/*     */   public static int changeAlpha(int origColor, int userInputedAlpha) {
/* 285 */     origColor &= 0xFFFFFF;
/* 286 */     return userInputedAlpha << 24 | origColor;
/*     */   }
/*     */   
/*     */   public static class HueCycler
/*     */   {
/* 291 */     public int index = 0;
/*     */     public int[] cycles;
/*     */     
/*     */     public HueCycler(int cycles) {
/* 295 */       if (cycles <= 0) throw new IllegalArgumentException("cycles <= 0"); 
/* 296 */       this.cycles = new int[cycles];
/* 297 */       double hue = 0.0D;
/* 298 */       double add = 1.0D / cycles;
/* 299 */       for (int i = 0; i < cycles; i++) {
/* 300 */         this.cycles[i] = Color.HSBtoRGB((float)hue, 1.0F, 1.0F);
/* 301 */         hue += add;
/*     */       } 
/*     */     }
/*     */     
/*     */     public void reset() {
/* 306 */       this.index = 0;
/*     */     }
/*     */     
/*     */     public void reset(int index) {
/* 310 */       this.index = index;
/*     */     }
/*     */     
/*     */     public int next() {
/* 314 */       int a = this.cycles[this.index];
/* 315 */       this.index++;
/* 316 */       if (this.index >= this.cycles.length) this.index = 0; 
/* 317 */       return a;
/*     */     }
/*     */     
/*     */     public void setNext() {
/* 321 */       int rgb = next();
/*     */     }
/*     */     
/*     */     public void set() {
/* 325 */       int rgb = this.cycles[this.index];
/* 326 */       float red = (rgb >> 16 & 0xFF) / 255.0F;
/* 327 */       float green = (rgb >> 8 & 0xFF) / 255.0F;
/* 328 */       float blue = (rgb & 0xFF) / 255.0F;
/* 329 */       GL11.glColor3f(red, green, blue);
/*     */     }
/*     */     
/*     */     public void setNext(float alpha) {
/* 333 */       int rgb = next();
/* 334 */       float red = (rgb >> 16 & 0xFF) / 255.0F;
/* 335 */       float green = (rgb >> 8 & 0xFF) / 255.0F;
/* 336 */       float blue = (rgb & 0xFF) / 255.0F;
/* 337 */       GL11.glColor4f(red, green, blue, alpha);
/*     */     }
/*     */     
/*     */     public int current() {
/* 341 */       return this.cycles[this.index];
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\me\earth\phobo\\util\ColorUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */