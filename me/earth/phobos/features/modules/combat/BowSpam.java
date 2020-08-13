/*     */ package me.earth.phobos.features.modules.combat;
/*     */ import me.earth.phobos.Phobos;
/*     */ import me.earth.phobos.event.events.PacketEvent;
/*     */ import me.earth.phobos.event.events.UpdateWalkingPlayerEvent;
/*     */ import me.earth.phobos.features.modules.Module;
/*     */ import me.earth.phobos.features.setting.Setting;
/*     */ import me.earth.phobos.util.EntityUtil;
/*     */ import me.earth.phobos.util.InventoryUtil;
/*     */ import me.earth.phobos.util.MathUtil;
/*     */ import me.earth.phobos.util.Timer;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.ItemBow;
/*     */ import net.minecraft.item.ItemEndCrystal;
/*     */ import net.minecraft.network.Packet;
/*     */ import net.minecraft.network.play.client.CPacketPlayer;
/*     */ import net.minecraft.network.play.client.CPacketPlayerDigging;
/*     */ import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
/*     */ import net.minecraft.util.EnumHand;
/*     */ import net.minecraft.util.math.BlockPos;
/*     */ import net.minecraft.util.math.Vec3d;
/*     */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*     */ import org.lwjgl.input.Mouse;
/*     */ 
/*     */ public class BowSpam extends Module {
/*  27 */   public Setting<Mode> mode = register(new Setting("Mode", Mode.FAST));
/*  28 */   public Setting<Boolean> bowbomb = register(new Setting("BowBomb", Boolean.valueOf(false), v -> (this.mode.getValue() != Mode.BOWBOMB)));
/*  29 */   public Setting<Boolean> allowOffhand = register(new Setting("Offhand", Boolean.valueOf(true), v -> (this.mode.getValue() != Mode.AUTORELEASE)));
/*  30 */   public Setting<Integer> ticks = register(new Setting("Ticks", Integer.valueOf(3), Integer.valueOf(0), Integer.valueOf(20), v -> (this.mode.getValue() == Mode.BOWBOMB || this.mode.getValue() == Mode.FAST), "Speed"));
/*  31 */   public Setting<Integer> delay = register(new Setting("Delay", Integer.valueOf(50), Integer.valueOf(0), Integer.valueOf(500), v -> (this.mode.getValue() == Mode.AUTORELEASE), "Speed"));
/*  32 */   public Setting<Boolean> tpsSync = register(new Setting("TpsSync", Boolean.valueOf(true)));
/*  33 */   public Setting<Boolean> autoSwitch = register(new Setting("AutoSwitch", Boolean.valueOf(false)));
/*  34 */   public Setting<Boolean> onlyWhenSave = register(new Setting("OnlyWhenSave", Boolean.valueOf(true), v -> ((Boolean)this.autoSwitch.getValue()).booleanValue()));
/*  35 */   public Setting<Target> targetMode = register(new Setting("Target", Target.LOWEST, v -> ((Boolean)this.autoSwitch.getValue()).booleanValue()));
/*  36 */   public Setting<Float> range = register(new Setting("Range", Float.valueOf(3.0F), Float.valueOf(0.0F), Float.valueOf(6.0F), v -> ((Boolean)this.autoSwitch.getValue()).booleanValue(), "Range of the target"));
/*  37 */   public Setting<Float> health = register(new Setting("Lethal", Float.valueOf(6.0F), Float.valueOf(0.1F), Float.valueOf(36.0F), v -> ((Boolean)this.autoSwitch.getValue()).booleanValue(), "When should it switch?"));
/*  38 */   public Setting<Float> ownHealth = register(new Setting("OwnHealth", Float.valueOf(20.0F), Float.valueOf(0.1F), Float.valueOf(36.0F), v -> ((Boolean)this.autoSwitch.getValue()).booleanValue(), "Own Health."));
/*     */   
/*  40 */   private final Timer timer = new Timer();
/*     */   private boolean offhand = false;
/*     */   private boolean switched = false;
/*  43 */   private int lastHotbarSlot = -1;
/*     */   
/*     */   public BowSpam() {
/*  46 */     super("BowSpam", "Spams your bow", Module.Category.COMBAT, true, false, false);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onEnable() {
/*  51 */     this.lastHotbarSlot = mc.field_71439_g.field_71071_by.field_70461_c;
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onUpdateWalkingPlayer(UpdateWalkingPlayerEvent event) {
/*  56 */     if (event.getStage() != 0) {
/*     */       return;
/*     */     }
/*     */     
/*  60 */     if (((Boolean)this.autoSwitch.getValue()).booleanValue() && InventoryUtil.findHotbarBlock(ItemBow.class) != -1 && ((Float)this.ownHealth.getValue()).floatValue() <= EntityUtil.getHealth((Entity)mc.field_71439_g) && (!((Boolean)this.onlyWhenSave.getValue()).booleanValue() || EntityUtil.isSafe((Entity)mc.field_71439_g))) {
/*  61 */       EntityPlayer target = getTarget();
/*  62 */       if (target != null) {
/*  63 */         AutoCrystal crystal = (AutoCrystal)Phobos.moduleManager.getModuleByClass(AutoCrystal.class);
/*  64 */         if (!crystal.isOn() || !InventoryUtil.holdingItem(ItemEndCrystal.class)) {
/*  65 */           Vec3d pos = target.func_174791_d();
/*  66 */           double xPos = pos.field_72450_a;
/*  67 */           double yPos = pos.field_72448_b;
/*  68 */           double zPos = pos.field_72449_c;
/*  69 */           if (mc.field_71439_g.func_70685_l((Entity)target)) {
/*     */             
/*  71 */             yPos += target.eyeHeight;
/*  72 */           } else if (EntityUtil.canEntityFeetBeSeen((Entity)target)) {
/*  73 */             yPos += 0.1D;
/*     */           } else {
/*     */             return;
/*     */           } 
/*     */           
/*  78 */           if (!(mc.field_71439_g.func_184614_ca().func_77973_b() instanceof ItemBow)) {
/*  79 */             this.lastHotbarSlot = mc.field_71439_g.field_71071_by.field_70461_c;
/*  80 */             InventoryUtil.switchToHotbarSlot(ItemBow.class, false);
/*  81 */             mc.field_71474_y.field_74313_G.field_74513_e = true;
/*  82 */             this.switched = true;
/*     */           } 
/*     */           
/*  85 */           Phobos.rotationManager.lookAtVec3d(xPos, yPos, zPos);
/*  86 */           if (mc.field_71439_g.func_184614_ca().func_77973_b() instanceof ItemBow) {
/*  87 */             this.switched = true;
/*     */           }
/*     */         } 
/*     */       } 
/*  91 */     } else if (event.getStage() == 0 && this.switched && this.lastHotbarSlot != -1) {
/*  92 */       InventoryUtil.switchToHotbarSlot(this.lastHotbarSlot, false);
/*  93 */       mc.field_71474_y.field_74313_G.field_74513_e = Mouse.isButtonDown(1);
/*  94 */       this.switched = false;
/*     */     } else {
/*  96 */       mc.field_71474_y.field_74313_G.field_74513_e = Mouse.isButtonDown(1);
/*     */     } 
/*     */     
/*  99 */     if (this.mode.getValue() == Mode.FAST && (
/* 100 */       this.offhand || mc.field_71439_g.field_71071_by.func_70448_g().func_77973_b() instanceof ItemBow) && 
/* 101 */       mc.field_71439_g.func_184587_cr() && mc.field_71439_g.func_184612_cw() >= ((Integer)this.ticks.getValue()).intValue() * (((Boolean)this.tpsSync.getValue()).booleanValue() ? Phobos.serverManager.getTpsFactor() : 1.0F)) {
/* 102 */       mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.field_177992_a, mc.field_71439_g.func_174811_aO()));
/* 103 */       mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayerTryUseItem(this.offhand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND));
/* 104 */       mc.field_71439_g.func_184597_cx();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void onUpdate() {
/* 112 */     this.offhand = (mc.field_71439_g.func_184592_cb().func_77973_b() == Items.field_151031_f && ((Boolean)this.allowOffhand.getValue()).booleanValue());
/* 113 */     switch ((Mode)this.mode.getValue()) {
/*     */       case AUTORELEASE:
/* 115 */         if ((this.offhand || mc.field_71439_g.field_71071_by.func_70448_g().func_77973_b() instanceof ItemBow) && 
/* 116 */           this.timer.passedMs((int)(((Integer)this.delay.getValue()).intValue() * (((Boolean)this.tpsSync.getValue()).booleanValue() ? Phobos.serverManager.getTpsFactor() : 1.0F)))) {
/* 117 */           mc.field_71442_b.func_78766_c((EntityPlayer)mc.field_71439_g);
/* 118 */           this.timer.reset();
/*     */         } 
/*     */         break;
/*     */       
/*     */       case BOWBOMB:
/* 123 */         if ((this.offhand || mc.field_71439_g.field_71071_by.func_70448_g().func_77973_b() instanceof ItemBow) && 
/* 124 */           mc.field_71439_g.func_184587_cr() && mc.field_71439_g.func_184612_cw() >= ((Integer)this.ticks.getValue()).intValue() * (((Boolean)this.tpsSync.getValue()).booleanValue() ? Phobos.serverManager.getTpsFactor() : 1.0F)) {
/* 125 */           mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.field_177992_a, mc.field_71439_g.func_174811_aO()));
/* 126 */           mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.PositionRotation(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u - 0.0624D, mc.field_71439_g.field_70161_v, mc.field_71439_g.field_70177_z, mc.field_71439_g.field_70125_A, false));
/* 127 */           mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.PositionRotation(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u - 999.0D, mc.field_71439_g.field_70161_v, mc.field_71439_g.field_70177_z, mc.field_71439_g.field_70125_A, true));
/* 128 */           mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayerTryUseItem(this.offhand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND));
/* 129 */           mc.field_71439_g.func_184597_cx();
/*     */         } 
/*     */         break;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onPacketSend(PacketEvent.Send event) {
/* 138 */     if (event.getStage() == 0 && ((Boolean)this.bowbomb.getValue()).booleanValue() && this.mode.getValue() != Mode.BOWBOMB && event.getPacket() instanceof CPacketPlayerDigging) {
/* 139 */       CPacketPlayerDigging packet = (CPacketPlayerDigging)event.getPacket();
/* 140 */       if (packet.func_180762_c() == CPacketPlayerDigging.Action.RELEASE_USE_ITEM && (
/* 141 */         this.offhand || mc.field_71439_g.field_71071_by.func_70448_g().func_77973_b() instanceof ItemBow) && mc.field_71439_g.func_184612_cw() >= 20 && !mc.field_71439_g.field_70122_E) {
/* 142 */         mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u - 0.10000000149011612D, mc.field_71439_g.field_70161_v, false));
/* 143 */         mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u - 10000.0D, mc.field_71439_g.field_70161_v, true));
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private EntityPlayer getTarget() {
/* 150 */     double maxHealth = 36.0D;
/* 151 */     EntityPlayer target = null;
/* 152 */     for (EntityPlayer player : mc.field_71441_e.field_73010_i) {
/* 153 */       if (player == null) {
/*     */         continue;
/*     */       }
/*     */       
/* 157 */       if (EntityUtil.isDead((Entity)player)) {
/*     */         continue;
/*     */       }
/*     */       
/* 161 */       if (EntityUtil.getHealth((Entity)player) > ((Float)this.health.getValue()).floatValue()) {
/*     */         continue;
/*     */       }
/*     */       
/* 165 */       if (player.equals(mc.field_71439_g)) {
/*     */         continue;
/*     */       }
/*     */       
/* 169 */       if (Phobos.friendManager.isFriend(player)) {
/*     */         continue;
/*     */       }
/*     */       
/* 173 */       if (mc.field_71439_g.func_70068_e((Entity)player) > MathUtil.square(((Float)this.range.getValue()).floatValue())) {
/*     */         continue;
/*     */       }
/*     */       
/* 177 */       if (!mc.field_71439_g.func_70685_l((Entity)player) && !EntityUtil.canEntityFeetBeSeen((Entity)player)) {
/*     */         continue;
/*     */       }
/*     */       
/* 181 */       if (target == null) {
/* 182 */         target = player;
/* 183 */         maxHealth = EntityUtil.getHealth((Entity)player);
/*     */       } 
/*     */       
/* 186 */       if (this.targetMode.getValue() == Target.CLOSEST && mc.field_71439_g.func_70068_e((Entity)player) < mc.field_71439_g.func_70068_e((Entity)target)) {
/* 187 */         target = player;
/* 188 */         maxHealth = EntityUtil.getHealth((Entity)player);
/*     */       } 
/*     */       
/* 191 */       if (this.targetMode.getValue() == Target.LOWEST && EntityUtil.getHealth((Entity)player) < maxHealth) {
/* 192 */         target = player;
/* 193 */         maxHealth = EntityUtil.getHealth((Entity)player);
/*     */       } 
/*     */     } 
/* 196 */     return target;
/*     */   }
/*     */   
/*     */   public enum Target {
/* 200 */     CLOSEST,
/* 201 */     LOWEST;
/*     */   }
/*     */   
/*     */   public enum Mode {
/* 205 */     FAST,
/* 206 */     AUTORELEASE,
/* 207 */     BOWBOMB;
/*     */   }
/*     */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\me\earth\phobos\features\modules\combat\BowSpam.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */