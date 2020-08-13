/*     */ package me.earth.phobos.util;
/*     */ 
/*     */ import java.util.Random;
/*     */ import java.util.regex.Pattern;
/*     */ 
/*     */ public class TextUtil
/*     */ {
/*     */   public static final String SECTIONSIGN = "§";
/*   9 */   private static final Pattern STRIP_COLOR_PATTERN = Pattern.compile("(?i)" + String.valueOf("§") + "[0-9A-FK-OR]");
/*     */   
/*     */   public static final String BLACK = "§0";
/*     */   
/*     */   public static final String DARK_BLUE = "§1";
/*     */   
/*     */   public static final String DARK_GREEN = "§2";
/*     */   
/*     */   public static final String DARK_AQUA = "§3";
/*     */   
/*     */   public static final String DARK_RED = "§4";
/*     */   
/*     */   public static final String DARK_PURPLE = "§5";
/*     */   
/*     */   public static final String GOLD = "§6";
/*     */   
/*     */   public static final String GRAY = "§7";
/*     */   
/*     */   public static final String DARK_GRAY = "§8";
/*     */   
/*     */   public static final String BLUE = "§9";
/*     */   
/*     */   public static final String GREEN = "§a";
/*     */   
/*     */   public static final String AQUA = "§b";
/*     */   
/*     */   public static final String RED = "§c";
/*     */   
/*     */   public static final String LIGHT_PURPLE = "§d";
/*     */   
/*     */   public static final String YELLOW = "§e";
/*     */   public static final String WHITE = "§f";
/*     */   public static final String OBFUSCATED = "§k";
/*     */   public static final String BOLD = "§l";
/*     */   public static final String STRIKE = "§m";
/*     */   public static final String UNDERLINE = "§n";
/*     */   public static final String ITALIC = "§o";
/*     */   public static final String RESET = "§r";
/*     */   public static final String blank = " ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒";
/*     */   public static final String line1 = " ███▒█▒█▒███▒███▒███▒███";
/*     */   public static final String line2 = " █▒█▒█▒█▒█▒█▒█▒█▒█▒█▒█▒▒";
/*     */   public static final String line3 = " ███▒███▒█▒█▒███▒█▒█▒███";
/*     */   public static final String line4 = " █▒▒▒█▒█▒█▒█▒█▒█▒█▒█▒▒▒█";
/*     */   public static final String line5 = " █▒▒▒█▒█▒███▒███▒███▒███";
/*     */   public static final String pword = "  ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒\n ███▒█▒█▒███▒███▒███▒███\n █▒█▒█▒█▒█▒█▒█▒█▒█▒█▒█▒▒\n ███▒███▒█▒█▒███▒█▒█▒███\n █▒▒▒█▒█▒█▒█▒█▒█▒█▒█▒▒▒█\n █▒▒▒█▒█▒███▒███▒███▒███\n ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒";
/*  54 */   public static String shrug = "¯\\_(ツ)_/¯";
/*     */   
/*  56 */   private static final Random rand = new Random();
/*     */ 
/*     */   
/*     */   public enum Color
/*     */   {
/*  61 */     NONE, WHITE, BLACK, DARK_BLUE, DARK_GREEN, DARK_AQUA, DARK_RED, DARK_PURPLE, GOLD, GRAY, DARK_GRAY, BLUE, GREEN, AQUA, RED, LIGHT_PURPLE, YELLOW;
/*     */   }
/*     */   
/*     */   public static String stripColor(String input) {
/*  65 */     if (input == null) {
/*  66 */       return null;
/*     */     }
/*     */     
/*  69 */     return STRIP_COLOR_PATTERN.matcher(input).replaceAll("");
/*     */   }
/*     */   
/*     */   public static String coloredString(String string, Color color) {
/*  73 */     String coloredString = string;
/*  74 */     switch (color) {
/*     */       case AQUA:
/*  76 */         coloredString = "§b" + coloredString + "§r";
/*     */         break;
/*     */       case WHITE:
/*  79 */         coloredString = "§f" + coloredString + "§r";
/*     */         break;
/*     */       case BLACK:
/*  82 */         coloredString = "§0" + coloredString + "§r";
/*     */         break;
/*     */       case DARK_BLUE:
/*  85 */         coloredString = "§1" + coloredString + "§r";
/*     */         break;
/*     */       case DARK_GREEN:
/*  88 */         coloredString = "§2" + coloredString + "§r";
/*     */         break;
/*     */       case DARK_AQUA:
/*  91 */         coloredString = "§3" + coloredString + "§r";
/*     */         break;
/*     */       case DARK_RED:
/*  94 */         coloredString = "§4" + coloredString + "§r";
/*     */         break;
/*     */       case DARK_PURPLE:
/*  97 */         coloredString = "§5" + coloredString + "§r";
/*     */         break;
/*     */       case GOLD:
/* 100 */         coloredString = "§6" + coloredString + "§r";
/*     */         break;
/*     */       case DARK_GRAY:
/* 103 */         coloredString = "§8" + coloredString + "§r";
/*     */         break;
/*     */       case GRAY:
/* 106 */         coloredString = "§7" + coloredString + "§r";
/*     */         break;
/*     */       case BLUE:
/* 109 */         coloredString = "§9" + coloredString + "§r";
/*     */         break;
/*     */       case RED:
/* 112 */         coloredString = "§c" + coloredString + "§r";
/*     */         break;
/*     */       case GREEN:
/* 115 */         coloredString = "§a" + coloredString + "§r";
/*     */         break;
/*     */       case LIGHT_PURPLE:
/* 118 */         coloredString = "§d" + coloredString + "§r";
/*     */         break;
/*     */       case YELLOW:
/* 121 */         coloredString = "§e" + coloredString + "§r";
/*     */         break;
/*     */     } 
/*     */     
/* 125 */     return coloredString;
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
/*     */   public static String cropMaxLengthMessage(String s, int i) {
/* 137 */     String output = "";
/* 138 */     if (s.length() >= 256 - i) {
/* 139 */       output = s.substring(0, 256 - i);
/*     */     }
/* 141 */     return output;
/*     */   }
/*     */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\me\earth\phobo\\util\TextUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */