/*    */ package me.earth.phobos.features.modules.misc;
/*    */ import java.util.Random;
/*    */ import me.earth.phobos.features.modules.Module;
/*    */ import me.earth.phobos.features.setting.Setting;
/*    */ import net.minecraft.network.Packet;
/*    */ import net.minecraft.network.play.client.CPacketAnimation;
/*    */ import net.minecraft.util.EnumHand;
/*    */ 
/*    */ public class NoAFK extends Module {
/*    */   private final Setting<Boolean> swing;
/*    */   
/*    */   public NoAFK() {
/* 13 */     super("NoAFK", "Prevents you from getting kicked for afk.", Module.Category.MISC, false, false, false);
/*    */ 
/*    */     
/* 16 */     this.swing = register(new Setting("Swing", Boolean.valueOf(true)));
/* 17 */     this.turn = register(new Setting("Turn", Boolean.valueOf(true)));
/*    */     
/* 19 */     this.random = new Random();
/*    */   }
/*    */   private final Setting<Boolean> turn; private final Random random;
/*    */   public void onUpdate() {
/* 23 */     if (mc.field_71442_b.func_181040_m()) {
/*    */       return;
/*    */     }
/*    */     
/* 27 */     if (mc.field_71439_g.field_70173_aa % 40 == 0 && ((Boolean)this.swing.getValue()).booleanValue()) {
/* 28 */       mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketAnimation(EnumHand.MAIN_HAND));
/*    */     }
/*    */     
/* 31 */     if (mc.field_71439_g.field_70173_aa % 15 == 0 && ((Boolean)this.turn.getValue()).booleanValue()) {
/* 32 */       mc.field_71439_g.field_70177_z = (this.random.nextInt(360) - 180);
/*    */     }
/*    */     
/* 35 */     if (!((Boolean)this.swing.getValue()).booleanValue() && !((Boolean)this.turn.getValue()).booleanValue() && mc.field_71439_g.field_70173_aa % 80 == 0)
/* 36 */       mc.field_71439_g.func_70664_aZ(); 
/*    */   }
/*    */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\me\earth\phobos\features\modules\misc\NoAFK.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */