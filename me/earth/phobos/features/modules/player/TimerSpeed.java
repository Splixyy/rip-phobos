/*    */ package me.earth.phobos.features.modules.player;
/*    */ 
/*    */ import me.earth.phobos.Phobos;
/*    */ import me.earth.phobos.features.modules.Module;
/*    */ import me.earth.phobos.features.setting.Setting;
/*    */ import me.earth.phobos.util.Timer;
/*    */ 
/*    */ public class TimerSpeed
/*    */   extends Module {
/* 10 */   public Setting<Boolean> autoOff = register(new Setting("AutoOff", Boolean.valueOf(false)));
/* 11 */   public Setting<Integer> timeLimit = register(new Setting("Limit", Integer.valueOf(250), Integer.valueOf(1), Integer.valueOf(2500), v -> ((Boolean)this.autoOff.getValue()).booleanValue()));
/* 12 */   public Setting<TimerMode> mode = register(new Setting("Mode", TimerMode.NORMAL));
/* 13 */   public Setting<Float> timerSpeed = register(new Setting("Speed", Float.valueOf(4.0F), Float.valueOf(0.1F), Float.valueOf(20.0F)));
/* 14 */   public Setting<Float> fastSpeed = register(new Setting("Fast", Float.valueOf(10.0F), Float.valueOf(0.1F), Float.valueOf(100.0F), v -> (this.mode.getValue() == TimerMode.SWITCH), "Fast Speed for switch."));
/* 15 */   public Setting<Integer> fastTime = register(new Setting("FastTime", Integer.valueOf(20), Integer.valueOf(1), Integer.valueOf(500), v -> (this.mode.getValue() == TimerMode.SWITCH), "How long you want to go fast.(ms * 10)"));
/* 16 */   public Setting<Integer> slowTime = register(new Setting("SlowTime", Integer.valueOf(20), Integer.valueOf(1), Integer.valueOf(500), v -> (this.mode.getValue() == TimerMode.SWITCH), "Recover from too fast.(ms * 10)"));
/* 17 */   public Setting<Boolean> startFast = register(new Setting("StartFast", Boolean.valueOf(false), v -> (this.mode.getValue() == TimerMode.SWITCH)));
/*    */   
/* 19 */   public float speed = 1.0F;
/* 20 */   private Timer timer = new Timer();
/* 21 */   private Timer turnOffTimer = new Timer();
/*    */   private boolean fast = false;
/*    */   
/*    */   public TimerSpeed() {
/* 25 */     super("Timer", "Will speed up the game.", Module.Category.PLAYER, false, false, false);
/*    */   }
/*    */ 
/*    */   
/*    */   public void onEnable() {
/* 30 */     this.turnOffTimer.reset();
/* 31 */     this.speed = ((Float)this.timerSpeed.getValue()).floatValue();
/* 32 */     if (!((Boolean)this.startFast.getValue()).booleanValue()) {
/* 33 */       this.timer.reset();
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public void onUpdate() {
/* 39 */     if (((Boolean)this.autoOff.getValue()).booleanValue() && this.turnOffTimer.passedMs(((Integer)this.timeLimit.getValue()).intValue())) {
/* 40 */       disable();
/*    */       
/*    */       return;
/*    */     } 
/* 44 */     if (this.mode.getValue() == TimerMode.NORMAL) {
/* 45 */       this.speed = ((Float)this.timerSpeed.getValue()).floatValue();
/*    */       
/*    */       return;
/*    */     } 
/* 49 */     if (!this.fast && this.timer.passedDms(((Integer)this.slowTime.getValue()).intValue())) {
/* 50 */       this.fast = true;
/* 51 */       this.speed = ((Float)this.fastSpeed.getValue()).floatValue();
/* 52 */       this.timer.reset();
/*    */     } 
/*    */     
/* 55 */     if (this.fast && this.timer.passedDms(((Integer)this.fastTime.getValue()).intValue())) {
/* 56 */       this.fast = false;
/* 57 */       this.speed = ((Float)this.timerSpeed.getValue()).floatValue();
/* 58 */       this.timer.reset();
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void onDisable() {
/* 64 */     this.speed = 1.0F;
/* 65 */     Phobos.timerManager.reset();
/* 66 */     this.fast = false;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getDisplayInfo() {
/* 71 */     return this.timerSpeed.getValueAsString();
/*    */   }
/*    */   
/*    */   public enum TimerMode {
/* 75 */     NORMAL,
/* 76 */     SWITCH;
/*    */   }
/*    */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\me\earth\phobos\features\modules\player\TimerSpeed.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */