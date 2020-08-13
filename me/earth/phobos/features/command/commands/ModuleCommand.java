/*    */ package me.earth.phobos.features.command.commands;
/*    */ 
/*    */ import com.google.gson.JsonParser;
/*    */ import me.earth.phobos.Phobos;
/*    */ import me.earth.phobos.features.Feature;
/*    */ import me.earth.phobos.features.command.Command;
/*    */ import me.earth.phobos.features.modules.Module;
/*    */ import me.earth.phobos.features.setting.Setting;
/*    */ import me.earth.phobos.manager.ConfigManager;
/*    */ 
/*    */ public class ModuleCommand
/*    */   extends Command {
/*    */   public ModuleCommand() {
/* 14 */     super("module", new String[] { "<module>", "<set/reset>", "<setting>", "<value>" });
/*    */   }
/*    */ 
/*    */   
/*    */   public void execute(String[] commands) {
/* 19 */     if (commands.length == 1) {
/* 20 */       sendMessage("Modules: ");
/* 21 */       for (Module.Category category : Phobos.moduleManager.getCategories()) {
/* 22 */         String modules = category.getName() + ": ";
/* 23 */         for (Module module1 : Phobos.moduleManager.getModulesByCategory(category)) {
/* 24 */           modules = modules + (module1.isEnabled() ? "§a" : "§c") + module1.getName() + "§r" + ", ";
/*    */         }
/* 26 */         sendMessage(modules);
/*    */       } 
/*    */       
/*    */       return;
/*    */     } 
/* 31 */     Module module = Phobos.moduleManager.getModuleByDisplayName(commands[0]);
/* 32 */     if (module == null) {
/* 33 */       module = Phobos.moduleManager.getModuleByName(commands[0]);
/* 34 */       if (module == null) {
/* 35 */         sendMessage("§cThis module doesnt exist.");
/*    */         return;
/*    */       } 
/* 38 */       sendMessage("§c This is the original name of the module. Its current name is: " + module.getDisplayName());
/*    */       
/*    */       return;
/*    */     } 
/* 42 */     if (commands.length == 2) {
/* 43 */       sendMessage(module.getDisplayName() + " : " + module.getDescription());
/* 44 */       for (Setting setting : module.getSettings()) {
/* 45 */         sendMessage(setting.getName() + " : " + setting.getValue() + ", " + setting.getDescription());
/*    */       }
/*    */       
/*    */       return;
/*    */     } 
/* 50 */     if (commands.length == 3) {
/* 51 */       if (commands[1].equalsIgnoreCase("set")) {
/* 52 */         sendMessage("§cPlease specify a setting.");
/* 53 */       } else if (commands[1].equalsIgnoreCase("reset")) {
/* 54 */         for (Setting setting : module.getSettings()) {
/* 55 */           setting.setValue(setting.getDefaultValue());
/*    */         }
/*    */       } else {
/* 58 */         sendMessage("§cThis command doesnt exist.");
/*    */       } 
/*    */       
/*    */       return;
/*    */     } 
/* 63 */     if (commands.length == 4) {
/* 64 */       sendMessage("§cPlease specify a value.");
/*    */       
/*    */       return;
/*    */     } 
/* 68 */     if (commands.length == 5) {
/* 69 */       Setting setting = module.getSettingByName(commands[2]);
/* 70 */       if (setting != null) {
/* 71 */         JsonParser jp = new JsonParser();
/* 72 */         if (setting.getType().equalsIgnoreCase("String")) {
/* 73 */           setting.setValue(commands[3]);
/* 74 */           sendMessage("§a" + module.getName() + " " + setting.getName() + " has been set to " + commands[3] + ".");
/*    */           return;
/*    */         } 
/*    */         try {
/* 78 */           if (setting.getName().equalsIgnoreCase("Enabled")) {
/* 79 */             if (commands[3].equalsIgnoreCase("true")) {
/* 80 */               module.enable();
/*    */             }
/* 82 */             if (commands[3].equalsIgnoreCase("false")) {
/* 83 */               module.disable();
/*    */             }
/*    */           } 
/* 86 */           ConfigManager.setValueFromJson((Feature)module, setting, jp.parse(commands[3]));
/* 87 */         } catch (Exception e) {
/* 88 */           sendMessage("§cBad Value! This setting requires a: " + setting.getType() + " value.");
/*    */           return;
/*    */         } 
/* 91 */         sendMessage("§a" + module.getName() + " " + setting.getName() + " has been set to " + commands[3] + ".");
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\me\earth\phobos\features\command\commands\ModuleCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */