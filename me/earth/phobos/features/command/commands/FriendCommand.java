/*    */ package me.earth.phobos.features.command.commands;
/*    */ 
/*    */ import me.earth.phobos.Phobos;
/*    */ import me.earth.phobos.features.command.Command;
/*    */ import me.earth.phobos.manager.FriendManager;
/*    */ 
/*    */ public class FriendCommand
/*    */   extends Command
/*    */ {
/*    */   public FriendCommand() {
/* 11 */     super("friend", new String[] { "<add/del/name/clear>", "<name>" });
/*    */   }
/*    */ 
/*    */   
/*    */   public void execute(String[] commands) {
/* 16 */     if (commands.length == 1) {
/* 17 */       if (Phobos.friendManager.getFriends().isEmpty()) {
/* 18 */         sendMessage("You currently dont have any friends added.");
/*    */       } else {
/* 20 */         String f = "Friends: ";
/* 21 */         for (FriendManager.Friend friend : Phobos.friendManager.getFriends()) {
/*    */           try {
/* 23 */             f = f + friend.getUsername() + ", ";
/* 24 */           } catch (Exception e) {}
/*    */         } 
/*    */ 
/*    */         
/* 28 */         sendMessage(f);
/*    */       } 
/*    */       
/*    */       return;
/*    */     } 
/* 33 */     if (commands.length == 2) {
/* 34 */       switch (commands[0]) {
/*    */         case "reset":
/* 36 */           Phobos.friendManager.onLoad();
/* 37 */           sendMessage("Friends got reset.");
/*    */           return;
/*    */       } 
/* 40 */       sendMessage(commands[0] + (Phobos.friendManager.isFriend(commands[0]) ? " is friended." : " isnt friended."));
/*    */ 
/*    */       
/*    */       return;
/*    */     } 
/*    */     
/* 46 */     if (commands.length >= 2) {
/* 47 */       switch (commands[0]) {
/*    */         case "add":
/* 49 */           Phobos.friendManager.addFriend(commands[1]);
/* 50 */           sendMessage("§b" + commands[1] + " has been friended");
/*    */           return;
/*    */         case "del":
/* 53 */           Phobos.friendManager.removeFriend(commands[1]);
/* 54 */           sendMessage("§c" + commands[1] + " has been unfriended");
/*    */           return;
/*    */       } 
/* 57 */       sendMessage("§cBad Command, try: friend <add/del/name> <name>.");
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\me\earth\phobos\features\command\commands\FriendCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */