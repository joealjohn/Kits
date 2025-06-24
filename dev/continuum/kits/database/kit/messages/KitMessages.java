/*     */ package dev.continuum.kits.database.kit.messages;
/*     */ 
/*     */ import dev.continuum.kits.config.Messages;
/*     */ import dev.continuum.kits.libs.utils.cachable.Cachable;
/*     */ import dev.continuum.kits.libs.utils.cachable.impl.CachableImpl;
/*     */ import dev.continuum.kits.libs.utils.elements.Elements;
/*     */ import dev.continuum.kits.libs.utils.location.LocationUtils;
/*     */ import dev.continuum.kits.libs.utils.misc.ObjectUtils;
/*     */ import dev.continuum.kits.libs.utils.model.Tuple;
/*     */ import dev.continuum.kits.libs.utils.scheduler.Schedulers;
/*     */ import dev.continuum.kits.libs.utils.server.Servers;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.text.DecimalFormat;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.UUID;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import net.kyori.adventure.audience.Audience;
/*     */ import org.bukkit.configuration.ConfigurationSection;
/*     */ import org.bukkit.configuration.MemorySection;
/*     */ import org.bukkit.configuration.file.FileConfiguration;
/*     */ import org.bukkit.configuration.file.YamlConfiguration;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.jetbrains.annotations.NotNull;
/*     */ 
/*     */ public class KitMessages {
/*  29 */   private static Cachable<UUID, Boolean> disabledCache = (Cachable<UUID, Boolean>)Cachable.of();
/*     */   
/*     */   @NotNull
/*     */   public static FileConfiguration config() {
/*     */     try {
/*  34 */       return CompletableFuture.<FileConfiguration>supplyAsync(() -> YamlConfiguration.loadConfiguration(file())).get();
/*  35 */     } catch (InterruptedException|java.util.concurrent.ExecutionException e) {
/*  36 */       throw new RuntimeException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   public static File file() {
/*  43 */     File file = new File(Servers.dataFolder(), "/kit_messages.yml");
/*     */     
/*  45 */     if (!file.exists()) {
/*     */       try {
/*  47 */         file.createNewFile();
/*  48 */       } catch (IOException e) {
/*  49 */         throw new RuntimeException(e);
/*     */       } 
/*     */     }
/*     */     
/*  53 */     return file;
/*     */   }
/*     */   
/*     */   public static void start() {
/*  57 */     Schedulers.async().execute(() -> {
/*     */           FileConfiguration config = config();
/*     */           ConfigurationSection section = (ConfigurationSection)ObjectUtils.defaultIfNull(config.getConfigurationSection("data"), config.createSection("data"));
/*     */           Object raw = section.get("disabled_cache");
/*     */           if (raw == null) {
/*     */             section.set("disabled_cache", new HashMap<>());
/*     */           }
/*     */           if (raw instanceof Map) {
/*     */             Map<?, ?> map = (Map<?, ?>)raw;
/*     */             CachableImpl cachableImpl1 = Cachable.of(map);
/*     */             CachableImpl cachableImpl2 = Cachable.of();
/*     */             for (Map.Entry<String, Boolean> entry : (Iterable<Map.Entry<String, Boolean>>)cachableImpl1.snapshot().asMap().entrySet()) {
/*     */               String rawUUID = entry.getKey();
/*     */               boolean disabled = ((Boolean)entry.getValue()).booleanValue();
/*     */               UUID uuid = UUID.fromString(rawUUID);
/*     */               cachableImpl2.cache(uuid, Boolean.valueOf(disabled));
/*     */             } 
/*     */             disabledCache = (Cachable<UUID, Boolean>)cachableImpl2;
/*     */           } else if (raw instanceof MemorySection) {
/*     */             MemorySection memorySection = (MemorySection)raw;
/*     */             Map<UUID, Boolean> map = new ConcurrentHashMap<>();
/*     */             for (String key : memorySection.getKeys(false)) {
/*     */               map.put(UUID.fromString(key), Boolean.valueOf(memorySection.getBoolean(key)));
/*     */               disabledCache = (Cachable<UUID, Boolean>)Cachable.of(map);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void stop() {
/*  99 */     FileConfiguration config = config();
/*     */     
/* 101 */     ConfigurationSection section = (ConfigurationSection)ObjectUtils.defaultIfNull(config
/* 102 */         .getConfigurationSection("data"), config
/* 103 */         .createSection("data", Map.of("disabled_cache", disabledCache
/*     */             
/* 105 */             .snapshot().asMap())));
/*     */ 
/*     */ 
/*     */     
/* 109 */     section.set("disabled_cache", disabledCache.snapshot().asMap());
/*     */     
/*     */     try {
/* 112 */       config.save(file());
/* 113 */     } catch (IOException e) {
/* 114 */       throw new RuntimeException(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static boolean isDisabledEntirely() {
/* 119 */     return config().getBoolean("kit_messages_disabled_entirely");
/*     */   }
/*     */   
/*     */   public static int radius() {
/* 123 */     int radius = config().getInt("kit_messages_receive_radius");
/* 124 */     if (radius == 0) return -1; 
/* 125 */     return radius;
/*     */   }
/*     */   
/*     */   public static void edit(@NotNull UUID uuid, boolean value) {
/* 129 */     disabledCache.cache(uuid, Boolean.valueOf(value));
/*     */   }
/*     */   
/*     */   public static void toggle(@NotNull Player player) {
/* 133 */     if (((Boolean)disabledCache.val(player.getUniqueId())).equals(Boolean.TRUE)) { enable(player); }
/* 134 */     else { disable(player); }
/*     */   
/*     */   }
/*     */   @NotNull
/*     */   private static String prettyState(boolean state) {
/* 139 */     return state ? 
/* 140 */       config().getString("state_on", "<green><bold>ON</bold>") : 
/* 141 */       config().getString("state_off", "<red><bold>OFF</bold>");
/*     */   }
/*     */   
/*     */   public static void enable(@NotNull Player player) {
/* 145 */     UUID uuid = player.getUniqueId();
/* 146 */     edit(uuid, false);
/* 147 */     Messages.findAndSend("now_seeing_kit_messages", (Elements)Elements.of((Object[])new Tuple[] { Tuple.tuple("state", prettyState(true)) }), (Audience)player);
/*     */   }
/*     */   
/*     */   public static void disable(@NotNull Player player) {
/* 151 */     UUID uuid = player.getUniqueId();
/* 152 */     edit(uuid, true);
/* 153 */     Messages.findAndSend("no_longer_seeing_kit_messages", (Elements)Elements.of((Object[])new Tuple[] { Tuple.tuple("state", prettyState(false)) }), (Audience)player);
/*     */   }
/*     */   
/*     */   public static void broadcast(@NotNull Player player, int kit) {
/* 157 */     if (kit == 0) kit = 1; 
/* 158 */     int copyKit = kit;
/*     */     
/* 160 */     if (isDisabledEntirely())
/* 161 */       return;  int radius = radius();
/*     */     
/* 163 */     if (radius != -1) {
/* 164 */       Schedulers.async().execute(() -> {
/*     */             for (Player target : LocationUtils.playersNearPlayer(player, radius)) {
/*     */               if (target.getName().equals(player.getName())) {
/*     */                 return;
/*     */               }
/*     */ 
/*     */ 
/*     */ 
/*     */               
/*     */               Boolean disabled = (Boolean)disabledCache.val(target.getUniqueId());
/*     */ 
/*     */ 
/*     */ 
/*     */               
/*     */               if (disabled != null && disabled.booleanValue()) {
/*     */                 continue;
/*     */               }
/*     */ 
/*     */ 
/*     */ 
/*     */               
/*     */               double distance = player.getLocation().distance(target.getLocation());
/*     */ 
/*     */ 
/*     */ 
/*     */               
/*     */               String formattedDistance = (new DecimalFormat("#.#")).format(distance);
/*     */ 
/*     */ 
/*     */ 
/*     */               
/*     */               Messages.findAndSend("player_equipped_kit", (Elements)Elements.of((Object[])new Tuple[] { Tuple.tuple("kit", String.valueOf(copyKit)), Tuple.tuple("player", player.getName()), Tuple.tuple("distance", formattedDistance) }), (Audience)target);
/*     */             } 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/*     */             Boolean playerDisabled = (Boolean)disabledCache.val(player.getUniqueId());
/*     */ 
/*     */ 
/*     */ 
/*     */             
/*     */             if (playerDisabled != null && playerDisabled.booleanValue()) {
/*     */               return;
/*     */             }
/*     */ 
/*     */ 
/*     */ 
/*     */             
/*     */             Messages.findAndSend("player_equipped_kit", (Elements)Elements.of((Object[])new Tuple[] { Tuple.tuple("kit", String.valueOf(copyKit)), Tuple.tuple("player", player.getName()), Tuple.tuple("distance", "0.0") }), (Audience)player);
/*     */           });
/*     */     } else {
/* 216 */       for (Player target : Servers.online()) {
/* 217 */         Boolean disabled = (Boolean)disabledCache.val(target.getUniqueId());
/* 218 */         if (disabled != null && disabled.booleanValue())
/*     */           continue; 
/* 220 */         Messages.findAndSend("player_equipped_kit", 
/* 221 */             (Elements)Elements.of((Object[])new Tuple[] {
/* 222 */                 Tuple.tuple("kit", 
/*     */                   
/* 224 */                   String.valueOf(copyKit)), 
/*     */                 
/* 226 */                 Tuple.tuple("player", player
/*     */                   
/* 228 */                   .getName())
/*     */               }), (Audience)target);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\joeal\Downloads\Kits-2.0.9.jar!\dev\continuum\kits\database\kit\messages\KitMessages.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */