/*    */ package me.earth.phobos.mixin.mixins;
/*    */ 
/*    */ import me.earth.phobos.Phobos;
/*    */ import me.earth.phobos.features.modules.client.HUD;
/*    */ import me.earth.phobos.features.modules.render.NoRender;
/*    */ import net.minecraft.client.gui.Gui;
/*    */ import net.minecraft.client.gui.GuiIngame;
/*    */ import net.minecraft.client.gui.ScaledResolution;
/*    */ import org.spongepowered.asm.mixin.Mixin;
/*    */ import org.spongepowered.asm.mixin.injection.At;
/*    */ import org.spongepowered.asm.mixin.injection.Inject;
/*    */ import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
/*    */ 
/*    */ @Mixin({GuiIngame.class})
/*    */ public class MixinGuiIngame
/*    */   extends Gui {
/*    */   @Inject(method = {"renderPortal"}, at = {@At("HEAD")}, cancellable = true)
/*    */   protected void renderPortalHook(float n, ScaledResolution scaledResolution, CallbackInfo info) {
/* 19 */     if (NoRender.getInstance().isOn() && ((Boolean)(NoRender.getInstance()).portal.getValue()).booleanValue()) {
/* 20 */       info.cancel();
/*    */     }
/*    */   }
/*    */   
/*    */   @Inject(method = {"renderPumpkinOverlay"}, at = {@At("HEAD")}, cancellable = true)
/*    */   protected void renderPumpkinOverlayHook(ScaledResolution scaledRes, CallbackInfo info) {
/* 26 */     if (NoRender.getInstance().isOn() && ((Boolean)(NoRender.getInstance()).pumpkin.getValue()).booleanValue()) {
/* 27 */       info.cancel();
/*    */     }
/*    */   }
/*    */   
/*    */   @Inject(method = {"renderPotionEffects"}, at = {@At("HEAD")}, cancellable = true)
/*    */   protected void renderPotionEffectsHook(ScaledResolution scaledRes, CallbackInfo info) {
/* 33 */     if (Phobos.moduleManager != null && !((Boolean)(HUD.getInstance()).potionIcons.getValue()).booleanValue())
/* 34 */       info.cancel(); 
/*    */   }
/*    */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\me\earth\phobos\mixin\mixins\MixinGuiIngame.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */