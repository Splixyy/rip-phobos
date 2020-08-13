/*    */ package me.earth.phobos.util;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Timer
/*    */ {
/*  8 */   private long time = -1L;
/*    */ 
/*    */   
/*    */   public boolean passedS(double s) {
/* 12 */     return (getMs(System.nanoTime() - this.time) >= (long)(s * 1000.0D));
/*    */   }
/*    */   
/*    */   public boolean passedM(double m) {
/* 16 */     return (getMs(System.nanoTime() - this.time) >= (long)(m * 1000.0D * 60.0D));
/*    */   }
/*    */   
/*    */   public boolean passedDms(double dms) {
/* 20 */     return (getMs(System.nanoTime() - this.time) >= (long)(dms * 10.0D));
/*    */   }
/*    */   
/*    */   public boolean passedDs(double ds) {
/* 24 */     return (getMs(System.nanoTime() - this.time) >= (long)(ds * 100.0D));
/*    */   }
/*    */   
/*    */   public boolean passedMs(long ms) {
/* 28 */     return (getMs(System.nanoTime() - this.time) >= ms);
/*    */   }
/*    */   
/*    */   public boolean passedNS(long ns) {
/* 32 */     return (System.nanoTime() - this.time >= ns);
/*    */   }
/*    */   
/*    */   public void setMs(long ms) {
/* 36 */     this.time = System.nanoTime() - ms * 1000000L;
/*    */   }
/*    */   
/*    */   public long getPassedTimeMs() {
/* 40 */     return getMs(System.nanoTime() - this.time);
/*    */   }
/*    */   
/*    */   public void reset() {
/* 44 */     this.time = System.nanoTime();
/*    */   }
/*    */   
/*    */   public long getMs(long time) {
/* 48 */     return time / 1000000L;
/*    */   }
/*    */ }


/* Location:              C:\Users\miss_\Desktop\Phobos-1.3.3-release.jar!\me\earth\phobo\\util\Timer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */