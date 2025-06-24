/*    */ package dev.continuum.kits.libs.utils.library;
/*    */ 
/*    */ import dev.continuum.kits.libs.utils.event.crystal.impl.SpigotAnchorEventListener;
/*    */ import dev.continuum.kits.libs.utils.event.crystal.impl.SpigotCrystalEventListener;
/*    */ import dev.continuum.kits.libs.utils.menu.listener.MenuListener;
/*    */ import dev.continuum.kits.libs.utils.misc.Versions;
/*    */ import dev.continuum.kits.libs.utils.registration.Registrar;
/*    */ import org.bukkit.event.Listener;
/*    */ import org.bukkit.plugin.java.JavaPlugin;
/*    */ import org.jetbrains.annotations.NotNull;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Utils
/*    */ {
/*    */   private static JavaPlugin plugin;
/*    */   
/*    */   private Utils(@NotNull JavaPlugin plugin) {
/* 23 */     Utils.plugin = plugin;
/*    */     
/* 25 */     if (Versions.isHigherThanOrEqualTo("1.20.1")) {
/*    */       try {
/* 27 */         Registrar.events((Listener)new SpigotAnchorEventListener());
/* 28 */       } catch (Exception exception) {}
/*    */     }
/*    */     
/* 31 */     Registrar.events((Listener)new SpigotCrystalEventListener());
/* 32 */     Registrar.events((Listener)new MenuListener());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @NotNull
/*    */   public static Utils init(@NotNull JavaPlugin plugin) {
/* 41 */     return new Utils(plugin);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @NotNull
/*    */   public static JavaPlugin plugin() {
/* 50 */     return plugin;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void plugin(@NotNull JavaPlugin plugin) {
/* 62 */     Utils.plugin = plugin;
/*    */   }
/*    */ }


/* Location:              C:\Users\joeal\Downloads\Kits-2.0.9.jar!\dev\continuum\kits\lib\\utils\library\Utils.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */