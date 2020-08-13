/*     */ package me.earth.phobos.util;
/*     */ 
/*     */ import java.math.BigDecimal;
/*     */ import java.math.RoundingMode;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Calendar;
/*     */ import java.util.Comparator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Random;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.util.math.AxisAlignedBB;
/*     */ import net.minecraft.util.math.BlockPos;
/*     */ import net.minecraft.util.math.MathHelper;
/*     */ import net.minecraft.util.math.Vec3d;
/*     */ 
/*     */ public class MathUtil
/*     */   implements Util
/*     */ {
/*  22 */   private static final Random random = new Random();
/*     */   
/*     */   public static int getRandom(int min, int max) {
/*  25 */     return min + random.nextInt(max - min + 1);
/*     */   }
/*     */   
/*     */   public static double getRandom(double min, double max) {
/*  29 */     return MathHelper.func_151237_a(min + random.nextDouble() * max, min, max);
/*     */   }
/*     */   
/*     */   public static float getRandom(float min, float max) {
/*  33 */     return MathHelper.func_76131_a(min + random.nextFloat() * max, min, max);
/*     */   }
/*     */   
/*     */   public static int clamp(int num, int min, int max) {
/*  37 */     return (num < min) ? min : Math.min(num, max);
/*     */   }
/*     */   
/*     */   public static float clamp(float num, float min, float max) {
/*  41 */     return (num < min) ? min : Math.min(num, max);
/*     */   }
/*     */   
/*     */   public static double clamp(double num, double min, double max) {
/*  45 */     return (num < min) ? min : Math.min(num, max);
/*     */   }
/*     */   
/*     */   public static float sin(float value) {
/*  49 */     return MathHelper.func_76126_a(value);
/*     */   }
/*     */   
/*     */   public static float cos(float value) {
/*  53 */     return MathHelper.func_76134_b(value);
/*     */   }
/*     */   
/*     */   public static float wrapDegrees(float value) {
/*  57 */     return MathHelper.func_76142_g(value);
/*     */   }
/*     */   
/*     */   public static double wrapDegrees(double value) {
/*  61 */     return MathHelper.func_76138_g(value);
/*     */   }
/*     */   
/*     */   public static Vec3d roundVec(Vec3d vec3d, int places) {
/*  65 */     return new Vec3d(round(vec3d.field_72450_a, places), round(vec3d.field_72448_b, places), round(vec3d.field_72449_c, places));
/*     */   }
/*     */   public static double square(double input) {
/*  68 */     return input * input;
/*     */   }
/*     */   public static double round(double value, int places) {
/*  71 */     if (places < 0) throw new IllegalArgumentException(); 
/*  72 */     BigDecimal bd = BigDecimal.valueOf(value);
/*  73 */     bd = bd.setScale(places, RoundingMode.FLOOR);
/*  74 */     return bd.doubleValue();
/*     */   }
/*     */   
/*     */   public static float wrap(float valI) {
/*  78 */     float val = valI % 360.0F;
/*  79 */     if (val >= 180.0F)
/*  80 */       val -= 360.0F; 
/*  81 */     if (val < -180.0F)
/*  82 */       val += 360.0F; 
/*  83 */     return val;
/*     */   }
/*     */   
/*     */   public static Vec3d direction(float yaw) {
/*  87 */     return new Vec3d(Math.cos(degToRad((yaw + 90.0F))), 0.0D, Math.sin(degToRad((yaw + 90.0F))));
/*     */   }
/*     */   
/*     */   public static float round(float value, int places) {
/*  91 */     if (places < 0) throw new IllegalArgumentException(); 
/*  92 */     BigDecimal bd = BigDecimal.valueOf(value);
/*  93 */     bd = bd.setScale(places, RoundingMode.FLOOR);
/*  94 */     return bd.floatValue();
/*     */   }
/*     */   
/*     */   public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map, boolean descending) {
/*  98 */     List<Map.Entry<K, V>> list = new LinkedList<>(map.entrySet());
/*  99 */     if (descending) {
/* 100 */       list.sort((Comparator)Map.Entry.comparingByValue(Comparator.reverseOrder()));
/*     */     } else {
/* 102 */       list.sort((Comparator)Map.Entry.comparingByValue());
/*     */     } 
/*     */     
/* 105 */     Map<K, V> result = new LinkedHashMap<>();
/* 106 */     for (Map.Entry<K, V> entry : list) {
/* 107 */       result.put(entry.getKey(), entry.getValue());
/*     */     }
/* 109 */     return result;
/*     */   }
/*     */   
/*     */   public static String getTimeOfDay() {
/* 113 */     Calendar c = Calendar.getInstance();
/* 114 */     int timeOfDay = c.get(11);
/*     */     
/* 116 */     if (timeOfDay < 12)
/* 117 */       return "Good Morning "; 
/* 118 */     if (timeOfDay < 16)
/* 119 */       return "Good Afternoon "; 
/* 120 */     if (timeOfDay < 21) {
/* 121 */       return "Good Evening ";
/*     */     }
/* 123 */     return "Good Night ";
/*     */   }
/*     */ 
/*     */   
/*     */   public static double radToDeg(double rad) {
/* 128 */     return rad * 57.295780181884766D;
/*     */   }
/*     */   
/*     */   public static double degToRad(double deg) {
/* 132 */     return deg * 0.01745329238474369D;
/*     */   }
/*     */   
/*     */   public static double getIncremental(double val, double inc) {
/* 136 */     double one = 1.0D / inc;
/* 137 */     return Math.round(val * one) / one;
/*     */   }
/*     */   
/*     */   public static double[] directionSpeed(double speed) {
/* 141 */     float forward = mc.field_71439_g.field_71158_b.field_192832_b;
/* 142 */     float side = mc.field_71439_g.field_71158_b.field_78902_a;
/* 143 */     float yaw = mc.field_71439_g.field_70126_B + (mc.field_71439_g.field_70177_z - mc.field_71439_g.field_70126_B) * mc.func_184121_ak();
/* 144 */     if (forward != 0.0F) {
/* 145 */       if (side > 0.0F) {
/* 146 */         yaw += ((forward > 0.0F) ? -45 : 45);
/* 147 */       } else if (side < 0.0F) {
/* 148 */         yaw += ((forward > 0.0F) ? 45 : -45);
/*     */       } 
/* 150 */       side = 0.0F;
/* 151 */       if (forward > 0.0F) {
/* 152 */         forward = 1.0F;
/* 153 */       } else if (forward < 0.0F) {
/* 154 */         forward = -1.0F;
/*     */       } 
/*     */     } 
/* 157 */     double sin = Math.sin(Math.toRadians((yaw + 90.0F)));
/* 158 */     double cos = Math.cos(Math.toRadians((yaw + 90.0F)));
/* 159 */     double posX = forward * speed * cos + side * speed * sin;
/* 160 */     double posZ = forward * speed * sin - side * speed * cos;
/* 161 */     return new double[] { posX, posZ };
/*     */   }
/*     */   
/*     */   public static List<Vec3d> getBlockBlocks(Entity entity) {
/* 165 */     List<Vec3d> vec3ds = new ArrayList<>();
/* 166 */     AxisAlignedBB bb = entity.func_174813_aQ();
/* 167 */     double y = entity.field_70163_u;
/* 168 */     double minX = round(bb.field_72340_a, 0);
/* 169 */     double minZ = round(bb.field_72339_c, 0);
/* 170 */     double maxX = round(bb.field_72336_d, 0);
/* 171 */     double maxZ = round(bb.field_72334_f, 0);
/* 172 */     if (minX != maxX) {
/* 173 */       vec3ds.add(new Vec3d(minX, y, minZ));
/* 174 */       vec3ds.add(new Vec3d(maxX, y, minZ));
/* 175 */       if (minZ != maxZ) {
/* 176 */         vec3ds.add(new Vec3d(minX, y, maxZ));
/* 177 */         vec3ds.add(new Vec3d(maxX, y, maxZ));
/* 178 */         return vec3ds;
/*     */       } 
/* 180 */     } else if (minZ != maxZ) {
/* 181 */       vec3ds.add(new Vec3d(minX, y, minZ));
/* 182 */       vec3ds.add(new Vec3d(minX, y, maxZ));
/* 183 */       return vec3ds;
/*     */     } 
/* 185 */     vec3ds.add(entity.func_174791_d());
/* 186 */     return vec3ds;
/*     */   }
/*     */   
/*     */   public static boolean areVec3dsAligned(Vec3d vec3d1, Vec3d vec3d2) {
/* 190 */     return areVec3dsAlignedRetarded(vec3d1, vec3d2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean areVec3dsAlignedRetarded(Vec3d vec3d1, Vec3d vec3d2) {
/* 197 */     BlockPos pos1 = new BlockPos(vec3d1);
/* 198 */     BlockPos pos2 = new BlockPos(vec3d2.field_72450_a, vec3d1.field_72448_b, vec3d2.field_72449_c);
/* 199 */     return pos1.equals(pos2);
/*     */   }
/*     */   
/*     */   public static float[] calcAngle(Vec3d from, Vec3d to) {
/* 203 */     double difX = to.field_72450_a - from.field_72450_a;
/* 204 */     double difY = (to.field_72448_b - from.field_72448_b) * -1.0D;
/* 205 */     double difZ = to.field_72449_c - from.field_72449_c;
/* 206 */     double dist = MathHelper.func_76133_a(difX * difX + difZ * difZ);
/* 207 */     return new float[] { (float)MathHelper.func_76138_g(Math.toDegrees(Math.atan2(difZ, difX)) - 90.0D), (float)MathHelper.func_76138_g(Math.toDegrees(Math.atan2(difY, dist))) };
/*     */   }
/*     */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\me\earth\phobo\\util\MathUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */