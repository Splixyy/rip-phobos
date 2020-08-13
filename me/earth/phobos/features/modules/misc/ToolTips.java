/*     */ package me.earth.phobos.features.modules.misc;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import me.earth.phobos.event.events.Render2DEvent;
/*     */ import me.earth.phobos.features.modules.Module;
/*     */ import me.earth.phobos.features.setting.Bind;
/*     */ import me.earth.phobos.features.setting.Setting;
/*     */ import me.earth.phobos.util.ColorUtil;
/*     */ import me.earth.phobos.util.EntityUtil;
/*     */ import me.earth.phobos.util.RenderUtil;
/*     */ import me.earth.phobos.util.Timer;
/*     */ import net.minecraft.client.gui.inventory.GuiContainer;
/*     */ import net.minecraft.client.renderer.BufferBuilder;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.client.renderer.RenderHelper;
/*     */ import net.minecraft.client.renderer.Tessellator;
/*     */ import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.inventory.IInventory;
/*     */ import net.minecraft.inventory.ItemStackHelper;
/*     */ import net.minecraft.inventory.Slot;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemShulkerBox;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.tileentity.TileEntityShulkerBox;
/*     */ import net.minecraft.util.NonNullList;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraft.world.storage.MapData;
/*     */ import net.minecraftforge.client.event.RenderTooltipEvent;
/*     */ import net.minecraftforge.event.entity.player.ItemTooltipEvent;
/*     */ import net.minecraftforge.fml.common.eventhandler.EventPriority;
/*     */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*     */ import org.lwjgl.input.Keyboard;
/*     */ 
/*     */ 
/*     */ public class ToolTips
/*     */   extends Module
/*     */ {
/*  44 */   public Setting<Boolean> maps = register(new Setting("Maps", Boolean.valueOf(true)));
/*  45 */   public Setting<Boolean> shulkers = register(new Setting("ShulkerViewer", Boolean.valueOf(true)));
/*  46 */   public Setting<Bind> peek = register(new Setting("Peek", new Bind(-1)));
/*  47 */   public Setting<Boolean> shulkerSpy = register(new Setting("ShulkerSpy", Boolean.valueOf(true)));
/*  48 */   public Setting<Boolean> render = register(new Setting("Render", Boolean.valueOf(true), v -> ((Boolean)this.shulkerSpy.getValue()).booleanValue()));
/*  49 */   public Setting<Boolean> own = register(new Setting("OwnShulker", Boolean.valueOf(true), v -> ((Boolean)this.shulkerSpy.getValue()).booleanValue()));
/*  50 */   public Setting<Integer> cooldown = register(new Setting("ShowForS", Integer.valueOf(2), Integer.valueOf(0), Integer.valueOf(5), v -> ((Boolean)this.shulkerSpy.getValue()).booleanValue()));
/*  51 */   public Setting<Boolean> textColor = register(new Setting("TextColor", Boolean.valueOf(false), v -> ((Boolean)this.shulkers.getValue()).booleanValue()));
/*  52 */   private final Setting<Integer> red = register(new Setting("Red", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> ((Boolean)this.textColor.getValue()).booleanValue()));
/*  53 */   private final Setting<Integer> green = register(new Setting("Green", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(255), v -> ((Boolean)this.textColor.getValue()).booleanValue()));
/*  54 */   private final Setting<Integer> blue = register(new Setting("Blue", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(255), v -> ((Boolean)this.textColor.getValue()).booleanValue()));
/*  55 */   private final Setting<Integer> alpha = register(new Setting("Alpha", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> ((Boolean)this.textColor.getValue()).booleanValue()));
/*  56 */   public Setting<Boolean> offsets = register(new Setting("Offsets", Boolean.valueOf(false)));
/*  57 */   private final Setting<Integer> yPerPlayer = register(new Setting("Y/Player", Integer.valueOf(18), v -> ((Boolean)this.offsets.getValue()).booleanValue()));
/*  58 */   private final Setting<Integer> xOffset = register(new Setting("XOffset", Integer.valueOf(4), v -> ((Boolean)this.offsets.getValue()).booleanValue()));
/*  59 */   private final Setting<Integer> yOffset = register(new Setting("YOffset", Integer.valueOf(2), v -> ((Boolean)this.offsets.getValue()).booleanValue()));
/*  60 */   private final Setting<Integer> trOffset = register(new Setting("TROffset", Integer.valueOf(2), v -> ((Boolean)this.offsets.getValue()).booleanValue()));
/*  61 */   public Setting<Integer> invH = register(new Setting("InvH", Integer.valueOf(3), v -> ((Boolean)this.offsets.getValue()).booleanValue()));
/*     */   
/*  63 */   private static final ResourceLocation MAP = new ResourceLocation("textures/map/map_background.png");
/*  64 */   private static final ResourceLocation SHULKER_GUI_TEXTURE = new ResourceLocation("textures/gui/container/shulker_box.png");
/*  65 */   private static ToolTips INSTANCE = new ToolTips();
/*  66 */   public Map<EntityPlayer, ItemStack> spiedPlayers = new ConcurrentHashMap<>();
/*  67 */   public Map<EntityPlayer, Timer> playerTimers = new ConcurrentHashMap<>();
/*  68 */   private int textRadarY = 0;
/*     */   
/*     */   public ToolTips() {
/*  71 */     super("ToolTips", "Several tweaks for tooltips.", Module.Category.MISC, true, false, false);
/*  72 */     setInstance();
/*     */   }
/*     */   
/*     */   private void setInstance() {
/*  76 */     INSTANCE = this;
/*     */   }
/*     */   
/*     */   public static ToolTips getInstance() {
/*  80 */     if (INSTANCE == null) {
/*  81 */       INSTANCE = new ToolTips();
/*     */     }
/*  83 */     return INSTANCE;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onUpdate() {
/*  88 */     if (fullNullCheck() || !((Boolean)this.shulkerSpy.getValue()).booleanValue()) {
/*     */       return;
/*     */     }
/*     */     
/*  92 */     if (((Bind)this.peek.getValue()).getKey() != -1 && mc.field_71462_r instanceof GuiContainer && Keyboard.isKeyDown(((Bind)this.peek.getValue()).getKey())) {
/*  93 */       Slot slot = ((GuiContainer)mc.field_71462_r).getSlotUnderMouse();
/*  94 */       if (slot != null) {
/*  95 */         ItemStack stack = slot.func_75211_c();
/*  96 */         if (stack != null && stack.func_77973_b() instanceof ItemShulkerBox) {
/*  97 */           displayInv(stack, (String)null);
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 102 */     for (EntityPlayer player : mc.field_71441_e.field_73010_i) {
/* 103 */       if (player != null && 
/* 104 */         player.func_184614_ca() != null && player.func_184614_ca().func_77973_b() instanceof ItemShulkerBox && 
/* 105 */         !EntityUtil.isFakePlayer(player) && (((Boolean)this.own.getValue()).booleanValue() || !mc.field_71439_g.equals(player))) {
/* 106 */         ItemStack stack = player.func_184614_ca();
/* 107 */         this.spiedPlayers.put(player, stack);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void onRender2D(Render2DEvent event) {
/* 116 */     if (fullNullCheck() || !((Boolean)this.shulkerSpy.getValue()).booleanValue() || !((Boolean)this.render.getValue()).booleanValue()) {
/*     */       return;
/*     */     }
/*     */     
/* 120 */     int x = -4 + ((Integer)this.xOffset.getValue()).intValue();
/* 121 */     int y = 10 + ((Integer)this.yOffset.getValue()).intValue();
/* 122 */     this.textRadarY = 0;
/* 123 */     for (EntityPlayer player : mc.field_71441_e.field_73010_i) {
/* 124 */       if (this.spiedPlayers.get(player) != null) {
/* 125 */         if (player.func_184614_ca() == null || !(player.func_184614_ca().func_77973_b() instanceof ItemShulkerBox)) {
/* 126 */           Timer playerTimer = this.playerTimers.get(player);
/* 127 */           if (playerTimer == null) {
/* 128 */             Timer timer = new Timer();
/* 129 */             timer.reset();
/* 130 */             this.playerTimers.put(player, timer);
/*     */           }
/* 132 */           else if (playerTimer.passedS(((Integer)this.cooldown.getValue()).intValue())) {
/*     */             
/*     */             continue;
/*     */           } 
/* 136 */         } else if (player.func_184614_ca().func_77973_b() instanceof ItemShulkerBox) {
/* 137 */           Timer playerTimer = this.playerTimers.get(player);
/* 138 */           if (playerTimer != null) {
/* 139 */             playerTimer.reset();
/* 140 */             this.playerTimers.put(player, playerTimer);
/*     */           } 
/*     */         } 
/*     */         
/* 144 */         ItemStack stack = this.spiedPlayers.get(player);
/* 145 */         renderShulkerToolTip(stack, x, y, player.func_70005_c_());
/* 146 */         y += ((Integer)this.yPerPlayer.getValue()).intValue() + 60;
/* 147 */         this.textRadarY = y - 10 - ((Integer)this.yOffset.getValue()).intValue() + ((Integer)this.trOffset.getValue()).intValue();
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public int getTextRadarY() {
/* 153 */     return this.textRadarY;
/*     */   }
/*     */ 
/*     */   
/*     */   @SubscribeEvent(priority = EventPriority.HIGHEST)
/*     */   public void makeTooltip(ItemTooltipEvent event) {}
/*     */ 
/*     */   
/*     */   @SubscribeEvent
/*     */   public void renderTooltip(RenderTooltipEvent.PostText event) {
/* 163 */     if (((Boolean)this.maps.getValue()).booleanValue() && !event.getStack().func_190926_b() && event.getStack().func_77973_b() instanceof net.minecraft.item.ItemMap) {
/* 164 */       MapData mapData = Items.field_151098_aY.func_77873_a(event.getStack(), (World)mc.field_71441_e);
/* 165 */       if (mapData != null) {
/* 166 */         GlStateManager.func_179094_E();
/* 167 */         GlStateManager.func_179124_c(1.0F, 1.0F, 1.0F);
/* 168 */         RenderHelper.func_74518_a();
/* 169 */         mc.func_110434_K().func_110577_a(MAP);
/* 170 */         Tessellator instance = Tessellator.func_178181_a();
/* 171 */         BufferBuilder buffer = instance.func_178180_c();
/* 172 */         int n = 7;
/* 173 */         float n2 = 135.0F;
/* 174 */         float n3 = 0.5F;
/* 175 */         GlStateManager.func_179109_b(event.getX(), event.getY() - n2 * n3 - 5.0F, 0.0F);
/* 176 */         GlStateManager.func_179152_a(n3, n3, n3);
/* 177 */         buffer.func_181668_a(7, DefaultVertexFormats.field_181707_g);
/* 178 */         buffer.func_181662_b(-n, n2, 0.0D).func_187315_a(0.0D, 1.0D).func_181675_d();
/* 179 */         buffer.func_181662_b(n2, n2, 0.0D).func_187315_a(1.0D, 1.0D).func_181675_d();
/* 180 */         buffer.func_181662_b(n2, -n, 0.0D).func_187315_a(1.0D, 0.0D).func_181675_d();
/* 181 */         buffer.func_181662_b(-n, -n, 0.0D).func_187315_a(0.0D, 0.0D).func_181675_d();
/* 182 */         instance.func_78381_a();
/* 183 */         mc.field_71460_t.func_147701_i().func_148250_a(mapData, false);
/* 184 */         GlStateManager.func_179145_e();
/* 185 */         GlStateManager.func_179121_F();
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void renderShulkerToolTip(ItemStack stack, int x, int y, String name) {
/* 191 */     NBTTagCompound tagCompound = stack.func_77978_p();
/* 192 */     if (tagCompound != null && tagCompound.func_150297_b("BlockEntityTag", 10)) {
/* 193 */       NBTTagCompound blockEntityTag = tagCompound.func_74775_l("BlockEntityTag");
/* 194 */       if (blockEntityTag.func_150297_b("Items", 9)) {
/* 195 */         GlStateManager.func_179098_w();
/* 196 */         GlStateManager.func_179140_f();
/* 197 */         GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
/* 198 */         GlStateManager.func_179147_l();
/* 199 */         GlStateManager.func_187428_a(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
/* 200 */         mc.func_110434_K().func_110577_a(SHULKER_GUI_TEXTURE);
/* 201 */         RenderUtil.drawTexturedRect(x, y, 0, 0, 176, 16, 500);
/* 202 */         RenderUtil.drawTexturedRect(x, y + 16, 0, 16, 176, 54 + ((Integer)this.invH.getValue()).intValue(), 500);
/* 203 */         RenderUtil.drawTexturedRect(x, y + 16 + 54, 0, 160, 176, 8, 500);
/* 204 */         GlStateManager.func_179097_i();
/* 205 */         Color color = new Color(0, 0, 0, 255);
/* 206 */         if (((Boolean)this.textColor.getValue()).booleanValue()) {
/* 207 */           color = new Color(((Integer)this.red.getValue()).intValue(), ((Integer)this.green.getValue()).intValue(), ((Integer)this.blue.getValue()).intValue(), ((Integer)this.alpha.getValue()).intValue());
/*     */         }
/* 209 */         this.renderer.drawStringWithShadow((name == null) ? stack.func_82833_r() : name, (x + 8), (y + 6), ColorUtil.toRGBA(color));
/* 210 */         GlStateManager.func_179126_j();
/* 211 */         RenderHelper.func_74520_c();
/* 212 */         GlStateManager.func_179091_B();
/* 213 */         GlStateManager.func_179142_g();
/* 214 */         GlStateManager.func_179145_e();
/* 215 */         NonNullList<ItemStack> nonnulllist = NonNullList.func_191197_a(27, ItemStack.field_190927_a);
/* 216 */         ItemStackHelper.func_191283_b(blockEntityTag, nonnulllist);
/*     */         
/* 218 */         for (int i = 0; i < nonnulllist.size(); i++) {
/* 219 */           int iX = x + i % 9 * 18 + 8;
/* 220 */           int iY = y + i / 9 * 18 + 18;
/* 221 */           ItemStack itemStack = (ItemStack)nonnulllist.get(i);
/* 222 */           (mc.func_175599_af()).field_77023_b = 501.0F;
/* 223 */           RenderUtil.itemRender.func_180450_b(itemStack, iX, iY);
/* 224 */           RenderUtil.itemRender.func_180453_a(mc.field_71466_p, itemStack, iX, iY, null);
/* 225 */           (mc.func_175599_af()).field_77023_b = 0.0F;
/*     */         } 
/*     */         
/* 228 */         GlStateManager.func_179140_f();
/* 229 */         GlStateManager.func_179084_k();
/* 230 */         GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void displayInv(ItemStack stack, String name) {
/*     */     try {
/* 237 */       Item item = stack.func_77973_b();
/* 238 */       TileEntityShulkerBox entityBox = new TileEntityShulkerBox();
/* 239 */       ItemShulkerBox shulker = (ItemShulkerBox)item;
/* 240 */       entityBox.field_145854_h = shulker.func_179223_d();
/* 241 */       entityBox.func_145834_a((World)mc.field_71441_e);
/* 242 */       ItemStackHelper.func_191283_b(stack.func_77978_p().func_74775_l("BlockEntityTag"), entityBox.field_190596_f);
/* 243 */       entityBox.func_145839_a(stack.func_77978_p().func_74775_l("BlockEntityTag"));
/* 244 */       entityBox.func_190575_a((name == null) ? stack.func_82833_r() : name);
/* 245 */       (new Thread(() -> {
/*     */             try {
/*     */               Thread.sleep(200L);
/* 248 */             } catch (InterruptedException interruptedException) {}
/*     */             mc.field_71439_g.func_71007_a((IInventory)entityBox);
/* 250 */           })).start();
/* 251 */     } catch (Exception exception) {}
/*     */   }
/*     */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\me\earth\phobos\features\modules\misc\ToolTips.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */