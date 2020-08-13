/*    */ package me.earth.phobos.manager;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.Comparator;
/*    */ import java.util.List;
/*    */ import me.earth.phobos.features.Feature;
/*    */ import me.earth.phobos.features.modules.client.Managers;
/*    */ import me.earth.phobos.features.modules.combat.HoleFiller;
/*    */ import me.earth.phobos.features.modules.movement.HoleTP;
/*    */ import me.earth.phobos.features.modules.render.HoleESP;
/*    */ import me.earth.phobos.util.BlockUtil;
/*    */ import me.earth.phobos.util.EntityUtil;
/*    */ import net.minecraft.block.Block;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.init.Blocks;
/*    */ import net.minecraft.util.math.BlockPos;
/*    */ import net.minecraft.util.math.Vec3i;
/*    */ 
/*    */ public class HoleManager extends Feature {
/* 20 */   private static final BlockPos[] surroundOffset = BlockUtil.toBlockPos(EntityUtil.getOffsets(0, true));
/* 21 */   private List<BlockPos> holes = new ArrayList<>();
/* 22 */   private List<BlockPos> midSafety = new ArrayList<>();
/*    */   
/*    */   public void update() {
/* 25 */     if (!fullNullCheck() && (HoleESP.getInstance().isOn() || HoleFiller.getInstance().isOn() || HoleTP.getInstance().isOn())) {
/* 26 */       this.holes = calcHoles();
/*    */     }
/*    */   }
/*    */   
/*    */   public List<BlockPos> getHoles() {
/* 31 */     return this.holes;
/*    */   }
/*    */   
/*    */   public List<BlockPos> getMidSafety() {
/* 35 */     return this.midSafety;
/*    */   }
/*    */   
/*    */   public List<BlockPos> getSortedHoles() {
/* 39 */     this.holes.sort(Comparator.comparingDouble(hole -> mc.field_71439_g.func_174818_b(hole)));
/* 40 */     return getHoles();
/*    */   }
/*    */   
/*    */   public List<BlockPos> calcHoles() {
/* 44 */     List<BlockPos> safeSpots = new ArrayList<>();
/* 45 */     this.midSafety.clear();
/* 46 */     List<BlockPos> positions = BlockUtil.getSphere(EntityUtil.getPlayerPos((EntityPlayer)mc.field_71439_g), ((Float)(Managers.getInstance()).holeRange.getValue()).floatValue(), ((Float)(Managers.getInstance()).holeRange.getValue()).intValue(), false, true, 0);
/* 47 */     for (BlockPos pos : positions) {
/* 48 */       if (!mc.field_71441_e.func_180495_p(pos).func_177230_c().equals(Blocks.field_150350_a)) {
/*    */         continue;
/*    */       }
/*    */       
/* 52 */       if (!mc.field_71441_e.func_180495_p(pos.func_177982_a(0, 1, 0)).func_177230_c().equals(Blocks.field_150350_a)) {
/*    */         continue;
/*    */       }
/*    */       
/* 56 */       if (!mc.field_71441_e.func_180495_p(pos.func_177982_a(0, 2, 0)).func_177230_c().equals(Blocks.field_150350_a)) {
/*    */         continue;
/*    */       }
/*    */       
/* 60 */       boolean isSafe = true;
/* 61 */       boolean midSafe = true;
/* 62 */       for (BlockPos offset : surroundOffset) {
/* 63 */         Block block = mc.field_71441_e.func_180495_p(pos.func_177971_a((Vec3i)offset)).func_177230_c();
/* 64 */         if (BlockUtil.isBlockUnSolid(block)) {
/* 65 */           midSafe = false;
/*    */         }
/*    */         
/* 68 */         if (block != Blocks.field_150357_h && block != Blocks.field_150343_Z && block != Blocks.field_150477_bB && block != Blocks.field_150467_bQ) {
/* 69 */           isSafe = false;
/*    */         }
/*    */       } 
/*    */       
/* 73 */       if (isSafe) {
/* 74 */         safeSpots.add(pos);
/*    */       }
/*    */       
/* 77 */       if (midSafe) {
/* 78 */         this.midSafety.add(pos);
/*    */       }
/*    */     } 
/* 81 */     return safeSpots;
/*    */   }
/*    */   
/*    */   public boolean isSafe(BlockPos pos) {
/* 85 */     boolean isSafe = true;
/* 86 */     for (BlockPos offset : surroundOffset) {
/* 87 */       Block block = mc.field_71441_e.func_180495_p(pos.func_177971_a((Vec3i)offset)).func_177230_c();
/* 88 */       if (block != Blocks.field_150357_h) {
/* 89 */         isSafe = false;
/*    */         break;
/*    */       } 
/*    */     } 
/* 93 */     return isSafe;
/*    */   }
/*    */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\me\earth\phobos\manager\HoleManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */