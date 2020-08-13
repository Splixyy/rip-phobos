/*    */ package me.earth.phobos.features.modules.misc;
/*    */ 
/*    */ import me.earth.phobos.event.events.PacketEvent;
/*    */ import me.earth.phobos.features.command.Command;
/*    */ import me.earth.phobos.features.modules.Module;
/*    */ import me.earth.phobos.features.setting.Setting;
/*    */ import me.earth.phobos.util.Timer;
/*    */ import net.minecraft.network.play.server.SPacketPlayerPosLook;
/*    */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*    */ 
/*    */ 
/*    */ public class NoRotate
/*    */   extends Module
/*    */ {
/* 15 */   private Setting<Integer> waitDelay = register(new Setting("Delay", Integer.valueOf(2500), Integer.valueOf(0), Integer.valueOf(10000)));
/*    */ 
/*    */   
/* 18 */   private Timer timer = new Timer();
/*    */   private boolean cancelPackets = true;
/*    */   private boolean timerReset = false;
/*    */   
/*    */   public NoRotate() {
/* 23 */     super("NoRotate", "Dangerous to use might desync you.", Module.Category.MISC, true, false, false);
/*    */   }
/*    */ 
/*    */   
/*    */   public void onLogout() {
/* 28 */     this.cancelPackets = false;
/*    */   }
/*    */ 
/*    */   
/*    */   public void onLogin() {
/* 33 */     this.timer.reset();
/* 34 */     this.timerReset = true;
/*    */   }
/*    */ 
/*    */   
/*    */   public void onUpdate() {
/* 39 */     if (this.timerReset && !this.cancelPackets && this.timer.passedMs(((Integer)this.waitDelay.getValue()).intValue())) {
/* 40 */       Command.sendMessage("<NoRotate> §cThis module might desync you!");
/* 41 */       this.cancelPackets = true;
/* 42 */       this.timerReset = false;
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void onEnable() {
/* 48 */     Command.sendMessage("<NoRotate> §cThis module might desync you!");
/*    */   }
/*    */   
/*    */   @SubscribeEvent
/*    */   public void onPacketReceive(PacketEvent.Receive event) {
/* 53 */     if (event.getStage() == 0 && this.cancelPackets && 
/* 54 */       event.getPacket() instanceof SPacketPlayerPosLook) {
/* 55 */       SPacketPlayerPosLook packet = (SPacketPlayerPosLook)event.getPacket();
/* 56 */       packet.field_148936_d = mc.field_71439_g.field_70177_z;
/* 57 */       packet.field_148937_e = mc.field_71439_g.field_70125_A;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\me\earth\phobos\features\modules\misc\NoRotate.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */