/*    */ package me.earth.phobos.features.gui.components.items.buttons;
/*    */ 
/*    */ import me.earth.phobos.Phobos;
/*    */ import me.earth.phobos.features.gui.PhobosGui;
/*    */ import me.earth.phobos.features.modules.client.ClickGui;
/*    */ import me.earth.phobos.features.setting.Setting;
/*    */ import me.earth.phobos.util.RenderUtil;
/*    */ import net.minecraft.client.audio.ISound;
/*    */ import net.minecraft.client.audio.PositionedSoundRecord;
/*    */ import net.minecraft.init.SoundEvents;
/*    */ 
/*    */ public class UnlimitedSlider
/*    */   extends Button {
/*    */   public Setting setting;
/*    */   
/*    */   public UnlimitedSlider(Setting setting) {
/* 17 */     super(setting.getName());
/* 18 */     this.setting = setting;
/* 19 */     this.width = 15;
/*    */   }
/*    */ 
/*    */   
/*    */   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
/* 24 */     RenderUtil.drawRect(this.x, this.y, this.x + this.width + 7.4F, this.y + this.height - 0.5F, !isHovering(mouseX, mouseY) ? Phobos.colorManager.getColorWithAlpha(((Integer)((ClickGui)Phobos.moduleManager.getModuleByClass(ClickGui.class)).hoverAlpha.getValue()).intValue()) : Phobos.colorManager.getColorWithAlpha(((Integer)((ClickGui)Phobos.moduleManager.getModuleByClass(ClickGui.class)).alpha.getValue()).intValue()));
/* 25 */     Phobos.textManager.drawStringWithShadow(" - " + this.setting.getName() + " " + "§7" + this.setting.getValue() + "§r" + " +", this.x + 2.3F, this.y - 1.7F - PhobosGui.getClickGui().getTextOffset(), getState() ? -1 : -5592406);
/*    */   }
/*    */ 
/*    */   
/*    */   public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
/* 30 */     super.mouseClicked(mouseX, mouseY, mouseButton);
/* 31 */     if (isHovering(mouseX, mouseY)) {
/* 32 */       mc.func_147118_V().func_147682_a((ISound)PositionedSoundRecord.func_184371_a(SoundEvents.field_187909_gi, 1.0F));
/* 33 */       if (isRight(mouseX)) {
/* 34 */         if (this.setting.getValue() instanceof Double) {
/* 35 */           this.setting.setValue(Double.valueOf(((Double)this.setting.getValue()).doubleValue() + 1.0D));
/* 36 */         } else if (this.setting.getValue() instanceof Float) {
/* 37 */           this.setting.setValue(Float.valueOf(((Float)this.setting.getValue()).floatValue() + 1.0F));
/* 38 */         } else if (this.setting.getValue() instanceof Integer) {
/* 39 */           this.setting.setValue(Integer.valueOf(((Integer)this.setting.getValue()).intValue() + 1));
/*    */         }
/*    */       
/* 42 */       } else if (this.setting.getValue() instanceof Double) {
/* 43 */         this.setting.setValue(Double.valueOf(((Double)this.setting.getValue()).doubleValue() - 1.0D));
/* 44 */       } else if (this.setting.getValue() instanceof Float) {
/* 45 */         this.setting.setValue(Float.valueOf(((Float)this.setting.getValue()).floatValue() - 1.0F));
/* 46 */       } else if (this.setting.getValue() instanceof Integer) {
/* 47 */         this.setting.setValue(Integer.valueOf(((Integer)this.setting.getValue()).intValue() - 1));
/*    */       } 
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void update() {
/* 55 */     setHidden(!this.setting.isVisible());
/*    */   }
/*    */ 
/*    */   
/*    */   public int getHeight() {
/* 60 */     return 14;
/*    */   }
/*    */   
/*    */   public void toggle() {}
/*    */   
/*    */   public boolean getState() {
/* 66 */     return true;
/*    */   }
/*    */   
/*    */   public boolean isRight(int x) {
/* 70 */     return (x > this.x + (this.width + 7.4F) / 2.0F);
/*    */   }
/*    */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\me\earth\phobos\features\gui\components\items\buttons\UnlimitedSlider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */