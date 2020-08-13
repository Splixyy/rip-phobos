/*    */ package me.earth.phobos.features.command.commands;
/*    */ 
/*    */ import me.earth.phobos.Phobos;
/*    */ import me.earth.phobos.features.command.Command;
/*    */ import me.earth.phobos.features.modules.render.XRay;
/*    */ import me.earth.phobos.features.setting.Setting;
/*    */ 
/*    */ public class XrayCommand
/*    */   extends Command
/*    */ {
/*    */   public XrayCommand() {
/* 12 */     super("xray", new String[] { "<add/del>", "<block>" });
/*    */   }
/*    */ 
/*    */   
/*    */   public void execute(String[] commands) {
/* 17 */     XRay module = (XRay)Phobos.moduleManager.getModuleByClass(XRay.class);
/* 18 */     if (module != null) {
/* 19 */       if (commands.length == 1) {
/* 20 */         StringBuilder blocks = new StringBuilder();
/* 21 */         for (Setting setting : module.getSettings()) {
/* 22 */           if (setting.equals(module.enabled) || setting.equals(module.drawn) || setting.equals(module.bind) || setting.equals(module.newBlock) || setting.equals(module.showBlocks)) {
/*    */             continue;
/*    */           }
/*    */           
/* 26 */           blocks.append(setting.getName()).append(", ");
/*    */         } 
/* 28 */         Command.sendMessage(blocks.toString()); return;
/*    */       } 
/* 30 */       if (commands.length == 2) {
/* 31 */         sendMessage("Please specify a block.");
/*    */         
/*    */         return;
/*    */       } 
/* 35 */       String addRemove = commands[0];
/* 36 */       String blockName = commands[1];
/* 37 */       if (addRemove.equalsIgnoreCase("del") || addRemove.equalsIgnoreCase("remove")) {
/* 38 */         Setting setting = module.getSettingByName(blockName);
/* 39 */         if (setting != null) {
/* 40 */           if (setting.equals(module.enabled) || setting.equals(module.drawn) || setting.equals(module.bind) || setting.equals(module.newBlock) || setting.equals(module.showBlocks)) {
/*    */             return;
/*    */           }
/* 43 */           module.unregister(setting);
/*    */         } 
/* 45 */         sendMessage("<XRay>§c Removed: " + blockName);
/* 46 */       } else if (addRemove.equalsIgnoreCase("add")) {
/* 47 */         if (!module.shouldRender(blockName)) {
/* 48 */           module.register(new Setting(blockName, Boolean.valueOf(true), v -> ((Boolean)module.showBlocks.getValue()).booleanValue()));
/* 49 */           sendMessage("<Xray> Added new Block: " + blockName);
/*    */         } 
/*    */       } else {
/* 52 */         sendMessage("§cAn error occured, block either exists or wrong use of command: .xray <add/del(remove)> <block>");
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\me\earth\phobos\features\command\commands\XrayCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */