/*    */ package me.earth.phobos.features.modules.player;
/*    */ import me.earth.phobos.features.modules.Module;
/*    */ import me.earth.phobos.features.setting.Setting;
/*    */ import me.earth.phobos.util.InventoryUtil;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.init.Items;
/*    */ import net.minecraft.item.ItemEnderPearl;
/*    */ import net.minecraft.util.EnumHand;
/*    */ import net.minecraft.world.World;
/*    */ import org.lwjgl.input.Mouse;
/*    */ 
/*    */ public class MCP extends Module {
/* 13 */   private Setting<Mode> mode = register(new Setting("Mode", Mode.MIDDLECLICK));
/* 14 */   private Setting<Boolean> stopRotation = register(new Setting("Rotation", Boolean.valueOf(true)));
/* 15 */   private Setting<Integer> rotation = register(new Setting("Delay", Integer.valueOf(10), Integer.valueOf(0), Integer.valueOf(100), v -> ((Boolean)this.stopRotation.getValue()).booleanValue()));
/*    */   
/*    */   private boolean clicked = false;
/*    */ 
/*    */   
/*    */   public MCP() {
/* 21 */     super("MCP", "Throws a pearl", Module.Category.PLAYER, false, false, false);
/*    */   }
/*    */ 
/*    */   
/*    */   public void onEnable() {
/* 26 */     if (!fullNullCheck() && this.mode.getValue() == Mode.TOGGLE) {
/* 27 */       throwPearl();
/* 28 */       disable();
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void onTick() {
/* 34 */     if (this.mode.getValue() == Mode.MIDDLECLICK) {
/* 35 */       if (Mouse.isButtonDown(2)) {
/* 36 */         if (!this.clicked) {
/* 37 */           throwPearl();
/*    */         }
/* 39 */         this.clicked = true;
/*    */       } else {
/* 41 */         this.clicked = false;
/*    */       } 
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private void throwPearl() {
/* 57 */     int pearlSlot = InventoryUtil.findHotbarBlock(ItemEnderPearl.class);
/* 58 */     boolean offhand = (mc.field_71439_g.func_184592_cb().func_77973_b() == Items.field_151079_bi);
/* 59 */     if (pearlSlot != -1 || offhand) {
/* 60 */       int oldslot = mc.field_71439_g.field_71071_by.field_70461_c;
/* 61 */       if (!offhand) {
/* 62 */         InventoryUtil.switchToHotbarSlot(pearlSlot, false);
/*    */       }
/* 64 */       mc.field_71442_b.func_187101_a((EntityPlayer)mc.field_71439_g, (World)mc.field_71441_e, offhand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND);
/* 65 */       if (!offhand) {
/* 66 */         InventoryUtil.switchToHotbarSlot(oldslot, false);
/*    */       }
/*    */     } 
/*    */   }
/*    */   
/*    */   public enum Mode
/*    */   {
/* 73 */     TOGGLE,
/* 74 */     MIDDLECLICK;
/*    */   }
/*    */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\me\earth\phobos\features\modules\player\MCP.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */