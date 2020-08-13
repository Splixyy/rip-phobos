/*     */ package me.earth.phobos.features.modules.combat;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Queue;
/*     */ import java.util.concurrent.ConcurrentLinkedQueue;
/*     */ import me.earth.phobos.Phobos;
/*     */ import me.earth.phobos.features.modules.Module;
/*     */ import me.earth.phobos.features.modules.player.XCarry;
/*     */ import me.earth.phobos.features.setting.Bind;
/*     */ import me.earth.phobos.features.setting.Setting;
/*     */ import me.earth.phobos.util.DamageUtil;
/*     */ import me.earth.phobos.util.EntityUtil;
/*     */ import me.earth.phobos.util.InventoryUtil;
/*     */ import me.earth.phobos.util.MathUtil;
/*     */ import me.earth.phobos.util.Timer;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.inventory.EntityEquipmentSlot;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemExpBottle;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*     */ import net.minecraftforge.fml.common.gameevent.InputEvent;
/*     */ import org.lwjgl.input.Keyboard;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AutoArmor
/*     */   extends Module
/*     */ {
/*  34 */   private final Setting<Integer> delay = register(new Setting("Delay", Integer.valueOf(50), Integer.valueOf(0), Integer.valueOf(500)));
/*  35 */   private final Setting<Boolean> mendingTakeOff = register(new Setting("AutoMend", Boolean.valueOf(false)));
/*  36 */   private final Setting<Integer> closestEnemy = register(new Setting("Enemy", Integer.valueOf(8), Integer.valueOf(1), Integer.valueOf(20), v -> ((Boolean)this.mendingTakeOff.getValue()).booleanValue()));
/*  37 */   private final Setting<Integer> helmetThreshold = register(new Setting("Helmet%", Integer.valueOf(80), Integer.valueOf(1), Integer.valueOf(100), v -> ((Boolean)this.mendingTakeOff.getValue()).booleanValue()));
/*  38 */   private final Setting<Integer> chestThreshold = register(new Setting("Chest%", Integer.valueOf(80), Integer.valueOf(1), Integer.valueOf(100), v -> ((Boolean)this.mendingTakeOff.getValue()).booleanValue()));
/*  39 */   private final Setting<Integer> legThreshold = register(new Setting("Legs%", Integer.valueOf(80), Integer.valueOf(1), Integer.valueOf(100), v -> ((Boolean)this.mendingTakeOff.getValue()).booleanValue()));
/*  40 */   private final Setting<Integer> bootsThreshold = register(new Setting("Boots%", Integer.valueOf(80), Integer.valueOf(1), Integer.valueOf(100), v -> ((Boolean)this.mendingTakeOff.getValue()).booleanValue()));
/*  41 */   private final Setting<Boolean> curse = register(new Setting("CurseOfBinding", Boolean.valueOf(false)));
/*  42 */   private final Setting<Integer> actions = register(new Setting("Actions", Integer.valueOf(3), Integer.valueOf(1), Integer.valueOf(12)));
/*  43 */   private final Setting<Bind> elytraBind = register(new Setting("Elytra", new Bind(-1)));
/*  44 */   private final Setting<Boolean> tps = register(new Setting("TpsSync", Boolean.valueOf(true)));
/*  45 */   private final Setting<Boolean> updateController = register(new Setting("Update", Boolean.valueOf(true)));
/*  46 */   private final Setting<Boolean> shiftClick = register(new Setting("ShiftClick", Boolean.valueOf(false)));
/*     */   
/*  48 */   private final Timer timer = new Timer();
/*  49 */   private final Timer elytraTimer = new Timer();
/*  50 */   private final Queue<InventoryUtil.Task> taskList = new ConcurrentLinkedQueue<>();
/*  51 */   private final List<Integer> doneSlots = new ArrayList<>();
/*     */   private boolean elytraOn = false;
/*     */   
/*     */   public AutoArmor() {
/*  55 */     super("AutoArmor", "Puts Armor on for you.", Module.Category.COMBAT, true, false, false);
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onKeyInput(InputEvent.KeyInputEvent event) {
/*  60 */     if (Keyboard.getEventKeyState() && !(mc.field_71462_r instanceof me.earth.phobos.features.gui.PhobosGui) && ((Bind)this.elytraBind.getValue()).getKey() == Keyboard.getEventKey()) {
/*  61 */       this.elytraOn = !this.elytraOn;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void onLogin() {
/*  67 */     this.timer.reset();
/*  68 */     this.elytraTimer.reset();
/*     */   }
/*     */ 
/*     */   
/*     */   public void onDisable() {
/*  73 */     this.taskList.clear();
/*  74 */     this.doneSlots.clear();
/*  75 */     this.elytraOn = false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onLogout() {
/*  80 */     this.taskList.clear();
/*  81 */     this.doneSlots.clear();
/*     */   }
/*     */ 
/*     */   
/*     */   public void onTick() {
/*  86 */     if (fullNullCheck() || (mc.field_71462_r instanceof net.minecraft.client.gui.inventory.GuiContainer && !(mc.field_71462_r instanceof net.minecraft.client.gui.inventory.GuiInventory))) {
/*     */       return;
/*     */     }
/*     */     
/*  90 */     if (this.taskList.isEmpty()) {
/*  91 */       if (((Boolean)this.mendingTakeOff.getValue()).booleanValue() && InventoryUtil.holdingItem(ItemExpBottle.class) && mc.field_71474_y.field_74313_G.func_151470_d() && (isSafe() || EntityUtil.isSafe((Entity)mc.field_71439_g, 1, false))) {
/*  92 */         ItemStack itemStack1 = mc.field_71439_g.field_71069_bz.func_75139_a(5).func_75211_c();
/*  93 */         if (!itemStack1.field_190928_g) {
/*  94 */           int helmDamage = DamageUtil.getRoundedDamage(itemStack1);
/*  95 */           if (helmDamage >= ((Integer)this.helmetThreshold.getValue()).intValue()) {
/*  96 */             takeOffSlot(5);
/*     */           }
/*     */         } 
/*     */         
/* 100 */         ItemStack itemStack2 = mc.field_71439_g.field_71069_bz.func_75139_a(6).func_75211_c();
/* 101 */         if (!itemStack2.field_190928_g) {
/* 102 */           int chestDamage = DamageUtil.getRoundedDamage(itemStack2);
/* 103 */           if (chestDamage >= ((Integer)this.chestThreshold.getValue()).intValue()) {
/* 104 */             takeOffSlot(6);
/*     */           }
/*     */         } 
/*     */         
/* 108 */         ItemStack itemStack3 = mc.field_71439_g.field_71069_bz.func_75139_a(7).func_75211_c();
/* 109 */         if (!itemStack3.field_190928_g) {
/* 110 */           int leggingDamage = DamageUtil.getRoundedDamage(itemStack3);
/* 111 */           if (leggingDamage >= ((Integer)this.legThreshold.getValue()).intValue()) {
/* 112 */             takeOffSlot(7);
/*     */           }
/*     */         } 
/*     */         
/* 116 */         ItemStack itemStack4 = mc.field_71439_g.field_71069_bz.func_75139_a(8).func_75211_c();
/* 117 */         if (!itemStack4.field_190928_g) {
/* 118 */           int bootDamage = DamageUtil.getRoundedDamage(itemStack4);
/* 119 */           if (bootDamage >= ((Integer)this.bootsThreshold.getValue()).intValue()) {
/* 120 */             takeOffSlot(8);
/*     */           }
/*     */         } 
/*     */         
/*     */         return;
/*     */       } 
/* 126 */       ItemStack helm = mc.field_71439_g.field_71069_bz.func_75139_a(5).func_75211_c();
/* 127 */       if (helm.func_77973_b() == Items.field_190931_a) {
/* 128 */         int slot = InventoryUtil.findArmorSlot(EntityEquipmentSlot.HEAD, ((Boolean)this.curse.getValue()).booleanValue(), XCarry.getInstance().isOn());
/* 129 */         if (slot != -1) {
/* 130 */           getSlotOn(5, slot);
/*     */         }
/*     */       } 
/*     */       
/* 134 */       ItemStack chest = mc.field_71439_g.field_71069_bz.func_75139_a(6).func_75211_c();
/* 135 */       if (chest.func_77973_b() == Items.field_190931_a) {
/* 136 */         if (this.taskList.isEmpty()) {
/* 137 */           if (this.elytraOn && this.elytraTimer.passedMs(500L)) {
/* 138 */             int elytraSlot = InventoryUtil.findItemInventorySlot(Items.field_185160_cR, false, XCarry.getInstance().isOn());
/* 139 */             if (elytraSlot != -1) {
/* 140 */               if ((elytraSlot < 5 && elytraSlot > 1) || !((Boolean)this.shiftClick.getValue()).booleanValue()) {
/* 141 */                 this.taskList.add(new InventoryUtil.Task(elytraSlot));
/* 142 */                 this.taskList.add(new InventoryUtil.Task(6));
/*     */               } else {
/* 144 */                 this.taskList.add(new InventoryUtil.Task(elytraSlot, true));
/*     */               } 
/*     */               
/* 147 */               if (((Boolean)this.updateController.getValue()).booleanValue()) {
/* 148 */                 this.taskList.add(new InventoryUtil.Task());
/*     */               }
/* 150 */               this.elytraTimer.reset();
/*     */             } 
/* 152 */           } else if (!this.elytraOn) {
/* 153 */             int slot = InventoryUtil.findArmorSlot(EntityEquipmentSlot.CHEST, ((Boolean)this.curse.getValue()).booleanValue(), XCarry.getInstance().isOn());
/* 154 */             if (slot != -1) {
/* 155 */               getSlotOn(6, slot);
/*     */             }
/*     */           }
/*     */         
/*     */         }
/* 160 */       } else if (this.elytraOn && chest.func_77973_b() != Items.field_185160_cR && this.elytraTimer.passedMs(500L)) {
/* 161 */         if (this.taskList.isEmpty()) {
/* 162 */           int slot = InventoryUtil.findItemInventorySlot(Items.field_185160_cR, false, XCarry.getInstance().isOn());
/* 163 */           if (slot != -1) {
/* 164 */             this.taskList.add(new InventoryUtil.Task(slot));
/* 165 */             this.taskList.add(new InventoryUtil.Task(6));
/* 166 */             this.taskList.add(new InventoryUtil.Task(slot));
/* 167 */             if (((Boolean)this.updateController.getValue()).booleanValue()) {
/* 168 */               this.taskList.add(new InventoryUtil.Task());
/*     */             }
/*     */           } 
/* 171 */           this.elytraTimer.reset();
/*     */         } 
/* 173 */       } else if (!this.elytraOn && chest.func_77973_b() == Items.field_185160_cR && this.elytraTimer.passedMs(500L) && this.taskList.isEmpty()) {
/*     */         
/* 175 */         int slot = InventoryUtil.findItemInventorySlot((Item)Items.field_151163_ad, false, XCarry.getInstance().isOn());
/* 176 */         if (slot == -1) {
/* 177 */           slot = InventoryUtil.findItemInventorySlot((Item)Items.field_151030_Z, false, XCarry.getInstance().isOn());
/* 178 */           if (slot == -1) {
/* 179 */             slot = InventoryUtil.findItemInventorySlot((Item)Items.field_151171_ah, false, XCarry.getInstance().isOn());
/* 180 */             if (slot == -1) {
/* 181 */               slot = InventoryUtil.findItemInventorySlot((Item)Items.field_151023_V, false, XCarry.getInstance().isOn());
/* 182 */               if (slot == -1) {
/* 183 */                 slot = InventoryUtil.findItemInventorySlot((Item)Items.field_151027_R, false, XCarry.getInstance().isOn());
/*     */               }
/*     */             } 
/*     */           } 
/*     */         } 
/*     */         
/* 189 */         if (slot != -1) {
/* 190 */           this.taskList.add(new InventoryUtil.Task(slot));
/* 191 */           this.taskList.add(new InventoryUtil.Task(6));
/* 192 */           this.taskList.add(new InventoryUtil.Task(slot));
/* 193 */           if (((Boolean)this.updateController.getValue()).booleanValue()) {
/* 194 */             this.taskList.add(new InventoryUtil.Task());
/*     */           }
/*     */         } 
/* 197 */         this.elytraTimer.reset();
/*     */       } 
/*     */ 
/*     */       
/* 201 */       ItemStack legging = mc.field_71439_g.field_71069_bz.func_75139_a(7).func_75211_c();
/* 202 */       if (legging.func_77973_b() == Items.field_190931_a) {
/* 203 */         int slot = InventoryUtil.findArmorSlot(EntityEquipmentSlot.LEGS, ((Boolean)this.curse.getValue()).booleanValue(), XCarry.getInstance().isOn());
/* 204 */         if (slot != -1) {
/* 205 */           getSlotOn(7, slot);
/*     */         }
/*     */       } 
/*     */       
/* 209 */       ItemStack feet = mc.field_71439_g.field_71069_bz.func_75139_a(8).func_75211_c();
/* 210 */       if (feet.func_77973_b() == Items.field_190931_a) {
/* 211 */         int slot = InventoryUtil.findArmorSlot(EntityEquipmentSlot.FEET, ((Boolean)this.curse.getValue()).booleanValue(), XCarry.getInstance().isOn());
/* 212 */         if (slot != -1) {
/* 213 */           getSlotOn(8, slot);
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 218 */     if (this.timer.passedMs((int)(((Integer)this.delay.getValue()).intValue() * (((Boolean)this.tps.getValue()).booleanValue() ? Phobos.serverManager.getTpsFactor() : 1.0F)))) {
/* 219 */       if (!this.taskList.isEmpty()) {
/* 220 */         for (int i = 0; i < ((Integer)this.actions.getValue()).intValue(); i++) {
/* 221 */           InventoryUtil.Task task = this.taskList.poll();
/* 222 */           if (task != null) {
/* 223 */             task.run();
/*     */           }
/*     */         } 
/*     */       }
/* 227 */       this.timer.reset();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public String getDisplayInfo() {
/* 233 */     if (this.elytraOn) {
/* 234 */       return "Elytra";
/*     */     }
/* 236 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   private void takeOffSlot(int slot) {
/* 241 */     if (this.taskList.isEmpty()) {
/* 242 */       int target = -1;
/* 243 */       for (Iterator<Integer> iterator = InventoryUtil.findEmptySlots(XCarry.getInstance().isOn()).iterator(); iterator.hasNext(); ) { int i = ((Integer)iterator.next()).intValue();
/* 244 */         if (!this.doneSlots.contains(Integer.valueOf(target))) {
/* 245 */           target = i;
/* 246 */           this.doneSlots.add(Integer.valueOf(i));
/*     */         }  }
/*     */ 
/*     */       
/* 250 */       if (target != -1) {
/* 251 */         if ((target < 5 && target > 0) || !((Boolean)this.shiftClick.getValue()).booleanValue()) {
/* 252 */           this.taskList.add(new InventoryUtil.Task(slot));
/* 253 */           this.taskList.add(new InventoryUtil.Task(target));
/*     */         } else {
/* 255 */           this.taskList.add(new InventoryUtil.Task(slot, true));
/*     */         } 
/* 257 */         if (((Boolean)this.updateController.getValue()).booleanValue()) {
/* 258 */           this.taskList.add(new InventoryUtil.Task());
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void getSlotOn(int slot, int target) {
/* 265 */     if (this.taskList.isEmpty()) {
/* 266 */       this.doneSlots.remove(Integer.valueOf(target));
/* 267 */       if ((target < 5 && target > 0) || !((Boolean)this.shiftClick.getValue()).booleanValue()) {
/* 268 */         this.taskList.add(new InventoryUtil.Task(target));
/* 269 */         this.taskList.add(new InventoryUtil.Task(slot));
/*     */       } else {
/* 271 */         this.taskList.add(new InventoryUtil.Task(target, true));
/*     */       } 
/* 273 */       if (((Boolean)this.updateController.getValue()).booleanValue()) {
/* 274 */         this.taskList.add(new InventoryUtil.Task());
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean isSafe() {
/* 315 */     EntityPlayer closest = EntityUtil.getClosestEnemy(((Integer)this.closestEnemy.getValue()).intValue());
/* 316 */     if (closest == null) {
/* 317 */       return true;
/*     */     }
/* 319 */     return (mc.field_71439_g.func_70068_e((Entity)closest) >= MathUtil.square(((Integer)this.closestEnemy.getValue()).intValue()));
/*     */   }
/*     */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\me\earth\phobos\features\modules\combat\AutoArmor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */