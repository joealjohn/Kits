/*      */ package dev.continuum.kits.libs.hikari;
/*      */ 
/*      */ import dev.continuum.kits.libs.hikari.metrics.MetricsTrackerFactory;
/*      */ import dev.continuum.kits.libs.hikari.util.PropertyElf;
/*      */ import dev.continuum.kits.libs.hikari.util.UtilityElf;
/*      */ import java.io.File;
/*      */ import java.io.FileInputStream;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.lang.reflect.Field;
/*      */ import java.lang.reflect.Modifier;
/*      */ import java.security.AccessControlException;
/*      */ import java.util.Properties;
/*      */ import java.util.TreeSet;
/*      */ import java.util.concurrent.ScheduledExecutorService;
/*      */ import java.util.concurrent.ThreadFactory;
/*      */ import java.util.concurrent.ThreadLocalRandom;
/*      */ import java.util.concurrent.TimeUnit;
/*      */ import javax.naming.InitialContext;
/*      */ import javax.naming.NamingException;
/*      */ import javax.sql.DataSource;
/*      */ import org.slf4j.Logger;
/*      */ import org.slf4j.LoggerFactory;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class HikariConfig
/*      */   implements HikariConfigMXBean
/*      */ {
/*   48 */   private static final Logger LOGGER = LoggerFactory.getLogger(HikariConfig.class);
/*      */   
/*   50 */   private static final char[] ID_CHARACTERS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
/*   51 */   private static final long CONNECTION_TIMEOUT = TimeUnit.SECONDS.toMillis(30L);
/*   52 */   private static final long VALIDATION_TIMEOUT = TimeUnit.SECONDS.toMillis(5L);
/*   53 */   private static final long SOFT_TIMEOUT_FLOOR = Long.getLong("dev.continuum.kits.libs.hikari.timeoutMs.floor", 250L).longValue();
/*   54 */   private static final long IDLE_TIMEOUT = TimeUnit.MINUTES.toMillis(10L);
/*   55 */   private static final long MAX_LIFETIME = TimeUnit.MINUTES.toMillis(30L);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static boolean unitTest = false;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  116 */   private Properties dataSourceProperties = new Properties();
/*  117 */   private Properties healthCheckProperties = new Properties();
/*      */   
/*  119 */   private volatile int minIdle = -1;
/*  120 */   private volatile int maxPoolSize = -1;
/*  121 */   private volatile long maxLifetime = MAX_LIFETIME;
/*  122 */   private volatile long connectionTimeout = CONNECTION_TIMEOUT;
/*  123 */   private volatile long validationTimeout = VALIDATION_TIMEOUT;
/*  124 */   private volatile long idleTimeout = IDLE_TIMEOUT;
/*  125 */   private long initializationFailTimeout = 1L; private boolean isAutoCommit = true; private static final long DEFAULT_KEEPALIVE_TIME = 0L; private static final int DEFAULT_POOL_SIZE = 10; private volatile String catalog; private volatile long leakDetectionThreshold; private volatile String username; private volatile String password;
/*      */   private String connectionInitSql;
/*  127 */   private long keepaliveTime = 0L; private String connectionTestQuery; private String dataSourceClassName; private String dataSourceJndiName; private String driverClassName; private String exceptionOverrideClassName; private String jdbcUrl;
/*      */   public HikariConfig() {
/*  129 */     String systemProp = System.getProperty("hikaricp.configurationFile");
/*  130 */     if (systemProp != null)
/*  131 */       loadProperties(systemProp); 
/*      */   }
/*      */   private String poolName; private String schema; private String transactionIsolationName; private boolean isReadOnly; private boolean isIsolateInternalQueries; private boolean isRegisterMbeans; private boolean isAllowPoolSuspension; private DataSource dataSource;
/*      */   private ThreadFactory threadFactory;
/*      */   private ScheduledExecutorService scheduledExecutor;
/*      */   private MetricsTrackerFactory metricsTrackerFactory;
/*      */   private Object metricRegistry;
/*      */   private Object healthCheckRegistry;
/*      */   private volatile boolean sealed;
/*      */   
/*      */   public HikariConfig(Properties properties) {
/*  142 */     this();
/*  143 */     PropertyElf.setTargetFromProperties(this, properties);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public HikariConfig(String propertyFileName) {
/*  155 */     this();
/*      */     
/*  157 */     loadProperties(propertyFileName);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getCatalog() {
/*  168 */     return this.catalog;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setCatalog(String catalog) {
/*  175 */     this.catalog = catalog;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long getConnectionTimeout() {
/*  183 */     return this.connectionTimeout;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setConnectionTimeout(long connectionTimeoutMs) {
/*  190 */     if (connectionTimeoutMs == 0L) {
/*  191 */       this.connectionTimeout = 2147483647L;
/*      */     } else {
/*  193 */       if (connectionTimeoutMs < SOFT_TIMEOUT_FLOOR) {
/*  194 */         throw new IllegalArgumentException("connectionTimeout cannot be less than " + SOFT_TIMEOUT_FLOOR + "ms");
/*      */       }
/*      */       
/*  197 */       this.connectionTimeout = connectionTimeoutMs;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long getIdleTimeout() {
/*  205 */     return this.idleTimeout;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setIdleTimeout(long idleTimeoutMs) {
/*  212 */     if (idleTimeoutMs < 0L) {
/*  213 */       throw new IllegalArgumentException("idleTimeout cannot be negative");
/*      */     }
/*  215 */     this.idleTimeout = idleTimeoutMs;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long getLeakDetectionThreshold() {
/*  222 */     return this.leakDetectionThreshold;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setLeakDetectionThreshold(long leakDetectionThresholdMs) {
/*  229 */     this.leakDetectionThreshold = leakDetectionThresholdMs;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long getMaxLifetime() {
/*  236 */     return this.maxLifetime;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setMaxLifetime(long maxLifetimeMs) {
/*  243 */     this.maxLifetime = maxLifetimeMs;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getMaximumPoolSize() {
/*  250 */     return this.maxPoolSize;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setMaximumPoolSize(int maxPoolSize) {
/*  257 */     if (maxPoolSize < 1) {
/*  258 */       throw new IllegalArgumentException("maxPoolSize cannot be less than 1");
/*      */     }
/*  260 */     this.maxPoolSize = maxPoolSize;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getMinimumIdle() {
/*  267 */     return this.minIdle;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setMinimumIdle(int minIdle) {
/*  274 */     if (minIdle < 0) {
/*  275 */       throw new IllegalArgumentException("minimumIdle cannot be negative");
/*      */     }
/*  277 */     this.minIdle = minIdle;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getPassword() {
/*  286 */     return this.password;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setPassword(String password) {
/*  296 */     this.password = password;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getUsername() {
/*  306 */     return this.username;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setUsername(String username) {
/*  317 */     this.username = username;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long getValidationTimeout() {
/*  324 */     return this.validationTimeout;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setValidationTimeout(long validationTimeoutMs) {
/*  331 */     if (validationTimeoutMs < SOFT_TIMEOUT_FLOOR) {
/*  332 */       throw new IllegalArgumentException("validationTimeout cannot be less than " + SOFT_TIMEOUT_FLOOR + "ms");
/*      */     }
/*      */     
/*  335 */     this.validationTimeout = validationTimeoutMs;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getConnectionTestQuery() {
/*  349 */     return this.connectionTestQuery;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setConnectionTestQuery(String connectionTestQuery) {
/*  361 */     checkIfSealed();
/*  362 */     this.connectionTestQuery = connectionTestQuery;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getConnectionInitSql() {
/*  373 */     return this.connectionInitSql;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setConnectionInitSql(String connectionInitSql) {
/*  385 */     checkIfSealed();
/*  386 */     this.connectionInitSql = connectionInitSql;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public DataSource getDataSource() {
/*  397 */     return this.dataSource;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setDataSource(DataSource dataSource) {
/*  408 */     checkIfSealed();
/*  409 */     this.dataSource = dataSource;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getDataSourceClassName() {
/*  419 */     return this.dataSourceClassName;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setDataSourceClassName(String className) {
/*  429 */     checkIfSealed();
/*  430 */     this.dataSourceClassName = className;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addDataSourceProperty(String propertyName, Object value) {
/*  448 */     checkIfSealed();
/*  449 */     this.dataSourceProperties.put(propertyName, value);
/*      */   }
/*      */ 
/*      */   
/*      */   public String getDataSourceJNDI() {
/*  454 */     return this.dataSourceJndiName;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setDataSourceJNDI(String jndiDataSource) {
/*  459 */     checkIfSealed();
/*  460 */     this.dataSourceJndiName = jndiDataSource;
/*      */   }
/*      */ 
/*      */   
/*      */   public Properties getDataSourceProperties() {
/*  465 */     return this.dataSourceProperties;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setDataSourceProperties(Properties dsProperties) {
/*  470 */     checkIfSealed();
/*  471 */     this.dataSourceProperties.putAll(dsProperties);
/*      */   }
/*      */ 
/*      */   
/*      */   public String getDriverClassName() {
/*  476 */     return this.driverClassName;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setDriverClassName(String driverClassName) {
/*  481 */     checkIfSealed();
/*      */     
/*  483 */     Class<?> driverClass = attemptFromContextLoader(driverClassName);
/*      */     try {
/*  485 */       if (driverClass == null) {
/*  486 */         driverClass = getClass().getClassLoader().loadClass(driverClassName);
/*  487 */         LOGGER.debug("Driver class {} found in the HikariConfig class classloader {}", driverClassName, getClass().getClassLoader());
/*      */       } 
/*  489 */     } catch (ClassNotFoundException e) {
/*  490 */       LOGGER.error("Failed to load driver class {} from HikariConfig class classloader {}", driverClassName, getClass().getClassLoader());
/*      */     } 
/*      */     
/*  493 */     if (driverClass == null) {
/*  494 */       throw new RuntimeException("Failed to load driver class " + driverClassName + " in either of HikariConfig class loader or Thread context classloader");
/*      */     }
/*      */     
/*      */     try {
/*  498 */       driverClass.getConstructor(new Class[0]).newInstance(new Object[0]);
/*  499 */       this.driverClassName = driverClassName;
/*      */     }
/*  501 */     catch (Exception e) {
/*  502 */       throw new RuntimeException("Failed to instantiate class " + driverClassName, e);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public String getJdbcUrl() {
/*  508 */     return this.jdbcUrl;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setJdbcUrl(String jdbcUrl) {
/*  513 */     checkIfSealed();
/*  514 */     this.jdbcUrl = jdbcUrl;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isAutoCommit() {
/*  524 */     return this.isAutoCommit;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setAutoCommit(boolean isAutoCommit) {
/*  534 */     checkIfSealed();
/*  535 */     this.isAutoCommit = isAutoCommit;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isAllowPoolSuspension() {
/*  545 */     return this.isAllowPoolSuspension;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setAllowPoolSuspension(boolean isAllowPoolSuspension) {
/*  557 */     checkIfSealed();
/*  558 */     this.isAllowPoolSuspension = isAllowPoolSuspension;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long getInitializationFailTimeout() {
/*  570 */     return this.initializationFailTimeout;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setInitializationFailTimeout(long initializationFailTimeout) {
/*  608 */     checkIfSealed();
/*  609 */     this.initializationFailTimeout = initializationFailTimeout;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isIsolateInternalQueries() {
/*  620 */     return this.isIsolateInternalQueries;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setIsolateInternalQueries(boolean isolate) {
/*  631 */     checkIfSealed();
/*  632 */     this.isIsolateInternalQueries = isolate;
/*      */   }
/*      */ 
/*      */   
/*      */   public MetricsTrackerFactory getMetricsTrackerFactory() {
/*  637 */     return this.metricsTrackerFactory;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setMetricsTrackerFactory(MetricsTrackerFactory metricsTrackerFactory) {
/*  642 */     if (this.metricRegistry != null) {
/*  643 */       throw new IllegalStateException("cannot use setMetricsTrackerFactory() and setMetricRegistry() together");
/*      */     }
/*      */     
/*  646 */     this.metricsTrackerFactory = metricsTrackerFactory;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Object getMetricRegistry() {
/*  656 */     return this.metricRegistry;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setMetricRegistry(Object metricRegistry) {
/*  666 */     if (this.metricsTrackerFactory != null) {
/*  667 */       throw new IllegalStateException("cannot use setMetricRegistry() and setMetricsTrackerFactory() together");
/*      */     }
/*      */     
/*  670 */     if (metricRegistry != null) {
/*  671 */       metricRegistry = getObjectOrPerformJndiLookup(metricRegistry);
/*      */       
/*  673 */       if (!UtilityElf.safeIsAssignableFrom(metricRegistry, "com.codahale.metrics.MetricRegistry") && 
/*  674 */         !UtilityElf.safeIsAssignableFrom(metricRegistry, "io.micrometer.core.instrument.MeterRegistry")) {
/*  675 */         throw new IllegalArgumentException("Class must be instance of com.codahale.metrics.MetricRegistry or io.micrometer.core.instrument.MeterRegistry");
/*      */       }
/*      */     } 
/*      */     
/*  679 */     this.metricRegistry = metricRegistry;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Object getHealthCheckRegistry() {
/*  690 */     return this.healthCheckRegistry;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setHealthCheckRegistry(Object healthCheckRegistry) {
/*  701 */     checkIfSealed();
/*      */     
/*  703 */     if (healthCheckRegistry != null) {
/*  704 */       healthCheckRegistry = getObjectOrPerformJndiLookup(healthCheckRegistry);
/*      */       
/*  706 */       if (!(healthCheckRegistry instanceof com.codahale.metrics.health.HealthCheckRegistry)) {
/*  707 */         throw new IllegalArgumentException("Class must be an instance of com.codahale.metrics.health.HealthCheckRegistry");
/*      */       }
/*      */     } 
/*      */     
/*  711 */     this.healthCheckRegistry = healthCheckRegistry;
/*      */   }
/*      */ 
/*      */   
/*      */   public Properties getHealthCheckProperties() {
/*  716 */     return this.healthCheckProperties;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setHealthCheckProperties(Properties healthCheckProperties) {
/*  721 */     checkIfSealed();
/*  722 */     this.healthCheckProperties.putAll(healthCheckProperties);
/*      */   }
/*      */ 
/*      */   
/*      */   public void addHealthCheckProperty(String key, String value) {
/*  727 */     checkIfSealed();
/*  728 */     this.healthCheckProperties.setProperty(key, value);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long getKeepaliveTime() {
/*  738 */     return this.keepaliveTime;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setKeepaliveTime(long keepaliveTimeMs) {
/*  748 */     this.keepaliveTime = keepaliveTimeMs;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isReadOnly() {
/*  758 */     return this.isReadOnly;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setReadOnly(boolean readOnly) {
/*  768 */     checkIfSealed();
/*  769 */     this.isReadOnly = readOnly;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isRegisterMbeans() {
/*  780 */     return this.isRegisterMbeans;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setRegisterMbeans(boolean register) {
/*  790 */     checkIfSealed();
/*  791 */     this.isRegisterMbeans = register;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getPoolName() {
/*  798 */     return this.poolName;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setPoolName(String poolName) {
/*  809 */     checkIfSealed();
/*  810 */     this.poolName = poolName;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ScheduledExecutorService getScheduledExecutor() {
/*  820 */     return this.scheduledExecutor;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setScheduledExecutor(ScheduledExecutorService executor) {
/*  830 */     checkIfSealed();
/*  831 */     this.scheduledExecutor = executor;
/*      */   }
/*      */ 
/*      */   
/*      */   public String getTransactionIsolation() {
/*  836 */     return this.transactionIsolationName;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getSchema() {
/*  846 */     return this.schema;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setSchema(String schema) {
/*  856 */     checkIfSealed();
/*  857 */     this.schema = schema;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getExceptionOverrideClassName() {
/*  868 */     return this.exceptionOverrideClassName;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setExceptionOverrideClassName(String exceptionOverrideClassName) {
/*  879 */     checkIfSealed();
/*      */     
/*  881 */     Class<?> overrideClass = attemptFromContextLoader(exceptionOverrideClassName);
/*      */     try {
/*  883 */       if (overrideClass == null) {
/*  884 */         overrideClass = getClass().getClassLoader().loadClass(exceptionOverrideClassName);
/*  885 */         LOGGER.debug("SQLExceptionOverride class {} found in the HikariConfig class classloader {}", exceptionOverrideClassName, getClass().getClassLoader());
/*      */       } 
/*  887 */     } catch (ClassNotFoundException e) {
/*  888 */       LOGGER.error("Failed to load SQLExceptionOverride class {} from HikariConfig class classloader {}", exceptionOverrideClassName, getClass().getClassLoader());
/*      */     } 
/*      */     
/*  891 */     if (overrideClass == null) {
/*  892 */       throw new RuntimeException("Failed to load SQLExceptionOverride class " + exceptionOverrideClassName + " in either of HikariConfig class loader or Thread context classloader");
/*      */     }
/*      */     
/*      */     try {
/*  896 */       overrideClass.getConstructor(new Class[0]).newInstance(new Object[0]);
/*  897 */       this.exceptionOverrideClassName = exceptionOverrideClassName;
/*      */     }
/*  899 */     catch (Exception e) {
/*  900 */       throw new RuntimeException("Failed to instantiate class " + exceptionOverrideClassName, e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setTransactionIsolation(String isolationLevel) {
/*  913 */     checkIfSealed();
/*  914 */     this.transactionIsolationName = isolationLevel;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ThreadFactory getThreadFactory() {
/*  924 */     return this.threadFactory;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setThreadFactory(ThreadFactory threadFactory) {
/*  934 */     checkIfSealed();
/*  935 */     this.threadFactory = threadFactory;
/*      */   }
/*      */ 
/*      */   
/*      */   void seal() {
/*  940 */     this.sealed = true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void copyStateTo(HikariConfig other) {
/*  950 */     for (Field field : HikariConfig.class.getDeclaredFields()) {
/*  951 */       if (!Modifier.isFinal(field.getModifiers())) {
/*  952 */         field.setAccessible(true);
/*      */         try {
/*  954 */           field.set(other, field.get(this));
/*      */         }
/*  956 */         catch (Exception e) {
/*  957 */           throw new RuntimeException("Failed to copy HikariConfig state: " + e.getMessage(), e);
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/*  962 */     other.sealed = false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Class<?> attemptFromContextLoader(String driverClassName) {
/*  970 */     ClassLoader threadContextClassLoader = Thread.currentThread().getContextClassLoader();
/*  971 */     if (threadContextClassLoader != null) {
/*      */       try {
/*  973 */         Class<?> driverClass = threadContextClassLoader.loadClass(driverClassName);
/*  974 */         LOGGER.debug("Driver class {} found in Thread context class loader {}", driverClassName, threadContextClassLoader);
/*  975 */         return driverClass;
/*  976 */       } catch (ClassNotFoundException e) {
/*  977 */         LOGGER.debug("Driver class {} not found in Thread context class loader {}, trying classloader {}", new Object[] { driverClassName, threadContextClassLoader, 
/*  978 */               getClass().getClassLoader() });
/*      */       } 
/*      */     }
/*      */     
/*  982 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void validate() {
/*  988 */     if (this.poolName == null) {
/*  989 */       this.poolName = generatePoolName();
/*      */     }
/*  991 */     else if (this.isRegisterMbeans && this.poolName.contains(":")) {
/*  992 */       throw new IllegalArgumentException("poolName cannot contain ':' when used with JMX");
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  997 */     this.catalog = UtilityElf.getNullIfEmpty(this.catalog);
/*  998 */     this.connectionInitSql = UtilityElf.getNullIfEmpty(this.connectionInitSql);
/*  999 */     this.connectionTestQuery = UtilityElf.getNullIfEmpty(this.connectionTestQuery);
/* 1000 */     this.transactionIsolationName = UtilityElf.getNullIfEmpty(this.transactionIsolationName);
/* 1001 */     this.dataSourceClassName = UtilityElf.getNullIfEmpty(this.dataSourceClassName);
/* 1002 */     this.dataSourceJndiName = UtilityElf.getNullIfEmpty(this.dataSourceJndiName);
/* 1003 */     this.driverClassName = UtilityElf.getNullIfEmpty(this.driverClassName);
/* 1004 */     this.jdbcUrl = UtilityElf.getNullIfEmpty(this.jdbcUrl);
/*      */ 
/*      */     
/* 1007 */     if (this.dataSource != null) {
/* 1008 */       if (this.dataSourceClassName != null) {
/* 1009 */         LOGGER.warn("{} - using dataSource and ignoring dataSourceClassName.", this.poolName);
/*      */       }
/*      */     }
/* 1012 */     else if (this.dataSourceClassName != null) {
/* 1013 */       if (this.driverClassName != null) {
/* 1014 */         LOGGER.error("{} - cannot use driverClassName and dataSourceClassName together.", this.poolName);
/*      */ 
/*      */         
/* 1017 */         throw new IllegalStateException("cannot use driverClassName and dataSourceClassName together.");
/*      */       } 
/* 1019 */       if (this.jdbcUrl != null) {
/* 1020 */         LOGGER.warn("{} - using dataSourceClassName and ignoring jdbcUrl.", this.poolName);
/*      */       }
/*      */     }
/* 1023 */     else if (this.jdbcUrl == null && this.dataSourceJndiName == null) {
/*      */ 
/*      */       
/* 1026 */       if (this.driverClassName != null) {
/* 1027 */         LOGGER.error("{} - jdbcUrl is required with driverClassName.", this.poolName);
/* 1028 */         throw new IllegalArgumentException("jdbcUrl is required with driverClassName.");
/*      */       } 
/*      */       
/* 1031 */       LOGGER.error("{} - dataSource or dataSourceClassName or jdbcUrl is required.", this.poolName);
/* 1032 */       throw new IllegalArgumentException("dataSource or dataSourceClassName or jdbcUrl is required.");
/*      */     } 
/*      */     
/* 1035 */     validateNumerics();
/*      */     
/* 1037 */     if (LOGGER.isDebugEnabled() || unitTest) {
/* 1038 */       logConfiguration();
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private void validateNumerics() {
/* 1044 */     if (this.maxLifetime != 0L && this.maxLifetime < TimeUnit.SECONDS.toMillis(30L)) {
/* 1045 */       LOGGER.warn("{} - maxLifetime is less than 30000ms, setting to default {}ms.", this.poolName, Long.valueOf(MAX_LIFETIME));
/* 1046 */       this.maxLifetime = MAX_LIFETIME;
/*      */     } 
/*      */ 
/*      */     
/* 1050 */     if (this.keepaliveTime != 0L && this.keepaliveTime < TimeUnit.SECONDS.toMillis(30L)) {
/* 1051 */       LOGGER.warn("{} - keepaliveTime is less than 30000ms, disabling it.", this.poolName);
/* 1052 */       this.keepaliveTime = 0L;
/*      */     } 
/*      */ 
/*      */     
/* 1056 */     if (this.keepaliveTime != 0L && this.maxLifetime != 0L && this.keepaliveTime >= this.maxLifetime) {
/* 1057 */       LOGGER.warn("{} - keepaliveTime is greater than or equal to maxLifetime, disabling it.", this.poolName);
/* 1058 */       this.keepaliveTime = 0L;
/*      */     } 
/*      */     
/* 1061 */     if (this.leakDetectionThreshold > 0L && !unitTest && (
/* 1062 */       this.leakDetectionThreshold < TimeUnit.SECONDS.toMillis(2L) || (this.leakDetectionThreshold > this.maxLifetime && this.maxLifetime > 0L))) {
/* 1063 */       LOGGER.warn("{} - leakDetectionThreshold is less than 2000ms or more than maxLifetime, disabling it.", this.poolName);
/* 1064 */       this.leakDetectionThreshold = 0L;
/*      */     } 
/*      */ 
/*      */     
/* 1068 */     if (this.connectionTimeout < SOFT_TIMEOUT_FLOOR) {
/* 1069 */       LOGGER.warn("{} - connectionTimeout is less than {}ms, setting to {}ms.", new Object[] { this.poolName, Long.valueOf(SOFT_TIMEOUT_FLOOR), Long.valueOf(CONNECTION_TIMEOUT) });
/* 1070 */       this.connectionTimeout = CONNECTION_TIMEOUT;
/*      */     } 
/*      */     
/* 1073 */     if (this.validationTimeout < SOFT_TIMEOUT_FLOOR) {
/* 1074 */       LOGGER.warn("{} - validationTimeout is less than {}ms, setting to {}ms.", new Object[] { this.poolName, Long.valueOf(SOFT_TIMEOUT_FLOOR), Long.valueOf(VALIDATION_TIMEOUT) });
/* 1075 */       this.validationTimeout = VALIDATION_TIMEOUT;
/*      */     } 
/*      */     
/* 1078 */     if (this.maxPoolSize < 1) {
/* 1079 */       this.maxPoolSize = 10;
/*      */     }
/*      */     
/* 1082 */     if (this.minIdle < 0 || this.minIdle > this.maxPoolSize) {
/* 1083 */       this.minIdle = this.maxPoolSize;
/*      */     }
/*      */     
/* 1086 */     if (this.idleTimeout + TimeUnit.SECONDS.toMillis(1L) > this.maxLifetime && this.maxLifetime > 0L && this.minIdle < this.maxPoolSize) {
/* 1087 */       LOGGER.warn("{} - idleTimeout is close to or more than maxLifetime, disabling it.", this.poolName);
/* 1088 */       this.idleTimeout = 0L;
/*      */     }
/* 1090 */     else if (this.idleTimeout != 0L && this.idleTimeout < TimeUnit.SECONDS.toMillis(10L) && this.minIdle < this.maxPoolSize) {
/* 1091 */       LOGGER.warn("{} - idleTimeout is less than 10000ms, setting to default {}ms.", this.poolName, Long.valueOf(IDLE_TIMEOUT));
/* 1092 */       this.idleTimeout = IDLE_TIMEOUT;
/*      */     }
/* 1094 */     else if (this.idleTimeout != IDLE_TIMEOUT && this.idleTimeout != 0L && this.minIdle == this.maxPoolSize) {
/* 1095 */       LOGGER.warn("{} - idleTimeout has been set but has no effect because the pool is operating as a fixed size pool.", this.poolName);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private void checkIfSealed() {
/* 1101 */     if (this.sealed) throw new IllegalStateException("The configuration of the pool is sealed once started. Use HikariConfigMXBean for runtime changes.");
/*      */   
/*      */   }
/*      */   
/*      */   private void logConfiguration() {
/* 1106 */     LOGGER.debug("{} - configuration:", this.poolName);
/* 1107 */     TreeSet<String> propertyNames = new TreeSet<>(PropertyElf.getPropertyNames(HikariConfig.class));
/* 1108 */     for (String prop : propertyNames) {
/*      */       try {
/* 1110 */         Object value = PropertyElf.getProperty(prop, this);
/* 1111 */         if ("dataSourceProperties".equals(prop)) {
/* 1112 */           Properties dsProps = PropertyElf.copyProperties(this.dataSourceProperties);
/* 1113 */           dsProps.setProperty("password", "<masked>");
/* 1114 */           value = dsProps;
/*      */         } 
/*      */         
/* 1117 */         if ("initializationFailTimeout".equals(prop) && this.initializationFailTimeout == Long.MAX_VALUE) {
/* 1118 */           value = "infinite";
/*      */         }
/* 1120 */         else if ("transactionIsolation".equals(prop) && this.transactionIsolationName == null) {
/* 1121 */           value = "default";
/*      */         }
/* 1123 */         else if (prop.matches("scheduledExecutorService|threadFactory") && value == null) {
/* 1124 */           value = "internal";
/*      */         }
/* 1126 */         else if (prop.contains("jdbcUrl") && value instanceof String) {
/* 1127 */           value = ((String)value).replaceAll("([?&;][^&#;=]*[pP]assword=)[^&#;]*", "$1<masked>");
/*      */         }
/* 1129 */         else if (prop.contains("password")) {
/* 1130 */           value = "<masked>";
/*      */         }
/* 1132 */         else if (value instanceof String) {
/* 1133 */           value = "\"" + value + "\"";
/*      */         }
/* 1135 */         else if (value == null) {
/* 1136 */           value = "none";
/*      */         } 
/* 1138 */         LOGGER.debug("{}{}", (prop + "................................................").substring(0, 32), value);
/*      */       }
/* 1140 */       catch (Exception exception) {}
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void loadProperties(String propertyFileName) {
/* 1148 */     File propFile = new File(propertyFileName); 
/* 1149 */     try { InputStream is = propFile.isFile() ? new FileInputStream(propFile) : getClass().getResourceAsStream(propertyFileName); 
/* 1150 */       try { if (is != null) {
/* 1151 */           Properties props = new Properties();
/* 1152 */           props.load(is);
/* 1153 */           PropertyElf.setTargetFromProperties(this, props);
/*      */         } else {
/*      */           
/* 1156 */           throw new IllegalArgumentException("Cannot find property file: " + propertyFileName);
/*      */         } 
/* 1158 */         if (is != null) is.close();  } catch (Throwable throwable) { if (is != null)
/* 1159 */           try { is.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (IOException io)
/* 1160 */     { throw new RuntimeException("Failed to read property file", io); }
/*      */   
/*      */   }
/*      */ 
/*      */   
/*      */   private String generatePoolName() {
/* 1166 */     String prefix = "HikariPool-";
/*      */     
/*      */     try {
/* 1169 */       synchronized (System.getProperties()) {
/* 1170 */         String next = String.valueOf(Integer.getInteger("dev.continuum.kits.libs.hikari.pool_number", 0).intValue() + 1);
/* 1171 */         System.setProperty("dev.continuum.kits.libs.hikari.pool_number", next);
/* 1172 */         return "HikariPool-" + next;
/*      */       } 
/* 1174 */     } catch (AccessControlException e) {
/*      */ 
/*      */       
/* 1177 */       ThreadLocalRandom random = ThreadLocalRandom.current();
/* 1178 */       StringBuilder buf = new StringBuilder("HikariPool-");
/*      */       
/* 1180 */       for (int i = 0; i < 4; i++) {
/* 1181 */         buf.append(ID_CHARACTERS[random.nextInt(62)]);
/*      */       }
/*      */       
/* 1184 */       LOGGER.info("assigned random pool name '{}' (security manager prevented access to system properties)", buf);
/*      */       
/* 1186 */       return buf.toString();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private Object getObjectOrPerformJndiLookup(Object object) {
/* 1192 */     if (object instanceof String) {
/*      */       try {
/* 1194 */         InitialContext initCtx = new InitialContext();
/* 1195 */         return initCtx.lookup((String)object);
/*      */       }
/* 1197 */       catch (NamingException e) {
/* 1198 */         throw new IllegalArgumentException(e);
/*      */       } 
/*      */     }
/* 1201 */     return object;
/*      */   }
/*      */ }


/* Location:              C:\Users\joeal\Downloads\Kits-2.0.9.jar!\dev\continuum\kits\libs\hikari\HikariConfig.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */