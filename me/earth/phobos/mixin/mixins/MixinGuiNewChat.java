/*    */ package me.earth.phobos.mixin.mixins;
/*    */ 
/*    */ import java.util.List;
/*    */ import me.earth.phobos.features.modules.misc.ChatModifier;
/*    */ import net.minecraft.client.gui.ChatLine;
/*    */ import net.minecraft.client.gui.Gui;
/*    */ import net.minecraft.client.gui.GuiNewChat;
/*    */ import org.spongepowered.asm.mixin.Mixin;
/*    */ import org.spongepowered.asm.mixin.injection.At;
/*    */ import org.spongepowered.asm.mixin.injection.Redirect;
/*    */ 
/*    */ @Mixin({GuiNewChat.class})
/*    */ public class MixinGuiNewChat
/*    */   extends Gui
/*    */ {
/*    */   @Redirect(method = {"drawChat"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiNewChat;drawRect(IIIII)V"))
/*    */   private void drawRectHook(int left, int top, int right, int bottom, int color) {
/* 18 */     Gui.func_73734_a(left, top, right, bottom, (ChatModifier.getInstance().isOn() && ((Boolean)(ChatModifier.getInstance()).clean.getValue()).booleanValue()) ? 0 : color);
/*    */   }
/*    */   
/*    */   @Redirect(method = {"setChatLine"}, at = @At(value = "INVOKE", target = "Ljava/util/List;size()I", ordinal = 0))
/*    */   public int drawnChatLinesSize(List<ChatLine> list) {
/* 23 */     return (ChatModifier.getInstance().isOn() && ((Boolean)(ChatModifier.getInstance()).infinite.getValue()).booleanValue()) ? -2147483647 : list.size();
/*    */   }
/*    */   
/*    */   @Redirect(method = {"setChatLine"}, at = @At(value = "INVOKE", target = "Ljava/util/List;size()I", ordinal = 2))
/*    */   public int chatLinesSize(List<ChatLine> list) {
/* 28 */     return (ChatModifier.getInstance().isOn() && ((Boolean)(ChatModifier.getInstance()).infinite.getValue()).booleanValue()) ? -2147483647 : list.size();
/*    */   }
/*    */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\me\earth\phobos\mixin\mixins\MixinGuiNewChat.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */