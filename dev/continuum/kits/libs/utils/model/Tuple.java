/*    */ package dev.continuum.kits.libs.utils.model;
/*    */ 
/*    */ import org.jetbrains.annotations.NotNull;
/*    */ import org.jetbrains.annotations.Nullable;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Tuple<K, V>
/*    */ {
/*    */   private K key;
/*    */   private V val;
/*    */   
/*    */   public Tuple(@Nullable K key, @Nullable V val) {
/* 23 */     this.key = key;
/* 24 */     this.val = val;
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
/*    */   @NotNull
/*    */   public static <K, V> Tuple<K, V> tuple(@Nullable K key, @Nullable V val) {
/* 37 */     return new Tuple<>(key, val);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @NotNull
/*    */   public String toString() {
/* 47 */     return "[<key> : <val>]"
/* 48 */       .replaceAll("<key>", this.key.toString())
/* 49 */       .replaceAll("<val>", this.val.toString());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @NotNull
/*    */   public Tuple<K, V> key(@Nullable K key) {
/* 59 */     this.key = key;
/* 60 */     return this;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @NotNull
/*    */   public Tuple<K, V> val(@Nullable V val) {
/* 70 */     this.val = val;
/* 71 */     return this;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public K key() {
/* 80 */     return this.key;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public V val() {
/* 89 */     return this.val;
/*    */   }
/*    */ }


/* Location:              C:\Users\joeal\Downloads\Kits-2.0.9.jar!\dev\continuum\kits\lib\\utils\model\Tuple.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */