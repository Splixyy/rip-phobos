/*    */ package me.earth.phobos.features.modules.misc;
/*    */ 
/*    */ import me.earth.phobos.features.modules.Module;
/*    */ import me.earth.phobos.features.setting.Setting;
/*    */ import me.earth.phobos.util.MathUtil;
/*    */ import me.earth.phobos.util.Timer;
/*    */ import net.minecraft.client.gui.GuiDisconnected;
/*    */ import net.minecraft.client.gui.GuiScreen;
/*    */ import net.minecraft.client.multiplayer.GuiConnecting;
/*    */ import net.minecraft.client.multiplayer.ServerData;
/*    */ import net.minecraftforge.client.event.GuiOpenEvent;
/*    */ import net.minecraftforge.event.world.WorldEvent;
/*    */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*    */ 
/*    */ public class AutoReconnect extends Module {
/*    */   private static ServerData serverData;
/* 17 */   private static AutoReconnect INSTANCE = new AutoReconnect();
/*    */   
/* 19 */   private final Setting<Integer> delay = register(new Setting("Delay", Integer.valueOf(5)));
/*    */   
/*    */   public AutoReconnect() {
/* 22 */     super("AutoReconnect", "Reconnects you if you disconnect.", Module.Category.MISC, true, false, false);
/* 23 */     setInstance();
/*    */   }
/*    */   
/*    */   private void setInstance() {
/* 27 */     INSTANCE = this;
/*    */   }
/*    */   
/*    */   public static AutoReconnect getInstance() {
/* 31 */     if (INSTANCE == null) {
/* 32 */       INSTANCE = new AutoReconnect();
/*    */     }
/* 34 */     return INSTANCE;
/*    */   }
/*    */   
/*    */   @SubscribeEvent
/*    */   public void sendPacket(GuiOpenEvent event) {
/* 39 */     if (event.getGui() instanceof GuiDisconnected) {
/* 40 */       updateLastConnectedServer();
/* 41 */       if (AutoLog.getInstance().isOff()) {
/* 42 */         GuiDisconnected disconnected = (GuiDisconnected)event.getGui();
/* 43 */         event.setGui((GuiScreen)new GuiDisconnectedHook(disconnected));
/*    */       } 
/*    */     } 
/*    */   }
/*    */   
/*    */   @SubscribeEvent
/*    */   public void onWorldUnload(WorldEvent.Unload event) {
/* 50 */     updateLastConnectedServer();
/*    */   }
/*    */   
/*    */   public void updateLastConnectedServer() {
/* 54 */     ServerData data = mc.func_147104_D();
/* 55 */     if (data != null)
/* 56 */       serverData = data; 
/*    */   }
/*    */   
/*    */   private class GuiDisconnectedHook
/*    */     extends GuiDisconnected
/*    */   {
/* 62 */     private final Timer timer = new Timer();
/*    */     
/*    */     public GuiDisconnectedHook(GuiDisconnected disconnected) {
/* 65 */       super(disconnected.field_146307_h, disconnected.field_146306_a, disconnected.field_146304_f);
/* 66 */       this.timer.reset();
/*    */     }
/*    */ 
/*    */     
/*    */     public void func_73876_c() {
/* 71 */       if (this.timer.passedS(((Integer)AutoReconnect.this.delay.getValue()).intValue())) {
/* 72 */         this.field_146297_k.func_147108_a((GuiScreen)new GuiConnecting(this.field_146307_h, this.field_146297_k, (AutoReconnect.serverData == null) ? this.field_146297_k.field_71422_O : AutoReconnect.serverData));
/*    */       }
/*    */     }
/*    */ 
/*    */     
/*    */     public void func_73863_a(int mouseX, int mouseY, float partialTicks) {
/* 78 */       super.func_73863_a(mouseX, mouseY, partialTicks);
/* 79 */       String s = "Reconnecting in " + MathUtil.round(((((Integer)AutoReconnect.this.delay.getValue()).intValue() * 1000) - this.timer.getPassedTimeMs()) / 1000.0D, 1);
/* 80 */       AutoReconnect.this.renderer.drawString(s, (this.field_146294_l / 2 - AutoReconnect.this.renderer.getStringWidth(s) / 2), (this.field_146295_m - 16), 16777215, true);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\me\earth\phobos\features\modules\misc\AutoReconnect.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */