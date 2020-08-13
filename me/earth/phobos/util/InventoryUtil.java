/*     */ package me.earth.phobos.util;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import me.earth.phobos.Phobos;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.enchantment.EnchantmentHelper;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.init.Enchantments;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.inventory.ClickType;
/*     */ import net.minecraft.inventory.EntityEquipmentSlot;
/*     */ import net.minecraft.inventory.Slot;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemArmor;
/*     */ import net.minecraft.item.ItemBlock;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.network.Packet;
/*     */ import net.minecraft.network.play.client.CPacketHeldItemChange;
/*     */ 
/*     */ public class InventoryUtil
/*     */   implements Util {
/*     */   public static void switchToHotbarSlot(int slot, boolean silent) {
/*  28 */     if (mc.field_71439_g.field_71071_by.field_70461_c == slot || slot < 0) {
/*     */       return;
/*     */     }
/*     */     
/*  32 */     if (silent) {
/*  33 */       mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketHeldItemChange(slot));
/*  34 */       mc.field_71442_b.func_78765_e();
/*     */     } else {
/*  36 */       mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketHeldItemChange(slot));
/*  37 */       mc.field_71439_g.field_71071_by.field_70461_c = slot;
/*  38 */       mc.field_71442_b.func_78765_e();
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void switchToHotbarSlot(Class clazz, boolean silent) {
/*  43 */     int slot = findHotbarBlock(clazz);
/*  44 */     if (slot > -1) {
/*  45 */       switchToHotbarSlot(slot, silent);
/*     */     }
/*     */   }
/*     */   
/*     */   public static boolean isNull(ItemStack stack) {
/*  50 */     return (stack == null || stack.func_77973_b() instanceof net.minecraft.item.ItemAir);
/*     */   }
/*     */   
/*     */   public static int findHotbarBlock(Class clazz) {
/*  54 */     for (int i = 0; i < 9; i++) {
/*  55 */       ItemStack stack = mc.field_71439_g.field_71071_by.func_70301_a(i);
/*  56 */       if (stack != ItemStack.field_190927_a) {
/*     */ 
/*     */ 
/*     */         
/*  60 */         if (clazz.isInstance(stack.func_77973_b())) {
/*  61 */           return i;
/*     */         }
/*     */         
/*  64 */         if (stack.func_77973_b() instanceof ItemBlock) {
/*  65 */           Block block = ((ItemBlock)stack.func_77973_b()).func_179223_d();
/*  66 */           if (clazz.isInstance(block))
/*  67 */             return i; 
/*     */         } 
/*     */       } 
/*     */     } 
/*  71 */     return -1;
/*     */   }
/*     */   
/*     */   public static int findHotbarBlock(Block blockIn) {
/*  75 */     for (int i = 0; i < 9; i++) {
/*  76 */       ItemStack stack = mc.field_71439_g.field_71071_by.func_70301_a(i);
/*  77 */       if (stack != ItemStack.field_190927_a)
/*     */       {
/*     */ 
/*     */         
/*  81 */         if (stack.func_77973_b() instanceof ItemBlock) {
/*  82 */           Block block = ((ItemBlock)stack.func_77973_b()).func_179223_d();
/*  83 */           if (block == blockIn)
/*  84 */             return i; 
/*     */         } 
/*     */       }
/*     */     } 
/*  88 */     return -1;
/*     */   }
/*     */   
/*     */   public static int getItemHotbar(Item input) {
/*  92 */     for (int i = 0; i < 9; i++) {
/*  93 */       Item item = mc.field_71439_g.field_71071_by.func_70301_a(i).func_77973_b();
/*  94 */       if (Item.func_150891_b(item) == Item.func_150891_b(input)) {
/*  95 */         return i;
/*     */       }
/*     */     } 
/*  98 */     return -1;
/*     */   }
/*     */   
/*     */   public static int findStackInventory(Item input) {
/* 102 */     return findStackInventory(input, false);
/*     */   }
/*     */   
/*     */   public static int findStackInventory(Item input, boolean withHotbar) {
/* 106 */     for (int i = withHotbar ? 0 : 9; i < 36; i++) {
/* 107 */       Item item = mc.field_71439_g.field_71071_by.func_70301_a(i).func_77973_b();
/* 108 */       if (Item.func_150891_b(input) == Item.func_150891_b(item)) {
/* 109 */         return i + ((i < 9) ? 36 : 0);
/*     */       }
/*     */     } 
/* 112 */     return -1;
/*     */   }
/*     */   
/*     */   public static int findItemInventorySlot(Item item, boolean offHand) {
/* 116 */     AtomicInteger slot = new AtomicInteger();
/* 117 */     slot.set(-1);
/* 118 */     for (Map.Entry<Integer, ItemStack> entry : getInventoryAndHotbarSlots().entrySet()) {
/* 119 */       if (((ItemStack)entry.getValue()).func_77973_b() != item || ((
/* 120 */         (Integer)entry.getKey()).intValue() == 45 && !offHand)) {
/*     */         continue;
/*     */       }
/* 123 */       slot.set(((Integer)entry.getKey()).intValue());
/* 124 */       return slot.get();
/*     */     } 
/*     */     
/* 127 */     return slot.get();
/*     */   }
/*     */   
/*     */   public static List<Integer> findEmptySlots(boolean withXCarry) {
/* 131 */     List<Integer> outPut = new ArrayList<>();
/* 132 */     for (Map.Entry<Integer, ItemStack> entry : getInventoryAndHotbarSlots().entrySet()) {
/* 133 */       if (((ItemStack)entry.getValue()).field_190928_g || ((ItemStack)entry.getValue()).func_77973_b() == Items.field_190931_a) {
/* 134 */         outPut.add(entry.getKey());
/*     */       }
/*     */     } 
/*     */     
/* 138 */     if (withXCarry) {
/* 139 */       for (int i = 1; i < 5; i++) {
/* 140 */         Slot craftingSlot = mc.field_71439_g.field_71069_bz.field_75151_b.get(i);
/* 141 */         ItemStack craftingStack = craftingSlot.func_75211_c();
/* 142 */         if (craftingStack.func_190926_b() || craftingStack.func_77973_b() == Items.field_190931_a) {
/* 143 */           outPut.add(Integer.valueOf(i));
/*     */         }
/*     */       } 
/*     */     }
/* 147 */     return outPut;
/*     */   }
/*     */   
/*     */   public static int findInventoryBlock(Class clazz, boolean offHand) {
/* 151 */     AtomicInteger slot = new AtomicInteger();
/* 152 */     slot.set(-1);
/* 153 */     for (Map.Entry<Integer, ItemStack> entry : getInventoryAndHotbarSlots().entrySet()) {
/* 154 */       if (!isBlock(((ItemStack)entry.getValue()).func_77973_b(), clazz) || ((
/* 155 */         (Integer)entry.getKey()).intValue() == 45 && !offHand)) {
/*     */         continue;
/*     */       }
/* 158 */       slot.set(((Integer)entry.getKey()).intValue());
/* 159 */       return slot.get();
/*     */     } 
/*     */     
/* 162 */     return slot.get();
/*     */   }
/*     */   
/*     */   public static boolean isBlock(Item item, Class clazz) {
/* 166 */     if (item instanceof ItemBlock) {
/* 167 */       Block block = ((ItemBlock)item).func_179223_d();
/* 168 */       return clazz.isInstance(block);
/*     */     } 
/* 170 */     return false;
/*     */   }
/*     */   
/*     */   public static void confirmSlot(int slot) {
/* 174 */     mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketHeldItemChange(slot));
/* 175 */     mc.field_71439_g.field_71071_by.field_70461_c = slot;
/* 176 */     mc.field_71442_b.func_78765_e();
/*     */   }
/*     */   
/*     */   public static Map<Integer, ItemStack> getInventoryAndHotbarSlots() {
/* 180 */     return getInventorySlots(9, 44);
/*     */   }
/*     */   
/*     */   private static Map<Integer, ItemStack> getInventorySlots(int currentI, int last) {
/* 184 */     int current = currentI;
/* 185 */     Map<Integer, ItemStack> fullInventorySlots = new HashMap<>();
/* 186 */     while (current <= last) {
/* 187 */       fullInventorySlots.put(Integer.valueOf(current), mc.field_71439_g.field_71069_bz.func_75138_a().get(current));
/* 188 */       current++;
/*     */     } 
/* 190 */     return fullInventorySlots;
/*     */   }
/*     */   
/*     */   public static boolean[] switchItem(boolean back, int lastHotbarSlot, boolean switchedItem, Switch mode, Class clazz) {
/* 194 */     boolean[] switchedItemSwitched = { switchedItem, false };
/* 195 */     switch (mode) {
/*     */       case NORMAL:
/* 197 */         if (!back && !switchedItem) {
/* 198 */           switchToHotbarSlot(findHotbarBlock(clazz), false);
/* 199 */           switchedItemSwitched[0] = true;
/* 200 */         } else if (back && switchedItem) {
/* 201 */           switchToHotbarSlot(lastHotbarSlot, false);
/* 202 */           switchedItemSwitched[0] = false;
/*     */         } 
/* 204 */         switchedItemSwitched[1] = true;
/*     */         break;
/*     */       case SILENT:
/* 207 */         if (!back && !switchedItem) {
/* 208 */           switchToHotbarSlot(findHotbarBlock(clazz), true);
/* 209 */           switchedItemSwitched[0] = true;
/* 210 */         } else if (back && switchedItem) {
/* 211 */           switchedItemSwitched[0] = false;
/* 212 */           Phobos.inventoryManager.recoverSilent(lastHotbarSlot);
/*     */         } 
/* 214 */         switchedItemSwitched[1] = true;
/*     */         break;
/*     */       case NONE:
/* 217 */         if (back) {
/* 218 */           switchedItemSwitched[1] = true; break;
/*     */         } 
/* 220 */         switchedItemSwitched[1] = (mc.field_71439_g.field_71071_by.field_70461_c == findHotbarBlock(clazz));
/*     */         break;
/*     */     } 
/* 223 */     return switchedItemSwitched;
/*     */   }
/*     */   
/*     */   public static boolean[] switchItemToItem(boolean back, int lastHotbarSlot, boolean switchedItem, Switch mode, Item item) {
/* 227 */     boolean[] switchedItemSwitched = { switchedItem, false };
/* 228 */     switch (mode) {
/*     */       case NORMAL:
/* 230 */         if (!back && !switchedItem) {
/* 231 */           switchToHotbarSlot(getItemHotbar(item), false);
/* 232 */           switchedItemSwitched[0] = true;
/* 233 */         } else if (back && switchedItem) {
/* 234 */           switchToHotbarSlot(lastHotbarSlot, false);
/* 235 */           switchedItemSwitched[0] = false;
/*     */         } 
/* 237 */         switchedItemSwitched[1] = true;
/*     */         break;
/*     */       case SILENT:
/* 240 */         if (!back && !switchedItem) {
/* 241 */           switchToHotbarSlot(getItemHotbar(item), true);
/* 242 */           switchedItemSwitched[0] = true;
/* 243 */         } else if (back && switchedItem) {
/* 244 */           switchedItemSwitched[0] = false;
/* 245 */           Phobos.inventoryManager.recoverSilent(lastHotbarSlot);
/*     */         } 
/* 247 */         switchedItemSwitched[1] = true;
/*     */         break;
/*     */       case NONE:
/* 250 */         if (back) {
/* 251 */           switchedItemSwitched[1] = true; break;
/*     */         } 
/* 253 */         switchedItemSwitched[1] = (mc.field_71439_g.field_71071_by.field_70461_c == getItemHotbar(item));
/*     */         break;
/*     */     } 
/* 256 */     return switchedItemSwitched;
/*     */   }
/*     */   
/*     */   public static boolean holdingItem(Class clazz) {
/* 260 */     boolean result = false;
/* 261 */     ItemStack stack = mc.field_71439_g.func_184614_ca();
/* 262 */     result = isInstanceOf(stack, clazz);
/* 263 */     if (!result) {
/* 264 */       ItemStack offhand = mc.field_71439_g.func_184592_cb();
/* 265 */       result = isInstanceOf(stack, clazz);
/*     */     } 
/*     */     
/* 268 */     return result;
/*     */   }
/*     */   
/*     */   public static boolean isInstanceOf(ItemStack stack, Class clazz) {
/* 272 */     if (stack == null) {
/* 273 */       return false;
/*     */     }
/*     */     
/* 276 */     Item item = stack.func_77973_b();
/* 277 */     if (clazz.isInstance(item)) {
/* 278 */       return true;
/*     */     }
/*     */     
/* 281 */     if (item instanceof ItemBlock) {
/* 282 */       Block block = Block.func_149634_a(item);
/* 283 */       return clazz.isInstance(block);
/*     */     } 
/*     */     
/* 286 */     return false;
/*     */   }
/*     */   
/*     */   public enum Switch {
/* 290 */     NORMAL,
/* 291 */     SILENT,
/* 292 */     NONE;
/*     */   }
/*     */   
/*     */   public static int getEmptyXCarry() {
/* 296 */     for (int i = 1; i < 5; i++) {
/* 297 */       Slot craftingSlot = mc.field_71439_g.field_71069_bz.field_75151_b.get(i);
/* 298 */       ItemStack craftingStack = craftingSlot.func_75211_c();
/* 299 */       if (craftingStack.func_190926_b() || craftingStack.func_77973_b() == Items.field_190931_a) {
/* 300 */         return i;
/*     */       }
/*     */     } 
/* 303 */     return -1;
/*     */   }
/*     */   
/*     */   public static boolean isSlotEmpty(int i) {
/* 307 */     Slot slot = mc.field_71439_g.field_71069_bz.field_75151_b.get(i);
/* 308 */     ItemStack stack = slot.func_75211_c();
/* 309 */     return stack.func_190926_b();
/*     */   }
/*     */   
/*     */   public static int convertHotbarToInv(int input) {
/* 313 */     return 36 + input;
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean areStacksCompatible(ItemStack stack1, ItemStack stack2) {
/* 318 */     if (!stack1.func_77973_b().equals(stack2.func_77973_b())) {
/* 319 */       return false;
/*     */     }
/*     */     
/* 322 */     if (stack1.func_77973_b() instanceof ItemBlock && stack2.func_77973_b() instanceof ItemBlock) {
/* 323 */       Block block1 = ((ItemBlock)stack1.func_77973_b()).func_179223_d();
/* 324 */       Block block2 = ((ItemBlock)stack2.func_77973_b()).func_179223_d();
/* 325 */       if (!block1.field_149764_J.equals(block2.field_149764_J)) {
/* 326 */         return false;
/*     */       }
/*     */     } 
/*     */     
/* 330 */     if (!stack1.func_82833_r().equals(stack2.func_82833_r())) {
/* 331 */       return false;
/*     */     }
/*     */     
/* 334 */     return (stack1.func_77952_i() == stack2.func_77952_i());
/*     */   }
/*     */   
/*     */   public static EntityEquipmentSlot getEquipmentFromSlot(int slot) {
/* 338 */     if (slot == 5)
/* 339 */       return EntityEquipmentSlot.HEAD; 
/* 340 */     if (slot == 6)
/* 341 */       return EntityEquipmentSlot.CHEST; 
/* 342 */     if (slot == 7) {
/* 343 */       return EntityEquipmentSlot.LEGS;
/*     */     }
/* 345 */     return EntityEquipmentSlot.FEET;
/*     */   }
/*     */ 
/*     */   
/*     */   public static int findArmorSlot(EntityEquipmentSlot type, boolean binding) {
/* 350 */     int slot = -1;
/* 351 */     float damage = 0.0F;
/* 352 */     for (int i = 9; i < 45; i++) {
/* 353 */       ItemStack s = (Minecraft.func_71410_x()).field_71439_g.field_71069_bz.func_75139_a(i).func_75211_c();
/* 354 */       if (s.func_77973_b() != Items.field_190931_a && s.func_77973_b() instanceof ItemArmor) {
/* 355 */         ItemArmor armor = (ItemArmor)s.func_77973_b();
/* 356 */         if (armor.field_77881_a == type) {
/* 357 */           float currentDamage = (armor.field_77879_b + EnchantmentHelper.func_77506_a(Enchantments.field_180310_c, s));
/* 358 */           boolean cursed = (binding && EnchantmentHelper.func_190938_b(s));
/* 359 */           if (currentDamage > damage && !cursed) {
/* 360 */             damage = currentDamage;
/* 361 */             slot = i;
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/* 366 */     return slot;
/*     */   }
/*     */   
/*     */   public static int findArmorSlot(EntityEquipmentSlot type, boolean binding, boolean withXCarry) {
/* 370 */     int slot = findArmorSlot(type, binding);
/* 371 */     if (slot == -1 && withXCarry) {
/* 372 */       float damage = 0.0F;
/* 373 */       for (int i = 1; i < 5; i++) {
/* 374 */         Slot craftingSlot = mc.field_71439_g.field_71069_bz.field_75151_b.get(i);
/* 375 */         ItemStack craftingStack = craftingSlot.func_75211_c();
/* 376 */         if (craftingStack.func_77973_b() != Items.field_190931_a && craftingStack.func_77973_b() instanceof ItemArmor) {
/* 377 */           ItemArmor armor = (ItemArmor)craftingStack.func_77973_b();
/* 378 */           if (armor.field_77881_a == type) {
/* 379 */             float currentDamage = (armor.field_77879_b + EnchantmentHelper.func_77506_a(Enchantments.field_180310_c, craftingStack));
/* 380 */             boolean cursed = (binding && EnchantmentHelper.func_190938_b(craftingStack));
/* 381 */             if (currentDamage > damage && !cursed) {
/* 382 */               damage = currentDamage;
/* 383 */               slot = i;
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/* 389 */     return slot;
/*     */   }
/*     */   
/*     */   public static int findItemInventorySlot(Item item, boolean offHand, boolean withXCarry) {
/* 393 */     int slot = findItemInventorySlot(item, offHand);
/* 394 */     if (slot == -1 && withXCarry) {
/* 395 */       for (int i = 1; i < 5; i++) {
/* 396 */         Slot craftingSlot = mc.field_71439_g.field_71069_bz.field_75151_b.get(i);
/* 397 */         ItemStack craftingStack = craftingSlot.func_75211_c();
/* 398 */         if (craftingStack.func_77973_b() != Items.field_190931_a) {
/* 399 */           Item craftingStackItem = craftingStack.func_77973_b();
/* 400 */           if (craftingStackItem == item) {
/* 401 */             slot = i;
/*     */           }
/*     */         } 
/*     */       } 
/*     */     }
/* 406 */     return slot;
/*     */   }
/*     */   
/*     */   public static int findBlockSlotInventory(Class clazz, boolean offHand, boolean withXCarry) {
/* 410 */     int slot = findInventoryBlock(clazz, offHand);
/* 411 */     if (slot == -1 && withXCarry) {
/* 412 */       for (int i = 1; i < 5; i++) {
/* 413 */         Slot craftingSlot = mc.field_71439_g.field_71069_bz.field_75151_b.get(i);
/* 414 */         ItemStack craftingStack = craftingSlot.func_75211_c();
/* 415 */         if (craftingStack.func_77973_b() != Items.field_190931_a) {
/* 416 */           Item craftingStackItem = craftingStack.func_77973_b();
/* 417 */           if (clazz.isInstance(craftingStackItem)) {
/* 418 */             slot = i;
/* 419 */           } else if (craftingStackItem instanceof ItemBlock) {
/* 420 */             Block block = ((ItemBlock)craftingStackItem).func_179223_d();
/* 421 */             if (clazz.isInstance(block)) {
/* 422 */               slot = i;
/*     */             }
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     }
/* 428 */     return slot;
/*     */   }
/*     */   
/*     */   public static class Task
/*     */   {
/*     */     private final int slot;
/*     */     private final boolean update;
/*     */     private final boolean quickClick;
/*     */     
/*     */     public Task() {
/* 438 */       this.update = true;
/* 439 */       this.slot = -1;
/* 440 */       this.quickClick = false;
/*     */     }
/*     */     
/*     */     public Task(int slot) {
/* 444 */       this.slot = slot;
/* 445 */       this.quickClick = false;
/* 446 */       this.update = false;
/*     */     }
/*     */     
/*     */     public Task(int slot, boolean quickClick) {
/* 450 */       this.slot = slot;
/* 451 */       this.quickClick = quickClick;
/* 452 */       this.update = false;
/*     */     }
/*     */     
/*     */     public void run() {
/* 456 */       if (this.update) {
/* 457 */         Util.mc.field_71442_b.func_78765_e();
/*     */       }
/*     */       
/* 460 */       if (this.slot != -1) {
/* 461 */         Util.mc.field_71442_b.func_187098_a(0, this.slot, 0, this.quickClick ? ClickType.QUICK_MOVE : ClickType.PICKUP, (EntityPlayer)Util.mc.field_71439_g);
/*     */       }
/*     */     }
/*     */     
/*     */     public boolean isSwitching() {
/* 466 */       return !this.update;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\me\earth\phobo\\util\InventoryUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */