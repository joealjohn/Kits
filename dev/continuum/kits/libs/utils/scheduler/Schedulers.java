/*    */ package dev.continuum.kits.libs.utils.scheduler;
/*    */ 
/*    */ import dev.continuum.kits.libs.utils.scheduler.async.AsyncScheduler;
/*    */ import dev.continuum.kits.libs.utils.scheduler.builder.SchedulerBuilder;
/*    */ import dev.continuum.kits.libs.utils.scheduler.stacker.SchedulerStacker;
/*    */ import dev.continuum.kits.libs.utils.scheduler.sync.SyncScheduler;
/*    */ import java.util.function.Consumer;
/*    */ import org.bukkit.scheduler.BukkitTask;
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
/*    */ public class Schedulers
/*    */ {
/*    */   @NotNull
/*    */   public static SyncScheduler sync() {
/* 23 */     return new SyncScheduler();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @NotNull
/*    */   public static AsyncScheduler async() {
/* 32 */     return new AsyncScheduler();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @NotNull
/*    */   public static SyncScheduler synchronous() {
/* 41 */     return sync();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @NotNull
/*    */   public static AsyncScheduler asynchronous() {
/* 50 */     return async();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @NotNull
/*    */   public static SchedulerBuilder builder() {
/* 59 */     return new SchedulerBuilder();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @NotNull
/*    */   public static SchedulerBuilder builder(@Nullable Runnable runnable) {
/* 68 */     if (runnable != null) {
/* 69 */       return new SchedulerBuilder(runnable);
/*    */     }
/*    */     
/* 72 */     return new SchedulerBuilder();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @NotNull
/*    */   public static SchedulerBuilder builder(@Nullable Consumer<BukkitTask> task) {
/* 81 */     if (task != null) {
/* 82 */       return new SchedulerBuilder(task);
/*    */     }
/*    */     
/* 85 */     return new SchedulerBuilder();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @NotNull
/*    */   public static SchedulerStacker stacker() {
/* 94 */     return new SchedulerStacker();
/*    */   }
/*    */ }


/* Location:              C:\Users\joeal\Downloads\Kits-2.0.9.jar!\dev\continuum\kits\lib\\utils\scheduler\Schedulers.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */