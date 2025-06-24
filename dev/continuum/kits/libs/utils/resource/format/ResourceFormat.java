/*    */ package dev.continuum.kits.libs.utils.resource.format;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ResourceFormat
/*    */ {
/*    */   private String extension;
/*    */   
/*    */   public ResourceFormat(String extension) {
/* 15 */     this.extension = extension;
/*    */     
/* 17 */     if (!this.extension.contains(".")) {
/* 18 */       this.extension = "." + extension;
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ResourceFormat() {
/* 26 */     this.extension = ".yml";
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static ResourceFormat format(String extension) {
/* 36 */     return new ResourceFormat(extension);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String extension() {
/* 45 */     return this.extension;
/*    */   }
/*    */ }


/* Location:              C:\Users\joeal\Downloads\Kits-2.0.9.jar!\dev\continuum\kits\lib\\utils\resource\format\ResourceFormat.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */