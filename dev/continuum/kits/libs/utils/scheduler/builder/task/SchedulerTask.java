/*    */ package dev.continuum.kits.libs.utils.scheduler.builder.task;
/*    */ 
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
/*    */ public class SchedulerTask
/*    */ {
/*    */   @Nullable
/*    */   private final Runnable runnable;
/*    */   @Nullable
/*    */   private final Consumer<BukkitTask> task;
/*    */   
/*    */   public SchedulerTask() {
/* 21 */     this.runnable = null;
/* 22 */     this.task = null;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public SchedulerTask(@Nullable Runnable runnable) {
/* 31 */     this.runnable = runnable;
/* 32 */     this.task = null;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public SchedulerTask(@Nullable Consumer<BukkitTask> task) {
/* 41 */     this.task = task;
/* 42 */     this.runnable = null;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @NotNull
/*    */   public static SchedulerTask wrap(@Nullable Runnable runnable) {
/* 52 */     return new SchedulerTask(runnable);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @NotNull
/*    */   public static SchedulerTask wrap(@Nullable Consumer<BukkitTask> task) {
/* 62 */     return new SchedulerTask(task);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public Runnable runnable() {
/* 71 */     return this.runnable;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public Consumer<BukkitTask> task() {
/* 80 */     return this.task;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public Object appropriate() {
/* 89 */     if (this.task == null && this.runnable != null) {
/* 90 */       return this.runnable;
/*    */     }
/*    */     
/* 93 */     if (this.runnable == null && this.task != null) {
/* 94 */       return this.task;
/*    */     }
/*    */     
/* 97 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\joeal\Downloads\Kits-2.0.9.jar!\dev\continuum\kits\lib\\utils\scheduler\builder\task\SchedulerTask.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */