/*     */ package me.earth.phobos.manager;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import me.earth.phobos.features.Feature;
/*     */ import me.earth.phobos.features.modules.client.HUD;
/*     */ import me.earth.phobos.features.modules.client.Managers;
/*     */ import net.minecraft.client.resources.I18n;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.potion.Potion;
/*     */ import net.minecraft.potion.PotionEffect;
/*     */ 
/*     */ 
/*     */ public class PotionManager
/*     */   extends Feature
/*     */ {
/*  19 */   private final Map<EntityPlayer, PotionList> potions = new ConcurrentHashMap<>();
/*     */   
/*     */   public void onLogout() {
/*  22 */     this.potions.clear();
/*     */   }
/*     */   
/*     */   public void updatePlayer() {
/*  26 */     PotionList list = new PotionList();
/*  27 */     for (PotionEffect effect : mc.field_71439_g.func_70651_bq()) {
/*  28 */       list.addEffect(effect);
/*     */     }
/*  30 */     this.potions.put(mc.field_71439_g, list);
/*     */   }
/*     */   
/*     */   public void update() {
/*  34 */     updatePlayer();
/*  35 */     if (HUD.getInstance().isOn() && ((Boolean)(HUD.getInstance()).textRadar.getValue()).booleanValue() && ((Boolean)(Managers.getInstance()).potions.getValue()).booleanValue()) {
/*  36 */       ArrayList<EntityPlayer> removeList = new ArrayList<>();
/*  37 */       for (Map.Entry<EntityPlayer, PotionList> potionEntry : this.potions.entrySet()) {
/*  38 */         boolean notFound = true;
/*  39 */         for (EntityPlayer player : mc.field_71441_e.field_73010_i) {
/*  40 */           if (this.potions.get(player) == null) {
/*  41 */             PotionList list = new PotionList();
/*  42 */             for (PotionEffect effect : player.func_70651_bq()) {
/*  43 */               list.addEffect(effect);
/*     */             }
/*  45 */             this.potions.put(player, list);
/*  46 */             notFound = false;
/*     */           } 
/*     */           
/*  49 */           if (((EntityPlayer)potionEntry.getKey()).equals(player)) {
/*  50 */             notFound = false;
/*     */           }
/*     */         } 
/*     */         
/*  54 */         if (notFound) {
/*  55 */           removeList.add(potionEntry.getKey());
/*     */         }
/*     */       } 
/*     */       
/*  59 */       for (EntityPlayer player : removeList) {
/*  60 */         this.potions.remove(player);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public List<PotionEffect> getOwnPotions() {
/*  66 */     return getPlayerPotions((EntityPlayer)mc.field_71439_g);
/*     */   }
/*     */   
/*     */   public List<PotionEffect> getPlayerPotions(EntityPlayer player) {
/*  70 */     PotionList list = this.potions.get(player);
/*  71 */     List<PotionEffect> potions = new ArrayList<>();
/*  72 */     if (list != null) {
/*  73 */       potions = list.getEffects();
/*     */     }
/*  75 */     return potions;
/*     */   }
/*     */   
/*     */   public void onTotemPop(EntityPlayer player) {
/*  79 */     PotionList list = new PotionList();
/*  80 */     this.potions.put(player, list);
/*     */   }
/*     */   
/*     */   public PotionEffect[] getImportantPotions(EntityPlayer player) {
/*  84 */     PotionEffect[] array = new PotionEffect[3];
/*  85 */     for (PotionEffect effect : getPlayerPotions(player)) {
/*  86 */       Potion potion = effect.func_188419_a();
/*  87 */       switch (I18n.func_135052_a(potion.func_76393_a(), new Object[0]).toLowerCase()) {
/*     */         case "strength":
/*  89 */           array[0] = effect;
/*     */         
/*     */         case "weakness":
/*  92 */           array[1] = effect;
/*     */         
/*     */         case "speed":
/*  95 */           array[2] = effect;
/*     */       } 
/*     */ 
/*     */     
/*     */     } 
/* 100 */     return array;
/*     */   }
/*     */   
/*     */   public String getPotionString(PotionEffect effect) {
/* 104 */     Potion potion = effect.func_188419_a();
/* 105 */     return I18n.func_135052_a(potion.func_76393_a(), new Object[0]) + " " + (effect.func_76458_c() + 1) + " " + "§f" + Potion.func_188410_a(effect, 1.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getColoredPotionString(PotionEffect effect) {
/* 110 */     Potion potion = effect.func_188419_a();
/* 111 */     switch (I18n.func_135052_a(potion.func_76393_a(), new Object[0])) {
/*     */       case "Jump Boost":
/*     */       case "Speed":
/* 114 */         return "§b" + getPotionString(effect);
/*     */       case "Resistance":
/*     */       case "Strength":
/* 117 */         return "§c" + getPotionString(effect);
/*     */       case "Wither":
/*     */       case "Slowness":
/*     */       case "Weakness":
/* 121 */         return "§0" + getPotionString(effect);
/*     */       case "Absorption":
/* 123 */         return "§9" + getPotionString(effect);
/*     */       case "Haste":
/*     */       case "Fire Resistance":
/* 126 */         return "§6" + getPotionString(effect);
/*     */       case "Regeneration":
/* 128 */         return "§d" + getPotionString(effect);
/*     */       case "Night Vision":
/*     */       case "Poison":
/* 131 */         return "§a" + getPotionString(effect);
/*     */     } 
/* 133 */     return "§f" + getPotionString(effect);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getTextRadarPotionWithDuration(EntityPlayer player) {
/* 138 */     PotionEffect[] array = getImportantPotions(player);
/* 139 */     PotionEffect strength = array[0];
/* 140 */     PotionEffect weakness = array[1];
/* 141 */     PotionEffect speed = array[2];
/* 142 */     return "" + ((strength != null) ? ("§c S" + (strength.func_76458_c() + 1) + " " + Potion.func_188410_a(strength, 1.0F)) : "") + ((weakness != null) ? ("§8 W " + 
/* 143 */       Potion.func_188410_a(weakness, 1.0F)) : "") + ((speed != null) ? ("§b S" + (speed
/* 144 */       .func_76458_c() + 1) + " " + Potion.func_188410_a(weakness, 1.0F)) : "");
/*     */   }
/*     */   
/*     */   public String getTextRadarPotion(EntityPlayer player) {
/* 148 */     PotionEffect[] array = getImportantPotions(player);
/* 149 */     PotionEffect strength = array[0];
/* 150 */     PotionEffect weakness = array[1];
/* 151 */     PotionEffect speed = array[2];
/* 152 */     return "" + ((strength != null) ? ("§c S" + (strength.func_76458_c() + 1) + " ") : "") + ((weakness != null) ? "§8 W " : "") + ((speed != null) ? ("§b S" + (speed
/*     */       
/* 154 */       .func_76458_c() + 1) + " ") : "");
/*     */   }
/*     */   
/*     */   public static class PotionList {
/* 158 */     private List<PotionEffect> effects = new ArrayList<>();
/*     */     
/*     */     public void addEffect(PotionEffect effect) {
/* 161 */       if (effect != null) {
/* 162 */         this.effects.add(effect);
/*     */       }
/*     */     }
/*     */     
/*     */     public List<PotionEffect> getEffects() {
/* 167 */       return this.effects;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\me\earth\phobos\manager\PotionManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */