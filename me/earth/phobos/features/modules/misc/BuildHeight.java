/*    */ package me.earth.phobos.features.modules.misc;
/*    */ 
/*    */ import me.earth.phobos.event.events.PacketEvent;
/*    */ import me.earth.phobos.features.modules.Module;
/*    */ import me.earth.phobos.features.setting.Setting;
/*    */ import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
/*    */ import net.minecraft.util.EnumFacing;
/*    */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*    */ 
/*    */ public class BuildHeight
/*    */   extends Module {
/* 12 */   private Setting<Integer> height = register(new Setting("Height", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255)));
/*    */   
/*    */   public BuildHeight() {
/* 15 */     super("BuildHeight", "Allows you to place at build height", Module.Category.MISC, true, false, false);
/*    */   }
/*    */   
/*    */   @SubscribeEvent
/*    */   public void onPacketSend(PacketEvent.Send event) {
/* 20 */     if (event.getStage() == 0 && 
/* 21 */       event.getPacket() instanceof CPacketPlayerTryUseItemOnBlock) {
/* 22 */       CPacketPlayerTryUseItemOnBlock packet = (CPacketPlayerTryUseItemOnBlock)event.getPacket();
/* 23 */       if (packet.func_187023_a().func_177956_o() >= ((Integer)this.height.getValue()).intValue() && packet.func_187024_b() == EnumFacing.UP)
/* 24 */         packet.field_149579_d = EnumFacing.DOWN; 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\me\earth\phobos\features\modules\misc\BuildHeight.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */