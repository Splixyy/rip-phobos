/*    */ package me.earth.phobos.mixin.mixins;
/*    */ 
/*    */ import me.earth.phobos.Phobos;
/*    */ import me.earth.phobos.features.modules.client.Managers;
/*    */ import me.earth.phobos.features.modules.player.MultiTask;
/*    */ import me.earth.phobos.features.modules.render.NoRender;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.entity.EntityPlayerSP;
/*    */ import net.minecraft.client.multiplayer.PlayerControllerMP;
/*    */ import net.minecraft.client.multiplayer.WorldClient;
/*    */ import net.minecraft.crash.CrashReport;
/*    */ import org.lwjgl.opengl.Display;
/*    */ import org.spongepowered.asm.mixin.Mixin;
/*    */ import org.spongepowered.asm.mixin.injection.At;
/*    */ import org.spongepowered.asm.mixin.injection.Inject;
/*    */ import org.spongepowered.asm.mixin.injection.Redirect;
/*    */ import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
/*    */ import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
/*    */ 
/*    */ @Mixin({Minecraft.class})
/*    */ public abstract class MixinMinecraft
/*    */ {
/*    */   @Inject(method = {"Lnet/minecraft/client/Minecraft;getLimitFramerate()I"}, at = {@At("HEAD")}, cancellable = true)
/*    */   public void getLimitFramerateHook(CallbackInfoReturnable<Integer> callbackInfoReturnable) {
/*    */     try {
/* 26 */       if (((Boolean)(Managers.getInstance()).unfocusedCpu.getValue()).booleanValue() && !Display.isActive()) {
/* 27 */         callbackInfoReturnable.setReturnValue((Managers.getInstance()).cpuFPS.getValue());
/*    */       }
/* 29 */     } catch (NullPointerException nullPointerException) {}
/*    */   }
/*    */   
/*    */   @Redirect(method = {"runGameLoop"}, at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/Display;sync(I)V"))
/*    */   public void syncHook(int maxFps) {
/* 34 */     if (((Boolean)(Managers.getInstance()).betterFrames.getValue()).booleanValue()) {
/* 35 */       Display.sync(((Integer)(Managers.getInstance()).betterFPS.getValue()).intValue());
/*    */     } else {
/* 37 */       Display.sync(maxFps);
/*    */     } 
/*    */   }
/*    */   
/*    */   @Redirect(method = {"run"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;displayCrashReport(Lnet/minecraft/crash/CrashReport;)V"))
/*    */   public void displayCrashReportHook(Minecraft minecraft, CrashReport crashReport) {
/* 43 */     unload();
/*    */   }
/*    */   
/*    */   @Redirect(method = {"runTick"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/WorldClient;doVoidFogParticles(III)V"))
/*    */   public void doVoidFogParticlesHook(WorldClient world, int x, int y, int z) {
/* 48 */     NoRender.getInstance().doVoidFogParticles(x, y, z);
/*    */   }
/*    */   
/*    */   @Inject(method = {"shutdown"}, at = {@At("HEAD")})
/*    */   public void shutdownHook(CallbackInfo info) {
/* 53 */     unload();
/*    */   }
/*    */   
/*    */   private void unload() {
/* 57 */     System.out.println("Shutting down: saving configuration");
/* 58 */     Phobos.onUnload();
/* 59 */     System.out.println("Configuration saved.");
/*    */   }
/*    */   
/*    */   @Redirect(method = {"sendClickBlockToController"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/entity/EntityPlayerSP;isHandActive()Z"))
/*    */   private boolean isHandActiveWrapper(EntityPlayerSP playerSP) {
/* 64 */     return (!MultiTask.getInstance().isOn() && playerSP.func_184587_cr());
/*    */   }
/*    */   
/*    */   @Redirect(method = {"rightClickMouse"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/PlayerControllerMP;getIsHittingBlock()Z", ordinal = 0), require = 1)
/*    */   private boolean isHittingBlockHook(PlayerControllerMP playerControllerMP) {
/* 69 */     return (!MultiTask.getInstance().isOn() && playerControllerMP.func_181040_m());
/*    */   }
/*    */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\me\earth\phobos\mixin\mixins\MixinMinecraft.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */