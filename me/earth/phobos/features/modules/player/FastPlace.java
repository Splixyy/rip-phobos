/*    */ package me.earth.phobos.features.modules.player;
/*    */ 
/*    */ import me.earth.phobos.Phobos;
/*    */ import me.earth.phobos.event.events.UpdateWalkingPlayerEvent;
/*    */ import me.earth.phobos.features.modules.Module;
/*    */ import me.earth.phobos.features.setting.Setting;
/*    */ import me.earth.phobos.util.InventoryUtil;
/*    */ import net.minecraft.block.BlockEnderChest;
/*    */ import net.minecraft.block.BlockObsidian;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.init.Items;
/*    */ import net.minecraft.item.ItemEndCrystal;
/*    */ import net.minecraft.item.ItemExpBottle;
/*    */ import net.minecraft.network.Packet;
/*    */ import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
/*    */ import net.minecraft.util.EnumFacing;
/*    */ import net.minecraft.util.EnumHand;
/*    */ import net.minecraft.util.math.BlockPos;
/*    */ import net.minecraft.util.math.RayTraceResult;
/*    */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*    */ 
/*    */ public class FastPlace extends Module {
/* 23 */   private Setting<Boolean> all = register(new Setting("All", Boolean.valueOf(false)));
/* 24 */   private Setting<Boolean> obby = register(new Setting("Obsidian", Boolean.valueOf(false), v -> !((Boolean)this.all.getValue()).booleanValue()));
/* 25 */   private Setting<Boolean> enderChests = register(new Setting("EnderChests", Boolean.valueOf(false), v -> !((Boolean)this.all.getValue()).booleanValue()));
/* 26 */   private Setting<Boolean> crystals = register(new Setting("Crystals", Boolean.valueOf(false), v -> !((Boolean)this.all.getValue()).booleanValue()));
/* 27 */   private Setting<Boolean> exp = register(new Setting("Experience", Boolean.valueOf(false), v -> !((Boolean)this.all.getValue()).booleanValue()));
/* 28 */   private Setting<Boolean> feetExp = register(new Setting("ExpFeet", Boolean.valueOf(false)));
/* 29 */   private Setting<Boolean> fastCrystal = register(new Setting("PacketCrystal", Boolean.valueOf(false)));
/*    */   
/* 31 */   private BlockPos mousePos = null;
/*    */   
/*    */   public FastPlace() {
/* 34 */     super("FastPlace", "Fast everything.", Module.Category.PLAYER, true, false, false);
/*    */   }
/*    */   
/*    */   @SubscribeEvent
/*    */   public void onUpdateWalkingPlayer(UpdateWalkingPlayerEvent event) {
/* 39 */     if (event.getStage() == 0 && ((Boolean)this.feetExp.getValue()).booleanValue()) {
/* 40 */       boolean mainHand = (mc.field_71439_g.func_184614_ca().func_77973_b() == Items.field_151062_by);
/* 41 */       boolean offHand = (mc.field_71439_g.func_184592_cb().func_77973_b() == Items.field_151062_by);
/* 42 */       if (mc.field_71474_y.field_74313_G.func_151470_d() && ((mc.field_71439_g.func_184600_cs() == EnumHand.MAIN_HAND && mainHand) || (mc.field_71439_g.func_184600_cs() == EnumHand.OFF_HAND && offHand))) {
/* 43 */         Phobos.rotationManager.lookAtVec3d(mc.field_71439_g.func_174791_d());
/*    */       }
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void onUpdate() {
/* 50 */     if (fullNullCheck()) {
/*    */       return;
/*    */     }
/*    */     
/* 54 */     if (InventoryUtil.holdingItem(ItemExpBottle.class) && ((Boolean)this.exp.getValue()).booleanValue()) {
/* 55 */       mc.field_71467_ac = 0;
/*    */     }
/*    */     
/* 58 */     if (InventoryUtil.holdingItem(BlockObsidian.class) && ((Boolean)this.obby.getValue()).booleanValue()) {
/* 59 */       mc.field_71467_ac = 0;
/*    */     }
/*    */     
/* 62 */     if (InventoryUtil.holdingItem(BlockEnderChest.class) && ((Boolean)this.enderChests.getValue()).booleanValue()) {
/* 63 */       mc.field_71467_ac = 0;
/*    */     }
/*    */     
/* 66 */     if (((Boolean)this.all.getValue()).booleanValue()) {
/* 67 */       mc.field_71467_ac = 0;
/*    */     }
/*    */     
/* 70 */     if (InventoryUtil.holdingItem(ItemEndCrystal.class) && (((Boolean)this.crystals.getValue()).booleanValue() || ((Boolean)this.all.getValue()).booleanValue())) {
/* 71 */       mc.field_71467_ac = 0;
/*    */     }
/*    */     
/* 74 */     if (((Boolean)this.fastCrystal.getValue()).booleanValue() && mc.field_71474_y.field_74313_G.func_151470_d()) {
/* 75 */       boolean offhand = (mc.field_71439_g.func_184592_cb().func_77973_b() == Items.field_185158_cP);
/* 76 */       if (offhand || mc.field_71439_g.func_184614_ca().func_77973_b() == Items.field_185158_cP) {
/* 77 */         RayTraceResult result = mc.field_71476_x;
/* 78 */         if (result == null) {
/*    */           return;
/*    */         }
/*    */         
/* 82 */         switch (result.field_72313_a) {
/*    */           case MISS:
/* 84 */             this.mousePos = null;
/*    */             break;
/*    */           case BLOCK:
/* 87 */             this.mousePos = mc.field_71476_x.func_178782_a();
/*    */             break;
/*    */           case ENTITY:
/* 90 */             if (this.mousePos != null) {
/* 91 */               Entity entity = result.field_72308_g;
/* 92 */               if (entity != null && 
/* 93 */                 this.mousePos.equals(new BlockPos(entity.field_70165_t, entity.field_70163_u - 1.0D, entity.field_70161_v)))
/* 94 */                 mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayerTryUseItemOnBlock(this.mousePos, EnumFacing.DOWN, offhand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, 0.0F, 0.0F, 0.0F)); 
/*    */             } 
/*    */             break;
/*    */         } 
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\me\earth\phobos\features\modules\player\FastPlace.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */