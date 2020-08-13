/*     */ package me.earth.phobos.features.modules.combat;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import me.earth.phobos.Phobos;
/*     */ import me.earth.phobos.event.events.UpdateWalkingPlayerEvent;
/*     */ import me.earth.phobos.features.command.Command;
/*     */ import me.earth.phobos.features.modules.Module;
/*     */ import me.earth.phobos.features.setting.Setting;
/*     */ import me.earth.phobos.util.BlockUtil;
/*     */ import me.earth.phobos.util.EntityUtil;
/*     */ import me.earth.phobos.util.InventoryUtil;
/*     */ import me.earth.phobos.util.MathUtil;
/*     */ import me.earth.phobos.util.Timer;
/*     */ import net.minecraft.block.BlockWeb;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.util.EnumHand;
/*     */ import net.minecraft.util.math.BlockPos;
/*     */ import net.minecraft.util.math.Vec3d;
/*     */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*     */ 
/*     */ 
/*     */ public class Webaura
/*     */   extends Module
/*     */ {
/*  28 */   private final Setting<Integer> delay = register(new Setting("Delay/Place", Integer.valueOf(50), Integer.valueOf(0), Integer.valueOf(250)));
/*  29 */   private final Setting<Integer> blocksPerPlace = register(new Setting("Block/Place", Integer.valueOf(8), Integer.valueOf(1), Integer.valueOf(30)));
/*  30 */   private final Setting<Double> targetRange = register(new Setting("TargetRange", Double.valueOf(10.0D), Double.valueOf(0.0D), Double.valueOf(20.0D)));
/*  31 */   private final Setting<Double> range = register(new Setting("PlaceRange", Double.valueOf(6.0D), Double.valueOf(0.0D), Double.valueOf(10.0D)));
/*  32 */   private final Setting<TargetMode> targetMode = register(new Setting("Target", TargetMode.CLOSEST));
/*  33 */   private final Setting<InventoryUtil.Switch> switchMode = register(new Setting("Switch", InventoryUtil.Switch.NORMAL));
/*  34 */   private final Setting<Boolean> rotate = register(new Setting("Rotate", Boolean.valueOf(true)));
/*  35 */   private final Setting<Boolean> raytrace = register(new Setting("Raytrace", Boolean.valueOf(false)));
/*  36 */   private final Setting<Double> speed = register(new Setting("Speed", Double.valueOf(30.0D), Double.valueOf(0.0D), Double.valueOf(30.0D)));
/*  37 */   private final Setting<Boolean> upperBody = register(new Setting("Upper", Boolean.valueOf(false)));
/*  38 */   private final Setting<Boolean> lowerbody = register(new Setting("Lower", Boolean.valueOf(true)));
/*  39 */   private final Setting<Boolean> ylower = register(new Setting("Y-1", Boolean.valueOf(false)));
/*  40 */   private final Setting<Boolean> antiSelf = register(new Setting("AntiSelf", Boolean.valueOf(false)));
/*  41 */   private final Setting<Integer> eventMode = register(new Setting("Updates", Integer.valueOf(3), Integer.valueOf(1), Integer.valueOf(3)));
/*  42 */   private final Setting<Boolean> freecam = register(new Setting("Freecam", Boolean.valueOf(false)));
/*  43 */   private final Setting<Boolean> info = register(new Setting("Info", Boolean.valueOf(false)));
/*  44 */   private final Setting<Boolean> disable = register(new Setting("TSelfMove", Boolean.valueOf(false)));
/*  45 */   private final Setting<Boolean> packet = register(new Setting("Packet", Boolean.valueOf(false)));
/*     */   
/*  47 */   private final Timer timer = new Timer();
/*     */   private boolean didPlace = false;
/*     */   private boolean switchedItem;
/*     */   public EntityPlayer target;
/*     */   private boolean isSneaking;
/*     */   private int lastHotbarSlot;
/*  53 */   private int placements = 0;
/*     */   public static boolean isPlacing = false;
/*     */   private boolean smartRotate = false;
/*  56 */   private BlockPos startPos = null;
/*     */   
/*     */   public Webaura() {
/*  59 */     super("Webaura", "Traps other players in webs", Module.Category.COMBAT, true, false, false);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onEnable() {
/*  64 */     if (fullNullCheck())
/*  65 */       return;  this.startPos = EntityUtil.getRoundedBlockPos((Entity)mc.field_71439_g);
/*  66 */     this.lastHotbarSlot = mc.field_71439_g.field_71071_by.field_70461_c;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onTick() {
/*  71 */     if (((Integer)this.eventMode.getValue()).intValue() == 3) {
/*  72 */       this.smartRotate = false;
/*  73 */       doTrap();
/*     */     } 
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onUpdateWalkingPlayer(UpdateWalkingPlayerEvent event) {
/*  79 */     if (event.getStage() == 0 && ((Integer)this.eventMode.getValue()).intValue() == 2) {
/*  80 */       this.smartRotate = (((Boolean)this.rotate.getValue()).booleanValue() && ((Integer)this.blocksPerPlace.getValue()).intValue() == 1);
/*  81 */       doTrap();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onUpdate() {
/*  87 */     if (((Integer)this.eventMode.getValue()).intValue() == 1) {
/*  88 */       this.smartRotate = false;
/*  89 */       doTrap();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public String getDisplayInfo() {
/*  95 */     if (((Boolean)this.info.getValue()).booleanValue() && this.target != null) {
/*  96 */       return this.target.func_70005_c_();
/*     */     }
/*  98 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onDisable() {
/* 103 */     isPlacing = false;
/* 104 */     this.isSneaking = EntityUtil.stopSneaking(this.isSneaking);
/* 105 */     switchItem(true);
/*     */   }
/*     */   
/*     */   private void doTrap() {
/* 109 */     if (check()) {
/*     */       return;
/*     */     }
/*     */     
/* 113 */     doWebTrap();
/*     */     
/* 115 */     if (this.didPlace) {
/* 116 */       this.timer.reset();
/*     */     }
/*     */   }
/*     */   
/*     */   private void doWebTrap() {
/* 121 */     List<Vec3d> placeTargets = getPlacements();
/* 122 */     placeList(placeTargets);
/*     */   }
/*     */   
/*     */   private List<Vec3d> getPlacements() {
/* 126 */     List<Vec3d> list = new ArrayList<>();
/* 127 */     Vec3d baseVec = this.target.func_174791_d();
/* 128 */     if (((Boolean)this.ylower.getValue()).booleanValue()) {
/* 129 */       list.add(baseVec.func_72441_c(0.0D, -1.0D, 0.0D));
/*     */     }
/*     */     
/* 132 */     if (((Boolean)this.lowerbody.getValue()).booleanValue()) {
/* 133 */       list.add(baseVec);
/*     */     }
/*     */     
/* 136 */     if (((Boolean)this.upperBody.getValue()).booleanValue()) {
/* 137 */       list.add(baseVec.func_72441_c(0.0D, 1.0D, 0.0D));
/*     */     }
/*     */     
/* 140 */     return list;
/*     */   }
/*     */   
/*     */   private void placeList(List<Vec3d> list) {
/* 144 */     list.sort((vec3d, vec3d2) -> Double.compare(mc.field_71439_g.func_70092_e(vec3d2.field_72450_a, vec3d2.field_72448_b, vec3d2.field_72449_c), mc.field_71439_g.func_70092_e(vec3d.field_72450_a, vec3d.field_72448_b, vec3d.field_72449_c)));
/* 145 */     list.sort(Comparator.comparingDouble(vec3d -> vec3d.field_72448_b));
/*     */     
/* 147 */     for (Vec3d vec3d : list) {
/* 148 */       BlockPos position = new BlockPos(vec3d);
/* 149 */       int placeability = BlockUtil.isPositionPlaceable(position, ((Boolean)this.raytrace.getValue()).booleanValue());
/* 150 */       if ((placeability == 3 || placeability == 1) && (!((Boolean)this.antiSelf.getValue()).booleanValue() || !MathUtil.areVec3dsAligned(mc.field_71439_g.func_174791_d(), vec3d))) {
/* 151 */         placeBlock(position);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean check() {
/* 157 */     isPlacing = false;
/* 158 */     this.didPlace = false;
/* 159 */     this.placements = 0;
/* 160 */     int obbySlot = InventoryUtil.findHotbarBlock(BlockWeb.class);
/*     */     
/* 162 */     if (isOff()) {
/* 163 */       return true;
/*     */     }
/*     */     
/* 166 */     if (((Boolean)this.disable.getValue()).booleanValue() && !this.startPos.equals(EntityUtil.getRoundedBlockPos((Entity)mc.field_71439_g))) {
/* 167 */       disable();
/* 168 */       return true;
/*     */     } 
/*     */     
/* 171 */     if (obbySlot == -1) {
/* 172 */       if (this.switchMode.getValue() != InventoryUtil.Switch.NONE) {
/* 173 */         if (((Boolean)this.info.getValue()).booleanValue()) {
/* 174 */           Command.sendMessage("<" + getDisplayName() + "> " + "§c" + "You are out of Obsidian.");
/*     */         }
/* 176 */         disable();
/*     */       } 
/* 178 */       return true;
/*     */     } 
/*     */     
/* 181 */     if (mc.field_71439_g.field_71071_by.field_70461_c != this.lastHotbarSlot && mc.field_71439_g.field_71071_by.field_70461_c != obbySlot) {
/* 182 */       this.lastHotbarSlot = mc.field_71439_g.field_71071_by.field_70461_c;
/*     */     }
/*     */     
/* 185 */     switchItem(true);
/* 186 */     this.isSneaking = EntityUtil.stopSneaking(this.isSneaking);
/* 187 */     this.target = getTarget(((Double)this.targetRange.getValue()).doubleValue(), (this.targetMode.getValue() == TargetMode.UNTRAPPED));
/*     */     
/* 189 */     return (this.target == null || (Phobos.moduleManager.isModuleEnabled("Freecam") && !((Boolean)this.freecam.getValue()).booleanValue()) || !this.timer.passedMs(((Integer)this.delay.getValue()).intValue()) || (this.switchMode.getValue() == InventoryUtil.Switch.NONE && mc.field_71439_g.field_71071_by.field_70461_c != InventoryUtil.findHotbarBlock(BlockWeb.class)));
/*     */   }
/*     */   
/*     */   private EntityPlayer getTarget(double range, boolean trapped) {
/* 193 */     EntityPlayer target = null;
/* 194 */     double distance = Math.pow(range, 2.0D) + 1.0D;
/* 195 */     for (EntityPlayer player : mc.field_71441_e.field_73010_i) {
/* 196 */       if (EntityUtil.isntValid((Entity)player, range)) {
/*     */         continue;
/*     */       }
/*     */       
/* 200 */       if (trapped && player.field_70134_J) {
/*     */         continue;
/*     */       }
/*     */       
/* 204 */       if (EntityUtil.getRoundedBlockPos((Entity)mc.field_71439_g).equals(EntityUtil.getRoundedBlockPos((Entity)player)) && ((Boolean)this.antiSelf.getValue()).booleanValue()) {
/*     */         continue;
/*     */       }
/*     */       
/* 208 */       if (Phobos.speedManager.getPlayerSpeed(player) > ((Double)this.speed.getValue()).doubleValue()) {
/*     */         continue;
/*     */       }
/*     */       
/* 212 */       if (target == null) {
/* 213 */         target = player;
/* 214 */         distance = mc.field_71439_g.func_70068_e((Entity)player);
/*     */         
/*     */         continue;
/*     */       } 
/* 218 */       if (mc.field_71439_g.func_70068_e((Entity)player) < distance) {
/* 219 */         target = player;
/* 220 */         distance = mc.field_71439_g.func_70068_e((Entity)player);
/*     */       } 
/*     */     } 
/* 223 */     return target;
/*     */   }
/*     */   
/*     */   private void placeBlock(BlockPos pos) {
/* 227 */     if (this.placements < ((Integer)this.blocksPerPlace.getValue()).intValue() && mc.field_71439_g.func_174818_b(pos) <= MathUtil.square(((Double)this.range.getValue()).doubleValue()) && switchItem(false)) {
/* 228 */       isPlacing = true;
/* 229 */       if (this.smartRotate) {
/* 230 */         this.isSneaking = BlockUtil.placeBlockSmartRotate(pos, EnumHand.MAIN_HAND, true, ((Boolean)this.packet.getValue()).booleanValue(), this.isSneaking);
/*     */       } else {
/* 232 */         this.isSneaking = BlockUtil.placeBlock(pos, EnumHand.MAIN_HAND, ((Boolean)this.rotate.getValue()).booleanValue(), ((Boolean)this.packet.getValue()).booleanValue(), this.isSneaking);
/*     */       } 
/* 234 */       this.didPlace = true;
/* 235 */       this.placements++;
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean switchItem(boolean back) {
/* 240 */     boolean[] value = InventoryUtil.switchItem(back, this.lastHotbarSlot, this.switchedItem, (InventoryUtil.Switch)this.switchMode.getValue(), BlockWeb.class);
/* 241 */     this.switchedItem = value[0];
/* 242 */     return value[1];
/*     */   }
/*     */   
/*     */   public enum TargetMode {
/* 246 */     CLOSEST,
/* 247 */     UNTRAPPED;
/*     */   }
/*     */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\me\earth\phobos\features\modules\combat\Webaura.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */