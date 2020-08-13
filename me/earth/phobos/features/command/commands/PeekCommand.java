/*    */ package me.earth.phobos.features.command.commands;
/*    */ 
/*    */ import java.util.Map;
/*    */ import me.earth.phobos.features.command.Command;
/*    */ import me.earth.phobos.features.modules.misc.ToolTips;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.item.ItemStack;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class PeekCommand
/*    */   extends Command
/*    */ {
/*    */   public PeekCommand() {
/* 15 */     super("peek", new String[] { "<player>" });
/*    */   }
/*    */ 
/*    */   
/*    */   public void execute(String[] commands) {
/* 20 */     if (commands.length == 1) {
/* 21 */       ItemStack stack = mc.field_71439_g.func_184614_ca();
/* 22 */       if (stack != null && stack.func_77973_b() instanceof net.minecraft.item.ItemShulkerBox) {
/* 23 */         ToolTips.displayInv(stack, null);
/*    */       } else {
/* 25 */         Command.sendMessage("§cYou need to hold a Shulker in your mainhand.");
/*    */         
/*    */         return;
/*    */       } 
/*    */     } 
/* 30 */     if (commands.length > 1)
/* 31 */       if (ToolTips.getInstance().isOn() && ((Boolean)(ToolTips.getInstance()).shulkerSpy.getValue()).booleanValue()) {
/* 32 */         for (Map.Entry<EntityPlayer, ItemStack> entry : (Iterable<Map.Entry<EntityPlayer, ItemStack>>)(ToolTips.getInstance()).spiedPlayers.entrySet()) {
/* 33 */           if (((EntityPlayer)entry.getKey()).func_70005_c_().equalsIgnoreCase(commands[0])) {
/* 34 */             ItemStack stack = entry.getValue();
/* 35 */             ToolTips.displayInv(stack, ((EntityPlayer)entry.getKey()).func_70005_c_());
/*    */             break;
/*    */           } 
/*    */         } 
/*    */       } else {
/* 40 */         Command.sendMessage("§cYou need to turn on Tooltips - ShulkerSpy");
/*    */       }  
/*    */   }
/*    */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\me\earth\phobos\features\command\commands\PeekCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */