/*    */ package dev.continuum.kits.api.event;
/*    */ 
/*    */ import org.bukkit.entity.Player;
/*    */ import org.jetbrains.annotations.NotNull;
/*    */ 
/*    */ public class AllowableEvent extends KitsEvent {
/*    */   private boolean allowed = true;
/*    */   
/*    */   public AllowableEvent(@NotNull Player player) {
/* 10 */     super(player);
/*    */   }
/*    */   
/*    */   public void allow() {
/* 14 */     this.allowed = true;
/*    */   }
/*    */   
/*    */   public void disallow() {
/* 18 */     this.allowed = false;
/*    */   }
/*    */   
/*    */   public void allowed(boolean allowed) {
/* 22 */     this.allowed = allowed;
/*    */   }
/*    */   
/*    */   public void allow(boolean allow) {
/* 26 */     allowed(allow);
/*    */   }
/*    */   
/*    */   public boolean allowed() {
/* 30 */     return this.allowed;
/*    */   }
/*    */ }


/* Location:              C:\Users\joeal\Downloads\Kits-2.0.9.jar!\dev\continuum\kits\api\event\AllowableEvent.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */