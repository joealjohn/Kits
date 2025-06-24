/*     */ package dev.continuum.kits.database.kit.impl;
/*     */ 
/*     */ import dev.continuum.kits.database.DatabaseError;
/*     */ import dev.continuum.kits.database.kit.KitsDatabase;
/*     */ import dev.continuum.kits.libs.utils.cachable.Cachable;
/*     */ import dev.continuum.kits.libs.utils.cachable.impl.CachableImpl;
/*     */ import dev.continuum.kits.libs.utils.elements.Elements;
/*     */ import dev.continuum.kits.libs.utils.scheduler.Schedulers;
/*     */ import dev.continuum.kits.libs.utils.server.Servers;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.UUID;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import org.bukkit.Material;
/*     */ import org.bukkit.configuration.ConfigurationSection;
/*     */ import org.bukkit.configuration.MemorySection;
/*     */ import org.bukkit.configuration.file.FileConfiguration;
/*     */ import org.bukkit.configuration.file.YamlConfiguration;
/*     */ import org.bukkit.inventory.ItemStack;
/*     */ import org.jetbrains.annotations.NotNull;
/*     */ import org.jetbrains.annotations.Nullable;
/*     */ 
/*     */ public class YAMLDatabase
/*     */   implements KitsDatabase
/*     */ {
/*  29 */   private static final File directory = new File(Servers.dataFolder(), "/kits/");
/*     */   
/*     */   @NotNull
/*     */   public File playerFile(@NotNull UUID uuid) {
/*  33 */     File file = new File(directory, "/" + String.valueOf(uuid) + ".yml");
/*     */     
/*  35 */     if (!file.exists()) {
/*     */       try {
/*  37 */         if (!file.createNewFile()) {
/*  38 */           DatabaseError.execute("<#ff0000>[" + 
/*  39 */               repr() + "] An error occured while attempting to create the kit file for UUID " + String.valueOf(uuid) + " at directory ~/plugins/ContinuumFFA/kits/");
/*     */         
/*     */         }
/*     */       
/*     */       }
/*  44 */       catch (IOException e) {
/*  45 */         DatabaseError.execute("<#ff0000>[" + 
/*  46 */             repr() + "] An error occured while attempting to create the kit file for UUID " + String.valueOf(uuid) + " at directory ~/plugins/ContinuumFFA/kits/");
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  53 */     return file;
/*     */   }
/*     */   
/*     */   @NotNull
/*     */   public FileConfiguration config(@NotNull UUID uuid) {
/*     */     try {
/*  59 */       return 
/*  60 */         CompletableFuture.<FileConfiguration>supplyAsync(() -> YamlConfiguration.loadConfiguration(playerFile(uuid)))
/*  61 */         .get();
/*  62 */     } catch (InterruptedException|java.util.concurrent.ExecutionException e) {
/*  63 */       throw new RuntimeException(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   @NotNull
/*     */   public String repr() {
/*  69 */     return "YAML Kits";
/*     */   }
/*     */   
/*     */   @NotNull
/*     */   public Elements<String> types() {
/*  74 */     return (Elements<String>)Elements.of((Object[])new String[] { "yaml", "yml" });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   public Cachable<String, Class<?>> requiredDetails() {
/*  82 */     return (Cachable<String, Class<?>>)Cachable.of();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void start(@NotNull Cachable<String, Object> details) {
/*  88 */     if (directory.exists()) directory.mkdir();
/*     */   
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void stop() {}
/*     */ 
/*     */   
/*     */   public boolean delete(@NotNull UUID uuid, int kit) {
/*  98 */     if (kit == 0) kit = 1; 
/*  99 */     int kitCopy = kit;
/*     */     
/*     */     try {
/* 102 */       return ((Boolean)CompletableFuture.<Boolean>supplyAsync(() -> {
/*     */             FileConfiguration config = config(uuid);
/*     */             ConfigurationSection kits = config.getConfigurationSection("kits");
/*     */             if (kits == null) {
/*     */               return Boolean.valueOf(false);
/*     */             }
/*     */             ConfigurationSection section = kits.getConfigurationSection(String.valueOf(kitCopy));
/*     */             if (section == null) {
/*     */               return Boolean.valueOf(false);
/*     */             }
/*     */             kits.set(String.valueOf(kitCopy), null);
/*     */             try {
/*     */               config.save(playerFile(uuid));
/* 115 */             } catch (IOException e) {
/*     */               throw new RuntimeException(e);
/*     */             } 
/*     */             
/*     */             return Boolean.valueOf(true);
/* 120 */           }).get()).booleanValue();
/* 121 */     } catch (InterruptedException|java.util.concurrent.ExecutionException e) {
/* 122 */       throw new RuntimeException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean save(@NotNull UUID uuid, int kit, @NotNull Cachable<Integer, ItemStack> data) {
/* 128 */     if (kit == 0) kit = 1; 
/* 129 */     int kitCopy = kit;
/*     */     
/* 131 */     return ((Boolean)Schedulers.async().supply(() -> {
/*     */           FileConfiguration config = config(uuid);
/*     */           ConfigurationSection kits = config.getConfigurationSection("kits");
/*     */           if (kits == null) {
/*     */             kits = config.createSection("kits");
/*     */           }
/*     */           ConfigurationSection section = kits.getConfigurationSection(String.valueOf(kitCopy));
/*     */           if (section == null) {
/*     */             section = kits.createSection(String.valueOf(kitCopy));
/*     */           }
/*     */           section.set("contents", data.snapshot().asMap());
/*     */           try {
/*     */             config.save(playerFile(uuid));
/* 144 */           } catch (IOException e) {
/*     */             throw new RuntimeException(e);
/*     */           } 
/*     */           return Boolean.valueOf(true);
/*     */         })).booleanValue();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Cachable<Integer, ItemStack> data(@NotNull UUID uuid, int kit) {
/* 155 */     if (kit == 0) kit = 1; 
/* 156 */     int kitCopy = kit;
/*     */     
/*     */     try {
/* 159 */       return CompletableFuture.<Cachable<Integer, ItemStack>>supplyAsync(() -> {
/*     */             FileConfiguration config = config(uuid);
/*     */             
/*     */             ConfigurationSection kits = config.getConfigurationSection("kits");
/*     */             
/*     */             if (kits == null) {
/*     */               return null;
/*     */             }
/*     */             
/*     */             ConfigurationSection section = kits.getConfigurationSection(String.valueOf(kitCopy));
/*     */             
/*     */             if (section == null) {
/*     */               return null;
/*     */             }
/*     */             Object raw = section.get("contents");
/*     */             if (raw == null) {
/*     */               return null;
/*     */             }
/*     */             if (raw instanceof Map) {
/*     */               Map<?, ?> map = (Map<?, ?>)raw;
/*     */               return Cachable.of(map);
/*     */             } 
/*     */             if (raw instanceof MemorySection) {
/*     */               MemorySection memorySection = (MemorySection)raw;
/*     */               Map<Integer, ItemStack> map = new ConcurrentHashMap<>();
/*     */               for (String key : memorySection.getKeys(false)) {
/*     */                 map.put(Integer.valueOf(Integer.parseInt(key)), Objects.<ItemStack>requireNonNullElse(memorySection.getItemStack(key), new ItemStack(Material.AIR)));
/*     */               }
/*     */               return Cachable.of(map);
/*     */             } 
/*     */             return null;
/* 190 */           }).get();
/* 191 */     } catch (InterruptedException|java.util.concurrent.ExecutionException e) {
/* 192 */       throw new RuntimeException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasAny(@NotNull UUID uuid) {
/* 198 */     FileConfiguration config = config(uuid);
/* 199 */     ConfigurationSection section = config.getConfigurationSection("kits");
/* 200 */     return (section != null);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean has(@NotNull UUID uuid, int kit) {
/* 205 */     if (kit == 0) kit = 1; 
/* 206 */     if (!hasAny(uuid)) return false;
/*     */     
/* 208 */     FileConfiguration config = config(uuid);
/* 209 */     ConfigurationSection kits = config.getConfigurationSection("kits");
/* 210 */     if (kits == null) return false;
/*     */     
/* 212 */     ConfigurationSection section = config.getConfigurationSection(String.valueOf(kit));
/* 213 */     if (section == null) return false;
/*     */     
/* 215 */     Object raw = section.get("contents");
/* 216 */     return (raw != null);
/*     */   }
/*     */ }


/* Location:              C:\Users\joeal\Downloads\Kits-2.0.9.jar!\dev\continuum\kits\database\kit\impl\YAMLDatabase.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */