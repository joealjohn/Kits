/*    */ package dev.continuum.kits.config;
/*    */ 
/*    */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*    */ import dev.continuum.kits.ContinuumKits;
/*    */ import dev.continuum.kits.libs.utils.server.Servers;
/*    */ import java.io.File;
/*    */ import java.io.IOException;
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import org.bukkit.configuration.file.FileConfiguration;
/*    */ import org.bukkit.configuration.file.YamlConfiguration;
/*    */ import org.jetbrains.annotations.NotNull;
/*    */ 
/*    */ public class Files
/*    */ {
/*    */   @NotNull
/*    */   @CanIgnoreReturnValue
/*    */   public static CompletableFuture<FileConfiguration> config(@NotNull File file) {
/* 18 */     return CompletableFuture.supplyAsync(() -> YamlConfiguration.loadConfiguration(file));
/*    */   }
/*    */   
/*    */   @NotNull
/*    */   @CanIgnoreReturnValue
/*    */   public static CompletableFuture<FileConfiguration> config(@NotNull CompletableFuture<File> cf) {
/* 24 */     return cf.thenApply(YamlConfiguration::loadConfiguration);
/*    */   }
/*    */   
/*    */   @CanIgnoreReturnValue
/*    */   public static CompletableFuture<Boolean> saveConfig(@NotNull File file, @NotNull FileConfiguration config) {
/* 29 */     return CompletableFuture.supplyAsync(() -> {
/*    */           try {
/*    */             config.save(file);
/*    */             return Boolean.valueOf(true);
/* 33 */           } catch (IOException ignored) {
/*    */             return Boolean.valueOf(false);
/*    */           } 
/*    */         });
/*    */   }
/*    */   
/*    */   @NotNull
/*    */   @CanIgnoreReturnValue
/*    */   public static CompletableFuture<File> create(@NotNull File file) {
/* 42 */     return CompletableFuture.supplyAsync(() -> {
/*    */           
/*    */           try {
/*    */             if (!file.exists() && !file.createNewFile()) {
/*    */               ((ContinuumKits)Servers.wrapped(ContinuumKits.class)).log(999, "Failed to create file '" + file.getPath() + "'.");
/*    */             }
/* 48 */           } catch (IOException e) {
/*    */             ((ContinuumKits)Servers.wrapped(ContinuumKits.class)).log(999, "Failed to create file '" + file.getPath() + "'.");
/*    */           } 
/*    */           return file;
/*    */         });
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @NotNull
/*    */   @CanIgnoreReturnValue
/*    */   public static CompletableFuture<File> mkdirs(@NotNull File directory) {
/* 61 */     return CompletableFuture.supplyAsync(() -> {
/*    */           if (!directory.exists() && !directory.mkdirs()) {
/*    */             ((ContinuumKits)Servers.wrapped(ContinuumKits.class)).log(999, "Failed to create directory '" + directory.getPath() + "'.");
/*    */           }
/*    */           return directory;
/*    */         });
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @NotNull
/*    */   @CanIgnoreReturnValue
/*    */   public static File file(@NotNull String... nodes) {
/* 74 */     String joined = String.join("/", (CharSequence[])nodes);
/* 75 */     return (nodes[nodes.length - 1].endsWith(".yml") || nodes[nodes.length - 1].endsWith(".schem")) ? new File(Servers.dataFolder(), "/" + joined) : (nodes[nodes.length - 1].endsWith("/") ? new File(Servers.dataFolder(), "/" + joined + "/") : new File(Servers.dataFolder(), "/" + joined));
/*    */   }
/*    */   
/*    */   @NotNull
/*    */   @CanIgnoreReturnValue
/*    */   public static CompletableFuture<Void> save(@NotNull String... nodes) {
/* 81 */     return CompletableFuture.runAsync(() -> {
/*    */           String joined = (nodes.length > 1) ? String.join("/", (CharSequence[])nodes) : nodes[0];
/*    */           File file = file(nodes);
/*    */           if (file.getPath().endsWith(".yml")) {
/*    */             if (!file.exists())
/*    */               ((ContinuumKits)Servers.wrapped(ContinuumKits.class)).saveResource(joined, false); 
/*    */           } else if (!file.exists()) {
/*    */             mkdirs(file);
/*    */           } 
/*    */         });
/*    */   }
/*    */ }


/* Location:              C:\Users\joeal\Downloads\Kits-2.0.9.jar!\dev\continuum\kits\config\Files.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */