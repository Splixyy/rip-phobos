/*     */ package me.earth.phobos.features.modules.player;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import me.earth.phobos.Phobos;
/*     */ import me.earth.phobos.event.events.BlockEvent;
/*     */ import me.earth.phobos.event.events.PacketEvent;
/*     */ import me.earth.phobos.event.events.Render3DEvent;
/*     */ import me.earth.phobos.features.modules.Module;
/*     */ import me.earth.phobos.features.setting.Setting;
/*     */ import me.earth.phobos.util.BlockUtil;
/*     */ import me.earth.phobos.util.InventoryUtil;
/*     */ import me.earth.phobos.util.RenderUtil;
/*     */ import me.earth.phobos.util.Timer;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.item.ItemSword;
/*     */ import net.minecraft.network.Packet;
/*     */ import net.minecraft.network.play.client.CPacketPlayerDigging;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.EnumHand;
/*     */ import net.minecraft.util.math.AxisAlignedBB;
/*     */ import net.minecraft.util.math.BlockPos;
/*     */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Speedmine
/*     */   extends Module
/*     */ {
/*  34 */   public Setting<Boolean> tweaks = register(new Setting("Tweaks", Boolean.valueOf(true)));
/*  35 */   public Setting<Mode> mode = register(new Setting("Mode", Mode.PACKET, v -> ((Boolean)this.tweaks.getValue()).booleanValue()));
/*  36 */   public Setting<Boolean> reset = register(new Setting("Reset", Boolean.valueOf(true)));
/*  37 */   public Setting<Float> damage = register(new Setting("Damage", Float.valueOf(0.7F), Float.valueOf(0.0F), Float.valueOf(1.0F), v -> (this.mode.getValue() == Mode.DAMAGE && ((Boolean)this.tweaks.getValue()).booleanValue())));
/*  38 */   public Setting<Boolean> noBreakAnim = register(new Setting("NoBreakAnim", Boolean.valueOf(false)));
/*  39 */   public Setting<Boolean> noDelay = register(new Setting("NoDelay", Boolean.valueOf(false)));
/*  40 */   public Setting<Boolean> noSwing = register(new Setting("NoSwing", Boolean.valueOf(false)));
/*  41 */   public Setting<Boolean> noTrace = register(new Setting("NoTrace", Boolean.valueOf(false)));
/*  42 */   public Setting<Boolean> allow = register(new Setting("AllowMultiTask", Boolean.valueOf(false)));
/*  43 */   public Setting<Boolean> pickaxe = register(new Setting("Pickaxe", Boolean.valueOf(true), v -> ((Boolean)this.noTrace.getValue()).booleanValue()));
/*  44 */   public Setting<Boolean> doubleBreak = register(new Setting("DoubleBreak", Boolean.valueOf(false)));
/*  45 */   public Setting<Boolean> webSwitch = register(new Setting("WebSwitch", Boolean.valueOf(false)));
/*  46 */   public Setting<Boolean> render = register(new Setting("Render", Boolean.valueOf(false)));
/*  47 */   public Setting<Boolean> box = register(new Setting("Box", Boolean.valueOf(false), v -> ((Boolean)this.render.getValue()).booleanValue()));
/*  48 */   public Setting<Boolean> outline = register(new Setting("Outline", Boolean.valueOf(true), v -> ((Boolean)this.render.getValue()).booleanValue()));
/*  49 */   private final Setting<Integer> boxAlpha = register(new Setting("BoxAlpha", Integer.valueOf(85), Integer.valueOf(0), Integer.valueOf(255), v -> (((Boolean)this.box.getValue()).booleanValue() && ((Boolean)this.render.getValue()).booleanValue())));
/*  50 */   private final Setting<Float> lineWidth = register(new Setting("LineWidth", Float.valueOf(1.0F), Float.valueOf(0.1F), Float.valueOf(5.0F), v -> (((Boolean)this.outline.getValue()).booleanValue() && ((Boolean)this.render.getValue()).booleanValue())));
/*     */   
/*  52 */   private static Speedmine INSTANCE = new Speedmine();
/*     */   
/*     */   public BlockPos currentPos;
/*     */   public IBlockState currentBlockState;
/*  56 */   private final Timer timer = new Timer();
/*     */   
/*     */   private boolean isMining = false;
/*  59 */   private BlockPos lastPos = null;
/*  60 */   private EnumFacing lastFacing = null;
/*     */   
/*     */   public Speedmine() {
/*  63 */     super("Speedmine", "Speeds up mining.", Module.Category.PLAYER, true, false, false);
/*  64 */     setInstance();
/*     */   }
/*     */   
/*     */   private void setInstance() {
/*  68 */     INSTANCE = this;
/*     */   }
/*     */   
/*     */   public static Speedmine getInstance() {
/*  72 */     if (INSTANCE == null) {
/*  73 */       INSTANCE = new Speedmine();
/*     */     }
/*  75 */     return INSTANCE;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onTick() {
/*  80 */     if (this.currentPos != null) {
/*  81 */       if (!mc.field_71441_e.func_180495_p(this.currentPos).equals(this.currentBlockState) || mc.field_71441_e.func_180495_p(this.currentPos).func_177230_c() == Blocks.field_150350_a) {
/*  82 */         this.currentPos = null;
/*  83 */         this.currentBlockState = null;
/*     */       }
/*  85 */       else if (((Boolean)this.webSwitch.getValue()).booleanValue() && this.currentBlockState.func_177230_c() == Blocks.field_150321_G && mc.field_71439_g.func_184614_ca().func_77973_b() instanceof net.minecraft.item.ItemPickaxe) {
/*  86 */         InventoryUtil.switchToHotbarSlot(ItemSword.class, false);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void onUpdate() {
/*  94 */     if (fullNullCheck()) {
/*     */       return;
/*     */     }
/*     */     
/*  98 */     if (((Boolean)this.noDelay.getValue()).booleanValue()) {
/*  99 */       mc.field_71442_b.field_78781_i = 0;
/*     */     }
/*     */     
/* 102 */     if (this.isMining && this.lastPos != null && this.lastFacing != null && ((Boolean)this.noBreakAnim.getValue()).booleanValue()) {
/* 103 */       mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.ABORT_DESTROY_BLOCK, this.lastPos, this.lastFacing));
/*     */     }
/*     */     
/* 106 */     if (((Boolean)this.reset.getValue()).booleanValue() && mc.field_71474_y.field_74313_G.func_151470_d() && !((Boolean)this.allow.getValue()).booleanValue()) {
/* 107 */       mc.field_71442_b.field_78778_j = false;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void onRender3D(Render3DEvent event) {
/* 113 */     if (((Boolean)this.render.getValue()).booleanValue() && this.currentPos != null) {
/* 114 */       Color color = new Color(this.timer.passedMs((int)(2000.0F * Phobos.serverManager.getTpsFactor())) ? 0 : 255, this.timer.passedMs((int)(2000.0F * Phobos.serverManager.getTpsFactor())) ? 255 : 0, 0, 255);
/* 115 */       RenderUtil.drawBoxESP(this.currentPos, color, false, color, ((Float)this.lineWidth.getValue()).floatValue(), ((Boolean)this.outline.getValue()).booleanValue(), ((Boolean)this.box.getValue()).booleanValue(), ((Integer)this.boxAlpha.getValue()).intValue(), false);
/*     */     } 
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onPacketSend(PacketEvent.Send event) {
/* 121 */     if (fullNullCheck()) {
/*     */       return;
/*     */     }
/*     */     
/* 125 */     if (event.getStage() == 0) {
/* 126 */       if (((Boolean)this.noSwing.getValue()).booleanValue() && event.getPacket() instanceof net.minecraft.network.play.client.CPacketAnimation) {
/* 127 */         event.setCanceled(true);
/*     */       }
/*     */       
/* 130 */       if (((Boolean)this.noBreakAnim.getValue()).booleanValue() && event.getPacket() instanceof CPacketPlayerDigging) {
/* 131 */         CPacketPlayerDigging packet = (CPacketPlayerDigging)event.getPacket();
/* 132 */         if (packet != null && packet.func_179715_a() != null) {
/*     */           try {
/* 134 */             for (Entity entity : mc.field_71441_e.func_72839_b(null, new AxisAlignedBB(packet.func_179715_a()))) {
/* 135 */               if (entity instanceof net.minecraft.entity.item.EntityEnderCrystal) {
/* 136 */                 showAnimation();
/*     */                 return;
/*     */               } 
/*     */             } 
/* 140 */           } catch (Exception exception) {}
/*     */           
/* 142 */           if (packet.func_180762_c().equals(CPacketPlayerDigging.Action.START_DESTROY_BLOCK)) {
/* 143 */             showAnimation(true, packet.func_179715_a(), packet.func_179714_b());
/*     */           }
/*     */           
/* 146 */           if (packet.func_180762_c().equals(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK)) {
/* 147 */             showAnimation();
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onBlockEvent(BlockEvent event) {
/* 156 */     if (fullNullCheck()) {
/*     */       return;
/*     */     }
/*     */     
/* 160 */     if (event.getStage() == 3 && ((Boolean)this.reset.getValue()).booleanValue() && 
/* 161 */       mc.field_71442_b.field_78770_f > 0.1F) {
/* 162 */       mc.field_71442_b.field_78778_j = true;
/*     */     }
/*     */ 
/*     */     
/* 166 */     if (event.getStage() == 4 && ((Boolean)this.tweaks.getValue()).booleanValue()) {
/* 167 */       if (BlockUtil.canBreak(event.pos)) {
/* 168 */         if (((Boolean)this.reset.getValue()).booleanValue()) {
/* 169 */           mc.field_71442_b.field_78778_j = false;
/*     */         }
/*     */         
/* 172 */         switch ((Mode)this.mode.getValue()) {
/*     */           case PACKET:
/* 174 */             if (this.currentPos == null) {
/* 175 */               this.currentPos = event.pos;
/* 176 */               this.currentBlockState = mc.field_71441_e.func_180495_p(this.currentPos);
/* 177 */               this.timer.reset();
/*     */             } 
/* 179 */             mc.field_71439_g.func_184609_a(EnumHand.MAIN_HAND);
/* 180 */             mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, event.pos, event.facing));
/* 181 */             mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, event.pos, event.facing));
/* 182 */             event.setCanceled(true);
/*     */             break;
/*     */           case DAMAGE:
/* 185 */             if (mc.field_71442_b.field_78770_f >= ((Float)this.damage.getValue()).floatValue()) {
/* 186 */               mc.field_71442_b.field_78770_f = 1.0F;
/*     */             }
/*     */             break;
/*     */           case INSTANT:
/* 190 */             mc.field_71439_g.func_184609_a(EnumHand.MAIN_HAND);
/* 191 */             mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, event.pos, event.facing));
/* 192 */             mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, event.pos, event.facing));
/* 193 */             mc.field_71442_b.func_187103_a(event.pos);
/* 194 */             mc.field_71441_e.func_175698_g(event.pos);
/*     */             break;
/*     */         } 
/*     */       
/*     */       } 
/* 199 */       if (((Boolean)this.doubleBreak.getValue()).booleanValue()) {
/* 200 */         BlockPos above = event.pos.func_177982_a(0, 1, 0);
/* 201 */         if (BlockUtil.canBreak(above) && mc.field_71439_g.func_70011_f(above.func_177958_n(), above.func_177956_o(), above.func_177952_p()) <= 5.0D) {
/* 202 */           mc.field_71439_g.func_184609_a(EnumHand.MAIN_HAND);
/* 203 */           mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, above, event.facing));
/* 204 */           mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, above, event.facing));
/* 205 */           mc.field_71442_b.func_187103_a(above);
/* 206 */           mc.field_71441_e.func_175698_g(above);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void showAnimation(boolean isMining, BlockPos lastPos, EnumFacing lastFacing) {
/* 213 */     this.isMining = isMining;
/* 214 */     this.lastPos = lastPos;
/* 215 */     this.lastFacing = lastFacing;
/*     */   }
/*     */   
/*     */   public void showAnimation() {
/* 219 */     showAnimation(false, (BlockPos)null, (EnumFacing)null);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getDisplayInfo() {
/* 224 */     return this.mode.currentEnumName();
/*     */   }
/*     */   
/*     */   public enum Mode {
/* 228 */     PACKET,
/* 229 */     DAMAGE,
/* 230 */     INSTANT;
/*     */   }
/*     */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\me\earth\phobos\features\modules\player\Speedmine.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */