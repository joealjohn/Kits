/*    */ package dev.continuum.kits.api;
/*    */ 
/*    */ import dev.continuum.kits.api.listener.EventListeners;
/*    */ import org.jetbrains.annotations.NotNull;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class KitsAPI
/*    */ {
/*    */   @NotNull
/*    */   public static KitsAPI api() {
/* 13 */     return new KitsAPI();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @NotNull
/*    */   public EventListeners listeners() {
/* 22 */     return EventListeners.listeners();
/*    */   }
/*    */ }


/* Location:              C:\Users\joeal\Downloads\Kits-2.0.9.jar!\dev\continuum\kits\api\KitsAPI.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */