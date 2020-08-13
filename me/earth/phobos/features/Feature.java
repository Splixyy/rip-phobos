/*    */ package me.earth.phobos.features;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import me.earth.phobos.Phobos;
/*    */ import me.earth.phobos.features.gui.PhobosGui;
/*    */ import me.earth.phobos.features.modules.Module;
/*    */ import me.earth.phobos.features.setting.Setting;
/*    */ import me.earth.phobos.manager.TextManager;
/*    */ import me.earth.phobos.util.Util;
/*    */ 
/*    */ public class Feature
/*    */   implements Util
/*    */ {
/* 15 */   public List<Setting> settings = new ArrayList<>(); private String name;
/* 16 */   public TextManager renderer = Phobos.textManager;
/*    */   
/*    */   public Feature() {}
/*    */   
/*    */   public Feature(String name) {
/* 21 */     this.name = name;
/*    */   }
/*    */   public static boolean nullCheck() {
/* 24 */     return (mc.field_71439_g == null);
/*    */   }
/*    */   
/*    */   public static boolean fullNullCheck() {
/* 28 */     return (mc.field_71439_g == null || mc.field_71441_e == null);
/*    */   }
/*    */   
/*    */   public String getName() {
/* 32 */     return this.name;
/*    */   }
/*    */   
/*    */   public List<Setting> getSettings() {
/* 36 */     return this.settings;
/*    */   }
/*    */   
/*    */   public boolean hasSettings() {
/* 40 */     return !this.settings.isEmpty();
/*    */   }
/*    */   
/*    */   public boolean isEnabled() {
/* 44 */     if (this instanceof Module) {
/* 45 */       return ((Module)this).isOn();
/*    */     }
/* 47 */     return false;
/*    */   }
/*    */   
/*    */   public boolean isDisabled() {
/* 51 */     return !isEnabled();
/*    */   }
/*    */   
/*    */   public Setting register(Setting setting) {
/* 55 */     setting.setFeature(this);
/* 56 */     this.settings.add(setting);
/* 57 */     if (this instanceof Module && mc.field_71462_r instanceof PhobosGui) {
/* 58 */       PhobosGui.getInstance().updateModule((Module)this);
/*    */     }
/* 60 */     return setting;
/*    */   }
/*    */   
/*    */   public void unregister(Setting settingIn) {
/* 64 */     List<Setting> removeList = new ArrayList<>();
/* 65 */     for (Setting setting : this.settings) {
/* 66 */       if (setting.equals(settingIn)) {
/* 67 */         removeList.add(setting);
/*    */       }
/*    */     } 
/*    */     
/* 71 */     if (!removeList.isEmpty()) {
/* 72 */       this.settings.removeAll(removeList);
/*    */     }
/*    */     
/* 75 */     if (this instanceof Module && mc.field_71462_r instanceof PhobosGui) {
/* 76 */       PhobosGui.getInstance().updateModule((Module)this);
/*    */     }
/*    */   }
/*    */   
/*    */   public Setting getSettingByName(String name) {
/* 81 */     for (Setting setting : this.settings) {
/* 82 */       if (setting.getName().equalsIgnoreCase(name)) {
/* 83 */         return setting;
/*    */       }
/*    */     } 
/* 86 */     return null;
/*    */   }
/*    */   
/*    */   public void reset() {
/* 90 */     for (Setting setting : this.settings) {
/* 91 */       setting.setValue(setting.getDefaultValue());
/*    */     }
/*    */   }
/*    */   
/*    */   public void clearSettings() {
/* 96 */     this.settings = new ArrayList<>();
/*    */   }
/*    */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\me\earth\phobos\features\Feature.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */