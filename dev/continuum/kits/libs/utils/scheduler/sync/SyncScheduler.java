/*    */ package dev.continuum.kits.libs.utils.scheduler.sync;
/*    */ 
/*    */ import dev.continuum.kits.libs.utils.library.Utils;
/*    */ import dev.continuum.kits.libs.utils.scheduler.SchedulerBase;
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
/*    */ public class SyncScheduler
/*    */   extends SchedulerBase
/*    */ {
/*    */   public void execute(@NotNull Consumer<BukkitTask> task) {
/* 21 */     scheduler().runTask((Plugin)Utils.plugin(), task);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void execute(@NotNull Runnable runnable) {
/* 29 */     scheduler().runTask((Plugin)Utils.plugin(), runnable);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void execute(@NotNull Consumer<BukkitTask> task, int afterTicks) {
/* 37 */     scheduler().runTaskLater((Plugin)Utils.plugin(), task, afterTicks);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void execute(@NotNull Runnable runnable, int afterTicks) {
/* 45 */     scheduler().runTaskLater((Plugin)Utils.plugin(), runnable, afterTicks);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void execute(@NotNull Consumer<BukkitTask> task, int afterTicks, int everyTicks) {
/* 53 */     scheduler().runTaskTimer((Plugin)Utils.plugin(), task, afterTicks, everyTicks);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void execute(@NotNull Runnable runnable, int afterTicks, int everyTicks) {
/* 61 */     scheduler().runTaskTimer((Plugin)Utils.plugin(), runnable, afterTicks, everyTicks);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public Object execute(@NotNull Supplier<?> supplier) {
/* 69 */     return supplier.get();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public <T> T supply(@NotNull Supplier<T> supplier) {
/* 77 */     return supplier.get();
/*    */   }
/*    */ }


/* Location:              C:\Users\joeal\Downloads\Kits-2.0.9.jar!\dev\continuum\kits\lib\\utils\scheduler\sync\SyncScheduler.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */