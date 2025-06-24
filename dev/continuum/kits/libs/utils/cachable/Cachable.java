/*    */ package dev.continuum.kits.libs.utils.cachable;
/*    */ 
/*    */ import dev.continuum.kits.libs.utils.cachable.impl.CachableImpl;
/*    */ import dev.continuum.kits.libs.utils.consumers.PairConsumer;
/*    */ import dev.continuum.kits.libs.utils.model.Tuple;
/*    */ import java.util.Arrays;
/*    */ import java.util.Collection;
/*    */ import java.util.Iterator;
/*    */ import java.util.Map;
/*    */ import org.jetbrains.annotations.NotNull;
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
/*    */ 
/*    */ public interface Cachable<K, V>
/*    */   extends Iterable<Tuple<K, V>>
/*    */ {
/*    */   static <K, V> CachableImpl<K, V> of() {
/* 28 */     return new CachableImpl();
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
/*    */   static <K, V> CachableImpl<K, V> of(Collection<Tuple<K, V>> entries) {
/* 40 */     CachableImpl<K, V> cachable = new CachableImpl();
/* 41 */     cachable.cacheAll(entries);
/*    */     
/* 43 */     return cachable;
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
/*    */   static <K, V> CachableImpl<K, V> of(Map<K, V> entries) {
/* 55 */     CachableImpl<K, V> cachable = new CachableImpl();
/* 56 */     cachable.cacheAll(entries);
/*    */     
/* 58 */     return cachable;
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
/*    */   @SafeVarargs
/*    */   static <K, V> CachableImpl<K, V> of(Tuple<K, V>... entries) {
/* 71 */     return of(Arrays.asList(entries));
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
/*    */ 
/*    */ 
/*    */   
/*    */   static <K, V> CachableImpl<K, V> of(Class<K> keyType, Class<V> valType) {
/* 86 */     return new CachableImpl();
/*    */   }
/*    */   
/*    */   V val(K paramK);
/*    */   
/*    */   K key(V paramV);
/*    */   
/*    */   boolean hasKey(K paramK);
/*    */   
/*    */   boolean hasVal(V paramV);
/*    */   
/*    */   void cache(K paramK, V paramV);
/*    */   
/*    */   void del(K paramK, V paramV);
/*    */   
/*    */   void del(K paramK);
/*    */   
/*    */   void cacheAll(Collection<Tuple<K, V>> paramCollection);
/*    */   
/*    */   void cacheAll(Map<K, V> paramMap);
/*    */   
/*    */   void delAll(Collection<K> paramCollection);
/*    */   
/*    */   void forEach(PairConsumer<K, V> paramPairConsumer);
/*    */   
/*    */   int cached();
/*    */   
/*    */   void clear();
/*    */   
/*    */   CachableSnapshot<K, V> snapshot();
/*    */   
/*    */   @NotNull
/*    */   Iterator<Tuple<K, V>> iterator();
/*    */ }


/* Location:              C:\Users\joeal\Downloads\Kits-2.0.9.jar!\dev\continuum\kits\lib\\utils\cachable\Cachable.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */