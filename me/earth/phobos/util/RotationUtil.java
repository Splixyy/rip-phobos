/*    */ package me.earth.phobos.util;
/*    */ 
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.network.Packet;
/*    */ import net.minecraft.network.play.client.CPacketPlayer;
/*    */ import net.minecraft.util.math.MathHelper;
/*    */ import net.minecraft.util.math.Vec3d;
/*    */ 
/*    */ public class RotationUtil implements Util {
/*    */   public static Vec3d getEyesPos() {
/* 12 */     return new Vec3d(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u + mc.field_71439_g.func_70047_e(), mc.field_71439_g.field_70161_v);
/*    */   }
/*    */   
/*    */   public static double[] calculateLookAt(double px, double py, double pz, EntityPlayer me) {
/* 16 */     double dirx = me.field_70165_t - px;
/* 17 */     double diry = me.field_70163_u - py;
/* 18 */     double dirz = me.field_70161_v - pz;
/*    */     
/* 20 */     double len = Math.sqrt(dirx * dirx + diry * diry + dirz * dirz);
/*    */     
/* 22 */     dirx /= len;
/* 23 */     diry /= len;
/* 24 */     dirz /= len;
/*    */     
/* 26 */     double pitch = Math.asin(diry);
/* 27 */     double yaw = Math.atan2(dirz, dirx);
/*    */     
/* 29 */     pitch = pitch * 180.0D / Math.PI;
/* 30 */     yaw = yaw * 180.0D / Math.PI;
/*    */     
/* 32 */     yaw += 90.0D;
/*    */     
/* 34 */     return new double[] { yaw, pitch };
/*    */   }
/*    */   
/*    */   public static float[] getLegitRotations(Vec3d vec) {
/* 38 */     Vec3d eyesPos = getEyesPos();
/* 39 */     double diffX = vec.field_72450_a - eyesPos.field_72450_a;
/* 40 */     double diffY = vec.field_72448_b - eyesPos.field_72448_b;
/* 41 */     double diffZ = vec.field_72449_c - eyesPos.field_72449_c;
/* 42 */     double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);
/*    */     
/* 44 */     float yaw = (float)Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0F;
/* 45 */     float pitch = (float)-Math.toDegrees(Math.atan2(diffY, diffXZ));
/*    */     
/* 47 */     return new float[] { mc.field_71439_g.field_70177_z + 
/* 48 */         MathHelper.func_76142_g(yaw - mc.field_71439_g.field_70177_z), mc.field_71439_g.field_70125_A + 
/* 49 */         MathHelper.func_76142_g(pitch - mc.field_71439_g.field_70125_A) };
/*    */   }
/*    */ 
/*    */   
/*    */   public static void faceYawAndPitch(float yaw, float pitch) {
/* 54 */     mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Rotation(yaw, pitch, mc.field_71439_g.field_70122_E));
/*    */   }
/*    */   
/*    */   public static void faceVector(Vec3d vec, boolean normalizeAngle) {
/* 58 */     float[] rotations = getLegitRotations(vec);
/* 59 */     mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Rotation(rotations[0], normalizeAngle ? MathHelper.func_180184_b((int)rotations[1], 360) : rotations[1], mc.field_71439_g.field_70122_E));
/*    */   }
/*    */   
/*    */   public static void faceEntity(Entity entity) {
/* 63 */     float[] angle = MathUtil.calcAngle(mc.field_71439_g.func_174824_e(mc.func_184121_ak()), entity.func_174824_e(mc.func_184121_ak()));
/* 64 */     faceYawAndPitch(angle[0], angle[1]);
/*    */   }
/*    */   
/*    */   public static float[] getAngle(Entity entity) {
/* 68 */     return MathUtil.calcAngle(mc.field_71439_g.func_174824_e(mc.func_184121_ak()), entity.func_174824_e(mc.func_184121_ak()));
/*    */   }
/*    */   
/*    */   public static int getDirection4D() {
/* 72 */     return MathHelper.func_76128_c((mc.field_71439_g.field_70177_z * 4.0F / 360.0F) + 0.5D) & 0x3;
/*    */   }
/*    */   
/*    */   public static String getDirection4D(boolean northRed) {
/* 76 */     int dirnumber = getDirection4D();
/* 77 */     if (dirnumber == 0) {
/* 78 */       return "South (+Z)";
/*    */     }
/* 80 */     if (dirnumber == 1) {
/* 81 */       return "West (-X)";
/*    */     }
/* 83 */     if (dirnumber == 2) {
/* 84 */       return (northRed ? "§c" : "") + "North (-Z)";
/*    */     }
/* 86 */     if (dirnumber == 3) {
/* 87 */       return "East (+X)";
/*    */     }
/* 89 */     return "Loading...";
/*    */   }
/*    */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\me\earth\phobo\\util\RotationUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */