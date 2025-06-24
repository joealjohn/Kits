/*     */ package dev.continuum.kits.libs.utils.cachable.impl;
/*     */ 
/*     */ import dev.continuum.kits.libs.utils.cachable.Cachable;
/*     */ import dev.continuum.kits.libs.utils.cachable.CachableSnapshot;
/*     */ import dev.continuum.kits.libs.utils.consumers.PairConsumer;
/*     */ import dev.continuum.kits.libs.utils.model.Tuple;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import org.jetbrains.annotations.NotNull;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CachableImpl<K, V>
/*     */   implements Cachable<K, V>
/*     */ {
/*  24 */   final Map<K, V> cache = new ConcurrentHashMap<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public V val(K key) {
/*  31 */     return this.cache.get(key);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public K key(V val) {
/*  39 */     for (Map.Entry<K, V> entry : this.cache.entrySet()) {
/*  40 */       if (entry.getValue().equals(val)) {
/*  41 */         return entry.getKey();
/*     */       }
/*     */     } 
/*     */     
/*  45 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasKey(K key) {
/*  53 */     return this.cache.containsKey(key);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasVal(V val) {
/*  61 */     return this.cache.containsValue(val);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void cache(K key, V val) {
/*  69 */     this.cache.put(key, val);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void del(K key, V val) {
/*  77 */     if (this.cache.get(key).equals(val)) {
/*  78 */       this.cache.remove(key);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void del(K key) {
/*  87 */     this.cache.remove(key);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void cacheAll(Collection<Tuple<K, V>> entries) {
/*  95 */     for (Tuple<K, V> entry : entries) {
/*  96 */       cache((K)entry.key(), (V)entry.val());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void cacheAll(Map<K, V> entries) {
/* 105 */     for (Map.Entry<K, V> entry : entries.entrySet()) {
/* 106 */       K key = entry.getKey();
/* 107 */       V val = entry.getValue();
/*     */       
/* 109 */       cache(key, val);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void delAll(Collection<K> keys) {
/* 118 */     for (K key : keys) {
/* 119 */       this.cache.remove(key);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void forEach(PairConsumer<K, V> forEach) {
/* 128 */     Objects.requireNonNull(forEach); this.cache.forEach(forEach::execute);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int cached() {
/* 136 */     return this.cache.size();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clear() {
/* 144 */     this.cache.clear();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   public CachableSnapshot<K, V> snapshot() {
/* 152 */     return new CachableSnapshotImpl<>(this);
/*     */   }
/*     */   
/*     */   @NotNull
/*     */   public Iterator<Tuple<K, V>> iterator() {
/* 157 */     return snapshot().asList().iterator();
/*     */   }
/*     */ }


/* Location:              C:\Users\joeal\Downloads\Kits-2.0.9.jar!\dev\continuum\kits\lib\\utils\cachable\impl\CachableImpl.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */