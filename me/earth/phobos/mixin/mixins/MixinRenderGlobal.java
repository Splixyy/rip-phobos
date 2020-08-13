/*    */ package me.earth.phobos.mixin.mixins;
/*    */ 
/*    */ import me.earth.phobos.features.modules.movement.Speed;
/*    */ import net.minecraft.client.renderer.ChunkRenderContainer;
/*    */ import net.minecraft.client.renderer.RenderGlobal;
/*    */ import net.minecraft.client.renderer.entity.RenderManager;
/*    */ import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
/*    */ import net.minecraft.util.math.AxisAlignedBB;
/*    */ import org.spongepowered.asm.mixin.Mixin;
/*    */ import org.spongepowered.asm.mixin.injection.At;
/*    */ import org.spongepowered.asm.mixin.injection.Redirect;
/*    */ 
/*    */ @Mixin({RenderGlobal.class})
/*    */ public abstract class MixinRenderGlobal
/*    */ {
/*    */   @Redirect(method = {"setupTerrain"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/ChunkRenderContainer;initialize(DDD)V"))
/*    */   public void initializeHook(ChunkRenderContainer chunkRenderContainer, double viewEntityXIn, double viewEntityYIn, double viewEntityZIn) {
/* 18 */     double y = viewEntityYIn;
/* 19 */     if (Speed.getInstance().isOn() && ((Boolean)(Speed.getInstance()).noShake.getValue()).booleanValue() && (Speed.getInstance()).mode.getValue() != Speed.Mode.INSTANT && (Speed.getInstance()).antiShake) {
/* 20 */       y = (Speed.getInstance()).startY;
/*    */     }
/* 22 */     chunkRenderContainer.func_178004_a(viewEntityXIn, y, viewEntityZIn);
/*    */   }
/*    */   
/*    */   @Redirect(method = {"renderEntities"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/RenderManager;setRenderPosition(DDD)V"))
/*    */   public void setRenderPositionHook(RenderManager renderManager, double renderPosXIn, double renderPosYIn, double renderPosZIn) {
/* 27 */     double y = renderPosYIn;
/* 28 */     if (Speed.getInstance().isOn() && ((Boolean)(Speed.getInstance()).noShake.getValue()).booleanValue() && (Speed.getInstance()).mode.getValue() != Speed.Mode.INSTANT && (Speed.getInstance()).antiShake) {
/* 29 */       y = (Speed.getInstance()).startY;
/*    */     }
/* 31 */     TileEntityRendererDispatcher.field_147555_c = y;
/* 32 */     renderManager.func_178628_a(renderPosXIn, y, renderPosZIn);
/*    */   }
/*    */   
/*    */   @Redirect(method = {"drawSelectionBox"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/AxisAlignedBB;offset(DDD)Lnet/minecraft/util/math/AxisAlignedBB;"))
/*    */   public AxisAlignedBB offsetHook(AxisAlignedBB axisAlignedBB, double x, double y, double z) {
/* 37 */     double yIn = y;
/* 38 */     if (Speed.getInstance().isOn() && ((Boolean)(Speed.getInstance()).noShake.getValue()).booleanValue() && (Speed.getInstance()).mode.getValue() != Speed.Mode.INSTANT && (Speed.getInstance()).antiShake) {
/* 39 */       yIn = (Speed.getInstance()).startY;
/*    */     }
/* 41 */     return axisAlignedBB.func_72317_d(x, y, z);
/*    */   }
/*    */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\me\earth\phobos\mixin\mixins\MixinRenderGlobal.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */