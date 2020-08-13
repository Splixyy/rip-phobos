/*    */ package me.earth.phobos.mixin.mixins;
/*    */ 
/*    */ import me.earth.phobos.features.modules.render.XRay;
/*    */ import net.minecraft.block.state.IBlockState;
/*    */ import net.minecraft.client.renderer.BlockModelRenderer;
/*    */ import net.minecraft.client.renderer.BufferBuilder;
/*    */ import net.minecraft.client.renderer.block.model.IBakedModel;
/*    */ import net.minecraft.util.math.BlockPos;
/*    */ import net.minecraft.world.IBlockAccess;
/*    */ import org.spongepowered.asm.mixin.Mixin;
/*    */ import org.spongepowered.asm.mixin.injection.At;
/*    */ import org.spongepowered.asm.mixin.injection.Inject;
/*    */ import org.spongepowered.asm.mixin.injection.ModifyArg;
/*    */ import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
/*    */ 
/*    */ @Mixin({BlockModelRenderer.class})
/*    */ public class MixinBlockModelRenderer
/*    */ {
/*    */   @Inject(method = {"renderModel"}, at = {@At("HEAD")}, cancellable = true)
/*    */   private void renderModelHook(IBlockAccess blockAccess, IBakedModel bakedModel, IBlockState blockState, BlockPos blockPos, BufferBuilder bufferBuilder, boolean b, CallbackInfoReturnable<Boolean> info) {
/*    */     try {
/* 22 */       if (XRay.getInstance().isOn() && !XRay.getInstance().shouldRender(blockState.func_177230_c())) {
/* 23 */         info.setReturnValue(Boolean.valueOf(false));
/* 24 */         info.cancel();
/*    */       } 
/* 26 */     } catch (Exception exception) {}
/*    */   }
/*    */   
/*    */   @ModifyArg(method = {"renderModel(Lnet/minecraft/world/IBlockAccess;Lnet/minecraft/client/renderer/block/model/IBakedModel;Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/client/renderer/BufferBuilder;ZJ)Z"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/BlockModelRenderer;renderModelFlat(Lnet/minecraft/world/IBlockAccess;Lnet/minecraft/client/renderer/block/model/IBakedModel;Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/client/renderer/BufferBuilder;ZJ)Z"))
/*    */   private boolean renderModelFlatHook(boolean input) {
/*    */     try {
/* 32 */       if (XRay.getInstance().isOn()) {
/* 33 */         return false;
/*    */       }
/* 35 */     } catch (Exception exception) {}
/* 36 */     return input;
/*    */   }
/*    */   
/*    */   @ModifyArg(method = {"renderModel(Lnet/minecraft/world/IBlockAccess;Lnet/minecraft/client/renderer/block/model/IBakedModel;Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/client/renderer/BufferBuilder;ZJ)Z"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/BlockModelRenderer;renderModelSmooth(Lnet/minecraft/world/IBlockAccess;Lnet/minecraft/client/renderer/block/model/IBakedModel;Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/client/renderer/BufferBuilder;ZJ)Z"))
/*    */   private boolean renderModelSmoothHook(boolean input) {
/*    */     try {
/* 42 */       if (XRay.getInstance().isOn()) {
/* 43 */         return false;
/*    */       }
/* 45 */     } catch (Exception exception) {}
/* 46 */     return input;
/*    */   }
/*    */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\me\earth\phobos\mixin\mixins\MixinBlockModelRenderer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */