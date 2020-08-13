/*    */ package me.earth.phobos.mixin.mixins;
/*    */ 
/*    */ import com.google.common.base.Predicate;
/*    */ import java.util.List;
/*    */ import me.earth.phobos.event.events.PushEvent;
/*    */ import me.earth.phobos.features.modules.misc.Tracker;
/*    */ import me.earth.phobos.features.modules.render.NoRender;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.util.math.AxisAlignedBB;
/*    */ import net.minecraft.util.math.BlockPos;
/*    */ import net.minecraft.world.EnumSkyBlock;
/*    */ import net.minecraft.world.World;
/*    */ import net.minecraft.world.chunk.Chunk;
/*    */ import net.minecraftforge.common.MinecraftForge;
/*    */ import net.minecraftforge.fml.common.eventhandler.Event;
/*    */ import org.spongepowered.asm.mixin.Mixin;
/*    */ import org.spongepowered.asm.mixin.injection.At;
/*    */ import org.spongepowered.asm.mixin.injection.Inject;
/*    */ import org.spongepowered.asm.mixin.injection.Redirect;
/*    */ import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
/*    */ import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
/*    */ 
/*    */ @Mixin({World.class})
/*    */ public class MixinWorld
/*    */ {
/*    */   @Redirect(method = {"getEntitiesWithinAABB(Ljava/lang/Class;Lnet/minecraft/util/math/AxisAlignedBB;Lcom/google/common/base/Predicate;)Ljava/util/List;"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/chunk/Chunk;getEntitiesOfTypeWithinAABB(Ljava/lang/Class;Lnet/minecraft/util/math/AxisAlignedBB;Ljava/util/List;Lcom/google/common/base/Predicate;)V"))
/*    */   public <T extends Entity> void getEntitiesOfTypeWithinAABBHook(Chunk chunk, Class<? extends T> entityClass, AxisAlignedBB aabb, List<T> listToFill, Predicate<? super T> filter) {
/*    */     try {
/* 29 */       chunk.func_177430_a(entityClass, aabb, listToFill, filter);
/* 30 */     } catch (Exception exception) {}
/*    */   }
/*    */   
/*    */   @Inject(method = {"onEntityAdded"}, at = {@At("HEAD")})
/*    */   private void onEntityAdded(Entity entityIn, CallbackInfo ci) {
/* 35 */     Tracker.getInstance().onSpawnEntity(entityIn);
/*    */   }
/*    */   
/*    */   @Inject(method = {"checkLightFor"}, at = {@At("HEAD")}, cancellable = true)
/*    */   private void updateLightmapHook(EnumSkyBlock lightType, BlockPos pos, CallbackInfoReturnable<Boolean> info) {
/* 40 */     if (lightType == EnumSkyBlock.SKY && NoRender.getInstance().isOn() && ((NoRender.getInstance()).skylight.getValue() == NoRender.Skylight.WORLD || (NoRender.getInstance()).skylight.getValue() == NoRender.Skylight.ALL)) {
/* 41 */       info.setReturnValue(Boolean.valueOf(true));
/* 42 */       info.cancel();
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   @Redirect(method = {"handleMaterialAcceleration"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;isPushedByWater()Z"))
/*    */   public boolean isPushedbyWaterHook(Entity entity) {
/* 49 */     PushEvent event = new PushEvent(2, entity);
/* 50 */     MinecraftForge.EVENT_BUS.post((Event)event);
/* 51 */     return (entity.func_96092_aw() && !event.isCanceled());
/*    */   }
/*    */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\me\earth\phobos\mixin\mixins\MixinWorld.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */