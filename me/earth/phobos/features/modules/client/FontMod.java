/*    */ package me.earth.phobos.features.modules.client;
/*    */ 
/*    */ import java.awt.GraphicsEnvironment;
/*    */ import me.earth.phobos.Phobos;
/*    */ import me.earth.phobos.event.events.ClientEvent;
/*    */ import me.earth.phobos.features.command.Command;
/*    */ import me.earth.phobos.features.modules.Module;
/*    */ import me.earth.phobos.features.setting.Setting;
/*    */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class FontMod
/*    */   extends Module
/*    */ {
/*    */   private boolean reloadFont = false;
/* 17 */   public Setting<String> fontName = register(new Setting("FontName", "Arial", "Name of the font."));
/* 18 */   public Setting<Integer> fontSize = register(new Setting("FontSize", Integer.valueOf(18), "Size of the font."));
/* 19 */   public Setting<Integer> fontStyle = register(new Setting("FontStyle", Integer.valueOf(0), "Style of the font."));
/* 20 */   public Setting<Boolean> antiAlias = register(new Setting("AntiAlias", Boolean.valueOf(true), "Smoother font."));
/* 21 */   public Setting<Boolean> fractionalMetrics = register(new Setting("Metrics", Boolean.valueOf(true), "Thinner font."));
/* 22 */   public Setting<Boolean> shadow = register(new Setting("Shadow", Boolean.valueOf(true), "Less shadow offset font."));
/* 23 */   public Setting<Boolean> showFonts = register(new Setting("Fonts", Boolean.valueOf(false), "Shows all fonts."));
/*    */   
/* 25 */   private static FontMod INSTANCE = new FontMod();
/*    */   
/*    */   public FontMod() {
/* 28 */     super("CustomFont", "CustomFont for all of the clients text. Use the font command.", Module.Category.CLIENT, true, false, false);
/* 29 */     setInstance();
/*    */   }
/*    */   
/*    */   private void setInstance() {
/* 33 */     INSTANCE = this;
/*    */   }
/*    */   
/*    */   public static FontMod getInstance() {
/* 37 */     if (INSTANCE == null) {
/* 38 */       INSTANCE = new FontMod();
/*    */     }
/* 40 */     return INSTANCE;
/*    */   }
/*    */   
/*    */   @SubscribeEvent
/*    */   public void onSettingChange(ClientEvent event) {
/* 45 */     if (event.getStage() == 2) {
/* 46 */       Setting setting = event.getSetting();
/* 47 */       if (setting != null && setting.getFeature().equals(this)) {
/* 48 */         if (setting.getName().equals("FontName") && 
/* 49 */           !checkFont(setting.getPlannedValue().toString(), false)) {
/* 50 */           Command.sendMessage("§cThat font doesnt exist.");
/* 51 */           event.setCanceled(true);
/*    */           
/*    */           return;
/*    */         } 
/* 55 */         this.reloadFont = true;
/*    */       } 
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void onTick() {
/* 62 */     if (((Boolean)this.showFonts.getValue()).booleanValue()) {
/* 63 */       checkFont("Hello", true);
/* 64 */       Command.sendMessage("Current Font: " + (String)this.fontName.getValue());
/* 65 */       this.showFonts.setValue(Boolean.valueOf(false));
/*    */     } 
/*    */     
/* 68 */     if (this.reloadFont) {
/* 69 */       Phobos.textManager.init(false);
/* 70 */       this.reloadFont = false;
/*    */     } 
/*    */   }
/*    */   
/*    */   public static boolean checkFont(String font, boolean message) {
/* 75 */     String[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
/* 76 */     for (String s : fonts) {
/* 77 */       if (!message && s.equals(font))
/* 78 */         return true; 
/* 79 */       if (message) {
/* 80 */         Command.sendMessage(s);
/*    */       }
/*    */     } 
/* 83 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\me\earth\phobos\features\modules\client\FontMod.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */