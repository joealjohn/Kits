/*     */ package dev.continuum.kits.database.enderchest.impl;
/*     */ 
/*     */ import dev.continuum.kits.ContinuumKits;
/*     */ import dev.continuum.kits.database.DatabaseError;
/*     */ import dev.continuum.kits.database.enderchest.EnderChestDatabase;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class EnderChestSQLDatabase
/*     */   implements EnderChestDatabase
/*     */ {
/*  34 */   private static final HikariConfig config = new HikariConfig();
/*     */   private static HikariDataSource source;
/*     */   private static Connection connection;
/*     */   
/*     */   @NotNull
/*     */   public String repr() {
/*  40 */     return "MySQL Ender Chests";
/*     */   }
/*     */   
/*     */   @NotNull
/*     */   public Elements<String> types() {
/*  45 */     return (Elements<String>)Elements.of((Object[])new String[] { "mysql" });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   public Cachable<String, Class<?>> requiredDetails() {
/*  52 */     return (Cachable<String, Class<?>>)Cachable.of(new Tuple[] {
/*  53 */           Tuple.tuple("ip", String.class), 
/*     */ 
/*     */ 
/*     */           
/*  57 */           Tuple.tuple("port", Integer.class), 
/*     */ 
/*     */ 
/*     */           
/*  61 */           Tuple.tuple("db", String.class), 
/*     */ 
/*     */ 
/*     */           
/*  65 */           Tuple.tuple("user", String.class), 
/*     */ 
/*     */ 
/*     */           
/*  69 */           Tuple.tuple("pass", String.class)
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void start(@NotNull Cachable<String, Object> details) {
/*  78 */     if (!details.hasKey("ip") || 
/*  79 */       !details.hasKey("port") || 
/*  80 */       !details.hasKey("db") || 
/*  81 */       !details.hasKey("user") || 
/*  82 */       !details.hasKey("pass")) {
/*     */       
/*  84 */       DatabaseError.execute("<#ff0000>An error occured while trying to connect to the MySQL database.");
/*  85 */       DatabaseError.execute("<#ff0000>Required details: [\"ip\", \"port\", \"db\", \"user\", \"pass\"]");
/*  86 */       DatabaseError.execute("<#ff0000>Disabling this plugin to prevent your players from stressing you out.");
/*  87 */       Bukkit.getPluginManager().disablePlugin((Plugin)ContinuumKits.getPlugin(ContinuumKits.class));
/*     */       
/*     */       return;
/*     */     } 
/*  91 */     Object rawIp = details.val("ip");
/*  92 */     Object rawPort = details.val("port");
/*  93 */     Object rawDb = details.val("db");
/*  94 */     Object rawUser = details.val("user");
/*  95 */     Object rawPass = details.val("pass");
/*     */     
/*  97 */     if (rawIp == null || rawPort == null || rawDb == null || rawUser == null || rawPass == null) {
/*  98 */       DatabaseError.execute("<#ff0000>An error occured while trying to connect to the MySQL database.");
/*  99 */       DatabaseError.execute("<#ff0000>Required details: [\"ip\", \"port\", \"db\", \"user\", \"pass\"]");
/* 100 */       DatabaseError.execute("<#ff0000>Disabling this plugin to prevent your players from stressing you out.");
/* 101 */       Bukkit.getPluginManager().disablePlugin((Plugin)ContinuumKits.getPlugin(ContinuumKits.class));
/*     */       
/*     */       return;
/*     */     } 
/* 105 */     String ip = ((Class<String>)requiredDetails().val("ip")).cast(rawIp);
/* 106 */     Integer port = ((Class<Integer>)requiredDetails().val("port")).cast(rawPort);
/* 107 */     String db = ((Class<String>)requiredDetails().val("db")).cast(rawDb);
/* 108 */     String user = ((Class<String>)requiredDetails().val("user")).cast(rawUser);
/* 109 */     String pass = ((Class<String>)requiredDetails().val("pass")).cast(rawPass);
/*     */     
/* 111 */     config.setJdbcUrl("jdbc:mysql://" + ip + ":" + port + "/" + db);
/* 112 */     config.setUsername(user);
/* 113 */     config.setPassword(pass);
/* 114 */     config.addDataSourceProperty("cachePrepStmts", "true");
/* 115 */     config.addDataSourceProperty("prepStmtCacheSize", "250");
/* 116 */     config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
/* 117 */     config.addDataSourceProperty("useServerPrepStmts", "true");
/* 118 */     config.addDataSourceProperty("useLocalSessionState", "true");
/* 119 */     config.addDataSourceProperty("rewriteBatchedStatements", "true");
/* 120 */     config.addDataSourceProperty("cacheResultSetMetadata", "true");
/* 121 */     config.addDataSourceProperty("cacheServerConfiguration", "true");
/* 122 */     config.addDataSourceProperty("elideSetAutoCommits", "true");
/* 123 */     config.addDataSourceProperty("maintainTimeStats", "false");
/*     */     
/* 125 */     source = new HikariDataSource(config);
/*     */     
/*     */     try {
/* 128 */       connection = source.getConnection();
/* 129 */     } catch (SQLException e) {
/* 130 */       DatabaseError.execute("<#ff0000>[" + repr() + "] An error occured while attempting to initialize SQL connection.");
/* 131 */       DatabaseError.execute("<#ff0000>Disabling this plugin to prevent your players from stressing you out.");
/* 132 */       Bukkit.getPluginManager().disablePlugin((Plugin)ContinuumKits.getPlugin(ContinuumKits.class));
/*     */       
/*     */       return;
/*     */     } 
/* 136 */     table();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void table() {
/* 145 */     String tableStatement = SQLTableBuilder.of().name("continuum_ender_chests").column("uuid", "VARCHAR(36) NOT NULL", true).column("kit", "INT NOT NULL", true).column("contents", "TEXT(65535) NOT NULL").build();
/*     */     
/* 147 */     if (source == null) {
/* 148 */       DatabaseError.execute("<#ff0000>[" + repr() + "] An error occured while attempting to create the Ender Chests SQL table.");
/* 149 */       DatabaseError.execute("<#ff0000>Disabling this plugin to prevent your players from stressing you out.");
/* 150 */       Bukkit.getPluginManager().disablePlugin((Plugin)ContinuumKits.getPlugin(ContinuumKits.class));
/*     */       return;
/*     */     } 
/*     */     
/* 154 */     try { PreparedStatement statement = connection.prepareStatement(tableStatement); 
/* 155 */       try { statement.executeUpdate();
/* 156 */         if (statement != null) statement.close();  } catch (Throwable throwable) { if (statement != null) try { statement.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (SQLException e)
/* 157 */     { DatabaseError.execute("An issue occurred while attempting to create the SQL Storage table for Ender Chests");
/* 158 */       DatabaseError.execute("<#ff0000>Disabling this plugin to prevent your players from stressing you out.");
/* 159 */       Bukkit.getPluginManager().disablePlugin((Plugin)ContinuumKits.getPlugin(ContinuumKits.class)); }
/*     */   
/*     */   }
/*     */ 
/*     */   
/*     */   public void stop() {
/*     */     try {
/* 166 */       if (connection != null && !connection.isClosed()) connection.close(); 
/* 167 */     } catch (SQLException e) {
/* 168 */       DatabaseError.execute("An issue occurred while attempting to disconnect/close the SQL database.");
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean delete(@NotNull UUID uuid, int kit) {
/* 174 */     if (kit == 0) kit = 1; 
/* 175 */     int kitCopy = kit;
/*     */     
/*     */     try {
/* 178 */       return ((Boolean)CompletableFuture.<Boolean>supplyAsync(() -> { String deleteStatement = "DELETE FROM continuum_ender_chests WHERE uuid = ? AND kit = ?"; try { PreparedStatement statement = connection.prepareStatement("DELETE FROM continuum_ender_chests WHERE uuid = ? AND kit = ?"); 
/*     */               try { statement.setString(1, uuid.toString()); statement.setInt(2, kitCopy); statement.executeUpdate(); Boolean bool = Boolean.valueOf(true); if (statement != null)
/*     */                   statement.close();  return bool; }
/* 181 */               catch (Throwable throwable) { if (statement != null) try { statement.close(); } catch (Throwable throwable1)
/*     */                   { throwable.addSuppressed(throwable1); }
/*     */                    
/*     */                 throw throwable; }
/*     */                }
/* 186 */             catch (SQLException e)
/*     */             { return Boolean.valueOf(false); }
/*     */           
/* 189 */           }).get()).booleanValue();
/* 190 */     } catch (InterruptedException|java.util.concurrent.ExecutionException e) {
/* 191 */       throw new RuntimeException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean save(@NotNull UUID uuid, int kit, @NotNull Cachable<Integer, ItemStack> data) {
/* 198 */     if (kit == 0) kit = 1; 
/* 199 */     int kitCopy = kit;
/*     */     
/* 201 */     return ((Boolean)Schedulers.async().supply(() -> { String insertStatement = "INSERT INTO continuum_ender_chests (uuid, kit, contents) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE contents = ?"; try { PreparedStatement statement = connection.prepareStatement("INSERT INTO continuum_ender_chests (uuid, kit, contents) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE contents = ?"); 
/*     */             try { statement.setString(1, uuid.toString()); statement.setInt(2, kitCopy); statement.setString(3, Serializers.base64().serializeItemStacks(data.snapshot().asMap())); statement.setString(4, Serializers.base64().serializeItemStacks(data.snapshot().asMap())); statement.executeUpdate(); if (statement != null)
/*     */                 statement.close();  }
/* 204 */             catch (Throwable throwable) { if (statement != null) try { statement.close(); } catch (Throwable throwable1)
/*     */                 { throwable.addSuppressed(throwable1); }
/*     */               
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */               
/*     */               throw throwable; }
/*     */              }
/* 214 */           catch (SQLException e)
/*     */           { return Boolean.valueOf(false); }
/*     */           
/*     */           return Boolean.valueOf(true);
/*     */         })).booleanValue();
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Cachable<Integer, ItemStack> data(@NotNull UUID uuid, int kit) {
/* 224 */     if (kit == 0) kit = 1; 
/* 225 */     int kitCopy = kit;
/*     */     
/*     */     try {
/* 228 */       return CompletableFuture.<Cachable<Integer, ItemStack>>supplyAsync(() -> {
/*     */             String selectStatement = "SELECT contents FROM continuum_ender_chests WHERE uuid = ? AND kit = ?";
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
/*     */               PreparedStatement statement = connection.prepareStatement("SELECT contents FROM continuum_ender_chests WHERE uuid = ? AND kit = ?");
/* 247 */             } catch (SQLException e) {
/*     */               DatabaseError.execute("An issue occurred while attempting to retrieve contents for uuid " + String.valueOf(uuid) + ", kit " + kitCopy);
/*     */             } finally {
/*     */               if (resultSet != null) {
/*     */                 try {
/*     */                   resultSet.close();
/* 253 */                 } catch (SQLException e) {
/*     */                   DatabaseError.execute("An issue occurred while attempting to close an SQL ResultSet");
/*     */                 } 
/*     */               }
/*     */             } 
/*     */             
/*     */             return null;
/* 260 */           }).get();
/* 261 */     } catch (InterruptedException|java.util.concurrent.ExecutionException e) {
/* 262 */       throw new RuntimeException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasAny(@NotNull UUID uuid) {
/* 268 */     for (int kit = 1; kit <= 10; ) { if (data(uuid, kit) != null) return true;  kit++; }
/* 269 */      return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean has(@NotNull UUID uuid, int kit) {
/* 274 */     if (kit == 0) kit = 1; 
/* 275 */     return (data(uuid, kit) != null);
/*     */   }
/*     */ }


/* Location:              C:\Users\joeal\Downloads\Kits-2.0.9.jar!\dev\continuum\kits\database\enderchest\impl\EnderChestSQLDatabase.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */