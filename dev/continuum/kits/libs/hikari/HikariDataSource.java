/*     */ package dev.continuum.kits.libs.hikari;
/*     */ 
/*     */ import dev.continuum.kits.libs.hikari.metrics.MetricsTrackerFactory;
/*     */ import dev.continuum.kits.libs.hikari.pool.HikariPool;
/*     */ import java.io.Closeable;
/*     */ import java.io.PrintWriter;
/*     */ import java.sql.Connection;
/*     */ import java.sql.SQLException;
/*     */ import java.sql.SQLFeatureNotSupportedException;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
/*     */ import java.util.logging.Logger;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class HikariDataSource
/*     */   extends HikariConfig
/*     */   implements DataSource, Closeable
/*     */ {
/*  42 */   private static final Logger LOGGER = LoggerFactory.getLogger(HikariDataSource.class);
/*     */   
/*  44 */   private final AtomicBoolean isShutdown = new AtomicBoolean();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final HikariPool fastPathPool;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private volatile HikariPool pool;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HikariDataSource() {
/*  61 */     this.fastPathPool = null;
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
/*     */   public HikariDataSource(HikariConfig configuration) {
/*  76 */     configuration.validate();
/*  77 */     configuration.copyStateTo(this);
/*     */     
/*  79 */     LOGGER.info("{} - Starting...", configuration.getPoolName());
/*  80 */     this.pool = this.fastPathPool = new HikariPool(this);
/*  81 */     LOGGER.info("{} - Start completed.", configuration.getPoolName());
/*     */     
/*  83 */     seal();
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
/*  94 */     if (isClosed()) {
/*  95 */       throw new SQLException("HikariDataSource " + this + " has been closed.");
/*     */     }
/*     */     
/*  98 */     if (this.fastPathPool != null) {
/*  99 */       return this.fastPathPool.getConnection();
/*     */     }
/*     */ 
/*     */     
/* 103 */     HikariPool result = this.pool;
/* 104 */     if (result == null) {
/* 105 */       synchronized (this) {
/* 106 */         result = this.pool;
/* 107 */         if (result == null) {
/* 108 */           validate();
/* 109 */           LOGGER.info("{} - Starting...", getPoolName());
/*     */           try {
/* 111 */             this.pool = result = new HikariPool(this);
/* 112 */             seal();
/*     */           }
/* 114 */           catch (dev.continuum.kits.libs.hikari.pool.HikariPool.PoolInitializationException pie) {
/* 115 */             if (pie.getCause() instanceof SQLException) {
/* 116 */               throw (SQLException)pie.getCause();
/*     */             }
/*     */             
/* 119 */             throw pie;
/*     */           } 
/*     */           
/* 122 */           LOGGER.info("{} - Start completed.", getPoolName());
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/* 127 */     return result.getConnection();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Connection getConnection(String username, String password) throws SQLException {
/* 134 */     throw new SQLFeatureNotSupportedException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PrintWriter getLogWriter() throws SQLException {
/* 141 */     HikariPool p = this.pool;
/* 142 */     return (p != null) ? p.getUnwrappedDataSource().getLogWriter() : null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLogWriter(PrintWriter out) throws SQLException {
/* 149 */     HikariPool p = this.pool;
/* 150 */     if (p != null) {
/* 151 */       p.getUnwrappedDataSource().setLogWriter(out);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLoginTimeout(int seconds) throws SQLException {
/* 159 */     HikariPool p = this.pool;
/* 160 */     if (p != null) {
/* 161 */       p.getUnwrappedDataSource().setLoginTimeout(seconds);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getLoginTimeout() throws SQLException {
/* 169 */     HikariPool p = this.pool;
/* 170 */     return (p != null) ? p.getUnwrappedDataSource().getLoginTimeout() : 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Logger getParentLogger() throws SQLFeatureNotSupportedException {
/* 177 */     throw new SQLFeatureNotSupportedException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> T unwrap(Class<T> iface) throws SQLException {
/* 185 */     if (iface.isInstance(this)) {
/* 186 */       return (T)this;
/*     */     }
/*     */     
/* 189 */     HikariPool p = this.pool;
/* 190 */     if (p != null) {
/* 191 */       DataSource unwrappedDataSource = p.getUnwrappedDataSource();
/* 192 */       if (iface.isInstance(unwrappedDataSource)) {
/* 193 */         return (T)unwrappedDataSource;
/*     */       }
/*     */       
/* 196 */       if (unwrappedDataSource != null) {
/* 197 */         return unwrappedDataSource.unwrap(iface);
/*     */       }
/*     */     } 
/*     */     
/* 201 */     throw new SQLException("Wrapped DataSource is not an instance of " + iface);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isWrapperFor(Class<?> iface) throws SQLException {
/* 208 */     if (iface.isInstance(this)) {
/* 209 */       return true;
/*     */     }
/*     */     
/* 212 */     HikariPool p = this.pool;
/* 213 */     if (p != null) {
/* 214 */       DataSource unwrappedDataSource = p.getUnwrappedDataSource();
/* 215 */       if (iface.isInstance(unwrappedDataSource)) {
/* 216 */         return true;
/*     */       }
/*     */       
/* 219 */       if (unwrappedDataSource != null) {
/* 220 */         return unwrappedDataSource.isWrapperFor(iface);
/*     */       }
/*     */     } 
/*     */     
/* 224 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMetricRegistry(Object metricRegistry) {
/* 235 */     boolean isAlreadySet = (getMetricRegistry() != null);
/* 236 */     super.setMetricRegistry(metricRegistry);
/*     */     
/* 238 */     HikariPool p = this.pool;
/* 239 */     if (p != null) {
/* 240 */       if (isAlreadySet) {
/* 241 */         throw new IllegalStateException("MetricRegistry can only be set one time");
/*     */       }
/*     */       
/* 244 */       p.setMetricRegistry(getMetricRegistry());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMetricsTrackerFactory(MetricsTrackerFactory metricsTrackerFactory) {
/* 253 */     boolean isAlreadySet = (getMetricsTrackerFactory() != null);
/* 254 */     super.setMetricsTrackerFactory(metricsTrackerFactory);
/*     */     
/* 256 */     HikariPool p = this.pool;
/* 257 */     if (p != null) {
/* 258 */       if (isAlreadySet) {
/* 259 */         throw new IllegalStateException("MetricsTrackerFactory can only be set one time");
/*     */       }
/*     */       
/* 262 */       p.setMetricsTrackerFactory(getMetricsTrackerFactory());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setHealthCheckRegistry(Object healthCheckRegistry) {
/* 271 */     boolean isAlreadySet = (getHealthCheckRegistry() != null);
/* 272 */     super.setHealthCheckRegistry(healthCheckRegistry);
/*     */     
/* 274 */     HikariPool p = this.pool;
/* 275 */     if (p != null) {
/* 276 */       if (isAlreadySet) {
/* 277 */         throw new IllegalStateException("HealthCheckRegistry can only be set one time");
/*     */       }
/*     */       
/* 280 */       p.setHealthCheckRegistry(getHealthCheckRegistry());
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
/*     */   
/*     */   public boolean isRunning() {
/* 296 */     return (this.pool != null && this.pool.poolState == 0);
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
/*     */   public HikariPoolMXBean getHikariPoolMXBean() {
/* 308 */     return (HikariPoolMXBean)this.pool;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HikariConfigMXBean getHikariConfigMXBean() {
/* 318 */     return this;
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
/*     */   public void evictConnection(Connection connection) {
/*     */     HikariPool p;
/* 331 */     if (!isClosed() && (p = this.pool) != null && connection.getClass().getName().startsWith("dev.continuum.kits.libs.hikari")) {
/* 332 */       p.evictConnection(connection);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() {
/* 342 */     if (this.isShutdown.getAndSet(true)) {
/*     */       return;
/*     */     }
/*     */     
/* 346 */     HikariPool p = this.pool;
/* 347 */     if (p != null) {
/*     */       try {
/* 349 */         LOGGER.info("{} - Shutdown initiated...", getPoolName());
/* 350 */         p.shutdown();
/* 351 */         LOGGER.info("{} - Shutdown completed.", getPoolName());
/*     */       }
/* 353 */       catch (InterruptedException e) {
/* 354 */         LOGGER.warn("{} - Interrupted during closing", getPoolName(), e);
/* 355 */         Thread.currentThread().interrupt();
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
/*     */   public boolean isClosed() {
/* 367 */     return this.isShutdown.get();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 374 */     return "HikariDataSource (" + this.pool + ")";
/*     */   }
/*     */ }


/* Location:              C:\Users\joeal\Downloads\Kits-2.0.9.jar!\dev\continuum\kits\libs\hikari\HikariDataSource.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */