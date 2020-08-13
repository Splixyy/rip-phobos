/*    */ package me.earth.phobos.features.modules.client;
/*    */ 
/*    */ import me.earth.phobos.Phobos;
/*    */ import me.earth.phobos.event.events.ClientEvent;
/*    */ import me.earth.phobos.features.command.Command;
/*    */ import me.earth.phobos.features.gui.PhobosGui;
/*    */ import me.earth.phobos.features.modules.Module;
/*    */ import me.earth.phobos.features.setting.Setting;
/*    */ import net.minecraft.client.gui.GuiScreen;
/*    */ import net.minecraft.client.settings.GameSettings;
/*    */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*    */ 
/*    */ public class ClickGui
/*    */   extends Module {
/* 15 */   public Setting<String> prefix = register(new Setting("Prefix", "."));
/* 16 */   public Setting<Integer> red = register(new Setting("Red", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255)));
/* 17 */   public Setting<Integer> green = register(new Setting("Green", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(255)));
/* 18 */   public Setting<Integer> blue = register(new Setting("Blue", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(255)));
/* 19 */   public Setting<Integer> hoverAlpha = register(new Setting("Alpha", Integer.valueOf(180), Integer.valueOf(0), Integer.valueOf(255)));
/* 20 */   public Setting<Integer> alpha = register(new Setting("HoverAlpha", Integer.valueOf(240), Integer.valueOf(0), Integer.valueOf(255)));
/* 21 */   public Setting<Boolean> customFov = register(new Setting("CustomFov", Boolean.valueOf(false)));
/* 22 */   public Setting<Float> fov = register(new Setting("Fov", Float.valueOf(150.0F), Float.valueOf(-180.0F), Float.valueOf(180.0F), v -> ((Boolean)this.customFov.getValue()).booleanValue()));
/* 23 */   public Setting<String> moduleButton = register(new Setting("Buttons", ""));
/* 24 */   public Setting<Boolean> devSettings = register(new Setting("DevSettings", Boolean.valueOf(false)));
/* 25 */   public Setting<Integer> topRed = register(new Setting("TopRed", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> ((Boolean)this.devSettings.getValue()).booleanValue()));
/* 26 */   public Setting<Integer> topGreen = register(new Setting("TopGreen", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(255), v -> ((Boolean)this.devSettings.getValue()).booleanValue()));
/* 27 */   public Setting<Integer> topBlue = register(new Setting("TopBlue", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(255), v -> ((Boolean)this.devSettings.getValue()).booleanValue()));
/* 28 */   public Setting<Integer> topAlpha = register(new Setting("TopAlpha", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> ((Boolean)this.devSettings.getValue()).booleanValue()));
/*    */   
/* 30 */   private static ClickGui INSTANCE = new ClickGui();
/*    */   
/*    */   public ClickGui() {
/* 33 */     super("ClickGui", "Opens the ClickGui", Module.Category.CLIENT, true, false, false);
/* 34 */     setInstance();
/*    */   }
/*    */   
/*    */   private void setInstance() {
/* 38 */     INSTANCE = this;
/*    */   }
/*    */   
/*    */   public static ClickGui getInstance() {
/* 42 */     if (INSTANCE == null) {
/* 43 */       INSTANCE = new ClickGui();
/*    */     }
/* 45 */     return INSTANCE;
/*    */   }
/*    */ 
/*    */   
/*    */   public void onUpdate() {
/* 50 */     if (((Boolean)this.customFov.getValue()).booleanValue()) {
/* 51 */       mc.field_71474_y.func_74304_a(GameSettings.Options.FOV, ((Float)this.fov.getValue()).floatValue());
/*    */     }
/*    */   }
/*    */   
/*    */   @SubscribeEvent
/*    */   public void onSettingChange(ClientEvent event) {
/* 57 */     if (event.getStage() == 2 && 
/* 58 */       event.getSetting().getFeature().equals(this)) {
/* 59 */       if (event.getSetting().equals(this.prefix)) {
/* 60 */         Phobos.commandManager.setPrefix((String)this.prefix.getPlannedValue());
/* 61 */         Command.sendMessage("Prefix set to §a" + Phobos.commandManager.getPrefix());
/*    */       } 
/* 63 */       Phobos.colorManager.setColor(((Integer)this.red.getPlannedValue()).intValue(), ((Integer)this.green.getPlannedValue()).intValue(), ((Integer)this.blue.getPlannedValue()).intValue(), ((Integer)this.hoverAlpha.getPlannedValue()).intValue());
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void onEnable() {
/* 70 */     mc.func_147108_a((GuiScreen)new PhobosGui());
/*    */   }
/*    */ 
/*    */   
/*    */   public void onLoad() {
/* 75 */     Phobos.colorManager.setColor(((Integer)this.red.getValue()).intValue(), ((Integer)this.green.getValue()).intValue(), ((Integer)this.blue.getValue()).intValue(), ((Integer)this.hoverAlpha.getValue()).intValue());
/* 76 */     Phobos.commandManager.setPrefix((String)this.prefix.getValue());
/*    */   }
/*    */ 
/*    */   
/*    */   public void onTick() {
/* 81 */     if (!(mc.field_71462_r instanceof PhobosGui)) {
/* 82 */       disable();
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public void onDisable() {
/* 88 */     if (mc.field_71462_r instanceof PhobosGui)
/* 89 */       mc.func_147108_a(null); 
/*    */   }
/*    */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\me\earth\phobos\features\modules\client\ClickGui.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */