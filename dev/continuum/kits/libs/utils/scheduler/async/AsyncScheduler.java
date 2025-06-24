/*    */ package dev.continuum.kits.libs.utils.scheduler.async;
/*    */ 
/*    */ import dev.continuum.kits.libs.utils.library.Utils;
/*    */ import dev.continuum.kits.libs.utils.scheduler.SchedulerBase;
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import java.util.function.Consumer;
/*    */ import java.util.function.Supplier;
/*    */ import org.bukkit.plugin.Plugin;
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
/*    */ public class AsyncScheduler
/*    */   extends SchedulerBase
/*    */ {
/*    */   public void execute(@NotNull Consumer<BukkitTask> task) {
/* 23 */     scheduler().runTaskAsynchronously((Plugin)Utils.plugin(), task);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void execute(@NotNull Runnable runnable) {
/* 31 */     scheduler().runTaskAsynchronously((Plugin)Utils.plugin(), runnable);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void execute(@NotNull Consumer<BukkitTask> task, int afterTicks) {
/* 39 */     scheduler().runTaskLaterAsynchronously((Plugin)Utils.plugin(), task, afterTicks);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void execute(@NotNull Runnable runnable, int afterTicks) {
/* 47 */     scheduler().runTaskLaterAsynchronously((Plugin)Utils.plugin(), runnable, afterTicks);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void execute(@NotNull Consumer<BukkitTask> task, int afterTicks, int everyTicks) {
/* 55 */     scheduler().runTaskTimerAsynchronously((Plugin)Utils.plugin(), task, afterTicks, everyTicks);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void execute(@NotNull Runnable runnable, int afterTicks, int everyTicks) {
/* 63 */     scheduler().runTaskTimerAsynchronously((Plugin)Utils.plugin(), runnable, afterTicks, everyTicks);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public Object execute(@NotNull Supplier<?> supplier) {
/*    */     try {
/* 72 */       return CompletableFuture.supplyAsync(supplier).get();
/* 73 */     } catch (InterruptedException|java.util.concurrent.ExecutionException e) {
/* 74 */       throw new RuntimeException(e);
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public <T> T supply(@NotNull Supplier<T> supplier) {
/*    */     try {
/* 84 */       return CompletableFuture.<T>supplyAsync(supplier).get();
/* 85 */     } catch (InterruptedException|java.util.concurrent.ExecutionException e) {
/* 86 */       throw new RuntimeException(e);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\joeal\Downloads\Kits-2.0.9.jar!\dev\continuum\kits\lib\\utils\scheduler\async\AsyncScheduler.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */