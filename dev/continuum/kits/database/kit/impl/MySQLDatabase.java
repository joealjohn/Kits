/*     */ package dev.continuum.kits.database.kit.impl;
/*     */ 
/*     */ import dev.continuum.kits.ContinuumKits;
/*     */ import dev.continuum.kits.database.DatabaseError;
/*     */ import dev.continuum.kits.database.kit.KitsDatabase;
/*     */ import dev.continuum.kits.libs.hikari.HikariConfig;
/*     */ import dev.continuum.kits.libs.hikari.HikariDataSource;
/*     */ import dev.continuum.kits.libs.utils.cachable.Cachable;
/*     */ import dev.continuum.kits.libs.utils.cachable.impl.CachableImpl;
/*     */ import dev.continuum.kits.libs.utils.elements.Elements;
/*     */ import dev.continuum.kits.libs.utils.model.Tuple;
/*     */ import dev.continuum.kits.libs.utils.scheduler.Schedulers;
/*     */ import dev.continuum.kits.libs.utils.serializers.Serializers;
/*     */ import dev.continuum.kits.libs.utils.sql.query.SQLTableBuilder;
/*     */ import java.sql.Connection;
/*     */ import java.sql.PreparedStatement;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
/*     */ import java.util.UUID;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import org.bukkit.Bukkit;
/*     */ import org.bukkit.inventory.ItemStack;
/*     */ import org.bukkit.plugin.Plugin;
/*     */ import org.jetbrains.annotations.NotNull;
/*     */ import org.jetbrains.annotations.Nullable;
/*     */ 
/*     */ public class MySQLDatabase
/*     */   implements KitsDatabase {
/*  29 */   private static final HikariConfig config = new HikariConfig();
/*     */   private static HikariDataSource source;
/*     */   private static Connection connection;
/*     */   
/*     */   @NotNull
/*     */   public String repr() {
/*  35 */     return "MySQL Kits";
/*     */   }
/*     */   
/*     */   @NotNull
/*     */   public Elements<String> types() {
/*  40 */     return (Elements<String>)Elements.of((Object[])new String[] { "mysql" });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   public Cachable<String, Class<?>> requiredDetails() {
/*  47 */     return (Cachable<String, Class<?>>)Cachable.of(new Tuple[] {
/*  48 */           Tuple.tuple("ip", String.class), 
/*     */ 
/*     */ 
/*     */           
/*  52 */           Tuple.tuple("port", Integer.class), 
/*     */ 
/*     */ 
/*     */           
/*  56 */           Tuple.tuple("db", String.class), 
/*     */ 
/*     */ 
/*     */           
/*  60 */           Tuple.tuple("user", String.class), 
/*     */ 
/*     */ 
/*     */           
/*  64 */           Tuple.tuple("pass", String.class)
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void start(@NotNull Cachable<String, Object> details) {
/*  73 */     if (!details.hasKey("ip") || 
/*  74 */       !details.hasKey("port") || 
/*  75 */       !details.hasKey("db") || 
/*  76 */       !details.hasKey("user") || 
/*  77 */       !details.hasKey("pass")) {
/*     */       
/*  79 */       DatabaseError.execute("<#ff0000>An error occured while trying to connect to the MySQL database.");
/*  80 */       DatabaseError.execute("<#ff0000>Required details: [\"ip\", \"port\", \"db\", \"user\", \"pass\"]");
/*  81 */       DatabaseError.execute("<#ff0000>Disabling this plugin to prevent your players from stressing you out.");
/*  82 */       Bukkit.getPluginManager().disablePlugin((Plugin)ContinuumKits.getPlugin(ContinuumKits.class));
/*     */       
/*     */       return;
/*     */     } 
/*  86 */     Object rawIp = details.val("ip");
/*  87 */     Object rawPort = details.val("port");
/*  88 */     Object rawDb = details.val("db");
/*  89 */     Object rawUser = details.val("user");
/*  90 */     Object rawPass = details.val("pass");
/*     */     
/*  92 */     if (rawIp == null || rawPort == null || rawDb == null || rawUser == null || rawPass == null) {
/*  93 */       DatabaseError.execute("<#ff0000>An error occured while trying to connect to the MySQL database.");
/*  94 */       DatabaseError.execute("<#ff0000>Required details: [\"ip\", \"port\", \"db\", \"user\", \"pass\"]");
/*  95 */       DatabaseError.execute("<#ff0000>Disabling this plugin to prevent your players from stressing you out.");
/*  96 */       Bukkit.getPluginManager().disablePlugin((Plugin)ContinuumKits.getPlugin(ContinuumKits.class));
/*     */       
/*     */       return;
/*     */     } 
/* 100 */     String ip = ((Class<String>)requiredDetails().val("ip")).cast(rawIp);
/* 101 */     Integer port = ((Class<Integer>)requiredDetails().val("port")).cast(rawPort);
/* 102 */     String db = ((Class<String>)requiredDetails().val("db")).cast(rawDb);
/* 103 */     String user = ((Class<String>)requiredDetails().val("user")).cast(rawUser);
/* 104 */     String pass = ((Class<String>)requiredDetails().val("pass")).cast(rawPass);
/*     */     
/* 106 */     config.setJdbcUrl("jdbc:mysql://" + ip + ":" + port + "/" + db);
/* 107 */     config.setUsername(user);
/* 108 */     config.setPassword(pass);
/* 109 */     config.addDataSourceProperty("cachePrepStmts", "true");
/* 110 */     config.addDataSourceProperty("prepStmtCacheSize", "250");
/* 111 */     config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
/* 112 */     config.addDataSourceProperty("useServerPrepStmts", "true");
/* 113 */     config.addDataSourceProperty("useLocalSessionState", "true");
/* 114 */     config.addDataSourceProperty("rewriteBatchedStatements", "true");
/* 115 */     config.addDataSourceProperty("cacheResultSetMetadata", "true");
/* 116 */     config.addDataSourceProperty("cacheServerConfiguration", "true");
/* 117 */     config.addDataSourceProperty("elideSetAutoCommits", "true");
/* 118 */     config.addDataSourceProperty("maintainTimeStats", "false");
/*     */     
/* 120 */     source = new HikariDataSource(config);
/*     */     
/*     */     try {
/* 123 */       connection = source.getConnection();
/* 124 */     } catch (SQLException e) {
/* 125 */       DatabaseError.execute("<#ff0000>[" + repr() + "] An error occured while attempting to initialize SQL connection.");
/* 126 */       DatabaseError.execute("<#ff0000>Disabling this plugin to prevent your players from stressing you out.");
/* 127 */       Bukkit.getPluginManager().disablePlugin((Plugin)ContinuumKits.getPlugin(ContinuumKits.class));
/*     */       
/*     */       return;
/*     */     } 
/* 131 */     table();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void table() {
/* 140 */     String tableStatement = SQLTableBuilder.of().name("continuum_kits").column("uuid", "VARCHAR(36) NOT NULL", true).column("kit", "INT NOT NULL", true).column("contents", "TEXT(65535) NOT NULL").build();
/*     */     
/* 142 */     if (source == null) {
/* 143 */       DatabaseError.execute("<#ff0000>[" + repr() + "] An error occured while attempting to create the Kits SQL table.");
/* 144 */       DatabaseError.execute("<#ff0000>Disabling this plugin to prevent your players from stressing you out.");
/* 145 */       Bukkit.getPluginManager().disablePlugin((Plugin)ContinuumKits.getPlugin(ContinuumKits.class));
/*     */       return;
/*     */     } 
/*     */     
/* 149 */     try { PreparedStatement statement = connection.prepareStatement(tableStatement); 
/* 150 */       try { statement.executeUpdate();
/* 151 */         if (statement != null) statement.close();  } catch (Throwable throwable) { if (statement != null) try { statement.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (SQLException e)
/* 152 */     { DatabaseError.execute("An issue occurred while attempting to create the SQL Storage table for Kits");
/* 153 */       DatabaseError.execute("<#ff0000>Disabling this plugin to prevent your players from stressing you out.");
/* 154 */       Bukkit.getPluginManager().disablePlugin((Plugin)ContinuumKits.getPlugin(ContinuumKits.class)); }
/*     */   
/*     */   }
/*     */ 
/*     */   
/*     */   public void stop() {
/*     */     try {
/* 161 */       if (connection != null && !connection.isClosed()) connection.close(); 
/* 162 */     } catch (SQLException e) {
/* 163 */       DatabaseError.execute("An issue occurred while attempting to disconnect/close the SQL database.");
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean delete(@NotNull UUID uuid, int kit) {
/* 169 */     if (kit == 0) kit = 1; 
/* 170 */     int kitCopy = kit;
/*     */     
/*     */     try {
/* 173 */       return ((Boolean)CompletableFuture.<Boolean>supplyAsync(() -> { String deleteStatement = "DELETE FROM continuum_kits WHERE uuid = ? AND kit = ?"; try { PreparedStatement statement = connection.prepareStatement("DELETE FROM continuum_kits WHERE uuid = ? AND kit = ?"); 
/*     */               try { statement.setString(1, uuid.toString()); statement.setInt(2, kitCopy); statement.executeUpdate(); Boolean bool = Boolean.valueOf(true); if (statement != null)
/*     */                   statement.close();  return bool; }
/* 176 */               catch (Throwable throwable) { if (statement != null) try { statement.close(); } catch (Throwable throwable1)
/*     */                   { throwable.addSuppressed(throwable1); }
/*     */                    
/*     */                 throw throwable; }
/*     */                }
/* 181 */             catch (SQLException e)
/*     */             { return Boolean.valueOf(false); }
/*     */           
/* 184 */           }).get()).booleanValue();
/* 185 */     } catch (InterruptedException|java.util.concurrent.ExecutionException e) {
/* 186 */       throw new RuntimeException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean save(@NotNull UUID uuid, int kit, @NotNull Cachable<Integer, ItemStack> data) {
/* 193 */     if (kit == 0) kit = 1; 
/* 194 */     int kitCopy = kit;
/*     */     
/* 196 */     return ((Boolean)Schedulers.async().supply(() -> { String insertStatement = "INSERT INTO continuum_kits (uuid, kit, contents) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE contents = ?"; try { PreparedStatement statement = connection.prepareStatement("INSERT INTO continuum_kits (uuid, kit, contents) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE contents = ?"); 
/*     */             try { statement.setString(1, uuid.toString()); statement.setInt(2, kitCopy); statement.setString(3, Serializers.base64().serializeItemStacks(data.snapshot().asMap())); statement.setString(4, Serializers.base64().serializeItemStacks(data.snapshot().asMap())); statement.executeUpdate(); if (statement != null)
/*     */                 statement.close();  }
/* 199 */             catch (Throwable throwable) { if (statement != null) try { statement.close(); } catch (Throwable throwable1)
/*     */                 { throwable.addSuppressed(throwable1); }
/*     */               
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */               
/*     */               throw throwable; }
/*     */              }
/* 209 */           catch (SQLException e)
/*     */           { return Boolean.valueOf(false); }
/*     */           
/*     */           return Boolean.valueOf(true);
/*     */         })).booleanValue();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Cachable<Integer, ItemStack> data(@NotNull UUID uuid, int kit) {
/* 220 */     if (kit == 0) kit = 1; 
/* 221 */     int kitCopy = kit;
/*     */     
/*     */     try {
/* 224 */       return CompletableFuture.<Cachable<Integer, ItemStack>>supplyAsync(() -> {
/*     */             String selectStatement = "SELECT contents FROM continuum_kits WHERE uuid = ? AND kit = ?";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/*     */             ResultSet resultSet = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/*     */             try {
/*     */               PreparedStatement statement = connection.prepareStatement("SELECT contents FROM continuum_kits WHERE uuid = ? AND kit = ?");
/* 243 */             } catch (SQLException e) {
/*     */               DatabaseError.execute("An issue occurred while attempting to retrieve contents for uuid " + String.valueOf(uuid) + ", kit " + kitCopy);
/*     */             } finally {
/*     */               if (resultSet != null) {
/*     */                 try {
/*     */                   resultSet.close();
/* 249 */                 } catch (SQLException e) {
/*     */                   DatabaseError.execute("An issue occurred while attempting to close an SQL ResultSet");
/*     */                 } 
/*     */               }
/*     */             } 
/*     */             
/*     */             return null;
/* 256 */           }).get();
/* 257 */     } catch (InterruptedException|java.util.concurrent.ExecutionException e) {
/* 258 */       throw new RuntimeException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasAny(@NotNull UUID uuid) {
/* 264 */     for (int kit = 1; kit <= 10; ) { if (data(uuid, kit) != null) return true;  kit++; }
/* 265 */      return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean has(@NotNull UUID uuid, int kit) {
/* 270 */     if (kit == 0) kit = 1; 
/* 271 */     return (data(uuid, kit) != null);
/*     */   }
/*     */ }


/* Location:              C:\Users\joeal\Downloads\Kits-2.0.9.jar!\dev\continuum\kits\database\kit\impl\MySQLDatabase.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */