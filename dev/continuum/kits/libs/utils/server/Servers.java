/*     */ package dev.continuum.kits.libs.utils.server;
/*     */ 
/*     */ import dev.continuum.kits.libs.utils.library.Utils;
/*     */ import dev.continuum.kits.libs.utils.library.wrapper.PluginWrapper;
/*     */ import java.io.File;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.bukkit.Server;
/*     */ import org.bukkit.configuration.file.FileConfiguration;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.plugin.Plugin;
/*     */ import org.bukkit.plugin.PluginManager;
/*     */ import org.bukkit.plugin.ServicesManager;
/*     */ import org.bukkit.plugin.java.JavaPlugin;
/*     */ import org.bukkit.scheduler.BukkitScheduler;
/*     */ import org.bukkit.scoreboard.ScoreboardManager;
/*     */ import org.bukkit.structure.StructureManager;
/*     */ import org.jetbrains.annotations.NotNull;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Servers
/*     */ {
/*     */   @NotNull
/*     */   public static PluginManager handler() {
/*  31 */     return Utils.plugin().getServer().getPluginManager();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   public static StructureManager structures() {
/*  40 */     return Utils.plugin().getServer().getStructureManager();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   public static ScoreboardManager scoreboards() {
/*  49 */     return Utils.plugin().getServer().getScoreboardManager();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   public static BukkitScheduler scheduler() {
/*  58 */     return Utils.plugin().getServer().getScheduler();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   public static ServicesManager services() {
/*  67 */     return Utils.plugin().getServer().getServicesManager();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   public static List<Player> online() {
/*  76 */     return new ArrayList<>(Utils.plugin().getServer().getOnlinePlayers());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   public static List<String> onlineNames() {
/*  85 */     List<String> list = new ArrayList<>();
/*     */     
/*  87 */     for (Player player : online()) {
/*  88 */       String name = player.getName();
/*  89 */       list.add(name);
/*     */     } 
/*     */     
/*  92 */     return list;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int playerCount() {
/* 101 */     return online().size();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Plugin plugin(@NotNull String name) {
/* 111 */     return Utils.plugin().getServer().getPluginManager().getPlugin(name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Server server(@NotNull String name) {
/* 121 */     return plugin(name).getServer();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   public static Server server() {
/* 129 */     return Utils.plugin().getServer();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   public static File dataFolder() {
/* 138 */     return Utils.plugin().getDataFolder();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   public static File dataFolder(@NotNull JavaPlugin plugin) {
/* 148 */     return plugin.getDataFolder();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   public static File dataFolder(@NotNull Plugin plugin) {
/* 158 */     return plugin.getDataFolder();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   public static FileConfiguration config() {
/* 167 */     return Utils.plugin().getConfig();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <P extends JavaPlugin> P plugin(@NotNull Class<P> clazz) {
/* 177 */     return (P)JavaPlugin.getPlugin(clazz);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <P extends PluginWrapper> P wrapped(@NotNull Class<P> clazz) {
/* 187 */     return (P)plugin(clazz);
/*     */   }
/*     */ }


/* Location:              C:\Users\joeal\Downloads\Kits-2.0.9.jar!\dev\continuum\kits\lib\\utils\server\Servers.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */