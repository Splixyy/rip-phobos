/*    */ package me.earth.phobos.features.command.commands;
/*    */ 
/*    */ import me.earth.phobos.features.command.Command;
/*    */ import net.minecraft.client.audio.SoundHandler;
/*    */ import net.minecraft.client.audio.SoundManager;
/*    */ import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
/*    */ 
/*    */ public class ReloadSoundCommand
/*    */   extends Command {
/*    */   public ReloadSoundCommand() {
/* 11 */     super("sound", new String[0]);
/*    */   }
/*    */ 
/*    */   
/*    */   public void execute(String[] commands) {
/*    */     try {
/* 17 */       SoundManager sndManager = (SoundManager)ObfuscationReflectionHelper.getPrivateValue(SoundHandler.class, mc.func_147118_V(), new String[] { "sndManager", "field_147694_f" });
/* 18 */       sndManager.func_148596_a();
/* 19 */       sendMessage("§aReloaded Sound System.");
/* 20 */     } catch (Exception e) {
/* 21 */       System.out.println("Could not restart sound manager: " + e.toString());
/* 22 */       e.printStackTrace();
/* 23 */       sendMessage("§cCouldnt Reload Sound System!");
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\me\earth\phobos\features\command\commands\ReloadSoundCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */