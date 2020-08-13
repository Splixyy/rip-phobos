/*    */ package me.earth.phobos.manager;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import java.util.Objects;
/*    */ import java.util.UUID;
/*    */ import me.earth.phobos.features.Feature;
/*    */ import me.earth.phobos.features.setting.Setting;
/*    */ import me.earth.phobos.util.PlayerUtil;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ 
/*    */ public class FriendManager
/*    */   extends Feature
/*    */ {
/* 15 */   private List<Friend> friends = new ArrayList<>();
/*    */   
/*    */   public FriendManager() {
/* 18 */     super("Friends");
/*    */   }
/*    */   
/*    */   public boolean isFriend(String name) {
/* 22 */     cleanFriends();
/* 23 */     return this.friends.stream().anyMatch(friend -> friend.username.equalsIgnoreCase(name));
/*    */   }
/*    */   
/*    */   public boolean isFriend(EntityPlayer player) {
/* 27 */     return isFriend(player.func_70005_c_());
/*    */   }
/*    */   
/*    */   public void addFriend(String name) {
/* 31 */     Friend friend = getFriendByName(name);
/* 32 */     if (friend != null) {
/* 33 */       this.friends.add(friend);
/*    */     }
/* 35 */     cleanFriends();
/*    */   }
/*    */   
/*    */   public void removeFriend(String name) {
/* 39 */     cleanFriends();
/* 40 */     for (Friend friend : this.friends) {
/* 41 */       if (friend.getUsername().equalsIgnoreCase(name)) {
/* 42 */         this.friends.remove(friend);
/*    */         break;
/*    */       } 
/*    */     } 
/*    */   }
/*    */   
/*    */   public void onLoad() {
/* 49 */     this.friends = new ArrayList<>();
/* 50 */     clearSettings();
/*    */   }
/*    */   
/*    */   public void saveFriends() {
/* 54 */     clearSettings();
/* 55 */     cleanFriends();
/* 56 */     for (Friend friend : this.friends) {
/* 57 */       register(new Setting(friend.getUuid().toString(), friend.getUsername()));
/*    */     }
/*    */   }
/*    */   
/*    */   public void cleanFriends() {
/* 62 */     this.friends.stream().filter(Objects::nonNull).filter(friend -> (friend.getUsername() != null));
/*    */   }
/*    */   
/*    */   public List<Friend> getFriends() {
/* 66 */     cleanFriends();
/* 67 */     return this.friends;
/*    */   }
/*    */   
/*    */   public static class Friend
/*    */   {
/*    */     private final String username;
/*    */     private final UUID uuid;
/*    */     
/*    */     public Friend(String username, UUID uuid) {
/* 76 */       this.username = username;
/* 77 */       this.uuid = uuid;
/*    */     }
/*    */     
/*    */     public String getUsername() {
/* 81 */       return this.username;
/*    */     }
/*    */     
/*    */     public UUID getUuid() {
/* 85 */       return this.uuid;
/*    */     }
/*    */   }
/*    */   
/*    */   public Friend getFriendByName(String input) {
/* 90 */     UUID uuid = PlayerUtil.getUUIDFromName(input);
/* 91 */     if (uuid != null) {
/* 92 */       Friend friend = new Friend(input, uuid);
/* 93 */       return friend;
/*    */     } 
/* 95 */     return null;
/*    */   }
/*    */   
/*    */   public void addFriend(Friend friend) {
/* 99 */     this.friends.add(friend);
/*    */   }
/*    */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\me\earth\phobos\manager\FriendManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */