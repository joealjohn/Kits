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
/*     */ import dev.continuum.kits.libs.utils.server.Servers;
/*     */ import dev.continuum.kits.libs.utils.sql.query.SQLTableBuilder;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
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
/*     */ public class SQLiteDatabase
/*     */   implements KitsDatabase
/*     */ {
/*  33 */   private static final HikariConfig config = new HikariConfig();
/*     */   private static HikariDataSource source;
/*     */   private static Connection connection;
/*     */   
/*     */   @NotNull
/*     */   public String repr() {
/*  39 */     return "SQLite Kits";
/*     */   }
/*     */   
/*     */   @NotNull
/*     */   public Elements<String> types() {
/*  44 */     return (Elements<String>)Elements.of((Object[])new String[] { "sqlite" });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   public Cachable<String, Class<?>> requiredDetails() {
/*  51 */     return (Cachable<String, Class<?>>)Cachable.of(new Tuple[] {
/*  52 */           Tuple.tuple("db", String.class)
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void start(@NotNull Cachable<String, Object> details) {
/*  61 */     if (!details.hasKey("db")) {
/*  62 */       DatabaseError.execute("<#ff0000>An error occured while trying to connect to the SQLite database.");
/*  63 */       DatabaseError.execute("<#ff0000>Required details: [\"db\"]");
/*  64 */       DatabaseError.execute("<#ff0000>Disabling this plugin to prevent your players from stressing you out.");
/*  65 */       Bukkit.getPluginManager().disablePlugin((Plugin)ContinuumKits.getPlugin(ContinuumKits.class));
/*     */       
/*     */       return;
/*     */     } 
/*  69 */     Object rawDb = details.val("db");
/*     */     
/*  71 */     if (rawDb == null) {
/*  72 */       DatabaseError.execute("<#ff0000>An error occured while trying to connect to the SQLite database.");
/*  73 */       DatabaseError.execute("<#ff0000>Required details: [\"db\"]");
/*  74 */       DatabaseError.execute("<#ff0000>Disabling this plugin to prevent your players from stressing you out.");
/*  75 */       Bukkit.getPluginManager().disablePlugin((Plugin)ContinuumKits.getPlugin(ContinuumKits.class));
/*     */       
/*     */       return;
/*     */     } 
/*  79 */     String db = ((Class<String>)requiredDetails().val("db")).cast(rawDb);
/*     */     
/*  81 */     File directory = new File(Servers.dataFolder(), "/kits/");
/*  82 */     File file = new File(directory, "/" + db + ".db");
/*     */     
/*  84 */     if (!directory.exists() && 
/*  85 */       !directory.mkdir()) {
/*  86 */       DatabaseError.execute("<#ff0000>An error occured while trying to connect to the SQLite database.");
/*  87 */       DatabaseError.execute("<#ff0000>Failed to create ~/plugins/ContinuumFFA/kits/kits.db file.");
/*  88 */       DatabaseError.execute("<#ff0000>Try creating it yourself.");
/*  89 */       Bukkit.getPluginManager().disablePlugin((Plugin)ContinuumKits.getPlugin(ContinuumKits.class));
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/*  94 */     if (!file.exists()) {
/*     */       try {
/*  96 */         if (!file.createNewFile()) {
/*  97 */           DatabaseError.execute("<#ff0000>An error occured while trying to connect to the SQLite database.");
/*  98 */           DatabaseError.execute("<#ff0000>Failed to create ~/plugins/ContinuumFFA/kits/kits.db file.");
/*  99 */           DatabaseError.execute("<#ff0000>Try creating it yourself.");
/* 100 */           Bukkit.getPluginManager().disablePlugin((Plugin)ContinuumKits.getPlugin(ContinuumKits.class));
/*     */           return;
/*     */         } 
/* 103 */       } catch (IOException e) {
/* 104 */         DatabaseError.execute("<#ff0000>An error occured while trying to connect to the SQLite database.");
/* 105 */         DatabaseError.execute("<#ff0000>Failed to create ~/plugins/ContinuumFFA/kits/kits.db file.");
/* 106 */         DatabaseError.execute("<#ff0000>Try creating it yourself.");
/* 107 */         Bukkit.getPluginManager().disablePlugin((Plugin)ContinuumKits.getPlugin(ContinuumKits.class));
/*     */         
/*     */         return;
/*     */       } 
/*     */     }
/* 112 */     config.setJdbcUrl("jdbc:sqlite:" + file.getPath());
/*     */     
/* 114 */     source = new HikariDataSource(config);
/*     */     
/*     */     try {
/* 117 */       connection = source.getConnection();
/* 118 */     } catch (SQLException e) {
/* 119 */       DatabaseError.execute("<#ff0000>[" + repr() + "] An error occured while attempting to initialize SQL connection.");
/* 120 */       DatabaseError.execute("<#ff0000>Disabling this plugin to prevent your players from stressing you out.");
/* 121 */       Bukkit.getPluginManager().disablePlugin((Plugin)ContinuumKits.getPlugin(ContinuumKits.class));
/*     */       
/*     */       return;
/*     */     } 
/* 125 */     table();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void table() {
/* 134 */     String tableStatement = SQLTableBuilder.of().name("continuum_kits").column("uuid", "VARCHAR(36) NOT NULL", true).column("kit", "INT NOT NULL", true).column("contents", "VARCHAR NOT NULL").build();
/*     */     
/* 136 */     if (source == null) {
/* 137 */       DatabaseError.execute("<#ff0000>[" + repr() + "] An error occured while attempting to create the Kits SQL table.");
/* 138 */       DatabaseError.execute("<#ff0000>Disabling this plugin to prevent your players from stressing you out.");
/* 139 */       Bukkit.getPluginManager().disablePlugin((Plugin)ContinuumKits.getPlugin(ContinuumKits.class));
/*     */       return;
/*     */     } 
/*     */     
/* 143 */     try { PreparedStatement statement = connection.prepareStatement(tableStatement); 
/* 144 */       try { statement.executeUpdate();
/* 145 */         if (statement != null) statement.close();  } catch (Throwable throwable) { if (statement != null) try { statement.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (SQLException e)
/* 146 */     { DatabaseError.execute("An issue occurred while attempting to create the SQL Storage table for Kits");
/* 147 */       DatabaseError.execute("<#ff0000>Disabling this plugin to prevent your players from stressing you out.");
/* 148 */       Bukkit.getPluginManager().disablePlugin((Plugin)ContinuumKits.getPlugin(ContinuumKits.class)); }
/*     */   
/*     */   }
/*     */ 
/*     */   
/*     */   public void stop() {
/*     */     try {
/* 155 */       if (connection != null && !connection.isClosed()) connection.close(); 
/* 156 */     } catch (SQLException e) {
/* 157 */       DatabaseError.execute("An issue occurred while attempting to disconnect/close the SQL database.");
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean delete(@NotNull UUID uuid, int kit) {
/*     */     try {
/* 164 */       return ((Boolean)CompletableFuture.<Boolean>supplyAsync(() -> { String deleteStatement = "DELETE FROM continuum_kits WHERE uuid = ? AND kit = ?"; try { PreparedStatement statement = connection.prepareStatement("DELETE FROM continuum_kits WHERE uuid = ? AND kit = ?"); try { statement.setString(0, uuid.toString()); if (kit == 0) { statement.setInt(1, 1); } else { statement.setInt(1, kit); }
/*     */                  statement.executeUpdate(); Boolean bool = Boolean.valueOf(true); if (statement != null)
/*     */                   statement.close();  return bool; }
/* 167 */               catch (Throwable throwable) { if (statement != null) try { statement.close(); } catch (Throwable throwable1)
/*     */                   { throwable.addSuppressed(throwable1); }
/*     */                 
/*     */ 
/*     */ 
/*     */ 
/*     */                 
/*     */                 throw throwable; }
/*     */                }
/* 176 */             catch (SQLException e)
/*     */             { return Boolean.valueOf(false); }
/*     */           
/* 179 */           }).get()).booleanValue();
/* 180 */     } catch (InterruptedException|java.util.concurrent.ExecutionException e) {
/* 181 */       throw new RuntimeException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean save(@NotNull UUID uuid, int kit, @NotNull Cachable<Integer, ItemStack> data) {
/* 188 */     return ((Boolean)Schedulers.async().supply(() -> { String insertStatement = "INSERT OR REPLACE INTO continuum_kits (uuid, kit, contents) VALUES (?, ?, ?)"; try { PreparedStatement statement = connection.prepareStatement("INSERT OR REPLACE INTO continuum_kits (uuid, kit, contents) VALUES (?, ?, ?)"); try { statement.setString(1, uuid.toString()); if (kit == 0) { statement.setInt(2, 1); } else { statement.setInt(2, kit); }
/*     */                statement.setString(3, Serializers.base64().serializeItemStacks(data.snapshot().asMap())); statement.executeUpdate(); if (statement != null)
/*     */                 statement.close();  }
/* 191 */             catch (Throwable throwable) { if (statement != null) try { statement.close(); } catch (Throwable throwable1)
/*     */                 { throwable.addSuppressed(throwable1); }
/*     */               
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */               
/*     */               throw throwable; }
/*     */              }
/* 202 */           catch (SQLException e)
/*     */           { e.printStackTrace();
/*     */             return Boolean.valueOf(false); }
/*     */           
/*     */           return Boolean.valueOf(true);
/*     */         })).booleanValue();
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Cachable<Integer, ItemStack> data(@NotNull UUID uuid, int kit) {
/*     */     try {
/* 214 */       return CompletableFuture.<Cachable<Integer, ItemStack>>supplyAsync(() -> {
/*     */             String selectStatement = "SELECT contents FROM continuum_kits WHERE uuid = ? AND kit = ?";
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
/*     */             ResultSet resultSet = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/*     */             try {
/*     */               PreparedStatement statement = connection.prepareStatement("SELECT contents FROM continuum_kits WHERE uuid = ? AND kit = ?");
/* 238 */             } catch (SQLException e) {
/*     */               DatabaseError.execute("An issue occurred while attempting to retrieve contents for uuid " + String.valueOf(uuid) + ", kit " + kit);
/*     */               e.printStackTrace();
/*     */             } finally {
/*     */               if (resultSet != null) {
/*     */                 try {
/*     */                   resultSet.close();
/* 245 */                 } catch (SQLException e) {
/*     */                   DatabaseError.execute("An issue occurred while attempting to close an SQL ResultSet");
/*     */                 } 
/*     */               }
/*     */             } 
/*     */             
/*     */             return null;
/* 252 */           }).get();
/* 253 */     } catch (InterruptedException|java.util.concurrent.ExecutionException e) {
/* 254 */       throw new RuntimeException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasAny(@NotNull UUID uuid) {
/* 260 */     for (int kit = 1; kit <= 10; ) { if (data(uuid, kit) != null) return true;  kit++; }
/* 261 */      return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean has(@NotNull UUID uuid, int kit) {
/* 266 */     return (data(uuid, kit) != null);
/*     */   }
/*     */ }


/* Location:              C:\Users\joeal\Downloads\Kits-2.0.9.jar!\dev\continuum\kits\database\kit\impl\SQLiteDatabase.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */