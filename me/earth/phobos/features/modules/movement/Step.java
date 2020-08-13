/*    */ package me.earth.phobos.features.modules.movement;
/*    */ 
/*    */ import me.earth.phobos.features.modules.Module;
/*    */ import me.earth.phobos.features.setting.Setting;
/*    */ import net.minecraft.block.Block;
/*    */ import net.minecraft.block.material.Material;
/*    */ import net.minecraft.network.Packet;
/*    */ import net.minecraft.network.play.client.CPacketPlayer;
/*    */ import net.minecraft.util.math.AxisAlignedBB;
/*    */ import net.minecraft.util.math.BlockPos;
/*    */ import net.minecraft.util.math.MathHelper;
/*    */ 
/*    */ public class Step
/*    */   extends Module {
/* 15 */   public Setting<Integer> stepHeight = register(new Setting("Height", Integer.valueOf(2), Integer.valueOf(1), Integer.valueOf(4)));
/* 16 */   public Setting<Boolean> spoof = register(new Setting("Spoof", Boolean.valueOf(true)));
/* 17 */   public Setting<Integer> ticks = register(new Setting("Delay", Integer.valueOf(3), Integer.valueOf(0), Integer.valueOf(25), v -> ((Boolean)this.spoof.getValue()).booleanValue(), "Amount of ticks you want to spoof."));
/* 18 */   public Setting<Boolean> turnOff = register(new Setting("Disable", Boolean.valueOf(false)));
/* 19 */   public Setting<Boolean> small = register(new Setting("Offset", Boolean.valueOf(false), v -> (((Integer)this.stepHeight.getValue()).intValue() > 1)));
/*    */   
/* 21 */   private final double[] oneblockPositions = new double[] { 0.42D, 0.75D };
/* 22 */   private final double[] twoblockPositions = new double[] { 0.4D, 0.75D, 0.5D, 0.41D, 0.83D, 1.16D, 1.41D, 1.57D, 1.58D, 1.42D };
/* 23 */   private final double[] futurePositions = new double[] { 0.42D, 0.78D, 0.63D, 0.51D, 0.9D, 1.21D, 1.45D, 1.43D };
/* 24 */   final double[] twoFiveOffset = new double[] { 0.425D, 0.821D, 0.699D, 0.599D, 1.022D, 1.372D, 1.652D, 1.869D, 2.019D, 1.907D };
/* 25 */   private final double[] threeBlockPositions = new double[] { 0.42D, 0.78D, 0.63D, 0.51D, 0.9D, 1.21D, 1.45D, 1.43D, 1.78D, 1.63D, 1.51D, 1.9D, 2.21D, 2.45D, 2.43D };
/* 26 */   private final double[] fourBlockPositions = new double[] { 0.42D, 0.78D, 0.63D, 0.51D, 0.9D, 1.21D, 1.45D, 1.43D, 1.78D, 1.63D, 1.51D, 1.9D, 2.21D, 2.45D, 2.43D, 2.78D, 2.63D, 2.51D, 2.9D, 3.21D, 3.45D, 3.43D };
/* 27 */   private double[] selectedPositions = new double[0];
/*    */   private int packets;
/*    */   
/*    */   public Step() {
/* 31 */     super("Step", "Allows you to step up blocks", Module.Category.MOVEMENT, true, false, false);
/*    */   }
/*    */ 
/*    */   
/*    */   public void onUpdate() {
/* 36 */     switch (((Integer)this.stepHeight.getValue()).intValue()) {
/*    */       case 1:
/* 38 */         this.selectedPositions = this.oneblockPositions;
/*    */         break;
/*    */       case 2:
/* 41 */         this.selectedPositions = ((Boolean)this.small.getValue()).booleanValue() ? this.twoblockPositions : this.futurePositions;
/*    */         break;
/*    */       case 3:
/* 44 */         this.selectedPositions = this.twoFiveOffset;
/*    */       
/*    */       case 4:
/* 47 */         this.selectedPositions = this.fourBlockPositions;
/*    */         break;
/*    */     } 
/* 50 */     if (mc.field_71439_g.field_70123_F && mc.field_71439_g.field_70122_E) {
/* 51 */       this.packets++;
/*    */     }
/*    */     
/* 54 */     AxisAlignedBB bb = mc.field_71439_g.func_174813_aQ();
/*    */     
/* 56 */     for (int x = MathHelper.func_76128_c(bb.field_72340_a); x < MathHelper.func_76128_c(bb.field_72336_d + 1.0D); x++) {
/* 57 */       for (int z = MathHelper.func_76128_c(bb.field_72339_c); z < MathHelper.func_76128_c(bb.field_72334_f + 1.0D); z++) {
/* 58 */         Block block = mc.field_71441_e.func_180495_p(new BlockPos(x, bb.field_72337_e + 1.0D, z)).func_177230_c();
/* 59 */         if (!(block instanceof net.minecraft.block.BlockAir)) {
/*    */           return;
/*    */         }
/*    */       } 
/*    */     } 
/*    */     
/* 65 */     if (mc.field_71439_g.field_70122_E && !mc.field_71439_g.func_70055_a(Material.field_151586_h) && !mc.field_71439_g.func_70055_a(Material.field_151587_i) && mc.field_71439_g.field_70124_G && mc.field_71439_g.field_70143_R == 0.0F && !mc.field_71474_y.field_74314_A.field_74513_e && mc.field_71439_g.field_70123_F && !mc.field_71439_g.func_70617_f_() && (this.packets > this.selectedPositions.length - 2 || (((Boolean)this.spoof.getValue()).booleanValue() && this.packets > ((Integer)this.ticks.getValue()).intValue()))) {
/* 66 */       for (double position : this.selectedPositions) {
/* 67 */         mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u + position, mc.field_71439_g.field_70161_v, true));
/*    */       }
/* 69 */       mc.field_71439_g.func_70107_b(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u + this.selectedPositions[this.selectedPositions.length - 1], mc.field_71439_g.field_70161_v);
/* 70 */       this.packets = 0;
/* 71 */       if (((Boolean)this.turnOff.getValue()).booleanValue())
/* 72 */         disable(); 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\me\earth\phobos\features\modules\movement\Step.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */