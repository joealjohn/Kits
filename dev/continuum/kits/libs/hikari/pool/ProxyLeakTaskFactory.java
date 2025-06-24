/*    */ package dev.continuum.kits.libs.hikari.pool;
/*    */ 
/*    */ import java.util.concurrent.ScheduledExecutorService;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class ProxyLeakTaskFactory
/*    */ {
/*    */   private ScheduledExecutorService executorService;
/*    */   private long leakDetectionThreshold;
/*    */   
/*    */   ProxyLeakTaskFactory(long leakDetectionThreshold, ScheduledExecutorService executorService) {
/* 34 */     this.executorService = executorService;
/* 35 */     this.leakDetectionThreshold = leakDetectionThreshold;
/*    */   }
/*    */ 
/*    */   
/*    */   ProxyLeakTask schedule(PoolEntry poolEntry) {
/* 40 */     return (this.leakDetectionThreshold == 0L) ? ProxyLeakTask.NO_LEAK : scheduleNewTask(poolEntry);
/*    */   }
/*    */ 
/*    */   
/*    */   void updateLeakDetectionThreshold(long leakDetectionThreshold) {
/* 45 */     this.leakDetectionThreshold = leakDetectionThreshold;
/*    */   }
/*    */   
/*    */   private ProxyLeakTask scheduleNewTask(PoolEntry poolEntry) {
/* 49 */     ProxyLeakTask task = new ProxyLeakTask(poolEntry);
/* 50 */     task.schedule(this.executorService, this.leakDetectionThreshold);
/*    */     
/* 52 */     return task;
/*    */   }
/*    */ }


/* Location:              C:\Users\joeal\Downloads\Kits-2.0.9.jar!\dev\continuum\kits\libs\hikari\pool\ProxyLeakTaskFactory.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */