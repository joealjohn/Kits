/*     */ package dev.continuum.kits.libs.utils.location;
/*     */ 
/*     */ import dev.continuum.kits.libs.utils.library.Utils;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import org.bukkit.Bukkit;
/*     */ import org.bukkit.Location;
/*     */ import org.bukkit.World;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.jetbrains.annotations.NotNull;
/*     */ import org.jetbrains.annotations.Nullable;
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
/*     */ public class LocationUtils
/*     */ {
/*     */   public static boolean isBetween(int a, int b, int numberToCheck) {
/*  28 */     return isBetween(a, b, numberToCheck, true);
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
/*     */   public static boolean isBetween(int a, int b, int numberToCheck, boolean inclusive) {
/*  41 */     if (inclusive) {
/*  42 */       return (numberToCheck >= Math.min(a, b) && numberToCheck <= Math.max(a, b));
/*     */     }
/*  44 */     return (numberToCheck > Math.min(a, b) && numberToCheck < Math.max(a, b));
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
/*     */   public static boolean locIsBetween(@NotNull Location a, @NotNull Location b, @NotNull Location locToCheck) {
/*  56 */     return (isBetween(a.getBlockX(), b.getBlockX(), locToCheck.getBlockX()) && 
/*  57 */       isBetween(a.getBlockY(), b.getBlockY(), locToCheck.getBlockY()) && 
/*  58 */       isBetween(a.getBlockZ(), b.getBlockZ(), locToCheck.getBlockZ()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   public static List<Player> playersInRadius(@NotNull Location center, double radius) {
/*  69 */     List<Player> nearbyPlayers = new ArrayList<>();
/*     */     
/*  71 */     for (Player player : Utils.plugin().getServer().getOnlinePlayers()) {
/*  72 */       Location playerLocation = player.getLocation();
/*  73 */       if (Objects.equals(center.getWorld(), playerLocation.getWorld()) && center
/*  74 */         .distance(playerLocation) <= radius) {
/*  75 */         nearbyPlayers.add(player);
/*     */       }
/*     */     } 
/*     */     
/*  79 */     return nearbyPlayers;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   public static List<Player> playersNearPlayer(@NotNull Player targetPlayer, double radius) {
/*  90 */     List<Player> nearbyPlayers = new ArrayList<>();
/*  91 */     Location targetLocation = targetPlayer.getLocation();
/*     */     
/*  93 */     for (Player player : Utils.plugin().getServer().getOnlinePlayers()) {
/*  94 */       if (!player.equals(targetPlayer)) {
/*  95 */         Location playerLocation = player.getLocation();
/*  96 */         if (Objects.equals(targetLocation.getWorld(), playerLocation.getWorld()) && targetLocation
/*  97 */           .distance(playerLocation) <= radius) {
/*  98 */           nearbyPlayers.add(player);
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 103 */     return nearbyPlayers;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static double distance(@NotNull Location loc1, @NotNull Location loc2) {
/* 114 */     return loc1.distance(loc2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   public static String serialize(@NotNull Location location) {
/* 124 */     return ((World)Objects.requireNonNull(location.getWorld())).getName() + "," + ((World)Objects.requireNonNull(location.getWorld())).getName() + "," + location
/* 125 */       .getX() + "," + location
/* 126 */       .getY() + "," + location
/* 127 */       .getZ() + "," + location
/* 128 */       .getPitch();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public static Location deserialize(@NotNull String serialized) {
/* 139 */     String[] parts = serialized.split(",");
/*     */     
/* 141 */     if (parts.length == 6) {
/* 142 */       World world = Bukkit.getWorld(parts[0]);
/* 143 */       double x = Double.parseDouble(parts[1]);
/* 144 */       double y = Double.parseDouble(parts[2]);
/* 145 */       double z = Double.parseDouble(parts[3]);
/* 146 */       float pitch = Float.parseFloat(parts[4]);
/* 147 */       float yaw = Float.parseFloat(parts[5]);
/* 148 */       return new Location(world, x, y, z, yaw, pitch);
/*     */     } 
/*     */     
/* 151 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\joeal\Downloads\Kits-2.0.9.jar!\dev\continuum\kits\lib\\utils\location\LocationUtils.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */