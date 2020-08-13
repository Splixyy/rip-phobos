/*     */ package me.earth.phobos.features.gui.components.items.buttons;
/*     */ 
/*     */ import me.earth.phobos.Phobos;
/*     */ import me.earth.phobos.features.gui.PhobosGui;
/*     */ import me.earth.phobos.features.modules.client.ClickGui;
/*     */ import me.earth.phobos.features.setting.Setting;
/*     */ import me.earth.phobos.util.RenderUtil;
/*     */ import net.minecraft.client.audio.ISound;
/*     */ import net.minecraft.client.audio.PositionedSoundRecord;
/*     */ import net.minecraft.init.SoundEvents;
/*     */ import net.minecraft.util.ChatAllowedCharacters;
/*     */ 
/*     */ public class StringButton
/*     */   extends Button {
/*     */   private Setting setting;
/*     */   public boolean isListening;
/*  17 */   private CurrentString currentString = new CurrentString("");
/*     */   
/*     */   public StringButton(Setting setting) {
/*  20 */     super(setting.getName());
/*  21 */     this.setting = setting;
/*  22 */     this.width = 15;
/*     */   }
/*     */ 
/*     */   
/*     */   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
/*  27 */     RenderUtil.drawRect(this.x, this.y, this.x + this.width + 7.4F, this.y + this.height - 0.5F, getState() ? (!isHovering(mouseX, mouseY) ? Phobos.colorManager.getColorWithAlpha(((Integer)((ClickGui)Phobos.moduleManager.getModuleByClass(ClickGui.class)).hoverAlpha.getValue()).intValue()) : Phobos.colorManager.getColorWithAlpha(((Integer)((ClickGui)Phobos.moduleManager.getModuleByClass(ClickGui.class)).alpha.getValue()).intValue())) : (!isHovering(mouseX, mouseY) ? 290805077 : -2007673515));
/*  28 */     if (this.isListening) {
/*  29 */       Phobos.textManager.drawStringWithShadow(this.currentString.getString() + Phobos.textManager.getIdleSign(), this.x + 2.3F, this.y - 1.7F - PhobosGui.getClickGui().getTextOffset(), getState() ? -1 : -5592406);
/*     */     } else {
/*  31 */       Phobos.textManager.drawStringWithShadow((this.setting.getName().equals("Buttons") ? "Buttons " : (this.setting.getName().equals("Prefix") ? "Prefix  §7" : "")) + this.setting.getValue(), this.x + 2.3F, this.y - 1.7F - PhobosGui.getClickGui().getTextOffset(), getState() ? -1 : -5592406);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
/*  37 */     super.mouseClicked(mouseX, mouseY, mouseButton);
/*  38 */     if (isHovering(mouseX, mouseY)) {
/*  39 */       mc.func_147118_V().func_147682_a((ISound)PositionedSoundRecord.func_184371_a(SoundEvents.field_187909_gi, 1.0F));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void onKeyTyped(char typedChar, int keyCode) {
/*  46 */     if (this.isListening) {
/*  47 */       switch (keyCode) {
/*     */         case 1:
/*     */           return;
/*     */         case 28:
/*  51 */           enterString();
/*     */         
/*     */         case 14:
/*  54 */           setString(removeLastChar(this.currentString.getString()));
/*     */       } 
/*     */       
/*  57 */       if (ChatAllowedCharacters.func_71566_a(typedChar)) {
/*  58 */         setString(this.currentString.getString() + typedChar);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void update() {
/*  66 */     setHidden(!this.setting.isVisible());
/*     */   }
/*     */   
/*     */   private void enterString() {
/*  70 */     if (this.currentString.getString().isEmpty()) {
/*  71 */       this.setting.setValue(this.setting.getDefaultValue());
/*     */     } else {
/*  73 */       this.setting.setValue(this.currentString.getString());
/*     */     } 
/*  75 */     setString("");
/*  76 */     onMouseClick();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getHeight() {
/*  81 */     return 14;
/*     */   }
/*     */   
/*     */   public void toggle() {
/*  85 */     this.isListening = !this.isListening;
/*     */   }
/*     */   
/*     */   public boolean getState() {
/*  89 */     return !this.isListening;
/*     */   }
/*     */   
/*     */   public void setString(String newString) {
/*  93 */     this.currentString = new CurrentString(newString);
/*     */   }
/*     */   
/*     */   public static String removeLastChar(String str) {
/*  97 */     String output = "";
/*  98 */     if (str != null && str.length() > 0) {
/*  99 */       output = str.substring(0, str.length() - 1);
/*     */     }
/* 101 */     return output;
/*     */   }
/*     */   
/*     */   public static class CurrentString
/*     */   {
/*     */     private String string;
/*     */     
/*     */     public CurrentString(String string) {
/* 109 */       this.string = string;
/*     */     }
/*     */     
/*     */     public String getString() {
/* 113 */       return this.string;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\me\earth\phobos\features\gui\components\items\buttons\StringButton.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */