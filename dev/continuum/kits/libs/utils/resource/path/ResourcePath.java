/*    */ package dev.continuum.kits.libs.utils.resource.path;
/*    */ 
/*    */ import dev.continuum.kits.libs.utils.library.Utils;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ResourcePath
/*    */ {
/*    */   private final String path;
/*    */   
/*    */   public ResourcePath(String path) {
/* 14 */     this.path = path;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ResourcePath() {
/* 21 */     this.path = Utils.plugin().getDataFolder().getPath();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static ResourcePath path(String path) {
/* 31 */     return new ResourcePath(path);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String path() {
/* 40 */     return this.path;
/*    */   }
/*    */ }


/* Location:              C:\Users\joeal\Downloads\Kits-2.0.9.jar!\dev\continuum\kits\lib\\utils\resource\path\ResourcePath.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */