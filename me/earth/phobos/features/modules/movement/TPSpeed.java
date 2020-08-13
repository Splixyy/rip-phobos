/*    */ package me.earth.phobos.features.modules.movement;
/*    */ 
/*    */ import java.util.Objects;
/*    */ import me.earth.phobos.event.events.UpdateWalkingPlayerEvent;
/*    */ import me.earth.phobos.features.modules.Module;
/*    */ import me.earth.phobos.features.setting.Setting;
/*    */ import me.earth.phobos.util.MathUtil;
/*    */ import net.minecraft.network.Packet;
/*    */ import net.minecraft.network.play.client.CPacketPlayer;
/*    */ import net.minecraft.util.math.AxisAlignedBB;
/*    */ import net.minecraft.util.math.BlockPos;
/*    */ import net.minecraft.util.math.Vec3d;
/*    */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*    */ 
/*    */ public class TPSpeed
/*    */   extends Module {
/* 17 */   private Setting<Mode> mode = register(new Setting("Mode", Mode.NORMAL));
/* 18 */   private Setting<Double> speed = register(new Setting("Speed", Double.valueOf(0.25D), Double.valueOf(0.1D), Double.valueOf(10.0D)));
/* 19 */   private Setting<Double> fallSpeed = register(new Setting("FallSpeed", Double.valueOf(0.25D), Double.valueOf(0.1D), Double.valueOf(10.0D), v -> (this.mode.getValue() == Mode.STEP)));
/* 20 */   private Setting<Boolean> turnOff = register(new Setting("Off", Boolean.valueOf(false)));
/* 21 */   private Setting<Integer> tpLimit = register(new Setting("Limit", Integer.valueOf(2), Integer.valueOf(1), Integer.valueOf(10), v -> ((Boolean)this.turnOff.getValue()).booleanValue(), "Turn it off."));
/*    */   
/* 23 */   private int tps = 0;
/* 24 */   private double[] selectedPositions = new double[] { 0.42D, 0.75D, 1.0D };
/*    */   
/*    */   public TPSpeed() {
/* 27 */     super("TpSpeed", "Teleports you.", Module.Category.MOVEMENT, true, false, false);
/*    */   }
/*    */ 
/*    */   
/*    */   public void onEnable() {
/* 32 */     this.tps = 0;
/*    */   }
/*    */   
/*    */   @SubscribeEvent
/*    */   public void onUpdatePlayerWalking(UpdateWalkingPlayerEvent event) {
/* 37 */     if (event.getStage() != 0) {
/*    */       return;
/*    */     }
/*    */     
/* 41 */     if (this.mode.getValue() == Mode.NORMAL) {
/* 42 */       if (((Boolean)this.turnOff.getValue()).booleanValue() && this.tps >= ((Integer)this.tpLimit.getValue()).intValue()) {
/* 43 */         disable();
/*    */         
/*    */         return;
/*    */       } 
/* 47 */       if (mc.field_71439_g.field_191988_bg != 0.0F || (mc.field_71439_g.field_70702_br != 0.0F && mc.field_71439_g.field_70122_E)) {
/* 48 */         for (double x = 0.0625D; x < ((Double)this.speed.getValue()).doubleValue(); x += 0.262D) {
/* 49 */           double[] dir = MathUtil.directionSpeed(x);
/* 50 */           mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(mc.field_71439_g.field_70165_t + dir[0], mc.field_71439_g.field_70163_u, mc.field_71439_g.field_70161_v + dir[1], mc.field_71439_g.field_70122_E));
/*    */         } 
/* 52 */         mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(mc.field_71439_g.field_70165_t + mc.field_71439_g.field_70159_w, 0.0D, mc.field_71439_g.field_70161_v + mc.field_71439_g.field_70179_y, mc.field_71439_g.field_70122_E));
/* 53 */         this.tps++;
/*    */       }
/*    */     
/* 56 */     } else if ((mc.field_71439_g.field_191988_bg != 0.0F || mc.field_71439_g.field_70702_br != 0.0F) && mc.field_71439_g.field_70122_E) {
/* 57 */       double pawnY = 0.0D;
/* 58 */       double[] lastStep = MathUtil.directionSpeed(0.262D);
/*    */       double x;
/* 60 */       for (x = 0.0625D; x < ((Double)this.speed.getValue()).doubleValue(); x += 0.262D) {
/* 61 */         double[] dir = MathUtil.directionSpeed(x);
/* 62 */         AxisAlignedBB bb = ((AxisAlignedBB)Objects.<AxisAlignedBB>requireNonNull(mc.field_71439_g.func_174813_aQ())).func_72317_d(dir[0], pawnY, dir[1]);
/*    */         
/* 64 */         while (collidesHorizontally(bb)) {
/* 65 */           for (double position : this.selectedPositions) {
/* 66 */             mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(mc.field_71439_g.field_70165_t + dir[0] - lastStep[0], mc.field_71439_g.field_70163_u + pawnY + position, mc.field_71439_g.field_70161_v + dir[1] - lastStep[1], true));
/*    */           }
/* 68 */           pawnY++;
/* 69 */           bb = ((AxisAlignedBB)Objects.<AxisAlignedBB>requireNonNull(mc.field_71439_g.func_174813_aQ())).func_72317_d(dir[0], pawnY, dir[1]);
/*    */         } 
/*    */         
/* 72 */         if (!mc.field_71441_e.func_72829_c(bb.func_72314_b(0.0125D, 0.0D, 0.0125D).func_72317_d(0.0D, -1.0D, 0.0D))) {
/* 73 */           double i; for (i = 0.0D; i <= 1.0D; i += ((Double)this.fallSpeed.getValue()).doubleValue()) {
/* 74 */             mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(mc.field_71439_g.field_70165_t + dir[0], mc.field_71439_g.field_70163_u + pawnY - i, mc.field_71439_g.field_70161_v + dir[1], true));
/*    */           }
/* 76 */           pawnY--;
/*    */         } 
/* 78 */         mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(mc.field_71439_g.field_70165_t + dir[0], mc.field_71439_g.field_70163_u + pawnY, mc.field_71439_g.field_70161_v + dir[1], mc.field_71439_g.field_70122_E));
/*    */       } 
/* 80 */       mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(mc.field_71439_g.field_70165_t + mc.field_71439_g.field_70159_w, 0.0D, mc.field_71439_g.field_70161_v + mc.field_71439_g.field_70179_y, mc.field_71439_g.field_70122_E));
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   private static boolean collidesHorizontally(AxisAlignedBB bb) {
/* 86 */     if (mc.field_71441_e.func_184143_b(bb)) {
/* 87 */       Vec3d center = bb.func_189972_c();
/* 88 */       BlockPos blockpos = new BlockPos(center.field_72450_a, bb.field_72338_b, center.field_72449_c);
/* 89 */       return (mc.field_71441_e.func_175665_u(blockpos.func_177976_e()) || mc.field_71441_e.func_175665_u(blockpos.func_177974_f()) || mc.field_71441_e.func_175665_u(blockpos.func_177978_c()) || mc.field_71441_e.func_175665_u(blockpos.func_177968_d()) || mc.field_71441_e.func_175665_u(blockpos));
/*    */     } 
/* 91 */     return false;
/*    */   }
/*    */   
/*    */   public enum Mode
/*    */   {
/* 96 */     NORMAL,
/* 97 */     STEP;
/*    */   }
/*    */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\me\earth\phobos\features\modules\movement\TPSpeed.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */