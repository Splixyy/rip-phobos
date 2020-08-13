/*    */ package me.earth.phobos.mixin.mixins;
/*    */ 
/*    */ import me.earth.phobos.Phobos;
/*    */ import me.earth.phobos.event.events.ChatEvent;
/*    */ import me.earth.phobos.event.events.MoveEvent;
/*    */ import me.earth.phobos.event.events.PushEvent;
/*    */ import me.earth.phobos.event.events.UpdateWalkingPlayerEvent;
/*    */ import me.earth.phobos.features.modules.misc.BetterPortals;
/*    */ import me.earth.phobos.features.modules.movement.Sprint;
/*    */ import me.earth.phobos.util.Util;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.entity.AbstractClientPlayer;
/*    */ import net.minecraft.client.entity.EntityPlayerSP;
/*    */ import net.minecraft.client.gui.GuiScreen;
/*    */ import net.minecraft.client.network.NetHandlerPlayClient;
/*    */ import net.minecraft.entity.MoverType;
/*    */ import net.minecraft.stats.RecipeBook;
/*    */ import net.minecraft.stats.StatisticsManager;
/*    */ import net.minecraft.world.World;
/*    */ import net.minecraftforge.common.MinecraftForge;
/*    */ import net.minecraftforge.fml.common.eventhandler.Event;
/*    */ import org.spongepowered.asm.mixin.Mixin;
/*    */ import org.spongepowered.asm.mixin.injection.At;
/*    */ import org.spongepowered.asm.mixin.injection.Inject;
/*    */ import org.spongepowered.asm.mixin.injection.Redirect;
/*    */ import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
/*    */ import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
/*    */ 
/*    */ @Mixin(value = {EntityPlayerSP.class}, priority = 9998)
/*    */ public abstract class MixinEntityPlayerSP extends AbstractClientPlayer {
/*    */   public MixinEntityPlayerSP(Minecraft p_i47378_1_, World p_i47378_2_, NetHandlerPlayClient p_i47378_3_, StatisticsManager p_i47378_4_, RecipeBook p_i47378_5_) {
/* 32 */     super(p_i47378_2_, p_i47378_3_.func_175105_e());
/*    */   }
/*    */   
/*    */   @Inject(method = {"sendChatMessage"}, at = {@At("HEAD")}, cancellable = true)
/*    */   public void sendChatMessage(String message, CallbackInfo callback) {
/* 37 */     ChatEvent chatEvent = new ChatEvent(message);
/* 38 */     MinecraftForge.EVENT_BUS.post((Event)chatEvent);
/*    */   }
/*    */   
/*    */   @Redirect(method = {"onLivingUpdate"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/entity/EntityPlayerSP;closeScreen()V"))
/*    */   public void closeScreenHook(EntityPlayerSP entityPlayerSP) {
/* 43 */     if (!BetterPortals.getInstance().isOn() || !((Boolean)(BetterPortals.getInstance()).portalChat.getValue()).booleanValue()) {
/* 44 */       entityPlayerSP.func_71053_j();
/*    */     }
/*    */   }
/*    */   
/*    */   @Redirect(method = {"onLivingUpdate"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;displayGuiScreen(Lnet/minecraft/client/gui/GuiScreen;)V"))
/*    */   public void displayGuiScreenHook(Minecraft mc, GuiScreen screen) {
/* 50 */     if (!BetterPortals.getInstance().isOn() || !((Boolean)(BetterPortals.getInstance()).portalChat.getValue()).booleanValue()) {
/* 51 */       mc.func_147108_a(screen);
/*    */     }
/*    */   }
/*    */   
/*    */   @Redirect(method = {"onLivingUpdate"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/entity/EntityPlayerSP;setSprinting(Z)V", ordinal = 2))
/*    */   public void onLivingUpdate(EntityPlayerSP entityPlayerSP, boolean sprinting) {
/* 57 */     if (Sprint.getInstance().isOn() && (Sprint.getInstance()).mode.getValue() == Sprint.Mode.RAGE && (Util.mc.field_71439_g.field_71158_b.field_192832_b != 0.0F || Util.mc.field_71439_g.field_71158_b.field_78902_a != 0.0F)) {
/* 58 */       entityPlayerSP.func_70031_b(true);
/*    */     } else {
/* 60 */       entityPlayerSP.func_70031_b(sprinting);
/*    */     } 
/*    */   }
/*    */   
/*    */   @Inject(method = {"pushOutOfBlocks"}, at = {@At("HEAD")}, cancellable = true)
/*    */   private void pushOutOfBlocksHook(double x, double y, double z, CallbackInfoReturnable<Boolean> info) {
/* 66 */     PushEvent event = new PushEvent(1);
/* 67 */     MinecraftForge.EVENT_BUS.post((Event)event);
/* 68 */     if (event.isCanceled()) {
/* 69 */       info.setReturnValue(Boolean.valueOf(false));
/*    */     }
/*    */   }
/*    */   
/*    */   @Inject(method = {"onUpdateWalkingPlayer"}, at = {@At("HEAD")})
/*    */   private void preMotion(CallbackInfo info) {
/* 75 */     UpdateWalkingPlayerEvent event = new UpdateWalkingPlayerEvent(0);
/* 76 */     MinecraftForge.EVENT_BUS.post((Event)event);
/*    */   }
/*    */   
/*    */   @Inject(method = {"onUpdateWalkingPlayer"}, at = {@At("RETURN")})
/*    */   private void postMotion(CallbackInfo info) {
/* 81 */     UpdateWalkingPlayerEvent event = new UpdateWalkingPlayerEvent(1);
/* 82 */     MinecraftForge.EVENT_BUS.post((Event)event);
/*    */   }
/*    */   
/*    */   @Inject(method = {"Lnet/minecraft/client/entity/EntityPlayerSP;setServerBrand(Ljava/lang/String;)V"}, at = {@At("HEAD")})
/*    */   public void getBrand(String brand, CallbackInfo callbackInfo) {
/* 87 */     if (Phobos.serverManager != null) {
/* 88 */       Phobos.serverManager.setServerBrand(brand);
/*    */     }
/*    */   }
/*    */   
/*    */   @Redirect(method = {"move"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/entity/AbstractClientPlayer;move(Lnet/minecraft/entity/MoverType;DDD)V"))
/*    */   public void move(AbstractClientPlayer player, MoverType moverType, double x, double y, double z) {
/* 94 */     MoveEvent event = new MoveEvent(0, moverType, x, y, z);
/* 95 */     MinecraftForge.EVENT_BUS.post((Event)event);
/* 96 */     if (!event.isCanceled())
/* 97 */       func_70091_d(event.getType(), event.getX(), event.getY(), event.getZ()); 
/*    */   }
/*    */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\me\earth\phobos\mixin\mixins\MixinEntityPlayerSP.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */