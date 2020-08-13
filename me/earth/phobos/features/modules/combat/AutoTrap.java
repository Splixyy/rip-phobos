/*     */ package me.earth.phobos.features.modules.combat;
/*     */ 
/*     */ import java.util.Comparator;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import me.earth.phobos.Phobos;
/*     */ import me.earth.phobos.event.events.UpdateWalkingPlayerEvent;
/*     */ import me.earth.phobos.features.command.Command;
/*     */ import me.earth.phobos.features.modules.Module;
/*     */ import me.earth.phobos.features.modules.player.BlockTweaks;
/*     */ import me.earth.phobos.features.setting.Setting;
/*     */ import me.earth.phobos.util.BlockUtil;
/*     */ import me.earth.phobos.util.EntityUtil;
/*     */ import me.earth.phobos.util.InventoryUtil;
/*     */ import me.earth.phobos.util.MathUtil;
/*     */ import me.earth.phobos.util.Timer;
/*     */ import net.minecraft.block.BlockObsidian;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.util.EnumHand;
/*     */ import net.minecraft.util.math.BlockPos;
/*     */ import net.minecraft.util.math.Vec3d;
/*     */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*     */ 
/*     */ public class AutoTrap
/*     */   extends Module
/*     */ {
/*  29 */   private final Setting<Integer> delay = register(new Setting("Delay/Place", Integer.valueOf(50), Integer.valueOf(0), Integer.valueOf(250)));
/*  30 */   private final Setting<Integer> blocksPerPlace = register(new Setting("Block/Place", Integer.valueOf(8), Integer.valueOf(1), Integer.valueOf(30)));
/*  31 */   private final Setting<Double> targetRange = register(new Setting("TargetRange", Double.valueOf(10.0D), Double.valueOf(0.0D), Double.valueOf(20.0D)));
/*  32 */   private final Setting<Double> range = register(new Setting("PlaceRange", Double.valueOf(6.0D), Double.valueOf(0.0D), Double.valueOf(10.0D)));
/*  33 */   private final Setting<TargetMode> targetMode = register(new Setting("Target", TargetMode.CLOSEST));
/*  34 */   private final Setting<InventoryUtil.Switch> switchMode = register(new Setting("Switch", InventoryUtil.Switch.NORMAL));
/*  35 */   private final Setting<Boolean> rotate = register(new Setting("Rotate", Boolean.valueOf(true)));
/*  36 */   private final Setting<Boolean> raytrace = register(new Setting("Raytrace", Boolean.valueOf(false)));
/*  37 */   private final Setting<Pattern> pattern = register(new Setting("Pattern", Pattern.STATIC));
/*  38 */   private final Setting<Integer> extend = register(new Setting("Extend", Integer.valueOf(4), Integer.valueOf(1), Integer.valueOf(4), v -> (this.pattern.getValue() != Pattern.STATIC), "Extending the Trap."));
/*  39 */   private final Setting<Boolean> antiScaffold = register(new Setting("AntiScaffold", Boolean.valueOf(false)));
/*  40 */   private final Setting<Boolean> antiStep = register(new Setting("AntiStep", Boolean.valueOf(false)));
/*  41 */   private final Setting<Boolean> legs = register(new Setting("Legs", Boolean.valueOf(false), v -> (this.pattern.getValue() != Pattern.OPEN)));
/*  42 */   private final Setting<Boolean> platform = register(new Setting("Platform", Boolean.valueOf(false), v -> (this.pattern.getValue() != Pattern.OPEN)));
/*  43 */   private final Setting<Boolean> antiDrop = register(new Setting("AntiDrop", Boolean.valueOf(false)));
/*  44 */   private final Setting<Double> speed = register(new Setting("Speed", Double.valueOf(10.0D), Double.valueOf(0.0D), Double.valueOf(30.0D)));
/*  45 */   private final Setting<Boolean> antiSelf = register(new Setting("AntiSelf", Boolean.valueOf(false)));
/*  46 */   private final Setting<Integer> eventMode = register(new Setting("Updates", Integer.valueOf(3), Integer.valueOf(1), Integer.valueOf(3)));
/*  47 */   private final Setting<Boolean> freecam = register(new Setting("Freecam", Boolean.valueOf(false)));
/*  48 */   private final Setting<Boolean> info = register(new Setting("Info", Boolean.valueOf(false)));
/*  49 */   private final Setting<Boolean> entityCheck = register(new Setting("NoBlock", Boolean.valueOf(true)));
/*  50 */   private final Setting<Boolean> disable = register(new Setting("TSelfMove", Boolean.valueOf(false)));
/*  51 */   private final Setting<Boolean> packet = register(new Setting("Packet", Boolean.valueOf(false)));
/*  52 */   private final Setting<Integer> retryer = register(new Setting("Retries", Integer.valueOf(4), Integer.valueOf(1), Integer.valueOf(15)));
/*     */   
/*  54 */   private final Timer timer = new Timer();
/*     */   private boolean didPlace = false;
/*     */   private boolean switchedItem;
/*     */   public EntityPlayer target;
/*     */   private boolean isSneaking;
/*     */   private int lastHotbarSlot;
/*  60 */   private int placements = 0;
/*     */   public static boolean isPlacing = false;
/*     */   private boolean smartRotate = false;
/*  63 */   private final Map<BlockPos, Integer> retries = new HashMap<>();
/*  64 */   private final Timer retryTimer = new Timer();
/*  65 */   private BlockPos startPos = null;
/*     */   
/*     */   public AutoTrap() {
/*  68 */     super("AutoTrap", "Traps other players", Module.Category.COMBAT, true, false, false);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onEnable() {
/*  73 */     if (fullNullCheck())
/*  74 */       return;  this.startPos = EntityUtil.getRoundedBlockPos((Entity)mc.field_71439_g);
/*  75 */     this.lastHotbarSlot = mc.field_71439_g.field_71071_by.field_70461_c;
/*  76 */     this.retries.clear();
/*     */   }
/*     */ 
/*     */   
/*     */   public void onTick() {
/*  81 */     if (((Integer)this.eventMode.getValue()).intValue() == 3) {
/*  82 */       this.smartRotate = false;
/*  83 */       doTrap();
/*     */     } 
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onUpdateWalkingPlayer(UpdateWalkingPlayerEvent event) {
/*  89 */     if (event.getStage() == 0 && ((Integer)this.eventMode.getValue()).intValue() == 2) {
/*  90 */       this.smartRotate = (((Boolean)this.rotate.getValue()).booleanValue() && ((Integer)this.blocksPerPlace.getValue()).intValue() == 1);
/*  91 */       doTrap();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onUpdate() {
/*  97 */     if (((Integer)this.eventMode.getValue()).intValue() == 1) {
/*  98 */       this.smartRotate = false;
/*  99 */       doTrap();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public String getDisplayInfo() {
/* 105 */     if (((Boolean)this.info.getValue()).booleanValue() && this.target != null) {
/* 106 */       return this.target.func_70005_c_();
/*     */     }
/* 108 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onDisable() {
/* 113 */     isPlacing = false;
/* 114 */     this.isSneaking = EntityUtil.stopSneaking(this.isSneaking);
/* 115 */     switchItem(true);
/*     */   }
/*     */   
/*     */   private void doTrap() {
/* 119 */     if (check()) {
/*     */       return;
/*     */     }
/*     */     
/* 123 */     switch ((Pattern)this.pattern.getValue()) {
/*     */       case STATIC:
/* 125 */         doStaticTrap();
/*     */         break;
/*     */       case SMART:
/*     */       case OPEN:
/* 129 */         doSmartTrap();
/*     */         break;
/*     */     } 
/*     */ 
/*     */     
/* 134 */     if (this.didPlace) {
/* 135 */       this.timer.reset();
/*     */     }
/*     */   }
/*     */   
/*     */   private void doSmartTrap() {
/* 140 */     List<Vec3d> placeTargets = EntityUtil.getUntrappedBlocksExtended(((Integer)this.extend.getValue()).intValue(), this.target, ((Boolean)this.antiScaffold.getValue()).booleanValue(), ((Boolean)this.antiStep.getValue()).booleanValue(), ((Boolean)this.legs.getValue()).booleanValue(), ((Boolean)this.platform.getValue()).booleanValue(), ((Boolean)this.antiDrop.getValue()).booleanValue(), ((Boolean)this.raytrace.getValue()).booleanValue());
/* 141 */     placeList(placeTargets);
/*     */   }
/*     */   
/*     */   private void doStaticTrap() {
/* 145 */     List<Vec3d> placeTargets = EntityUtil.targets(this.target.func_174791_d(), ((Boolean)this.antiScaffold.getValue()).booleanValue(), ((Boolean)this.antiStep.getValue()).booleanValue(), ((Boolean)this.legs.getValue()).booleanValue(), ((Boolean)this.platform.getValue()).booleanValue(), ((Boolean)this.antiDrop.getValue()).booleanValue(), ((Boolean)this.raytrace.getValue()).booleanValue());
/* 146 */     placeList(placeTargets);
/*     */   }
/*     */   
/*     */   private void placeList(List<Vec3d> list) {
/* 150 */     list.sort((vec3d, vec3d2) -> Double.compare(mc.field_71439_g.func_70092_e(vec3d2.field_72450_a, vec3d2.field_72448_b, vec3d2.field_72449_c), mc.field_71439_g.func_70092_e(vec3d.field_72450_a, vec3d.field_72448_b, vec3d.field_72449_c)));
/* 151 */     list.sort(Comparator.comparingDouble(vec3d -> vec3d.field_72448_b));
/*     */     
/* 153 */     for (Vec3d vec3d : list) {
/* 154 */       BlockPos position = new BlockPos(vec3d);
/* 155 */       int placeability = BlockUtil.isPositionPlaceable(position, ((Boolean)this.raytrace.getValue()).booleanValue());
/* 156 */       if (((Boolean)this.entityCheck.getValue()).booleanValue() && placeability == 1 && (this.switchMode.getValue() == InventoryUtil.Switch.SILENT || (BlockTweaks.getINSTANCE().isOn() && ((Boolean)(BlockTweaks.getINSTANCE()).noBlock.getValue()).booleanValue())) && (this.retries.get(position) == null || ((Integer)this.retries.get(position)).intValue() < ((Integer)this.retryer.getValue()).intValue())) {
/* 157 */         placeBlock(position);
/* 158 */         this.retries.put(position, Integer.valueOf((this.retries.get(position) == null) ? 1 : (((Integer)this.retries.get(position)).intValue() + 1)));
/* 159 */         this.retryTimer.reset();
/*     */         
/*     */         continue;
/*     */       } 
/* 163 */       if (placeability == 3 && (!((Boolean)this.antiSelf.getValue()).booleanValue() || !MathUtil.areVec3dsAligned(mc.field_71439_g.func_174791_d(), vec3d))) {
/* 164 */         placeBlock(position);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean check() {
/* 170 */     isPlacing = false;
/* 171 */     this.didPlace = false;
/* 172 */     this.placements = 0;
/* 173 */     int obbySlot = InventoryUtil.findHotbarBlock(BlockObsidian.class);
/*     */     
/* 175 */     if (isOff()) {
/* 176 */       return true;
/*     */     }
/*     */     
/* 179 */     if (((Boolean)this.disable.getValue()).booleanValue() && !this.startPos.equals(EntityUtil.getRoundedBlockPos((Entity)mc.field_71439_g))) {
/* 180 */       disable();
/* 181 */       return true;
/*     */     } 
/*     */     
/* 184 */     if (this.retryTimer.passedMs(2000L)) {
/* 185 */       this.retries.clear();
/* 186 */       this.retryTimer.reset();
/*     */     } 
/*     */     
/* 189 */     if (obbySlot == -1) {
/* 190 */       if (this.switchMode.getValue() != InventoryUtil.Switch.NONE) {
/* 191 */         if (((Boolean)this.info.getValue()).booleanValue()) {
/* 192 */           Command.sendMessage("<" + getDisplayName() + "> " + "§c" + "You are out of Obsidian.");
/*     */         }
/* 194 */         disable();
/*     */       } 
/* 196 */       return true;
/*     */     } 
/*     */     
/* 199 */     if (mc.field_71439_g.field_71071_by.field_70461_c != this.lastHotbarSlot && mc.field_71439_g.field_71071_by.field_70461_c != obbySlot) {
/* 200 */       this.lastHotbarSlot = mc.field_71439_g.field_71071_by.field_70461_c;
/*     */     }
/*     */     
/* 203 */     switchItem(true);
/* 204 */     this.isSneaking = EntityUtil.stopSneaking(this.isSneaking);
/* 205 */     this.target = getTarget(((Double)this.targetRange.getValue()).doubleValue(), (this.targetMode.getValue() == TargetMode.UNTRAPPED));
/*     */     
/* 207 */     return (this.target == null || (Phobos.moduleManager.isModuleEnabled("Freecam") && !((Boolean)this.freecam.getValue()).booleanValue()) || !this.timer.passedMs(((Integer)this.delay.getValue()).intValue()) || (this.switchMode.getValue() == InventoryUtil.Switch.NONE && mc.field_71439_g.field_71071_by.field_70461_c != InventoryUtil.findHotbarBlock(BlockObsidian.class)));
/*     */   }
/*     */   
/*     */   private EntityPlayer getTarget(double range, boolean trapped) {
/* 211 */     EntityPlayer target = null;
/* 212 */     double distance = Math.pow(range, 2.0D) + 1.0D;
/* 213 */     for (EntityPlayer player : mc.field_71441_e.field_73010_i) {
/* 214 */       if (EntityUtil.isntValid((Entity)player, range)) {
/*     */         continue;
/*     */       }
/*     */       
/* 218 */       if (this.pattern.getValue() == Pattern.STATIC && trapped && EntityUtil.isTrapped(player, ((Boolean)this.antiScaffold.getValue()).booleanValue(), ((Boolean)this.antiStep.getValue()).booleanValue(), ((Boolean)this.legs.getValue()).booleanValue(), ((Boolean)this.platform.getValue()).booleanValue(), ((Boolean)this.antiDrop.getValue()).booleanValue())) {
/*     */         continue;
/*     */       }
/*     */       
/* 222 */       if (this.pattern.getValue() != Pattern.STATIC && trapped && EntityUtil.isTrappedExtended(((Integer)this.extend.getValue()).intValue(), player, ((Boolean)this.antiScaffold.getValue()).booleanValue(), ((Boolean)this.antiStep.getValue()).booleanValue(), ((Boolean)this.legs.getValue()).booleanValue(), ((Boolean)this.platform.getValue()).booleanValue(), ((Boolean)this.antiDrop.getValue()).booleanValue(), ((Boolean)this.raytrace.getValue()).booleanValue())) {
/*     */         continue;
/*     */       }
/*     */       
/* 226 */       if (EntityUtil.getRoundedBlockPos((Entity)mc.field_71439_g).equals(EntityUtil.getRoundedBlockPos((Entity)player)) && ((Boolean)this.antiSelf.getValue()).booleanValue()) {
/*     */         continue;
/*     */       }
/*     */       
/* 230 */       if (Phobos.speedManager.getPlayerSpeed(player) > ((Double)this.speed.getValue()).doubleValue()) {
/*     */         continue;
/*     */       }
/*     */       
/* 234 */       if (target == null) {
/* 235 */         target = player;
/* 236 */         distance = mc.field_71439_g.func_70068_e((Entity)player);
/*     */         
/*     */         continue;
/*     */       } 
/* 240 */       if (mc.field_71439_g.func_70068_e((Entity)player) < distance) {
/* 241 */         target = player;
/* 242 */         distance = mc.field_71439_g.func_70068_e((Entity)player);
/*     */       } 
/*     */     } 
/* 245 */     return target;
/*     */   }
/*     */   
/*     */   private void placeBlock(BlockPos pos) {
/* 249 */     if (this.placements < ((Integer)this.blocksPerPlace.getValue()).intValue() && mc.field_71439_g.func_174818_b(pos) <= MathUtil.square(((Double)this.range.getValue()).doubleValue()) && switchItem(false)) {
/* 250 */       isPlacing = true;
/* 251 */       if (this.smartRotate) {
/* 252 */         this.isSneaking = BlockUtil.placeBlockSmartRotate(pos, EnumHand.MAIN_HAND, true, ((Boolean)this.packet.getValue()).booleanValue(), this.isSneaking);
/*     */       } else {
/* 254 */         this.isSneaking = BlockUtil.placeBlock(pos, EnumHand.MAIN_HAND, ((Boolean)this.rotate.getValue()).booleanValue(), ((Boolean)this.packet.getValue()).booleanValue(), this.isSneaking);
/*     */       } 
/* 256 */       this.didPlace = true;
/* 257 */       this.placements++;
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean switchItem(boolean back) {
/* 262 */     boolean[] value = InventoryUtil.switchItem(back, this.lastHotbarSlot, this.switchedItem, (InventoryUtil.Switch)this.switchMode.getValue(), BlockObsidian.class);
/* 263 */     this.switchedItem = value[0];
/* 264 */     return value[1];
/*     */   }
/*     */   
/*     */   public enum Pattern {
/* 268 */     STATIC,
/* 269 */     SMART,
/* 270 */     OPEN;
/*     */   }
/*     */   
/*     */   public enum TargetMode {
/* 274 */     CLOSEST,
/* 275 */     UNTRAPPED;
/*     */   }
/*     */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\me\earth\phobos\features\modules\combat\AutoTrap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */