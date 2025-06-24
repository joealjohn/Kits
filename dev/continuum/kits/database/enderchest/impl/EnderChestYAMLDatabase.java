/*     */ package dev.continuum.kits.database.enderchest.impl;
/*     */ 
/*     */ import dev.continuum.kits.database.DatabaseError;
/*     */ import dev.continuum.kits.database.enderchest.EnderChestDatabase;
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
/*     */ public class EnderChestYAMLDatabase
/*     */   implements EnderChestDatabase {
/*  28 */   private static final File directory = new File(Servers.dataFolder(), "/ender_chests/");
/*     */   
/*     */   @NotNull
/*     */   public File playerFile(@NotNull UUID uuid) {
/*  32 */     File file = new File(directory, "/" + String.valueOf(uuid) + ".yml");
/*     */     
/*  34 */     if (!file.exists()) {
/*     */       try {
/*  36 */         if (!file.createNewFile()) {
/*  37 */           DatabaseError.execute("<#ff0000>[" + 
/*  38 */               repr() + "] An error occured while attempting to create the ender chest file for UUID " + String.valueOf(uuid) + " at directory ~/plugins/ContinuumFFA/ender_chests/");
/*     */         
/*     */         }
/*     */       
/*     */       }
/*  43 */       catch (IOException e) {
/*  44 */         DatabaseError.execute("<#ff0000>[" + 
/*  45 */             repr() + "] An error occured while attempting to create the ender chest file for UUID " + String.valueOf(uuid) + " at directory ~/plugins/ContinuumFFA/ender_chests/");
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  52 */     return file;
/*     */   }
/*     */   
/*     */   @NotNull
/*     */   public FileConfiguration config(@NotNull UUID uuid) {
/*     */     try {
/*  58 */       return 
/*  59 */         CompletableFuture.<FileConfiguration>supplyAsync(() -> YamlConfiguration.loadConfiguration(playerFile(uuid)))
/*  60 */         .get();
/*  61 */     } catch (InterruptedException|java.util.concurrent.ExecutionException e) {
/*  62 */       throw new RuntimeException(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   @NotNull
/*     */   public String repr() {
/*  68 */     return "YAML Ender Chests";
/*     */   }
/*     */   
/*     */   @NotNull
/*     */   public Elements<String> types() {
/*  73 */     return (Elements<String>)Elements.of((Object[])new String[] { "yaml", "yml" });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   public Cachable<String, Class<?>> requiredDetails() {
/*  81 */     return (Cachable<String, Class<?>>)Cachable.of();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void start(@NotNull Cachable<String, Object> details) {
/*  87 */     if (directory.exists()) directory.mkdir();
/*     */   
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void stop() {}
/*     */ 
/*     */   
/*     */   public boolean delete(@NotNull UUID uuid, int kit) {
/*  97 */     if (kit == 0) kit = 1; 
/*  98 */     int kitCopy = kit;
/*     */     
/*     */     try {
/* 101 */       return ((Boolean)CompletableFuture.<Boolean>supplyAsync(() -> {
/*     */             FileConfiguration config = config(uuid);
/*     */             ConfigurationSection enderChests = config.getConfigurationSection("ender_chests");
/*     */             if (enderChests == null) {
/*     */               return Boolean.valueOf(false);
/*     */             }
/*     */             ConfigurationSection section = enderChests.getConfigurationSection(String.valueOf(kitCopy));
/*     */             if (section == null) {
/*     */               return Boolean.valueOf(false);
/*     */             }
/*     */             enderChests.set(String.valueOf(kitCopy), null);
/*     */             try {
/*     */               config.save(playerFile(uuid));
/* 114 */             } catch (IOException e) {
/*     */               throw new RuntimeException(e);
/*     */             } 
/*     */             
/*     */             return Boolean.valueOf(true);
/* 119 */           }).get()).booleanValue();
/* 120 */     } catch (InterruptedException|java.util.concurrent.ExecutionException e) {
/* 121 */       throw new RuntimeException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean save(@NotNull UUID uuid, int kit, @NotNull Cachable<Integer, ItemStack> data) {
/* 128 */     if (kit == 0) kit = 1; 
/* 129 */     int kitCopy = kit;
/*     */     
/* 131 */     return ((Boolean)Schedulers.async().supply(() -> {
/*     */           FileConfiguration config = config(uuid);
/*     */           ConfigurationSection enderChests = config.getConfigurationSection("ender_chests");
/*     */           if (enderChests == null) {
/*     */             enderChests = config.createSection("ender_chests");
/*     */           }
/*     */           ConfigurationSection section = enderChests.getConfigurationSection(String.valueOf(kitCopy));
/*     */           if (section == null) {
/*     */             section = enderChests.createSection(String.valueOf(kitCopy));
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
/*     */             ConfigurationSection kits = config.getConfigurationSection("ender_chests");
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
/* 199 */     ConfigurationSection section = config.getConfigurationSection("ender_chests");
/* 200 */     return (section != null);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean has(@NotNull UUID uuid, int kit) {
/* 205 */     if (kit == 0) kit = 1; 
/* 206 */     if (!hasAny(uuid)) return false;
/*     */     
/* 208 */     FileConfiguration config = config(uuid);
/* 209 */     ConfigurationSection enderChests = config.getConfigurationSection("ender_chests");
/* 210 */     if (enderChests == null) return false;
/*     */     
/* 212 */     ConfigurationSection section = config.getConfigurationSection(String.valueOf(kit));
/* 213 */     if (section == null) return false;
/*     */     
/* 215 */     Object raw = section.get("contents");
/* 216 */     return (raw != null);
/*     */   }
/*     */ }


/* Location:              C:\Users\joeal\Downloads\Kits-2.0.9.jar!\dev\continuum\kits\database\enderchest\impl\EnderChestYAMLDatabase.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */