/*    */ package dev.continuum.kits.api.event;
/*    */ 
/*    */ import dev.continuum.kits.api.KitsAPI;
/*    */ import org.bukkit.entity.Player;
/*    */ import org.jetbrains.annotations.NotNull;
/*    */ 
/*    */ public abstract class KitsEvent {
/*    */   private final Player player;
/*    */   
/*    */   public KitsEvent(@NotNull Player player) {
/* 11 */     this.player = player;
/*    */   }
/*    */   
/*    */   @NotNull
/*    */   public Player player() {
/* 16 */     return this.player;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean fire() {
/* 21 */     return KitsAPI.api().listeners().fire(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\joeal\Downloads\Kits-2.0.9.jar!\dev\continuum\kits\api\event\KitsEvent.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */