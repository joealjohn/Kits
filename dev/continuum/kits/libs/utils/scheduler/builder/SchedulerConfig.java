/*     */ package dev.continuum.kits.libs.utils.scheduler.builder;
/*     */ 
/*     */ import org.jetbrains.annotations.NotNull;
/*     */ import org.jetbrains.annotations.Nullable;
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
/*     */ public class SchedulerConfig
/*     */ {
/*     */   @Nullable
/*  19 */   private Integer everyTicks = null; @Nullable
/*  20 */   private Integer afterTicks = null; @NotNull
/*  21 */   private SchedulerThreadType threadType = SchedulerThreadType.SYNC;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Integer everyTicks() {
/*  30 */     return this.everyTicks;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   public SchedulerConfig everyTicks(@Nullable Integer everyTicks) {
/*  40 */     this.everyTicks = everyTicks;
/*  41 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Integer afterTicks() {
/*  50 */     return this.afterTicks;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   public SchedulerConfig afterTicks(@Nullable Integer afterTicks) {
/*  60 */     this.afterTicks = afterTicks;
/*  61 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   public SchedulerConfig async() {
/*  70 */     this.threadType = SchedulerThreadType.ASYNC;
/*  71 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   public SchedulerConfig sync() {
/*  80 */     this.threadType = SchedulerThreadType.SYNC;
/*  81 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   public SchedulerThreadType threadType() {
/*  90 */     return this.threadType;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   public SchedulerConfig threadType(@NotNull SchedulerThreadType threadType) {
/* 100 */     this.threadType = threadType;
/* 101 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\Users\joeal\Downloads\Kits-2.0.9.jar!\dev\continuum\kits\lib\\utils\scheduler\builder\SchedulerConfig.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */