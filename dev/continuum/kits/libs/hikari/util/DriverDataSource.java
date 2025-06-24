/*     */ package dev.continuum.kits.libs.hikari.util;
/*     */ 
/*     */ import java.io.PrintWriter;
/*     */ import java.sql.Connection;
/*     */ import java.sql.Driver;
/*     */ import java.sql.DriverManager;
/*     */ import java.sql.SQLException;
/*     */ import java.sql.SQLFeatureNotSupportedException;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Map;
/*     */ import java.util.Properties;
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
/*     */ public final class DriverDataSource
/*     */   implements DataSource
/*     */ {
/*  33 */   private static final Logger LOGGER = LoggerFactory.getLogger(DriverDataSource.class);
/*     */   
/*     */   private static final String PASSWORD = "password";
/*     */   
/*     */   private static final String USER = "user";
/*     */   private final String jdbcUrl;
/*     */   private final Properties driverProperties;
/*     */   private Driver driver;
/*     */   
/*     */   public DriverDataSource(String jdbcUrl, String driverClassName, Properties properties, String username, String password) {
/*  43 */     this.jdbcUrl = jdbcUrl;
/*  44 */     this.driverProperties = new Properties();
/*     */     
/*  46 */     for (Map.Entry<Object, Object> entry : properties.entrySet()) {
/*  47 */       this.driverProperties.setProperty(entry.getKey().toString(), entry.getValue().toString());
/*     */     }
/*     */     
/*  50 */     if (username != null) {
/*  51 */       this.driverProperties.put("user", this.driverProperties.getProperty("user", username));
/*     */     }
/*  53 */     if (password != null) {
/*  54 */       this.driverProperties.put("password", this.driverProperties.getProperty("password", password));
/*     */     }
/*     */     
/*  57 */     if (driverClassName != null) {
/*  58 */       Enumeration<Driver> drivers = DriverManager.getDrivers();
/*  59 */       while (drivers.hasMoreElements()) {
/*  60 */         Driver d = drivers.nextElement();
/*  61 */         if (d.getClass().getName().equals(driverClassName)) {
/*  62 */           this.driver = d;
/*     */           
/*     */           break;
/*     */         } 
/*     */       } 
/*  67 */       if (this.driver == null) {
/*  68 */         LOGGER.warn("Registered driver with driverClassName={} was not found, trying direct instantiation.", driverClassName);
/*  69 */         Class<?> driverClass = null;
/*  70 */         ClassLoader threadContextClassLoader = Thread.currentThread().getContextClassLoader();
/*     */         try {
/*  72 */           if (threadContextClassLoader != null) {
/*     */             try {
/*  74 */               driverClass = threadContextClassLoader.loadClass(driverClassName);
/*  75 */               LOGGER.debug("Driver class {} found in Thread context class loader {}", driverClassName, threadContextClassLoader);
/*     */             }
/*  77 */             catch (ClassNotFoundException e) {
/*  78 */               LOGGER.debug("Driver class {} not found in Thread context class loader {}, trying classloader {}", new Object[] { driverClassName, threadContextClassLoader, 
/*  79 */                     getClass().getClassLoader() });
/*     */             } 
/*     */           }
/*     */           
/*  83 */           if (driverClass == null) {
/*  84 */             driverClass = getClass().getClassLoader().loadClass(driverClassName);
/*  85 */             LOGGER.debug("Driver class {} found in the HikariConfig class classloader {}", driverClassName, getClass().getClassLoader());
/*     */           } 
/*  87 */         } catch (ClassNotFoundException e) {
/*  88 */           LOGGER.debug("Failed to load driver class {} from HikariConfig class classloader {}", driverClassName, getClass().getClassLoader());
/*     */         } 
/*     */         
/*  91 */         if (driverClass != null) {
/*     */           try {
/*  93 */             this.driver = driverClass.getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
/*  94 */           } catch (Exception e) {
/*  95 */             LOGGER.warn("Failed to create instance of driver class {}, trying jdbcUrl resolution", driverClassName, e);
/*     */           } 
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 101 */     String sanitizedUrl = jdbcUrl.replaceAll("([?&;][^&#;=]*[pP]assword=)[^&#;]*", "$1<masked>");
/*     */     
/*     */     try {
/* 104 */       if (this.driver == null) {
/* 105 */         this.driver = DriverManager.getDriver(jdbcUrl);
/* 106 */         LOGGER.debug("Loaded driver with class name {} for jdbcUrl={}", this.driver.getClass().getName(), sanitizedUrl);
/*     */       }
/* 108 */       else if (!this.driver.acceptsURL(jdbcUrl)) {
/* 109 */         throw new RuntimeException("Driver " + driverClassName + " claims to not accept jdbcUrl, " + sanitizedUrl);
/*     */       }
/*     */     
/* 112 */     } catch (SQLException e) {
/* 113 */       throw new RuntimeException("Failed to get driver instance for jdbcUrl=" + sanitizedUrl, e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Connection getConnection() throws SQLException {
/* 120 */     return this.driver.connect(this.jdbcUrl, this.driverProperties);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Connection getConnection(String username, String password) throws SQLException {
/* 126 */     Properties cloned = (Properties)this.driverProperties.clone();
/* 127 */     if (username != null) {
/* 128 */       cloned.put("user", username);
/* 129 */       if (cloned.containsKey("username")) {
/* 130 */         cloned.put("username", username);
/*     */       }
/*     */     } 
/* 133 */     if (password != null) {
/* 134 */       cloned.put("password", password);
/*     */     }
/*     */     
/* 137 */     return this.driver.connect(this.jdbcUrl, cloned);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public PrintWriter getLogWriter() throws SQLException {
/* 143 */     throw new SQLFeatureNotSupportedException();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLogWriter(PrintWriter logWriter) throws SQLException {
/* 149 */     throw new SQLFeatureNotSupportedException();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLoginTimeout(int seconds) throws SQLException {
/* 155 */     DriverManager.setLoginTimeout(seconds);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getLoginTimeout() throws SQLException {
/* 161 */     return DriverManager.getLoginTimeout();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Logger getParentLogger() throws SQLFeatureNotSupportedException {
/* 167 */     return this.driver.getParentLogger();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> T unwrap(Class<T> iface) throws SQLException {
/* 173 */     throw new SQLFeatureNotSupportedException();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isWrapperFor(Class<?> iface) throws SQLException {
/* 179 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\joeal\Downloads\Kits-2.0.9.jar!\dev\continuum\kits\libs\hikar\\util\DriverDataSource.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */