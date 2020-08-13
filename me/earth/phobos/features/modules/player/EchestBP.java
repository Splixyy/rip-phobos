/*    */ package me.earth.phobos.features.modules.player;
/*    */ 
/*    */ import me.earth.phobos.features.modules.Module;
/*    */ import net.minecraft.client.gui.GuiScreen;
/*    */ import net.minecraft.client.gui.inventory.GuiContainer;
/*    */ import net.minecraft.inventory.Container;
/*    */ import net.minecraft.inventory.ContainerChest;
/*    */ import net.minecraft.inventory.InventoryBasic;
/*    */ 
/*    */ public class EchestBP
/*    */   extends Module {
/* 12 */   private GuiScreen echestScreen = null;
/*    */   
/*    */   public EchestBP() {
/* 15 */     super("EchestBP", "Allows to open your echest later.", Module.Category.PLAYER, false, false, false);
/*    */   }
/*    */ 
/*    */   
/*    */   public void onUpdate() {
/* 20 */     if (mc.field_71462_r instanceof GuiContainer) {
/* 21 */       Container container = ((GuiContainer)mc.field_71462_r).field_147002_h;
/* 22 */       if (container instanceof ContainerChest && ((ContainerChest)container).func_85151_d() instanceof InventoryBasic) {
/* 23 */         InventoryBasic basic = (InventoryBasic)((ContainerChest)container).func_85151_d();
/* 24 */         if (basic.func_70005_c_().equalsIgnoreCase("Ender Chest")) {
/* 25 */           this.echestScreen = mc.field_71462_r;
/* 26 */           mc.field_71462_r = null;
/*    */         } 
/*    */       } 
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void onDisable() {
/* 34 */     if (!fullNullCheck() && this.echestScreen != null) {
/* 35 */       mc.func_147108_a(this.echestScreen);
/*    */     }
/* 37 */     this.echestScreen = null;
/*    */   }
/*    */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\me\earth\phobos\features\modules\player\EchestBP.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */