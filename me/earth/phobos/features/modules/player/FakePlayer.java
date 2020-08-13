/*     */ package me.earth.phobos.features.modules.player;
/*     */ import com.mojang.authlib.GameProfile;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.UUID;
/*     */ import me.earth.phobos.Phobos;
/*     */ import me.earth.phobos.features.modules.Module;
/*     */ import me.earth.phobos.features.setting.Setting;
/*     */ import net.minecraft.client.entity.EntityOtherPlayerMP;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.potion.PotionEffect;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class FakePlayer extends Module {
/*  16 */   private Setting<Boolean> copyInv = register(new Setting("CopyInv", Boolean.valueOf(true)));
/*  17 */   public Setting<Boolean> multi = register(new Setting("Multi", Boolean.valueOf(false)));
/*  18 */   private Setting<Integer> players = register(new Setting("Players", Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(9), v -> ((Boolean)this.multi.getValue()).booleanValue(), "Amount of other players."));
/*     */   
/*  20 */   private static final String[] fitInfo = new String[] { "fdee323e-7f0c-4c15-8d1c-0f277442342a", "Fit" };
/*  21 */   public static final String[][] phobosInfo = new String[][] { { "8af022c8-b926-41a0-8b79-2b544ff00fcf", "3arthqu4ke", "3", "0" }, { "0aa3b04f-786a-49c8-bea9-025ee0dd1e85", "zb0b", "-3", "0" }, { "19bf3f1f-fe06-4c86-bea5-3dad5df89714", "3vt", "0", "-3" }, { "e47d6571-99c2-415b-955e-c4bc7b55941b", "Phobos_eu", "0", "3" }, { "b01f9bc1-cb7c-429a-b178-93d771f00926", "bakpotatisen", "6", "0" }, { "b232930c-c28a-4e10-8c90-f152235a65c5", "948", "-6", "0" }, { "ace08461-3db3-4579-98d3-390a67d5645b", "Browswer", "0", "-6" }, { "5bead5b0-3bab-460d-af1d-7929950f40c2", "fsck", "0", "6" }, { "78ee2bd6-64c4-45f0-96e5-0b6747ba7382", "Fit", "0", "9" }, { "78ee2bd6-64c4-45f0-96e5-0b6747ba7382", "deathcurz0", "0", "-9" } };
/*  22 */   public List<Integer> fakePlayerIdList = new ArrayList<>();
/*  23 */   private final List<EntityOtherPlayerMP> fakeEntities = new ArrayList<>();
/*     */   
/*  25 */   private static FakePlayer INSTANCE = new FakePlayer();
/*     */   
/*     */   public FakePlayer() {
/*  28 */     super("FakePlayer", "Spawns in a fake player", Module.Category.PLAYER, true, false, false);
/*  29 */     setInstance();
/*     */   }
/*     */   
/*     */   private void setInstance() {
/*  33 */     INSTANCE = this;
/*     */   }
/*     */   
/*     */   public static FakePlayer getInstance() {
/*  37 */     if (INSTANCE == null) {
/*  38 */       INSTANCE = new FakePlayer();
/*     */     }
/*  40 */     return INSTANCE;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onLoad() {
/*  45 */     disable();
/*     */   }
/*     */ 
/*     */   
/*     */   public void onEnable() {
/*  50 */     if (fullNullCheck()) {
/*  51 */       disable();
/*     */       
/*     */       return;
/*     */     } 
/*  55 */     this.fakePlayerIdList = new ArrayList<>();
/*  56 */     if (((Boolean)this.multi.getValue()).booleanValue()) {
/*  57 */       int amount = 0;
/*  58 */       int entityId = -101;
/*  59 */       for (String[] data : phobosInfo) {
/*  60 */         addFakePlayer(data[0], data[1], entityId, Integer.parseInt(data[2]), Integer.parseInt(data[3]));
/*  61 */         amount++;
/*  62 */         if (amount >= ((Integer)this.players.getValue()).intValue()) {
/*     */           return;
/*     */         }
/*  65 */         entityId -= amount;
/*     */       } 
/*     */     } else {
/*  68 */       addFakePlayer(fitInfo[0], fitInfo[1], -100, 0, 0);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onDisable() {
/*  74 */     if (fullNullCheck())
/*  75 */       return;  for (Iterator<Integer> iterator = this.fakePlayerIdList.iterator(); iterator.hasNext(); ) { int id = ((Integer)iterator.next()).intValue();
/*  76 */       mc.field_71441_e.func_73028_b(id); }
/*     */   
/*     */   }
/*     */ 
/*     */   
/*     */   public void onLogout() {
/*  82 */     if (isOn()) {
/*  83 */       disable();
/*     */     }
/*     */   }
/*     */   
/*     */   private void addFakePlayer(String uuid, String name, int entityId, int offsetX, int offsetZ) {
/*  88 */     GameProfile profile = new GameProfile(UUID.fromString(uuid), name);
/*  89 */     EntityOtherPlayerMP fakePlayer = new EntityOtherPlayerMP((World)mc.field_71441_e, profile);
/*  90 */     fakePlayer.func_82149_j((Entity)mc.field_71439_g);
/*  91 */     fakePlayer.field_70165_t += offsetX;
/*  92 */     fakePlayer.field_70161_v += offsetZ;
/*  93 */     if (((Boolean)this.copyInv.getValue()).booleanValue()) {
/*  94 */       for (PotionEffect potionEffect : Phobos.potionManager.getOwnPotions()) {
/*  95 */         fakePlayer.func_70690_d(potionEffect);
/*     */       }
/*  97 */       fakePlayer.field_71071_by.func_70455_b(mc.field_71439_g.field_71071_by);
/*     */     } 
/*  99 */     fakePlayer.func_70606_j(mc.field_71439_g.func_110143_aJ() + mc.field_71439_g.func_110139_bj());
/* 100 */     this.fakeEntities.add(fakePlayer);
/* 101 */     mc.field_71441_e.func_73027_a(entityId, (Entity)fakePlayer);
/* 102 */     this.fakePlayerIdList.add(Integer.valueOf(entityId));
/*     */   }
/*     */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\me\earth\phobos\features\modules\player\FakePlayer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */