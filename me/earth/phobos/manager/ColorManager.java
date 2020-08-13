/*    */ package me.earth.phobos.manager;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import me.earth.phobos.util.ColorUtil;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ColorManager
/*    */ {
/* 16 */   private float red = 1.0F;
/* 17 */   private float green = 1.0F;
/* 18 */   private float blue = 1.0F;
/* 19 */   private float alpha = 1.0F;
/* 20 */   private Color color = new Color(this.red, this.green, this.blue, this.alpha);
/*    */ 
/*    */   
/*    */   public Color getColor() {
/* 24 */     return this.color;
/*    */   }
/*    */   
/*    */   public int getColorAsInt() {
/* 28 */     return ColorUtil.toRGBA(this.color);
/*    */   }
/*    */   
/*    */   public int getColorAsIntFullAlpha() {
/* 32 */     return ColorUtil.toRGBA(new Color(this.color.getRed(), this.color.getGreen(), this.color.getBlue(), 255));
/*    */   }
/*    */   
/*    */   public int getColorWithAlpha(int alpha) {
/* 36 */     return ColorUtil.toRGBA(new Color(this.red, this.green, this.blue, alpha / 255.0F));
/*    */   }
/*    */   
/*    */   public void setColor(float red, float green, float blue, float alpha) {
/* 40 */     this.red = red;
/* 41 */     this.green = green;
/* 42 */     this.blue = blue;
/* 43 */     this.alpha = alpha;
/* 44 */     updateColor();
/*    */   }
/*    */   
/*    */   public void updateColor() {
/* 48 */     setColor(new Color(this.red, this.green, this.blue, this.alpha));
/*    */   }
/*    */   
/*    */   public void setColor(Color color) {
/* 52 */     this.color = color;
/*    */   }
/*    */   
/*    */   public void setColor(int red, int green, int blue, int alpha) {
/* 56 */     this.red = red / 255.0F;
/* 57 */     this.green = green / 255.0F;
/* 58 */     this.blue = blue / 255.0F;
/* 59 */     this.alpha = alpha / 255.0F;
/* 60 */     updateColor();
/*    */   }
/*    */   
/*    */   public void setRed(float red) {
/* 64 */     this.red = red;
/* 65 */     updateColor();
/*    */   }
/*    */   
/*    */   public void setGreen(float green) {
/* 69 */     this.green = green;
/* 70 */     updateColor();
/*    */   }
/*    */   
/*    */   public void setBlue(float blue) {
/* 74 */     this.blue = blue;
/* 75 */     updateColor();
/*    */   }
/*    */   
/*    */   public void setAlpha(float alpha) {
/* 79 */     this.alpha = alpha;
/* 80 */     updateColor();
/*    */   }
/*    */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\me\earth\phobos\manager\ColorManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */