/*    */ package me.earth.phobos.features.modules.misc;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.HashMap;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.UUID;
/*    */ import java.util.concurrent.ConcurrentHashMap;
/*    */ import me.earth.phobos.features.modules.Module;
/*    */ import me.earth.phobos.util.PlayerUtil;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.entity.passive.AbstractHorse;
/*    */ import net.minecraft.entity.passive.EntityTameable;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ 
/*    */ public class MobOwner
/*    */   extends Module
/*    */ {
/* 19 */   private final Map<Entity, String> owners = new HashMap<>();
/* 20 */   private final Map<Entity, UUID> toLookUp = new ConcurrentHashMap<>();
/* 21 */   private final List<Entity> lookedUp = new ArrayList<>();
/*    */   
/*    */   public MobOwner() {
/* 24 */     super("MobOwner", "Shows you who owns mobs.", Module.Category.MISC, false, false, false);
/*    */   }
/*    */ 
/*    */   
/*    */   public void onUpdate() {
/* 29 */     if (fullNullCheck()) {
/*    */       return;
/*    */     }
/*    */     
/* 33 */     if (PlayerUtil.timer.passedS(5.0D)) {
/* 34 */       for (Map.Entry<Entity, UUID> entry : this.toLookUp.entrySet()) {
/* 35 */         Entity entity = entry.getKey();
/* 36 */         UUID uuid = entry.getValue();
/* 37 */         if (uuid != null) {
/* 38 */           EntityPlayer owner = mc.field_71441_e.func_152378_a(uuid);
/* 39 */           if (owner != null) {
/* 40 */             this.owners.put(entity, owner.func_70005_c_());
/* 41 */             this.lookedUp.add(entity);
/*    */             continue;
/*    */           } 
/*    */           try {
/* 45 */             String name = PlayerUtil.getNameFromUUID(uuid);
/* 46 */             if (name != null) {
/* 47 */               this.owners.put(entity, name);
/* 48 */               this.lookedUp.add(entity);
/*    */             } 
/* 50 */           } catch (Exception e) {
/* 51 */             this.lookedUp.add(entity);
/* 52 */             this.toLookUp.remove(entry);
/*    */           } 
/*    */           
/* 55 */           PlayerUtil.timer.reset();
/*    */           break;
/*    */         } 
/* 58 */         this.lookedUp.add(entity);
/* 59 */         this.toLookUp.remove(entry);
/*    */       } 
/*    */     }
/*    */ 
/*    */     
/* 64 */     for (Entity entity : mc.field_71441_e.func_72910_y()) {
/* 65 */       if (!entity.func_174833_aM()) {
/* 66 */         if (entity instanceof EntityTameable) {
/* 67 */           EntityTameable tameableEntity = (EntityTameable)entity;
/* 68 */           if (tameableEntity.func_70909_n() && tameableEntity.func_184753_b() != null) {
/* 69 */             if (this.owners.get(tameableEntity) != null) {
/* 70 */               tameableEntity.func_174805_g(true);
/* 71 */               tameableEntity.func_96094_a(this.owners.get(tameableEntity)); continue;
/* 72 */             }  if (!this.lookedUp.contains(entity))
/* 73 */               this.toLookUp.put(tameableEntity, tameableEntity.func_184753_b()); 
/*    */           }  continue;
/*    */         } 
/* 76 */         if (entity instanceof AbstractHorse) {
/* 77 */           AbstractHorse tameableEntity = (AbstractHorse)entity;
/* 78 */           if (tameableEntity.func_110248_bS() && tameableEntity.func_184780_dh() != null) {
/* 79 */             if (this.owners.get(tameableEntity) != null) {
/* 80 */               tameableEntity.func_174805_g(true);
/* 81 */               tameableEntity.func_96094_a(this.owners.get(tameableEntity)); continue;
/* 82 */             }  if (!this.lookedUp.contains(entity)) {
/* 83 */               this.toLookUp.put(tameableEntity, tameableEntity.func_184780_dh());
/*    */             }
/*    */           } 
/*    */         } 
/*    */       } 
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void onDisable() {
/* 93 */     for (Entity entity : mc.field_71441_e.field_72996_f) {
/* 94 */       if (entity instanceof EntityTameable || entity instanceof AbstractHorse)
/*    */         try {
/* 96 */           entity.func_174805_g(false);
/* 97 */         } catch (Exception exception) {} 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\me\earth\phobos\features\modules\misc\MobOwner.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */