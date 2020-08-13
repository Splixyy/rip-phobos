/*     */ package me.earth.phobos.features.modules.misc;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ import me.earth.phobos.features.modules.Module;
/*     */ import me.earth.phobos.features.setting.Setting;
/*     */ import me.earth.phobos.util.FileUtil;
/*     */ import me.earth.phobos.util.Timer;
/*     */ import net.minecraft.client.network.NetworkPlayerInfo;
/*     */ import net.minecraft.network.Packet;
/*     */ import net.minecraft.network.play.client.CPacketChatMessage;
/*     */ import net.minecraft.util.StringUtils;
/*     */ 
/*     */ public class Spammer
/*     */   extends Module
/*     */ {
/*  19 */   public Setting<Mode> mode = register(new Setting("Mode", Mode.PWORD));
/*  20 */   public Setting<PwordMode> type = register(new Setting("Pword", PwordMode.CHAT, v -> (this.mode.getValue() == Mode.PWORD)));
/*  21 */   public Setting<DelayType> delayType = register(new Setting("DelayType", DelayType.S));
/*  22 */   public Setting<Integer> delay = register(new Setting("DelayS", Integer.valueOf(10), Integer.valueOf(1), Integer.valueOf(20), v -> (this.delayType.getValue() == DelayType.S)));
/*  23 */   public Setting<Integer> delayDS = register(new Setting("DelayDS", Integer.valueOf(10), Integer.valueOf(1), Integer.valueOf(500), v -> (this.delayType.getValue() == DelayType.DS)));
/*  24 */   public Setting<Integer> delayMS = register(new Setting("DelayDS", Integer.valueOf(10), Integer.valueOf(1), Integer.valueOf(1000), v -> (this.delayType.getValue() == DelayType.MS)));
/*  25 */   public Setting<String> msgTarget = register(new Setting("MsgTarget", "Target...", v -> (this.mode.getValue() == Mode.PWORD && this.type.getValue() == PwordMode.MSG)));
/*  26 */   public Setting<Boolean> greentext = register(new Setting("Greentext", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.FILE)));
/*  27 */   public Setting<Boolean> random = register(new Setting("Random", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.FILE)));
/*  28 */   public Setting<Boolean> loadFile = register(new Setting("LoadFile", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.FILE)));
/*     */   
/*  30 */   private final Timer timer = new Timer();
/*  31 */   private final List<String> sendPlayers = new ArrayList<>();
/*     */   private static final String fileName = "phobos/util/Spammer.txt";
/*     */   private static final String defaultMessage = "gg";
/*  34 */   private static final List<String> spamMessages = new ArrayList<>();
/*  35 */   private static final Random rnd = new Random();
/*     */   
/*     */   public Spammer() {
/*  38 */     super("Spammer", "Spams stuff.", Module.Category.MISC, true, false, false);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onLoad() {
/*  43 */     readSpamFile();
/*  44 */     disable();
/*     */   }
/*     */ 
/*     */   
/*     */   public void onEnable() {
/*  49 */     if (fullNullCheck()) {
/*  50 */       disable();
/*     */       return;
/*     */     } 
/*  53 */     readSpamFile();
/*     */   }
/*     */ 
/*     */   
/*     */   public void onLogin() {
/*  58 */     disable();
/*     */   }
/*     */ 
/*     */   
/*     */   public void onLogout() {
/*  63 */     disable();
/*     */   }
/*     */ 
/*     */   
/*     */   public void onDisable() {
/*  68 */     spamMessages.clear();
/*  69 */     this.timer.reset();
/*     */   }
/*     */ 
/*     */   
/*     */   public void onUpdate() {
/*  74 */     if (fullNullCheck()) {
/*  75 */       disable();
/*     */       
/*     */       return;
/*     */     } 
/*  79 */     if (((Boolean)this.loadFile.getValue()).booleanValue()) {
/*  80 */       readSpamFile();
/*  81 */       this.loadFile.setValue(Boolean.valueOf(false));
/*     */     } 
/*     */     
/*  84 */     switch ((DelayType)this.delayType.getValue()) {
/*     */       case MSG:
/*  86 */         if (!this.timer.passedMs(((Integer)this.delayMS.getValue()).intValue())) {
/*     */           return;
/*     */         }
/*     */         break;
/*     */       case EVERYONE:
/*  91 */         if (!this.timer.passedS(((Integer)this.delay.getValue()).intValue())) {
/*     */           return;
/*     */         }
/*     */         break;
/*     */       case null:
/*  96 */         if (!this.timer.passedDs(((Integer)this.delayDS.getValue()).intValue())) {
/*     */           return;
/*     */         }
/*     */         break;
/*     */     } 
/*     */ 
/*     */     
/* 103 */     if (this.mode.getValue() == Mode.PWORD) {
/* 104 */       String target, msg = "  ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒\n ███▒█▒█▒███▒███▒███▒███\n █▒█▒█▒█▒█▒█▒█▒█▒█▒█▒█▒▒\n ███▒███▒█▒█▒███▒█▒█▒███\n █▒▒▒█▒█▒█▒█▒█▒█▒█▒█▒▒▒█\n █▒▒▒█▒█▒███▒███▒███▒███\n ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒";
/* 105 */       switch ((PwordMode)this.type.getValue()) {
/*     */         case MSG:
/* 107 */           msg = "/msg " + (String)this.msgTarget.getValue() + msg;
/*     */           break;
/*     */         case EVERYONE:
/* 110 */           target = null;
/* 111 */           if (mc.func_147114_u() != null && mc.func_147114_u().func_175106_d() != null) {
/* 112 */             for (NetworkPlayerInfo info : mc.func_147114_u().func_175106_d()) {
/* 113 */               if (info != null && info.func_178854_k() != null) {
/*     */                 try {
/* 115 */                   String str = info.func_178854_k().func_150254_d();
/* 116 */                   String name = StringUtils.func_76338_a(str);
/* 117 */                   if (name.equals(mc.field_71439_g.func_70005_c_()) || this.sendPlayers.contains(name)) {
/*     */                     continue;
/*     */                   }
/* 120 */                   target = name;
/* 121 */                   this.sendPlayers.add(name);
/*     */                   break;
/* 123 */                 } catch (Exception exception) {}
/*     */               }
/*     */             } 
/*     */             
/* 127 */             if (target == null) {
/* 128 */               this.sendPlayers.clear();
/*     */               return;
/*     */             } 
/* 131 */             msg = "/msg " + target + msg;
/*     */             break;
/*     */           } 
/*     */           return;
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 139 */       mc.field_71439_g.func_71165_d(msg);
/*     */     }
/* 141 */     else if (spamMessages.size() > 0) {
/*     */       String messageOut;
/* 143 */       if (((Boolean)this.random.getValue()).booleanValue()) {
/* 144 */         int index = rnd.nextInt(spamMessages.size());
/* 145 */         messageOut = spamMessages.get(index);
/* 146 */         spamMessages.remove(index);
/*     */       } else {
/* 148 */         messageOut = spamMessages.get(0);
/* 149 */         spamMessages.remove(0);
/*     */       } 
/* 151 */       spamMessages.add(messageOut);
/* 152 */       if (((Boolean)this.greentext.getValue()).booleanValue()) {
/* 153 */         messageOut = "> " + messageOut;
/*     */       }
/* 155 */       mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketChatMessage(messageOut.replaceAll("§", "")));
/*     */     } 
/*     */     
/* 158 */     this.timer.reset();
/*     */   }
/*     */   
/*     */   private void readSpamFile() {
/* 162 */     List<String> fileInput = FileUtil.readTextFileAllLines("phobos/util/Spammer.txt");
/* 163 */     Iterator<String> i = fileInput.iterator();
/* 164 */     spamMessages.clear();
/* 165 */     while (i.hasNext()) {
/* 166 */       String s = i.next();
/* 167 */       if (!s.replaceAll("\\s", "").isEmpty()) {
/* 168 */         spamMessages.add(s);
/*     */       }
/*     */     } 
/* 171 */     if (spamMessages.size() == 0)
/* 172 */       spamMessages.add("gg"); 
/*     */   }
/*     */   
/*     */   public enum Mode
/*     */   {
/* 177 */     FILE,
/* 178 */     PWORD;
/*     */   }
/*     */   
/*     */   public enum PwordMode {
/* 182 */     MSG,
/* 183 */     EVERYONE,
/* 184 */     CHAT;
/*     */   }
/*     */   
/*     */   public enum DelayType {
/* 188 */     MS,
/* 189 */     DS,
/* 190 */     S;
/*     */   }
/*     */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\me\earth\phobos\features\modules\misc\Spammer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */