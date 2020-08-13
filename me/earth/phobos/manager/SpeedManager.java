/*    */ package me.earth.phobos.manager;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ import me.earth.phobos.features.Feature;
/*    */ import me.earth.phobos.features.modules.client.Managers;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.util.math.MathHelper;
/*    */ 
/*    */ public class SpeedManager
/*    */   extends Feature {
/* 13 */   public double firstJumpSpeed = 0.0D;
/* 14 */   public double lastJumpSpeed = 0.0D;
/* 15 */   public double percentJumpSpeedChanged = 0.0D;
/* 16 */   public double jumpSpeedChanged = 0.0D;
/*    */   public static boolean didJumpThisTick = false;
/*    */   public static boolean isJumping = false;
/*    */   public boolean didJumpLastTick = false;
/* 20 */   public long jumpInfoStartTime = 0L;
/*    */   public boolean wasFirstJump = true;
/*    */   public static final double LAST_JUMP_INFO_DURATION_DEFAULT = 3.0D;
/* 23 */   public double speedometerCurrentSpeed = 0.0D;
/*    */   
/* 25 */   public HashMap<EntityPlayer, Double> playerSpeeds = new HashMap<>();
/* 26 */   private int distancer = 20;
/*    */   
/*    */   public static void setDidJumpThisTick(boolean val) {
/* 29 */     didJumpThisTick = val;
/*    */   }
/*    */   
/*    */   public static void setIsJumping(boolean val) {
/* 33 */     isJumping = val;
/*    */   }
/*    */   
/*    */   public float lastJumpInfoTimeRemaining() {
/* 37 */     return (float)(Minecraft.func_71386_F() - this.jumpInfoStartTime) / 1000.0F;
/*    */   }
/*    */   
/*    */   public void updateValues() {
/* 41 */     double distTraveledLastTickX = mc.field_71439_g.field_70165_t - mc.field_71439_g.field_70169_q;
/* 42 */     double distTraveledLastTickZ = mc.field_71439_g.field_70161_v - mc.field_71439_g.field_70166_s;
/* 43 */     this.speedometerCurrentSpeed = distTraveledLastTickX * distTraveledLastTickX + distTraveledLastTickZ * distTraveledLastTickZ;
/* 44 */     if (didJumpThisTick && (!mc.field_71439_g.field_70122_E || isJumping)) {
/* 45 */       if (didJumpThisTick && !this.didJumpLastTick) {
/* 46 */         this.wasFirstJump = (this.lastJumpSpeed == 0.0D);
/* 47 */         this.percentJumpSpeedChanged = (this.speedometerCurrentSpeed != 0.0D) ? (this.speedometerCurrentSpeed / this.lastJumpSpeed - 1.0D) : -1.0D;
/* 48 */         this.jumpSpeedChanged = this.speedometerCurrentSpeed - this.lastJumpSpeed;
/* 49 */         this.jumpInfoStartTime = Minecraft.func_71386_F();
/* 50 */         this.lastJumpSpeed = this.speedometerCurrentSpeed;
/* 51 */         this.firstJumpSpeed = this.wasFirstJump ? this.lastJumpSpeed : 0.0D;
/*    */       } 
/*    */       
/* 54 */       this.didJumpLastTick = didJumpThisTick;
/*    */     } else {
/* 56 */       this.didJumpLastTick = false;
/* 57 */       this.lastJumpSpeed = 0.0D;
/*    */     } 
/*    */     
/* 60 */     if (((Boolean)(Managers.getInstance()).speed.getValue()).booleanValue()) {
/* 61 */       updatePlayers();
/*    */     }
/*    */   }
/*    */   
/*    */   public void updatePlayers() {
/* 66 */     for (EntityPlayer player : mc.field_71441_e.field_73010_i) {
/* 67 */       if (mc.field_71439_g.func_70068_e((Entity)player) < (this.distancer * this.distancer)) {
/* 68 */         double distTraveledLastTickX = player.field_70165_t - player.field_70169_q;
/* 69 */         double distTraveledLastTickZ = player.field_70161_v - player.field_70166_s;
/* 70 */         double playerSpeed = distTraveledLastTickX * distTraveledLastTickX + distTraveledLastTickZ * distTraveledLastTickZ;
/* 71 */         this.playerSpeeds.put(player, Double.valueOf(playerSpeed));
/*    */       } 
/*    */     } 
/*    */   }
/*    */   
/*    */   public double getPlayerSpeed(EntityPlayer player) {
/* 77 */     if (this.playerSpeeds.get(player) == null) {
/* 78 */       return 0.0D;
/*    */     }
/* 80 */     return turnIntoKpH(((Double)this.playerSpeeds.get(player)).doubleValue());
/*    */   }
/*    */   
/*    */   public double turnIntoKpH(double input) {
/* 84 */     return MathHelper.func_76133_a(input) * 71.2729367892D;
/*    */   }
/*    */   
/*    */   public double getSpeedKpH() {
/* 88 */     double speedometerkphdouble = turnIntoKpH(this.speedometerCurrentSpeed);
/* 89 */     speedometerkphdouble = Math.round(10.0D * speedometerkphdouble) / 10.0D;
/* 90 */     return speedometerkphdouble;
/*    */   }
/*    */   
/*    */   public double getSpeedMpS() {
/* 94 */     double speedometerMpsdouble = turnIntoKpH(this.speedometerCurrentSpeed) / 3.6D;
/* 95 */     speedometerMpsdouble = Math.round(10.0D * speedometerMpsdouble) / 10.0D;
/* 96 */     return speedometerMpsdouble;
/*    */   }
/*    */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\me\earth\phobos\manager\SpeedManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */