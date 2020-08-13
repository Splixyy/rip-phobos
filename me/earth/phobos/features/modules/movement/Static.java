/*    */ package me.earth.phobos.features.modules.movement;
/*    */ 
/*    */ import me.earth.phobos.Phobos;
/*    */ import me.earth.phobos.features.modules.Module;
/*    */ import me.earth.phobos.features.setting.Setting;
/*    */ import net.minecraft.network.Packet;
/*    */ import net.minecraft.network.play.client.CPacketPlayer;
/*    */ import net.minecraft.util.math.RayTraceResult;
/*    */ import net.minecraft.util.math.Vec3d;
/*    */ 
/*    */ public class Static extends Module {
/* 12 */   private final Setting<Mode> mode = register(new Setting("Mode", Mode.ROOF));
/* 13 */   private final Setting<Boolean> disabler = register(new Setting("Disable", Boolean.valueOf(true), v -> (this.mode.getValue() == Mode.ROOF)));
/* 14 */   private final Setting<Boolean> ySpeed = register(new Setting("YSpeed", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.STATIC)));
/* 15 */   private final Setting<Float> speed = register(new Setting("Speed", Float.valueOf(0.1F), Float.valueOf(0.0F), Float.valueOf(10.0F), v -> (((Boolean)this.ySpeed.getValue()).booleanValue() && this.mode.getValue() == Mode.STATIC)));
/* 16 */   private final Setting<Float> height = register(new Setting("Height", Float.valueOf(3.0F), Float.valueOf(0.0F), Float.valueOf(256.0F), v -> (this.mode.getValue() == Mode.NOVOID)));
/*    */   
/*    */   public Static() {
/* 19 */     super("Static", "Stops any movement. Glitches you up.", Module.Category.MOVEMENT, false, false, false);
/*    */   }
/*    */ 
/*    */   
/*    */   public void onUpdate() {
/* 24 */     if (fullNullCheck()) {
/*    */       return;
/*    */     }
/*    */     
/* 28 */     switch ((Mode)this.mode.getValue()) {
/*    */       case STATIC:
/* 30 */         mc.field_71439_g.field_71075_bZ.field_75100_b = false;
/* 31 */         mc.field_71439_g.field_70159_w = 0.0D;
/* 32 */         mc.field_71439_g.field_70181_x = 0.0D;
/* 33 */         mc.field_71439_g.field_70179_y = 0.0D;
/*    */         
/* 35 */         if (((Boolean)this.ySpeed.getValue()).booleanValue()) {
/* 36 */           mc.field_71439_g.field_70747_aH = ((Float)this.speed.getValue()).floatValue();
/* 37 */           if (mc.field_71474_y.field_74314_A.func_151470_d()) {
/* 38 */             mc.field_71439_g.field_70181_x += ((Float)this.speed.getValue()).floatValue();
/*    */           }
/* 40 */           if (mc.field_71474_y.field_74311_E.func_151470_d()) {
/* 41 */             mc.field_71439_g.field_70181_x -= ((Float)this.speed.getValue()).floatValue();
/*    */           }
/*    */         } 
/*    */         break;
/*    */       case ROOF:
/* 46 */         mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(mc.field_71439_g.field_70165_t, 10000.0D, mc.field_71439_g.field_70161_v, mc.field_71439_g.field_70122_E));
/* 47 */         if (((Boolean)this.disabler.getValue()).booleanValue()) {
/* 48 */           disable();
/*    */         }
/*    */         break;
/*    */       case NOVOID:
/* 52 */         if (!mc.field_71439_g.field_70145_X && mc.field_71439_g.field_70163_u <= ((Float)this.height.getValue()).floatValue()) {
/* 53 */           RayTraceResult trace = mc.field_71441_e.func_147447_a(mc.field_71439_g.func_174791_d(), new Vec3d(mc.field_71439_g.field_70165_t, 0.0D, mc.field_71439_g.field_70161_v), false, false, false);
/* 54 */           if (trace != null && trace.field_72313_a == RayTraceResult.Type.BLOCK) {
/*    */             return;
/*    */           }
/*    */           
/* 58 */           if (Phobos.moduleManager.isModuleEnabled(Phase.class) || Phobos.moduleManager.isModuleEnabled(Flight.class)) {
/*    */             return;
/*    */           }
/*    */           
/* 62 */           mc.field_71439_g.func_70016_h(0.0D, 0.0D, 0.0D);
/* 63 */           if (mc.field_71439_g.func_184187_bx() != null) {
/* 64 */             mc.field_71439_g.func_184187_bx().func_70016_h(0.0D, 0.0D, 0.0D);
/*    */           }
/*    */         } 
/*    */         break;
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public String getDisplayInfo() {
/* 73 */     if (this.mode.getValue() == Mode.ROOF)
/* 74 */       return "Roof"; 
/* 75 */     if (this.mode.getValue() == Mode.NOVOID) {
/* 76 */       return "NoVoid";
/*    */     }
/* 78 */     return null;
/*    */   }
/*    */   
/*    */   public enum Mode
/*    */   {
/* 83 */     STATIC,
/* 84 */     ROOF,
/* 85 */     NOVOID;
/*    */   }
/*    */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\me\earth\phobos\features\modules\movement\Static.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */