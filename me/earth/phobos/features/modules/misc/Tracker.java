/*    */ package me.earth.phobos.features.modules.misc;
/*    */ 
/*    */ import java.util.Objects;
/*    */ import me.earth.phobos.event.events.DeathEvent;
/*    */ import me.earth.phobos.features.command.Command;
/*    */ import me.earth.phobos.features.modules.Module;
/*    */ import me.earth.phobos.features.modules.combat.AutoCrystal;
/*    */ import me.earth.phobos.features.setting.Setting;
/*    */ import me.earth.phobos.util.EntityUtil;
/*    */ import me.earth.phobos.util.TextUtil;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*    */ 
/*    */ public class Tracker extends Module {
/*    */   public Setting<TextUtil.Color> color;
/*    */   private EntityPlayer trackedPlayer;
/*    */   private static Tracker instance;
/*    */   
/*    */   public Tracker() {
/* 21 */     super("Tracker", "Tracks players in 1v1s.", Module.Category.MISC, true, false, false);
/*    */ 
/*    */ 
/*    */     
/* 25 */     this.color = register(new Setting("Color", TextUtil.Color.RED));
/*    */ 
/*    */ 
/*    */     
/* 29 */     this.usedExp = 0;
/* 30 */     this.usedStacks = 0;
/* 31 */     this.usedCrystals = 0;
/* 32 */     this.usedCStacks = 0;
/*    */     instance = this;
/*    */   } private int usedExp; private int usedStacks; private int usedCrystals; private int usedCStacks;
/*    */   public void onUpdate() {
/* 36 */     if (this.trackedPlayer == null) {
/* 37 */       this.trackedPlayer = EntityUtil.getClosestEnemy(1000.0D);
/*    */     } else {
/* 39 */       if (this.usedStacks != this.usedExp / 64) {
/* 40 */         this.usedStacks = this.usedExp / 64;
/* 41 */         Command.sendMessage(TextUtil.coloredString(this.trackedPlayer.func_70005_c_() + " used: " + this.usedStacks + " Stacks of EXP.", (TextUtil.Color)this.color.getValue()));
/*    */       } 
/* 43 */       if (this.usedCStacks != this.usedCrystals / 64) {
/* 44 */         this.usedCStacks = this.usedCrystals / 64;
/* 45 */         Command.sendMessage(TextUtil.coloredString(this.trackedPlayer.func_70005_c_() + " used: " + this.usedCStacks + " Stacks of Crystals.", (TextUtil.Color)this.color.getValue()));
/*    */       } 
/*    */     } 
/*    */   }
/*    */   
/*    */   public void onSpawnEntity(Entity entity) {
/* 51 */     if (entity instanceof net.minecraft.entity.item.EntityExpBottle && 
/* 52 */       Objects.equals(mc.field_71441_e.func_72890_a(entity, 3.0D), this.trackedPlayer)) {
/* 53 */       this.usedExp++;
/*    */     }
/*    */ 
/*    */     
/* 57 */     if (entity instanceof net.minecraft.entity.item.EntityEnderCrystal) {
/* 58 */       if (AutoCrystal.placedPos.contains(entity.func_180425_c())) {
/* 59 */         AutoCrystal.placedPos.remove(entity.func_180425_c());
/*    */       } else {
/* 61 */         this.usedCrystals++;
/*    */       } 
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public void onDisable() {
/* 68 */     this.trackedPlayer = null;
/* 69 */     this.usedExp = 0;
/* 70 */     this.usedStacks = 0;
/* 71 */     this.usedCrystals = 0;
/* 72 */     this.usedCStacks = 0;
/*    */   }
/*    */   
/*    */   @SubscribeEvent
/*    */   public void onDeath(DeathEvent event) {
/* 77 */     if (event.player.equals(this.trackedPlayer)) {
/* 78 */       this.usedExp = 0;
/* 79 */       this.usedStacks = 0;
/* 80 */       this.usedCrystals = 0;
/* 81 */       this.usedCStacks = 0;
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public String getDisplayInfo() {
/* 87 */     if (this.trackedPlayer != null) {
/* 88 */       return this.trackedPlayer.func_70005_c_();
/*    */     }
/* 90 */     return null;
/*    */   }
/*    */   
/*    */   public static Tracker getInstance() {
/* 94 */     if (instance == null) {
/* 95 */       instance = new Tracker();
/*    */     }
/* 97 */     return instance;
/*    */   }
/*    */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\me\earth\phobos\features\modules\misc\Tracker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */