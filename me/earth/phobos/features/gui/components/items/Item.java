/*    */ package me.earth.phobos.features.gui.components.items;
/*    */ 
/*    */ import me.earth.phobos.features.Feature;
/*    */ 
/*    */ public class Item
/*    */   extends Feature
/*    */ {
/*    */   protected float x;
/*    */   protected float y;
/*    */   
/*    */   public Item(String name) {
/* 12 */     super(name);
/*    */   }
/*    */   protected int width; protected int height; private boolean hidden;
/*    */   public void setLocation(float x, float y) {
/* 16 */     this.x = x;
/* 17 */     this.y = y;
/*    */   }
/*    */   
/*    */   public void drawScreen(int mouseX, int mouseY, float partialTicks) {}
/*    */   
/*    */   public void mouseClicked(int mouseX, int mouseY, int mouseButton) {}
/*    */   
/*    */   public void mouseReleased(int mouseX, int mouseY, int releaseButton) {}
/*    */   
/*    */   public void update() {}
/*    */   
/*    */   public void onKeyTyped(char typedChar, int keyCode) {}
/*    */   
/*    */   public float getX() {
/* 31 */     return this.x;
/*    */   }
/*    */   
/*    */   public float getY() {
/* 35 */     return this.y;
/*    */   }
/*    */   
/*    */   public int getWidth() {
/* 39 */     return this.width;
/*    */   }
/*    */   
/*    */   public int getHeight() {
/* 43 */     return this.height;
/*    */   }
/*    */   
/*    */   public boolean isHidden() {
/* 47 */     return this.hidden;
/*    */   }
/*    */   
/*    */   public boolean setHidden(boolean hidden) {
/* 51 */     return this.hidden = hidden;
/*    */   }
/*    */   
/*    */   public void setWidth(int width) {
/* 55 */     this.width = width;
/*    */   }
/*    */   
/*    */   public void setHeight(int height) {
/* 59 */     this.height = height;
/*    */   }
/*    */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\me\earth\phobos\features\gui\components\items\Item.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */