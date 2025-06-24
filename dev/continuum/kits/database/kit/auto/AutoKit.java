/*     */ package dev.continuum.kits.database.kit.auto;
/*     */ 
/*     */ import dev.continuum.kits.database.DatabaseError;
/*     */ import dev.continuum.kits.libs.utils.scheduler.Schedulers;
/*     */ import dev.continuum.kits.libs.utils.server.Servers;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.util.UUID;
/*     */ import org.bukkit.configuration.file.YamlConfiguration;
/*     */ import org.jetbrains.annotations.NotNull;
/*     */ 
/*     */ 
/*     */ public class AutoKit
/*     */ {
/*     */   private int kit;
/*     */   private final UUID uuid;
/*     */   private boolean enabled;
/*     */   
/*     */   public AutoKit(int kit, @NotNull UUID uuid, boolean enabled) {
/*  20 */     this.kit = kit;
/*  21 */     this.uuid = uuid;
/*  22 */     this.enabled = enabled;
/*     */   }
/*     */   
/*     */   public AutoKit(int kit, @NotNull UUID uuid) {
/*  26 */     this.kit = kit;
/*  27 */     this.uuid = uuid;
/*  28 */     this.enabled = false;
/*     */   }
/*     */   
/*     */   public int kit() {
/*  32 */     return this.kit;
/*     */   }
/*     */   
/*     */   public void kit(int kit) {
/*  36 */     this.kit = kit;
/*     */   }
/*     */   
/*     */   @NotNull
/*     */   public UUID uuid() {
/*  41 */     return this.uuid;
/*     */   }
/*     */   
/*     */   public boolean enabled() {
/*  45 */     return this.enabled;
/*     */   }
/*     */   
/*     */   public void enabled(boolean enabled) {
/*  49 */     this.enabled = enabled;
/*     */   }
/*     */   
/*     */   public void disable() {
/*  53 */     this.enabled = false;
/*     */   }
/*     */   
/*     */   public void enable() {
/*  57 */     this.enabled = true;
/*     */   }
/*     */   
/*     */   public void toggle() {
/*  61 */     this.enabled = !this.enabled;
/*     */   }
/*     */   
/*     */   public void load() {
/*  65 */     File autoKitDir = new File(Servers.dataFolder(), "/autokit/");
/*     */     
/*  67 */     if (!autoKitDir.exists() && !autoKitDir.mkdirs()) {
/*  68 */       DatabaseError.execute("An error occured while trying to create folder plugins/ContinuumFFA/<bold>autokit</bold>/");
/*     */       
/*     */       return;
/*     */     } 
/*  72 */     File autoKitFile = new File(autoKitDir, "/" + String.valueOf(this.uuid) + ".yml");
/*     */     
/*  74 */     if (!autoKitFile.exists()) {
/*     */       try {
/*  76 */         if (!autoKitFile.createNewFile()) {
/*  77 */           YamlConfiguration yamlConfiguration1 = YamlConfiguration.loadConfiguration(autoKitFile);
/*     */           
/*  79 */           int i = yamlConfiguration1.getInt("kit");
/*  80 */           if (i == 0 || i == -1) i = 1;
/*     */           
/*  82 */           this.kit = i;
/*     */           
/*  84 */           this.enabled = yamlConfiguration1.getBoolean("enabled");
/*     */           return;
/*     */         } 
/*  87 */       } catch (IOException e) {
/*  88 */         throw new RuntimeException(e);
/*     */       } 
/*     */     }
/*     */     
/*  92 */     YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(autoKitFile);
/*     */     
/*  94 */     int kitFound = yamlConfiguration.getInt("kit");
/*  95 */     if (kitFound == 0 || kitFound == -1) kitFound = 1;
/*     */     
/*  97 */     this.kit = kitFound;
/*     */     
/*  99 */     this.enabled = yamlConfiguration.getBoolean("enabled");
/*     */   }
/*     */   
/*     */   public void loadAsync() {
/* 103 */     Schedulers.async().execute(this::load);
/*     */   }
/*     */   
/*     */   public void save() {
/* 107 */     File autoKitDir = new File(Servers.dataFolder(), "/autokit/");
/*     */     
/* 109 */     if (!autoKitDir.exists() && !autoKitDir.mkdirs()) {
/* 110 */       DatabaseError.execute("An error occured while trying to create folder plugins/ContinuumFFA/<bold>autokit</bold>/");
/*     */       
/*     */       return;
/*     */     } 
/* 114 */     File autoKitFile = new File(autoKitDir, "/" + String.valueOf(this.uuid) + ".yml");
/*     */     
/* 116 */     if (!autoKitFile.exists()) {
/*     */       try {
/* 118 */         if (!autoKitFile.createNewFile()) {
/* 119 */           YamlConfiguration yamlConfiguration1 = YamlConfiguration.loadConfiguration(autoKitFile);
/*     */           
/* 121 */           yamlConfiguration1.set("kit", Integer.valueOf(this.kit));
/* 122 */           yamlConfiguration1.set("enabled", Boolean.valueOf(this.enabled));
/*     */           
/*     */           try {
/* 125 */             yamlConfiguration1.save(autoKitFile);
/* 126 */           } catch (IOException e) {
/* 127 */             throw new RuntimeException(e);
/*     */           } 
/*     */           
/*     */           return;
/*     */         } 
/* 132 */       } catch (IOException e) {
/* 133 */         throw new RuntimeException(e);
/*     */       } 
/*     */     }
/*     */     
/* 137 */     YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(autoKitFile);
/*     */     
/* 139 */     yamlConfiguration.set("kit", Integer.valueOf(this.kit));
/* 140 */     yamlConfiguration.set("enabled", Boolean.valueOf(this.enabled));
/*     */     
/*     */     try {
/* 143 */       yamlConfiguration.save(autoKitFile);
/* 144 */     } catch (IOException e) {
/* 145 */       throw new RuntimeException(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void saveAsync() {
/* 150 */     Schedulers.async().execute(this::save);
/*     */   }
/*     */ }


/* Location:              C:\Users\joeal\Downloads\Kits-2.0.9.jar!\dev\continuum\kits\database\kit\auto\AutoKit.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */