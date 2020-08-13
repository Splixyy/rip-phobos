/*    */ package me.earth.phobos.event.events;
/*    */ 
/*    */ import me.earth.phobos.event.EventStage;
/*    */ import net.minecraft.client.model.ModelBase;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraftforge.fml.common.eventhandler.Cancelable;
/*    */ 
/*    */ @Cancelable
/*    */ public class RenderEntityModelEvent
/*    */   extends EventStage {
/*    */   public ModelBase modelBase;
/*    */   public Entity entity;
/*    */   public float limbSwing;
/*    */   public float limbSwingAmount;
/*    */   public float age;
/*    */   public float headYaw;
/*    */   public float headPitch;
/*    */   public float scale;
/*    */   
/*    */   public RenderEntityModelEvent(int stage, ModelBase modelBase, Entity entity, float limbSwing, float limbSwingAmount, float age, float headYaw, float headPitch, float scale) {
/* 21 */     super(stage);
/* 22 */     this.modelBase = modelBase;
/* 23 */     this.entity = entity;
/* 24 */     this.limbSwing = limbSwing;
/* 25 */     this.limbSwingAmount = limbSwingAmount;
/* 26 */     this.age = age;
/* 27 */     this.headYaw = headYaw;
/* 28 */     this.headPitch = headPitch;
/* 29 */     this.scale = scale;
/*    */   }
/*    */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\me\earth\phobos\event\events\RenderEntityModelEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */