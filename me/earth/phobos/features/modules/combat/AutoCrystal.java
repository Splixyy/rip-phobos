/*     */ package me.earth.phobos.features.modules.combat;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Queue;
/*     */ import java.util.concurrent.ConcurrentLinkedQueue;
/*     */ import me.earth.phobos.Phobos;
/*     */ import me.earth.phobos.event.events.PacketEvent;
/*     */ import me.earth.phobos.event.events.Render3DEvent;
/*     */ import me.earth.phobos.event.events.UpdateWalkingPlayerEvent;
/*     */ import me.earth.phobos.features.command.Command;
/*     */ import me.earth.phobos.features.modules.Module;
/*     */ import me.earth.phobos.features.modules.misc.Tracker;
/*     */ import me.earth.phobos.features.setting.Bind;
/*     */ import me.earth.phobos.features.setting.Setting;
/*     */ import me.earth.phobos.util.BlockUtil;
/*     */ import me.earth.phobos.util.DamageUtil;
/*     */ import me.earth.phobos.util.EntityUtil;
/*     */ import me.earth.phobos.util.InventoryUtil;
/*     */ import me.earth.phobos.util.MathUtil;
/*     */ import me.earth.phobos.util.RenderUtil;
/*     */ import me.earth.phobos.util.Timer;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.ItemEndCrystal;
/*     */ import net.minecraft.network.play.client.CPacketPlayer;
/*     */ import net.minecraft.network.play.server.SPacketExplosion;
/*     */ import net.minecraft.util.EnumHand;
/*     */ import net.minecraft.util.math.AxisAlignedBB;
/*     */ import net.minecraft.util.math.BlockPos;
/*     */ import net.minecraft.util.math.RayTraceResult;
/*     */ import net.minecraft.util.math.Vec3d;
/*     */ import net.minecraftforge.fml.common.eventhandler.EventPriority;
/*     */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*     */ import net.minecraftforge.fml.common.gameevent.InputEvent;
/*     */ import org.lwjgl.input.Keyboard;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AutoCrystal
/*     */   extends Module
/*     */ {
/*  51 */   private final Setting<Settings> setting = register(new Setting("Settings", Settings.PLACE));
/*     */   
/*  53 */   public Setting<Raytrace> raytrace = register(new Setting("Raytrace", Raytrace.NONE, v -> (this.setting.getValue() == Settings.MISC)));
/*     */ 
/*     */   
/*  56 */   public Setting<Boolean> place = register(new Setting("Place", Boolean.valueOf(true), v -> (this.setting.getValue() == Settings.PLACE)));
/*  57 */   public Setting<Integer> placeDelay = register(new Setting("PlaceDelay", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(1000), v -> (this.setting.getValue() == Settings.PLACE && ((Boolean)this.place.getValue()).booleanValue())));
/*  58 */   public Setting<Float> placeRange = register(new Setting("PlaceRange", Float.valueOf(6.0F), Float.valueOf(0.0F), Float.valueOf(10.0F), v -> (this.setting.getValue() == Settings.PLACE && ((Boolean)this.place.getValue()).booleanValue())));
/*  59 */   public Setting<Float> minDamage = register(new Setting("MinDamage", Float.valueOf(4.0F), Float.valueOf(0.1F), Float.valueOf(20.0F), v -> (this.setting.getValue() == Settings.PLACE && ((Boolean)this.place.getValue()).booleanValue())));
/*  60 */   public Setting<Integer> wasteAmount = register(new Setting("WasteAmount", Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(5), v -> (this.setting.getValue() == Settings.PLACE && ((Boolean)this.place.getValue()).booleanValue())));
/*  61 */   public Setting<Boolean> wasteMinDmgCount = register(new Setting("CountMinDmg", Boolean.valueOf(true), v -> (this.setting.getValue() == Settings.PLACE && ((Boolean)this.place.getValue()).booleanValue())));
/*  62 */   public Setting<Float> facePlace = register(new Setting("FacePlace", Float.valueOf(8.0F), Float.valueOf(0.1F), Float.valueOf(20.0F), v -> (this.setting.getValue() == Settings.PLACE && ((Boolean)this.place.getValue()).booleanValue())));
/*  63 */   public Setting<Float> placetrace = register(new Setting("Placetrace", Float.valueOf(6.0F), Float.valueOf(0.0F), Float.valueOf(10.0F), v -> (this.setting.getValue() == Settings.PLACE && ((Boolean)this.place.getValue()).booleanValue() && this.raytrace.getValue() != Raytrace.NONE && this.raytrace.getValue() != Raytrace.BREAK)));
/*  64 */   public Setting<Boolean> antiSurround = register(new Setting("AntiSurround", Boolean.valueOf(false), v -> (this.setting.getValue() == Settings.PLACE && ((Boolean)this.place.getValue()).booleanValue())));
/*     */ 
/*     */   
/*  67 */   public Setting<Boolean> explode = register(new Setting("Break", Boolean.valueOf(true), v -> (this.setting.getValue() == Settings.BREAK)));
/*  68 */   public Setting<Switch> switchMode = register(new Setting("Attack", Switch.BREAKSLOT, v -> (this.setting.getValue() == Settings.BREAK && ((Boolean)this.explode.getValue()).booleanValue())));
/*  69 */   public Setting<Integer> breakDelay = register(new Setting("BreakDelay", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(1000), v -> (this.setting.getValue() == Settings.BREAK && ((Boolean)this.explode.getValue()).booleanValue())));
/*  70 */   public Setting<Float> breakRange = register(new Setting("BreakRange", Float.valueOf(6.0F), Float.valueOf(0.0F), Float.valueOf(10.0F), v -> (this.setting.getValue() == Settings.BREAK && ((Boolean)this.explode.getValue()).booleanValue())));
/*  71 */   public Setting<Integer> packets = register(new Setting("Packets", Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(6), v -> (this.setting.getValue() == Settings.BREAK && ((Boolean)this.explode.getValue()).booleanValue())));
/*  72 */   public Setting<Float> breaktrace = register(new Setting("Breaktrace", Float.valueOf(6.0F), Float.valueOf(0.0F), Float.valueOf(10.0F), v -> (this.setting.getValue() == Settings.BREAK && ((Boolean)this.explode.getValue()).booleanValue() && this.raytrace.getValue() != Raytrace.NONE && this.raytrace.getValue() != Raytrace.PLACE)));
/*  73 */   public Setting<Boolean> manual = register(new Setting("Manual", Boolean.valueOf(false), v -> (this.setting.getValue() == Settings.BREAK)));
/*  74 */   public Setting<Boolean> manualMinDmg = register(new Setting("ManMinDmg", Boolean.valueOf(false), v -> (this.setting.getValue() == Settings.BREAK && ((Boolean)this.manual.getValue()).booleanValue())));
/*  75 */   public Setting<Integer> manualBreak = register(new Setting("ManualDelay", Integer.valueOf(500), Integer.valueOf(0), Integer.valueOf(1000), v -> (this.setting.getValue() == Settings.BREAK && ((Boolean)this.manual.getValue()).booleanValue())));
/*  76 */   public Setting<Boolean> sync = register(new Setting("Sync", Boolean.valueOf(true), v -> (this.setting.getValue() == Settings.BREAK && (((Boolean)this.explode.getValue()).booleanValue() || ((Boolean)this.manual.getValue()).booleanValue()))));
/*     */ 
/*     */   
/*  79 */   public Setting<Boolean> render = register(new Setting("Render", Boolean.valueOf(true), v -> (this.setting.getValue() == Settings.RENDER)));
/*  80 */   public Setting<Boolean> box = register(new Setting("Box", Boolean.valueOf(true), v -> (this.setting.getValue() == Settings.RENDER && ((Boolean)this.render.getValue()).booleanValue())));
/*  81 */   public Setting<Boolean> outline = register(new Setting("Outline", Boolean.valueOf(true), v -> (this.setting.getValue() == Settings.RENDER && ((Boolean)this.render.getValue()).booleanValue())));
/*  82 */   public Setting<Boolean> text = register(new Setting("Text", Boolean.valueOf(false), v -> (this.setting.getValue() == Settings.RENDER && ((Boolean)this.render.getValue()).booleanValue())));
/*  83 */   private final Setting<Integer> red = register(new Setting("Red", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> (this.setting.getValue() == Settings.RENDER && ((Boolean)this.render.getValue()).booleanValue())));
/*  84 */   private final Setting<Integer> green = register(new Setting("Green", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> (this.setting.getValue() == Settings.RENDER && ((Boolean)this.render.getValue()).booleanValue())));
/*  85 */   private final Setting<Integer> blue = register(new Setting("Blue", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> (this.setting.getValue() == Settings.RENDER && ((Boolean)this.render.getValue()).booleanValue())));
/*  86 */   private final Setting<Integer> alpha = register(new Setting("Alpha", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> (this.setting.getValue() == Settings.RENDER && ((Boolean)this.render.getValue()).booleanValue())));
/*  87 */   private final Setting<Integer> boxAlpha = register(new Setting("BoxAlpha", Integer.valueOf(125), Integer.valueOf(0), Integer.valueOf(255), v -> (this.setting.getValue() == Settings.RENDER && ((Boolean)this.render.getValue()).booleanValue() && ((Boolean)this.box.getValue()).booleanValue())));
/*  88 */   private final Setting<Float> lineWidth = register(new Setting("LineWidth", Float.valueOf(1.5F), Float.valueOf(0.1F), Float.valueOf(5.0F), v -> (this.setting.getValue() == Settings.RENDER && ((Boolean)this.render.getValue()).booleanValue() && ((Boolean)this.outline.getValue()).booleanValue())));
/*  89 */   public Setting<Boolean> customOutline = register(new Setting("CustomLine", Boolean.valueOf(false), v -> (this.setting.getValue() == Settings.RENDER && ((Boolean)this.render.getValue()).booleanValue() && ((Boolean)this.outline.getValue()).booleanValue())));
/*  90 */   private final Setting<Integer> cRed = register(new Setting("OL-Red", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> (this.setting.getValue() == Settings.RENDER && ((Boolean)this.render.getValue()).booleanValue() && ((Boolean)this.customOutline.getValue()).booleanValue() && ((Boolean)this.outline.getValue()).booleanValue())));
/*  91 */   private final Setting<Integer> cGreen = register(new Setting("OL-Green", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> (this.setting.getValue() == Settings.RENDER && ((Boolean)this.render.getValue()).booleanValue() && ((Boolean)this.customOutline.getValue()).booleanValue() && ((Boolean)this.outline.getValue()).booleanValue())));
/*  92 */   private final Setting<Integer> cBlue = register(new Setting("OL-Blue", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> (this.setting.getValue() == Settings.RENDER && ((Boolean)this.render.getValue()).booleanValue() && ((Boolean)this.customOutline.getValue()).booleanValue() && ((Boolean)this.outline.getValue()).booleanValue())));
/*  93 */   private final Setting<Integer> cAlpha = register(new Setting("OL-Alpha", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> (this.setting.getValue() == Settings.RENDER && ((Boolean)this.render.getValue()).booleanValue() && ((Boolean)this.customOutline.getValue()).booleanValue() && ((Boolean)this.outline.getValue()).booleanValue())));
/*     */ 
/*     */   
/*  96 */   public Setting<Float> range = register(new Setting("Range", Float.valueOf(12.0F), Float.valueOf(0.1F), Float.valueOf(20.0F), v -> (this.setting.getValue() == Settings.MISC)));
/*  97 */   public Setting<Target> targetMode = register(new Setting("Target", Target.CLOSEST, v -> (this.setting.getValue() == Settings.MISC)));
/*  98 */   public Setting<Integer> minArmor = register(new Setting("MinArmor", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(125), v -> (this.setting.getValue() == Settings.MISC)));
/*  99 */   private final Setting<Integer> switchCooldown = register(new Setting("Cooldown", Integer.valueOf(500), Integer.valueOf(0), Integer.valueOf(1000), v -> (this.setting.getValue() == Settings.MISC)));
/* 100 */   public Setting<AutoSwitch> autoSwitch = register(new Setting("Switch", AutoSwitch.TOGGLE, v -> (this.setting.getValue() == Settings.MISC)));
/* 101 */   public Setting<Bind> switchBind = register(new Setting("SwitchBind", new Bind(-1), v -> (this.setting.getValue() == Settings.MISC && this.autoSwitch.getValue() == AutoSwitch.TOGGLE)));
/* 102 */   public Setting<Boolean> offhandSwitch = register(new Setting("Offhand", Boolean.valueOf(false), v -> (this.setting.getValue() == Settings.MISC && this.autoSwitch.getValue() != AutoSwitch.NONE)));
/* 103 */   public Setting<Boolean> switchBack = register(new Setting("Switchback", Boolean.valueOf(false), v -> (this.setting.getValue() == Settings.MISC && this.autoSwitch.getValue() != AutoSwitch.NONE && ((Boolean)this.offhandSwitch.getValue()).booleanValue())));
/* 104 */   public Setting<Boolean> lethalSwitch = register(new Setting("LethalSwitch", Boolean.valueOf(false), v -> (this.setting.getValue() == Settings.MISC && this.autoSwitch.getValue() != AutoSwitch.NONE)));
/* 105 */   public Setting<Boolean> mineSwitch = register(new Setting("MineSwitch", Boolean.valueOf(false), v -> (this.setting.getValue() == Settings.MISC && this.autoSwitch.getValue() != AutoSwitch.NONE)));
/* 106 */   public Setting<Rotate> rotate = register(new Setting("Rotate", Rotate.OFF, v -> (this.setting.getValue() == Settings.MISC)));
/* 107 */   private final Setting<Integer> eventMode = register(new Setting("Updates", Integer.valueOf(3), Integer.valueOf(1), Integer.valueOf(3), v -> (this.setting.getValue() == Settings.MISC)));
/* 108 */   public Setting<Logic> logic = register(new Setting("Logic", Logic.BREAKPLACE, v -> (this.setting.getValue() == Settings.MISC)));
/* 109 */   public Setting<Boolean> doubleMap = register(new Setting("DoubleMap", Boolean.valueOf(false), v -> (this.setting.getValue() == Settings.MISC && this.logic.getValue() == Logic.PLACEBREAK)));
/* 110 */   public Setting<Boolean> suicide = register(new Setting("Suicide", Boolean.valueOf(false), v -> (this.setting.getValue() == Settings.MISC)));
/* 111 */   public Setting<Boolean> webAttack = register(new Setting("WebAttack", Boolean.valueOf(false), v -> (this.setting.getValue() == Settings.MISC && this.targetMode.getValue() != Target.DAMAGE)));
/* 112 */   public Setting<Boolean> fullCalc = register(new Setting("ExtraCalc", Boolean.valueOf(false), v -> (this.setting.getValue() == Settings.MISC)));
/* 113 */   public Setting<Boolean> extraSelfCalc = register(new Setting("MinSelfDmg", Boolean.valueOf(true), v -> (this.setting.getValue() == Settings.MISC)));
/* 114 */   public Setting<DamageSync> damageSync = register(new Setting("DamageSync", DamageSync.NONE, v -> (this.setting.getValue() == Settings.MISC)));
/* 115 */   public Setting<Integer> damageSyncTime = register(new Setting("SyncDelay", Integer.valueOf(500), Integer.valueOf(0), Integer.valueOf(1000), v -> (this.setting.getValue() == Settings.MISC && this.damageSync.getValue() != DamageSync.NONE)));
/* 116 */   public Setting<Float> dropOff = register(new Setting("DropOff", Float.valueOf(5.0F), Float.valueOf(0.0F), Float.valueOf(10.0F), v -> (this.setting.getValue() == Settings.MISC && this.damageSync.getValue() == DamageSync.BREAK)));
/* 117 */   public Setting<Integer> confirm = register(new Setting("Confirm", Integer.valueOf(250), Integer.valueOf(0), Integer.valueOf(1000), v -> (this.setting.getValue() == Settings.MISC && this.damageSync.getValue() != DamageSync.NONE)));
/*     */   
/* 119 */   private Queue<Entity> attackList = new ConcurrentLinkedQueue<>();
/* 120 */   private Map<Entity, Float> crystalMap = new HashMap<>();
/* 121 */   private final Timer switchTimer = new Timer();
/* 122 */   private final Timer manualTimer = new Timer();
/* 123 */   private final Timer breakTimer = new Timer();
/* 124 */   private final Timer placeTimer = new Timer();
/* 125 */   private final Timer syncTimer = new Timer();
/* 126 */   public static EntityPlayer target = null;
/* 127 */   private Entity efficientTarget = null;
/* 128 */   private double currentDamage = 0.0D;
/* 129 */   private double renderDamage = 0.0D;
/* 130 */   private double lastDamage = 0.0D;
/*     */   private boolean didRotation = false;
/*     */   private boolean switching = false;
/* 133 */   private BlockPos placePos = null;
/* 134 */   private BlockPos renderPos = null;
/*     */   private boolean mainHand = false;
/*     */   private boolean rotating = false;
/*     */   private boolean offHand = false;
/* 138 */   private int crystalCount = 0;
/* 139 */   private int minDmgCount = 0;
/* 140 */   private int lastSlot = -1;
/* 141 */   private float yaw = 0.0F;
/* 142 */   private float pitch = 0.0F;
/* 143 */   private BlockPos webPos = null;
/* 144 */   private final Timer renderTimer = new Timer();
/* 145 */   private BlockPos lastPos = null;
/* 146 */   public static ArrayList<BlockPos> placedPos = new ArrayList<>();
/* 147 */   public static ArrayList<BlockPos> brokenPos = new ArrayList<>();
/*     */   private boolean posConfirmed = false;
/*     */   
/*     */   public AutoCrystal() {
/* 151 */     super("AutoCrystal", "Best CA on the market", Module.Category.COMBAT, true, false, false);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onTick() {
/* 156 */     if (((Integer)this.eventMode.getValue()).intValue() == 3) {
/* 157 */       doAutoCrystal();
/*     */     }
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onUpdateWalkingPlayer(UpdateWalkingPlayerEvent event) {
/* 163 */     if (event.getStage() == 0 && ((Integer)this.eventMode.getValue()).intValue() == 2) {
/* 164 */       doAutoCrystal();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void onUpdate() {
/* 170 */     if (((Integer)this.eventMode.getValue()).intValue() == 1) {
/* 171 */       doAutoCrystal();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void onDisable() {
/* 177 */     this.rotating = false;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getDisplayInfo() {
/* 182 */     if (this.switching) {
/* 183 */       return "§aSwitch";
/*     */     }
/*     */     
/* 186 */     if (target != null) {
/* 187 */       return target.func_70005_c_();
/*     */     }
/*     */     
/* 190 */     return null;
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onPacketSend(PacketEvent.Send event) {
/* 195 */     if (event.getStage() == 0 && this.rotate.getValue() != Rotate.OFF && this.rotating && ((Integer)this.eventMode.getValue()).intValue() != 2 && 
/* 196 */       event.getPacket() instanceof CPacketPlayer) {
/* 197 */       CPacketPlayer packet = (CPacketPlayer)event.getPacket();
/* 198 */       packet.field_149476_e = this.yaw;
/* 199 */       packet.field_149473_f = this.pitch;
/* 200 */       this.rotating = false;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   @SubscribeEvent(priority = EventPriority.HIGHEST, receiveCanceled = true)
/*     */   public void onPacketReceive(PacketEvent.Receive event) {
/* 207 */     if (event.getPacket() instanceof SPacketExplosion) {
/* 208 */       SPacketExplosion packet = (SPacketExplosion)event.getPacket();
/* 209 */       BlockPos pos = (new BlockPos(packet.func_149148_f(), packet.func_149143_g(), packet.func_149145_h())).func_177977_b();
/* 210 */       if (this.damageSync.getValue() == DamageSync.PLACE) {
/* 211 */         if (placedPos.contains(pos)) {
/* 212 */           if (!Tracker.getInstance().isOn()) {
/* 213 */             placedPos.remove(pos);
/*     */           }
/* 215 */           this.posConfirmed = true;
/*     */         } 
/* 217 */       } else if (this.damageSync.getValue() == DamageSync.BREAK && 
/* 218 */         brokenPos.contains(pos)) {
/* 219 */         brokenPos.remove(pos);
/* 220 */         this.posConfirmed = true;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void onRender3D(Render3DEvent event) {
/* 228 */     if ((this.offHand || this.mainHand || this.switchMode.getValue() == Switch.CALC) && this.renderPos != null && ((Boolean)this.render.getValue()).booleanValue() && (((Boolean)this.box.getValue()).booleanValue() || ((Boolean)this.text.getValue()).booleanValue() || ((Boolean)this.outline.getValue()).booleanValue())) {
/* 229 */       RenderUtil.drawBoxESP(this.renderPos, new Color(((Integer)this.red.getValue()).intValue(), ((Integer)this.green.getValue()).intValue(), ((Integer)this.blue.getValue()).intValue(), ((Integer)this.alpha.getValue()).intValue()), ((Boolean)this.customOutline.getValue()).booleanValue(), new Color(((Integer)this.cRed.getValue()).intValue(), ((Integer)this.cGreen.getValue()).intValue(), ((Integer)this.cBlue.getValue()).intValue(), ((Integer)this.cAlpha.getValue()).intValue()), ((Float)this.lineWidth.getValue()).floatValue(), ((Boolean)this.outline.getValue()).booleanValue(), ((Boolean)this.box.getValue()).booleanValue(), ((Integer)this.boxAlpha.getValue()).intValue(), false);
/* 230 */       if (((Boolean)this.text.getValue()).booleanValue()) {
/* 231 */         RenderUtil.drawText(this.renderPos, ((Math.floor(this.renderDamage) == this.renderDamage) ? (String)Integer.valueOf((int)this.renderDamage) : String.format("%.1f", new Object[] { Double.valueOf(this.renderDamage) })) + "");
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onKeyInput(InputEvent.KeyInputEvent event) {
/* 238 */     if (Keyboard.getEventKeyState() && !(mc.field_71462_r instanceof me.earth.phobos.features.gui.PhobosGui) && ((Bind)this.switchBind.getValue()).getKey() == Keyboard.getEventKey()) {
/* 239 */       if (((Boolean)this.switchBack.getValue()).booleanValue() && ((Boolean)this.offhandSwitch.getValue()).booleanValue() && this.offHand) {
/* 240 */         Offhand module = (Offhand)Phobos.moduleManager.getModuleByClass(Offhand.class);
/* 241 */         if (module.isOff()) {
/* 242 */           Command.sendMessage("<" + getDisplayName() + "> " + "§c" + "Switch failed. Enable the Offhand module.");
/*     */         }
/* 244 */         else if (module.type.getValue() == Offhand.Type.NEW) {
/* 245 */           module.setSwapToTotem(true);
/* 246 */           module.doOffhand();
/*     */         } else {
/* 248 */           module.setMode(Offhand.Mode2.TOTEMS);
/* 249 */           module.doSwitch();
/*     */         } 
/*     */         
/*     */         return;
/*     */       } 
/* 254 */       this.switching = !this.switching;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void doAutoCrystal() {
/* 259 */     if (check()) {
/* 260 */       switch ((Logic)this.logic.getValue()) {
/*     */         case OFF:
/* 262 */           placeCrystal();
/* 263 */           if (((Boolean)this.doubleMap.getValue()).booleanValue()) {
/* 264 */             mapCrystals();
/*     */           }
/* 266 */           breakCrystal();
/*     */           break;
/*     */         case PLACE:
/* 269 */           breakCrystal();
/* 270 */           placeCrystal();
/*     */           break;
/*     */       } 
/*     */       
/* 274 */       manualBreaker();
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean check() {
/* 279 */     if (fullNullCheck()) {
/* 280 */       return false;
/*     */     }
/*     */     
/* 283 */     if (this.renderTimer.passedMs(500L)) {
/* 284 */       this.renderPos = null;
/* 285 */       this.renderTimer.reset();
/*     */     } 
/*     */ 
/*     */     
/* 289 */     this.mainHand = (mc.field_71439_g.func_184614_ca().func_77973_b() == Items.field_185158_cP);
/* 290 */     this.offHand = (mc.field_71439_g.func_184592_cb().func_77973_b() == Items.field_185158_cP);
/* 291 */     this.currentDamage = 0.0D;
/* 292 */     this.placePos = null;
/*     */     
/* 294 */     if (this.lastSlot != mc.field_71439_g.field_71071_by.field_70461_c || AutoTrap.isPlacing || Surround.isPlacing) {
/* 295 */       this.lastSlot = mc.field_71439_g.field_71071_by.field_70461_c;
/* 296 */       this.switchTimer.reset();
/*     */     } 
/*     */     
/* 299 */     if (this.offHand || this.mainHand) {
/* 300 */       this.switching = false;
/*     */     }
/*     */     
/* 303 */     if ((!this.offHand && !this.mainHand && this.switchMode.getValue() == Switch.BREAKSLOT && !this.switching) || !DamageUtil.canBreakWeakness((EntityPlayer)mc.field_71439_g) || !this.switchTimer.passedMs(((Integer)this.switchCooldown.getValue()).intValue())) {
/* 304 */       this.renderPos = null;
/* 305 */       target = null;
/* 306 */       this.rotating = false;
/* 307 */       return false;
/*     */     } 
/*     */     
/* 310 */     if (((Boolean)this.mineSwitch.getValue()).booleanValue() && mc.field_71474_y.field_74312_F.func_151470_d() && (this.switching || this.autoSwitch.getValue() == AutoSwitch.ALWAYS) && mc.field_71474_y.field_74313_G.func_151470_d() && mc.field_71439_g.func_184614_ca().func_77973_b() instanceof net.minecraft.item.ItemPickaxe) {
/* 311 */       switchItem();
/*     */     }
/*     */     
/* 314 */     mapCrystals();
/*     */     
/* 316 */     if (!this.posConfirmed && this.damageSync.getValue() != DamageSync.NONE && this.syncTimer.passedMs(((Integer)this.confirm.getValue()).intValue())) {
/* 317 */       this.syncTimer.setMs((((Integer)this.damageSyncTime.getValue()).intValue() + 1));
/*     */     }
/* 319 */     return true;
/*     */   }
/*     */   
/*     */   private void mapCrystals() {
/* 323 */     this.efficientTarget = null;
/* 324 */     if (((Integer)this.packets.getValue()).intValue() != 1) {
/* 325 */       this.attackList = new ConcurrentLinkedQueue<>();
/* 326 */       this.crystalMap = new HashMap<>();
/*     */     } 
/* 328 */     this.crystalCount = 0;
/* 329 */     this.minDmgCount = 0;
/* 330 */     Entity maxCrystal = null;
/* 331 */     float maxDamage = 0.5F;
/* 332 */     for (Entity crystal : mc.field_71441_e.field_72996_f) {
/* 333 */       if (crystal instanceof net.minecraft.entity.item.EntityEnderCrystal && 
/* 334 */         isValid(crystal)) {
/* 335 */         boolean count = false;
/* 336 */         boolean countMin = false;
/* 337 */         float selfDamage = DamageUtil.calculateDamage(crystal, (Entity)mc.field_71439_g);
/* 338 */         if (selfDamage + 0.5D < EntityUtil.getHealth((Entity)mc.field_71439_g) || !DamageUtil.canTakeDamage(((Boolean)this.suicide.getValue()).booleanValue())) {
/* 339 */           for (EntityPlayer player : mc.field_71441_e.field_73010_i) {
/* 340 */             if (player.func_70068_e(crystal) < MathUtil.square(((Float)this.range.getValue()).floatValue()) && EntityUtil.isValid((Entity)player, (((Float)this.range.getValue()).floatValue() + ((Float)this.breakRange.getValue()).floatValue()))) {
/* 341 */               float damage = DamageUtil.calculateDamage(crystal, (Entity)player);
/* 342 */               if (damage > selfDamage || (damage > ((Float)this.minDamage.getValue()).floatValue() && !DamageUtil.canTakeDamage(((Boolean)this.suicide.getValue()).booleanValue())) || damage > EntityUtil.getHealth((Entity)player)) {
/* 343 */                 if (damage > maxDamage) {
/* 344 */                   maxDamage = damage;
/* 345 */                   maxCrystal = crystal;
/*     */                 } 
/*     */                 
/* 348 */                 if (((Integer)this.packets.getValue()).intValue() == 1) {
/* 349 */                   if (damage >= ((Float)this.minDamage.getValue()).floatValue() || !((Boolean)this.wasteMinDmgCount.getValue()).booleanValue()) {
/* 350 */                     count = true;
/*     */                   }
/* 352 */                   countMin = true; continue;
/*     */                 } 
/* 354 */                 if (this.crystalMap.get(crystal) == null || ((Float)this.crystalMap.get(crystal)).floatValue() < damage) {
/* 355 */                   this.crystalMap.put(crystal, Float.valueOf(damage));
/*     */                 }
/*     */               } 
/*     */             } 
/*     */           } 
/*     */         }
/*     */ 
/*     */         
/* 363 */         if (countMin) {
/* 364 */           this.minDmgCount++;
/* 365 */           if (count) {
/* 366 */             this.crystalCount++;
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 373 */     if (this.damageSync.getValue() == DamageSync.BREAK && (maxDamage > this.lastDamage || this.syncTimer.passedMs(((Integer)this.damageSyncTime.getValue()).intValue()) || this.damageSync.getValue() == DamageSync.NONE)) {
/* 374 */       this.lastDamage = maxDamage;
/*     */     }
/*     */     
/* 377 */     if (((Boolean)this.webAttack.getValue()).booleanValue() && this.webPos != null) {
/* 378 */       if (mc.field_71439_g.func_174818_b(this.webPos.func_177984_a()) > MathUtil.square(((Float)this.breakRange.getValue()).floatValue())) {
/* 379 */         this.webPos = null;
/*     */       } else {
/* 381 */         for (Entity entity : mc.field_71441_e.func_72872_a(Entity.class, new AxisAlignedBB(this.webPos.func_177984_a()))) {
/* 382 */           if (entity instanceof net.minecraft.entity.item.EntityEnderCrystal) {
/* 383 */             this.attackList.add(entity);
/* 384 */             this.efficientTarget = entity;
/* 385 */             this.webPos = null;
/* 386 */             this.lastDamage = 0.5D;
/*     */             
/*     */             return;
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     }
/* 393 */     if (((Boolean)this.manual.getValue()).booleanValue() && ((Boolean)this.manualMinDmg.getValue()).booleanValue() && mc.field_71474_y.field_74313_G.func_151470_d() && ((this.offHand && mc.field_71439_g.func_184600_cs() == EnumHand.OFF_HAND) || (this.mainHand && mc.field_71439_g.func_184600_cs() == EnumHand.MAIN_HAND)) && maxDamage < ((Float)this.minDamage.getValue()).floatValue()) {
/* 394 */       this.efficientTarget = null;
/*     */       
/*     */       return;
/*     */     } 
/* 398 */     if (((Integer)this.packets.getValue()).intValue() == 1) {
/* 399 */       this.efficientTarget = maxCrystal;
/*     */     } else {
/*     */       
/* 402 */       this.crystalMap = MathUtil.sortByValue(this.crystalMap, true);
/* 403 */       for (Map.Entry<Entity, Float> entry : this.crystalMap.entrySet()) {
/* 404 */         Entity crystal = entry.getKey();
/* 405 */         float damage = ((Float)entry.getValue()).floatValue();
/* 406 */         if (damage >= ((Float)this.minDamage.getValue()).floatValue() || !((Boolean)this.wasteMinDmgCount.getValue()).booleanValue()) {
/* 407 */           this.crystalCount++;
/*     */         }
/* 409 */         this.attackList.add(crystal);
/* 410 */         this.minDmgCount++;
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void placeCrystal() {
/* 416 */     int crystalLimit = ((Integer)this.wasteAmount.getValue()).intValue();
/* 417 */     if (this.placeTimer.passedMs(((Integer)this.placeDelay.getValue()).intValue()) && ((Boolean)this.place.getValue()).booleanValue() && (this.offHand || this.mainHand || this.switchMode.getValue() == Switch.CALC || (this.switchMode.getValue() == Switch.BREAKSLOT && this.switching))) {
/* 418 */       if ((this.offHand || this.mainHand || (this.switchMode.getValue() != Switch.ALWAYS && !this.switching)) && this.crystalCount >= crystalLimit && (!((Boolean)this.antiSurround.getValue()).booleanValue() || this.lastPos == null || !this.lastPos.equals(this.placePos))) {
/*     */         return;
/*     */       }
/* 421 */       calculateDamage(getTarget((this.targetMode.getValue() == Target.UNSAFE)));
/* 422 */       if (target != null && this.placePos != null) {
/* 423 */         if (!this.offHand && !this.mainHand && this.autoSwitch.getValue() != AutoSwitch.NONE && (this.currentDamage > ((Float)this.minDamage.getValue()).floatValue() || (((Boolean)this.lethalSwitch.getValue()).booleanValue() && EntityUtil.getHealth((Entity)target) < ((Float)this.facePlace.getValue()).floatValue())) && !switchItem()) {
/*     */           return;
/*     */         }
/*     */         
/* 427 */         if (this.currentDamage < ((Float)this.minDamage.getValue()).floatValue()) {
/* 428 */           crystalLimit = 1;
/*     */         }
/*     */         
/* 431 */         if ((this.offHand || this.mainHand || this.autoSwitch.getValue() != AutoSwitch.NONE) && (this.crystalCount < crystalLimit || (((Boolean)this.antiSurround.getValue()).booleanValue() && this.lastPos != null && this.lastPos.equals(this.placePos))) && (this.currentDamage > ((Float)this.minDamage.getValue()).floatValue() || this.minDmgCount < crystalLimit) && this.currentDamage >= 1.0D && (DamageUtil.isArmorLow(target, ((Integer)this.minArmor.getValue()).intValue()) || EntityUtil.getHealth((Entity)target) < ((Float)this.facePlace.getValue()).floatValue() || this.currentDamage > ((Float)this.minDamage.getValue()).floatValue())) {
/* 432 */           float damageOffset = (this.damageSync.getValue() == DamageSync.BREAK) ? (((Float)this.dropOff.getValue()).floatValue() - 5.0F) : 0.0F;
/* 433 */           if (this.currentDamage - damageOffset > this.lastDamage || this.syncTimer.passedMs(((Integer)this.damageSyncTime.getValue()).intValue()) || this.damageSync.getValue() == DamageSync.NONE) {
/* 434 */             if (this.damageSync.getValue() != DamageSync.BREAK) {
/* 435 */               this.lastDamage = this.currentDamage;
/*     */             }
/* 437 */             this.renderPos = this.placePos;
/* 438 */             this.renderDamage = this.currentDamage;
/* 439 */             if (switchItem()) {
/* 440 */               rotateToPos(this.placePos);
/* 441 */               BlockUtil.placeCrystalOnBlock(this.placePos, this.offHand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND);
/* 442 */               placedPos.add(this.placePos);
/* 443 */               this.lastPos = this.placePos;
/* 444 */               this.placeTimer.reset();
/* 445 */               this.posConfirmed = false;
/* 446 */               if (this.syncTimer.passedMs(((Integer)this.damageSyncTime.getValue()).intValue())) {
/* 447 */                 this.syncTimer.reset();
/*     */               }
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } else {
/* 453 */         this.renderPos = null;
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean switchItem() {
/* 459 */     if (this.offHand || this.mainHand) {
/* 460 */       return true;
/*     */     }
/*     */     
/* 463 */     switch ((AutoSwitch)this.autoSwitch.getValue()) {
/*     */       case OFF:
/* 465 */         return false;
/*     */       case PLACE:
/* 467 */         if (!this.switching) {
/* 468 */           return false;
/*     */         }
/*     */       case BREAK:
/* 471 */         if (doSwitch()) {
/* 472 */           return true;
/*     */         }
/*     */         break;
/*     */     } 
/* 476 */     return false;
/*     */   }
/*     */   
/*     */   private boolean doSwitch() {
/* 480 */     if (((Boolean)this.offhandSwitch.getValue()).booleanValue()) {
/* 481 */       Offhand module = (Offhand)Phobos.moduleManager.getModuleByClass(Offhand.class);
/* 482 */       if (module.isOff()) {
/* 483 */         Command.sendMessage("<" + getDisplayName() + "> " + "§c" + "Switch failed. Enable the Offhand module.");
/* 484 */         this.switching = false;
/* 485 */         return false;
/*     */       } 
/* 487 */       if (module.type.getValue() == Offhand.Type.NEW) {
/* 488 */         module.setSwapToTotem(false);
/* 489 */         module.setMode(Offhand.Mode.CRYSTALS);
/* 490 */         module.doOffhand();
/*     */       } else {
/* 492 */         module.setMode(Offhand.Mode2.CRYSTALS);
/* 493 */         module.doSwitch();
/*     */       } 
/* 495 */       this.switching = false;
/* 496 */       return true;
/*     */     } 
/*     */ 
/*     */     
/* 500 */     if (mc.field_71439_g.func_184592_cb().func_77973_b() == Items.field_185158_cP) {
/* 501 */       this.mainHand = false;
/*     */     } else {
/* 503 */       InventoryUtil.switchToHotbarSlot(ItemEndCrystal.class, false);
/* 504 */       this.mainHand = true;
/*     */     } 
/* 506 */     this.switching = false;
/* 507 */     return true;
/*     */   }
/*     */   
/*     */   private void calculateDamage(EntityPlayer targettedPlayer) {
/* 511 */     if (targettedPlayer == null && this.targetMode.getValue() != Target.DAMAGE && !((Boolean)this.fullCalc.getValue()).booleanValue()) {
/*     */       return;
/*     */     }
/*     */     
/* 515 */     float maxDamage = 0.5F;
/* 516 */     EntityPlayer currentTarget = null;
/* 517 */     BlockPos currentPos = null;
/* 518 */     float maxSelfDamage = 0.0F;
/*     */     
/* 520 */     BlockPos setToAir = null;
/* 521 */     IBlockState state = null;
/* 522 */     if (((Boolean)this.webAttack.getValue()).booleanValue() && targettedPlayer != null) {
/* 523 */       BlockPos playerPos = new BlockPos(targettedPlayer.func_174791_d());
/* 524 */       Block web = mc.field_71441_e.func_180495_p(playerPos).func_177230_c();
/* 525 */       if (web == Blocks.field_150321_G) {
/* 526 */         setToAir = playerPos;
/* 527 */         state = mc.field_71441_e.func_180495_p(playerPos);
/* 528 */         mc.field_71441_e.func_175698_g(playerPos);
/*     */       } 
/*     */     } 
/*     */     
/* 532 */     for (BlockPos pos : BlockUtil.possiblePlacePositions(((Float)this.placeRange.getValue()).floatValue(), ((Boolean)this.antiSurround.getValue()).booleanValue())) {
/* 533 */       if (BlockUtil.rayTracePlaceCheck(pos, ((this.raytrace.getValue() == Raytrace.PLACE || this.raytrace.getValue() == Raytrace.FULL) && mc.field_71439_g.func_174818_b(pos) > MathUtil.square(((Float)this.placetrace.getValue()).floatValue())), 1.0F)) {
/* 534 */         float selfDamage = -1.0F;
/* 535 */         if (DamageUtil.canTakeDamage(((Boolean)this.suicide.getValue()).booleanValue())) {
/* 536 */           selfDamage = DamageUtil.calculateDamage(pos, (Entity)mc.field_71439_g);
/*     */         }
/*     */         
/* 539 */         if (selfDamage + 0.5D < EntityUtil.getHealth((Entity)mc.field_71439_g)) {
/* 540 */           if (targettedPlayer != null) {
/* 541 */             float playerDamage = DamageUtil.calculateDamage(pos, (Entity)targettedPlayer);
/* 542 */             if ((playerDamage > maxDamage || (((Boolean)this.extraSelfCalc.getValue()).booleanValue() && playerDamage >= maxDamage && selfDamage < maxSelfDamage)) && (playerDamage > selfDamage || (playerDamage > ((Float)this.minDamage.getValue()).floatValue() && !DamageUtil.canTakeDamage(((Boolean)this.suicide.getValue()).booleanValue())) || playerDamage > EntityUtil.getHealth((Entity)targettedPlayer))) {
/* 543 */               maxDamage = playerDamage;
/* 544 */               currentTarget = targettedPlayer;
/* 545 */               currentPos = pos;
/* 546 */               maxSelfDamage = selfDamage;
/*     */             }  continue;
/*     */           } 
/* 549 */           for (EntityPlayer player : mc.field_71441_e.field_73010_i) {
/* 550 */             if (EntityUtil.isValid((Entity)player, (((Float)this.placeRange.getValue()).floatValue() + ((Float)this.range.getValue()).floatValue()))) {
/* 551 */               float playerDamage = DamageUtil.calculateDamage(pos, (Entity)player);
/* 552 */               if ((playerDamage > maxDamage || (((Boolean)this.extraSelfCalc.getValue()).booleanValue() && playerDamage >= maxDamage && selfDamage < maxSelfDamage)) && (playerDamage > selfDamage || (playerDamage > ((Float)this.minDamage.getValue()).floatValue() && !DamageUtil.canTakeDamage(((Boolean)this.suicide.getValue()).booleanValue())) || playerDamage > EntityUtil.getHealth((Entity)player))) {
/* 553 */                 maxDamage = playerDamage;
/* 554 */                 currentTarget = player;
/* 555 */                 currentPos = pos;
/* 556 */                 maxSelfDamage = selfDamage;
/*     */               } 
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 565 */     if (setToAir != null) {
/* 566 */       mc.field_71441_e.func_175656_a(setToAir, state);
/* 567 */       this.webPos = currentPos;
/*     */     } 
/*     */     
/* 570 */     target = currentTarget;
/* 571 */     this.currentDamage = maxDamage;
/* 572 */     this.placePos = currentPos;
/*     */   }
/*     */   
/*     */   private EntityPlayer getTarget(boolean unsafe) {
/* 576 */     if (this.targetMode.getValue() == Target.DAMAGE) {
/* 577 */       return null;
/*     */     }
/*     */     
/* 580 */     EntityPlayer currentTarget = null;
/* 581 */     for (EntityPlayer player : mc.field_71441_e.field_73010_i) {
/* 582 */       if (EntityUtil.isntValid((Entity)player, (((Float)this.placeRange.getValue()).floatValue() + ((Float)this.range.getValue()).floatValue()))) {
/*     */         continue;
/*     */       }
/*     */       
/* 586 */       if (unsafe && EntityUtil.isSafe((Entity)player)) {
/*     */         continue;
/*     */       }
/*     */       
/* 590 */       if (((Integer)this.minArmor.getValue()).intValue() > 0 && DamageUtil.isArmorLow(player, ((Integer)this.minArmor.getValue()).intValue())) {
/* 591 */         currentTarget = player;
/*     */         
/*     */         break;
/*     */       } 
/* 595 */       if (currentTarget == null) {
/* 596 */         currentTarget = player;
/*     */         
/*     */         continue;
/*     */       } 
/* 600 */       if (mc.field_71439_g.func_70068_e((Entity)player) < mc.field_71439_g.func_70068_e((Entity)currentTarget)) {
/* 601 */         currentTarget = player;
/*     */       }
/*     */     } 
/*     */     
/* 605 */     if (unsafe && currentTarget == null) {
/* 606 */       return getTarget(false);
/*     */     }
/*     */     
/* 609 */     return currentTarget;
/*     */   }
/*     */   
/*     */   private void breakCrystal() {
/* 613 */     if (((Boolean)this.explode.getValue()).booleanValue() && this.breakTimer.passedMs(((Integer)this.breakDelay.getValue()).intValue()) && (this.switchMode.getValue() == Switch.ALWAYS || this.mainHand || this.offHand)) {
/* 614 */       if (((Integer)this.packets.getValue()).intValue() == 1 && this.efficientTarget != null) {
/* 615 */         rotateTo(this.efficientTarget);
/* 616 */         EntityUtil.attackEntity(this.efficientTarget, ((Boolean)this.sync.getValue()).booleanValue(), true);
/* 617 */         brokenPos.add(this.efficientTarget.func_180425_c().func_177977_b());
/* 618 */       } else if (!this.attackList.isEmpty()) {
/* 619 */         for (int i = 0; i < ((Integer)this.packets.getValue()).intValue(); i++) {
/* 620 */           Entity entity = this.attackList.poll();
/* 621 */           if (entity != null) {
/* 622 */             rotateTo(entity);
/* 623 */             EntityUtil.attackEntity(entity, ((Boolean)this.sync.getValue()).booleanValue(), true);
/* 624 */             brokenPos.add(entity.func_180425_c().func_177977_b());
/*     */           } 
/*     */         } 
/*     */       } 
/* 628 */       this.breakTimer.reset();
/*     */     } 
/*     */   }
/*     */   
/*     */   private void manualBreaker() {
/* 633 */     if (this.rotate.getValue() != Rotate.OFF && ((Integer)this.eventMode.getValue()).intValue() != 2 && this.rotating) {
/* 634 */       if (this.didRotation) {
/* 635 */         mc.field_71439_g.field_70125_A = (float)(mc.field_71439_g.field_70125_A + 4.0E-4D);
/* 636 */         this.didRotation = false;
/*     */       } else {
/* 638 */         mc.field_71439_g.field_70125_A = (float)(mc.field_71439_g.field_70125_A - 4.0E-4D);
/* 639 */         this.didRotation = true;
/*     */       } 
/*     */     }
/*     */     
/* 643 */     if ((this.offHand || this.mainHand) && ((Boolean)this.manual.getValue()).booleanValue() && this.manualTimer.passedMs(((Integer)this.manualBreak.getValue()).intValue()) && mc.field_71474_y.field_74313_G.func_151470_d() && mc.field_71439_g.func_184592_cb().func_77973_b() != Items.field_151153_ao && mc.field_71439_g.field_71071_by.func_70448_g().func_77973_b() != Items.field_151153_ao && mc.field_71439_g.field_71071_by.func_70448_g().func_77973_b() != Items.field_151031_f && mc.field_71439_g.field_71071_by.func_70448_g().func_77973_b() != Items.field_151062_by) {
/* 644 */       RayTraceResult result = mc.field_71476_x;
/* 645 */       if (result != null) {
/* 646 */         Entity entity; BlockPos mousePos; switch (result.field_72313_a) {
/*     */           case OFF:
/* 648 */             entity = result.field_72308_g;
/* 649 */             if (entity instanceof net.minecraft.entity.item.EntityEnderCrystal) {
/* 650 */               EntityUtil.attackEntity(entity, ((Boolean)this.sync.getValue()).booleanValue(), true);
/* 651 */               this.manualTimer.reset();
/*     */             } 
/*     */             break;
/*     */           case PLACE:
/* 655 */             mousePos = new BlockPos(mc.field_71476_x.func_178782_a().func_177958_n(), mc.field_71476_x.func_178782_a().func_177956_o() + 1.0D, mc.field_71476_x.func_178782_a().func_177952_p());
/* 656 */             for (Entity target : mc.field_71441_e.func_72839_b(null, new AxisAlignedBB(mousePos))) {
/* 657 */               if (target instanceof net.minecraft.entity.item.EntityEnderCrystal) {
/* 658 */                 EntityUtil.attackEntity(target, ((Boolean)this.sync.getValue()).booleanValue(), true);
/* 659 */                 this.manualTimer.reset();
/*     */               } 
/*     */             } 
/*     */             break;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   private void rotateTo(Entity entity) {
/*     */     float[] angle;
/* 669 */     switch ((Rotate)this.rotate.getValue()) {
/*     */       case OFF:
/* 671 */         this.rotating = false;
/*     */         break;
/*     */       
/*     */       case BREAK:
/*     */       case ALL:
/* 676 */         angle = MathUtil.calcAngle(mc.field_71439_g.func_174824_e(mc.func_184121_ak()), entity.func_174791_d());
/* 677 */         if (((Integer)this.eventMode.getValue()).intValue() == 2) {
/* 678 */           Phobos.rotationManager.setPlayerRotations(angle[0], angle[1]); break;
/*     */         } 
/* 680 */         this.yaw = angle[0];
/* 681 */         this.pitch = angle[1];
/* 682 */         this.rotating = true;
/*     */         break;
/*     */     } 
/*     */   }
/*     */   private void rotateToPos(BlockPos pos) {
/*     */     float[] angle;
/* 688 */     switch ((Rotate)this.rotate.getValue()) {
/*     */       case OFF:
/* 690 */         this.rotating = false;
/*     */         break;
/*     */       
/*     */       case PLACE:
/*     */       case ALL:
/* 695 */         angle = MathUtil.calcAngle(mc.field_71439_g.func_174824_e(mc.func_184121_ak()), new Vec3d((pos.func_177958_n() + 0.5F), (pos.func_177956_o() - 0.5F), (pos.func_177952_p() + 0.5F)));
/* 696 */         if (((Integer)this.eventMode.getValue()).intValue() == 2) {
/* 697 */           Phobos.rotationManager.setPlayerRotations(angle[0], angle[1]); break;
/*     */         } 
/* 699 */         this.yaw = angle[0];
/* 700 */         this.pitch = angle[1];
/* 701 */         this.rotating = true;
/*     */         break;
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean isValid(Entity entity) {
/* 707 */     return (entity != null && mc.field_71439_g.func_70068_e(entity) <= MathUtil.square(((Float)this.breakRange.getValue()).floatValue()) && (this.raytrace
/* 708 */       .getValue() == Raytrace.NONE || this.raytrace.getValue() == Raytrace.PLACE || mc.field_71439_g
/* 709 */       .func_70685_l(entity) || (
/* 710 */       !mc.field_71439_g.func_70685_l(entity) && mc.field_71439_g.func_70068_e(entity) <= MathUtil.square(((Float)this.breaktrace.getValue()).floatValue()))));
/*     */   }
/*     */   
/*     */   public enum Settings {
/* 714 */     PLACE,
/* 715 */     BREAK,
/* 716 */     RENDER,
/* 717 */     MISC;
/*     */   }
/*     */   
/*     */   public enum DamageSync {
/* 721 */     NONE,
/* 722 */     PLACE,
/* 723 */     BREAK;
/*     */   }
/*     */   
/*     */   public enum Rotate {
/* 727 */     OFF,
/* 728 */     PLACE,
/* 729 */     BREAK,
/* 730 */     ALL;
/*     */   }
/*     */   
/*     */   public enum Target {
/* 734 */     CLOSEST,
/* 735 */     UNSAFE,
/* 736 */     DAMAGE;
/*     */   }
/*     */   
/*     */   public enum Logic {
/* 740 */     BREAKPLACE,
/* 741 */     PLACEBREAK;
/*     */   }
/*     */   
/*     */   public enum Switch {
/* 745 */     ALWAYS,
/* 746 */     BREAKSLOT,
/* 747 */     CALC;
/*     */   }
/*     */   
/*     */   public enum Raytrace {
/* 751 */     NONE,
/* 752 */     PLACE,
/* 753 */     BREAK,
/* 754 */     FULL;
/*     */   }
/*     */   
/*     */   public enum AutoSwitch {
/* 758 */     NONE,
/* 759 */     TOGGLE,
/* 760 */     ALWAYS;
/*     */   }
/*     */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\me\earth\phobos\features\modules\combat\AutoCrystal.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */