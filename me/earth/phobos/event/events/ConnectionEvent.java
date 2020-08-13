/*    */ package me.earth.phobos.event.events;
/*    */ 
/*    */ import java.util.UUID;
/*    */ import me.earth.phobos.event.EventStage;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ 
/*    */ public class ConnectionEvent
/*    */   extends EventStage
/*    */ {
/*    */   private final UUID uuid;
/*    */   private final EntityPlayer entity;
/*    */   private final String name;
/*    */   
/*    */   public ConnectionEvent(int stage, UUID uuid, String name) {
/* 15 */     super(stage);
/* 16 */     this.uuid = uuid;
/* 17 */     this.name = name;
/* 18 */     this.entity = null;
/*    */   }
/*    */   
/*    */   public ConnectionEvent(int stage, EntityPlayer entity, UUID uuid, String name) {
/* 22 */     super(stage);
/* 23 */     this.entity = entity;
/* 24 */     this.uuid = uuid;
/* 25 */     this.name = name;
/*    */   }
/*    */   
/*    */   public String getName() {
/* 29 */     return this.name;
/*    */   }
/*    */   
/*    */   public UUID getUuid() {
/* 33 */     return this.uuid;
/*    */   }
/*    */   
/*    */   public EntityPlayer getEntity() {
/* 37 */     return this.entity;
/*    */   }
/*    */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\me\earth\phobos\event\events\ConnectionEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */