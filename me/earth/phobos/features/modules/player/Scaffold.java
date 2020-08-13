/*     */ package me.earth.phobos.features.modules.player;
/*     */ 
/*     */ import me.earth.phobos.event.events.UpdateWalkingPlayerEvent;
/*     */ import me.earth.phobos.features.modules.Module;
/*     */ import me.earth.phobos.features.setting.Setting;
/*     */ import me.earth.phobos.util.BlockUtil;
/*     */ import me.earth.phobos.util.EntityUtil;
/*     */ import me.earth.phobos.util.InventoryUtil;
/*     */ import me.earth.phobos.util.MathUtil;
/*     */ import me.earth.phobos.util.Timer;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.network.Packet;
/*     */ import net.minecraft.network.play.client.CPacketEntityAction;
/*     */ import net.minecraft.network.play.client.CPacketHeldItemChange;
/*     */ import net.minecraft.network.play.client.CPacketPlayer;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.EnumHand;
/*     */ import net.minecraft.util.math.BlockPos;
/*     */ import net.minecraft.util.math.MathHelper;
/*     */ import net.minecraft.util.math.Vec3d;
/*     */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*     */ 
/*     */ public class Scaffold extends Module {
/*  26 */   public Setting<Boolean> rotation = register(new Setting("Rotate", Boolean.valueOf(false)));
/*     */   
/*  28 */   private final Timer timer = new Timer();
/*     */   
/*     */   public Scaffold() {
/*  31 */     super("Scaffold", "Places Blocks underneath you.", Module.Category.PLAYER, true, false, false);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onEnable() {
/*  36 */     this.timer.reset();
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onUpdateWalkingPlayerPost(UpdateWalkingPlayerEvent event) {
/*  41 */     if (isOff() || fullNullCheck() || event.getStage() == 0) {
/*     */       return;
/*     */     }
/*     */     
/*  45 */     if (!mc.field_71474_y.field_74314_A.func_151470_d()) {
/*  46 */       this.timer.reset();
/*     */     }
/*     */     
/*  49 */     BlockPos playerBlock = EntityUtil.getPlayerPosWithEntity();
/*  50 */     if (BlockUtil.isScaffoldPos(playerBlock.func_177982_a(0, -1, 0))) {
/*  51 */       if (BlockUtil.isValidBlock(playerBlock.func_177982_a(0, -2, 0))) {
/*  52 */         place(playerBlock.func_177982_a(0, -1, 0), EnumFacing.UP);
/*  53 */       } else if (BlockUtil.isValidBlock(playerBlock.func_177982_a(-1, -1, 0))) {
/*  54 */         place(playerBlock.func_177982_a(0, -1, 0), EnumFacing.EAST);
/*  55 */       } else if (BlockUtil.isValidBlock(playerBlock.func_177982_a(1, -1, 0))) {
/*  56 */         place(playerBlock.func_177982_a(0, -1, 0), EnumFacing.WEST);
/*  57 */       } else if (BlockUtil.isValidBlock(playerBlock.func_177982_a(0, -1, -1))) {
/*  58 */         place(playerBlock.func_177982_a(0, -1, 0), EnumFacing.SOUTH);
/*  59 */       } else if (BlockUtil.isValidBlock(playerBlock.func_177982_a(0, -1, 1))) {
/*  60 */         place(playerBlock.func_177982_a(0, -1, 0), EnumFacing.NORTH);
/*  61 */       } else if (BlockUtil.isValidBlock(playerBlock.func_177982_a(1, -1, 1))) {
/*  62 */         if (BlockUtil.isValidBlock(playerBlock.func_177982_a(0, -1, 1))) {
/*  63 */           place(playerBlock.func_177982_a(0, -1, 1), EnumFacing.NORTH);
/*     */         }
/*  65 */         place(playerBlock.func_177982_a(1, -1, 1), EnumFacing.EAST);
/*  66 */       } else if (BlockUtil.isValidBlock(playerBlock.func_177982_a(-1, -1, 1))) {
/*  67 */         if (BlockUtil.isValidBlock(playerBlock.func_177982_a(-1, -1, 0))) {
/*  68 */           place(playerBlock.func_177982_a(0, -1, 1), EnumFacing.WEST);
/*     */         }
/*  70 */         place(playerBlock.func_177982_a(-1, -1, 1), EnumFacing.SOUTH);
/*  71 */       } else if (BlockUtil.isValidBlock(playerBlock.func_177982_a(1, -1, 1))) {
/*  72 */         if (BlockUtil.isValidBlock(playerBlock.func_177982_a(0, -1, 1))) {
/*  73 */           place(playerBlock.func_177982_a(0, -1, 1), EnumFacing.SOUTH);
/*     */         }
/*  75 */         place(playerBlock.func_177982_a(1, -1, 1), EnumFacing.WEST);
/*  76 */       } else if (BlockUtil.isValidBlock(playerBlock.func_177982_a(1, -1, 1))) {
/*  77 */         if (BlockUtil.isValidBlock(playerBlock.func_177982_a(0, -1, 1))) {
/*  78 */           place(playerBlock.func_177982_a(0, -1, 1), EnumFacing.EAST);
/*     */         }
/*  80 */         place(playerBlock.func_177982_a(1, -1, 1), EnumFacing.NORTH);
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   public void place(BlockPos posI, EnumFacing face) {
/*  86 */     BlockPos pos = posI;
/*  87 */     if (face == EnumFacing.UP) {
/*  88 */       pos = pos.func_177982_a(0, -1, 0);
/*  89 */     } else if (face == EnumFacing.NORTH) {
/*  90 */       pos = pos.func_177982_a(0, 0, 1);
/*  91 */     } else if (face == EnumFacing.SOUTH) {
/*  92 */       pos = pos.func_177982_a(0, 0, -1);
/*  93 */     } else if (face == EnumFacing.EAST) {
/*  94 */       pos = pos.func_177982_a(-1, 0, 0);
/*  95 */     } else if (face == EnumFacing.WEST) {
/*  96 */       pos = pos.func_177982_a(1, 0, 0);
/*     */     } 
/*  98 */     int oldSlot = mc.field_71439_g.field_71071_by.field_70461_c;
/*  99 */     int newSlot = -1;
/* 100 */     for (int i = 0; i < 9; i++) {
/* 101 */       ItemStack stack = mc.field_71439_g.field_71071_by.func_70301_a(i);
/* 102 */       if (!InventoryUtil.isNull(stack) && stack.func_77973_b() instanceof net.minecraft.item.ItemBlock && Block.func_149634_a(stack.func_77973_b()).func_176223_P().func_185913_b()) {
/* 103 */         newSlot = i;
/*     */         
/*     */         break;
/*     */       } 
/*     */     } 
/* 108 */     if (newSlot == -1) {
/*     */       return;
/*     */     }
/*     */     
/* 112 */     boolean crouched = false;
/* 113 */     if (!mc.field_71439_g.func_70093_af()) {
/* 114 */       Block block = mc.field_71441_e.func_180495_p(pos).func_177230_c();
/* 115 */       if (BlockUtil.blackList.contains(block)) {
/* 116 */         mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketEntityAction((Entity)mc.field_71439_g, CPacketEntityAction.Action.START_SNEAKING));
/* 117 */         crouched = true;
/*     */       } 
/*     */     } 
/*     */     
/* 121 */     if (!(mc.field_71439_g.func_184614_ca().func_77973_b() instanceof net.minecraft.item.ItemBlock)) {
/* 122 */       mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketHeldItemChange(newSlot));
/* 123 */       mc.field_71439_g.field_71071_by.field_70461_c = newSlot;
/* 124 */       mc.field_71442_b.func_78765_e();
/*     */     } 
/*     */     
/* 127 */     if (mc.field_71474_y.field_74314_A.func_151470_d()) {
/* 128 */       mc.field_71439_g.field_70159_w *= 0.3D;
/* 129 */       mc.field_71439_g.field_70179_y *= 0.3D;
/* 130 */       mc.field_71439_g.func_70664_aZ();
/* 131 */       if (this.timer.passedMs(1500L)) {
/* 132 */         mc.field_71439_g.field_70181_x = -0.28D;
/* 133 */         this.timer.reset();
/*     */       } 
/*     */     } 
/*     */     
/* 137 */     if (((Boolean)this.rotation.getValue()).booleanValue()) {
/* 138 */       float[] angle = MathUtil.calcAngle(mc.field_71439_g.func_174824_e(mc.func_184121_ak()), new Vec3d((pos.func_177958_n() + 0.5F), (pos.func_177956_o() - 0.5F), (pos.func_177952_p() + 0.5F)));
/* 139 */       mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Rotation(angle[0], MathHelper.func_180184_b((int)angle[1], 360), mc.field_71439_g.field_70122_E));
/*     */     } 
/* 141 */     mc.field_71442_b.func_187099_a(mc.field_71439_g, mc.field_71441_e, pos, face, new Vec3d(0.5D, 0.5D, 0.5D), EnumHand.MAIN_HAND);
/* 142 */     mc.field_71439_g.func_184609_a(EnumHand.MAIN_HAND);
/* 143 */     mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketHeldItemChange(oldSlot));
/* 144 */     mc.field_71439_g.field_71071_by.field_70461_c = oldSlot;
/* 145 */     mc.field_71442_b.func_78765_e();
/*     */     
/* 147 */     if (crouched)
/* 148 */       mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketEntityAction((Entity)mc.field_71439_g, CPacketEntityAction.Action.STOP_SNEAKING)); 
/*     */   }
/*     */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\me\earth\phobos\features\modules\player\Scaffold.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */