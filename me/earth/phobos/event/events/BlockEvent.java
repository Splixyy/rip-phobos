/*    */ package me.earth.phobos.event.events;
/*    */ 
/*    */ import me.earth.phobos.event.EventStage;
/*    */ import net.minecraft.util.EnumFacing;
/*    */ import net.minecraft.util.math.BlockPos;
/*    */ import net.minecraftforge.fml.common.eventhandler.Cancelable;
/*    */ 
/*    */ @Cancelable
/*    */ public class BlockEvent
/*    */   extends EventStage {
/*    */   public BlockPos pos;
/*    */   public EnumFacing facing;
/*    */   
/*    */   public BlockEvent(int stage, BlockPos pos, EnumFacing facing) {
/* 15 */     super(stage);
/* 16 */     this.pos = pos;
/* 17 */     this.facing = facing;
/*    */   }
/*    */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\me\earth\phobos\event\events\BlockEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */