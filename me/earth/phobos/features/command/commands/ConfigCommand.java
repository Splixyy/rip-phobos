/*    */ package me.earth.phobos.features.command.commands;
/*    */ 
/*    */ import java.io.File;
/*    */ import java.util.Arrays;
/*    */ import java.util.List;
/*    */ import java.util.stream.Collectors;
/*    */ import me.earth.phobos.Phobos;
/*    */ import me.earth.phobos.features.command.Command;
/*    */ 
/*    */ 
/*    */ public class ConfigCommand
/*    */   extends Command
/*    */ {
/*    */   public ConfigCommand() {
/* 15 */     super("config", new String[] { "<save/load>" });
/*    */   }
/*    */ 
/*    */   
/*    */   public void execute(String[] commands) {
/* 20 */     if (commands.length == 1) {
/* 21 */       sendMessage("You`ll find the config files in your gameProfile directory under phobos/config");
/*    */       
/*    */       return;
/*    */     } 
/* 25 */     if (commands.length == 2) {
/* 26 */       if ("list".equals(commands[0])) {
/* 27 */         String configs = "Configs: ";
/* 28 */         File file = new File("phobos/");
/*    */ 
/*    */ 
/*    */         
/* 32 */         List<File> directories = (List<File>)Arrays.<File>stream(file.listFiles()).filter(File::isDirectory).filter(f -> !f.getName().equals("util")).collect(Collectors.toList());
/* 33 */         StringBuilder builder = new StringBuilder(configs);
/* 34 */         for (File file1 : directories) {
/* 35 */           builder.append(file1.getName() + ", ");
/*    */         }
/* 37 */         configs = builder.toString();
/* 38 */         sendMessage("§a" + configs);
/*    */       } else {
/* 40 */         sendMessage("§cNot a valid command... Possible usage: <list>");
/*    */       } 
/*    */     }
/*    */     
/* 44 */     if (commands.length >= 3) {
/* 45 */       switch (commands[0]) {
/*    */         case "save":
/* 47 */           Phobos.configManager.saveConfig(commands[1]);
/* 48 */           sendMessage("§aConfig has been saved.");
/*    */           return;
/*    */         case "load":
/* 51 */           Phobos.configManager.loadConfig(commands[1]);
/* 52 */           sendMessage("§aConfig has been loaded.");
/*    */           return;
/*    */       } 
/* 55 */       sendMessage("§cNot a valid command... Possible usage: <save/load>");
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\me\earth\phobos\features\command\commands\ConfigCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */