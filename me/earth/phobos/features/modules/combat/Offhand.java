/*     */ package me.earth.phobos.features.modules.combat;
/*     */ 
/*     */ import java.util.Queue;
/*     */ import java.util.concurrent.ConcurrentLinkedQueue;
/*     */ import me.earth.phobos.Phobos;
/*     */ import me.earth.phobos.event.events.PacketEvent;
/*     */ import me.earth.phobos.event.events.ProcessRightClickBlockEvent;
/*     */ import me.earth.phobos.features.modules.Module;
/*     */ import me.earth.phobos.features.setting.Bind;
/*     */ import me.earth.phobos.features.setting.EnumConverter;
/*     */ import me.earth.phobos.features.setting.Setting;
/*     */ import me.earth.phobos.util.EntityUtil;
/*     */ import me.earth.phobos.util.InventoryUtil;
/*     */ import me.earth.phobos.util.Timer;
/*     */ import net.minecraft.block.BlockObsidian;
/*     */ import net.minecraft.block.BlockWeb;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.inventory.ClickType;
/*     */ import net.minecraft.inventory.EntityEquipmentSlot;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemBlock;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.network.Packet;
/*     */ import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
/*     */ import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
/*     */ import net.minecraft.util.EnumHand;
/*     */ import net.minecraft.util.math.AxisAlignedBB;
/*     */ import net.minecraft.util.math.BlockPos;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraftforge.fml.common.eventhandler.EventPriority;
/*     */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*     */ import net.minecraftforge.fml.common.gameevent.InputEvent;
/*     */ import org.lwjgl.input.Keyboard;
/*     */ import org.lwjgl.input.Mouse;
/*     */ 
/*     */ public class Offhand
/*     */   extends Module
/*     */ {
/*  43 */   public Setting<Type> type = register(new Setting("Mode", Type.NEW));
/*     */   
/*  45 */   public Setting<Boolean> cycle = register(new Setting("Cycle", Boolean.valueOf(false), v -> (this.type.getValue() == Type.OLD)));
/*  46 */   public Setting<Bind> cycleKey = register(new Setting("Key", new Bind(-1), v -> (((Boolean)this.cycle.getValue()).booleanValue() && this.type.getValue() == Type.OLD)));
/*     */   
/*  48 */   public Setting<Bind> offHandGapple = register(new Setting("Gapple", new Bind(-1)));
/*  49 */   public Setting<Float> gappleHealth = register(new Setting("G-Health", Float.valueOf(13.0F), Float.valueOf(0.1F), Float.valueOf(36.0F)));
/*  50 */   public Setting<Float> gappleHoleHealth = register(new Setting("G-H-Health", Float.valueOf(3.5F), Float.valueOf(0.1F), Float.valueOf(36.0F)));
/*  51 */   public Setting<Bind> offHandCrystal = register(new Setting("Crystal", new Bind(-1)));
/*  52 */   public Setting<Float> crystalHealth = register(new Setting("C-Health", Float.valueOf(13.0F), Float.valueOf(0.1F), Float.valueOf(36.0F)));
/*  53 */   public Setting<Float> crystalHoleHealth = register(new Setting("C-H-Health", Float.valueOf(3.5F), Float.valueOf(0.1F), Float.valueOf(36.0F)));
/*  54 */   public Setting<Float> cTargetDistance = register(new Setting("C-Distance", Float.valueOf(10.0F), Float.valueOf(1.0F), Float.valueOf(20.0F)));
/*  55 */   public Setting<Bind> obsidian = register(new Setting("Obsidian", new Bind(-1)));
/*  56 */   public Setting<Float> obsidianHealth = register(new Setting("O-Health", Float.valueOf(13.0F), Float.valueOf(0.1F), Float.valueOf(36.0F)));
/*  57 */   public Setting<Float> obsidianHoleHealth = register(new Setting("O-H-Health", Float.valueOf(8.0F), Float.valueOf(0.1F), Float.valueOf(36.0F)));
/*  58 */   public Setting<Bind> webBind = register(new Setting("Webs", new Bind(-1)));
/*  59 */   public Setting<Float> webHealth = register(new Setting("W-Health", Float.valueOf(13.0F), Float.valueOf(0.1F), Float.valueOf(36.0F)));
/*  60 */   public Setting<Float> webHoleHealth = register(new Setting("W-H-Health", Float.valueOf(8.0F), Float.valueOf(0.1F), Float.valueOf(36.0F)));
/*  61 */   public Setting<Boolean> holeCheck = register(new Setting("Hole-Check", Boolean.valueOf(true)));
/*  62 */   public Setting<Boolean> crystalCheck = register(new Setting("Crystal-Check", Boolean.valueOf(false)));
/*  63 */   public Setting<Boolean> gapSwap = register(new Setting("Gap-Swap", Boolean.valueOf(true)));
/*  64 */   public Setting<Integer> updates = register(new Setting("Updates", Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(2)));
/*     */   
/*  66 */   public Setting<Boolean> cycleObby = register(new Setting("CycleObby", Boolean.valueOf(false), v -> (this.type.getValue() == Type.OLD)));
/*  67 */   public Setting<Boolean> cycleWebs = register(new Setting("CycleWebs", Boolean.valueOf(false), v -> (this.type.getValue() == Type.OLD)));
/*  68 */   public Setting<Boolean> crystalToTotem = register(new Setting("Crystal-Totem", Boolean.valueOf(true), v -> (this.type.getValue() == Type.OLD)));
/*  69 */   public Setting<Boolean> absorption = register(new Setting("Absorption", Boolean.valueOf(false), v -> (this.type.getValue() == Type.OLD)));
/*  70 */   public Setting<Boolean> autoGapple = register(new Setting("AutoGapple", Boolean.valueOf(false), v -> (this.type.getValue() == Type.OLD)));
/*  71 */   public Setting<Boolean> onlyWTotem = register(new Setting("OnlyWTotem", Boolean.valueOf(true), v -> (((Boolean)this.autoGapple.getValue()).booleanValue() && this.type.getValue() == Type.OLD)));
/*  72 */   public Setting<Boolean> unDrawTotem = register(new Setting("DrawTotems", Boolean.valueOf(true), v -> (this.type.getValue() == Type.OLD)));
/*  73 */   public Setting<Boolean> noOffhandGC = register(new Setting("NoOGC", Boolean.valueOf(false)));
/*  74 */   public Setting<Boolean> returnToCrystal = register(new Setting("RecoverySwitch", Boolean.valueOf(false)));
/*  75 */   public Setting<Integer> timeout = register(new Setting("Timeout", Integer.valueOf(50), Integer.valueOf(0), Integer.valueOf(500)));
/*  76 */   public Setting<Integer> timeout2 = register(new Setting("Timeout2", Integer.valueOf(50), Integer.valueOf(0), Integer.valueOf(500)));
/*  77 */   public Setting<Integer> actions = register(new Setting("Actions", Integer.valueOf(4), Integer.valueOf(1), Integer.valueOf(4), v -> (this.type.getValue() == Type.OLD)));
/*  78 */   public Setting<NameMode> displayNameChange = register(new Setting("Name", NameMode.TOTEM, v -> (this.type.getValue() == Type.OLD)));
/*     */   
/*  80 */   public Mode mode = Mode.CRYSTALS;
/*  81 */   public Mode oldMode = Mode.CRYSTALS;
/*  82 */   private int oldSlot = -1;
/*     */   
/*     */   private boolean swapToTotem = false;
/*  85 */   public Mode2 currentMode = Mode2.TOTEMS; private boolean eatingApple = false; private boolean oldSwapToTotem = false;
/*  86 */   public int totems = 0;
/*  87 */   public int crystals = 0;
/*  88 */   public int gapples = 0;
/*  89 */   public int obby = 0;
/*  90 */   public int webs = 0;
/*  91 */   public int lastTotemSlot = -1;
/*  92 */   public int lastGappleSlot = -1;
/*  93 */   public int lastCrystalSlot = -1;
/*  94 */   public int lastObbySlot = -1;
/*  95 */   public int lastWebSlot = -1;
/*     */   public boolean holdingCrystal = false;
/*     */   public boolean holdingTotem = false;
/*     */   public boolean holdingGapple = false;
/*     */   public boolean holdingObby = false;
/*     */   public boolean holdingWeb = false;
/*     */   public boolean didSwitchThisTick = false;
/* 102 */   private final Queue<InventoryUtil.Task> taskList = new ConcurrentLinkedQueue<>();
/*     */   private boolean autoGappleSwitch = false;
/*     */   private static Offhand instance;
/* 105 */   private Timer timer = new Timer();
/* 106 */   private Timer secondTimer = new Timer();
/*     */   private boolean second = false;
/*     */   private boolean switchedForHealthReason = false;
/*     */   
/*     */   public Offhand() {
/* 111 */     super("Offhand", "Allows you to switch up your Offhand.", Module.Category.COMBAT, true, false, false);
/* 112 */     instance = this;
/*     */   }
/*     */   
/*     */   public static Offhand getInstance() {
/* 116 */     if (instance == null) {
/* 117 */       instance = new Offhand();
/*     */     }
/* 119 */     return instance;
/*     */   }
/*     */   
/*     */   public void onItemFinish(ItemStack stack, EntityLivingBase base) {
/* 123 */     if (((Boolean)this.noOffhandGC.getValue()).booleanValue() && base.equals(mc.field_71439_g) && stack.func_77973_b() == mc.field_71439_g.func_184592_cb().func_77973_b()) {
/* 124 */       this.secondTimer.reset();
/* 125 */       this.second = true;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onTick() {
/* 131 */     if (nullCheck() || ((Integer)this.updates.getValue()).intValue() == 1) {
/*     */       return;
/*     */     }
/* 134 */     doOffhand();
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onUpdateWalkingPlayer(ProcessRightClickBlockEvent event) {
/* 139 */     if (((Boolean)this.noOffhandGC.getValue()).booleanValue() && event.hand == EnumHand.MAIN_HAND && event.stack.func_77973_b() == Items.field_185158_cP && mc.field_71439_g.func_184592_cb().func_77973_b() == Items.field_151153_ao && mc.field_71476_x != null && event.pos == mc.field_71476_x.func_178782_a()) {
/* 140 */       event.setCanceled(true);
/* 141 */       mc.field_71439_g.func_184598_c(EnumHand.OFF_HAND);
/* 142 */       mc.field_71442_b.func_187101_a((EntityPlayer)mc.field_71439_g, (World)mc.field_71441_e, EnumHand.OFF_HAND);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onUpdate() {
/* 148 */     if (((Boolean)this.noOffhandGC.getValue()).booleanValue()) {
/* 149 */       if (this.timer.passedMs(((Integer)this.timeout.getValue()).intValue())) {
/* 150 */         if (mc.field_71439_g != null && mc.field_71439_g.func_184592_cb().func_77973_b() == Items.field_151153_ao && mc.field_71439_g.func_184614_ca().func_77973_b() == Items.field_185158_cP && Mouse.isButtonDown(1)) {
/* 151 */           mc.field_71439_g.func_184598_c(EnumHand.OFF_HAND);
/* 152 */           mc.field_71474_y.field_74313_G.field_74513_e = Mouse.isButtonDown(1);
/*     */         } 
/* 154 */       } else if (mc.field_71439_g.func_184592_cb().func_77973_b() == Items.field_151153_ao && mc.field_71439_g.func_184614_ca().func_77973_b() == Items.field_185158_cP) {
/* 155 */         mc.field_71474_y.field_74313_G.field_74513_e = false;
/*     */       } 
/*     */     }
/*     */     
/* 159 */     if (nullCheck() || ((Integer)this.updates.getValue()).intValue() == 2) {
/*     */       return;
/*     */     }
/* 162 */     doOffhand();
/* 163 */     if (this.secondTimer.passedMs(((Integer)this.timeout2.getValue()).intValue()) && this.second) {
/* 164 */       this.second = false;
/* 165 */       this.timer.reset();
/*     */     } 
/*     */   }
/*     */   
/*     */   @SubscribeEvent(priority = EventPriority.NORMAL, receiveCanceled = true)
/*     */   public void onKeyInput(InputEvent.KeyInputEvent event) {
/* 171 */     if (Keyboard.getEventKeyState()) {
/* 172 */       if (this.type.getValue() == Type.NEW) {
/* 173 */         if (((Bind)this.offHandCrystal.getValue()).getKey() == Keyboard.getEventKey()) {
/* 174 */           if (this.mode == Mode.CRYSTALS)
/* 175 */           { setSwapToTotem(!isSwapToTotem()); }
/* 176 */           else { setSwapToTotem(false); }
/* 177 */            setMode(Mode.CRYSTALS);
/*     */         } 
/* 179 */         if (((Bind)this.offHandGapple.getValue()).getKey() == Keyboard.getEventKey()) {
/* 180 */           if (this.mode == Mode.GAPPLES)
/* 181 */           { setSwapToTotem(!isSwapToTotem()); }
/* 182 */           else { setSwapToTotem(false); }
/* 183 */            setMode(Mode.GAPPLES);
/*     */         } 
/* 185 */         if (((Bind)this.obsidian.getValue()).getKey() == Keyboard.getEventKey()) {
/* 186 */           if (this.mode == Mode.OBSIDIAN)
/* 187 */           { setSwapToTotem(!isSwapToTotem()); }
/* 188 */           else { setSwapToTotem(false); }
/* 189 */            setMode(Mode.OBSIDIAN);
/*     */         } 
/* 191 */         if (((Bind)this.webBind.getValue()).getKey() == Keyboard.getEventKey()) {
/* 192 */           if (this.mode == Mode.WEBS)
/* 193 */           { setSwapToTotem(!isSwapToTotem()); }
/* 194 */           else { setSwapToTotem(false); }
/* 195 */            setMode(Mode.WEBS);
/*     */         }
/*     */       
/* 198 */       } else if (((Boolean)this.cycle.getValue()).booleanValue()) {
/* 199 */         if (((Bind)this.cycleKey.getValue()).getKey() == Keyboard.getEventKey()) {
/* 200 */           Mode2 newMode = (Mode2)EnumConverter.increaseEnum(this.currentMode);
/* 201 */           if ((newMode == Mode2.OBSIDIAN && !((Boolean)this.cycleObby.getValue()).booleanValue()) || (newMode == Mode2.WEBS && !((Boolean)this.cycleWebs.getValue()).booleanValue())) {
/* 202 */             newMode = Mode2.TOTEMS;
/*     */           }
/* 204 */           setMode(newMode);
/*     */         } 
/*     */       } else {
/* 207 */         if (((Bind)this.offHandCrystal.getValue()).getKey() == Keyboard.getEventKey()) {
/* 208 */           setMode(Mode2.CRYSTALS);
/*     */         }
/*     */         
/* 211 */         if (((Bind)this.offHandGapple.getValue()).getKey() == Keyboard.getEventKey()) {
/* 212 */           setMode(Mode2.GAPPLES);
/*     */         }
/*     */         
/* 215 */         if (((Bind)this.obsidian.getValue()).getKey() == Keyboard.getEventKey()) {
/* 216 */           setMode(Mode2.OBSIDIAN);
/*     */         }
/*     */         
/* 219 */         if (((Bind)this.webBind.getValue()).getKey() == Keyboard.getEventKey()) {
/* 220 */           setMode(Mode2.WEBS);
/*     */         }
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onPacketSend(PacketEvent.Send event) {
/* 229 */     if (((Boolean)this.noOffhandGC.getValue()).booleanValue() && !fullNullCheck() && mc.field_71439_g.func_184592_cb().func_77973_b() == Items.field_151153_ao && mc.field_71439_g.func_184614_ca().func_77973_b() == Items.field_185158_cP && mc.field_71474_y.field_74313_G.func_151470_d()) {
/* 230 */       if (event.getPacket() instanceof CPacketPlayerTryUseItemOnBlock) {
/* 231 */         CPacketPlayerTryUseItemOnBlock packet = (CPacketPlayerTryUseItemOnBlock)event.getPacket();
/* 232 */         if (packet.func_187022_c() == EnumHand.MAIN_HAND && 
/* 233 */           !AutoCrystal.placedPos.contains(packet.func_187023_a())) {
/* 234 */           if (this.timer.passedMs(((Integer)this.timeout.getValue()).intValue())) {
/* 235 */             mc.field_71439_g.func_184598_c(EnumHand.OFF_HAND);
/* 236 */             mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayerTryUseItem(EnumHand.OFF_HAND));
/*     */           } 
/* 238 */           event.setCanceled(true);
/*     */         }
/*     */       
/* 241 */       } else if (event.getPacket() instanceof CPacketPlayerTryUseItem) {
/* 242 */         CPacketPlayerTryUseItem packet = (CPacketPlayerTryUseItem)event.getPacket();
/* 243 */         if (packet.func_187028_a() == EnumHand.OFF_HAND && !this.timer.passedMs(((Integer)this.timeout.getValue()).intValue())) {
/* 244 */           event.setCanceled(true);
/*     */         }
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public String getDisplayInfo() {
/* 252 */     if (this.type.getValue() == Type.NEW) {
/* 253 */       return String.valueOf(getStackSize());
/*     */     }
/* 255 */     switch ((NameMode)this.displayNameChange.getValue()) {
/*     */       case GAPPLES:
/* 257 */         return EnumConverter.getProperName(this.currentMode);
/*     */       case WEBS:
/* 259 */         if (this.currentMode == Mode2.TOTEMS) {
/* 260 */           return this.totems + "";
/*     */         }
/* 262 */         return EnumConverter.getProperName(this.currentMode);
/*     */     } 
/* 264 */     switch (this.currentMode) {
/*     */       case GAPPLES:
/* 266 */         return this.totems + "";
/*     */       case WEBS:
/* 268 */         return this.gapples + "";
/*     */     } 
/* 270 */     return this.crystals + "";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getDisplayName() {
/* 278 */     if (this.type.getValue() == Type.NEW) {
/* 279 */       if (!shouldTotem()) {
/* 280 */         switch (this.mode) {
/*     */           case GAPPLES:
/* 282 */             return "OffhandGapple";
/*     */           case WEBS:
/* 284 */             return "OffhandWebs";
/*     */           case OBSIDIAN:
/* 286 */             return "OffhandObby";
/*     */         } 
/* 288 */         return "OffhandCrystal";
/*     */       } 
/*     */       
/* 291 */       return "AutoTotem" + (!isSwapToTotem() ? ("-" + getModeStr()) : "");
/*     */     } 
/* 293 */     switch ((NameMode)this.displayNameChange.getValue()) {
/*     */       case GAPPLES:
/* 295 */         return (String)this.displayName.getValue();
/*     */       case WEBS:
/* 297 */         if (this.currentMode == Mode2.TOTEMS) {
/* 298 */           return "AutoTotem";
/*     */         }
/* 300 */         return (String)this.displayName.getValue();
/*     */     } 
/* 302 */     switch (this.currentMode) {
/*     */       case GAPPLES:
/* 304 */         return "AutoTotem";
/*     */       case WEBS:
/* 306 */         return "OffhandGapple";
/*     */       case OBSIDIAN:
/* 308 */         return "OffhandWebs";
/*     */       case CRYSTALS:
/* 310 */         return "OffhandObby";
/*     */     } 
/* 312 */     return "OffhandCrystal";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void doOffhand() {
/* 319 */     if (this.type.getValue() == Type.NEW) {
/* 320 */       if (mc.field_71462_r instanceof net.minecraft.client.gui.inventory.GuiContainer && !(mc.field_71462_r instanceof net.minecraft.client.gui.inventory.GuiInventory))
/*     */         return; 
/* 322 */       if (((Boolean)this.gapSwap.getValue()).booleanValue()) {
/* 323 */         if ((getSlot(Mode.GAPPLES) != -1 || mc.field_71439_g.func_184592_cb().func_77973_b() == Items.field_151153_ao) && mc.field_71439_g.func_184614_ca().func_77973_b() != Items.field_151153_ao && mc.field_71474_y.field_74313_G.func_151470_d()) {
/* 324 */           setMode(Mode.GAPPLES);
/* 325 */           this.eatingApple = true;
/* 326 */           this.swapToTotem = false;
/*     */         }
/* 328 */         else if (this.eatingApple) {
/* 329 */           setMode(this.oldMode);
/* 330 */           this.swapToTotem = this.oldSwapToTotem;
/* 331 */           this.eatingApple = false;
/*     */         } else {
/* 333 */           this.oldMode = this.mode;
/* 334 */           this.oldSwapToTotem = this.swapToTotem;
/*     */         } 
/*     */       }
/*     */ 
/*     */       
/* 339 */       if (!shouldTotem()) {
/* 340 */         if (mc.field_71439_g.func_184592_cb() == ItemStack.field_190927_a || !isItemInOffhand()) {
/* 341 */           int slot = (getSlot(this.mode) < 9) ? (getSlot(this.mode) + 36) : getSlot(this.mode);
/* 342 */           if (getSlot(this.mode) != -1) {
/* 343 */             if (this.oldSlot != -1) {
/* 344 */               mc.field_71442_b.func_187098_a(0, 45, 0, ClickType.PICKUP, (EntityPlayer)mc.field_71439_g);
/* 345 */               mc.field_71442_b.func_187098_a(0, this.oldSlot, 0, ClickType.PICKUP, (EntityPlayer)mc.field_71439_g);
/*     */             } 
/* 347 */             this.oldSlot = slot;
/* 348 */             mc.field_71442_b.func_187098_a(0, slot, 0, ClickType.PICKUP, (EntityPlayer)mc.field_71439_g);
/* 349 */             mc.field_71442_b.func_187098_a(0, 45, 0, ClickType.PICKUP, (EntityPlayer)mc.field_71439_g);
/* 350 */             mc.field_71442_b.func_187098_a(0, slot, 0, ClickType.PICKUP, (EntityPlayer)mc.field_71439_g);
/*     */           } 
/*     */         } 
/* 353 */       } else if (!this.eatingApple && (mc.field_71439_g.func_184592_cb() == ItemStack.field_190927_a || mc.field_71439_g.func_184592_cb().func_77973_b() != Items.field_190929_cY)) {
/* 354 */         int slot = (getTotemSlot() < 9) ? (getTotemSlot() + 36) : getTotemSlot();
/* 355 */         if (getTotemSlot() != -1) {
/* 356 */           mc.field_71442_b.func_187098_a(0, slot, 0, ClickType.PICKUP, (EntityPlayer)mc.field_71439_g);
/* 357 */           mc.field_71442_b.func_187098_a(0, 45, 0, ClickType.PICKUP, (EntityPlayer)mc.field_71439_g);
/* 358 */           mc.field_71442_b.func_187098_a(0, this.oldSlot, 0, ClickType.PICKUP, (EntityPlayer)mc.field_71439_g);
/* 359 */           this.oldSlot = -1;
/*     */         } 
/*     */       } 
/*     */     } else {
/* 363 */       if (!((Boolean)this.unDrawTotem.getValue()).booleanValue()) {
/* 364 */         manageDrawn();
/*     */       }
/*     */       
/* 367 */       this.didSwitchThisTick = false;
/* 368 */       this.holdingCrystal = (mc.field_71439_g.func_184592_cb().func_77973_b() == Items.field_185158_cP);
/* 369 */       this.holdingTotem = (mc.field_71439_g.func_184592_cb().func_77973_b() == Items.field_190929_cY);
/* 370 */       this.holdingGapple = (mc.field_71439_g.func_184592_cb().func_77973_b() == Items.field_151153_ao);
/* 371 */       this.holdingObby = InventoryUtil.isBlock(mc.field_71439_g.func_184592_cb().func_77973_b(), BlockObsidian.class);
/* 372 */       this.holdingWeb = InventoryUtil.isBlock(mc.field_71439_g.func_184592_cb().func_77973_b(), BlockWeb.class);
/*     */       
/* 374 */       this.totems = mc.field_71439_g.field_71071_by.field_70462_a.stream().filter(itemStack -> (itemStack.func_77973_b() == Items.field_190929_cY)).mapToInt(ItemStack::func_190916_E).sum();
/* 375 */       if (this.holdingTotem) {
/* 376 */         this.totems += mc.field_71439_g.field_71071_by.field_184439_c.stream().filter(itemStack -> (itemStack.func_77973_b() == Items.field_190929_cY)).mapToInt(ItemStack::func_190916_E).sum();
/*     */       }
/*     */       
/* 379 */       this.crystals = mc.field_71439_g.field_71071_by.field_70462_a.stream().filter(itemStack -> (itemStack.func_77973_b() == Items.field_185158_cP)).mapToInt(ItemStack::func_190916_E).sum();
/* 380 */       if (this.holdingCrystal) {
/* 381 */         this.crystals += mc.field_71439_g.field_71071_by.field_184439_c.stream().filter(itemStack -> (itemStack.func_77973_b() == Items.field_185158_cP)).mapToInt(ItemStack::func_190916_E).sum();
/*     */       }
/*     */       
/* 384 */       this.gapples = mc.field_71439_g.field_71071_by.field_70462_a.stream().filter(itemStack -> (itemStack.func_77973_b() == Items.field_151153_ao)).mapToInt(ItemStack::func_190916_E).sum();
/* 385 */       if (this.holdingGapple) {
/* 386 */         this.gapples += mc.field_71439_g.field_71071_by.field_184439_c.stream().filter(itemStack -> (itemStack.func_77973_b() == Items.field_151153_ao)).mapToInt(ItemStack::func_190916_E).sum();
/*     */       }
/*     */       
/* 389 */       if (this.currentMode == Mode2.WEBS || this.currentMode == Mode2.OBSIDIAN) {
/* 390 */         this.obby = mc.field_71439_g.field_71071_by.field_70462_a.stream().filter(itemStack -> InventoryUtil.isBlock(itemStack.func_77973_b(), BlockObsidian.class)).mapToInt(ItemStack::func_190916_E).sum();
/* 391 */         if (this.holdingObby) {
/* 392 */           this.obby += mc.field_71439_g.field_71071_by.field_184439_c.stream().filter(itemStack -> InventoryUtil.isBlock(itemStack.func_77973_b(), BlockObsidian.class)).mapToInt(ItemStack::func_190916_E).sum();
/*     */         }
/*     */         
/* 395 */         this.webs = mc.field_71439_g.field_71071_by.field_70462_a.stream().filter(itemStack -> InventoryUtil.isBlock(itemStack.func_77973_b(), BlockWeb.class)).mapToInt(ItemStack::func_190916_E).sum();
/* 396 */         if (this.holdingWeb) {
/* 397 */           this.webs += mc.field_71439_g.field_71071_by.field_184439_c.stream().filter(itemStack -> InventoryUtil.isBlock(itemStack.func_77973_b(), BlockWeb.class)).mapToInt(ItemStack::func_190916_E).sum();
/*     */         }
/*     */       } 
/*     */       
/* 401 */       doSwitch();
/*     */     } 
/*     */   }
/*     */   private void manageDrawn() {
/* 405 */     if (this.currentMode == Mode2.TOTEMS && ((Boolean)this.drawn.getValue()).booleanValue()) {
/* 406 */       this.drawn.setValue(Boolean.valueOf(false));
/*     */     }
/*     */     
/* 409 */     if (this.currentMode != Mode2.TOTEMS && !((Boolean)this.drawn.getValue()).booleanValue()) {
/* 410 */       this.drawn.setValue(Boolean.valueOf(true));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void doSwitch() {
/* 416 */     if (((Boolean)this.autoGapple.getValue()).booleanValue()) {
/* 417 */       if (mc.field_71474_y.field_74313_G.func_151470_d()) {
/* 418 */         if (mc.field_71439_g.func_184614_ca().func_77973_b() instanceof net.minecraft.item.ItemSword && (!((Boolean)this.onlyWTotem.getValue()).booleanValue() || mc.field_71439_g.func_184592_cb().func_77973_b() == Items.field_190929_cY)) {
/* 419 */           setMode(Mode.GAPPLES);
/* 420 */           this.autoGappleSwitch = true;
/*     */         } 
/* 422 */       } else if (this.autoGappleSwitch) {
/* 423 */         setMode(Mode2.TOTEMS);
/* 424 */         this.autoGappleSwitch = false;
/*     */       } 
/*     */     }
/*     */     
/* 428 */     if ((this.currentMode == Mode2.GAPPLES && ((!EntityUtil.isSafe((Entity)mc.field_71439_g) && EntityUtil.getHealth((Entity)mc.field_71439_g, ((Boolean)this.absorption.getValue()).booleanValue()) <= ((Float)this.gappleHealth.getValue()).floatValue()) || EntityUtil.getHealth((Entity)mc.field_71439_g, ((Boolean)this.absorption.getValue()).booleanValue()) <= ((Float)this.gappleHoleHealth.getValue()).floatValue())) || (this.currentMode == Mode2.CRYSTALS && ((
/* 429 */       !EntityUtil.isSafe((Entity)mc.field_71439_g) && EntityUtil.getHealth((Entity)mc.field_71439_g, ((Boolean)this.absorption.getValue()).booleanValue()) <= ((Float)this.crystalHealth.getValue()).floatValue()) || EntityUtil.getHealth((Entity)mc.field_71439_g, ((Boolean)this.absorption.getValue()).booleanValue()) <= ((Float)this.crystalHoleHealth.getValue()).floatValue())) || (this.currentMode == Mode2.OBSIDIAN && ((
/* 430 */       !EntityUtil.isSafe((Entity)mc.field_71439_g) && EntityUtil.getHealth((Entity)mc.field_71439_g, ((Boolean)this.absorption.getValue()).booleanValue()) <= ((Float)this.obsidianHealth.getValue()).floatValue()) || EntityUtil.getHealth((Entity)mc.field_71439_g, ((Boolean)this.absorption.getValue()).booleanValue()) <= ((Float)this.obsidianHoleHealth.getValue()).floatValue())) || (this.currentMode == Mode2.WEBS && ((
/* 431 */       !EntityUtil.isSafe((Entity)mc.field_71439_g) && EntityUtil.getHealth((Entity)mc.field_71439_g, ((Boolean)this.absorption.getValue()).booleanValue()) <= ((Float)this.webHealth.getValue()).floatValue()) || EntityUtil.getHealth((Entity)mc.field_71439_g, ((Boolean)this.absorption.getValue()).booleanValue()) <= ((Float)this.webHoleHealth.getValue()).floatValue()))) {
/* 432 */       if (((Boolean)this.returnToCrystal.getValue()).booleanValue() && this.currentMode == Mode2.CRYSTALS) {
/* 433 */         this.switchedForHealthReason = true;
/*     */       }
/* 435 */       setMode(Mode2.TOTEMS);
/*     */     } 
/*     */     
/* 438 */     if (this.switchedForHealthReason && ((EntityUtil.isSafe((Entity)mc.field_71439_g) && EntityUtil.getHealth((Entity)mc.field_71439_g, ((Boolean)this.absorption.getValue()).booleanValue()) > ((Float)this.crystalHoleHealth.getValue()).floatValue()) || EntityUtil.getHealth((Entity)mc.field_71439_g, ((Boolean)this.absorption.getValue()).booleanValue()) > ((Float)this.crystalHealth.getValue()).floatValue())) {
/* 439 */       setMode(Mode2.CRYSTALS);
/* 440 */       this.switchedForHealthReason = false;
/*     */     } 
/*     */     
/* 443 */     if (mc.field_71462_r instanceof net.minecraft.client.gui.inventory.GuiContainer && !(mc.field_71462_r instanceof net.minecraft.client.gui.inventory.GuiInventory)) {
/*     */       return;
/*     */     }
/*     */     
/* 447 */     Item currentOffhandItem = mc.field_71439_g.func_184592_cb().func_77973_b();
/*     */ 
/*     */     
/* 450 */     switch (this.currentMode) {
/*     */       case GAPPLES:
/* 452 */         if (this.totems > 0 && !this.holdingTotem) {
/* 453 */           this.lastTotemSlot = InventoryUtil.findItemInventorySlot(Items.field_190929_cY, false);
/* 454 */           int lastSlot = getLastSlot(currentOffhandItem, this.lastTotemSlot);
/* 455 */           putItemInOffhand(this.lastTotemSlot, lastSlot);
/*     */         } 
/*     */         break;
/*     */       case WEBS:
/* 459 */         if (this.gapples > 0 && !this.holdingGapple) {
/* 460 */           this.lastGappleSlot = InventoryUtil.findItemInventorySlot(Items.field_151153_ao, false);
/* 461 */           int lastSlot = getLastSlot(currentOffhandItem, this.lastGappleSlot);
/* 462 */           putItemInOffhand(this.lastGappleSlot, lastSlot);
/*     */         } 
/*     */         break;
/*     */       case OBSIDIAN:
/* 466 */         if (this.webs > 0 && !this.holdingWeb) {
/* 467 */           this.lastWebSlot = InventoryUtil.findInventoryBlock(BlockWeb.class, false);
/* 468 */           int lastSlot = getLastSlot(currentOffhandItem, this.lastWebSlot);
/* 469 */           putItemInOffhand(this.lastWebSlot, lastSlot);
/*     */         } 
/*     */         break;
/*     */       case CRYSTALS:
/* 473 */         if (this.obby > 0 && !this.holdingObby) {
/* 474 */           this.lastObbySlot = InventoryUtil.findInventoryBlock(BlockObsidian.class, false);
/* 475 */           int lastSlot = getLastSlot(currentOffhandItem, this.lastObbySlot);
/* 476 */           putItemInOffhand(this.lastObbySlot, lastSlot);
/*     */         } 
/*     */         break;
/*     */       default:
/* 480 */         if (this.crystals > 0 && !this.holdingCrystal) {
/* 481 */           this.lastCrystalSlot = InventoryUtil.findItemInventorySlot(Items.field_185158_cP, false);
/* 482 */           int lastSlot = getLastSlot(currentOffhandItem, this.lastCrystalSlot);
/* 483 */           putItemInOffhand(this.lastCrystalSlot, lastSlot);
/*     */         } 
/*     */         break;
/*     */     } 
/* 487 */     for (int i = 0; i < ((Integer)this.actions.getValue()).intValue(); i++) {
/* 488 */       InventoryUtil.Task task = this.taskList.poll();
/* 489 */       if (task != null) {
/* 490 */         task.run();
/* 491 */         if (task.isSwitching()) {
/* 492 */           this.didSwitchThisTick = true;
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private int getLastSlot(Item item, int slotIn) {
/* 499 */     if (item == Items.field_185158_cP)
/* 500 */       return this.lastCrystalSlot; 
/* 501 */     if (item == Items.field_151153_ao)
/* 502 */       return this.lastGappleSlot; 
/* 503 */     if (item == Items.field_190929_cY)
/* 504 */       return this.lastTotemSlot; 
/* 505 */     if (InventoryUtil.isBlock(item, BlockObsidian.class))
/* 506 */       return this.lastObbySlot; 
/* 507 */     if (InventoryUtil.isBlock(item, BlockWeb.class))
/* 508 */       return this.lastWebSlot; 
/* 509 */     if (item == Items.field_190931_a) {
/* 510 */       return -1;
/*     */     }
/* 512 */     return slotIn;
/*     */   }
/*     */ 
/*     */   
/*     */   private void putItemInOffhand(int slotIn, int slotOut) {
/* 517 */     if (slotIn != -1 && this.taskList.isEmpty()) {
/* 518 */       this.taskList.add(new InventoryUtil.Task(slotIn));
/* 519 */       this.taskList.add(new InventoryUtil.Task(45));
/* 520 */       this.taskList.add(new InventoryUtil.Task(slotOut));
/* 521 */       this.taskList.add(new InventoryUtil.Task());
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean noNearbyPlayers() {
/* 526 */     return (this.mode == Mode.CRYSTALS && mc.field_71441_e.field_73010_i.stream().noneMatch(e -> (e != mc.field_71439_g && !Phobos.friendManager.isFriend(e) && mc.field_71439_g.func_70032_d((Entity)e) <= ((Float)this.cTargetDistance.getValue()).floatValue())));
/*     */   }
/*     */   
/*     */   private boolean isItemInOffhand() {
/* 530 */     switch (this.mode) {
/*     */       case GAPPLES:
/* 532 */         return (mc.field_71439_g.func_184592_cb().func_77973_b() == Items.field_151153_ao);
/*     */       case CRYSTALS:
/* 534 */         return (mc.field_71439_g.func_184592_cb().func_77973_b() == Items.field_185158_cP);
/*     */       case OBSIDIAN:
/* 536 */         return (mc.field_71439_g.func_184592_cb().func_77973_b() instanceof ItemBlock && ((ItemBlock)mc.field_71439_g.func_184592_cb().func_77973_b()).field_150939_a == Blocks.field_150343_Z);
/*     */       case WEBS:
/* 538 */         return (mc.field_71439_g.func_184592_cb().func_77973_b() instanceof ItemBlock && ((ItemBlock)mc.field_71439_g.func_184592_cb().func_77973_b()).field_150939_a == Blocks.field_150321_G);
/*     */     } 
/* 540 */     return false;
/*     */   }
/*     */   
/*     */   private boolean isHeldInMainHand() {
/* 544 */     switch (this.mode) {
/*     */       case GAPPLES:
/* 546 */         return (mc.field_71439_g.func_184614_ca().func_77973_b() == Items.field_151153_ao);
/*     */       case CRYSTALS:
/* 548 */         return (mc.field_71439_g.func_184614_ca().func_77973_b() == Items.field_185158_cP);
/*     */       case OBSIDIAN:
/* 550 */         return (mc.field_71439_g.func_184614_ca().func_77973_b() instanceof ItemBlock && ((ItemBlock)mc.field_71439_g.func_184614_ca().func_77973_b()).field_150939_a == Blocks.field_150343_Z);
/*     */       case WEBS:
/* 552 */         return (mc.field_71439_g.func_184614_ca().func_77973_b() instanceof ItemBlock && ((ItemBlock)mc.field_71439_g.func_184614_ca().func_77973_b()).field_150939_a == Blocks.field_150321_G);
/*     */     } 
/* 554 */     return false;
/*     */   }
/*     */   
/*     */   private boolean shouldTotem() {
/* 558 */     if (isHeldInMainHand() || isSwapToTotem()) return true; 
/* 559 */     if (((Boolean)this.holeCheck.getValue()).booleanValue() && EntityUtil.isInHole((Entity)mc.field_71439_g)) {
/* 560 */       return (mc.field_71439_g.func_110143_aJ() + mc.field_71439_g.func_110139_bj() <= getHoleHealth() || mc.field_71439_g.func_184582_a(EntityEquipmentSlot.CHEST).func_77973_b() == Items.field_185160_cR || mc.field_71439_g.field_70143_R >= 3.0F || noNearbyPlayers() || (((Boolean)this.crystalCheck.getValue()).booleanValue() && isCrystalsAABBEmpty()));
/*     */     }
/* 562 */     return (mc.field_71439_g.func_110143_aJ() + mc.field_71439_g.func_110139_bj() <= getHealth() || mc.field_71439_g.func_184582_a(EntityEquipmentSlot.CHEST).func_77973_b() == Items.field_185160_cR || mc.field_71439_g.field_70143_R >= 3.0F || noNearbyPlayers() || (((Boolean)this.crystalCheck.getValue()).booleanValue() && isCrystalsAABBEmpty()));
/*     */   }
/*     */   
/*     */   private boolean isNotEmpty(BlockPos pos) {
/* 566 */     return mc.field_71441_e.func_72839_b(null, new AxisAlignedBB(pos)).stream().anyMatch(e -> e instanceof net.minecraft.entity.item.EntityEnderCrystal);
/*     */   }
/*     */   
/*     */   private float getHealth() {
/* 570 */     switch (this.mode) {
/*     */       case CRYSTALS:
/* 572 */         return ((Float)this.crystalHealth.getValue()).floatValue();
/*     */       case GAPPLES:
/* 574 */         return ((Float)this.gappleHealth.getValue()).floatValue();
/*     */       case OBSIDIAN:
/* 576 */         return ((Float)this.obsidianHealth.getValue()).floatValue();
/*     */     } 
/* 578 */     return ((Float)this.webHealth.getValue()).floatValue();
/*     */   }
/*     */   
/*     */   private float getHoleHealth() {
/* 582 */     switch (this.mode) {
/*     */       case CRYSTALS:
/* 584 */         return ((Float)this.crystalHoleHealth.getValue()).floatValue();
/*     */       case GAPPLES:
/* 586 */         return ((Float)this.gappleHoleHealth.getValue()).floatValue();
/*     */       case OBSIDIAN:
/* 588 */         return ((Float)this.obsidianHoleHealth.getValue()).floatValue();
/*     */     } 
/* 590 */     return ((Float)this.webHoleHealth.getValue()).floatValue();
/*     */   }
/*     */   
/*     */   private boolean isCrystalsAABBEmpty() {
/* 594 */     return (isNotEmpty(mc.field_71439_g.func_180425_c().func_177982_a(1, 0, 0)) || isNotEmpty(mc.field_71439_g.func_180425_c().func_177982_a(-1, 0, 0)) || isNotEmpty(mc.field_71439_g.func_180425_c().func_177982_a(0, 0, 1)) || isNotEmpty(mc.field_71439_g.func_180425_c().func_177982_a(0, 0, -1)) || isNotEmpty(mc.field_71439_g.func_180425_c()));
/*     */   }
/*     */   
/*     */   int getStackSize() {
/* 598 */     int size = 0;
/* 599 */     if (shouldTotem()) {
/* 600 */       for (int i = 45; i > 0; i--) {
/* 601 */         if (mc.field_71439_g.field_71071_by.func_70301_a(i).func_77973_b() == Items.field_190929_cY) {
/* 602 */           size += mc.field_71439_g.field_71071_by.func_70301_a(i).func_190916_E();
/*     */         }
/*     */       } 
/* 605 */     } else if (this.mode == Mode.OBSIDIAN) {
/* 606 */       for (int i = 45; i > 0; i--) {
/* 607 */         if (mc.field_71439_g.field_71071_by.func_70301_a(i).func_77973_b() instanceof ItemBlock && ((ItemBlock)mc.field_71439_g.field_71071_by.func_70301_a(i).func_77973_b()).field_150939_a == Blocks.field_150343_Z) {
/* 608 */           size += mc.field_71439_g.field_71071_by.func_70301_a(i).func_190916_E();
/*     */         }
/*     */       } 
/* 611 */     } else if (this.mode == Mode.WEBS) {
/* 612 */       for (int i = 45; i > 0; i--) {
/* 613 */         if (mc.field_71439_g.field_71071_by.func_70301_a(i).func_77973_b() instanceof ItemBlock && ((ItemBlock)mc.field_71439_g.field_71071_by.func_70301_a(i).func_77973_b()).field_150939_a == Blocks.field_150321_G) {
/* 614 */           size += mc.field_71439_g.field_71071_by.func_70301_a(i).func_190916_E();
/*     */         }
/*     */       } 
/*     */     } else {
/* 618 */       for (int i = 45; i > 0; i--) {
/* 619 */         if (mc.field_71439_g.field_71071_by.func_70301_a(i).func_77973_b() == ((this.mode == Mode.CRYSTALS) ? Items.field_185158_cP : Items.field_151153_ao)) {
/* 620 */           size += mc.field_71439_g.field_71071_by.func_70301_a(i).func_190916_E();
/*     */         }
/*     */       } 
/*     */     } 
/* 624 */     return size;
/*     */   }
/*     */   
/*     */   int getSlot(Mode m) {
/* 628 */     int slot = -1;
/* 629 */     if (m == Mode.OBSIDIAN) {
/* 630 */       for (int i = 45; i > 0; i--) {
/* 631 */         if (mc.field_71439_g.field_71071_by.func_70301_a(i).func_77973_b() instanceof ItemBlock && ((ItemBlock)mc.field_71439_g.field_71071_by.func_70301_a(i).func_77973_b()).field_150939_a == Blocks.field_150343_Z) {
/* 632 */           slot = i;
/*     */           break;
/*     */         } 
/*     */       } 
/* 636 */     } else if (m == Mode.WEBS) {
/* 637 */       for (int i = 45; i > 0; i--) {
/* 638 */         if (mc.field_71439_g.field_71071_by.func_70301_a(i).func_77973_b() instanceof ItemBlock && ((ItemBlock)mc.field_71439_g.field_71071_by.func_70301_a(i).func_77973_b()).field_150939_a == Blocks.field_150321_G) {
/* 639 */           slot = i;
/*     */           break;
/*     */         } 
/*     */       } 
/*     */     } else {
/* 644 */       for (int i = 45; i > 0; i--) {
/* 645 */         if (mc.field_71439_g.field_71071_by.func_70301_a(i).func_77973_b() == ((m == Mode.CRYSTALS) ? Items.field_185158_cP : Items.field_151153_ao)) {
/* 646 */           slot = i;
/*     */           break;
/*     */         } 
/*     */       } 
/*     */     } 
/* 651 */     return slot;
/*     */   }
/*     */   
/*     */   int getTotemSlot() {
/* 655 */     int totemSlot = -1;
/* 656 */     for (int i = 45; i > 0; i--) {
/* 657 */       if (mc.field_71439_g.field_71071_by.func_70301_a(i).func_77973_b() == Items.field_190929_cY) {
/* 658 */         totemSlot = i;
/*     */         break;
/*     */       } 
/*     */     } 
/* 662 */     return totemSlot;
/*     */   }
/*     */   
/*     */   private String getModeStr() {
/* 666 */     switch (this.mode) {
/*     */       case GAPPLES:
/* 668 */         return "G";
/*     */       case WEBS:
/* 670 */         return "W";
/*     */       case OBSIDIAN:
/* 672 */         return "O";
/*     */     } 
/* 674 */     return "C";
/*     */   }
/*     */ 
/*     */   
/*     */   public void setMode(Mode mode) {
/* 679 */     this.mode = mode;
/*     */   }
/*     */   
/*     */   public void setMode(Mode2 mode) {
/* 683 */     if (this.currentMode == mode) {
/* 684 */       this.currentMode = Mode2.TOTEMS;
/* 685 */     } else if (!((Boolean)this.cycle.getValue()).booleanValue() && ((Boolean)this.crystalToTotem.getValue()).booleanValue() && (this.currentMode == Mode2.CRYSTALS || this.currentMode == Mode2.OBSIDIAN || this.currentMode == Mode2.WEBS) && mode == Mode2.GAPPLES) {
/* 686 */       this.currentMode = Mode2.TOTEMS;
/*     */     } else {
/* 688 */       this.currentMode = mode;
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean isSwapToTotem() {
/* 693 */     return this.swapToTotem;
/*     */   }
/*     */   
/*     */   public void setSwapToTotem(boolean swapToTotem) {
/* 697 */     this.swapToTotem = swapToTotem;
/*     */   }
/*     */   
/*     */   public enum Mode {
/* 701 */     CRYSTALS,
/* 702 */     GAPPLES,
/* 703 */     OBSIDIAN,
/* 704 */     WEBS;
/*     */   }
/*     */   
/*     */   public enum Type {
/* 708 */     OLD,
/* 709 */     NEW;
/*     */   }
/*     */   
/*     */   public enum Mode2 {
/* 713 */     TOTEMS,
/* 714 */     GAPPLES,
/* 715 */     CRYSTALS,
/* 716 */     OBSIDIAN,
/* 717 */     WEBS;
/*     */   }
/*     */   
/*     */   public enum NameMode {
/* 721 */     MODE,
/* 722 */     TOTEM,
/* 723 */     AMOUNT;
/*     */   }
/*     */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\me\earth\phobos\features\modules\combat\Offhand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */