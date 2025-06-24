/*     */ package dev.continuum.kits.libs.hikari.metrics.dropwizard;
/*     */ 
/*     */ import com.codahale.metrics.Metric;
/*     */ import com.codahale.metrics.MetricRegistry;
/*     */ import com.codahale.metrics.Timer;
/*     */ import com.codahale.metrics.health.HealthCheck;
/*     */ import com.codahale.metrics.health.HealthCheckRegistry;
/*     */ import dev.continuum.kits.libs.hikari.HikariConfig;
/*     */ import dev.continuum.kits.libs.hikari.pool.HikariPool;
/*     */ import java.sql.Connection;
/*     */ import java.sql.SQLException;
/*     */ import java.util.Map;
/*     */ import java.util.Properties;
/*     */ import java.util.SortedMap;
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
/*     */ public final class CodahaleHealthChecker
/*     */ {
/*     */   public static void registerHealthChecks(HikariPool pool, HikariConfig hikariConfig, HealthCheckRegistry registry) {
/*  56 */     Properties healthCheckProperties = hikariConfig.getHealthCheckProperties();
/*     */     
/*  58 */     long checkTimeoutMs = Long.parseLong(healthCheckProperties.getProperty("connectivityCheckTimeoutMs", String.valueOf(hikariConfig.getConnectionTimeout())));
/*  59 */     registry.register(MetricRegistry.name(hikariConfig.getPoolName(), new String[] { "pool", "ConnectivityCheck" }), new ConnectivityHealthCheck(pool, checkTimeoutMs));
/*     */     
/*  61 */     long expected99thPercentile = Long.parseLong(healthCheckProperties.getProperty("expected99thPercentileMs", "0"));
/*     */     
/*  63 */     Object metricRegistryObj = hikariConfig.getMetricRegistry();
/*     */     
/*  65 */     if (expected99thPercentile > 0L && metricRegistryObj instanceof MetricRegistry) {
/*  66 */       MetricRegistry metricRegistry = (MetricRegistry)metricRegistryObj;
/*  67 */       SortedMap<String, Timer> timers = metricRegistry.getTimers((name, metric) -> name.equals(MetricRegistry.name(hikariConfig.getPoolName(), new String[] { "pool", "Wait" })));
/*     */       
/*  69 */       if (!timers.isEmpty()) {
/*  70 */         Timer timer = (Timer)((Map.Entry)timers.entrySet().iterator().next()).getValue();
/*  71 */         registry.register(MetricRegistry.name(hikariConfig.getPoolName(), new String[] { "pool", "Connection99Percent" }), new Connection99Percent(timer, expected99thPercentile));
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class ConnectivityHealthCheck
/*     */     extends HealthCheck
/*     */   {
/*     */     private final HikariPool pool;
/*     */ 
/*     */     
/*     */     private final long checkTimeoutMs;
/*     */ 
/*     */     
/*     */     ConnectivityHealthCheck(HikariPool pool, long checkTimeoutMs) {
/*  88 */       this.pool = pool;
/*  89 */       this.checkTimeoutMs = (checkTimeoutMs > 0L && checkTimeoutMs != 2147483647L) ? checkTimeoutMs : TimeUnit.SECONDS.toMillis(10L);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     protected HealthCheck.Result check() throws Exception {
/*     */       
/*  96 */       try { Connection connection = this.pool.getConnection(this.checkTimeoutMs); 
/*  97 */         try { HealthCheck.Result result = HealthCheck.Result.healthy();
/*  98 */           if (connection != null) connection.close();  return result; } catch (Throwable throwable) { if (connection != null)
/*  99 */             try { connection.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (SQLException e)
/* 100 */       { return HealthCheck.Result.unhealthy(e); }
/*     */     
/*     */     }
/*     */   }
/*     */   
/*     */   private static class Connection99Percent
/*     */     extends HealthCheck
/*     */   {
/*     */     private final Timer waitTimer;
/*     */     private final long expected99thPercentile;
/*     */     
/*     */     Connection99Percent(Timer waitTimer, long expected99thPercentile) {
/* 112 */       this.waitTimer = waitTimer;
/* 113 */       this.expected99thPercentile = expected99thPercentile;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected HealthCheck.Result check() throws Exception {
/* 120 */       long the99thPercentile = TimeUnit.NANOSECONDS.toMillis(Math.round(this.waitTimer.getSnapshot().get99thPercentile()));
/* 121 */       return (the99thPercentile <= this.expected99thPercentile) ? HealthCheck.Result.healthy() : HealthCheck.Result.unhealthy("99th percentile connection wait time of %dms exceeds the threshold %dms", new Object[] { Long.valueOf(the99thPercentile), Long.valueOf(this.expected99thPercentile) });
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\joeal\Downloads\Kits-2.0.9.jar!\dev\continuum\kits\libs\hikari\metrics\dropwizard\CodahaleHealthChecker.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */