/*     */ package dev.continuum.kits.database;
/*     */ import dev.continuum.kits.config.ConfigHandler;
/*     */ import dev.continuum.kits.database.enderchest.EnderChestDatabase;
/*     */ import dev.continuum.kits.database.enderchest.impl.EnderChestSQLDatabase;
/*     */ import dev.continuum.kits.database.enderchest.impl.EnderChestSQLiteDatabase;
/*     */ import dev.continuum.kits.database.enderchest.impl.EnderChestYAMLDatabase;
/*     */ import dev.continuum.kits.database.kit.KitsDatabase;
/*     */ import dev.continuum.kits.database.kit.impl.MySQLDatabase;
/*     */ import dev.continuum.kits.database.kit.impl.SQLiteDatabase;
/*     */ import dev.continuum.kits.database.kit.impl.YAMLDatabase;
/*     */ import dev.continuum.kits.libs.utils.cachable.Cachable;
/*     */ import dev.continuum.kits.libs.utils.cachable.impl.CachableImpl;
/*     */ import dev.continuum.kits.libs.utils.elements.Elements;
/*     */ import dev.continuum.kits.libs.utils.scheduler.Schedulers;
/*     */ import org.bukkit.configuration.ConfigurationSection;
/*     */ import org.bukkit.configuration.file.FileConfiguration;
/*     */ import org.jetbrains.annotations.NotNull;
/*     */ import org.jetbrains.annotations.Nullable;
/*     */ 
/*     */ public class DatabaseProvider {
/*  21 */   private static final YAMLDatabase yaml = new YAMLDatabase();
/*  22 */   private static final MySQLDatabase mySQL = new MySQLDatabase();
/*  23 */   private static final SQLiteDatabase sqLite = new SQLiteDatabase();
/*     */   
/*  25 */   private static final EnderChestYAMLDatabase ecYaml = new EnderChestYAMLDatabase();
/*  26 */   private static final EnderChestSQLiteDatabase ecSQLite = new EnderChestSQLiteDatabase();
/*  27 */   private static final EnderChestSQLDatabase ecSQL = new EnderChestSQLDatabase();
/*     */   
/*     */   public static KitsDatabase database() {
/*  30 */     FileConfiguration config = ConfigHandler.config();
/*  31 */     ConfigurationSection storage = config.getConfigurationSection("storage");
/*  32 */     if (storage == null) return (KitsDatabase)yaml;
/*     */     
/*  34 */     for (String key : storage.getKeys(false)) {
/*  35 */       ConfigurationSection section = storage.getConfigurationSection(key);
/*  36 */       if (section == null)
/*     */         continue; 
/*  38 */       boolean enabled = section.getBoolean("enabled");
/*  39 */       if (!enabled)
/*     */         continue; 
/*  41 */       String type = section.getString("type");
/*  42 */       if (type == null)
/*     */         continue; 
/*  44 */       KitsDatabase byType = databaseByType(type);
/*  45 */       if (byType == null)
/*     */         continue; 
/*  47 */       return byType;
/*     */     } 
/*     */     
/*  50 */     return (KitsDatabase)yaml;
/*     */   }
/*     */   
/*     */   public static EnderChestDatabase ecDatabase() {
/*  54 */     FileConfiguration config = ConfigHandler.config();
/*  55 */     ConfigurationSection storage = config.getConfigurationSection("storage");
/*  56 */     if (storage == null) return (EnderChestDatabase)ecYaml;
/*     */     
/*  58 */     for (String key : storage.getKeys(false)) {
/*  59 */       ConfigurationSection section = storage.getConfigurationSection(key);
/*  60 */       if (section == null)
/*     */         continue; 
/*  62 */       boolean enabled = section.getBoolean("enabled");
/*  63 */       if (!enabled)
/*     */         continue; 
/*  65 */       String type = section.getString("type");
/*  66 */       if (type == null)
/*     */         continue; 
/*  68 */       EnderChestDatabase byType = ecDatabaseByType(type);
/*  69 */       if (byType == null)
/*     */         continue; 
/*  71 */       return byType;
/*     */     } 
/*     */     
/*  74 */     return (EnderChestDatabase)ecYaml;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private static KitsDatabase databaseByType(@NotNull String type) {
/*  79 */     Elements<String> yamlTypes = yaml.types();
/*  80 */     if (yamlTypes.has(type.toLowerCase())) return (KitsDatabase)yaml;
/*     */     
/*  82 */     Elements<String> mySQLTypes = mySQL.types();
/*  83 */     if (mySQLTypes.has(type.toLowerCase())) return (KitsDatabase)mySQL;
/*     */     
/*  85 */     Elements<String> sqLiteTypes = sqLite.types();
/*  86 */     if (sqLiteTypes.has(type.toLowerCase())) return (KitsDatabase)sqLite;
/*     */     
/*  88 */     return null;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private static EnderChestDatabase ecDatabaseByType(@NotNull String type) {
/*  93 */     Elements<String> yamlTypes = ecYaml.types();
/*  94 */     if (yamlTypes.has(type.toLowerCase())) return (EnderChestDatabase)ecYaml;
/*     */     
/*  96 */     Elements<String> sqLiteTypes = ecSQLite.types();
/*  97 */     if (sqLiteTypes.has(type.toLowerCase())) return (EnderChestDatabase)ecSQLite;
/*     */     
/*  99 */     Elements<String> sqlTypes = ecSQL.types();
/* 100 */     if (sqlTypes.has(type.toLowerCase())) return (EnderChestDatabase)ecSQL;
/*     */     
/* 102 */     return null;
/*     */   }
/*     */   
/*     */   public static void start() {
/* 106 */     Schedulers.async().execute(() -> {
/*     */           FileConfiguration config = ConfigHandler.config();
/*     */           ConfigurationSection storage = config.getConfigurationSection("storage");
/*     */           if (storage == null) {
/*     */             yaml.start((Cachable)Cachable.of());
/*     */             return;
/*     */           } 
/*     */           for (String key : storage.getKeys(false)) {
/*     */             ConfigurationSection section = storage.getConfigurationSection(key);
/*     */             if (section == null) {
/*     */               continue;
/*     */             }
/*     */             ConfigurationSection details = section.getConfigurationSection("details");
/*     */             boolean enabled = section.getBoolean("enabled");
/*     */             if (!enabled) {
/*     */               continue;
/*     */             }
/*     */             String type = section.getString("type");
/*     */             if (type == null) {
/*     */               continue;
/*     */             }
/*     */             KitsDatabase database = databaseByType(type);
/*     */             if (database == null) {
/*     */               continue;
/*     */             }
/*     */             Cachable<String, Class<?>> requiredMap = database.requiredDetails();
/*     */             if (requiredMap.snapshot().asMap().isEmpty()) {
/*     */               database.start((Cachable)Cachable.of());
/*     */               return;
/*     */             } 
/*     */             if (details == null) {
/*     */               continue;
/*     */             }
/*     */             CachableImpl cachableImpl1 = Cachable.of();
/*     */             for (String requiredKey : requiredMap.snapshot().asMap().keySet()) {
/*     */               Class<?> clazzType = (Class)requiredMap.val(requiredKey);
/*     */               if (clazzType == null) {
/*     */                 continue;
/*     */               }
/*     */               cachableImpl1.cache(requiredKey, details.getObject(requiredKey, clazzType));
/*     */             } 
/*     */             database.start((Cachable)cachableImpl1);
/*     */             EnderChestDatabase ec = ecDatabaseByType(type);
/*     */             if (ec == null)
/*     */               continue; 
/*     */             Cachable<String, Class<?>> ecRequiredMap = ec.requiredDetails();
/*     */             if (ecRequiredMap.snapshot().asMap().isEmpty()) {
/*     */               ec.start((Cachable)Cachable.of());
/*     */               return;
/*     */             } 
/*     */             CachableImpl cachableImpl2 = Cachable.of();
/*     */             for (String requiredKey : ecRequiredMap.snapshot().asMap().keySet()) {
/*     */               Class<?> clazzType = (Class)ecRequiredMap.val(requiredKey);
/*     */               if (clazzType == null)
/*     */                 continue; 
/*     */               cachableImpl2.cache(requiredKey, details.getObject(requiredKey, clazzType));
/*     */             } 
/*     */             ec.start((Cachable)cachableImpl2);
/*     */             return;
/*     */           } 
/*     */           yaml.start((Cachable)Cachable.of());
/*     */         });
/*     */   }
/*     */   
/*     */   public static void stop() {
/* 171 */     database().stop();
/*     */   }
/*     */ }


/* Location:              C:\Users\joeal\Downloads\Kits-2.0.9.jar!\dev\continuum\kits\database\DatabaseProvider.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */