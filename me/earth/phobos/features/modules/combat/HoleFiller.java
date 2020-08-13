/*     */ package me.earth.phobos.features.modules.combat;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import me.earth.phobos.Phobos;
/*     */ import me.earth.phobos.event.events.UpdateWalkingPlayerEvent;
/*     */ import me.earth.phobos.features.modules.Module;
/*     */ import me.earth.phobos.features.modules.player.BlockTweaks;
/*     */ import me.earth.phobos.features.modules.player.Freecam;
/*     */ import me.earth.phobos.features.setting.Bind;
/*     */ import me.earth.phobos.features.setting.Setting;
/*     */ import me.earth.phobos.util.BlockUtil;
/*     */ import me.earth.phobos.util.EntityUtil;
/*     */ import me.earth.phobos.util.InventoryUtil;
/*     */ import me.earth.phobos.util.MathUtil;
/*     */ import me.earth.phobos.util.Timer;
/*     */ import net.minecraft.block.BlockObsidian;
/*     */ import net.minecraft.block.BlockWeb;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.util.EnumHand;
/*     */ import net.minecraft.util.math.BlockPos;
/*     */ import net.minecraftforge.fml.common.eventhandler.EventPriority;
/*     */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*     */ import net.minecraftforge.fml.common.gameevent.InputEvent;
/*     */ import org.lwjgl.input.Keyboard;
/*     */ 
/*     */ public class HoleFiller
/*     */   extends Module {
/*  30 */   public Setting<Mode> mode = register(new Setting("Mode", Mode.OBSIDIAN));
/*  31 */   public Setting<Bind> obbyBind = register(new Setting("Obsidian", new Bind(-1)));
/*  32 */   public Setting<Bind> webBind = register(new Setting("Webs", new Bind(-1)));
/*  33 */   private final Setting<Double> range = register(new Setting("PlaceRange", Double.valueOf(6.0D), Double.valueOf(0.0D), Double.valueOf(10.0D)));
/*  34 */   private final Setting<Integer> delay = register(new Setting("Delay/Place", Integer.valueOf(50), Integer.valueOf(0), Integer.valueOf(250)));
/*  35 */   private final Setting<Integer> blocksPerTick = register(new Setting("Block/Place", Integer.valueOf(8), Integer.valueOf(1), Integer.valueOf(20)));
/*  36 */   private final Setting<Boolean> rotate = register(new Setting("Rotate", Boolean.valueOf(true)));
/*  37 */   private final Setting<Boolean> raytrace = register(new Setting("Raytrace", Boolean.valueOf(false)));
/*  38 */   private final Setting<Boolean> disable = register(new Setting("Disable", Boolean.valueOf(true)));
/*  39 */   private final Setting<Integer> disableTime = register(new Setting("Ms/Disable", Integer.valueOf(200), Integer.valueOf(1), Integer.valueOf(250)));
/*  40 */   private final Setting<Boolean> offhand = register(new Setting("OffHand", Boolean.valueOf(true)));
/*  41 */   private final Setting<InventoryUtil.Switch> switchMode = register(new Setting("Switch", InventoryUtil.Switch.NORMAL));
/*  42 */   private final Setting<Boolean> onlySafe = register(new Setting("OnlySafe", Boolean.valueOf(true), v -> ((Boolean)this.offhand.getValue()).booleanValue()));
/*  43 */   private final Setting<Boolean> webSelf = register(new Setting("SelfWeb", Boolean.valueOf(false)));
/*  44 */   private final Setting<Boolean> highWeb = register(new Setting("HighWeb", Boolean.valueOf(false)));
/*  45 */   private final Setting<Boolean> freecam = register(new Setting("Freecam", Boolean.valueOf(false)));
/*  46 */   private final Setting<Boolean> midSafeHoles = register(new Setting("MidSafe", Boolean.valueOf(false)));
/*  47 */   private final Setting<Boolean> packet = register(new Setting("Packet", Boolean.valueOf(false)));
/*     */   
/*  49 */   private static HoleFiller INSTANCE = new HoleFiller();
/*  50 */   public Mode currentMode = Mode.OBSIDIAN;
/*  51 */   private final Timer offTimer = new Timer();
/*  52 */   private final Timer timer = new Timer();
/*     */   private boolean accessedViaBind = false;
/*  54 */   private int targetSlot = -1;
/*  55 */   private int blocksThisTick = 0;
/*  56 */   private Offhand.Mode offhandMode = Offhand.Mode.CRYSTALS;
/*  57 */   private Offhand.Mode2 offhandMode2 = Offhand.Mode2.CRYSTALS;
/*  58 */   private final Map<BlockPos, Integer> retries = new HashMap<>();
/*  59 */   private final Timer retryTimer = new Timer();
/*     */   private boolean isSneaking;
/*     */   private boolean hasOffhand = false;
/*     */   private boolean placeHighWeb = false;
/*  63 */   private int lastHotbarSlot = -1;
/*     */   private boolean switchedItem = false;
/*     */   
/*     */   public HoleFiller() {
/*  67 */     super("HoleFiller", "Fills holes around you.", Module.Category.COMBAT, true, false, true);
/*  68 */     setInstance();
/*     */   }
/*     */   
/*     */   private void setInstance() {
/*  72 */     INSTANCE = this;
/*     */   }
/*     */   
/*     */   public static HoleFiller getInstance() {
/*  76 */     if (INSTANCE == null) {
/*  77 */       INSTANCE = new HoleFiller();
/*     */     }
/*  79 */     return INSTANCE;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onEnable() {
/*  84 */     if (fullNullCheck()) {
/*  85 */       disable();
/*     */     }
/*     */     
/*  88 */     this.lastHotbarSlot = mc.field_71439_g.field_71071_by.field_70461_c;
/*     */     
/*  90 */     if (!this.accessedViaBind) {
/*  91 */       this.currentMode = (Mode)this.mode.getValue();
/*     */     }
/*     */     
/*  94 */     Offhand module = (Offhand)Phobos.moduleManager.getModuleByClass(Offhand.class);
/*     */     
/*  96 */     this.offhandMode = module.mode;
/*  97 */     this.offhandMode2 = module.currentMode;
/*     */     
/*  99 */     if (((Boolean)this.offhand.getValue()).booleanValue() && (EntityUtil.isSafe((Entity)mc.field_71439_g) || !((Boolean)this.onlySafe.getValue()).booleanValue())) {
/* 100 */       if (module.type.getValue() == Offhand.Type.NEW) {
/* 101 */         if (this.currentMode == Mode.WEBS) {
/* 102 */           module.setSwapToTotem(false);
/* 103 */           module.setMode(Offhand.Mode.WEBS);
/*     */         } else {
/* 105 */           module.setSwapToTotem(false);
/* 106 */           module.setMode(Offhand.Mode.OBSIDIAN);
/*     */         } 
/*     */       } else {
/* 109 */         if (this.currentMode == Mode.WEBS) {
/* 110 */           module.setMode(Offhand.Mode2.WEBS);
/*     */         } else {
/* 112 */           module.setMode(Offhand.Mode2.OBSIDIAN);
/*     */         } 
/* 114 */         if (!module.didSwitchThisTick) {
/* 115 */           module.doOffhand();
/*     */         }
/*     */       } 
/*     */     }
/*     */     
/* 120 */     Phobos.holeManager.update();
/* 121 */     this.offTimer.reset();
/*     */   }
/*     */ 
/*     */   
/*     */   public void onTick() {
/* 126 */     if (isOn() && (((Integer)this.blocksPerTick.getValue()).intValue() != 1 || !((Boolean)this.rotate.getValue()).booleanValue())) {
/* 127 */       doHoleFill();
/*     */     }
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onUpdateWalkingPlayer(UpdateWalkingPlayerEvent event) {
/* 133 */     if (isOn() && event.getStage() == 0 && ((Integer)this.blocksPerTick.getValue()).intValue() == 1 && ((Boolean)this.rotate.getValue()).booleanValue()) {
/* 134 */       doHoleFill();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void onDisable() {
/* 140 */     if (((Boolean)this.offhand.getValue()).booleanValue()) {
/* 141 */       ((Offhand)Phobos.moduleManager.getModuleByClass(Offhand.class)).setMode(this.offhandMode);
/* 142 */       ((Offhand)Phobos.moduleManager.getModuleByClass(Offhand.class)).setMode(this.offhandMode2);
/*     */     } 
/* 144 */     switchItem(true);
/* 145 */     this.isSneaking = EntityUtil.stopSneaking(this.isSneaking);
/* 146 */     this.retries.clear();
/* 147 */     this.accessedViaBind = false;
/* 148 */     this.hasOffhand = false;
/*     */   }
/*     */   
/*     */   @SubscribeEvent(priority = EventPriority.NORMAL, receiveCanceled = true)
/*     */   public void onKeyInput(InputEvent.KeyInputEvent event) {
/* 153 */     if (Keyboard.getEventKeyState()) {
/* 154 */       if (((Bind)this.obbyBind.getValue()).getKey() == Keyboard.getEventKey()) {
/* 155 */         this.accessedViaBind = true;
/* 156 */         this.currentMode = Mode.OBSIDIAN;
/* 157 */         toggle();
/*     */       } 
/*     */       
/* 160 */       if (((Bind)this.webBind.getValue()).getKey() == Keyboard.getEventKey()) {
/* 161 */         this.accessedViaBind = true;
/* 162 */         this.currentMode = Mode.WEBS;
/* 163 */         toggle();
/*     */       } 
/*     */     } 
/*     */   }
/*     */   private void doHoleFill() {
/*     */     List<BlockPos> targets;
/* 169 */     if (check()) {
/*     */       return;
/*     */     }
/*     */     
/* 173 */     if (this.placeHighWeb) {
/* 174 */       BlockPos pos = new BlockPos(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u + 1.0D, mc.field_71439_g.field_70161_v);
/* 175 */       placeBlock(pos);
/* 176 */       this.placeHighWeb = false;
/*     */     } 
/*     */ 
/*     */     
/* 180 */     if (((Boolean)this.midSafeHoles.getValue()).booleanValue()) {
/* 181 */       targets = Phobos.holeManager.getMidSafety();
/*     */     } else {
/* 183 */       targets = Phobos.holeManager.getHoles();
/*     */     } 
/*     */     
/* 186 */     for (BlockPos position : targets) {
/* 187 */       if (mc.field_71439_g.func_174818_b(position) > MathUtil.square(((Double)this.range.getValue()).doubleValue())) {
/*     */         continue;
/*     */       }
/*     */       
/* 191 */       if (position.equals(new BlockPos(mc.field_71439_g.func_174791_d()))) {
/* 192 */         if (this.currentMode != Mode.WEBS || !((Boolean)this.webSelf.getValue()).booleanValue()) {
/*     */           continue;
/*     */         }
/* 195 */         if (((Boolean)this.highWeb.getValue()).booleanValue()) {
/* 196 */           this.placeHighWeb = true;
/*     */         }
/*     */       } 
/*     */ 
/*     */       
/* 201 */       int placeability = BlockUtil.isPositionPlaceable(position, ((Boolean)this.raytrace.getValue()).booleanValue());
/*     */       
/* 203 */       if (placeability == 1 && (
/* 204 */         this.currentMode == Mode.WEBS || this.switchMode.getValue() == InventoryUtil.Switch.SILENT || (BlockTweaks.getINSTANCE().isOn() && ((Boolean)(BlockTweaks.getINSTANCE()).noBlock.getValue()).booleanValue())) && (
/* 205 */         this.currentMode == Mode.WEBS || this.retries.get(position) == null || ((Integer)this.retries.get(position)).intValue() < 4)) {
/* 206 */         placeBlock(position);
/* 207 */         if (this.currentMode != Mode.WEBS) {
/* 208 */           this.retries.put(position, Integer.valueOf((this.retries.get(position) == null) ? 1 : (((Integer)this.retries.get(position)).intValue() + 1)));
/*     */         }
/*     */ 
/*     */         
/*     */         continue;
/*     */       } 
/*     */       
/* 215 */       if (placeability == 3) {
/* 216 */         placeBlock(position);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private void placeBlock(BlockPos pos) {
/* 222 */     if (this.blocksThisTick < ((Integer)this.blocksPerTick.getValue()).intValue() && 
/* 223 */       switchItem(false)) {
/* 224 */       boolean smartRotate = (((Integer)this.blocksPerTick.getValue()).intValue() == 1 && ((Boolean)this.rotate.getValue()).booleanValue());
/* 225 */       if (smartRotate) {
/* 226 */         this.isSneaking = BlockUtil.placeBlockSmartRotate(pos, this.hasOffhand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, true, ((Boolean)this.packet.getValue()).booleanValue(), this.isSneaking);
/*     */       } else {
/* 228 */         this.isSneaking = BlockUtil.placeBlock(pos, this.hasOffhand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, ((Boolean)this.rotate.getValue()).booleanValue(), ((Boolean)this.packet.getValue()).booleanValue(), this.isSneaking);
/*     */       } 
/* 230 */       this.timer.reset();
/* 231 */       this.blocksThisTick++;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean check() {
/* 237 */     if (fullNullCheck() || (((Boolean)this.disable.getValue()).booleanValue() && this.offTimer.passedMs(((Integer)this.disableTime.getValue()).intValue()))) {
/* 238 */       disable();
/* 239 */       return true;
/*     */     } 
/*     */     
/* 242 */     if (mc.field_71439_g.field_71071_by.field_70461_c != this.lastHotbarSlot && mc.field_71439_g.field_71071_by.field_70461_c != InventoryUtil.findHotbarBlock((this.currentMode == Mode.WEBS) ? BlockWeb.class : BlockObsidian.class)) {
/* 243 */       this.lastHotbarSlot = mc.field_71439_g.field_71071_by.field_70461_c;
/*     */     }
/*     */     
/* 246 */     switchItem(true);
/*     */     
/* 248 */     if (!((Boolean)this.freecam.getValue()).booleanValue() && Phobos.moduleManager.isModuleEnabled(Freecam.class)) {
/* 249 */       return true;
/*     */     }
/*     */     
/* 252 */     this.blocksThisTick = 0;
/* 253 */     this.isSneaking = EntityUtil.stopSneaking(this.isSneaking);
/*     */     
/* 255 */     if (this.retryTimer.passedMs(2000L)) {
/* 256 */       this.retries.clear();
/* 257 */       this.retryTimer.reset();
/*     */     } 
/*     */     
/* 260 */     switch (this.currentMode) {
/*     */       case WEBS:
/* 262 */         this.hasOffhand = InventoryUtil.isBlock(mc.field_71439_g.func_184592_cb().func_77973_b(), BlockWeb.class);
/* 263 */         this.targetSlot = InventoryUtil.findHotbarBlock(BlockWeb.class);
/*     */         break;
/*     */       case OBSIDIAN:
/* 266 */         this.hasOffhand = InventoryUtil.isBlock(mc.field_71439_g.func_184592_cb().func_77973_b(), BlockObsidian.class);
/* 267 */         this.targetSlot = InventoryUtil.findHotbarBlock(BlockObsidian.class);
/*     */         break;
/*     */     } 
/*     */ 
/*     */     
/* 272 */     if (((Boolean)this.onlySafe.getValue()).booleanValue() && !EntityUtil.isSafe((Entity)mc.field_71439_g)) {
/* 273 */       disable();
/* 274 */       return true;
/*     */     } 
/*     */     
/* 277 */     if (!this.hasOffhand && this.targetSlot == -1 && (!((Boolean)this.offhand.getValue()).booleanValue() || (!EntityUtil.isSafe((Entity)mc.field_71439_g) && ((Boolean)this.onlySafe.getValue()).booleanValue()))) {
/* 278 */       return true;
/*     */     }
/*     */     
/* 281 */     if (((Boolean)this.offhand.getValue()).booleanValue() && !this.hasOffhand) {
/* 282 */       return true;
/*     */     }
/*     */     
/* 285 */     return !this.timer.passedMs(((Integer)this.delay.getValue()).intValue());
/*     */   }
/*     */   
/*     */   private boolean switchItem(boolean back) {
/* 289 */     if (((Boolean)this.offhand.getValue()).booleanValue()) {
/* 290 */       return true;
/*     */     }
/* 292 */     boolean[] value = InventoryUtil.switchItem(back, this.lastHotbarSlot, this.switchedItem, (InventoryUtil.Switch)this.switchMode.getValue(), (this.currentMode == Mode.WEBS) ? BlockWeb.class : BlockObsidian.class);
/* 293 */     this.switchedItem = value[0];
/* 294 */     return value[1];
/*     */   }
/*     */   
/*     */   public enum Mode {
/* 298 */     WEBS,
/* 299 */     OBSIDIAN;
/*     */   }
/*     */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\me\earth\phobos\features\modules\combat\HoleFiller.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */