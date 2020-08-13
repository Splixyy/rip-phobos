/*     */ package me.earth.phobos.features.modules.movement;
/*     */ import me.earth.phobos.event.events.PacketEvent;
/*     */ import me.earth.phobos.features.command.Command;
/*     */ import me.earth.phobos.features.modules.Module;
/*     */ import me.earth.phobos.features.setting.Setting;
/*     */ import me.earth.phobos.util.EntityUtil;
/*     */ import me.earth.phobos.util.InventoryUtil;
/*     */ import me.earth.phobos.util.Timer;
/*     */ import me.earth.phobos.util.Util;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.inventory.ClickType;
/*     */ import net.minecraft.inventory.EntityEquipmentSlot;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.network.Packet;
/*     */ import net.minecraft.network.play.client.CPacketClickWindow;
/*     */ import net.minecraft.network.play.client.CPacketEntityAction;
/*     */ import net.minecraft.network.play.client.CPacketPlayer;
/*     */ import net.minecraft.util.EnumHand;
/*     */ import net.minecraft.util.math.RayTraceResult;
/*     */ import net.minecraft.util.math.Vec3d;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*     */ 
/*     */ public class NoFall extends Module {
/*  28 */   private Setting<Mode> mode = register(new Setting("Mode", Mode.PACKET));
/*  29 */   private Setting<Integer> distance = register(new Setting("Distance", Integer.valueOf(15), Integer.valueOf(0), Integer.valueOf(50), v -> (this.mode.getValue() == Mode.BUCKET)));
/*  30 */   private Setting<Boolean> glide = register(new Setting("Glide", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.ELYTRA)));
/*  31 */   private Setting<Boolean> silent = register(new Setting("Silent", Boolean.valueOf(true), v -> (this.mode.getValue() == Mode.ELYTRA)));
/*  32 */   private Setting<Boolean> bypass = register(new Setting("Bypass", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.ELYTRA)));
/*     */   
/*  34 */   private Timer timer = new Timer();
/*     */   private boolean equipped = false;
/*     */   private boolean gotElytra = false;
/*  37 */   private State currentState = State.FALL_CHECK;
/*  38 */   private static Timer bypassTimer = new Timer();
/*  39 */   private static int ogslot = -1;
/*     */   
/*     */   public NoFall() {
/*  42 */     super("NoFall", "Prevents fall damage.", Module.Category.MOVEMENT, true, false, false);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onEnable() {
/*  47 */     ogslot = -1;
/*  48 */     this.currentState = State.FALL_CHECK;
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onPacketSend(PacketEvent.Send event) {
/*  53 */     if (fullNullCheck()) {
/*     */       return;
/*     */     }
/*  56 */     if (this.mode.getValue() == Mode.ELYTRA) {
/*  57 */       if (((Boolean)this.bypass.getValue()).booleanValue()) {
/*  58 */         this.currentState = this.currentState.onSend(event);
/*     */       }
/*  60 */       else if (!this.equipped && event.getPacket() instanceof CPacketPlayer && mc.field_71439_g.field_70143_R >= 3.0F) {
/*  61 */         RayTraceResult result = null;
/*  62 */         if (!((Boolean)this.glide.getValue()).booleanValue()) {
/*  63 */           result = mc.field_71441_e.func_147447_a(mc.field_71439_g.func_174791_d(), mc.field_71439_g.func_174791_d().func_72441_c(0.0D, -3.0D, 0.0D), true, true, false);
/*     */         }
/*  65 */         if (((Boolean)this.glide.getValue()).booleanValue() || (result != null && result.field_72313_a == RayTraceResult.Type.BLOCK)) {
/*  66 */           if (mc.field_71439_g.func_184582_a(EntityEquipmentSlot.CHEST).func_77973_b().equals(Items.field_185160_cR)) {
/*  67 */             mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketEntityAction((Entity)mc.field_71439_g, CPacketEntityAction.Action.START_FALL_FLYING));
/*  68 */           } else if (((Boolean)this.silent.getValue()).booleanValue()) {
/*  69 */             int slot = InventoryUtil.getItemHotbar(Items.field_185160_cR);
/*  70 */             if (slot != -1) {
/*  71 */               mc.field_71442_b.func_187098_a(mc.field_71439_g.field_71069_bz.field_75152_c, 6, slot, ClickType.SWAP, (EntityPlayer)mc.field_71439_g);
/*  72 */               mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketEntityAction((Entity)mc.field_71439_g, CPacketEntityAction.Action.START_FALL_FLYING));
/*     */             } 
/*  74 */             ogslot = slot;
/*  75 */             this.equipped = true;
/*     */           } 
/*     */         }
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*  83 */     if (this.mode.getValue() == Mode.PACKET && event.getPacket() instanceof CPacketPlayer) {
/*  84 */       CPacketPlayer packet = (CPacketPlayer)event.getPacket();
/*  85 */       packet.field_149474_g = true;
/*     */     } 
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onPacketReceive(PacketEvent.Receive event) {
/*  91 */     if (fullNullCheck()) {
/*     */       return;
/*     */     }
/*  94 */     if ((this.equipped || ((Boolean)this.bypass.getValue()).booleanValue()) && this.mode.getValue() == Mode.ELYTRA && (event.getPacket() instanceof net.minecraft.network.play.server.SPacketWindowItems || event.getPacket() instanceof net.minecraft.network.play.server.SPacketSetSlot)) {
/*  95 */       if (((Boolean)this.bypass.getValue()).booleanValue()) {
/*  96 */         this.currentState = this.currentState.onReceive(event);
/*     */       } else {
/*  98 */         this.gotElytra = true;
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void onUpdate() {
/* 105 */     if (fullNullCheck()) {
/*     */       return;
/*     */     }
/* 108 */     if (this.mode.getValue() == Mode.ELYTRA) {
/* 109 */       if (((Boolean)this.bypass.getValue()).booleanValue()) {
/* 110 */         this.currentState = this.currentState.onUpdate();
/*     */       }
/* 112 */       else if (((Boolean)this.silent.getValue()).booleanValue() && this.equipped && this.gotElytra) {
/* 113 */         mc.field_71442_b.func_187098_a(mc.field_71439_g.field_71069_bz.field_75152_c, 6, ogslot, ClickType.SWAP, (EntityPlayer)mc.field_71439_g);
/* 114 */         mc.field_71442_b.func_78765_e();
/* 115 */         this.equipped = false;
/* 116 */         this.gotElytra = false;
/* 117 */       } else if (((Boolean)this.silent.getValue()).booleanValue() && InventoryUtil.getItemHotbar(Items.field_185160_cR) == -1) {
/* 118 */         int slot = InventoryUtil.findStackInventory(Items.field_185160_cR);
/* 119 */         if (slot != -1 && ogslot != -1) {
/* 120 */           System.out.println(String.format("Moving %d to hotbar %d", new Object[] { Integer.valueOf(slot), Integer.valueOf(ogslot) }));
/* 121 */           mc.field_71442_b.func_187098_a(mc.field_71439_g.field_71069_bz.field_75152_c, slot, ogslot, ClickType.SWAP, (EntityPlayer)mc.field_71439_g);
/* 122 */           mc.field_71442_b.func_78765_e();
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void onTick() {
/* 131 */     if (fullNullCheck()) {
/*     */       return;
/*     */     }
/* 134 */     if (this.mode.getValue() == Mode.BUCKET && mc.field_71439_g.field_70143_R >= ((Integer)this.distance.getValue()).intValue() && !EntityUtil.isAboveWater((Entity)mc.field_71439_g) && this.timer.passedMs(100L)) {
/* 135 */       Vec3d posVec = mc.field_71439_g.func_174791_d();
/* 136 */       RayTraceResult result = mc.field_71441_e.func_147447_a(posVec, posVec.func_72441_c(0.0D, -5.329999923706055D, 0.0D), true, true, false);
/* 137 */       if (result != null && result.field_72313_a == RayTraceResult.Type.BLOCK) {
/* 138 */         EnumHand hand = EnumHand.MAIN_HAND;
/* 139 */         if (mc.field_71439_g.func_184592_cb().func_77973_b() == Items.field_151131_as) { hand = EnumHand.OFF_HAND; }
/* 140 */         else if (mc.field_71439_g.func_184614_ca().func_77973_b() != Items.field_151131_as)
/* 141 */         { for (int i = 0; i < 9; i++) {
/* 142 */             if (mc.field_71439_g.field_71071_by.func_70301_a(i).func_77973_b() == Items.field_151131_as) {
/* 143 */               mc.field_71439_g.field_71071_by.field_70461_c = i;
/* 144 */               mc.field_71439_g.field_70125_A = 90.0F;
/* 145 */               this.timer.reset();
/*     */               return;
/*     */             } 
/*     */           } 
/*     */           return; }
/*     */         
/* 151 */         mc.field_71439_g.field_70125_A = 90.0F;
/* 152 */         mc.field_71442_b.func_187101_a((EntityPlayer)mc.field_71439_g, (World)mc.field_71441_e, hand);
/* 153 */         this.timer.reset();
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public String getDisplayInfo() {
/* 160 */     return this.mode.currentEnumName();
/*     */   }
/*     */   
/*     */   public enum Mode {
/* 164 */     PACKET,
/* 165 */     BUCKET,
/* 166 */     ELYTRA;
/*     */   }
/*     */   
/*     */   public enum State {
/* 170 */     FALL_CHECK
/*     */     {
/*     */       public State onSend(PacketEvent.Send event) {
/* 173 */         RayTraceResult result = Util.mc.field_71441_e.func_147447_a(Util.mc.field_71439_g.func_174791_d(), Util.mc.field_71439_g.func_174791_d().func_72441_c(0.0D, -3.0D, 0.0D), true, true, false);
/* 174 */         if (event.getPacket() instanceof CPacketPlayer && Util.mc.field_71439_g.field_70143_R >= 3.0F && result != null && result.field_72313_a == RayTraceResult.Type.BLOCK) {
/* 175 */           int slot = InventoryUtil.getItemHotbar(Items.field_185160_cR);
/* 176 */           if (slot != -1) {
/* 177 */             Util.mc.field_71442_b.func_187098_a(Util.mc.field_71439_g.field_71069_bz.field_75152_c, 6, slot, ClickType.SWAP, (EntityPlayer)Util.mc.field_71439_g);
/* 178 */             NoFall.ogslot = slot;
/* 179 */             Util.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketEntityAction((Entity)Util.mc.field_71439_g, CPacketEntityAction.Action.START_FALL_FLYING));
/* 180 */             return WAIT_FOR_ELYTRA_DEQUIP;
/*     */           } 
/* 182 */           return this;
/*     */         } 
/* 184 */         return this;
/*     */       }
/*     */     },
/* 187 */     WAIT_FOR_ELYTRA_DEQUIP
/*     */     {
/*     */       public State onReceive(PacketEvent.Receive event) {
/* 190 */         if (event.getPacket() instanceof net.minecraft.network.play.server.SPacketWindowItems || event.getPacket() instanceof net.minecraft.network.play.server.SPacketSetSlot) {
/* 191 */           return REEQUIP_ELYTRA;
/*     */         }
/* 193 */         return this;
/*     */       }
/*     */     },
/* 196 */     REEQUIP_ELYTRA {
/*     */       public State onUpdate() {
/* 198 */         Util.mc.field_71442_b.func_187098_a(Util.mc.field_71439_g.field_71069_bz.field_75152_c, 6, NoFall.ogslot, ClickType.SWAP, (EntityPlayer)Util.mc.field_71439_g);
/* 199 */         Util.mc.field_71442_b.func_78765_e();
/* 200 */         int slot = InventoryUtil.findStackInventory(Items.field_185160_cR, true);
/* 201 */         if (slot == -1) {
/* 202 */           Command.sendMessage("§cElytra not found after regain?");
/* 203 */           return WAIT_FOR_NEXT_REQUIP;
/*     */         } 
/* 205 */         Util.mc.field_71442_b.func_187098_a(Util.mc.field_71439_g.field_71069_bz.field_75152_c, slot, NoFall.ogslot, ClickType.SWAP, (EntityPlayer)Util.mc.field_71439_g);
/* 206 */         Util.mc.field_71442_b.func_78765_e();
/* 207 */         NoFall.bypassTimer.reset();
/* 208 */         return RESET_TIME;
/*     */       }
/*     */     },
/*     */     
/* 212 */     WAIT_FOR_NEXT_REQUIP {
/*     */       public State onUpdate() {
/* 214 */         if (NoFall.bypassTimer.passedMs(250L)) {
/* 215 */           return REEQUIP_ELYTRA;
/*     */         }
/* 217 */         return this;
/*     */       }
/*     */     },
/* 220 */     RESET_TIME {
/*     */       public State onUpdate() {
/* 222 */         if (Util.mc.field_71439_g.field_70122_E || NoFall.bypassTimer.passedMs(250L)) {
/* 223 */           Util.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketClickWindow(0, 0, 0, ClickType.PICKUP, new ItemStack(Blocks.field_150357_h), (short)1337));
/* 224 */           return FALL_CHECK;
/*     */         } 
/* 226 */         return this;
/*     */       }
/*     */     };
/*     */     
/* 230 */     public State onSend(PacketEvent.Send e) { return this; }
/* 231 */     public State onReceive(PacketEvent.Receive e) { return this; } public State onUpdate() {
/* 232 */       return this;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\me\earth\phobos\features\modules\movement\NoFall.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */