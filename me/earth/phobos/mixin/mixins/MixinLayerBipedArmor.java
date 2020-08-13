/*    */ package me.earth.phobos.mixin.mixins;
/*    */ 
/*    */ import me.earth.phobos.features.modules.render.NoRender;
/*    */ import net.minecraft.client.model.ModelBiped;
/*    */ import net.minecraft.client.renderer.entity.RenderLivingBase;
/*    */ import net.minecraft.client.renderer.entity.layers.LayerArmorBase;
/*    */ import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
/*    */ import net.minecraft.inventory.EntityEquipmentSlot;
/*    */ import org.spongepowered.asm.mixin.Mixin;
/*    */ import org.spongepowered.asm.mixin.injection.At;
/*    */ import org.spongepowered.asm.mixin.injection.Inject;
/*    */ import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
/*    */ 
/*    */ @Mixin({LayerBipedArmor.class})
/*    */ public abstract class MixinLayerBipedArmor
/*    */   extends LayerArmorBase<ModelBiped> {
/*    */   public MixinLayerBipedArmor(RenderLivingBase<?> rendererIn) {
/* 18 */     super(rendererIn);
/*    */   }
/*    */   
/*    */   @Inject(method = {"setModelSlotVisible"}, at = {@At("HEAD")}, cancellable = true)
/*    */   protected void setModelSlotVisible(ModelBiped model, EntityEquipmentSlot slotIn, CallbackInfo info) {
/* 23 */     NoRender noArmor = NoRender.getInstance();
/* 24 */     if (noArmor.isOn() && noArmor.noArmor.getValue() != NoRender.NoArmor.NONE) {
/* 25 */       info.cancel();
/* 26 */       switch (slotIn) {
/*    */         case HEAD:
/* 28 */           model.field_78116_c.field_78806_j = false;
/* 29 */           model.field_178720_f.field_78806_j = false;
/*    */           break;
/*    */         case CHEST:
/* 32 */           model.field_78115_e.field_78806_j = (noArmor.noArmor.getValue() != NoRender.NoArmor.ALL);
/* 33 */           model.field_178723_h.field_78806_j = (noArmor.noArmor.getValue() != NoRender.NoArmor.ALL);
/* 34 */           model.field_178724_i.field_78806_j = (noArmor.noArmor.getValue() != NoRender.NoArmor.ALL);
/*    */           break;
/*    */         case LEGS:
/* 37 */           model.field_78115_e.field_78806_j = (noArmor.noArmor.getValue() != NoRender.NoArmor.ALL);
/* 38 */           model.field_178721_j.field_78806_j = (noArmor.noArmor.getValue() != NoRender.NoArmor.ALL);
/* 39 */           model.field_178722_k.field_78806_j = (noArmor.noArmor.getValue() != NoRender.NoArmor.ALL);
/*    */           break;
/*    */         case FEET:
/* 42 */           model.field_178721_j.field_78806_j = (noArmor.noArmor.getValue() != NoRender.NoArmor.ALL);
/* 43 */           model.field_178722_k.field_78806_j = (noArmor.noArmor.getValue() != NoRender.NoArmor.ALL);
/*    */           break;
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\me\earth\phobos\mixin\mixins\MixinLayerBipedArmor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */