/*    */ package me.earth.phobos.features.notifications;
/*    */ 
/*    */ import me.earth.phobos.Phobos;
/*    */ import me.earth.phobos.features.modules.client.HUD;
/*    */ import me.earth.phobos.util.RenderUtil;
/*    */ import me.earth.phobos.util.Timer;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.gui.ScaledResolution;
/*    */ 
/*    */ public class Notifications {
/*    */   private final String text;
/*    */   private final long disableTime;
/*    */   private final float width;
/* 14 */   private final Timer timer = new Timer();
/*    */   public Notifications(String text, long disableTime) {
/* 16 */     this.text = text;
/* 17 */     this.disableTime = disableTime;
/* 18 */     this.width = ((HUD)Phobos.moduleManager.getModuleByClass(HUD.class)).renderer.getStringWidth(text);
/* 19 */     this.timer.reset();
/*    */   }
/*    */   
/*    */   public void onDraw(int y) {
/* 23 */     ScaledResolution scaledResolution = new ScaledResolution(Minecraft.func_71410_x());
/* 24 */     if (this.timer.passedMs(this.disableTime)) Phobos.notificationManager.getNotifications().remove(this); 
/* 25 */     RenderUtil.drawRect((scaledResolution.func_78326_a() - 4) - this.width, y, (scaledResolution.func_78326_a() - 2), (y + ((HUD)Phobos.moduleManager.getModuleByClass(HUD.class)).renderer.getFontHeight() + 3), 1962934272);
/* 26 */     ((HUD)Phobos.moduleManager.getModuleByClass(HUD.class)).renderer.drawString(this.text, scaledResolution.func_78326_a() - this.width - 3.0F, (y + 2), -1, true);
/*    */   }
/*    */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\me\earth\phobos\features\notifications\Notifications.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */