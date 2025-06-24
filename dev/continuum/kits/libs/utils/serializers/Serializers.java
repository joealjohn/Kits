/*    */ package dev.continuum.kits.libs.utils.serializers;
/*    */ 
/*    */ import org.jetbrains.annotations.NotNull;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Serializers
/*    */ {
/*    */   @NotNull
/*    */   public static Base64Serializer base64() {
/* 16 */     return new Base64Serializer();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @NotNull
/*    */   public static ByteSerializer bytes() {
/* 26 */     return new ByteSerializer();
/*    */   }
/*    */ }


/* Location:              C:\Users\joeal\Downloads\Kits-2.0.9.jar!\dev\continuum\kits\lib\\utils\serializers\Serializers.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */