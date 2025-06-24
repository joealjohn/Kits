/*     */ package dev.continuum.kits.libs.utils.player;
/*     */ 
/*     */ import dev.continuum.kits.libs.utils.item.ItemBuilder;
/*     */ import dev.continuum.kits.libs.utils.library.Utils;
/*     */ import dev.continuum.kits.libs.utils.message.ActionBarMessager;
/*     */ import dev.continuum.kits.libs.utils.message.ChatMessager;
/*     */ import dev.continuum.kits.libs.utils.message.Messagers;
/*     */ import dev.continuum.kits.libs.utils.text.color.TextStyle;
/*     */ import java.time.Duration;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.UUID;
/*     */ import java.util.stream.IntStream;
/*     */ import net.kyori.adventure.audience.Audience;
/*     */ import net.kyori.adventure.text.Component;
/*     */ import net.kyori.adventure.title.Title;
/*     */ import org.bukkit.Bukkit;
/*     */ import org.bukkit.GameMode;
/*     */ import org.bukkit.Location;
/*     */ import org.bukkit.OfflinePlayer;
/*     */ import org.bukkit.attribute.Attribute;
/*     */ import org.bukkit.attribute.AttributeInstance;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.inventory.ItemStack;
/*     */ import org.bukkit.plugin.Plugin;
/*     */ import org.bukkit.potion.PotionEffect;
/*     */ import org.bukkit.potion.PotionEffectType;
/*     */ import org.jetbrains.annotations.NotNull;
/*     */ import org.jetbrains.annotations.Nullable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PlayerUtils
/*     */ {
/*     */   public static void reset(@NotNull Player player) {
/*  39 */     player.setFlying(false);
/*  40 */     player.setAllowFlight(false);
/*  41 */     player.setGameMode(GameMode.SURVIVAL);
/*  42 */     player.setLevel(0);
/*  43 */     player.setExp(0.0F);
/*  44 */     player.setTotalExperience(0);
/*  45 */     player.setFoodLevel(20);
/*  46 */     player.setHealth(20.0D);
/*  47 */     player.getInventory().clear();
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
/*     */   public static void clearChat(@NotNull Player player, int count) {
/*  59 */     Objects.requireNonNull(player); IntStream.range(0, count).mapToObj(i -> "").forEach(player::sendMessage);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void hide(@NotNull Player target) {
/*  68 */     Utils.plugin()
/*  69 */       .getServer()
/*  70 */       .getOnlinePlayers()
/*  71 */       .forEach(player -> player.hidePlayer((Plugin)Utils.plugin(), target));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void show(@NotNull Player target) {
/*  82 */     Bukkit.getOnlinePlayers()
/*  83 */       .forEach(player -> player.showPlayer((Plugin)Utils.plugin(), target));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void heal(@NotNull Player player) {
/*  94 */     player.setSaturation(20.0F);
/*  95 */     player.setHealth(((AttributeInstance)Objects.<AttributeInstance>requireNonNull(player.getAttribute(Attribute.GENERIC_MAX_HEALTH))).getValue());
/*  96 */     player.setArrowsInBody(0);
/*  97 */     player.setFoodLevel(20);
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
/*     */   public static void title(@NotNull Player player, @NotNull Component title, @NotNull Component subtitle, @NotNull Duration fadeIn, @NotNull Duration stay, @NotNull Duration fadeOut) {
/* 111 */     Audience audience = Audience.audience(new Audience[] { (Audience)player });
/*     */     
/* 113 */     audience.showTitle(Title.title(title, subtitle, Title.Times.times(fadeIn, stay, fadeOut)));
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
/*     */   public static void broadcastTitle(@NotNull Component title, @NotNull Component subtitle, @NotNull Duration fadeIn, @NotNull Duration stay, @NotNull Duration fadeOut) {
/* 126 */     Bukkit.getOnlinePlayers()
/* 127 */       .forEach(player -> title(player, title, subtitle, fadeIn, stay, fadeOut));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void teleport(@NotNull Player player, @NotNull Location location) {
/* 137 */     player.teleport(location);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void teleport(@NotNull Player player, @NotNull Player target) {
/* 147 */     player.teleport(target.getLocation());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void giveItem(@NotNull Player player, @NotNull ItemBuilder item) {
/* 157 */     player.getInventory().addItem(new ItemStack[] { item.build() });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void clearInventory(@NotNull Player player) {
/* 166 */     player.getInventory().clear();
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
/*     */   public static void applyPotionEffect(@NotNull Player player, @NotNull PotionEffectType type, int duration, int amplifier) {
/* 178 */     player.addPotionEffect(new PotionEffect(type, duration, amplifier));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void removePotionEffect(@NotNull Player player, @NotNull PotionEffectType type) {
/* 188 */     player.removePotionEffect(type);
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
/*     */   public static void clearPotionEffects(@NotNull Player player) {
/* 200 */     Objects.requireNonNull(player); player.getActivePotionEffects().stream().map(PotionEffect::getType).forEach(player::removePotionEffect);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void removeItem(@NotNull Player player, @NotNull ItemBuilder item) {
/* 210 */     player.getInventory().removeItem(new ItemStack[] { item.build() });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void gamemodeOfPlayers(@NotNull GameMode gamemode, @NotNull List<Player> players) {
/* 220 */     players.forEach(player -> player.setGameMode(gamemode));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   public static Player player(@NotNull Player player) {
/* 230 */     return player;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public static Player player(@NotNull UUID uuid) {
/* 240 */     return Utils.plugin().getServer().getPlayer(uuid);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public static Player player(@NotNull String playerName) {
/* 250 */     return Utils.plugin().getServer().getPlayer(playerName);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   public static OfflinePlayer offline(@NotNull UUID uuid) {
/* 260 */     return Utils.plugin().getServer().getOfflinePlayer(uuid);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void chat(@NotNull Player player, @NotNull String text) {
/* 270 */     Messagers.chat(player).send(TextStyle.component(text));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void chat(@NotNull Player player, @NotNull Component text) {
/* 280 */     Messagers.chat(player).send(text);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   public static ChatMessager chatMessager(@NotNull Player player) {
/* 290 */     return Messagers.chat(player);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void actionBar(@NotNull Player player, @NotNull String text) {
/* 300 */     Messagers.actionBar(player).send(TextStyle.component(text));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void actionBar(@NotNull Player player, @NotNull Component text) {
/* 310 */     Messagers.actionBar(player).send(text);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   public static ActionBarMessager actionBarMessager(@NotNull Player player) {
/* 320 */     return Messagers.actionBar(player);
/*     */   }
/*     */ }


/* Location:              C:\Users\joeal\Downloads\Kits-2.0.9.jar!\dev\continuum\kits\lib\\utils\player\PlayerUtils.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */