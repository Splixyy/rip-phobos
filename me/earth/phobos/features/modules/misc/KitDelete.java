/*    */ package me.earth.phobos.features.modules.misc;
/*    */ 
/*    */ import me.earth.phobos.features.modules.Module;
/*    */ import me.earth.phobos.features.setting.Bind;
/*    */ import me.earth.phobos.features.setting.Setting;
/*    */ import me.earth.phobos.util.TextUtil;
/*    */ import net.minecraft.client.gui.inventory.GuiContainer;
/*    */ import net.minecraft.inventory.Slot;
/*    */ import org.lwjgl.input.Keyboard;
/*    */ 
/*    */ public class KitDelete
/*    */   extends Module {
/* 13 */   private Setting<Bind> deleteKey = register(new Setting("Key", new Bind(-1)));
/*    */   
/*    */   private boolean keyDown;
/*    */   
/*    */   public KitDelete() {
/* 18 */     super("KitDelete", "Automates /deleteukit", Module.Category.MISC, false, false, false);
/*    */   }
/*    */ 
/*    */   
/*    */   public void onTick() {
/* 23 */     if (((Bind)this.deleteKey.getValue()).getKey() != -1)
/* 24 */       if (mc.field_71462_r instanceof GuiContainer && Keyboard.isKeyDown(((Bind)this.deleteKey.getValue()).getKey())) {
/* 25 */         Slot slot = ((GuiContainer)mc.field_71462_r).getSlotUnderMouse();
/* 26 */         if (slot != null && !this.keyDown) {
/* 27 */           mc.field_71439_g.func_71165_d("/deleteukit " + TextUtil.stripColor(slot.func_75211_c().func_82833_r()));
/* 28 */           this.keyDown = true;
/*    */         } 
/* 30 */       } else if (this.keyDown) {
/* 31 */         this.keyDown = false;
/*    */       }  
/*    */   }
/*    */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\me\earth\phobos\features\modules\misc\KitDelete.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */