/*     */ package me.earth.phobos.features.modules.combat;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Comparator;
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
/*     */ import me.earth.phobos.util.Timer;
/*     */ import net.minecraft.block.BlockObsidian;
/*     */ import net.minecraft.block.BlockWeb;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.util.EnumHand;
/*     */ import net.minecraft.util.math.BlockPos;
/*     */ import net.minecraft.util.math.Vec3i;
/*     */ import net.minecraftforge.fml.common.eventhandler.EventPriority;
/*     */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*     */ import net.minecraftforge.fml.common.gameevent.InputEvent;
/*     */ import org.lwjgl.input.Keyboard;
/*     */ 
/*     */ public class Selftrap
/*     */   extends Module {
/*  32 */   public Setting<Mode> mode = register(new Setting("Mode", Mode.OBSIDIAN));
/*  33 */   public Setting<Bind> obbyBind = register(new Setting("Obsidian", new Bind(-1)));
/*  34 */   public Setting<Bind> webBind = register(new Setting("Webs", new Bind(-1)));
/*  35 */   private final Setting<Integer> delay = register(new Setting("Delay/Place", Integer.valueOf(50), Integer.valueOf(0), Integer.valueOf(250)));
/*  36 */   private final Setting<Integer> blocksPerTick = register(new Setting("Block/Place", Integer.valueOf(8), Integer.valueOf(1), Integer.valueOf(20)));
/*  37 */   private final Setting<Boolean> rotate = register(new Setting("Rotate", Boolean.valueOf(true)));
/*  38 */   private final Setting<Boolean> disable = register(new Setting("Disable", Boolean.valueOf(true)));
/*  39 */   private final Setting<Integer> disableTime = register(new Setting("Ms/Disable", Integer.valueOf(200), Integer.valueOf(1), Integer.valueOf(250)));
/*  40 */   private final Setting<Boolean> offhand = register(new Setting("OffHand", Boolean.valueOf(true)));
/*  41 */   private final Setting<InventoryUtil.Switch> switchMode = register(new Setting("Switch", InventoryUtil.Switch.NORMAL));
/*  42 */   private final Setting<Boolean> onlySafe = register(new Setting("OnlySafe", Boolean.valueOf(true), v -> ((Boolean)this.offhand.getValue()).booleanValue()));
/*  43 */   private final Setting<Boolean> highWeb = register(new Setting("HighWeb", Boolean.valueOf(false)));
/*  44 */   private final Setting<Boolean> freecam = register(new Setting("Freecam", Boolean.valueOf(false)));
/*  45 */   private final Setting<Boolean> packet = register(new Setting("Packet", Boolean.valueOf(false)));
/*     */   
/*  47 */   public Mode currentMode = Mode.OBSIDIAN;
/*  48 */   private final Timer offTimer = new Timer();
/*  49 */   private final Timer timer = new Timer();
/*     */   private boolean accessedViaBind = false;
/*  51 */   private int blocksThisTick = 0;
/*  52 */   private Offhand.Mode offhandMode = Offhand.Mode.CRYSTALS;
/*  53 */   private Offhand.Mode2 offhandMode2 = Offhand.Mode2.CRYSTALS;
/*  54 */   private final Map<BlockPos, Integer> retries = new HashMap<>();
/*  55 */   private final Timer retryTimer = new Timer();
/*     */   private boolean isSneaking;
/*     */   private boolean hasOffhand = false;
/*     */   private boolean placeHighWeb = false;
/*  59 */   private int lastHotbarSlot = -1;
/*     */   private boolean switchedItem = false;
/*     */   
/*     */   public Selftrap() {
/*  63 */     super("Selftrap", "Lure your enemies in!", Module.Category.COMBAT, true, false, true);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onEnable() {
/*  68 */     if (fullNullCheck()) {
/*  69 */       disable();
/*     */     }
/*     */     
/*  72 */     this.lastHotbarSlot = mc.field_71439_g.field_71071_by.field_70461_c;
/*     */     
/*  74 */     if (!this.accessedViaBind) {
/*  75 */       this.currentMode = (Mode)this.mode.getValue();
/*     */     }
/*     */     
/*  78 */     Offhand module = (Offhand)Phobos.moduleManager.getModuleByClass(Offhand.class);
/*     */     
/*  80 */     this.offhandMode = module.mode;
/*  81 */     this.offhandMode2 = module.currentMode;
/*     */     
/*  83 */     if (((Boolean)this.offhand.getValue()).booleanValue() && (EntityUtil.isSafe((Entity)mc.field_71439_g) || !((Boolean)this.onlySafe.getValue()).booleanValue())) {
/*  84 */       if (module.type.getValue() == Offhand.Type.OLD) {
/*  85 */         if (this.currentMode == Mode.WEBS) {
/*  86 */           module.setMode(Offhand.Mode2.WEBS);
/*     */         } else {
/*  88 */           module.setMode(Offhand.Mode2.OBSIDIAN);
/*     */         }
/*     */       
/*  91 */       } else if (this.currentMode == Mode.WEBS) {
/*  92 */         module.setSwapToTotem(false);
/*  93 */         module.setMode(Offhand.Mode.WEBS);
/*     */       } else {
/*  95 */         module.setSwapToTotem(false);
/*  96 */         module.setMode(Offhand.Mode.OBSIDIAN);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 101 */     Phobos.holeManager.update();
/* 102 */     this.offTimer.reset();
/*     */   }
/*     */ 
/*     */   
/*     */   public void onTick() {
/* 107 */     if (isOn() && (((Integer)this.blocksPerTick.getValue()).intValue() != 1 || !((Boolean)this.rotate.getValue()).booleanValue())) {
/* 108 */       doHoleFill();
/*     */     }
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onUpdateWalkingPlayer(UpdateWalkingPlayerEvent event) {
/* 114 */     if (isOn() && event.getStage() == 0 && ((Integer)this.blocksPerTick.getValue()).intValue() == 1 && ((Boolean)this.rotate.getValue()).booleanValue()) {
/* 115 */       doHoleFill();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void onDisable() {
/* 121 */     if (((Boolean)this.offhand.getValue()).booleanValue()) {
/* 122 */       ((Offhand)Phobos.moduleManager.getModuleByClass(Offhand.class)).setMode(this.offhandMode);
/* 123 */       ((Offhand)Phobos.moduleManager.getModuleByClass(Offhand.class)).setMode(this.offhandMode2);
/*     */     } 
/* 125 */     switchItem(true);
/* 126 */     this.isSneaking = EntityUtil.stopSneaking(this.isSneaking);
/* 127 */     this.retries.clear();
/* 128 */     this.accessedViaBind = false;
/* 129 */     this.hasOffhand = false;
/*     */   }
/*     */   
/*     */   @SubscribeEvent(priority = EventPriority.NORMAL, receiveCanceled = true)
/*     */   public void onKeyInput(InputEvent.KeyInputEvent event) {
/* 134 */     if (Keyboard.getEventKeyState()) {
/* 135 */       if (((Bind)this.obbyBind.getValue()).getKey() == Keyboard.getEventKey()) {
/* 136 */         this.accessedViaBind = true;
/* 137 */         this.currentMode = Mode.OBSIDIAN;
/* 138 */         toggle();
/*     */       } 
/*     */       
/* 141 */       if (((Bind)this.webBind.getValue()).getKey() == Keyboard.getEventKey()) {
/* 142 */         this.accessedViaBind = true;
/* 143 */         this.currentMode = Mode.WEBS;
/* 144 */         toggle();
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void doHoleFill() {
/* 150 */     if (check()) {
/*     */       return;
/*     */     }
/*     */     
/* 154 */     if (this.placeHighWeb) {
/* 155 */       BlockPos pos = new BlockPos(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u + 1.0D, mc.field_71439_g.field_70161_v);
/* 156 */       placeBlock(pos);
/* 157 */       this.placeHighWeb = false;
/*     */     } 
/*     */     
/* 160 */     for (BlockPos position : getPositions()) {
/* 161 */       int placeability = BlockUtil.isPositionPlaceable(position, false);
/* 162 */       if (placeability == 1) {
/* 163 */         switch (this.currentMode) {
/*     */           case WEBS:
/* 165 */             placeBlock(position);
/*     */             break;
/*     */           case OBSIDIAN:
/* 168 */             if ((this.switchMode.getValue() == InventoryUtil.Switch.SILENT || (BlockTweaks.getINSTANCE().isOn() && ((Boolean)(BlockTweaks.getINSTANCE()).noBlock.getValue()).booleanValue())) && (
/* 169 */               this.retries.get(position) == null || ((Integer)this.retries.get(position)).intValue() < 4)) {
/* 170 */               placeBlock(position);
/* 171 */               this.retries.put(position, Integer.valueOf((this.retries.get(position) == null) ? 1 : (((Integer)this.retries.get(position)).intValue() + 1)));
/*     */             } 
/*     */             break;
/*     */         } 
/*     */ 
/*     */       
/*     */       }
/* 178 */       if (placeability == 3)
/* 179 */         placeBlock(position); 
/*     */     } 
/*     */   }
/*     */   
/*     */   private List<BlockPos> getPositions() {
/*     */     int placeability;
/* 185 */     List<BlockPos> positions = new ArrayList<>();
/* 186 */     switch (this.currentMode) {
/*     */       case WEBS:
/* 188 */         positions.add(new BlockPos(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u, mc.field_71439_g.field_70161_v));
/* 189 */         if (((Boolean)this.highWeb.getValue()).booleanValue()) {
/* 190 */           positions.add(new BlockPos(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u + 1.0D, mc.field_71439_g.field_70161_v));
/*     */         }
/*     */         break;
/*     */       case OBSIDIAN:
/* 194 */         positions.add(new BlockPos(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u + 2.0D, mc.field_71439_g.field_70161_v));
/* 195 */         placeability = BlockUtil.isPositionPlaceable(positions.get(0), false);
/* 196 */         switch (placeability) {
/*     */           case 0:
/* 198 */             return new ArrayList<>();
/*     */           case 3:
/* 200 */             return positions;
/*     */           case 1:
/* 202 */             if (BlockUtil.isPositionPlaceable(positions.get(0), false, false) == 3) {
/* 203 */               return positions;
/*     */             }
/*     */           case 2:
/* 206 */             positions.add(new BlockPos(mc.field_71439_g.field_70165_t + 1.0D, mc.field_71439_g.field_70163_u + 1.0D, mc.field_71439_g.field_70161_v));
/* 207 */             positions.add(new BlockPos(mc.field_71439_g.field_70165_t + 1.0D, mc.field_71439_g.field_70163_u + 2.0D, mc.field_71439_g.field_70161_v));
/*     */             break;
/*     */         } 
/*     */         break;
/*     */     } 
/* 212 */     positions.sort(Comparator.comparingDouble(Vec3i::func_177956_o));
/* 213 */     return positions;
/*     */   }
/*     */   
/*     */   private void placeBlock(BlockPos pos) {
/* 217 */     if (this.blocksThisTick < ((Integer)this.blocksPerTick.getValue()).intValue() && 
/* 218 */       switchItem(false)) {
/* 219 */       boolean smartRotate = (((Integer)this.blocksPerTick.getValue()).intValue() == 1 && ((Boolean)this.rotate.getValue()).booleanValue());
/* 220 */       if (smartRotate) {
/* 221 */         this.isSneaking = BlockUtil.placeBlockSmartRotate(pos, this.hasOffhand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, true, ((Boolean)this.packet.getValue()).booleanValue(), this.isSneaking);
/*     */       } else {
/* 223 */         this.isSneaking = BlockUtil.placeBlock(pos, this.hasOffhand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, ((Boolean)this.rotate.getValue()).booleanValue(), ((Boolean)this.packet.getValue()).booleanValue(), this.isSneaking);
/*     */       } 
/* 225 */       this.timer.reset();
/*     */       
/* 227 */       this.blocksThisTick++;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean check() {
/* 233 */     if (fullNullCheck() || (((Boolean)this.disable.getValue()).booleanValue() && this.offTimer.passedMs(((Integer)this.disableTime.getValue()).intValue()))) {
/* 234 */       disable();
/* 235 */       return true;
/*     */     } 
/*     */     
/* 238 */     if (mc.field_71439_g.field_71071_by.field_70461_c != this.lastHotbarSlot && mc.field_71439_g.field_71071_by.field_70461_c != InventoryUtil.findHotbarBlock((this.currentMode == Mode.WEBS) ? BlockWeb.class : BlockObsidian.class)) {
/* 239 */       this.lastHotbarSlot = mc.field_71439_g.field_71071_by.field_70461_c;
/*     */     }
/*     */     
/* 242 */     switchItem(true);
/*     */     
/* 244 */     if (!((Boolean)this.freecam.getValue()).booleanValue() && Phobos.moduleManager.isModuleEnabled(Freecam.class)) {
/* 245 */       return true;
/*     */     }
/*     */ 
/*     */     
/* 249 */     this.blocksThisTick = 0;
/* 250 */     this.isSneaking = EntityUtil.stopSneaking(this.isSneaking);
/*     */     
/* 252 */     if (this.retryTimer.passedMs(2000L)) {
/* 253 */       this.retries.clear();
/* 254 */       this.retryTimer.reset();
/*     */     } 
/*     */     
/* 257 */     int targetSlot = -1;
/* 258 */     switch (this.currentMode) {
/*     */       case WEBS:
/* 260 */         this.hasOffhand = InventoryUtil.isBlock(mc.field_71439_g.func_184592_cb().func_77973_b(), BlockWeb.class);
/* 261 */         targetSlot = InventoryUtil.findHotbarBlock(BlockWeb.class);
/*     */         break;
/*     */       case OBSIDIAN:
/* 264 */         this.hasOffhand = InventoryUtil.isBlock(mc.field_71439_g.func_184592_cb().func_77973_b(), BlockObsidian.class);
/* 265 */         targetSlot = InventoryUtil.findHotbarBlock(BlockObsidian.class);
/*     */         break;
/*     */     } 
/*     */ 
/*     */     
/* 270 */     if (((Boolean)this.onlySafe.getValue()).booleanValue() && !EntityUtil.isSafe((Entity)mc.field_71439_g)) {
/* 271 */       disable();
/* 272 */       return true;
/*     */     } 
/*     */     
/* 275 */     if (!this.hasOffhand && targetSlot == -1 && (!((Boolean)this.offhand.getValue()).booleanValue() || (!EntityUtil.isSafe((Entity)mc.field_71439_g) && ((Boolean)this.onlySafe.getValue()).booleanValue()))) {
/* 276 */       return true;
/*     */     }
/*     */     
/* 279 */     if (((Boolean)this.offhand.getValue()).booleanValue() && !this.hasOffhand) {
/* 280 */       return true;
/*     */     }
/*     */     
/* 283 */     return !this.timer.passedMs(((Integer)this.delay.getValue()).intValue());
/*     */   }
/*     */   
/*     */   private boolean switchItem(boolean back) {
/* 287 */     if (((Boolean)this.offhand.getValue()).booleanValue()) {
/* 288 */       return true;
/*     */     }
/* 290 */     boolean[] value = InventoryUtil.switchItem(back, this.lastHotbarSlot, this.switchedItem, (InventoryUtil.Switch)this.switchMode.getValue(), (this.currentMode == Mode.WEBS) ? BlockWeb.class : BlockObsidian.class);
/* 291 */     this.switchedItem = value[0];
/* 292 */     return value[1];
/*     */   }
/*     */   
/*     */   public enum Mode {
/* 296 */     WEBS,
/* 297 */     OBSIDIAN;
/*     */   }
/*     */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\me\earth\phobos\features\modules\combat\Selftrap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */