/*    */ package dev.continuum.kits.api.event;
/*    */ 
/*    */ import org.bukkit.entity.Player;
/*    */ import org.jetbrains.annotations.NotNull;
/*    */ 
/*    */ public abstract class CancellableEvent extends KitsEvent {
/*    */   private boolean cancelled = false;
/*    */   
/*    */   public CancellableEvent(@NotNull Player player) {
/* 10 */     super(player);
/*    */   }
/*    */   
/*    */   public void cancel() {
/* 14 */     this.cancelled = true;
/*    */   }
/*    */   
/*    */   public boolean cancelled() {
/* 18 */     return this.cancelled;
/*    */   }
/*    */   
/*    */   public void cancelled(boolean cancelled) {
/* 22 */     this.cancelled = cancelled;
/*    */   }
/*    */   
/*    */   public void cancel(boolean cancel) {
/* 26 */     cancelled(cancel);
/*    */   }
/*    */ }


/* Location:              C:\Users\joeal\Downloads\Kits-2.0.9.jar!\dev\continuum\kits\api\event\CancellableEvent.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */