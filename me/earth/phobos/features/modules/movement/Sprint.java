/*    */ package me.earth.phobos.features.modules.movement;
/*    */ 
/*    */ import me.earth.phobos.event.events.MoveEvent;
/*    */ import me.earth.phobos.features.modules.Module;
/*    */ import me.earth.phobos.features.setting.Setting;
/*    */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*    */ 
/*    */ public class Sprint
/*    */   extends Module {
/* 10 */   public Setting<Mode> mode = register(new Setting("Mode", Mode.LEGIT));
/*    */   
/* 12 */   private static Sprint INSTANCE = new Sprint();
/*    */   
/*    */   public Sprint() {
/* 15 */     super("Sprint", "Modifies sprinting", Module.Category.MOVEMENT, false, false, false);
/* 16 */     setInstance();
/*    */   }
/*    */   
/*    */   private void setInstance() {
/* 20 */     INSTANCE = this;
/*    */   }
/*    */   
/*    */   public static Sprint getInstance() {
/* 24 */     if (INSTANCE == null) {
/* 25 */       INSTANCE = new Sprint();
/*    */     }
/* 27 */     return INSTANCE;
/*    */   }
/*    */   
/*    */   public enum Mode {
/* 31 */     LEGIT,
/* 32 */     RAGE;
/*    */   }
/*    */   
/*    */   @SubscribeEvent
/*    */   public void onSprint(MoveEvent event) {
/* 37 */     if (event.getStage() == 1 && this.mode.getValue() == Mode.RAGE && (mc.field_71439_g.field_71158_b.field_192832_b != 0.0F || mc.field_71439_g.field_71158_b.field_78902_a != 0.0F)) {
/* 38 */       event.setCanceled(true);
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public void onUpdate() {
/* 44 */     switch ((Mode)this.mode.getValue()) {
/*    */       case RAGE:
/* 46 */         if ((mc.field_71474_y.field_74351_w.func_151470_d() || mc.field_71474_y.field_74368_y.func_151470_d() || mc.field_71474_y.field_74370_x.func_151470_d() || mc.field_71474_y.field_74366_z.func_151470_d()) && !mc.field_71439_g.func_70093_af() && !mc.field_71439_g.field_70123_F && mc.field_71439_g.func_71024_bL().func_75116_a() > 6.0F) {
/* 47 */           mc.field_71439_g.func_70031_b(true);
/*    */         }
/*    */         break;
/*    */       case LEGIT:
/* 51 */         if (mc.field_71474_y.field_74351_w.func_151470_d() && !mc.field_71439_g.func_70093_af() && !mc.field_71439_g.func_184587_cr() && !mc.field_71439_g.field_70123_F && mc.field_71439_g.func_71024_bL().func_75116_a() > 6.0F && mc.field_71462_r == null) {
/* 52 */           mc.field_71439_g.func_70031_b(true);
/*    */         }
/*    */         break;
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void onDisable() {
/* 60 */     if (!nullCheck()) {
/* 61 */       mc.field_71439_g.func_70031_b(false);
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public String getDisplayInfo() {
/* 67 */     return this.mode.currentEnumName();
/*    */   }
/*    */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\me\earth\phobos\features\modules\movement\Sprint.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */