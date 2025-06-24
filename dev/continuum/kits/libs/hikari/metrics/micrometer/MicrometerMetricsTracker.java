/*     */ package dev.continuum.kits.libs.hikari.metrics.micrometer;
/*     */ 
/*     */ import dev.continuum.kits.libs.hikari.metrics.IMetricsTracker;
/*     */ import dev.continuum.kits.libs.hikari.metrics.PoolStats;
/*     */ import io.micrometer.core.instrument.Counter;
/*     */ import io.micrometer.core.instrument.Gauge;
/*     */ import io.micrometer.core.instrument.Meter;
/*     */ import io.micrometer.core.instrument.MeterRegistry;
/*     */ import io.micrometer.core.instrument.Timer;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MicrometerMetricsTracker
/*     */   implements IMetricsTracker
/*     */ {
/*     */   public static final String HIKARI_METRIC_NAME_PREFIX = "hikaricp";
/*     */   private static final String METRIC_CATEGORY = "pool";
/*     */   private static final String METRIC_NAME_WAIT = "hikaricp.connections.acquire";
/*     */   private static final String METRIC_NAME_USAGE = "hikaricp.connections.usage";
/*     */   private static final String METRIC_NAME_CONNECT = "hikaricp.connections.creation";
/*     */   private static final String METRIC_NAME_TIMEOUT_RATE = "hikaricp.connections.timeout";
/*     */   private static final String METRIC_NAME_TOTAL_CONNECTIONS = "hikaricp.connections";
/*     */   private static final String METRIC_NAME_IDLE_CONNECTIONS = "hikaricp.connections.idle";
/*     */   private static final String METRIC_NAME_ACTIVE_CONNECTIONS = "hikaricp.connections.active";
/*     */   private static final String METRIC_NAME_PENDING_CONNECTIONS = "hikaricp.connections.pending";
/*     */   private static final String METRIC_NAME_MAX_CONNECTIONS = "hikaricp.connections.max";
/*     */   private static final String METRIC_NAME_MIN_CONNECTIONS = "hikaricp.connections.min";
/*     */   private final Timer connectionObtainTimer;
/*     */   private final Counter connectionTimeoutCounter;
/*     */   private final Timer connectionUsage;
/*     */   private final Timer connectionCreation;
/*     */   private final Gauge totalConnectionGauge;
/*     */   private final Gauge idleConnectionGauge;
/*     */   private final Gauge activeConnectionGauge;
/*     */   private final Gauge pendingConnectionGauge;
/*     */   private final Gauge maxConnectionGauge;
/*     */   private final Gauge minConnectionGauge;
/*     */   private final MeterRegistry meterRegistry;
/*     */   private final PoolStats poolStats;
/*     */   
/*     */   MicrometerMetricsTracker(String poolName, PoolStats poolStats, MeterRegistry meterRegistry) {
/*  77 */     this.poolStats = poolStats;
/*     */     
/*  79 */     this.meterRegistry = meterRegistry;
/*     */     
/*  81 */     this
/*     */ 
/*     */       
/*  84 */       .connectionObtainTimer = Timer.builder("hikaricp.connections.acquire").description("Connection acquire time").tags(new String[] { "pool", poolName }).register(meterRegistry);
/*     */     
/*  86 */     this
/*     */ 
/*     */       
/*  89 */       .connectionCreation = Timer.builder("hikaricp.connections.creation").description("Connection creation time").tags(new String[] { "pool", poolName }).register(meterRegistry);
/*     */     
/*  91 */     this
/*     */ 
/*     */       
/*  94 */       .connectionUsage = Timer.builder("hikaricp.connections.usage").description("Connection usage time").tags(new String[] { "pool", poolName }).register(meterRegistry);
/*     */     
/*  96 */     this
/*     */ 
/*     */       
/*  99 */       .connectionTimeoutCounter = Counter.builder("hikaricp.connections.timeout").description("Connection timeout total count").tags(new String[] { "pool", poolName }).register(meterRegistry);
/*     */     
/* 101 */     this
/*     */ 
/*     */       
/* 104 */       .totalConnectionGauge = Gauge.builder("hikaricp.connections", poolStats, PoolStats::getTotalConnections).description("Total connections").tags(new String[] { "pool", poolName }).register(meterRegistry);
/*     */     
/* 106 */     this
/*     */ 
/*     */       
/* 109 */       .idleConnectionGauge = Gauge.builder("hikaricp.connections.idle", poolStats, PoolStats::getIdleConnections).description("Idle connections").tags(new String[] { "pool", poolName }).register(meterRegistry);
/*     */     
/* 111 */     this
/*     */ 
/*     */       
/* 114 */       .activeConnectionGauge = Gauge.builder("hikaricp.connections.active", poolStats, PoolStats::getActiveConnections).description("Active connections").tags(new String[] { "pool", poolName }).register(meterRegistry);
/*     */     
/* 116 */     this
/*     */ 
/*     */       
/* 119 */       .pendingConnectionGauge = Gauge.builder("hikaricp.connections.pending", poolStats, PoolStats::getPendingThreads).description("Pending threads").tags(new String[] { "pool", poolName }).register(meterRegistry);
/*     */     
/* 121 */     this
/*     */ 
/*     */       
/* 124 */       .maxConnectionGauge = Gauge.builder("hikaricp.connections.max", poolStats, PoolStats::getMaxConnections).description("Max connections").tags(new String[] { "pool", poolName }).register(meterRegistry);
/*     */     
/* 126 */     this
/*     */ 
/*     */       
/* 129 */       .minConnectionGauge = Gauge.builder("hikaricp.connections.min", poolStats, PoolStats::getMinConnections).description("Min connections").tags(new String[] { "pool", poolName }).register(meterRegistry);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void recordConnectionAcquiredNanos(long elapsedAcquiredNanos) {
/* 137 */     this.connectionObtainTimer.record(elapsedAcquiredNanos, TimeUnit.NANOSECONDS);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void recordConnectionUsageMillis(long elapsedBorrowedMillis) {
/* 144 */     this.connectionUsage.record(elapsedBorrowedMillis, TimeUnit.MILLISECONDS);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void recordConnectionTimeout() {
/* 150 */     this.connectionTimeoutCounter.increment();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void recordConnectionCreatedMillis(long connectionCreatedMillis) {
/* 156 */     this.connectionCreation.record(connectionCreatedMillis, TimeUnit.MILLISECONDS);
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() {
/* 161 */     this.meterRegistry.remove((Meter)this.connectionObtainTimer);
/* 162 */     this.meterRegistry.remove((Meter)this.connectionTimeoutCounter);
/* 163 */     this.meterRegistry.remove((Meter)this.connectionUsage);
/* 164 */     this.meterRegistry.remove((Meter)this.connectionCreation);
/* 165 */     this.meterRegistry.remove((Meter)this.totalConnectionGauge);
/* 166 */     this.meterRegistry.remove((Meter)this.idleConnectionGauge);
/* 167 */     this.meterRegistry.remove((Meter)this.activeConnectionGauge);
/* 168 */     this.meterRegistry.remove((Meter)this.pendingConnectionGauge);
/* 169 */     this.meterRegistry.remove((Meter)this.maxConnectionGauge);
/* 170 */     this.meterRegistry.remove((Meter)this.minConnectionGauge);
/*     */   }
/*     */ }


/* Location:              C:\Users\joeal\Downloads\Kits-2.0.9.jar!\dev\continuum\kits\libs\hikari\metrics\micrometer\MicrometerMetricsTracker.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */