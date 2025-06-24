/*    */ package dev.continuum.kits.libs.hikari.metrics.micrometer;
/*    */ 
/*    */ import dev.continuum.kits.libs.hikari.metrics.IMetricsTracker;
/*    */ import dev.continuum.kits.libs.hikari.metrics.MetricsTrackerFactory;
/*    */ import dev.continuum.kits.libs.hikari.metrics.PoolStats;
/*    */ import io.micrometer.core.instrument.MeterRegistry;
/*    */ 
/*    */ 
/*    */ public class MicrometerMetricsTrackerFactory
/*    */   implements MetricsTrackerFactory
/*    */ {
/*    */   private final MeterRegistry registry;
/*    */   
/*    */   public MicrometerMetricsTrackerFactory(MeterRegistry registry) {
/* 15 */     this.registry = registry;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public IMetricsTracker create(String poolName, PoolStats poolStats) {
/* 21 */     return new MicrometerMetricsTracker(poolName, poolStats, this.registry);
/*    */   }
/*    */ }


/* Location:              C:\Users\joeal\Downloads\Kits-2.0.9.jar!\dev\continuum\kits\libs\hikari\metrics\micrometer\MicrometerMetricsTrackerFactory.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */