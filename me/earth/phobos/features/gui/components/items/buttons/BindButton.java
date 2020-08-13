/*    */ package me.earth.phobos.features.gui.components.items.buttons;
/*    */ 
/*    */ import me.earth.phobos.Phobos;
/*    */ import me.earth.phobos.features.gui.PhobosGui;
/*    */ import me.earth.phobos.features.modules.client.ClickGui;
/*    */ import me.earth.phobos.features.setting.Bind;
/*    */ import me.earth.phobos.features.setting.Setting;
/*    */ import me.earth.phobos.util.RenderUtil;
/*    */ import net.minecraft.client.audio.ISound;
/*    */ import net.minecraft.client.audio.PositionedSoundRecord;
/*    */ import net.minecraft.init.SoundEvents;
/*    */ 
/*    */ public class BindButton
/*    */   extends Button {
/*    */   private Setting setting;
/*    */   public boolean isListening;
/*    */   
/*    */   public BindButton(Setting setting) {
/* 19 */     super(setting.getName());
/* 20 */     this.setting = setting;
/* 21 */     this.width = 15;
/*    */   }
/*    */ 
/*    */   
/*    */   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
/* 26 */     RenderUtil.drawRect(this.x, this.y, this.x + this.width + 7.4F, this.y + this.height - 0.5F, getState() ? (!isHovering(mouseX, mouseY) ? Phobos.colorManager.getColorWithAlpha(((Integer)((ClickGui)Phobos.moduleManager.getModuleByName("ClickGui")).hoverAlpha.getValue()).intValue()) : Phobos.colorManager.getColorWithAlpha(((Integer)((ClickGui)Phobos.moduleManager.getModuleByName("ClickGui")).alpha.getValue()).intValue())) : (!isHovering(mouseX, mouseY) ? 290805077 : -2007673515));
/* 27 */     if (this.isListening) {
/* 28 */       Phobos.textManager.drawStringWithShadow("Listening...", this.x + 2.3F, this.y - 1.7F - PhobosGui.getClickGui().getTextOffset(), getState() ? -1 : -5592406);
/*    */     } else {
/* 30 */       Phobos.textManager.drawStringWithShadow(this.setting.getName() + " " + "§7" + ((Bind)this.setting.getValue()).toString(), this.x + 2.3F, this.y - 1.7F - PhobosGui.getClickGui().getTextOffset(), getState() ? -1 : -5592406);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void update() {
/* 36 */     setHidden(!this.setting.isVisible());
/*    */   }
/*    */ 
/*    */   
/*    */   public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
/* 41 */     super.mouseClicked(mouseX, mouseY, mouseButton);
/* 42 */     if (isHovering(mouseX, mouseY)) {
/* 43 */       mc.func_147118_V().func_147682_a((ISound)PositionedSoundRecord.func_184371_a(SoundEvents.field_187909_gi, 1.0F));
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public void onKeyTyped(char typedChar, int keyCode) {
/* 49 */     if (this.isListening) {
/* 50 */       Bind bind = new Bind(keyCode);
/* 51 */       if (bind.toString().equalsIgnoreCase("Escape"))
/*    */         return; 
/* 53 */       if (bind.toString().equalsIgnoreCase("Delete")) {
/* 54 */         bind = new Bind(-1);
/*    */       }
/* 56 */       this.setting.setValue(bind);
/* 57 */       onMouseClick();
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public int getHeight() {
/* 63 */     return 14;
/*    */   }
/*    */   
/*    */   public void toggle() {
/* 67 */     this.isListening = !this.isListening;
/*    */   }
/*    */   
/*    */   public boolean getState() {
/* 71 */     return !this.isListening;
/*    */   }
/*    */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\me\earth\phobos\features\gui\components\items\buttons\BindButton.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */