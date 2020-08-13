/*    */ package me.earth.phobos.manager;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import me.earth.phobos.Phobos;
/*    */ import me.earth.phobos.features.modules.client.HUD;
/*    */ import me.earth.phobos.features.notifications.Notifications;
/*    */ 
/*    */ 
/*    */ public class NotificationManager
/*    */ {
/* 11 */   private final ArrayList<Notifications> notifications = new ArrayList<>();
/*    */   
/*    */   public void handleNotifications(int posY) {
/* 14 */     for (int i = 0; i < getNotifications().size(); i++) {
/* 15 */       ((Notifications)getNotifications().get(i)).onDraw(posY);
/* 16 */       posY -= ((HUD)Phobos.moduleManager.getModuleByClass((Class)HUD.class)).renderer.getFontHeight() + 5;
/*    */     } 
/*    */   }
/*    */   
/*    */   public void addNotification(String text, long duration) {
/* 21 */     getNotifications().add(new Notifications(text, duration));
/*    */   }
/*    */   
/*    */   public ArrayList<Notifications> getNotifications() {
/* 25 */     return this.notifications;
/*    */   }
/*    */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\me\earth\phobos\manager\NotificationManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */