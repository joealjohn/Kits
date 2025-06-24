/*     */ package dev.continuum.kits.libs.hikari.metrics;
/*     */ 
/*     */ import dev.continuum.kits.libs.hikari.util.ClockSource;
/*     */ import java.util.concurrent.atomic.AtomicLong;
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
/*     */ public abstract class PoolStats
/*     */ {
/*     */   private final AtomicLong reloadAt;
/*     */   private final long timeoutMs;
/*     */   protected volatile int totalConnections;
/*     */   protected volatile int idleConnections;
/*     */   protected volatile int activeConnections;
/*     */   protected volatile int pendingThreads;
/*     */   protected volatile int maxConnections;
/*     */   protected volatile int minConnections;
/*     */   
/*     */   public PoolStats(long timeoutMs) {
/*  42 */     this.timeoutMs = timeoutMs;
/*  43 */     this.reloadAt = new AtomicLong();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getTotalConnections() {
/*  48 */     if (shouldLoad()) {
/*  49 */       update();
/*     */     }
/*     */     
/*  52 */     return this.totalConnections;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getIdleConnections() {
/*  57 */     if (shouldLoad()) {
/*  58 */       update();
/*     */     }
/*     */     
/*  61 */     return this.idleConnections;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getActiveConnections() {
/*  66 */     if (shouldLoad()) {
/*  67 */       update();
/*     */     }
/*     */     
/*  70 */     return this.activeConnections;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getPendingThreads() {
/*  75 */     if (shouldLoad()) {
/*  76 */       update();
/*     */     }
/*     */     
/*  79 */     return this.pendingThreads;
/*     */   }
/*     */   
/*     */   public int getMaxConnections() {
/*  83 */     if (shouldLoad()) {
/*  84 */       update();
/*     */     }
/*     */     
/*  87 */     return this.maxConnections;
/*     */   }
/*     */   
/*     */   public int getMinConnections() {
/*  91 */     if (shouldLoad()) {
/*  92 */       update();
/*     */     }
/*     */     
/*  95 */     return this.minConnections;
/*     */   }
/*     */ 
/*     */   
/*     */   protected abstract void update();
/*     */   
/*     */   private boolean shouldLoad() {
/*     */     while (true) {
/* 103 */       long now = ClockSource.currentTime();
/* 104 */       long reloadTime = this.reloadAt.get();
/* 105 */       if (reloadTime > now) {
/* 106 */         return false;
/*     */       }
/* 108 */       if (this.reloadAt.compareAndSet(reloadTime, ClockSource.plusMillis(now, this.timeoutMs)))
/* 109 */         return true; 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\joeal\Downloads\Kits-2.0.9.jar!\dev\continuum\kits\libs\hikari\metrics\PoolStats.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */