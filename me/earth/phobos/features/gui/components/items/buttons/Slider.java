/*    */ package me.earth.phobos.features.gui.components.items.buttons;
/*    */ 
/*    */ import me.earth.phobos.Phobos;
/*    */ import me.earth.phobos.features.gui.PhobosGui;
/*    */ import me.earth.phobos.features.gui.components.Component;
/*    */ import me.earth.phobos.features.modules.client.ClickGui;
/*    */ import me.earth.phobos.features.setting.Setting;
/*    */ import me.earth.phobos.util.RenderUtil;
/*    */ import org.lwjgl.input.Mouse;
/*    */ 
/*    */ public class Slider
/*    */   extends Button
/*    */ {
/*    */   public Setting setting;
/*    */   private Number min;
/*    */   private Number max;
/*    */   private int difference;
/*    */   
/*    */   public Slider(Setting setting) {
/* 20 */     super(setting.getName());
/* 21 */     this.setting = setting;
/* 22 */     this.min = (Number)setting.getMin();
/* 23 */     this.max = (Number)setting.getMax();
/* 24 */     this.difference = this.max.intValue() - this.min.intValue();
/* 25 */     this.width = 15;
/*    */   }
/*    */ 
/*    */   
/*    */   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
/* 30 */     dragSetting(mouseX, mouseY);
/* 31 */     RenderUtil.drawRect(this.x, this.y, this.x + this.width + 7.4F, this.y + this.height - 0.5F, !isHovering(mouseX, mouseY) ? 290805077 : -2007673515);
/* 32 */     RenderUtil.drawRect(this.x, this.y, (((Number)this.setting.getValue()).floatValue() <= this.min.floatValue()) ? this.x : (this.x + (this.width + 7.4F) * partialMultiplier()), this.y + this.height - 0.5F, !isHovering(mouseX, mouseY) ? Phobos.colorManager.getColorWithAlpha(((Integer)((ClickGui)Phobos.moduleManager.getModuleByClass(ClickGui.class)).hoverAlpha.getValue()).intValue()) : Phobos.colorManager.getColorWithAlpha(((Integer)((ClickGui)Phobos.moduleManager.getModuleByClass(ClickGui.class)).alpha.getValue()).intValue()));
/* 33 */     Phobos.textManager.drawStringWithShadow(getName() + " " + "§7" + ((this.setting.getValue() instanceof Float) ? (String)this.setting.getValue() : (String)Double.valueOf(((Number)this.setting.getValue()).doubleValue())), this.x + 2.3F, this.y - 1.7F - PhobosGui.getClickGui().getTextOffset(), -1);
/*    */   }
/*    */ 
/*    */   
/*    */   public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
/* 38 */     super.mouseClicked(mouseX, mouseY, mouseButton);
/* 39 */     if (isHovering(mouseX, mouseY)) {
/* 40 */       setSettingFromX(mouseX);
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isHovering(int mouseX, int mouseY) {
/* 46 */     for (Component component : PhobosGui.getClickGui().getComponents()) {
/* 47 */       if (component.drag) {
/* 48 */         return false;
/*    */       }
/*    */     } 
/* 51 */     return (mouseX >= getX() && mouseX <= getX() + getWidth() + 8.0F && mouseY >= getY() && mouseY <= getY() + this.height);
/*    */   }
/*    */ 
/*    */   
/*    */   public void update() {
/* 56 */     setHidden(!this.setting.isVisible());
/*    */   }
/*    */   
/*    */   private void dragSetting(int mouseX, int mouseY) {
/* 60 */     if (isHovering(mouseX, mouseY) && Mouse.isButtonDown(0)) {
/* 61 */       setSettingFromX(mouseX);
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public int getHeight() {
/* 67 */     return 14;
/*    */   }
/*    */   
/*    */   private void setSettingFromX(int mouseX) {
/* 71 */     float percent = (mouseX - this.x) / (this.width + 7.4F);
/* 72 */     if (this.setting.getValue() instanceof Double) {
/* 73 */       double result = ((Double)this.setting.getMin()).doubleValue() + (this.difference * percent);
/* 74 */       this.setting.setValue(Double.valueOf(Math.round(10.0D * result) / 10.0D));
/* 75 */     } else if (this.setting.getValue() instanceof Float) {
/* 76 */       float result = ((Float)this.setting.getMin()).floatValue() + this.difference * percent;
/* 77 */       this.setting.setValue(Float.valueOf(Math.round(10.0F * result) / 10.0F));
/* 78 */     } else if (this.setting.getValue() instanceof Integer) {
/* 79 */       this.setting.setValue(Integer.valueOf(((Integer)this.setting.getMin()).intValue() + (int)(this.difference * percent)));
/*    */     } 
/*    */   }
/*    */   
/*    */   private float middle() {
/* 84 */     return this.max.floatValue() - this.min.floatValue();
/*    */   }
/*    */   
/*    */   private float part() {
/* 88 */     return ((Number)this.setting.getValue()).floatValue() - this.min.floatValue();
/*    */   }
/*    */   
/*    */   private float partialMultiplier() {
/* 92 */     return part() / middle();
/*    */   }
/*    */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\me\earth\phobos\features\gui\components\items\buttons\Slider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */