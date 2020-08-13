/*    */ package me.earth.phobos.features.modules.misc;
/*    */ import me.earth.phobos.Phobos;
/*    */ import me.earth.phobos.features.modules.Module;
/*    */ import me.earth.phobos.features.setting.Setting;
/*    */ import net.minecraft.network.Packet;
/*    */ import net.minecraft.network.play.server.SPacketDisconnect;
/*    */ import net.minecraft.util.text.ITextComponent;
/*    */ import net.minecraft.util.text.TextComponentString;
/*    */ 
/*    */ public class AutoLog extends Module {
/* 11 */   private Setting<Float> health = register(new Setting("Health", Float.valueOf(16.0F), Float.valueOf(0.1F), Float.valueOf(36.0F)));
/* 12 */   private Setting<Boolean> logout = register(new Setting("LogoutOff", Boolean.valueOf(true)));
/*    */   
/* 14 */   private static AutoLog INSTANCE = new AutoLog();
/*    */   
/*    */   public AutoLog() {
/* 17 */     super("AutoLog", "Logs when in danger.", Module.Category.MISC, false, false, false);
/* 18 */     setInstance();
/*    */   }
/*    */   
/*    */   private void setInstance() {
/* 22 */     INSTANCE = this;
/*    */   }
/*    */   
/*    */   public static AutoLog getInstance() {
/* 26 */     if (INSTANCE == null) {
/* 27 */       INSTANCE = new AutoLog();
/*    */     }
/* 29 */     return INSTANCE;
/*    */   }
/*    */ 
/*    */   
/*    */   public void onTick() {
/* 34 */     if (!nullCheck() && 
/* 35 */       mc.field_71439_g.func_110143_aJ() <= ((Float)this.health.getValue()).floatValue()) {
/* 36 */       Phobos.moduleManager.disableModule("AutoReconnect");
/* 37 */       mc.field_71439_g.field_71174_a.func_147297_a((Packet)new SPacketDisconnect((ITextComponent)new TextComponentString("AutoLogged")));
/* 38 */       if (((Boolean)this.logout.getValue()).booleanValue())
/* 39 */         disable(); 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\me\earth\phobos\features\modules\misc\AutoLog.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */