/*     */ package dev.continuum.kits.database.kit.premade;
/*     */ 
/*     */ import dev.continuum.kits.config.Messages;
/*     */ import dev.continuum.kits.database.DatabaseError;
/*     */ import dev.continuum.kits.libs.utils.cachable.Cachable;
/*     */ import dev.continuum.kits.libs.utils.scheduler.Schedulers;
/*     */ import dev.continuum.kits.libs.utils.server.Servers;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import net.kyori.adventure.audience.Audience;
/*     */ import org.bukkit.Material;
/*     */ import org.bukkit.configuration.MemorySection;
/*     */ import org.bukkit.configuration.file.FileConfiguration;
/*     */ import org.bukkit.configuration.file.YamlConfiguration;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.inventory.ItemStack;
/*     */ import org.bukkit.inventory.PlayerInventory;
/*     */ import org.jetbrains.annotations.NotNull;
/*     */ import org.jetbrains.annotations.Nullable;
/*     */ 
/*     */ public class PremadeKit
/*     */ {
/*     */   @NotNull
/*     */   public static FileConfiguration config() {
/*  30 */     return (FileConfiguration)Schedulers.async().supply(() -> {
/*     */           File file = new File(Servers.dataFolder(), "premade_kit.yml");
/*     */           
/*     */           if (!file.exists()) {
/*     */             try {
/*     */               file.createNewFile();
/*  36 */             } catch (IOException e) {
/*     */               throw new RuntimeException(e);
/*     */             } 
/*     */           }
/*     */           return YamlConfiguration.loadConfiguration(file);
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   public static void save(@NotNull FileConfiguration config) {
/*     */     try {
/*  47 */       config.save(new File(Servers.dataFolder(), "premade_kit.yml"));
/*  48 */     } catch (IOException e) {
/*  49 */       DatabaseError.execute("<#ff0000>Failed to save premade kit file.");
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void contents(@NotNull Cachable<Integer, ItemStack> contents) {
/*  54 */     FileConfiguration config = config();
/*  55 */     config.set("contents", contents.snapshot().asMap());
/*  56 */     save(config);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public static Cachable<Integer, ItemStack> contents() {
/*  62 */     FileConfiguration config = config();
/*  63 */     Object raw = config.get("contents");
/*  64 */     if (raw == null) return null;
/*     */     
/*  66 */     if (raw instanceof Map) { Map<?, ?> map = (Map<?, ?>)raw;
/*  67 */       return (Cachable<Integer, ItemStack>)Cachable.of(map); }
/*  68 */      if (raw instanceof MemorySection) { MemorySection memorySection = (MemorySection)raw;
/*  69 */       Map<Integer, ItemStack> map = new ConcurrentHashMap<>();
/*     */       
/*  71 */       for (String key : memorySection.getKeys(false)) {
/*  72 */         map.put(
/*  73 */             Integer.valueOf(Integer.parseInt(key)), 
/*  74 */             Objects.<ItemStack>requireNonNullElse(memorySection
/*  75 */               .getItemStack(key), new ItemStack(Material.AIR)));
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  81 */       return (Cachable<Integer, ItemStack>)Cachable.of(map); }
/*     */ 
/*     */     
/*  84 */     return null;
/*     */   }
/*     */   
/*     */   public static void give(@NotNull Player player) {
/*  88 */     Cachable<Integer, ItemStack> contents = contents();
/*     */     
/*  90 */     if (contents == null || contents.cached() == 0) {
/*  91 */       Messages.findAndSend("premade_kit_is_empty", (Audience)player);
/*     */       
/*     */       return;
/*     */     } 
/*  95 */     PlayerInventory inventory = (PlayerInventory)player.getOpenInventory().getBottomInventory();
/*     */     
/*  97 */     List<ItemStack> list = new ArrayList<>(contents.snapshot().asMap().values());
/*  98 */     ItemStack[] array = list.<ItemStack>toArray(new ItemStack[0]);
/*     */     
/* 100 */     inventory.clear();
/* 101 */     inventory.setContents(array);
/* 102 */     Messages.findAndSend("premade_kit_loaded", (Audience)player);
/*     */   }
/*     */ }


/* Location:              C:\Users\joeal\Downloads\Kits-2.0.9.jar!\dev\continuum\kits\database\kit\premade\PremadeKit.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */