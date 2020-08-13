/*      */ package me.earth.phobos.features.modules.combat;
/*      */ 
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collection;
/*      */ import java.util.Comparator;
/*      */ import java.util.List;
/*      */ import java.util.stream.Collectors;
/*      */ import me.earth.phobos.event.events.ClientEvent;
/*      */ import me.earth.phobos.event.events.PacketEvent;
/*      */ import me.earth.phobos.event.events.UpdateWalkingPlayerEvent;
/*      */ import me.earth.phobos.features.command.Command;
/*      */ import me.earth.phobos.features.modules.Module;
/*      */ import me.earth.phobos.features.modules.player.Freecam;
/*      */ import me.earth.phobos.features.setting.Bind;
/*      */ import me.earth.phobos.features.setting.Setting;
/*      */ import me.earth.phobos.util.BlockUtil;
/*      */ import me.earth.phobos.util.EntityUtil;
/*      */ import me.earth.phobos.util.InventoryUtil;
/*      */ import me.earth.phobos.util.MathUtil;
/*      */ import me.earth.phobos.util.RotationUtil;
/*      */ import me.earth.phobos.util.Timer;
/*      */ import net.minecraft.block.Block;
/*      */ import net.minecraft.block.BlockDispenser;
/*      */ import net.minecraft.block.BlockHopper;
/*      */ import net.minecraft.block.BlockObsidian;
/*      */ import net.minecraft.block.BlockShulkerBox;
/*      */ import net.minecraft.entity.Entity;
/*      */ import net.minecraft.entity.player.EntityPlayer;
/*      */ import net.minecraft.init.Blocks;
/*      */ import net.minecraft.init.Items;
/*      */ import net.minecraft.inventory.ClickType;
/*      */ import net.minecraft.inventory.Slot;
/*      */ import net.minecraft.item.ItemBlock;
/*      */ import net.minecraft.item.ItemPickaxe;
/*      */ import net.minecraft.network.Packet;
/*      */ import net.minecraft.network.play.client.CPacketEntityAction;
/*      */ import net.minecraft.network.play.client.CPacketPlayer;
/*      */ import net.minecraft.util.EnumFacing;
/*      */ import net.minecraft.util.EnumHand;
/*      */ import net.minecraft.util.NonNullList;
/*      */ import net.minecraft.util.math.AxisAlignedBB;
/*      */ import net.minecraft.util.math.BlockPos;
/*      */ import net.minecraft.util.math.RayTraceResult;
/*      */ import net.minecraft.util.math.Vec3d;
/*      */ import net.minecraft.util.math.Vec3i;
/*      */ import net.minecraftforge.client.event.GuiOpenEvent;
/*      */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*      */ import net.minecraftforge.fml.common.gameevent.InputEvent;
/*      */ import org.lwjgl.input.Keyboard;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class Auto32k
/*      */   extends Module
/*      */ {
/*   63 */   private final Setting<Mode> mode = register(new Setting("Mode", Mode.NORMAL));
/*   64 */   private final Setting<Integer> delay = register(new Setting("Delay/Place", Integer.valueOf(25), Integer.valueOf(0), Integer.valueOf(250)));
/*   65 */   private final Setting<Integer> delayDispenser = register(new Setting("Blocks/Place", Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(8), v -> (this.mode.getValue() != Mode.NORMAL)));
/*   66 */   private final Setting<Integer> blocksPerPlace = register(new Setting("Actions/Place", Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(3), v -> (this.mode.getValue() == Mode.NORMAL)));
/*   67 */   private final Setting<Float> range = register(new Setting("PlaceRange", Float.valueOf(4.5F), Float.valueOf(0.0F), Float.valueOf(6.0F)));
/*   68 */   private final Setting<Boolean> raytrace = register(new Setting("Raytrace", Boolean.valueOf(false)));
/*   69 */   private final Setting<Boolean> rotate = register(new Setting("Rotate", Boolean.valueOf(false)));
/*   70 */   public Setting<Boolean> autoSwitch = register(new Setting("AutoSwitch", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.NORMAL)));
/*   71 */   public Setting<Boolean> withBind = register(new Setting("WithBind", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.NORMAL && ((Boolean)this.autoSwitch.getValue()).booleanValue())));
/*   72 */   public Setting<Bind> switchBind = register(new Setting("SwitchBind", new Bind(-1), v -> (((Boolean)this.autoSwitch.getValue()).booleanValue() && this.mode.getValue() == Mode.NORMAL && ((Boolean)this.withBind.getValue()).booleanValue())));
/*   73 */   private final Setting<Double> targetRange = register(new Setting("TargetRange", Double.valueOf(6.0D), Double.valueOf(0.0D), Double.valueOf(20.0D)));
/*   74 */   private final Setting<Boolean> extra = register(new Setting("ExtraRotation", Boolean.valueOf(false), v -> (((Boolean)this.rotate.getValue()).booleanValue() && ((Integer)this.blocksPerPlace.getValue()).intValue() > 1)));
/*   75 */   private final Setting<PlaceType> placeType = register(new Setting("Place", PlaceType.CLOSE));
/*   76 */   private final Setting<Boolean> freecam = register(new Setting("Freecam", Boolean.valueOf(false)));
/*   77 */   private final Setting<Boolean> onOtherHoppers = register(new Setting("UseHoppers", Boolean.valueOf(false)));
/*   78 */   private final Setting<Boolean> preferObby = register(new Setting("UseObby", Boolean.valueOf(false), v -> (this.mode.getValue() != Mode.NORMAL)));
/*   79 */   private final Setting<Boolean> messages = register(new Setting("Messages", Boolean.valueOf(false)));
/*   80 */   private final Setting<Boolean> checkForShulker = register(new Setting("CheckShulker", Boolean.valueOf(true)));
/*   81 */   private final Setting<Integer> checkDelay = register(new Setting("CheckDelay", Integer.valueOf(500), Integer.valueOf(0), Integer.valueOf(500), v -> ((Boolean)this.checkForShulker.getValue()).booleanValue()));
/*   82 */   private final Setting<Boolean> drop = register(new Setting("Drop", Boolean.valueOf(false)));
/*   83 */   private final Setting<Boolean> checkStatus = register(new Setting("CheckState", Boolean.valueOf(true)));
/*   84 */   private final Setting<Boolean> packet = register(new Setting("Packet", Boolean.valueOf(false)));
/*   85 */   private final Setting<Boolean> repeatSwitch = register(new Setting("SwitchOnFail", Boolean.valueOf(true)));
/*   86 */   private final Setting<Boolean> cancelNextRotation = register(new Setting("AntiPacket", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.DISPENSER)));
/*   87 */   private final Setting<Boolean> mine = register(new Setting("Mine", Boolean.valueOf(false), v -> ((Boolean)this.drop.getValue()).booleanValue()));
/*   88 */   private final Setting<Float> hopperDistance = register(new Setting("HopperRange", Float.valueOf(8.0F), Float.valueOf(0.0F), Float.valueOf(20.0F)));
/*   89 */   private final Setting<Integer> trashSlot = register(new Setting("32kSlot", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(9)));
/*      */   
/*      */   private float yaw;
/*      */   
/*      */   private float pitch;
/*      */   private boolean spoof;
/*      */   public boolean switching;
/*   96 */   private int lastHotbarSlot = -1;
/*      */   
/*   98 */   private int shulkerSlot = -1;
/*   99 */   private int hopperSlot = -1;
/*      */   
/*      */   private BlockPos hopperPos;
/*      */   private EntityPlayer target;
/*  103 */   public Step currentStep = Step.PRE;
/*  104 */   private final Timer placeTimer = new Timer();
/*      */   
/*      */   private static Auto32k instance;
/*  107 */   private int obbySlot = -1;
/*  108 */   private int dispenserSlot = -1;
/*  109 */   private int redstoneSlot = -1;
/*      */   private DispenserData finalDispenserData;
/*  111 */   private int actionsThisTick = 0;
/*      */   private boolean checkedThisTick = false;
/*      */   private boolean nextRotCanceled = false;
/*      */   
/*      */   public Auto32k() {
/*  116 */     super("Auto32k", "Auto32ks", Module.Category.COMBAT, false, false, true);
/*  117 */     instance = this;
/*      */   }
/*      */   
/*      */   public static Auto32k getInstance() {
/*  121 */     if (instance == null) {
/*  122 */       instance = new Auto32k();
/*      */     }
/*  124 */     return instance;
/*      */   }
/*      */ 
/*      */   
/*      */   public void onToggle() {
/*  129 */     this.checkedThisTick = false;
/*  130 */     resetFields();
/*  131 */     if (isOn() && 
/*  132 */       mc.field_71462_r instanceof net.minecraft.client.gui.GuiHopper) {
/*  133 */       this.currentStep = Step.HOPPERGUI;
/*      */     }
/*      */ 
/*      */     
/*  137 */     if (this.mode.getValue() == Mode.NORMAL && ((Boolean)this.autoSwitch.getValue()).booleanValue() && !((Boolean)this.withBind.getValue()).booleanValue()) {
/*  138 */       this.switching = true;
/*      */     }
/*      */   }
/*      */   
/*      */   @SubscribeEvent
/*      */   public void onUpdateWalkingPlayer(UpdateWalkingPlayerEvent event) {
/*  144 */     if (event.getStage() != 0) {
/*      */       return;
/*      */     }
/*      */     
/*  148 */     this.checkedThisTick = false;
/*  149 */     this.actionsThisTick = 0;
/*  150 */     if (isOff() || (this.mode.getValue() == Mode.NORMAL && ((Boolean)this.autoSwitch.getValue()).booleanValue() && !this.switching)) {
/*      */       return;
/*      */     }
/*      */     
/*  154 */     if (this.mode.getValue() == Mode.NORMAL) {
/*  155 */       normal32k();
/*      */     } else {
/*  157 */       processDispenser32k();
/*      */     } 
/*      */   }
/*      */   
/*      */   @SubscribeEvent
/*      */   public void onGui(GuiOpenEvent event) {
/*  163 */     if (isOff()) {
/*      */       return;
/*      */     }
/*      */     
/*  167 */     if (mc.field_71462_r instanceof net.minecraft.client.gui.GuiHopper) {
/*  168 */       if (((Boolean)this.drop.getValue()).booleanValue() && mc.field_71439_g.func_184614_ca().func_77973_b() == Items.field_151048_u && this.hopperPos != null) {
/*  169 */         mc.field_71439_g.func_71040_bB(true);
/*  170 */         if (((Boolean)this.mine.getValue()).booleanValue() && this.hopperPos != null) {
/*      */           
/*  172 */           int pickaxeSlot = InventoryUtil.findHotbarBlock(ItemPickaxe.class);
/*  173 */           if (pickaxeSlot != -1) {
/*  174 */             InventoryUtil.switchToHotbarSlot(pickaxeSlot, false);
/*  175 */             if (((Boolean)this.rotate.getValue()).booleanValue()) {
/*  176 */               rotateToPos(this.hopperPos.func_177984_a(), (Vec3d)null);
/*      */             }
/*  178 */             mc.field_71442_b.func_180512_c(this.hopperPos.func_177984_a(), mc.field_71439_g.func_174811_aO());
/*  179 */             mc.field_71442_b.func_180512_c(this.hopperPos.func_177984_a(), mc.field_71439_g.func_174811_aO());
/*  180 */             mc.field_71439_g.func_184609_a(EnumHand.MAIN_HAND);
/*      */           } 
/*      */         } 
/*      */       } 
/*      */       
/*  185 */       resetFields();
/*  186 */       if (this.mode.getValue() != Mode.NORMAL) {
/*  187 */         disable();
/*      */         return;
/*      */       } 
/*  190 */       if (!((Boolean)this.autoSwitch.getValue()).booleanValue()) {
/*  191 */         disable();
/*  192 */       } else if (!((Boolean)this.withBind.getValue()).booleanValue()) {
/*  193 */         disable();
/*      */       } 
/*  195 */     } else if (event.getGui() instanceof net.minecraft.client.gui.GuiHopper) {
/*  196 */       this.currentStep = Step.HOPPERGUI;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public String getDisplayInfo() {
/*  202 */     if (this.switching) {
/*  203 */       return "§aSwitch";
/*      */     }
/*  205 */     return null;
/*      */   }
/*      */   
/*      */   @SubscribeEvent
/*      */   public void onKeyInput(InputEvent.KeyInputEvent event) {
/*  210 */     if (isOff()) {
/*      */       return;
/*      */     }
/*      */     
/*  214 */     if (Keyboard.getEventKeyState() && !(mc.field_71462_r instanceof me.earth.phobos.features.gui.PhobosGui) && ((Bind)this.switchBind.getValue()).getKey() == Keyboard.getEventKey() && ((Boolean)this.withBind.getValue()).booleanValue()) {
/*  215 */       if (this.switching) {
/*  216 */         resetFields();
/*  217 */         this.switching = true;
/*      */       } 
/*  219 */       this.switching = !this.switching;
/*      */     } 
/*      */   }
/*      */   
/*      */   @SubscribeEvent
/*      */   public void onSettingChange(ClientEvent event) {
/*  225 */     if (event.getStage() == 2) {
/*  226 */       Setting setting = event.getSetting();
/*  227 */       if (setting != null && setting.getFeature().equals(this) && setting.equals(this.mode)) {
/*  228 */         resetFields();
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   @SubscribeEvent
/*      */   public void onPacketSend(PacketEvent.Send event) {
/*  235 */     if (isOff()) {
/*      */       return;
/*      */     }
/*      */     
/*  239 */     if (event.getPacket() instanceof CPacketPlayer) {
/*  240 */       if (this.nextRotCanceled && event.getPacket() instanceof CPacketPlayer.Rotation) {
/*  241 */         event.setCanceled(true);
/*  242 */         this.nextRotCanceled = false;
/*      */       } 
/*  244 */       if (this.spoof) {
/*  245 */         CPacketPlayer packet = (CPacketPlayer)event.getPacket();
/*  246 */         packet.field_149476_e = this.yaw;
/*  247 */         packet.field_149473_f = this.pitch;
/*  248 */         this.spoof = false;
/*      */       } 
/*  250 */     } else if (event.getPacket() instanceof net.minecraft.network.play.client.CPacketCloseWindow && 
/*  251 */       mc.field_71462_r instanceof net.minecraft.client.gui.GuiHopper && this.hopperPos != null) {
/*  252 */       if (((Boolean)this.drop.getValue()).booleanValue() && mc.field_71439_g.func_184614_ca().func_77973_b() == Items.field_151048_u) {
/*  253 */         mc.field_71439_g.func_71040_bB(true);
/*  254 */         if (((Boolean)this.mine.getValue()).booleanValue()) {
/*      */           
/*  256 */           int pickaxeSlot = InventoryUtil.findHotbarBlock(ItemPickaxe.class);
/*  257 */           if (pickaxeSlot != -1) {
/*  258 */             InventoryUtil.switchToHotbarSlot(pickaxeSlot, false);
/*  259 */             if (((Boolean)this.rotate.getValue()).booleanValue()) {
/*  260 */               rotateToPos(this.hopperPos.func_177984_a(), (Vec3d)null);
/*      */             }
/*  262 */             mc.field_71442_b.func_180512_c(this.hopperPos.func_177984_a(), mc.field_71439_g.func_174811_aO());
/*  263 */             mc.field_71442_b.func_180512_c(this.hopperPos.func_177984_a(), mc.field_71439_g.func_174811_aO());
/*  264 */             mc.field_71439_g.func_184609_a(EnumHand.MAIN_HAND);
/*      */           } 
/*      */         } 
/*      */       } 
/*      */       
/*  269 */       resetFields();
/*  270 */       if (!((Boolean)this.autoSwitch.getValue()).booleanValue()) {
/*  271 */         disable();
/*  272 */       } else if (!((Boolean)this.withBind.getValue()).booleanValue()) {
/*  273 */         disable();
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void normal32k() {
/*  284 */     if (((Boolean)this.autoSwitch.getValue()).booleanValue()) {
/*  285 */       if (this.switching) {
/*  286 */         processNormal32k();
/*      */       } else {
/*  288 */         resetFields();
/*      */       } 
/*      */     } else {
/*  291 */       processNormal32k();
/*      */     } 
/*      */   }
/*      */   
/*      */   private void processNormal32k() {
/*  296 */     if (this.placeTimer.passedMs(((Integer)this.delay.getValue()).intValue())) {
/*  297 */       check();
/*  298 */       switch (this.currentStep) {
/*      */         case MOUSE:
/*  300 */           runPreStep();
/*      */         case CLOSE:
/*  302 */           if (this.currentStep == Step.HOPPER) {
/*  303 */             checkState();
/*  304 */             if (this.currentStep == Step.PRE) {
/*  305 */               if (this.checkedThisTick) {
/*  306 */                 processNormal32k();
/*      */               }
/*      */               return;
/*      */             } 
/*  310 */             runHopperStep();
/*  311 */             if (this.actionsThisTick >= ((Integer)this.blocksPerPlace.getValue()).intValue() && !this.placeTimer.passedMs(((Integer)this.delay.getValue()).intValue())) {
/*      */               return;
/*      */             }
/*      */           } 
/*      */         case ENEMY:
/*  316 */           checkState();
/*  317 */           if (this.currentStep == Step.PRE) {
/*  318 */             if (this.checkedThisTick) {
/*  319 */               processNormal32k();
/*      */             }
/*      */             return;
/*      */           } 
/*  323 */           runShulkerStep();
/*  324 */           if (this.actionsThisTick >= ((Integer)this.blocksPerPlace.getValue()).intValue() && !this.placeTimer.passedMs(((Integer)this.delay.getValue()).intValue())) {
/*      */             return;
/*      */           }
/*      */         case MIDDLE:
/*  328 */           checkState();
/*  329 */           if (this.currentStep == Step.PRE) {
/*  330 */             if (this.checkedThisTick) {
/*  331 */               processNormal32k();
/*      */             }
/*      */             return;
/*      */           } 
/*  335 */           runClickHopper();
/*      */         case FAR:
/*  337 */           runHopperGuiStep();
/*      */           return;
/*      */       } 
/*  340 */       Command.sendMessage("§cThis shouldnt happen, report to 3arthqu4ke!!!");
/*  341 */       Command.sendMessage("§cThis shouldnt happen, report to 3arthqu4ke!!!");
/*  342 */       Command.sendMessage("§cThis shouldnt happen, report to 3arthqu4ke!!!");
/*  343 */       Command.sendMessage("§cThis shouldnt happen, report to 3arthqu4ke!!!");
/*  344 */       Command.sendMessage("§cThis shouldnt happen, report to 3arthqu4ke!!!");
/*  345 */       this.currentStep = Step.PRE;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void runPreStep() {
/*  352 */     PlaceType type = (PlaceType)this.placeType.getValue();
/*      */     
/*  354 */     if (Freecam.getInstance().isOn() && !((Boolean)this.freecam.getValue()).booleanValue()) {
/*  355 */       if (((Boolean)this.messages.getValue()).booleanValue()) {
/*  356 */         Command.sendMessage("§c<Auto32k> Disable Freecam.");
/*      */       }
/*  358 */       if (((Boolean)this.autoSwitch.getValue()).booleanValue()) {
/*  359 */         resetFields();
/*  360 */         if (!((Boolean)this.withBind.getValue()).booleanValue()) {
/*  361 */           disable();
/*      */         }
/*      */       } else {
/*  364 */         disable();
/*      */       } 
/*      */       
/*      */       return;
/*      */     } 
/*  369 */     this.lastHotbarSlot = mc.field_71439_g.field_71071_by.field_70461_c;
/*  370 */     this.hopperSlot = InventoryUtil.findHotbarBlock(BlockHopper.class);
/*  371 */     this.shulkerSlot = InventoryUtil.findHotbarBlock(BlockShulkerBox.class);
/*      */     
/*  373 */     if (mc.field_71439_g.func_184592_cb().func_77973_b() instanceof ItemBlock) {
/*  374 */       Block block = ((ItemBlock)mc.field_71439_g.func_184592_cb().func_77973_b()).func_179223_d();
/*  375 */       if (block instanceof BlockShulkerBox) {
/*  376 */         this.shulkerSlot = -2;
/*  377 */       } else if (block instanceof BlockHopper) {
/*  378 */         this.hopperSlot = -2;
/*      */       } 
/*      */     } 
/*      */     
/*  382 */     if (this.shulkerSlot == -1 || this.hopperSlot == -1) {
/*  383 */       if (((Boolean)this.messages.getValue()).booleanValue()) {
/*  384 */         Command.sendMessage("§c<Auto32k> Materials not found.");
/*      */       }
/*  386 */       if (((Boolean)this.autoSwitch.getValue()).booleanValue()) {
/*  387 */         resetFields();
/*  388 */         if (!((Boolean)this.withBind.getValue()).booleanValue()) {
/*  389 */           disable();
/*      */         }
/*      */       } else {
/*  392 */         disable();
/*      */       } 
/*      */       
/*      */       return;
/*      */     } 
/*  397 */     this.target = EntityUtil.getClosestEnemy(((Double)this.targetRange.getValue()).doubleValue());
/*  398 */     if (this.target == null) {
/*  399 */       if (((Boolean)this.autoSwitch.getValue()).booleanValue()) {
/*  400 */         if (this.switching) {
/*  401 */           resetFields();
/*  402 */           this.switching = true;
/*      */         } else {
/*  404 */           resetFields();
/*      */         } 
/*      */         return;
/*      */       } 
/*  408 */       type = (this.placeType.getValue() == PlaceType.MOUSE) ? PlaceType.MOUSE : PlaceType.CLOSE;
/*      */     } 
/*      */     
/*  411 */     this.hopperPos = findBestPos(type, this.target);
/*  412 */     if (this.hopperPos != null) {
/*  413 */       if (mc.field_71441_e.func_180495_p(this.hopperPos).func_177230_c() instanceof BlockHopper) {
/*  414 */         this.currentStep = Step.SHULKER;
/*      */       } else {
/*  416 */         this.currentStep = Step.HOPPER;
/*      */       } 
/*      */     } else {
/*  419 */       if (((Boolean)this.messages.getValue()).booleanValue()) {
/*  420 */         Command.sendMessage("§c<Auto32k> Block not found.");
/*      */       }
/*  422 */       if (((Boolean)this.autoSwitch.getValue()).booleanValue()) {
/*  423 */         resetFields();
/*  424 */         if (!((Boolean)this.withBind.getValue()).booleanValue()) {
/*  425 */           disable();
/*      */         }
/*      */       } else {
/*  428 */         disable();
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   private void runHopperStep() {
/*  434 */     if (this.currentStep == Step.HOPPER) {
/*  435 */       runPlaceStep(this.hopperPos, this.hopperSlot);
/*  436 */       this.currentStep = Step.SHULKER;
/*      */     } 
/*      */   }
/*      */   
/*      */   private void runShulkerStep() {
/*  441 */     if (this.currentStep == Step.SHULKER) {
/*  442 */       runPlaceStep(this.hopperPos.func_177984_a(), this.shulkerSlot);
/*  443 */       this.currentStep = Step.CLICKHOPPER;
/*      */     } 
/*      */   }
/*      */   
/*      */   private void runClickHopper() {
/*  448 */     if (this.currentStep != Step.CLICKHOPPER) {
/*      */       return;
/*      */     }
/*      */     
/*  452 */     if (this.mode.getValue() == Mode.NORMAL && !(mc.field_71441_e.func_180495_p(this.hopperPos.func_177984_a()).func_177230_c() instanceof BlockShulkerBox) && ((Boolean)this.checkForShulker.getValue()).booleanValue()) {
/*  453 */       if (this.placeTimer.passedMs(((Integer)this.checkDelay.getValue()).intValue())) {
/*  454 */         this.currentStep = Step.SHULKER;
/*      */       }
/*      */       
/*      */       return;
/*      */     } 
/*  459 */     clickBlock(this.hopperPos);
/*  460 */     this.currentStep = Step.HOPPERGUI;
/*      */   }
/*      */   
/*      */   private void runHopperGuiStep() {
/*  464 */     if (mc.field_71439_g.field_71070_bA instanceof net.minecraft.inventory.ContainerHopper && this.currentStep == Step.HOPPERGUI && 
/*  465 */       !EntityUtil.holding32k((EntityPlayer)mc.field_71439_g)) {
/*  466 */       int swordIndex = -1;
/*  467 */       for (int i = 0; i < 5; i++) {
/*  468 */         if (EntityUtil.is32k(((Slot)mc.field_71439_g.field_71070_bA.field_75151_b.get(0)).field_75224_c.func_70301_a(i))) {
/*  469 */           swordIndex = i;
/*      */           
/*      */           break;
/*      */         } 
/*      */       } 
/*  474 */       if (swordIndex == -1) {
/*      */         return;
/*      */       }
/*      */       
/*  478 */       if (((Integer)this.trashSlot.getValue()).intValue() != 0) {
/*  479 */         InventoryUtil.switchToHotbarSlot(((Integer)this.trashSlot.getValue()).intValue() - 1, false);
/*      */       }
/*  481 */       else if (this.mode.getValue() != Mode.NORMAL && this.shulkerSlot > 35 && this.shulkerSlot != 45) {
/*  482 */         InventoryUtil.switchToHotbarSlot(44 - this.shulkerSlot, false);
/*      */       } 
/*      */       
/*  485 */       mc.field_71442_b.func_187098_a(mc.field_71439_g.field_71070_bA.field_75152_c, swordIndex, (((Integer)this.trashSlot.getValue()).intValue() == 0) ? mc.field_71439_g.field_71071_by.field_70461_c : (((Integer)this.trashSlot.getValue()).intValue() - 1), ClickType.SWAP, (EntityPlayer)mc.field_71439_g);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void runPlaceStep(BlockPos pos, int slot) {
/*  492 */     EnumFacing side = BlockUtil.getFirstFacing(pos);
/*  493 */     if (side == null) {
/*      */       return;
/*      */     }
/*      */     
/*  497 */     BlockPos neighbour = pos.func_177972_a(side);
/*  498 */     EnumFacing opposite = side.func_176734_d();
/*  499 */     Vec3d hitVec = (new Vec3d((Vec3i)neighbour)).func_72441_c(0.5D, 0.5D, 0.5D).func_178787_e((new Vec3d(opposite.func_176730_m())).func_186678_a(0.5D));
/*  500 */     Block neighbourBlock = mc.field_71441_e.func_180495_p(neighbour).func_177230_c();
/*      */     
/*  502 */     if (!mc.field_71439_g.func_70093_af() && (BlockUtil.blackList.contains(neighbourBlock) || BlockUtil.shulkerList.contains(neighbourBlock))) {
/*  503 */       mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketEntityAction((Entity)mc.field_71439_g, CPacketEntityAction.Action.START_SNEAKING));
/*  504 */       mc.field_71439_g.func_70095_a(true);
/*      */     } 
/*      */     
/*  507 */     if (((Boolean)this.rotate.getValue()).booleanValue()) {
/*  508 */       if (((Integer)this.blocksPerPlace.getValue()).intValue() > 1) {
/*  509 */         float[] angle = RotationUtil.getLegitRotations(hitVec);
/*      */         
/*  511 */         if (((Boolean)this.extra.getValue()).booleanValue()) {
/*  512 */           RotationUtil.faceYawAndPitch(angle[0], angle[1]);
/*      */         }
/*      */       } else {
/*  515 */         rotateToPos((BlockPos)null, hitVec);
/*      */       } 
/*      */     }
/*      */     
/*  519 */     InventoryUtil.switchToHotbarSlot(slot, false);
/*  520 */     BlockUtil.rightClickBlock(neighbour, hitVec, (slot == -2) ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, opposite, ((Boolean)this.packet.getValue()).booleanValue());
/*  521 */     this.placeTimer.reset();
/*  522 */     this.actionsThisTick++;
/*      */   } private BlockPos findBestPos(PlaceType type, EntityPlayer target) {
/*      */     List<BlockPos> toRemove;
/*      */     NonNullList<BlockPos> copy;
/*  526 */     BlockPos pos = null;
/*  527 */     NonNullList<BlockPos> positions = NonNullList.func_191196_a();
/*  528 */     positions.addAll((Collection)BlockUtil.getSphere(EntityUtil.getPlayerPos((EntityPlayer)mc.field_71439_g), ((Float)this.range.getValue()).floatValue(), ((Float)this.range.getValue()).intValue(), false, true, 0).stream().filter(this::canPlace).collect(Collectors.toList()));
/*      */     
/*  530 */     switch (type) {
/*      */       case MOUSE:
/*  532 */         if (mc.field_71476_x.field_72313_a == RayTraceResult.Type.BLOCK) {
/*  533 */           BlockPos mousePos = mc.field_71476_x.func_178782_a();
/*  534 */           if (mousePos != null && !canPlace(mousePos)) {
/*  535 */             BlockPos mousePosUp = mousePos.func_177984_a();
/*  536 */             if (canPlace(mousePosUp)) {
/*  537 */               pos = mousePosUp;
/*      */             }
/*      */           } else {
/*  540 */             pos = mousePos;
/*      */           } 
/*      */         } 
/*  543 */         if (pos != null) {
/*      */           break;
/*      */         }
/*      */       case CLOSE:
/*  547 */         positions.sort(Comparator.comparingDouble(pos2 -> mc.field_71439_g.func_174818_b(pos2)));
/*  548 */         pos = (BlockPos)positions.get(0);
/*      */         break;
/*      */       case ENEMY:
/*  551 */         positions.sort(Comparator.comparingDouble(target::func_174818_b));
/*  552 */         pos = (BlockPos)positions.get(0);
/*      */         break;
/*      */       case MIDDLE:
/*  555 */         toRemove = new ArrayList<>();
/*  556 */         copy = NonNullList.func_191196_a();
/*  557 */         copy.addAll((Collection)positions);
/*  558 */         for (BlockPos position : copy) {
/*  559 */           double difference = mc.field_71439_g.func_174818_b(position) - target.func_174818_b(position);
/*  560 */           if (difference > 1.0D || difference < -1.0D) {
/*  561 */             toRemove.add(position);
/*      */           }
/*      */         } 
/*  564 */         copy.removeAll(toRemove);
/*  565 */         if (copy.isEmpty()) {
/*  566 */           copy.addAll((Collection)positions);
/*      */         }
/*  568 */         copy.sort(Comparator.comparingDouble(pos2 -> mc.field_71439_g.func_174818_b(pos2)));
/*  569 */         pos = (BlockPos)copy.get(0);
/*      */         break;
/*      */       case FAR:
/*  572 */         positions.sort(Comparator.comparingDouble(pos2 -> -target.func_174818_b(pos2)));
/*  573 */         pos = (BlockPos)positions.get(0);
/*      */         break;
/*      */     } 
/*      */     
/*  577 */     return pos;
/*      */   }
/*      */   
/*      */   private boolean canPlace(BlockPos pos) {
/*  581 */     if (pos == null) {
/*  582 */       return false;
/*      */     }
/*      */     
/*  585 */     BlockPos boost = pos.func_177984_a();
/*      */     
/*  587 */     if (!isGoodMaterial(mc.field_71441_e.func_180495_p(pos).func_177230_c(), ((Boolean)this.onOtherHoppers.getValue()).booleanValue()) || !isGoodMaterial(mc.field_71441_e.func_180495_p(boost).func_177230_c(), false)) {
/*  588 */       return false;
/*      */     }
/*      */     
/*  591 */     if (((Boolean)this.raytrace.getValue()).booleanValue() && (!BlockUtil.rayTracePlaceCheck(pos, ((Boolean)this.raytrace.getValue()).booleanValue()) || !BlockUtil.rayTracePlaceCheck(pos, ((Boolean)this.raytrace.getValue()).booleanValue()))) {
/*  592 */       return false;
/*      */     }
/*      */     
/*  595 */     if (!mc.field_71441_e.func_72872_a(Entity.class, new AxisAlignedBB(pos)).isEmpty() || !mc.field_71441_e.func_72872_a(Entity.class, new AxisAlignedBB(boost)).isEmpty()) {
/*  596 */       return false;
/*      */     }
/*      */     
/*  599 */     if (((Boolean)this.onOtherHoppers.getValue()).booleanValue() && mc.field_71441_e.func_180495_p(pos).func_177230_c() instanceof BlockHopper) {
/*  600 */       return true;
/*      */     }
/*      */     
/*  603 */     boolean hasGoodFacing = false;
/*  604 */     for (EnumFacing facing : EnumFacing.values()) {
/*  605 */       if (facing != EnumFacing.UP && 
/*  606 */         !mc.field_71441_e.func_180495_p(pos.func_177972_a(facing)).func_185904_a().func_76222_j()) {
/*  607 */         hasGoodFacing = true;
/*      */         
/*      */         break;
/*      */       } 
/*      */     } 
/*      */     
/*  613 */     return hasGoodFacing;
/*      */   }
/*      */   
/*      */   private void check() {
/*  617 */     if (this.currentStep != Step.PRE && this.currentStep != Step.HOPPER && this.hopperPos != null && !(mc.field_71462_r instanceof net.minecraft.client.gui.GuiHopper) && !EntityUtil.holding32k((EntityPlayer)mc.field_71439_g) && (mc.field_71439_g.func_174818_b(this.hopperPos) > MathUtil.square(((Float)this.hopperDistance.getValue()).floatValue()) || mc.field_71441_e.func_180495_p(this.hopperPos).func_177230_c() != Blocks.field_150438_bZ)) {
/*  618 */       resetFields();
/*  619 */       if (!((Boolean)this.autoSwitch.getValue()).booleanValue() || !((Boolean)this.withBind.getValue()).booleanValue() || this.mode.getValue() != Mode.NORMAL) {
/*  620 */         disable();
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   private void checkState() {
/*  626 */     if (!((Boolean)this.checkStatus.getValue()).booleanValue() || this.checkedThisTick || (this.currentStep != Step.HOPPER && this.currentStep != Step.SHULKER && this.currentStep != Step.CLICKHOPPER)) {
/*  627 */       this.checkedThisTick = false;
/*      */       
/*      */       return;
/*      */     } 
/*  631 */     if (this.hopperPos == null || !isGoodMaterial(mc.field_71441_e.func_180495_p(this.hopperPos).func_177230_c(), true) || (!isGoodMaterial(mc.field_71441_e.func_180495_p(this.hopperPos.func_177984_a()).func_177230_c(), false) && !(mc.field_71441_e.func_180495_p(this.hopperPos.func_177984_a()).func_177230_c() instanceof BlockShulkerBox)) || !mc.field_71441_e.func_72872_a(Entity.class, new AxisAlignedBB(this.hopperPos)).isEmpty() || !mc.field_71441_e.func_72872_a(Entity.class, new AxisAlignedBB(this.hopperPos.func_177984_a())).isEmpty()) {
/*  632 */       if (((Boolean)this.autoSwitch.getValue()).booleanValue()) {
/*  633 */         if (this.switching) {
/*  634 */           resetFields();
/*  635 */           if (((Boolean)this.repeatSwitch.getValue()).booleanValue()) {
/*  636 */             this.switching = true;
/*      */           }
/*      */         } else {
/*  639 */           resetFields();
/*      */         } 
/*  641 */         if (!((Boolean)this.withBind.getValue()).booleanValue()) {
/*  642 */           disable();
/*      */         }
/*      */       } else {
/*  645 */         disable();
/*      */       } 
/*  647 */       this.checkedThisTick = true;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void processDispenser32k() {
/*  656 */     if (this.placeTimer.passedMs(((Integer)this.delay.getValue()).intValue())) {
/*  657 */       check();
/*  658 */       switch (this.currentStep) {
/*      */         case MOUSE:
/*  660 */           runDispenserPreStep();
/*  661 */           if (this.currentStep == Step.PRE) {
/*      */             break;
/*      */           }
/*      */         case CLOSE:
/*  665 */           runHopperStep();
/*  666 */           this.currentStep = Step.DISPENSER;
/*  667 */           if (this.actionsThisTick >= ((Integer)this.delayDispenser.getValue()).intValue() && !this.placeTimer.passedMs(((Integer)this.delay.getValue()).intValue())) {
/*      */             break;
/*      */           }
/*      */         case null:
/*  671 */           runDispenserStep();
/*  672 */           if ((this.actionsThisTick >= ((Integer)this.delayDispenser.getValue()).intValue() && !this.placeTimer.passedMs(((Integer)this.delay.getValue()).intValue())) || (this.currentStep != Step.DISPENSER_HELPING && this.currentStep != Step.CLICK_DISPENSER)) {
/*      */             break;
/*      */           }
/*      */         case null:
/*  676 */           runDispenserStep();
/*  677 */           if ((this.actionsThisTick >= ((Integer)this.delayDispenser.getValue()).intValue() && !this.placeTimer.passedMs(((Integer)this.delay.getValue()).intValue())) || (this.currentStep != Step.CLICK_DISPENSER && this.currentStep != Step.DISPENSER_HELPING)) {
/*      */             break;
/*      */           }
/*      */         case null:
/*  681 */           clickDispenser();
/*  682 */           if (this.actionsThisTick >= ((Integer)this.delayDispenser.getValue()).intValue() && !this.placeTimer.passedMs(((Integer)this.delay.getValue()).intValue())) {
/*      */             break;
/*      */           }
/*      */         case null:
/*  686 */           dispenserGui();
/*  687 */           if (this.currentStep == Step.DISPENSER_GUI) {
/*      */             break;
/*      */           }
/*      */         case null:
/*  691 */           placeRedstone();
/*  692 */           if (this.actionsThisTick >= ((Integer)this.delayDispenser.getValue()).intValue() && !this.placeTimer.passedMs(((Integer)this.delay.getValue()).intValue())) {
/*      */             break;
/*      */           }
/*      */         case MIDDLE:
/*  696 */           runClickHopper();
/*  697 */           if (this.actionsThisTick >= ((Integer)this.delayDispenser.getValue()).intValue() && !this.placeTimer.passedMs(((Integer)this.delay.getValue()).intValue())) {
/*      */             break;
/*      */           }
/*      */         case FAR:
/*  701 */           runHopperGuiStep();
/*  702 */           if (this.actionsThisTick < ((Integer)this.delayDispenser.getValue()).intValue() || !this.placeTimer.passedMs(((Integer)this.delay.getValue()).intValue()));
/*      */           break;
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void placeRedstone() {
/*  712 */     if (!mc.field_71441_e.func_72872_a(Entity.class, new AxisAlignedBB(this.hopperPos.func_177984_a())).isEmpty() && !(mc.field_71441_e.func_180495_p(this.hopperPos.func_177984_a()).func_177230_c() instanceof BlockShulkerBox)) {
/*      */       return;
/*      */     }
/*      */     
/*  716 */     mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketEntityAction((Entity)mc.field_71439_g, CPacketEntityAction.Action.START_SNEAKING));
/*  717 */     mc.field_71439_g.func_70095_a(true);
/*  718 */     runPlaceStep(this.finalDispenserData.getRedStonePos(), this.redstoneSlot);
/*  719 */     this.currentStep = Step.CLICKHOPPER;
/*      */   }
/*      */   
/*      */   private void clickDispenser() {
/*  723 */     clickBlock(this.finalDispenserData.getDispenserPos());
/*  724 */     this.currentStep = Step.DISPENSER_GUI;
/*      */   }
/*      */   
/*      */   private void dispenserGui() {
/*  728 */     if (!(mc.field_71462_r instanceof net.minecraft.client.gui.inventory.GuiDispenser)) {
/*      */       return;
/*      */     }
/*      */ 
/*      */     
/*  733 */     mc.field_71442_b.func_187098_a(mc.field_71439_g.field_71070_bA.field_75152_c, this.shulkerSlot, 0, ClickType.QUICK_MOVE, (EntityPlayer)mc.field_71439_g);
/*  734 */     mc.field_71439_g.func_71053_j();
/*  735 */     this.currentStep = Step.REDSTONE;
/*      */   }
/*      */   
/*      */   private void clickBlock(BlockPos pos) {
/*  739 */     mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketEntityAction((Entity)mc.field_71439_g, CPacketEntityAction.Action.STOP_SNEAKING));
/*  740 */     mc.field_71439_g.func_70095_a(false);
/*  741 */     Vec3d hitVec = (new Vec3d((Vec3i)pos)).func_72441_c(0.5D, -0.5D, 0.5D);
/*      */     
/*  743 */     if (((Boolean)this.rotate.getValue()).booleanValue()) {
/*  744 */       rotateToPos((BlockPos)null, hitVec);
/*      */     }
/*      */     
/*  747 */     EnumFacing facing = EnumFacing.UP;
/*  748 */     if (this.finalDispenserData != null && this.finalDispenserData.getDispenserPos() != null && this.finalDispenserData.getDispenserPos().equals(pos) && pos.func_177956_o() > (new BlockPos(mc.field_71439_g.func_174791_d())).func_177984_a().func_177956_o()) {
/*  749 */       facing = EnumFacing.DOWN;
/*      */     }
/*  751 */     BlockUtil.rightClickBlock(pos, hitVec, (this.shulkerSlot == -2) ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, facing, ((Boolean)this.packet.getValue()).booleanValue());
/*  752 */     mc.field_71439_g.func_184609_a(EnumHand.MAIN_HAND);
/*  753 */     mc.field_71467_ac = 4;
/*  754 */     this.actionsThisTick++;
/*      */   }
/*      */   
/*      */   private void runDispenserStep() {
/*  758 */     if (this.finalDispenserData == null || this.finalDispenserData.getDispenserPos() == null || this.finalDispenserData.getHelpingPos() == null) {
/*  759 */       resetFields();
/*      */       
/*      */       return;
/*      */     } 
/*  763 */     if (this.currentStep != Step.DISPENSER && this.currentStep != Step.DISPENSER_HELPING) {
/*      */       return;
/*      */     }
/*      */     
/*  767 */     BlockPos dispenserPos = this.finalDispenserData.getDispenserPos();
/*  768 */     BlockPos helpingPos = this.finalDispenserData.getHelpingPos();
/*  769 */     if (mc.field_71441_e.func_180495_p(helpingPos).func_185904_a().func_76222_j()) {
/*  770 */       this.currentStep = Step.DISPENSER_HELPING;
/*  771 */       EnumFacing facing = EnumFacing.DOWN;
/*  772 */       boolean foundHelpingPos = false;
/*  773 */       for (EnumFacing enumFacing : EnumFacing.values()) {
/*  774 */         BlockPos position = helpingPos.func_177972_a(enumFacing);
/*  775 */         if (!position.equals(this.hopperPos) && 
/*  776 */           !position.equals(this.hopperPos.func_177984_a()) && 
/*  777 */           !position.equals(dispenserPos) && 
/*  778 */           !position.equals(this.finalDispenserData.getRedStonePos()) && mc.field_71439_g
/*  779 */           .func_174818_b(position) <= MathUtil.square(((Float)this.range.getValue()).floatValue()) && (
/*  780 */           !((Boolean)this.raytrace.getValue()).booleanValue() || BlockUtil.rayTracePlaceCheck(position, ((Boolean)this.raytrace.getValue()).booleanValue())) && 
/*  781 */           !mc.field_71441_e.func_180495_p(position).func_185904_a().func_76222_j()) {
/*  782 */           foundHelpingPos = true;
/*  783 */           facing = enumFacing;
/*      */           
/*      */           break;
/*      */         } 
/*      */       } 
/*  788 */       if (!foundHelpingPos) {
/*  789 */         disable();
/*      */         
/*      */         return;
/*      */       } 
/*  793 */       BlockPos neighbour = helpingPos.func_177972_a(facing);
/*  794 */       EnumFacing opposite = facing.func_176734_d();
/*  795 */       Vec3d hitVec = (new Vec3d((Vec3i)neighbour)).func_72441_c(0.5D, 0.5D, 0.5D).func_178787_e((new Vec3d(opposite.func_176730_m())).func_186678_a(0.5D));
/*  796 */       Block neighbourBlock = mc.field_71441_e.func_180495_p(neighbour).func_177230_c();
/*      */       
/*  798 */       if (!mc.field_71439_g.func_70093_af() && (BlockUtil.blackList.contains(neighbourBlock) || BlockUtil.shulkerList.contains(neighbourBlock))) {
/*  799 */         mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketEntityAction((Entity)mc.field_71439_g, CPacketEntityAction.Action.START_SNEAKING));
/*  800 */         mc.field_71439_g.func_70095_a(true);
/*      */       } 
/*      */       
/*  803 */       if (((Boolean)this.rotate.getValue()).booleanValue()) {
/*  804 */         if (((Integer)this.blocksPerPlace.getValue()).intValue() > 1) {
/*  805 */           float[] angle = RotationUtil.getLegitRotations(hitVec);
/*      */           
/*  807 */           if (((Boolean)this.extra.getValue()).booleanValue()) {
/*  808 */             RotationUtil.faceYawAndPitch(angle[0], angle[1]);
/*      */           }
/*      */         } else {
/*  811 */           rotateToPos((BlockPos)null, hitVec);
/*      */         } 
/*      */       }
/*      */       
/*  815 */       int slot = (((Boolean)this.preferObby.getValue()).booleanValue() && this.obbySlot != -1) ? this.obbySlot : this.dispenserSlot;
/*  816 */       InventoryUtil.switchToHotbarSlot(slot, false);
/*  817 */       BlockUtil.rightClickBlock(neighbour, hitVec, (slot == -2) ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, opposite, ((Boolean)this.packet.getValue()).booleanValue());
/*  818 */       this.placeTimer.reset();
/*  819 */       this.actionsThisTick++;
/*      */       
/*      */       return;
/*      */     } 
/*  823 */     placeDispenserAgainstBlock(dispenserPos, helpingPos);
/*  824 */     this.actionsThisTick++;
/*  825 */     this.currentStep = Step.CLICK_DISPENSER;
/*      */   }
/*      */   
/*      */   private void placeDispenserAgainstBlock(BlockPos dispenserPos, BlockPos helpingPos) {
/*  829 */     EnumFacing facing = EnumFacing.DOWN;
/*  830 */     for (EnumFacing enumFacing : EnumFacing.values()) {
/*  831 */       BlockPos position = dispenserPos.func_177972_a(enumFacing);
/*  832 */       if (position.equals(helpingPos)) {
/*  833 */         facing = enumFacing;
/*      */         
/*      */         break;
/*      */       } 
/*      */     } 
/*  838 */     EnumFacing opposite = facing.func_176734_d();
/*  839 */     Vec3d hitVec = (new Vec3d((Vec3i)helpingPos)).func_72441_c(0.5D, 0.5D, 0.5D).func_178787_e((new Vec3d(opposite.func_176730_m())).func_186678_a(0.5D));
/*  840 */     Block neighbourBlock = mc.field_71441_e.func_180495_p(helpingPos).func_177230_c();
/*  841 */     if (!mc.field_71439_g.func_70093_af() && (BlockUtil.blackList.contains(neighbourBlock) || BlockUtil.shulkerList.contains(neighbourBlock))) {
/*  842 */       mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketEntityAction((Entity)mc.field_71439_g, CPacketEntityAction.Action.START_SNEAKING));
/*  843 */       mc.field_71439_g.func_70095_a(true);
/*      */     } 
/*      */     
/*  846 */     Vec3d rotationVec = null;
/*  847 */     EnumFacing facings = EnumFacing.UP;
/*  848 */     if (((Boolean)this.rotate.getValue()).booleanValue()) {
/*  849 */       if (((Integer)this.blocksPerPlace.getValue()).intValue() > 1) {
/*  850 */         float[] arrayOfFloat = RotationUtil.getLegitRotations(hitVec);
/*      */         
/*  852 */         if (((Boolean)this.extra.getValue()).booleanValue()) {
/*  853 */           RotationUtil.faceYawAndPitch(arrayOfFloat[0], arrayOfFloat[1]);
/*      */         }
/*      */       } else {
/*  856 */         rotateToPos((BlockPos)null, hitVec);
/*      */       } 
/*  858 */       rotationVec = (new Vec3d((Vec3i)helpingPos)).func_72441_c(0.5D, 0.5D, 0.5D).func_178787_e((new Vec3d(opposite.func_176730_m())).func_186678_a(0.5D));
/*      */     }
/*  860 */     else if (dispenserPos.func_177956_o() <= (new BlockPos(mc.field_71439_g.func_174791_d())).func_177984_a().func_177956_o()) {
/*  861 */       for (EnumFacing enumFacing : EnumFacing.values()) {
/*  862 */         BlockPos position = this.hopperPos.func_177984_a().func_177972_a(enumFacing);
/*  863 */         if (position.equals(dispenserPos)) {
/*  864 */           facings = enumFacing;
/*      */           break;
/*      */         } 
/*      */       } 
/*  868 */       rotationVec = new Vec3d(facings.func_176730_m());
/*  869 */       rotateToPos((BlockPos)null, rotationVec);
/*      */     } else {
/*  871 */       rotationVec = new Vec3d(facings.func_176730_m());
/*      */     } 
/*      */     
/*  874 */     float[] angle = RotationUtil.getLegitRotations(hitVec);
/*  875 */     RotationUtil.faceYawAndPitch(angle[0], angle[1]);
/*  876 */     if (((Boolean)this.cancelNextRotation.getValue()).booleanValue()) {
/*  877 */       this.nextRotCanceled = true;
/*      */     }
/*      */     
/*  880 */     InventoryUtil.switchToHotbarSlot(this.dispenserSlot, false);
/*  881 */     BlockUtil.rightClickBlock(helpingPos, rotationVec, (this.dispenserSlot == -2) ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, opposite, ((Boolean)this.packet.getValue()).booleanValue());
/*  882 */     this.placeTimer.reset();
/*  883 */     this.actionsThisTick++;
/*  884 */     this.currentStep = Step.CLICK_DISPENSER;
/*      */   }
/*      */   
/*      */   private void runDispenserPreStep() {
/*  888 */     if (Freecam.getInstance().isOn() && !((Boolean)this.freecam.getValue()).booleanValue()) {
/*  889 */       if (((Boolean)this.messages.getValue()).booleanValue()) {
/*  890 */         Command.sendMessage("§c<Auto32k> Disable Freecam.");
/*      */       }
/*  892 */       disable();
/*      */       
/*      */       return;
/*      */     } 
/*  896 */     this.lastHotbarSlot = mc.field_71439_g.field_71071_by.field_70461_c;
/*  897 */     this.hopperSlot = InventoryUtil.findHotbarBlock(BlockHopper.class);
/*  898 */     this.shulkerSlot = InventoryUtil.findBlockSlotInventory(BlockShulkerBox.class, false, false);
/*  899 */     this.dispenserSlot = InventoryUtil.findHotbarBlock(BlockDispenser.class);
/*  900 */     this.redstoneSlot = InventoryUtil.findHotbarBlock(Blocks.field_150451_bX);
/*  901 */     this.obbySlot = InventoryUtil.findHotbarBlock(BlockObsidian.class);
/*      */     
/*  903 */     if (mc.field_71439_g.func_184592_cb().func_77973_b() instanceof ItemBlock) {
/*  904 */       Block block = ((ItemBlock)mc.field_71439_g.func_184592_cb().func_77973_b()).func_179223_d();
/*  905 */       if (block instanceof BlockHopper) {
/*  906 */         this.hopperSlot = -2;
/*  907 */       } else if (block instanceof BlockDispenser) {
/*  908 */         this.dispenserSlot = -2;
/*  909 */       } else if (block == Blocks.field_150451_bX) {
/*  910 */         this.redstoneSlot = -2;
/*  911 */       } else if (block instanceof BlockObsidian) {
/*  912 */         this.obbySlot = -2;
/*      */       } 
/*      */     } 
/*      */     
/*  916 */     if (this.shulkerSlot == -1 || this.hopperSlot == -1 || this.dispenserSlot == -1 || this.redstoneSlot == -1) {
/*  917 */       if (((Boolean)this.messages.getValue()).booleanValue()) {
/*  918 */         Command.sendMessage("§c<Auto32k> Materials not found.");
/*      */       }
/*  920 */       disable();
/*      */       
/*      */       return;
/*      */     } 
/*  924 */     this.finalDispenserData = findBestPos();
/*  925 */     if (this.finalDispenserData.isPlaceable()) {
/*  926 */       this.hopperPos = this.finalDispenserData.getHopperPos();
/*  927 */       if (mc.field_71441_e.func_180495_p(this.hopperPos).func_177230_c() instanceof BlockHopper) {
/*  928 */         this.currentStep = Step.DISPENSER;
/*      */       } else {
/*  930 */         this.currentStep = Step.HOPPER;
/*      */       } 
/*      */     } else {
/*  933 */       if (((Boolean)this.messages.getValue()).booleanValue()) {
/*  934 */         Command.sendMessage("§c<Auto32k> Block not found.");
/*      */       }
/*  936 */       disable();
/*      */     } 
/*      */   } private DispenserData findBestPos() {
/*      */     List<BlockPos> toRemove;
/*      */     NonNullList<BlockPos> copy;
/*  941 */     PlaceType type = (PlaceType)this.placeType.getValue();
/*  942 */     this.target = EntityUtil.getClosestEnemy(((Double)this.targetRange.getValue()).doubleValue());
/*  943 */     if (this.target == null) {
/*  944 */       type = (this.placeType.getValue() == PlaceType.MOUSE) ? PlaceType.MOUSE : PlaceType.CLOSE;
/*      */     }
/*      */     
/*  947 */     NonNullList<BlockPos> positions = NonNullList.func_191196_a();
/*  948 */     positions.addAll(BlockUtil.getSphere(EntityUtil.getPlayerPos((EntityPlayer)mc.field_71439_g), ((Float)this.range.getValue()).floatValue(), ((Float)this.range.getValue()).intValue(), false, true, 0));
/*      */     
/*  950 */     DispenserData data = new DispenserData();
/*  951 */     switch (type) {
/*      */       case MOUSE:
/*  953 */         if (mc.field_71476_x != null && mc.field_71476_x.field_72313_a == RayTraceResult.Type.BLOCK) {
/*  954 */           BlockPos mousePos = mc.field_71476_x.func_178782_a();
/*  955 */           if (mousePos != null) {
/*  956 */             data = analyzePos(mousePos);
/*  957 */             if (!data.isPlaceable()) {
/*  958 */               data = analyzePos(mousePos.func_177984_a());
/*      */             }
/*      */           } 
/*      */         } 
/*      */         
/*  963 */         if (data.isPlaceable()) {
/*  964 */           return data;
/*      */         }
/*      */       case CLOSE:
/*  967 */         positions.sort(Comparator.comparingDouble(pos2 -> mc.field_71439_g.func_174818_b(pos2)));
/*      */         break;
/*      */       case ENEMY:
/*  970 */         positions.sort(Comparator.comparingDouble(this.target::func_174818_b));
/*      */         break;
/*      */       case MIDDLE:
/*  973 */         toRemove = new ArrayList<>();
/*  974 */         copy = NonNullList.func_191196_a();
/*  975 */         copy.addAll((Collection)positions);
/*  976 */         for (BlockPos position : copy) {
/*  977 */           double difference = mc.field_71439_g.func_174818_b(position) - this.target.func_174818_b(position);
/*  978 */           if (difference > 1.0D || difference < -1.0D) {
/*  979 */             toRemove.add(position);
/*      */           }
/*      */         } 
/*  982 */         copy.removeAll(toRemove);
/*  983 */         if (copy.isEmpty()) {
/*  984 */           copy.addAll((Collection)positions);
/*      */         }
/*  986 */         copy.sort(Comparator.comparingDouble(pos2 -> mc.field_71439_g.func_174818_b(pos2)));
/*      */         break;
/*      */       case FAR:
/*  989 */         positions.sort(Comparator.comparingDouble(pos2 -> -this.target.func_174818_b(pos2)));
/*      */         break;
/*      */     } 
/*  992 */     data = findData(positions);
/*  993 */     return data;
/*      */   }
/*      */   
/*      */   private DispenserData findData(NonNullList<BlockPos> positions) {
/*  997 */     for (BlockPos position : positions) {
/*  998 */       DispenserData data = analyzePos(position);
/*  999 */       if (data.isPlaceable()) {
/* 1000 */         return data;
/*      */       }
/*      */     } 
/* 1003 */     return new DispenserData();
/*      */   }
/*      */   
/*      */   private DispenserData analyzePos(BlockPos pos) {
/* 1007 */     DispenserData data = new DispenserData(pos);
/* 1008 */     if (pos == null) {
/* 1009 */       return data;
/*      */     }
/*      */     
/* 1012 */     if (!isGoodMaterial(mc.field_71441_e.func_180495_p(pos).func_177230_c(), ((Boolean)this.onOtherHoppers.getValue()).booleanValue()) || !isGoodMaterial(mc.field_71441_e.func_180495_p(pos.func_177984_a()).func_177230_c(), false)) {
/* 1013 */       return data;
/*      */     }
/*      */     
/* 1016 */     if (((Boolean)this.raytrace.getValue()).booleanValue() && !BlockUtil.rayTracePlaceCheck(pos, ((Boolean)this.raytrace.getValue()).booleanValue())) {
/* 1017 */       return data;
/*      */     }
/*      */     
/* 1020 */     if (!mc.field_71441_e.func_72872_a(Entity.class, new AxisAlignedBB(pos)).isEmpty() || !mc.field_71441_e.func_72872_a(Entity.class, new AxisAlignedBB(pos.func_177984_a())).isEmpty()) {
/* 1021 */       return data;
/*      */     }
/*      */     
/* 1024 */     if (hasAdjancedRedstone(pos)) {
/* 1025 */       return data;
/*      */     }
/*      */     
/* 1028 */     BlockPos[] otherPositions = checkForDispenserPos(pos);
/* 1029 */     if (otherPositions[0] == null || otherPositions[1] == null || otherPositions[2] == null) {
/* 1030 */       return data;
/*      */     }
/*      */     
/* 1033 */     data.setDispenserPos(otherPositions[0]);
/* 1034 */     data.setRedStonePos(otherPositions[1]);
/* 1035 */     data.setHelpingPos(otherPositions[2]);
/* 1036 */     data.setPlaceable(true);
/* 1037 */     return data;
/*      */   }
/*      */   
/*      */   private BlockPos[] checkForDispenserPos(BlockPos posIn) {
/* 1041 */     BlockPos[] pos = new BlockPos[3];
/* 1042 */     BlockPos playerPos = new BlockPos(mc.field_71439_g.func_174791_d());
/*      */     
/* 1044 */     if (posIn.func_177956_o() < playerPos.func_177977_b().func_177956_o()) {
/* 1045 */       return pos;
/*      */     }
/*      */     
/* 1048 */     List<BlockPos> possiblePositions = getDispenserPositions(posIn);
/* 1049 */     if (posIn.func_177956_o() < playerPos.func_177956_o()) {
/* 1050 */       possiblePositions.remove(posIn.func_177984_a().func_177984_a());
/* 1051 */     } else if (posIn.func_177956_o() > playerPos.func_177956_o()) {
/* 1052 */       possiblePositions.remove(posIn.func_177976_e().func_177984_a());
/* 1053 */       possiblePositions.remove(posIn.func_177978_c().func_177984_a());
/* 1054 */       possiblePositions.remove(posIn.func_177968_d().func_177984_a());
/* 1055 */       possiblePositions.remove(posIn.func_177974_f().func_177984_a());
/*      */     } 
/*      */     
/* 1058 */     if (((Boolean)this.rotate.getValue()).booleanValue()) {
/* 1059 */       possiblePositions.sort(Comparator.comparingDouble(pos2 -> -mc.field_71439_g.func_174818_b(pos2)));
/*      */       
/* 1061 */       BlockPos posToCheck = possiblePositions.get(0);
/*      */       
/* 1063 */       if (!isGoodMaterial(mc.field_71441_e.func_180495_p(posToCheck).func_177230_c(), false)) {
/* 1064 */         return pos;
/*      */       }
/*      */       
/* 1067 */       if (mc.field_71439_g.func_174818_b(posToCheck) > MathUtil.square(((Float)this.range.getValue()).floatValue())) {
/* 1068 */         return pos;
/*      */       }
/*      */       
/* 1071 */       if (((Boolean)this.raytrace.getValue()).booleanValue() && !BlockUtil.rayTracePlaceCheck(posToCheck, ((Boolean)this.raytrace.getValue()).booleanValue())) {
/* 1072 */         return pos;
/*      */       }
/*      */       
/* 1075 */       if (!mc.field_71441_e.func_72872_a(Entity.class, new AxisAlignedBB(posToCheck)).isEmpty()) {
/* 1076 */         return pos;
/*      */       }
/*      */       
/* 1079 */       if (hasAdjancedRedstone(posToCheck)) {
/* 1080 */         return pos;
/*      */       }
/*      */       
/* 1083 */       List<BlockPos> possibleRedStonePositions = checkRedStone(posToCheck, posIn);
/* 1084 */       if (possiblePositions.isEmpty()) {
/* 1085 */         return pos;
/*      */       }
/* 1087 */       BlockPos[] helpingStuff = getHelpingPos(posToCheck, posIn, possibleRedStonePositions);
/* 1088 */       if (helpingStuff != null && helpingStuff[0] != null && helpingStuff[1] != null) {
/* 1089 */         pos[0] = posToCheck;
/* 1090 */         pos[1] = helpingStuff[1];
/* 1091 */         pos[2] = helpingStuff[0];
/*      */       } 
/*      */     } else {
/* 1094 */       possiblePositions.removeIf(position -> (mc.field_71439_g.func_174818_b(position) > MathUtil.square(((Float)this.range.getValue()).floatValue())));
/* 1095 */       possiblePositions.removeIf(position -> !isGoodMaterial(mc.field_71441_e.func_180495_p(position).func_177230_c(), false));
/* 1096 */       possiblePositions.removeIf(position -> (((Boolean)this.raytrace.getValue()).booleanValue() && !BlockUtil.rayTracePlaceCheck(position, ((Boolean)this.raytrace.getValue()).booleanValue())));
/* 1097 */       possiblePositions.removeIf(position -> !mc.field_71441_e.func_72872_a(Entity.class, new AxisAlignedBB(position)).isEmpty());
/* 1098 */       possiblePositions.removeIf(this::hasAdjancedRedstone);
/* 1099 */       for (BlockPos position : possiblePositions) {
/* 1100 */         List<BlockPos> possibleRedStonePositions = checkRedStone(position, posIn);
/* 1101 */         if (possiblePositions.isEmpty()) {
/*      */           continue;
/*      */         }
/* 1104 */         BlockPos[] helpingStuff = getHelpingPos(position, posIn, possibleRedStonePositions);
/* 1105 */         if (helpingStuff != null && helpingStuff[0] != null && helpingStuff[1] != null) {
/* 1106 */           pos[0] = position;
/* 1107 */           pos[1] = helpingStuff[1];
/* 1108 */           pos[2] = helpingStuff[0];
/*      */           
/*      */           break;
/*      */         } 
/*      */       } 
/*      */     } 
/* 1114 */     return pos;
/*      */   }
/*      */   
/*      */   private List<BlockPos> checkRedStone(BlockPos pos, BlockPos hopperPos) {
/* 1118 */     List<BlockPos> toCheck = new ArrayList<>();
/* 1119 */     for (EnumFacing facing : EnumFacing.values()) {
/* 1120 */       toCheck.add(pos.func_177972_a(facing));
/*      */     }
/*      */     
/* 1123 */     toCheck.removeIf(position -> position.equals(hopperPos.func_177984_a()));
/* 1124 */     toCheck.removeIf(position -> (mc.field_71439_g.func_174818_b(position) > MathUtil.square(((Float)this.range.getValue()).floatValue())));
/* 1125 */     toCheck.removeIf(position -> !isGoodMaterial(mc.field_71441_e.func_180495_p(position).func_177230_c(), false));
/* 1126 */     toCheck.removeIf(position -> (((Boolean)this.raytrace.getValue()).booleanValue() && !BlockUtil.rayTracePlaceCheck(position, ((Boolean)this.raytrace.getValue()).booleanValue())));
/* 1127 */     toCheck.removeIf(position -> !mc.field_71441_e.func_72872_a(Entity.class, new AxisAlignedBB(position)).isEmpty());
/* 1128 */     toCheck.sort(Comparator.comparingDouble(pos2 -> mc.field_71439_g.func_174818_b(pos2)));
/* 1129 */     return toCheck;
/*      */   }
/*      */   
/*      */   private boolean hasAdjancedRedstone(BlockPos pos) {
/* 1133 */     for (EnumFacing facing : EnumFacing.values()) {
/* 1134 */       BlockPos position = pos.func_177972_a(facing);
/*      */       
/* 1136 */       if (mc.field_71441_e.func_180495_p(position).func_177230_c() == Blocks.field_150451_bX || mc.field_71441_e.func_180495_p(position).func_177230_c() == Blocks.field_150429_aA) {
/* 1137 */         return true;
/*      */       }
/*      */     } 
/* 1140 */     return false;
/*      */   }
/*      */   
/*      */   private List<BlockPos> getDispenserPositions(BlockPos pos) {
/* 1144 */     List<BlockPos> list = new ArrayList<>();
/* 1145 */     for (EnumFacing facing : EnumFacing.values()) {
/* 1146 */       if (facing != EnumFacing.DOWN) {
/* 1147 */         list.add(pos.func_177972_a(facing).func_177984_a());
/*      */       }
/*      */     } 
/* 1150 */     return list;
/*      */   }
/*      */   
/*      */   private BlockPos[] getHelpingPos(BlockPos pos, BlockPos hopperPos, List<BlockPos> redStonePositions) {
/* 1154 */     BlockPos[] result = new BlockPos[2];
/* 1155 */     List<BlockPos> possiblePositions = new ArrayList<>();
/* 1156 */     for (EnumFacing facing : EnumFacing.values()) {
/* 1157 */       BlockPos facingPos = pos.func_177972_a(facing);
/* 1158 */       if (!facingPos.equals(hopperPos) && !facingPos.equals(hopperPos.func_177984_a())) {
/* 1159 */         if (!mc.field_71441_e.func_180495_p(facingPos).func_185904_a().func_76222_j()) {
/* 1160 */           if (redStonePositions.contains(facingPos)) {
/* 1161 */             redStonePositions.remove(facingPos);
/* 1162 */             if (redStonePositions.isEmpty()) {
/* 1163 */               redStonePositions.add(facingPos);
/*      */             } else {
/* 1165 */               result[0] = facingPos;
/* 1166 */               result[1] = redStonePositions.get(0);
/* 1167 */               return result;
/*      */             } 
/*      */           } else {
/* 1170 */             result[0] = facingPos;
/* 1171 */             result[1] = redStonePositions.get(0);
/* 1172 */             return result;
/*      */           } 
/*      */         } else {
/* 1175 */           for (EnumFacing facing1 : EnumFacing.values()) {
/* 1176 */             BlockPos facingPos1 = facingPos.func_177972_a(facing1);
/* 1177 */             if (!facingPos1.equals(hopperPos) && !facingPos1.equals(hopperPos.func_177984_a()) && !facingPos1.equals(pos) && !mc.field_71441_e.func_180495_p(facingPos1).func_185904_a().func_76222_j()) {
/* 1178 */               if (redStonePositions.contains(facingPos)) {
/* 1179 */                 redStonePositions.remove(facingPos);
/* 1180 */                 if (redStonePositions.isEmpty()) {
/* 1181 */                   redStonePositions.add(facingPos);
/*      */                 } else {
/* 1183 */                   possiblePositions.add(facingPos);
/*      */                 } 
/*      */               } else {
/* 1186 */                 possiblePositions.add(facingPos);
/*      */               } 
/*      */             }
/*      */           } 
/*      */         } 
/*      */       }
/*      */     } 
/* 1193 */     possiblePositions.removeIf(position -> (mc.field_71439_g.func_174818_b(position) > MathUtil.square(((Float)this.range.getValue()).floatValue())));
/* 1194 */     possiblePositions.sort(Comparator.comparingDouble(position -> mc.field_71439_g.func_174818_b(position)));
/*      */     
/* 1196 */     if (!possiblePositions.isEmpty()) {
/* 1197 */       redStonePositions.remove(possiblePositions.get(0));
/* 1198 */       result[0] = possiblePositions.get(0);
/* 1199 */       result[1] = redStonePositions.get(0);
/* 1200 */       return result;
/*      */     } 
/*      */     
/* 1203 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void rotateToPos(BlockPos pos, Vec3d vec3d) {
/*      */     float[] angle;
/* 1212 */     if (vec3d == null) {
/* 1213 */       angle = MathUtil.calcAngle(mc.field_71439_g.func_174824_e(mc.func_184121_ak()), new Vec3d((pos.func_177958_n() + 0.5F), (pos.func_177956_o() - 0.5F), (pos.func_177952_p() + 0.5F)));
/*      */     } else {
/* 1215 */       angle = RotationUtil.getLegitRotations(vec3d);
/*      */     } 
/* 1217 */     this.yaw = angle[0];
/* 1218 */     this.pitch = angle[1];
/* 1219 */     this.spoof = true;
/*      */   }
/*      */   
/*      */   private boolean isGoodMaterial(Block block, boolean allowHopper) {
/* 1223 */     return (block instanceof net.minecraft.block.BlockAir || block instanceof net.minecraft.block.BlockLiquid || block instanceof net.minecraft.block.BlockTallGrass || block instanceof net.minecraft.block.BlockFire || block instanceof net.minecraft.block.BlockDeadBush || block instanceof net.minecraft.block.BlockSnow || (allowHopper && block instanceof BlockHopper));
/*      */   }
/*      */   
/*      */   private void resetFields() {
/* 1227 */     this.spoof = false;
/* 1228 */     this.switching = false;
/* 1229 */     this.lastHotbarSlot = -1;
/* 1230 */     this.shulkerSlot = -1;
/* 1231 */     this.hopperSlot = -1;
/* 1232 */     this.hopperPos = null;
/* 1233 */     this.target = null;
/* 1234 */     this.currentStep = Step.PRE;
/* 1235 */     this.obbySlot = -1;
/* 1236 */     this.dispenserSlot = -1;
/* 1237 */     this.redstoneSlot = -1;
/* 1238 */     this.finalDispenserData = null;
/* 1239 */     this.actionsThisTick = 0;
/* 1240 */     this.nextRotCanceled = false;
/*      */   }
/*      */   
/*      */   public static class DispenserData
/*      */   {
/*      */     private BlockPos dispenserPos;
/*      */     private BlockPos redStonePos;
/*      */     private BlockPos hopperPos;
/*      */     private BlockPos helpingPos;
/*      */     private boolean isPlaceable;
/*      */     
/*      */     public DispenserData() {
/* 1252 */       this.isPlaceable = false;
/*      */     }
/*      */     
/*      */     public DispenserData(BlockPos pos) {
/* 1256 */       this.isPlaceable = false;
/* 1257 */       this.hopperPos = pos;
/*      */     }
/*      */     
/*      */     public void setPlaceable(boolean placeable) {
/* 1261 */       this.isPlaceable = placeable;
/*      */     }
/*      */     
/*      */     public boolean isPlaceable() {
/* 1265 */       return (this.dispenserPos != null && this.hopperPos != null && this.redStonePos != null && this.helpingPos != null);
/*      */     }
/*      */     
/*      */     public BlockPos getDispenserPos() {
/* 1269 */       return this.dispenserPos;
/*      */     }
/*      */     
/*      */     public void setDispenserPos(BlockPos dispenserPos) {
/* 1273 */       this.dispenserPos = dispenserPos;
/*      */     }
/*      */     
/*      */     public BlockPos getRedStonePos() {
/* 1277 */       return this.redStonePos;
/*      */     }
/*      */     
/*      */     public void setRedStonePos(BlockPos redStonePos) {
/* 1281 */       this.redStonePos = redStonePos;
/*      */     }
/*      */     
/*      */     public BlockPos getHopperPos() {
/* 1285 */       return this.hopperPos;
/*      */     }
/*      */     
/*      */     public void setHopperPos(BlockPos hopperPos) {
/* 1289 */       this.hopperPos = hopperPos;
/*      */     }
/*      */     
/*      */     public BlockPos getHelpingPos() {
/* 1293 */       return this.helpingPos;
/*      */     }
/*      */     
/*      */     public void setHelpingPos(BlockPos helpingPos) {
/* 1297 */       this.helpingPos = helpingPos;
/*      */     }
/*      */   }
/*      */   
/*      */   public enum PlaceType {
/* 1302 */     MOUSE,
/* 1303 */     CLOSE,
/* 1304 */     ENEMY,
/* 1305 */     MIDDLE,
/* 1306 */     FAR;
/*      */   }
/*      */   
/*      */   public enum Mode {
/* 1310 */     NORMAL, DISPENSER;
/*      */   }
/*      */   
/*      */   public enum Step {
/* 1314 */     PRE,
/* 1315 */     HOPPER,
/* 1316 */     SHULKER,
/* 1317 */     CLICKHOPPER,
/* 1318 */     HOPPERGUI,
/* 1319 */     DISPENSER_HELPING,
/* 1320 */     DISPENSER_GUI,
/* 1321 */     DISPENSER,
/* 1322 */     CLICK_DISPENSER,
/* 1323 */     REDSTONE;
/*      */   }
/*      */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\me\earth\phobos\features\modules\combat\Auto32k.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */