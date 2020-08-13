/*     */ package me.earth.phobos.features.gui.components.items.buttons;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import me.earth.phobos.Phobos;
/*     */ import me.earth.phobos.features.gui.PhobosGui;
/*     */ import me.earth.phobos.features.gui.components.items.Item;
/*     */ import me.earth.phobos.features.modules.Module;
/*     */ import me.earth.phobos.features.modules.client.ClickGui;
/*     */ import me.earth.phobos.features.setting.Setting;
/*     */ import net.minecraft.client.audio.ISound;
/*     */ import net.minecraft.client.audio.PositionedSoundRecord;
/*     */ import net.minecraft.init.SoundEvents;
/*     */ 
/*     */ public class ModuleButton
/*     */   extends Button
/*     */ {
/*     */   private final Module module;
/*  19 */   private List<Item> items = new ArrayList<>();
/*     */   private boolean subOpen;
/*     */   
/*     */   public ModuleButton(Module module) {
/*  23 */     super(module.getName());
/*  24 */     this.module = module;
/*  25 */     initSettings();
/*     */   }
/*     */   
/*     */   public void initSettings() {
/*  29 */     List<Item> newItems = new ArrayList<>();
/*  30 */     if (!this.module.getSettings().isEmpty()) {
/*  31 */       for (Setting setting : this.module.getSettings()) {
/*  32 */         if (setting.getValue() instanceof Boolean && !setting.getName().equals("Enabled")) {
/*  33 */           newItems.add(new BooleanButton(setting));
/*     */         }
/*     */         
/*  36 */         if (setting.getValue() instanceof me.earth.phobos.features.setting.Bind && !this.module.getName().equalsIgnoreCase("Hud")) {
/*  37 */           newItems.add(new BindButton(setting));
/*     */         }
/*     */         
/*  40 */         if (setting.getValue() instanceof String || setting.getValue() instanceof Character) {
/*  41 */           newItems.add(new StringButton(setting));
/*     */         }
/*     */         
/*  44 */         if (setting.isNumberSetting()) {
/*  45 */           if (setting.hasRestriction()) {
/*  46 */             newItems.add(new Slider(setting));
/*     */             continue;
/*     */           } 
/*  49 */           newItems.add(new UnlimitedSlider(setting));
/*     */         } 
/*     */         
/*  52 */         if (setting.isEnumSetting()) {
/*  53 */           newItems.add(new EnumButton(setting));
/*     */         }
/*     */       } 
/*     */     }
/*  57 */     this.items = newItems;
/*     */   }
/*     */ 
/*     */   
/*     */   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
/*  62 */     super.drawScreen(mouseX, mouseY, partialTicks);
/*  63 */     if (!this.items.isEmpty()) {
/*  64 */       Phobos.textManager.drawStringWithShadow(((ClickGui)Phobos.moduleManager.getModuleByClass(ClickGui.class)).getSettingByName("Buttons").getValueAsString(), this.x - 1.5F + this.width - 7.4F, this.y - 2.0F - PhobosGui.getClickGui().getTextOffset(), -1);
/*  65 */       if (this.subOpen) {
/*  66 */         float height = 1.0F;
/*  67 */         for (Item item : this.items) {
/*  68 */           if (!item.isHidden()) {
/*  69 */             height += 15.0F;
/*  70 */             item.setLocation(this.x + 1.0F, this.y + height);
/*  71 */             item.setHeight(15);
/*  72 */             item.setWidth(this.width - 9);
/*  73 */             item.drawScreen(mouseX, mouseY, partialTicks);
/*     */           } 
/*  75 */           item.update();
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
/*  83 */     super.mouseClicked(mouseX, mouseY, mouseButton);
/*  84 */     if (!this.items.isEmpty()) {
/*  85 */       if (mouseButton == 1 && isHovering(mouseX, mouseY)) {
/*  86 */         this.subOpen = !this.subOpen;
/*  87 */         mc.func_147118_V().func_147682_a((ISound)PositionedSoundRecord.func_184371_a(SoundEvents.field_187909_gi, 1.0F));
/*     */       } 
/*     */       
/*  90 */       if (this.subOpen) {
/*  91 */         for (Item item : this.items) {
/*  92 */           if (!item.isHidden()) {
/*  93 */             item.mouseClicked(mouseX, mouseY, mouseButton);
/*     */           }
/*     */         } 
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onKeyTyped(char typedChar, int keyCode) {
/* 102 */     super.onKeyTyped(typedChar, keyCode);
/* 103 */     if (!this.items.isEmpty() && this.subOpen) {
/* 104 */       for (Item item : this.items) {
/* 105 */         if (!item.isHidden()) {
/* 106 */           item.onKeyTyped(typedChar, keyCode);
/*     */         }
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public int getHeight() {
/* 114 */     if (this.subOpen) {
/* 115 */       int height = 14;
/* 116 */       for (Item item : this.items) {
/* 117 */         if (!item.isHidden()) {
/* 118 */           height += item.getHeight() + 1;
/*     */         }
/*     */       } 
/* 121 */       return height + 2;
/*     */     } 
/* 123 */     return 14;
/*     */   }
/*     */ 
/*     */   
/*     */   public Module getModule() {
/* 128 */     return this.module;
/*     */   }
/*     */   
/*     */   public void toggle() {
/* 132 */     this.module.toggle();
/*     */   }
/*     */   
/*     */   public boolean getState() {
/* 136 */     return this.module.isEnabled();
/*     */   }
/*     */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\me\earth\phobos\features\gui\components\items\buttons\ModuleButton.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */