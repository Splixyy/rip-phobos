/*     */ package me.earth.phobos.features.modules.misc;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import me.earth.phobos.Phobos;
/*     */ import me.earth.phobos.event.events.BlockEvent;
/*     */ import me.earth.phobos.event.events.UpdateWalkingPlayerEvent;
/*     */ import me.earth.phobos.features.modules.Module;
/*     */ import me.earth.phobos.features.setting.Setting;
/*     */ import me.earth.phobos.util.BlockUtil;
/*     */ import me.earth.phobos.util.MathUtil;
/*     */ import me.earth.phobos.util.Timer;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.util.EnumHand;
/*     */ import net.minecraft.util.math.BlockPos;
/*     */ import net.minecraft.util.math.Vec3d;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Nuker
/*     */   extends Module
/*     */ {
/*  28 */   public Setting<Boolean> rotate = register(new Setting("Rotate", Boolean.valueOf(false)));
/*  29 */   public Setting<Float> distance = register(new Setting("Range", Float.valueOf(6.0F), Float.valueOf(0.1F), Float.valueOf(10.0F)));
/*  30 */   public Setting<Integer> blockPerTick = register(new Setting("Blocks/Attack", Integer.valueOf(50), Integer.valueOf(1), Integer.valueOf(100)));
/*  31 */   public Setting<Integer> delay = register(new Setting("Delay/Attack", Integer.valueOf(50), Integer.valueOf(1), Integer.valueOf(1000)));
/*  32 */   public Setting<Boolean> nuke = register(new Setting("Nuke", Boolean.valueOf(false)));
/*  33 */   public Setting<Mode> mode = register(new Setting("Mode", Mode.NUKE, v -> ((Boolean)this.nuke.getValue()).booleanValue()));
/*  34 */   public Setting<Boolean> antiRegear = register(new Setting("AntiRegear", Boolean.valueOf(false)));
/*  35 */   public Setting<Boolean> hopperNuker = register(new Setting("HopperAura", Boolean.valueOf(false)));
/*     */ 
/*     */   
/*  38 */   private Setting<Boolean> autoSwitch = register(new Setting("AutoSwitch", Boolean.valueOf(false)));
/*     */   
/*  40 */   private int oldSlot = -1;
/*     */   private boolean isMining = false;
/*  42 */   private final Timer timer = new Timer();
/*     */   private Block selected;
/*     */   
/*     */   public Nuker() {
/*  46 */     super("Nuker", "Mines many blocks", Module.Category.MISC, true, false, false);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onToggle() {
/*  51 */     this.selected = null;
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onClickBlock(BlockEvent event) {
/*  56 */     if (event.getStage() == 3 && (this.mode.getValue() == Mode.SELECTION || this.mode.getValue() == Mode.NUKE)) {
/*  57 */       Block block = mc.field_71441_e.func_180495_p(event.pos).func_177230_c();
/*  58 */       if (block != null && block != this.selected) {
/*  59 */         this.selected = block;
/*  60 */         event.setCanceled(true);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onUpdateWalkingPlayerPre(UpdateWalkingPlayerEvent event) {
/*  67 */     if (event.getStage() == 0) {
/*  68 */       if (((Boolean)this.nuke.getValue()).booleanValue()) {
/*  69 */         BlockPos pos = null;
/*  70 */         switch ((Mode)this.mode.getValue()) {
/*     */           case SELECTION:
/*     */           case NUKE:
/*  73 */             pos = getClosestBlockSelection();
/*     */             break;
/*     */           case ALL:
/*  76 */             pos = getClosestBlockAll();
/*     */             break;
/*     */         } 
/*     */ 
/*     */         
/*  81 */         if (pos != null) {
/*  82 */           if (this.mode.getValue() == Mode.SELECTION || this.mode.getValue() == Mode.ALL) {
/*  83 */             if (((Boolean)this.rotate.getValue()).booleanValue()) {
/*  84 */               float[] angle = MathUtil.calcAngle(mc.field_71439_g.func_174824_e(mc.func_184121_ak()), new Vec3d((pos.func_177958_n() + 0.5F), (pos.func_177956_o() + 0.5F), (pos.func_177952_p() + 0.5F)));
/*  85 */               Phobos.rotationManager.setPlayerRotations(angle[0], angle[1]);
/*     */             } 
/*  87 */             if (canBreak(pos)) {
/*  88 */               mc.field_71442_b.func_180512_c(pos, mc.field_71439_g.func_174811_aO());
/*  89 */               mc.field_71439_g.func_184609_a(EnumHand.MAIN_HAND);
/*     */             } 
/*     */           } else {
/*  92 */             for (int i = 0; i < ((Integer)this.blockPerTick.getValue()).intValue(); i++) {
/*     */               
/*  94 */               pos = getClosestBlockSelection();
/*     */               
/*  96 */               if (pos != null) {
/*  97 */                 if (((Boolean)this.rotate.getValue()).booleanValue()) {
/*  98 */                   float[] angle = MathUtil.calcAngle(mc.field_71439_g.func_174824_e(mc.func_184121_ak()), new Vec3d((pos.func_177958_n() + 0.5F), (pos.func_177956_o() + 0.5F), (pos.func_177952_p() + 0.5F)));
/*  99 */                   Phobos.rotationManager.setPlayerRotations(angle[0], angle[1]);
/*     */                 } 
/*     */                 
/* 102 */                 if (this.timer.passedMs(((Integer)this.delay.getValue()).intValue())) {
/* 103 */                   mc.field_71442_b.func_180512_c(pos, mc.field_71439_g.func_174811_aO());
/* 104 */                   mc.field_71439_g.func_184609_a(EnumHand.MAIN_HAND);
/* 105 */                   this.timer.reset();
/*     */                 } 
/*     */               } 
/*     */             } 
/*     */           } 
/*     */         }
/*     */       } 
/*     */       
/* 113 */       if (((Boolean)this.antiRegear.getValue()).booleanValue()) {
/* 114 */         breakBlocks(BlockUtil.shulkerList);
/*     */       }
/*     */       
/* 117 */       if (((Boolean)this.hopperNuker.getValue()).booleanValue()) {
/* 118 */         List<Block> blocklist = new ArrayList<>();
/* 119 */         blocklist.add(Blocks.field_150438_bZ);
/* 120 */         breakBlocks(blocklist);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void breakBlocks(List<Block> blocks) {
/* 126 */     BlockPos pos = getNearestBlock(blocks);
/* 127 */     if (pos != null) {
/* 128 */       if (!this.isMining) {
/* 129 */         this.oldSlot = mc.field_71439_g.field_71071_by.field_70461_c;
/* 130 */         this.isMining = true;
/*     */       } 
/*     */       
/* 133 */       if (((Boolean)this.rotate.getValue()).booleanValue()) {
/* 134 */         float[] angle = MathUtil.calcAngle(mc.field_71439_g.func_174824_e(mc.func_184121_ak()), new Vec3d((pos.func_177958_n() + 0.5F), (pos.func_177956_o() + 0.5F), (pos.func_177952_p() + 0.5F)));
/* 135 */         Phobos.rotationManager.setPlayerRotations(angle[0], angle[1]);
/*     */       } 
/*     */       
/* 138 */       if (canBreak(pos)) {
/* 139 */         if (((Boolean)this.autoSwitch.getValue()).booleanValue()) {
/* 140 */           int newSlot = -1;
/* 141 */           for (int i = 0; i < 9; i++) {
/* 142 */             ItemStack stack = mc.field_71439_g.field_71071_by.func_70301_a(i);
/*     */             
/* 144 */             if (stack != ItemStack.field_190927_a)
/*     */             {
/*     */ 
/*     */               
/* 148 */               if (stack.func_77973_b() instanceof net.minecraft.item.ItemPickaxe) {
/* 149 */                 newSlot = i;
/*     */                 
/*     */                 break;
/*     */               } 
/*     */             }
/*     */           } 
/* 155 */           if (newSlot != -1) {
/* 156 */             mc.field_71439_g.field_71071_by.field_70461_c = newSlot;
/*     */           }
/*     */         } 
/* 159 */         mc.field_71442_b.func_180512_c(pos, mc.field_71439_g.func_174811_aO());
/* 160 */         mc.field_71439_g.func_184609_a(EnumHand.MAIN_HAND);
/*     */       }
/*     */     
/* 163 */     } else if (((Boolean)this.autoSwitch.getValue()).booleanValue() && this.oldSlot != -1) {
/* 164 */       mc.field_71439_g.field_71071_by.field_70461_c = this.oldSlot;
/* 165 */       this.oldSlot = -1;
/* 166 */       this.isMining = false;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean canBreak(BlockPos pos) {
/* 172 */     IBlockState blockState = mc.field_71441_e.func_180495_p(pos);
/* 173 */     Block block = blockState.func_177230_c();
/* 174 */     return (block.func_176195_g(blockState, (World)mc.field_71441_e, pos) != -1.0F);
/*     */   }
/*     */   
/*     */   private BlockPos getNearestBlock(List<Block> blocks) {
/* 178 */     double maxDist = MathUtil.square(((Float)this.distance.getValue()).floatValue());
/* 179 */     BlockPos ret = null;
/*     */     double x;
/* 181 */     for (x = maxDist; x >= -maxDist; x--) {
/*     */       double y;
/* 183 */       for (y = maxDist; y >= -maxDist; y--) {
/*     */         double z;
/* 185 */         for (z = maxDist; z >= -maxDist; z--) {
/* 186 */           BlockPos pos = new BlockPos(mc.field_71439_g.field_70165_t + x, mc.field_71439_g.field_70163_u + y, mc.field_71439_g.field_70161_v + z);
/* 187 */           double dist = mc.field_71439_g.func_70092_e(pos.func_177958_n(), pos.func_177956_o(), pos.func_177952_p());
/* 188 */           if (dist <= maxDist && blocks.contains(mc.field_71441_e.func_180495_p(pos).func_177230_c()) && canBreak(pos)) {
/* 189 */             maxDist = dist;
/* 190 */             ret = pos;
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/* 195 */     return ret;
/*     */   }
/*     */   
/*     */   private BlockPos getClosestBlockAll() {
/* 199 */     float maxDist = ((Float)this.distance.getValue()).floatValue();
/* 200 */     BlockPos ret = null;
/* 201 */     for (float x = maxDist; x >= -maxDist; x--) {
/* 202 */       float y; for (y = maxDist; y >= -maxDist; y--) {
/* 203 */         float z; for (z = maxDist; z >= -maxDist; z--) {
/* 204 */           BlockPos pos = new BlockPos(mc.field_71439_g.field_70165_t + x, mc.field_71439_g.field_70163_u + y, mc.field_71439_g.field_70161_v + z);
/* 205 */           double dist = mc.field_71439_g.func_70011_f(pos.func_177958_n(), pos.func_177956_o(), pos.func_177952_p());
/* 206 */           if (dist <= maxDist && mc.field_71441_e.func_180495_p(pos).func_177230_c() != Blocks.field_150350_a && !(mc.field_71441_e.func_180495_p(pos).func_177230_c() instanceof net.minecraft.block.BlockLiquid) && canBreak(pos) && 
/* 207 */             pos.func_177956_o() >= mc.field_71439_g.field_70163_u) {
/* 208 */             maxDist = (float)dist;
/* 209 */             ret = pos;
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 216 */     return ret;
/*     */   }
/*     */   
/*     */   private BlockPos getClosestBlockSelection() {
/* 220 */     float maxDist = ((Float)this.distance.getValue()).floatValue();
/* 221 */     BlockPos ret = null;
/* 222 */     for (float x = maxDist; x >= -maxDist; x--) {
/* 223 */       float y; for (y = maxDist; y >= -maxDist; y--) {
/* 224 */         float z; for (z = maxDist; z >= -maxDist; z--) {
/* 225 */           BlockPos pos = new BlockPos(mc.field_71439_g.field_70165_t + x, mc.field_71439_g.field_70163_u + y, mc.field_71439_g.field_70161_v + z);
/* 226 */           double dist = mc.field_71439_g.func_70011_f(pos.func_177958_n(), pos.func_177956_o(), pos.func_177952_p());
/* 227 */           if (dist <= maxDist && mc.field_71441_e.func_180495_p(pos).func_177230_c() != Blocks.field_150350_a && !(mc.field_71441_e.func_180495_p(pos).func_177230_c() instanceof net.minecraft.block.BlockLiquid) && mc.field_71441_e.func_180495_p(pos).func_177230_c() == this.selected && canBreak(pos) && 
/* 228 */             pos.func_177956_o() >= mc.field_71439_g.field_70163_u) {
/* 229 */             maxDist = (float)dist;
/* 230 */             ret = pos;
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 237 */     return ret;
/*     */   }
/*     */   
/*     */   public enum Mode {
/* 241 */     SELECTION,
/* 242 */     ALL,
/* 243 */     NUKE;
/*     */   }
/*     */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\me\earth\phobos\features\modules\misc\Nuker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */