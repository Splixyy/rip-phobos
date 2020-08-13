/*    */ package me.earth.phobos.features.modules.client;
/*    */ 
/*    */ import me.earth.phobos.Phobos;
/*    */ import me.earth.phobos.event.events.ClientEvent;
/*    */ import me.earth.phobos.features.modules.Module;
/*    */ import me.earth.phobos.features.setting.Setting;
/*    */ import me.earth.phobos.util.TextUtil;
/*    */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*    */ 
/*    */ public class Managers
/*    */   extends Module {
/* 12 */   public Setting<Boolean> betterFrames = register(new Setting("BetterMaxFPS", Boolean.valueOf(false)));
/*    */   
/* 14 */   private static Managers INSTANCE = new Managers();
/* 15 */   public Setting<String> commandBracket = register(new Setting("Bracket", "<"));
/* 16 */   public Setting<String> commandBracket2 = register(new Setting("Bracket2", ">"));
/* 17 */   public Setting<String> command = register(new Setting("Command", "Phobos.eu"));
/* 18 */   public Setting<TextUtil.Color> bracketColor = register(new Setting("BColor", TextUtil.Color.BLUE));
/* 19 */   public Setting<TextUtil.Color> commandColor = register(new Setting("CColor", TextUtil.Color.BLUE));
/* 20 */   public Setting<Integer> betterFPS = register(new Setting("MaxFPS", Integer.valueOf(300), Integer.valueOf(30), Integer.valueOf(1000), v -> ((Boolean)this.betterFrames.getValue()).booleanValue()));
/* 21 */   public Setting<Boolean> potions = register(new Setting("Potions", Boolean.valueOf(true)));
/* 22 */   public Setting<Integer> textRadarUpdates = register(new Setting("TRUpdates", Integer.valueOf(500), Integer.valueOf(0), Integer.valueOf(1000)));
/* 23 */   public Setting<Integer> respondTime = register(new Setting("SeverTime", Integer.valueOf(500), Integer.valueOf(0), Integer.valueOf(1000)));
/* 24 */   public Setting<Integer> moduleListUpdates = register(new Setting("ALUpdates", Integer.valueOf(1000), Integer.valueOf(0), Integer.valueOf(1000)));
/* 25 */   public Setting<Float> holeRange = register(new Setting("HoleRange", Float.valueOf(6.0F), Float.valueOf(1.0F), Float.valueOf(32.0F)));
/* 26 */   public Setting<Boolean> speed = register(new Setting("Speed", Boolean.valueOf(true)));
/* 27 */   public Setting<Boolean> tRadarInv = register(new Setting("TRadarInv", Boolean.valueOf(true)));
/* 28 */   public Setting<Boolean> unfocusedCpu = register(new Setting("UnfocusedCPU", Boolean.valueOf(false)));
/* 29 */   public Setting<Integer> cpuFPS = register(new Setting("UnfocusedFPS", Integer.valueOf(60), Integer.valueOf(1), Integer.valueOf(60), v -> ((Boolean)this.unfocusedCpu.getValue()).booleanValue()));
/*    */   
/*    */   public Managers() {
/* 32 */     super("Management", "ClientManagement", Module.Category.CLIENT, false, false, true);
/* 33 */     setInstance();
/*    */   }
/*    */   
/*    */   private void setInstance() {
/* 37 */     INSTANCE = this;
/*    */   }
/*    */   
/*    */   public static Managers getInstance() {
/* 41 */     if (INSTANCE == null) {
/* 42 */       INSTANCE = new Managers();
/*    */     }
/* 44 */     return INSTANCE;
/*    */   }
/*    */ 
/*    */   
/*    */   public void onLoad() {
/* 49 */     Phobos.commandManager.setClientMessage(getCommandMessage());
/*    */   }
/*    */   
/*    */   @SubscribeEvent
/*    */   public void onSettingChange(ClientEvent event) {
/* 54 */     if (event.getStage() == 2 && 
/* 55 */       equals(event.getSetting().getFeature())) {
/* 56 */       Phobos.commandManager.setClientMessage(getCommandMessage());
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public String getCommandMessage() {
/* 62 */     return TextUtil.coloredString((String)this.commandBracket.getPlannedValue(), (TextUtil.Color)this.bracketColor.getPlannedValue()) + TextUtil.coloredString((String)this.command.getPlannedValue(), (TextUtil.Color)this.commandColor.getPlannedValue()) + TextUtil.coloredString((String)this.commandBracket2.getPlannedValue(), (TextUtil.Color)this.bracketColor.getPlannedValue());
/*    */   }
/*    */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\me\earth\phobos\features\modules\client\Managers.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */