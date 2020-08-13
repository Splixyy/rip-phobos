/*    */ package me.earth.phobos.mixin.mixins;
/*    */ 
/*    */ import me.earth.phobos.event.events.KeyEvent;
/*    */ import net.minecraft.client.settings.KeyBinding;
/*    */ import net.minecraftforge.common.MinecraftForge;
/*    */ import net.minecraftforge.fml.common.eventhandler.Event;
/*    */ import org.spongepowered.asm.mixin.Mixin;
/*    */ import org.spongepowered.asm.mixin.Shadow;
/*    */ import org.spongepowered.asm.mixin.injection.At;
/*    */ import org.spongepowered.asm.mixin.injection.Inject;
/*    */ import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
/*    */ 
/*    */ @Mixin({KeyBinding.class})
/*    */ public class MixinKeyBinding
/*    */ {
/*    */   @Shadow
/*    */   private boolean field_74513_e;
/*    */   
/*    */   @Inject(method = {"isKeyDown"}, at = {@At("RETURN")}, cancellable = true)
/*    */   private void isKeyDown(CallbackInfoReturnable<Boolean> info) {
/* 21 */     KeyEvent event = new KeyEvent(0, ((Boolean)info.getReturnValue()).booleanValue(), this.field_74513_e);
/* 22 */     MinecraftForge.EVENT_BUS.post((Event)event);
/* 23 */     info.setReturnValue(Boolean.valueOf(event.info));
/*    */   }
/*    */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\me\earth\phobos\mixin\mixins\MixinKeyBinding.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */