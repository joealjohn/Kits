/*     */ package dev.continuum.kits.database.kit.auto;
/*     */ 
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import dev.continuum.kits.database.DatabaseError;
/*     */ import dev.continuum.kits.libs.utils.elements.Elements;
/*     */ import dev.continuum.kits.libs.utils.scheduler.Schedulers;
/*     */ import dev.continuum.kits.libs.utils.server.Servers;
/*     */ import java.io.File;
/*     */ import java.util.UUID;
/*     */ import org.bukkit.configuration.file.YamlConfiguration;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.jetbrains.annotations.NotNull;
/*     */ import org.jetbrains.annotations.Nullable;
/*     */ 
/*     */ 
/*     */ public class AutoKits
/*     */ {
/*  18 */   private static final Elements<AutoKit> cached = (Elements<AutoKit>)Elements.of();
/*     */   
/*     */   public static void start() {
/*  21 */     Schedulers.async().execute(() -> {
/*     */           cached.clear();
/*     */           File[] files = directory().listFiles();
/*     */           if (files == null) {
/*     */             return;
/*     */           }
/*     */           for (File file : files) {
/*     */             if (file.exists() && file.getName().endsWith(".yml")) {
/*     */               YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(file);
/*     */               int kitFound = yamlConfiguration.getInt("kit");
/*     */               if (kitFound == 0 || kitFound == -1) {
/*     */                 kitFound = 1;
/*     */               }
/*     */               boolean enabledFound = yamlConfiguration.getBoolean("enabled");
/*     */               UUID uuid = UUID.fromString(file.getName().replaceAll(".yml", ""));
/*     */               cached.element(new AutoKit(kitFound, uuid, enabledFound));
/*     */             } 
/*     */           } 
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void stop() {
/*  48 */     for (AutoKit autoKit : cached()) {
/*  49 */       autoKit.save();
/*     */     }
/*     */   }
/*     */   
/*     */   @NotNull
/*     */   @CanIgnoreReturnValue
/*     */   public static AutoKit create(@NotNull AutoKit autoKit) {
/*  56 */     if (cached.has(autoKit)) {
/*  57 */       autoKit.saveAsync();
/*  58 */       return autoKit;
/*     */     } 
/*     */     
/*  61 */     autoKit.saveAsync();
/*  62 */     cached.element(autoKit);
/*  63 */     return autoKit;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public static AutoKit by(@NotNull UUID uuid) {
/*  68 */     for (AutoKit autoKit : cached) { if (autoKit.uuid().equals(uuid)) return autoKit;  }
/*  69 */      return null;
/*     */   }
/*     */   
/*     */   @NotNull
/*     */   public static File directory() {
/*  74 */     File file = new File(Servers.dataFolder(), "/autokit/");
/*     */     
/*  76 */     if (!file.exists() && !file.mkdirs()) {
/*  77 */       DatabaseError.execute("An error occured while trying to create folder plugins/ContinuumFFA/<bold>autokit</bold>/");
/*  78 */       return file;
/*     */     } 
/*     */     
/*  81 */     return file;
/*     */   }
/*     */   
/*     */   @NotNull
/*     */   public static Elements<AutoKit> cached() {
/*  86 */     return cached;
/*     */   }
/*     */   
/*     */   public static void on(@NotNull Player player) {
/*  90 */     on(player.getUniqueId());
/*     */   }
/*     */   
/*     */   public static void on(@NotNull UUID uuid) {
/*  94 */     AutoKit autoKit = by(uuid);
/*  95 */     if (autoKit == null) autoKit = create(new AutoKit(1, uuid, true));
/*     */     
/*  97 */     autoKit.enable();
/*  98 */     autoKit.saveAsync();
/*     */   }
/*     */   
/*     */   public static void off(@NotNull Player player) {
/* 102 */     off(player.getUniqueId());
/*     */   }
/*     */   
/*     */   public static void off(@NotNull UUID uuid) {
/* 106 */     AutoKit autoKit = by(uuid);
/* 107 */     if (autoKit == null) autoKit = create(new AutoKit(1, uuid, false));
/*     */     
/* 109 */     autoKit.disable();
/*     */     
/* 111 */     autoKit.saveAsync();
/*     */   }
/*     */   
/*     */   public static void edit(@NotNull Player player, int kit) {
/* 115 */     UUID uuid = player.getUniqueId();
/*     */     
/* 117 */     AutoKit autoKit = by(uuid);
/* 118 */     if (autoKit == null) autoKit = create(new AutoKit(kit, uuid, true));
/*     */     
/* 120 */     autoKit.enable();
/* 121 */     autoKit.kit(kit);
/*     */     
/* 123 */     autoKit.saveAsync();
/*     */   }
/*     */ }


/* Location:              C:\Users\joeal\Downloads\Kits-2.0.9.jar!\dev\continuum\kits\database\kit\auto\AutoKits.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */