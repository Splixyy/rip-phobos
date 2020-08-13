/*    */ package me.earth.phobos.features.modules.misc;
/*    */ 
/*    */ import me.earth.phobos.Phobos;
/*    */ import me.earth.phobos.features.modules.Module;
/*    */ import me.earth.phobos.features.setting.Setting;
/*    */ import net.minecraft.client.network.NetworkPlayerInfo;
/*    */ import net.minecraft.scoreboard.ScorePlayerTeam;
/*    */ import net.minecraft.scoreboard.Team;
/*    */ 
/*    */ public class ExtraTab
/*    */   extends Module {
/* 12 */   public Setting<Integer> size = register(new Setting("Size", Integer.valueOf(250), Integer.valueOf(1), Integer.valueOf(1000)));
/*    */   
/* 14 */   private static ExtraTab INSTANCE = new ExtraTab();
/*    */   
/*    */   public ExtraTab() {
/* 17 */     super("ExtraTab", "Extends Tab.", Module.Category.MISC, false, false, false);
/* 18 */     setInstance();
/*    */   }
/*    */   
/*    */   private void setInstance() {
/* 22 */     INSTANCE = this;
/*    */   }
/*    */   
/*    */   public static String getPlayerName(NetworkPlayerInfo networkPlayerInfoIn) {
/* 26 */     String name = (networkPlayerInfoIn.func_178854_k() != null) ? networkPlayerInfoIn.func_178854_k().func_150254_d() : ScorePlayerTeam.func_96667_a((Team)networkPlayerInfoIn.func_178850_i(), networkPlayerInfoIn.func_178845_a().getName());
/* 27 */     if (Phobos.friendManager.isFriend(name)) {
/* 28 */       return "§b" + name;
/*    */     }
/*    */     
/* 31 */     return name;
/*    */   }
/*    */   
/*    */   public static ExtraTab getINSTANCE() {
/* 35 */     if (INSTANCE == null) {
/* 36 */       INSTANCE = new ExtraTab();
/*    */     }
/* 38 */     return INSTANCE;
/*    */   }
/*    */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\me\earth\phobos\features\modules\misc\ExtraTab.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */