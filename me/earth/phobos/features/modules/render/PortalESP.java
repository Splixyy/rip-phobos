/*    */ package me.earth.phobos.features.modules.render;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import java.util.ArrayList;
/*    */ import me.earth.phobos.event.events.Render3DEvent;
/*    */ import me.earth.phobos.features.modules.Module;
/*    */ import me.earth.phobos.features.setting.Setting;
/*    */ import me.earth.phobos.util.RenderUtil;
/*    */ import net.minecraft.block.Block;
/*    */ import net.minecraft.init.Blocks;
/*    */ import net.minecraft.util.math.BlockPos;
/*    */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*    */ import net.minecraftforge.fml.common.gameevent.TickEvent;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class PortalESP
/*    */   extends Module
/*    */ {
/*    */   private int cooldownTicks;
/* 24 */   private final ArrayList<BlockPos> blockPosArrayList = new ArrayList<>();
/* 25 */   private final Setting<Integer> distance = register(new Setting("Distance", Integer.valueOf(60), Integer.valueOf(10), Integer.valueOf(100)));
/* 26 */   private final Setting<Boolean> box = register(new Setting("Box", Boolean.valueOf(false)));
/* 27 */   private final Setting<Integer> boxAlpha = register(new Setting("BoxAlpha", Integer.valueOf(125), Integer.valueOf(0), Integer.valueOf(255), v -> ((Boolean)this.box.getValue()).booleanValue()));
/* 28 */   private final Setting<Boolean> outline = register(new Setting("Outline", Boolean.valueOf(true)));
/* 29 */   private final Setting<Float> lineWidth = register(new Setting("LineWidth", Float.valueOf(1.0F), Float.valueOf(0.1F), Float.valueOf(5.0F), v -> ((Boolean)this.outline.getValue()).booleanValue()));
/*    */   public PortalESP() {
/* 31 */     super("PortalESP", "Draws portals", Module.Category.RENDER, true, false, false);
/*    */   }
/*    */   
/*    */   @SubscribeEvent
/*    */   public void onTickEvent(TickEvent.ClientTickEvent event) {
/* 36 */     if (mc.field_71441_e == null) {
/*    */       return;
/*    */     }
/* 39 */     if (this.cooldownTicks < 1) {
/* 40 */       this.blockPosArrayList.clear();
/* 41 */       compileDL();
/* 42 */       this.cooldownTicks = 80;
/*    */     } 
/* 44 */     this.cooldownTicks--;
/*    */   }
/*    */ 
/*    */   
/*    */   public void onRender3D(Render3DEvent event) {
/* 49 */     this; if (mc.field_71441_e == null) {
/*    */       return;
/*    */     }
/* 52 */     for (BlockPos pos : this.blockPosArrayList) {
/* 53 */       RenderUtil.drawBoxESP(pos, new Color(204, 0, 153, 255), false, new Color(204, 0, 153, 255), ((Float)this.lineWidth.getValue()).floatValue(), ((Boolean)this.outline.getValue()).booleanValue(), ((Boolean)this.box.getValue()).booleanValue(), ((Integer)this.boxAlpha.getValue()).intValue(), false);
/*    */     }
/*    */   }
/*    */   
/*    */   private void compileDL() {
/* 58 */     if (mc.field_71441_e == null || mc.field_71439_g == null) {
/*    */       return;
/*    */     }
/* 61 */     for (int x = (int)mc.field_71439_g.field_70165_t - ((Integer)this.distance.getValue()).intValue(); x <= (int)mc.field_71439_g.field_70165_t + ((Integer)this.distance.getValue()).intValue(); x++) {
/* 62 */       for (int y = (int)mc.field_71439_g.field_70161_v - ((Integer)this.distance.getValue()).intValue(); y <= (int)mc.field_71439_g.field_70161_v + ((Integer)this.distance.getValue()).intValue(); y++) {
/* 63 */         for (int z = (int)Math.max(mc.field_71439_g.field_70163_u - ((Integer)this.distance.getValue()).intValue(), 0.0D); z <= Math.min(mc.field_71439_g.field_70163_u + ((Integer)this.distance.getValue()).intValue(), 255.0D); z++) {
/* 64 */           BlockPos pos = new BlockPos(x, y, z);
/* 65 */           Block block = mc.field_71441_e.func_180495_p(pos).func_177230_c();
/* 66 */           if (block == Blocks.field_150427_aO)
/* 67 */             this.blockPosArrayList.add(pos); 
/*    */         } 
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\me\earth\phobos\features\modules\render\PortalESP.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */