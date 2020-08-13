/*    */ package me.earth.phobos.features.modules.movement;
/*    */ 
/*    */ import me.earth.phobos.event.events.KeyEvent;
/*    */ import me.earth.phobos.features.modules.Module;
/*    */ import me.earth.phobos.features.setting.Setting;
/*    */ import net.minecraft.client.settings.KeyBinding;
/*    */ import net.minecraftforge.client.event.InputUpdateEvent;
/*    */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*    */ import org.lwjgl.input.Keyboard;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class NoSlowDown
/*    */   extends Module
/*    */ {
/* 19 */   public Setting<Boolean> guiMove = register(new Setting("GuiMove", Boolean.valueOf(true)));
/* 20 */   public Setting<Boolean> noSlow = register(new Setting("NoSlow", Boolean.valueOf(true)));
/* 21 */   public Setting<Boolean> soulSand = register(new Setting("SoulSand", Boolean.valueOf(true)));
/*    */   
/* 23 */   private static NoSlowDown INSTANCE = new NoSlowDown();
/*    */   
/* 25 */   private static KeyBinding[] keys = new KeyBinding[] { mc.field_71474_y.field_74351_w, mc.field_71474_y.field_74368_y, mc.field_71474_y.field_74370_x, mc.field_71474_y.field_74366_z, mc.field_71474_y.field_74314_A, mc.field_71474_y.field_151444_V };
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public NoSlowDown() {
/* 35 */     super("NoSlowDown", "Prevents you from getting slowed down.", Module.Category.MOVEMENT, true, false, false);
/* 36 */     setInstance();
/*    */   }
/*    */   
/*    */   private void setInstance() {
/* 40 */     INSTANCE = this;
/*    */   }
/*    */   
/*    */   public static NoSlowDown getInstance() {
/* 44 */     if (INSTANCE == null) {
/* 45 */       INSTANCE = new NoSlowDown();
/*    */     }
/* 47 */     return INSTANCE;
/*    */   }
/*    */ 
/*    */   
/*    */   public void onUpdate() {
/* 52 */     if (((Boolean)this.guiMove.getValue()).booleanValue()) {
/* 53 */       if (mc.field_71462_r instanceof net.minecraft.client.gui.GuiOptions || mc.field_71462_r instanceof net.minecraft.client.gui.GuiVideoSettings || mc.field_71462_r instanceof net.minecraft.client.gui.GuiScreenOptionsSounds || mc.field_71462_r instanceof net.minecraft.client.gui.inventory.GuiContainer || mc.field_71462_r instanceof net.minecraft.client.gui.GuiIngameMenu) {
/*    */ 
/*    */ 
/*    */ 
/*    */         
/* 58 */         for (KeyBinding bind : keys) {
/* 59 */           KeyBinding.func_74510_a(bind.func_151463_i(), Keyboard.isKeyDown(bind.func_151463_i()));
/*    */         }
/* 61 */       } else if (mc.field_71462_r == null) {
/* 62 */         for (KeyBinding bind : keys) {
/* 63 */           if (!Keyboard.isKeyDown(bind.func_151463_i())) {
/* 64 */             KeyBinding.func_74510_a(bind.func_151463_i(), false);
/*    */           }
/*    */         } 
/*    */       } 
/*    */     }
/*    */   }
/*    */   
/*    */   @SubscribeEvent
/*    */   public void onInput(InputUpdateEvent event) {
/* 73 */     if (((Boolean)this.noSlow.getValue()).booleanValue() && mc.field_71439_g.func_184587_cr() && !mc.field_71439_g.func_184218_aH()) {
/* 74 */       (event.getMovementInput()).field_78902_a *= 5.0F;
/* 75 */       (event.getMovementInput()).field_192832_b *= 5.0F;
/*    */     } 
/*    */   }
/*    */   
/*    */   @SubscribeEvent
/*    */   public void onKeyEvent(KeyEvent event) {
/* 81 */     if (((Boolean)this.guiMove.getValue()).booleanValue() && event.getStage() == 0 && !(mc.field_71462_r instanceof net.minecraft.client.gui.GuiChat))
/* 82 */       event.info = event.pressed; 
/*    */   }
/*    */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\me\earth\phobos\features\modules\movement\NoSlowDown.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */