/*    */ package dev.continuum.kits.libs.utils.scheduler;
/*    */ 
/*    */ import dev.continuum.kits.libs.utils.library.Utils;
/*    */ import java.util.function.Consumer;
/*    */ import java.util.function.Supplier;
/*    */ import org.bukkit.scheduler.BukkitScheduler;
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
/*    */ public abstract class SchedulerBase
/*    */ {
/*    */   public abstract void execute(@NotNull Consumer<BukkitTask> paramConsumer);
/*    */   
/*    */   public abstract void execute(@NotNull Runnable paramRunnable);
/*    */   
/*    */   public abstract void execute(@NotNull Consumer<BukkitTask> paramConsumer, int paramInt);
/*    */   
/*    */   public abstract void execute(@NotNull Runnable paramRunnable, int paramInt);
/*    */   
/*    */   public abstract void execute(@NotNull Consumer<BukkitTask> paramConsumer, int paramInt1, int paramInt2);
/*    */   
/*    */   public abstract void execute(@NotNull Runnable paramRunnable, int paramInt1, int paramInt2);
/*    */   
/*    */   @Nullable
/*    */   public abstract Object execute(@NotNull Supplier<?> paramSupplier);
/*    */   
/*    */   @Nullable
/*    */   public abstract <T> T supply(@NotNull Supplier<T> paramSupplier);
/*    */   
/*    */   @NotNull
/*    */   public final BukkitScheduler scheduler() {
/* 89 */     return Utils.plugin().getServer().getScheduler();
/*    */   }
/*    */ }


/* Location:              C:\Users\joeal\Downloads\Kits-2.0.9.jar!\dev\continuum\kits\lib\\utils\scheduler\SchedulerBase.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */