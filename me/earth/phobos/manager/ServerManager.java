/*    */ package me.earth.phobos.manager;
/*    */ 
/*    */ import java.text.DecimalFormat;
/*    */ import java.util.Arrays;
/*    */ import java.util.Objects;
/*    */ import me.earth.phobos.features.Feature;
/*    */ import me.earth.phobos.features.modules.client.Managers;
/*    */ import me.earth.phobos.util.Timer;
/*    */ import net.minecraft.client.network.NetHandlerPlayClient;
/*    */ 
/*    */ public class ServerManager
/*    */   extends Feature {
/* 13 */   private float TPS = 20.0F;
/* 14 */   private long lastUpdate = -1L;
/* 15 */   private final float[] tpsCounts = new float[10];
/* 16 */   private final DecimalFormat format = new DecimalFormat("##.00#");
/* 17 */   private String serverBrand = "";
/* 18 */   private final Timer timer = new Timer();
/*    */   
/*    */   public void onPacketReceived() {
/* 21 */     this.timer.reset();
/*    */   }
/*    */   
/*    */   public boolean isServerNotResponding() {
/* 25 */     return this.timer.passedMs(((Integer)(Managers.getInstance()).respondTime.getValue()).intValue());
/*    */   }
/*    */   
/*    */   public long serverRespondingTime() {
/* 29 */     return this.timer.getPassedTimeMs();
/*    */   }
/*    */   
/*    */   public void update() {
/* 33 */     long currentTime = System.currentTimeMillis();
/* 34 */     if (this.lastUpdate == -1L) {
/* 35 */       this.lastUpdate = currentTime;
/*    */       return;
/*    */     } 
/* 38 */     long timeDiff = currentTime - this.lastUpdate;
/* 39 */     float tickTime = (float)timeDiff / 20.0F;
/* 40 */     if (tickTime == 0.0F) {
/* 41 */       tickTime = 50.0F;
/*    */     }
/* 43 */     float tps = 1000.0F / tickTime;
/* 44 */     if (tps > 20.0F) {
/* 45 */       tps = 20.0F;
/*    */     }
/* 47 */     System.arraycopy(this.tpsCounts, 0, this.tpsCounts, 1, this.tpsCounts.length - 1);
/* 48 */     this.tpsCounts[0] = tps;
/* 49 */     double total = 0.0D;
/* 50 */     for (float f : this.tpsCounts) {
/* 51 */       total += f;
/*    */     }
/* 53 */     total /= this.tpsCounts.length;
/*    */     
/* 55 */     if (total > 20.0D) {
/* 56 */       total = 20.0D;
/*    */     }
/*    */     
/* 59 */     this.TPS = Float.parseFloat(this.format.format(total));
/* 60 */     this.lastUpdate = currentTime;
/*    */   }
/*    */   
/*    */   public void reset() {
/* 64 */     Arrays.fill(this.tpsCounts, 20.0F);
/* 65 */     this.TPS = 20.0F;
/*    */   }
/*    */   
/*    */   public float getTpsFactor() {
/* 69 */     return 20.0F / this.TPS;
/*    */   }
/*    */   
/*    */   public float getTPS() {
/* 73 */     return this.TPS;
/*    */   }
/*    */   
/*    */   public String getServerBrand() {
/* 77 */     return this.serverBrand;
/*    */   }
/*    */   
/*    */   public void setServerBrand(String brand) {
/* 81 */     this.serverBrand = brand;
/*    */   }
/*    */   
/*    */   public int getPing() {
/* 85 */     if (fullNullCheck()) {
/* 86 */       return 0;
/*    */     }
/*    */     
/*    */     try {
/* 90 */       return ((NetHandlerPlayClient)Objects.<NetHandlerPlayClient>requireNonNull(mc.func_147114_u())).func_175102_a(mc.func_147114_u().func_175105_e().getId()).func_178853_c();
/* 91 */     } catch (Exception e) {
/* 92 */       return 0;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\me\earth\phobos\manager\ServerManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */