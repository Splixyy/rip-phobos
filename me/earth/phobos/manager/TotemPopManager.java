/*    */ package me.earth.phobos.manager;
/*    */ 
/*    */ import java.util.HashSet;
/*    */ import java.util.Map;
/*    */ import java.util.Set;
/*    */ import java.util.concurrent.ConcurrentHashMap;
/*    */ import me.earth.phobos.Phobos;
/*    */ import me.earth.phobos.features.Feature;
/*    */ import me.earth.phobos.features.command.Command;
/*    */ import me.earth.phobos.features.modules.client.Notifications;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class TotemPopManager
/*    */   extends Feature
/*    */ {
/*    */   private Notifications notifications;
/* 19 */   private Map<EntityPlayer, Integer> poplist = new ConcurrentHashMap<>();
/* 20 */   private Set<EntityPlayer> toAnnounce = new HashSet<>();
/*    */   
/*    */   public void onUpdate() {
/* 23 */     if (this.notifications.totemAnnounce.passedMs(((Integer)this.notifications.delay.getValue()).intValue()) && this.notifications.isOn() && ((Boolean)this.notifications.totemPops.getValue()).booleanValue()) {
/* 24 */       for (EntityPlayer player : this.toAnnounce) {
/* 25 */         if (player == null) {
/*    */           continue;
/*    */         }
/* 28 */         Command.sendMessage("§c" + player.func_70005_c_() + " popped " + "§a" + getTotemPops(player) + "§c" + " Totem" + ((getTotemPops(player) == 1) ? "" : "s") + ".", ((Boolean)this.notifications.totemNoti.getValue()).booleanValue());
/* 29 */         this.toAnnounce.remove(player);
/* 30 */         this.notifications.totemAnnounce.reset();
/*    */       } 
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public void onLogout() {
/* 37 */     onOwnLogout(((Boolean)this.notifications.clearOnLogout.getValue()).booleanValue());
/*    */   }
/*    */   
/*    */   public void init() {
/* 41 */     this.notifications = Phobos.moduleManager.<Notifications>getModuleByClass(Notifications.class);
/*    */   }
/*    */   
/*    */   public void onTotemPop(EntityPlayer player) {
/* 45 */     popTotem(player);
/* 46 */     if (!player.equals(mc.field_71439_g)) {
/* 47 */       this.toAnnounce.add(player);
/* 48 */       this.notifications.totemAnnounce.reset();
/*    */     } 
/*    */   }
/*    */   
/*    */   public void onDeath(EntityPlayer player) {
/* 53 */     if (getTotemPops(player) != 0 && !player.equals(mc.field_71439_g) && this.notifications.isOn() && ((Boolean)this.notifications.totemPops.getValue()).booleanValue()) {
/* 54 */       Command.sendMessage("§c" + player.func_70005_c_() + " died after popping " + "§a" + getTotemPops(player) + "§c" + " Totem" + ((getTotemPops(player) == 1) ? "" : "s") + ".", ((Boolean)this.notifications.totemNoti.getValue()).booleanValue());
/* 55 */       this.toAnnounce.remove(player);
/*    */     } 
/* 57 */     resetPops(player);
/*    */   }
/*    */   
/*    */   public void onLogout(EntityPlayer player, boolean clearOnLogout) {
/* 61 */     if (clearOnLogout) {
/* 62 */       resetPops(player);
/*    */     }
/*    */   }
/*    */   
/*    */   public void onOwnLogout(boolean clearOnLogout) {
/* 67 */     if (clearOnLogout) {
/* 68 */       clearList();
/*    */     }
/*    */   }
/*    */   
/*    */   public void clearList() {
/* 73 */     this.poplist = new ConcurrentHashMap<>();
/*    */   }
/*    */   
/*    */   public void resetPops(EntityPlayer player) {
/* 77 */     setTotemPops(player, 0);
/*    */   }
/*    */   
/*    */   public void popTotem(EntityPlayer player) {
/* 81 */     this.poplist.merge(player, Integer.valueOf(1), Integer::sum);
/*    */   }
/*    */   
/*    */   public void setTotemPops(EntityPlayer player, int amount) {
/* 85 */     this.poplist.put(player, Integer.valueOf(amount));
/*    */   }
/*    */   
/*    */   public int getTotemPops(EntityPlayer player) {
/* 89 */     Integer pops = this.poplist.get(player);
/* 90 */     if (pops == null) {
/* 91 */       return 0;
/*    */     }
/* 93 */     return pops.intValue();
/*    */   }
/*    */   
/*    */   public String getTotemPopString(EntityPlayer player) {
/* 97 */     return "§f" + ((getTotemPops(player) <= 0) ? "" : ("-" + getTotemPops(player) + " "));
/*    */   }
/*    */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\me\earth\phobos\manager\TotemPopManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */