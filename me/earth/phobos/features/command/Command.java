/*    */ package me.earth.phobos.features.command;
/*    */ 
/*    */ import java.util.regex.Matcher;
/*    */ import java.util.regex.Pattern;
/*    */ import me.earth.phobos.Phobos;
/*    */ import me.earth.phobos.features.Feature;
/*    */ import net.minecraft.util.text.ITextComponent;
/*    */ import net.minecraft.util.text.TextComponentBase;
/*    */ 
/*    */ 
/*    */ public abstract class Command
/*    */   extends Feature
/*    */ {
/*    */   protected String name;
/*    */   protected String[] commands;
/*    */   
/*    */   public Command(String name) {
/* 18 */     super(name);
/* 19 */     this.name = name;
/* 20 */     this.commands = new String[] { "" };
/*    */   }
/*    */   
/*    */   public Command(String name, String[] commands) {
/* 24 */     super(name);
/* 25 */     this.name = name;
/* 26 */     this.commands = commands;
/*    */   }
/*    */   
/*    */   public abstract void execute(String[] paramArrayOfString);
/*    */   
/*    */   public static void sendMessage(String message, boolean notification) {
/* 32 */     sendSilentMessage(Phobos.commandManager.getClientMessage() + " " + "§r" + message);
/* 33 */     if (notification)
/* 34 */       Phobos.notificationManager.addNotification(message, 3000L); 
/*    */   }
/*    */   public static void sendMessage(String message) {
/* 37 */     sendSilentMessage(Phobos.commandManager.getClientMessage() + " " + "§r" + message);
/*    */   }
/*    */   public static void sendSilentMessage(String message) {
/* 40 */     if (nullCheck())
/* 41 */       return;  mc.field_71439_g.func_145747_a((ITextComponent)new ChatMessage(message));
/*    */   }
/*    */   
/*    */   public String getName() {
/* 45 */     return this.name;
/*    */   }
/*    */   
/*    */   public String[] getCommands() {
/* 49 */     return this.commands;
/*    */   }
/*    */   
/*    */   public static String getCommandPrefix() {
/* 53 */     return Phobos.commandManager.getPrefix();
/*    */   }
/*    */   
/*    */   public static class ChatMessage
/*    */     extends TextComponentBase {
/*    */     private final String text;
/*    */     
/*    */     public ChatMessage(String text) {
/* 61 */       Pattern pattern = Pattern.compile("&[0123456789abcdefrlosmk]");
/* 62 */       Matcher matcher = pattern.matcher(text);
/* 63 */       StringBuffer stringBuffer = new StringBuffer();
/* 64 */       while (matcher.find()) {
/* 65 */         String replacement = "§" + matcher.group().substring(1);
/* 66 */         matcher.appendReplacement(stringBuffer, replacement);
/*    */       } 
/* 68 */       matcher.appendTail(stringBuffer);
/* 69 */       this.text = stringBuffer.toString();
/*    */     }
/*    */     
/*    */     public String func_150261_e() {
/* 73 */       return this.text;
/*    */     }
/*    */ 
/*    */     
/*    */     public ITextComponent func_150259_f() {
/* 78 */       return (ITextComponent)new ChatMessage(this.text);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\me\earth\phobos\features\command\Command.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */