/*    */ package me.earth.phobos.features.modules.misc;
/*    */ 
/*    */ import java.util.Queue;
/*    */ import java.util.concurrent.ConcurrentLinkedQueue;
/*    */ import me.earth.phobos.event.events.PacketEvent;
/*    */ import me.earth.phobos.features.modules.Module;
/*    */ import me.earth.phobos.features.setting.Setting;
/*    */ import me.earth.phobos.util.MathUtil;
/*    */ import me.earth.phobos.util.Timer;
/*    */ import net.minecraft.network.Packet;
/*    */ import net.minecraft.network.play.client.CPacketKeepAlive;
/*    */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*    */ 
/*    */ public class PingSpoof
/*    */   extends Module
/*    */ {
/* 17 */   private Setting<Boolean> seconds = register(new Setting("Seconds", Boolean.valueOf(false)));
/* 18 */   private Setting<Integer> delay = register(new Setting("DelayMS", Integer.valueOf(20), Integer.valueOf(0), Integer.valueOf(1000), v -> !((Boolean)this.seconds.getValue()).booleanValue()));
/* 19 */   private Setting<Integer> secondDelay = register(new Setting("DelayS", Integer.valueOf(5), Integer.valueOf(0), Integer.valueOf(30), v -> ((Boolean)this.seconds.getValue()).booleanValue()));
/* 20 */   private Setting<Boolean> extraPacket = register(new Setting("Packet", Boolean.valueOf(true)));
/* 21 */   private Setting<Boolean> offOnLogout = register(new Setting("Logout", Boolean.valueOf(false)));
/*    */   
/* 23 */   private Queue<Packet<?>> packets = new ConcurrentLinkedQueue<>();
/* 24 */   private Timer timer = new Timer();
/*    */   private boolean receive = true;
/*    */   
/*    */   public PingSpoof() {
/* 28 */     super("PingSpoof", "Spoofs your ping!", Module.Category.MISC, true, false, false);
/*    */   }
/*    */ 
/*    */   
/*    */   public void onLoad() {
/* 33 */     if (((Boolean)this.offOnLogout.getValue()).booleanValue()) {
/* 34 */       disable();
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public void onLogout() {
/* 40 */     if (((Boolean)this.offOnLogout.getValue()).booleanValue()) {
/* 41 */       disable();
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public void onUpdate() {
/* 47 */     clearQueue();
/*    */   }
/*    */ 
/*    */   
/*    */   public void onDisable() {
/* 52 */     clearQueue();
/*    */   }
/*    */   
/*    */   @SubscribeEvent
/*    */   public void onPacketSend(PacketEvent.Send event) {
/* 57 */     if (this.receive && mc.field_71439_g != null && !mc.func_71356_B() && mc.field_71439_g.func_70089_S() && event.getStage() == 0 && event.getPacket() instanceof CPacketKeepAlive) {
/* 58 */       this.packets.add(event.getPacket());
/* 59 */       event.setCanceled(true);
/*    */     } 
/*    */   }
/*    */   
/*    */   public void clearQueue() {
/* 64 */     if (mc.field_71439_g != null && !mc.func_71356_B() && mc.field_71439_g.func_70089_S() && ((!((Boolean)this.seconds.getValue()).booleanValue() && this.timer.passedMs(((Integer)this.delay.getValue()).intValue())) || (((Boolean)this.seconds.getValue()).booleanValue() && this.timer.passedS(((Integer)this.secondDelay.getValue()).intValue())))) {
/* 65 */       double limit = MathUtil.getIncremental(Math.random() * 10.0D, 1.0D);
/* 66 */       this.receive = false;
/* 67 */       for (int i = 0; i < limit; i++) {
/* 68 */         Packet<?> packet = this.packets.poll();
/* 69 */         if (packet != null) {
/* 70 */           mc.field_71439_g.field_71174_a.func_147297_a(packet);
/*    */         }
/*    */       } 
/*    */       
/* 74 */       if (((Boolean)this.extraPacket.getValue()).booleanValue()) {
/* 75 */         mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketKeepAlive(10000L));
/*    */       }
/* 77 */       this.timer.reset();
/* 78 */       this.receive = true;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\me\earth\phobos\features\modules\misc\PingSpoof.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */