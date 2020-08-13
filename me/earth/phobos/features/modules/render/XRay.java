/*    */ package me.earth.phobos.features.modules.render;
/*    */ 
/*    */ import me.earth.phobos.event.events.ClientEvent;
/*    */ import me.earth.phobos.features.command.Command;
/*    */ import me.earth.phobos.features.modules.Module;
/*    */ import me.earth.phobos.features.setting.Setting;
/*    */ import net.minecraft.block.Block;
/*    */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*    */ 
/*    */ public class XRay
/*    */   extends Module
/*    */ {
/* 13 */   public Setting<String> newBlock = register(new Setting("NewBlock", "Add Block..."));
/*    */   
/* 15 */   public Setting<Boolean> showBlocks = register(new Setting("ShowBlocks", Boolean.valueOf(false)));
/*    */   
/* 17 */   private static XRay INSTANCE = new XRay();
/*    */   
/*    */   public XRay() {
/* 20 */     super("XRay", "Lets you look through walls.", Module.Category.RENDER, false, false, true);
/* 21 */     setInstance();
/*    */   }
/*    */   
/*    */   private void setInstance() {
/* 25 */     INSTANCE = this;
/*    */   }
/*    */   
/*    */   public static XRay getInstance() {
/* 29 */     if (INSTANCE == null) {
/* 30 */       INSTANCE = new XRay();
/*    */     }
/* 32 */     return INSTANCE;
/*    */   }
/*    */ 
/*    */   
/*    */   public void onEnable() {
/* 37 */     mc.field_71438_f.func_72712_a();
/*    */   }
/*    */ 
/*    */   
/*    */   public void onDisable() {
/* 42 */     mc.field_71438_f.func_72712_a();
/*    */   }
/*    */   
/*    */   @SubscribeEvent
/*    */   public void onSettingChange(ClientEvent event) {
/* 47 */     if (event.getStage() == 2 && 
/* 48 */       event.getSetting() != null && event.getSetting().getFeature() != null && event.getSetting().getFeature().equals(this)) {
/* 49 */       if (event.getSetting().equals(this.newBlock) && !shouldRender((String)this.newBlock.getPlannedValue())) {
/* 50 */         register(new Setting((String)this.newBlock.getPlannedValue(), Boolean.valueOf(true), v -> ((Boolean)this.showBlocks.getValue()).booleanValue()));
/* 51 */         Command.sendMessage("<Xray> Added new Block: " + (String)this.newBlock.getPlannedValue());
/* 52 */         if (isOn()) {
/* 53 */           mc.field_71438_f.func_72712_a();
/*    */         }
/* 55 */         event.setCanceled(true);
/*    */       } else {
/* 57 */         Setting setting = event.getSetting();
/* 58 */         if (setting.equals(this.enabled) || setting.equals(this.drawn) || setting.equals(this.bind) || setting.equals(this.newBlock) || setting.equals(this.showBlocks)) {
/*    */           return;
/*    */         }
/*    */         
/* 62 */         if (setting.getValue() instanceof Boolean && 
/* 63 */           !((Boolean)setting.getPlannedValue()).booleanValue()) {
/* 64 */           unregister(setting);
/* 65 */           if (isOn()) {
/* 66 */             mc.field_71438_f.func_72712_a();
/*    */           }
/* 68 */           event.setCanceled(true);
/*    */         } 
/*    */       } 
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean shouldRender(Block block) {
/* 77 */     return shouldRender(block.func_149732_F());
/*    */   }
/*    */   
/*    */   public boolean shouldRender(String name) {
/* 81 */     for (Setting setting : getSettings()) {
/* 82 */       if (name.equalsIgnoreCase(setting.getName())) {
/* 83 */         return true;
/*    */       }
/*    */     } 
/* 86 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\me\earth\phobos\features\modules\render\XRay.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */