/*     */ package me.earth.phobos.features.modules.player;
/*     */ 
/*     */ import me.earth.phobos.event.events.PacketEvent;
/*     */ import me.earth.phobos.event.events.PushEvent;
/*     */ import me.earth.phobos.features.modules.Module;
/*     */ import me.earth.phobos.features.setting.Setting;
/*     */ import me.earth.phobos.util.MathUtil;
/*     */ import net.minecraft.client.entity.EntityOtherPlayerMP;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.util.math.AxisAlignedBB;
/*     */ import net.minecraft.util.math.Vec3d;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*     */ 
/*     */ 
/*     */ public class Freecam
/*     */   extends Module
/*     */ {
/*  19 */   public Setting<Double> speed = register(new Setting("Speed", Double.valueOf(0.5D), Double.valueOf(0.1D), Double.valueOf(5.0D)));
/*  20 */   public Setting<Boolean> view = register(new Setting("3D", Boolean.valueOf(false)));
/*  21 */   public Setting<Boolean> packet = register(new Setting("Packet", Boolean.valueOf(true)));
/*  22 */   public Setting<Boolean> disable = register(new Setting("Logout/Off", Boolean.valueOf(true)));
/*     */   
/*  24 */   private static Freecam INSTANCE = new Freecam();
/*     */   
/*     */   private AxisAlignedBB oldBoundingBox;
/*     */   private EntityOtherPlayerMP entity;
/*     */   private Vec3d position;
/*     */   private Entity riding;
/*     */   private float yaw;
/*     */   private float pitch;
/*     */   
/*     */   public Freecam() {
/*  34 */     super("Freecam", "Look around freely.", Module.Category.PLAYER, true, false, false);
/*  35 */     setInstance();
/*     */   }
/*     */   
/*     */   private void setInstance() {
/*  39 */     INSTANCE = this;
/*     */   }
/*     */   
/*     */   public static Freecam getInstance() {
/*  43 */     if (INSTANCE == null) {
/*  44 */       INSTANCE = new Freecam();
/*     */     }
/*  46 */     return INSTANCE;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onEnable() {
/*  51 */     if (!fullNullCheck()) {
/*  52 */       this.oldBoundingBox = mc.field_71439_g.func_174813_aQ();
/*  53 */       mc.field_71439_g.func_174826_a(new AxisAlignedBB(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u, mc.field_71439_g.field_70161_v, mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u, mc.field_71439_g.field_70161_v));
/*  54 */       if (mc.field_71439_g.func_184187_bx() != null) {
/*  55 */         this.riding = mc.field_71439_g.func_184187_bx();
/*  56 */         mc.field_71439_g.func_184210_p();
/*     */       } 
/*  58 */       this.entity = new EntityOtherPlayerMP((World)mc.field_71441_e, mc.field_71449_j.func_148256_e());
/*  59 */       this.entity.func_82149_j((Entity)mc.field_71439_g);
/*  60 */       this.entity.field_70177_z = mc.field_71439_g.field_70177_z;
/*  61 */       this.entity.field_70759_as = mc.field_71439_g.field_70759_as;
/*  62 */       this.entity.field_71071_by.func_70455_b(mc.field_71439_g.field_71071_by);
/*  63 */       mc.field_71441_e.func_73027_a(69420, (Entity)this.entity);
/*  64 */       this.position = mc.field_71439_g.func_174791_d();
/*  65 */       this.yaw = mc.field_71439_g.field_70177_z;
/*  66 */       this.pitch = mc.field_71439_g.field_70125_A;
/*  67 */       mc.field_71439_g.field_70145_X = true;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onDisable() {
/*  73 */     if (!fullNullCheck()) {
/*  74 */       mc.field_71439_g.func_174826_a(this.oldBoundingBox);
/*  75 */       if (this.riding != null) {
/*  76 */         mc.field_71439_g.func_184205_a(this.riding, true);
/*     */       }
/*  78 */       if (this.entity != null) {
/*  79 */         mc.field_71441_e.func_72900_e((Entity)this.entity);
/*     */       }
/*  81 */       if (this.position != null) {
/*  82 */         mc.field_71439_g.func_70107_b(this.position.field_72450_a, this.position.field_72448_b, this.position.field_72449_c);
/*     */       }
/*  84 */       mc.field_71439_g.field_70177_z = this.yaw;
/*  85 */       mc.field_71439_g.field_70125_A = this.pitch;
/*  86 */       mc.field_71439_g.field_70145_X = false;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onUpdate() {
/*  92 */     mc.field_71439_g.field_70145_X = true;
/*  93 */     mc.field_71439_g.func_70016_h(0.0D, 0.0D, 0.0D);
/*  94 */     mc.field_71439_g.field_70747_aH = ((Double)this.speed.getValue()).floatValue();
/*  95 */     double[] dir = MathUtil.directionSpeed(((Double)this.speed.getValue()).doubleValue());
/*  96 */     if (mc.field_71439_g.field_71158_b.field_78902_a != 0.0F || mc.field_71439_g.field_71158_b.field_192832_b != 0.0F) {
/*  97 */       mc.field_71439_g.field_70159_w = dir[0];
/*  98 */       mc.field_71439_g.field_70179_y = dir[1];
/*     */     } else {
/* 100 */       mc.field_71439_g.field_70159_w = 0.0D;
/* 101 */       mc.field_71439_g.field_70179_y = 0.0D;
/*     */     } 
/* 103 */     mc.field_71439_g.func_70031_b(false);
/* 104 */     if (((Boolean)this.view.getValue()).booleanValue() && !mc.field_71474_y.field_74311_E.func_151470_d() && !mc.field_71474_y.field_74314_A.func_151470_d()) {
/* 105 */       mc.field_71439_g.field_70181_x = ((Double)this.speed.getValue()).doubleValue() * -MathUtil.degToRad(mc.field_71439_g.field_70125_A) * mc.field_71439_g.field_71158_b.field_192832_b;
/*     */     }
/*     */     
/* 108 */     if (mc.field_71474_y.field_74314_A.func_151470_d()) {
/* 109 */       mc.field_71439_g.field_70181_x += ((Double)this.speed.getValue()).doubleValue();
/*     */     }
/*     */     
/* 112 */     if (mc.field_71474_y.field_74311_E.func_151470_d()) {
/* 113 */       mc.field_71439_g.field_70181_x -= ((Double)this.speed.getValue()).doubleValue();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void onLogout() {
/* 119 */     if (((Boolean)this.disable.getValue()).booleanValue()) {
/* 120 */       disable();
/*     */     }
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onPacketSend(PacketEvent.Send event) {
/* 126 */     if (event.getStage() == 0 && (event.getPacket() instanceof net.minecraft.network.play.client.CPacketPlayer || event.getPacket() instanceof net.minecraft.network.play.client.CPacketInput)) {
/* 127 */       event.setCanceled(true);
/*     */     }
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onPush(PushEvent event) {
/* 133 */     if (event.getStage() == 1)
/* 134 */       event.setCanceled(true); 
/*     */   }
/*     */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\me\earth\phobos\features\modules\player\Freecam.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */