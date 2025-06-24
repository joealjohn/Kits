/*    */ package dev.continuum.kits.libs.utils.cachable.impl;
/*    */ 
/*    */ import dev.continuum.kits.libs.utils.cachable.CachableSnapshot;
/*    */ import dev.continuum.kits.libs.utils.model.Tuple;
/*    */ import java.lang.reflect.Array;
/*    */ import java.util.ArrayList;
/*    */ import java.util.Collection;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class CachableSnapshotImpl<K, V>
/*    */   implements CachableSnapshot<K, V>
/*    */ {
/*    */   private final CachableImpl<K, V> cachable;
/*    */   
/*    */   CachableSnapshotImpl(CachableImpl<K, V> cachable) {
/* 19 */     this.cachable = cachable;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Map<K, V> asMap() {
/* 27 */     return this.cachable.cache;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public List<Tuple<K, V>> asList() {
/* 35 */     List<Tuple<K, V>> list = new ArrayList<>();
/*    */     
/* 37 */     for (K key : this.cachable.cache.keySet()) {
/* 38 */       for (V val : this.cachable.cache.values()) {
/* 39 */         list.add(Tuple.tuple(key, val));
/*    */       }
/*    */     } 
/*    */     
/* 43 */     return list;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Collection<Tuple<K, V>> asCollection() {
/* 51 */     return asList();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Tuple<K, V>[] asTupleArray() {
/* 60 */     Tuple[] arrayOfTuple = (Tuple[])Array.newInstance(Tuple.class, asList().size());
/*    */     
/* 62 */     int i = 0;
/* 63 */     for (Tuple<K, V> tuple : asList()) {
/* 64 */       arrayOfTuple[i++] = tuple;
/*    */     }
/*    */     
/* 67 */     return (Tuple<K, V>[])arrayOfTuple;
/*    */   }
/*    */ }


/* Location:              C:\Users\joeal\Downloads\Kits-2.0.9.jar!\dev\continuum\kits\lib\\utils\cachable\impl\CachableSnapshotImpl.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */