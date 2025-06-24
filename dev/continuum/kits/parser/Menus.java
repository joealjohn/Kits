/*    */ package dev.continuum.kits.parser;
/*    */ 
/*    */ import dev.continuum.kits.libs.utils.scheduler.Schedulers;
/*    */ import dev.continuum.kits.libs.utils.server.Servers;
/*    */ import java.io.File;
/*    */ import java.io.IOException;
/*    */ import org.bukkit.configuration.file.FileConfiguration;
/*    */ import org.bukkit.configuration.file.YamlConfiguration;
/*    */ import org.jetbrains.annotations.NotNull;
/*    */ 
/*    */ 
/*    */ public class Menus
/*    */ {
/*    */   @NotNull
/*    */   public static FileConfiguration of(@NotNull String name) {
/* 16 */     return (FileConfiguration)Schedulers.async().supply(() -> {
/*    */           File file = new File(directory(), "/" + name + ".yml");
/*    */           
/*    */           if (!file.exists()) {
/*    */             try {
/*    */               file.createNewFile();
/* 22 */             } catch (IOException e) {
/*    */               throw new RuntimeException(e);
/*    */             } 
/*    */           }
/*    */           return YamlConfiguration.loadConfiguration(file);
/*    */         });
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @NotNull
/*    */   public static File directory() {
/* 34 */     File directory = new File(Servers.dataFolder(), "/menus/");
/*    */     
/* 36 */     directory.mkdirs();
/* 37 */     return directory;
/*    */   }
/*    */ }


/* Location:              C:\Users\joeal\Downloads\Kits-2.0.9.jar!\dev\continuum\kits\parser\Menus.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */