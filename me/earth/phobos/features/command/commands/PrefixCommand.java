/*    */ package me.earth.phobos.features.command.commands;
/*    */ 
/*    */ import me.earth.phobos.Phobos;
/*    */ import me.earth.phobos.features.command.Command;
/*    */ import me.earth.phobos.features.modules.client.ClickGui;
/*    */ 
/*    */ public class PrefixCommand
/*    */   extends Command
/*    */ {
/*    */   public PrefixCommand() {
/* 11 */     super("prefix", new String[] { "<char>" });
/*    */   }
/*    */ 
/*    */   
/*    */   public void execute(String[] commands) {
/* 16 */     if (commands.length == 1) {
/* 17 */       Command.sendMessage("§cSpecify a new prefix.");
/*    */       
/*    */       return;
/*    */     } 
/* 21 */     ((ClickGui)Phobos.moduleManager.getModuleByClass(ClickGui.class)).prefix.setValue(commands[0]);
/* 22 */     Command.sendMessage("Prefix set to §a" + Phobos.commandManager.getPrefix());
/*    */   }
/*    */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\me\earth\phobos\features\command\commands\PrefixCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */