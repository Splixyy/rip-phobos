/*     */ package me.earth.phobos.features.modules.render;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import me.earth.phobos.event.events.Render3DEvent;
/*     */ import me.earth.phobos.features.modules.Module;
/*     */ import me.earth.phobos.features.setting.Setting;
/*     */ import me.earth.phobos.util.ColorUtil;
/*     */ import me.earth.phobos.util.MathUtil;
/*     */ import me.earth.phobos.util.RenderUtil;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.item.EntityItemFrame;
/*     */ import net.minecraft.tileentity.TileEntity;
/*     */ import net.minecraft.util.math.BlockPos;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class StorageESP
/*     */   extends Module
/*     */ {
/*  28 */   private final Setting<Float> range = register(new Setting("Range", Float.valueOf(50.0F), Float.valueOf(1.0F), Float.valueOf(300.0F)));
/*  29 */   private final Setting<Boolean> chest = register(new Setting("Chest", Boolean.valueOf(true)));
/*  30 */   private final Setting<Boolean> dispenser = register(new Setting("Dispenser", Boolean.valueOf(false)));
/*  31 */   private final Setting<Boolean> shulker = register(new Setting("Shulker", Boolean.valueOf(true)));
/*  32 */   private final Setting<Boolean> echest = register(new Setting("Ender Chest", Boolean.valueOf(true)));
/*  33 */   private final Setting<Boolean> furnace = register(new Setting("Furnace", Boolean.valueOf(false)));
/*  34 */   private final Setting<Boolean> hopper = register(new Setting("Hopper", Boolean.valueOf(false)));
/*  35 */   private final Setting<Boolean> cart = register(new Setting("Minecart", Boolean.valueOf(false)));
/*  36 */   private final Setting<Boolean> frame = register(new Setting("Item Frame", Boolean.valueOf(false)));
/*  37 */   private final Setting<Boolean> box = register(new Setting("Box", Boolean.valueOf(false)));
/*  38 */   private final Setting<Integer> boxAlpha = register(new Setting("BoxAlpha", Integer.valueOf(125), Integer.valueOf(0), Integer.valueOf(255), v -> ((Boolean)this.box.getValue()).booleanValue()));
/*  39 */   private final Setting<Boolean> outline = register(new Setting("Outline", Boolean.valueOf(true)));
/*  40 */   private final Setting<Float> lineWidth = register(new Setting("LineWidth", Float.valueOf(1.0F), Float.valueOf(0.1F), Float.valueOf(5.0F), v -> ((Boolean)this.outline.getValue()).booleanValue()));
/*     */   
/*     */   public StorageESP() {
/*  43 */     super("StorageESP", "Highlights Containers.", Module.Category.RENDER, false, false, false);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onRender3D(Render3DEvent event) {
/*  48 */     Map<BlockPos, Integer> positions = new HashMap<>();
/*  49 */     for (TileEntity tileEntity : mc.field_71441_e.field_147482_g) {
/*  50 */       if ((tileEntity instanceof net.minecraft.tileentity.TileEntityChest && ((Boolean)this.chest.getValue()).booleanValue()) || (tileEntity instanceof net.minecraft.tileentity.TileEntityDispenser && ((Boolean)this.dispenser.getValue()).booleanValue()) || (tileEntity instanceof net.minecraft.tileentity.TileEntityShulkerBox && ((Boolean)this.shulker.getValue()).booleanValue()) || (tileEntity instanceof net.minecraft.tileentity.TileEntityEnderChest && ((Boolean)this.echest.getValue()).booleanValue()) || (tileEntity instanceof net.minecraft.tileentity.TileEntityFurnace && ((Boolean)this.furnace.getValue()).booleanValue()) || (tileEntity instanceof net.minecraft.tileentity.TileEntityHopper && ((Boolean)this.hopper.getValue()).booleanValue())) {
/*  51 */         BlockPos pos = tileEntity.func_174877_v();
/*  52 */         if (mc.field_71439_g.func_174818_b(pos) <= MathUtil.square(((Float)this.range.getValue()).floatValue())) {
/*  53 */           int color = getTileEntityColor(tileEntity);
/*  54 */           if (color != -1) {
/*  55 */             positions.put(pos, Integer.valueOf(color));
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/*  61 */     for (Entity entity : mc.field_71441_e.field_72996_f) {
/*  62 */       if ((entity instanceof EntityItemFrame && ((Boolean)this.frame.getValue()).booleanValue()) || (entity instanceof net.minecraft.entity.item.EntityMinecartChest && ((Boolean)this.cart.getValue()).booleanValue())) {
/*  63 */         BlockPos pos = entity.func_180425_c();
/*  64 */         if (mc.field_71439_g.func_174818_b(pos) <= MathUtil.square(((Float)this.range.getValue()).floatValue())) {
/*  65 */           int color = getEntityColor(entity);
/*  66 */           if (color != -1) {
/*  67 */             positions.put(pos, Integer.valueOf(color));
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/*  73 */     for (Map.Entry<BlockPos, Integer> entry : positions.entrySet()) {
/*  74 */       BlockPos blockPos = entry.getKey();
/*  75 */       int color = ((Integer)entry.getValue()).intValue();
/*  76 */       RenderUtil.drawBoxESP(blockPos, new Color(color), false, new Color(color), ((Float)this.lineWidth.getValue()).floatValue(), ((Boolean)this.outline.getValue()).booleanValue(), ((Boolean)this.box.getValue()).booleanValue(), ((Integer)this.boxAlpha.getValue()).intValue(), false);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int getTileEntityColor(TileEntity tileEntity) {
/*  86 */     if (tileEntity instanceof net.minecraft.tileentity.TileEntityChest)
/*     */     {
/*  88 */       return ColorUtil.Colors.BLUE; } 
/*  89 */     if (tileEntity instanceof net.minecraft.tileentity.TileEntityShulkerBox)
/*  90 */       return ColorUtil.Colors.RED; 
/*  91 */     if (tileEntity instanceof net.minecraft.tileentity.TileEntityEnderChest)
/*  92 */       return ColorUtil.Colors.PURPLE; 
/*  93 */     if (tileEntity instanceof net.minecraft.tileentity.TileEntityFurnace)
/*  94 */       return ColorUtil.Colors.GRAY; 
/*  95 */     if (tileEntity instanceof net.minecraft.tileentity.TileEntityHopper)
/*  96 */       return ColorUtil.Colors.DARK_RED; 
/*  97 */     if (tileEntity instanceof net.minecraft.tileentity.TileEntityDispenser) {
/*  98 */       return ColorUtil.Colors.ORANGE;
/*     */     }
/* 100 */     return -1;
/*     */   }
/*     */ 
/*     */   
/*     */   private int getEntityColor(Entity entity) {
/* 105 */     if (entity instanceof net.minecraft.entity.item.EntityMinecartChest)
/* 106 */       return ColorUtil.Colors.ORANGE; 
/* 107 */     if (entity instanceof EntityItemFrame && ((EntityItemFrame)entity).func_82335_i().func_77973_b() instanceof net.minecraft.item.ItemShulkerBox)
/* 108 */       return ColorUtil.Colors.YELLOW; 
/* 109 */     if (entity instanceof EntityItemFrame && !(((EntityItemFrame)entity).func_82335_i().func_77973_b() instanceof net.minecraft.item.ItemShulkerBox)) {
/* 110 */       return ColorUtil.Colors.ORANGE;
/*     */     }
/* 112 */     return -1;
/*     */   }
/*     */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\me\earth\phobos\features\modules\render\StorageESP.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */