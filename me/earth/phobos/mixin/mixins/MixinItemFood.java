/*    */ package me.earth.phobos.mixin.mixins;
/*    */ 
/*    */ import me.earth.phobos.features.modules.combat.Offhand;
/*    */ import net.minecraft.entity.EntityLivingBase;
/*    */ import net.minecraft.item.ItemFood;
/*    */ import net.minecraft.item.ItemStack;
/*    */ import net.minecraft.world.World;
/*    */ import org.spongepowered.asm.mixin.Mixin;
/*    */ import org.spongepowered.asm.mixin.injection.At;
/*    */ import org.spongepowered.asm.mixin.injection.Inject;
/*    */ import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
/*    */ 
/*    */ @Mixin({ItemFood.class})
/*    */ public class MixinItemFood
/*    */ {
/*    */   @Inject(method = {"onItemUseFinish"}, at = {@At("RETURN")}, cancellable = true)
/*    */   public void onItemUseFinishHook(ItemStack stack, World worldIn, EntityLivingBase entityLiving, CallbackInfoReturnable<ItemStack> info) {
/* 18 */     Offhand.getInstance().onItemFinish(stack, entityLiving);
/*    */   }
/*    */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\me\earth\phobos\mixin\mixins\MixinItemFood.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */