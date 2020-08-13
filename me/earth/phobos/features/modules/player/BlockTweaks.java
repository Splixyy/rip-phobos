/*     */ package me.earth.phobos.features.modules.player;
/*     */ 
/*     */ import me.earth.phobos.Phobos;
/*     */ import me.earth.phobos.event.events.PacketEvent;
/*     */ import me.earth.phobos.features.modules.Module;
/*     */ import me.earth.phobos.features.setting.Setting;
/*     */ import me.earth.phobos.util.EntityUtil;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.enchantment.EnchantmentHelper;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.entity.EnumCreatureAttribute;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.init.Enchantments;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.item.ItemSword;
/*     */ import net.minecraft.item.ItemTool;
/*     */ import net.minecraft.network.play.client.CPacketUseEntity;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.EnumHand;
/*     */ import net.minecraft.util.math.BlockPos;
/*     */ import net.minecraft.util.math.Vec3d;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraftforge.event.entity.player.AttackEntityEvent;
/*     */ import net.minecraftforge.event.entity.player.PlayerInteractEvent;
/*     */ import net.minecraftforge.event.world.BlockEvent;
/*     */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*     */ 
/*     */ public class BlockTweaks
/*     */   extends Module
/*     */ {
/*  32 */   public Setting<Boolean> autoTool = register(new Setting("AutoTool", Boolean.valueOf(false)));
/*  33 */   public Setting<Boolean> autoWeapon = register(new Setting("AutoWeapon", Boolean.valueOf(false)));
/*  34 */   public Setting<Boolean> noFriendAttack = register(new Setting("NoFriendAttack", Boolean.valueOf(false)));
/*  35 */   public Setting<Boolean> noBlock = register(new Setting("NoHitboxBlock", Boolean.valueOf(true)));
/*  36 */   public Setting<Boolean> noGhost = register(new Setting("NoGlitchBlocks", Boolean.valueOf(false)));
/*  37 */   public Setting<Boolean> destroy = register(new Setting("Destroy", Boolean.valueOf(false), v -> ((Boolean)this.noGhost.getValue()).booleanValue()));
/*     */   
/*  39 */   private static BlockTweaks INSTANCE = new BlockTweaks();
/*  40 */   private int lastHotbarSlot = -1;
/*  41 */   private int currentTargetSlot = -1;
/*     */   private boolean switched = false;
/*     */   
/*     */   public BlockTweaks() {
/*  45 */     super("BlockTweaks", "Some tweaks for blocks.", Module.Category.PLAYER, true, false, false);
/*  46 */     setInstance();
/*     */   }
/*     */   
/*     */   private void setInstance() {
/*  50 */     INSTANCE = this;
/*     */   }
/*     */   
/*     */   public static BlockTweaks getINSTANCE() {
/*  54 */     if (INSTANCE == null) {
/*  55 */       INSTANCE = new BlockTweaks();
/*     */     }
/*  57 */     return INSTANCE;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onDisable() {
/*  62 */     if (this.switched) {
/*  63 */       equip(this.lastHotbarSlot, false);
/*     */     }
/*  65 */     this.lastHotbarSlot = -1;
/*  66 */     this.currentTargetSlot = -1;
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onBreak(BlockEvent.BreakEvent event) {
/*  71 */     if (fullNullCheck() || !((Boolean)this.noGhost.getValue()).booleanValue() || !((Boolean)this.destroy.getValue()).booleanValue()) {
/*     */       return;
/*     */     }
/*     */     
/*  75 */     if (!(mc.field_71439_g.func_184614_ca().func_77973_b() instanceof net.minecraft.item.ItemBlock)) {
/*  76 */       BlockPos pos = mc.field_71439_g.func_180425_c();
/*  77 */       removeGlitchBlocks(pos);
/*     */     } 
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onBlockInteract(PlayerInteractEvent.LeftClickBlock event) {
/*  83 */     if (((Boolean)this.autoTool.getValue()).booleanValue() && ((Speedmine.getInstance()).mode.getValue() != Speedmine.Mode.PACKET || Speedmine.getInstance().isOff() || !((Boolean)(Speedmine.getInstance()).tweaks.getValue()).booleanValue()) && 
/*  84 */       !fullNullCheck() && event.getPos() != null) {
/*  85 */       equipBestTool(mc.field_71441_e.func_180495_p(event.getPos()));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onAttack(AttackEntityEvent event) {
/*  92 */     if (((Boolean)this.autoWeapon.getValue()).booleanValue() && 
/*  93 */       !fullNullCheck() && event.getTarget() != null) {
/*  94 */       equipBestWeapon(event.getTarget());
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onPacketSend(PacketEvent.Send event) {
/* 101 */     if (fullNullCheck()) {
/*     */       return;
/*     */     }
/*     */     
/* 105 */     if (((Boolean)this.noFriendAttack.getValue()).booleanValue() && event.getPacket() instanceof CPacketUseEntity) {
/* 106 */       CPacketUseEntity packet = (CPacketUseEntity)event.getPacket();
/* 107 */       Entity entity = packet.func_149564_a((World)mc.field_71441_e);
/* 108 */       if (entity != null && Phobos.friendManager.isFriend(entity.func_70005_c_())) {
/* 109 */         event.setCanceled(true);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onUpdate() {
/* 116 */     if (!fullNullCheck()) {
/* 117 */       if (mc.field_71439_g.field_71071_by.field_70461_c != this.lastHotbarSlot && mc.field_71439_g.field_71071_by.field_70461_c != this.currentTargetSlot) {
/* 118 */         this.lastHotbarSlot = mc.field_71439_g.field_71071_by.field_70461_c;
/*     */       }
/*     */       
/* 121 */       if (!mc.field_71474_y.field_74312_F.func_151470_d() && this.switched) {
/* 122 */         equip(this.lastHotbarSlot, false);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private void removeGlitchBlocks(BlockPos pos) {
/* 128 */     for (int dx = -4; dx <= 4; dx++) {
/* 129 */       for (int dy = -4; dy <= 4; dy++) {
/* 130 */         for (int dz = -4; dz <= 4; dz++) {
/* 131 */           BlockPos blockPos = new BlockPos(pos.func_177958_n() + dx, pos.func_177956_o() + dy, pos.func_177952_p() + dz);
/* 132 */           if (mc.field_71441_e.func_180495_p(blockPos).func_177230_c().equals(Blocks.field_150350_a)) {
/* 133 */             mc.field_71442_b.func_187099_a(mc.field_71439_g, mc.field_71441_e, blockPos, EnumFacing.DOWN, new Vec3d(0.5D, 0.5D, 0.5D), EnumHand.MAIN_HAND);
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void equipBestTool(IBlockState blockState) {
/* 141 */     int bestSlot = -1;
/* 142 */     double max = 0.0D;
/* 143 */     for (int i = 0; i < 9; i++) {
/* 144 */       ItemStack stack = mc.field_71439_g.field_71071_by.func_70301_a(i);
/* 145 */       if (!stack.field_190928_g) {
/* 146 */         float speed = stack.func_150997_a(blockState);
/*     */         
/* 148 */         if (speed > 1.0F) {
/* 149 */           int eff; speed = (float)(speed + (((eff = EnchantmentHelper.func_77506_a(Enchantments.field_185305_q, stack)) > 0) ? (Math.pow(eff, 2.0D) + 1.0D) : 0.0D));
/* 150 */           if (speed > max) {
/* 151 */             max = speed;
/* 152 */             bestSlot = i;
/*     */           } 
/*     */         } 
/*     */       } 
/* 156 */     }  equip(bestSlot, true);
/*     */   }
/*     */   
/*     */   public void equipBestWeapon(Entity entity) {
/* 160 */     int bestSlot = -1;
/* 161 */     double maxDamage = 0.0D;
/* 162 */     EnumCreatureAttribute creatureAttribute = EnumCreatureAttribute.UNDEFINED;
/* 163 */     if (EntityUtil.isLiving(entity)) {
/* 164 */       EntityLivingBase base = (EntityLivingBase)entity;
/* 165 */       creatureAttribute = base.func_70668_bt();
/*     */     } 
/*     */     
/* 168 */     for (int i = 0; i < 9; i++) {
/* 169 */       ItemStack stack = mc.field_71439_g.field_71071_by.func_70301_a(i);
/* 170 */       if (!stack.field_190928_g)
/*     */       {
/*     */ 
/*     */         
/* 174 */         if (stack.func_77973_b() instanceof ItemTool) {
/* 175 */           double damage = ((ItemTool)stack.func_77973_b()).field_77865_bY + EnchantmentHelper.func_152377_a(stack, creatureAttribute);
/* 176 */           if (damage > maxDamage) {
/* 177 */             maxDamage = damage;
/* 178 */             bestSlot = i;
/*     */           } 
/* 180 */         } else if (stack.func_77973_b() instanceof ItemSword) {
/* 181 */           double damage = ((ItemSword)stack.func_77973_b()).func_150931_i() + EnchantmentHelper.func_152377_a(stack, creatureAttribute);
/* 182 */           if (damage > maxDamage) {
/* 183 */             maxDamage = damage;
/* 184 */             bestSlot = i;
/*     */           } 
/*     */         }  } 
/*     */     } 
/* 188 */     equip(bestSlot, true);
/*     */   }
/*     */   
/*     */   private void equip(int slot, boolean equipTool) {
/* 192 */     if (slot != -1) {
/* 193 */       if (slot != mc.field_71439_g.field_71071_by.field_70461_c) {
/* 194 */         this.lastHotbarSlot = mc.field_71439_g.field_71071_by.field_70461_c;
/*     */       }
/* 196 */       this.currentTargetSlot = slot;
/* 197 */       mc.field_71439_g.field_71071_by.field_70461_c = slot;
/* 198 */       mc.field_71442_b.func_78750_j();
/* 199 */       this.switched = equipTool;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\me\earth\phobos\features\modules\player\BlockTweaks.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */