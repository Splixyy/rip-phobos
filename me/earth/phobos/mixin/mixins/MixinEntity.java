/*    */ package me.earth.phobos.mixin.mixins;
/*    */ 
/*    */ import me.earth.phobos.event.events.PushEvent;
/*    */ import me.earth.phobos.features.modules.misc.BetterPortals;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.world.World;
/*    */ import net.minecraftforge.common.MinecraftForge;
/*    */ import net.minecraftforge.fml.common.eventhandler.Event;
/*    */ import org.spongepowered.asm.mixin.Mixin;
/*    */ import org.spongepowered.asm.mixin.Shadow;
/*    */ import org.spongepowered.asm.mixin.injection.At;
/*    */ import org.spongepowered.asm.mixin.injection.Redirect;
/*    */ 
/*    */ @Mixin({Entity.class})
/*    */ public abstract class MixinEntity {
/*    */   public MixinEntity(World worldIn) {}
/*    */   
/*    */   @Shadow
/*    */   public abstract int func_82145_z();
/*    */   
/*    */   @Redirect(method = {"onEntityUpdate"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;getMaxInPortalTime()I"))
/*    */   private int getMaxInPortalTimeHook(Entity entity) {
/* 23 */     int time = func_82145_z();
/* 24 */     if (BetterPortals.getInstance().isOn() && ((Boolean)(BetterPortals.getInstance()).fastPortal.getValue()).booleanValue()) {
/* 25 */       time = ((Integer)(BetterPortals.getInstance()).time.getValue()).intValue();
/*    */     }
/* 27 */     return time;
/*    */   }
/*    */   
/*    */   @Redirect(method = {"applyEntityCollision"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;addVelocity(DDD)V"))
/*    */   public void addVelocityHook(Entity entity, double x, double y, double z) {
/* 32 */     PushEvent event = new PushEvent(entity, x, y, z, true);
/* 33 */     MinecraftForge.EVENT_BUS.post((Event)event);
/* 34 */     if (!event.isCanceled()) {
/* 35 */       entity.field_70159_w += event.x;
/* 36 */       entity.field_70181_x += event.y;
/* 37 */       entity.field_70179_y += event.z;
/* 38 */       entity.field_70160_al = event.airbone;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\me\earth\phobos\mixin\mixins\MixinEntity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */