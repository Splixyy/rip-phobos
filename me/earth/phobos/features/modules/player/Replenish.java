/*     */ package me.earth.phobos.features.modules.player;
/*     */ 
/*     */ import java.util.Map;
/*     */ import java.util.Queue;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.ConcurrentLinkedQueue;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import me.earth.phobos.features.modules.Module;
/*     */ import me.earth.phobos.features.modules.combat.Auto32k;
/*     */ import me.earth.phobos.features.setting.Setting;
/*     */ import me.earth.phobos.util.InventoryUtil;
/*     */ import me.earth.phobos.util.Timer;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.ItemStack;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Replenish
/*     */   extends Module
/*     */ {
/*  23 */   private final Setting<Integer> threshold = register(new Setting("Threshold", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(63)));
/*  24 */   private final Setting<Integer> replenishments = register(new Setting("RUpdates", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(1000)));
/*  25 */   private final Setting<Integer> updates = register(new Setting("HBUpdates", Integer.valueOf(100), Integer.valueOf(0), Integer.valueOf(1000)));
/*  26 */   private final Setting<Integer> actions = register(new Setting("Actions", Integer.valueOf(2), Integer.valueOf(1), Integer.valueOf(30)));
/*  27 */   private final Setting<Boolean> pauseInv = register(new Setting("PauseInv", Boolean.valueOf(true)));
/*  28 */   private final Setting<Boolean> putBack = register(new Setting("PutBack", Boolean.valueOf(true)));
/*     */   
/*  30 */   private final Timer timer = new Timer();
/*  31 */   private final Timer replenishTimer = new Timer();
/*  32 */   private Map<Integer, ItemStack> hotbar = new ConcurrentHashMap<>();
/*  33 */   private final Queue<InventoryUtil.Task> taskList = new ConcurrentLinkedQueue<>();
/*     */   
/*     */   public Replenish() {
/*  36 */     super("Replenish", "Replenishes your hotbar", Module.Category.PLAYER, false, false, false);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onUpdate() {
/*  41 */     if (Auto32k.getInstance().isOn() && (!((Boolean)(Auto32k.getInstance()).autoSwitch.getValue()).booleanValue() || (Auto32k.getInstance()).switching)) {
/*     */       return;
/*     */     }
/*     */     
/*  45 */     if (mc.field_71462_r instanceof net.minecraft.client.gui.inventory.GuiContainer && (!(mc.field_71462_r instanceof net.minecraft.client.gui.inventory.GuiInventory) || ((Boolean)this.pauseInv.getValue()).booleanValue())) {
/*     */       return;
/*     */     }
/*     */     
/*  49 */     if (this.timer.passedMs(((Integer)this.updates.getValue()).intValue())) {
/*  50 */       mapHotbar();
/*     */     }
/*     */     
/*  53 */     if (this.replenishTimer.passedMs(((Integer)this.replenishments.getValue()).intValue())) {
/*  54 */       for (int i = 0; i < ((Integer)this.actions.getValue()).intValue(); i++) {
/*  55 */         InventoryUtil.Task task = this.taskList.poll();
/*  56 */         if (task != null) {
/*  57 */           task.run();
/*     */         }
/*     */       } 
/*  60 */       this.replenishTimer.reset();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onDisable() {
/*  66 */     this.hotbar.clear();
/*     */   }
/*     */ 
/*     */   
/*     */   public void onLogout() {
/*  71 */     onDisable();
/*     */   }
/*     */   
/*     */   private void mapHotbar() {
/*  75 */     Map<Integer, ItemStack> map = new ConcurrentHashMap<>();
/*  76 */     for (int i = 0; i < 9; i++) {
/*  77 */       ItemStack stack = mc.field_71439_g.field_71071_by.func_70301_a(i);
/*  78 */       map.put(Integer.valueOf(i), stack);
/*     */     } 
/*     */     
/*  81 */     if (this.hotbar.isEmpty()) {
/*  82 */       this.hotbar = map;
/*     */       
/*     */       return;
/*     */     } 
/*  86 */     Map<Integer, Integer> fromTo = new ConcurrentHashMap<>();
/*  87 */     for (Map.Entry<Integer, ItemStack> hotbarItem : map.entrySet()) {
/*  88 */       ItemStack stack = hotbarItem.getValue();
/*  89 */       Integer slotKey = hotbarItem.getKey();
/*  90 */       if (slotKey != null && stack != null && (stack.field_190928_g || stack.func_77973_b() == Items.field_190931_a || (stack.field_77994_a <= ((Integer)this.threshold.getValue()).intValue() && stack.field_77994_a < stack.func_77976_d()))) {
/*  91 */         ItemStack previousStack = hotbarItem.getValue();
/*  92 */         if (stack.field_190928_g || stack.func_77973_b() != Items.field_190931_a) {
/*  93 */           previousStack = this.hotbar.get(slotKey);
/*     */         }
/*  95 */         if (previousStack != null && !previousStack.field_190928_g && previousStack.func_77973_b() != Items.field_190931_a) {
/*  96 */           int replenishSlot = getReplenishSlot(previousStack);
/*  97 */           if (replenishSlot == -1) {
/*     */             continue;
/*     */           }
/* 100 */           fromTo.put(Integer.valueOf(replenishSlot), Integer.valueOf(InventoryUtil.convertHotbarToInv(slotKey.intValue())));
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 105 */     if (!fromTo.isEmpty()) {
/* 106 */       for (Map.Entry<Integer, Integer> slotMove : fromTo.entrySet()) {
/* 107 */         this.taskList.add(new InventoryUtil.Task(((Integer)slotMove.getKey()).intValue()));
/* 108 */         this.taskList.add(new InventoryUtil.Task(((Integer)slotMove.getValue()).intValue()));
/* 109 */         this.taskList.add(new InventoryUtil.Task(((Integer)slotMove.getKey()).intValue()));
/* 110 */         this.taskList.add(new InventoryUtil.Task());
/*     */       } 
/*     */     }
/*     */     
/* 114 */     this.hotbar = map;
/*     */   }
/*     */   
/*     */   private int getReplenishSlot(ItemStack stack) {
/* 118 */     AtomicInteger slot = new AtomicInteger();
/* 119 */     slot.set(-1);
/* 120 */     for (Map.Entry<Integer, ItemStack> entry : (Iterable<Map.Entry<Integer, ItemStack>>)InventoryUtil.getInventoryAndHotbarSlots().entrySet()) {
/* 121 */       if (((Integer)entry.getKey()).intValue() < 36 && 
/* 122 */         InventoryUtil.areStacksCompatible(stack, entry.getValue())) {
/* 123 */         slot.set(((Integer)entry.getKey()).intValue());
/* 124 */         return slot.get();
/*     */       } 
/*     */     } 
/*     */     
/* 128 */     return slot.get();
/*     */   }
/*     */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\me\earth\phobos\features\modules\player\Replenish.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */