/*    */ package dev.continuum.kits.libs.utils.resource.path;
/*    */ 
/*    */ import dev.continuum.kits.libs.utils.library.Utils;
/*    */ import org.bukkit.plugin.java.JavaPlugin;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ResourcePaths
/*    */ {
/*    */   public static ResourcePath plugin() {
/* 16 */     return ResourcePath.path(Utils.plugin().getDataFolder().getPath());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static ResourcePath plugin(JavaPlugin plugin) {
/* 26 */     return ResourcePath.path(plugin.getDataFolder().getPath());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static ResourcePath root() {
/* 35 */     return ResourcePath.path(Utils.plugin()
/* 36 */         .getDataFolder()
/* 37 */         .getPath()
/* 38 */         .replaceAll("plugins/" + Utils.plugin().getName() + "/", ""));
/*    */   }
/*    */ }


/* Location:              C:\Users\joeal\Downloads\Kits-2.0.9.jar!\dev\continuum\kits\lib\\utils\resource\path\ResourcePaths.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */