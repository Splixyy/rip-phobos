/*     */ package me.earth.phobos.features.modules.combat;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import me.earth.phobos.Phobos;
/*     */ import me.earth.phobos.event.events.UpdateWalkingPlayerEvent;
/*     */ import me.earth.phobos.features.command.Command;
/*     */ import me.earth.phobos.features.modules.Module;
/*     */ import me.earth.phobos.features.modules.player.BlockTweaks;
/*     */ import me.earth.phobos.features.setting.Setting;
/*     */ import me.earth.phobos.util.BlockUtil;
/*     */ import me.earth.phobos.util.EntityUtil;
/*     */ import me.earth.phobos.util.InventoryUtil;
/*     */ import me.earth.phobos.util.Timer;
/*     */ import net.minecraft.block.BlockEnderChest;
/*     */ import net.minecraft.block.BlockObsidian;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.util.EnumHand;
/*     */ import net.minecraft.util.math.BlockPos;
/*     */ import net.minecraft.util.math.Vec3d;
/*     */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*     */ 
/*     */ public class Surround
/*     */   extends Module
/*     */ {
/*  28 */   private final Setting<Integer> delay = register(new Setting("Delay/Place", Integer.valueOf(50), Integer.valueOf(0), Integer.valueOf(250)));
/*  29 */   private final Setting<Integer> blocksPerTick = register(new Setting("Block/Place", Integer.valueOf(8), Integer.valueOf(1), Integer.valueOf(20)));
/*  30 */   private final Setting<Boolean> rotate = register(new Setting("Rotate", Boolean.valueOf(true)));
/*  31 */   private final Setting<Boolean> raytrace = register(new Setting("Raytrace", Boolean.valueOf(false)));
/*  32 */   private final Setting<InventoryUtil.Switch> switchMode = register(new Setting("Switch", InventoryUtil.Switch.NORMAL));
/*  33 */   private final Setting<Boolean> center = register(new Setting("Center", Boolean.valueOf(false)));
/*  34 */   private final Setting<Boolean> helpingBlocks = register(new Setting("HelpingBlocks", Boolean.valueOf(true)));
/*  35 */   private final Setting<Boolean> intelligent = register(new Setting("Intelligent", Boolean.valueOf(false), v -> ((Boolean)this.helpingBlocks.getValue()).booleanValue()));
/*  36 */   private final Setting<Boolean> antiPedo = register(new Setting("NoPedo", Boolean.valueOf(false)));
/*  37 */   private final Setting<Integer> extender = register(new Setting("Extend", Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(4)));
/*  38 */   private final Setting<Boolean> extendMove = register(new Setting("MoveExtend", Boolean.valueOf(false), v -> (((Integer)this.extender.getValue()).intValue() > 1)));
/*  39 */   private final Setting<MovementMode> movementMode = register(new Setting("Movement", MovementMode.STATIC));
/*  40 */   private final Setting<Double> speed = register(new Setting("Speed", Double.valueOf(10.0D), Double.valueOf(0.0D), Double.valueOf(30.0D), v -> (this.movementMode.getValue() == MovementMode.LIMIT || this.movementMode.getValue() == MovementMode.OFF), "Maximum Movement Speed"));
/*  41 */   private final Setting<Integer> eventMode = register(new Setting("Updates", Integer.valueOf(3), Integer.valueOf(1), Integer.valueOf(3)));
/*  42 */   private final Setting<Boolean> floor = register(new Setting("Floor", Boolean.valueOf(false)));
/*  43 */   private final Setting<Boolean> echests = register(new Setting("Echests", Boolean.valueOf(false)));
/*  44 */   private final Setting<Boolean> noGhost = register(new Setting("Packet", Boolean.valueOf(false)));
/*  45 */   private final Setting<Boolean> info = register(new Setting("Info", Boolean.valueOf(false)));
/*  46 */   private final Setting<Integer> retryer = register(new Setting("Retries", Integer.valueOf(4), Integer.valueOf(1), Integer.valueOf(15)));
/*     */   
/*  48 */   private final Timer timer = new Timer();
/*  49 */   private final Timer retryTimer = new Timer();
/*     */   private int isSafe;
/*     */   private BlockPos startPos;
/*     */   private boolean didPlace = false;
/*     */   private boolean switchedItem;
/*     */   private int lastHotbarSlot;
/*     */   private boolean isSneaking;
/*  56 */   private int placements = 0;
/*  57 */   private final Set<Vec3d> extendingBlocks = new HashSet<>();
/*  58 */   private int extenders = 1;
/*     */   public static boolean isPlacing = false;
/*  60 */   private int obbySlot = -1;
/*     */   private boolean offHand = false;
/*  62 */   private final Map<BlockPos, Integer> retries = new HashMap<>();
/*     */   
/*     */   public Surround() {
/*  65 */     super("Surround", "Surrounds you with Obsidian", Module.Category.COMBAT, true, false, false);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onEnable() {
/*  70 */     if (fullNullCheck()) {
/*  71 */       disable();
/*     */     }
/*  73 */     this.lastHotbarSlot = mc.field_71439_g.field_71071_by.field_70461_c;
/*  74 */     this.startPos = EntityUtil.getRoundedBlockPos((Entity)mc.field_71439_g);
/*  75 */     if (((Boolean)this.center.getValue()).booleanValue() && !Phobos.moduleManager.isModuleEnabled("Freecam")) {
/*  76 */       Phobos.positionManager.setPositionPacket(this.startPos.func_177958_n() + 0.5D, this.startPos.func_177956_o(), this.startPos.func_177952_p() + 0.5D, true, true, true);
/*     */     }
/*  78 */     this.retries.clear();
/*  79 */     this.retryTimer.reset();
/*     */   }
/*     */ 
/*     */   
/*     */   public void onTick() {
/*  84 */     if (((Integer)this.eventMode.getValue()).intValue() == 3) {
/*  85 */       doFeetPlace();
/*     */     }
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onUpdateWalkingPlayer(UpdateWalkingPlayerEvent event) {
/*  91 */     if (event.getStage() == 0 && ((Integer)this.eventMode.getValue()).intValue() == 2) {
/*  92 */       doFeetPlace();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void onUpdate() {
/*  98 */     if (((Integer)this.eventMode.getValue()).intValue() == 1) {
/*  99 */       doFeetPlace();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void onDisable() {
/* 105 */     if (nullCheck()) {
/*     */       return;
/*     */     }
/* 108 */     isPlacing = false;
/* 109 */     this.isSneaking = EntityUtil.stopSneaking(this.isSneaking);
/* 110 */     switchItem(true);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getDisplayInfo() {
/* 115 */     if (!((Boolean)this.info.getValue()).booleanValue()) {
/* 116 */       return null;
/*     */     }
/*     */     
/* 119 */     switch (this.isSafe) {
/*     */       case 0:
/* 121 */         return "§cUnsafe";
/*     */       case 1:
/* 123 */         return "§eSecure";
/*     */     } 
/* 125 */     return "§aSecure";
/*     */   }
/*     */ 
/*     */   
/*     */   private void doFeetPlace() {
/* 130 */     if (check()) {
/*     */       return;
/*     */     }
/*     */     
/* 134 */     if (!EntityUtil.isSafe((Entity)mc.field_71439_g, 0, ((Boolean)this.floor.getValue()).booleanValue())) {
/* 135 */       this.isSafe = 0;
/* 136 */       placeBlocks(mc.field_71439_g.func_174791_d(), EntityUtil.getUnsafeBlockArray((Entity)mc.field_71439_g, 0, ((Boolean)this.floor.getValue()).booleanValue()), ((Boolean)this.helpingBlocks.getValue()).booleanValue(), false, false);
/* 137 */     } else if (!EntityUtil.isSafe((Entity)mc.field_71439_g, -1, false)) {
/* 138 */       this.isSafe = 1;
/* 139 */       if (((Boolean)this.antiPedo.getValue()).booleanValue()) {
/* 140 */         placeBlocks(mc.field_71439_g.func_174791_d(), EntityUtil.getUnsafeBlockArray((Entity)mc.field_71439_g, -1, false), false, false, true);
/*     */       }
/*     */     } else {
/* 143 */       this.isSafe = 2;
/*     */     } 
/*     */     
/* 146 */     processExtendingBlocks();
/*     */     
/* 148 */     if (this.didPlace) {
/* 149 */       this.timer.reset();
/*     */     }
/*     */   }
/*     */   
/*     */   private void processExtendingBlocks() {
/* 154 */     if (this.extendingBlocks.size() == 2 && this.extenders < ((Integer)this.extender.getValue()).intValue()) {
/* 155 */       Vec3d[] array = new Vec3d[2];
/* 156 */       int i = 0;
/* 157 */       for (Vec3d vec3d : this.extendingBlocks) {
/* 158 */         array[i] = vec3d;
/* 159 */         i++;
/*     */       } 
/* 161 */       int placementsBefore = this.placements;
/* 162 */       if (areClose(array) != null) {
/* 163 */         placeBlocks(areClose(array), EntityUtil.getUnsafeBlockArrayFromVec3d(areClose(array), 0, ((Boolean)this.floor.getValue()).booleanValue()), ((Boolean)this.helpingBlocks.getValue()).booleanValue(), false, true);
/*     */       }
/*     */       
/* 166 */       if (placementsBefore < this.placements) {
/* 167 */         this.extendingBlocks.clear();
/*     */       }
/* 169 */     } else if (this.extendingBlocks.size() > 2 || this.extenders >= ((Integer)this.extender.getValue()).intValue()) {
/* 170 */       this.extendingBlocks.clear();
/*     */     } 
/*     */   }
/*     */   
/*     */   private Vec3d areClose(Vec3d[] vec3ds) {
/* 175 */     int matches = 0;
/* 176 */     for (Vec3d vec3d : vec3ds) {
/* 177 */       for (Vec3d pos : EntityUtil.getUnsafeBlockArray((Entity)mc.field_71439_g, 0, ((Boolean)this.floor.getValue()).booleanValue())) {
/* 178 */         if (vec3d.equals(pos)) {
/* 179 */           matches++;
/*     */         }
/*     */       } 
/*     */     } 
/* 183 */     if (matches == 2) {
/* 184 */       return mc.field_71439_g.func_174791_d().func_178787_e(vec3ds[0].func_178787_e(vec3ds[1]));
/*     */     }
/* 186 */     return null;
/*     */   }
/*     */   
/*     */   private boolean placeBlocks(Vec3d pos, Vec3d[] vec3ds, boolean hasHelpingBlocks, boolean isHelping, boolean isExtending) {
/* 190 */     int helpings = 0;
/* 191 */     boolean gotHelp = true;
/* 192 */     for (Vec3d vec3d : vec3ds) {
/* 193 */       gotHelp = true;
/* 194 */       helpings++;
/* 195 */       if (isHelping && !((Boolean)this.intelligent.getValue()).booleanValue() && helpings > 1) {
/* 196 */         return false;
/*     */       }
/* 198 */       BlockPos position = (new BlockPos(pos)).func_177963_a(vec3d.field_72450_a, vec3d.field_72448_b, vec3d.field_72449_c);
/* 199 */       switch (BlockUtil.isPositionPlaceable(position, ((Boolean)this.raytrace.getValue()).booleanValue())) {
/*     */ 
/*     */         
/*     */         case 1:
/* 203 */           if ((this.switchMode.getValue() == InventoryUtil.Switch.SILENT || (BlockTweaks.getINSTANCE().isOn() && ((Boolean)(BlockTweaks.getINSTANCE()).noBlock.getValue()).booleanValue())) && (this.retries.get(position) == null || ((Integer)this.retries.get(position)).intValue() < ((Integer)this.retryer.getValue()).intValue())) {
/* 204 */             placeBlock(position);
/* 205 */             this.retries.put(position, Integer.valueOf((this.retries.get(position) == null) ? 1 : (((Integer)this.retries.get(position)).intValue() + 1)));
/* 206 */             this.retryTimer.reset();
/*     */             
/*     */             break;
/*     */           } 
/* 210 */           if ((((Boolean)this.extendMove.getValue()).booleanValue() || Phobos.speedManager.getSpeedKpH() == 0.0D) && !isExtending && this.extenders < ((Integer)this.extender.getValue()).intValue()) {
/* 211 */             placeBlocks(mc.field_71439_g.func_174791_d().func_178787_e(vec3d), EntityUtil.getUnsafeBlockArrayFromVec3d(mc.field_71439_g.func_174791_d().func_178787_e(vec3d), 0, ((Boolean)this.floor.getValue()).booleanValue()), hasHelpingBlocks, false, true);
/* 212 */             this.extendingBlocks.add(vec3d);
/* 213 */             this.extenders++;
/*     */           } 
/*     */           break;
/*     */         case 2:
/* 217 */           if (hasHelpingBlocks) {
/* 218 */             gotHelp = placeBlocks(pos, BlockUtil.getHelpingBlocks(vec3d), false, true, true);
/*     */           } else {
/*     */             break;
/*     */           } 
/*     */         case 3:
/* 223 */           if (gotHelp) {
/* 224 */             placeBlock(position);
/*     */           }
/* 226 */           if (isHelping)
/* 227 */             return true; 
/*     */           break;
/*     */       } 
/*     */     } 
/* 231 */     return false;
/*     */   }
/*     */   
/*     */   private boolean check() {
/* 235 */     if (nullCheck()) {
/* 236 */       return true;
/*     */     }
/*     */     
/* 239 */     this.offHand = InventoryUtil.isBlock(mc.field_71439_g.func_184592_cb().func_77973_b(), BlockObsidian.class);
/* 240 */     isPlacing = false;
/* 241 */     this.didPlace = false;
/* 242 */     this.extenders = 1;
/* 243 */     this.placements = 0;
/* 244 */     this.obbySlot = InventoryUtil.findHotbarBlock(BlockObsidian.class);
/* 245 */     int echestSlot = InventoryUtil.findHotbarBlock(BlockEnderChest.class);
/*     */     
/* 247 */     if (isOff()) {
/* 248 */       return true;
/*     */     }
/*     */     
/* 251 */     if (this.retryTimer.passedMs(2500L)) {
/* 252 */       this.retries.clear();
/* 253 */       this.retryTimer.reset();
/*     */     } 
/*     */     
/* 256 */     switchItem(true);
/*     */     
/* 258 */     if (this.obbySlot == -1 && !this.offHand && (
/* 259 */       !((Boolean)this.echests.getValue()).booleanValue() || echestSlot == -1)) {
/* 260 */       if (((Boolean)this.info.getValue()).booleanValue()) {
/* 261 */         Command.sendMessage("<" + getDisplayName() + "> " + "§c" + "You are out of Obsidian.");
/*     */       }
/* 263 */       disable();
/* 264 */       return true;
/*     */     } 
/*     */ 
/*     */     
/* 268 */     this.isSneaking = EntityUtil.stopSneaking(this.isSneaking);
/*     */     
/* 270 */     if (mc.field_71439_g.field_71071_by.field_70461_c != this.lastHotbarSlot && mc.field_71439_g.field_71071_by.field_70461_c != this.obbySlot && mc.field_71439_g.field_71071_by.field_70461_c != echestSlot) {
/* 271 */       this.lastHotbarSlot = mc.field_71439_g.field_71071_by.field_70461_c;
/*     */     }
/*     */     
/* 274 */     switch ((MovementMode)this.movementMode.getValue()) {
/*     */ 
/*     */       
/*     */       case STATIC:
/* 278 */         if (!this.startPos.equals(EntityUtil.getRoundedBlockPos((Entity)mc.field_71439_g))) {
/* 279 */           disable();
/* 280 */           return true;
/*     */         } 
/*     */       case LIMIT:
/* 283 */         if (Phobos.speedManager.getSpeedKpH() > ((Double)this.speed.getValue()).doubleValue()) {
/* 284 */           return true;
/*     */         }
/*     */         break;
/*     */       case OFF:
/* 288 */         if (Phobos.speedManager.getSpeedKpH() > ((Double)this.speed.getValue()).doubleValue()) {
/* 289 */           disable();
/* 290 */           return true;
/*     */         }  break;
/*     */     } 
/* 293 */     return (Phobos.moduleManager.isModuleEnabled("Freecam") || !this.timer.passedMs(((Integer)this.delay.getValue()).intValue()) || (this.switchMode.getValue() == InventoryUtil.Switch.NONE && mc.field_71439_g.field_71071_by.field_70461_c != InventoryUtil.findHotbarBlock(BlockObsidian.class)));
/*     */   }
/*     */   
/*     */   private void placeBlock(BlockPos pos) {
/* 297 */     if (this.placements < ((Integer)this.blocksPerTick.getValue()).intValue() && switchItem(false)) {
/* 298 */       isPlacing = true;
/* 299 */       this.isSneaking = BlockUtil.placeBlock(pos, this.offHand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, ((Boolean)this.rotate.getValue()).booleanValue(), ((Boolean)this.noGhost.getValue()).booleanValue(), this.isSneaking);
/* 300 */       this.didPlace = true;
/* 301 */       this.placements++;
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean switchItem(boolean back) {
/* 306 */     if (this.offHand) {
/* 307 */       return true;
/*     */     }
/* 309 */     boolean[] value = InventoryUtil.switchItem(back, this.lastHotbarSlot, this.switchedItem, (InventoryUtil.Switch)this.switchMode.getValue(), (this.obbySlot == -1) ? BlockEnderChest.class : BlockObsidian.class);
/* 310 */     this.switchedItem = value[0];
/* 311 */     return value[1];
/*     */   }
/*     */   
/*     */   public enum MovementMode {
/* 315 */     NONE,
/* 316 */     STATIC,
/* 317 */     LIMIT,
/* 318 */     OFF;
/*     */   }
/*     */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\me\earth\phobos\features\modules\combat\Surround.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */