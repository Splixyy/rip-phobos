/*    */ package me.earth.phobos.features.modules.combat;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import me.earth.phobos.Phobos;
/*    */ import me.earth.phobos.event.events.UpdateWalkingPlayerEvent;
/*    */ import me.earth.phobos.features.command.Command;
/*    */ import me.earth.phobos.features.modules.Module;
/*    */ import me.earth.phobos.features.setting.Setting;
/*    */ import me.earth.phobos.util.DamageUtil;
/*    */ import me.earth.phobos.util.Timer;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.init.Items;
/*    */ import net.minecraft.item.ItemStack;
/*    */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*    */ 
/*    */ public class ArmorMessage
/*    */   extends Module
/*    */ {
/* 20 */   private final Setting<Integer> armorThreshhold = register(new Setting("Armor%", Integer.valueOf(20), Integer.valueOf(1), Integer.valueOf(100)));
/* 21 */   private final Setting<Boolean> notifySelf = register(new Setting("NotifySelf", Boolean.valueOf(true)));
/* 22 */   private final Setting<Boolean> notification = register(new Setting("Notification", Boolean.valueOf(true)));
/* 23 */   private final Map<EntityPlayer, Integer> entityArmorArraylist = new HashMap<>();
/* 24 */   private final Timer timer = new Timer();
/*    */   
/*    */   public ArmorMessage() {
/* 27 */     super("ArmorMessage", "Message friends when their armor is low", Module.Category.COMBAT, true, false, false);
/*    */   }
/*    */   
/*    */   @SubscribeEvent
/*    */   public void onUpdate(UpdateWalkingPlayerEvent event) {
/* 32 */     for (EntityPlayer player : mc.field_71441_e.field_73010_i) {
/* 33 */       if (player.field_70128_L || !Phobos.friendManager.isFriend(player.func_70005_c_()))
/* 34 */         continue;  for (ItemStack stack : player.field_71071_by.field_70460_b) {
/* 35 */         if (stack != ItemStack.field_190927_a) {
/* 36 */           int percent = DamageUtil.getRoundedDamage(stack);
/* 37 */           if (percent <= ((Integer)this.armorThreshhold.getValue()).intValue() && !this.entityArmorArraylist.containsKey(player)) {
/* 38 */             if (player == mc.field_71439_g && ((Boolean)this.notifySelf.getValue()).booleanValue()) {
/* 39 */               Command.sendMessage(player.func_70005_c_() + " watchout your " + getArmorPieceName(stack) + " low dura!", ((Boolean)this.notification.getValue()).booleanValue());
/*    */             } else {
/* 41 */               mc.field_71439_g.func_71165_d("/msg " + player.func_70005_c_() + " " + player.func_70005_c_() + " watchout your " + getArmorPieceName(stack) + " low dura!");
/* 42 */             }  this.entityArmorArraylist.put(player, Integer.valueOf(player.field_71071_by.field_70460_b.indexOf(stack)));
/*    */           } 
/* 44 */           if (this.entityArmorArraylist.containsKey(player) && ((Integer)this.entityArmorArraylist.get(player)).intValue() == player.field_71071_by.field_70460_b.indexOf(stack) && percent > ((Integer)this.armorThreshhold.getValue()).intValue()) {
/* 45 */             this.entityArmorArraylist.remove(player);
/*    */           }
/*    */         } 
/*    */       } 
/* 49 */       if (this.entityArmorArraylist.containsKey(player) && player.field_71071_by.field_70460_b.get(((Integer)this.entityArmorArraylist.get(player)).intValue()) == ItemStack.field_190927_a) {
/* 50 */         this.entityArmorArraylist.remove(player);
/*    */       }
/*    */     } 
/*    */   }
/*    */   
/*    */   private String getArmorPieceName(ItemStack stack) {
/* 56 */     if (stack.func_77973_b() == Items.field_151161_ac || stack.func_77973_b() == Items.field_151169_ag || stack.func_77973_b() == Items.field_151028_Y || stack.func_77973_b() == Items.field_151020_U || stack.func_77973_b() == Items.field_151024_Q)
/* 57 */       return "helmet is"; 
/* 58 */     if (stack.func_77973_b() == Items.field_151163_ad || stack.func_77973_b() == Items.field_151171_ah || stack.func_77973_b() == Items.field_151030_Z || stack.func_77973_b() == Items.field_151023_V || stack.func_77973_b() == Items.field_151027_R)
/* 59 */       return "chestplate is"; 
/* 60 */     if (stack.func_77973_b() == Items.field_151173_ae || stack.func_77973_b() == Items.field_151149_ai || stack.func_77973_b() == Items.field_151165_aa || stack.func_77973_b() == Items.field_151022_W || stack.func_77973_b() == Items.field_151026_S)
/* 61 */       return "leggings are"; 
/* 62 */     return "boots are";
/*    */   }
/*    */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\me\earth\phobos\features\modules\combat\ArmorMessage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */