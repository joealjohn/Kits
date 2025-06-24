/*     */ package dev.continuum.kits.category;
/*     */ 
/*     */ import dev.continuum.kits.database.DatabaseError;
/*     */ import dev.continuum.kits.libs.utils.cachable.Cachable;
/*     */ import dev.continuum.kits.libs.utils.elements.Elements;
/*     */ import dev.continuum.kits.libs.utils.elements.impl.ElementsImpl;
/*     */ import dev.continuum.kits.libs.utils.server.Servers;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.function.Supplier;
/*     */ import org.bukkit.Material;
/*     */ import org.bukkit.configuration.MemorySection;
/*     */ import org.bukkit.configuration.file.FileConfiguration;
/*     */ import org.bukkit.configuration.file.YamlConfiguration;
/*     */ import org.bukkit.inventory.ItemStack;
/*     */ import org.jetbrains.annotations.NotNull;
/*     */ 
/*     */ public class KitRoomCategories
/*     */ {
/*     */   @NotNull
/*  26 */   public static final KitRoomCategory CRYSTAL_PVP = category(Integer.valueOf(47), () -> "crystal_pvp");
/*     */   
/*     */   @NotNull
/*  29 */   public static final KitRoomCategory POTIONS = category(Integer.valueOf(48), () -> "potions");
/*     */   
/*     */   @NotNull
/*  32 */   public static final KitRoomCategory CONSUMABLES = category(Integer.valueOf(49), () -> "consumables");
/*     */   
/*     */   @NotNull
/*  35 */   public static final KitRoomCategory ARROWS = category(Integer.valueOf(50), () -> "arrows");
/*     */   
/*     */   @NotNull
/*  38 */   public static final KitRoomCategory MISCELLANEOUS = category(Integer.valueOf(51), () -> "miscellaneous");
/*     */   
/*     */   @NotNull
/*     */   public static KitRoomCategory category(@NotNull final Integer at, @NotNull final Supplier<String> rawName) {
/*  42 */     return new KitRoomCategory() { @NotNull
/*     */         public Integer at() {
/*  44 */           return at;
/*     */         } @NotNull
/*     */         public String rawName() {
/*  47 */           return rawName.get();
/*     */         } }
/*     */       ;
/*     */   }
/*     */   @NotNull
/*     */   public static Elements<KitRoomCategory> categories() {
/*  53 */     ElementsImpl elementsImpl = Elements.of();
/*     */     
/*  55 */     elementsImpl.element(CRYSTAL_PVP);
/*  56 */     elementsImpl.element(POTIONS);
/*  57 */     elementsImpl.element(CONSUMABLES);
/*  58 */     elementsImpl.element(ARROWS);
/*  59 */     elementsImpl.element(MISCELLANEOUS);
/*     */     
/*  61 */     return (Elements<KitRoomCategory>)elementsImpl;
/*     */   }
/*     */   
/*     */   @NotNull
/*     */   public static KitRoomCategory crystalPvP() {
/*  66 */     return CRYSTAL_PVP;
/*     */   }
/*     */   
/*     */   @NotNull
/*     */   public static KitRoomCategory potions() {
/*  71 */     return POTIONS;
/*     */   }
/*     */   
/*     */   @NotNull
/*     */   public static KitRoomCategory consumables() {
/*  76 */     return CONSUMABLES;
/*     */   }
/*     */   
/*     */   @NotNull
/*     */   public static KitRoomCategory arrows() {
/*  81 */     return ARROWS;
/*     */   }
/*     */   
/*     */   @NotNull
/*     */   public static KitRoomCategory miscellaneous() {
/*  86 */     return MISCELLANEOUS;
/*     */   }
/*     */   
/*     */   @NotNull
/*     */   public static CompletableFuture<FileConfiguration> config() {
/*  91 */     return CompletableFuture.supplyAsync(() -> {
/*     */           File file = new File(Servers.dataFolder(), "/kit_room.yml");
/*     */           
/*     */           if (!file.exists()) {
/*     */             try {
/*     */               file.createNewFile();
/*  97 */             } catch (IOException e) {
/*     */               throw new RuntimeException(e);
/*     */             } 
/*     */           }
/*     */           return (FileConfiguration)YamlConfiguration.loadConfiguration(file);
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   public static CompletableFuture<Cachable<Integer, ItemStack>> items(@NotNull KitRoomCategory category) {
/* 109 */     return config().thenApply(configuration -> {
/*     */           Object raw = configuration.get("kit_room." + category.rawName() + ".contents");
/*     */           if (raw instanceof Map) {
/*     */             Map<?, ?> map = (Map<?, ?>)raw;
/*     */             return (Cachable)Cachable.of(map);
/*     */           } 
/*     */           if (raw instanceof MemorySection) {
/*     */             MemorySection memorySection = (MemorySection)raw;
/*     */             Map<Integer, ItemStack> map = new ConcurrentHashMap<>();
/*     */             for (String key : memorySection.getKeys(false)) {
/*     */               map.put(Integer.valueOf(Integer.parseInt(key)), Objects.<ItemStack>requireNonNullElse(memorySection.getItemStack(key), new ItemStack(Material.AIR)));
/*     */             }
/*     */             return (Cachable)Cachable.of(map);
/*     */           } 
/*     */           return (Cachable)Cachable.of();
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void save(@NotNull FileConfiguration configuration) {
/* 136 */     File file = new File(Servers.dataFolder(), "/kit_room.yml");
/*     */     
/* 138 */     if (!file.exists()) {
/*     */       try {
/* 140 */         file.createNewFile();
/* 141 */       } catch (IOException e) {
/* 142 */         throw new RuntimeException(e);
/*     */       } 
/*     */     }
/*     */     
/*     */     try {
/* 147 */       configuration.save(file);
/* 148 */     } catch (IOException e) {
/* 149 */       DatabaseError.execute("An issue occured while trying to save the kit room file configuration.");
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void items(@NotNull KitRoomCategory category, @NotNull Cachable<Integer, ItemStack> items) {
/* 154 */     CompletableFuture.runAsync(() -> {
/*     */           Map<Integer, ItemStack> map = items.snapshot().asMap();
/*     */           String rawName = category.rawName();
/*     */           items.delAll(List.of(Integer.valueOf(45), Integer.valueOf(46), Integer.valueOf(47), Integer.valueOf(48), Integer.valueOf(49), Integer.valueOf(50), Integer.valueOf(51), Integer.valueOf(52), Integer.valueOf(53)));
/*     */           config().thenAcceptAsync(());
/*     */         });
/*     */   }
/*     */ }


/* Location:              C:\Users\joeal\Downloads\Kits-2.0.9.jar!\dev\continuum\kits\category\KitRoomCategories.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */