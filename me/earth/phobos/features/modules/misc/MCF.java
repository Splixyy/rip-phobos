/*    */ package me.earth.phobos.features.modules.misc;
/*    */ 
/*    */ import me.earth.phobos.Phobos;
/*    */ import me.earth.phobos.features.command.Command;
/*    */ import me.earth.phobos.features.modules.Module;
/*    */ import me.earth.phobos.features.setting.Bind;
/*    */ import me.earth.phobos.features.setting.Setting;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.util.math.RayTraceResult;
/*    */ import net.minecraftforge.fml.common.eventhandler.EventPriority;
/*    */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*    */ import net.minecraftforge.fml.common.gameevent.InputEvent;
/*    */ import org.lwjgl.input.Keyboard;
/*    */ import org.lwjgl.input.Mouse;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class MCF
/*    */   extends Module
/*    */ {
/* 21 */   private final Setting<Boolean> middleClick = register(new Setting("MiddleClick", Boolean.valueOf(true)));
/* 22 */   private final Setting<Boolean> keyboard = register(new Setting("Keyboard", Boolean.valueOf(false)));
/* 23 */   private final Setting<Bind> key = register(new Setting("KeyBind", new Bind(-1), v -> ((Boolean)this.keyboard.getValue()).booleanValue()));
/*    */   
/*    */   private boolean clicked = false;
/*    */   
/*    */   public MCF() {
/* 28 */     super("MCF", "Middleclick Friends.", Module.Category.MISC, true, false, false);
/*    */   }
/*    */ 
/*    */   
/*    */   public void onUpdate() {
/* 33 */     if (Mouse.isButtonDown(2)) {
/* 34 */       if (!this.clicked && ((Boolean)this.middleClick.getValue()).booleanValue() && mc.field_71462_r == null) {
/* 35 */         onClick();
/*    */       }
/* 37 */       this.clicked = true;
/*    */     } else {
/* 39 */       this.clicked = false;
/*    */     } 
/*    */   }
/*    */   
/*    */   @SubscribeEvent(priority = EventPriority.NORMAL, receiveCanceled = true)
/*    */   public void onKeyInput(InputEvent.KeyInputEvent event) {
/* 45 */     if (((Boolean)this.keyboard.getValue()).booleanValue() && Keyboard.getEventKeyState() && !(mc.field_71462_r instanceof me.earth.phobos.features.gui.PhobosGui) && ((Bind)this.key.getValue()).getKey() == Keyboard.getEventKey()) {
/* 46 */       onClick();
/*    */     }
/*    */   }
/*    */   
/*    */   private void onClick() {
/* 51 */     RayTraceResult result = mc.field_71476_x;
/* 52 */     if (result != null && result.field_72313_a == RayTraceResult.Type.ENTITY) {
/* 53 */       Entity entity = result.field_72308_g;
/* 54 */       if (entity instanceof net.minecraft.entity.player.EntityPlayer) {
/* 55 */         if (Phobos.friendManager.isFriend(entity.func_70005_c_())) {
/* 56 */           Phobos.friendManager.removeFriend(entity.func_70005_c_());
/* 57 */           Command.sendMessage("§c" + entity.func_70005_c_() + "§r" + " unfriended.");
/*    */         } else {
/* 59 */           Phobos.friendManager.addFriend(entity.func_70005_c_());
/* 60 */           Command.sendMessage("§b" + entity.func_70005_c_() + "§r" + " friended.");
/*    */         } 
/*    */       }
/*    */     } 
/* 64 */     this.clicked = true;
/*    */   }
/*    */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\me\earth\phobos\features\modules\misc\MCF.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */