/*     */ package me.earth.phobos.features.gui;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import me.earth.phobos.Phobos;
/*     */ import me.earth.phobos.features.gui.components.Component;
/*     */ import me.earth.phobos.features.gui.components.items.Item;
/*     */ import me.earth.phobos.features.gui.components.items.buttons.Button;
/*     */ import me.earth.phobos.features.gui.components.items.buttons.ModuleButton;
/*     */ import me.earth.phobos.features.modules.Module;
/*     */ import net.minecraft.client.gui.GuiScreen;
/*     */ import org.lwjgl.input.Mouse;
/*     */ 
/*     */ public class PhobosGui
/*     */   extends GuiScreen {
/*     */   private static PhobosGui phobosGui;
/*  17 */   private final ArrayList<Component> components = new ArrayList<>();
/*  18 */   private static PhobosGui INSTANCE = new PhobosGui();
/*     */   
/*     */   public PhobosGui() {
/*  21 */     setInstance();
/*  22 */     load();
/*     */   }
/*     */   
/*     */   public static PhobosGui getInstance() {
/*  26 */     if (INSTANCE == null) {
/*  27 */       INSTANCE = new PhobosGui();
/*     */     }
/*  29 */     return INSTANCE;
/*     */   }
/*     */   
/*     */   private void setInstance() {
/*  33 */     INSTANCE = this;
/*     */   }
/*     */   
/*     */   public static PhobosGui getClickGui() {
/*  37 */     return getInstance();
/*     */   }
/*     */   
/*     */   private void load() {
/*  41 */     int x = -84;
/*  42 */     for (Module.Category category : Phobos.moduleManager.getCategories()) {
/*  43 */       x += 90; this.components.add(new Component(category.getName(), x, 4, true)
/*     */           {
/*     */             public void setupItems() {
/*  46 */               Phobos.moduleManager.getModulesByCategory(category).forEach(module -> {
/*     */                     if (!module.hidden) {
/*     */                       addButton((Button)new ModuleButton(module));
/*     */                     }
/*     */                   });
/*     */             }
/*     */           });
/*     */     } 
/*  54 */     this.components.forEach(components -> components.getItems().sort(()));
/*     */   }
/*     */   
/*     */   public void updateModule(Module module) {
/*  58 */     for (Component component : this.components) {
/*  59 */       for (Item item : component.getItems()) {
/*  60 */         if (item instanceof ModuleButton) {
/*  61 */           ModuleButton button = (ModuleButton)item;
/*  62 */           Module mod = button.getModule();
/*  63 */           if (module != null && module.equals(mod)) {
/*  64 */             button.initSettings();
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void func_73863_a(int mouseX, int mouseY, float partialTicks) {
/*  74 */     checkMouseWheel();
/*  75 */     func_146276_q_();
/*  76 */     this.components.forEach(components -> components.drawScreen(mouseX, mouseY, partialTicks));
/*     */   }
/*     */ 
/*     */   
/*     */   public void func_73864_a(int mouseX, int mouseY, int clickedButton) {
/*  81 */     this.components.forEach(components -> components.mouseClicked(mouseX, mouseY, clickedButton));
/*     */   }
/*     */ 
/*     */   
/*     */   public void func_146286_b(int mouseX, int mouseY, int releaseButton) {
/*  86 */     this.components.forEach(components -> components.mouseReleased(mouseX, mouseY, releaseButton));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean func_73868_f() {
/*  91 */     return false;
/*     */   }
/*     */   
/*     */   public final ArrayList<Component> getComponents() {
/*  95 */     return this.components;
/*     */   }
/*     */   
/*     */   public void checkMouseWheel() {
/*  99 */     int dWheel = Mouse.getDWheel();
/* 100 */     if (dWheel < 0) {
/* 101 */       this.components.forEach(component -> component.setY(component.getY() - 10));
/* 102 */     } else if (dWheel > 0) {
/* 103 */       this.components.forEach(component -> component.setY(component.getY() + 10));
/*     */     } 
/*     */   }
/*     */   
/*     */   public int getTextOffset() {
/* 108 */     return -6;
/*     */   }
/*     */   
/*     */   public Component getComponentByName(String name) {
/* 112 */     for (Component component : this.components) {
/* 113 */       if (component.getName().equalsIgnoreCase(name)) {
/* 114 */         return component;
/*     */       }
/*     */     } 
/* 117 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void func_73869_a(char typedChar, int keyCode) throws IOException {
/* 122 */     super.func_73869_a(typedChar, keyCode);
/* 123 */     this.components.forEach(component -> component.onKeyTyped(typedChar, keyCode));
/*     */   }
/*     */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\me\earth\phobos\features\gui\PhobosGui.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */