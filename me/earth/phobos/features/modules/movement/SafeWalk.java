/*    */ package me.earth.phobos.features.modules.movement;
/*    */ 
/*    */ import me.earth.phobos.event.events.MoveEvent;
/*    */ import me.earth.phobos.features.modules.Module;
/*    */ import net.minecraft.client.entity.EntityPlayerSP;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*    */ 
/*    */ public class SafeWalk extends Module {
/*    */   public SafeWalk() {
/* 11 */     super("SafeWalk", "Walks safe", Module.Category.MOVEMENT, true, false, false);
/*    */   }
/*    */   
/*    */   @SubscribeEvent
/*    */   public void onMove(MoveEvent event) {
/* 16 */     if (event.getStage() == 0) {
/* 17 */       double x = event.getX();
/* 18 */       double y = event.getY();
/* 19 */       double z = event.getZ();
/* 20 */       if (mc.field_71439_g.field_70122_E) {
/*    */         double increment;
/* 22 */         for (increment = 0.05D; x != 0.0D && isOffsetBBEmpty(x, -1.0D, 0.0D); ) {
/* 23 */           if (x < increment && x >= -increment) {
/* 24 */             x = 0.0D; continue;
/* 25 */           }  if (x > 0.0D) {
/* 26 */             x -= increment; continue;
/*    */           } 
/* 28 */           x += increment;
/*    */         } 
/*    */         
/* 31 */         while (z != 0.0D && isOffsetBBEmpty(0.0D, -1.0D, z)) {
/* 32 */           if (z < increment && z >= -increment) {
/* 33 */             z = 0.0D; continue;
/* 34 */           }  if (z > 0.0D) {
/* 35 */             z -= increment; continue;
/*    */           } 
/* 37 */           z += increment;
/*    */         } 
/*    */         
/* 40 */         while (x != 0.0D && z != 0.0D && isOffsetBBEmpty(x, -1.0D, z)) {
/* 41 */           if (x < increment && x >= -increment) {
/* 42 */             x = 0.0D;
/* 43 */           } else if (x > 0.0D) {
/* 44 */             x -= increment;
/*    */           } else {
/* 46 */             x += increment;
/*    */           } 
/* 48 */           if (z < increment && z >= -increment) {
/* 49 */             z = 0.0D; continue;
/* 50 */           }  if (z > 0.0D) {
/* 51 */             z -= increment; continue;
/*    */           } 
/* 53 */           z += increment;
/*    */         } 
/*    */       } 
/*    */       
/* 57 */       event.setX(x);
/* 58 */       event.setY(y);
/* 59 */       event.setZ(z);
/*    */     } 
/*    */   }
/*    */   
/*    */   public boolean isOffsetBBEmpty(double offsetX, double offsetY, double offsetZ) {
/* 64 */     EntityPlayerSP playerSP = mc.field_71439_g;
/* 65 */     return mc.field_71441_e.func_184144_a((Entity)playerSP, playerSP.func_174813_aQ().func_72317_d(offsetX, offsetY, offsetZ)).isEmpty();
/*    */   }
/*    */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\me\earth\phobos\features\modules\movement\SafeWalk.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */