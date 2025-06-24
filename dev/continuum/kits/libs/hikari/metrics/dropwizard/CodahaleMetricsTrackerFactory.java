/*    */ package dev.continuum.kits.libs.hikari.metrics.dropwizard;
/*    */ 
/*    */ import com.codahale.metrics.MetricRegistry;
/*    */ import dev.continuum.kits.libs.hikari.metrics.IMetricsTracker;
/*    */ import dev.continuum.kits.libs.hikari.metrics.MetricsTrackerFactory;
/*    */ import dev.continuum.kits.libs.hikari.metrics.PoolStats;
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
/*    */ public final class CodahaleMetricsTrackerFactory
/*    */   implements MetricsTrackerFactory
/*    */ {
/*    */   private final MetricRegistry registry;
/*    */   
/*    */   public CodahaleMetricsTrackerFactory(MetricRegistry registry) {
/* 30 */     this.registry = registry;
/*    */   }
/*    */ 
/*    */   
/*    */   public MetricRegistry getRegistry() {
/* 35 */     return this.registry;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public IMetricsTracker create(String poolName, PoolStats poolStats) {
/* 41 */     return new CodaHaleMetricsTracker(poolName, poolStats, this.registry);
/*    */   }
/*    */ }


/* Location:              C:\Users\joeal\Downloads\Kits-2.0.9.jar!\dev\continuum\kits\libs\hikari\metrics\dropwizard\CodahaleMetricsTrackerFactory.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */