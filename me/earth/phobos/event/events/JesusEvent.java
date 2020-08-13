/*    */ package me.earth.phobos.event.events;
/*    */ 
/*    */ import me.earth.phobos.event.EventStage;
/*    */ import net.minecraft.util.math.AxisAlignedBB;
/*    */ import net.minecraft.util.math.BlockPos;
/*    */ import net.minecraftforge.fml.common.eventhandler.Cancelable;
/*    */ 
/*    */ @Cancelable
/*    */ public class JesusEvent
/*    */   extends EventStage {
/*    */   private BlockPos pos;
/*    */   private AxisAlignedBB boundingBox;
/*    */   
/*    */   public JesusEvent(int stage, BlockPos pos) {
/* 15 */     super(stage);
/* 16 */     this.pos = pos;
/*    */   }
/*    */   
/*    */   public void setBoundingBox(AxisAlignedBB boundingBox) {
/* 20 */     this.boundingBox = boundingBox;
/*    */   }
/*    */   
/*    */   public void setPos(BlockPos pos) {
/* 24 */     this.pos = pos;
/*    */   }
/*    */   
/*    */   public BlockPos getPos() {
/* 28 */     return this.pos;
/*    */   }
/*    */   
/*    */   public AxisAlignedBB getBoundingBox() {
/* 32 */     return this.boundingBox;
/*    */   }
/*    */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\me\earth\phobos\event\events\JesusEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */