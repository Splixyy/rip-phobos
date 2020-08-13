/*    */ package me.earth.phobos.features.command.commands;
/*    */ 
/*    */ import me.earth.phobos.Phobos;
/*    */ import me.earth.phobos.features.command.Command;
/*    */ import me.earth.phobos.features.modules.Module;
/*    */ import me.earth.phobos.features.setting.Bind;
/*    */ import org.lwjgl.input.Keyboard;
/*    */ 
/*    */ public class BindCommand
/*    */   extends Command {
/*    */   public BindCommand() {
/* 12 */     super("bind", new String[] { "<module>", "<bind>" });
/*    */   }
/*    */ 
/*    */   
/*    */   public void execute(String[] commands) {
/* 17 */     if (commands.length == 1) {
/* 18 */       sendMessage("Please specify a module.");
/*    */       
/*    */       return;
/*    */     } 
/* 22 */     String rkey = commands[1];
/* 23 */     String moduleName = commands[0];
/*    */     
/* 25 */     Module module = Phobos.moduleManager.getModuleByName(moduleName);
/*    */     
/* 27 */     if (module == null) {
/* 28 */       sendMessage("Unknown module '" + module + "'!");
/*    */       
/*    */       return;
/*    */     } 
/* 32 */     if (rkey == null) {
/* 33 */       sendMessage(module.getName() + " is bound to &b" + module.getBind().toString());
/*    */       
/*    */       return;
/*    */     } 
/* 37 */     int key = Keyboard.getKeyIndex(rkey.toUpperCase());
/*    */     
/* 39 */     if (rkey.equalsIgnoreCase("none")) {
/* 40 */       key = -1;
/*    */     }
/*    */     
/* 43 */     if (key == 0) {
/* 44 */       sendMessage("Unknown key '" + rkey + "'!");
/*    */       
/*    */       return;
/*    */     } 
/* 48 */     module.bind.setValue(new Bind(key));
/* 49 */     sendMessage("Bind for &b" + module.getName() + "&r set to &b" + rkey.toUpperCase());
/*    */   }
/*    */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\me\earth\phobos\features\command\commands\BindCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */