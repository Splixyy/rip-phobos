/*     */ package me.earth.phobos.manager;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import me.earth.phobos.features.Feature;
/*     */ import me.earth.phobos.features.command.Command;
/*     */ import me.earth.phobos.features.command.commands.BindCommand;
/*     */ import me.earth.phobos.features.command.commands.BookCommand;
/*     */ import me.earth.phobos.features.command.commands.ConfigCommand;
/*     */ import me.earth.phobos.features.command.commands.CrashCommand;
/*     */ import me.earth.phobos.features.command.commands.FriendCommand;
/*     */ import me.earth.phobos.features.command.commands.HelpCommand;
/*     */ import me.earth.phobos.features.command.commands.HistoryCommand;
/*     */ import me.earth.phobos.features.command.commands.ModuleCommand;
/*     */ import me.earth.phobos.features.command.commands.PeekCommand;
/*     */ import me.earth.phobos.features.command.commands.PrefixCommand;
/*     */ import me.earth.phobos.features.command.commands.ReloadCommand;
/*     */ import me.earth.phobos.features.command.commands.ReloadSoundCommand;
/*     */ import me.earth.phobos.features.command.commands.UnloadCommand;
/*     */ import me.earth.phobos.features.command.commands.XrayCommand;
/*     */ 
/*     */ 
/*     */ public class CommandManager
/*     */   extends Feature
/*     */ {
/*  27 */   private String clientMessage = "<Phobos.eu>";
/*  28 */   private String prefix = ".";
/*     */ 
/*     */   
/*     */   private ArrayList<Command> commands;
/*     */ 
/*     */   
/*     */   public CommandManager() {
/*  35 */     super("Command");
/*  36 */     this.commands = new ArrayList<>();
/*  37 */     this.commands.add(new BindCommand());
/*  38 */     this.commands.add(new ModuleCommand());
/*  39 */     this.commands.add(new PrefixCommand());
/*  40 */     this.commands.add(new ConfigCommand());
/*  41 */     this.commands.add(new FriendCommand());
/*  42 */     this.commands.add(new HelpCommand());
/*  43 */     this.commands.add(new ReloadCommand());
/*  44 */     this.commands.add(new UnloadCommand());
/*  45 */     this.commands.add(new ReloadSoundCommand());
/*  46 */     this.commands.add(new PeekCommand());
/*  47 */     this.commands.add(new XrayCommand());
/*  48 */     this.commands.add(new BookCommand());
/*  49 */     this.commands.add(new CrashCommand());
/*  50 */     this.commands.add(new HistoryCommand());
/*     */   }
/*     */   
/*     */   public void executeCommand(String command) {
/*  54 */     String[] parts = command.split(" (?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
/*  55 */     String name = parts[0].substring(1);
/*  56 */     String[] args = removeElement(parts, 0);
/*  57 */     for (int i = 0; i < args.length; i++) {
/*  58 */       if (args[i] != null)
/*  59 */         args[i] = strip(args[i], "\""); 
/*     */     } 
/*  61 */     for (Command c : this.commands) {
/*  62 */       if (c.getName().equalsIgnoreCase(name)) {
/*  63 */         c.execute(parts);
/*     */         return;
/*     */       } 
/*     */     } 
/*  67 */     Command.sendMessage("Unknown command. try 'commands' for a list of commands.");
/*     */   }
/*     */   
/*     */   public static String[] removeElement(String[] input, int indexToDelete) {
/*  71 */     List<String> result = new LinkedList();
/*  72 */     for (int i = 0; i < input.length; i++) {
/*  73 */       if (i != indexToDelete) result.add(input[i]); 
/*     */     } 
/*  75 */     return result.<String>toArray(input);
/*     */   }
/*     */   
/*     */   private static String strip(String str, String key) {
/*  79 */     if (str.startsWith(key) && str.endsWith(key)) return str.substring(key.length(), str.length() - key.length()); 
/*  80 */     return str;
/*     */   }
/*     */   
/*     */   public Command getCommandByName(String name) {
/*  84 */     for (Command command : this.commands) {
/*  85 */       if (command.getName().equals(name)) {
/*  86 */         return command;
/*     */       }
/*     */     } 
/*  89 */     return null;
/*     */   }
/*     */   
/*     */   public ArrayList<Command> getCommands() {
/*  93 */     return this.commands;
/*     */   }
/*     */   
/*     */   public String getClientMessage() {
/*  97 */     return this.clientMessage;
/*     */   }
/*     */   
/*     */   public void setClientMessage(String clientMessage) {
/* 101 */     this.clientMessage = clientMessage;
/*     */   }
/*     */   
/*     */   public String getPrefix() {
/* 105 */     return this.prefix;
/*     */   }
/*     */   
/*     */   public void setPrefix(String prefix) {
/* 109 */     this.prefix = prefix;
/*     */   }
/*     */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\me\earth\phobos\manager\CommandManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */