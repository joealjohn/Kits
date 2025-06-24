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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class EnderChestSQLiteDatabase
/*     */   implements EnderChestDatabase
/*     */ {
/*  38 */   private static final HikariConfig config = new HikariConfig();
/*     */   private static HikariDataSource source;
/*     */   private static Connection connection;
/*     */   
/*     */   @NotNull
/*     */   public String repr() {
/*  44 */     return "SQLite Ender Chests";
/*     */   }
/*     */   
/*     */   @NotNull
/*     */   public Elements<String> types() {
/*  49 */     return (Elements<String>)Elements.of((Object[])new String[] { "sqlite" });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   public Cachable<String, Class<?>> requiredDetails() {
/*  56 */     return (Cachable<String, Class<?>>)Cachable.of(new Tuple[] {
/*  57 */           Tuple.tuple("ender_chest_db", String.class)
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void start(@NotNull Cachable<String, Object> details) {
/*  66 */     if (!details.hasKey("ender_chest_db")) {
/*  67 */       DatabaseError.execute("<#ff0000>An error occured while trying to connect to the SQLite database.");
/*  68 */       DatabaseError.execute("<#ff0000>Required details: [\"ender_chest_db\"]");
/*  69 */       DatabaseError.execute("<#ff0000>Disabling this plugin to prevent your players from stressing you out.");
/*  70 */       Bukkit.getPluginManager().disablePlugin((Plugin)ContinuumKits.getPlugin(ContinuumKits.class));
/*     */       
/*     */       return;
/*     */     } 
/*  74 */     Object rawDb = details.val("ender_chest_db");
/*     */     
/*  76 */     if (rawDb == null) {
/*  77 */       DatabaseError.execute("<#ff0000>An error occured while trying to connect to the SQLite database.");
/*  78 */       DatabaseError.execute("<#ff0000>Required details: [\"ender_chest_db\"]");
/*  79 */       DatabaseError.execute("<#ff0000>Disabling this plugin to prevent your players from stressing you out.");
/*  80 */       Bukkit.getPluginManager().disablePlugin((Plugin)ContinuumKits.getPlugin(ContinuumKits.class));
/*     */       
/*     */       return;
/*     */     } 
/*  84 */     String db = ((Class<String>)requiredDetails().val("ender_chest_db")).cast(rawDb);
/*     */     
/*  86 */     File directory = new File(Servers.dataFolder(), "/ender_chests/");
/*  87 */     File file = new File(directory, "/" + db + ".db");
/*     */     
/*  89 */     if (!directory.exists() && 
/*  90 */       !directory.mkdir()) {
/*  91 */       DatabaseError.execute("<#ff0000>An error occured while trying to connect to the SQLite database.");
/*  92 */       DatabaseError.execute("<#ff0000>Failed to create ~/plugins/ContinuumFFA/ender_chests/ender_chests.db file.");
/*  93 */       DatabaseError.execute("<#ff0000>Try creating it yourself.");
/*  94 */       Bukkit.getPluginManager().disablePlugin((Plugin)ContinuumKits.getPlugin(ContinuumKits.class));
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/*  99 */     if (!file.exists()) {
/*     */       try {
/* 101 */         if (!file.createNewFile()) {
/* 102 */           DatabaseError.execute("<#ff0000>An error occured while trying to connect to the SQLite database.");
/* 103 */           DatabaseError.execute("<#ff0000>Failed to create ~/plugins/ContinuumFFA/ender_chests/ender_chests.db file.");
/* 104 */           DatabaseError.execute("<#ff0000>Try creating it yourself.");
/* 105 */           Bukkit.getPluginManager().disablePlugin((Plugin)ContinuumKits.getPlugin(ContinuumKits.class));
/*     */           return;
/*     */         } 
/* 108 */       } catch (IOException e) {
/* 109 */         DatabaseError.execute("<#ff0000>An error occured while trying to connect to the SQLite database.");
/* 110 */         DatabaseError.execute("<#ff0000>Failed to create ~/plugins/ContinuumFFA/ender_chests/ender_chests.db file.");
/* 111 */         DatabaseError.execute("<#ff0000>Try creating it yourself.");
/* 112 */         Bukkit.getPluginManager().disablePlugin((Plugin)ContinuumKits.getPlugin(ContinuumKits.class));
/*     */         
/*     */         return;
/*     */       } 
/*     */     }
/* 117 */     config.setJdbcUrl("jdbc:sqlite:" + file.getPath());
/*     */     
/* 119 */     source = new HikariDataSource(config);
/*     */     
/*     */     try {
/* 122 */       connection = source.getConnection();
/* 123 */     } catch (SQLException e) {
/* 124 */       DatabaseError.execute("<#ff0000>[" + repr() + "] An error occured while attempting to initialize SQL connection.");
/* 125 */       DatabaseError.execute("<#ff0000>Disabling this plugin to prevent your players from stressing you out.");
/* 126 */       Bukkit.getPluginManager().disablePlugin((Plugin)ContinuumKits.getPlugin(ContinuumKits.class));
/*     */       
/*     */       return;
/*     */     } 
/* 130 */     table();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void table() {
/* 139 */     String tableStatement = SQLTableBuilder.of().name("continuum_ender_chests").column("uuid", "VARCHAR(36) NOT NULL", true).column("kit", "INT NOT NULL", true).column("contents", "VARCHAR NOT NULL").build();
/*     */     
/* 141 */     if (source == null) {
/* 142 */       DatabaseError.execute("<#ff0000>[" + repr() + "] An error occured while attempting to create the Ender Chests SQL table.");
/* 143 */       DatabaseError.execute("<#ff0000>Disabling this plugin to prevent your players from stressing you out.");
/* 144 */       Bukkit.getPluginManager().disablePlugin((Plugin)ContinuumKits.getPlugin(ContinuumKits.class));
/*     */       return;
/*     */     } 
/*     */     
/* 148 */     try { PreparedStatement statement = connection.prepareStatement(tableStatement); 
/* 149 */       try { statement.executeUpdate();
/* 150 */         if (statement != null) statement.close();  } catch (Throwable throwable) { if (statement != null) try { statement.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (SQLException e)
/* 151 */     { DatabaseError.execute("An issue occurred while attempting to create the SQL Storage table for Ender Chests");
/* 152 */       DatabaseError.execute("<#ff0000>Disabling this plugin to prevent your players from stressing you out.");
/* 153 */       Bukkit.getPluginManager().disablePlugin((Plugin)ContinuumKits.getPlugin(ContinuumKits.class)); }
/*     */   
/*     */   }
/*     */ 
/*     */   
/*     */   public void stop() {
/*     */     try {
/* 160 */       if (connection != null && !connection.isClosed()) connection.close(); 
/* 161 */     } catch (SQLException e) {
/* 162 */       DatabaseError.execute("An issue occurred while attempting to disconnect/close the SQL database.");
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean delete(@NotNull UUID uuid, int kit) {
/*     */     try {
/* 169 */       return ((Boolean)CompletableFuture.<Boolean>supplyAsync(() -> { String deleteStatement = "DELETE FROM continuum_ender_chests WHERE uuid = ? AND kit = ?"; try { PreparedStatement statement = connection.prepareStatement("DELETE FROM continuum_ender_chests WHERE uuid = ? AND kit = ?"); try { statement.setString(0, uuid.toString()); if (kit == 0) { statement.setInt(1, 1); } else { statement.setInt(1, kit); }
/*     */                  statement.executeUpdate(); Boolean bool = Boolean.valueOf(true); if (statement != null)
/*     */                   statement.close();  return bool; }
/* 172 */               catch (Throwable throwable) { if (statement != null) try { statement.close(); } catch (Throwable throwable1)
/*     */                   { throwable.addSuppressed(throwable1); }
/*     */                 
/*     */ 
/*     */ 
/*     */ 
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
/* 193 */     return ((Boolean)Schedulers.async().supply(() -> { String insertStatement = "INSERT OR REPLACE INTO continuum_ender_chests (uuid, kit, contents) VALUES (?, ?, ?)"; try { PreparedStatement statement = connection.prepareStatement("INSERT OR REPLACE INTO continuum_ender_chests (uuid, kit, contents) VALUES (?, ?, ?)"); try { statement.setString(1, uuid.toString()); if (kit == 0) { statement.setInt(2, 1); } else { statement.setInt(2, kit); }
/*     */                statement.setString(3, Serializers.base64().serializeItemStacks(data.snapshot().asMap())); statement.executeUpdate(); if (statement != null)
/*     */                 statement.close();  }
/* 196 */             catch (Throwable throwable) { if (statement != null) try { statement.close(); } catch (Throwable throwable1)
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
/* 207 */           catch (SQLException e)
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
/* 219 */       return CompletableFuture.<Cachable<Integer, ItemStack>>supplyAsync(() -> {
/*     */             String selectStatement = "SELECT contents FROM continuum_ender_chests WHERE uuid = ? AND kit = ?";
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
/*     */               PreparedStatement statement = connection.prepareStatement("SELECT contents FROM continuum_ender_chests WHERE uuid = ? AND kit = ?");
/* 243 */             } catch (SQLException e) {
/*     */               DatabaseError.execute("An issue occurred while attempting to retrieve contents for uuid " + String.valueOf(uuid) + ", kit " + kit);
/*     */               e.printStackTrace();
/*     */             } finally {
/*     */               if (resultSet != null) {
/*     */                 try {
/*     */                   resultSet.close();
/* 250 */                 } catch (SQLException e) {
/*     */                   DatabaseError.execute("An issue occurred while attempting to close an SQL ResultSet");
/*     */                 } 
/*     */               }
/*     */             } 
/*     */             
/*     */             return null;
/* 257 */           }).get();
/* 258 */     } catch (InterruptedException|java.util.concurrent.ExecutionException e) {
/* 259 */       throw new RuntimeException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasAny(@NotNull UUID uuid) {
/* 265 */     for (int kit = 1; kit <= 10; ) { if (data(uuid, kit) != null) return true;  kit++; }
/* 266 */      return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean has(@NotNull UUID uuid, int kit) {
/* 271 */     return (data(uuid, kit) != null);
/*     */   }
/*     */ }


/* Location:              C:\Users\joeal\Downloads\Kits-2.0.9.jar!\dev\continuum\kits\database\enderchest\impl\EnderChestSQLiteDatabase.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */