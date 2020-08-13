/*    */ package me.earth.phobos.features.command.commands;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.UUID;
/*    */ import me.earth.phobos.features.command.Command;
/*    */ import me.earth.phobos.util.PlayerUtil;
/*    */ 
/*    */ 
/*    */ public class HistoryCommand
/*    */   extends Command
/*    */ {
/*    */   public HistoryCommand() {
/* 13 */     super("history", new String[] { "<player>" });
/*    */   }
/*    */   public void execute(String[] commands) {
/*    */     UUID uuid;
/*    */     List<String> names;
/* 18 */     if (commands.length == 1 || commands.length == 0) {
/* 19 */       sendMessage("§cPlease specify a player.");
/*    */     }
/*    */ 
/*    */     
/*    */     try {
/* 24 */       uuid = PlayerUtil.getUUIDFromName(commands[0]);
/* 25 */     } catch (Exception e) {
/* 26 */       sendMessage("An error occured.");
/*    */       
/*    */       return;
/*    */     } 
/*    */     
/*    */     try {
/* 32 */       names = PlayerUtil.getHistoryOfNames(uuid);
/* 33 */     } catch (Exception e) {
/* 34 */       sendMessage("An error occured.");
/*    */       
/*    */       return;
/*    */     } 
/* 38 */     if (names != null) {
/* 39 */       sendMessage(commands[0] + "Â´s name history:");
/* 40 */       for (String name : names) {
/* 41 */         sendMessage(name);
/*    */       }
/*    */     } else {
/* 44 */       sendMessage("No names found.");
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\me\earth\phobos\features\command\commands\HistoryCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */