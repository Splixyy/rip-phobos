/*     */ package me.earth.phobos.mixin.mixins;
/*     */ import me.earth.phobos.Phobos;
/*     */ import me.earth.phobos.event.events.BlockEvent;
/*     */ import me.earth.phobos.event.events.ProcessRightClickBlockEvent;
/*     */ import me.earth.phobos.features.modules.player.BlockTweaks;
/*     */ import me.earth.phobos.features.modules.player.Reach;
/*     */ import me.earth.phobos.features.modules.player.Speedmine;
/*     */ import me.earth.phobos.features.modules.player.TpsSync;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.entity.EntityPlayerSP;
/*     */ import net.minecraft.client.multiplayer.PlayerControllerMP;
/*     */ import net.minecraft.client.multiplayer.WorldClient;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.item.ItemBlock;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.util.EnumActionResult;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.EnumHand;
/*     */ import net.minecraft.util.math.AxisAlignedBB;
/*     */ import net.minecraft.util.math.BlockPos;
/*     */ import net.minecraft.util.math.Vec3d;
/*     */ import net.minecraft.world.IBlockAccess;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraftforge.common.MinecraftForge;
/*     */ import net.minecraftforge.fml.common.eventhandler.Event;
/*     */ import org.spongepowered.asm.mixin.Mixin;
/*     */ import org.spongepowered.asm.mixin.injection.At;
/*     */ import org.spongepowered.asm.mixin.injection.Inject;
/*     */ import org.spongepowered.asm.mixin.injection.Redirect;
/*     */ import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
/*     */ import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
/*     */ 
/*     */ @Mixin({PlayerControllerMP.class})
/*     */ public class MixinPlayerControllerMP {
/*     */   @Redirect(method = {"onPlayerDamageBlock"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/block/state/IBlockState;getPlayerRelativeBlockHardness(Lnet/minecraft/entity/player/EntityPlayer;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;)F"))
/*     */   public float getPlayerRelativeBlockHardnessHook(IBlockState state, EntityPlayer player, World worldIn, BlockPos pos) {
/*  41 */     return state.func_185903_a(player, worldIn, pos) * ((TpsSync.getInstance().isOn() && ((Boolean)(TpsSync.getInstance()).mining.getValue()).booleanValue()) ? (1.0F / Phobos.serverManager.getTpsFactor()) : 1.0F);
/*     */   }
/*     */   
/*     */   @Inject(method = {"resetBlockRemoving"}, at = {@At("HEAD")}, cancellable = true)
/*     */   public void resetBlockRemovingHook(CallbackInfo info) {
/*  46 */     if (Speedmine.getInstance().isOn() && ((Boolean)(Speedmine.getInstance()).reset.getValue()).booleanValue()) {
/*  47 */       info.cancel();
/*     */     }
/*     */   }
/*     */   
/*     */   @Inject(method = {"clickBlock"}, at = {@At("HEAD")}, cancellable = true)
/*     */   private void clickBlockHook(BlockPos pos, EnumFacing face, CallbackInfoReturnable<Boolean> info) {
/*  53 */     BlockEvent event = new BlockEvent(3, pos, face);
/*  54 */     MinecraftForge.EVENT_BUS.post((Event)event);
/*     */   }
/*     */   
/*     */   @Inject(method = {"onPlayerDamageBlock"}, at = {@At("HEAD")}, cancellable = true)
/*     */   private void onPlayerDamageBlockHook(BlockPos pos, EnumFacing face, CallbackInfoReturnable<Boolean> info) {
/*  59 */     BlockEvent event = new BlockEvent(4, pos, face);
/*  60 */     MinecraftForge.EVENT_BUS.post((Event)event);
/*     */   }
/*     */   
/*     */   @Inject(method = {"getBlockReachDistance"}, at = {@At("RETURN")}, cancellable = true)
/*     */   private void getReachDistanceHook(CallbackInfoReturnable<Float> distance) {
/*  65 */     if (Reach.getInstance().isOn()) {
/*  66 */       float range = ((Float)distance.getReturnValue()).floatValue();
/*  67 */       distance.setReturnValue(((Boolean)(Reach.getInstance()).override.getValue()).booleanValue() ? (Reach.getInstance()).reach.getValue() : Float.valueOf(range + ((Float)(Reach.getInstance()).add.getValue()).floatValue()));
/*     */     } 
/*     */   }
/*     */   
/*     */   @Redirect(method = {"processRightClickBlock"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemBlock;canPlaceBlockOnSide(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/EnumFacing;Lnet/minecraft/entity/player/EntityPlayer;Lnet/minecraft/item/ItemStack;)Z"))
/*     */   public boolean canPlaceBlockOnSideHook(ItemBlock itemBlock, World worldIn, BlockPos pos, EnumFacing side, EntityPlayer player, ItemStack stack) {
/*  73 */     Block block = worldIn.func_180495_p(pos).func_177230_c();
/*     */     
/*  75 */     if (block == Blocks.field_150431_aC && block.func_176200_f((IBlockAccess)worldIn, pos)) {
/*     */       
/*  77 */       side = EnumFacing.UP;
/*     */     }
/*  79 */     else if (!block.func_176200_f((IBlockAccess)worldIn, pos)) {
/*     */       
/*  81 */       pos = pos.func_177972_a(side);
/*     */     } 
/*     */     
/*  84 */     IBlockState iblockstate1 = worldIn.func_180495_p(pos);
/*  85 */     AxisAlignedBB axisalignedbb = itemBlock.field_150939_a.func_176223_P().func_185890_d((IBlockAccess)worldIn, pos);
/*  86 */     if (axisalignedbb != Block.field_185506_k && !worldIn.func_72917_a(axisalignedbb.func_186670_a(pos), null)) {
/*  87 */       if (BlockTweaks.getINSTANCE().isOff() || !((Boolean)(BlockTweaks.getINSTANCE()).noBlock.getValue()).booleanValue()) {
/*  88 */         return false;
/*     */       }
/*  90 */     } else if (iblockstate1.func_185904_a() == Material.field_151594_q && itemBlock.field_150939_a == Blocks.field_150467_bQ) {
/*  91 */       return true;
/*     */     } 
/*     */     
/*  94 */     return (iblockstate1.func_177230_c().func_176200_f((IBlockAccess)worldIn, pos) && itemBlock.field_150939_a.func_176198_a(worldIn, pos, side));
/*     */   }
/*     */   
/*     */   @Inject(method = {"processRightClickBlock"}, at = {@At("HEAD")}, cancellable = true)
/*     */   public void processRightClickBlock(EntityPlayerSP player, WorldClient worldIn, BlockPos pos, EnumFacing direction, Vec3d vec, EnumHand hand, CallbackInfoReturnable<EnumActionResult> cir) {
/*  99 */     ProcessRightClickBlockEvent event = new ProcessRightClickBlockEvent(pos, hand, (Minecraft.func_71410_x()).field_71439_g.func_184586_b(hand));
/* 100 */     MinecraftForge.EVENT_BUS.post((Event)event);
/* 101 */     if (event.isCanceled())
/* 102 */       cir.cancel(); 
/*     */   }
/*     */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\me\earth\phobos\mixin\mixins\MixinPlayerControllerMP.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */