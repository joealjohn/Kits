/*    */ package dev.continuum.kits.libs.utils.scheduler.stacker;
/*    */ 
/*    */ import dev.continuum.kits.libs.utils.scheduler.builder.SchedulerBuilder;
/*    */ import java.util.ArrayList;
/*    */ import java.util.Arrays;
/*    */ import java.util.List;
/*    */ import org.jetbrains.annotations.NotNull;
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
/*    */ public class SchedulerStacker
/*    */ {
/*    */   @NotNull
/* 22 */   private final List<SchedulerBuilder> builders = new ArrayList<>();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @NotNull
/*    */   public SchedulerStacker stack(@NotNull SchedulerBuilder builder) {
/* 32 */     this.builders.add(builder);
/* 33 */     return this;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @NotNull
/*    */   public SchedulerStacker stack(@NotNull List<SchedulerBuilder> builders) {
/* 43 */     this.builders.addAll(builders);
/* 44 */     return this;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @NotNull
/*    */   public SchedulerStacker stack(@NotNull SchedulerBuilder... builders) {
/* 54 */     this.builders.addAll(Arrays.asList(builders));
/* 55 */     return this;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void execute() {
/* 63 */     for (SchedulerBuilder builder : this.builders)
/* 64 */       builder.execute(); 
/*    */   }
/*    */ }


/* Location:              C:\Users\joeal\Downloads\Kits-2.0.9.jar!\dev\continuum\kits\lib\\utils\scheduler\stacker\SchedulerStacker.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */