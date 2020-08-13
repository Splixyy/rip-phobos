/*     */ package me.earth.phobos.features.modules.misc;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Random;
/*     */ import me.earth.phobos.features.command.Command;
/*     */ import me.earth.phobos.features.modules.Module;
/*     */ import me.earth.phobos.features.setting.Setting;
/*     */ import me.earth.phobos.manager.FileManager;
/*     */ 
/*     */ public class Announcer
/*     */   extends Module
/*     */ {
/*  17 */   private final Setting<Boolean> join = register(new Setting("Join", Boolean.valueOf(true)));
/*  18 */   private final Setting<Boolean> leave = register(new Setting("Leave", Boolean.valueOf(true)));
/*  19 */   private final Setting<Boolean> eat = register(new Setting("Eat", Boolean.valueOf(true)));
/*  20 */   private final Setting<Boolean> walk = register(new Setting("Walk", Boolean.valueOf(true)));
/*  21 */   private final Setting<Boolean> mine = register(new Setting("Mine", Boolean.valueOf(true)));
/*  22 */   private final Setting<Boolean> place = register(new Setting("Place", Boolean.valueOf(true)));
/*  23 */   private final Setting<Boolean> totem = register(new Setting("TotemPop", Boolean.valueOf(true)));
/*     */   
/*  25 */   private final Setting<Boolean> random = register(new Setting("Random", Boolean.valueOf(true)));
/*  26 */   private final Setting<Boolean> greentext = register(new Setting("Greentext", Boolean.valueOf(false)));
/*  27 */   private final Setting<Boolean> loadFiles = register(new Setting("LoadFiles", Boolean.valueOf(false)));
/*  28 */   private final Setting<Integer> delay = register(new Setting("SendDelay", Integer.valueOf(40)));
/*  29 */   private final Setting<Integer> queueSize = register(new Setting("QueueSize", Integer.valueOf(5), Integer.valueOf(1), Integer.valueOf(100)));
/*  30 */   private final Setting<Integer> mindistance = register(new Setting("Min Distance", Integer.valueOf(10), Integer.valueOf(1), Integer.valueOf(100)));
/*  31 */   private final Setting<Boolean> clearQueue = register(new Setting("ClearQueue", Boolean.valueOf(false)));
/*     */   
/*     */   private static final String directory = "phobos/announcer/";
/*     */   
/*  35 */   private Map<Action, ArrayList<String>> loadedMessages = new HashMap<>();
/*     */   
/*  37 */   private Map<Action, Message> queue = new HashMap<>();
/*     */   
/*     */   public Announcer() {
/*  40 */     super("Announcer", "How to get muted quick.", Module.Category.MISC, true, false, false);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onLoad() {
/*  45 */     loadMessages();
/*     */   }
/*     */ 
/*     */   
/*     */   public void onEnable() {
/*  50 */     loadMessages();
/*     */   }
/*     */ 
/*     */   
/*     */   public void onUpdate() {
/*  55 */     if (((Boolean)this.loadFiles.getValue()).booleanValue()) {
/*  56 */       loadMessages();
/*  57 */       Command.sendMessage("<Announcer> Loaded messages.");
/*  58 */       this.loadFiles.setValue(Boolean.valueOf(false));
/*     */     } 
/*     */   }
/*     */   
/*     */   public void loadMessages() {
/*  63 */     HashMap<Action, ArrayList<String>> newLoadedMessages = new HashMap<>();
/*  64 */     for (Action action : Action.values()) {
/*  65 */       String fileName = "phobos/announcer/" + action.getName() + ".txt";
/*  66 */       List<String> fileInput = FileManager.readTextFileAllLines(fileName);
/*  67 */       Iterator<String> i = fileInput.iterator();
/*  68 */       ArrayList<String> msgs = new ArrayList<>();
/*  69 */       while (i.hasNext()) {
/*  70 */         String string = i.next();
/*  71 */         if (!string.replaceAll("\\s", "").isEmpty()) {
/*  72 */           msgs.add(string);
/*     */         }
/*     */       } 
/*     */       
/*  76 */       if (msgs.isEmpty()) {
/*  77 */         msgs.add(action.getStandartMessage());
/*     */       }
/*     */       
/*  80 */       newLoadedMessages.put(action, msgs);
/*     */     } 
/*  82 */     this.loadedMessages = newLoadedMessages;
/*     */   }
/*     */ 
/*     */   
/*     */   private String getMessage(Action action, int number, String info) {
/*  87 */     return "";
/*     */   }
/*     */   
/*     */   private Action getRandomAction() {
/*  91 */     Random rnd = new Random();
/*  92 */     int index = rnd.nextInt(7);
/*  93 */     int i = 0;
/*  94 */     for (Action action : Action.values()) {
/*  95 */       if (i == index) {
/*  96 */         return action;
/*     */       }
/*  98 */       i++;
/*     */     } 
/* 100 */     return Action.WALK;
/*     */   }
/*     */   
/*     */   public static class Message
/*     */   {
/*     */     public final Announcer.Action action;
/*     */     public final String name;
/*     */     public final int amount;
/*     */     
/*     */     public Message(Announcer.Action action, String name, int amount) {
/* 110 */       this.action = action;
/* 111 */       this.name = name;
/* 112 */       this.amount = amount;
/*     */     }
/*     */   }
/*     */   
/*     */   public enum Action {
/* 117 */     JOIN("Join", "Welcome _!"),
/* 118 */     LEAVE("Leave", "Goodbye _!"),
/* 119 */     EAT("Eat", "I just ate % _!"),
/* 120 */     WALK("Walk", "I just walked % Blocks!"),
/* 121 */     MINE("Mine", "I mined % _!"),
/* 122 */     PLACE("Place", "I just placed % _!"),
/* 123 */     TOTEM("Totem", "_ just popped % Totems!");
/*     */     
/*     */     private final String name;
/*     */     private final String standartMessage;
/*     */     
/*     */     Action(String name, String standartMessage) {
/* 129 */       this.name = name;
/* 130 */       this.standartMessage = standartMessage;
/*     */     }
/*     */     
/*     */     public String getName() {
/* 134 */       return this.name;
/*     */     }
/*     */     
/*     */     public String getStandartMessage() {
/* 138 */       return this.standartMessage;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\me\earth\phobos\features\modules\misc\Announcer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */