/*     */ package me.earth.phobos.features.modules.misc;
/*     */ 
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.Date;
/*     */ import me.earth.phobos.event.events.PacketEvent;
/*     */ import me.earth.phobos.features.command.Command;
/*     */ import me.earth.phobos.features.modules.Module;
/*     */ import me.earth.phobos.features.setting.Setting;
/*     */ import me.earth.phobos.util.TextUtil;
/*     */ import me.earth.phobos.util.Timer;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.network.play.client.CPacketChatMessage;
/*     */ import net.minecraft.network.play.server.SPacketChat;
/*     */ import net.minecraft.util.math.Vec3i;
/*     */ import net.minecraft.util.text.ITextComponent;
/*     */ import net.minecraft.util.text.TextComponentString;
/*     */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*     */ 
/*     */ public class ChatModifier
/*     */   extends Module
/*     */ {
/*  22 */   public Setting<Suffix> suffix = register(new Setting("Suffix", Suffix.NONE, "Your Suffix."));
/*  23 */   public Setting<Boolean> clean = register(new Setting("CleanChat", Boolean.valueOf(false), "Cleans your chat"));
/*  24 */   public Setting<Boolean> infinite = register(new Setting("Infinite", Boolean.valueOf(false), "Makes your chat infinite."));
/*  25 */   public Setting<Boolean> autoQMain = register(new Setting("AutoQMain", Boolean.valueOf(false), "Spams AutoQMain"));
/*  26 */   public Setting<Boolean> qNotification = register(new Setting("QNotification", Boolean.valueOf(false), v -> ((Boolean)this.autoQMain.getValue()).booleanValue()));
/*  27 */   public Setting<Integer> qDelay = register(new Setting("QDelay", Integer.valueOf(9), Integer.valueOf(1), Integer.valueOf(90), v -> ((Boolean)this.autoQMain.getValue()).booleanValue()));
/*  28 */   public Setting<TextUtil.Color> timeStamps = register(new Setting("Time", TextUtil.Color.NONE));
/*  29 */   public Setting<TextUtil.Color> bracket = register(new Setting("Bracket", TextUtil.Color.WHITE, v -> (this.timeStamps.getValue() != TextUtil.Color.NONE)));
/*  30 */   public Setting<Boolean> space = register(new Setting("Space", Boolean.valueOf(true), v -> (this.timeStamps.getValue() != TextUtil.Color.NONE)));
/*  31 */   public Setting<Boolean> all = register(new Setting("All", Boolean.valueOf(false), v -> (this.timeStamps.getValue() != TextUtil.Color.NONE)));
/*  32 */   public Setting<Boolean> shrug = register(new Setting("Shrug", Boolean.valueOf(false)));
/*  33 */   public Setting<Boolean> disability = register(new Setting("Disability", Boolean.valueOf(false)));
/*     */   
/*  35 */   private final Timer timer = new Timer();
/*  36 */   private static ChatModifier INSTANCE = new ChatModifier();
/*     */   
/*     */   public ChatModifier() {
/*  39 */     super("Chat", "Modifies your chat", Module.Category.MISC, true, false, false);
/*  40 */     setInstance();
/*     */   }
/*     */   
/*     */   private void setInstance() {
/*  44 */     INSTANCE = this;
/*     */   }
/*     */   
/*     */   public static ChatModifier getInstance() {
/*  48 */     if (INSTANCE == null) {
/*  49 */       INSTANCE = new ChatModifier();
/*     */     }
/*  51 */     return INSTANCE;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onUpdate() {
/*  56 */     if (((Boolean)this.shrug.getValue()).booleanValue()) {
/*  57 */       mc.field_71439_g.func_71165_d(TextUtil.shrug);
/*  58 */       this.shrug.setValue(Boolean.valueOf(false));
/*     */     } 
/*     */     
/*  61 */     if (((Boolean)this.autoQMain.getValue()).booleanValue()) {
/*  62 */       if (!shouldSendMessage((EntityPlayer)mc.field_71439_g)) {
/*     */         return;
/*     */       }
/*  65 */       if (((Boolean)this.qNotification.getValue()).booleanValue()) {
/*  66 */         Command.sendMessage("<AutoQueueMain> Sending message: /queue main");
/*     */       }
/*  68 */       mc.field_71439_g.func_71165_d("/queue main");
/*  69 */       this.timer.reset();
/*     */     } 
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onPacketSend(PacketEvent.Send event) {
/*  75 */     if (event.getStage() == 0 && event.getPacket() instanceof CPacketChatMessage) {
/*  76 */       CPacketChatMessage packet = (CPacketChatMessage)event.getPacket();
/*  77 */       String s = packet.func_149439_c();
/*  78 */       if (s.startsWith("/"))
/*  79 */         return;  switch ((Suffix)this.suffix.getValue()) {
/*     */         case EARTH:
/*  81 */           s = s + " ⏐ 3ᴀʀᴛʜʜ4ᴄᴋ";
/*     */           break;
/*     */         case PHOBOS:
/*  84 */           s = s + " ⏐ ᴘʜᴏʙᴏꜱ";
/*     */           break;
/*     */       } 
/*     */       
/*  88 */       if (s.length() >= 256) s = s.substring(0, 256); 
/*  89 */       packet.field_149440_a = s;
/*     */     } 
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onPacketReceive(PacketEvent.Receive event) {
/*  95 */     if (event.getStage() == 0 && this.timeStamps.getValue() != TextUtil.Color.NONE && event.getPacket() instanceof SPacketChat) {
/*  96 */       if (!((SPacketChat)event.getPacket()).func_148916_d()) {
/*     */         return;
/*     */       }
/*     */       
/* 100 */       String originalMessage = ((SPacketChat)event.getPacket()).field_148919_a.func_150260_c();
/* 101 */       String message = getTimeString() + originalMessage;
/* 102 */       ((SPacketChat)event.getPacket()).field_148919_a = (ITextComponent)new TextComponentString(message);
/*     */     } 
/*     */   }
/*     */   
/*     */   public String getTimeString() {
/* 107 */     String date = (new SimpleDateFormat("k:mm")).format(new Date());
/* 108 */     return ((this.bracket.getValue() == TextUtil.Color.NONE) ? "" : TextUtil.coloredString("<", (TextUtil.Color)this.bracket.getValue())) + 
/* 109 */       TextUtil.coloredString(date, (TextUtil.Color)this.timeStamps.getValue()) + (
/* 110 */       (this.bracket.getValue() == TextUtil.Color.NONE) ? "" : TextUtil.coloredString(">", (TextUtil.Color)this.bracket.getValue())) + (
/* 111 */       ((Boolean)this.space.getValue()).booleanValue() ? " " : "") + "§r";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean shouldSendMessage(EntityPlayer player) {
/* 117 */     if (player.field_71093_bK != 1) {
/* 118 */       return false;
/*     */     }
/*     */     
/* 121 */     if (!this.timer.passedS(((Integer)this.qDelay.getValue()).intValue())) {
/* 122 */       return false;
/*     */     }
/*     */     
/* 125 */     return player.func_180425_c().equals(new Vec3i(0, 240, 0));
/*     */   }
/*     */   
/*     */   public enum Suffix
/*     */   {
/* 130 */     NONE,
/* 131 */     PHOBOS,
/* 132 */     EARTH;
/*     */   }
/*     */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\me\earth\phobos\features\modules\misc\ChatModifier.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */