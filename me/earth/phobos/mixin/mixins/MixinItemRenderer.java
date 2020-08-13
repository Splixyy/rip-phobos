/*    */ package me.earth.phobos.mixin.mixins;
/*    */ 
/*    */ import me.earth.phobos.features.modules.render.NoRender;
/*    */ import me.earth.phobos.features.modules.render.SmallShield;
/*    */ import net.minecraft.client.entity.AbstractClientPlayer;
/*    */ import net.minecraft.client.renderer.GlStateManager;
/*    */ import net.minecraft.client.renderer.ItemRenderer;
/*    */ import net.minecraft.item.ItemStack;
/*    */ import net.minecraft.util.EnumHand;
/*    */ import org.spongepowered.asm.mixin.Mixin;
/*    */ import org.spongepowered.asm.mixin.Shadow;
/*    */ import org.spongepowered.asm.mixin.injection.At;
/*    */ import org.spongepowered.asm.mixin.injection.Inject;
/*    */ import org.spongepowered.asm.mixin.injection.Redirect;
/*    */ import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
/*    */ 
/*    */ 
/*    */ @Mixin({ItemRenderer.class})
/*    */ public abstract class MixinItemRenderer
/*    */ {
/*    */   private boolean injection = true;
/*    */   
/*    */   @Shadow
/*    */   public abstract void func_187457_a(AbstractClientPlayer paramAbstractClientPlayer, float paramFloat1, float paramFloat2, EnumHand paramEnumHand, float paramFloat3, ItemStack paramItemStack, float paramFloat4);
/*    */   
/*    */   @Inject(method = {"renderItemInFirstPerson(Lnet/minecraft/client/entity/AbstractClientPlayer;FFLnet/minecraft/util/EnumHand;FLnet/minecraft/item/ItemStack;F)V"}, at = {@At("HEAD")}, cancellable = true)
/*    */   public void renderItemInFirstPersonHook(AbstractClientPlayer player, float p_187457_2_, float p_187457_3_, EnumHand hand, float p_187457_5_, ItemStack stack, float p_187457_7_, CallbackInfo info) {
/* 28 */     if (this.injection) {
/* 29 */       info.cancel();
/* 30 */       SmallShield offset = SmallShield.getINSTANCE();
/* 31 */       float xOffset = 0.0F;
/* 32 */       float yOffset = 0.0F;
/* 33 */       this.injection = false;
/* 34 */       if (hand == EnumHand.MAIN_HAND) {
/* 35 */         if (offset.isOn()) {
/* 36 */           xOffset = ((Float)offset.mainX.getValue()).floatValue();
/* 37 */           yOffset = ((Float)offset.mainY.getValue()).floatValue();
/*    */         }
/*    */       
/* 40 */       } else if (!((Boolean)offset.normalOffset.getValue()).booleanValue() && offset.isOn()) {
/* 41 */         xOffset = ((Float)offset.offX.getValue()).floatValue();
/* 42 */         yOffset = ((Float)offset.offY.getValue()).floatValue();
/*    */       } 
/*    */       
/* 45 */       func_187457_a(player, p_187457_2_, p_187457_3_, hand, p_187457_5_ + xOffset, stack, p_187457_7_ + yOffset);
/* 46 */       this.injection = true;
/*    */     } 
/*    */   }
/*    */   
/*    */   @Redirect(method = {"renderArmFirstPerson"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/GlStateManager;translate(FFF)V", ordinal = 0))
/*    */   public void translateHook(float x, float y, float z) {
/* 52 */     SmallShield offset = SmallShield.getINSTANCE();
/* 53 */     GlStateManager.func_179109_b(x + (offset.isOn() ? ((Float)offset.mainX.getValue()).floatValue() : 0.0F), y + (offset.isOn() ? ((Float)offset.mainY.getValue()).floatValue() : 0.0F), z);
/*    */   }
/*    */   
/*    */   @Inject(method = {"renderFireInFirstPerson"}, at = {@At("HEAD")}, cancellable = true)
/*    */   public void renderFireInFirstPersonHook(CallbackInfo info) {
/* 58 */     if (NoRender.getInstance().isOn() && ((Boolean)(NoRender.getInstance()).fire.getValue()).booleanValue())
/* 59 */       info.cancel(); 
/*    */   }
/*    */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\me\earth\phobos\mixin\mixins\MixinItemRenderer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */