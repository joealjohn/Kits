/*     */ package dev.continuum.kits.libs.hikari.pool;
/*     */ 
/*     */ import com.codahale.metrics.MetricRegistry;
/*     */ import com.codahale.metrics.health.HealthCheckRegistry;
/*     */ import dev.continuum.kits.libs.hikari.HikariConfig;
/*     */ import dev.continuum.kits.libs.hikari.HikariPoolMXBean;
/*     */ import dev.continuum.kits.libs.hikari.metrics.MetricsTrackerFactory;
/*     */ import dev.continuum.kits.libs.hikari.metrics.PoolStats;
/*     */ import dev.continuum.kits.libs.hikari.metrics.dropwizard.CodahaleHealthChecker;
/*     */ import dev.continuum.kits.libs.hikari.metrics.dropwizard.CodahaleMetricsTrackerFactory;
/*     */ import dev.continuum.kits.libs.hikari.metrics.micrometer.MicrometerMetricsTrackerFactory;
/*     */ import dev.continuum.kits.libs.hikari.util.ClockSource;
/*     */ import dev.continuum.kits.libs.hikari.util.ConcurrentBag;
/*     */ import dev.continuum.kits.libs.hikari.util.SuspendResumeLock;
/*     */ import dev.continuum.kits.libs.hikari.util.UtilityElf;
/*     */ import io.micrometer.core.instrument.MeterRegistry;
/*     */ import java.sql.Connection;
/*     */ import java.sql.SQLException;
/*     */ import java.sql.SQLTransientConnectionException;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.concurrent.Callable;
/*     */ import java.util.concurrent.ExecutorService;
/*     */ import java.util.concurrent.LinkedBlockingQueue;
/*     */ import java.util.concurrent.RejectedExecutionHandler;
/*     */ import java.util.concurrent.ScheduledExecutorService;
/*     */ import java.util.concurrent.ScheduledFuture;
/*     */ import java.util.concurrent.ScheduledThreadPoolExecutor;
/*     */ import java.util.concurrent.ThreadFactory;
/*     */ import java.util.concurrent.ThreadLocalRandom;
/*     */ import java.util.concurrent.ThreadPoolExecutor;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
/*     */ import javax.sql.DataSource;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
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
/*     */ public final class HikariPool
/*     */   extends PoolBase
/*     */   implements HikariPoolMXBean, ConcurrentBag.IBagStateListener
/*     */ {
/*  57 */   private final Logger logger = LoggerFactory.getLogger(HikariPool.class);
/*     */   
/*     */   public static final int POOL_NORMAL = 0;
/*     */   
/*     */   public static final int POOL_SUSPENDED = 1;
/*     */   
/*     */   public static final int POOL_SHUTDOWN = 2;
/*     */   public volatile int poolState;
/*  65 */   private final long aliveBypassWindowMs = Long.getLong("dev.continuum.kits.libs.hikari.aliveBypassWindowMs", TimeUnit.MILLISECONDS.toMillis(500L)).longValue();
/*  66 */   private final long housekeepingPeriodMs = Long.getLong("dev.continuum.kits.libs.hikari.housekeeping.periodMs", TimeUnit.SECONDS.toMillis(30L)).longValue();
/*     */   
/*     */   private static final String EVICTED_CONNECTION_MESSAGE = "(connection was evicted)";
/*     */   
/*     */   private static final String DEAD_CONNECTION_MESSAGE = "(connection is dead)";
/*  71 */   private final PoolEntryCreator poolEntryCreator = new PoolEntryCreator();
/*  72 */   private final PoolEntryCreator postFillPoolEntryCreator = new PoolEntryCreator("After adding ");
/*     */ 
/*     */   
/*     */   private final ThreadPoolExecutor addConnectionExecutor;
/*     */   
/*     */   private final ThreadPoolExecutor closeConnectionExecutor;
/*     */   
/*     */   private final ConcurrentBag<PoolEntry> connectionBag;
/*     */   
/*     */   private final ProxyLeakTaskFactory leakTaskFactory;
/*     */   
/*     */   private final SuspendResumeLock suspendResumeLock;
/*     */   
/*     */   private final ScheduledExecutorService houseKeepingExecutorService;
/*     */   
/*     */   private ScheduledFuture<?> houseKeeperTask;
/*     */ 
/*     */   
/*     */   public HikariPool(HikariConfig config) {
/*  91 */     super(config);
/*     */     
/*  93 */     this.connectionBag = new ConcurrentBag(this);
/*  94 */     this.suspendResumeLock = config.isAllowPoolSuspension() ? new SuspendResumeLock() : SuspendResumeLock.FAUX_LOCK;
/*     */     
/*  96 */     this.houseKeepingExecutorService = initializeHouseKeepingExecutorService();
/*     */     
/*  98 */     checkFailFast();
/*     */     
/* 100 */     if (config.getMetricsTrackerFactory() != null) {
/* 101 */       setMetricsTrackerFactory(config.getMetricsTrackerFactory());
/*     */     } else {
/*     */       
/* 104 */       setMetricRegistry(config.getMetricRegistry());
/*     */     } 
/*     */     
/* 107 */     setHealthCheckRegistry(config.getHealthCheckRegistry());
/*     */     
/* 109 */     handleMBeans(this, true);
/*     */     
/* 111 */     ThreadFactory threadFactory = config.getThreadFactory();
/*     */     
/* 113 */     int maxPoolSize = config.getMaximumPoolSize();
/* 114 */     LinkedBlockingQueue<Runnable> addConnectionQueue = new LinkedBlockingQueue<>(maxPoolSize);
/* 115 */     this.addConnectionExecutor = UtilityElf.createThreadPoolExecutor(addConnectionQueue, this.poolName + " connection adder", threadFactory, (RejectedExecutionHandler)new UtilityElf.CustomDiscardPolicy());
/* 116 */     this.closeConnectionExecutor = UtilityElf.createThreadPoolExecutor(maxPoolSize, this.poolName + " connection closer", threadFactory, new ThreadPoolExecutor.CallerRunsPolicy());
/*     */     
/* 118 */     this.leakTaskFactory = new ProxyLeakTaskFactory(config.getLeakDetectionThreshold(), this.houseKeepingExecutorService);
/*     */     
/* 120 */     this.houseKeeperTask = this.houseKeepingExecutorService.scheduleWithFixedDelay(new HouseKeeper(), 100L, this.housekeepingPeriodMs, TimeUnit.MILLISECONDS);
/*     */     
/* 122 */     if (Boolean.getBoolean("dev.continuum.kits.libs.hikari.blockUntilFilled") && config.getInitializationFailTimeout() > 1L) {
/* 123 */       this.addConnectionExecutor.setMaximumPoolSize(Math.min(16, Runtime.getRuntime().availableProcessors()));
/* 124 */       this.addConnectionExecutor.setCorePoolSize(Math.min(16, Runtime.getRuntime().availableProcessors()));
/*     */       
/* 126 */       long startTime = ClockSource.currentTime();
/* 127 */       while (ClockSource.elapsedMillis(startTime) < config.getInitializationFailTimeout() && getTotalConnections() < config.getMinimumIdle()) {
/* 128 */         UtilityElf.quietlySleep(TimeUnit.MILLISECONDS.toMillis(100L));
/*     */       }
/*     */       
/* 131 */       this.addConnectionExecutor.setCorePoolSize(1);
/* 132 */       this.addConnectionExecutor.setMaximumPoolSize(1);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Connection getConnection() throws SQLException {
/* 144 */     return getConnection(this.connectionTimeout);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Connection getConnection(long hardTimeout) throws SQLException {
/* 156 */     this.suspendResumeLock.acquire();
/* 157 */     long startTime = ClockSource.currentTime();
/*     */     
/*     */     try {
/* 160 */       long timeout = hardTimeout;
/*     */       do {
/* 162 */         PoolEntry poolEntry = (PoolEntry)this.connectionBag.borrow(timeout, TimeUnit.MILLISECONDS);
/* 163 */         if (poolEntry == null) {
/*     */           break;
/*     */         }
/*     */         
/* 167 */         long now = ClockSource.currentTime();
/* 168 */         if (poolEntry.isMarkedEvicted() || (ClockSource.elapsedMillis(poolEntry.lastAccessed, now) > this.aliveBypassWindowMs && isConnectionDead(poolEntry.connection))) {
/* 169 */           closeConnection(poolEntry, poolEntry.isMarkedEvicted() ? "(connection was evicted)" : "(connection is dead)");
/* 170 */           timeout = hardTimeout - ClockSource.elapsedMillis(startTime);
/*     */         } else {
/*     */           
/* 173 */           this.metricsTracker.recordBorrowStats(poolEntry, startTime);
/* 174 */           return poolEntry.createProxyConnection(this.leakTaskFactory.schedule(poolEntry));
/*     */         } 
/* 176 */       } while (timeout > 0L);
/*     */       
/* 178 */       this.metricsTracker.recordBorrowTimeoutStats(startTime);
/* 179 */       throw createTimeoutException(startTime);
/*     */     }
/* 181 */     catch (InterruptedException e) {
/* 182 */       Thread.currentThread().interrupt();
/* 183 */       throw new SQLException(this.poolName + " - Interrupted during connection acquisition", e);
/*     */     } finally {
/*     */       
/* 186 */       this.suspendResumeLock.release();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void shutdown() throws InterruptedException {
/*     */     try {
/* 199 */       this.poolState = 2;
/*     */       
/* 201 */       if (this.addConnectionExecutor == null) {
/*     */         return;
/*     */       }
/*     */       
/* 205 */       logPoolState(new String[] { "Before shutdown " });
/*     */       
/* 207 */       if (this.houseKeeperTask != null) {
/* 208 */         this.houseKeeperTask.cancel(false);
/* 209 */         this.houseKeeperTask = null;
/*     */       } 
/*     */       
/* 212 */       softEvictConnections();
/*     */       
/* 214 */       this.addConnectionExecutor.shutdown();
/* 215 */       if (!this.addConnectionExecutor.awaitTermination(getLoginTimeout(), TimeUnit.SECONDS)) {
/* 216 */         this.logger.warn("Timed-out waiting for add connection executor to shutdown");
/*     */       }
/*     */       
/* 219 */       destroyHouseKeepingExecutorService();
/*     */       
/* 221 */       this.connectionBag.close();
/*     */       
/* 223 */       ThreadPoolExecutor assassinExecutor = UtilityElf.createThreadPoolExecutor(this.config.getMaximumPoolSize(), this.poolName + " connection assassinator", this.config
/* 224 */           .getThreadFactory(), new ThreadPoolExecutor.CallerRunsPolicy());
/*     */       try {
/* 226 */         long start = ClockSource.currentTime();
/*     */         do {
/* 228 */           abortActiveConnections(assassinExecutor);
/* 229 */           softEvictConnections();
/* 230 */         } while (getTotalConnections() > 0 && ClockSource.elapsedMillis(start) < TimeUnit.SECONDS.toMillis(10L));
/*     */       } finally {
/*     */         
/* 233 */         assassinExecutor.shutdown();
/* 234 */         if (!assassinExecutor.awaitTermination(10L, TimeUnit.SECONDS)) {
/* 235 */           this.logger.warn("Timed-out waiting for connection assassin to shutdown");
/*     */         }
/*     */       } 
/*     */       
/* 239 */       shutdownNetworkTimeoutExecutor();
/* 240 */       this.closeConnectionExecutor.shutdown();
/* 241 */       if (!this.closeConnectionExecutor.awaitTermination(10L, TimeUnit.SECONDS)) {
/* 242 */         this.logger.warn("Timed-out waiting for close connection executor to shutdown");
/*     */       }
/*     */     } finally {
/*     */       
/* 246 */       logPoolState(new String[] { "After shutdown " });
/* 247 */       handleMBeans(this, false);
/* 248 */       this.metricsTracker.close();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void evictConnection(Connection connection) {
/* 259 */     ProxyConnection proxyConnection = (ProxyConnection)connection;
/* 260 */     proxyConnection.cancelLeakTask();
/*     */     
/*     */     try {
/* 263 */       softEvictConnection(proxyConnection.getPoolEntry(), "(connection evicted by user)", !connection.isClosed());
/*     */     }
/* 265 */     catch (SQLException sQLException) {}
/*     */   }
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
/*     */   public void setMetricRegistry(Object metricRegistry) {
/* 279 */     if (metricRegistry != null && UtilityElf.safeIsAssignableFrom(metricRegistry, "com.codahale.metrics.MetricRegistry")) {
/* 280 */       setMetricsTrackerFactory((MetricsTrackerFactory)new CodahaleMetricsTrackerFactory((MetricRegistry)metricRegistry));
/*     */     }
/* 282 */     else if (metricRegistry != null && UtilityElf.safeIsAssignableFrom(metricRegistry, "io.micrometer.core.instrument.MeterRegistry")) {
/* 283 */       setMetricsTrackerFactory((MetricsTrackerFactory)new MicrometerMetricsTrackerFactory((MeterRegistry)metricRegistry));
/*     */     } else {
/*     */       
/* 286 */       setMetricsTrackerFactory((MetricsTrackerFactory)null);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMetricsTrackerFactory(MetricsTrackerFactory metricsTrackerFactory) {
/* 297 */     if (metricsTrackerFactory != null) {
/* 298 */       this.metricsTracker = new PoolBase.MetricsTrackerDelegate(metricsTrackerFactory.create(this.config.getPoolName(), getPoolStats()));
/*     */     } else {
/*     */       
/* 301 */       this.metricsTracker = new PoolBase.NopMetricsTrackerDelegate();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setHealthCheckRegistry(Object healthCheckRegistry) {
/* 313 */     if (healthCheckRegistry != null) {
/* 314 */       CodahaleHealthChecker.registerHealthChecks(this, this.config, (HealthCheckRegistry)healthCheckRegistry);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addBagItem(int waiting) {
/* 326 */     if (waiting > this.addConnectionExecutor.getQueue().size()) {
/* 327 */       this.addConnectionExecutor.submit(this.poolEntryCreator);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getActiveConnections() {
/* 338 */     return this.connectionBag.getCount(1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getIdleConnections() {
/* 345 */     return this.connectionBag.getCount(0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getTotalConnections() {
/* 352 */     return this.connectionBag.size();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getThreadsAwaitingConnection() {
/* 359 */     return this.connectionBag.getWaitingThreadCount();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void softEvictConnections() {
/* 366 */     this.connectionBag.values().forEach(poolEntry -> softEvictConnection(poolEntry, "(connection evicted)", false));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void suspendPool() {
/* 373 */     if (this.suspendResumeLock == SuspendResumeLock.FAUX_LOCK) {
/* 374 */       throw new IllegalStateException(this.poolName + " - is not suspendable");
/*     */     }
/* 376 */     if (this.poolState != 1) {
/* 377 */       this.suspendResumeLock.suspend();
/* 378 */       this.poolState = 1;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void resumePool() {
/* 386 */     if (this.poolState == 1) {
/* 387 */       this.poolState = 0;
/* 388 */       fillPool(false);
/* 389 */       this.suspendResumeLock.resume();
/*     */     } 
/*     */   }
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
/*     */   void logPoolState(String... prefix) {
/* 404 */     if (this.logger.isDebugEnabled()) {
/* 405 */       this.logger.debug("{} - {}stats (total={}, active={}, idle={}, waiting={})", new Object[] { this.poolName, 
/* 406 */             (prefix.length > 0) ? prefix[0] : "", 
/* 407 */             Integer.valueOf(getTotalConnections()), Integer.valueOf(getActiveConnections()), Integer.valueOf(getIdleConnections()), Integer.valueOf(getThreadsAwaitingConnection()) });
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void recycle(PoolEntry poolEntry) {
/* 419 */     this.metricsTracker.recordConnectionUsage(poolEntry);
/*     */     
/* 421 */     this.connectionBag.requite(poolEntry);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void closeConnection(PoolEntry poolEntry, String closureReason) {
/* 432 */     if (this.connectionBag.remove(poolEntry)) {
/* 433 */       Connection connection = poolEntry.close();
/* 434 */       this.closeConnectionExecutor.execute(() -> {
/*     */             quietlyCloseConnection(connection, closureReason);
/*     */             if (this.poolState == 0) {
/*     */               fillPool(false);
/*     */             }
/*     */           });
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   int[] getPoolStateCounts() {
/* 446 */     return this.connectionBag.getStateCounts();
/*     */   }
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
/*     */   private PoolEntry createPoolEntry() {
/*     */     try {
/* 461 */       PoolEntry poolEntry = newPoolEntry();
/*     */       
/* 463 */       long maxLifetime = this.config.getMaxLifetime();
/* 464 */       if (maxLifetime > 0L) {
/*     */         
/* 466 */         long variance = (maxLifetime > 10000L) ? ThreadLocalRandom.current().nextLong(maxLifetime / 40L) : 0L;
/* 467 */         long lifetime = maxLifetime - variance;
/* 468 */         poolEntry.setFutureEol(this.houseKeepingExecutorService.schedule(new MaxLifetimeTask(poolEntry), lifetime, TimeUnit.MILLISECONDS));
/*     */       } 
/*     */       
/* 471 */       long keepaliveTime = this.config.getKeepaliveTime();
/* 472 */       if (keepaliveTime > 0L) {
/*     */         
/* 474 */         long variance = ThreadLocalRandom.current().nextLong(keepaliveTime / 10L);
/* 475 */         long heartbeatTime = keepaliveTime - variance;
/* 476 */         poolEntry.setKeepalive(this.houseKeepingExecutorService.scheduleWithFixedDelay(new KeepaliveTask(poolEntry), heartbeatTime, heartbeatTime, TimeUnit.MILLISECONDS));
/*     */       } 
/*     */       
/* 479 */       return poolEntry;
/*     */     }
/* 481 */     catch (ConnectionSetupException e) {
/* 482 */       if (this.poolState == 0) {
/* 483 */         this.logger.error("{} - Error thrown while acquiring connection from data source", this.poolName, e.getCause());
/* 484 */         this.lastConnectionFailure.set(e);
/*     */       }
/*     */     
/* 487 */     } catch (Exception e) {
/* 488 */       if (this.poolState == 0) {
/* 489 */         this.logger.debug("{} - Cannot acquire connection from data source", this.poolName, e);
/*     */       }
/*     */     } 
/*     */     
/* 493 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private synchronized void fillPool(boolean isAfterAdd) {
/* 501 */     int idle = getIdleConnections();
/* 502 */     boolean shouldAdd = (getTotalConnections() < this.config.getMaximumPoolSize() && idle < this.config.getMinimumIdle());
/*     */     
/* 504 */     if (shouldAdd) {
/* 505 */       int countToAdd = this.config.getMinimumIdle() - idle;
/* 506 */       for (int i = 0; i < countToAdd; i++) {
/* 507 */         this.addConnectionExecutor.submit(isAfterAdd ? this.postFillPoolEntryCreator : this.poolEntryCreator);
/*     */       }
/* 509 */     } else if (isAfterAdd) {
/* 510 */       this.logger.debug("{} - Fill pool skipped, pool has sufficient level or currently being filled.", this.poolName);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void abortActiveConnections(ExecutorService assassinExecutor) {
/* 521 */     for (PoolEntry poolEntry : this.connectionBag.values(1)) {
/* 522 */       Connection connection = poolEntry.close();
/*     */       try {
/* 524 */         connection.abort(assassinExecutor);
/*     */       }
/* 526 */       catch (Throwable e) {
/* 527 */         quietlyCloseConnection(connection, "(connection aborted during shutdown)");
/*     */       } finally {
/*     */         
/* 530 */         this.connectionBag.remove(poolEntry);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void checkFailFast() {
/* 543 */     long initializationTimeout = this.config.getInitializationFailTimeout();
/* 544 */     if (initializationTimeout < 0L) {
/*     */       return;
/*     */     }
/*     */     
/* 548 */     long startTime = ClockSource.currentTime();
/*     */     do {
/* 550 */       PoolEntry poolEntry = createPoolEntry();
/* 551 */       if (poolEntry != null) {
/* 552 */         if (this.config.getMinimumIdle() > 0) {
/* 553 */           this.connectionBag.add(poolEntry);
/* 554 */           this.logger.info("{} - Added connection {}", this.poolName, poolEntry.connection);
/*     */         } else {
/*     */           
/* 557 */           quietlyCloseConnection(poolEntry.close(), "(initialization check complete and minimumIdle is zero)");
/*     */         } 
/*     */         
/*     */         return;
/*     */       } 
/*     */       
/* 563 */       if (getLastConnectionFailure() instanceof PoolBase.ConnectionSetupException) {
/* 564 */         throwPoolInitializationException(getLastConnectionFailure().getCause());
/*     */       }
/*     */       
/* 567 */       UtilityElf.quietlySleep(TimeUnit.SECONDS.toMillis(1L));
/* 568 */     } while (ClockSource.elapsedMillis(startTime) < initializationTimeout);
/*     */     
/* 570 */     if (initializationTimeout > 0L) {
/* 571 */       throwPoolInitializationException(getLastConnectionFailure());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void throwPoolInitializationException(Throwable t) {
/* 583 */     destroyHouseKeepingExecutorService();
/* 584 */     throw new PoolInitializationException(t);
/*     */   }
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
/*     */   private boolean softEvictConnection(PoolEntry poolEntry, String reason, boolean owner) {
/* 602 */     poolEntry.markEvicted();
/* 603 */     if (owner || this.connectionBag.reserve(poolEntry)) {
/* 604 */       closeConnection(poolEntry, reason);
/* 605 */       return true;
/*     */     } 
/*     */     
/* 608 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ScheduledExecutorService initializeHouseKeepingExecutorService() {
/* 620 */     if (this.config.getScheduledExecutor() == null) {
/* 621 */       ThreadFactory threadFactory = Optional.<ThreadFactory>ofNullable(this.config.getThreadFactory()).orElseGet(() -> new UtilityElf.DefaultThreadFactory(this.poolName + " housekeeper"));
/* 622 */       ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1, threadFactory, new ThreadPoolExecutor.DiscardPolicy());
/* 623 */       executor.setExecuteExistingDelayedTasksAfterShutdownPolicy(false);
/* 624 */       executor.setRemoveOnCancelPolicy(true);
/* 625 */       return executor;
/*     */     } 
/*     */     
/* 628 */     return this.config.getScheduledExecutor();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void destroyHouseKeepingExecutorService() {
/* 637 */     if (this.config.getScheduledExecutor() == null) {
/* 638 */       this.houseKeepingExecutorService.shutdownNow();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private PoolStats getPoolStats() {
/* 649 */     return new PoolStats(TimeUnit.SECONDS.toMillis(1L))
/*     */       {
/*     */         protected void update() {
/* 652 */           this.pendingThreads = HikariPool.this.getThreadsAwaitingConnection();
/* 653 */           this.idleConnections = HikariPool.this.getIdleConnections();
/* 654 */           this.totalConnections = HikariPool.this.getTotalConnections();
/* 655 */           this.activeConnections = HikariPool.this.getActiveConnections();
/* 656 */           this.maxConnections = HikariPool.this.config.getMaximumPoolSize();
/* 657 */           this.minConnections = HikariPool.this.config.getMinimumIdle();
/*     */         }
/*     */       };
/*     */   }
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
/*     */   private SQLException createTimeoutException(long startTime) {
/* 676 */     logPoolState(new String[] { "Timeout failure " });
/* 677 */     this.metricsTracker.recordConnectionTimeout();
/*     */     
/* 679 */     String sqlState = null;
/* 680 */     Exception originalException = getLastConnectionFailure();
/* 681 */     if (originalException instanceof SQLException) {
/* 682 */       sqlState = ((SQLException)originalException).getSQLState();
/*     */     }
/*     */ 
/*     */     
/* 686 */     SQLTransientConnectionException connectionException = new SQLTransientConnectionException(this.poolName + " - Connection is not available, request timed out after " + this.poolName + "ms (total=" + ClockSource.elapsedMillis(startTime) + ", active=" + getTotalConnections() + ", idle=" + getActiveConnections() + ", waiting=" + getIdleConnections() + ")", sqlState, originalException);
/*     */     
/* 688 */     if (originalException instanceof SQLException) {
/* 689 */       connectionException.setNextException((SQLException)originalException);
/*     */     }
/*     */     
/* 692 */     return connectionException;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final class PoolEntryCreator
/*     */     implements Callable<Boolean>
/*     */   {
/*     */     private final String loggingPrefix;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     PoolEntryCreator() {
/* 709 */       this(null);
/*     */     }
/*     */ 
/*     */     
/*     */     PoolEntryCreator(String loggingPrefix) {
/* 714 */       this.loggingPrefix = loggingPrefix;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public Boolean call() {
/* 720 */       long backoffMs = 10L;
/* 721 */       boolean added = false;
/*     */       try {
/* 723 */         while (shouldContinueCreating()) {
/* 724 */           PoolEntry poolEntry = HikariPool.this.createPoolEntry();
/* 725 */           if (poolEntry != null) {
/* 726 */             added = true;
/* 727 */             HikariPool.this.connectionBag.add(poolEntry);
/* 728 */             HikariPool.this.logger.debug("{} - Added connection {}", HikariPool.this.poolName, poolEntry.connection);
/* 729 */             UtilityElf.quietlySleep(30L);
/*     */             break;
/*     */           } 
/* 732 */           if (this.loggingPrefix != null && backoffMs % 50L == 0L)
/* 733 */             HikariPool.this.logger.debug("{} - Connection add failed, sleeping with backoff: {}ms", HikariPool.this.poolName, Long.valueOf(backoffMs)); 
/* 734 */           UtilityElf.quietlySleep(backoffMs);
/* 735 */           backoffMs = Math.min(TimeUnit.SECONDS.toMillis(5L), backoffMs * 2L);
/*     */         }
/*     */       
/*     */       } finally {
/*     */         
/* 740 */         if (added && this.loggingPrefix != null) {
/* 741 */           HikariPool.this.logPoolState(new String[] { this.loggingPrefix });
/*     */         } else {
/* 743 */           HikariPool.this.logPoolState(new String[] { "Connection not added, " });
/*     */         } 
/*     */       } 
/*     */       
/* 747 */       return Boolean.FALSE;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private synchronized boolean shouldContinueCreating() {
/* 757 */       return (HikariPool.this.poolState == 0 && HikariPool.this.getTotalConnections() < HikariPool.this.config.getMaximumPoolSize() && (HikariPool.this
/* 758 */         .getIdleConnections() < HikariPool.this.config.getMinimumIdle() || HikariPool.this.connectionBag.getWaitingThreadCount() > HikariPool.this.getIdleConnections()));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private final class HouseKeeper
/*     */     implements Runnable
/*     */   {
/* 767 */     private volatile long previous = ClockSource.plusMillis(ClockSource.currentTime(), -HikariPool.this.housekeepingPeriodMs);
/*     */     
/* 769 */     private final AtomicReferenceFieldUpdater<PoolBase, String> catalogUpdater = AtomicReferenceFieldUpdater.newUpdater(PoolBase.class, String.class, "catalog");
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void run() {
/*     */       try {
/* 776 */         HikariPool.this.connectionTimeout = HikariPool.this.config.getConnectionTimeout();
/* 777 */         HikariPool.this.validationTimeout = HikariPool.this.config.getValidationTimeout();
/* 778 */         HikariPool.this.leakTaskFactory.updateLeakDetectionThreshold(HikariPool.this.config.getLeakDetectionThreshold());
/*     */         
/* 780 */         if (HikariPool.this.config.getCatalog() != null && !HikariPool.this.config.getCatalog().equals(HikariPool.this.catalog)) {
/* 781 */           this.catalogUpdater.set(HikariPool.this, HikariPool.this.config.getCatalog());
/*     */         }
/*     */         
/* 784 */         long idleTimeout = HikariPool.this.config.getIdleTimeout();
/* 785 */         long now = ClockSource.currentTime();
/*     */ 
/*     */         
/* 788 */         if (ClockSource.plusMillis(now, 128L) < ClockSource.plusMillis(this.previous, HikariPool.this.housekeepingPeriodMs)) {
/* 789 */           HikariPool.this.logger.warn("{} - Retrograde clock change detected (housekeeper delta={}), soft-evicting connections from pool.", HikariPool.this.poolName, 
/* 790 */               ClockSource.elapsedDisplayString(this.previous, now));
/* 791 */           this.previous = now;
/* 792 */           HikariPool.this.softEvictConnections();
/*     */           return;
/*     */         } 
/* 795 */         if (now > ClockSource.plusMillis(this.previous, 3L * HikariPool.this.housekeepingPeriodMs / 2L))
/*     */         {
/* 797 */           HikariPool.this.logger.warn("{} - Thread starvation or clock leap detected (housekeeper delta={}).", HikariPool.this.poolName, ClockSource.elapsedDisplayString(this.previous, now));
/*     */         }
/*     */         
/* 800 */         this.previous = now;
/*     */         
/* 802 */         if (idleTimeout > 0L && HikariPool.this.config.getMinimumIdle() < HikariPool.this.config.getMaximumPoolSize()) {
/* 803 */           HikariPool.this.logPoolState(new String[] { "Before cleanup " });
/* 804 */           List<PoolEntry> notInUse = HikariPool.this.connectionBag.values(0);
/* 805 */           int maxToRemove = notInUse.size() - HikariPool.this.config.getMinimumIdle();
/* 806 */           for (PoolEntry entry : notInUse) {
/* 807 */             if (maxToRemove > 0 && ClockSource.elapsedMillis(entry.lastAccessed, now) > idleTimeout && HikariPool.this.connectionBag.reserve(entry)) {
/* 808 */               HikariPool.this.closeConnection(entry, "(connection has passed idleTimeout)");
/* 809 */               maxToRemove--;
/*     */             } 
/*     */           } 
/* 812 */           HikariPool.this.logPoolState(new String[] { "After cleanup  " });
/*     */         } else {
/*     */           
/* 815 */           HikariPool.this.logPoolState(new String[] { "Pool " });
/*     */         } 
/* 817 */         HikariPool.this.fillPool(true);
/*     */       }
/* 819 */       catch (Exception e) {
/* 820 */         HikariPool.this.logger.error("Unexpected exception in housekeeping task", e);
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   private final class MaxLifetimeTask
/*     */     implements Runnable
/*     */   {
/*     */     private final PoolEntry poolEntry;
/*     */     
/*     */     MaxLifetimeTask(PoolEntry poolEntry) {
/* 831 */       this.poolEntry = poolEntry;
/*     */     }
/*     */ 
/*     */     
/*     */     public void run() {
/* 836 */       if (HikariPool.this.softEvictConnection(this.poolEntry, "(connection has passed maxLifetime)", false)) {
/* 837 */         HikariPool.this.addBagItem(HikariPool.this.connectionBag.getWaitingThreadCount());
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private final class KeepaliveTask
/*     */     implements Runnable
/*     */   {
/*     */     private final PoolEntry poolEntry;
/*     */     
/*     */     KeepaliveTask(PoolEntry poolEntry) {
/* 848 */       this.poolEntry = poolEntry;
/*     */     }
/*     */ 
/*     */     
/*     */     public void run() {
/* 853 */       if (HikariPool.this.connectionBag.reserve(this.poolEntry)) {
/* 854 */         if (HikariPool.this.isConnectionDead(this.poolEntry.connection)) {
/* 855 */           HikariPool.this.softEvictConnection(this.poolEntry, "(connection is dead)", true);
/* 856 */           HikariPool.this.addBagItem(HikariPool.this.connectionBag.getWaitingThreadCount());
/*     */         } else {
/*     */           
/* 859 */           HikariPool.this.connectionBag.unreserve(this.poolEntry);
/* 860 */           HikariPool.this.logger.debug("{} - keepalive: connection {} is alive", HikariPool.this.poolName, this.poolEntry.connection);
/*     */         } 
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static class PoolInitializationException
/*     */     extends RuntimeException
/*     */   {
/*     */     private static final long serialVersionUID = 929872118275916520L;
/*     */ 
/*     */ 
/*     */     
/*     */     public PoolInitializationException(Throwable t) {
/* 876 */       super("Failed to initialize pool: " + t.getMessage(), t);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\joeal\Downloads\Kits-2.0.9.jar!\dev\continuum\kits\libs\hikari\pool\HikariPool.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */