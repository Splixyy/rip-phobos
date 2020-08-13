/*    */ package me.earth.phobos.mixin.mixins;
/*    */ 
/*    */ import me.earth.phobos.features.modules.movement.NoSlowDown;
/*    */ import net.minecraft.block.Block;
/*    */ import net.minecraft.block.BlockSoulSand;
/*    */ import net.minecraft.block.material.MapColor;
/*    */ import net.minecraft.block.material.Material;
/*    */ import net.minecraft.block.state.IBlockState;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.util.math.BlockPos;
/*    */ import net.minecraft.world.World;
/*    */ import org.spongepowered.asm.mixin.Mixin;
/*    */ import org.spongepowered.asm.mixin.injection.At;
/*    */ import org.spongepowered.asm.mixin.injection.Inject;
/*    */ import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
/*    */ 
/*    */ @Mixin({BlockSoulSand.class})
/*    */ public class MixinBlockSoulSand
/*    */   extends Block {
/*    */   public MixinBlockSoulSand() {
/* 21 */     super(Material.field_151595_p, MapColor.field_151650_B);
/*    */   }
/*    */   
/*    */   @Inject(method = {"onEntityCollision"}, at = {@At("HEAD")}, cancellable = true)
/*    */   public void onEntityCollisionHook(World worldIn, BlockPos pos, IBlockState state, Entity entityIn, CallbackInfo info) {
/* 26 */     if (NoSlowDown.getInstance().isOn() && ((Boolean)(NoSlowDown.getInstance()).soulSand.getValue()).booleanValue())
/* 27 */       info.cancel(); 
/*    */   }
/*    */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\me\earth\phobos\mixin\mixins\MixinBlockSoulSand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */