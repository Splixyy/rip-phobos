/*    */ package me.earth.phobos.features.gui.components.items.buttons;
/*    */ 
/*    */ import me.earth.phobos.Phobos;
/*    */ import me.earth.phobos.features.gui.PhobosGui;
/*    */ import me.earth.phobos.features.gui.components.Component;
/*    */ import me.earth.phobos.features.gui.components.items.Item;
/*    */ import me.earth.phobos.features.modules.client.ClickGui;
/*    */ import me.earth.phobos.util.RenderUtil;
/*    */ import net.minecraft.client.audio.ISound;
/*    */ import net.minecraft.client.audio.PositionedSoundRecord;
/*    */ import net.minecraft.init.SoundEvents;
/*    */ 
/*    */ public class Button extends Item {
/*    */   private boolean state;
/*    */   
/*    */   public Button(String name) {
/* 17 */     super(name);
/* 18 */     this.height = 15;
/*    */   }
/*    */ 
/*    */   
/*    */   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
/* 23 */     RenderUtil.drawRect(this.x, this.y, this.x + this.width, this.y + this.height - 0.5F, getState() ? (!isHovering(mouseX, mouseY) ? Phobos.colorManager.getColorWithAlpha(((Integer)((ClickGui)Phobos.moduleManager.getModuleByClass(ClickGui.class)).hoverAlpha.getValue()).intValue()) : Phobos.colorManager.getColorWithAlpha(((Integer)((ClickGui)Phobos.moduleManager.getModuleByClass(ClickGui.class)).alpha.getValue()).intValue())) : (!isHovering(mouseX, mouseY) ? 290805077 : -2007673515));
/* 24 */     Phobos.textManager.drawStringWithShadow(getName(), this.x + 2.3F, this.y - 2.0F - PhobosGui.getClickGui().getTextOffset(), getState() ? -1 : -5592406);
/*    */   }
/*    */ 
/*    */   
/*    */   public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
/* 29 */     if (mouseButton == 0 && isHovering(mouseX, mouseY)) {
/* 30 */       onMouseClick();
/*    */     }
/*    */   }
/*    */   
/*    */   public void onMouseClick() {
/* 35 */     this.state = !this.state;
/* 36 */     toggle();
/* 37 */     mc.func_147118_V().func_147682_a((ISound)PositionedSoundRecord.func_184371_a(SoundEvents.field_187909_gi, 1.0F));
/*    */   }
/*    */   
/*    */   public void toggle() {}
/*    */   
/*    */   public boolean getState() {
/* 43 */     return this.state;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getHeight() {
/* 48 */     return 14;
/*    */   }
/*    */   
/*    */   public boolean isHovering(int mouseX, int mouseY) {
/* 52 */     for (Component component : PhobosGui.getClickGui().getComponents()) {
/* 53 */       if (component.drag) {
/* 54 */         return false;
/*    */       }
/*    */     } 
/* 57 */     return (mouseX >= getX() && mouseX <= getX() + getWidth() && mouseY >= getY() && mouseY <= getY() + this.height);
/*    */   }
/*    */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\me\earth\phobos\features\gui\components\items\buttons\Button.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */