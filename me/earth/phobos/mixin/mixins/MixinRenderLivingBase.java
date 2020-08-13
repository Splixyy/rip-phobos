/*    */ package me.earth.phobos.mixin.mixins;
/*    */ 
/*    */ import me.earth.phobos.event.events.RenderEntityModelEvent;
/*    */ import me.earth.phobos.features.modules.render.Chams;
/*    */ import me.earth.phobos.features.modules.render.ESP;
/*    */ import me.earth.phobos.features.modules.render.Skeleton;
/*    */ import net.minecraft.client.model.ModelBase;
/*    */ import net.minecraft.client.renderer.entity.Render;
/*    */ import net.minecraft.client.renderer.entity.RenderLivingBase;
/*    */ import net.minecraft.client.renderer.entity.RenderManager;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.entity.EntityLivingBase;
/*    */ import org.lwjgl.opengl.GL11;
/*    */ import org.spongepowered.asm.mixin.Mixin;
/*    */ import org.spongepowered.asm.mixin.injection.At;
/*    */ import org.spongepowered.asm.mixin.injection.Inject;
/*    */ import org.spongepowered.asm.mixin.injection.Redirect;
/*    */ import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
/*    */ 
/*    */ @Mixin({RenderLivingBase.class})
/*    */ public abstract class MixinRenderLivingBase<T extends EntityLivingBase>
/*    */   extends Render<T> {
/*    */   public MixinRenderLivingBase(RenderManager renderManagerIn, ModelBase modelBaseIn, float shadowSizeIn) {
/* 24 */     super(renderManagerIn);
/*    */   }
/*    */   
/*    */   @Redirect(method = {"renderModel"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/ModelBase;render(Lnet/minecraft/entity/Entity;FFFFFF)V"))
/*    */   private void renderModelHook(ModelBase modelBase, Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
/* 29 */     if (Skeleton.getInstance().isEnabled() || ESP.getInstance().isEnabled()) {
/* 30 */       RenderEntityModelEvent event = new RenderEntityModelEvent(0, modelBase, entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
/* 31 */       if (Skeleton.getInstance().isEnabled()) {
/* 32 */         Skeleton.getInstance().onRenderModel(event);
/*    */       }
/*    */       
/* 35 */       if (ESP.getInstance().isEnabled()) {
/* 36 */         ESP.getInstance().onRenderModel(event);
/* 37 */         if (event.isCanceled()) {
/*    */           return;
/*    */         }
/*    */       } 
/*    */     } 
/* 42 */     modelBase.func_78088_a(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
/*    */   }
/*    */   
/*    */   @Inject(method = {"doRender"}, at = {@At("HEAD")})
/*    */   public void doRenderPre(T entity, double x, double y, double z, float entityYaw, float partialTicks, CallbackInfo info) {
/* 47 */     if (Chams.getInstance().isEnabled() && entity != null) {
/* 48 */       GL11.glEnable(32823);
/* 49 */       GL11.glPolygonOffset(1.0F, -1100000.0F);
/*    */     } 
/*    */   }
/*    */   
/*    */   @Inject(method = {"doRender"}, at = {@At("RETURN")})
/*    */   public void doRenderPost(T entity, double x, double y, double z, float entityYaw, float partialTicks, CallbackInfo info) {
/* 55 */     if (Chams.getInstance().isEnabled() && entity != null) {
/* 56 */       GL11.glPolygonOffset(1.0F, 1000000.0F);
/* 57 */       GL11.glDisable(32823);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\me\earth\phobos\mixin\mixins\MixinRenderLivingBase.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */