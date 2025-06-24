/*     */ package dev.continuum.kits.libs.hikari.pool;
/*     */ 
/*     */ import dev.continuum.kits.libs.hikari.HikariConfig;
/*     */ import dev.continuum.kits.libs.hikari.SQLExceptionOverride;
/*     */ import dev.continuum.kits.libs.hikari.metrics.IMetricsTracker;
/*     */ import dev.continuum.kits.libs.hikari.util.ClockSource;
/*     */ import dev.continuum.kits.libs.hikari.util.DriverDataSource;
/*     */ import dev.continuum.kits.libs.hikari.util.PropertyElf;
/*     */ import dev.continuum.kits.libs.hikari.util.UtilityElf;
/*     */ import java.lang.management.ManagementFactory;
/*     */ import java.sql.Connection;
/*     */ import java.sql.SQLException;
/*     */ import java.sql.SQLTransientConnectionException;
/*     */ import java.sql.Statement;
/*     */ import java.util.Properties;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.concurrent.Executors;
/*     */ import java.util.concurrent.ThreadFactory;
/*     */ import java.util.concurrent.ThreadPoolExecutor;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.atomic.AtomicReference;
/*     */ import javax.management.MBeanServer;
/*     */ import javax.management.ObjectName;
/*     */ import javax.naming.InitialContext;
/*     */ import javax.naming.NamingException;
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
/*     */ abstract class PoolBase
/*     */ {
/*  53 */   private final Logger logger = LoggerFactory.getLogger(PoolBase.class);
/*     */   
/*     */   public final HikariConfig config;
/*     */   
/*     */   IMetricsTrackerDelegate metricsTracker;
/*     */   
/*     */   protected final String poolName;
/*     */   
/*     */   volatile String catalog;
/*     */   
/*     */   final AtomicReference<Exception> lastConnectionFailure;
/*     */   
/*     */   long connectionTimeout;
/*     */   long validationTimeout;
/*     */   SQLExceptionOverride exceptionOverride;
/*  68 */   private static final String[] RESET_STATES = new String[] { "readOnly", "autoCommit", "isolation", "catalog", "netTimeout", "schema" };
/*     */   private static final int UNINITIALIZED = -1;
/*     */   private static final int TRUE = 1;
/*     */   private static final int FALSE = 0;
/*  72 */   private static final int MINIMUM_LOGIN_TIMEOUT = Integer.getInteger("dev.continuum.kits.libs.hikari.minimumLoginTimeoutSecs", 1).intValue();
/*     */   
/*     */   private int networkTimeout;
/*     */   
/*     */   private int isNetworkTimeoutSupported;
/*     */   
/*     */   private int isQueryTimeoutSupported;
/*     */   
/*     */   private int defaultTransactionIsolation;
/*     */   
/*     */   private int transactionIsolation;
/*     */   private Executor netTimeoutExecutor;
/*     */   private DataSource dataSource;
/*     */   private final String schema;
/*     */   private final boolean isReadOnly;
/*     */   private final boolean isAutoCommit;
/*     */   private final boolean isUseJdbc4Validation;
/*     */   private final boolean isIsolateInternalQueries;
/*     */   private volatile boolean isValidChecked;
/*     */   
/*     */   PoolBase(HikariConfig config) {
/*  93 */     this.config = config;
/*     */     
/*  95 */     this.networkTimeout = -1;
/*  96 */     this.catalog = config.getCatalog();
/*  97 */     this.schema = config.getSchema();
/*  98 */     this.isReadOnly = config.isReadOnly();
/*  99 */     this.isAutoCommit = config.isAutoCommit();
/* 100 */     this.exceptionOverride = (SQLExceptionOverride)UtilityElf.createInstance(config.getExceptionOverrideClassName(), SQLExceptionOverride.class, new Object[0]);
/* 101 */     this.transactionIsolation = UtilityElf.getTransactionIsolation(config.getTransactionIsolation());
/*     */     
/* 103 */     this.isQueryTimeoutSupported = -1;
/* 104 */     this.isNetworkTimeoutSupported = -1;
/* 105 */     this.isUseJdbc4Validation = (config.getConnectionTestQuery() == null);
/* 106 */     this.isIsolateInternalQueries = config.isIsolateInternalQueries();
/*     */     
/* 108 */     this.poolName = config.getPoolName();
/* 109 */     this.connectionTimeout = config.getConnectionTimeout();
/* 110 */     this.validationTimeout = config.getValidationTimeout();
/* 111 */     this.lastConnectionFailure = new AtomicReference<>();
/*     */     
/* 113 */     initializeDataSource();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 120 */     return this.poolName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   abstract void recycle(PoolEntry paramPoolEntry);
/*     */ 
/*     */ 
/*     */   
/*     */   void quietlyCloseConnection(Connection connection, String closureReason) {
/* 131 */     if (connection != null) {
/*     */       try {
/* 133 */         this.logger.debug("{} - Closing connection {}: {}", new Object[] { this.poolName, connection, closureReason });
/*     */ 
/*     */         
/* 136 */         try { Connection connection1 = connection; try { Connection connection2 = connection; 
/* 137 */             try { if (!connection.isClosed())
/* 138 */                 setNetworkTimeout(connection, TimeUnit.SECONDS.toMillis(15L)); 
/* 139 */               if (connection2 != null) connection2.close();  } catch (Throwable throwable) { if (connection2 != null) try { connection2.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  if (connection1 != null) connection1.close();  } catch (Throwable throwable) { if (connection1 != null) try { connection1.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (SQLException sQLException) {}
/*     */ 
/*     */       
/*     */       }
/* 143 */       catch (Exception e) {
/* 144 */         this.logger.debug("{} - Closing connection {} failed", new Object[] { this.poolName, connection, e });
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   boolean isConnectionDead(Connection connection) {
/*     */     try {
/* 152 */       setNetworkTimeout(connection, this.validationTimeout);
/*     */       
/* 154 */       try { int validationSeconds = (int)Math.max(1000L, this.validationTimeout) / 1000;
/*     */         
/* 156 */         if (this.isUseJdbc4Validation) {
/* 157 */           return !connection.isValid(validationSeconds);
/*     */         }
/*     */         
/* 160 */         Statement statement = connection.createStatement(); 
/* 161 */         try { if (this.isNetworkTimeoutSupported != 1) {
/* 162 */             setQueryTimeout(statement, validationSeconds);
/*     */           }
/*     */           
/* 165 */           statement.execute(this.config.getConnectionTestQuery());
/* 166 */           if (statement != null) statement.close();  } catch (Throwable throwable) { if (statement != null)
/*     */             try { statement.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }
/*     */          }
/* 169 */       finally { setNetworkTimeout(connection, this.networkTimeout);
/*     */         
/* 171 */         if (this.isIsolateInternalQueries && !this.isAutoCommit) {
/* 172 */           connection.rollback();
/*     */         } }
/*     */ 
/*     */       
/* 176 */       return false;
/*     */     }
/* 178 */     catch (Exception e) {
/* 179 */       this.lastConnectionFailure.set(e);
/* 180 */       this.logger.warn("{} - Failed to validate connection {} ({}). Possibly consider using a shorter maxLifetime value.", new Object[] { this.poolName, connection, e
/* 181 */             .getMessage() });
/* 182 */       return true;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   Exception getLastConnectionFailure() {
/* 188 */     return this.lastConnectionFailure.get();
/*     */   }
/*     */ 
/*     */   
/*     */   public DataSource getUnwrappedDataSource() {
/* 193 */     return this.dataSource;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   PoolEntry newPoolEntry() throws Exception {
/* 202 */     return new PoolEntry(newConnection(), this, this.isReadOnly, this.isAutoCommit);
/*     */   }
/*     */ 
/*     */   
/*     */   void resetConnectionState(Connection connection, ProxyConnection proxyConnection, int dirtyBits) throws SQLException {
/* 207 */     int resetBits = 0;
/*     */     
/* 209 */     if ((dirtyBits & 0x1) != 0 && proxyConnection.getReadOnlyState() != this.isReadOnly) {
/* 210 */       connection.setReadOnly(this.isReadOnly);
/* 211 */       resetBits |= 0x1;
/*     */     } 
/*     */     
/* 214 */     if ((dirtyBits & 0x2) != 0 && proxyConnection.getAutoCommitState() != this.isAutoCommit) {
/* 215 */       connection.setAutoCommit(this.isAutoCommit);
/* 216 */       resetBits |= 0x2;
/*     */     } 
/*     */     
/* 219 */     if ((dirtyBits & 0x4) != 0 && proxyConnection.getTransactionIsolationState() != this.transactionIsolation) {
/* 220 */       connection.setTransactionIsolation(this.transactionIsolation);
/* 221 */       resetBits |= 0x4;
/*     */     } 
/*     */     
/* 224 */     if ((dirtyBits & 0x8) != 0 && this.catalog != null && !this.catalog.equals(proxyConnection.getCatalogState())) {
/* 225 */       connection.setCatalog(this.catalog);
/* 226 */       resetBits |= 0x8;
/*     */     } 
/*     */     
/* 229 */     if ((dirtyBits & 0x10) != 0 && proxyConnection.getNetworkTimeoutState() != this.networkTimeout) {
/* 230 */       setNetworkTimeout(connection, this.networkTimeout);
/* 231 */       resetBits |= 0x10;
/*     */     } 
/*     */     
/* 234 */     if ((dirtyBits & 0x20) != 0 && this.schema != null && !this.schema.equals(proxyConnection.getSchemaState())) {
/* 235 */       connection.setSchema(this.schema);
/* 236 */       resetBits |= 0x20;
/*     */     } 
/*     */     
/* 239 */     if (resetBits != 0 && this.logger.isDebugEnabled()) {
/* 240 */       this.logger.debug("{} - Reset ({}) on connection {}", new Object[] { this.poolName, stringFromResetBits(resetBits), connection });
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   void shutdownNetworkTimeoutExecutor() {
/* 246 */     if (this.netTimeoutExecutor instanceof ThreadPoolExecutor) {
/* 247 */       ((ThreadPoolExecutor)this.netTimeoutExecutor).shutdownNow();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   long getLoginTimeout() {
/*     */     try {
/* 254 */       return (this.dataSource != null) ? this.dataSource.getLoginTimeout() : TimeUnit.SECONDS.toSeconds(5L);
/* 255 */     } catch (SQLException e) {
/* 256 */       return TimeUnit.SECONDS.toSeconds(5L);
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
/*     */   void handleMBeans(HikariPool hikariPool, boolean register) {
/* 271 */     if (!this.config.isRegisterMbeans()) {
/*     */       return;
/*     */     }
/*     */     try {
/*     */       ObjectName beanConfigName, beanPoolName;
/* 276 */       MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
/*     */ 
/*     */       
/* 279 */       if ("true".equals(System.getProperty("hikaricp.jmx.register2.0"))) {
/* 280 */         beanConfigName = new ObjectName("dev.continuum.kits.libs.hikari:type=PoolConfig,name=" + this.poolName);
/* 281 */         beanPoolName = new ObjectName("dev.continuum.kits.libs.hikari:type=Pool,name=" + this.poolName);
/*     */       } else {
/* 283 */         beanConfigName = new ObjectName("dev.continuum.kits.libs.hikari:type=PoolConfig (" + this.poolName + ")");
/* 284 */         beanPoolName = new ObjectName("dev.continuum.kits.libs.hikari:type=Pool (" + this.poolName + ")");
/*     */       } 
/* 286 */       if (register) {
/* 287 */         if (!mBeanServer.isRegistered(beanConfigName)) {
/* 288 */           mBeanServer.registerMBean(this.config, beanConfigName);
/* 289 */           mBeanServer.registerMBean(hikariPool, beanPoolName);
/*     */         } else {
/* 291 */           this.logger.error("{} - JMX name ({}) is already registered.", this.poolName, this.poolName);
/*     */         }
/*     */       
/* 294 */       } else if (mBeanServer.isRegistered(beanConfigName)) {
/* 295 */         mBeanServer.unregisterMBean(beanConfigName);
/* 296 */         mBeanServer.unregisterMBean(beanPoolName);
/*     */       }
/*     */     
/* 299 */     } catch (Exception e) {
/* 300 */       this.logger.warn("{} - Failed to {} management beans.", new Object[] { this.poolName, register ? "register" : "unregister", e });
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
/*     */   private void initializeDataSource() {
/* 313 */     String jdbcUrl = this.config.getJdbcUrl();
/* 314 */     String username = this.config.getUsername();
/* 315 */     String password = this.config.getPassword();
/* 316 */     String dsClassName = this.config.getDataSourceClassName();
/* 317 */     String driverClassName = this.config.getDriverClassName();
/* 318 */     String dataSourceJNDI = this.config.getDataSourceJNDI();
/* 319 */     Properties dataSourceProperties = this.config.getDataSourceProperties();
/*     */     
/* 321 */     DataSource dataSource = this.config.getDataSource();
/* 322 */     if (dsClassName != null && dataSource == null) {
/* 323 */       dataSource = (DataSource)UtilityElf.createInstance(dsClassName, DataSource.class, new Object[0]);
/* 324 */       PropertyElf.setTargetFromProperties(dataSource, dataSourceProperties);
/*     */     } else {
/* 326 */       DriverDataSource driverDataSource; if (jdbcUrl != null && dataSource == null) {
/* 327 */         driverDataSource = new DriverDataSource(jdbcUrl, driverClassName, dataSourceProperties, username, password);
/*     */       }
/* 329 */       else if (dataSourceJNDI != null && driverDataSource == null) {
/*     */         try {
/* 331 */           InitialContext ic = new InitialContext();
/* 332 */           dataSource = (DataSource)ic.lookup(dataSourceJNDI);
/* 333 */         } catch (NamingException e) {
/* 334 */           throw new HikariPool.PoolInitializationException(e);
/*     */         } 
/*     */       } 
/*     */     } 
/* 338 */     if (dataSource != null) {
/* 339 */       setLoginTimeout(dataSource);
/* 340 */       createNetworkTimeoutExecutor(dataSource, dsClassName, jdbcUrl);
/*     */     } 
/*     */     
/* 343 */     this.dataSource = dataSource;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Connection newConnection() throws Exception {
/* 353 */     long start = ClockSource.currentTime();
/*     */     
/* 355 */     Connection connection = null;
/*     */     try {
/* 357 */       String username = this.config.getUsername();
/* 358 */       String password = this.config.getPassword();
/*     */       
/* 360 */       connection = (username == null) ? this.dataSource.getConnection() : this.dataSource.getConnection(username, password);
/* 361 */       if (connection == null) {
/* 362 */         throw new SQLTransientConnectionException("DataSource returned null unexpectedly");
/*     */       }
/*     */       
/* 365 */       setupConnection(connection);
/* 366 */       this.lastConnectionFailure.set(null);
/* 367 */       return connection;
/*     */     }
/* 369 */     catch (Exception e) {
/* 370 */       if (connection != null) {
/* 371 */         quietlyCloseConnection(connection, "(Failed to create/setup connection)");
/*     */       }
/* 373 */       else if (getLastConnectionFailure() == null) {
/* 374 */         this.logger.debug("{} - Failed to create/setup connection: {}", this.poolName, e.getMessage());
/*     */       } 
/*     */       
/* 377 */       this.lastConnectionFailure.set(e);
/* 378 */       throw e;
/*     */     }
/*     */     finally {
/*     */       
/* 382 */       if (this.metricsTracker != null) {
/* 383 */         this.metricsTracker.recordConnectionCreated(ClockSource.elapsedMillis(start));
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
/*     */   private void setupConnection(Connection connection) throws ConnectionSetupException {
/*     */     try {
/* 397 */       if (this.networkTimeout == -1) {
/* 398 */         this.networkTimeout = getAndSetNetworkTimeout(connection, this.validationTimeout);
/*     */       } else {
/*     */         
/* 401 */         setNetworkTimeout(connection, this.validationTimeout);
/*     */       } 
/*     */       
/* 404 */       if (connection.isReadOnly() != this.isReadOnly) {
/* 405 */         connection.setReadOnly(this.isReadOnly);
/*     */       }
/*     */       
/* 408 */       if (connection.getAutoCommit() != this.isAutoCommit) {
/* 409 */         connection.setAutoCommit(this.isAutoCommit);
/*     */       }
/*     */       
/* 412 */       checkDriverSupport(connection);
/*     */       
/* 414 */       if (this.transactionIsolation != this.defaultTransactionIsolation) {
/* 415 */         connection.setTransactionIsolation(this.transactionIsolation);
/*     */       }
/*     */       
/* 418 */       if (this.catalog != null) {
/* 419 */         connection.setCatalog(this.catalog);
/*     */       }
/*     */       
/* 422 */       if (this.schema != null) {
/* 423 */         connection.setSchema(this.schema);
/*     */       }
/*     */       
/* 426 */       executeSql(connection, this.config.getConnectionInitSql(), true);
/*     */       
/* 428 */       setNetworkTimeout(connection, this.networkTimeout);
/*     */     }
/* 430 */     catch (SQLException e) {
/* 431 */       throw new ConnectionSetupException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void checkDriverSupport(Connection connection) throws SQLException {
/* 442 */     if (!this.isValidChecked) {
/* 443 */       checkValidationSupport(connection);
/* 444 */       checkDefaultIsolation(connection);
/*     */       
/* 446 */       this.isValidChecked = true;
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
/*     */   private void checkValidationSupport(Connection connection) throws SQLException {
/*     */     try {
/* 459 */       if (this.isUseJdbc4Validation) {
/* 460 */         connection.isValid(1);
/*     */       } else {
/*     */         
/* 463 */         executeSql(connection, this.config.getConnectionTestQuery(), false);
/*     */       }
/*     */     
/* 466 */     } catch (Exception|AbstractMethodError e) {
/* 467 */       this.logger.error("{} - Failed to execute{} connection test query ({}).", new Object[] { this.poolName, this.isUseJdbc4Validation ? " isValid() for connection, configure" : "", e.getMessage() });
/* 468 */       throw e;
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
/*     */   private void checkDefaultIsolation(Connection connection) throws SQLException {
/*     */     try {
/* 481 */       this.defaultTransactionIsolation = connection.getTransactionIsolation();
/* 482 */       if (this.transactionIsolation == -1) {
/* 483 */         this.transactionIsolation = this.defaultTransactionIsolation;
/*     */       }
/*     */     }
/* 486 */     catch (SQLException e) {
/* 487 */       this.logger.warn("{} - Default transaction isolation level detection failed ({}).", this.poolName, e.getMessage());
/* 488 */       if (e.getSQLState() != null && !e.getSQLState().startsWith("08")) {
/* 489 */         throw e;
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
/*     */   private void setQueryTimeout(Statement statement, int timeoutSec) {
/* 502 */     if (this.isQueryTimeoutSupported != 0) {
/*     */       try {
/* 504 */         statement.setQueryTimeout(timeoutSec);
/* 505 */         this.isQueryTimeoutSupported = 1;
/*     */       }
/* 507 */       catch (Exception e) {
/* 508 */         if (this.isQueryTimeoutSupported == -1) {
/* 509 */           this.isQueryTimeoutSupported = 0;
/* 510 */           this.logger.info("{} - Failed to set query timeout for statement. ({})", this.poolName, e.getMessage());
/*     */         } 
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
/*     */ 
/*     */   
/*     */   private int getAndSetNetworkTimeout(Connection connection, long timeoutMs) {
/* 526 */     if (this.isNetworkTimeoutSupported != 0) {
/*     */       try {
/* 528 */         int originalTimeout = connection.getNetworkTimeout();
/* 529 */         connection.setNetworkTimeout(this.netTimeoutExecutor, (int)timeoutMs);
/* 530 */         this.isNetworkTimeoutSupported = 1;
/* 531 */         return originalTimeout;
/*     */       }
/* 533 */       catch (Exception|AbstractMethodError e) {
/* 534 */         if (this.isNetworkTimeoutSupported == -1) {
/* 535 */           this.isNetworkTimeoutSupported = 0;
/*     */           
/* 537 */           this.logger.info("{} - Driver does not support get/set network timeout for connections. ({})", this.poolName, e.getMessage());
/* 538 */           if (this.validationTimeout < TimeUnit.SECONDS.toMillis(1L)) {
/* 539 */             this.logger.warn("{} - A validationTimeout of less than 1 second cannot be honored on drivers without setNetworkTimeout() support.", this.poolName);
/*     */           }
/* 541 */           else if (this.validationTimeout % TimeUnit.SECONDS.toMillis(1L) != 0L) {
/* 542 */             this.logger.warn("{} - A validationTimeout with fractional second granularity cannot be honored on drivers without setNetworkTimeout() support.", this.poolName);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/* 548 */     return 0;
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
/*     */   private void setNetworkTimeout(Connection connection, long timeoutMs) throws SQLException {
/* 561 */     if (this.isNetworkTimeoutSupported == 1) {
/* 562 */       connection.setNetworkTimeout(this.netTimeoutExecutor, (int)timeoutMs);
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
/*     */   private void executeSql(Connection connection, String sql, boolean isCommit) throws SQLException {
/* 576 */     if (sql != null) {
/* 577 */       Statement statement = connection.createStatement();
/*     */       
/* 579 */       try { statement.execute(sql);
/* 580 */         if (statement != null) statement.close();  } catch (Throwable throwable) { if (statement != null)
/*     */           try { statement.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }
/* 582 */        if (this.isIsolateInternalQueries && !this.isAutoCommit) {
/* 583 */         if (isCommit) {
/* 584 */           connection.commit();
/*     */         } else {
/*     */           
/* 587 */           connection.rollback();
/*     */         } 
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void createNetworkTimeoutExecutor(DataSource dataSource, String dsClassName, String jdbcUrl) {
/* 596 */     if ((dsClassName != null && dsClassName.contains("Mysql")) || (jdbcUrl != null && jdbcUrl
/* 597 */       .contains("mysql")) || (dataSource != null && dataSource
/* 598 */       .getClass().getName().contains("Mysql"))) {
/* 599 */       this.netTimeoutExecutor = new SynchronousExecutor();
/*     */     } else {
/*     */       
/* 602 */       ThreadFactory threadFactory = this.config.getThreadFactory();
/* 603 */       threadFactory = (threadFactory != null) ? threadFactory : (ThreadFactory)new UtilityElf.DefaultThreadFactory(this.poolName + " network timeout executor");
/* 604 */       ThreadPoolExecutor executor = (ThreadPoolExecutor)Executors.newCachedThreadPool(threadFactory);
/* 605 */       executor.setKeepAliveTime(15L, TimeUnit.SECONDS);
/* 606 */       executor.allowCoreThreadTimeOut(true);
/* 607 */       this.netTimeoutExecutor = executor;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void setLoginTimeout(DataSource dataSource) {
/* 618 */     if (this.connectionTimeout != 2147483647L) {
/*     */       try {
/* 620 */         dataSource.setLoginTimeout(Math.max(MINIMUM_LOGIN_TIMEOUT, (int)TimeUnit.MILLISECONDS.toSeconds(500L + this.connectionTimeout)));
/*     */       }
/* 622 */       catch (Exception e) {
/* 623 */         this.logger.info("{} - Failed to set login timeout for data source. ({})", this.poolName, e.getMessage());
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String stringFromResetBits(int bits) {
/* 640 */     StringBuilder sb = new StringBuilder();
/* 641 */     for (int ndx = 0; ndx < RESET_STATES.length; ndx++) {
/* 642 */       if ((bits & 1 << ndx) != 0) {
/* 643 */         sb.append(RESET_STATES[ndx]).append(", ");
/*     */       }
/*     */     } 
/*     */     
/* 647 */     sb.setLength(sb.length() - 2);
/* 648 */     return sb.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static class ConnectionSetupException
/*     */     extends Exception
/*     */   {
/*     */     private static final long serialVersionUID = 929872118275916521L;
/*     */ 
/*     */ 
/*     */     
/*     */     ConnectionSetupException(Throwable t) {
/* 661 */       super(t);
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
/*     */   private static class SynchronousExecutor
/*     */     implements Executor
/*     */   {
/*     */     public void execute(Runnable command) {
/*     */       try {
/* 677 */         command.run();
/*     */       }
/* 679 */       catch (Exception t) {
/* 680 */         LoggerFactory.getLogger(PoolBase.class).debug("Failed to execute: {}", command, t);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   static interface IMetricsTrackerDelegate
/*     */     extends AutoCloseable
/*     */   {
/*     */     default void recordConnectionUsage(PoolEntry poolEntry) {}
/*     */ 
/*     */     
/*     */     default void recordConnectionCreated(long connectionCreatedMillis) {}
/*     */ 
/*     */     
/*     */     default void recordBorrowTimeoutStats(long startTime) {}
/*     */ 
/*     */     
/*     */     default void recordBorrowStats(PoolEntry poolEntry, long startTime) {}
/*     */ 
/*     */     
/*     */     default void recordConnectionTimeout() {}
/*     */     
/*     */     default void close() {}
/*     */   }
/*     */   
/*     */   static class MetricsTrackerDelegate
/*     */     implements IMetricsTrackerDelegate
/*     */   {
/*     */     final IMetricsTracker tracker;
/*     */     
/*     */     MetricsTrackerDelegate(IMetricsTracker tracker) {
/* 712 */       this.tracker = tracker;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void recordConnectionUsage(PoolEntry poolEntry) {
/* 718 */       this.tracker.recordConnectionUsageMillis(poolEntry.getMillisSinceBorrowed());
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void recordConnectionCreated(long connectionCreatedMillis) {
/* 724 */       this.tracker.recordConnectionCreatedMillis(connectionCreatedMillis);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void recordBorrowTimeoutStats(long startTime) {
/* 730 */       this.tracker.recordConnectionAcquiredNanos(ClockSource.elapsedNanos(startTime));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void recordBorrowStats(PoolEntry poolEntry, long startTime) {
/* 736 */       long now = ClockSource.currentTime();
/* 737 */       poolEntry.lastBorrowed = now;
/* 738 */       this.tracker.recordConnectionAcquiredNanos(ClockSource.elapsedNanos(startTime, now));
/*     */     }
/*     */ 
/*     */     
/*     */     public void recordConnectionTimeout() {
/* 743 */       this.tracker.recordConnectionTimeout();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void close() {
/* 749 */       this.tracker.close();
/*     */     }
/*     */   }
/*     */   
/*     */   static final class NopMetricsTrackerDelegate implements IMetricsTrackerDelegate {}
/*     */ }


/* Location:              C:\Users\joeal\Downloads\Kits-2.0.9.jar!\dev\continuum\kits\libs\hikari\pool\PoolBase.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */