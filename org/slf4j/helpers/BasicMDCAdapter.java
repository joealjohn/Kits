/*     */ package org.slf4j.helpers;
/*     */ 
/*     */ import java.util.Deque;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.slf4j.spi.MDCAdapter;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BasicMDCAdapter
/*     */   implements MDCAdapter
/*     */ {
/*  46 */   private final ThreadLocalMapOfStacks threadLocalMapOfDeques = new ThreadLocalMapOfStacks();
/*     */   
/*  48 */   private final InheritableThreadLocal<Map<String, String>> inheritableThreadLocalMap = new InheritableThreadLocal<Map<String, String>>()
/*     */     {
/*     */       protected Map<String, String> childValue(Map<String, String> parentValue) {
/*  51 */         if (parentValue == null) {
/*  52 */           return null;
/*     */         }
/*  54 */         return new HashMap<>(parentValue);
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void put(String key, String val) {
/*  71 */     if (key == null) {
/*  72 */       throw new IllegalArgumentException("key cannot be null");
/*     */     }
/*  74 */     Map<String, String> map = this.inheritableThreadLocalMap.get();
/*  75 */     if (map == null) {
/*  76 */       map = new HashMap<>();
/*  77 */       this.inheritableThreadLocalMap.set(map);
/*     */     } 
/*  79 */     map.put(key, val);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String get(String key) {
/*  86 */     Map<String, String> map = this.inheritableThreadLocalMap.get();
/*  87 */     if (map != null && key != null) {
/*  88 */       return map.get(key);
/*     */     }
/*  90 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void remove(String key) {
/*  98 */     Map<String, String> map = this.inheritableThreadLocalMap.get();
/*  99 */     if (map != null) {
/* 100 */       map.remove(key);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clear() {
/* 108 */     Map<String, String> map = this.inheritableThreadLocalMap.get();
/* 109 */     if (map != null) {
/* 110 */       map.clear();
/* 111 */       this.inheritableThreadLocalMap.remove();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<String> getKeys() {
/* 122 */     Map<String, String> map = this.inheritableThreadLocalMap.get();
/* 123 */     if (map != null) {
/* 124 */       return map.keySet();
/*     */     }
/* 126 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<String, String> getCopyOfContextMap() {
/* 136 */     Map<String, String> oldMap = this.inheritableThreadLocalMap.get();
/* 137 */     if (oldMap != null) {
/* 138 */       return new HashMap<>(oldMap);
/*     */     }
/* 140 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setContextMap(Map<String, String> contextMap) {
/* 145 */     Map<String, String> copy = null;
/* 146 */     if (contextMap != null) {
/* 147 */       copy = new HashMap<>(contextMap);
/*     */     }
/* 149 */     this.inheritableThreadLocalMap.set(copy);
/*     */   }
/*     */ 
/*     */   
/*     */   public void pushByKey(String key, String value) {
/* 154 */     this.threadLocalMapOfDeques.pushByKey(key, value);
/*     */   }
/*     */ 
/*     */   
/*     */   public String popByKey(String key) {
/* 159 */     return this.threadLocalMapOfDeques.popByKey(key);
/*     */   }
/*     */ 
/*     */   
/*     */   public Deque<String> getCopyOfDequeByKey(String key) {
/* 164 */     return this.threadLocalMapOfDeques.getCopyOfDequeByKey(key);
/*     */   }
/*     */   
/*     */   public void clearDequeByKey(String key) {
/* 168 */     this.threadLocalMapOfDeques.clearDequeByKey(key);
/*     */   }
/*     */ }


/* Location:              C:\Users\joeal\Downloads\Kits-2.0.9.jar!\org\slf4j\helpers\BasicMDCAdapter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */