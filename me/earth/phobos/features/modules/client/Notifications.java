/*     */ package me.earth.phobos.features.modules.client;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import me.earth.phobos.Phobos;
/*     */ import me.earth.phobos.event.events.ClientEvent;
/*     */ import me.earth.phobos.features.command.Command;
/*     */ import me.earth.phobos.features.modules.Module;
/*     */ import me.earth.phobos.features.setting.Setting;
/*     */ import me.earth.phobos.manager.FileManager;
/*     */ import me.earth.phobos.util.Timer;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*     */ 
/*     */ 
/*     */ public class Notifications
/*     */   extends Module
/*     */ {
/*  20 */   public Setting<Boolean> totemPops = register(new Setting("TotemPops", Boolean.valueOf(false)));
/*  21 */   public Setting<Boolean> totemNoti = register(new Setting("TotemNoti", Boolean.valueOf(true), v -> ((Boolean)this.totemPops.getValue()).booleanValue()));
/*  22 */   public Setting<Integer> delay = register(new Setting("Delay", Integer.valueOf(2000), Integer.valueOf(0), Integer.valueOf(5000), v -> ((Boolean)this.totemPops.getValue()).booleanValue(), "Delays messages."));
/*  23 */   public Setting<Boolean> clearOnLogout = register(new Setting("LogoutClear", Boolean.valueOf(false)));
/*  24 */   public Setting<Boolean> moduleMessage = register(new Setting("ModuleMessage", Boolean.valueOf(false)));
/*  25 */   public Setting<Boolean> list = register(new Setting("List", Boolean.valueOf(false), v -> ((Boolean)this.moduleMessage.getValue()).booleanValue()));
/*  26 */   private Setting<Boolean> readfile = register(new Setting("LoadFile", Boolean.valueOf(false), v -> ((Boolean)this.moduleMessage.getValue()).booleanValue()));
/*  27 */   public Setting<Boolean> watermark = register(new Setting("Watermark", Boolean.valueOf(true), v -> ((Boolean)this.moduleMessage.getValue()).booleanValue()));
/*  28 */   public Setting<Boolean> visualRange = register(new Setting("VisualRange", Boolean.valueOf(false)));
/*  29 */   public Setting<Boolean> coords = register(new Setting("Coords", Boolean.valueOf(true), v -> ((Boolean)this.visualRange.getValue()).booleanValue()));
/*  30 */   public Setting<Boolean> leaving = register(new Setting("Leaving", Boolean.valueOf(false), v -> ((Boolean)this.visualRange.getValue()).booleanValue()));
/*  31 */   public Setting<Boolean> crash = register(new Setting("Crash", Boolean.valueOf(false)));
/*  32 */   private List<EntityPlayer> knownPlayers = new ArrayList<>();
/*  33 */   private static List<String> modules = new ArrayList<>();
/*     */   private static final String fileName = "phobos/util/ModuleMessage_List.txt";
/*  35 */   private final Timer timer = new Timer();
/*  36 */   public Timer totemAnnounce = new Timer();
/*     */   private boolean check;
/*  38 */   private static Notifications INSTANCE = new Notifications();
/*     */   
/*     */   public Notifications() {
/*  41 */     super("Notifications", "Sends Messages.", Module.Category.CLIENT, true, false, false);
/*  42 */     setInstance();
/*     */   }
/*     */   
/*     */   private void setInstance() {
/*  46 */     INSTANCE = this;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onLoad() {
/*  51 */     this.check = true;
/*  52 */     loadFile();
/*  53 */     this.check = false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onEnable() {
/*  58 */     this.knownPlayers = new ArrayList<>();
/*  59 */     if (!this.check) {
/*  60 */       loadFile();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void onUpdate() {
/*  66 */     if (((Boolean)this.readfile.getValue()).booleanValue()) {
/*  67 */       if (!this.check) {
/*  68 */         Command.sendMessage("Loading File...");
/*  69 */         this.timer.reset();
/*  70 */         loadFile();
/*     */       } 
/*  72 */       this.check = true;
/*     */     } 
/*     */     
/*  75 */     if (this.check && this.timer.passedMs(750L)) {
/*  76 */       this.readfile.setValue(Boolean.valueOf(false));
/*  77 */       this.check = false;
/*     */     } 
/*     */     
/*  80 */     if (((Boolean)this.visualRange.getValue()).booleanValue()) {
/*  81 */       List<EntityPlayer> tickPlayerList = new ArrayList<>(mc.field_71441_e.field_73010_i);
/*  82 */       if (tickPlayerList.size() > 0) {
/*  83 */         for (EntityPlayer player : tickPlayerList) {
/*  84 */           if (player.func_70005_c_().equals(mc.field_71439_g.func_70005_c_())) {
/*     */             continue;
/*     */           }
/*  87 */           if (!this.knownPlayers.contains(player)) {
/*  88 */             this.knownPlayers.add(player);
/*  89 */             if (Phobos.friendManager.isFriend(player)) {
/*  90 */               Command.sendMessage("Player §a" + player.func_70005_c_() + "§r" + " entered your visual range" + (((Boolean)this.coords.getValue()).booleanValue() ? (" at (" + (int)player.field_70165_t + ", " + (int)player.field_70163_u + ", " + (int)player.field_70161_v + ")!") : "!"), true);
/*     */             } else {
/*  92 */               Command.sendMessage("Player §c" + player.func_70005_c_() + "§r" + " entered your visual range" + (((Boolean)this.coords.getValue()).booleanValue() ? (" at (" + (int)player.field_70165_t + ", " + (int)player.field_70163_u + ", " + (int)player.field_70161_v + ")!") : "!"), true);
/*     */             } 
/*     */             
/*     */             return;
/*     */           } 
/*     */         } 
/*     */       }
/*  99 */       if (this.knownPlayers.size() > 0) {
/* 100 */         for (EntityPlayer player : this.knownPlayers) {
/* 101 */           if (!tickPlayerList.contains(player)) {
/* 102 */             this.knownPlayers.remove(player);
/* 103 */             if (((Boolean)this.leaving.getValue()).booleanValue()) {
/* 104 */               if (Phobos.friendManager.isFriend(player)) {
/* 105 */                 Command.sendMessage("Player §a" + player.func_70005_c_() + "§r" + " left your visual range" + (((Boolean)this.coords.getValue()).booleanValue() ? (" at (" + (int)player.field_70165_t + ", " + (int)player.field_70163_u + ", " + (int)player.field_70161_v + ")!") : "!"), true);
/*     */               } else {
/* 107 */                 Command.sendMessage("Player §c" + player.func_70005_c_() + "§r" + " left your visual range" + (((Boolean)this.coords.getValue()).booleanValue() ? (" at (" + (int)player.field_70165_t + ", " + (int)player.field_70163_u + ", " + (int)player.field_70161_v + ")!") : "!"), true);
/*     */               } 
/*     */             }
/*     */             return;
/*     */           } 
/*     */         } 
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public void loadFile() {
/* 118 */     List<String> fileInput = FileManager.readTextFileAllLines("phobos/util/ModuleMessage_List.txt");
/* 119 */     Iterator<String> i = fileInput.iterator();
/* 120 */     modules.clear();
/* 121 */     while (i.hasNext()) {
/* 122 */       String s = i.next();
/* 123 */       if (!s.replaceAll("\\s", "").isEmpty()) {
/* 124 */         modules.add(s);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onToggleModule(ClientEvent event) {
/* 131 */     if (!((Boolean)this.moduleMessage.getValue()).booleanValue()) {
/*     */       return;
/*     */     }
/*     */     
/* 135 */     if (event.getStage() == 0) {
/* 136 */       Module module = (Module)event.getFeature();
/* 137 */       if (!module.equals(this) && (modules.contains(module.getDisplayName()) || !((Boolean)this.list.getValue()).booleanValue())) {
/* 138 */         if (((Boolean)this.watermark.getValue()).booleanValue()) {
/* 139 */           Command.sendMessage("§c" + module.getDisplayName() + " disabled.");
/*     */         } else {
/* 141 */           Command.sendSilentMessage("§c" + module.getDisplayName() + " disabled.");
/*     */         } 
/*     */       }
/*     */     } 
/*     */     
/* 146 */     if (event.getStage() == 1) {
/* 147 */       Module module = (Module)event.getFeature();
/* 148 */       if (modules.contains(module.getDisplayName()) || !((Boolean)this.list.getValue()).booleanValue()) {
/* 149 */         if (((Boolean)this.watermark.getValue()).booleanValue()) {
/* 150 */           Command.sendMessage("§a" + module.getDisplayName() + " enabled.");
/*     */         } else {
/* 152 */           Command.sendSilentMessage("§a" + module.getDisplayName() + " enabled.");
/*     */         } 
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static Notifications getInstance() {
/* 161 */     if (INSTANCE == null) {
/* 162 */       INSTANCE = new Notifications();
/*     */     }
/* 164 */     return INSTANCE;
/*     */   }
/*     */   
/*     */   public static void displayCrash(Exception e) {
/* 168 */     Command.sendMessage("§cException caught: " + e.getMessage());
/*     */   }
/*     */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\me\earth\phobos\features\modules\client\Notifications.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */