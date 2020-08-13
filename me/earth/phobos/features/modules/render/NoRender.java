/*     */ package me.earth.phobos.features.modules.render;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Random;
/*     */ import java.util.UUID;
/*     */ import me.earth.phobos.features.modules.Module;
/*     */ import me.earth.phobos.features.setting.Setting;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.client.gui.BossInfoClient;
/*     */ import net.minecraft.client.gui.GuiBossOverlay;
/*     */ import net.minecraft.client.gui.ScaledResolution;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.item.EntityItem;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.init.SoundEvents;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.util.EnumParticleTypes;
/*     */ import net.minecraft.util.math.BlockPos;
/*     */ import net.minecraft.world.BossInfo;
/*     */ import net.minecraft.world.GameType;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraftforge.client.event.RenderGameOverlayEvent;
/*     */ import net.minecraftforge.client.event.RenderLivingEvent;
/*     */ import net.minecraftforge.event.entity.PlaySoundAtEntityEvent;
/*     */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*     */ import org.lwjgl.opengl.GL11;
/*     */ 
/*     */ public class NoRender
/*     */   extends Module
/*     */ {
/*  34 */   public Setting<Boolean> fire = register(new Setting("Fire", Boolean.valueOf(false), "Removes the portal overlay."));
/*  35 */   public Setting<Boolean> portal = register(new Setting("Portal", Boolean.valueOf(false), "Removes the portal overlay."));
/*  36 */   public Setting<Boolean> pumpkin = register(new Setting("Pumpkin", Boolean.valueOf(false), "Removes the pumpkin overlay."));
/*  37 */   public Setting<Boolean> totemPops = register(new Setting("TotemPop", Boolean.valueOf(false), "Removes the Totem overlay."));
/*  38 */   public Setting<Boolean> items = register(new Setting("Items", Boolean.valueOf(false), "Removes items on the ground."));
/*  39 */   public Setting<Boolean> nausea = register(new Setting("Nausea", Boolean.valueOf(false), "Removes Portal Nausea."));
/*  40 */   public Setting<Boolean> hurtcam = register(new Setting("HurtCam", Boolean.valueOf(false), "Removes shaking after taking damage."));
/*  41 */   public Setting<Fog> fog = register(new Setting("Fog", Fog.NONE, "Removes Fog."));
/*  42 */   public Setting<Boolean> noWeather = register(new Setting("Weather", Boolean.valueOf(false), "AntiWeather"));
/*  43 */   public Setting<Boss> boss = register(new Setting("BossBars", Boss.NONE, "Modifies the bossbars."));
/*  44 */   public Setting<Float> scale = register(new Setting("Scale", Float.valueOf(0.0F), Float.valueOf(0.5F), Float.valueOf(1.0F), v -> (this.boss.getValue() == Boss.MINIMIZE || this.boss.getValue() != Boss.STACK), "Scale of the bars."));
/*  45 */   public Setting<Boolean> bats = register(new Setting("Bats", Boolean.valueOf(false), "Removes bats."));
/*  46 */   public Setting<NoArmor> noArmor = register(new Setting("NoArmor", NoArmor.NONE, "Doesnt Render Armor on players."));
/*  47 */   public Setting<Skylight> skylight = register(new Setting("Skylight", Skylight.NONE));
/*  48 */   public Setting<Boolean> barriers = register(new Setting("Barriers", Boolean.valueOf(false), "Barriers"));
/*     */   
/*     */   public enum Skylight {
/*  51 */     NONE,
/*  52 */     WORLD,
/*  53 */     ENTITY,
/*  54 */     ALL;
/*     */   }
/*     */   
/*  57 */   private static NoRender INSTANCE = new NoRender();
/*     */   
/*     */   public NoRender() {
/*  60 */     super("NoRender", "Allows you to stop rendering stuff", Module.Category.RENDER, true, false, false);
/*  61 */     setInstance();
/*     */   }
/*     */   
/*     */   private void setInstance() {
/*  65 */     INSTANCE = this;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onUpdate() {
/*  70 */     if (((Boolean)this.items.getValue()).booleanValue()) {
/*  71 */       mc.field_71441_e.field_72996_f.stream().filter(EntityItem.class::isInstance).map(EntityItem.class::cast).forEach(Entity::func_70106_y);
/*     */     }
/*     */     
/*  74 */     if (((Boolean)this.noWeather.getValue()).booleanValue() && mc.field_71441_e.func_72896_J()) {
/*  75 */       mc.field_71441_e.func_72894_k(0.0F);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void doVoidFogParticles(int posX, int posY, int posZ) {
/*  81 */     int i = 32;
/*  82 */     Random random = new Random();
/*  83 */     ItemStack itemstack = mc.field_71439_g.func_184614_ca();
/*  84 */     boolean flag = (!((Boolean)this.barriers.getValue()).booleanValue() || (mc.field_71442_b.func_178889_l() == GameType.CREATIVE && !itemstack.func_190926_b() && itemstack.func_77973_b() == Item.func_150898_a(Blocks.field_180401_cv)));
/*  85 */     BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
/*     */     
/*  87 */     for (int j = 0; j < 667; j++) {
/*     */       
/*  89 */       showBarrierParticles(posX, posY, posZ, 16, random, flag, blockpos$mutableblockpos);
/*  90 */       showBarrierParticles(posX, posY, posZ, 32, random, flag, blockpos$mutableblockpos);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void showBarrierParticles(int x, int y, int z, int offset, Random random, boolean holdingBarrier, BlockPos.MutableBlockPos pos) {
/*  96 */     int i = x + mc.field_71441_e.field_73012_v.nextInt(offset) - mc.field_71441_e.field_73012_v.nextInt(offset);
/*  97 */     int j = y + mc.field_71441_e.field_73012_v.nextInt(offset) - mc.field_71441_e.field_73012_v.nextInt(offset);
/*  98 */     int k = z + mc.field_71441_e.field_73012_v.nextInt(offset) - mc.field_71441_e.field_73012_v.nextInt(offset);
/*  99 */     pos.func_181079_c(i, j, k);
/* 100 */     IBlockState iblockstate = mc.field_71441_e.func_180495_p((BlockPos)pos);
/* 101 */     iblockstate.func_177230_c().func_180655_c(iblockstate, (World)mc.field_71441_e, (BlockPos)pos, random);
/*     */     
/* 103 */     if (!holdingBarrier && iblockstate.func_177230_c() == Blocks.field_180401_cv)
/*     */     {
/* 105 */       mc.field_71441_e.func_175688_a(EnumParticleTypes.BARRIER, (i + 0.5F), (j + 0.5F), (k + 0.5F), 0.0D, 0.0D, 0.0D, new int[0]);
/*     */     }
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onRenderPre(RenderGameOverlayEvent.Pre event) {
/* 111 */     if (event.getType() == RenderGameOverlayEvent.ElementType.BOSSINFO && this.boss.getValue() != Boss.NONE) {
/* 112 */       event.setCanceled(true);
/*     */     }
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onRenderPost(RenderGameOverlayEvent.Post event) {
/* 118 */     if (event.getType() == RenderGameOverlayEvent.ElementType.BOSSINFO && this.boss.getValue() != Boss.NONE) {
/* 119 */       if (this.boss.getValue() == Boss.MINIMIZE) {
/* 120 */         Map<UUID, BossInfoClient> map = (mc.field_71456_v.func_184046_j()).field_184060_g;
/* 121 */         if (map == null)
/* 122 */           return;  ScaledResolution scaledresolution = new ScaledResolution(mc);
/* 123 */         int i = scaledresolution.func_78326_a();
/* 124 */         int j = 12;
/* 125 */         for (Map.Entry<UUID, BossInfoClient> entry : map.entrySet()) {
/* 126 */           BossInfoClient info = entry.getValue();
/* 127 */           String text = info.func_186744_e().func_150254_d();
/* 128 */           int k = (int)(i / ((Float)this.scale.getValue()).floatValue() / 2.0F - 91.0F);
/* 129 */           GL11.glScaled(((Float)this.scale.getValue()).floatValue(), ((Float)this.scale.getValue()).floatValue(), 1.0D);
/* 130 */           if (!event.isCanceled()) {
/* 131 */             GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
/* 132 */             mc.func_110434_K().func_110577_a(GuiBossOverlay.field_184058_a);
/* 133 */             mc.field_71456_v.func_184046_j().func_184052_a(k, j, (BossInfo)info);
/* 134 */             mc.field_71466_p.func_175063_a(text, i / ((Float)this.scale.getValue()).floatValue() / 2.0F - (mc.field_71466_p.func_78256_a(text) / 2), (j - 9), 16777215);
/*     */           } 
/* 136 */           GL11.glScaled(1.0D / ((Float)this.scale.getValue()).floatValue(), 1.0D / ((Float)this.scale.getValue()).floatValue(), 1.0D);
/* 137 */           j += 10 + mc.field_71466_p.field_78288_b;
/*     */         } 
/* 139 */       } else if (this.boss.getValue() == Boss.STACK) {
/* 140 */         Map<UUID, BossInfoClient> map = (mc.field_71456_v.func_184046_j()).field_184060_g;
/* 141 */         HashMap<String, Pair<BossInfoClient, Integer>> to = new HashMap<>();
/* 142 */         for (Map.Entry<UUID, BossInfoClient> entry : map.entrySet()) {
/* 143 */           String s = ((BossInfoClient)entry.getValue()).func_186744_e().func_150254_d();
/* 144 */           if (to.containsKey(s)) {
/* 145 */             Pair<BossInfoClient, Integer> pair = to.get(s);
/* 146 */             pair = new Pair<>(pair.getKey(), Integer.valueOf(((Integer)pair.getValue()).intValue() + 1));
/* 147 */             to.put(s, pair); continue;
/*     */           } 
/* 149 */           Pair<BossInfoClient, Integer> p = new Pair<>(entry.getValue(), Integer.valueOf(1));
/* 150 */           to.put(s, p);
/*     */         } 
/*     */         
/* 153 */         ScaledResolution scaledresolution = new ScaledResolution(mc);
/* 154 */         int i = scaledresolution.func_78326_a();
/* 155 */         int j = 12;
/* 156 */         for (Map.Entry<String, Pair<BossInfoClient, Integer>> entry : to.entrySet()) {
/* 157 */           String text = entry.getKey();
/* 158 */           BossInfoClient info = (BossInfoClient)((Pair)entry.getValue()).getKey();
/* 159 */           int a = ((Integer)((Pair)entry.getValue()).getValue()).intValue();
/* 160 */           text = text + " x" + a;
/* 161 */           int k = (int)(i / ((Float)this.scale.getValue()).floatValue() / 2.0F - 91.0F);
/* 162 */           GL11.glScaled(((Float)this.scale.getValue()).floatValue(), ((Float)this.scale.getValue()).floatValue(), 1.0D);
/* 163 */           if (!event.isCanceled()) {
/* 164 */             GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
/* 165 */             mc.func_110434_K().func_110577_a(GuiBossOverlay.field_184058_a);
/* 166 */             mc.field_71456_v.func_184046_j().func_184052_a(k, j, (BossInfo)info);
/* 167 */             mc.field_71466_p.func_175063_a(text, i / ((Float)this.scale.getValue()).floatValue() / 2.0F - (mc.field_71466_p.func_78256_a(text) / 2), (j - 9), 16777215);
/*     */           } 
/* 169 */           GL11.glScaled(1.0D / ((Float)this.scale.getValue()).floatValue(), 1.0D / ((Float)this.scale.getValue()).floatValue(), 1.0D);
/* 170 */           j += 10 + mc.field_71466_p.field_78288_b;
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onRenderLiving(RenderLivingEvent.Pre<?> event) {
/* 178 */     if (((Boolean)this.bats.getValue()).booleanValue() && event.getEntity() instanceof net.minecraft.entity.passive.EntityBat) {
/* 179 */       event.setCanceled(true);
/*     */     }
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onPlaySound(PlaySoundAtEntityEvent event) {
/* 185 */     if ((((Boolean)this.bats.getValue()).booleanValue() && event.getSound().equals(SoundEvents.field_187740_w)) || event
/* 186 */       .getSound().equals(SoundEvents.field_187742_x) || event
/* 187 */       .getSound().equals(SoundEvents.field_187743_y) || event
/* 188 */       .getSound().equals(SoundEvents.field_189108_z) || event
/* 189 */       .getSound().equals(SoundEvents.field_187744_z)) {
/* 190 */       event.setVolume(0.0F);
/* 191 */       event.setPitch(0.0F);
/* 192 */       event.setCanceled(true);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static NoRender getInstance() {
/* 197 */     if (INSTANCE == null) {
/* 198 */       INSTANCE = new NoRender();
/*     */     }
/* 200 */     return INSTANCE;
/*     */   }
/*     */   
/*     */   public enum Fog {
/* 204 */     NONE,
/* 205 */     AIR,
/* 206 */     NOFOG;
/*     */   }
/*     */   
/*     */   public enum Boss {
/* 210 */     NONE,
/* 211 */     REMOVE,
/* 212 */     STACK,
/* 213 */     MINIMIZE;
/*     */   }
/*     */   
/*     */   public enum NoArmor {
/* 217 */     NONE,
/* 218 */     ALL,
/* 219 */     HELMET;
/*     */   }
/*     */   
/*     */   public static class Pair<T, S>
/*     */   {
/*     */     private T key;
/*     */     private S value;
/*     */     
/*     */     public Pair(T key, S value) {
/* 228 */       this.key = key;
/* 229 */       this.value = value;
/*     */     }
/*     */     
/*     */     public T getKey() {
/* 233 */       return this.key;
/*     */     }
/*     */     
/*     */     public S getValue() {
/* 237 */       return this.value;
/*     */     }
/*     */     
/*     */     public void setKey(T key) {
/* 241 */       this.key = key;
/*     */     }
/*     */     
/*     */     public void setValue(S value) {
/* 245 */       this.value = value;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\me\earth\phobos\features\modules\render\NoRender.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */