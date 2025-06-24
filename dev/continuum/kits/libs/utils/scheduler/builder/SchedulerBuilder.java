/*     */ package dev.continuum.kits.libs.utils.scheduler.builder;
/*     */ 
/*     */ import dev.continuum.kits.libs.utils.scheduler.Schedulers;
/*     */ import dev.continuum.kits.libs.utils.scheduler.builder.task.SchedulerTask;
/*     */ import java.util.function.Consumer;
/*     */ import org.bukkit.scheduler.BukkitTask;
/*     */ import org.jetbrains.annotations.NotNull;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SchedulerBuilder
/*     */ {
/*     */   private SchedulerTask schedulerTask;
/*     */   @NotNull
/*     */   private final SchedulerConfig config;
/*     */   
/*     */   public SchedulerBuilder() {
/*  23 */     this.config = new SchedulerConfig();
/*  24 */     this.schedulerTask = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SchedulerBuilder(@NotNull Runnable runnable) {
/*  33 */     this.config = new SchedulerConfig();
/*  34 */     this.schedulerTask = SchedulerTask.wrap(runnable);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SchedulerBuilder(@NotNull Consumer<BukkitTask> task) {
/*  43 */     this.config = new SchedulerConfig();
/*  44 */     this.schedulerTask = SchedulerTask.wrap(task);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   public SchedulerBuilder config(@NotNull Consumer<SchedulerConfig> configConsumer) {
/*  54 */     configConsumer.accept(this.config);
/*  55 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   public SchedulerBuilder async() {
/*  64 */     this.config.async();
/*  65 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   public SchedulerBuilder sync() {
/*  74 */     this.config.sync();
/*  75 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   public SchedulerBuilder everyTicks(int everyTicks) {
/*  85 */     this.config.everyTicks(Integer.valueOf(everyTicks));
/*  86 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   public SchedulerBuilder afterTicks(int afterTicks) {
/*  96 */     this.config.afterTicks(Integer.valueOf(afterTicks));
/*  97 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   public SchedulerBuilder runnable(@NotNull Runnable runnable) {
/* 107 */     this.schedulerTask = SchedulerTask.wrap(runnable);
/* 108 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   public SchedulerBuilder task(@NotNull Consumer<BukkitTask> task) {
/* 118 */     this.schedulerTask = SchedulerTask.wrap(task);
/* 119 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void execute() {
/* 127 */     switch (this.config.threadType()) {
/*     */       
/*     */       case SYNC:
/* 130 */         if (this.config.everyTicks() == null && this.config.afterTicks() == null) {
/* 131 */           Object object = this.schedulerTask.appropriate(); if (object instanceof Consumer) { Consumer<?> consumer = (Consumer)object;
/* 132 */             Consumer<BukkitTask> taskConsumer = (Consumer)consumer;
/* 133 */             Schedulers.sync().execute(taskConsumer); break; }
/* 134 */            object = this.schedulerTask.appropriate(); if (object instanceof Runnable) { Runnable runnable = (Runnable)object;
/* 135 */             Schedulers.sync().execute(runnable); }
/*     */ 
/*     */ 
/*     */           
/*     */           break;
/*     */         } 
/*     */         
/* 142 */         if (this.config.everyTicks() == null && this.config.afterTicks() != null) {
/* 143 */           Object object = this.schedulerTask.appropriate(); if (object instanceof Consumer) { Consumer<?> consumer = (Consumer)object;
/* 144 */             Consumer<BukkitTask> taskConsumer = (Consumer)consumer;
/* 145 */             Schedulers.sync().execute(taskConsumer, this.config.afterTicks().intValue()); break; }
/* 146 */            object = this.schedulerTask.appropriate(); if (object instanceof Runnable) { Runnable runnable = (Runnable)object;
/* 147 */             Schedulers.sync().execute(runnable, this.config.afterTicks().intValue()); }
/*     */ 
/*     */ 
/*     */           
/*     */           break;
/*     */         } 
/*     */         
/* 154 */         if (this.config.everyTicks() != null && this.config.afterTicks() != null) {
/* 155 */           Object object = this.schedulerTask.appropriate(); if (object instanceof Consumer) { Consumer<?> consumer = (Consumer)object;
/* 156 */             Consumer<BukkitTask> taskConsumer = (Consumer)consumer;
/* 157 */             Schedulers.sync().execute(taskConsumer, this.config.afterTicks().intValue(), this.config.everyTicks().intValue()); break; }
/* 158 */            object = this.schedulerTask.appropriate(); if (object instanceof Runnable) { Runnable runnable = (Runnable)object;
/* 159 */             Schedulers.sync().execute(runnable, this.config.afterTicks().intValue(), this.config.everyTicks().intValue()); }
/*     */ 
/*     */           
/*     */           break;
/*     */         } 
/*     */         
/* 165 */         throw new UnsupportedOperationException("Not supported");
/*     */ 
/*     */       
/*     */       case ASYNC:
/* 169 */         if (this.config.everyTicks() == null && this.config.afterTicks() == null) {
/* 170 */           Object object = this.schedulerTask.appropriate(); if (object instanceof Consumer) { Consumer<?> consumer = (Consumer)object;
/* 171 */             Consumer<BukkitTask> taskConsumer = (Consumer)consumer;
/* 172 */             Schedulers.async().execute(taskConsumer); break; }
/* 173 */            object = this.schedulerTask.appropriate(); if (object instanceof Runnable) { Runnable runnable = (Runnable)object;
/* 174 */             Schedulers.async().execute(runnable); }
/*     */ 
/*     */ 
/*     */           
/*     */           break;
/*     */         } 
/*     */         
/* 181 */         if (this.config.everyTicks() == null && this.config.afterTicks() != null) {
/* 182 */           Object object = this.schedulerTask.appropriate(); if (object instanceof Consumer) { Consumer<?> consumer = (Consumer)object;
/* 183 */             Consumer<BukkitTask> taskConsumer = (Consumer)consumer;
/* 184 */             Schedulers.async().execute(taskConsumer, this.config.afterTicks().intValue()); break; }
/* 185 */            object = this.schedulerTask.appropriate(); if (object instanceof Runnable) { Runnable runnable = (Runnable)object;
/* 186 */             Schedulers.async().execute(runnable, this.config.afterTicks().intValue()); }
/*     */ 
/*     */ 
/*     */           
/*     */           break;
/*     */         } 
/*     */         
/* 193 */         if (this.config.everyTicks() != null && this.config.afterTicks() != null) {
/* 194 */           Object object = this.schedulerTask.appropriate(); if (object instanceof Consumer) { Consumer<?> consumer = (Consumer)object;
/* 195 */             Consumer<BukkitTask> taskConsumer = (Consumer)consumer;
/* 196 */             Schedulers.async().execute(taskConsumer, this.config.afterTicks().intValue(), this.config.everyTicks().intValue()); break; }
/* 197 */            object = this.schedulerTask.appropriate(); if (object instanceof Runnable) { Runnable runnable = (Runnable)object;
/* 198 */             Schedulers.async().execute(runnable, this.config.afterTicks().intValue(), this.config.everyTicks().intValue()); }
/*     */ 
/*     */           
/*     */           break;
/*     */         } 
/*     */         
/* 204 */         throw new UnsupportedOperationException("Not supported");
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\joeal\Downloads\Kits-2.0.9.jar!\dev\continuum\kits\lib\\utils\scheduler\builder\SchedulerBuilder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */